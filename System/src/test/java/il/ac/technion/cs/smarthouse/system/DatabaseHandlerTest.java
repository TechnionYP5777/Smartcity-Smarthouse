package il.ac.technion.cs.smarthouse.system;

import java.util.function.Consumer;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import il.ac.technion.cs.smarthouse.system.exceptions.SensorNotFoundException;

/**
 * @author Sharon
 * @author Inbal Zukerman
 */


//TODO: inbal
public class DatabaseHandlerTest {
	private DatabaseHandler handler;
	

	@Before
	public void init() {
		handler = new DatabaseHandler();
	}

	
	@Test(expected = SensorNotFoundException.class)
	public void throwsExceptionWhenAddingListenerToNonExistingSensor() throws SensorNotFoundException {
		handler.addListener("Some id", null);
	}

	@Test
	public void addListenerToExistingSensorWithoutException() throws SensorNotFoundException {
		handler.addSensor("00:11:22:33:44:55", 100);
		handler.addListener("00:11:22:33:44:55", null);
	}

	@Test(expected = SensorNotFoundException.class)
	public void removeNotAddedListenerAndGetException() throws SensorNotFoundException {
		handler.removeListener("1111", "2222");
	}

	
	@Test
	public void listenerIsCalledWhenAddingEntryToSensorTable() throws SensorNotFoundException {
		@SuppressWarnings("unchecked")
		final Consumer<String> listener = Mockito.mock(Consumer.class);

		handler.addSensor("00:22:44:66:88:00", 100);
		handler.addListener("00:22:44:66:88:00", listener);
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
