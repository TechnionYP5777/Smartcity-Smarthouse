package il.ac.technion.cs.smarthouse.system;

import java.util.Arrays;

import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;
import org.junit.runner.RunWith;

import org.junit.Assert;
import org.junit.Test;

/** @author Yarden
 * @since 6.4.17 */

@RunWith(Parameterized.class)
public class SensorLocationTest {
    String strLocation;
    SensorLocation enumLocation;

    public SensorLocationTest(String strLocation, SensorLocation enumLocation) {
        this.strLocation = strLocation;
        this.enumLocation = enumLocation;
    }

    @Parameters(name = "{index}: fromString({0})={1})") public static Iterable<Object[]> locations() {
        return Arrays.asList(new Object[][] { { "UNDEFINED", SensorLocation.UNDEFINED }, { "kitchen", SensorLocation.KITCHEN },
                { "DINING_room", SensorLocation.DINING_ROOM }, { "BeDrOoM", SensorLocation.BEDROOM }, { "BATHroom", SensorLocation.BATHROOM },
                { "living_ROOM", SensorLocation.LIVING_ROOM }, { "tv_room", SensorLocation.TV_ROOM }, { "stuDy", SensorLocation.STUDY },
                { "yard", SensorLocation.YARD }, { "PORCH", SensorLocation.PORCH }, { "baseMent", SensorLocation.BASEMENT },
                { "Hallway", SensorLocation.HALLWAY }, { "GaraGE", SensorLocation.GARAGE }, { "notAlocation", null } });
    }

    @Test public void validate() {
        Assert.assertEquals(enumLocation, SensorLocation.fromString(strLocation));
    }

}
