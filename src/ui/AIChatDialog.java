package ui;

import api.GeminiClient;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.function.Consumer;

public class AIChatDialog extends JDialog {
    private JTextArea chatArea;
    private JTextField inputField;
    private JButton sendButton;
    private GeminiClient geminiClient;
    private String context;
    private Consumer<String> onDataReceived;

    public AIChatDialog(JFrame parent, String context, Consumer<String> onDataReceived) {
        super(parent, "AI Assistant - " + context, true);
        this.context = context;
        this.onDataReceived = onDataReceived;

        setSize(450, 550);
        setLocationRelativeTo(parent);
        setLayout(new BorderLayout());

        chatArea = new JTextArea();
        chatArea.setEditable(false);
        chatArea.setLineWrap(true);
        chatArea.setWrapStyleWord(true);
        chatArea.setMargin(new Insets(10, 10, 10, 10));
        chatArea.setFont(new Font("Segoe UI", Font.PLAIN, 14));

        JScrollPane scrollPane = new JScrollPane(chatArea);
        add(scrollPane, BorderLayout.CENTER);

        JPanel inputPanel = new JPanel(new BorderLayout(5, 5));
        inputPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        inputField = new JTextField();
        inputField.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        sendButton = new JButton("Send");
        sendButton.setBackground(new Color(64, 116, 175));
        sendButton.setForeground(Color.WHITE);
        sendButton.setFont(new Font("Segoe UI", Font.BOLD, 13));

        inputPanel.add(inputField, BorderLayout.CENTER);
        inputPanel.add(sendButton, BorderLayout.EAST);

        add(inputPanel, BorderLayout.SOUTH);

        setupAI();

        ActionListener sendAction = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String text = inputField.getText().trim();
                if (!text.isEmpty()) {
                    sendMessage(text);
                }
            }
        };

        sendButton.addActionListener(sendAction);
        inputField.addActionListener(sendAction);
    }

    private void setupAI() {
        String systemPrompt = "You are a professional medical assistant helping to add a " + context + " to the E-Medical Management System.\n"
                + "CRITICAL INSTRUCTIONS:\n"
                + "1. FIRST, ask the user for the Authorization Key. The required key is 'ADMIN123'. Do NOT proceed or ask for any details until they provide this exact key. If they provide a wrong key, tell them access is denied and ask again.\n"
                + "2. Once they provide the correct key, acknowledge it and then ask for the " + context + " details ONE BY ONE or all at once if they prefer.\n"
                + "3. The required fields are:\n";

        if (context.equalsIgnoreCase("Patient")) {
            systemPrompt += "- ID\n- Name\n- Age\n- Gender\n- Contact\n";
        } else if (context.equalsIgnoreCase("Doctor")) {
            systemPrompt += "- ID\n- Name\n- Specialization\n- Contact\n";
        } else if (context.equalsIgnoreCase("Appointment")) {
            systemPrompt += "- Patient ID\n- Doctor ID\n- Date\n- Time\n";
        }

        systemPrompt += "\n4. ONLY when you have gathered all the required information, output EXACTLY the following format and NOTHING ELSE at the very end of your response:\n"
                + "###DATA###\n"
                + "{\"id\": \"val\", \"name\": \"val\", ...}\n"
                + "Replace 'val' with the collected values. Keep keys lowercase. For age use string representation. For appointment keys are patientId, doctorId, date, time.\n";

        geminiClient = new GeminiClient(systemPrompt);
        appendMessage("System", "AI Assistant initialized. Waiting for connection...");

        // Initial greeting
        SwingWorker<String, Void> worker = new SwingWorker<>() {
            @Override
            protected String doInBackground() throws Exception {
                // Send an invisible initial prompt to trigger the AI to ask for the key
                return geminiClient.sendMessage("Hello, I would like to add a " + context + ".");
            }

            @Override
            protected void done() {
                try {
                    String reply = get();
                    chatArea.setText(""); // Clear waiting message
                    appendMessage("AI", reply);
                } catch (Exception ex) {
                    if (ex.getMessage() != null && ex.getMessage().contains("429")) {
                        appendMessage("Error", "Google API Rate Limit Exceeded.\nThe free tier only allows 15 requests per minute.\nPlease wait 30-60 seconds and try again.");
                    } else {
                        appendMessage("Error", "Failed to connect to AI: " + ex.getMessage());
                    }
                    ex.printStackTrace();
                }
            }
        };
        worker.execute();
    }

    private void sendMessage(String text) {
        appendMessage("You", text);
        inputField.setText("");
        inputField.setEnabled(false);
        sendButton.setEnabled(false);

        SwingWorker<String, Void> worker = new SwingWorker<>() {
            @Override
            protected String doInBackground() throws Exception {
                return geminiClient.sendMessage(text);
            }

            @Override
            protected void done() {
                try {
                    String reply = get();
                    
                    // Check for DATA block
                    if (reply.contains("###DATA###")) {
                        int index = reply.indexOf("###DATA###");
                        String chatReply = reply.substring(0, index).trim();
                        String jsonBlock = reply.substring(index + 10).trim();
                        
                        if (!chatReply.isEmpty()) {
                            appendMessage("AI", chatReply);
                        }
                        
                        onDataReceived.accept(jsonBlock);
                        
                        JOptionPane.showMessageDialog(AIChatDialog.this, context + " added successfully via AI!", "Success", JOptionPane.INFORMATION_MESSAGE);
                        dispose();
                    } else {
                        appendMessage("AI", reply);
                    }
                } catch (Exception ex) {
                    if (ex.getMessage() != null && ex.getMessage().contains("429")) {
                        appendMessage("Error", "API Rate Limit Exceeded. Please wait 60 seconds and try again.");
                    } else {
                        appendMessage("Error", "Communication failed: " + ex.getMessage());
                    }
                } finally {
                    inputField.setEnabled(true);
                    sendButton.setEnabled(true);
                    inputField.requestFocus();
                }
            }
        };
        worker.execute();
    }

    private void appendMessage(String sender, String message) {
        chatArea.append(sender + ": " + message + "\n\n");
        chatArea.setCaretPosition(chatArea.getDocument().getLength());
    }
}
