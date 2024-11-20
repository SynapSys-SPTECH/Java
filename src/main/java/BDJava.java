import org.springframework.jdbc.core.JdbcTemplate;

import java.util.List;
import java.util.logging.Logger;

public class BDJava extends DBConnectionProvider {


    Logger log = Logger.getLogger(Main.class.getName());


    public void inserirBanco(List<List<BaseClima>> climasExtraidos){
        log.info("Iniciando a Inserção de dados");
        int i = 0;
        for (List<BaseClima> dados : climasExtraidos) {
            if (!dados.isEmpty()) {
                log.info("inserido dados do Municipio " + dados.get(i).getMunicipio());
            }
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


                getConnection().update(
                        "INSERT INTO leitura (dataHora, latitude, longitude, direcaoVento, rajadaMax, velocidadeHoraria, municipio, estado) VALUES (?, ?,? , ?, ?, ?, ?, ?)",
                        data +" "+ hora, latitude, longitude, direcaoVento, rajadaMax, velocidadeMax, cidade, estado);
            }

            i++;
        }
        log.fine("Finalizado inserção dos dados!");
    }
}
