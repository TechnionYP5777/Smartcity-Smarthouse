package il.ac.technion.cs.eldery.utils;

import org.junit.Assert;
import org.junit.Test;

/** @author Inbal Zukerman
 * @since 8.12.2016 */
@SuppressWarnings("boxing")
public class TupleTest {
    private final Tuple<String, String> tupleStrStr = new Tuple<>("A", "a");
    private final Tuple<String, Integer> tupleStrInt = new Tuple<>("A", 7);
    private final Tuple<Integer, Integer> tupleIntInt = new Tuple<>(8, 7);

    @Test public void getLeftTest() {
        Assert.assertEquals("A", tupleStrStr.left);
        Assert.assertEquals(8, (int) tupleIntInt.left);
        Assert.assertNotEquals(7, tupleStrInt.left);
    }

    @Test public void getRightTest() {
        Assert.assertEquals("a", tupleStrStr.right);
        Assert.assertEquals(7, (int) tupleIntInt.right);
        Assert.assertNotEquals("A", tupleStrInt.right);
    }

    @Test public void equalsTest() {
        assert !tupleStrStr.equals(tupleStrInt);
        assert !tupleStrInt.equals(tupleIntInt);
        assert tupleStrInt.equals(tupleStrInt);
        final Tuple<String, String> newTuple = new Tuple<>("A", "a");
        assert tupleStrStr.equals(newTuple);
        assert newTuple.equals(tupleStrStr);
    }
}
