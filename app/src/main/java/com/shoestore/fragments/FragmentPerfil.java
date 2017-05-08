package com.shoestore.fragments;

import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.shoestore.R;
import com.shoestore.objects.FirebaseReference;
import com.shoestore.objects.UsuariosVo;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Rangel on 02/04/2017.
 */

public class FragmentPerfil extends Fragment implements View.OnClickListener{


    CircleImageView imgPerfil;
    TextInputLayout txtNombrePerfil,txtEmailPerfil,txtTelefonoPerfil,txtDireccionPerfil,txtCiudadPerfil;
    EditText editNombrePerfil,editEmailPerfil,editTelefonoPerfil,editDireccionPerfil,editCiudadPerfil;
    FloatingActionButton btnPerfil;
    ImageView btnEditPerfil;
    public FragmentPerfil() {
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view=inflater.inflate(R.layout.fragment_perfil,container,false);
        Typeface typeface= Typeface.createFromAsset(getContext().getAssets(),"font/Roboto-Regular.ttf");
        imgPerfil= (CircleImageView) view.findViewById(R.id.imgPerfil);
        btnPerfil= (FloatingActionButton) view.findViewById(R.id.fabPerfil);
        btnEditPerfil= (ImageView) view.findViewById(R.id.btnEditPerfil);
        txtNombrePerfil= (TextInputLayout) view.findViewById(R.id.input_layout_name);
        editNombrePerfil= (EditText) view.findViewById(R.id.input_name);
        txtEmailPerfil= (TextInputLayout) view.findViewById(R.id.input_layout_email);
        editEmailPerfil= (EditText) view.findViewById(R.id.input_email);
        txtTelefonoPerfil= (TextInputLayout) view.findViewById(R.id.input_layout_phone);
        editTelefonoPerfil= (EditText) view.findViewById(R.id.input_phone);
        txtDireccionPerfil= (TextInputLayout) view.findViewById(R.id.input_layout_addres);
        editDireccionPerfil= (EditText) view.findViewById(R.id.input_addres);
        txtCiudadPerfil= (TextInputLayout) view.findViewById(R.id.input_layout_city);
        editCiudadPerfil= (EditText) view.findViewById(R.id.input_city);
        txtNombrePerfil.setTypeface(typeface);
        editNombrePerfil.setTypeface(typeface);
        txtEmailPerfil.setTypeface(typeface);
        editEmailPerfil.setTypeface(typeface);
        txtTelefonoPerfil.setTypeface(typeface);
        editTelefonoPerfil.setTypeface(typeface);
        txtDireccionPerfil.setTypeface(typeface);
        editDireccionPerfil.setTypeface(typeface);
        txtCiudadPerfil.setTypeface(typeface);
        editCiudadPerfil.setTypeface(typeface);
        editNombrePerfil.setEnabled(false);
        editEmailPerfil.setEnabled(false);
        editTelefonoPerfil.setEnabled(false);
        editDireccionPerfil.setEnabled(false);
        editCiudadPerfil.setEnabled(false);

        FirebaseDatabase database=FirebaseDatabase.getInstance();
        database.getReference(FirebaseReference.CODIGO_USUARIO_REFERENCE).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                UsuariosVo usuariosVo=dataSnapshot.getValue(UsuariosVo.class);
                Glide.with(getContext()).load(usuariosVo.getPhoto()).into(imgPerfil);
                editNombrePerfil.setText(usuariosVo.getName());
                editEmailPerfil.setText(usuariosVo.getEmail());
                editTelefonoPerfil.setText(usuariosVo.getPhone());
                editDireccionPerfil.setText(usuariosVo.getAddress());
                editCiudadPerfil.setText(usuariosVo.getCity());

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        } );
        return view;
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()){

            case R.id.fabPerfil:

                break;
        }
    }
}
