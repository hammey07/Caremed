package com.assitance.caremed;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

public class Patient_UI_MedicationEntryForm extends AppCompatActivity {
    private Spinner dosageFreq;
    private Spinner dosageUnit;
    private EditText medicationName;
    private EditText dosageAmount;
    private Button btnSubmit;
    ProgressBar progressBar;
    private FirebaseAuth firebaseAuth;
    DatabaseReference medicationEntryRef;
    PatientEntryFormGetterSetter medicationInfo;
    public String userEmail;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_ui_medication_entry_form);
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            userEmail = user.getEmail();
        } else {
            // No user is signed in
        }
        medicationInfo = new PatientEntryFormGetterSetter();
        medicationEntryRef = FirebaseDatabase.getInstance().getReference().child("PatientMedicationRecord");

        addListenerOnButton();
    }


    // get the selected dropdown list value
    public void addListenerOnButton() {
        medicationName = (EditText) findViewById(R.id.txt_medicaitonName);
        dosageAmount = (EditText) findViewById(R.id.txt_dosageAmount);
        dosageFreq = (Spinner) findViewById(R.id.spinner_dosageFreq);
        dosageUnit = (Spinner) findViewById(R.id.spinner_dosageUnit);
        btnSubmit = (Button) findViewById(R.id.btnSubmit);

        btnSubmit.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                String MedicationName = medicationName.getText().toString().trim();
                String DosageAmount = dosageAmount.getText().toString().trim();
                String DosageFreq = dosageFreq.getSelectedItem().toString().trim();
                String DosageUnit = dosageUnit.getSelectedItem().toString().trim();

                if (TextUtils.isEmpty(MedicationName)) {
                    Toast.makeText(Patient_UI_MedicationEntryForm.this, "Please enter Medication Name", Toast.LENGTH_LONG).show();
                    return;
                }
                if (TextUtils.isEmpty(DosageAmount)) {
                    Toast.makeText(Patient_UI_MedicationEntryForm.this, "Please enter Dosage Amount", Toast.LENGTH_LONG).show();
                    return;
                }

                medicationInfo.setMedicationName(MedicationName);
                medicationInfo.setDosageAmount(DosageAmount);
                medicationInfo.setDosageFrequency(DosageFreq);
                medicationInfo.setDosageUnit(DosageUnit);
                medicationInfo.setEmail(userEmail);

                medicationEntryRef.push().setValue(medicationInfo);

                Toast.makeText(Patient_UI_MedicationEntryForm.this, "Medication Record has been Created",
                        Toast.LENGTH_LONG).show();
                finish();
                startActivity(new Intent(getApplicationContext(), PatientUI.class));

            }

        });
    }
}