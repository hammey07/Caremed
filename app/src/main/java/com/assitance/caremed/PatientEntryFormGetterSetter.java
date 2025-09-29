package com.assitance.caremed;

public class PatientEntryFormGetterSetter {

    private String MedicationName;
    private String DosageFrequency;
    private String DosageAmount;
    private String DosageUnit;
    private String Email;

    public PatientEntryFormGetterSetter() {
    }

    public PatientEntryFormGetterSetter(String medicationName, String dosageFrequency, String dosageAmount, String dosageUnit, String email) {
        MedicationName = medicationName;
        DosageFrequency = dosageFrequency;
        DosageAmount = dosageAmount;
        DosageUnit = dosageUnit;
        Email = email;
    }

    public String getMedicationName() {
        return MedicationName;
    }

    public void setMedicationName(String medicationName) {
        MedicationName = medicationName;
    }

    public String getDosageFrequency() {
        return DosageFrequency;
    }

    public void setDosageFrequency(String dosageFrequency) {
        DosageFrequency = dosageFrequency;
    }

    public String getDosageAmount() {
        return DosageAmount;
    }

    public void setDosageAmount(String dosageAmount) {
        DosageAmount = dosageAmount;
    }

    public String getDosageUnit() {
        return DosageUnit;
    }

    public void setDosageUnit(String dosageUnit) {
        DosageUnit = dosageUnit;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }


}
