import Models.Activity;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.ResultSetHandler;
import org.apache.commons.dbutils.handlers.BeanListHandler;
import org.junit.Test;

import java.sql.SQLException;
import java.util.List;

public class dbConnectionTest {

    private static HikariConfig getConfig(){
        HikariConfig config = new HikariConfig();

        config.setJdbcUrl("jdbc:postgresql://localhost:5432/chatdb");
        config.setUsername("chat");
        config.setPassword("1234");

        return config;
    }

    @Test
    public void createConnection(){
        HikariDataSource ds = new HikariDataSource( getConfig() );
        ds.close();
    }

    @Test
    public void getActivities(){
        HikariDataSource ds = new HikariDataSource( getConfig() );
        try {
            QueryRunner run = new QueryRunner(ds);
            ResultSetHandler<List<Activity>> handler = new BeanListHandler<>(Activity.class);
            List<Activity> activities = run.query("SELECT * FROM \"activityLog\"", handler);
            for (Activity activity : activities){
                System.out.println(activity);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
