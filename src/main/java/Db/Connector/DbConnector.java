package Db.Connector;

import Sensor.SensorData;
import java.util.List;

public interface DbConnector {
    enum DbFields {
        SENSOR_ID("sensorId"),
        READING("reading"),
        TIMESTAMP("timestamp");

        private final String name;

        DbFields(String name) {
            this.name = name;
        }

        @Override
        public String toString() {
            return name;
        }
    }

    String DB_TABLE_NAME = "Temperature";

    void write(SensorData data);
    List<SensorData> read(Integer page);
}
