package com.example.firebasetutorial;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class RegisterActivity extends AppCompatActivity {

    EditText mFullName, mEmail, mPassword, mPhone;
    Button mRegisterButton;
    TextView mLoginButton;
    FirebaseAuth fAuth;
    ProgressBar progressBar;
    FirebaseFirestore fStore;
    String userID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register);
        setTitle("Register Account");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mFullName = findViewById(R.id.fullName);
        mEmail = findViewById(R.id.email);
        mPassword = findViewById(R.id.password);
        mPhone = findViewById(R.id.phone);
        mRegisterButton = findViewById(R.id.registerConfirm);
        mLoginButton = findViewById(R.id.backToLogin);


        fAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();

        //if there is already a user logged in from previous time. Send straight to main menu
        if(fAuth.getCurrentUser() != null){
            startActivity(new Intent(getApplicationContext(), MainMenu.class));
            finish();
        }
        //gets all information in text boxes
       mRegisterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = mEmail.getText().toString().trim();
                String password = mPassword.getText().toString().trim();
                String fullName = mFullName.getText().toString();
                String phone = mPhone.getText().toString();

                //validation
                if(TextUtils.isEmpty(email)){
                    mEmail.setError("Email is not valid");
                    return;
                }
                if(TextUtils.isEmpty(password)){
                    mPassword.setError("Password is not valid");
                    return;

                }
                if(password.length() < 6){
                    mPassword.setError("Password has to be 6 chars minimum");
                    return;
                }

                //register user.

                fAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            Toast.makeText(RegisterActivity.this, "User Created and Logged in", Toast.LENGTH_SHORT).show();
                            //Store User Data
                            userID = fAuth.getCurrentUser().getUid();
                            DocumentReference documentReference = fStore.collection("users").document(userID);
                            Map<String, Object> user = new HashMap<>();
                            user.put("fName", fullName);
                            user.put("email", email);
                            user.put("phone", phone);
                            documentReference.set(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void unused) {
                                    Log.d(TAG,"User Info is stored for" + userID);
                                }
                            });
                            //sending to main menu
                            startActivity(new Intent(getApplicationContext(), MainMenu.class));
                        }
                        else{
                            Toast.makeText(RegisterActivity.this, "Error Registering", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });
    }
    public void backButton(View view){
        startActivity(new Intent(getApplicationContext(), Login.class));
    }
}