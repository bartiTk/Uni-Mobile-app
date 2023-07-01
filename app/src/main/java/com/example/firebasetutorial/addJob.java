package com.example.firebasetutorial;

import static android.content.ContentValues.TAG;

import androidx.activity.result.ActivityResultLauncher;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.journeyapps.barcodescanner.ScanContract;
import com.journeyapps.barcodescanner.ScanOptions;

import org.checkerframework.checker.units.qual.Current;

import java.util.HashMap;
import java.util.Map;

public class addJob extends AppCompatActivity {

    EditText mCompany, mName, mJobID, mProgress;
    Button jobConfirm, scanButton;
    FirebaseFirestore fStore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_job);
        setTitle("Add Job");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        jobConfirm = findViewById(R.id.jobConfirmButton);
        mCompany = findViewById(R.id.inputCompany);
        mName   = findViewById(R.id.inputJobName);
        mJobID = findViewById(R.id.inputJobID);
        mProgress=findViewById(R.id.inputProgress);
        scanButton = findViewById(R.id.qrScanButton);

        fStore = FirebaseFirestore.getInstance();

        jobConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String company = mCompany.getText().toString().trim();
                String name =mName.getText().toString().trim();
                String jobID = mJobID.getText().toString().trim();
                String progress = mProgress.getText().toString();


                //Validation
                if(TextUtils.isEmpty(company)){
                    mCompany.setError("Company name is not valid");
                    return;
                }
                if(TextUtils.isEmpty(name)){
                    mName.setError("Name is not valid");
                    return;
                }
                if(TextUtils.isEmpty(jobID)){
                    mJobID.setError("JobID is not valid");
                    return;
                }
                if(jobID.length() != 6){
                    mJobID.setError("JobID needs to be 6 characters long");
                    return;
                }
                if(TextUtils.isEmpty(progress)) {
                    mProgress.setError("Progress Value is not valid");
                    return;
                }
                int progressNum = Integer.parseInt(progress);
                if(progressNum  >100 ||  progressNum < 0){
                    mProgress.setError("Progress Value is not valid");
                    return;
                }
                DocumentReference documentReference = fStore.collection("Current Jobs").document(jobID);
                Map<String, Object> CurrentJobs = new HashMap<>();
                CurrentJobs.put("JobID", jobID);
                CurrentJobs.put("JobName", name);
                CurrentJobs.put("CompanyName",company);
                CurrentJobs.put("Progress",progress);
                documentReference.set(CurrentJobs).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Log.d(TAG,"User Info is stored for" + name);
                        startActivity(new Intent(getApplicationContext(),MainMenu.class));
                        Toast.makeText(addJob.this, "Job has been Uploaded to the database",
                                Toast.LENGTH_SHORT).show();

                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(addJob.this, "Job Upload Failed", Toast.LENGTH_SHORT).show();
                    }
                });

            }
        });

        scanButton.setOnClickListener(v ->
        {
            scanCode();
        });
    }

    private void scanCode() {
        ScanOptions options = new ScanOptions();
        options.setPrompt("Volume up to enable the Flash");
        options.setOrientationLocked(true);
        options.setBeepEnabled(true);
        options.setCaptureActivity(CaptureAct.class);
        barLauncher.launch(options);

    }
    ActivityResultLauncher<ScanOptions> barLauncher = registerForActivityResult(new ScanContract(), result ->
    {
        if(result.getContents() != null){
            String jobInfo = result.getContents();
            String[] jobInfoSplit = jobInfo.split(":");
            mCompany.setText(jobInfoSplit[0]);
            mName.setText(jobInfoSplit[1]);
            mJobID.setText(jobInfoSplit[2]);
            mProgress.setText(jobInfoSplit[3]);

            AlertDialog.Builder builder = new AlertDialog.Builder( addJob.this);
            builder.setTitle("Result");
            builder.setMessage(result.getContents());
            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    dialogInterface.dismiss();
                }
            }).show();
        }

    });
}