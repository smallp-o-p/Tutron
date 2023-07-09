package com.example.tutron;

import static android.content.ContentValues.TAG;

import android.app.AlertDialog;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

public class EditTopics extends AppCompatActivity {

    TextView offered_topics_dropdown, profile_topics_dropdown;
    ImageButton back;
    FirebaseFirestore db;
    FirebaseUser usr;
    Button add_profile_topics, add_offered_topics;
    ArrayList<String> topic_ids = new ArrayList<>();
    ArrayList<Integer> selected_topics = new ArrayList<>();
    ArrayList<Topic> topic_list = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_topics);

        db = FirebaseFirestore.getInstance();
        usr = FirebaseAuth.getInstance().getCurrentUser();

        profile_topics_dropdown = findViewById(R.id.profile_topics_dropdown);

        offered_topics_dropdown = findViewById(R.id.offered_topics_dropdown);

        add_profile_topics = findViewById(R.id.add_profile_topic);

        add_offered_topics = findViewById(R.id.add_offered_topic);

        back = findViewById(R.id.tutor_edit_back);

        CollectionReference coll = db.collection("Tutors").document(usr.getUid()).collection("profile_topics");

        coll.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                for(QueryDocumentSnapshot document : task.getResult()){
                    if(document.exists()){
                        topic_list.add(document.toObject(Topic.class));
                        topic_ids.add(document.getId());
                        Log.d(TAG, document.toObject(Topic.class).getName());
                    }
                }
                BuildTopics(topic_list);
                BuildOfferedTopics(topic_list);
            } else {
                Log.d(TAG, "get failed with ", task.getException());
            }
        });

        add_profile_topics.setOnClickListener(v -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(EditTopics.this);
            builder.setTitle("Add Topic To Your Profile");

            LinearLayout layout = new LinearLayout(EditTopics.this);
            layout.setOrientation(LinearLayout.VERTICAL);

            final EditText topic_name = new EditText(EditTopics.this);
            topic_name.setInputType(InputType.TYPE_CLASS_TEXT);
            topic_name.setHint("Add Topic");

            final EditText exp_details = new EditText(EditTopics.this);
            exp_details.setInputType(InputType.TYPE_CLASS_TEXT);
            exp_details.setHint("Tell us about your experience.");

            final EditText exp_years = new EditText(EditTopics.this);
            exp_years.setInputType(InputType.TYPE_CLASS_NUMBER);
            exp_years.setHint("Years of Experience");

            layout.addView(topic_name);
            layout.addView(exp_years);
            layout.addView(exp_details);

            builder.setView(layout);
            builder.setPositiveButton(R.string.add_topic, (dialog, which) -> {
                AddTopic(topic_name.getText(), exp_years.getText(), exp_details.getText());
            });
            builder.setNegativeButton(R.string.cancel, (dialog, which) -> dialog.dismiss());
            builder.show();
        });
        back.setOnClickListener(v -> finish());
    }
    public void BuildTopics(List<Topic> topics){
        ArrayList<String> user_viewable_profile = new ArrayList<>();
        for(Topic t: topics){
            user_viewable_profile.add(t.getName());
        }
        boolean[] bool1 = new boolean[user_viewable_profile.size()];
        profile_topics_dropdown.setOnClickListener(v -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(EditTopics.this);

            builder.setTitle("Remove Topics/Add To Offered");
            Log.d(TAG, "click");

            builder.setMultiChoiceItems(user_viewable_profile.toArray(new String[0]), bool1, (dialog, which, isChecked) -> {
                bool1[which] = isChecked;
            });
            builder.setPositiveButton(R.string.add_offered_topics, (dialog, which) -> {
                for(int i = 0; i<bool1.length; i++){
                    if(bool1[i]){
                        db.collection("Tutors").document(usr.getUid()).collection("profile_topics").document(topic_ids.get(i)).update("offered", true);
                    }
                }
                recreate(); // reset view so the updated list is shown
            });

            builder.setNeutralButton(R.string.cancel, (dialog, which) -> {
                dialog.dismiss();
            });
            builder.setNegativeButton(R.string.remove, (dialog, which) -> {
               Log.d(TAG, "remove");
               for(Integer i: selected_topics){
                   db.collection("Tutors").document(usr.getUid()).collection("profile_topics").document(topic_ids.get(i)).delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                       @Override
                       public void onComplete(@NonNull Task<Void> task) {
                           if(task.isSuccessful()){
                               Log.d(TAG, "Deleted!");
                           }
                       }
                   });
               }
            });
            builder.show();
        });
    }

    public void BuildOfferedTopics(List<Topic> topics){
        ArrayList<String> user_viewable_offered = new ArrayList<>();
        for(Topic t: topics){
            if(t.isOffered()){
                user_viewable_offered.add(t.getName());
            }
        }
        boolean[] bool1 = new boolean[user_viewable_offered.size()];
        offered_topics_dropdown.setOnClickListener(v -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(EditTopics.this);
            builder.setTitle("Remove Offered Topics");

            builder.setMultiChoiceItems(user_viewable_offered.toArray(new String[0]), bool1, (dialog, which, isChecked) -> {
               bool1[which] = isChecked;
            });

            builder.setPositiveButton(R.string.remove, (dialog, which) -> {
                for(int i = 0; i<bool1.length; i++){
                    db.collection("Tutors").document(usr.getUid()).collection("profile_topics").document(topic_ids.get(i)).update("offered", false);
                }
                recreate();
            });
            builder.setNegativeButton(R.string.cancel, (dialog, which) -> {
               dialog.dismiss();
            });
            builder.show();
        });
    }

    public void AddTopic(Editable name, Editable exp_yr, Editable exp_desc){
        Topic newTopic = new Topic(name.toString(), exp_desc.toString(), Integer.parseInt(exp_yr.toString()), false);
        db.collection("Tutors").document(usr.getUid()).collection("profile_topics").add(newTopic);
        recreate();
    }


}
