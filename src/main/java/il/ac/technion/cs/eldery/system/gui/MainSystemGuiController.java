package il.ac.technion.cs.eldery.system.gui;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import il.ac.technion.cs.eldery.system.DatabaseHandler;
import il.ac.technion.cs.eldery.system.applications.ApplicationsHandler;
import il.ac.technion.cs.eldery.system.gui.applications.ApplicationViewController;
import il.ac.technion.cs.eldery.system.gui.mapping.MappingController;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Tab;

public class MainSystemGuiController implements Initializable {
    private DatabaseHandler dbHandler;
    private ApplicationsHandler appsHandler;
    
    private MappingController mappingController;
    private ApplicationViewController appsController;

    @FXML Tab homeTab;
    @FXML Tab userTab;
    @FXML Tab appsTab;
    @FXML Tab sensorsTab;

    @Override public void initialize(URL arg0, ResourceBundle arg1) {
        // TODO Auto-generated method stub
        try {
            FXMLLoader loader;
            
            // user tab: 
            userTab.setContent((Node) FXMLLoader.load(this.getClass().getResource("user information.fxml")));
            
            // sensors tab:
            loader = new FXMLLoader(this.getClass().getResource("/il/ac/technion/cs/eldery/system/gui/mapping/house_mapping.fxml"));
            sensorsTab.setContent(loader.load());
            mappingController = loader.getController();
            
            // applications tab:
            loader = new FXMLLoader(this.getClass().getResource("/il/ac/technion/cs/eldery/system/gui/applications/application_view.fxml"));
            appsTab.setContent(loader.load());
            appsController = loader.getController();
            
        } catch (IOException ¢) {
            ¢.printStackTrace();
        }
    }
    
    public MainSystemGuiController setDatabaseHandler(DatabaseHandler dbHandler) {
        this.dbHandler = dbHandler;
        mappingController.setDatabaseHandler(dbHandler);
        
        return this;
    }
    
    public MainSystemGuiController setApplicationsHandler(ApplicationsHandler appsHandler) {
        this.appsHandler = appsHandler;
        appsController.setAppsHandler(appsHandler);
        
        return this;
    }
}
