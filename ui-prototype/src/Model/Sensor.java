package Model;

import Constants.Level;

public class Sensor {
    private String sensorId;
    private Level level = Level.NO_FIRE;
    private int temperature = getTemperature(Level.NO_FIRE);

    public Sensor(String sensorId) {
        this.sensorId = sensorId;
        this.level = Level.NO_FIRE;
        this.temperature = getTemperature(this.level);
    }

    public String getSensorId() {
        return sensorId;
    }

    public void setSensorId(String sensorId) {
        this.sensorId = sensorId;
    }

    public Level getLevel() {
        return level;
    }

    public void setLevel(Level level) {
        this.level = level;
        this.temperature = getTemperature(level);
    }

    public int getTemperature() {
        return temperature;
    }

    public void setTemperature(int temperature) {
        this.temperature = temperature;
    }

    private static int getTemperature(Level level){
        return (int) ((Math.random() * (level.max - level.min)) + level.min);
    }
    
}
