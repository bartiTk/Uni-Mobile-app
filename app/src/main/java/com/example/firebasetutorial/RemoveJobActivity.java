package com.example.firebasetutorial;

import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class RemoveJobActivity extends AppCompatActivity {

    Button remove_job_button;
    FirebaseFirestore mStore;
    EditText mJobID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_remove_job);
        setTitle("Remove Job");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        remove_job_button = findViewById(R.id.jobRemoveConfirm);
        mJobID = findViewById(R.id.inputRemoveJobID);
        mStore = FirebaseFirestore.getInstance();

        CollectionReference collectionReference = mStore.collection("Current Jobs");


        remove_job_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String jobID = mJobID.getText().toString().trim();

                //validation
                if(TextUtils.isEmpty(jobID)){
                    mJobID.setError("Need to Input JobID");
                    return;
                }
                 mStore.collection("Current Jobs").document(jobID).delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Toast.makeText(RemoveJobActivity.this,"Deleted Job",Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(getApplicationContext(), MainMenu.class));
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(RemoveJobActivity.this, "Error Deleting", Toast.LENGTH_SHORT).show();

                    }
                });
            }
        });
    }
}