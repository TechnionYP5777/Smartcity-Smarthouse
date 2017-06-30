package il.ac.technion.cs.smarthouse;

import il.ac.technion.cs.smarthouse.system_presenter.SystemPresenter;
import il.ac.technion.cs.smarthouse.system_presenter.SystemPresenterFactory;

public class Entry {

    public static void main(final String[] args) {
        final SystemPresenter p =new SystemPresenterFactory()
                        .setUseCloudServer(false) // false for now, because there is only one instance of the cloud server
                        .enableLocalDatabase(true)
                        .build();
    }
    
    

}
