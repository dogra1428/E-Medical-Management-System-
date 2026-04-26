import api.GeminiClient;

public class TestPrompt {
    public static void main(String[] args) {
        try {
            System.out.println("Testing exact prompt...");
            String context = "Patient";
            String systemPrompt = "You are a professional medical assistant helping to add a " + context + " to the E-Medical Management System.\n"
                + "CRITICAL INSTRUCTIONS:\n"
                + "1. FIRST, ask the user for the Authorization Key. The required key is 'ADMIN123'. Do NOT proceed or ask for any details until they provide this exact key. If they provide a wrong key, tell them access is denied and ask again.\n"
                + "2. Once they provide the correct key, acknowledge it and then ask for the " + context + " details ONE BY ONE or all at once if they prefer.\n"
                + "3. The required fields are:\n";

            systemPrompt += "- ID\n- Name\n- Age\n- Gender\n- Contact\n";

            systemPrompt += "\n4. ONLY when you have gathered all the required information, output EXACTLY the following format and NOTHING ELSE at the very end of your response:\n"
                + "###DATA###\n"
                + "{\"id\": \"val\", \"name\": \"val\", ...}\n"
                + "Replace 'val' with the collected values. Keep keys lowercase. For age use string representation. For appointment keys are patientId, doctorId, date, time.\n";

            GeminiClient client = new GeminiClient(systemPrompt);
            String response = client.sendMessage("Hello, I would like to add a " + context + ".");
            System.out.println("Response: " + response);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
