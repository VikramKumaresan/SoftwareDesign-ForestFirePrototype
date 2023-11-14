import DbTest.MySqlTest;

import java.sql.SQLException;
import java.util.Arrays;

public class Main {
    enum Scenario {
        MYSQL_TEST
    }

    public static void main(String[] args) throws SQLException {
        System.out.println("\n\n >>> Starting... <<<");

        Scenario scenario = args.length == 1 ? getFromString(args[0].trim()) : null;
        if(scenario == null) {
            System.out.println("Pls enter a valid test scenario\n\nPossible values:");
            Arrays.stream(Scenario.values()).forEach(System.out::println);
            return;
        }

        switch(scenario) {
            case MYSQL_TEST:
                MySqlTest.run();
                return;
        }
    }

    private static Scenario getFromString(String scenarioString) {
        try {
            return Scenario.valueOf(scenarioString);
        } catch (IllegalArgumentException ex) {
            return null;
        }
    }
}