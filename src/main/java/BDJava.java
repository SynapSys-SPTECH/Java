//import org.springframework.jdbc.core.JdbcTemplate;
//
//import java.util.List;
//import java.util.logging.Logger;
//
//public class BDJava extends DBConnectionProvider {
//
//
//    Logger log = Logger.getLogger(Main.class.getName());
//    DBConnectionProvider dbConnectionProvider = new DBConnectionProvider();
//    JdbcTemplate connection = dbConnectionProvider.getConnection();
//
//    public void inserirBanco(List<List<BaseClima>> climasExtraidos){
//        log.info("Iniciando a Inserção de dados");
//        log.info(dbConnectionProvider.toString());
//        int i = 0;
//        for (List<BaseClima> dados : climasExtraidos) {
//            if (!dados.isEmpty()) {
//                log.info("inserido dados do Municipio " + dados.get(i).getMunicipio());
//            }
//            for (BaseClima baseClima : dados) {
//                String data = baseClima.getData().toString();
//                String cidade = baseClima.getMunicipio();
//                String hora = baseClima.getHora();
//                String estado = baseClima.getEstado();
//                Integer direcaoVento = baseClima.getDirecaoVento();
//                Double rajadaMax = baseClima.getVentoRajada();
//                Double velocidadeMax = baseClima.getVentoVelocidade();
//                Double latitude = baseClima.getLatitude();
//                Double longitude = baseClima.getLongitude();
//
//                connection.update(
//                        "INSERT INTO leitura (dataHora, latitude, longitude, direcaoVento, rajadaMax, velocidadeHoraria, municipio, estado) VALUES (?, ?,? , ?, ?, ?, ?, ?)",
//                        data +" "+ hora, latitude, longitude, direcaoVento, rajadaMax, velocidadeMax, cidade, estado);
//            }
//
//            i++;
//        }
//        log.fine("Finalizado inserção dos dados!");
//    }
//}
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class BDJava extends DBConnectionProvider {

    private static final Logger log = Logger.getLogger(Main.class.getName());
    private final JdbcTemplate connection;

    public BDJava() {
        // Usar a conexão herdada
        this.connection = super.getConnection();
    }

    public void inserirBanco(List<List<BaseClima>> climasExtraidos) {
        log.info("Iniciando a inserção de dados...");

        if (climasExtraidos == null || climasExtraidos.isEmpty()) {
            log.warning("A lista de climas está vazia ou nula. Nada para inserir.");
            return;
        }
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

                    connection.update(
                            "INSERT INTO leitura (dataHora, latitude, longitude, direcaoVento, rajadaMax, velocidadeHoraria, municipio, estado) VALUES (?, ?,? , ?, ?, ?, ?, ?)",
                            data + " " + hora, latitude, longitude, direcaoVento, rajadaMax, velocidadeMax, cidade, estado);
                }
                log.info("Inserção em lote concluída para o município: " + municipio);
            }
        } catch (Exception e) {
            // Logar erros com detalhes
            log.log(Level.SEVERE, "Erro ao inserir dados no banco: " + e.getMessage(), e);
            throw new RuntimeException("Erro durante a inserção de dados.", e);
        }

        log.info("Finalizada a inserção de dados.");
    }
}
