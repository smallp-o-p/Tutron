package com.example.tutron;

import static android.content.ContentValues.TAG;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class TutorProfile extends AppCompatActivity {

    FirebaseFirestore db;
    FirebaseAuth mAuth;
    FirebaseUser user;
    ArrayList<String> profile_topics_list, offered_topics_list;
    TextView desc, header;
    Spinner offered_topics, profile_topics;
    Button edit_topics;
    SpinnerAdapter offered_adapter, profile_adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tutor_profile);

        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();

        desc = findViewById(R.id.tutor_desc);
        header = findViewById(R.id.tutor_profile_header);
        offered_topics = findViewById(R.id.offered_topics);
        profile_topics = findViewById(R.id.profile_topics);
        edit_topics = findViewById(R.id.edit_topics);

        BuildProfile(mAuth.getCurrentUser().getUid());

        GetTopics(mAuth.getCurrentUser().getUid());

        edit_topics.setOnClickListener(v -> {
            Intent intent = new Intent(TutorProfile.this, EditTopics.class);
            startActivity(intent);
        });
    }

    public void BuildProfile(String Uuid){
        DocumentReference doc = db.collection("Tutors").document(Uuid);
        doc.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                DocumentSnapshot document = task.getResult();
                if (document.exists()) {
                    Log.d(TAG, "DocumentSnapshot data: " + document.getData());
                    if(document.getData().get("firstName") != null && document.getData().get("lastName") != null){
                        header.append(" " + document.getData().get("firstName").toString() + " " + document.getData().get("lastName").toString());
                    }
                    if(document.getData().get("description") != null){
                        desc.append(document.getData().get("description").toString());
                    }
                } else {
                    Log.d(TAG, "No such document");
                }
            } else {
                Log.d(TAG, "get failed with ", task.getException());
            }
        });
    }
    public void GetTopics(String Uuid){
        DocumentReference doc = db.collection("Tutors").document(Uuid);
        doc.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                DocumentSnapshot document = task.getResult();
                if (document.exists()) {
                    Log.d(TAG, "DocumentSnapshot data: " + document.getData());
                    profile_topics_list = (ArrayList<String>) document.get("profile_topics");
                    offered_topics_list = (ArrayList<String>) document.get("offered_topics");
                    if(profile_topics_list != null && offered_topics_list != null){
                        SetAdapters();
                        profile_topics.setAdapter(profile_adapter);
                        offered_topics.setAdapter(offered_adapter);
                    }
                    else {
                        Map<String, List<String[]>> template = new HashMap<>();
                        List<String[]> a = new ArrayList<>();
                        template.put("profile_topics", a);
                        template.put("offered_topics", a);
                        db.collection("Tutors").document(mAuth.getCurrentUser().getUid()).set(template, SetOptions.merge());
                        recreate();
                    }
                }
            } else {
                Log.d(TAG, "get failed with ", task.getException());
            }
        });
    }
    public void SetAdapters(){
        profile_adapter = new ArrayAdapter<String>(this, R.layout.simple_list_item_1, profile_topics_list){
            @Override
            public boolean isEnabled(int position){
                return false;
            }
        };
        offered_adapter = new ArrayAdapter<String>(this, R.layout.simple_list_item_1, offered_topics_list){
            @Override
            public boolean isEnabled(int position) {
                return false;
            }
        };
    }
}