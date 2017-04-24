package com.shoestore.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.shoestore.R;
import com.shoestore.adapter.AdapterFavoritos;
import com.shoestore.adapter.AdapterProductos;
import com.shoestore.objects.CategoriasVo;
import com.shoestore.objects.ProductosVo;

import java.util.ArrayList;

/**
 * Created by Rangel on 02/04/2017.
 */

public class FragmentFavoritos extends Fragment {

    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    RecyclerView.Adapter adapter;
    ArrayList<ProductosVo> arrayList;
    Fragment fragment;
    public FragmentFavoritos() {
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view=inflater.inflate(R.layout.fragment_favoritos,container,false);

        recyclerView= (RecyclerView) view.findViewById(R.id.reciclerFavoritos);
        arrayList=new ArrayList<>();
        layoutManager=new LinearLayoutManager(getActivity(),LinearLayoutManager.VERTICAL,false);
        adapter=new AdapterFavoritos(arrayList,getContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
        fragment=new FragmentFavoritoVacio();
        if (fragment!=null && arrayList.size()==0) {
            FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.frameFavorito,fragment);
            ft.commit();
        }
        return view;
    }
}
