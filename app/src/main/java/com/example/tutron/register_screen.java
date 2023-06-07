package com.example.tutron;

import static android.content.ContentValues.TAG;

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
    private TextInputEditText EmailAddr, Pwd, ConfirmPwd;
    private Button RegisterBtn;
    private FirebaseAuth mAuth;
    private RadioButton studentRadio, tutorRadio;

    FirebaseFirestore db;

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

        db = FirebaseFirestore.getInstance();

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

            mAuth = FirebaseAuth.getInstance();

            Log.d("STATE", "mAuth created");

            if(mAuth.getCurrentUser() != null){
                Log.d("STATE", mAuth.getCurrentUser().getUid());

                mAuth.signOut(); // the most recently created user is remembered by mAuth, have no idea why
            }
            /* Move this elsewhere, this is a decent demo of how it functions */
            mAuth.createUserWithEmailAndPassword(email, password) // the register process, user is automatically logged in
                    .addOnCompleteListener(register_screen.this, task -> {
                        if(task.isSuccessful()){
                            Log.d(TAG, "createUserWithEmail:success");
                            GoToNext();
                        }
                        else{
                            Log.d("STATE", "user create failed");
                            Log.w(TAG, "createUserWithEmail:failure", task.getException());
                            Toast.makeText(register_screen.this,
                                    "fail :(",
                                    Toast.LENGTH_SHORT).show();
                        }
                    });
        });
    }
    /*Stores details in Firestore */
    /**TODO: bring user to the extra details they need to fill out to finally register, then actually register them there.  */
    public void GoToNext(){

        FirebaseUser user = mAuth.getCurrentUser();
        Map<String, Object> data = new HashMap<>();

        String uuid = user.getUid();
        String email = user.getEmail();

        String type;
        data.put("Email", user.getEmail());
        if(studentRadio.isChecked()){
            type = "Student";
            data.put("Type", type);
        }
        else{
            type = "Tutor";
            data.put("Type", type);
        }
        db.collection("Users").document(uuid).set(data);
    }

}