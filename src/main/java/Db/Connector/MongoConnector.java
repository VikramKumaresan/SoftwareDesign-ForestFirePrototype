package Db.Connector;

import Db.DbContants;
import Sensor.SensorData;
import Sensor.TemperatureSensorData;
import com.mongodb.client.*;
import com.mongodb.client.model.Projections;
import org.bson.Document;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import static Db.Connector.DbConnector.DbFields.*;
import static com.mongodb.client.model.Sorts.ascending;

public class MongoConnector implements DbConnector {
    private final MongoClient client;
    private final MongoDatabase db;
    private final MongoCollection<Document> collection;
    private final FindIterable<Document> partialCursor;

    private final String INSERT_ID_DB_FIELD = "insertId";
    private static int objId = 0;

    public MongoConnector(String port) {
        client = MongoClients.create(String.format("mongodb://localhost:%s", port));
        db = client.getDatabase(DbContants.getEnv(DbContants.ENV_VARS.FOREST_FIRE_PROTO_DB_NAME));
        collection = db.getCollection(DB_TABLE_NAME);

        partialCursor = collection.find()
                .sort(ascending(INSERT_ID_DB_FIELD))
                .limit(100)
                .projection(Projections.fields(
                        Projections.include(SENSOR_ID.toString(), READING.toString(), TIMESTAMP.toString()),
                        Projections.excludeId()))
        ;
    }

    @Override
    public void write(SensorData data) {
        Document doc = new Document();
        doc.append(SENSOR_ID.toString(), data.getSensorId());
        doc.append(READING.toString(), data.getSensorReading().toString());
        doc.append(TIMESTAMP.toString(), data.getReadingTime().toString());
        doc.append(INSERT_ID_DB_FIELD, ++objId);

        collection.insertOne(doc);
    }

    @Override
    public List<SensorData> read(Integer page) {
        List<SensorData> result = new ArrayList<>();
        MongoCursor<Document> cursor = partialCursor.skip(page*100).iterator();

        try(cursor) {
            while(cursor.hasNext()) {
                Document doc = cursor.next();
                result.add(new TemperatureSensorData(
                        doc.getString(SENSOR_ID.toString()),
                        Float.parseFloat(doc.getString(READING.toString())),
                        Timestamp.valueOf(doc.getString(TIMESTAMP.toString()))));
            }
        }
        return result;
    }

}
