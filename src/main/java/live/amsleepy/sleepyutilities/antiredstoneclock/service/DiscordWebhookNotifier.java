package live.amsleepy.sleepyutilities.notification;

import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class DiscordWebhookNotifier {
    private final String webhookUrl;

    public DiscordWebhookNotifier(String webhookUrl) {
        this.webhookUrl = webhookUrl;
    }

    public void sendEmbedNotification(String title, String description, String item, String coordinates, String world) {
        try {
            URL url = new URL(webhookUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setDoOutput(true);

            String jsonPayload = "{"
                    + "\"embeds\": [{"
                    + "\"title\": \"" + title + "\","
                    + "\"description\": \"" + description + "\","
                    + "\"color\": 16711680," // Red color
                    + "\"fields\": ["
                    + "{\"name\": \"Item\", \"value\": \"" + item + "\", \"inline\": true},"
                    + "{\"name\": \"Coordinates\", \"value\": \"" + coordinates + "\", \"inline\": true},"
                    + "{\"name\": \"World\", \"value\": \"" + world + "\", \"inline\": true}"
                    + "]"
                    + "}]"
                    + "}";

            try (OutputStream os = connection.getOutputStream()) {
                byte[] input = jsonPayload.getBytes("utf-8");
                os.write(input, 0, input.length);
            }

            connection.getResponseCode(); // Trigger the request
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}