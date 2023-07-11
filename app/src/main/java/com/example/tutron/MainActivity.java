package com.example.tutron;

import static android.content.ContentValues.TAG;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class MainActivity extends AppCompatActivity {
    FirebaseAuth mAuth;
    FirebaseFirestore db;
    TextView main;
    Button logout, edit;
    String type;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAuth = FirebaseAuth.getInstance();

        main = findViewById(R.id.mainText);

        logout = findViewById(R.id.logoutbtn);

        edit = findViewById(R.id.editprofile);

        if(mAuth.getCurrentUser() != null){
            db = FirebaseFirestore.getInstance();

            DocumentReference doc = db.collection("Users").document(mAuth.getCurrentUser().getUid());

            doc.get().addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        Log.d(TAG, "DocumentSnapshot data: " + document.getData());
                        type = document.getData().get("Type").toString();
                        if(type.equals("Student")){
                            ;
                        }
                        if(type.equals("Tutor")){

                        }
                    } else {
                        Log.d(TAG, "No such document");
                    }
                } else {
                    Log.d(TAG, "get failed with ", task.getException());
                }
            });
        }
        logout.setOnClickListener(v -> {
            mAuth.signOut();
            Intent gotologin = new Intent(MainActivity.this, login_screen.class);
            gotologin.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(gotologin);
        });

        edit.setOnClickListener(v -> {
            if(type.equals("Student")){
                ;
            }
            if(type.equals("Tutor")){
                Intent TutorIntent = new Intent(MainActivity.this, TutorProfile.class);
                TutorIntent.putExtra("id", mAuth.getCurrentUser().getUid());
                startActivity(TutorIntent);
            }
        });
    }
}