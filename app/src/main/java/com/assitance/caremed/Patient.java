package com.assitance.caremed;

public class Patient {
    private String autoGenID;
    private String Firstname;
    private String Lastname;
    private String PatientPhone;
    private String CaregiverName;
    private String CaregiverPhone;
    private String PatientEmail;
    private String Password;
    private String ConfirmPassword;

    public Patient() {
    }

    public Patient(String autoGenID, String firstname, String lastname, String patientPhone, String caregiverName, String caregiverPhone, String patientEmail, String password, String confirmPassword) {
        this.autoGenID = autoGenID;
        Firstname = firstname;
        Lastname = lastname;
        PatientPhone = patientPhone;
        CaregiverName = caregiverName;
        CaregiverPhone = caregiverPhone;
        PatientEmail = patientEmail;
        Password = password;
        ConfirmPassword = confirmPassword;

    }

    public String getAutoGenID() {
        return autoGenID;
    }

    public void setAutoGenID(String autoGenID) {
        this.autoGenID = autoGenID;
    }

    public String getFirstname() {
        return Firstname;
    }

    public void setFirstname(String firstname) {
        Firstname = firstname;
    }

    public String getLastname() {
        return Lastname;
    }

    public void setLastname(String lastname) {
        Lastname = lastname;
    }

    public String getPatientPhone() {
        return PatientPhone;
    }

    public void setPatientPhone(String patientPhone) {
        PatientPhone = patientPhone;
    }

    public String getCaregiverName() {
        return CaregiverName;
    }

    public void setCaregiverName(String caregiverName) {
        CaregiverName = caregiverName;
    }

    public String getCaregiverPhone() {
        return CaregiverPhone;
    }

    public void setCaregiverPhone(String caregiverPhone) {
        CaregiverPhone = caregiverPhone;
    }

    public String getPatientEmail() {
        return PatientEmail;
    }

    public void setPatientEmail(String patientEmail) {
        PatientEmail = patientEmail;
    }

    public String getPassword() {
        return Password;
    }

    public void setPassword(String password) {
        Password = password;
    }

    public String getConfirmPassword() {
        return ConfirmPassword;
    }

    public void setConfirmPassword(String confirmPassword) {
        ConfirmPassword = confirmPassword;
    }

}