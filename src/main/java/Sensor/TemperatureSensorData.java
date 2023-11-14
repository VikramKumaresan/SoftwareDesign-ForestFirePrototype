package Sensor;

import java.sql.Timestamp;

public class TemperatureSensorData implements SensorData {
    private final String sensorId;
    private final Float temperature;
    private final Timestamp sensorReadTime;

    public TemperatureSensorData(String sensorId, Float temperature, Timestamp sensorReadTime) {
        this.sensorId = sensorId;
        this.temperature = temperature;
        this.sensorReadTime = sensorReadTime;
    }

    @Override
    public String getSensorId() {
        return sensorId;
    }

    @Override
    public Float getSensorReading() {
        return temperature;
    }

    @Override
    public Timestamp getReadingTime() {
        return sensorReadTime;
    }

    @Override
    public String toString() {
        return String.format("{sensorId= %s, temperature= %s, sensorReadTime= %s}", sensorId, temperature, sensorReadTime);
    }
}
