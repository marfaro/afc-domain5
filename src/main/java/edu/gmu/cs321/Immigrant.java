package edu.gmu.cs321;

import java.util.ArrayList;
import java.util.List;

public class Immigrant {
    int id;
    String aNumber;
    String firstName;
    String lastName;
    String dob;
    String email;
    String phone;
    String address;
    List<Dependent> dependents;

    public Immigrant(int id, String aNumber, String firstName, String lastName, String dob, String email, String phone, String address) {
        this.id = id;
        this.aNumber = aNumber;
        this.firstName = firstName;
        this.lastName = lastName;
        this.dob = dob;
        this.email = email;
        this.phone = phone;
        this.address = address;
        this.dependents = new ArrayList<>();
    }

    public Immigrant(String aNumber, String firstName, String lastName, String dob, String email, String phone, String address) {
        this.aNumber = aNumber;
        this.firstName = firstName;
        this.lastName = lastName;
        this.dob = dob;
        this.email = email;
        this.phone = phone;
        this.address = address;
        this.dependents = new ArrayList<>();
    }

    public Immigrant(String aNumber) {
        this.aNumber = aNumber;
    }

    public int getId() {
        return id;
    }

    public String getANumber() {
        return aNumber;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName){
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName){
        this.lastName = lastName;
    }

    public String getDob() {
        return dob;
    }

    public void setDob(String dob){
        this.dob = dob;
    }

    public String getEmail() {
        return email;
    }

    public String getPhone() {
        return phone;
    }

    public String getAddress() {
        return address;
    }


    public void addDependent(Dependent dependent) {
        // Ensure the dependents list is initialized before adding
        if (dependents == null) {
            dependents = new ArrayList<>();  // Initialize the list if it was null
        }
        this.dependents.add(dependent);  // Add dependent to the list
    }

    public List<Dependent> getDependents() {
        return dependents;
    }

    
}
