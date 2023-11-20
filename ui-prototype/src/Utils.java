import Constants.Level;

public class Utils {
    public static int getTemperature(Level level){
        return (int) ((Math.random() * (level.max - level.min)) + level.min);
    }
}
