package com.example.tutron;

import static android.content.ContentValues.TAG;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
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

public class register_screen extends AppCompatActivity {

    private ActivityResultLauncher<Intent> launcher;
    private TextInputEditText EmailAddr, Pwd, ConfirmPwd;
    private Button RegisterBtn;
    private FirebaseAuth mAuth;
    private RadioButton studentRadio, tutorRadio;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_screen);

        EmailAddr = findViewById(R.id.email_register);

        Pwd = findViewById(R.id.pwd_register);

        ConfirmPwd =findViewById(R.id.confirm_pwd_register);

        RegisterBtn =findViewById(R.id.registerbtn);

        studentRadio = findViewById(R.id.studentRadio);

        tutorRadio = findViewById(R.id.tutorRadio);

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