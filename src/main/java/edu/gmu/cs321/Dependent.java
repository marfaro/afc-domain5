package edu.gmu.cs321;

public class Dependent {

    String aNumber; 
    String firstName;
    String lastName;
    String dob;
    String country;
    String relationship;
    private boolean submitted;

    public Dependent(String aNumber, String firstName, String lastName, String dob, String country, String relationship) {
        this.aNumber = aNumber;
        this.firstName = firstName;
        this.lastName = lastName;
        this.dob = dob;
        this.country = country;
        this.relationship = relationship;
        this.submitted = false;
    }

    public Dependent(String firstName, String lastName, String dob, String country, String relationship) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.dob = dob;
        this.country = country;
        this.relationship = relationship;
        this.submitted = false;
    }

    public boolean isSubmitted() {
        return submitted;
    }

    public void setSubmitted(boolean submitted) {
        this.submitted = submitted;
    }

    public String getANumber() {
        return aNumber;
    }

    public void setANumber(String aNumber) {
        this.aNumber = aNumber;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getDob() {
        return dob;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getRelationship() {
        return relationship;
    }

    public void setRelationship(String relationship) {
        this.relationship = relationship;
    }


}