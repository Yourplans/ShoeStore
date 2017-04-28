package com.shoestore.login;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;

import com.google.firebase.database.FirebaseDatabase;
import com.shoestore.R;

public class Login extends AppCompatActivity {

    public static Activity finalizar;
    private boolean isInitialized=false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        persistence();
        requestWindowFeature( Window.FEATURE_NO_TITLE);
        getWindow().setFlags( WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_login);


        finalizar=this;
    }

    /**
     * Valida y activa la persistencia de Firebase
     */
    private void persistence() {
        try{
            if(!isInitialized){
                FirebaseDatabase.getInstance().setPersistenceEnabled(true);
                isInitialized = true;
            }else {
                Log.d("this Main","Already Initialized");
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

}