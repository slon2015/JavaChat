package Data;

import Models.Activity;
import lombok.var;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.ResultSetHandler;
import org.apache.commons.dbutils.handlers.BeanListHandler;

import javax.sql.DataSource;
import java.sql.SQLException;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class ActivityService {
    private DataSource ds;

    public ActivityService(DataSource ds){
        this.ds = ds;
    }

    public List<Activity> getActivities(){
        QueryRunner run = new QueryRunner(ds);
        ResultSetHandler<List<Activity>> handler = new BeanListHandler<>(Activity.class);
        List<Activity> activities = null;
        try {
            activities = run.query("SELECT * FROM \"activityLog\"", handler);
        } catch (SQLException e) {
            e.printStackTrace();
            return Collections.emptyList();
        }
        return activities;
    }

    public void insertActivity(Activity activity){
        QueryRunner run = new QueryRunner(ds);

        Map<String, String> fields = new TreeMap<>();
        if( activity.getMessage() != null )
            fields.put("message",activity.getMessage() );
        if( activity.getAction() != null )
            fields.put("action", activity.getAction() );
        if( activity.getActor() != null )
            fields.put("actor", activity.getActor() );

        var keys = fields.keySet().iterator();
        StringBuilder sqlKeys = new StringBuilder();
        while (keys.hasNext()){
            sqlKeys.append(keys.next() );
            if( keys.hasNext() )
                sqlKeys.append(" ,");
        }

        var values = fields.values().iterator();
        StringBuilder sqlValues = new StringBuilder();
        while (values.hasNext()){
            sqlValues.append("'");
            sqlValues.append(values.next() );
            sqlValues.append("'");
            if( values.hasNext() )
                sqlValues.append(" ,");
        }

        try {
            run.update( "INSERT INTO \"activityLog\"("+sqlKeys+") VALUES("+sqlValues+")" );
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
