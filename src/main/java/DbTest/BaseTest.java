package DbTest;

import Db.Connector.DbConnector;
import Sensor.SensorData;
import Sensor.TemperatureSensorData;

import java.sql.Timestamp;
import java.time.Duration;
import java.time.Instant;
import java.util.List;

abstract public class BaseTest {
    protected abstract DbConnector getDbConnector();

    public void run() {
        DbConnector connector = getDbConnector();

        Instant start = Instant.now();

        for(int i=0; i<10_000; i++) {
            connector.write(new TemperatureSensorData(String.valueOf(i), i*1.3f, Timestamp.from(Instant.now())));
        }

        int i =0;
        List<SensorData> ans = connector.read(i);
        while(!ans.isEmpty()) {
            ans = connector.read(++i);
        }

        Instant end = Instant.now();
        Duration difference = Duration.between(start, end);
        System.out.printf("%sm %ss %sms %sns\n", difference.toMinutesPart(), difference.toSecondsPart(), difference.toMinutesPart(), difference.toNanosPart());
    }
}
