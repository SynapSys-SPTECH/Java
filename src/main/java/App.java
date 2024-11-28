import org.json.JSONObject;
import org.springframework.jdbc.core.JdbcTemplate;

public class App{
    public static void main(String[] args) throws Exception {

        NotificacaoSlack slack = new NotificacaoSlack();

        slack.EnviarNotificacaoSlack("Testando envio de notificações");
    }
}