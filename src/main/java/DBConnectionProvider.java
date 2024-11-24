import org.apache.commons.dbcp2.BasicDataSource;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;

public class DBConnectionProvider {

    private final DataSource dataSource;

    public DBConnectionProvider() {
        BasicDataSource basicDataSource = new BasicDataSource();
//        basicDataSource.setUrl("jdbc:mysql://0.0.0.0:3306/Synapsys");
//        basicDataSource.setUsername("root");
//        basicDataSource.setPassword("urubu100");
        basicDataSource.setUrl(System.getenv().getOrDefault("DB_URL", "jdbc:mysql://localhost:3306/Synapsys"));
        basicDataSource.setUsername(System.getenv().getOrDefault("DB_USERNAME", "root"));
        basicDataSource.setPassword(System.getenv().getOrDefault("DB_PASSWORD", "09241724"));

        this.dataSource = basicDataSource;
    }

    public JdbcTemplate getConnection() {
        return new JdbcTemplate(dataSource);
    }
}
