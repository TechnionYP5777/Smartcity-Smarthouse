package il.ac.technion.cs.smarthouse.utils;

import java.time.LocalTime;

import org.junit.Test;

public class TimedListenerTest {
	int counter;

	@Test(expected = AssertionError.class)
	public void badInitTest() {
		new TimedListener(null, null, null);
	}

	@Test
	public void startNow_noRep_Test() throws InterruptedException {
		new TimedListener(() -> counter++, null, null).start();
		Thread.sleep(500);
		assert counter == 1;
		Thread.sleep(500);
		assert counter == 1;
		Thread.sleep(500);
		assert counter == 1;
	}

	@Test
	public void startLater_noRep_Test() throws InterruptedException {
		new TimedListener(() -> counter++, LocalTime.now().plusSeconds(2), null).start();
		Thread.sleep(1000);
		assert counter == 0;
		Thread.sleep(1500);
		assert counter == 1;
		Thread.sleep(1000);
		assert counter == 1;
	}

	@Test
	public void startNow_rep_Test() throws InterruptedException {
		TimedListener t = new TimedListener(() -> counter++, null, 1000L);

		assert counter == 0;
		t.start();
		t.start();

		Thread.sleep(500);
		assert counter == 1;

		Thread.sleep(1000);
		assert counter == 2;

		Thread.sleep(1000);
		assert counter == 3;

		t.kill();
		t.kill();

		Thread.sleep(1000);
		assert counter == 3;

		Thread.sleep(1000);
		assert counter == 3;
	}

	@Test
	public void startLater_rep_Test() throws InterruptedException {
		TimedListener t = new TimedListener(() -> counter++, LocalTime.now().plusSeconds(2), 1000L);

		assert counter == 0;
		t.start();

		Thread.sleep(1500);
		assert counter == 0;

		Thread.sleep(1000);
		assert counter == 1;

		Thread.sleep(1000);
		assert counter == 2;

		Thread.sleep(1000);
		assert counter == 3;

		t.kill();

		Thread.sleep(1000);
		assert counter == 3;

		Thread.sleep(1000);
		assert counter == 3;
	}
}
