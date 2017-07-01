package il.ac.technion.cs.smarthouse.system.file_system;

import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import il.ac.technion.cs.smarthouse.system.SystemCore;
import il.ac.technion.cs.smarthouse.system.file_system.FileSystem.ReadOnlyFileNode;
import il.ac.technion.cs.smarthouse.utils.BoolLatch;

/**
 * @author RON
 * @since 28-05-2017
 */
public class FileSystemImplTest {
    FileSystemImpl fs;
    int resultNum;
    String resultString;

    @Before
    public void init() {
        fs = new FileSystemImpl();
    }

    @Test(timeout = 1000)
    public void subscribeAndSendTest() {
        final int NEW_VAL = 5;
        final BoolLatch wasCalled = new BoolLatch();
        resultNum = 0;

        fs.subscribe((path, data) -> {
            wasCalled.setTrueAndRelease();
            resultNum = (int) data;
        }, "");
        fs.sendMessage(NEW_VAL, "");

        wasCalled.blockUntilTrue();

        assert resultNum == NEW_VAL;
    }

    @Test(timeout = 1000)
    public void subscribeAndSendTest2() {
        final int NEW_VAL = 5;
        final BoolLatch wasCalled = new BoolLatch();
        resultNum = 0;
        resultString = "";

        fs.subscribe((path, data) -> {
            wasCalled.setTrueAndRelease();
            resultNum = (int) data;
            resultString = path;
        }, "a.b");
        
        fs.subscribe((path, data) -> {
            wasCalled.setTrueAndRelease();
            resultNum = data;
            resultString = path;
        }, Integer.class, "a.b");
        
        fs.sendMessage(NEW_VAL, "a.b.c.d");

        wasCalled.blockUntilTrue();

        assert resultNum == NEW_VAL;
        Assert.assertEquals(resultString, "a.b.c.d");

        assert fs.getData("a") == null;
        assert fs.getData("a.b") == null;
        assert fs.getData("a.b.d") == null;
        assert fs.<Integer>getData("a.b.c.d") == NEW_VAL;

        assert fs.getChildren("a").contains("b");
        assert !fs.getChildren("a").contains("c");
        assert fs.getChildren("a.b").contains("c");
        assert fs.getChildren("a.b.c").contains("d");
        assert !fs.getChildren("a.b.c").contains("e");
        assert fs.getChildren("a.b.c.d").isEmpty();
        assert fs.getChildren("s").isEmpty();
        assert !fs.wasPathInitiated("s");
        assert fs.wasPathInitiated("a");

        assert fs.getReadOnlyFileSystem("a") != null;

        for (String p : allPathsWithOutLeaves(fs.getReadOnlyFileSystem(), new ArrayList<>()))
            assert p != null;

        assert fs.<Integer>getMostRecentDataOnBranch("a") == NEW_VAL;
    }

    @Test
    public void testOnCoreFs() {
        for (String p : allPathsWithOutLeaves(new SystemCore().getFileSystem()
                        .getReadOnlyFileSystem(FileSystemEntries.SENSORS_DATA.buildPath()), new ArrayList<>()))
            assert p.startsWith("a");
    }
    
    @Test
    public void testToString() {
        assert fs.toString().contains(fs.toString(""));
    }
    
    @Test
    public void testRemove() {
        fs.sendMessage(11, "a.b.c");
        fs.sendMessage(12, "a.b.d");
        Assert.assertEquals(2, fs.getChildren("a.b").size());
        fs.deleteFromPath("a.b");
        Assert.assertEquals(0, fs.getChildren("a.b").size());
    }

    private List<String> allPathsWithOutLeaves(ReadOnlyFileNode n, List<String> ss) {
        if (n.isLeaf())
            return ss;

        List<String> l = PathBuilder.decomposePath(n.getFullPath());
        ss.add(PathBuilder.buildPath(l.subList(1, l.size())));

        n.getChildren().forEach(c -> ss.addAll(allPathsWithOutLeaves(c, new ArrayList<>())));
        return ss;
    }

}
