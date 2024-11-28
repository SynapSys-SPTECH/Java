import org.json.JSONObject;
import org.springframework.jdbc.core.JdbcTemplate;

public class App{



    public static void main(String[] args) throws Exception {
        DBConnectionProvider db = new DBConnectionProvider();
        JdbcTemplate connection = db.getConnection();

        Slack slack = new Slack();

        JSONObject message = new JSONObject();

//        slack.executarNotificacoes();
        slack.gerarNotificacaoLinhas(connection);


    }
}