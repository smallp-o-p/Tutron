package com.example.tutron;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class ViewTutorProfile extends AppCompatActivity {
    FirebaseFirestore db;
    Button tutor_result_purchase_request;

    FirebaseAuth mAuth;

    ImageButton tutor_result_back;
    TextView tutor_result_rating, tutor_result_profile_desc, tutor_result_header, tutor_result_education;
    Spinner tutor_result_offered_topics;

    ArrayList<Topic> tutors_topics = new ArrayList<>();

    ArrayList<String> topic_ids = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_tutor_profile);

        tutor_result_header = findViewById(R.id.tutor_result_header);

        tutor_result_profile_desc = findViewById(R.id.tutor_result_profile_desc);

        tutor_result_rating = findViewById(R.id.tutor_result_rating);

        tutor_result_purchase_request = findViewById(R.id.tutor_result_purchase_request);

        tutor_result_offered_topics = findViewById(R.id.tutor_result_offered_topics);

        tutor_result_education = findViewById(R.id.tutor_result_education);

        tutor_result_back = findViewById(R.id.tutor_result_back);

        db = FirebaseFirestore.getInstance();

        mAuth = FirebaseAuth.getInstance();

        Bundle extras = getIntent().getExtras();
        db.collection("Tutors").document(extras.getString("tutorid")).get().addOnCompleteListener(task -> {
            if(task.isSuccessful()){
                DocumentSnapshot doc = task.getResult();
                tutor_result_profile_desc.setText(doc.getString("description"));
                tutor_result_header.append("\n" + doc.getString("firstName") + " " + doc.getString("lastName"));
                tutor_result_education.append(" " + doc.getString("education"));
                if(doc.getString("rating") == null){
                    tutor_result_rating.append(" " + "N/A");
                }
                else{
                    tutor_result_rating.append(" " + doc.getString("rating"));
                }
            }
        });

        db.collection("Tutors").document(extras.getString("tutorid")).collection("profile_topics").get().addOnCompleteListener(task -> {
            if(task.isSuccessful()){
                for(DocumentSnapshot doc: task.getResult()){
                    if(Boolean.TRUE.equals(doc.getBoolean("offered"))){
                        tutors_topics.add(doc.toObject(Topic.class));
                        topic_ids.add(doc.getId());
                    }
                }
                BuildList();
            }
        });

        tutor_result_back.setOnClickListener(v -> finish());

        tutor_result_purchase_request.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(tutor_result_offered_topics.getSelectedItem().toString().equals("Tutor's Offered Topics")){
                    Toast.makeText(ViewTutorProfile.this, "You must select a topic.", Toast.LENGTH_SHORT).show();
                }
                else{
                    DatePickerDialog datepicker = new DatePickerDialog(ViewTutorProfile.this);
                    datepicker.setOnDateSetListener((view, year, month, dayOfMonth) -> {
                        final long today = System.currentTimeMillis() - 1000;
                        Calendar cal = Calendar.getInstance();
                        cal.set(Calendar.YEAR, year);
                        cal.set(Calendar.MONTH, month);
                        cal.set(Calendar.DAY_OF_MONTH, dayOfMonth);

                        if(cal.getTimeInMillis() < today){
                            Toast.makeText(ViewTutorProfile.this, "Invalid date.", Toast.LENGTH_SHORT).show();
                        }
                        else{
                            Date d = cal.getTime();
                            db.collection("Students").document(mAuth.getCurrentUser().getUid()).get().addOnCompleteListener(task -> {
                                if(task.isSuccessful()){
                                    DocumentSnapshot doc = task.getResult();
                                    Request newRequest = new Request(
                                            0,
                                            mAuth.getCurrentUser().getUid(),
                                            extras.getString("tutorid"),
                                            topic_ids.get(tutor_result_offered_topics.getSelectedItemPosition()-1),
                                            d,
                                            doc.getString("firstName"),
                                            doc.getString("lastName"),
                                            tutor_result_offered_topics.getSelectedItem().toString());
                                    db.collection("Tutors").document(extras.getString("tutorid")).collection("requests").add(newRequest);
                                    Toast.makeText(ViewTutorProfile.this, "Request made!", Toast.LENGTH_LONG).show();
                                }
                            });
                        }
                    });
                    datepicker.show();
                }
            }
        });
    }

    public void BuildList(){
        ArrayList<String> user_viewable = new ArrayList<>();
        user_viewable.add("Tutor's Offered Topics");
        for(Topic t: tutors_topics){
            user_viewable.add(t.getName());
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(ViewTutorProfile.this, R.layout.simple_list_item_1, user_viewable.toArray(new String[0])){
            @Override
            public boolean isEnabled(int position){
                return position != 0;
            }
        };
        tutor_result_offered_topics.setAdapter(adapter);


    }
}