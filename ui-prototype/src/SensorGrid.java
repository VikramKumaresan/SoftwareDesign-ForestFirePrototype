import Managers.PropagationManager;
import Model.Sensor;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.util.ArrayList;
import java.util.Random;

public class SensorGrid extends Application {

    private static ArrayList<Sensor[][]> sensorValues;

    private static final int GRID_ROW = 7;
    private static final int GRID_COL = 10;
    private static final int RECTANGLE_WIDTH = 80;
    private static final int RECTANGLE_HEIGHT = 50;
    private static final int CIRCLE_RADIUS = 3;
    private static final int CIRCLE_OFFSET = 7;
    private static final int GAP = 50; // Adjust the gap between rectangles
    private static final Random random = new Random();

    int timeStep = 0;

    GridPane gridPane = new GridPane();
    Timeline timeline;

    @Override
    public void start(Stage primaryStage) {
        Sensor[][] sensors = new Sensor[GRID_ROW][GRID_COL];
        for (int row = 0; row < GRID_ROW; row++) {
            for (int col = 0; col < GRID_COL; col++) {
                sensors[row][col] = new Sensor(String.valueOf(row*GRID_COL+col));
            }
        }

        sensorValues = PropagationManager.propagateFire(sensors);

        constructSensorGrid();

        // Center the grid in the canvas
        HBox hBox = new HBox(gridPane);
        hBox.setAlignment(Pos.CENTER);

        VBox vBox = new VBox(hBox);
        vBox.setAlignment(Pos.CENTER);

        StackPane root = new StackPane(vBox);
        root.setPrefSize(1440, 1080);

        Scene scene = new Scene(root);

        primaryStage.setTitle("Wireless Sensor Network");
        primaryStage.setScene(scene);
        primaryStage.show();

        // Use a timeline to update the temperatures every second
        timeline = new Timeline(new KeyFrame(Duration.seconds(3), event -> updateTemperatures()));
        timeline.setCycleCount(sensorValues.size());
        timeline.play();
    }

    private void constructSensorGrid() {
        for (int row = 0; row < GRID_ROW; row++) {
            for (int col = 0; col < GRID_COL; col++) {
                StackPane sensorNode = createSensorNode(row, col);
                gridPane.add(sensorNode, col, row);
            }
        }
    }

    private StackPane createSensorNode(int row, int col) {
        Sensor sensor = sensorValues.get(timeStep)[row][col];
        String sensorValue = String.valueOf(sensor.getTemperature());
        boolean isActive = random.nextFloat(0, 1) > 0.01;

        Rectangle rectangle = new Rectangle(RECTANGLE_WIDTH, RECTANGLE_HEIGHT, getRGBFromTemp(sensorValue));
        rectangle.setStroke(Color.BLACK);

        Circle circle = getSensorStatusIndicator(isActive);

        Text text = new Text(sensorValue + "°C");
        text.setFill(Color.BLACK);
        text.setTextAlignment(TextAlignment.CENTER);
        text.setX((RECTANGLE_WIDTH - text.getBoundsInLocal().getWidth()) / 2); // Center horizontally
        text.setY((RECTANGLE_HEIGHT + text.getBoundsInLocal().getHeight()) / 2 - 2); // Center vertically

        Group linesGroup = new Group();
        if (row > 0) {
            Line topLine = new Line(RECTANGLE_WIDTH / 2.0, 0, RECTANGLE_WIDTH / 2.0, -GAP);
            linesGroup.getChildren().add(topLine);
        }

        if (col > 0) {
            Line leftLine = new Line(-GAP, RECTANGLE_HEIGHT / 2.0, 0, RECTANGLE_HEIGHT / 2.0);
            linesGroup.getChildren().add(leftLine);
        }

        rectangle.setOpacity(isActive ? 1 : .5);
        text.setOpacity(isActive ? 1 : .5);

        Group group = new Group(rectangle, circle, text, linesGroup);
        addClickEvent(group, row, col);
        return new StackPane(group);
    }

    private static Circle getSensorStatusIndicator(boolean isActive) {
        Circle circle = new Circle(CIRCLE_RADIUS, isActive ? Color.GREEN : Color.RED);
        circle.setStroke(Color.WHITE);
        circle.setTranslateX(RECTANGLE_WIDTH - CIRCLE_OFFSET + CIRCLE_RADIUS / 2.0); // Position the circle to the top right
        circle.setTranslateY(CIRCLE_OFFSET); // Position the circle to the top right
        return circle;
    }

    private void addClickEvent(Group group, int row, int col) {
        group.setOnMouseClicked(event -> {
            if (event.getButton() == MouseButton.PRIMARY) {
                // Show modal on left-click
                showTemperatureModal(row, col);
            }
        });
    }

    private void showTemperatureModal(int row, int col) {

        Stage modalStage = new Stage();
        modalStage.initModality(Modality.APPLICATION_MODAL);
        modalStage.setTitle("Sensor Information");

        Label sensorLabel = new Label("Sensor ID: " + (row*GRID_COL+col+1) + "\n" +
                "Status: Active\n" +
                "Temperature: " + sensorValues.get(timeStep)[row][col].getTemperature() + "°C\n" +
                "Latitude: -29.227619\n" +
                "Longitude: -51.878448\n"
        );

        sensorLabel.setLineSpacing(6);

        StackPane modalLayout = new StackPane(sensorLabel);
        modalLayout.setPadding(new Insets(30, 50, 30, 50));

        Scene modalScene = new Scene(modalLayout);
        modalStage.setScene(modalScene);

        // Show the modal
        modalStage.showAndWait();
    }

    private Paint getRGBFromTemp(String sensorValue) {
        int temperature = Integer.parseInt(sensorValue);
        if (0 <= temperature && temperature < 30) return Color.rgb(53, 92, 125);
        else if (temperature < 100) return Color.rgb(255,165,0);
        else if (temperature < 300) return Color.rgb(224, 119, 78);
        else return Color.rgb(204, 48, 63);
    }

    private void updateTemperatures() {
        timeStep++;
        gridPane.getChildren().clear();
        constructSensorGrid();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
