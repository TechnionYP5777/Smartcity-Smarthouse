package il.ac.technion.cs.smarthouse.developers_api;

import java.net.URL;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;

public class CustomRegionBuilder extends AbstractRegionBuilder {
    private static Logger log = LoggerFactory.getLogger(CustomRegionBuilder.class);
    
    public CustomRegionBuilder() {
        super.setTitle("Custom Region");
    }
    
    @Override
    public CustomRegionBuilder setTitle(String title) {
        super.setTitle(title);
        return this;
    }
    
    public CustomRegionBuilder add(final Node n) {
        if (n != null)
            addAppBuilderItem(new AppBuilderItem(null, n));
        return this;
    }
    
    public CustomRegionBuilder add(final String fxmlFileName, final DataObject<Initializable> outController) {
        Node rootNode;
        try {
            final FXMLLoader fxmlLoader = createFXMLLoader(fxmlFileName);
            fxmlLoader.setClassLoader(getClass().getClassLoader());
            rootNode = fxmlLoader.load();
            if (outController != null)
                outController.setData(fxmlLoader.getController());
        } catch (final Exception e) {
            rootNode = null;
            log.error("Couldn't load the fxml: " + fxmlFileName, e);
        }
        
        return add(rootNode);
    }
    
    private FXMLLoader createFXMLLoader(final String fxmlFileName) {
        final URL url = getResource(fxmlFileName);
        final FXMLLoader fxmlLoader = new FXMLLoader(url);
        fxmlLoader.setClassLoader(getClass().getClassLoader());
        return fxmlLoader;
    }
    
    private URL getResource(final String resourcePath) {
        return Optional.ofNullable(getClass().getClassLoader().getResource(resourcePath))
                        .orElse(getClass().getResource(resourcePath));
    }
}
