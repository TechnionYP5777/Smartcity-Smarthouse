package applications;

import system.MainSystem;


/**
 * @author roysh
 * @since 8.12.2016
 */

public abstract class BaseApplication {
	MainSystem mainSystem;
	
	/**
	 * Adds the mainSystem Object to the app
	 * 
	 * @param mainSystem
	 * 
	 * @return true if successful or false if failed  
	 */
	public boolean setMainSystemInstance(MainSystem ¢){
		this.mainSystem = ¢;
		return true;
	}
	
	/**
	 * the function that will run when the system installs the app in the system  
	 */
	public abstract void onInstall();
	
	/**
	 * The main loop of the application. Will be runed on a thread in the main system  
	 */
	public abstract void main();
	
	/**
	 * Adds the app to the listener list of a specific sensor
	 * 
	 * @param sensorID
	 * 
	 * @return true if successful or false if failed  
	 */
	@SuppressWarnings("static-method")
	public boolean subscribeToSensor(String sensorID){
		return true;
	}
	
	/**
	 * Check if the sensor specified is active in the house
	 * 
	 * @param sensorID
	 * 
	 * @return  true if the main system has this sensor or false otherwise   
	 */
	@SuppressWarnings("static-method")
	public boolean checkIfSensorExists(String sensorID){
		return true;
	}
	
	
}
