import java.net.HttpURLConnection;
import java.net.URL;
import java.io.OutputStream;

public class NotificacaoSlack {
    private static final String WEBHOOK_URL;

    static {
        WEBHOOK_URL = System.getenv("SLACK_URL");
        if (WEBHOOK_URL == null || WEBHOOK_URL.isEmpty()) {
            throw new IllegalArgumentException("A variável de ambiente SLACK_URL não foi definida.");
        }
    }

    public static void EnviarNotificacaoSlack(String message) throws Exception {
        String payload = "{\"text\":\"" + message + "\"}";
        URL url = new URL(WEBHOOK_URL);
        HttpURLConnection httpConn = (HttpURLConnection) url.openConnection();
        httpConn.setDoOutput(true);
        httpConn.setRequestMethod("POST");
        httpConn.setRequestProperty("Content-Type", "application/json");
        httpConn.setRequestProperty("Accept", "application/json");

        try (OutputStream os = httpConn.getOutputStream()) {
            byte[] input = payload.getBytes("utf-8");
            os.write(input, 0, input.length);
        }

        int responseCode = httpConn.getResponseCode();
        System.out.println("Response Code: " + responseCode);
    }
}