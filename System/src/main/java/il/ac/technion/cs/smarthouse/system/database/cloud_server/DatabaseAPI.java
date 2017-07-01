package il.ac.technion.cs.smarthouse.system.database.cloud_server;

import org.parse4j.ParseException;
import org.parse4j.ParseObject;

import il.ac.technion.cs.smarthouse.system.database.cloud_server.DatabaseManager.DataEntry;
import il.ac.technion.cs.smarthouse.system.file_system.FileSystemEntries;

/**
 * This interface defines the required methods for communication with the
 * database.
 * 
 * @author Inbal Zukerman
 * @since May 22, 2017
 */
public interface DatabaseAPI {

    /**
     * This method adds new information to the DB on the server
     * 
     * @param path
     *            The path on the data is relevant to
     * @param value
     *            the new value to save on the path
     * @return The newly created parseObject which is saved on the server
     * @throws ParseException
     */
    public ParseObject addInfo(String path, Object value) throws ParseException;

    /**
     * This method deletes all occurrences of information of a certain InfoType
     * from the DB
     * 
     * @param fsEntry
     *            The infoType to delete all information of
     */
    public void deleteInfo(FileSystemEntries fsEntry);

    /**
     * This method deletes all records from the DB with a certain path.
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
    public DataEntry getLastEntry(String... path);

}
