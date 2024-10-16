package school.sptech;

import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public class Main {

    public static void main(String[] args) {

        DBConnectionProvider dbConnectionProvider = new DBConnectionProvider();
        JdbcTemplate connection = dbConnectionProvider.getConnection();

        connection.execute("""
                DROP TABLE IF EXISTS Leitura;
                """);
        connection.execute("""
                CREATE TABLE IF NOT EXISTS leitura(
                  idLeitura INT PRIMARY KEY,
                  data DATE NOT NULL,
                  hora TIME NOT NULL,
                  direcaoVento INT NOT NULL,
                  rajadaMax DOUBLE NOT NULL,
                  velocidadeHoraria DOUBLE NOT NULL,
                  cidade VARCHAR(45) NOT NULL,
                  estado VARCHAR(45) NOT NULL,
                  createAt TIMESTAMP DEFAULT CURRENT_TIMESTAMP
                  );
                """);

        // Inserindo algumas leituras

        connection.update(
                "INSERT INTO Leitura (idLeitura, data, hora, direcaoVento, rajadaMax, velocidadeHoraria, cidade, estado) VALUES (?, ?, ?, ?, ?, ?, ?, ?)",
                1, "2024-10-09", "14:30:00", 180, 25.5, 15.2, "São Paulo", "SP");

        connection.update(
                "INSERT INTO Leitura (idLeitura, data, hora, direcaoVento, rajadaMax, velocidadeHoraria, cidade, estado) VALUES (?, ?, ?, ?, ?, ?, ?, ?)",
                2, "2024-10-08", "09:45:00", 90, 20.8, 10.6, "Rio de Janeiro", "RJ");


        // Listando todas as leituras

        List<Leitura> leiturasDoBanco = connection.query("SELECT * FROM Leitura", new BeanPropertyRowMapper<>(Leitura.class));

        System.out.println("Leituras no banco de dados:");

        for (Leitura leituras : leiturasDoBanco) {
            System.out.println(leituras);
        }

        // Inserindo um novo filme a partir de um objeto

        LocalDate dataHorario = LocalDate.now();

        Leitura novaLeitura = new Leitura(46, "2024-10-13", "13:40:50", 90, 2.5 , 2.5 , "cidade" , "estado" );

        connection.update(
                "INSERT INTO Leitura (idLeitura, data, hora, direcaoVento, rajadaMax, velocidadeHoraria, cidade, estado, createAt) VALUES (?, ?, ?, ?, ?, ?, ?, ?, now())",
                novaLeitura.getIdLeitura(), novaLeitura.getData(), novaLeitura.getHora(), novaLeitura.getDirecaoVento(), novaLeitura.getRajadaMax(), novaLeitura.getVelocidadeHoraria(), novaLeitura.getCidade(), novaLeitura.getEstado());


        System.out.println("\nLeituras no banco de dados após inserção de nova leitura:");


        leiturasDoBanco = connection.query("SELECT * FROM Leitura", new BeanPropertyRowMapper<>(Leitura.class));

        for (Leitura leituras : leiturasDoBanco) {
            System.out.println(leituras);
        }


//        // Atualizando uma leitura

        connection.update("UPDATE leitura SET cidade = ?, estado = ? WHERE idLeitura = ?",
                "Salvador", "BA", 2);

        System.out.println("\nLeituras no banco de dados após atualização de filme:");

        leiturasDoBanco = connection.query("SELECT * FROM leitura", new BeanPropertyRowMapper<>(Leitura.class));

        for (Leitura leituras: leiturasDoBanco) {
            System.out.println(leituras);
        }
//
//        // Deletando uma leitura

        connection.update("DELETE FROM leitura WHERE idLeitura = ?", 1);

        System.out.println("\nLeituras no banco de dados após exclusão de leitura:");

        leiturasDoBanco = connection.query("SELECT * FROM leitura", new BeanPropertyRowMapper<>(Leitura.class));

        for (Leitura leituras : leiturasDoBanco) {
            System.out.println(leituras);
        }
//
//        // Busca personalizada
//
        System.out.println("\nLeituras de Salvador no banco de dados:");

        leiturasDoBanco = connection.query("SELECT * FROM leitura WHERE cidade = ?", new BeanPropertyRowMapper<>(Leitura.class), "Salvador");

        for (Leitura leituras : leiturasDoBanco) {
            System.out.println(leituras);
        }
//
//        // Buscar um filme pelo ID
//
        Leitura leituraEncontrada = connection.queryForObject("SELECT * FROM leitura WHERE idLeitura = ?", new BeanPropertyRowMapper<>(Leitura.class), 2);
        System.out.println("\nLeitura com ID 2: " + leituraEncontrada);

//        // Obs: se sua query retornar nenhum ou mais de um item, ao executar, uma exceção será lançada.
    }
}