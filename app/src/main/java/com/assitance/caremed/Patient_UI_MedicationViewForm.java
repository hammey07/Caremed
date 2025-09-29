package com.assitance.caremed;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

public class Patient_UI_MedicationViewForm extends AppCompatActivity {
    FirebaseDatabase mFirebaseDatabase;
    FirebaseAuth mAuth;
    DatabaseReference myRef;
    TextView recordView;
    StringBuilder records = new StringBuilder();
    String userEmail;
    PatientEntryFormGetterSetter patientRecordsClass;

    private static final String TAG = "ViewDatabase";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient__ui__medication_view_form);
        recordView = (TextView) findViewById(R.id.txt_viewPatientRecord1);

        mAuth = FirebaseAuth.getInstance();
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        myRef = mFirebaseDatabase.getReference().child("PatientMedicationRecord");
        patientRecordsClass = new PatientEntryFormGetterSetter();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            userEmail = user.getEmail();
        } else {
            // No user is signed in
        }
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot child : dataSnapshot.getChildren()) {
                    PatientEntryFormGetterSetter details = child.getValue(PatientEntryFormGetterSetter.class);
                    assert details != null;
                    if (details.getEmail().equals(userEmail)) {
                        String r1 = "Name: " + details.getMedicationName() +
                                "\nFrequency: " + details.getDosageFrequency() +
                                "\nDosage Amount: " + details.getDosageAmount() +
                                " " + details.getDosageUnit() + "\n\n";
                        records.append(r1);
                    }
                }
                recordView.setText(records.toString());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }


        });
    }

    public void btn_updateMedicationRecord(View view) {
        startActivity(new Intent(getApplicationContext(), Patient_UI_MedicationUpdateForm.class));
    }
}