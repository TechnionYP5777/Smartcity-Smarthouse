package il.ac.technion.cs.smarthouse.database;

/**
 * 
 * @author Inbal Zukerman
 * @date May 26, 2017
 */

public class DatabaseManagerTest {

    private final DatabaseManager dbManager = new DatabaseManager();
/*
    @Test
    public void additionAndDeletionTest() {
        final String[] path = new String[3];
        path[0] = InfoType.TEST.toString();
        path[1] = InfoType.SENSOR.toString();
        path[2] = "temp";

        try {
            dbManager.addInfo(DispatcherCore.getPathAsString(path), "32");
            Assert.assertNotEquals("", dbManager.getLastEntry(path));
            Assert.assertEquals("test.sensor.temp=32", dbManager.getLastEntry(path));

            dbManager.deleteInfo(InfoType.TEST);
            Assert.assertEquals("", dbManager.getLastEntry(path));

            dbManager.addInfo(DispatcherCore.getPathAsString(path), "55");
            Assert.assertEquals("test.sensor.temp=55", dbManager.getLastEntry(path));

            dbManager.deleteInfo(path);
            Assert.assertEquals("", dbManager.getLastEntry(path));

        } catch (final ParseException e) {
            assert null != null;
        }

    }

    @Test
    public void testLastEntry() {
        final String[] path = new String[4];
        path[0] = InfoType.TEST.toString();
        path[1] = InfoType.SENSOR.toString();
        path[2] = "temp";
        path[3] = "house";

        try {
            dbManager.addInfo(DispatcherCore.getPathAsString(path), "10");

            path[3] = "garden";
            dbManager.addInfo(DispatcherCore.getPathAsString(path), "23");

            String lastEntry = dbManager.getLastEntry(InfoType.TEST.toString() + Dispatcher.DELIMITER
                            + InfoType.SENSOR.toString() + Dispatcher.DELIMITER + "temp");

            Assert.assertEquals("test.sensor.temp.garden=23", lastEntry);

            path[3] = "house";
            lastEntry = dbManager.getLastEntry(path);
            Assert.assertEquals("test.sensor.temp.house=10", lastEntry);

            Collection<String> tmp = dbManager.getPathChildren(
                            InfoType.TEST.toString() + Dispatcher.DELIMITER + InfoType.SENSOR.toString());

            // TODO: inbal
            System.out.println("results found: " + tmp.size());

            for (String str : tmp)
                System.out.println(str + "\n");

            dbManager.deleteInfo(InfoType.TEST);
        } catch (ParseException e) {
            assert null != null;
        }
    }
*/
}
