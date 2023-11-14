package Sensor;

import java.sql.Timestamp;

public interface SensorData {
    public String getSensorId();
    public Float getSensorReading();
    public Timestamp getReadingTime();
}