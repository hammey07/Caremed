package com.assitance.caremed;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ProgressBar;
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
import java.util.Calendar;

public class Medical_Pract_Patient_Record extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {
    FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference myRef2;
    ProgressBar progressBar;
    private FirebaseAuth firebaseAuth;
    EditText patientname;
    EditText patientsurname;
    TextView record_date;
    DatabaseReference medPrac_patient_record_Ref;
    medicalPatientGetterSetter medicalPractitioner_patient_record;
    TextView recordView;
    String userEmail;
    StringBuilder records = new StringBuilder();
    EditText visit_notes;
    Button btn_save;
    String id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_medical__pract__patient__record);

        recordView = (TextView) findViewById(R.id.MedicalPractitionerDetailsTextView);
        firebaseAuth = FirebaseAuth.getInstance();
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        myRef2 = mFirebaseDatabase.getReference().child("Medical_Practitioner");
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            userEmail = user.getEmail();
        }
        Button button = (Button) findViewById(R.id.date_button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment datePicker = new DatePickerFragment();
                datePicker.show(getSupportFragmentManager(), "datePicker");
            }
        });

        myRef2.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot child : dataSnapshot.getChildren()) {
                    Medical_Practitioner details = child.getValue(Medical_Practitioner.class);
                    assert details != null;
                    if (details.getEmail().equals(userEmail)) {
                        String r1 = "Practitioner : " + details.getFirstname().toUpperCase() +" "+ details.getLastname().toUpperCase();
                        records.append(r1);
                    }
                }
                recordView.setText(records.toString());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }


        });

        patientname = (EditText) findViewById(R.id.txtPatient_name);
        patientsurname = (EditText) findViewById(R.id.patient_surname);
        record_date = (TextView) findViewById(R.id.datetextView);
        visit_notes = (EditText) findViewById(R.id.visit_notes);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        btn_save = (Button) findViewById(R.id.btn_save);

        firebaseAuth = FirebaseAuth.getInstance();
        medicalPractitioner_patient_record = new medicalPatientGetterSetter();
        medPrac_patient_record_Ref = FirebaseDatabase.getInstance().getReference().child("Medical_Practitioner_Patient_Record");

        btn_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String Firstname = patientname.getText().toString().trim();
                final String Lastname = patientsurname.getText().toString().trim();
                final String date = record_date.getText().toString().trim();
                final String notes = visit_notes.getText().toString().trim();

                if (TextUtils.isEmpty(Firstname)) {

                    Toast.makeText(Medical_Pract_Patient_Record.this, "Please enter Patient Name: ", Toast.LENGTH_LONG).show();
                    return;
                }
                if (TextUtils.isEmpty(Lastname)) {
                    Toast.makeText(Medical_Pract_Patient_Record.this, "Please enter Patient Surname", Toast.LENGTH_LONG).show();
                    return;
                }
                if (TextUtils.isEmpty(date)) {
                    Toast.makeText(Medical_Pract_Patient_Record.this, "Date can't be empty", Toast.LENGTH_LONG).show();
                    return;
                }
                id = medPrac_patient_record_Ref.push().getKey();

                if (TextUtils.isEmpty(notes)) {
                    new AlertDialog.Builder(Medical_Pract_Patient_Record.this)
                            .setIcon(android.R.drawable.ic_dialog_alert)
                            .setTitle("Empty Field!!")
                            .setMessage("Are you sure you have no Notes for this Patient?")
                            .setPositiveButton("Yes", new DialogInterface.OnClickListener() {

                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    //  finishAffinity();
                                    //   startActivity(new Intent(getApplicationContext(),loginForm.class));

                                    medicalPractitioner_patient_record.setPatientfirstname(Firstname);
                                    medicalPractitioner_patient_record.setPatientlastname(Lastname);
                                    medicalPractitioner_patient_record.setRecord_date(date);
                                    medicalPractitioner_patient_record.setNotes(notes);
                                    medicalPractitioner_patient_record.setMed_email_id(userEmail);
                                    medicalPractitioner_patient_record.setAuto_id(id);


                                    progressBar.setVisibility(View.VISIBLE);
                                    btn_save.setVisibility(View.GONE);

                                    medPrac_patient_record_Ref.child(id).setValue(medicalPractitioner_patient_record);

                                    Toast.makeText(Medical_Pract_Patient_Record.this, "New Patient Record has been Created",
                                            Toast.LENGTH_LONG).show();
                                    finish();
                                    startActivity(new Intent(getApplicationContext(), medicalPracUI.class));


                                }

                            })
                            .setNegativeButton("No", null)
                            .show();
                } else {
                    medicalPractitioner_patient_record.setPatientfirstname(Firstname);
                    medicalPractitioner_patient_record.setPatientlastname(Lastname);
                    medicalPractitioner_patient_record.setRecord_date(date);
                    medicalPractitioner_patient_record.setNotes(notes);
                    medicalPractitioner_patient_record.setMed_email_id(userEmail);
                    medicalPractitioner_patient_record.setAuto_id(id);




                    progressBar.setVisibility(View.VISIBLE);
                    btn_save.setVisibility(View.GONE);

                    medPrac_patient_record_Ref.child(id).setValue(medicalPractitioner_patient_record);

                    Toast.makeText(Medical_Pract_Patient_Record.this, "New Patient Record has been Created",
                            Toast.LENGTH_LONG).show();
                    finish();
                    startActivity(new Intent(getApplicationContext(), medicalPracUI.class));

                }
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
}

// the sub class of the medical_practitioner_patient_record
class medicalPatientGetterSetter {
    private String patientfirstname;
    private String patientlastname;
    private String record_date;
    private String notes;
    private String med_email_id;
    private String auto_id;

    public medicalPatientGetterSetter() {
    }

    public medicalPatientGetterSetter(String patientfirstname, String patientlastname, String date, String notes, String med_email_id, String auto_id) {
        this.patientfirstname = patientfirstname;
        this.patientlastname = patientlastname;
        this.record_date = date;
        this.notes = notes;
        this.med_email_id = med_email_id;
        this.auto_id = auto_id;

    }

    public String getPatientfirstname() {
        return patientfirstname;
    }

    public void setPatientfirstname(String patientfirstname) {
        this.patientfirstname = patientfirstname;
    }

    public String getPatientlastname() {
        return patientlastname;
    }

    public void setPatientlastname(String patientlastname) {
        this.patientlastname = patientlastname;
    }

    public String getRecord_date() {
        return record_date;
    }

    public void setRecord_date(String record_date) {
        this.record_date = record_date;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public String getMed_email_id() {
        return med_email_id;
    }

    public void setMed_email_id(String med_email_id) {
        this.med_email_id = med_email_id;
    }

    public String getAuto_id() {
        return auto_id;
    }

    public void setAuto_id(String auto_id) {
        this.auto_id = auto_id;
    }
}


