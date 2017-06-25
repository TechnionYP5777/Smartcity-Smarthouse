package il.ac.technion.cs.smarthouse.developers_api.application_builder.implementations;

import java.net.URL;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import il.ac.technion.cs.smarthouse.developers_api.application_builder.CustomRegionBuilder;
import il.ac.technion.cs.smarthouse.developers_api.application_builder.GuiBinderObject;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;

/**
 * Implementation of {@link CustomRegionBuilder}
 * 
 * @author RON
 * @since 10-06-2017
 */
public class CustomRegionBuilderImpl extends AbstractRegionBuilder implements CustomRegionBuilder {
    private static Logger log = LoggerFactory.getLogger(CustomRegionBuilderImpl.class);
    
    private final ClassLoader applicationsClassLoader;
    
    public CustomRegionBuilderImpl(final ClassLoader applicationsClassLoader) {
        super.setTitle("Custom Region");
        this.applicationsClassLoader = applicationsClassLoader;
    }
    
    @Override
    public CustomRegionBuilderImpl setTitle(String title) {
        super.setTitle(title);
        return this;
    }
    
    @Override
    public CustomRegionBuilderImpl add(final Node n) {
        if (n != null)
            addAppBuilderItem(new AppBuilderItem(null, n));
        return this;
    }
    
    @Override
    public CustomRegionBuilderImpl add(final String fxmlFileName, final GuiBinderObject<? extends Initializable> outController) {
        Node rootNode;
        try {
            final FXMLLoader fxmlLoader = createFXMLLoader(fxmlFileName);
            rootNode = fxmlLoader.load();
            if (outController != null)
                outController.setData(fxmlLoader.getController());
        } catch (final Exception e) {
            log.error("\n\tCouldn't load the fxml: " + fxmlFileName, e);
            throw new RuntimeException("Couldn't load the fxml: " + fxmlFileName + "\n" + e.getClass().getSimpleName());
        }
        
        return add(rootNode);
    }
    
    private FXMLLoader createFXMLLoader(final String fxmlFileName) {
        final URL url = getResource(fxmlFileName);
        final FXMLLoader fxmlLoader = new FXMLLoader(url);
        fxmlLoader.setClassLoader(applicationsClassLoader);
        return fxmlLoader;
    }
    
    private URL getResource(final String resourcePath) {
        return Optional.ofNullable(applicationsClassLoader.getResource(resourcePath))
                        .orElse(getClass().getResource(resourcePath));
    }
}
