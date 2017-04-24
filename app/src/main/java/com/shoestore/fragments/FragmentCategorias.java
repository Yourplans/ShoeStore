package com.shoestore.fragments;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.shoestore.R;
import com.shoestore.adapter.AdapterCategorias;
import com.shoestore.objects.CategoriasVo;
import com.shoestore.objects.FirebaseReference;

import java.util.ArrayList;

/**
 * Created by Rangel on 02/04/2017.
 */

public class FragmentCategorias extends Fragment {


    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    RecyclerView.Adapter adapter;
    ArrayList<CategoriasVo> arrayList;
    ProgressDialog progressDialog;

    public FragmentCategorias() {
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view=inflater.inflate(R.layout.fragment_categorias,container,false);
        progressDialog=new ProgressDialog(getContext());
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setMessage("Cargando informacion...");
        progressDialog.show();
        recyclerView= (RecyclerView) view.findViewById(R.id.reciclerCategorias);
        arrayList=new ArrayList<>();
        layoutManager=new LinearLayoutManager(getActivity(),LinearLayoutManager.VERTICAL,false);
        adapter=new AdapterCategorias(arrayList,getActivity());
        recyclerView.setLayoutManager(layoutManager);
        FirebaseDatabase database=FirebaseDatabase.getInstance();
        database.getReference(FirebaseReference.CATEGORY_REFERENCE).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                arrayList.removeAll(arrayList);
                for (DataSnapshot snapshot:
                        dataSnapshot.getChildren()) {

                    CategoriasVo categoriasVo=snapshot.getValue(CategoriasVo.class);
                    arrayList.add(categoriasVo);
                    recyclerView.setAdapter( adapter );
                    progressDialog.cancel();
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        } );
        return view;
    }
}
