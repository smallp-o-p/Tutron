package com.example.tutron;

import static android.content.ContentValues.TAG;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.renderscript.ScriptGroup;
import android.text.InputType;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class MainActivity extends AppCompatActivity {
    FirebaseAuth mAuth;
    FirebaseFirestore db;
    Button logout, edit, rate, search, view_requests;
    TextView welcome;
    String type;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAuth = FirebaseAuth.getInstance();

        logout = findViewById(R.id.logoutbtn);

        edit = findViewById(R.id.editprofile);

        rate = findViewById(R.id.rate_a_tutor);

        search = findViewById(R.id.search_button);

        view_requests = findViewById(R.id.view_purchase_requests_button);

        welcome = findViewById(R.id.student_welcome);

        search.setOnClickListener(v -> {

            AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
            builder.setTitle("Search for a Tutor");
            builder.setMessage("You may fill in more than one text box to get the most specific results. Leaving every text input empty will query all tutors.");

            LinearLayout layout = new LinearLayout(MainActivity.this);
            layout.setOrientation(LinearLayout.VERTICAL);

            final EditText tutor_name = new EditText(MainActivity.this);
            tutor_name.setInputType(InputType.TYPE_CLASS_TEXT);
            tutor_name.setHint("Search by Tutor's First Name");

            final EditText tutor_lang = new EditText(MainActivity.this);
            tutor_lang.setInputType(InputType.TYPE_CLASS_TEXT);
            tutor_lang.setHint("Language (Default: English)");

            final EditText tutor_topic = new EditText(MainActivity.this);
            tutor_topic.setInputType(InputType.TYPE_CLASS_TEXT);
            tutor_topic.setHint("Search by Specific Topic (Optional)");

            layout.addView(tutor_name);
            layout.addView(tutor_lang);
            layout.addView(tutor_topic);

            builder.setView(layout);
            builder.setPositiveButton(R.string.search, (dialog, which) -> {
                Intent search_intent = new Intent(MainActivity.this, SearchTutors.class);
                Bundle search_terms = new Bundle();
                search_terms.putCharSequence("tutor_name", tutor_name.getText());
                search_terms.putCharSequence("tutor_topic", tutor_topic.getText());
                search_terms.putCharSequence("tutor_lang", tutor_lang.getText());
                search_intent.putExtras(search_terms);
                startActivity(search_intent);
            });
            builder.setNegativeButton(R.string.cancel, (dialog, which) -> {
               dialog.dismiss();
            });

            builder.show();
        });

        logout.setOnClickListener(v -> {
            mAuth.signOut();
            Intent gotologin = new Intent(MainActivity.this, login_screen.class);
            gotologin.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(gotologin);
        });

        edit.setOnClickListener(v -> {
            if(type.equals("Student")){
                ;
            }
            if(type.equals("Tutor")){
                Intent TutorIntent = new Intent(MainActivity.this, TutorProfile.class);
                TutorIntent.putExtra("id", mAuth.getCurrentUser().getUid());
                startActivity(TutorIntent);
            }
        });
    }
}