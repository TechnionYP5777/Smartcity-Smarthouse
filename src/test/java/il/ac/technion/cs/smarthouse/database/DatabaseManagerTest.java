package il.ac.technion.cs.smarthouse.database;

import java.util.HashMap;
import java.util.Map;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.parse4j.ParseException;
import org.parse4j.ParseObject;
import org.parse4j.ParseQuery;
import org.parse4j.ParseUser;

/** @author Inbal Zukerman
 * @date Apr 5, 2017 */
public class DatabaseManagerTest {

    public static String testParse = "DatabaseManagerTest";

    @BeforeClass
    public static void init() {
        try {
            ParseUser.currentUser = new ParseUser();
            DatabaseManager.initialize();
            Map<String, Object> m = new HashMap<>();
            m.put("col1", "res1");
            m.put("col2", 1234);
            DatabaseManager.putValue(testParse, m);

        } catch (ParseException ¢) {
            ¢.printStackTrace();

        }
    }

    @Test
    @SuppressWarnings("static-method")  // JUnit tests cannot be static
    public void dataManagmentTest() throws ParseException {
        Map<String, Object> m = new HashMap<>();
        m.put("col1", "test1");
        m.put("col1", "test1a");
        m.put("col2", 123);

        ParseObject temp = DatabaseManager.putValue(testParse, m);

        Assert.assertEquals(123, DatabaseManager.getValue(testParse, temp.getObjectId()).getInt("col2"));
        Assert.assertEquals("test1a".compareTo(DatabaseManager.getValue(testParse, temp.getObjectId()).getString("col1")), 0);

        DatabaseManager.deleteById(testParse, temp.getObjectId());

        ParseQuery<ParseObject> countQuery = ParseQuery.getQuery(testParse);
        countQuery.whereEqualTo("col2", 123);
        Assert.assertEquals(0, countQuery.count());
    }

}
