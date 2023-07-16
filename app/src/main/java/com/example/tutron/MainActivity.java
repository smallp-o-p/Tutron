package com.example.tutron;

import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {
    FirebaseAuth mAuth;
    Button logout, complaint, search, view_requests;
    TextView welcome;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAuth = FirebaseAuth.getInstance();

        logout = findViewById(R.id.logoutbtn);

        complaint = findViewById(R.id.complaint);

        search = findViewById(R.id.search_button);

        view_requests = findViewById(R.id.view_purchase_requests_button);

        welcome = findViewById(R.id.student_welcome);

        view_requests.setOnClickListener(v -> {
            Intent gotorequests = new Intent(MainActivity.this, StudentRequests.class);
            startActivity(gotorequests);
        });


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

        complaint.setOnClickListener(v -> {
            Intent gotocomplaint = new Intent(MainActivity.this, SubmitComplaint.class);
            startActivity(gotocomplaint);
        });

        logout.setOnClickListener(v -> {
            mAuth.signOut();
            finish();
        });

    }
}