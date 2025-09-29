package com.assitance.caremed;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class Med_UI_CreateNewPrescription extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {
    private Spinner choosePatientSpinnerList;
    private TextView recordView;
    TextView patient_name;
    TextView record_date;
    Button btn_selectDate;
    EditText medication_name;
    Spinner spinner_medication_freq;
    TextView dosage_heading;
    EditText dosage_amount;
    TextView select_dosage_freq;
    Spinner spinner_dosage_freq_unit;
    Button btn_createPrescription;
    ProgressBar progressBar;
    private String userEmail;
    StringBuilder records = new StringBuilder();
    private DatabaseReference medicalPatientRef;
    private DatabaseReference prescriptionRef;

    PrescriptionGetterSetter patientInfo;

    String autoGenID;
    String practitionerName;
    String practitionerSurname;
    String patientFirstname;
    String patientSurname;
    String patientName;
    String date;
    String medicationName;
    String medicationFreq;
    String medicationDosage;
    String medPracEmail;
    String patientEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_med__ui__create_new_prescription);


//------------------------------------------------------------- finding Ids-------------------------------------------------------------

        choosePatientSpinnerList = (Spinner) findViewById(R.id.spinner_FindPatientLastname);
        recordView = (TextView) findViewById(R.id.MedicalPracNameView);
        patient_name = (TextView) findViewById(R.id.textView_patientName);
        record_date = (TextView) findViewById(R.id.datetextView);
        btn_selectDate = (Button) findViewById(R.id.date_button);

        medication_name = (EditText) findViewById(R.id.txt_medicaitonName);
        select_dosage_freq = (TextView) findViewById(R.id.textView_medicationFreq);
        spinner_medication_freq = (Spinner) findViewById(R.id.spinner_medicationFreq);
        dosage_heading = (TextView) findViewById(R.id.txt_HeadingDosageAmount);
        dosage_amount = (EditText) findViewById(R.id.txt_dosageAmount);
        spinner_dosage_freq_unit = (Spinner) findViewById(R.id.spinner_dosageUnit);
        btn_createPrescription = (Button) findViewById(R.id.btnSubmit);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);

//------------------------------------------------------------- Firebase initialization -------------------------------------------------------------

        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        FirebaseDatabase mFirebaseDatabase = FirebaseDatabase.getInstance();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            userEmail = user.getEmail();
        }

//------------------------------------------------------------- Reference initialization -------------------------------------------------------------
        DatabaseReference medicalPracRef = mFirebaseDatabase.getReference().child("Medical_Practitioner");
        medicalPatientRef = mFirebaseDatabase.getReference().child("Patient");
        prescriptionRef = mFirebaseDatabase.getReference().child("Patient_Prescription");

        //------------------------------------------------------------- Setting up Header Practitioner Name -------------------------------------------------------------

        medicalPracRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot child : dataSnapshot.getChildren()) {
                    Medical_Practitioner details = child.getValue(Medical_Practitioner.class);
                    assert details != null;
                    if (details.getEmail().equals(userEmail)) {
                        practitionerName = details.getFirstname();
                        practitionerSurname = details.getLastname();
                        medPracEmail = details.getEmail();
                        String r1 = "Practitioner : " + details.getFirstname().toUpperCase() + " " + details.getLastname().toUpperCase();
                        records.append(r1);
                    }
                }
                recordView.setText(records.toString());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }


        });
        //------------------------------------------------------------- Filling spinner list -------------------------------------------------------------
        populateNames();

        //------------------------------------------------------------- Method to select from spinner list -------------------------------------------------------------

        choosePatientSpinnerList.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                String lastName = choosePatientSpinnerList.getSelectedItem().toString();
                if (position > 0) {
                    populateDetails(lastName);
                    showFields();
                } else {
                    hideFields();
                    // emptyDetails();
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // your code here
                Toast.makeText(Med_UI_CreateNewPrescription.this, "Error occurred during selection. Please contact developers", Toast.LENGTH_LONG).show();

            }

        });

//------------------------------------------------------------- Date Picker -------------------------------------------------------------

        btn_selectDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment datePicker = new DatePickerFragment();
                datePicker.show(getSupportFragmentManager(), "datePicker");
            }
        });

    }

    private void hideFields() {
        patient_name.setVisibility(View.GONE);
        record_date.setVisibility(View.GONE);
        btn_selectDate.setVisibility(View.GONE);
        medication_name.setVisibility(View.GONE);
        select_dosage_freq.setVisibility(View.GONE);
        spinner_medication_freq.setVisibility(View.GONE);
        dosage_heading.setVisibility(View.GONE);
        dosage_amount.setVisibility(View.GONE);
        spinner_dosage_freq_unit.setVisibility(View.GONE);
        btn_createPrescription.setVisibility(View.GONE);
    }


    private void showFields() {

        patient_name.setVisibility(View.VISIBLE);
        record_date.setVisibility(View.VISIBLE);
        btn_selectDate.setVisibility(View.VISIBLE);
        medication_name.setVisibility(View.VISIBLE);
        select_dosage_freq.setVisibility(View.VISIBLE);
        spinner_medication_freq.setVisibility(View.VISIBLE);
        dosage_heading.setVisibility(View.VISIBLE);
        dosage_amount.setVisibility(View.VISIBLE);
        spinner_dosage_freq_unit.setVisibility(View.VISIBLE);
        btn_createPrescription.setVisibility(View.VISIBLE);

    }


    //------------------------------------------------------------- Filling spinner list method-------------------------------------------------------------

    public void populateNames() {
        medicalPatientRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                final List<String> patientNameList = new ArrayList<String>();
                patientNameList.add("Please select lastname");
                for (DataSnapshot areaSnapshot : dataSnapshot.getChildren()) {
                    Patient patient = areaSnapshot.getValue(Patient.class);
                    assert patient != null;
                    patientNameList.add(patient.getLastname());
                }
                ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(Med_UI_CreateNewPrescription.this, android.R.layout.simple_spinner_item, patientNameList);
                arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                choosePatientSpinnerList.setAdapter(arrayAdapter);
            }


            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }

    //------------------------------------------------------------- A method to fill layout fields such as textView-------------------------------------------------------------
