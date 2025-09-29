package com.assitance.caremed;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
//import androidx.annotation.Nullable;
//import android.support.v7.app.AlertDialog;
//import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

public class loginForm extends AppCompatActivity {
    EditText txtEmail, txtPassword;
    TextView forgetPassword;
    Button btn_login;
    Button btn_signup;
    boolean found = false;
    private DatabaseReference patientRef;
    private DatabaseReference medicalRef;
    ProgressBar progressBar;
    private FirebaseAuth firebaseAuth;
    List<String> errors;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        FirebaseApp.initializeApp(this);
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_login_form);

        txtEmail = (EditText) findViewById(R.id.txtEmail);
        txtPassword = (EditText) findViewById(R.id.txtPassword);
        forgetPassword = (TextView) findViewById(R.id.forgetPassword);
        btn_login = (Button) findViewById(R.id.btn_login);
        btn_signup = (Button) findViewById(R.id.btn_signup);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();
        patientRef = FirebaseDatabase.getInstance().getReference().child("Patient");
        medicalRef = FirebaseDatabase.getInstance().getReference().child("Medical_Practitioner");


        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String email = txtEmail.getText().toString().trim();
                String password = txtPassword.getText().toString().trim();
                errors = new ArrayList<String>();
                if (TextUtils.isEmpty(email)) {
                    errors.add("Please enter Email Address: ");
                    Toast.makeText(loginForm.this, "Please enter Email Address: ", Toast.LENGTH_LONG).show();
                } else if (TextUtils.isEmpty(password)) {
                    errors.add("Please enter password");
                    Toast.makeText(loginForm.this, "Please enter password", Toast.LENGTH_LONG).show();

                } else if (password.length() < 6) {
                    errors.add("Password must be over 6 characters long");
                    Toast.makeText(loginForm.this, "Password must be over 6 characters long", Toast.LENGTH_LONG).show();
                }
                if (errors.isEmpty()) {
                    progressBar.setVisibility(View.VISIBLE);
                    btn_login.setVisibility(View.GONE);
                    btn_signup.setVisibility(View.GONE);

                    firebaseAuth.signInWithEmailAndPassword(email, password)
                            .addOnCompleteListener(loginForm.this, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
                                        final FirebaseUser user = firebaseAuth.getCurrentUser();

                                        patientRef.addListenerForSingleValueEvent(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(DataSnapshot dataSnapshot) {
                                                for (DataSnapshot areaSnapshot : dataSnapshot.getChildren()) {
                                                    Patient patient = areaSnapshot.getValue(Patient.class);
                                                    if(patient.getPatientEmail().equals(email)){
                                                        Toast.makeText(loginForm.this, "login Patient Successful", Toast.LENGTH_LONG).show();

                                                        finish();
                                                        startActivity(new Intent(loginForm.this, PatientUI.class));
//
                                                    }
                                                }                                          }

                                            @Override
                                            public void onCancelled(DatabaseError databaseError) {

                                            }
                                        });

                                        medicalRef.addListenerForSingleValueEvent(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(DataSnapshot dataSnapshot) {
                                                for (DataSnapshot areaSnapshot : dataSnapshot.getChildren()) {
                                                    Medical_Practitioner medicalPrac = areaSnapshot.getValue(Medical_Practitioner.class);
                                                    if(medicalPrac.getEmail().equals(email)){
                                                        Toast.makeText(loginForm.this, "login Medical Successful", Toast.LENGTH_LONG).show();

                                                        finish();
                                                        startActivity(new Intent(loginForm.this, medicalPracUI.class));
//
                                                    }
                                                }                                          }

                                            @Override
                                            public void onCancelled(DatabaseError databaseError) {

                                            }
                                        });
//
                                        btn_login.setVisibility(View.VISIBLE);
                                        btn_signup.setVisibility(View.VISIBLE);
                                        progressBar.setVisibility(View.GONE);


                                    }
                                    else {
                                        Toast.makeText(loginForm.this, "Authentication Failed. Please check if email and password are correct!", Toast.LENGTH_LONG).show();

                                        btn_login.setVisibility(View.VISIBLE);
                                        btn_signup.setVisibility(View.VISIBLE);
                                        progressBar.setVisibility(View.GONE);

                                    }
                                }
                            });
                }
            }
        });

        forgetPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = txtEmail.getText().toString().trim();
                if (TextUtils.isEmpty(email)) {
                    Toast.makeText(getApplication(), "Please Enter only Email address to reset your password ", Toast.LENGTH_LONG).show();
                    return;
                }

                progressBar.setVisibility(View.VISIBLE);

                firebaseAuth.sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(loginForm.this, "We have sent you instructions to reset your password!", Toast.LENGTH_LONG).show();
                        } else {
                            Toast.makeText(loginForm.this, "Failed to send reset email!", Toast.LENGTH_LONG).show();
                        }

                        progressBar.setVisibility(View.GONE);
                    }
                });
            }
        });
    }

    public void btn_signupform(View view) {
        startActivity(new Intent(getApplicationContext(), registrationMenu.class));
    }

    public void onBackPressed() {
        new AlertDialog.Builder(this)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setTitle("Quit Application")
                .setMessage("Are you sure you want to Leave?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finishAffinity();


                    }

                })
                .setNegativeButton("No", null)
                .show();
    }
}
