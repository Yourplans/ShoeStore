package com.shoestore.adapter;

import android.content.Context;
import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.shoestore.R;
import com.shoestore.objects.ProductosVo;

import java.util.ArrayList;

/**
 * Created by Rangel on 14/04/2017.
 */

public class AdapterFavoritos extends RecyclerView.Adapter<AdapterFavoritos.AdapterViewHolder> {

    ArrayList<ProductosVo> arrayList;
    Context context;

    public AdapterFavoritos(ArrayList<ProductosVo> arrayList, Context context) {
        this.arrayList = arrayList;
        this.context = context;
    }

    @Override
    public AdapterFavoritos.AdapterViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.adapter_favoritos,parent,false);
        AdapterViewHolder adapterViewHolder=new AdapterViewHolder(view);
        return adapterViewHolder;
    }

    @Override
    public void onBindViewHolder(AdapterFavoritos.AdapterViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class AdapterViewHolder extends RecyclerView.ViewHolder {



        public AdapterViewHolder(View itemView) {
            super(itemView);


            Typeface typeface= Typeface.createFromAsset(context.getAssets(),"font/Roboto-Regular.ttf");
        }
    }
}
