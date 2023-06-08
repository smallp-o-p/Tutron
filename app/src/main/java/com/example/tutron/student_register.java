package com.example.tutron;

import static android.content.ContentValues.TAG;

import androidx.activity.result.ActivityResultCaller;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import org.w3c.dom.Text;

import java.util.HashMap;
import java.util.Map;

public class student_register extends AppCompatActivity {
    FirebaseFirestore db;
    Button submitbtn;

    TextInputEditText address, creditnum, expm, expyear, cvv;

    FirebaseAuth mAuth;

    Bundle extras;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_register);

        extras = getIntent().getExtras();

        submitbtn = findViewById(R.id.submit);

        address = findViewById(R.id.address_form);

        creditnum = findViewById(R.id.credit_form);

        expm = findViewById(R.id.expm);

        expyear = findViewById(R.id.expy);

        cvv = findViewById(R.id.cvv);

        db = FirebaseFirestore.getInstance();

        submitbtn.setOnClickListener(v -> {
            String address_ = String.valueOf(address.getText());

            String creditnum_ = String.valueOf(creditnum.getText());

            String expm2 = String.valueOf(expm);

            String expy2 = String.valueOf(expyear);

            String cvv2 = String.valueOf(cvv);

            if(TextUtils.isEmpty(address_) || TextUtils.isEmpty(creditnum_) || TextUtils.isEmpty(expm2) || TextUtils.isEmpty(expy2)){
                Toast.makeText(student_register.this, "missing field :(", Toast.LENGTH_SHORT).show();
                return;
            }

            mAuth = FirebaseAuth.getInstance();

            Student newStudent = new Student(address_, creditnum_, cvv2, expm2, expy2);

            mAuth.createUserWithEmailAndPassword(extras.getString("email"), extras.getString("password"))
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                           Log.d(TAG, "createUserWithEmail:success");
                           GoToNext(newStudent);
                        }
                    });

        });
    }
    public void GoToNext(Student student ){

        Map<String, Object> ouruser = new HashMap<>();
        ouruser.put("Email", extras.getString("email"));
        ouruser.put("Type", "Student");
        db.collection("Users").document(mAuth.getCurrentUser().getUid()).set(ouruser);

        db.collection("Students").document(mAuth.getCurrentUser().getUid()).set(student);

        Intent intent = new Intent(student_register.this, MainActivity.class);
        intent.putExtra("user", mAuth.getCurrentUser());
        startActivity(intent);

    }
}