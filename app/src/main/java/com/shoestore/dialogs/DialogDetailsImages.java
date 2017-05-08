package com.shoestore.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.shoestore.R;
import com.shoestore.adapter.DetailsPagerAdapter;

import uk.co.senab.photoview.PhotoView;
import uk.co.senab.photoview.PhotoViewAttacher;

/**
 * Created by Rangel on 06/05/2017.
 */

public class DialogDetailsImages extends Dialog {

    FloatingActionButton fbCancel;
    String image;
    ImageView imagen;
    PhotoViewAttacher photoViewAttacher;

    public DialogDetailsImages(Context context) {
        super(context);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature( Window.FEATURE_NO_TITLE);
        getWindow().setFlags( WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.dialog_details_image);
        fbCancel= (FloatingActionButton) findViewById(R.id.fabClose);
        image= DetailsPagerAdapter.imagen;
        imagen= (ImageView) findViewById(R.id.imageDetails);
        Glide.with(getContext()).load(image).crossFade()
                .diskCacheStrategy(DiskCacheStrategy.ALL).placeholder(R.drawable.cargando).
                into(imagen);
        photoViewAttacher=new PhotoViewAttacher(imagen);
        photoViewAttacher.update();
        fbCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               cancel();
            }
        });
    }

}
