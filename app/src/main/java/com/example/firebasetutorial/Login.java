package com.example.firebasetutorial;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;


public class Login extends AppCompatActivity {


    FirebaseFirestore db = FirebaseFirestore.getInstance();
    private FirebaseAuth mAuth;
    Button forget_password_btn;
    AlertDialog.Builder reset_alert;
    LayoutInflater inflater;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setTitle("Login");
        mAuth = FirebaseAuth.getInstance();

        //if there is already a user logged in from previous time. Send straight to main menu
        if(mAuth.getCurrentUser() != null){
            startActivity(new Intent(getApplicationContext(), MainMenu.class));
            finish();
        }

        reset_alert = new AlertDialog.Builder(this);
        inflater = this.getLayoutInflater();




        forget_password_btn = findViewById(R.id.forget_password_btn);
        forget_password_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // start Forgotten password alertdialog
                View view = inflater.inflate(R.layout.reset_popup, null);
                reset_alert.setTitle("Reset forgot Password ?")
                        .setMessage("Enter Email to get password reset link")
                        .setPositiveButton("Reset", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                //Validate the email address
                                EditText email = view.findViewById(R.id.reset_email_popup);
                                if(email.getText().toString().isEmpty()){
                                    email.setError("Required Field");
                                    return;
                                }

                                //Send the reset link
                                mAuth.sendPasswordResetEmail(email.getText().toString()).addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        Toast.makeText(Login.this, "Reset Email Sent", Toast.LENGTH_SHORT).show();

                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(Login.this,e.getMessage(), Toast.LENGTH_SHORT).show();

                                    }
                                });
                            }
                        }).setNegativeButton("Cancel", null)
                        .setView(view)
                        .create().show();
            }
        });
    }
    @Override
    public void onStart() {
        super.onStart();

        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser != null) {

        }
    }

    public void signIn(String email, String password) {
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    //Checking if data is valid with auth system
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            FirebaseUser user = mAuth.getCurrentUser();
                            Toast.makeText(Login.this, "Authentication Success.",
                                    Toast.LENGTH_SHORT).show();
                            Intent MainMenu   = new Intent(getApplicationContext(), com.example.firebasetutorial.MainMenu.class);
                            startActivity(MainMenu);

                        } else {
                            Log.w("MainActivity", "signInWithEmail:failure",
                                    task.getException());
                            Toast.makeText(Login.this, "Authentication Failed", Toast.LENGTH_SHORT).show();
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(Login.this, "Authentication Failed",Toast.LENGTH_SHORT).show();
                    }
                });

    }
    public void signInButtonClicked(View view){
        EditText email = findViewById(R.id.TextEmailAddress);
        EditText password = findViewById(R.id.TextPassword);
        String sEmail = email.getText().toString();
        String sPassword = password.getText().toString();
        //validation
        if(TextUtils.isEmpty(sEmail)){
            email.setError("Email cannot be empty");
            return;
        }
        if(TextUtils.isEmpty(sPassword)){
            password.setError("Password cannot be empty");
            return;
        }
        signIn(sEmail, sPassword);


    }
    public void RegisterButtonClicked(View view){
        startActivity(new Intent(getApplicationContext(), RegisterActivity.class));
    }
}