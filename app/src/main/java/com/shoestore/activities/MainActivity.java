package com.shoestore.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.facebook.login.LoginManager;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;
import com.shoestore.R;
import com.shoestore.adapter.ViewPagerAdapter;
import com.shoestore.chat.ChatMain;
import com.shoestore.fragments.FragmentCategorias;
import com.shoestore.fragments.FragmentFavoritos;
import com.shoestore.fragments.FragmentHome;
import com.shoestore.fragments.FragmentPerfil;
import com.shoestore.login.Login;
import com.shoestore.objects.FirebaseReference;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener {

    ProgressDialog progressDialog;
    private GoogleApiClient googleApiClient;
    private FirebaseAuth.AuthStateListener firebaseAuthListener;
    BottomNavigationView bottomNavigationView;
    //This is our viewPager
    private ViewPager viewPager;
    FragmentHome fragmentHome;
    FragmentCategorias fragmentCategorias;
    FragmentFavoritos fragmentFavoritos;
    FragmentPerfil fragmentPerfil;
    MenuItem prevMenuItem;
    private boolean isInitialized =  false;// validador para persistencia


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Shoe Store");
        setSupportActionBar(toolbar);

        persistence();

        //Initializing viewPager
        viewPager = (ViewPager) findViewById(R.id.viewpager);
        bottomNavigationView = (BottomNavigationView)findViewById(R.id.bottom_navigation);
        consultarCodigoUsuario();
        cargarViewPager();
        progressDialog=new ProgressDialog(MainActivity.this);
        progressDialog.setMessage( "Cerrando Session" );
        progressDialog.setProgressStyle( progressDialog.STYLE_SPINNER );
        /**
         Instancias para detectar la autenticacion con google y cerrar session
         */
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder( GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        googleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();

        firebaseAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user == null) {
                    logOut();
                }
            }
        };
    }

    private void cargarViewPager() {

        bottomNavigationView.setOnNavigationItemSelectedListener(
                new BottomNavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.home:
                                viewPager.setCurrentItem(0);
                                break;
                            case R.id.categorias:
                                viewPager.setCurrentItem(1);
                                break;
                            case R.id.favoritos:
                                viewPager.setCurrentItem(2);
                                break;
                            case R.id.perfil:
                                viewPager.setCurrentItem(3);
                                break;
                        }
                        return false;
                    }
                });

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if (prevMenuItem != null) {
                    prevMenuItem.setChecked(false);
                }
                else
                {
                    bottomNavigationView.getMenu().getItem(0).setChecked(false);
                }
                Log.d("page", "onPageSelected: "+position);
                bottomNavigationView.getMenu().getItem(position).setChecked(true);
                prevMenuItem = bottomNavigationView.getMenu().getItem(position);

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

       /*  //Disable ViewPager Swipe
       viewPager.setOnTouchListener(new View.OnTouchListener()
        {
            @Override
            public boolean onTouch(View v, MotionEvent event)
            {
                return true;
            }
        });
        */

        setupViewPager(viewPager);
    }


    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        fragmentHome=new FragmentHome();
        fragmentCategorias=new FragmentCategorias();
        fragmentFavoritos=new FragmentFavoritos();
        fragmentPerfil=new FragmentPerfil();
        adapter.addFragment(fragmentHome);
        adapter.addFragment(fragmentCategorias);
        adapter.addFragment(fragmentFavoritos);
        adapter.addFragment(fragmentPerfil);
        viewPager.setAdapter(adapter);
    }

    /**
     * metodo para cerrar la session de firebase,google y facebook
     *
     */
    private void cerrarSession() {
        FirebaseAuth.getInstance().signOut();
        LoginManager.getInstance().logOut();
        Auth.GoogleSignInApi.signOut(googleApiClient).setResultCallback( new ResultCallback<Status>() {
            @Override
            public void onResult(@NonNull Status status) {
                if (status.isSuccess()) {
                    logOut();
                } else {
                    Toast.makeText(getApplicationContext(),"Fallo al cerrar session", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    /**
     * metodo que cree para finalizar todas las actividades cuando se presione el boton salir
     * ya que por algun motivo me estaba llamando dos veces al main activity
     * y me toco finalizar con el finishAffinity() que cierra cualquier actividad
     * y agregar una condicion ya que solo funciona en la api 16 en adelante
     */
    public void onBackPressed(){
        super.onBackPressed();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            finishAffinity();

        }else {
            finish();
        }
    }

    /**
     * Con este metodo se llama de nuevo a la actividad login cuando se presione cerrar Session
     */

    public void logOut(){
        Intent intent=new Intent( getApplicationContext(), Login.class );
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity( intent );
        progressDialog.dismiss();
        finish();
    }

    /**
     * metodo obligatorio para poder identificar la autenticacion con google
     * @param connectionResult
     */
    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.cerrar_sesion) {
            progressDialog.show();
            Timer timer=new Timer();
            TimerTask timerTask=new TimerTask() {
                @Override
                public void run() {
                    cerrarSession();
                }
            };timer.schedule(timerTask,2000);

            return true;
        }
        //Action Chat
        if (id == R.id.chat){

            Intent mIntent =  new Intent(getApplicationContext(),ChatMain.class);
            startActivity(mIntent);


        }

        return super.onOptionsItemSelected(item);
    }

    private void consultarCodigoUsuario(){

        try {
            BufferedReader bufferedReader=new BufferedReader(new InputStreamReader(openFileInput("codigo.txt")));
            FirebaseReference.CODIGO_USUARIO_REFERENCE="users/"+bufferedReader.readLine();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Log.d("###", FirebaseReference.CODIGO_USUARIO_REFERENCE.toString());
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
