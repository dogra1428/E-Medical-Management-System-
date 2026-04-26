import api.GeminiClient;

public class TestGemini {
    public static void main(String[] args) {
        try {
            System.out.println("Testing Gemini Client...");
            GeminiClient client = new GeminiClient("You are a helpful assistant.");
            String response = client.sendMessage("Hello! This is a test.");
            System.out.println("Response: " + response);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
