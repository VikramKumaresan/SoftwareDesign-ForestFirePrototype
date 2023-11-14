package DbTest;

import Db.Connector.DbConnector;
import Db.Connector.MySqlConnector;
import Db.DbContants;
import Sensor.SensorData;
import Sensor.TemperatureSensorData;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.Duration;
import java.time.Instant;
import java.util.List;

import static Db.DbContants.ENV_VARS.*;

public class MySqlTest {

    static public void run() throws SQLException {
        DbConnector connector = new MySqlConnector(
                getEnv(FOREST_FIRE_PROTO_MYSQL_PORT),
                getEnv(FOREST_FIRE_PROTO_MYSQL_DB_NAME),
                getEnv(FOREST_FIRE_PROTO_MYSQL_APP_USER),
                getEnv(FOREST_FIRE_PROTO_MYSQL_APP_USER_PASSWORD)
        );

        Instant start = Instant.now();

        for(int i=0; i<1000; i++) {
            connector.write(new TemperatureSensorData("102", 36.5f, Timestamp.from(Instant.now())));
        }

        int i =0;
        List<SensorData> ans = connector.read(i);
        while(!ans.isEmpty()) {
            ans = connector.read(++i);
        }

        Instant end = Instant.now();
        Duration difference = Duration.between(start, end);

        System.out.printf("%sm %ss\n", difference.toMinutesPart(), difference.toSecondsPart());
    }

    static String getEnv(DbContants.ENV_VARS var) {
        return System.getenv(var.toString());
    }
}
