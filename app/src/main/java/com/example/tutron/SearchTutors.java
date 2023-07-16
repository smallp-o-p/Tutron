package com.example.tutron;

import static android.content.ContentValues.TAG;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class SearchTutors extends AppCompatActivity {
    Bundle extras;
    ImageButton back_button;
    ListView search_results;
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    ArrayList<Tutor> tutors = new ArrayList<>();

    ArrayList<String> tutor_ids = new ArrayList<>();

    CollectionReference collection = db.collection("Tutors");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_tutors);

        back_button = findViewById(R.id.search_tutors_back);

        search_results = findViewById(R.id.tutor_results);

        Intent intent = getIntent();

        extras = intent.getExtras();

        String tn = extras.get("tutor_name").toString();

        String tl;

        if(TextUtils.isEmpty(extras.get("tutor_lang").toString())){
            tl = "English";
        }
        else{
            tl = extras.get("tutor_lang").toString();
        }

        Log.d(TAG, tl);

        String tt = extras.get("tutor_topic").toString();

        Log.d(TAG, tt);
        Log.d(TAG, tl);

        Log.d(TAG, String.valueOf(tn.isEmpty()));

        back_button.setOnClickListener(v -> {
            finish();
        });
        Query topics_query = db.collectionGroup("profile_topics")
                .whereGreaterThanOrEqualTo("name", tt).whereEqualTo("offered", true)
                .orderBy("name");

        topics_query.get().addOnCompleteListener(task -> {
            if(task.isSuccessful()){
                for(QueryDocumentSnapshot doc: task.getResult()){
                    tutor_ids.add(doc.getReference().getParent().getParent().getId());
                }
                for(String t: tutor_ids){
                    Log.d(TAG, t);
                }
                Log.d(TAG, String.valueOf(tutor_ids.size()));
                secondQuery(tn, tl);
            }
        });
}
    public void secondQuery(String tn, String tl){
        Query ourQuery = collection
                .whereGreaterThanOrEqualTo("firstName", tn).
                whereEqualTo("lang", tl)
                .whereEqualTo("Suspended", false);
        ourQuery.get().addOnCompleteListener(task -> {
            if(task.isSuccessful()){
                for(QueryDocumentSnapshot doc : task.getResult()){
                    Log.d(TAG, doc.getData().toString());
                    if(tutor_ids.contains(doc.getId())){
                        tutors.add(doc.toObject(Tutor.class).setUUID(doc.getId()));
                    }
                }
                BuildSearchResults();
            }
        });
    }

    public void BuildSearchResults(){
        ArrayList<Map<String, String>> tutorMaps = new ArrayList<>();

        ArrayList<String> tutorNames = new ArrayList<>();
        for(Tutor t: tutors){
            Map<String, String> map = new HashMap<>();
            map.put(t.getFirstName() + " " + t.getLastName(), t.getUUID());
            tutorMaps.add(map);
            tutorNames.add(t.getFirstName() + " " + t.getLastName());
        }
        Log.d(TAG, String.valueOf(tutorNames.size()));
        ArrayAdapter<String> adapter = new ArrayAdapter(SearchTutors.this, R.layout.simple_list_item_1, tutorNames);

        search_results.setAdapter(adapter);
        search_results.setOnItemClickListener((parent, view, position, id) -> {
            Intent viewProfile = new Intent(SearchTutors.this, ViewTutorProfile.class);
            Log.d(TAG, tutorMaps.get(position).get(tutorNames.get(position)));
            viewProfile.putExtra("tutorid", tutorMaps.get(position).get(tutorNames.get(position)));
            startActivity(viewProfile);
        });
    }
}