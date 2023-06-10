package com.example.tutron;

import static android.content.ContentValues.TAG;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;

public class login_screen extends AppCompatActivity {
    Button Registerbtn, Loginbtn;
    TextInputEditText emailform, passwordform;

    FirebaseAuth mAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_screen);

        mAuth = FirebaseAuth.getInstance();

        Registerbtn = findViewById(R.id.login_test);

        Loginbtn = findViewById(R.id.login_loginbtn);

        emailform = findViewById(R.id.login_email);

        passwordform = findViewById(R.id.login_password);

        Registerbtn.setOnClickListener(v -> {
            Intent i = new Intent(login_screen.this, register_screen.class);
            startActivity(i);
        });

        Loginbtn.setOnClickListener(v -> {

            if(TextUtils.isEmpty(String.valueOf(emailform.getText()))){
                Toast.makeText(login_screen.this, "Email empty.",
                        Toast.LENGTH_SHORT).show();
            }
            if(TextUtils.isEmpty(String.valueOf(passwordform.getText()))){
                Toast.makeText(login_screen.this, "Password empty.",
                        Toast.LENGTH_SHORT).show();
            }

            mAuth.signInWithEmailAndPassword(String.valueOf(emailform.getText()), String.valueOf(passwordform.getText()))
                    .addOnCompleteListener(this, task -> {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithEmail:success");

                            Intent gotomain = new Intent(login_screen.this, MainActivity.class);
                            startActivity(gotomain);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithEmail:failure", task.getException());
                            Toast.makeText(login_screen.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }
                    });
        });

    }
}