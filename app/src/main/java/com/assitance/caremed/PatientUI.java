package com.assitance.caremed;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

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

public class PatientUI extends AppCompatActivity {
    Menu optionsMenu;
    private DatabaseReference patientRef;
    Patient patientInfo;
    String userName;
    String userEmail;
    FirebaseDatabase mFirebaseDatabase;
    FirebaseAuth mAuth;
    DatabaseReference myRef;
    PatientEntryFormGetterSetter patientRecordsClass;
    int counter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_ui);
        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            userEmail = user.getEmail();
        } else {
            // No user is signed in
        }

        patientInfo = new Patient();
        patientRef = FirebaseDatabase.getInstance().getReference().child("Patient");

        patientRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot child : dataSnapshot.getChildren()) {
                    Patient details = child.getValue(Patient.class);
                    assert details != null;
                    if (details.getPatientEmail().equals(userEmail)) {
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

        mAuth = FirebaseAuth.getInstance();
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        myRef = mFirebaseDatabase.getReference().child("PatientMedicationRecord");
        patientRecordsClass = new PatientEntryFormGetterSetter();
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) { counter = 0;
                for (DataSnapshot child : dataSnapshot.getChildren()) {
                    PatientEntryFormGetterSetter details = child.getValue(PatientEntryFormGetterSetter.class);
                    assert details != null;
                    if (details.getEmail().equals(userEmail)) {
                        counter++;
                    }
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


    public void btn_createMedicationRecord(View view) {
        if(counter<4){
            startActivity(new Intent(getApplicationContext(), Patient_UI_MedicationEntryForm.class));
    }
        else {
            Toast.makeText(PatientUI.this, "A maximum of 4 records allow. Please update and use old records", Toast.LENGTH_LONG).show();
        }
    }

    public void btn_viewMedicationRecord(View view) {
        startActivity(new Intent(getApplicationContext(), Patient_UI_MedicationViewForm.class));
    }

    public void btn_updateMedicationRecord(View view) {
        startActivity(new Intent(getApplicationContext(), Patient_UI_MedicationUpdateForm.class));
    }

    public void btn_viewPrescription(View view) {
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

}
