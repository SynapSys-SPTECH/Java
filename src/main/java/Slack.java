import java.net.HttpURLConnection;
import java.net.URL;
import java.io.OutputStream;
import java.util.logging.Logger;

public class Slack {
    private static final String WEBHOOK_URL;

    static Logger log = Logger.getLogger(Main.class.getName());

    static {
        WEBHOOK_URL = System.getenv("SLACK_URL");
        if (WEBHOOK_URL == null || WEBHOOK_URL.isEmpty()) {
            throw new IllegalArgumentException("A variável de ambiente SLACK_URL não foi definida.");
        }
    }

    public static void notificar(String message) throws Exception {
        String payload = "{\"text\":\"" + message + "\"}";
        URL url = new URL(WEBHOOK_URL);
        HttpURLConnection conxaoHTTP = (HttpURLConnection) url.openConnection();
        conxaoHTTP.setDoOutput(true);
        conxaoHTTP.setRequestMethod("POST");
        conxaoHTTP.setRequestProperty("Content-Type", "application/json");
        conxaoHTTP.setRequestProperty("Accept", "application/json");

        try (OutputStream os = conxaoHTTP.getOutputStream()) {
            byte[] input = payload.getBytes("utf-8");
            os.write(input, 0, input.length);
        }

        int responseCode = conxaoHTTP.getResponseCode();
        if (responseCode == 200){
            log.info("Mensagem enviada para slack, Response Code: " + responseCode);
        }else {
            log.warning("Mensagem não enviada para slack, Response Code: " + responseCode);
        }

    }
}