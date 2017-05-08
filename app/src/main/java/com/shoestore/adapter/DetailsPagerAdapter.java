package com.shoestore.adapter;

import android.app.Activity;
import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.shoestore.R;
import com.shoestore.dialogs.DialogDetailsImages;
import com.shoestore.objects.ImagesDetails;

import java.util.ArrayList;


/**
 * Created by Rangel on 03/06/2016.
 */
public class DetailsPagerAdapter extends PagerAdapter {

    ArrayList<ImagesDetails> arrayList;
    Activity context;
    LayoutInflater layoutInflater;
    public static String imagen;

    public DetailsPagerAdapter(Context context, ArrayList<ImagesDetails> arrayList) {
        this.context = (Activity) context;
        this.arrayList=arrayList;
    }

    @Override
    public int getCount() {
        return arrayList.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return (view==object);
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        layoutInflater=(LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view=(LayoutInflater.from(context)).inflate(R.layout.details_imagen,container,false);
        ImageView imageView= (ImageView) view.findViewById(R.id.imageInformacion);
        Glide.with(context).load(arrayList.get(position).getImage()).crossFade()
                .diskCacheStrategy(DiskCacheStrategy.ALL).placeholder(R.drawable.cargando)
                .into(imageView);
        final ImagesDetails imagesDetails=arrayList.get(position);

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imagen=imagesDetails.getImage();
                DialogDetailsImages dialogDetailsImages =new DialogDetailsImages(context);
                dialogDetailsImages.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialogDetailsImages.setCancelable(false);
                dialogDetailsImages.show();
            }
        });
        container.addView(view);
        return view;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((LinearLayout)object);
    }
}
