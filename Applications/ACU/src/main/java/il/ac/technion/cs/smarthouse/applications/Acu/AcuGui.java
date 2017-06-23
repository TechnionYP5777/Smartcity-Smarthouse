/**
 * 
 */
package il.ac.technion.cs.smarthouse.applications.Acu;

import java.util.HashMap;
import java.util.Map;

import il.ac.technion.cs.simulation.Simulation;
import il.ac.technion.cs.simulation.Simulation.AcuAction;
import il.ac.technion.cs.smarthouse.developers_api.SmarthouseApplication;
import il.ac.technion.cs.smarthouse.developers_api.application_builder.GuiBinderObject;
import il.ac.technion.cs.smarthouse.sensors.PathType;
import il.ac.technion.cs.smarthouse.system.dashboard.InfoCollector;
import il.ac.technion.cs.smarthouse.system.dashboard.WidgetType;
import il.ac.technion.cs.smarthouse.system.services.ServiceType;
import il.ac.technion.cs.smarthouse.system.services.sensors_service.SensorApi;
import il.ac.technion.cs.smarthouse.system.services.sensors_service.SensorData;
import il.ac.technion.cs.smarthouse.system.services.sensors_service.SensorsService;
import il.ac.technion.cs.smarthouse.system.services.sensors_service.SystemPath;

/**
 * @author Elia Traore
 * @since Jun 22, 2017
 */
public class AcuGui extends SmarthouseApplication{
	static Simulation simulation = new Simulation(30,//summer sucks
										"Roy's Man Cave", "Ron's Home Cinema", "Yarden's AI lab", "Inbal's sleeping chamber");
	final static String setTempPath = Simulation.getPath(PathType.INSTRUCTION_RECEIVING,Simulation.defaultTempSuffix);
	final static String setStatePath = Simulation.getPath(PathType.INSTRUCTION_RECEIVING,Simulation.stateSuffix);
	final static String getTempPath = Simulation.getPath(PathType.INFO_SENDING,Simulation.tempSuffix);
	
	public static void main(String[] args) throws Exception {
		simulation.start();
		launch();
	}

	/* (non-Javadoc)
	 * @see il.ac.technion.cs.smarthouse.developers_api.SmarthouseApplication#onLoad()
	 */
	@Override
	public void onLoad() throws Exception {
		final GuiBinderObject<Integer> currentTemp = new GuiBinderObject<>(20);
		final GuiBinderObject<Integer> wantedTemp = new GuiBinderObject<>(20);
		
		final SensorApi<ACUSensor> sensor = super.<SensorsService>getService(ServiceType.SENSORS_SERVICE)
				.getSensor(Simulation.commname, ACUSensor.class);
		sensor.subscribe(data -> {
				Integer want = wantedTemp.getData(100), have = currentTemp.getData(0);
				if(want < have)
				sensor.instruct(AcuAction.HOTTER+"", setStatePath);
				if(want > have)
				sensor.instruct(AcuAction.COLDER+"", setStatePath);
				if(want == have)
				sensor.instruct(AcuAction.STOP+"", setStatePath);
		});
		
		wantedTemp.addOnDataChangedListener(
				desiredT -> desiredT.getDataAsOptional().ifPresent(t->sensor.instruct(t+"", setTempPath)));
		
		getAppBuilder().getStatusRegionBuilder().addStatusField("Current Temperature:", currentTemp);
		getAppBuilder().getConfigurationsRegionBuilder()
											.addIntegerInputField("Desired Temperature:", wantedTemp)
											.addSensorAliasSelectionField("The Controlled location:", sensor);
		
		simulation.getAliases()
					.stream()
					.map(alias -> super.<SensorsService>getService(ServiceType.SENSORS_SERVICE)
										.getSensor(Simulation.commname, ACUSensor.class, alias))
					.forEach(currSensor -> {
						getAppBuilder().getWidgetsRegionBuilder()
										.addWidget(WidgetType.LINES_GRAPH, 
													new InfoCollector().addInfoEntry(getTempPath, sensor.getSensorAlias()), 
													currSensor, 
													acu -> {
														Map<String,Object> data = new HashMap<>();
														data.put(getTempPath, acu.getTemp());
														return data;
													});
					});
		
	}

	/* (non-Javadoc)
	 * @see il.ac.technion.cs.smarthouse.developers_api.SmarthouseApplication#getApplicationName()
	 */
	@Override
	public String getApplicationName() {
		return "Air conditioner Contoller";
	}
	
	public class ACUSensor extends SensorData {
		@SystemPath("ACUnit.current.state")
		private boolean isOn;
		@SystemPath("ACUnit.current.temperature")
		private int temp;
		
		
		public boolean isOn() {
			return isOn;
		}
		
		public int getTemp() {
			return temp;
		}
	}

}
