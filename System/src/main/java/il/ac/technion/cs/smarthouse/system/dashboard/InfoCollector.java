package il.ac.technion.cs.smarthouse.system.dashboard;

import java.util.HashMap;
import java.util.Map;

import com.google.gson.annotations.Expose;

/**
 * @author Elia Traore
 * @since Jun 12, 2017
 */
public class InfoCollector {

    @Expose private String unit = "", title = "";
    @Expose private Map<String, String> entries = new HashMap<>();

    public InfoCollector addInfoEntry(final String path, final String name) {
        entries.put(path, name);
        return this;
    }

    public InfoCollector setUnit(final String u) {
        unit = u;
        return this;
    }

    public InfoCollector setTitle(final String title) {
        this.title = title;
        return this;
    }

    /**
     * @return a map where the key is a given path and the values are names
     */
    public Map<String, String> getInfoEntries() {
        return new HashMap<>(entries);
    }

    public String getUnit() {
        return unit;
    }

    public String getTitle() {
        return title;
    }
}
