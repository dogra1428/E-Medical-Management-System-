package ui;

import com.formdev.flatlaf.FlatDarkLaf;
import models.SystemData;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;

public class MainFrame extends JFrame {
    private JTabbedPane tabbedPane;
    private JPanel dashboardPanel;
    private PatientPanel patientPanel;
    private DoctorPanel doctorPanel;
    private AppointmentPanel appointmentPanel;
    
    private JLabel totalPatientsLabel;
    private JLabel totalDoctorsLabel;
    private JLabel totalAppointmentsLabel;

    public MainFrame() {
        setTitle("E-Medical Management System - Professional Edition");
        setSize(900, 650);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Customize UI Defaults for a more professional look
        UIManager.put("TabbedPane.tabHeight", 45);
        UIManager.put("TabbedPane.font", new Font("Segoe UI", Font.BOLD, 14));
        UIManager.put("Label.font", new Font("Segoe UI", Font.PLAIN, 14));
        UIManager.put("Button.font", new Font("Segoe UI", Font.BOLD, 14));
        UIManager.put("Table.font", new Font("Segoe UI", Font.PLAIN, 14));
        UIManager.put("TableHeader.font", new Font("Segoe UI", Font.BOLD, 14));
        UIManager.put("TextField.margin", new Insets(6, 12, 6, 12));
        
        // FlatLaf Rounded Corners & Styling
        UIManager.put("Button.arc", 15);
        UIManager.put("Component.arc", 15);
        UIManager.put("TextComponent.arc", 15);
        
        // Table Styling
        UIManager.put("Table.rowHeight", 35);
        UIManager.put("Table.showHorizontalLines", true);
        UIManager.put("Table.showVerticalLines", false);
        UIManager.put("Table.gridColor", new Color(60, 63, 65));
        UIManager.put("Table.alternateRowColor", new Color(45, 48, 50));
        UIManager.put("Table.selectionBackground", new Color(43, 67, 100));
        
        // Scrollbar Styling
        UIManager.put("ScrollBar.thumbArc", 999);
        UIManager.put("ScrollBar.thumbInsets", new Insets(2, 2, 2, 2));
        
        tabbedPane = new JTabbedPane();

        createDashboard();
        patientPanel = new PatientPanel();
        doctorPanel = new DoctorPanel();
        appointmentPanel = new AppointmentPanel();

        tabbedPane.addTab("Dashboard", null, dashboardPanel, "Overview");
        tabbedPane.addTab("Patients", null, patientPanel, "Manage Patients");
        tabbedPane.addTab("Doctors", null, doctorPanel, "Manage Doctors");
        tabbedPane.addTab("Appointments", null, appointmentPanel, "Manage Appointments");

        tabbedPane.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                int selectedIndex = tabbedPane.getSelectedIndex();
                if (selectedIndex == 0) {
                    refreshDashboard();
                } else if (selectedIndex == 3) {
                    appointmentPanel.refreshTableAndBoxes();
                }
            }
        });

        // Add padding to the whole frame
        JPanel contentPane = new JPanel(new BorderLayout());
        contentPane.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        contentPane.add(tabbedPane, BorderLayout.CENTER);
        
        setContentPane(contentPane);
    }
    
    private void createDashboard() {
        dashboardPanel = new JPanel(new BorderLayout(20, 20));
        dashboardPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        // Header Panel
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(new Color(35, 38, 40));
        headerPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(60, 63, 65), 1, true),
            BorderFactory.createEmptyBorder(25, 30, 25, 30)
        ));
        
        JLabel welcomeLabel = new JLabel("Welcome back to E-Medical System");
        welcomeLabel.setFont(new Font("Segoe UI", Font.BOLD, 28));
        welcomeLabel.setForeground(new Color(240, 240, 240));
        
        java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("EEEE, MMMM d, yyyy");
        JLabel dateLabel = new JLabel(sdf.format(new java.util.Date()));
        dateLabel.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        dateLabel.setForeground(new Color(129, 199, 132)); // Success green tint
        
        headerPanel.add(welcomeLabel, BorderLayout.NORTH);
        headerPanel.add(Box.createVerticalStrut(10), BorderLayout.CENTER);
        headerPanel.add(dateLabel, BorderLayout.SOUTH);
        
        // Stats Panel
        JPanel statsPanel = new JPanel(new GridLayout(1, 3, 20, 20));
        statsPanel.setBorder(BorderFactory.createEmptyBorder(20, 0, 0, 0));
        totalPatientsLabel = createStatCard();
        totalDoctorsLabel = createStatCard();
        totalAppointmentsLabel = createStatCard();
        statsPanel.add(totalPatientsLabel);
        statsPanel.add(totalDoctorsLabel);
        statsPanel.add(totalAppointmentsLabel);
        
        JPanel topContainer = new JPanel(new BorderLayout());
        topContainer.add(headerPanel, BorderLayout.NORTH);
        topContainer.add(statsPanel, BorderLayout.CENTER);
        
        dashboardPanel.add(topContainer, BorderLayout.NORTH);
        
        // Quick Info Panel
        JPanel recentPanel = new JPanel(new BorderLayout());
        recentPanel.setBackground(new Color(40, 43, 45));
        recentPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(60, 63, 65), 1, true),
            BorderFactory.createEmptyBorder(20, 20, 20, 20)
        ));
        
        JLabel infoLabel = new JLabel("<html><center><h2 style='color:#E0E0E0; font-family: Segoe UI;'>Fast & Intelligent Medical Management</h2>"
            + "<p style='color:#A0A0A0; font-family: Segoe UI; font-size: 14px;'>Manage patients, doctors, and appointments seamlessly.</p>"
            + "<br><br><p style='color: #64B5F6; font-family: Segoe UI; font-size: 16px;'>Use the <b>Add via AI 🤖</b> button in each tab to add records using natural language!</p></center></html>");
        infoLabel.setHorizontalAlignment(SwingConstants.CENTER);
        recentPanel.add(infoLabel, BorderLayout.CENTER);
        
        dashboardPanel.add(recentPanel, BorderLayout.CENTER);
        
        refreshDashboard();
    }
    
    private JLabel createStatCard() {
        JLabel label = new JLabel("", SwingConstants.CENTER);
        label.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(60, 63, 65), 1, true),
                BorderFactory.createEmptyBorder(20, 20, 20, 20)
        ));
        label.setOpaque(true);
        label.setBackground(new Color(45, 48, 50));
        return label;
    }
    
    private void refreshDashboard() {
        totalPatientsLabel.setText("<html><center><h2 style='color:#B0BEC5; font-family: Segoe UI;'>Total Patients</h2><h1 style='color:#64B5F6; font-size: 48px; font-family: Segoe UI;'>" + SystemData.patients.size() + "</h1></center></html>");
        totalDoctorsLabel.setText("<html><center><h2 style='color:#B0BEC5; font-family: Segoe UI;'>Total Doctors</h2><h1 style='color:#81C784; font-size: 48px; font-family: Segoe UI;'>" + SystemData.doctors.size() + "</h1></center></html>");
        totalAppointmentsLabel.setText("<html><center><h2 style='color:#B0BEC5; font-family: Segoe UI;'>Appointments</h2><h1 style='color:#FFB74D; font-size: 48px; font-family: Segoe UI;'>" + SystemData.appointments.size() + "</h1></center></html>");
    }

    public static void main(String[] args) {
        // Set FlatLaf Dark Theme
        try {
            UIManager.setLookAndFeel(new FlatDarkLaf());
        } catch (Exception ex) {
            System.err.println("Failed to initialize LaF");
        }

        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new MainFrame().setVisible(true);
            }
        });
    }
}