//
    private void populateDetails(final String lastName) {
        medicalPatientRef.orderByValue().addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for (DataSnapshot details : dataSnapshot.getChildren()) {
                    Patient patient = details.getValue(Patient.class);
                    assert patient != null;

                    if (patient.getLastname().equals(lastName)) {
                        patientFirstname = patient.getFirstname();
                        patientSurname = patient.getLastname();
                        patientEmail = patient.getPatientEmail();
                        patient_name.setText("Patient Name: " + patientFirstname + " " + patientSurname);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }


    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        Calendar c = Calendar.getInstance();
        c.set(Calendar.YEAR, year);
        c.set(Calendar.MONTH, month);
        c.set(Calendar.DAY_OF_MONTH, dayOfMonth);
        String currentDateString = DateFormat.getDateInstance(DateFormat.FULL).format(c.getTime());
        TextView textView = (TextView) findViewById(R.id.datetextView);
        textView.setText(currentDateString);
    }

    public void btn_savePrescription(View view) {
        patientInfo = new PrescriptionGetterSetter();
        autoGenID = prescriptionRef.push().getKey();

        Toast.makeText(Med_UI_CreateNewPrescription.this, practitionerName, Toast.LENGTH_LONG).show();
        patientInfo.setAutoGenID(autoGenID);
        patientInfo.setPractitionerName(practitionerName);
        patientInfo.setPractitionerSurname(practitionerSurname);
        patientInfo.setPatientFirstname(patientFirstname);
        patientInfo.setPatientSurname(patientSurname);
        patientInfo.setPatientEmail(patientEmail);
        patientInfo.setMedPracEmail(medPracEmail);
        patientInfo.setDate(record_date.getText().toString());
        patientInfo.setMedicationName(medication_name.getText().toString().trim());
        patientInfo.setMedicationFreq(spinner_medication_freq.getSelectedItem().toString());
        patientInfo.setMedicationDosage(dosage_amount.getText().toString().trim());
        patientInfo.setDosageUnit(spinner_dosage_freq_unit.getSelectedItem().toString());


        prescriptionRef.child(autoGenID).setValue(patientInfo);


    }
}

class PrescriptionGetterSetter {
    private String autoGenID;
    private String practitionerName;
    private String practitionerSurname;
    private String patientFirstname;
    private String patientSurname;
    private String date;
    private String medicationName;
    private String medicationFreq;
    private String medicationDosage;
    private String medPracEmail;
    private String patientEmail;
    private String dosageUnit;

    public PrescriptionGetterSetter() {
    }

    public PrescriptionGetterSetter(String autoGenID, String practitionerName, String practitionerSurname, String patientFirstname, String patientSurname, String date, String medicationName, String medicationFreq, String medicationDosage, String medPracEmail, String patientEmail, String dosageUnit) {
        this.autoGenID = autoGenID;
        this.practitionerName = practitionerName;
        this.practitionerSurname = practitionerSurname;
        this.patientFirstname = patientFirstname;
        this.patientSurname = patientSurname;
        this.date = date;
        this.medicationName = medicationName;
        this.medicationFreq = medicationFreq;
        this.medicationDosage = medicationDosage;
        this.medPracEmail = medPracEmail;
        this.patientEmail = patientEmail;
        this.dosageUnit = dosageUnit;
    }

    public String getAutoGenID() {
        return autoGenID;
    }

    public String getDosageUnit() {
        return dosageUnit;
    }

    public void setDosageUnit(String dosageUnit) {
        this.dosageUnit = dosageUnit;
    }

    public void setAutoGenID(String autoGenID) {
        this.autoGenID = autoGenID;
    }

    public String getPractitionerName() {
        return practitionerName;
    }

    public void setPractitionerName(String practitionerName) {
        this.practitionerName = practitionerName;
    }

    public String getPractitionerSurname() {
        return practitionerSurname;
    }

    public void setPractitionerSurname(String practitionerSurname) {
        this.practitionerSurname = practitionerSurname;
    }

    public String getPatientFirstname() {
        return patientFirstname;
    }

    public void setPatientFirstname(String patientFirstname) {
        this.patientFirstname = patientFirstname;
    }

    public String getPatientSurname() {
        return patientSurname;
    }

    public void setPatientSurname(String patientSurname) {
        this.patientSurname = patientSurname;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getMedicationName() {
        return medicationName;
    }

    public void setMedicationName(String medicationName) {
        this.medicationName = medicationName;
    }

    public String getMedicationFreq() {
        return medicationFreq;
    }

    public void setMedicationFreq(String medicationFreq) {
        this.medicationFreq = medicationFreq;
    }

    public String getMedicationDosage() {
        return medicationDosage;
    }

    public void setMedicationDosage(String medicationDosage) {
        this.medicationDosage = medicationDosage;
    }

    public String getMedPracEmail() {
        return medPracEmail;
    }

    public void setMedPracEmail(String medPracEmail) {
        this.medPracEmail = medPracEmail;
    }

    public String getPatientEmail() {
        return patientEmail;
    }

    public void setPatientEmail(String patientEmail) {
        this.patientEmail = patientEmail;
    }
}




