package il.ac.technion.cs.smarthouse;

import il.ac.technion.cs.smarthouse.mvp.system.SystemPresenterFactory;
import il.ac.technion.cs.smarthouse.sensors.sos.gui.SosSensorSimulator;
import il.ac.technion.cs.smarthouse.sensors.stove.gui.StoveSensorSimulator;
import il.ac.technion.cs.smarthouse.sensors.vitals.gui.VitalsSensorSimulator;
import il.ac.technion.cs.smarthouse.utils.JavaFxHelper;

public class Entry {

    public static void main(final String[] args) {
        new SystemPresenterFactory().setUseCloudServer(false).setRegularFileSystemListeners(false).build();
        JavaFxHelper.startGui(new SosSensorSimulator());
        JavaFxHelper.startGui(new StoveSensorSimulator());
        JavaFxHelper.startGui(new VitalsSensorSimulator());
    }

}
