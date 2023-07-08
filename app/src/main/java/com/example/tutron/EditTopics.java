package com.example.tutron;

import static android.content.ContentValues.TAG;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.InputType;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;

public class EditTopics extends AppCompatActivity {

    TextView offered_topics_dropdown, profile_topics_dropdown;

    ImageButton back;
    FirebaseFirestore db;
    FirebaseAuth mAuth;
    FirebaseUser usr;

    Button add_profile_topics, add_offered_topics;
    ArrayList<Integer> selected_topics = new ArrayList<>();
    ArrayList<Integer> selected_offered_topics = new ArrayList<>();
    ArrayList<String> temp1, temp2;

    String[] profile_topics, offered_topics;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_topics);

        db = FirebaseFirestore.getInstance();

        usr = FirebaseAuth.getInstance().getCurrentUser();

        temp1 = new ArrayList<>();

        temp2 = new ArrayList<>();

        profile_topics_dropdown = findViewById(R.id.profile_topics_dropdown);

        offered_topics_dropdown = findViewById(R.id.offered_topics_dropdown);

        add_profile_topics = findViewById(R.id.add_profile_topic);

        add_offered_topics = findViewById(R.id.add_offered_topic);

        DocumentReference doc = db.collection("Tutors").document(usr.getUid());
        doc.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                DocumentSnapshot document = task.getResult();
                if (document.exists()) {
                    Log.d(TAG, "DocumentSnapshot data: " + document.getData());
                    temp1 = (ArrayList<String>) document.get("profile_topics");
                    temp2 = (ArrayList<String>) document.get("offered_topics");
                    profile_topics = temp1.toArray(new String[0]);
                    offered_topics = temp2.toArray(new String[0]);
                    BuildProfileTopics(profile_topics);
                    BuildOfferedTopics(offered_topics);
                    BuildOfferedAdd(profile_topics, offered_topics);
                }
            } else {
                Log.d(TAG, "get failed with ", task.getException());
            }
        });

        add_profile_topics.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(EditTopics.this);
                builder.setTitle("Add Topic To Your Profile");

                final EditText input = new EditText(EditTopics.this);
                input.setInputType(InputType.TYPE_CLASS_TEXT);
                builder.setView(input);
                builder.setPositiveButton(R.string.add_topic, (dialog, which) -> {
                    if(TextUtils.isEmpty(input.getText())){
                        Toast.makeText(EditTopics.this, "Field cannot be empty.", Toast.LENGTH_SHORT).show();
                    }
                    else{
                        doc.update("profile_topics", FieldValue.arrayUnion(input.getText().toString()));
                        recreate();
                    }
                });
                builder.setNegativeButton(R.string.cancel, (dialog, which) -> dialog.dismiss());
                builder.show();
            }
        });

    }
    public void BuildOfferedAdd(String[] profile_topics, String[] offered_topics){
        add_offered_topics.setOnClickListener(v -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(EditTopics.this);
            builder.setTitle("Add To Your Offered Topics");
            ArrayList<String> addable = new ArrayList<>();
            ArrayList<Integer> toAdd = new ArrayList<>();

            for(String topic : profile_topics){
                if(!Arrays.asList(offered_topics).contains(topic)){
                    addable.add(topic);
                }
                else{
                    continue;
                }
            }

            String[] end = addable.toArray(new String[0]);
            boolean[] bool = new boolean[end.length];
            builder.setMultiChoiceItems(end, bool, (dialog, which, isChecked) -> {
                    if(isChecked){
                        toAdd.add(which);
                    }
                    else{
                        toAdd.remove(which);
                    }
            });

            builder.setPositiveButton(R.string.add_topic_s, (dialog, which) -> {
                for(Integer i: toAdd){
                    db.collection("Tutors").document(usr.getUid()).update("offered_topics", FieldValue.arrayUnion(end[i]));
                }
                recreate();
            });
            builder.setNegativeButton(R.string.cancel, (dialog, which) -> dialog.dismiss());
            builder.show();
        });
    }
    public void BuildProfileTopics(String[] profile_topics){ // so i remembered firestore is done asynchronously...

        boolean[] bool1 = new boolean[profile_topics.length];
        profile_topics_dropdown.setOnClickListener(v -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(EditTopics.this);

            builder.setTitle("Remove Topics");
            Log.d(TAG, "click");

            builder.setMultiChoiceItems(profile_topics, bool1, (dialog, which, isChecked) -> {
                if(isChecked){
                    selected_topics.add(which);
                }
                else{
                    selected_topics.remove(which);
                }
            });
            builder.setPositiveButton(R.string.add_offered_topics, (dialog, which) -> {
                for(Integer i: selected_topics){
                    db.collection("Tutors").document(usr.getUid()).update("offered_topics", FieldValue.arrayUnion(profile_topics[i]));
                }
                recreate(); // reset view so the updated list is shown
            });
            builder.setNeutralButton(R.string.cancel, (dialog, which) -> dialog.dismiss()); // it does not matter, all i care about is the positioning of the button

            builder.setNegativeButton(R.string.remove, (dialog, which) -> {
                Log.d(TAG, "remove button clicked");
                for(Integer i: selected_topics){ // iterate through selected indexes
                    RemoveTopic(profile_topics[i], true);
                }
                recreate();
            });
            builder.show();
        });

    }
    public void BuildOfferedTopics(String[] offered_topics){
        boolean[] bool2 = new boolean[offered_topics.length];

        offered_topics_dropdown.setOnClickListener(v -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(EditTopics.this);

            builder.setTitle("Remove Offered Topics");

            builder.setMultiChoiceItems(offered_topics, bool2, (dialog, which, isChecked) -> {
                if(isChecked){
                    selected_offered_topics.add(which);
                }
                else{
                    selected_offered_topics.remove(which);
                }
            });
            builder.setPositiveButton(R.string.remove, (dialog, which) -> {
                for(Integer i: selected_offered_topics){ // iterate through selected indexes
                    RemoveTopic(offered_topics[i], false);
                }
                recreate();
            });
            builder.setNegativeButton(R.string.cancel, (dialog, which) -> dialog.dismiss());
            builder.show();
        });
    }

    public void RemoveTopic(String rm, boolean profile){
        if(profile){ // you have to remove offered topics too if you're removing from profile topics
            db.collection("Tutors").document(usr.getUid()).update("profile_topics", FieldValue.arrayRemove(rm));
            db.collection("Tutors").document(usr.getUid()).update("offered_topics", FieldValue.arrayRemove(rm));
        }
        else{ // remove offered topics only
            db.collection("Tutors").document(usr.getUid()).update("offered_topics", FieldValue.arrayRemove(rm));
        }
    }
}
