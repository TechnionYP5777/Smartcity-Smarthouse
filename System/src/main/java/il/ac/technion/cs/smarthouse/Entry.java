package il.ac.technion.cs.smarthouse;

import il.ac.technion.cs.smarthouse.sensors.sos.gui.SosSensorSimulator;
import il.ac.technion.cs.smarthouse.sensors.stove.gui.StoveSensorSimulator;
import il.ac.technion.cs.smarthouse.sensors.vitals.gui.VitalsSensorSimulator;
import il.ac.technion.cs.smarthouse.system_presenter.SystemPresenterFactory;
import il.ac.technion.cs.smarthouse.utils.JavaFxHelper;

public class Entry {

    public static void main(final String[] args) {
        new SystemPresenterFactory()
                        .setUseCloudServer(false) // false for now, because there is only one instance of the cloud server
                        .enableLocalDatabase(true)
                        .build();
        JavaFxHelper.startGui(new SosSensorSimulator());
        JavaFxHelper.startGui(new StoveSensorSimulator());
        JavaFxHelper.startGui(new VitalsSensorSimulator());
    }

}
