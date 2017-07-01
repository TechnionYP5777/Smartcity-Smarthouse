package il.ac.technion.cs.smarthouse.system.database.local_cache;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author RON
 * @since 18-06-2017
 * 
 *        Saves system data to local file
 */
public class LocalSaver {
    private static Logger log = LoggerFactory.getLogger(LocalSaver.class);

    /**
     * Default system DB file path
     */
    public static final String DATA_FILE_PATH = "SmarthouseDb.data";

    private static File getFile(String path) {
        File db = new File(Optional.ofNullable(path).orElse(DATA_FILE_PATH));

        if (!db.exists())
            try {
                db.createNewFile();
            } catch (IOException e) {
                log.error("\n\tCouldn't create file " + db.getAbsolutePath(), e);
            }

        return db;
    }

    /**
     * Saves data to the default file: {@link #DATA_FILE_PATH}
     * 
     * @param data
     *            the data to save
     */
    public static synchronized void saveData(String data) {
        saveData(data, null);
    }

    /**
     * Reads data from the default file: {@link #DATA_FILE_PATH}
     * 
     * @return the file's content or null if a problem occurred
     */
    public static synchronized String readData() {
        return readData(null);
    }

    /**
     * Save data to a file
     * 
     * @param data
     *            the data to save in the file
     * @param path
     *            the file's path
     */
    public static synchronized void saveData(String data, String path) {
        File f = getFile(path);
        try (BufferedWriter o = new BufferedWriter(new FileWriter(f))) {
            o.write(data);
            o.flush();
            log.trace("\n\tLocalSaver: Wrote data to file\n\tFile name: " + f.getAbsolutePath() + "\n\tData: " + data);
            log.info("\n\tLocalSaver: Wrote data to file\n\tFile name: " + f.getAbsolutePath());
        } catch (NumberFormatException | IOException e) {
            log.error("\n\tCan't write to file (" + f.getAbsolutePath() + ")", e);
        }
    }

    /**
     * Read the file's content and return it
     * 
     * @param path
     *            the file's path
     * @return the file's content or null if a problem occurred
     */
    public static synchronized String readData(String path) {
        File f = getFile(path);
        try (BufferedReader o = new BufferedReader(new FileReader(f))) {
            String data = o.readLine();
            log.trace("\n\tLocalSaver: Read data from file\n\tFile name: " + f.getAbsolutePath() + "\n\tData: " + data);
            log.info("\n\tLocalSaver: Read data from file\n\tFile name: " + f.getAbsolutePath());
            return data;
        } catch (NumberFormatException | IOException e) {
            log.error("\n\tCan't write to file (" + f.getAbsolutePath() + ")", e);
        }

        return null;
    }
}
