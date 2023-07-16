package com.example.tutron;

import static android.content.ContentValues.TAG;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import org.checkerframework.checker.units.qual.A;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class ViewComplaint extends AppCompatActivity {
    String tutorid;
    FirebaseFirestore db;
    TextView fullname;
    TextView desc;
    Button suspend_temp;
    Button suspend_perm;
    Button dismiss;
    ImageButton back;
    DatePickerDialog datepicker;

    String docid;

    ArrayList<Complaint> complaints = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_complaint);

        db = FirebaseFirestore.getInstance();

        suspend_temp = findViewById(R.id.suspendtemp);

        suspend_perm = findViewById(R.id.suspendperm);

        dismiss = findViewById(R.id.dismiss);

        back = findViewById(R.id.back_arrow_complaint);

        fullname = findViewById(R.id.tutorfullname);

        desc = findViewById(R.id.desc);

        Intent intent = getIntent();

        docid = intent.getStringExtra("docid");
        Log.d(TAG, "docid:" + docid);

        datepicker = new DatePickerDialog(ViewComplaint.this);

        db.collection("Complaints").document(docid)
                .get().addOnCompleteListener(task -> {
                    if(task.isSuccessful()){
                        DocumentSnapshot document = task.getResult();
                        if(document.exists()){
                            Log.d(TAG, document.getData().toString());
                            desc.append(" " + document.toObject(Complaint.class).getComplaintDesc());
                            tutorid = document.toObject(Complaint.class).getTutorID();
                            getTutorName(docid);
                            setDatePicker(tutorid);
                        }
                    }
                });

        suspend_temp.setOnClickListener(v -> {
            datepicker.show();
        });

        suspend_perm.setOnClickListener(v -> {
            db.collection("Complaints").document(docid).update("completed", true);
            db.collection("Complaints").document(docid).update("decision", "Suspended (Permanent)");
            db.collection("Tutors").document(tutorid).update("Suspended", true);
            finish();
        });

        dismiss.setOnClickListener(v -> {
            Log.d(TAG, tutorid);
            db.collection("Complaints").document(docid).update("completed", true);
            db.collection("Complaints").document(docid).update("decision", "Dismissed");
            finish();
        });

        back.setOnClickListener(v -> finish());
    }

    public void setDatePicker(String tutor_id){
        DatePickerDialog.OnDateSetListener DatePickerListener = (view, year, month, dayOfMonth) -> {
            Calendar cal = Calendar.getInstance();
            cal.set(Calendar.YEAR, year);
            cal.set(Calendar.MONTH, month);
            cal.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            Date d = cal.getTime();
            Log.d(TAG, d.toString());
            db.collection("Complaints").document(docid).update("completed", true);
            db.collection("Complaints").document(docid).update("Decision", "Suspended (Temporary)");
            db.collection("Tutors").document(tutor_id).update("SuspendTime", d);
            db.collection("Tutors").document(tutor_id).update("Suspended", true);
            finish();
        };
        datepicker.setOnDateSetListener(DatePickerListener);
    }
    public void getTutorName(String complaintID){
        db.collection("Complaints").document(complaintID).get().addOnCompleteListener(task -> {
            if(task.isSuccessful()){
                DocumentSnapshot document = task.getResult();
                if(document.exists()){
                    fullname.append(" " + document.getString("tutorFirstName") + " " + document.getString("tutorLastName"));
                }
            }
        });
    }
}