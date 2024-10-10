package sptech.school;

import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;

import java.time.LocalDateTime;
import java.util.List;

public class MainDB {

    public static void main(String[] args) {

        DBConnectionProvider dbConnectionProvider = new DBConnectionProvider();
        JdbcTemplate connection = dbConnectionProvider.getConnection();

        connection.execute("""
                CREATE TABLE filme (
                    id INT AUTO_INCREMENT PRIMARY KEY,
                    nome VARCHAR(255) NOT NULL,
                    ano INT NOT NULL,
                    genero VARCHAR(255) NOT NULL,
                    diretor VARCHAR(255) NOT NULL
                )
                """);

        // Inserindo alguns filmes

        connection.update("INSERT INTO filme (nome, ano, genero, diretor) VALUES (?, ?, ?, ?)",
                "Matrix", 1999, "Ficção Científica", "Lana Wachowski, Lilly Wachowski");

        connection.update("INSERT INTO filme (nome, ano, genero, diretor) VALUES (?, ?, ?, ?)",
                "O Poderoso Chefão", 1972, "Drama", "Francis Ford Coppola");

        connection.update("INSERT INTO filme (nome, ano, genero, diretor) VALUES (?, ?, ?, ?)",
                "O Senhor dos Anéis: O Retorno do Rei", 2003, "Fantasia", "Peter Jackson");

        connection.update("INSERT INTO filme (nome, ano, genero, diretor) VALUES (?, ?, ?, ?)",
                "Forrest Gump", 1994, "Drama", "Robert Zemeckis");

        // Listando todos os filmes

        List<Leitura> LeiturasDoBanco = connection.query("SELECT * FROM leitura", new BeanPropertyRowMapper<>(Leitura.class));

        System.out.println("Filmes no banco de dados:");

        for (Leitura leitura : LeiturasDoBanco) {
            System.out.println(leitura);
        }

        // Inserindo um novo filme a partir de um objeto
        LocalDateTime dataHorario = LocalDateTime.now();


        Leitura novaLeitura = new Leitura(1,10,23,90,2.5 , 2.5 , "cidade" , "estado" , dataHorario);


        connection.update("INSERT INTO leitura (idLeitura , dia , hora , direcaoVento, rajadaMax , velocidadeHoraria , cidade, estado , creatAt)");


//        Filme novoFilme = new Filme(null, "Vingadores: Ultimato", 2019, "Ação", "Anthony Russo, Joe Russo");

//        connection.update("INSERT INTO filme (nome, ano, genero, diretor) VALUES (?, ?, ?, ?)",
//                novoFilme.getNome(), novoFilme.getAno(), novoFilme.getGenero(), novoFilme.getDiretor());

        System.out.println("\nLeituras no banco de dados após inserção de novo leitura:");


        System.out.println("Filmes no banco de dados:");
        for (Leitura leitura : LeiturasDoBanco) {
            System.out.println(leitura);
        }

        // Atualizando um filme
// EXEMPLO DE UPDATE
//        connection.update("UPDATE filme SET nome = ?, ano = ?, genero = ?, diretor = ? WHERE id = ?",
//                "Shrek", 2001, "Animação", "Andrew Adamson, Vicky Jenson", 5);


        // Deletando um filme
// EXEMPLO DE DELETE
//        connection.update("DELETE FROM filme WHERE id = ?", 5);

        // Busca personalizada
//  EXEMPLO DE QUERY COM WHERE
        LeiturasDoBanco = connection.query("SELECT * FROM leitura WHERE cidade = ?", new BeanPropertyRowMapper<>(Leitura.class), "cidade");

        for (Leitura leitura : LeiturasDoBanco) {
            System.out.println(leitura);
        }
        System.out.println("Leituras encontradas");

        // Buscar um filme pelo ID
// EXEMPLO DE QUERY BUSCANDO PELO ID
        Leitura leituraEncontrada = connection.queryForObject("SELECT * FROM filme WHERE id = ?", new BeanPropertyRowMapper<>(Leitura.class), 1);
        System.out.println("\nleitura com ID 1: " + leituraEncontrada);

        // Obs: se sua query retornar nenhum ou mais de um item, ao executar, uma exceção será lançada.
    }
}