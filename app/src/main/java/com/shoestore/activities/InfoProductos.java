package com.shoestore.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.Window;
import android.widget.RatingBar;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.shoestore.R;
import com.shoestore.adapter.DetailsPagerAdapter;
import com.shoestore.adapter.DividerItemDecoration;
import com.shoestore.adapter.OpinionesAdapterRecycler;
import com.shoestore.dialogs.DialogCarrito;
import com.shoestore.dialogs.DialogOpiniones;
import com.shoestore.objects.FirebaseReference;
import com.shoestore.objects.ImagesDetails;
import com.shoestore.objects.ProductosVo;
import com.shoestore.objects.ReviewsVo;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

public class InfoProductos extends AppCompatActivity {

    ViewPager viewPager;
    DetailsPagerAdapter detailsPagerAdapter;
    ArrayList<ImagesDetails> arrayList;
    private Timer timer;
    private TimerTask timerTask;
    private int contador;
    ProgressDialog progressDialog;
    private String referencia;
    private String name;
    private String image;
    private String desc;
    private int price;
    private int promotion;
    private float rating;
    private String colors,size;
    TextView txtName,txtDesc,txtPrice,txtPricePromotion,txtCalificanos,txtOpiniones,txtColores,txtTallas;
    TextView tituColores,tituTallas;
    Toolbar toolbar;
    public static RatingBar ratingBar;
    public static float ratinValue;
    public static String key_product;
    ArrayList<ReviewsVo> arrayListOpiniones;
    RecyclerView listaOpiniones;
    CardView layout_variacion_talla;
    CardView layout_variacion_colores;
    RecyclerView.Adapter adapterOpiniones;
    RecyclerView.LayoutManager layoutManagerOpiniones;
    private Typeface typeface;
    private ProductosVo productosVo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info_productos);
        recuperarInfo();
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(name);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        layout_variacion_talla = (CardView) findViewById(R.id.layou_variacion_talla);
        layout_variacion_colores = (CardView) findViewById(R.id.layou_variacion_colores);
        txtName= (TextView) findViewById(R.id.txtName);
        txtDesc= (TextView) findViewById(R.id.txtDesc);
        tituColores= (TextView) findViewById(R.id.tituColores);
        tituTallas= (TextView) findViewById(R.id.tituTallas);
        txtPrice= (TextView) findViewById(R.id.txtPrice);
        txtColores= (TextView) findViewById(R.id.txtColores);
        txtTallas= (TextView) findViewById(R.id.txtTallas);
        txtCalificanos= (TextView) findViewById(R.id.textCalificanos);
        txtOpiniones= (TextView) findViewById(R.id.textOpiniones);
        txtPricePromotion= (TextView) findViewById(R.id.txtPricePromotion);
        arrayListOpiniones = new ArrayList<>();
        listaOpiniones = (RecyclerView) findViewById(R.id.listaOpiniones);
        adapterOpiniones = new OpinionesAdapterRecycler(getApplicationContext(), arrayListOpiniones);
        layoutManagerOpiniones = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, true);
        listaOpiniones.setLayoutManager(layoutManagerOpiniones);
        listaOpiniones.setNestedScrollingEnabled(false);
        listaOpiniones.setHasFixedSize(false);
        listaOpiniones.addItemDecoration(new DividerItemDecoration(getApplicationContext(), R.drawable.divider));
        listaOpiniones.setAdapter(adapterOpiniones);
        consultaOpiniones();
        typeface= Typeface.createFromAsset(getAssets(),"font/Roboto-Regular.ttf");

        ratingBar = (RatingBar) findViewById(R.id.ratingInfo);
        ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                DialogOpiniones dialogOpiniones = new DialogOpiniones(InfoProductos.this);
                dialogOpiniones.requestWindowFeature(Window.FEATURE_NO_TITLE);
                ratinValue = ratingBar.getRating();
                dialogOpiniones.setCancelable(false);
                dialogOpiniones.show();

            }
        });
        progressDialog=new ProgressDialog(getApplicationContext());
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setMessage("Cargando informacion...");
        arrayList=new ArrayList<>();
        viewPager = (ViewPager) findViewById(R.id.viewPager);
        detailsPagerAdapter = new DetailsPagerAdapter(this,arrayList);
        final FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogCarrito dialogCarrito=new DialogCarrito(InfoProductos.this,productosVo);
                dialogCarrito.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialogCarrito.setCancelable(false);
                dialogCarrito.show();
            }
        });

        FirebaseDatabase database=FirebaseDatabase.getInstance();
        database.getReference(referencia+"/image_details/").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                arrayList.removeAll(arrayList);
                for (DataSnapshot snapshot:
                        dataSnapshot.getChildren()) {

                    ImagesDetails imagesDetails=snapshot.getValue(ImagesDetails.class);
                    imagesDetails.setKey(snapshot.getKey());
                    arrayList.add(imagesDetails);
                    viewPager.setAdapter(detailsPagerAdapter);
                    progressDialog.cancel();
                }
                detailsPagerAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                progressDialog.cancel();
            }
        } );
        timerAnimacion();
    }

    private void consultaOpiniones() {
        FirebaseDatabase database=FirebaseDatabase.getInstance();
        database.getReference(FirebaseReference.REVIEWS_REFERENCE+"/"+key_product+"/").addValueEventListener( new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                arrayListOpiniones.removeAll(arrayListOpiniones);
                for (DataSnapshot snapshot:
                        dataSnapshot.getChildren()) {

                    ReviewsVo reviewsVo=snapshot.getValue(ReviewsVo.class);
                    arrayListOpiniones.add(reviewsVo);
                    listaOpiniones.setAdapter(adapterOpiniones);
                    progressDialog.cancel();
                }
                adapterOpiniones.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                progressDialog.cancel();
            }
        } );

    }

    private void recuperarInfo() {

        Intent intent=getIntent();
        Bundle extras =intent.getExtras();
        if (extras != null) {//ver si contiene datos
            referencia=(String)extras.get("referencia");
            key_product=(String)extras.get("key_product");
            image= (String) extras.get("image");
   /*         name= (String) extras.get("name");
            desc= (String) extras.get("desc");
            price= (int) extras.get("price");
            promotion= (int) extras.get("promotion");
            rating= (float) extras.get("rating");*/
        }
        Log.i("Referencia**",referencia);
        FirebaseDatabase database=FirebaseDatabase.getInstance();
        database.getReference(referencia+"/").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                    productosVo=dataSnapshot.getValue(ProductosVo.class);
                    name=productosVo.getName();
                    desc=productosVo.getDesc();
                    price=productosVo.getPrice();
                    promotion=productosVo.getPromotion();
                    rating=productosVo.getRating();
                    colors=productosVo.getColors();
                    size=productosVo.getSize();
                    progressDialog.cancel();
                txtName.setText(name);
                txtDesc.setText(desc);
                txtTallas.setText(size);
                txtColores.setText(colors);
                txtPrice.setText("Precio: $"+price);
                txtPricePromotion.setText("Ahora: $"+promotion);
                tituTallas.setText("Tallas");
                tituColores.setText("Colores");
                if (promotion!=0){
                    txtPrice.setText("Antes: $"+price);
                    txtPrice.setPaintFlags(txtPrice.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                    txtPrice.setTextColor(Color.parseColor("#FFA9A9A9"));
                    txtPricePromotion.setTextColor(Color.parseColor("#FFFF0000"));
                    txtPricePromotion.setVisibility(View.VISIBLE);
                    txtPrice.setPadding(0,0,0,0);
                }else {
                    txtPrice.setTextSize(TypedValue.COMPLEX_UNIT_SP,20);
                    txtPricePromotion.setText("");
                    txtPrice.setPadding(0,10,0,20);
                    txtPrice.setTextColor(Color.parseColor("#ff000000"));
                    txtPricePromotion.setVisibility(View.GONE);
                }
                if (size==null || size.equals("")) {
                    layout_variacion_talla.setVisibility(View.GONE);
                }else {
                    layout_variacion_talla.setVisibility(View.VISIBLE);
                }
                if (colors==null || colors.equals("")) {
                    layout_variacion_colores.setVisibility(View.GONE);
                }else {
                    layout_variacion_colores.setVisibility(View.VISIBLE);
                }

                txtName.setTypeface(typeface);
                tituColores.setTypeface(typeface);
                tituTallas.setTypeface(typeface);
                txtDesc.setTypeface(typeface);
                txtPrice.setTypeface(typeface);
                txtCalificanos.setTypeface(typeface);
                txtOpiniones.setTypeface(typeface);
                txtPricePromotion.setTypeface(typeface);
                }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                progressDialog.cancel();
            }
        } );
    }

    private int getItem(int i) {
        return viewPager.getCurrentItem() + i;
    }

    private void timerAnimacion() {
        timer = new Timer();
        timerTask = new TimerTask() {
            @Override
            public void run() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (arrayList != null) {
                            viewPager.setCurrentItem(getItem(+1));
                            contador++;
                            if (contador ==arrayList.size()) {
                                contador = 0;
                                viewPager.setCurrentItem(0);
                            }
                        }
                    }
                });
            }
        };
        timer.schedule(timerTask, 4000, 4000);

    }
}
