package il.ac.technion.cs.smarthouse.utils;

import org.junit.Assert;
import org.junit.Test;

/** @author Inbal Zukerman
 * @since 8.12.2016 */
public class TupleTest {
    private final Tuple<String, String> tupleStrStr = new Tuple<>("A", "a");
    private final Tuple<String, Integer> tupleStrInt = new Tuple<>("A", 7);
    private final Tuple<Integer, Integer> tupleIntInt = new Tuple<>(8, 7);
    private final Tuple<String, String> nullTuple = new Tuple<>(null, null);

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
        assert tupleIntInt.equals(tupleIntInt);
        assert !tupleStrInt.equals(tupleIntInt);
        assert tupleStrInt.equals(tupleStrInt);
        final Tuple<String, String> newTuple = new Tuple<>("A", "a");
        assert tupleStrStr.equals(newTuple);
        assert newTuple.equals(tupleStrStr);

        final Tuple<String, String> leftNullTuple = new Tuple<>(null, "a"), rightNullTuple = new Tuple<>("A", null);
        assert !nullTuple.equals(tupleStrStr);
        assert !leftNullTuple.equals(tupleStrStr);
        assert !rightNullTuple.equals(tupleStrStr);

        assert !nullTuple.equals(Integer.valueOf(7));
        assert !"testing".equals(newTuple);

        assert !tupleStrInt.equals(nullTuple);
        assert !rightNullTuple.equals(tupleStrInt);
        assert !nullTuple.equals(leftNullTuple);
        assert !rightNullTuple.equals(leftNullTuple);
    }

    @Test public void hashCodeTest() {
        Assert.assertEquals(tupleIntInt.hashCode(), tupleIntInt.hashCode());
        Assert.assertEquals(nullTuple.hashCode(), nullTuple.hashCode());
        Assert.assertNotEquals(tupleIntInt.hashCode(), tupleStrInt.hashCode());
    }

}
