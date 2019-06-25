package com.example.bilgisayar.gykproje.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.bilgisayar.gykproje.R;
import com.example.bilgisayar.gykproje.models.UserModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserInfo;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class RegisterActivity extends AppCompatActivity {

    private static final String TAG = "UserRegister";

    //ForFirebase
    private FirebaseAuth mAuth;
    private FirebaseDatabase mfirebaseDatabase;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private DatabaseReference myRef;
    String userID;


    EditText register_usernameET;
    EditText register_phoneET;
    EditText register_emailEt;
    EditText register_passwordET;
    EditText register_password2ET;
    Button register_btn;
    String userEmail;
    String userPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mAuth = FirebaseAuth.getInstance();
        mfirebaseDatabase = FirebaseDatabase.getInstance();
        myRef = mfirebaseDatabase.getReference();


        register_usernameET = findViewById(R.id.register_username);
        register_phoneET = findViewById(R.id.register_phone);
        register_emailEt = findViewById(R.id.register_email);
        register_passwordET = findViewById(R.id.register_password);
        register_password2ET = findViewById(R.id.register_password2);
        register_btn = findViewById(R.id.register_button);


        register_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                getUserInfoAndRegister();

            }
        });

    }

    private void getUserInfoAndRegister() {
        userEmail = register_emailEt.getText().toString().trim();
        userPassword = register_passwordET.getText().toString().trim();
        String userConfirmPassword = register_password2ET.getText().toString();

        if (!userEmail.isEmpty() && !userPassword.isEmpty() && !userConfirmPassword.isEmpty()) {
            if (userPassword.equals(userConfirmPassword)) {
                register();
            }
        } else {
            Toast.makeText(getApplicationContext(), "Kayıt için tüm alanları doldurunuz!", Toast.LENGTH_LONG).show();
        }
    }

    private void register() {


        mAuth.createUserWithEmailAndPassword(userEmail, userPassword).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    FirebaseUser firebaseUser = mAuth.getCurrentUser();
                    Toast.makeText(getApplicationContext(), "Kayıt işlemi başarılı", Toast.LENGTH_SHORT).show();
                    Intent loginIntent = new Intent(RegisterActivity.this, LoginActivity.class);
                    startActivity(loginIntent);


                }
            }
        }).addOnFailureListener(this, new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                if (e instanceof FirebaseAuthException) {
                    if (((FirebaseAuthException) e).getErrorCode().equals("ERROR_WEAK_PASSWORD")) {
                        Toast.makeText(getApplicationContext(), "Eksik Şifre", Toast.LENGTH_SHORT).show();

                    } else if (((FirebaseAuthException) e).getErrorCode().equals("ERROR_INVALID_EMAIL")) {
                        Toast.makeText(getApplicationContext(), "Geçersiz mail", Toast.LENGTH_SHORT).show();

                    } else if (((FirebaseAuthException) e).getErrorCode().equals("ERROR_EMAIL_ALREADY_IN_USE")) {
                        Toast.makeText(getApplicationContext(), "Mail zaten kayıtlı", Toast.LENGTH_SHORT).show();

                    }

                }
            }
        });

          adduser();

    }
    //Kaydolan kişilerin verilerini database e ekleyen kısım
    private void adduser(){
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference reference = firebaseDatabase.getReference().child("ProfileData");
        String userID = reference.push().getKey();
        String receivedUserName = register_usernameET.getText().toString();
        String receivedUserMail = register_emailEt.getText().toString();
        String receivedUserPhone = register_phoneET.getText().toString();

        if (receivedUserName.length() > 0) {
            reference.child(userID).child("UserName").setValue(receivedUserName);
            reference.child(userID).child("UserMail").setValue(receivedUserMail);
            reference.child(userID).child("UserPhone").setValue(receivedUserPhone);
            Toast.makeText(getApplicationContext(), "Başarılı", Toast.LENGTH_SHORT).show();

        } else {
            Toast.makeText(this, "Başarısız", Toast.LENGTH_SHORT).show();
        }
        register_usernameET.setText("");
        register_emailEt.setText("");
        register_phoneET.setText("");
        register_passwordET.setText("");
        register_password2ET.setText("");
    }
}


