package Managers;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.Queue;

import Constants.Level;
import Model.Sensor;

public class PropagationManager {
    
    public static ArrayList<Sensor[][]> propagateFire(Sensor[][] sensors){
        ArrayList<Sensor[][]> sensorDataList = new ArrayList<>();

        int rows = sensors.length;
        int cols = sensors[0].length;
        Queue<int[]> queue = new LinkedList<>();

        sensors[3][4].setLevel(Level.ON_FIRE);
        sensors[0][0].setLevel(Level.ON_FIRE);
        queue.offer(new int[]{3,4});
        queue.offer(new int[]{0,0});

        // Define all four directions
        int[][] directions = {{-1, 0}, {1, 0}, {0, -1}, {0, 1}};


        while (!queue.isEmpty()) {
            int size = queue.size();
            
            for (int i = 0; i < size; i++) {
                int[] current = queue.poll();
                int row = current[0];
                int col = current[1];

                // Explore all four directions
                for (int[] dir : directions) {
                    int newRow = row + dir[0];
                    int newCol = col + dir[1];

                    if (isValid(newRow, newCol, rows, cols) && !Level.DEEP_FIRE.equals(sensors[newRow][newCol].getLevel())) {
                        // Ignite the cell and increase the fire level
                        sensors[newRow][newCol].setLevel(Level.nextLevel(sensors[newRow][newCol].getLevel()));;
                        queue.offer(new int[]{newRow, newCol});
                    }
                }
            }

            Sensor[][] sensorsCopy = new Sensor[sensors.length][];
            for(int i = 0; i < sensors.length; i++)
            {
                sensorsCopy[i] = new Sensor[sensors[i].length];
                for (int j = 0; j < sensors[i].length; j++)
                {
                    Sensor s = new Sensor(sensors[i][j].getSensorId());
                    s.setLevel(sensors[i][j].getLevel());
                    s.setTemperature(sensors[i][j].getTemperature());
                    sensorsCopy[i][j] = s;
                }
            }

            sensorDataList.add(sensorsCopy);
        }
        return sensorDataList;
    }

    private static boolean isValid(int row, int col, int rows, int cols) {
        return row >= 0 && row < rows && col >= 0 && col < cols;
    }

}
