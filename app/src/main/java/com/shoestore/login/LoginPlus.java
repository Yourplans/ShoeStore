package com.shoestore.login;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.shoestore.R;

import com.shoestore.activities.MainActivity;
import com.shoestore.chat.UserDetails;
import com.shoestore.objects.FirebaseReference;
import com.shoestore.objects.UsuariosVo;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStreamWriter;

import static android.content.ContentValues.TAG;


public class LoginPlus extends Fragment {

    private SignInButton btnSignIn;
    private static final int RC_SIGN_IN=1;
    private GoogleApiClient mGoogleApiClient;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthStateListener;
    private ProgressDialog progressDialog;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_plus, container, false);
        progressDialog=new ProgressDialog(getActivity());
        progressDialog.setMessage("Obteniendo Datos...");
        progressDialog.setProgressStyle(progressDialog.STYLE_SPINNER);
        progressDialog.setCancelable( false );
        mAuth=FirebaseAuth.getInstance();
        FirebaseDatabase database=FirebaseDatabase.getInstance();
       final DatabaseReference reference=database.getReference(FirebaseReference.USERS_REFERENCE);
        mAuthStateListener=new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                    FirebaseUser user=firebaseAuth.getCurrentUser();
                if (user!=null){
                    String id=user.getUid();
                    String name=user.getDisplayName();
                    String email=user.getEmail();
                    String photo= String.valueOf( user.getPhotoUrl() );
                    String token="";

                    UsuariosVo usuariosVo=new UsuariosVo(id,name,email,photo,token);
                    //si se logra obtener los datos del usuario se hace la insercion en base de datos
                    reference.child( user.getUid()).setValue(usuariosVo);
                    Toast.makeText( getContext(),"Bienvenido a ShoeStore "+user.getDisplayName(),Toast.LENGTH_SHORT).show();
                    guardarTextCodigo(id);
                    UserDetails.user=id;
                   goMainActivity();
                }
            }
        };
        btnSignIn = (SignInButton) view.findViewById(R.id.btn_sign_in);
        // Configure Google Sign In
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
         mGoogleApiClient=new GoogleApiClient.Builder(getActivity())
                 .enableAutoManage(getActivity(), new GoogleApiClient.OnConnectionFailedListener() {
                     @Override
                     public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
                    Toast.makeText(getContext(),"Error",Toast.LENGTH_SHORT).show();
                     }
                 })
                 .addApi(Auth.GOOGLE_SIGN_IN_API,gso)
                 .build();
        btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signIn();
            }
        });

       return view;

    }

    private void guardarTextCodigo(String id) {
        try {
            OutputStreamWriter outputStreamWriter =
                    new OutputStreamWriter(getActivity().openFileOutput("codigo.txt", Context.MODE_PRIVATE));
            outputStreamWriter.write(id);
            outputStreamWriter.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private void goMainActivity() {

        progressDialog.dismiss();
        Intent intent=new Intent( getContext(), MainActivity.class );
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity( intent );
        Login.finalizar.finish();
    }


    @Override
    public void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthStateListener);
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mAuthStateListener != null) {
            mAuth.removeAuthStateListener(mAuthStateListener);
        }
    }
    private void signIn() {
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent, RC_SIGN_IN);
        progressDialog.show();
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            if (result.isSuccess()) {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = result.getSignInAccount();
                firebaseAuthWithGoogle(account);
            } else {
                // Google Sign In failed, update UI appropriately
                // ...
            }
        }
    }

    private void firebaseAuthWithGoogle(GoogleSignInAccount account) {
        Log.d(TAG, "firebaseAuthWithGoogle:" + account.getId());

        AuthCredential credential = GoogleAuthProvider.getCredential(account.getIdToken(), null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Log.d(TAG, "signInWithCredential:onComplete:" + task.isSuccessful());

                        if (!task.isSuccessful()) {
                            Log.w(TAG, "signInWithCredential", task.getException());
                            Toast.makeText(getContext(), "Comprueba tu conexion.",
                                    Toast.LENGTH_SHORT).show();
                            progressDialog.dismiss();
                        }
                        // ...
                    }
                });
    }
}
