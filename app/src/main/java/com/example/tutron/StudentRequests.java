package com.example.tutron;

import static android.content.ContentValues.TAG;

import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class StudentRequests extends AppCompatActivity {

    // not actioned: 0
    // approved : 1
    // rejected : -1

    ImageButton back_to_profile;
    FirebaseAuth mAuth;
    FirebaseFirestore db;
    ArrayList<Request> rqs = new ArrayList<>();
    ListView list;
    ArrayList<String> students = new ArrayList<>();
    ArrayList<String> rqs_id = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_requests);

        mAuth = FirebaseAuth.getInstance();

        db = FirebaseFirestore.getInstance();

        back_to_profile = findViewById(R.id.students_requests_back);

        list = findViewById(R.id.listView_purchase_requests);

        if (mAuth.getCurrentUser() != null) {
            String currentStudentID = mAuth.getCurrentUser().getUid();

            db.collectionGroup("requests")
                    .whereEqualTo("studentID", currentStudentID)
                    .get()
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            QuerySnapshot docs = task.getResult();
                            for (QueryDocumentSnapshot doc : docs) {
                                Request request = doc.toObject(Request.class);
                                rqs.add(request);
                                rqs_id.add(doc.getId());
                            }
                            Log.d(TAG, "Number of requests: " + rqs.size());
                            BuildList();

                        } else {
                            Log.d(TAG, "failed :(", task.getException());
                        }
                    });
        }

        back_to_profile.setOnClickListener(v -> finish());

    }

    public void BuildList() {

        ArrayList<Task<?>> tasks = new ArrayList<>();
        students.clear();

        for(Request r : rqs){
            Task<DocumentSnapshot> task = db.collection("Tutors").document(r.getTutorID()).get();
            tasks.add(task);
        }
        Tasks.whenAllComplete(tasks).addOnSuccessListener(new OnSuccessListener<List<Task<?>>>() {
            @Override
            public void onSuccess(List<Task<?>> tasks) {
                int count = 0;
                for(Task<?> t: tasks){
                    if(t.isSuccessful()){
                        DocumentSnapshot d = (DocumentSnapshot) t.getResult();
                        students.add(d.getString("firstName") + " " + d.getString("lastName") +": " + rqs.get(count++).getTopicName());
                    }
                }
                ArrayAdapter<String> adapter = new ArrayAdapter<>(StudentRequests.this, R.layout.simple_list_item_1, students);
                list.setAdapter(adapter);
            }
        });

        list.setOnItemClickListener((parent, view, position, id) -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(StudentRequests.this);
            builder.setTitle("Rate Tutor");

            // Inflate the custom layout for the AlertDialog
            View viewInflated = LayoutInflater.from(StudentRequests.this).inflate(R.layout.dialog_rate_tutor, null);
            builder.setView(viewInflated);

            EditText ratingEditText = viewInflated.findViewById(R.id.editText_rating);
            ratingEditText.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
            EditText reviewEditText = viewInflated.findViewById(R.id.editText_review);
            CheckBox anonymous_check = viewInflated.findViewById(R.id.anonymous_check);

            builder.setPositiveButton("Rate", (dialog, which) -> {
                String ratingStr = ratingEditText.getText().toString().trim();
                String review = reviewEditText.getText().toString().trim();
                Query query = db.collection("Tutors").document(rqs.get(position)
                                .getTutorID()).collection("ratings")
                        .whereEqualTo("studentID", mAuth.getCurrentUser().getUid())
                        .whereEqualTo("topicName", rqs.get(position).getTopicName())
                        .whereEqualTo("tutorID", rqs.get(position).getTutorID());
                    if (!ratingStr.isEmpty() && !review.isEmpty()) {
                        double rating = Double.parseDouble(ratingStr);
                        if (rating >= 1 && rating <= 5) {
                            String currentStudentID = mAuth.getCurrentUser().getUid();

                            int selectedPosition = position;
                            if (selectedPosition != ListView.INVALID_POSITION) {
                                Request selectedRequest = rqs.get(selectedPosition);
                                int status = selectedRequest.getStatus();
                                if (status == 1) {
                                    // Request is approved, proceed with the rating
                                    String tutorID = selectedRequest.getTutorID(); // Get the tutor ID from the selected request
                                    String topicName = selectedRequest.getTopicName(); // Get the topic name from the selected request
                                    //String requestId = rqs_id.get(selectedPosition); // Get the request ID from the list

                                    DocumentReference tutorRef = db.collection("Tutors").document(tutorID);

                                    // Create a new document in the "ratings" subcollection with the given rating ID
                                    DocumentReference ratingRef = tutorRef.collection("ratings").document(currentStudentID);
                                    SimpleDateFormat formatter = new SimpleDateFormat();
                                    Date date = new Date();
                                    // Set the data for the new rating document
                                    String name;
                                    if(anonymous_check.isChecked()){
                                        name = "Anonymous";
                                    }
                                    else{
                                        name = rqs.get(position).getStudentFirstName() + " " + rqs.get(position).getStudentLastName();
                                    }
                                    Rating newreview = new Rating(rating, review, topicName, name, date, currentStudentID, tutorID);

                                    ratingRef.set(newreview)
                                            .addOnSuccessListener(aVoid -> {
                                                Toast.makeText(StudentRequests.this, "Tutor rated successfully!", Toast.LENGTH_SHORT).show();
                                            })
                                            .addOnFailureListener(e -> {
                                                Toast.makeText(StudentRequests.this, "Failed to rate tutor. Please try again.", Toast.LENGTH_SHORT).show();
                                            });
                                } else {
                                    Toast.makeText(StudentRequests.this, "You can only rate an approved request.", Toast.LENGTH_SHORT).show();
                                }
                            } else {
                                Toast.makeText(StudentRequests.this, "Please select a tutor request to rate.", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(StudentRequests.this, "Please enter a rating between 1 and 5.", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(StudentRequests.this, "Please fill out all fields.", Toast.LENGTH_SHORT).show();
                    }
            });

            builder.setNegativeButton("Cancel", (dialog, which) -> {
                dialog.dismiss();
            });

            builder.show();
        });

        list.setOnItemLongClickListener((parent, view, position, id) -> {

                AlertDialog.Builder builder = new AlertDialog.Builder(StudentRequests.this);

                builder.setTitle("Info about this request:");

                Date date = rqs.get(position).getRequestedDate();
                String tutorID = rqs.get(position).getTutorID();

                SimpleDateFormat DateFor = new SimpleDateFormat("dd/MM/yyyy");
                String stringDate = DateFor.format(date);

                String statusText;
                int status = rqs.get(position).getStatus();
                if (status == 0) {
                    statusText = "Status: Pending";
                } else if (status == -1) {
                    statusText = "Status: Not Approved";
                } else if (status == 1) {
                    statusText = "Status: Approved";
                } else {
                    statusText = "Status: Unknown";
                }

                //still need to get and enter tutor name associated with student  requests
                builder.setMessage("Topic: " + rqs.get(position).getTopicName() + "\n" + "Requested Date: " + stringDate + "\n" + statusText);

                builder.setNeutralButton("Cancel", (dialog, which) -> {
                    dialog.dismiss();
                });

                builder.show();
                return true;

            });


        }


    }
