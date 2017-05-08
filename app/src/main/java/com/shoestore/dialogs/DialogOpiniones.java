package com.shoestore.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.shoestore.R;
import com.shoestore.activities.InfoProductos;
import com.shoestore.activities.MainActivity;
import com.shoestore.objects.FirebaseReference;
import com.shoestore.objects.ReviewsVo;
import com.shoestore.objects.UsuariosVo;


import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Andres Rangel on 24/01/2016.
 */
public class DialogOpiniones extends Dialog {

    RatingBar ratinBar;
    EditText titulo,opinion;
    Button cancelar,calificar;
    private TextView textTitulo;
    public float calificacion;

    private DatabaseReference reference_product;
    private DatabaseReference reference_user;


    public DialogOpiniones(Context context) {
        super(context);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_calificaciones);
        ratinBar = (RatingBar) findViewById(R.id.ratingCalificacion);
        titulo= (EditText) findViewById(R.id.editTitulo);
        opinion= (EditText) findViewById(R.id.editOpinion);
        cancelar= (Button) findViewById(R.id.cancelar);
        calificar= (Button) findViewById(R.id.calificar);
        textTitulo= (TextView) findViewById(R.id.textTitulo);
        Typeface typeface= Typeface.createFromAsset(getContext().getAssets(),"font/Roboto-Regular.ttf");
        textTitulo.setTypeface(typeface);
        recuperarInfo();
        cancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        ratinBar.setRating(InfoProductos.ratinValue);
        calificacion=InfoProductos.ratinValue;
        FirebaseDatabase database=FirebaseDatabase.getInstance();
       reference_product=database.getReference( FirebaseReference.REVIEWS_REFERENCE);
       reference_user=database.getReference( FirebaseReference.REVIEWS_REFERENCE+"/"+InfoProductos.key_product+"/");
        ratinBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                dismiss();
                InfoProductos.ratingBar.setRating(ratingBar.getRating());

            }
        });
        calificar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!titulo.getText().toString().equals("") && !opinion.getText().toString().equals("")) {
                    llenarDatos();
                    dismiss();
                }


            }
        });

    }

    private void recuperarInfo() {

        FirebaseDatabase database=FirebaseDatabase.getInstance();
        database.getReference(FirebaseReference.REVIEWS_REFERENCE+"/"+InfoProductos.key_product+"/"+MainActivity.id_user+"/").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                ReviewsVo reviewsVo=dataSnapshot.getValue(ReviewsVo.class);
                if (reviewsVo!=null) {
                    titulo.setText(reviewsVo.getTitle());
                    opinion.setText(reviewsVo.getReview());
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        } );

    }

    private void llenarDatos() {
        ReviewsVo reviewsVo=new ReviewsVo();
        reviewsVo.setId_user(MainActivity.id_user);
        reviewsVo.setName(MainActivity.nameUsuario);
        reviewsVo.setPhoto(MainActivity.imgUsuario);
        reviewsVo.setTitle(titulo.getText().toString());
        reviewsVo.setReview(opinion.getText().toString());
        reviewsVo.setRating(ratinBar.getRating());
        guardarOpinion(reviewsVo);

    }

    private void guardarOpinion(ReviewsVo reviewsVo) {

        reference_product.child(InfoProductos.key_product);
        reference_user.child(reviewsVo.getId_user()).setValue(reviewsVo);

    }


}
