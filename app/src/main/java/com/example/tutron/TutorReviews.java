package com.example.tutron;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class TutorReviews extends AppCompatActivity {

    ListView tutor_reviews;

    ImageButton back;

    TextView header;

    FirebaseFirestore db;

    FirebaseAuth mAuth;

    ArrayList<Rating> ratings = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tutor_reviews);

        header = findViewById(R.id.tutor_reviews_header);

        tutor_reviews = findViewById(R.id.tutor_reviews_rating_list);

        back = findViewById(R.id.tutor_reviews_back);

        mAuth = FirebaseAuth.getInstance();

        db = FirebaseFirestore.getInstance();

        Bundle extras = getIntent().getExtras();

        String tutor_id;
        if(extras == null){
            tutor_id = mAuth.getCurrentUser().getUid();
        }
        else{
            tutor_id = extras.getString("tutorid");
        }

        db.collection("Tutors").document(tutor_id).get().addOnCompleteListener(task -> {
            if(task.isSuccessful()){
                header.append(" " + task.getResult().getString("firstName") + " " + task.getResult().getString("lastName"));
            }
        });

        db.collection("Tutors").document(tutor_id).collection("ratings").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()){
                    for(DocumentSnapshot doc: task.getResult()){
                        ratings.add(doc.toObject(Rating.class));
                    }
                    BuildList();
                }
            }
        });

        back.setOnClickListener(v -> finish());
    }

    public void BuildList(){
        ArrayList<String> rating_titles = new ArrayList<>();
        for(Rating r: ratings){

            double rounded = Math.round(r.getRating()*100)/100.0d;
            rating_titles.add(r.getRating() + "/5 : " + r.getTopicName());
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<>(TutorReviews.this, R.layout.simple_list_item_1, rating_titles.toArray(new String[0]));

        tutor_reviews.setAdapter(adapter);

        tutor_reviews.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                AlertDialog.Builder builder = new AlertDialog.Builder(TutorReviews.this);

                builder.setTitle("Review by: " + ratings.get(position).getName());
                builder.setMessage("Topic: " + ratings.get(position).getTopicName() + "\n\nRating Body: " + ratings.get(position).getDesc());

                builder.setNegativeButton(R.string.dismiss, (dialog, which) -> dialog.dismiss());

                builder.show();
            }
        });

    }
}