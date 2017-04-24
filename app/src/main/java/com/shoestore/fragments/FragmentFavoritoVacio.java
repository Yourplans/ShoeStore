package com.shoestore.fragments;

import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.shoestore.R;

/**
 * Created by Rangel on 14/04/2017.
 */

public class FragmentFavoritoVacio extends Fragment {

    TextView textView;
    public FragmentFavoritoVacio() {
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.favoritos_vacio,container,false);
        textView= (TextView) view.findViewById(R.id.txtListaVacia);
        Typeface typeface= Typeface.createFromAsset(getContext().getAssets(),"font/Roboto-Regular.ttf");
        textView.setTypeface(typeface);
        return view;
    }
}
