package il.ac.technion.cs.eldery.applications.vitals;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

public class Controller implements Initializable {
    private static final int MAX_POINTS = 30;

    private int points;
    private XYChart.Series<Number, Number> pulseSeries = new XYChart.Series<>();
    private XYChart.Series<Number, Number> systolicSeries = new XYChart.Series<>();
    private XYChart.Series<Number, Number> diastolicSeries = new XYChart.Series<>();
    private NumberAxis xAxis = new NumberAxis(0, MAX_POINTS, 1);
    @FXML public Label pulseLabel;
    @FXML public Label bpLabel;
    @FXML public VBox mainVBox;

    @Override public void initialize(final URL location, final ResourceBundle __) {
        pulseLabel.setDisable(false);
        bpLabel.setDisable(false);
        xAxis.setForceZeroInRange(false);
        xAxis.setAutoRanging(false);
        xAxis.setTickLabelsVisible(false);
        xAxis.setTickMarkVisible(false);
        xAxis.setMinorTickVisible(false);
        NumberAxis yAxis = new NumberAxis();
        yAxis.setLabel("Blood Pressure (mmHg)");
        final LineChart<Number, Number> lineChart = new LineChart<Number, Number>(xAxis, yAxis) {
            @Override protected void dataItemAdded(Series<Number, Number> __1, int itemIndex, Data<Number, Number> item) {
                // Override to remove symbols from points
            }
        };
        lineChart.setDisable(false);
        pulseSeries.setName("Pulse");
        systolicSeries.setName("Systolic");
        diastolicSeries.setName("Diaslolic");
        lineChart.getData().add(pulseSeries);
        lineChart.getData().add(systolicSeries);
        lineChart.getData().add(diastolicSeries);
        mainVBox.getChildren().add(3, lineChart);

    }

    public void updateChart(final int pulse, final int systolicBP, final int diastolicBP) {
        pulseLabel.setText("Pulse: " + pulse);
        bpLabel.setText("Blood Pressure: " + systolicBP + "/" + diastolicBP);
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
