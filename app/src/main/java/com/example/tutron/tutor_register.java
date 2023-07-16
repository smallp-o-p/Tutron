package com.example.tutron;

import static android.content.ContentValues.TAG;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class tutor_register extends AppCompatActivity {
    Bundle extras;
    FirebaseFirestore db;
    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tutor_register);

        List<String> empty = Collections.emptyList();

        extras = getIntent().getExtras();

        mAuth = FirebaseAuth.getInstance();

        db = FirebaseFirestore.getInstance();

        Spinner educationspinner = findViewById(R.id.education_level);
        Spinner languagespinner = findViewById(R.id.native_lang); //spinner stuff

        ArrayAdapter<CharSequence> ad_educ = ArrayAdapter.createFromResource(this, R.array.education_levels, android.R.layout.simple_spinner_item);
        ad_educ.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        ArrayAdapter<CharSequence> ad_lang = ArrayAdapter.createFromResource(this, R.array.languages, android.R.layout.simple_spinner_item);
        ad_lang.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        educationspinner.setAdapter(ad_educ);
        languagespinner.setAdapter(ad_lang);

        Button submit = findViewById(R.id.submit_tutor);
        TextInputEditText description = findViewById(R.id.tutor_profile_desc);

        submit.setOnClickListener(v -> {

            String desc = String.valueOf(description.getText());

            String educ = educationspinner.getSelectedItem().toString();
            String lang = languagespinner.getSelectedItem().toString();

            if(TextUtils.isEmpty(desc)){
                Toast.makeText(getApplicationContext(), "Profile description required.", Toast.LENGTH_SHORT).show();
                return;
            }

            if(desc.trim().split("\\s+").length > 600){
                Toast.makeText(getApplicationContext(), "Profile description is too long.", Toast.LENGTH_SHORT).show();
                return;
            }

            Tutor newTutor = new Tutor(extras.get("firstname").toString(), extras.get("lastname").toString(), lang, educ, desc, false, null, -1, 15.50);

            mAuth.createUserWithEmailAndPassword(extras.getString("email"), extras.getString("password"))
                    .addOnCompleteListener(this, task -> {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "createUserWithEmail:success");
                            GoToNext(newTutor);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "createUserWithEmail:failure", task.getException());
                            Toast.makeText(getApplicationContext(), "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                            finish();
                        }
                    });
        });
    }
    public void GoToNext(Tutor tutor){
        Map<String, Object> ouruser = new HashMap<>();
        ouruser.put("Email", extras.getString("email"));
        ouruser.put("Type", "Tutor");
        db.collection("Users").document(Objects.requireNonNull(mAuth.getCurrentUser()).getUid()).set(ouruser);
        db.collection("Tutors").document(mAuth.getCurrentUser().getUid()).set(tutor);
        db.collection("Tutors").document(mAuth.getCurrentUser().getUid()).update("Suspended", false);
        Intent intent = new Intent(tutor_register.this, MainActivity.class);
        intent.putExtra("user", mAuth.getCurrentUser());
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }


}