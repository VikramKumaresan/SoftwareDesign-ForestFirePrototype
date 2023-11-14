package DbTest;

import Db.Connector.DbConnector;
import Db.Connector.MongoConnector;
import static Db.DbContants.ENV_VARS.FOREST_FIRE_PROTO_MONGO_PORT;
import static Db.DbContants.getEnv;

public class MongoTest extends BaseTest {
    @Override
    protected DbConnector getDbConnector() {
        return new MongoConnector(getEnv(FOREST_FIRE_PROTO_MONGO_PORT));
    }
}