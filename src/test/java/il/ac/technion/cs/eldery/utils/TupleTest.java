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
    Assert.assertEquals("A", tupleStrStr.getLeft());
    Assert.assertEquals(8, (int)tupleIntInt.getLeft());
    Assert.assertNotEquals(7, tupleStrInt.getLeft());
  }

  @Test public void getRightTest() {
    Assert.assertEquals("a", tupleStrStr.getRight());
    Assert.assertEquals(7, (int)tupleIntInt.getRight());
    Assert.assertNotEquals("A", tupleStrInt.getRight());
  }

  @Test public void setLeftTest() {
    tupleIntInt.setLeft(9);
    Assert.assertNotEquals(8, (int)tupleIntInt.getLeft());
    Assert.assertEquals(9, (int) tupleIntInt.getLeft());
    tupleIntInt.setLeft(8);
    Assert.assertNotEquals(9, (int)tupleIntInt.getLeft());
    Assert.assertEquals(8, (int) tupleIntInt.getLeft());
    tupleStrStr.setLeft("BB");
    Assert.assertEquals("BB", tupleStrStr.getLeft());
    Assert.assertNotEquals("BB", tupleStrStr.getRight());
  }

  @Test public void setRightTest() {
    tupleIntInt.setRight(0);
    Assert.assertNotEquals(9, (int)tupleIntInt.getRight());
    Assert.assertEquals( 0, (int)tupleIntInt.getRight());
    tupleIntInt.setRight(7);
    Assert.assertNotEquals(0, (int) tupleIntInt.getRight());
    Assert.assertEquals(7, (int) tupleIntInt.getRight());
    tupleStrStr.setRight("HI");
    Assert.assertEquals("HI", tupleStrStr.getRight());
    Assert.assertNotEquals("a", tupleStrStr.getRight());
  }

  @Test public void equalsTest() {
    Assert.assertFalse(tupleStrStr.equals(tupleStrInt));
    Assert.assertFalse(tupleStrInt.equals(tupleIntInt));
    Assert.assertTrue(tupleStrInt.equals(tupleStrInt));
    final Tuple<String, String> newTuple = new Tuple<>("A", "a");
    Assert.assertTrue(tupleStrStr.equals(newTuple));
    Assert.assertTrue(newTuple.equals(tupleStrStr));
    newTuple.setLeft("changed");
    Assert.assertFalse(tupleStrStr.equals(newTuple));
  }
}
