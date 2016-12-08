package Utils;

import org.junit.Assert;
import org.junit.Test;

import Utils.Tuple;


/**
 * 
 * @author Inbal Zukerman
 * @since 8.12.2016
 *
 */

@SuppressWarnings("boxing")
public class TupleTest {
	
	private Tuple<String, String> tupleStrStr = new Tuple<String, String> ("A", "a");
	private Tuple<String, Integer> tupleStrInt = new Tuple<String, Integer>("A", 7);
	private Tuple<Integer, Integer> tupleIntInt = new Tuple<Integer, Integer>(8,7);
	
	@Test
	public void getLeftTest(){
		Assert.assertEquals("A", this.tupleStrStr.getLeft());
		Assert.assertTrue(this.tupleIntInt.getLeft() == 8);
		Assert.assertNotEquals(7, this.tupleStrInt.getLeft());
	}

	@Test
	public void getRightTest(){
		Assert.assertEquals("a", this.tupleStrStr.getRight());
		Assert.assertTrue(this.tupleIntInt.getRight() == 7);
		Assert.assertNotEquals("A", this.tupleStrInt.getRight());
	}
	
	@Test
	public void setLeftTest(){
		this.tupleIntInt.setLeft(9);
		Assert.assertFalse( this.tupleIntInt.getLeft() == 8);
		Assert.assertTrue( this.tupleIntInt.getLeft() == 9);
		
		this.tupleIntInt.setLeft(8);
		Assert.assertFalse( this.tupleIntInt.getLeft() == 9);
		Assert.assertTrue( this.tupleIntInt.getLeft() == 8);
		
		this.tupleStrStr.setLeft("BB");
		Assert.assertEquals("BB", this.tupleStrStr.getLeft());
		Assert.assertNotEquals("BB", this.tupleStrStr.getRight());
	}
	
	@Test
	public void setRightTest(){
		this.tupleIntInt.setRight(0);
		Assert.assertFalse( this.tupleIntInt.getRight() == 7);
		Assert.assertTrue( this.tupleIntInt.getRight() == 0);
		
		this.tupleIntInt.setRight(7);
		Assert.assertFalse( this.tupleIntInt.getRight() == 0);
		Assert.assertTrue( this.tupleIntInt.getRight() == 7);
		
		this.tupleStrStr.setRight("HI");
		Assert.assertEquals("HI", this.tupleStrStr.getRight());
		Assert.assertNotEquals("a", this.tupleStrStr.getRight());
	}
	
	@Test
	public void equalsTest(){
		Assert.assertFalse(this.tupleStrStr.equals(this.tupleStrInt));
		Assert.assertFalse(this.tupleStrInt.equals(this.tupleIntInt));
		Assert.assertTrue(this.tupleStrInt.equals(this.tupleStrInt));
		
		Tuple<String, String> newTuple = new Tuple<String, String>("A", "a");
		Assert.assertTrue(this.tupleStrStr.equals(newTuple));
		Assert.assertTrue(newTuple.equals(this.tupleStrStr));
		
		newTuple.setLeft("changed");
		Assert.assertFalse(this.tupleStrStr.equals(newTuple));
		
		
		
		
	}
	
}


