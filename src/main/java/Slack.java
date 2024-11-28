import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;

import org.json.JSONObject;
import org.springframework.jdbc.core.JdbcTemplate;

public class Slack {

    private static final HttpClient client = HttpClient.newHttpClient();
    private static final String URL = System.getenv("https://hooks.slack.com/services/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");

    // Conexão com o Slack
    public static void conexao(JSONObject content) throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder(
                        URI.create(URL))
                .header("accept", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(content.toString()))
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        System.out.println(String.format("Status: %s", response.statusCode()));
        System.out.println(String.format("Response: %s", response.body()));
    }

    private void inserirNotificacaoBanco(JdbcTemplate connection, String titulo, String descricao) {
        try {
            DateTimeFormatter formatoData = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            String dataHora = LocalDateTime.now().format(formatoData);

            String query = "INSERT INTO slack (titulo, descricao) VALUES (?, ?)";
            int fkEmpresa = 1;

            connection.update(query, titulo, descricao, dataHora, fkEmpresa);
            System.out.println("Notificação inserida no banco de dados com sucesso!");

        } catch (Exception e) {
            System.out.println("Erro ao inserir notificação no banco de dados: ");
        }
    }



    //notificação do total de linhas inseridas
    public void gerarNotificacaoLinhas(JdbcTemplate connection) {
        String query = "SELECT COUNT(*) AS total_linhas FROM Synapsys.leitura";

        List<Map<String, Object>> resultados = connection.queryForList(query);

        if (!resultados.isEmpty()) {
            Long totalLinhas = (Long) resultados.get(0).get("total_linhas");
            String descricao = "Total de linhas inseridas: " + totalLinhas;

            try {
                JSONObject json = criarJson("Linhas inseridas", descricao);
                inserirNotificacaoBanco(connection, "Linhas inseridas", descricao);

                conexao(json);
            } catch (Exception e) {
                System.out.println("Erro ao realizar Query");
            }

        }
    }


    private JSONObject criarJson(String titulo, String descricao) {
        JSONObject json = new JSONObject();

        json.put("text", "*" + titulo + "*\n" + descricao);

        return json;
    }

    public void executarNotificacoes() {

        JdbcTemplate connection = new JdbcTemplate();
        gerarNotificacaoLinhas(connection);
    }
}


