package il.ac.technion.cs.smarthouse.system.database;

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
 * Saves system data to local file
 * 
 * @author RON
 * @since 18-06-2017
 */
public class LocalSaver {
    private static Logger log = LoggerFactory.getLogger(LocalSaver.class);

    public static final String DATA_FILE_PATH = "SmarthouseDb.data";

    private static File getFile(String path) {
        File db = new File(Optional.ofNullable(path).orElse(DATA_FILE_PATH));

        if (!db.exists())
            try {
                db.createNewFile();
            } catch (IOException e) {
                log.error("Couldn't create file " + db.getAbsolutePath(), e);
            }

        return db;
    }

    public static synchronized void saveData(String data) {
        saveData(data, null);
    }

    public static synchronized String readData() {
        return readData(null);
    }

    public static synchronized void saveData(String data, String path) {
        File f = getFile(path);
        try (BufferedWriter o = new BufferedWriter(new FileWriter(f))) {
            o.write(data);
            o.flush();
            log.info("\n\tLocalSaver: Wrote data to file\n\tFile name: " + f.getAbsolutePath() + "\n\tData: " + data);
        } catch (NumberFormatException | IOException e) {
            log.error("Can't write to file (" + f.getAbsolutePath() + ")", e);
        }
    }

    public static synchronized String readData(String path) {
        File f = getFile(path);
        try (BufferedReader o = new BufferedReader(new FileReader(f))) {
            String data = o.readLine();
            log.info("\n\tLocalSaver: Read data from file\n\tFile name: " + f.getAbsolutePath() + "\n\tData: " + data);
            return data;
        } catch (NumberFormatException | IOException e) {
            log.error("Can't write to file (" + f.getAbsolutePath() + ")", e);
        }

        return null;
    }
}
