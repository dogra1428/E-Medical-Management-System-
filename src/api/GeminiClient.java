package api;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;

public class GeminiClient {
    private static final String API_KEY = loadApiKey();
    private static final String API_URL = "https://generativelanguage.googleapis.com/v1beta/models/gemma-3-4b-it:generateContent?key=" + API_KEY;

    private static String loadApiKey() {
        // Try environment variable first
        String key = System.getenv("GEMINI_API_KEY");
        if (key != null && !key.isEmpty()) {
            return key;
        }
        // Try reading from api_key.txt in the project root
        try {
            return java.nio.file.Files.readString(java.nio.file.Paths.get("api_key.txt")).trim();
        } catch (Exception e) {
            System.err.println("Warning: GEMINI_API_KEY environment variable not set and api_key.txt not found.");
            return "YOUR_API_KEY_HERE";
        }
    }
    private HttpClient httpClient;
    private String systemPrompt;
    private List<ChatMessage> history;

    public GeminiClient(String systemPrompt) {
        this.httpClient = HttpClient.newHttpClient();
        this.systemPrompt = systemPrompt;
        this.history = new ArrayList<>();
    }

    public void addHistory(String role, String text) {
        history.add(new ChatMessage(role, text));
    }

    public String sendMessage(String text) throws Exception {
        addHistory("user", text);

        StringBuilder json = new StringBuilder();
        json.append("{\n");
        
        // Contents (history)
        json.append("  \"contents\": [\n");
        for (int i = 0; i < history.size(); i++) {
            ChatMessage msg = history.get(i);
            String textToSend = msg.text;
            
            // Prepend system prompt to the first user message instead of using systemInstruction
            if (i == 0 && msg.role.equals("user") && systemPrompt != null && !systemPrompt.isEmpty()) {
                textToSend = systemPrompt + "\n\n" + msg.text;
            }
            
            json.append("    {\n");
            json.append("      \"role\": \"").append(msg.role).append("\",\n");
            json.append("      \"parts\": [{\"text\": \"").append(escapeJson(textToSend)).append("\"}]\n");
            json.append("    }");
            if (i < history.size() - 1) {
                json.append(",");
            }
            json.append("\n");
        }
        json.append("  ]\n");
        json.append("}");

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(API_URL))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(json.toString()))
                .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        
        if (response.statusCode() != 200) {
            throw new Exception("API Error: " + response.statusCode() + " - " + response.body());
        }

        String responseText = extractText(response.body());
        if (responseText != null) {
            addHistory("model", responseText);
        }
        return responseText;
    }

    private String escapeJson(String text) {
        if (text == null) return "";
        return text.replace("\\", "\\\\")
                   .replace("\"", "\\\"")
                   .replace("\b", "\\b")
                   .replace("\f", "\\f")
                   .replace("\n", "\\n")
                   .replace("\r", "\\r")
                   .replace("\t", "\\t");
    }

    private String extractText(String json) {
        // Find the first "text": "..." in the response
        String search = "\"text\": \"";
        int start = json.indexOf(search);
        if (start == -1) return "Sorry, I couldn't understand that.";
        
        start += search.length();
        // The text can contain escaped quotes. We need to find the unescaped ending quote.
        int end = start;
        while (end < json.length()) {
            if (json.charAt(end) == '"' && json.charAt(end - 1) != '\\') {
                break;
            }
            end++;
        }
        
        String extracted = json.substring(start, end);
        // Unescape the newlines and quotes
        return extracted.replace("\\n", "\n").replace("\\\"", "\"").replace("\\\\", "\\");
    }

    private static class ChatMessage {
        String role;
        String text;
        ChatMessage(String role, String text) {
            this.role = role;
            this.text = text;
        }
    }
}
