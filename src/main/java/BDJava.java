import org.springframework.jdbc.core.JdbcTemplate;

import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

public class BDJava extends DBConnectionProvider {

    private static final Logger log = Logger.getLogger(Main.class.getName());
    private final JdbcTemplate connection;

    public BDJava() throws Exception {
        super();
        // Usar a conexão herdada
        this.connection = super.getConnection();
    }

    public void inserirBanco(List<List<BaseClima>> climasExtraidos) throws Exception {
        log.info("Iniciando a inserção de dados...");

        if (climasExtraidos == null || climasExtraidos.isEmpty()) {
            log.warning("A lista de climas está vazia ou nula. Nada para inserir.");
            return;
        }
        int totalInsercoes = 0;
        try {
            for (List<BaseClima> dados : climasExtraidos) {
                if (dados.isEmpty()) {
                    log.warning("Encontrada lista vazia de dados. Pulando iteração.");
                    continue;
                }
                // Pegar o município do primeiro registro (se existir)
                String municipio = dados.get(0).getMunicipio();
                log.info("Inserindo dados do município: " + municipio);

                // Inserção em lote para melhorar performance
                totalInsercoes = 0;
                for (BaseClima baseClima : dados) {
                    String data = baseClima.getData().toString();
                    String cidade = baseClima.getMunicipio();
                    String hora = baseClima.getHora();
                    String estado = baseClima.getEstado();
                    Integer direcaoVento = baseClima.getDirecaoVento();
                    Double rajadaMax = baseClima.getVentoRajada();
                    Double velocidadeMax = baseClima.getVentoVelocidade();
                    Double latitude = baseClima.getLatitude();
                    Double longitude = baseClima.getLongitude();

                    String query = """
                        SELECT dataHora , municipio
                        FROM leitura
                        WHERE dataHora = ? AND municipio = ?
                        """;
                    String dataHora = data + " " + hora;
                    List<Map<String, Object>> topLocais = connection.queryForList(query, dataHora, municipio);
                    int contador = 0;
                    StringBuilder message = new StringBuilder();
                    for (Map<String, Object> registro : topLocais) {
                        contador++;
                    }

                    if (contador == 0) {
                        int rows = connection.update(
                                "INSERT INTO leitura (dataHora, latitude, longitude, direcaoVento, rajadaMax, velocidadeHoraria, municipio, estado) VALUES (?, ?,? , ?, ?, ?, ?, ?)",
                                data + " " + hora, latitude, longitude, direcaoVento, rajadaMax, velocidadeMax, cidade, estado);

                        totalInsercoes += rows;
                    }

                }
                log.info("Inserção em lote concluída para o município: " + municipio);
                log.info("Quantidade de campos inseridos foi de: " + totalInsercoes);
                Slack.notificar("Quantidade de campos inseridos no banco de dados do municipio %s foi de: %d".formatted(municipio, totalInsercoes));
                if (totalInsercoes == 0) {
                    log.info("Dados já estavam presentes no banco de dados.");
                }
            }
        } catch (Exception e) {
            log.log(Level.SEVERE, "Erro ao inserir dados no banco: " + e.getMessage(), e);
            Slack.notificar("Erro ao inserir dados no banco: " + e.getMessage());
            throw new RuntimeException("Erro durante a inserção de dados.", e);
        }

        log.info("Finalizada a inserção de dados.");
    }
}
