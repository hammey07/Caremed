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

public class medicalPractitionerSignUp extends AppCompatActivity {
    EditText txtFirstname, txtLastname, txtOccupation, txtOrganisationName, txtPhone, txtEmail, txtPassword, txtConfirmPassword;
    Button btn_signup;
    ProgressBar progressBar;
    private FirebaseAuth firebaseAuth;
    DatabaseReference medPracRef;
    Medical_Practitioner medicalPractitioner;
    List<String> errors;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_medical_practitioner_sign_up);

        txtFirstname = (EditText) findViewById(R.id.txtFirstname);
        txtLastname = (EditText) findViewById(R.id.txtLastname);
        txtOccupation = (EditText) findViewById(R.id.txtOccupation);
        txtOrganisationName = (EditText) findViewById(R.id.txtOrganisationName);
        txtPhone = (EditText) findViewById(R.id.txtPhone);
        txtEmail = (EditText) findViewById(R.id.txtEmail);
        txtPassword = (EditText) findViewById(R.id.txtPassword);
        txtConfirmPassword = (EditText) findViewById(R.id.txtConfirmPassword);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        btn_signup = (Button) findViewById(R.id.btn_signup);

        firebaseAuth = FirebaseAuth.getInstance();
        medicalPractitioner = new Medical_Practitioner();
        medPracRef = FirebaseDatabase.getInstance().getReference().child("Medical_Practitioner");
        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);
        getSupportActionBar().setTitle("Medical Practitioner Registration");


        btn_signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String Firstname = txtFirstname.getText().toString().trim();
                String Lastname = txtLastname.getText().toString().trim();
                String Occupation = txtOccupation.getText().toString().trim();
                String OrganisationName = txtOrganisationName.getText().toString().trim();
                String Phone = txtPhone.getText().toString().trim();
                String email = txtEmail.getText().toString().trim();
                String password = txtPassword.getText().toString().trim();
                String confirmPassword = txtConfirmPassword.getText().toString().trim();
                String stringHashPassword = computeMD5Hash(password);
                String stringHashConfirmPassword = computeMD5Hash(confirmPassword);

                errors = new ArrayList<String>();

                if (TextUtils.isEmpty(Firstname)) {
                    errors.add("Please enter your First Name");
                    Toast.makeText(medicalPractitionerSignUp.this, "Please enter your First Name", Toast.LENGTH_LONG).show();
                } else if (TextUtils.isEmpty(Lastname)) {
                    errors.add("Please enter your Surname");
                    Toast.makeText(medicalPractitionerSignUp.this, "Please enter your Surname", Toast.LENGTH_LONG).show();
                } else if (TextUtils.isEmpty(Occupation)) {
                    errors.add("This field can't be empty");
                    Toast.makeText(medicalPractitionerSignUp.this, "This field can't be empty", Toast.LENGTH_LONG).show();
                } else if (TextUtils.isEmpty(Phone)) {
                    errors.add("Please enter your Phone Number");
                    Toast.makeText(medicalPractitionerSignUp.this, "Please enter your Phone Number", Toast.LENGTH_LONG).show();
                } else if (!isPhoneValid(Phone)) {
                    errors.add("Make sure phone number is in valid format\n (08x xxx xxxx) OR (0x xxx xxxx)");
                    Toast.makeText(medicalPractitionerSignUp.this, "Make sure phone number is in valid format\n (08x xxx xxxx) OR (0x xxx xxxx)", Toast.LENGTH_LONG).show();
                } else if (TextUtils.isEmpty(email)) {
                    errors.add("Please enter Email address");
                    Toast.makeText(medicalPractitionerSignUp.this, "Please enter Email address", Toast.LENGTH_LONG).show();
                } else if (!isEmailValid(email)) {
                    errors.add("Please enter a valid Email address\n E.g (john.Doe@gmail.com) or (harry_smith@yahoo.co.uk)");
                    Toast.makeText(medicalPractitionerSignUp.this, "Please enter a valid Email address\n E.g (john.Doe@gmail.com) or (harry_smith@yahoo.co.uk)", Toast.LENGTH_LONG).show();
                } else if (TextUtils.isEmpty(password)) {
                    errors.add("Please enter password");
                    Toast.makeText(medicalPractitionerSignUp.this, "Please enter password", Toast.LENGTH_LONG).show();
                } else if (TextUtils.isEmpty(confirmPassword)) {
                    errors.add("Please confirm password");
                    Toast.makeText(medicalPractitionerSignUp.this, "Please confirm password", Toast.LENGTH_LONG).show();
                } else if (!password.equals(confirmPassword)) {
                    errors.add("Passwords do not match");
                    Toast.makeText(medicalPractitionerSignUp.this, "Passwords do not match", Toast.LENGTH_LONG).show();

                } else if (password.length() < 6) {
                    errors.add("Password must be at least 6 characters long");
                    Toast.makeText(medicalPractitionerSignUp.this, "Password must be at least 6 characters long", Toast.LENGTH_LONG).show();

                }

                if(errors.isEmpty()) {

                    medicalPractitioner.setFirstname(Firstname);
                    medicalPractitioner.setLastname(Lastname);
                    medicalPractitioner.setOcupation(Occupation);
                    medicalPractitioner.setOrganizationName(OrganisationName);
                    medicalPractitioner.setPhone(Phone);
                    medicalPractitioner.setEmail(email);
                    medicalPractitioner.setPassword(stringHashPassword);
                    medicalPractitioner.setConfirmPassword(stringHashConfirmPassword);

                    progressBar.setVisibility(View.VISIBLE);
                    btn_signup.setVisibility(View.GONE);
                    firebaseAuth.createUserWithEmailAndPassword(email, password)
                            .addOnCompleteListener(medicalPractitionerSignUp.this, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    progressBar.setVisibility(View.GONE);
                                    if (task.isSuccessful()) {
                                        medPracRef.push().setValue(medicalPractitioner);
                                        Toast.makeText(medicalPractitionerSignUp.this, "Registration Successful", Toast.LENGTH_LONG).show();
                                        startActivity(new Intent(getApplicationContext(), loginForm.class));


                                    } else {
                                        btn_signup.setVisibility(View.VISIBLE);
                                        Toast.makeText(medicalPractitionerSignUp.this, "Authentication Failed, Email Address has been registered previously", Toast.LENGTH_LONG).show();
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
        boolean check = false;
        if (!Pattern.matches("[a-zA-Z]+", phone)) {
            if (phone.length() < 9 || phone.length() > 10 || phone.charAt(0) != 48) {
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
