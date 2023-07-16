package com.example.tutron;

import static android.content.ContentValues.TAG;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.Locale;

public class TutorProfile extends AppCompatActivity {

    FirebaseFirestore db;
    FirebaseAuth mAuth;
    FirebaseUser user;
    ArrayList<Topic> all_topics_list = new ArrayList<>();
    TextView desc, header, hourlyrate;
    Spinner offered_topics, profile_topics;
    Button edit_topics;
    ImageButton back;
    SpinnerAdapter offered_adapter, profile_adapter;


    TextView tutor_rating;
    @Override
    protected void onRestart(){
        super.onRestart();
        recreate();
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tutor_profile);

        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();

        desc = findViewById(R.id.tutor_desc);
        header = findViewById(R.id.tutor_profile_header);

        hourlyrate = findViewById(R.id.tutor_profile_hourlyrate);
        offered_topics = findViewById(R.id.offered_topics);
        profile_topics = findViewById(R.id.profile_topics);
        edit_topics = findViewById(R.id.edit_topics);

        tutor_rating = findViewById(R.id.tutor_rating);

        back = findViewById(R.id.tutor_profile_back);

        BuildProfile(mAuth.getCurrentUser().getUid());

        GetTopics(mAuth.getCurrentUser().getUid());

        GetRating();

        edit_topics.setOnClickListener(v -> {
            Intent intent = new Intent(TutorProfile.this, EditTopics.class);
            startActivity(intent);
        });

        hourlyrate.setOnClickListener(v -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(TutorProfile.this);
            builder.setTitle("Edit Hourly Rate");
            EditText newrate = new EditText(TutorProfile.this);
            newrate.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
            builder.setView(newrate);
            builder.setPositiveButton(R.string.proceed, (dialog, which) -> {
                        db.collection("Tutors")
                                .document(mAuth.getCurrentUser().getUid())
                                .update("hourly", Double.parseDouble(newrate.getText().toString()));
                recreate();
            });

            builder.setNegativeButton(R.string.cancel, (dialog, which) -> {
                dialog.dismiss();
            });

            builder.show();



        });
        profile_topics.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(position == 0){
                }
                else{
                    AlertDialog.Builder builder = new AlertDialog.Builder(TutorProfile.this);

                    builder.setTitle("Details");

                    builder.setMessage("Description: " + all_topics_list.get(--position).getDescription() + "\n" + "Years of Experience: " + all_topics_list.get(position).getExp());
                    builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
                    builder.show();
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }

        });

        back.setOnClickListener(v -> {
            finish();
        });
    }
    public void BuildProfile(String Uuid){
        DocumentReference doc = db.collection("Tutors").document(Uuid);
        doc.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                DocumentSnapshot document = task.getResult();
                if (document.exists()) {
                    Log.d(TAG, "DocumentSnapshot data: " + document.getData());
                    if(document.get("firstName") != null && document.getData().get("lastName") != null){
                        header.append(" " + document.get("firstName").toString() + " " + document.get("lastName").toString());
                    }
                    if(document.get("description") != null){
                        desc.append(document.get("description").toString());
                    }
                    if(document.get("hourly") != null){
                        hourlyrate.append(" " + document.get("hourly").toString());
                    }
                    if(document.get("hourly") == null){
                        db.collection("Tutors").document(Uuid).update("hourly", 15.5);
                        recreate();
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
        CollectionReference topics = db.collection("Tutors").document(Uuid).collection("profile_topics");
        topics.get().addOnCompleteListener(task -> {
            if(task.isSuccessful()){
                for(QueryDocumentSnapshot document : task.getResult()){
                    Log.d(TAG, String.valueOf(document.getData()));
                    all_topics_list.add(document.toObject(Topic.class));
                }
                Log.d(TAG, String.valueOf(all_topics_list.size()));
                SetAdapters();
            }
        });
    }
    public void SetAdapters(){
        ArrayList<String> user_viewable_profile = new ArrayList<>();
        user_viewable_profile.add("Topics");
        ArrayList<String> user_viewable_offered = new ArrayList<>();
        user_viewable_offered.add("Offered Topics");
        for(Topic t: all_topics_list){
            if(t.isOffered()){
                user_viewable_offered.add(t.getName());
            }
            user_viewable_profile.add(t.getName());
        }
        profile_adapter = new ArrayAdapter<String>(this, R.layout.simple_list_item_1, user_viewable_profile.toArray(new String[0])){
            @Override
            public boolean isEnabled(int position){
                return position != 0;
            }
        };
        offered_adapter = new ArrayAdapter<String>(this, R.layout.simple_list_item_1, user_viewable_offered.toArray(new String[0])){
            @Override
            public boolean isEnabled(int position){
                return position != 0;
            }

        };

        profile_topics.setAdapter(profile_adapter);
        offered_topics.setAdapter(offered_adapter);
    }

    public void GetRating(){
        db.collection("Tutors").document(mAuth.getCurrentUser().getUid()).collection("ratings").get().addOnCompleteListener(task -> {
            if(task.isSuccessful()){
                if(task.getResult().size() != 0) {
                    float rating = 0;
                    int size = 0;
                    for (DocumentSnapshot doc : task.getResult()) {
                        rating += Float.parseFloat(doc.get("rating").toString());
                        size++;
                    }
                    String format = String.format(Locale.US, "%.2f", rating/size);
                    Log.d(TAG, format);
                    db.collection("Tutors").document(mAuth.getCurrentUser().getUid()).update("rating", Double.parseDouble(format));
                    tutor_rating.append(" " + format +"/5");
                }
                else{
                    tutor_rating.setText("N/A");
                }
            }
        });
        tutor_rating.setOnClickListener(v -> {
            Intent to_ratings = new Intent(TutorProfile.this, TutorReviews.class);
            startActivity(to_ratings);
        });
    }
}