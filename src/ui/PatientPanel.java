package ui;

import models.Patient;
import models.SystemData;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import utils.JsonHelper;

public class PatientPanel extends JPanel {
    private JTable table;
    private DefaultTableModel tableModel;
    private JTextField idField, nameField, ageField, genderField, contactField;

    public PatientPanel() {
        setLayout(new BorderLayout());

        // Top panel for Table
        tableModel = new DefaultTableModel(new String[] { "ID", "Name", "Age", "Gender", "Contact" }, 0);
        table = new JTable(tableModel);
        updateTable();
        add(new JScrollPane(table), BorderLayout.CENTER);

        // Bottom panel for Form
        JPanel formPanel = new JPanel(new GridLayout(3, 4, 15, 15));
        formPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createTitledBorder("Add New Patient"),
            BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));

        formPanel.add(new JLabel("Patient ID:"));
        idField = new JTextField();
        formPanel.add(idField);

        formPanel.add(new JLabel("Name:"));
        nameField = new JTextField();
        formPanel.add(nameField);

        formPanel.add(new JLabel("Age:"));
        ageField = new JTextField();
        formPanel.add(ageField);

        formPanel.add(new JLabel("Gender:"));
        genderField = new JTextField();
        formPanel.add(genderField);

        formPanel.add(new JLabel("Contact:"));
        contactField = new JTextField();
        formPanel.add(contactField);

        JButton addButton = new JButton("Add Patient");
        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addPatient();
            }
        });

        JButton aiButton = new JButton("Add via AI 🤖");
        aiButton.setBackground(new Color(46, 125, 50)); // Success dark green
        aiButton.setForeground(Color.WHITE);
        aiButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                AIChatDialog dialog = new AIChatDialog((JFrame) SwingUtilities.getWindowAncestor(PatientPanel.this), "Patient", json -> {
                    String id = JsonHelper.getValue(json, "id");
                    String name = JsonHelper.getValue(json, "name");
                    String ageStr = JsonHelper.getValue(json, "age");
                    String gender = JsonHelper.getValue(json, "gender");
                    String contact = JsonHelper.getValue(json, "contact");
                    
                    int age = 0;
                    try { age = Integer.parseInt(ageStr); } catch (Exception ignored) {}
                    
                    Patient newPatient = new Patient(id, name, age, gender, contact);
                    SystemData.patients.add(newPatient);
                    SystemData.saveData();
                    updateTable();
                });
                dialog.setVisible(true);
            }
        });

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        buttonPanel.add(aiButton);
        buttonPanel.add(addButton);

        formPanel.add(new JLabel("")); // Spacer
        formPanel.add(buttonPanel);

        add(formPanel, BorderLayout.SOUTH);
    }

    private void updateTable() {
        tableModel.setRowCount(0);
        for (Patient p : SystemData.patients) {
            tableModel.addRow(
                    new Object[] { p.getPatientId(), p.getName(), p.getAge(), p.getGender(), p.getContactNumber() });
        }
    }

    private void addPatient() {
        try {
            String id = idField.getText();
            String name = nameField.getText();
            int age = Integer.parseInt(ageField.getText());
            String gender = genderField.getText();
            String contact = contactField.getText();

            if (id.isEmpty() || name.isEmpty() || gender.isEmpty() || contact.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please fill all fields.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            Patient newPatient = new Patient(id, name, age, gender, contact);
            SystemData.patients.add(newPatient);
            SystemData.saveData();
            updateTable();

            // Clear fields
            idField.setText("");
            nameField.setText("");
            ageField.setText("");
            genderField.setText("");
            contactField.setText("");
            JOptionPane.showMessageDialog(this, "Patient added successfully!");
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Age must be a valid number.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
