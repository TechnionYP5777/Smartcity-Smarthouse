package il.ac.technion.cs.smarthouse.sensors.stove.gui;

import java.util.Arrays;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

import il.ac.technion.cs.smarthouse.sensors.simulator.SensorBuilder;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

/**
 * This class simulates a temperature sensor for a stove.
 * 
 * @author Sharon
 * @since 9.12.16
 */
public class StoveSensorSimulator extends Application {
	public static void main(final String[] args) {
		launch(args);
	}

	private void startSensor(Consumer<String> logger) {
		new SensorBuilder().setCommname("iStoves").setAlias("Roy's awesome stove")
				.addInfoSendingPath("stove.is_on", Boolean.class).addInfoSendingPath("stove.temperature", Integer.class)
				.addStreamingRange("stove.is_on", Arrays.asList(true, false))
				.addStreamingRange("stove.temperature", Arrays.asList(0, 140))
				.setStreamInterval(TimeUnit.SECONDS.toMillis(1000)).addInfoSendingLogger(logger).build()
				.streamMessages();
	}

	private Scene setFromSensorSimulator() {
		VBox box = new VBox();
		box.setAlignment(Pos.CENTER);
		box.setPadding(new Insets(20));
		box.setSpacing(20.0);

		startSensor(msg -> Platform.runLater(() -> {
			box.getChildren().add(new Label(msg));
			if (box.getChildren().size() > 50)
				box.getChildren().remove(0);
		}));

		ScrollPane pane = new ScrollPane();
		pane.setPrefSize(500, 500);
		pane.setContent(box);
		VBox parentBox = new VBox();
		parentBox.getChildren().add(new Label("Im a place holder! :D heres a basic console:\n"));
		parentBox.getChildren().add(pane);
		return new Scene(parentBox);
	}

	@Override
	public void start(final Stage s) throws Exception {
		// s.setScene(setFromController());
		s.setScene(setFromSensorSimulator());
		s.setTitle("Stove Sensor Simulator");
		s.show();
	}
}
