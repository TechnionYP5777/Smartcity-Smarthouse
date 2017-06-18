package il.ac.technion.cs.smarthouse.database;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LocalSaver {
    private static Logger log = LoggerFactory.getLogger(LocalSaver.class);

    private static final String DATA_FILE_PATH = "SmarthouseDb.data";

    private static File getFile() throws IOException {        
        File db = new File(DATA_FILE_PATH);

        if (!db.exists())
            db.createNewFile();

        return db;
    }

    public static synchronized void saveData(String data) {
        try (BufferedWriter o = new BufferedWriter(new FileWriter(getFile()))) {
            o.write(data);
            o.flush();
            log.info("\n\tLocalSaver: Wrote data to file\n\tFile name: " + new File(DATA_FILE_PATH).getAbsolutePath() + "\n\tData: " + data);
        } catch (NumberFormatException | IOException e) {
            log.error("Can't write to file (" + DATA_FILE_PATH + ")", e);
        }
    }
    
    public static synchronized String readData() {
        try (BufferedReader o = new BufferedReader(new FileReader(getFile()))) {
            String data = o.readLine();
            log.info("\n\tLocalSaver: Read data from file\n\tFile name: " + new File(DATA_FILE_PATH).getAbsolutePath() + "\n\tData: " + data);
            return data;
        } catch (NumberFormatException | IOException e) {
            log.error("Can't write to file (" + DATA_FILE_PATH + ")", e);
        }
        
        return null;
    }
}
