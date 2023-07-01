package com.example.firebasetutorial;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

public class AccountDetailsActivity extends AppCompatActivity {
    TextView email, fullName, phone;
     FirebaseAuth mAuth;
     FirebaseFirestore mStore;
     String userID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("Account Details");
        setContentView(R.layout.account_details);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        phone   = findViewById(R.id.displayPhone);
        fullName = findViewById(R.id.displayName);
        email = findViewById(R.id.displayEmail);

        mAuth = FirebaseAuth.getInstance();
        mStore = FirebaseFirestore.getInstance();


        userID  = mAuth.getCurrentUser().getUid();
        DocumentReference documentReference = mStore.collection("users").document(userID);
        documentReference.addSnapshotListener(this, new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
                phone.setText(documentSnapshot.getString("phone"));
                fullName.setText(documentSnapshot.getString("fName"));
                email.setText(documentSnapshot.getString("email"));
            }
        });

        mAuth = FirebaseAuth.getInstance();
    }
}