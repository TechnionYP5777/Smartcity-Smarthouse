package il.ac.technion.cs.smarthouse.networking.messages;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;

import org.junit.Assert;
import org.junit.Test;
import org.mockito.Matchers;
import org.mockito.Mockito;

import il.ac.technion.cs.smarthouse.networking.messages.Message;

/** @author Sharon
 * @since 30.12.16 */
public abstract class MessageTest {
    protected abstract Message defaultMessage();

    @Test public void testMessageDeliveryWithoutInput() {
        Assert.assertNull(defaultMessage().send(null, null));
    }

    @Test public void testMessageDeliveryWithoutResponse() {
        final Message message = defaultMessage();
        @SuppressWarnings("resource") final PrintWriter pw = Mockito.mock(PrintWriter.class);

        Assert.assertNull(message.send(pw, null));
        Mockito.verify(pw, Mockito.times(1)).println(Matchers.anyString());
    }

    @Test public void testMessageDeliveryWithResponse() throws IOException {
        final Message message = defaultMessage();
        @SuppressWarnings("resource") final PrintWriter pw = Mockito.mock(PrintWriter.class);
        @SuppressWarnings("resource") final BufferedReader br = Mockito.mock(BufferedReader.class);
        Mockito.when(br.readLine()).thenReturn("A response");

        Assert.assertEquals("A response", message.send(pw, br));
        Mockito.verify(pw, Mockito.times(1)).println(Matchers.anyString());
        Mockito.verify(br, Mockito.times(1)).readLine();
    }
}
