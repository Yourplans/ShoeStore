package com.shoestore.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.shoestore.R;
import com.shoestore.activities.ActivityProductos;
import com.shoestore.objects.CategoriasVo;

import java.util.ArrayList;

/**
 * Created by Andres Rangel on 23/12/2015.
 */
public class AdapterCategorias extends RecyclerView.Adapter<AdapterCategorias.AdapterViewHolder> {

    ArrayList<CategoriasVo> items;
    Context context;
    public static String categoria;


    public AdapterCategorias(ArrayList<CategoriasVo> items, Context context) {
        this.items = items;
        this.context = context;
    }

    /**
    metodo que se implementa al extender del recycler view y es donde inflamos nuestro layout adapter con los datos de
    la clase que se esta instanciando y que posteriomente hemos creado y casteado los datos
     */
    @Override
    public AdapterCategorias.AdapterViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate( R.layout.adapter_categorias,parent,false);
        AdapterViewHolder adapterViewHolder=new AdapterViewHolder(view);
        return adapterViewHolder;
    }

    /**
    metodo que se implementa al extender del recycler view y es donde asiganmos a nuestro arreglo los datos
    correspondientes de cada componente
     */

    @Override
    public void onBindViewHolder(final AdapterCategorias.AdapterViewHolder holder, int position) {

        holder.glide.with(context).load(items.get(position).getImage()).override(800,400).fitCenter()
                .into(holder.imagenCategoria);
        items.get(position).getName();

    }


    /**
    metodo que obtiene y retorna el arreglo con su tamaño
     */
    @Override
    public int getItemCount() {
        return items.size();
    }

    /**
     *clase que se crea para hacer el casteo de los componentes que utilizaremos en el adaptador
     */
    public class AdapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        Glide glide;
        ImageView imagenCategoria;
        TextView nombre;
        public AdapterViewHolder(View view) {
            super(view);
            imagenCategoria = (ImageView) view.findViewById(R.id.imagenCategoria);
            // asignamos la tipografia roboto de material design a nuestros componentes
            Typeface typeface= Typeface.createFromAsset(context.getAssets(),"font/Roboto-Regular.ttf");
            view.setOnClickListener(this);


        }


        @Override
        public void onClick(View view) {
            CategoriasVo categoriasVo=items.get(getAdapterPosition());
            Toast.makeText(context,""+categoriasVo.getName(),Toast.LENGTH_SHORT).show();
            Intent intent=new Intent(view.getContext(),ActivityProductos.class);
            categoria=categoriasVo.getName();
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            view.getContext().startActivity(intent);

        }
    }
}
