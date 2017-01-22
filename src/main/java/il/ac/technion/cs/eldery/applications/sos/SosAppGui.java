package il.ac.technion.cs.eldery.applications.sos;

import java.io.IOException;
import java.util.List;

import com.sun.glass.events.MouseEvent;

import il.ac.technion.cs.eldery.system.EmergencyLevel;
import il.ac.technion.cs.eldery.system.applications.api.SensorData;
import il.ac.technion.cs.eldery.system.applications.api.SmartHouseApplication;
import il.ac.technion.cs.eldery.system.applications.api.exceptions.OnLoadException;
import il.ac.technion.cs.eldery.system.applications.api.exceptions.OnLoadException.ErrorCode;
import il.ac.technion.cs.eldery.system.exceptions.SensorNotFoundException;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.event.EventType;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Button;

public class SosAppGui extends SmartHouseApplication {
    
    SosController sosController;
    private Button killerButon;
    public boolean shouldAlert=true;
    
    @Override public void onLoad() throws OnLoadException {
        final List<String> ids = super.inquireAbout("iSOS");
        if (ids.isEmpty())
            throw new OnLoadException(ErrorCode.SENSOR_COM_NAME_NOT_FOUND);

        final String sensorId = ids.get(0);
        System.out.println("msg from app: onLoad");
        try {
            subscribeToSensor(sensorId, SosSensor.class, sosSensor -> {
                final String t = "SOS " + (sosSensor.isPressed() ? "" : "Not ") + "Pressed";
                if (sosController != null && shouldAlert){
                    sosController.sosBtnPressed();
                    sendAlert("ATTENTION SOS: Client is requesting help.", EmergencyLevel.EMAIL_EMERGENCY_CONTACT);
                    shouldAlert=false;
                }
                System.out.println("msg from app: " + t);
            });
        } catch (final SensorNotFoundException ¢) {
            throw new OnLoadException(ErrorCode.SENSOR_ID_NOT_FOUND, ¢.getMessage());
        }
    }

    @Override public String getApplicationName() {
        return "SOS Application";
    }

    @Override public Node getRootNode() {
        try {
            final FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("sos_app_ui.fxml"));
            final Parent $ = fxmlLoader.load();
            sosController = fxmlLoader.getController();
            killerButon = sosController.getBtn();
            killerButon.setOnAction(new EventHandler<ActionEvent>() {
                
                @Override public void handle(ActionEvent __) {
                    if(SosAppGui.this.shouldAlert)
                        sosController.sosBtnPressed();
                    else {
                        sosController.sosBtnUnpress();
                        SosAppGui.this.shouldAlert = !SosAppGui.this.shouldAlert;
                    }
                }
            });
            return $;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
    
    
}

class SosSensor extends SensorData {
    private boolean pressed;

    boolean isPressed() {
        return pressed;
    }
}
