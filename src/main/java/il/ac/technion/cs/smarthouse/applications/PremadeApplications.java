package il.ac.technion.cs.smarthouse.applications;

import il.ac.technion.cs.smarthouse.applications.sos.SosAppGui;
import il.ac.technion.cs.smarthouse.applications.stove.StoveModuleGui;
import il.ac.technion.cs.smarthouse.applications.vitals.VitalsApp;
import il.ac.technion.cs.smarthouse.system.applications.api.SmartHouseApplication;

public enum PremadeApplications {
    SOS_APPLICATION(SosAppGui.class, "SOS Application"),
    STOVE_APPLICATION(StoveModuleGui.class, "Stove Application"),
    VITALS_APPLICATION(VitalsApp.class, "Vitals Application");

    private final Class<? extends SmartHouseApplication> appClass;
    private final String appName;

    PremadeApplications(final Class<? extends SmartHouseApplication> appClass, final String appName) {
        this.appClass = appClass;
        this.appName = appName;
    }

    public Class<? extends SmartHouseApplication> getAppClass() {
        return appClass;
    }

    public String getAppName() {
        return appName;
    }

    public static PremadeApplications getByName(final String name) {
        for (final PremadeApplications $ : PremadeApplications.values())
            if ($.getAppName().equals(name))
                return $;
        return null;
    }
}
