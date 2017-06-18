package il.ac.technion.cs.smarthouse.database;

import java.util.HashMap;
import java.util.Map;

import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Test;
import org.parse4j.ParseException;
import org.parse4j.ParseObject;
import org.parse4j.ParseQuery;

import il.ac.technion.cs.smarthouse.system.database.cloud_server.ServerManager;

/**
 * @author Inbal Zukerman
 * @date Apr 5, 2017
 */
public class ServerManagerTest {

    public static String testParse = "DatabaseManagerTest";

    private static final ServerManager serverManager = new ServerManager();

    private static ParseObject temp;
    private static ParseObject temp1;
    private static ParseObject mObj;

    @Test
    @SuppressWarnings("static-method")
    public void dataManagmentTest() {
        try {
            final Map<String, Object> m = new HashMap<>();
            m.put("col1", "test1");
            m.put("col1", "test1a");
            m.put("col2", 123);
            temp = serverManager.putValue(testParse, m);

            assert serverManager.isInDB(testParse, temp.getObjectId());

            final Map<String, Object> m1 = new HashMap<>();
            m1.put("col1", "res1");
            m1.put("col2", 1234);
            temp1 = serverManager.putValue(testParse, m1);

            Assert.assertEquals(123, serverManager.getValue(testParse, temp.getObjectId()).getInt("col2"));
            Assert.assertEquals(
                            "test1a".compareTo(serverManager.getValue(testParse, temp.getObjectId()).getString("col1")),
                            0);

            serverManager.deleteById(testParse, temp.getObjectId());
            assert !serverManager.isInDB(testParse, temp.getObjectId());

            ParseQuery<ParseObject> countQuery = ParseQuery.getQuery(testParse);
            countQuery.whereEqualTo("col2", 123);

            Assert.assertEquals(0, countQuery.count());

            countQuery = ParseQuery.getQuery(testParse);
            countQuery.whereEqualTo("col2", 1234);

            Assert.assertEquals(1, countQuery.count());

            serverManager.deleteById(testParse, temp1.getObjectId());
            countQuery.whereEqualTo("col2", 1234);

            Assert.assertEquals(0, countQuery.count());

        } catch (final ParseException e) {
            e.printStackTrace();
            assert false;
        }
    }

    @Test
    @SuppressWarnings("static-method")
    public void testingUpdates() {

        try {
            final Map<String, Object> m = new HashMap<>();
            m.put("col1", "one");
            m.put("col2", 1);

            mObj = serverManager.putValue(testParse, m);

            Assert.assertEquals(
                            "one".compareTo(serverManager.getValue(testParse, mObj.getObjectId()).getString("col1")),
                            0);
            Assert.assertEquals(1, serverManager.getValue(testParse, mObj.getObjectId()).getInt("col2"));

            final Map<String, Object> newVals = new HashMap<>();
            newVals.put("col2", 2);
            serverManager.update(testParse, mObj.getObjectId(), newVals);

            Assert.assertEquals(
                            "one".compareTo(serverManager.getValue(testParse, mObj.getObjectId()).getString("col1")),
                            0);
            Assert.assertEquals(2, serverManager.getValue(testParse, mObj.getObjectId()).getInt("col2"));

            serverManager.deleteById(testParse, mObj.getObjectId());

        } catch (ParseException e) {
            e.printStackTrace();
            assert false;
        }

    }

    @AfterClass
    public static void cleanup() {
        serverManager.deleteById(testParse, temp.getObjectId());
        serverManager.deleteById(testParse, temp1.getObjectId());
        serverManager.deleteById(testParse, mObj.getObjectId());

    }

}
