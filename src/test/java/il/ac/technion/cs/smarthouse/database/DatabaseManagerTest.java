package il.ac.technion.cs.smarthouse.database;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;

import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.parse4j.ParseException;
import org.parse4j.ParseObject;
import org.parse4j.ParseQuery;
import org.parse4j.callback.SaveCallback;

/** @author Inbal Zukerman
 * @date Apr 5, 2017 */
public class DatabaseManagerTest {

    public static String testParse = "DatabaseManagerTest";

    private static ParseObject temp;
    private static ParseObject temp1;
    private static ParseObject temp2;
    private static ParseObject mObj;

    @BeforeClass public static void init() {

        DatabaseManager.initialize();

    }

    @Test @SuppressWarnings("static-method") public void dataManagmentTest() {
        try {
            final Map<String, Object> m = new HashMap<>();
            m.put("col1", "test1");
            m.put("col1", "test1a");
            m.put("col2", 123);
            temp = DatabaseManager.putValue(testParse, m);

            assert DatabaseManager.isInDB(testParse, temp.getObjectId());

            final Map<String, Object> m1 = new HashMap<>();
            m1.put("col1", "res1");
            m1.put("col2", 1234);
            temp1 = DatabaseManager.putValue(testParse, m1);

            Assert.assertEquals(123, DatabaseManager.getValue(testParse, temp.getObjectId()).getInt("col2"));
            Assert.assertEquals("test1a".compareTo(DatabaseManager.getValue(testParse, temp.getObjectId()).getString("col1")), 0);

            DatabaseManager.deleteById(testParse, temp.getObjectId());
            assert !DatabaseManager.isInDB(testParse, temp.getObjectId());

            ParseQuery<ParseObject> countQuery = ParseQuery.getQuery(testParse);
            countQuery.whereEqualTo("col2", 123);

            Assert.assertEquals(0, countQuery.count());

            countQuery = ParseQuery.getQuery(testParse);
            countQuery.whereEqualTo("col2", 1234);

            Assert.assertEquals(1, countQuery.count());

            DatabaseManager.deleteById(testParse, temp1.getObjectId());
            countQuery.whereEqualTo("col2", 1234);

            Assert.assertEquals(0, countQuery.count());

        } catch (final ParseException e) {
            assert null != null;
        }
    }

    @Test @SuppressWarnings("static-method") public void testingUpdates() {

        try {
            final Map<String, Object> m = new HashMap<>();
            m.put("col1", "one");
            m.put("col2", 1);

            mObj = DatabaseManager.putValue(testParse, m);

            Assert.assertEquals("one".compareTo(DatabaseManager.getValue(testParse, mObj.getObjectId()).getString("col1")), 0);
            Assert.assertEquals(1, DatabaseManager.getValue(testParse, mObj.getObjectId()).getInt("col2"));

            final Map<String, Object> newVals = new HashMap<>();
            newVals.put("col2", 2);
            DatabaseManager.update(testParse, mObj.getObjectId(), newVals);

            Thread.sleep(1000); // Server operations take a while....
            Assert.assertEquals("one".compareTo(DatabaseManager.getValue(testParse, mObj.getObjectId()).getString("col1")), 0);
            Assert.assertEquals(2, DatabaseManager.getValue(testParse, mObj.getObjectId()).getInt("col2"));

            DatabaseManager.deleteById(testParse, mObj.getObjectId());

        } catch (InterruptedException e) {
            assert null != null;
        }

    }

    @Test(timeout = 30000) @SuppressWarnings("static-method") public void savingInBackgound() {

        final Map<String, Object> m = new HashMap<>();
        m.put("col1", "AA");
        m.put("col2", 12);

        final AtomicBoolean value = new AtomicBoolean();
        boolean changed = false;

        DatabaseManager.putValue(testParse, m, new SaveCallback() {

            @Override public void done(final ParseException arg0) {
                if (arg0 != null)
                    value.compareAndSet(false, false);
                value.compareAndSet(false, true);

            }
        });

        while (!changed)
            changed = value.getAndSet(false);

        Assert.assertEquals(true, changed);

        temp2 = DatabaseManager.getObjectByFields(testParse, m);
        DatabaseManager.deleteById(testParse, temp2.getObjectId());
        Assert.assertNull(DatabaseManager.getValue(testParse, temp2.getObjectId()));

    }

    @AfterClass public static void cleanup() {
        DatabaseManager.deleteById(testParse, temp.getObjectId());
        DatabaseManager.deleteById(testParse, temp1.getObjectId());
        DatabaseManager.deleteById(testParse, temp2.getObjectId());
        DatabaseManager.deleteById(testParse, mObj.getObjectId());

    }

}
