package com.shoestore.adapter;

import android.content.Context;
import android.graphics.Typeface;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.shoestore.R;
import com.shoestore.fragments.FragmenProductos;
import com.shoestore.objects.FirebaseReference;

/**
 * Created by Andres Rangel on 04/11/2015.
 */
public class CustomPagerAdapter extends FragmentPagerAdapter {

    private Context context;
    String titulos[]={"Sport","Formal","Sandalias"};


    public CustomPagerAdapter(Context applicationContext, FragmentManager supportFragmentManager) {
        super(supportFragmentManager);
        this.context=applicationContext;
    }

    public View getTabView(int position){
        View view= LayoutInflater.from(context).inflate(R.layout.custom_tabs,null);
        TextView textView= (TextView) view.findViewById(R.id.textTabs);
        Typeface typeface= Typeface.createFromAsset(context.getAssets(),"font/Roboto-Regular.ttf");
        textView.setText(titulos[position]);
        textView.setTypeface(typeface);

        return view;
    }

    @Override
    public Fragment getItem(int position) {

        String sports="";
        String formal="";
        String sandalias="";

        if (AdapterCategorias.categoria.equals("damas")){

            sports= FirebaseReference.DAMAS_SPORTS_REFERENCE;
            formal=FirebaseReference.DAMAS_FORMAL_REFERENCE;
            sandalias=FirebaseReference.DAMAS_SANDALIAS_REFERENCE;
        }else {
            sports= FirebaseReference.CABALLEROS_SPORTS_REFERENCE;
            formal=FirebaseReference.CABALLEROS_FORMAL_REFERENCE;
            sandalias=FirebaseReference.CABALLEROS_SANDALIAS_REFERENCE;

        }
        switch (position){
            case 0:
                return new FragmenProductos(sports);
            case 1:
                return new FragmenProductos(formal);
            case 2:
                return new FragmenProductos(sandalias);
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return titulos.length;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return titulos[position];
    }
}
