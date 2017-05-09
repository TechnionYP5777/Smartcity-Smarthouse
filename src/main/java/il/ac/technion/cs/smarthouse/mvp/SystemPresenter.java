package il.ac.technion.cs.smarthouse.mvp;

import java.net.URL;
import java.util.ResourceBundle;

import il.ac.technion.cs.smarthouse.system.SystemCore;
import il.ac.technion.cs.smarthouse.utils.BoolLatch;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;

public abstract class SystemPresenter implements Initializable {
    private SystemPresenter parent;
    
    private static SystemCore systemCore;
    private BoolLatch startedLatch = new BoolLatch();
    
    @SuppressWarnings("static-method")
    public final SystemCore getModel() {
        if (systemCore != null)
            return systemCore;
        
        systemCore = new SystemCore();
        systemCore.initializeSystemComponents(); // TODO: this should look better...
        
        return systemCore;
    }
    
    @SuppressWarnings("static-method")
    public final void cleanModel() {
        systemCore.shutdown();
        systemCore = null;
    }

    @Override public final void initialize(URL location, ResourceBundle b) {
        init(getModel(), location, b);
        notifyOnLoaded();
    }
    
    public abstract void init(SystemCore model, URL location, ResourceBundle b);
    
    public void waitUntilLoaded() {
        startedLatch.blockUntilTrue();
    }
    
    private void notifyOnLoaded() {
        System.out.println("Loaded " + getClass().getName());
        startedLatch.setTrueAndRelease();
    }
    
    public class ChildPresenterInfo {
        private Node rootViewNode;
        private SystemPresenter childPresenter;
        
        ChildPresenterInfo(Node rootViewNode, SystemPresenter childPresenter) {
            this.rootViewNode = rootViewNode;
            this.childPresenter = childPresenter;
        }

        public Node getRootViewNode() {
            return rootViewNode;
        }
        
        @SuppressWarnings("unchecked")
        public <T extends SystemPresenter> T getChildPresenter() {
            return (T) childPresenter;
        }
    }
    
    protected final ChildPresenterInfo createChildPresenter(URL fxmlLocation) throws Exception {
        FXMLLoader loader = new FXMLLoader(fxmlLocation);
        
        Node n = loader.load();
        Object child = loader.getController();
        
        if (!SystemPresenter.class.isAssignableFrom(child.getClass()))
            throw new Exception("Child (" + child.getClass() + ") must extend " + SystemPresenter.class);
        
        SystemPresenter p = (SystemPresenter) child;
        p.parent = this;
        return new ChildPresenterInfo(n, p);
    }

    @SuppressWarnings("unchecked")
    public <T extends SystemPresenter> T getParentPresenter() {
        return (T) parent;
    }
}
