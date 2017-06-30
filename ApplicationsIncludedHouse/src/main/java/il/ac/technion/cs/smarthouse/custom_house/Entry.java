package il.ac.technion.cs.smarthouse.custom_house;

import java.util.concurrent.TimeUnit;

import org.reflections.Reflections;
import org.reflections.scanners.SubTypesScanner;

import il.ac.technion.cs.smarthouse.DeveloperSimulator.DeveloperSimulatorGui;
import il.ac.technion.cs.smarthouse.developers_api.SmarthouseApplication;
import il.ac.technion.cs.smarthouse.sensors.Simulatable;
import il.ac.technion.cs.smarthouse.sensors.simulator.SensorsSimulator;
import il.ac.technion.cs.smarthouse.system.SystemMode;
import il.ac.technion.cs.smarthouse.system.applications.installer.ApplicationPath;
import il.ac.technion.cs.smarthouse.system.applications.installer.ApplicationPath.PathType;
import il.ac.technion.cs.smarthouse.system_presenter.SystemPresenterFactory;
import il.ac.technion.cs.smarthouse.utils.JavaFxHelper;

/**
 * @author Elia Traore
 * @since Jun 26, 2017
 */
public class Entry {
	
	public static void main(final String[] args) {
		SensorsSimulator simulator = new SensorsSimulator()
										.setGeneralStreamingInteval(TimeUnit.SECONDS.toMillis(5));
		
		SystemPresenterFactory factory = new SystemPresenterFactory()
											.initMode(SystemMode.USER_MODE)
											.enableModePopup(false)
											.setUseCloudServer(false)
											.enableLocalDatabase(false);
		new Reflections("il.ac.technion.cs.smarthouse.applications", new SubTypesScanner(false))
				.getSubTypesOf(SmarthouseApplication.class)
				.forEach(cls -> {
					factory.addApplicationToInstall(new ApplicationPath(PathType.CLASS, cls));
					if(Simulatable.class.isAssignableFrom(cls))
						try {
							simulator.addAllSensor(((Simulatable)cls.newInstance()).getSimulatedSensors());
						} catch (InstantiationException | IllegalAccessException e){}
				});
		factory.build();

		new Thread(){

            @Override
            public void interrupt() {
            	simulator.stopSendingMsgsInAllSensors();
                super.interrupt();
            }

            @Override
            public void run() {
            	simulator.startSendingMsgsInAllSensors();
                JavaFxHelper.startGui(new DeveloperSimulatorGui().setSimulator(simulator));
                super.run();
            }
            
        }.start();
		        
				
		
	}
}
