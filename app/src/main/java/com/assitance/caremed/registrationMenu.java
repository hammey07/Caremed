package com.assitance.caremed;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class registrationMenu extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration_menu);
        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);
        getSupportActionBar().setTitle("CareMed User Selection");

    }

    public void btn_medicalPractitioner_signup(View view) {
        finishAffinity();
        startActivity(new Intent(getApplicationContext(), medicalPractitionerSignUp.class));

    }

    public void btn_patient_signup(View view) {
        finishAffinity();
        startActivity(new Intent(getApplicationContext(), patientSignUp.class));

    }
}
