package com.shoestore.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.shoestore.R;
import com.shoestore.objects.ProductosVo;

/**
 * Created by Rangel on 29/05/2017.
 */

public class DialogCarrito extends Dialog {

    Button cancelar,comprar;
    Spinner spTallas,spColores;
    TextInputLayout textInputLayout;
    EditText editCantidad;
    TextView titulo;
    ProductosVo productosVo;
    ImageView img;
    TextView txtPrecioUnitario,txtValorUnitario,txtPrecioTotal,txtValorTotal;
    private Typeface typeface;
    private int valorProducto=0;

    public DialogCarrito(Context context, ProductosVo productosVo) {
        super(context);
        this.productosVo=productosVo;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_carrito);
        typeface= Typeface.createFromAsset(getContext().getAssets(),"font/Roboto-Regular.ttf");
        cancelar= (Button) findViewById(R.id.btnCancelar);
        comprar= (Button) findViewById(R.id.btnComprar);
        txtPrecioUnitario= (TextView) findViewById(R.id.txtPrecioUnitario);
        txtValorUnitario= (TextView) findViewById(R.id.txtValorUnitario);
        txtPrecioTotal= (TextView) findViewById(R.id.txtPrecioTotal);
        txtValorTotal= (TextView) findViewById(R.id.txtValorTotal);
        txtPrecioUnitario.setTypeface(typeface);
        txtValorUnitario.setTypeface(typeface);
        txtPrecioTotal.setTypeface(typeface);
        txtValorTotal.setTypeface(typeface);
        if (productosVo.getPromotion()==0){
           valorProducto=productosVo.getPrice();
        }else {
          valorProducto=productosVo.getPromotion();
        }
        txtValorUnitario.setText("$"+valorProducto);
        txtPrecioTotal.setText("Precio total x1: ");
        txtValorTotal.setText("$" + valorProducto);
        img= (ImageView) findViewById(R.id.imgProduct);
        spColores= (Spinner) findViewById(R.id.spColor);
        spTallas= (Spinner) findViewById(R.id.spTalla);
        textInputLayout= (TextInputLayout) findViewById(R.id.input_layout_cantidad);
        editCantidad= (EditText) findViewById(R.id.input_cantidad);
        editCantidad.setText("1");
        titulo= (TextView) findViewById(R.id.nameProduct);
        titulo.setTypeface(typeface);
        editCantidad.setTypeface(typeface);
        cancelar.setTypeface(typeface);
        comprar.setTypeface(typeface);
        String size="Seleccione Una Talla,"+productosVo.getSize();
        String colors="Seleccione Un Color,"+productosVo.getColors();
        String [] tallas=size.split(",");
        ArrayAdapter<String>adapterTallas=new ArrayAdapter<String>(getContext(),android.R.layout.simple_spinner_item,tallas);
        spTallas.setAdapter(adapterTallas);
        String [] colores=colors.split(",");
        ArrayAdapter<String>adapterColores=new ArrayAdapter<String>(getContext(),android.R.layout.simple_spinner_item,colores);
        spColores.setAdapter(adapterColores);
        titulo.setText(productosVo.getName());
        Glide.with(getContext()).load(productosVo.getImage()).into(img);
        validarCantidad();
        cancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cancel();
            }
        });
    }

    private void validarCantidad() {
        editCantidad.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
             int cantidad=0;

             if (!editable.toString().equals("")) {
                 cantidad= Integer.parseInt(editable.toString());
                 txtPrecioTotal.setText("Precio total x"+cantidad+": ");
                 txtValorTotal.setText("$" + valorProducto * cantidad);
             }else {
                 txtPrecioTotal.setText("Precio total x1: ");
                 txtValorTotal.setText("$" + valorProducto);
             }
            }
        });
    }
}
