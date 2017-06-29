package il.ac.technion.cs.smarthouse.applications.stove;

import java.util.Arrays;
import java.util.Collection;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import il.ac.technion.cs.smarthouse.developers_api.SmarthouseApplication;
import il.ac.technion.cs.smarthouse.developers_api.application_builder.ColorRange;
import il.ac.technion.cs.smarthouse.developers_api.application_builder.GuiBinderObject;
import il.ac.technion.cs.smarthouse.sensors.Simulatable;
import il.ac.technion.cs.smarthouse.sensors.simulator.GenericSensor;
import il.ac.technion.cs.smarthouse.sensors.simulator.SensorBuilder;
import il.ac.technion.cs.smarthouse.sensors.simulator.SensorsSimulator;
import il.ac.technion.cs.smarthouse.system.dashboard.InfoCollector;
import il.ac.technion.cs.smarthouse.system.dashboard.WidgetType;
import il.ac.technion.cs.smarthouse.system.file_system.PathBuilder;
import il.ac.technion.cs.smarthouse.system.services.ServiceType;
import il.ac.technion.cs.smarthouse.system.services.alerts_service.AlertsManager;
import il.ac.technion.cs.smarthouse.system.services.alerts_service.EmergencyLevel;
import il.ac.technion.cs.smarthouse.system.services.sensors_service.SensorData;
import il.ac.technion.cs.smarthouse.system.services.sensors_service.SensorsService;
import il.ac.technion.cs.smarthouse.system.services.sensors_service.SystemPath;
import javafx.scene.paint.Color;

public class StoveModuleGui extends SmarthouseApplication implements Simulatable {
	private static Logger log = LoggerFactory.getLogger(StoveModuleGui.class);

	private Boolean alertCalled = true;
	private static SensorsSimulator simulator = initSimulator();

	public static void main(String[] args) throws Exception {
		launch(simulator, true);
	}

	private static SensorsSimulator initSimulator() {
		final String onPath = "stove" + PathBuilder.DELIMITER + "is_on",
				temperPath = "stove" + PathBuilder.DELIMITER + "temperature";
		SensorsSimulator s = new SensorsSimulator();
		s.addSensor(new SensorBuilder().setCommname("iStoves").setAlias("Roy's awesome stove")
				.addInfoSendingPath(onPath, Boolean.class).addInfoSendingPath(temperPath, Integer.class)
				.addStreamingRange(onPath, Arrays.asList(true, false))
				.addStreamingRange(temperPath, Arrays.asList(0, 140)).setStreamInterval(1000L)
				.build());
		return s;
	}

	@Override
	public Collection<GenericSensor> getSimulatedSensors() {
		return simulator.getAllSensors();
	}

	@Override
	public void onLoad() throws Exception {
		final GuiBinderObject<Double> alertAfterSecs = new GuiBinderObject<>(15.0);
		final GuiBinderObject<Integer> alertAboveDegs = new GuiBinderObject<>(120), temps = new GuiBinderObject<>(0);
		final GuiBinderObject<Boolean> isStoveOn = new GuiBinderObject<>(false);
		final GuiBinderObject<Double> timer = new GuiBinderObject<>();

		getAppBuilder().getConfigurationsRegionBuilder().addDoubleInputField("Alert after (seconds):", alertAfterSecs)
				.addIntegerInputField("Alert at (degrees celsius):", alertAboveDegs);

		getAppBuilder().getStatusRegionBuilder()
				.addStatusField("Current temperature:", temps,
						new ColorRange<Integer>().addRange(alertAboveDegs, Color.RED))
				.addTimerStatusField("Stove running timer:", isStoveOn, timer,
						new ColorRange<Double>().addRange(alertAfterSecs, Color.RED));

		super.<SensorsService>getService(ServiceType.SENSORS_SERVICE).getSensor("iStoves", StoveSensor.class)
				.subscribe(stove -> {
					final String t = "Stove is " + (stove.isOn() ? "" : "Not ") + "On at " + stove.getTemperture()
							+ " degrees";
					isStoveOn.setData(stove.isOn());
					temps.setData(!stove.isOn() ? 0 : stove.getTemperture());
					log.debug("App msg (from function subscibed to stove sensor): " + t + " | Sensor is located at: "
							+ stove.getSensorLocation());
				});

		Runnable c = () -> {
			synchronized (alertCalled) {

				if (timer.getData(0.0) <= alertAfterSecs.getData(0.0) || temps.getData(0) <= alertAboveDegs.getData(0))
					alertCalled = false;
				else if (!alertCalled) {
					((AlertsManager) getService(ServiceType.ALERTS_SERVICE)).sendAlert(getApplicationName(),
							"Stove is running too long", EmergencyLevel.EMAIL_EMERGENCY_CONTACT);
					alertCalled = true;
				}
			}
		};

		timer.addOnDataChangedListener(c);
		temps.addOnDataChangedListener(c);

		getAppBuilder().getWidgetsRegionBuilder()
				.addWidget(WidgetType.BASIC_DASHBOARD,
						new InfoCollector().setUnit("C").addInfoEntry("stove.temperature", "temper"))
				.addWidget(WidgetType.LINES_GRAPH,
						new InfoCollector().setUnit("C").addInfoEntry("stove.temperature", "temper"))
				.addWidget(WidgetType.SWITCH,
						new InfoCollector().setUnit("Boolean").addInfoEntry("stove.is_on", "isOn"));

	}

	@Override
	public String getApplicationName() {
		return "Stove Application";
	}

	public static class StoveSensor extends SensorData {
		@SystemPath("stove.is_on")
		private boolean on;

		@SystemPath("stove.temperature")
		private int temperature;

		boolean isOn() {
			return on;
		}

		int getTemperture() {
			return temperature;
		}
	}
}
