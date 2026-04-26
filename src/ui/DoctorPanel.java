package ui;

import models.Doctor;
import models.SystemData;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import utils.JsonHelper;

public class DoctorPanel extends JPanel {
    private JTable table;
    private DefaultTableModel tableModel;
    private JTextField idField, nameField, specialtyField, contactField;

    public DoctorPanel() {
        setLayout(new BorderLayout());

        // Top panel for Table
        tableModel = new DefaultTableModel(new String[]{"Doctor ID", "Name", "Specialization", "Contact"}, 0);
        table = new JTable(tableModel);
        updateTable();
        add(new JScrollPane(table), BorderLayout.CENTER);

        // Bottom panel for Form
        JPanel formPanel = new JPanel(new GridLayout(3, 4, 15, 15));
        formPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createTitledBorder("Add New Doctor"),
            BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));

        formPanel.add(new JLabel("Doctor ID:"));
        idField = new JTextField();
        formPanel.add(idField);

        formPanel.add(new JLabel("Name:"));
        nameField = new JTextField();
        formPanel.add(nameField);

        formPanel.add(new JLabel("Specialization:"));
        specialtyField = new JTextField();
        formPanel.add(specialtyField);

        formPanel.add(new JLabel("Contact:"));
        contactField = new JTextField();
        formPanel.add(contactField);

        JButton addButton = new JButton("Add Doctor");
        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addDoctor();
            }
        });
        
        JButton aiButton = new JButton("Add via AI 🤖");
        aiButton.setBackground(new Color(46, 125, 50));
        aiButton.setForeground(Color.WHITE);
        aiButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                AIChatDialog dialog = new AIChatDialog((JFrame) SwingUtilities.getWindowAncestor(DoctorPanel.this), "Doctor", json -> {
                    String id = JsonHelper.getValue(json, "id");
                    String name = JsonHelper.getValue(json, "name");
                    String specialty = JsonHelper.getValue(json, "specialization");
                    String contact = JsonHelper.getValue(json, "contact");
                    
                    Doctor newDoctor = new Doctor(id, name, specialty, contact);
                    SystemData.doctors.add(newDoctor);
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
        formPanel.add(new JLabel("")); // Spacer
        formPanel.add(new JLabel("")); // Spacer
        formPanel.add(buttonPanel);

        add(formPanel, BorderLayout.SOUTH);
    }

    private void updateTable() {
        tableModel.setRowCount(0);
        for (Doctor d : SystemData.doctors) {
            tableModel.addRow(new Object[]{d.getDoctorId(), d.getName(), d.getSpecialization(), d.getContactNumber()});
        }
    }

    private void addDoctor() {
        String id = idField.getText();
        String name = nameField.getText();
        String specialty = specialtyField.getText();
        String contact = contactField.getText();

        if (id.isEmpty() || name.isEmpty() || specialty.isEmpty() || contact.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please fill all fields.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        Doctor newDoctor = new Doctor(id, name, specialty, contact);
        SystemData.doctors.add(newDoctor);
        SystemData.saveData();
        updateTable();

        // Clear fields
        idField.setText("");
        nameField.setText("");
        specialtyField.setText("");
        contactField.setText("");
        JOptionPane.showMessageDialog(this, "Doctor added successfully!");
    }
}
