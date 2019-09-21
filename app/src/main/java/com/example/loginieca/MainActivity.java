package com.example.loginieca;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, GoogleApiClient.OnConnectionFailedListener {

    //global
    EditText txtUsuario, txtContrasena;
    Button btnLogin, btnAdd;
    private SignInButton signInButton;
    private ProgressDialog progressDialog;
    private GoogleApiClient googleApiClient;
    public static  final int SIGN_IN_CODE = 777;
    // crear objetos de conexion
    FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        GoogleSignInOptions gso =new  GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().build();


        googleApiClient = new GoogleApiClient.Builder(this).enableAutoManage(this,this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso).build();


        signInButton = (SignInButton) findViewById(R.id.signInButton);
        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = Auth.GoogleSignInApi.getSignInIntent(googleApiClient);
                startActivityForResult(intent, SIGN_IN_CODE);
            }
        });




        //inicializamos el objeto firebaseAuth
        firebaseAuth = FirebaseAuth.getInstance();

        //Referenciamos los views
        txtUsuario = (EditText) findViewById(R.id.txtUser);
        txtContrasena = (EditText) findViewById(R.id.txtPass);

        btnAdd = (Button) findViewById(R.id.btnAdd);
        btnLogin = (Button) findViewById(R.id.btnLogin);

        progressDialog = new ProgressDialog(this);
        //attaching listener to button
        btnAdd.setOnClickListener(this);
        btnLogin.setOnClickListener(this);
    }

    @SuppressWarnings("deprecation")
    private void registrarUsuario() {

        //Obtenemos el email y la contraseña desde las cajas de texto
        String email = txtUsuario.getText().toString().trim();
        String password = txtContrasena.getText().toString().trim();

        //Verificamos que las cajas de texto no esten vacías
        if (TextUtils.isEmpty(email)) {
            Toast.makeText(this, "Se debe ingresar un email", Toast.LENGTH_LONG).show();
            return;
        }
        if (TextUtils.isEmpty(password)) {
            Toast.makeText(this, "Falta ingresar la contraseña", Toast.LENGTH_LONG).show();
            return;
        }
        progressDialog.setMessage("Espera un momento");
        progressDialog.show();


        //creating a new user
        firebaseAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(MainActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        if (task.isSuccessful()) {
                            Toast.makeText(MainActivity.this, "Se ha registrado el usuario con Emil: " + txtUsuario.getText(), Toast.LENGTH_LONG).show();
                        } else {

                            if (task.getException() instanceof FirebaseAuthUserCollisionException) {

                                Toast.makeText(MainActivity.this, "Usuario ya existente", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(MainActivity.this, "No se pudo registrar el usuario", Toast.LENGTH_LONG).show();
                            }
                        }

                        progressDialog.dismiss();

                    }
                });
    }


    private void Loguearusuario() {


        //Obtenemos el email y la contraseña desde las cajas de texto
        final String email = txtUsuario.getText().toString().trim();
        String password = txtContrasena.getText().toString().trim();

        //Verificamos que las cajas de texto no esten vacías
        if (TextUtils.isEmpty(email)) {
            Toast.makeText(this, "Se debe ingresar un email", Toast.LENGTH_LONG).show();
            return;
        }
        if (TextUtils.isEmpty(password)) {
            Toast.makeText(this, "Falta ingresar la contraseña", Toast.LENGTH_LONG).show();
            return;
        }
        progressDialog.setMessage("Realisando la consulta");
        progressDialog.show();


        //Loguear usuario
        firebaseAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(MainActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        if (task.isSuccessful()) {
                            int pos = email.indexOf("@");
                            String user = email.substring(0, pos);

                            Toast.makeText(MainActivity.this, "¡BIENVENIDO! " + txtUsuario.getText(), Toast.LENGTH_LONG).show();

                            Intent intencion = new Intent(getApplication(), Menu.class);
                            intencion.putExtra(Menu.user, user);
                            startActivity(intencion);

                        } else {

                            if (task.getException() instanceof FirebaseAuthUserCollisionException) {

                                Toast.makeText(MainActivity.this, "", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(MainActivity.this, "No se pudo registrar el usuario", Toast.LENGTH_LONG).show();
                            }
                        }

                        progressDialog.dismiss();

                    }
                });

    }


    @Override
    public void onClick(View view) {

        switch (view.getId()){


            case R.id.btnAdd:

                registrarUsuario();
                break;

            case R.id.btnLogin:

                Loguearusuario();

        }

        //Invocamos al método:
        registrarUsuario();
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == SIGN_IN_CODE){

            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            handlesSignInResult(result);
        }
    }

    private void handlesSignInResult(GoogleSignInResult result) {

        if (result.isSuccess()){
            goMainScreen();

        }else{

            Toast.makeText(this, "Nose pudo iniciar secion", Toast.LENGTH_SHORT).show();
        }
    }

    private void goMainScreen() {

        Intent intent = new Intent(MainActivity.this,Menu.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }
}
