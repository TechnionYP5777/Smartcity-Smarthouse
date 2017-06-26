/**
 * 
 */
package il.ac.technion.cs.smarthouse.applications.Acu;

import java.util.Arrays;
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
										"Roy's Man Cave", "Ron's Home Cinema");
//										, "Yarden's AI lab", "Inbal's dogs kennel");
	static final String setTempPath = Simulation.getPath(PathType.INSTRUCTION_RECEIVING, Simulation.defaultTempSuffix);
	static final String setStatePath = Simulation.getPath(PathType.INSTRUCTION_RECEIVING, Simulation.stateSuffix);
	static final String getTempPath = Simulation.getPath(PathType.INFO_SENDING, Simulation.tempSuffix);
	
	static final String ON="on", OFF="off";
	
	AcuAction prevAction;
	
	public static void main(String[] args) throws Exception {
		launch(simulation.getSimulator(), false);
	}

	/* (non-Javadoc)
	 * @see il.ac.technion.cs.smarthouse.developers_api.SmarthouseApplication#onLoad()
	 */
	/**
	 * [[SuppressWarningsSpartan]]
	 */
	@Override
	public void onLoad() throws Exception {
		final GuiBinderObject<Integer> currentTemp = new GuiBinderObject<>(20);
		final GuiBinderObject<Integer> wantedTemp = new GuiBinderObject<>(18);
		final GuiBinderObject<String> AcuState = new GuiBinderObject<>(OFF);
		
		final SensorApi<ACUSensor> sensor = super.<SensorsService>getService(ServiceType.SENSORS_SERVICE)
																	.getSensor(Simulation.commname, ACUSensor.class);
		sensor.subscribe(data -> {
				//update GUI
				currentTemp.setData(data.getTemp());
				AcuState.setData(data.isOn() ? ON : OFF);
				
				//instruct sensor
				Integer want = wantedTemp.getData(100), have = currentTemp.getData(0);
				AcuAction nextAction = (want < have) ? AcuAction.HOTTER : (want > have)? AcuAction.COLDER: AcuAction.STOP;
				if(!nextAction.equals(prevAction)){
					sensor.instruct(nextAction+"", setStatePath);
					prevAction = nextAction;
				}
		});
		
		sensor.instruct(wantedTemp.getData()+0.1+"", setTempPath); //init so the simulation and the gui are on the same page
		
		wantedTemp.addOnDataChangedListener(
				desiredT -> desiredT.getDataAsOptional().ifPresent(t->sensor.instruct(t+0.2+"", setTempPath)));
		
		getAppBuilder().getStatusRegionBuilder()
											.addStatusField("Current temperature in controlled location:", currentTemp)
											.addStatusField("The air conditioner in controlled location is", AcuState);
		
		getAppBuilder().getConfigurationsRegionBuilder()
											.addIntegerInputField("Desired Temperature:", wantedTemp)
											.addSensorAliasSelectionField("The Controlled location:", sensor, 
													(oldA, newA)->{
														super.<SensorsService>getService(ServiceType.SENSORS_SERVICE)
														.getSensor(Simulation.commname, ACUSensor.class, oldA)
														.instruct(AcuAction.STOP+"", setStatePath);
														
														wantedTemp.getDataAsOptional()
																	.ifPresent(t->
															super.<SensorsService>getService(ServiceType.SENSORS_SERVICE)
															.getSensor(Simulation.commname, ACUSensor.class, newA)
															.instruct(t+0.3+"", setTempPath)
														);
													}
													);

		simulation.getAliases()
					.forEach( alias-> {
						SensorApi<ACUSensor> currSensor = super.<SensorsService>getService(ServiceType.SENSORS_SERVICE)
															.getSensor(Simulation.commname, ACUSensor.class, alias);
						getAppBuilder().getWidgetsRegionBuilder()
										.addWidget(WidgetType.PROGRESS_LINE_GRAPH, 
													new InfoCollector()
														.addInfoEntry(getTempPath, "temperature")
														.setTitle("At "+alias), 
													currSensor, 
													acu -> {
														Map<String,Object> data = new HashMap<>();
														data.put(getTempPath, acu.getTemp());
														return data;
													});
						}
					);
		
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
