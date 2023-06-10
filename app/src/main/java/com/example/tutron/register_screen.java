package com.example.tutron;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;

public class register_screen extends AppCompatActivity {

    private ActivityResultLauncher<Intent> launcher;
    private TextInputEditText EmailAddr, Pwd, ConfirmPwd;
    private Button RegisterBtn;

    private ImageButton BackBtn;
    private RadioButton studentRadio, tutorRadio;



    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_screen);

        EmailAddr = findViewById(R.id.email_register);

        Pwd = findViewById(R.id.pwd_register);

        ConfirmPwd =findViewById(R.id.confirm_pwd_register);

        RegisterBtn =findViewById(R.id.registerbtn);

        BackBtn = findViewById(R.id.back_arrow);

        studentRadio = findViewById(R.id.studentRadio);

        tutorRadio = findViewById(R.id.tutorRadio);

        BackBtn.setOnClickListener(v -> {
            Intent i = new Intent(register_screen.this, login_screen.class);
            startActivity(i);
        });

        RegisterBtn.setOnClickListener(v -> {

            Log.d("STATE", "Button pressed.");
            String email, password, confirm;

            email = String.valueOf(EmailAddr.getText());

            password= String.valueOf(Pwd.getText());

            confirm = String.valueOf(ConfirmPwd.getText());

            if(TextUtils.isEmpty(email)){
                Toast.makeText(register_screen.this,
                        "Missing email",
                        Toast.LENGTH_SHORT).show();
            }

            if(TextUtils.isEmpty(password)){
                Toast.makeText(register_screen.this,
                        "Missing password",
                        Toast.LENGTH_SHORT).show();
                return;
            }

            if(TextUtils.isEmpty(confirm)){
                Toast.makeText(register_screen.this,
                        "Missing confirm",
                        Toast.LENGTH_SHORT).show();
                return;
            }

            if(!password.equals(confirm)){
                Toast.makeText(register_screen.this,
                        "Password and confirm not equal",
                        Toast.LENGTH_SHORT).show();
                return;
            }

            Log.d("STATE", "Validation passed.");

            GoToNext(email, password);
        });
    }
    /*Stores details in Firestore */
    /**TODO: bring user to the extra details they need to fill out to finally register, then actually register them there.  */
    public void GoToNext(String email, String password){

        if(studentRadio.isChecked()){
            Intent intent = new Intent(register_screen.this, student_register.class);
            intent.putExtra("email", email);
            intent.putExtra("password", password);
            startActivity(intent);
        }
    }

}