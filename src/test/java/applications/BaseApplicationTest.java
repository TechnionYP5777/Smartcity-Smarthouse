package applications;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import system.MainSystem;


/**
 * @author Roy
 * @since 8.12.16
 */

class MockApp extends BaseApplication{
	
	public String testFeedbak;
	
	@Override
	public void onInstall() {
		this.testFeedbak = "onInstall";
	}
	
	@Override
	public void main() {
		this.testFeedbak = "main";
	}
}

public class BaseApplicationTest {
	MockApp testApp;
	MainSystem mainSystem;
	@Before
	public void createApplication(){
		this.testApp = new MockApp();
		this.mainSystem = new MainSystem();
	}
	
	@Test
	public void testSetMainSystem(){
		Assert.assertTrue(this.testApp.setMainSystemInstance(this.mainSystem));
	}
	
	@Test
	public void testOnInstall(){
		this.testApp.onInstall();
		Assert.assertEquals(this.testApp.testFeedbak,"onInstall");
	}
	
	@Test
	public void testMain(){
		this.testApp.main();
		Assert.assertEquals(this.testApp.testFeedbak,"main");
	}
	
	@Test
	public void testCheckIfSensorExists(){
		Assert.assertTrue(this.testApp.checkIfSensorExists("id"));
	}
	
	@Test
	public void testSubscribeSensorExists(){
		Assert.assertTrue(this.testApp.subscribeToSensor("id"));
	}
	
}
