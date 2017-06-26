package il.ac.technion.cs.smarthouse;

import java.util.ArrayList;
import java.util.Collection;

import il.ac.technion.cs.smarthouse.DeveloperSimulator.DeveloperSimulatorGui;
import il.ac.technion.cs.smarthouse.sensors.simulator.GenericSensor;
import il.ac.technion.cs.smarthouse.sensors.simulator.SensorsSimulator;
import il.ac.technion.cs.smarthouse.sensors.sos.gui.SosSensorSimulator;
import il.ac.technion.cs.smarthouse.sensors.stove.gui.StoveSensorSimulator;
import il.ac.technion.cs.smarthouse.sensors.vitals.gui.VitalsSensorSimulator;
import il.ac.technion.cs.smarthouse.system_presenter.SystemPresenter;
import il.ac.technion.cs.smarthouse.system_presenter.SystemPresenterFactory;
import il.ac.technion.cs.smarthouse.utils.JavaFxHelper;

public class Entry {

    public static void main(final String[] args) {
        final SystemPresenter p =new SystemPresenterFactory()
                        .setUseCloudServer(false) // false for now, because there is only one instance of the cloud server
                        .enableLocalDatabase(true)
                        .enableLocalDatabase(false)
                        .build();
    }

}
