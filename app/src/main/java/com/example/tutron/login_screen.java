package com.example.tutron;

import static android.content.ContentValues.TAG;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.util.Util;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Objects;

public class login_screen extends AppCompatActivity {
    Button Registerbtn, Loginbtn;
    TextInputEditText emailform, passwordform;
    FirebaseAuth mAuth;
    FirebaseFirestore db;

    Utils utils = new Utils();

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
                return;
            }
            if(TextUtils.isEmpty(String.valueOf(passwordform.getText()))){
                Toast.makeText(login_screen.this, "Password empty.",
                        Toast.LENGTH_SHORT).show();
                return;
            }

            mAuth.signInWithEmailAndPassword(String.valueOf(emailform.getText()), String.valueOf(passwordform.getText()))
                    .addOnCompleteListener(this, task -> {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithEmail:success");
                            sendtonext();
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithEmail:failure", task.getException());
                            Toast.makeText(login_screen.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }
                    });
        });

    }
    public void sendtonext(){
        db = FirebaseFirestore.getInstance();
        DocumentReference doc = db.collection("Users").document(mAuth.getCurrentUser().getUid());
        doc.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                DocumentSnapshot document = task.getResult();
                if (document.exists()) {
                    Log.d(TAG, "DocumentSnapshot data: " + document.getData());
                    if(document.getData().get("Type").toString().equals("Administrator")){
                        Intent adminIntent = new Intent(login_screen.this, Administrator.class);
                        startActivity(adminIntent);
                    }
                    if(document.getData().get("Type").toString().equals("Tutor")){
                        CheckSuspended(mAuth.getCurrentUser().getUid());
                    }
                    if(document.getData().get("Type").toString().equals("Student")){
                        Intent gotomain = new Intent(login_screen.this, MainActivity.class);
                        startActivity(gotomain);
                    }
                } else {
                    Log.d(TAG, "No such document");
                }
            } else {
                Log.d(TAG, "get failed with ", task.getException());
            }
        });
    }

    public void CheckSuspended(String uid){ // check if a tutor has been suspended
        DocumentReference doc = db.collection("Tutors").document(uid);
        doc.get().addOnCompleteListener(task -> {
            if(task.isSuccessful()){
                DocumentSnapshot document = task.getResult();
                if(document.exists()){
                    Log.d(TAG, String.valueOf(document.getData()));
                    if(Boolean.TRUE.equals(document.getBoolean("Suspended"))){
                        AlertDialog.Builder builder = new AlertDialog.Builder(login_screen.this);
                        Timestamp timestamp = document.getTimestamp("SuspendTime");
                        if(timestamp != null){
                            Date d = timestamp.toDate();
                            Calendar cal = Calendar.getInstance();
                            cal.setTime(d);
                            SimpleDateFormat formatted = new SimpleDateFormat("dd-MM-yyyy");
                            formatted.setTimeZone(cal.getTimeZone());
                            builder.setMessage("You've been suspended until:\n" + formatted.format(cal.getTime()) + "\n(DD-MM-YYYY)");
                            AlertDialog alert = builder.create();
                            alert.setTitle("Temporary Suspension Notice");
                            alert.show();
                            mAuth.signOut();
                        }
                        else{
                            builder.setMessage("You've been permanently suspended. Goodbye.");
                            AlertDialog alert = builder.create();
                            alert.setTitle("Permanent Suspension Notice");
                            alert.show();
                            mAuth.signOut();
                        }
                    }
                    else{
                        Intent gototutorscreen = new Intent(login_screen.this, TutorScreen.class);
                        startActivity(gototutorscreen);
                    }
                }
            }
        });
    }
}