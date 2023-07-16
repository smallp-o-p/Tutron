package com.example.tutron;

import static android.content.ContentValues.TAG;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.Pair;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.Map;

public class SubmitComplaint extends AppCompatActivity {
    FirebaseFirestore db;
    FirebaseAuth mAuth;
    TextView tutor_name;
    ImageButton back;
    String studentFirstName;
    String studentLastName;
    String docid;

    ArrayList<String> tutors = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_submit_complaint);

        db = FirebaseFirestore.getInstance();

        mAuth = FirebaseAuth.getInstance();

        back = findViewById(R.id.back_arrow_complaint);

        tutor_name = findViewById(R.id.tutor_name);

        Spinner tutorspinner = findViewById(R.id.tutors);

        TextInputEditText complaint_description = findViewById(R.id.complaint_description);

        Button submit = findViewById(R.id.submit_complaint);

        Intent intent = getIntent();

        docid = intent.getStringExtra("docid");
        Log.d(TAG, "docid:" + docid);

        // Get the first and the last name of the currently logged in student
        if(mAuth.getCurrentUser() != null) {
            db.collection("Students").document(mAuth.getCurrentUser().getUid()).get().addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    DocumentSnapshot doc = task.getResult();
                    if (doc.exists()) {
                        Map<String, Object> map = doc.getData();
                        Log.d(TAG, String.valueOf(map));
                        studentFirstName = (String) map.get("firstName");
                        studentLastName = (String) map.get("lastName");

                    }
                }
            });

            db.collection("Tutors").get().addOnSuccessListener(querySnapshot -> {
                ArrayList<Pair<String, Pair<String, String>>> keyValueList = new ArrayList<>();

                for (DocumentSnapshot documentSnapshot : querySnapshot.getDocuments()) {
                    String documentId = documentSnapshot.getId();
                    String firstname = documentSnapshot.getString("firstName");
                    String lastname = documentSnapshot.getString("lastName");

                    Pair<String, String> namePair = new Pair<>(firstname, lastname);
                    Pair<String, Pair<String, String>> keyValuePair = new Pair<>(documentId, namePair);

                    keyValueList.add(keyValuePair);
                }

                db.collectionGroup("requests")
                        .whereEqualTo("studentFirstName", studentFirstName)
                        .whereEqualTo("studentLastName", studentLastName)
                        .whereEqualTo("status", 1)
                        .get()
                        .addOnSuccessListener(querySnapshot1 -> {
                            ArrayList<String> names = new ArrayList<>();
                            ArrayList<Pair<String, String>> names_two = new ArrayList<>();
                            for (QueryDocumentSnapshot document : querySnapshot1) {
                                String tutor = document.getString("tutorID");
                                tutors.add(tutor);
                            }
                            for (int i = 0; i < tutors.size(); i++) {
                                String tutorID = tutors.get(i);
                                for (Pair<String, Pair<String, String>> keyValuePair : keyValueList) {
                                    String documentId = keyValuePair.first;
                                    Pair<String, String> namePair = keyValuePair.second;
                                    String firstname = namePair.first;
                                    String lastname = namePair.second;

                                    if (tutorID.equals(documentId)) {
                                        names.add(firstname + " " + lastname);
                                        names_two.add(namePair);
                                    }
                                }
                            }
                            ArrayAdapter<String> ad_tutor = new ArrayAdapter<>(SubmitComplaint.this, android.R.layout.simple_spinner_item, names);
                            tutorspinner.setAdapter(ad_tutor);
                        });
            });

        }

        submit.setOnClickListener(v -> {

            String desc = String.valueOf(complaint_description.getText());

            String tutor = tutorspinner.getSelectedItem().toString();
            String[] parts = tutor.split(" ");
            String firstName = parts[0];
            String lastName = parts[1];

            String tutorID = tutors.get(tutorspinner.getSelectedItemPosition());

            if(TextUtils.isEmpty(desc)){
                Toast.makeText(getApplicationContext(), "Complaint description required.", Toast.LENGTH_SHORT).show();
                return;
            }

            if(desc.length() > 700){
                Toast.makeText(getApplicationContext(), "Complaint description is too long.", Toast.LENGTH_SHORT).show();
                return;
            }

            Log.d(TAG, String.valueOf(tutorID));

            Complaint newComplaint = new Complaint(tutorID, firstName, lastName, desc);
            Toast.makeText(SubmitComplaint.this, "Complaint submitted.", Toast.LENGTH_SHORT).show();
            GoToNext(newComplaint);

        });
        back.setOnClickListener(v -> finish());
    }

    public void GoToNext(Complaint complaint) {
        db.collection("Complaints").document().set(complaint)
                .addOnSuccessListener(aVoid -> {
                    // The complaint was successfully written to the database
                    finish();
                })
                .addOnFailureListener(e -> {
                    // An error occurred while writing the complaint to the database
                    Toast.makeText(SubmitComplaint.this, "Failed to submit complaint", Toast.LENGTH_SHORT).show();
                });
    }

}
