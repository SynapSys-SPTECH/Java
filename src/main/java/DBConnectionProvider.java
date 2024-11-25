//import org.apache.commons.dbcp2.BasicDataSource;
//import org.springframework.jdbc.core.JdbcTemplate;
//import javax.sql.DataSource;
//
//public class DBConnectionProvider {
//    private final DataSource dataSource;
//
//    public DBConnectionProvider() {
//        BasicDataSource basicDataSource = new BasicDataSource();
////        basicDataSource.setUrl("jdbc:mysql://0.0.0.0:3306/Synapsys");
////        basicDataSource.setUsername("root");
////        basicDataSource.setPassword("urubu100");
//        basicDataSource.setUrl(System.getenv().getOrDefault("DB_URL", "jdbc:mysql://localhost:3306/Synapsys"));
//        basicDataSource.setUsername(System.getenv().getOrDefault("DB_USERNAME", "root"));
//        basicDataSource.setPassword(System.getenv().getOrDefault("DB_PASSWORD", "09241724"));
//        this.dataSource = basicDataSource;
//    }
//    public JdbcTemplate getConnection() {
//        return new JdbcTemplate(dataSource);
//    }
//}
import org.apache.commons.dbcp2.BasicDataSource;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DBConnectionProvider {

    private static final Logger log = Logger.getLogger(Main.class.getName());
    private final DataSource dataSource;

    public DBConnectionProvider() {
        BasicDataSource basicDataSource = new BasicDataSource();

        // Obter configurações do banco de dados a partir das variáveis de ambiente
        String dbUrl = System.getenv().getOrDefault("DB_URL", "jdbc:mysql://172.18.0.2:3306/Synapsys");
        String dbUsername = System.getenv().getOrDefault("DB_USERNAME", "root");
        String dbPassword = System.getenv().getOrDefault("DB_PASSWORD", "urubu100");

        // Validar URL do banco de dados
        if (dbUrl.isEmpty()) {
            log.warning("A URL do banco de dados não foi fornecida! Verifique as configurações do ambiente.");
            throw new IllegalArgumentException("A URL do banco de dados é obrigatória.");
        }

        // Configurações do BasicDataSource
        basicDataSource.setUrl(dbUrl);
        basicDataSource.setUsername(dbUsername);
        basicDataSource.setPassword(dbPassword);

        // Configurar propriedades adicionais do pool de conexões
        basicDataSource.setMinIdle(5); // Número mínimo de conexões ociosas
        basicDataSource.setMaxIdle(10); // Número máximo de conexões ociosas
        basicDataSource.setMaxOpenPreparedStatements(100); // Máximo de prepared statements

        log.info("Iniciando a configuração da fonte de dados com as informações fornecidas.");

        try {
            // Testar conexão com o banco
            log.info("Testando conexão com o banco de dados...");
            basicDataSource.getConnection().close(); // Fecha imediatamente após testar
            log.info("Conexão com o banco de dados testada com sucesso!");
        } catch (Exception e) {
            log.log(Level.SEVERE, "Falha ao testar a conexão com o banco de dados. Verifique as configurações.", e);
            throw new RuntimeException("Não foi possível estabelecer uma conexão com o banco de dados.", e);
        }

        this.dataSource = basicDataSource;
        log.info("Fonte de dados configurada com sucesso.");
    }

    public JdbcTemplate getConnection() {
        log.info("Criando e retornando uma nova instância de JdbcTemplate.");
        return new JdbcTemplate(dataSource);
    }
}

