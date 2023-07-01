package com.example.firebasetutorial;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TextView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

public class CurrentJobListActivity extends AppCompatActivity {
    TextView companyDetails;
    FirebaseFirestore mStore;
    AlertDialog.Builder deleteJob_alert;
    LayoutInflater inflater;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.current_job_list);
        setTitle("Current Job List");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);



        companyDetails = findViewById(R.id.displayCompany);
        mStore = FirebaseFirestore.getInstance();

        CollectionReference collectionReference = mStore.collection("Current Jobs");
        collectionReference.addSnapshotListener(this, new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent( QuerySnapshot queryDocumentSnapshots ,FirebaseFirestoreException error) {
                if (error != null) {
                    return;
                }
                String data = "";

                for(QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                    String jobID = documentSnapshot.getString("JobID");
                    String companyName = documentSnapshot.getString("CompanyName");
                    String jobName = documentSnapshot.getString("JobName");
                    String progress = documentSnapshot.getString("Progress");

                    data += "JobID: " + jobID  +  "\nCompany Name: " + companyName + "\nJob Name: " + jobName + "\nProgress: " + progress + "%\n\n";
                }
                companyDetails.setText(data);
            }
        });
    }
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.option_menu,menu);
        return super.onCreateOptionsMenu(menu);
    }
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if(item.getItemId() == R.id.resetUserPassword){
            startActivity(new Intent(getApplicationContext(), ResetPasswordActivity.class ));
        }
        if(item.getItemId() == R.id.removeJob){
            startActivity(new Intent(getApplicationContext(), RemoveJobActivity.class));
        }

        return super.onOptionsItemSelected(item);
    }
}