package sensors;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import sensors.Sensor;

/**
 * @author Sharon
 * @author Yarden
 * @since 7.12.16
 */
public class SensorTest {
	private Sensor sensor;

	@Before
	public void initSensor() {
		this.sensor = new Sensor("Default Sensor", "1") {
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
	public void initializedIdIsCorrect() {
		Assert.assertEquals("1", this.sensor.getId());
	}

	@Test
	public void nameIsCorrectAfterChanging() {
		this.sensor.setName("Stove Sensor");
		Assert.assertEquals("Stove Sensor", this.sensor.getName());
	}

	@Test
	public void initializedObservationsNamesAreCorrect() {
		Assert.assertArrayEquals(new String[] { "name", "last name" }, this.sensor.getObservationsNames());
	}
}
