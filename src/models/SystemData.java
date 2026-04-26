package models;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class SystemData {
    public static List<Patient> patients = new ArrayList<>();
    public static List<Doctor> doctors = new ArrayList<>();
    public static List<Appointment> appointments = new ArrayList<>();

    private static final String DATA_DIR = "data";
    private static final String PATIENTS_FILE = DATA_DIR + "/patients.txt";
    private static final String DOCTORS_FILE = DATA_DIR + "/doctors.txt";
    private static final String APPOINTMENTS_FILE = DATA_DIR + "/appointments.txt";

    static {
        File dir = new File(DATA_DIR);
        if (!dir.exists()) dir.mkdirs();
        loadAllData();
    }

    public static void loadAllData() {
        patients.clear();
        doctors.clear();
        appointments.clear();

        // Load Patients
        try (BufferedReader br = new BufferedReader(new FileReader(PATIENTS_FILE))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length == 5) {
                    patients.add(new Patient(parts[0], parts[1], Integer.parseInt(parts[2]), parts[3], parts[4]));
                }
            }
        } catch (IOException e) { /* Ignored, file might not exist yet */ }

        // Load Doctors
        try (BufferedReader br = new BufferedReader(new FileReader(DOCTORS_FILE))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length == 4) {
                    doctors.add(new Doctor(parts[0], parts[1], parts[2], parts[3]));
                }
            }
        } catch (IOException e) {}

        // Load Appointments
        try (BufferedReader br = new BufferedReader(new FileReader(APPOINTMENTS_FILE))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",", 6);
                if (parts.length == 6) {
                    Patient p = getPatientById(parts[1]);
                    Doctor d = getDoctorById(parts[2]);
                    if (p != null && d != null) {
                        appointments.add(new Appointment(parts[0], p, d, parts[3], parts[4], parts[5]));
                    }
                }
            }
        } catch (IOException e) {}
    }

    public static void saveData() {
        try (PrintWriter pw = new PrintWriter(new FileWriter(PATIENTS_FILE))) {
            for (Patient p : patients) {
                pw.println(p.getPatientId() + "," + p.getName() + "," + p.getAge() + "," + p.getGender() + "," + p.getContactNumber());
            }
        } catch (IOException e) { e.printStackTrace(); }

        try (PrintWriter pw = new PrintWriter(new FileWriter(DOCTORS_FILE))) {
            for (Doctor d : doctors) {
                pw.println(d.getDoctorId() + "," + d.getName() + "," + d.getSpecialization() + "," + d.getContactNumber());
            }
        } catch (IOException e) { e.printStackTrace(); }

        try (PrintWriter pw = new PrintWriter(new FileWriter(APPOINTMENTS_FILE))) {
            for (Appointment a : appointments) {
                pw.println(a.getAppointmentId() + "," + a.getPatient().getPatientId() + "," + a.getDoctor().getDoctorId() + "," + a.getDate() + "," + a.getTime() + "," + a.getDescription().replace(",", " "));
            }
        } catch (IOException e) { e.printStackTrace(); }
    }

    private static Patient getPatientById(String id) {
        for (Patient p : patients) {
            if (p.getPatientId().equals(id)) return p;
        }
        return null;
    }

    private static Doctor getDoctorById(String id) {
        for (Doctor d : doctors) {
            if (d.getDoctorId().equals(id)) return d;
        }
        return null;
    }
}
