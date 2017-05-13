package il.ac.technion.cs.smarthouse.networking.messages;

import org.junit.Assert;
import org.junit.Test;

/** @author Sharon
 * @since 30.12.16 */



public  class MessageTest {
    

	@Test public void testMessage(){
		String m1 = Message.createMessage("11", "sensor11", MessageType.REGISTRATION, "");
		Assert.assertEquals("11@sensor11@registration", m1);
		
		m1 = Message.createMessage("", "", MessageType.ANSWER, "success");
		Assert.assertEquals("answer@success", m1);
		
		Assert.assertNull(Message.send(m1, null, null));
		
		//TODO: inbal test more sending options
		
		
		//TODO: inbal, make sure to change this
		Assert.assertTrue(Message.isInMessage(m1, "answer"));
		Assert.assertFalse(Message.isInMessage(m1, "failure"));
	}

    
}
