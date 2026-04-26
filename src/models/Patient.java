package models;

public class Patient {
    private String patientId;
    private String name;
    private int age;
    private String gender;
    private String contactNumber;

    public Patient(String patientId, String name, int age, String gender, String contactNumber) {
        this.patientId = patientId;
        this.name = name;
        this.age = age;
        this.gender = gender;
        this.contactNumber = contactNumber;
    }

    public String getPatientId() { return patientId; }
    public String getName() { return name; }
    public int getAge() { return age; }
    public String getGender() { return gender; }
    public String getContactNumber() { return contactNumber; }

    @Override
    public String toString() {
        return name + " (" + patientId + ")";
    }
}
