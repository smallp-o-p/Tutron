package com.example.tutron;

import static android.content.ContentValues.TAG;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Map;

public class TutorScreen extends AppCompatActivity {

    // not actioned: 0
    // approved : 1
    // rejected : -1
    Button to_profile, logout_btn;
    TextView tutor_name;
    FirebaseAuth mAuth;
    FirebaseFirestore db;
    ArrayList<Request> rqs = new ArrayList<>();
    ListView list;
    ArrayList<String> students = new ArrayList<>();

    ArrayList<String> rqs_id = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tutor_screen);

        mAuth = FirebaseAuth.getInstance();

        db = FirebaseFirestore.getInstance();

        to_profile = findViewById(R.id.tutor_profile_to);

        logout_btn = findViewById(R.id.tutor_screen_logout);

        tutor_name = findViewById(R.id.tutor_screen_welcome);

        list = findViewById(R.id.purchase_request_lists);

        if(mAuth.getCurrentUser() != null){
            db.collection("Tutors").document(mAuth.getCurrentUser().getUid()).get().addOnCompleteListener(task -> {
                if(task.isSuccessful()){
                    DocumentSnapshot doc = task.getResult();
                    if(doc.exists()){
                        Map<String, Object> map = doc.getData();
                        Log.d(TAG, String.valueOf(map));
                        tutor_name.setText(getString(R.string.hello_there, map.get("firstName"), map.get("lastName")));
                    }
                }
            });

            Query query = db.collection("Tutors")
                    .document(mAuth.getCurrentUser().getUid()).collection("requests").whereEqualTo("status", 0);
            query.get().addOnCompleteListener(task -> {
               if(task.isSuccessful()){
                   QuerySnapshot docs = task.getResult();
                   for(QueryDocumentSnapshot doc : docs){
                       Request request = doc.toObject(Request.class);
                       rqs.add(request);
                       rqs_id.add(doc.getId());
                   }
                    BuildList();
               }
               else{
                   Log.d(TAG, "failed :(");
               }
            });
        }

        to_profile.setOnClickListener(v -> {
            Intent gotoprofile = new Intent(TutorScreen.this, TutorProfile.class);
            startActivity(gotoprofile);
        });

        logout_btn.setOnClickListener(v -> {
            mAuth.signOut();
            finish();
        });
    }
    public void BuildList(){
        for(Request r: rqs){
            students.add(r.getStudentFirstName() + " " + r.getStudentLastName());
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<>(TutorScreen.this, R.layout.simple_list_item_1, students);
        list.setAdapter(adapter);

        list.setOnItemClickListener((parent, view, position, id) -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(TutorScreen.this);

            builder.setTitle("Info about this request:");

            Date date = rqs.get(position).getRequestedDate();

            SimpleDateFormat DateFor = new SimpleDateFormat("dd/MM/yyyy");
            String stringDate= DateFor.format(date);

            builder.setMessage("Topic: " + rqs.get(position).getTopicName() + "\n" + "Requested Date: " + stringDate + "\n");

            builder.setNeutralButton("Cancel", (dialog, which) -> {
                dialog.dismiss();
            });

            builder.setPositiveButton("Approve", (dialog, which) -> {
                db.collection("Tutors").document(mAuth.getCurrentUser().getUid()).collection("requests").document(rqs_id.get(position)).update("status", 1);
                recreate();
            });

            builder.setNegativeButton("Reject", (dialog, which) -> {
                db.collection("Tutors").document(mAuth.getCurrentUser().getUid()).collection("requests").document(rqs_id.get(position)).update("status", -1);
                recreate();
            });
            builder.show();
        });
    }
}