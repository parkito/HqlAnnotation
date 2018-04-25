package ru.siksmfp.spring.hibernatehsql.config;

import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * @author Artem Karnov @date 4/17/2018.
 * @email artem.karnov@t-systems.com
 */
@Component
public class SetUpBean {
    private static final String DRIVER = "org.apache.derby.jdbc.EmbeddedDriver";
    private static final String JDBC_URL = "jdbc:derby:mydb;create=true";

    public void setUp() throws SQLException {
        Connection conn = null;
        try {
            Class.forName(DRIVER);
            conn = DriverManager.getConnection(JDBC_URL);
        } catch (Exception ex) {
            System.err.println(ex);
            System.exit(1);
        }
        System.out.println("Database created " + conn.isClosed());
    }
}
