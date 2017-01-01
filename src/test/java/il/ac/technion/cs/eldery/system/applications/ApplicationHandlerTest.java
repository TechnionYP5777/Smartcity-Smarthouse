/**
 * 
 */
package il.ac.technion.cs.eldery.system.applications;


import static org.junit.Assert.*;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;


import org.hamcrest.core.IsInstanceOf;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import il.ac.technion.cs.eldery.networking.messages.UpdateMessage;
import il.ac.technion.cs.eldery.system.DatabaseHandler;
import il.ac.technion.cs.eldery.system.applications.api.SensorData;
import il.ac.technion.cs.eldery.system.exceptions.SensorNotFoundException;

/**
 * @author Elia Traore
 * @since Dec 28, 2016
 */
public class ApplicationHandlerTest {
    private DatabaseHandler databaseHandler;
    private ApplicationsHandler appsHandler;
        
    class AppDev_SensorRep extends SensorData {
                
                public Boolean state;
                public Integer temp;
                
                public AppDev_SensorRep(){
                    state = null;
                    temp = null;
                }
                
                public AppDev_SensorRep(Boolean state, Integer temp) {
                    super();
                    this.state = state;
                    this.temp = temp;
                }
                
    
                
                public void updateWithOther(AppDev_SensorRep other){
                    this.state = other.state;
                    this.temp = other.temp;
                }

                /* (non-Javadoc)
                 * @see java.lang.Object#hashCode()
                 */
                @Override public int hashCode() {
                    final int prime = 31;
                    int result = 1;
                    result = prime * result + getOuterType().hashCode();
                    result = prime * result + ((state == null) ? 0 : state.hashCode());
                    result = prime * result + ((temp == null) ? 0 : temp.hashCode());
                    return result;
                }

                /* (non-Javadoc)
                 * @see java.lang.Object#equals(java.lang.Object)
                 */
                @Override public boolean equals(Object obj) {
                    if (this == obj)
                        return true;
                    if (obj == null)
                        return false;
                    if (getClass() != obj.getClass())
                        return false;
                    AppDev_SensorRep other = (AppDev_SensorRep) obj;
                    if (!getOuterType().equals(other.getOuterType()))
                        return false;
                    if (state == null) {
                        if (other.state != null)
                            return false;
                    } else if (!state.equals(other.state))
                        return false;
                    if (temp == null) {
//                        if (other.temp != null)
//                            return false;
                    } else if (!temp.equals(other.temp))
                        return false;
                    return true;
                }

                private ApplicationHandlerTest getOuterType() {
                    return ApplicationHandlerTest.this;
                }


              
    
            }
        
    class SensorDev_TestSensor {
          String id, commName;
          DatabaseHandler dbh;
      
          public SensorDev_TestSensor(String id, String comm, DatabaseHandler dbh) {
              this.id = id;
              this.commName = comm;
              this.dbh = dbh;
              this.dbh.addSensor(id, commName, 10);
          }
          
          public String createUpdateMsg(Boolean state, Integer temp){
            final Map<String, String> data = new HashMap<>();
            data.put("state", state + "");
            data.put("temp", temp +"");
            final UpdateMessage message = new UpdateMessage(id, data);
//            return message.toJson();
            JsonObject json = new JsonObject();
            message.getData().entrySet().forEach(entry -> json.addProperty(entry.getKey(), entry.getValue()));
            return json +"";
          }
          
          public void update(Boolean state, Integer temp) {
              final String message = createUpdateMsg(state, temp);
              try {
                  dbh.getList(id).add(message);
              } catch (SensorNotFoundException e) {
                  // TODO: Auto-generated catch block
                    e.printStackTrace();
              }
          }
      }
    
    
    @Before public void prepTest(){
        databaseHandler = new DatabaseHandler();
        appsHandler = new ApplicationsHandler(databaseHandler);
    }
    
    @Test public void testSubscribeOnUpdate(){
        String sensorId = "yo";
        Boolean state = Boolean.TRUE;
        Integer temp = Integer.valueOf(102);
        SensorDev_TestSensor sensor = new SensorDev_TestSensor(sensorId, "iYo", databaseHandler);
        AppDev_SensorRep expected = new AppDev_SensorRep(state,temp);
        AppDev_SensorRep actual = new AppDev_SensorRep();
        try {
            appsHandler.subscribeToSensor(sensorId, AppDev_SensorRep.class, x -> {
                                                                              actual.updateWithOther(x);   
                                                                          });
        } catch (SensorNotFoundException e) {
            assertTrue("can't find the sensorId in the dbh. weird", false);
        }
        sensor.update(state,temp);
        assertEquals(expected, actual);
    }
    
}
