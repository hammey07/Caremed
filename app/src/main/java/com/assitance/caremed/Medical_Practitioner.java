package com.assitance.caremed;

public class Medical_Practitioner {

    private String Firstname;
    private String Lastname;
    private String Ocupation;
    private String OrganizationName;
    private String Phone;
    private String Email;
    private String Password;
    private String ConfirmPassword;

    public Medical_Practitioner() {
    }

    public Medical_Practitioner(String firstname, String lastname, String ocupation, String organizationName,
                                String phone, String email, String password, String confirmPassword) {
        Firstname = firstname;
        Lastname = lastname;
        Ocupation = ocupation;
        OrganizationName = organizationName;
        Phone = phone;
        Email = email;
        Password = password;
        ConfirmPassword = confirmPassword;
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

    public String getOcupation() {
        return Ocupation;
    }

    public void setOcupation(String ocupation) {
        Ocupation = ocupation;
    }

    public String getOrganizationName() {
        return OrganizationName;
    }

    public void setOrganizationName(String organizationName) {
        OrganizationName = organizationName;
    }

    public String getPhone() {
        return Phone;
    }

    public void setPhone(String phone) {
        Phone = phone;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
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