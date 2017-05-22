package il.ac.technion.cs.smarthouse.database;

import org.parse4j.ParseException;
import org.parse4j.ParseObject;

/**
 * @author Inbal Zukerman
 * @date May 22, 2017
 */
public interface DatabaseAPI {

	/**
	 * This method adds new information to the DB on the server
	 * 
	 * @param info
	 *            The information to add to the DB
	 * @return The newly created parseObject which is saved on the server
	 * @throws ParseException
	 */
	public ParseObject addInfo(String info) throws ParseException;

	/**
	 * This method deletes all occurrences of information of a certain InfoType
	 * from the DB
	 * 
	 * @param infoType
	 *            The infoType to delete all information of
	 */
	public void deleteInfo(InfoType infoType);

	/**
	 * This method deletes from the DB the object containing specific
	 * information
	 * 
	 * @param info
	 *            The information to delete from the DB
	 */
	public void deleteInfo(String info);

}
