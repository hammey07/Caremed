package com.assitance.caremed;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
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

public class Medical_Pract_ViewExistingRecord extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {
    private Spinner choosePatientSpinnerList;
    medicalPatientGetterSetter patientInfo;
    String userEmail;
    String firstname;
    String surname;
    String auto_id;
    TextView patientName;
    TextView date;
    EditText visitNotes;
    FirebaseUser currentuser;
    DatabaseReference patientRef;
    DatabaseReference medicalPatientRef;
    String updatedNotes;
    String updatedDate;
    View layoutMedPracPatRecUpdate;
    Button btn_date;
    TextView notesHeading;
    Button btn_update;


    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_medical_pract_view_existing_record);
        patientName = (TextView) findViewById(R.id.txtPatient_name);
        date = (TextView) findViewById(R.id.datetextView);
        choosePatientSpinnerList = (Spinner) findViewById(R.id.spinner_FindPatientLastname);
        visitNotes = (EditText) findViewById(R.id.visit_notes);
        layoutMedPracPatRecUpdate = (View) findViewById(R.id.layoutMedPracPatRecUpdate);
        btn_date= (Button) findViewById(R.id.date_button);
        notesHeading = (TextView) findViewById(R.id.heading_notes);
        btn_update = (Button)findViewById(R.id.btnUpdate);

        btn_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment datePicker = new DatePickerFragment();
                datePicker.show(getSupportFragmentManager(), "datePicker");
            }
        });

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            userEmail = user.getEmail();
        } else {
            // No user is signed in
        }
        patientInfo = new medicalPatientGetterSetter();
        medicalPatientRef = FirebaseDatabase.getInstance().getReference().child("Medical_Practitioner_Patient_Record");
        currentuser = FirebaseAuth.getInstance().getCurrentUser();

        // change below this line



        choosePatientSpinnerList.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                String lastName = choosePatientSpinnerList.getSelectedItem().toString();

                if (position > 0) {
                    populateDetails(lastName);
                    showFields();
                } else {
                    hideFields();
                    emptyDetails();
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // your code here
                Toast.makeText(Medical_Pract_ViewExistingRecord.this, "Error occurred during selection. Please contact developers", Toast.LENGTH_LONG).show();

            }

        });

        populateNames();
    }




    private void emptyDetails() {
        visitNotes.setText(null);
        date.setText(null);


    }


    private void populateDetails(final String lastName) {
        medicalPatientRef.orderByValue().addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for (DataSnapshot details : dataSnapshot.getChildren()) {
                    medicalPatientGetterSetter patient = details.getValue(medicalPatientGetterSetter.class);
                    assert patient != null;
                    if (patient.getPatientlastname().equals(lastName)) {
                        firstname = patient.getPatientfirstname();
                        surname = patient.getPatientlastname();
                        patientName.setText(new StringBuilder().append(patient.getPatientfirstname()).append(" ").append(patient.getPatientlastname()).toString());
                        visitNotes.setText(patient.getNotes());
                        date.setText(patient.getRecord_date());
                        auto_id = patient.getAuto_id();



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


    public void populateNames() {


        medicalPatientRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                final List<String> patientNameList = new ArrayList<String>();
                patientNameList.add("Please select lastname");


                for (DataSnapshot areaSnapshot : dataSnapshot.getChildren()) {
                    medicalPatientGetterSetter patient = areaSnapshot.getValue(medicalPatientGetterSetter.class);
                    assert patient != null;
                    if (patient.getMed_email_id().equals(userEmail))
                        patientNameList.add(patient.getPatientlastname());

                }


                ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(Medical_Pract_ViewExistingRecord.this, android.R.layout.simple_spinner_item, patientNameList);
                arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                choosePatientSpinnerList.setAdapter(arrayAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }


    public void btn_Update(View view) {

        updatedDate = date.getText().toString().trim();
        updatedNotes = visitNotes.getText().toString().trim();
        if (choosePatientSpinnerList.getSelectedItem().equals("Please select lastname")) {
            Toast.makeText(Medical_Pract_ViewExistingRecord.this, "Sorry! Nothing to update.", Toast.LENGTH_LONG).show();
        } else {

            medicalPatientRef.child(auto_id).child("notes").setValue(updatedNotes);
            medicalPatientRef.child(auto_id).child("record_date").setValue(updatedDate);
            Toast.makeText(Medical_Pract_ViewExistingRecord.this, "Record has been updated!.", Toast.LENGTH_LONG).show();

        }

    }

    public void hideFields(){
        layoutMedPracPatRecUpdate.setVisibility(View.GONE);
        date.setVisibility(View.GONE);
        btn_date.setVisibility(View.GONE);
        notesHeading.setVisibility(View.GONE);
        visitNotes.setVisibility(View.GONE);
        btn_update.setVisibility(View.GONE);

    }
    private void showFields() {
        layoutMedPracPatRecUpdate.setVisibility(View.VISIBLE);
        date.setVisibility(View.VISIBLE);
        btn_date.setVisibility(View.VISIBLE);
        notesHeading.setVisibility(View.VISIBLE);
        visitNotes.setVisibility(View.VISIBLE);
        btn_update.setVisibility(View.VISIBLE);

    }

}