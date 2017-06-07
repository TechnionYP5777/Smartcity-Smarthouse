package il.ac.technion.cs.smarthouse.mvp;

import java.net.URL;
import java.util.ResourceBundle;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import il.ac.technion.cs.smarthouse.utils.BoolLatch;
import il.ac.technion.cs.smarthouse.utils.TimeCounter;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;

/**
 * An abstruct controller class for JavaFx's controllers.
 * <p>
 * This controller class support a specific model that is used by all
 * sub-controllers that are created by the root controller
 * 
 * @author RON
 * @since 07-06-2017
 */
public abstract class GuiController<M> implements Initializable {
    private static final Logger log = LoggerFactory.getLogger(GuiController.class);

    private GuiController<M> parent;
    private M model;
    private Parent rootViewNode;
    private final BoolLatch startedLatch = new BoolLatch();

    protected final M getModel() {
        return model;
    }

    @Override
    public final void initialize(final URL location, final ResourceBundle b) {
        init(getModel(), location, b);
        notifyOnLoaded();
    }

    protected abstract void init(M model1, URL location, ResourceBundle b);

    public final void waitUntilInitFinishes() {
        final TimeCounter t = new TimeCounter().start();
        startedLatch.blockUntilTrue();
        log.info("GuiController: Waited " + t.getTimePassedMillis() + " [ms] for controller to load");
    }

    private final void notifyOnLoaded() {
        log.trace("Loaded GuiController: " + getClass().getName());
        startedLatch.setTrueAndRelease();
    }

    public final Parent getRootViewNode() {
        return rootViewNode;
    }

    @SuppressWarnings("unchecked")
    protected <T extends GuiController<M>> T getParentController() {
        return (T) parent;
    }

    @SuppressWarnings("unchecked")
    private static <ModelType, T extends GuiController<ModelType>> T loadPresenter(final FXMLLoader l,
                    final ModelType model1, final GuiController<ModelType> parent) {
        assert l != null;
        assert model1 != null;

        try {
            l.setControllerFactory(controllerClass -> {
                try {
                    final GuiController<ModelType> c = (GuiController<ModelType>) controllerClass.newInstance();
                    c.model = model1;
                    c.parent = parent;
                    return c;
                } catch (final Exception e) {
                    log.error("Couldn't start controller", e);
                }
                return null;
            });

            final Parent p = l.load();
            final Object child = l.getController();

            if (!GuiController.class.isAssignableFrom(child.getClass()))
                throw new Exception("Child (" + child.getClass() + ") must extend " + GuiController.class);

            GuiController<ModelType> c = (GuiController<ModelType>) child;
            c.rootViewNode = p;

            return (T) c;
        } catch (Exception e) {
            String r = GuiController.class.getName() + " can't load the FXML: " + l.getLocation();
            log.error(r, e);
            throw new RuntimeException(r);
        }
    }

    public static <ModelType, T extends GuiController<ModelType>> T createRootController(final URL fxmlLocation,
                    final ModelType c) {
        return loadPresenter(new FXMLLoader(fxmlLocation), c, null);
    }

    protected final <T extends GuiController<M>> T createChildController(final URL fxmlLocation) {
        return loadPresenter(new FXMLLoader(fxmlLocation), getModel(), this);
    }
}
