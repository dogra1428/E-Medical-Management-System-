package models;

public class Appointment {
    private String appointmentId;
    private Patient patient;
    private Doctor doctor;
    private String date;
    private String time;
    private String description;

    public Appointment(String appointmentId, Patient patient, Doctor doctor, String date, String time, String description) {
        this.appointmentId = appointmentId;
        this.patient = patient;
        this.doctor = doctor;
        this.date = date;
        this.time = time;
        this.description = description;
    }

    public String getAppointmentId() { return appointmentId; }
    public Patient getPatient() { return patient; }
    public Doctor getDoctor() { return doctor; }
    public String getDate() { return date; }
    public String getTime() { return time; }
    public String getDescription() { return description; }

    @Override
    public String toString() {
        return "Apt " + appointmentId + ": " + patient.getName() + " with " + doctor.getName() + " on " + date + " " + time;
    }
}
