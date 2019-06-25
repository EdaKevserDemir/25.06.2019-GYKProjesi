package com.example.bilgisayar.gykproje.activities;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.bilgisayar.gykproje.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;

    EditText userepostaET;
    EditText userpasswordET;
    Button loginbutton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mAuth = FirebaseAuth.getInstance();


       userepostaET=findViewById(R.id.epostaEt);
        userpasswordET=findViewById(R.id.passwordEt);
        loginbutton=findViewById(R.id.loginBTN);


        ((Button)findViewById(R.id.registerBTN)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this,RegisterActivity.class));
            }
        });


 loginbutton.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            String userEmail = userepostaET.getText().toString().trim();
            String userPassword = userpasswordET.getText().toString().trim();
            if (!userEmail.isEmpty() && !userPassword.isEmpty()) {
                login(userEmail, userPassword);
            } else {
                Toast.makeText(getApplicationContext(), "Email ya da parola boş bırakılamaz!", Toast.LENGTH_LONG).show();
            }
        }
    });
}

    private void login(String email, String password) {
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            startActivity(new Intent(LoginActivity.this, MainActivity.class));
                            finish();
                            // Sign in success, update UI with the signed-in user's information
                            Log.d("EMail", "signInWithEmail:success");
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w("Fail", "signInWithEmail:failure", task.getException());

                        }
                    }
                });
    }
}
