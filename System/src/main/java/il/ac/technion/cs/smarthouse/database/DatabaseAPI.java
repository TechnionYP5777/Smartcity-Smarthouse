package il.ac.technion.cs.smarthouse.database;

import java.util.Collection;

import org.parse4j.ParseException;
import org.parse4j.ParseObject;

import il.ac.technion.cs.smarthouse.system.InfoType;

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
    public ParseObject addInfo( String path, Object value) throws ParseException;

    /**
     * This method deletes all occurrences of information of a certain InfoType
     * from the DB
     * 
     * @param infoType
     *            The infoType to delete all information of
     */
    public void deleteInfo(InfoType infoType);

    /**
     * This method deletes all record from the DB with a certain path.
     * 
     * @param path
     */
    public void deleteInfo(String... path);

    /**
     * This method allows to query the last record saved in the DB on a specific
     * path
     * 
     * @param path
     *            The path to find the last entry of
     * @return The last entry (full path+value)
     */
    public String getLastEntry(String... path);

    /**
     * This methods returns a collection which contains all children saved for a
     * certain path
     * 
     * @param path
     *            the path to find all children of
     * @return Collection with the children
     */
    public Collection<String> getPathChildren(String... path);
}
