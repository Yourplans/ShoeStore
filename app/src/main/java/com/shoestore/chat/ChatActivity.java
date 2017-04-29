package com.shoestore.chat;

import android.content.BroadcastReceiver;
import android.content.IntentFilter;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.shoestore.R;
import com.shoestore.chat.Notifications.Config;
import com.shoestore.chat.Notifications.NotificationUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ChatActivity extends AppCompatActivity {

    //Se instancia la base de datos y se obtiene la referencia
    FirebaseDatabase mFirebaseDatabase = FirebaseDatabase.getInstance();
    DatabaseReference mDatabaseReference = mFirebaseDatabase.getReference();

    //Se instancia components
    EditText mEditText;
    FloatingActionButton mButton;
    CustomChatAdapter mCustomChatAdapter;
    ArrayList<ChatMessages> mArrayList = new ArrayList<>();
    ListView mListView;

    //Referenciamos los componentes para recibir la notificacion
    private static final String TAG = ChatActivity.class.getSimpleName();
    private BroadcastReceiver mReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        broadCast();


        Toast.makeText(getApplicationContext(),UserDetails.tokenUser,Toast.LENGTH_SHORT).show();
        mDatabaseReference.keepSynced(true);

        mListView = (ListView) findViewById(R.id.list_messages);
        mCustomChatAdapter = new CustomChatAdapter(getApplicationContext(),R.layout.item_message_s,mArrayList);

        mButton = (FloatingActionButton) findViewById(R.id.fabSend);
        mEditText = (EditText) findViewById(R.id.message);


        onClick();

        setTitle(UserDetails.userChat);

        messageEvents();

    }

    private void messageEvents() {

        mDatabaseReference.child("messages").child(UserDetails.user+"_"+UserDetails.userChat).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {



                GenericTypeIndicator<Map<String, String>> genericTypeIndicator = new GenericTypeIndicator<Map<String, String>>() {};

                Map map = dataSnapshot.getValue(genericTypeIndicator);
                String message = map.get("message").toString();
                String userName = map.get("user").toString();

                if (UserDetails.index!=55555555) {
                    Map<String, Object> update = new HashMap<String, Object>();
                    update.put("message", message);
                    update.put("user", userName);
                    update.put("visible", "true");
                    Log.e("", "si estoy actualizando");
                    mDatabaseReference.child("messages").child(UserDetails.user + "_" + UserDetails.userChat).child(dataSnapshot.getKey()).setValue(update);
                }
                if (userName.equals(UserDetails.user)){
                    addMessage("You: \n"+message,true);
                }else{
                    addMessage(UserDetails.userChat+"\n"+message,false);
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

    private void addMessage(String message, boolean type) {

        ChatMessages mChatMessages = new ChatMessages();
        mChatMessages.setMessage(message);
        mChatMessages.setType(type);
        mArrayList.add(mChatMessages);
        mListView.setAdapter(mCustomChatAdapter);
        mCustomChatAdapter.notifyDataSetChanged();
    }

    private void broadCast() {

    }

    private void onClick (){

    }

    /**
     *Send Notification
     */
    private void sendNotification(final String user, final String message) {

        StringRequest request = new StringRequest(Request.Method.POST, ResourceChat.UrlNotifications, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Toast.makeText(getApplicationContext(),response,Toast.LENGTH_LONG).show();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }) {

            protected Map<String, String> getParams() throws AuthFailureError {

                Map<String,String> parameters = new HashMap<String, String>();
                parameters.put("title", user);
                parameters.put("msg",message);
                parameters.put("img",UserDetails.imageUserChat);
                parameters.put("token",UserDetails.tokenUserChat);
                return parameters;
            }

        };

        RequestQueue mRequestQueue = Volley.newRequestQueue(ChatActivity.this);
        mRequestQueue.add(request);

    }

    @Override
    protected void onResume() {
        super.onResume();

        LocalBroadcastManager.getInstance(this).registerReceiver(mReceiver, new IntentFilter(Config.PUSH_NOTIFICATION));
        NotificationUtils.clearNotifications(getApplicationContext());

    }

}
