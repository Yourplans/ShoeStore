package com.shoestore.adapter;

import android.content.Context;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.shoestore.R;
import com.shoestore.objects.ProductosVo;

import java.util.ArrayList;

/**
 * Created by Andres Rangel on 23/12/2015.
 */
public class AdapterProductos extends RecyclerView.Adapter<AdapterProductos.AdapterViewHolder> {

    ArrayList<ProductosVo> items;
    Context context;
    public static String categoria;


    public AdapterProductos(ArrayList<ProductosVo> items, Context context) {
        this.items = items;
        this.context = context;
    }

    /**
    metodo que se implementa al extender del recycler view y es donde inflamos nuestro layout adapter con los datos de
    la clase que se esta instanciando y que posteriomente hemos creado y casteado los datos
     */
    @Override
    public AdapterProductos.AdapterViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate( R.layout.adapter_productos,parent,false);
        AdapterViewHolder adapterViewHolder=new AdapterViewHolder(view);
        return adapterViewHolder;
    }

    /**
    metodo que se implementa al extender del recycler view y es donde asiganmos a nuestro arreglo los datos
    correspondientes de cada componente
     */

    @Override
    public void onBindViewHolder(final AdapterProductos.AdapterViewHolder holder, int position) {

        holder.glide.with(context).load(items.get(position).getImage()).override(800,400).fitCenter()
                .into(holder.imagenProducto);
        holder.nombre.setText(items.get(position).getName());
        holder.precio.setText("$"+items.get(position).getPrice());
        holder.precioPromocion.setText("$"+items.get(position).getPromotion());
        if (items.get(position).getPromotion()!=0){
            holder.precio.setPaintFlags(holder.precio.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        }else {
           holder.precioPromocion.setText("");
        }
        holder.ratingBar.setRating(items.get(position).getRating());


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
        ImageView imagenProducto;
        TextView nombre,precio,precioPromocion;
        RatingBar ratingBar;
        public AdapterViewHolder(View view) {
            super(view);
            imagenProducto = (ImageView) view.findViewById(R.id.imgProductos);
            nombre= (TextView) view.findViewById(R.id.txtNombreProducto);
            precio= (TextView) view.findViewById(R.id.txtPrecioGeneral);
            precioPromocion= (TextView) view.findViewById(R.id.txtPrecioPromocion);
            ratingBar= (RatingBar) view.findViewById(R.id.ratingProducto);

            // asignamos la tipografia roboto de material design a nuestros componentes
            Typeface typeface= Typeface.createFromAsset(context.getAssets(),"font/Roboto-Regular.ttf");
            nombre.setTypeface(typeface);
            precio.setTypeface(typeface);
            precioPromocion.setTypeface(typeface);
            view.setOnClickListener(this);


        }


        @Override
        public void onClick(View view) {
            ProductosVo productosVo=items.get(getAdapterPosition());
            Toast.makeText(context,""+productosVo.getName(),Toast.LENGTH_SHORT).show();


        }
    }
}
