package temp2.ElderlySupport.sensors;

import org.junit.Test;

import junit.framework.Assert;

/**
 * @author Sharon
 * @since 7.12.16
 */
@SuppressWarnings("static-method")
public class SensorTest {
	@Test
	public void testSensorCreation() {
		@SuppressWarnings("unused")
		Sensor sensor = new Sensor("Default Sensor");
	}

	@Test
	public void checkIfNameIsCorrect() {
		Assert.assertEquals("Default Sensor", new Sensor("Default Sensor").getName());
	}

	@Test
	public void nameIsCorrectAfterChanging() {
		Sensor sensor = new Sensor("Default Sensor");
		sensor.setName("Stove Sensor");
		Assert.assertEquals("Stove Sensor", sensor.getName());
	}
}
