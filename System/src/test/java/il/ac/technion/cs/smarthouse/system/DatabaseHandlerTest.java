package il.ac.technion.cs.smarthouse.system;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import il.ac.technion.cs.smarthouse.system.exceptions.SensorNotFoundException;

/**
 * @author Sharon
 * @author Inbal Zukerman
 */

// TODO: inbal
public class DatabaseHandlerTest {
	private DatabaseHandler handler;

	@Before
	public void init() {
		handler = new DatabaseHandler();
	}

	

	@Test(expected = SensorNotFoundException.class)
	public void getLocationOfNotAddedSensorThrows() throws SensorNotFoundException {
		handler.getSensorLocation("00");
	}

	@Test(expected = SensorNotFoundException.class)
	public void setLocationOfNotAddedSensorThrows() throws SensorNotFoundException {
		handler.setSensorLocation("00", SensorLocation.BASEMENT);
	}

	@Test
	public void newSensorLocationIsUndefined() throws SensorNotFoundException {
		handler.addSensor("00", 100);
		Assert.assertEquals(SensorLocation.UNDEFINED, handler.getSensorLocation("00"));
	}

	@Test
	public void correctlySetSensorLocation() throws SensorNotFoundException {
		handler.addSensor("00", 100);
		handler.setSensorLocation("00", SensorLocation.BATHROOM);
		Assert.assertEquals(SensorLocation.BATHROOM, handler.getSensorLocation("00"));
	}

}
