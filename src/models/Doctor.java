package models;

public class Doctor {
    private String doctorId;
    private String name;
    private String specialization;
    private String contactNumber;

    public Doctor(String doctorId, String name, String specialization, String contactNumber) {
        this.doctorId = doctorId;
        this.name = name;
        this.specialization = specialization;
        this.contactNumber = contactNumber;
    }

    public String getDoctorId() { return doctorId; }
    public String getName() { return name; }
    public String getSpecialization() { return specialization; }
    public String getContactNumber() { return contactNumber; }

    @Override
    public String toString() {
        return "Dr. " + name + " - " + specialization + " (" + doctorId + ")";
    }
}
