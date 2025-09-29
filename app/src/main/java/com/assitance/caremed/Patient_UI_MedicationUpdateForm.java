package com.assitance.caremed;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class Patient_UI_MedicationUpdateForm extends AppCompatActivity {
    private EditText medicationName1;
    private EditText dosageAmount1;
    private Spinner dosageFreq1;
    private Spinner dosageUnit1;
    private EditText medicationName2;
    private EditText dosageAmount2;
    private Spinner dosageFreq2;
    private Spinner dosageUnit2;
    private EditText medicationName3;
    private EditText dosageAmount3;
    private Spinner dosageFreq3;
    private Spinner dosageUnit3;
    private EditText medicationName4;
    private EditText dosageAmount4;
    private Spinner dosageFreq4;
    private Spinner dosageUnit4;
    private Button btnUpdate;
    private LinearLayout layout1;
    private LinearLayout layout2;
    private LinearLayout layout3;
    private LinearLayout layout4;
    DatabaseReference medicationUpdateRef;
    String userEmail;
    PatientEntryFormGetterSetter patientRecordsClass;
    List<PatientEntryFormGetterSetter> patientList = new ArrayList<>();
    int spinnerPosition;
    TextView noUpdateTextView;
    Button btn_CreateNewPatientRecord;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient__ui__medication_update_form);
        noUpdateTextView = (TextView) findViewById(R.id.noUpdateTextView);

        btn_CreateNewPatientRecord = (Button) findViewById(R.id.btn_CreateNewPatientRecord);
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            userEmail = user.getEmail();
        } else {
            // No user is signed in
        }
        patientRecordsClass = new PatientEntryFormGetterSetter();
        medicationUpdateRef = FirebaseDatabase.getInstance().getReference().child("PatientMedicationRecord");
        addListenerOnButton();



    }
    public void addListenerOnButton() {
        medicationName1 = (EditText) findViewById(R.id.txt_medicaitonName1);
        dosageAmount1 = (EditText) findViewById(R.id.txt_dosageAmount1);
        dosageFreq1 = (Spinner) findViewById(R.id.spinner_dosageFreq1);
        dosageUnit1 = (Spinner) findViewById(R.id.spinner_dosageUnit1);

        medicationName2 = (EditText) findViewById(R.id.txt_medicaitonName2);
        dosageAmount2 = (EditText) findViewById(R.id.txt_dosageAmount2);
        dosageFreq2 = (Spinner) findViewById(R.id.spinner_dosageFreq2);
        dosageUnit2 = (Spinner) findViewById(R.id.spinner_dosageUnit2);

        medicationName3 = (EditText) findViewById(R.id.txt_medicaitonName3);
        dosageAmount3 = (EditText) findViewById(R.id.txt_dosageAmount3);
        dosageFreq3 = (Spinner) findViewById(R.id.spinner_dosageFreq3);
        dosageUnit3 = (Spinner) findViewById(R.id.spinner_dosageUnit3);

        medicationName4 = (EditText) findViewById(R.id.txt_medicaitonName4);
        dosageAmount4 = (EditText) findViewById(R.id.txt_dosageAmount4);
        dosageFreq4 = (Spinner) findViewById(R.id.spinner_dosageFreq4);
        dosageUnit4 = (Spinner) findViewById(R.id.spinner_dosageUnit4);

        layout1 = (LinearLayout) findViewById(R.id.medication1);
        layout2 = (LinearLayout) findViewById(R.id.medication2);
        layout3 = (LinearLayout) findViewById(R.id.medication3);
        layout4 = (LinearLayout) findViewById(R.id.medication4);

        btnUpdate = (Button) findViewById(R.id.btnUpdate);
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        patientRecordsClass = new PatientEntryFormGetterSetter();

        if (user != null) {
            userEmail = user.getEmail();
        } else {
            // No user is signed in
        }

        medicationUpdateRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for (DataSnapshot child : dataSnapshot.getChildren()) {
                    PatientEntryFormGetterSetter medicationRec = child.getValue(PatientEntryFormGetterSetter.class);
                    assert medicationRec != null;
                    if (medicationRec.getEmail().equals(userEmail)) {
                        patientList.add(medicationRec);
                    }
                }
                if (patientList.size() == 0) {
                    btnUpdate.setVisibility(View.GONE);
                    layout1.setVisibility(View.GONE);
                    layout2.setVisibility(View.GONE);
                    layout3.setVisibility(View.GONE);
                    layout4.setVisibility(View.GONE);
                    noUpdateTextView.setVisibility(View.VISIBLE);
                    btn_CreateNewPatientRecord.setVisibility(View.VISIBLE);
                }
                if (patientList.size() > 0) {
                    setPageValues1(patientList.get(0));
                    layout1.setVisibility(View.VISIBLE);
                    layout2.setVisibility(View.GONE);
                    layout3.setVisibility(View.GONE);
                    layout4.setVisibility(View.GONE);
                    noUpdateTextView.setVisibility(View.GONE);
                    btn_CreateNewPatientRecord.setVisibility(View.GONE);

                }
                if (patientList.size() > 1) {
                    setPageValues2(patientList.get(1));
                    layout2.setVisibility(View.VISIBLE);
                    noUpdateTextView.setVisibility(View.GONE);
                    btn_CreateNewPatientRecord.setVisibility(View.GONE);

                }
                if (patientList.size() > 2) {
                    setPageValues3(patientList.get(2));
                    layout3.setVisibility(View.VISIBLE);
                    noUpdateTextView.setVisibility(View.GONE);
                    btn_CreateNewPatientRecord.setVisibility(View.GONE);

                }
                if (patientList.size() > 3) {
                    setPageValues4(patientList.get(3));
                    layout4.setVisibility(View.VISIBLE);
                    noUpdateTextView.setVisibility(View.GONE);
                    btn_CreateNewPatientRecord.setVisibility(View.GONE);

                }

            }


            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(Patient_UI_MedicationUpdateForm.this, "Database Error, Sorry We are working on it. ",
                        Toast.LENGTH_LONG).show();
            }

            private void setPageValues1(PatientEntryFormGetterSetter medication1) {
                @SuppressWarnings("unchecked")
                ArrayAdapter<String> myAdapFreq1 = (ArrayAdapter<String>) dosageFreq1.getAdapter(); //cast to an ArrayAdapter
                @SuppressWarnings("unchecked")
                ArrayAdapter<String> myAdapDosg1 = (ArrayAdapter<String>) dosageUnit1.getAdapter(); //cast to an ArrayAdapter
                medicationName1.setText(medication1.getMedicationName());
                dosageAmount1.setText(medication1.getDosageAmount());
                spinnerPosition = myAdapFreq1.getPosition(medication1.getDosageFrequency());
                dosageFreq1.setSelection(spinnerPosition);
                spinnerPosition = myAdapDosg1.getPosition(medication1.getDosageUnit());
                dosageUnit1.setSelection(spinnerPosition);
            }

            private void setPageValues2(PatientEntryFormGetterSetter medication2) {

                @SuppressWarnings("unchecked")
                ArrayAdapter<String> myAdapFreq2 = (ArrayAdapter<String>) dosageFreq2.getAdapter(); //cast to an ArrayAdapter
                @SuppressWarnings("unchecked")
                ArrayAdapter<String> myAdapDosg2 = (ArrayAdapter<String>) dosageUnit2.getAdapter(); //cast to an ArrayAdapter
                medicationName2.setText(medication2.getMedicationName());
                dosageAmount2.setText(medication2.getDosageAmount());
                spinnerPosition = myAdapFreq2.getPosition(medication2.getDosageFrequency());
                dosageFreq2.setSelection(spinnerPosition);
                spinnerPosition = myAdapDosg2.getPosition(medication2.getDosageUnit());
                dosageUnit2.setSelection(spinnerPosition);

            }

            private void setPageValues3(PatientEntryFormGetterSetter medication3) {

                @SuppressWarnings("unchecked")
                ArrayAdapter<String> myAdapFreq3 = (ArrayAdapter<String>) dosageFreq3.getAdapter(); //cast to an ArrayAdapter
                @SuppressWarnings("unchecked")
                ArrayAdapter<String> myAdapDosg3 = (ArrayAdapter<String>) dosageUnit3.getAdapter(); //cast to an ArrayAdapter
                medicationName3.setText(medication3.getMedicationName());
                dosageAmount3.setText(medication3.getDosageAmount());
                spinnerPosition = myAdapFreq3.getPosition(medication3.getDosageFrequency());
                dosageFreq3.setSelection(spinnerPosition);
                spinnerPosition = myAdapDosg3.getPosition(medication3.getDosageUnit());
                dosageUnit3.setSelection(spinnerPosition);

            }

            private void setPageValues4(PatientEntryFormGetterSetter medication4) {

                @SuppressWarnings("unchecked")
                ArrayAdapter<String> myAdapFreq4 = (ArrayAdapter<String>) dosageFreq4.getAdapter(); //cast to an ArrayAdapter
                @SuppressWarnings("unchecked")
                ArrayAdapter<String> myAdapDosg4 = (ArrayAdapter<String>) dosageUnit4.getAdapter(); //cast to an ArrayAdapter
                medicationName4.setText(medication4.getMedicationName());
                dosageAmount4.setText(medication4.getDosageAmount());
                spinnerPosition = myAdapFreq4.getPosition(medication4.getDosageFrequency());
                dosageFreq4.setSelection(spinnerPosition);
                spinnerPosition = myAdapDosg4.getPosition(medication4.getDosageUnit());
                dosageUnit4.setSelection(spinnerPosition);

            }


        });
    }

    public void btnSave(View view) {
        if (layout4.getVisibility() == View.VISIBLE) {
            removeData();
            String MedicationName1 = medicationName1.getText().toString().trim();
            String DosageAmount1 = dosageAmount1.getText().toString().trim();
            String DosageFreq1 = dosageFreq1.getSelectedItem().toString().trim();
            String DosageUnit1 = dosageUnit1.getSelectedItem().toString().trim();
            String MedicationName2 = medicationName2.getText().toString().trim();
            String DosageAmount2 = dosageAmount2.getText().toString().trim();
            String DosageFreq2 = dosageFreq2.getSelectedItem().toString().trim();
            String DosageUnit2 = dosageUnit2.getSelectedItem().toString().trim();
            String MedicationName3 = medicationName3.getText().toString().trim();
            String DosageAmount3 = dosageAmount3.getText().toString().trim();
            String DosageFreq3 = dosageFreq3.getSelectedItem().toString().trim();
            String DosageUnit3 = dosageUnit3.getSelectedItem().toString().trim();
            String MedicationName4 = medicationName4.getText().toString().trim();
            String DosageAmount4 = dosageAmount4.getText().toString().trim();
            String DosageFreq4 = dosageFreq4.getSelectedItem().toString().trim();
            String DosageUnit4 = dosageUnit4.getSelectedItem().toString().trim();

            if (TextUtils.isEmpty(MedicationName1)) {
                Toast.makeText(Patient_UI_MedicationUpdateForm.this, "Please enter Medication 1 Name", Toast.LENGTH_LONG).show();
                return;
            }
            else if (TextUtils.isEmpty(DosageAmount1)) {
                Toast.makeText(Patient_UI_MedicationUpdateForm.this, "Please enter Dosage 1 Amount", Toast.LENGTH_LONG).show();
                return;
            }
            else if (TextUtils.isEmpty(MedicationName2)) {
                Toast.makeText(Patient_UI_MedicationUpdateForm.this, "Please enter Medication 2 Name", Toast.LENGTH_LONG).show();
                return;
            }
            else if (TextUtils.isEmpty(DosageAmount2)) {
                Toast.makeText(Patient_UI_MedicationUpdateForm.this, "Please enter Dosage 2 Amount", Toast.LENGTH_LONG).show();
                return;
            }
            else if (TextUtils.isEmpty(MedicationName3)) {
                Toast.makeText(Patient_UI_MedicationUpdateForm.this, "Please enter Medication 3 Name", Toast.LENGTH_LONG).show();
                return;
            }
            else if (TextUtils.isEmpty(DosageAmount3)) {
                Toast.makeText(Patient_UI_MedicationUpdateForm.this, "Please enter Dosage 3 Amount", Toast.LENGTH_LONG).show();
                return;
            }
            else if (TextUtils.isEmpty(MedicationName4)) {
                Toast.makeText(Patient_UI_MedicationUpdateForm.this, "Please enter Medication 4 Name", Toast.LENGTH_LONG).show();
                return;
            }
            else if (TextUtils.isEmpty(DosageAmount4)) {
                Toast.makeText(Patient_UI_MedicationUpdateForm.this, "Please enter Dosage 4 Amount", Toast.LENGTH_LONG).show();
                return;
            }
            else {
                patientRecordsClass.setMedicationName(MedicationName1);
                patientRecordsClass.setDosageAmount(DosageAmount1);
                patientRecordsClass.setDosageFrequency(DosageFreq1);
                patientRecordsClass.setDosageUnit(DosageUnit1);
                patientRecordsClass.setEmail(userEmail);
                medicationUpdateRef.push().setValue(patientRecordsClass);

                patientRecordsClass.setMedicationName(MedicationName2);
                patientRecordsClass.setDosageAmount(DosageAmount2);
                patientRecordsClass.setDosageFrequency(DosageFreq2);
                patientRecordsClass.setDosageUnit(DosageUnit2);
                patientRecordsClass.setEmail(userEmail);
                medicationUpdateRef.push().setValue(patientRecordsClass);

                patientRecordsClass.setMedicationName(MedicationName3);
                patientRecordsClass.setDosageAmount(DosageAmount3);
                patientRecordsClass.setDosageFrequency(DosageFreq3);
                patientRecordsClass.setDosageUnit(DosageUnit3);
                patientRecordsClass.setEmail(userEmail);
                medicationUpdateRef.push().setValue(patientRecordsClass);

                patientRecordsClass.setMedicationName(MedicationName4);
                patientRecordsClass.setDosageAmount(DosageAmount4);
                patientRecordsClass.setDosageFrequency(DosageFreq4);
                patientRecordsClass.setDosageUnit(DosageUnit4);
                patientRecordsClass.setEmail(userEmail);
                medicationUpdateRef.push().setValue(patientRecordsClass);
                finish();
                startActivity(new Intent(getApplicationContext(), PatientUI.class));
                Toast.makeText(Patient_UI_MedicationUpdateForm.this, "Medication Record Updated! ",
                        Toast.LENGTH_LONG).show();

            }
        }
        if (layout3.getVisibility() == View.VISIBLE && layout4.getVisibility() == View.GONE) {
            removeData();
            String MedicationName1 = medicationName1.getText().toString().trim();
            String DosageAmount1 = dosageAmount1.getText().toString().trim();
            String DosageFreq1 = dosageFreq1.getSelectedItem().toString().trim();
            String DosageUnit1 = dosageUnit1.getSelectedItem().toString().trim();
            String MedicationName2 = medicationName2.getText().toString().trim();
            String DosageAmount2 = dosageAmount2.getText().toString().trim();
            String DosageFreq2 = dosageFreq2.getSelectedItem().toString().trim();
            String DosageUnit2 = dosageUnit2.getSelectedItem().toString().trim();
            String MedicationName3 = medicationName3.getText().toString().trim();
            String DosageAmount3 = dosageAmount3.getText().toString().trim();
            String DosageFreq3 = dosageFreq3.getSelectedItem().toString().trim();
            String DosageUnit3 = dosageUnit3.getSelectedItem().toString().trim();

            if (TextUtils.isEmpty(MedicationName1)) {
                Toast.makeText(Patient_UI_MedicationUpdateForm.this, "Please enter Medication 1 Name", Toast.LENGTH_LONG).show();
                return;
            }
            else if (TextUtils.isEmpty(DosageAmount1)) {
                Toast.makeText(Patient_UI_MedicationUpdateForm.this, "Please enter Dosage 1 Amount", Toast.LENGTH_LONG).show();
                return;
            }
            else if (TextUtils.isEmpty(MedicationName2)) {
                Toast.makeText(Patient_UI_MedicationUpdateForm.this, "Please enter Medication 2 Name", Toast.LENGTH_LONG).show();
                return;
            }
            else if (TextUtils.isEmpty(DosageAmount2)) {
                Toast.makeText(Patient_UI_MedicationUpdateForm.this, "Please enter Dosage 2 Amount", Toast.LENGTH_LONG).show();
                return;
            }
            else if (TextUtils.isEmpty(MedicationName3)) {
                Toast.makeText(Patient_UI_MedicationUpdateForm.this, "Please enter Medication 3 Name", Toast.LENGTH_LONG).show();
                return;
            }
            else if (TextUtils.isEmpty(DosageAmount3)) {
                Toast.makeText(Patient_UI_MedicationUpdateForm.this, "Please enter Dosage 3 Amount", Toast.LENGTH_LONG).show();
                return;
            }
            else {
                patientRecordsClass.setMedicationName(MedicationName1);
                patientRecordsClass.setDosageAmount(DosageAmount1);
                patientRecordsClass.setDosageFrequency(DosageFreq1);
                patientRecordsClass.setDosageUnit(DosageUnit1);
                patientRecordsClass.setEmail(userEmail);
                medicationUpdateRef.push().setValue(patientRecordsClass);

                patientRecordsClass.setMedicationName(MedicationName2);
                patientRecordsClass.setDosageAmount(DosageAmount2);
                patientRecordsClass.setDosageFrequency(DosageFreq2);
                patientRecordsClass.setDosageUnit(DosageUnit2);
                patientRecordsClass.setEmail(userEmail);
                medicationUpdateRef.push().setValue(patientRecordsClass);

                patientRecordsClass.setMedicationName(MedicationName3);
                patientRecordsClass.setDosageAmount(DosageAmount3);
                patientRecordsClass.setDosageFrequency(DosageFreq3);
                patientRecordsClass.setDosageUnit(DosageUnit3);
                patientRecordsClass.setEmail(userEmail);
                medicationUpdateRef.push().setValue(patientRecordsClass);

                finish();
                startActivity(new Intent(getApplicationContext(), PatientUI.class));
                Toast.makeText(Patient_UI_MedicationUpdateForm.this, "Medication Record Updated! ",
                        Toast.LENGTH_LONG).show();
            }

        }
        if (layout2.getVisibility() == View.VISIBLE && layout3.getVisibility() == View.GONE) {
            removeData();
            String MedicationName1 = medicationName1.getText().toString().trim();
            String DosageAmount1 = dosageAmount1.getText().toString().trim();
            String DosageFreq1 = dosageFreq1.getSelectedItem().toString().trim();
            String DosageUnit1 = dosageUnit1.getSelectedItem().toString().trim();
            String MedicationName2 = medicationName2.getText().toString().trim();
            String DosageAmount2 = dosageAmount2.getText().toString().trim();
            String DosageFreq2 = dosageFreq2.getSelectedItem().toString().trim();
            String DosageUnit2 = dosageUnit2.getSelectedItem().toString().trim();

            if (TextUtils.isEmpty(MedicationName1)) {
                Toast.makeText(Patient_UI_MedicationUpdateForm.this, "Please enter Medication 1 Name", Toast.LENGTH_LONG).show();
                return;
            }
            else if (TextUtils.isEmpty(DosageAmount1)) {
                Toast.makeText(Patient_UI_MedicationUpdateForm.this, "Please enter Dosage 1 Amount", Toast.LENGTH_LONG).show();
                return;
            }
            else if (TextUtils.isEmpty(MedicationName2)) {
                Toast.makeText(Patient_UI_MedicationUpdateForm.this, "Please enter Medication 2 Name", Toast.LENGTH_LONG).show();
                return;
            }
            else if (TextUtils.isEmpty(DosageAmount2)) {
                Toast.makeText(Patient_UI_MedicationUpdateForm.this, "Please enter Dosage 2 Amount", Toast.LENGTH_LONG).show();
                return;
            }
            else {
                patientRecordsClass.setMedicationName(MedicationName1);
                patientRecordsClass.setDosageAmount(DosageAmount1);
                patientRecordsClass.setDosageFrequency(DosageFreq1);
                patientRecordsClass.setDosageUnit(DosageUnit1);
                patientRecordsClass.setEmail(userEmail);
                medicationUpdateRef.push().setValue(patientRecordsClass);

                patientRecordsClass.setMedicationName(MedicationName2);
                patientRecordsClass.setDosageAmount(DosageAmount2);
                patientRecordsClass.setDosageFrequency(DosageFreq2);
                patientRecordsClass.setDosageUnit(DosageUnit2);
                patientRecordsClass.setEmail(userEmail);
                medicationUpdateRef.push().setValue(patientRecordsClass);

                finish();
                startActivity(new Intent(getApplicationContext(), PatientUI.class));
                Toast.makeText(Patient_UI_MedicationUpdateForm.this, "Medication Record Updated! ",
                        Toast.LENGTH_LONG).show();
            }
        }
        if (layout1.getVisibility() == View.VISIBLE && layout2.getVisibility() == View.GONE) {
            removeData();
            String MedicationName1 = medicationName1.getText().toString().trim();
            String DosageAmount1 = dosageAmount1.getText().toString().trim();
            String DosageFreq1 = dosageFreq1.getSelectedItem().toString().trim();
            String DosageUnit1 = dosageUnit1.getSelectedItem().toString().trim();

            if (TextUtils.isEmpty(MedicationName1)) {
                Toast.makeText(Patient_UI_MedicationUpdateForm.this, "Please enter Medication 1 Name", Toast.LENGTH_LONG).show();
                return;
            }
            else if (TextUtils.isEmpty(DosageAmount1)) {
                Toast.makeText(Patient_UI_MedicationUpdateForm.this, "Please enter Dosage 1 Amount", Toast.LENGTH_LONG).show();
                return;
            }
            else {
            patientRecordsClass.setMedicationName(MedicationName1);
            patientRecordsClass.setDosageAmount(DosageAmount1);
            patientRecordsClass.setDosageFrequency(DosageFreq1);
            patientRecordsClass.setDosageUnit(DosageUnit1);
            patientRecordsClass.setEmail(userEmail);
            medicationUpdateRef.push().setValue(patientRecordsClass);

            finishAffinity();
            startActivity(new Intent(getApplicationContext(), PatientUI.class));
            Toast.makeText(Patient_UI_MedicationUpdateForm.this, "Medication Record Updated! ",
                    Toast.LENGTH_LONG).show();
            }
        }
    }

    public void removeData() {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
        Query myQuery = ref.child("PatientMedicationRecord").orderByChild("email").equalTo(userEmail);

            myQuery.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot mySnapshot : dataSnapshot.getChildren()) {
                    mySnapshot.getRef().removeValue();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                //Log.e(TAG, "onCancelled", databaseError.toException());
            }
        });
    }

    public void onBackPressed() {
            new AlertDialog.Builder(this)
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .setTitle("Discard Changes")
                    .setMessage("Are you sure to discard changes?")
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            finish();

                        }

                    })
                    .setNegativeButton("No", null)
                    .show();
        }
    public void btn_createMedicationRecord(View view) {
         startActivity(new Intent(getApplicationContext(), Patient_UI_MedicationEntryForm.class));
    }

}