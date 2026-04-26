package ui;

import models.Appointment;
import models.Doctor;
import models.Patient;
import models.SystemData;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import utils.JsonHelper;

public class AppointmentPanel extends JPanel {
    private JTable table;
    private DefaultTableModel tableModel;
    private JTextField idField, dateField, timeField, descField;
    private JComboBox<Patient> patientComboBox;
    private JComboBox<Doctor> doctorComboBox;

    public AppointmentPanel() {
        setLayout(new BorderLayout());

        // Top panel for Table
        tableModel = new DefaultTableModel(new String[]{"Apt ID", "Patient", "Doctor", "Date", "Time", "Description"}, 0);
        table = new JTable(tableModel);
        updateTable();
        add(new JScrollPane(table), BorderLayout.CENTER);

        // Bottom panel for Form
        JPanel formPanel = new JPanel(new GridLayout(4, 4, 15, 15));
        formPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createTitledBorder("Schedule Appointment"),
            BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));

        formPanel.add(new JLabel("Apt ID:"));
        idField = new JTextField();
        formPanel.add(idField);

        formPanel.add(new JLabel("Select Patient:"));
        patientComboBox = new JComboBox<>();
        formPanel.add(patientComboBox);

        formPanel.add(new JLabel("Select Doctor:"));
        doctorComboBox = new JComboBox<>();
        formPanel.add(doctorComboBox);

        formPanel.add(new JLabel("Date (YYYY-MM-DD):"));
        dateField = new JTextField();
        formPanel.add(dateField);

        formPanel.add(new JLabel("Time (HH:MM AM/PM):"));
        timeField = new JTextField();
        formPanel.add(timeField);

        formPanel.add(new JLabel("Description:"));
        descField = new JTextField();
        formPanel.add(descField);

        JButton addButton = new JButton("Schedule");
        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addAppointment();
            }
        });
        
        JButton aiButton = new JButton("Schedule via AI 🤖");
        aiButton.setBackground(new Color(46, 125, 50));
        aiButton.setForeground(Color.WHITE);
        aiButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                AIChatDialog dialog = new AIChatDialog((JFrame) SwingUtilities.getWindowAncestor(AppointmentPanel.this), "Appointment", json -> {
                    String id = JsonHelper.getValue(json, "id");
                    if (id.isEmpty()) id = "APT" + (SystemData.appointments.size() + 1); // fallback
                    String patientId = JsonHelper.getValue(json, "patientId");
                    String doctorId = JsonHelper.getValue(json, "doctorId");
                    String date = JsonHelper.getValue(json, "date");
                    String time = JsonHelper.getValue(json, "time");
                    String desc = "AI Scheduled Appointment";
                    
                    Patient p = null;
                    for (Patient pat : SystemData.patients) {
                        if (pat.getPatientId().equalsIgnoreCase(patientId)) { p = pat; break; }
                    }
                    Doctor d = null;
                    for (Doctor doc : SystemData.doctors) {
                        if (doc.getDoctorId().equalsIgnoreCase(doctorId)) { d = doc; break; }
                    }
                    
                    if (p != null && d != null) {
                        Appointment newAppt = new Appointment(id, p, d, date, time, desc);
                        SystemData.appointments.add(newAppt);
                        SystemData.saveData();
                        updateTable();
                    } else {
                        JOptionPane.showMessageDialog(AppointmentPanel.this, "Could not find Patient ID or Doctor ID. Appointment not added.", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                });
                dialog.setVisible(true);
            }
        });

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        buttonPanel.add(aiButton);
        buttonPanel.add(addButton);

        formPanel.add(new JLabel("")); // Spacer
        formPanel.add(buttonPanel);

        refreshComboBoxes();

        add(formPanel, BorderLayout.SOUTH);
    }

    private void updateTable() {
        tableModel.setRowCount(0);
        for (Appointment a : SystemData.appointments) {
            tableModel.addRow(new Object[]{
                    a.getAppointmentId(), 
                    a.getPatient().getName(), 
                    a.getDoctor().getName(), 
                    a.getDate(), 
                    a.getTime(), 
                    a.getDescription()
            });
        }
    }

    public void refreshComboBoxes() {
        patientComboBox.removeAllItems();
        for (Patient p : SystemData.patients) {
            patientComboBox.addItem(p);
        }

        doctorComboBox.removeAllItems();
        for (Doctor d : SystemData.doctors) {
            doctorComboBox.addItem(d);
        }
    }

    public void refreshTableAndBoxes() {
        updateTable();
        refreshComboBoxes();
    }

    private void addAppointment() {
        String id = idField.getText();
        Patient patient = (Patient) patientComboBox.getSelectedItem();
        Doctor doctor = (Doctor) doctorComboBox.getSelectedItem();
        String date = dateField.getText();
        String time = timeField.getText();
        String desc = descField.getText();

        if (id.isEmpty() || patient == null || doctor == null || date.isEmpty() || time.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please fill all required fields and select patient/doctor.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        Appointment newAppt = new Appointment(id, patient, doctor, date, time, desc);
        SystemData.appointments.add(newAppt);
        SystemData.saveData();
        updateTable();

        // Clear fields
        idField.setText("");
        dateField.setText("");
        timeField.setText("");
        descField.setText("");
        JOptionPane.showMessageDialog(this, "Appointment scheduled successfully!");
    }
}
