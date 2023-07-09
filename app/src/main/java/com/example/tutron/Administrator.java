package com.example.tutron;

import static android.content.ContentValues.TAG;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;

public class Administrator extends AppCompatActivity {

    private ListView complaintListView;
    private FirebaseFirestore db;
    private FirebaseAuth mAuth;
    private final ArrayList<String> list = new ArrayList<>();
    private ArrayAdapter<String> arrayAdapter;

    private Button logout_btn;

    @Override
    protected void onRestart(){ // refresh the complaints view
        super.onRestart();
        recreate();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_administrator);

        complaintListView = findViewById(R.id.complaintList);
        logout_btn = findViewById(R.id.logout_admin);
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        if(mAuth.getCurrentUser() != null){
            GenerateComplaints();
            Log.d(TAG, String.valueOf(list.size()));

            complaintListView.setOnItemClickListener((parent, view, position, id) -> {
                String docid = complaintListView.getItemAtPosition(position).toString();
                Intent vc = new Intent(Administrator.this, ViewComplaint.class);
                vc.putExtra("docid", docid);
                startActivity(vc);
            });
        }
        logout_btn.setOnClickListener(v -> {
            Intent gotologin = new Intent(Administrator.this, login_screen.class);
            gotologin.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(gotologin);
        });
    }
    public void GenerateComplaints(){ // populate the complaintlistview
        list.clear();
        CollectionReference complaints = db.collection("Complaints");
        complaints.get().addOnCompleteListener(task -> {
            if(task.isSuccessful()) {
                for (QueryDocumentSnapshot document : task.getResult()) {
                    if(!document.getBoolean("Completed")){
                        list.add(document.getId());
                    }
                }
            }
            arrayAdapter = new ArrayAdapter<>(Administrator.this, R.layout.simple_list_item_1, list);
            complaintListView.setAdapter(arrayAdapter);
        });
    }

}

