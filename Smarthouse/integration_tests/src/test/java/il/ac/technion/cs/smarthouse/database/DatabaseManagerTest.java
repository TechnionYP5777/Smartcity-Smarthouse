package il.ac.technion.cs.smarthouse.database;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.parse4j.ParseException;
import org.parse4j.ParseObject;
import org.parse4j.ParseQuery;
import org.parse4j.callback.GetCallback;
import org.parse4j.callback.SaveCallback;

/** @author Inbal Zukerman
 * @date Apr 5, 2017 */
public class DatabaseManagerTest {

    public static String testParse = "DatabaseManagerTest";

    @BeforeClass public static void init() {

        DatabaseManager.initialize();

    }

    @Test @SuppressWarnings("static-method") // JUnit tests cannot be static
    public void dataManagmentTest() {
        try {
            Map<String, Object> m = new HashMap<>();
            m.put("col1", "test1");
            m.put("col1", "test1a");
            m.put("col2", 123);
            ParseObject temp = DatabaseManager.putValue(testParse, m);

            Map<String, Object> m1 = new HashMap<>();
            m1.put("col1", "res1");
            m1.put("col2", 1234);
            ParseObject temp1 = DatabaseManager.putValue(testParse, m1);

            Assert.assertEquals(123, DatabaseManager.getValue(testParse, temp.getObjectId()).getInt("col2"));
            Assert.assertEquals("test1a".compareTo(DatabaseManager.getValue(testParse, temp.getObjectId()).getString("col1")), 0);

            DatabaseManager.deleteById(testParse, temp.getObjectId());

            ParseQuery<ParseObject> countQuery = ParseQuery.getQuery(testParse);
            countQuery.whereEqualTo("col2", 123);
            Assert.assertEquals(0, countQuery.count());

            countQuery = ParseQuery.getQuery(testParse);
            countQuery.whereEqualTo("col2", 1234);
            Assert.assertEquals(1, countQuery.count());

            DatabaseManager.deleteById(testParse, temp1.getObjectId());

            countQuery.whereEqualTo("col2", 1234);
            Assert.assertEquals(0, countQuery.count());
        } catch (ParseException e) {
            assert null != null;
        }

    }

    @Test @SuppressWarnings("static-method") public void testingUpdates() {

        try {
            Map<String, Object> m = new HashMap<>();
            m.put("col1", "one");
            m.put("col2", 1);

            ParseObject mObj = DatabaseManager.putValue(testParse, m);

            Assert.assertEquals("one".compareTo(DatabaseManager.getValue(testParse, mObj.getObjectId()).getString("col1")), 0);
            Assert.assertEquals(1, DatabaseManager.getValue(testParse, mObj.getObjectId()).getInt("col2"));

            Map<String, Object> newVals = new HashMap<>();
            newVals.put("col2", 2);
            DatabaseManager.update(testParse, mObj.getObjectId(), newVals);

            Thread.sleep(1000); // Server operations take a while....
            Assert.assertEquals("one".compareTo(DatabaseManager.getValue(testParse, mObj.getObjectId()).getString("col1")), 0);
            Assert.assertEquals(2, DatabaseManager.getValue(testParse, mObj.getObjectId()).getInt("col2"));

            DatabaseManager.deleteById(testParse, mObj.getObjectId());

        } catch (ParseException | InterruptedException e) {
            assert null != null;
        }

    }

    @Test(timeout = 30000) @SuppressWarnings("static-method") public void savingInBackgound() {

        Map<String, Object> m = new HashMap<>();
        m.put("col1", "AA");
        m.put("col2", 12);

        final AtomicBoolean value = new AtomicBoolean();
        boolean changed = false;

        DatabaseManager.putValue(testParse, m, new SaveCallback() {

            @Override public void done(ParseException arg0) {
                if (arg0 != null)
                    value.compareAndSet(false, false);
                value.compareAndSet(false, true);

            }
        });

        while (!changed)
            changed = value.getAndSet(false);

        Assert.assertEquals(true, changed);

        DatabaseManager.getObjectByFields(testParse, m, new GetCallback<ParseObject>() {

            @Override public void done(ParseObject arg0, ParseException arg1) {
                if (arg0 == null || arg1 != null) {
                    assert null != null;
                    return;
                }

                DatabaseManager.deleteById(testParse, arg0.getObjectId());
                Assert.assertNull(DatabaseManager.getValue(testParse, arg0.getObjectId()));

            }
        });

    }
}
