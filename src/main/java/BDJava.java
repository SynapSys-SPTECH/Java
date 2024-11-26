import org.springframework.jdbc.core.JdbcTemplate;

import java.util.List;
import java.util.logging.Logger;

public class BDJava {

    DBConnectionProvider dbConnectionProvider = new DBConnectionProvider();
    JdbcTemplate connection = dbConnectionProvider.getConnection();
    Logger log = Logger.getLogger(Main.class.getName());

    public void inserirBanco(List<List<BaseClima>> climasExtraidos){
        log.info("Iniciando a Inserção de dados");
        int i = 0;
        for (List<BaseClima> dados : climasExtraidos) {
            log.info("Lendo dados da cidade " + dados.get(i).getCidade());

            for (BaseClima baseClima : dados) {
                String data = baseClima.getData().toString();
                String cidade = baseClima.getCidade();
                String hora = baseClima.getHora();
                String estado = baseClima.getEstado();
                Integer direcaoVento = baseClima.getDirecaoVento();
                Double rajadaMax = baseClima.getVentoRajada();
                Double velocidadeMax = baseClima.getVentoVelocidade();

                connection.update(
                        "INSERT INTO leitura (dia, hora, direcaoVento, rajadaMax, velocidadeHoraria, cidade, estado) VALUES (? , ?, ?, ?, ?, ?, ?)",
                        data, hora, direcaoVento, rajadaMax, velocidadeMax, cidade, estado);
            }
            log.info("inserido dados da cidade " + dados.get(i).getCidade());
            i++;
        }
        log.fine("Finalizado inserção dos dados!");
    }
}
