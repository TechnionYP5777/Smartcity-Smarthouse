package il.ac.technion.cs.smarthouse.applications.vitals;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Side;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;

/** This class is responsible for the visual part of the vitals signs
 * application.
 * @author Yarden
 * @since 19.1.17 */
public class Controller implements Initializable {
    private static final int MAX_POINTS = 30;

    private int points;
    private final XYChart.Series<Number, Number> pulseSeries = new XYChart.Series<>();
    private final XYChart.Series<Number, Number> systolicSeries = new XYChart.Series<>();
    private final XYChart.Series<Number, Number> diastolicSeries = new XYChart.Series<>();
    private final XYChart.Series<Number, Number> tmpSeries = new XYChart.Series<>();
    private final NumberAxis xAxis = new NumberAxis(0, MAX_POINTS, 1);
    private final HBox backHBox = new HBox();
    private final HBox frontHBox = new HBox();

    @FXML public Label pulseLabel;
    @FXML public Label bpLabel;
    @FXML public StackPane stackPane;

    @Override public void initialize(final URL location, final ResourceBundle __) {
        pulseLabel.setDisable(false);
        bpLabel.setDisable(false);

        xAxis.setForceZeroInRange(false);
        xAxis.setAutoRanging(false);
        xAxis.setTickLabelsVisible(false);
        xAxis.setTickMarkVisible(false);
        xAxis.setMinorTickVisible(false);

        final NumberAxis bpYAxis = new NumberAxis();
        bpYAxis.setLabel("Blood Pressure (mmHg)");

        final NumberAxis pulseXAxis = new NumberAxis();
        pulseXAxis.setAutoRanging(false);
        pulseXAxis.setVisible(false);
        pulseXAxis.lowerBoundProperty().bind(xAxis.lowerBoundProperty());
        pulseXAxis.upperBoundProperty().bind(xAxis.upperBoundProperty());
        pulseXAxis.tickUnitProperty().bind(xAxis.tickUnitProperty());

        final NumberAxis pulseYAxis = new NumberAxis();
        pulseYAxis.setSide(Side.RIGHT);
        pulseYAxis.setLabel("Pulse (BPM)");

        final LineChart<Number, Number> bpLineChart = new LineChart<Number, Number>(xAxis, bpYAxis) {
            @Override protected void dataItemAdded(final Series<Number, Number> __1, final int itemIndex, final Data<Number, Number> item) {
                // remove symbols from data points
            }
        }, pulseLineChart = new LineChart<Number, Number>(xAxis, pulseYAxis) {
            @Override protected void dataItemAdded(final Series<Number, Number> __1, final int itemIndex, final Data<Number, Number> item) {
                // remove symbols from data points
            }
        };
        bpLineChart.setDisable(false);
        pulseLineChart.setDisable(false);

        tmpSeries.setName("Pulse");
        systolicSeries.setName("Systolic");
        diastolicSeries.setName("Diastolic");

        pulseLineChart.getData().add(pulseSeries);
        bpLineChart.getData().add(tmpSeries);
        bpLineChart.getData().add(systolicSeries);
        bpLineChart.getData().add(diastolicSeries);

        bpLineChart.lookup(".chart-content").lookup(".chart-plot-background").setStyle("-fx-background-color: transparent;");
        bpLineChart.setVerticalGridLinesVisible(false);
        bpLineChart.setHorizontalGridLinesVisible(false);

        // stackPane.getChildren().add(pulseLineChart);
        // stackPane.getChildren().add(bpLineChart);
        final Pane tmpBackPane = new Pane();
        tmpBackPane.setMinSize(50, Region.USE_COMPUTED_SIZE);
        tmpBackPane.setMaxSize(50, Region.USE_COMPUTED_SIZE);
        final Pane tmpFrontPane = new Pane();
        tmpFrontPane.setMinSize(50, Region.USE_COMPUTED_SIZE);
        tmpFrontPane.setMaxSize(50, Region.USE_COMPUTED_SIZE);

        backHBox.setMaxSize(Region.USE_COMPUTED_SIZE, Region.USE_COMPUTED_SIZE);
        frontHBox.setMaxSize(Region.USE_COMPUTED_SIZE, Region.USE_COMPUTED_SIZE);

        HBox.setHgrow(pulseLineChart, Priority.ALWAYS);
        HBox.setHgrow(bpLineChart, Priority.ALWAYS);

        VBox.setVgrow(stackPane, Priority.ALWAYS);

        backHBox.getChildren().add(tmpBackPane);
        backHBox.getChildren().add(pulseLineChart);
        frontHBox.getChildren().add(bpLineChart);
        frontHBox.getChildren().add(tmpFrontPane);

        stackPane.getChildren().add(backHBox);
        stackPane.getChildren().add(frontHBox);

    }

    /* This method assumes the client is an adult. */
    public void updateChart(final int pulse, final int systolicBP, final int diastolicBP) {
        pulseLabel.setText("Pulse: " + pulse);
        bpLabel.setText("Blood Pressure: " + systolicBP + "/" + diastolicBP);

        // minor alerts
        pulseLabel.setTextFill(pulse < 60 || pulse > 100 ? Color.RED : Color.BLACK);
        bpLabel.setTextFill(systolicBP < 90 || systolicBP > 140 || diastolicBP < 60 || diastolicBP > 90 ? Color.RED : Color.BLACK);

        if (points > MAX_POINTS) {
            pulseSeries.getData().remove(0);
            systolicSeries.getData().remove(0);
            diastolicSeries.getData().remove(0);
        }

        pulseSeries.getData().add(new XYChart.Data<>(Integer.valueOf(points), Integer.valueOf(pulse)));
        systolicSeries.getData().add(new XYChart.Data<>(Integer.valueOf(points), Integer.valueOf(systolicBP)));
        diastolicSeries.getData().add(new XYChart.Data<>(Integer.valueOf(points), Integer.valueOf(diastolicBP)));

        xAxis.setLowerBound(Math.max(points, MAX_POINTS) - MAX_POINTS);
        xAxis.setUpperBound(Math.max(points, MAX_POINTS));

        ++points;
    }
}
