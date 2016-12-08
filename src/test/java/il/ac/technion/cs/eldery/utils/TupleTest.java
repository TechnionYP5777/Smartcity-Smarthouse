package il.ac.technion.cs.eldery.utils;

import org.junit.*;

/** @author Inbal Zukerman
 * @since 8.12.2016 */
@SuppressWarnings("boxing")
public class TupleTest {
  private final Tuple<String, String> tupleStrStr = new Tuple<>("A", "a");
  private final Tuple<String, Integer> tupleStrInt = new Tuple<>("A", 7);
  private final Tuple<Integer, Integer> tupleIntInt = new Tuple<>(8, 7);

  @Test public void getLeftTest() {
    Assert.assertEquals("A", tupleStrStr.getLeft());
    Assert.assertTrue(tupleIntInt.getLeft() == 8);
    Assert.assertNotEquals(7, tupleStrInt.getLeft());
  }

  @Test public void getRightTest() {
    Assert.assertEquals("a", tupleStrStr.getRight());
    Assert.assertTrue(tupleIntInt.getRight() == 7);
    Assert.assertNotEquals("A", tupleStrInt.getRight());
  }

  @Test public void setLeftTest() {
    tupleIntInt.setLeft(9);
    Assert.assertFalse(tupleIntInt.getLeft() == 8);
    Assert.assertTrue(tupleIntInt.getLeft() == 9);
    tupleIntInt.setLeft(8);
    Assert.assertFalse(tupleIntInt.getLeft() == 9);
    Assert.assertTrue(tupleIntInt.getLeft() == 8);
    tupleStrStr.setLeft("BB");
    Assert.assertEquals("BB", tupleStrStr.getLeft());
    Assert.assertNotEquals("BB", tupleStrStr.getRight());
  }

  @Test public void setRightTest() {
    tupleIntInt.setRight(0);
    Assert.assertFalse(tupleIntInt.getRight() == 7);
    Assert.assertTrue(tupleIntInt.getRight() == 0);
    tupleIntInt.setRight(7);
    Assert.assertFalse(tupleIntInt.getRight() == 0);
    Assert.assertTrue(tupleIntInt.getRight() == 7);
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
