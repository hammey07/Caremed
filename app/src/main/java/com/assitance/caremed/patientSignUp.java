package com.assitance.caremed;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;


import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class patientSignUp extends AppCompatActivity {
    EditText txtFirstname, txtLastname, txtPatientPhone, txtCaregiverName,
            getTxtCaregiverPhone, txtPatientEmail, txtPassword, txtConfirmPassword;
    Button btn_signup;
    ProgressBar progressBar;
    private FirebaseAuth firebaseAuth;
    DatabaseReference patientRef;
    Patient patient;
    List <String> errors;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_sign_up);
        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);
        getSupportActionBar().setTitle("Patient Registration Form");


        txtFirstname = (EditText) findViewById(R.id.txtFirstname);
        txtLastname = (EditText) findViewById(R.id.txtLastname);
        txtPatientPhone = (EditText) findViewById(R.id.txtPatientPhone);
        txtCaregiverName = (EditText) findViewById(R.id.txtCaregiverName);
        getTxtCaregiverPhone = (EditText) findViewById(R.id.txtCaregiverPhone);
        txtPatientEmail = (EditText) findViewById(R.id.txtPatientEmail);
        txtPassword = (EditText) findViewById(R.id.txtPassword);
        txtConfirmPassword = (EditText) findViewById(R.id.txtConfirmPassword);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        btn_signup = (Button) findViewById(R.id.btn_signup);

        firebaseAuth = FirebaseAuth.getInstance();
        patient = new Patient();
        patientRef = FirebaseDatabase.getInstance().getReference().child("Patient");

        btn_signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String patientFirstname = txtFirstname.getText().toString().trim();
                String patientLastname = txtLastname.getText().toString().trim();
                String patientPhone = txtPatientPhone.getText().toString().trim();
                String patientEmail = txtPatientEmail.getText().toString().trim();
                String caregiverName = txtCaregiverName.getText().toString().trim();
                String caregiverPhone = getTxtCaregiverPhone.getText().toString().trim();
                String password = txtPassword.getText().toString().trim();
                String confirmPassword = txtConfirmPassword.getText().toString().trim();
                String stringHashPassword = computeMD5Hash(password);
                String stringHashConfirmPassword = computeMD5Hash(confirmPassword);

                errors = new ArrayList<String>();

                if (TextUtils.isEmpty(patientFirstname)) {
                    errors.add("Please enter First Name");
                    Toast.makeText(patientSignUp.this, "Please enter First Name", Toast.LENGTH_LONG).show();
                } else if (TextUtils.isEmpty(patientLastname)) {
                    errors.add("Please enter Last Name");
                    Toast.makeText(patientSignUp.this, "Please enter Last Name", Toast.LENGTH_LONG).show();
                } else if (TextUtils.isEmpty(patientPhone)) {
                    errors.add("Please enter phone number");
                    Toast.makeText(patientSignUp.this, "Please enter phone number", Toast.LENGTH_LONG).show();
                } else if (!isPhoneValid(patientPhone)) {
                    errors.add("Please enter valid Patient Phone number\n(08x xxx xxxx) OR (0x xxx xxxx)");
                    Toast.makeText(patientSignUp.this, "Please enter valid Patient Phone number\n(08x xxx xxxx) OR (0x xxx xxxx)", Toast.LENGTH_LONG).show();
                } else if (TextUtils.isEmpty(patientEmail)) {
                    errors.add("Please enter Email address");
                    Toast.makeText(patientSignUp.this, "Please enter Email address", Toast.LENGTH_LONG).show();
                } else if (!isEmailValid(patientEmail)) {
                    errors.add("Please enter valid Email address\n E.g (john.doe@gmail.com) or (harry_smith@yahoo.co.uk)");
                    Toast.makeText(patientSignUp.this, "Please enter valid Email address\n E.g (john.doe@gmail.com) or (harry_smith@yahoo.co.uk)", Toast.LENGTH_LONG).show();
                } else if (TextUtils.isEmpty(caregiverName)) {
                    errors.add("Please enter Caregiver Name");
                    Toast.makeText(patientSignUp.this, "Please enter Caregiver Name", Toast.LENGTH_LONG).show();
                } else if (TextUtils.isEmpty(caregiverPhone)) {
                    errors.add("Please enter Caregiver Phone number");
                    Toast.makeText(patientSignUp.this, "Please enter Caregiver Phone number", Toast.LENGTH_LONG).show();
                } else if (!isPhoneValid(caregiverPhone)) {
                    errors.add("Please enter valid Caregiver Phone number\n(08x xxx xxxx) OR (0x xxx xxxx)");
                    Toast.makeText(patientSignUp.this, "Please enter valid Caregiver Phone number\n(08x xxx xxxx) OR (0x xxx xxxx)", Toast.LENGTH_LONG).show();
                } else if (TextUtils.isEmpty(password)) {
                    errors.add("Please enter valid password");
                    Toast.makeText(patientSignUp.this, "Please enter valid password", Toast.LENGTH_LONG).show();
                } else if (TextUtils.isEmpty(confirmPassword)) {
                    errors.add("Please enter confirm password");
                    Toast.makeText(patientSignUp.this, "Please enter confirm password", Toast.LENGTH_LONG).show();
                } else if (!password.equals(confirmPassword)) {
                    errors.add("Passwords do not match");
                    Toast.makeText(patientSignUp.this, "Passwords do not match", Toast.LENGTH_LONG).show();
                } else if (password.length() < 6) {
                    errors.add("Password must be at least 6 characters long");
                    Toast.makeText(patientSignUp.this, "Password must be at least 6 characters long", Toast.LENGTH_LONG).show();
                }
                if(errors.isEmpty()){

                    patient.setFirstname(patientFirstname);
                    patient.setLastname(patientLastname);
                    patient.setPatientPhone(patientPhone);
                    patient.setCaregiverName(caregiverName);
                    patient.setCaregiverPhone(caregiverPhone);
                    patient.setPatientEmail(patientEmail);
                    patient.setPassword(stringHashPassword);
                    patient.setConfirmPassword(stringHashConfirmPassword);

                    progressBar.setVisibility(View.VISIBLE);
                    btn_signup.setVisibility(View.GONE);
                    firebaseAuth.createUserWithEmailAndPassword(patientEmail, password)
                            .addOnCompleteListener(patientSignUp.this, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    progressBar.setVisibility(View.GONE);
                                       if (task.isSuccessful()) {
                                        patientRef.push().setValue(patient);
                                        Toast.makeText(patientSignUp.this, "Registration Successful", Toast.LENGTH_LONG).show();
                                        Intent login = new Intent(patientSignUp.this, loginForm.class);
                                        startActivity(login);
                                    } else {
                                        btn_signup.setVisibility(View.VISIBLE);
                                        Toast.makeText(patientSignUp.this, "Authentication Failed, Email Address has been registered previously", Toast.LENGTH_LONG).show();
                                    }
                                }
                            });
                }
            }
        });
    }


            public String computeMD5Hash(String password) {
                try {
                    // Create MD5 Hash
                    MessageDigest digest = java.security.MessageDigest.getInstance("MD5");
                    digest.update(password.getBytes());
                    byte messageDigest[] = digest.digest();

                    // Create Hex String
                    StringBuilder hexString = new StringBuilder();
                    for (int i = 0; i < messageDigest.length; i++)
                        hexString.append(Integer.toHexString(0xFF & messageDigest[i]));

                    return hexString.toString();
                } catch (NoSuchAlgorithmException e) {
                    e.printStackTrace();
                }
                return "";
            }






    public static boolean isEmailValid(String email) {
        String expression = "^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$";
        Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }

    private static boolean isPhoneValid(String phone) {
        boolean check;
        if (!Pattern.matches("[a-zA-Z]+", phone)) {
            if (phone.length() < 9 || phone.length() > 10 || phone.charAt(0)!=48) {
                check = false;

            } else {
                check = true;

            }
        } else {
            check = false;
        }
        return check;
    }
}
