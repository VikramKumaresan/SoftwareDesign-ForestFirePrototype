package Db;

public class DbContants {
    public static enum ENV_VARS {
        FOREST_FIRE_PROTO_MYSQL_PORT,
        FOREST_FIRE_PROTO_MONGO_PORT,
        FOREST_FIRE_PROTO_APP_USER,
        FOREST_FIRE_PROTO_APP_USER_PASSWORD,
        FOREST_FIRE_PROTO_DB_NAME
    }

    public static String getEnv(DbContants.ENV_VARS var) {
        return System.getenv(var.toString());
    }
}
