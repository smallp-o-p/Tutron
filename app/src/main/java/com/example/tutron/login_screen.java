package com.example.tutron;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;

public class login_screen extends AppCompatActivity {
    Button Registerbtn, Loginbtn;
    TextInputEditText email, password;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_screen);

        Registerbtn = (Button) findViewById(R.id.login_test);

        Loginbtn = (Button) findViewById(R.id.login_loginbtn);

        email = findViewById(R.id.login_email);

        password = findViewById(R.id.login_password);

        Registerbtn.setOnClickListener(v -> {
            Intent i = new Intent(login_screen.this, register_screen.class);
            startActivity(i);
        });

    }
}