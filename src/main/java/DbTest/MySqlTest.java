package DbTest;

import Db.Connector.DbConnector;
import Db.Connector.MySqlConnector;
import java.sql.SQLException;
import static Db.DbContants.ENV_VARS.*;
import static Db.DbContants.getEnv;

public class MySqlTest extends BaseTest {

    @Override
    protected final DbConnector getDbConnector() {
        try {
            return new MySqlConnector(
                    getEnv(FOREST_FIRE_PROTO_MYSQL_PORT),
                    getEnv(FOREST_FIRE_PROTO_DB_NAME),
                    getEnv(FOREST_FIRE_PROTO_APP_USER),
                    getEnv(FOREST_FIRE_PROTO_APP_USER_PASSWORD)
            );
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
