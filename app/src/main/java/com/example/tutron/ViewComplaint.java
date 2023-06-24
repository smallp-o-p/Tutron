package com.example.tutron;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class ViewComplaint extends AppCompatActivity {
    String tutorid;
    FirebaseFirestore db;

    TextView fullname;

    TextView desc;

    Button suspend_temp;

    Button suspend_perm;

    Button dismiss;

    ImageButton back;

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

        String docid = intent.getStringExtra("docid");

        DatePickerDialog datepicker = new DatePickerDialog(ViewComplaint.this);

        DatePickerDialog.OnDateSetListener DatePickerListener = (view, year, month, dayOfMonth) -> {
            Calendar cal = Calendar.getInstance();
            cal.set(Calendar.YEAR, year);
            cal.set(Calendar.MONTH, month);
            cal.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            Date d = cal.getTime();
            Log.d(TAG, d.toString());
            db.collection("Complaints").document(docid).update("Completed", true);
            db.collection("Complaints").document(docid).update("Decision", "Suspended (Temporary)");
            db.collection("Tutors").document(tutorid).update("SuspendTime", d);
            db.collection("Tutors").document(tutorid).update("Suspended", true);
            finish();
        };

        datepicker.setOnDateSetListener(DatePickerListener);

        db.collection("Complaints").document(docid)
                .get().addOnCompleteListener(task -> {
                    if(task.isSuccessful()){
                        DocumentSnapshot document = task.getResult();
                        if(document.exists()){
                            desc.append(" " + document.getString("complaintDesc"));
                            tutorid = document.getString("TutorID");
                            Log.d(TAG, tutorid);
                            getTutorName(docid);
                        }
                    }
                });

        suspend_temp.setOnClickListener(v -> {
            Log.d(TAG, tutorid);
            datepicker.show();
        });

        suspend_perm.setOnClickListener(v -> {
            Log.d(TAG, tutorid);
            db.collection("Complaints").document(docid).update("Completed", true);
            db.collection("Complaints").document(docid).update("Decision", "Suspended (Permanent)");
            db.collection("Tutors").document(tutorid).update("Suspended", true);
            finish();
        });

        dismiss.setOnClickListener(v -> {
            Log.d(TAG, tutorid);
            db.collection("Complaints").document(docid).update("Completed", true);
            db.collection("Complaints").document(docid).update("Decision", "Dismissed");
            finish();
        });

        back.setOnClickListener(v -> finish());
    }
    public void getTutorName(String complaintID){
        db.collection("Complaints").document(complaintID).get().addOnCompleteListener(task -> {
            if(task.isSuccessful()){
                DocumentSnapshot document = task.getResult();
                Log.d(TAG, "document success");
                if(document.exists()){
                    Log.d(TAG, String.valueOf(document.getString("firstName")));
                    fullname.append(" " + document.getString("TutorFirstName") + " " + document.getString("TutorLastName"));
                }
            }
        });
    }
}