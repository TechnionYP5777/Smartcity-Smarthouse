package il.ac.technion.cs.smarthouse.mvp;

import java.net.URL;
import java.util.ResourceBundle;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import il.ac.technion.cs.smarthouse.system.SystemCore;
import il.ac.technion.cs.smarthouse.utils.BoolLatch;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;

public abstract class SystemPresenter implements Initializable {
    private static Logger log = LoggerFactory.getLogger(SystemPresenter.class);
    
    public static String FXML_BASE_PATH = "/fxmls/system/";
    
    private SystemPresenter parent;
    private SystemCore systemCore;
    
    private BoolLatch startedLatch = new BoolLatch();
    private boolean securityBoolean;
    
    public final SystemCore getModel() {
        securityCheck();
        
        if (systemCore != null)
            return systemCore;
        
        systemCore = new SystemCore();
        systemCore.initializeSystemComponents(); // TODO: this should look better...
        
        return systemCore;
    }
    
    public final void cleanModel() {
        systemCore.shutdown();
        systemCore = null;
    }

    @Override public final void initialize(URL location, ResourceBundle b) {
        securityBoolean = true;
        init(getModel(), location, b);
        notifyOnLoaded();
    }
    
    public abstract void init(final SystemCore model, final URL location, final ResourceBundle b);
    
    public void waitUntilLoaded() {
        startedLatch.blockUntilTrue();
    }
    
    private void notifyOnLoaded() {
        log.info("Loaded Presenter: " + getClass().getName());
        startedLatch.setTrueAndRelease();
    }
    
    public static class PresenterInfo {
        private Node rootViewNode;
        private SystemPresenter presenter;
        
        PresenterInfo(Node rootViewNode, SystemPresenter presenter) {
            this.rootViewNode = rootViewNode;
            this.presenter = presenter;
        }

        public Node getRootViewNode() {
            return rootViewNode;
        }
        
        @SuppressWarnings("unchecked")
        public <T extends SystemPresenter> T getPresenter() {
            return (T) presenter;
        }
    }
    
    private final PresenterInfo createChildPresenter(URL fxmlLocation) throws Exception {
        FXMLLoader loader = new FXMLLoader(fxmlLocation);
        loader.setControllerFactory(param-> {
                try {
                    SystemPresenter p = (SystemPresenter) param.newInstance();
                    p.systemCore = getModel();
                    p.parent = this;
                    return p;
                } catch (Exception e) {
                    log.error("Couldn't start child controller", e);
                }
                return null;
            });
        
        return loadPresenter(loader);
    }
    
    protected final PresenterInfo createChildPresenter(String fxmlFileName) throws Exception {
        return createChildPresenter(SystemPresenter.class.getResource(FXML_BASE_PATH + fxmlFileName));
    }

    @SuppressWarnings("unchecked")
    public <T extends SystemPresenter> T getParentPresenter() {
        securityCheck();
        return (T) parent;
    }
    
    private void securityCheck() {
        if (securityBoolean)
            return;
        
        RuntimeException r = new RuntimeException("Can't call " + new Throwable().getStackTrace()[1].getMethodName() + " before init");
        log.error(r.getMessage());
        throw r;
    }
    
    private static PresenterInfo createRootPresenter(URL fxmlLocation) throws Exception {
        return loadPresenter((new FXMLLoader(fxmlLocation)));
    }
    
    public static PresenterInfo createRootPresenter(String fxmlFileName) throws Exception {
        return createRootPresenter(SystemPresenter.class.getResource(FXML_BASE_PATH + fxmlFileName));
    }
    
    private static PresenterInfo loadPresenter(FXMLLoader l) throws Exception {
        Node n = l.load();
        Object child = l.getController();
        
        if (!SystemPresenter.class.isAssignableFrom(child.getClass()))
            throw new Exception("Child (" + child.getClass() + ") must extend " + SystemPresenter.class);
        
        return new PresenterInfo(n, (SystemPresenter) child);
    }
}
