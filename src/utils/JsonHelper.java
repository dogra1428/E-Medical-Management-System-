package utils;

public class JsonHelper {
    public static String getValue(String json, String key) {
        String search = "\"" + key + "\": \"";
        int start = json.indexOf(search);
        if (start == -1) {
            // Try without space after colon
            search = "\"" + key + "\":\"";
            start = json.indexOf(search);
        }
        if (start == -1) return "";
        
        start += search.length();
        int end = start;
        while (end < json.length()) {
            if (json.charAt(end) == '"' && json.charAt(end - 1) != '\\') {
                break;
            }
            end++;
        }
        if (end == json.length()) return "";
        return json.substring(start, end).replace("\\\"", "\"").replace("\\\\", "\\");
    }
}
