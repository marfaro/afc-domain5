package edu.gmu.cs321;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.LinkedList;
import java.util.Queue;

public class Workflow {

    private static Workflow instance;
    private Queue<DependentForm> formQueue; 
    private String filePath;     

    private Workflow(String filePath) {
        this.formQueue = new LinkedList<>();
        this.filePath = filePath;
    }

    public static Workflow getInstance(String filePath) {
        if (instance == null) {
            instance = new Workflow("src/resources/workflow.txt");
        }
        return instance;
    }

    public void addDependentForm(DependentForm form) {
        formQueue.add(form);
        System.out.println("DependentForm added to workflow: " + form);
        writeToTextFile(form);  
        printWorkflowContents();  
    }

    public DependentForm getNextDependentForm() {
        if (formQueue.isEmpty()) {
            System.out.println("No more forms in the workflow.");
            return null;
        }
        return formQueue.poll();
    }

    public boolean isEmpty() {
        return formQueue.isEmpty();
    }

    public int getWorkflowSize() {
        return formQueue.size();
    }

    public void printWorkflowContents() {
        System.out.println("Current Workflow Contents:");
        if (formQueue.isEmpty()) {
            System.out.println("No forms in the workflow.");
        } else {
            for (DependentForm form : formQueue) {
                System.out.println("  - " + form);
            }
        }
    }

    private void writeToTextFile(DependentForm form) {
        try {
            File file = new File(filePath);
            FileWriter fw;
            BufferedWriter bw;

            if (file.exists()) {
                fw = new FileWriter(file, true);
            } else {
                fw = new FileWriter(file); 
            }

            bw = new BufferedWriter(fw);

            bw.write("  ANumber: " + form.getANumber());
            bw.newLine();
            bw.write("  First Name: " + form.getFirstName());
            bw.newLine();
            bw.write("  Last Name: " + form.getLastName());
            bw.newLine();
            bw.write("  Date of Birth: " + form.getDob());
            bw.newLine();
            bw.newLine();  

            bw.close();
            System.out.println("Form written to text file: " + filePath);

        } catch (IOException e) {
            System.err.println("Error writing to text file: " + e.getMessage());
        }
    }

    public static void saveImmigrantToFile(Immigrant immigrant) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("src/resources/workflow.txt", true))) {
            writer.write("Immigrant: " + immigrant.aNumber + ", " + immigrant.firstName + ", " + immigrant.lastName + ", " + immigrant.dob + "\n");
            writer.write("Email: " + immigrant.email + "\n");
            writer.write("Phone: " + immigrant.phone + "\n");
            writer.write("Address: " + immigrant.address + "\n");
            writer.write("Dependents:\n");

            for (Dependent dependent : immigrant.getDependents()) {
                saveDependentToFile(immigrant, dependent); // Save dependents
            }

            writer.write("\n---\n");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void saveDependentToFile(Immigrant immigrant, Dependent dependent) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("src/resources/workflow.txt", true))) {
            writer.write("ANumber: " + immigrant.aNumber + "\n");
            writer.write("First Name: " + dependent.firstName + "\n");
            writer.write("Last Name: " + dependent.lastName + "\n");
            writer.write("DOB: " + dependent.dob + "\n");
            writer.write("Country: " + dependent.country + "\n");
            writer.write("Relationship: " + dependent.relationship + "\n");
            writer.write("\n");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}