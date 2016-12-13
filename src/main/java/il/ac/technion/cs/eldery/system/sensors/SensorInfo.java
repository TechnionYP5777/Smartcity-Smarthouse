package il.ac.technion.cs.eldery.system.sensors;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

import il.ac.technion.cs.eldery.system.SensorInformationDatabase;
import il.ac.technion.cs.eldery.utils.Tuple;

public class SensorInfo<L, R> {
    List<Consumer<Tuple<L, R>>> listeners = new ArrayList<>();
    SensorInformationDatabase<L, R> database;

    public SensorInfo(String sensorId, int maxCapacity) {
        database = new SensorInformationDatabase<>(sensorId, maxCapacity);
    }

    public void addListener(Consumer<Tuple<L, R>> $) {
        listeners.add($);
    }

    public Tuple<L, R> getLastUpdate() {
        return database.getLastUpdate();
    }
}
