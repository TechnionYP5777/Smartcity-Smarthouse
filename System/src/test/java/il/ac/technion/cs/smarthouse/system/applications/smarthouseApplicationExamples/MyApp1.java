package il.ac.technion.cs.smarthouse.system.applications.smarthouseApplicationExamples;

import il.ac.technion.cs.smarthouse.system.applications.api.SmartHouseApplication;

public class MyApp1 extends SmartHouseApplication {

    @Override
    public void onLoad() throws Exception {
        // TODO Auto-generated method stub
        
    }

    @Override
    public String getApplicationName() {
        return getClass().getName();
    }

}
