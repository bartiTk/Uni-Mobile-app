package com.example.firebasetutorial;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import com.google.firebase.auth.FirebaseAuth;

public class MainMenu extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        setTitle("Main Menu");
    }

    public void accountDetailsButtonClicked(View view){
        Intent accountDetailsActivity = new Intent(getApplicationContext(), AccountDetailsActivity.class);
        startActivity(accountDetailsActivity);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.option_menu,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if(item.getItemId() == R.id.resetUserPassword){
            startActivity(new Intent(getApplicationContext(), ResetPasswordActivity.class ));
        }
        if(item.getItemId() == R.id.removeJob){
            startActivity(new Intent(getApplicationContext(), RemoveJobActivity.class));
        }

        return super.onOptionsItemSelected(item);
    }
    public void Logout (View view){
        FirebaseAuth.getInstance().signOut();
        Intent signOut = new Intent(getApplicationContext(), Login.class);
        startActivity(signOut);
    }

    public void addJobButtonClicked (View view){
        startActivity(new Intent(getApplicationContext(), addJob.class));
    }
    public void viewJobListButtonPressed(View view){
        startActivity(new Intent(getApplicationContext(), CurrentJobListActivity.class));
    }
}