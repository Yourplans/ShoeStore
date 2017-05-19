package com.shoestore.chat;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;
import com.shoestore.R;
import com.shoestore.chat.adapter.MyAdapter;
import com.shoestore.chat.model.ContactModel;

import java.util.ArrayList;
import java.util.Map;

public class ChatMain extends AppCompatActivity {

    //Instance Firebase Database
    FirebaseDatabase mFirebaseDatabase;
    DatabaseReference mDatabaseReference;

    //Declare Components of Recycler View
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private  RecyclerView.LayoutManager mLayoutManager;
    private ArrayList<ContactModel> contactModels;
    private LinearLayoutManager linearLayoutManager;
    boolean isNew=true;
    static int index;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_main);

        contactModels = new ArrayList<>();
        mFirebaseDatabase  = FirebaseDatabase.getInstance();//Obtiene la instancia de la base de datos
        mDatabaseReference= mFirebaseDatabase.getReference();//Obtiene las referencia

        llenarArray();

        setTitle("Chat de Ayuda");//Envia el titulo de la ventana

        // Instancia los componentes del RecyclerView
        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerContacts);

        linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);

        mRecyclerView.setLayoutManager(linearLayoutManager);

        mAdapter = new MyAdapter(contactModels,R.layout.cardview_contact,this);

        mRecyclerView.setAdapter(mAdapter);

    }

    /**
     * Llena el arreglo para luego implementarlo en el adaptador
     */
    private void llenarArray() {
        //Obtenemos los cambios que se realicen en la base de datos
        mDatabaseReference.child("users_soporte").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                contactModels.clear();

                for (DataSnapshot postSnapshot: dataSnapshot.getChildren()) {

                    ContactModel contactModel = new ContactModel();
                    contactModel.setName(postSnapshot.child("name").getValue().toString());
                    contactModel.setImage(postSnapshot.child("image").getValue().toString());
                    contactModel.setDesc(postSnapshot.child("desc").getValue().toString());
                    contactModel.setId(postSnapshot.child("id").getValue().toString());
                    contactModel.setNew(true);

                    contactModels.add(contactModel);
                }

                // mAdapter.notifyDataSetChanged();
                mensajesNuevos();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    /**
     * Identifica  cuando hay mensajes nuevo sin leer
     */
    private void mensajesNuevos() {

        Log.e("]]]]]",contactModels.size()+""+ChatMain.class.getName());
        for ( int i =0; i < contactModels.size();i++) {


            mDatabaseReference.child("messages").child(UserDetails.user + "_" +contactModels.get(i).getName()).addChildEventListener(new ChildEventListener() {
                @Override
                public void onChildAdded(DataSnapshot dataSnapshot, String s) {

                    Log.e("****++++", dataSnapshot.getValue().toString()+"    ");
                    if (dataSnapshot.getValue()!=null) {

                        GenericTypeIndicator<Map<String, String>> genericTypeIndicator = new GenericTypeIndicator<Map<String, String>>() {};

                        Map mMap = dataSnapshot.getValue(genericTypeIndicator);
                        if (mMap.get("visible") != null) {
                            if (mMap.get("visible").equals("false")) {
                                for (int i =0;i<contactModels.size();i++) {
                                    if (contactModels.get(i).getName().equals(mMap.get("user"))) {
                                        isNew = false;
                                        contactModels.get(i).setNew(isNew);
                                        mRecyclerView.clearOnChildAttachStateChangeListeners();
                                        mRecyclerView.setAdapter(mAdapter);
                                        mAdapter.notifyDataSetChanged();
                                        Log.e("****++++", dataSnapshot.getValue().toString());
                                        break;
                                    }
                                }

                            } else {
                                for (int i =0;i<contactModels.size();i++) {
                                    if (contactModels.get(i).getName().equals(mMap.get("user"))) {
                                        isNew = true;
                                        contactModels.get(i).setNew(isNew);
                                        mRecyclerView.clearOnChildAttachStateChangeListeners();
                                        mRecyclerView.setAdapter(mAdapter);
                                        mAdapter.notifyDataSetChanged();
                                        break;
                                    }
                                }
                            }

                        }else{
                            for (int i =0;i<contactModels.size();i++) {
                                if (contactModels.get(i).getName().equals(mMap.get("user"))) {
                                    isNew = true;
                                    contactModels.get(index).setNew(isNew);
                                    break;
                                }
                            }
                        }
                    }

                }

                @Override
                public void onChildChanged(DataSnapshot dataSnapshot, String s) {

                }

                @Override
                public void onChildRemoved(DataSnapshot dataSnapshot) {

                }

                @Override
                public void onChildMoved(DataSnapshot dataSnapshot, String s) {

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });

        }

    }

    /**
     * On Resume
     */
    @Override
    protected void onResume() {
        super.onResume();

        if (UserDetails.index!=55555555) {
            contactModels.get(UserDetails.index).setNew(true);
            mRecyclerView.clearOnChildAttachStateChangeListeners();
            mRecyclerView.setAdapter(mAdapter);
            mAdapter.notifyDataSetChanged();
            UserDetails.index=55555555;
        }
    }
}
