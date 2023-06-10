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
    Button logout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAuth = FirebaseAuth.getInstance();

        main = findViewById(R.id.mainText);

        logout = findViewById(R.id.logoutbtn);

        if(mAuth.getCurrentUser() != null){
            db = FirebaseFirestore.getInstance();

            DocumentReference doc = db.collection("Users").document(mAuth.getCurrentUser().getUid());

            doc.get().addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        Log.d(TAG, "DocumentSnapshot data: " + document.getData());
                        main.append("\"" + document.getData().get("Type").toString() + "\"");
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
            Intent sendback = new Intent(MainActivity.this, login_screen.class);
            startActivity(sendback);
        });
    }
}