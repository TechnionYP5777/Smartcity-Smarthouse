/**
 * 
 */
package il.ac.technion.cs.smarthouse.applications.dashboard;

/**
 * @author Elia Traore
 * @since May 30, 2017
 */
public enum TileType {
	CLOCK, MEDUSA_CLOCK;
	
	public static TileType fromstring(String from){
		return TileType.CLOCK;
	}
}
