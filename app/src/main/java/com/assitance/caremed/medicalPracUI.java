package com.assitance.caremed;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class medicalPracUI extends AppCompatActivity {
    String userName;
    String userEmail;
    Menu optionsMenu;
    private DatabaseReference medicalRef;
    Medical_Practitioner medicalPractitionerInfo;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_medical_prac_ui);
        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            userEmail = user.getEmail();
        } else {
            // No user is signed in
        }
        medicalPractitionerInfo = new Medical_Practitioner();
        medicalRef = FirebaseDatabase.getInstance().getReference().child("Medical_Practitioner");

        medicalRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot child : dataSnapshot.getChildren()) {
                    Medical_Practitioner details = child.getValue(Medical_Practitioner.class);
                    assert details != null;
                    if (details.getEmail().equals(userEmail)) {
                        userName = details.getFirstname();
                    }
                }
                if(userName!=null) {
                    getSupportActionBar().setTitle("Welcome " + userName.toUpperCase());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }


        });
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        MenuInflater mi = getMenuInflater();
        mi.inflate(R.menu.actionbar1, menu);
        optionsMenu = menu;
        return super.onCreateOptionsMenu(menu);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        MenuItem logoutItem = optionsMenu.findItem(R.id.item_logout);
        int id = logoutItem.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.item_logout) {
            onBackPressed();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    public void MPcreateNewRecordforPatient(View view) {
        startActivity(new Intent(getApplicationContext(), Medical_Pract_Patient_Record.class));
    }

    public void onBackPressed() {
        new AlertDialog.Builder(this)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setTitle("Application Logout")
                .setMessage("Are you sure you want to Logout?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finishAffinity();
                        startActivity(new Intent(getApplicationContext(), loginForm.class));

                    }

                })
                .setNegativeButton("No", null)
                .show();
    }

    public void btn_viewExistingPatientRecord(View view) {
        startActivity(new Intent(getApplicationContext(), Medical_Pract_ViewExistingRecord.class));
    }

    public void btn_createNewPrescription(View view) {
        startActivity(new Intent(getApplicationContext(), Med_UI_CreateNewPrescription.class));

    }
}
