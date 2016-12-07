package temp2.ElderlySupport.sensors;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * @author Sharon
 * @since 7.12.16
 */
public class SensorTest {
	private Sensor sensor;

	@Before
	public void initSensor() {
		this.sensor = new Sensor("Default Sensor") {
			@Override
			protected String[] getObservationsNames() {
				return new String[] { "name", "last name" };
			}
		};
	}

	@Test
	public void initializedNameIsCorrect() {
		Assert.assertEquals("Default Sensor", this.sensor.getName());
	}

	@Test
	public void nameIsCorrectAfterChanging() {
		this.sensor.setName("Stove Sensor");
		Assert.assertEquals("Stove Sensor", this.sensor.getName());
	}

	@Test
	public void initializedObsevationsNamesAreCorrect() {
		Assert.assertArrayEquals(new String[] { "name", "last name" }, this.sensor.getObservationsNames());
	}
}
