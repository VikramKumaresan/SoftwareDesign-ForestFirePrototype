package Db.Connector;

import Sensor.SensorData;
import Sensor.TemperatureSensorData;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import static Db.Connector.DbConnector.DbFields.*;

public class MySqlConnector implements DbConnector {
    private final Connection connection;

    public MySqlConnector(String port, String dbName, String username, String password) throws SQLException {
        connection = DriverManager.getConnection(String.format("jdbc:mysql://127.0.0.1:%s/%s?autoReconnect=true", port, dbName), username, password);
    }

    @Override
    public void setup() {}

    @Override
    public void write(SensorData data) {
        try {
            Statement stmt = connection.createStatement();
            stmt.executeUpdate(String.format("INSERT INTO %s VALUES (DEFAULT,\"%s\", \"%s\", \"%s\");",
                    DB_TABLE_NAME, data.getSensorId(), data.getSensorReading().toString(), data.getReadingTime().toString()));
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<SensorData> read(Integer page) {
        try {
            Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery(String.format("SELECT %s, %s, %s" +
                            " FROM %s" +
                            " ORDER BY id" +
                            " LIMIT %s,100;",
                    SENSOR_ID, READING, TIMESTAMP, DB_TABLE_NAME, page*100));
            return resultSetToTemperatureSensorDataList(rs);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private List<SensorData> resultSetToTemperatureSensorDataList(ResultSet rs) throws SQLException {
        List<SensorData> ans = new ArrayList<>();

        while(rs.next()) {
            ans.add(new TemperatureSensorData(rs.getString(SENSOR_ID.toString()),
                    Float.parseFloat(rs.getString(READING.toString())),
                    Timestamp.valueOf(rs.getString(TIMESTAMP.toString()))));
        }
        return ans;
    }
}
