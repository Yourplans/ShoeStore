package com.shoestore.fragments;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.shoestore.R;
import com.shoestore.adapter.AdapterCategorias;
import com.shoestore.adapter.AdapterProductos;
import com.shoestore.objects.CategoriasVo;
import com.shoestore.objects.ProductosVo;

import java.util.ArrayList;

/**
 * Created by Rangel on 08/04/2017.
 */

@SuppressLint("ValidFragment")
public class FragmenProductos extends Fragment {

    private final String categoria;
    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    RecyclerView.Adapter adapter;
    ArrayList<ProductosVo> arrayList;
    ProgressDialog progressDialog;

    public FragmenProductos(String categoria) {
        this.categoria=categoria;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view=inflater.inflate(R.layout.fragment_productos,container,false);
        progressDialog=new ProgressDialog(getContext());
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setMessage("Cargando informacion...");
        recyclerView= (RecyclerView) view.findViewById(R.id.reciclerProductos);
        arrayList=new ArrayList<>();
        layoutManager=new LinearLayoutManager(getActivity(),LinearLayoutManager.VERTICAL,false);
        adapter=new AdapterProductos(arrayList,getActivity(),categoria);
        recyclerView.setLayoutManager(layoutManager);
        FirebaseDatabase database=FirebaseDatabase.getInstance();
        database.getReference(categoria).addValueEventListener( new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                arrayList.removeAll(arrayList);
                for (DataSnapshot snapshot:
                        dataSnapshot.getChildren()) {

                    ProductosVo productosVo=snapshot.getValue(ProductosVo.class);
                    productosVo.setKey(snapshot.getKey());
                    arrayList.add(productosVo);
                    recyclerView.setAdapter( adapter );
                    progressDialog.cancel();
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
               progressDialog.cancel();
            }
        } );

        return view;
    }


    @Override
    public void onDetach() {
        super.onDetach();
        progressDialog.cancel();
    }
}
