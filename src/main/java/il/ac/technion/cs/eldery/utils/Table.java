package il.ac.technion.cs.eldery.utils;

import java.util.ArrayList;
import java.util.Map;

/**
 * @author Inbal Zukerman
 * @since 15.12.2016
 */
public class Table {
    private final ArrayList<Map<Object, Object>> data = new ArrayList<>();
    boolean limitedSize;
    private int maxCapacity;

    public Table () {
        limitedSize = false;
    }
    
    public Table (final int maxCapacity) {
        limitedSize = true;
        this.maxCapacity = maxCapacity <= 0 ? 1 : maxCapacity;
    }

    public void addInfo(final Map<Object, Object> info) {
        if (limitedSize && data.size() == maxCapacity)
            data.remove(0);
        data.add(info);
    }

    public int getMaxCapacity() {
        return !limitedSize ? -1 : maxCapacity;
    }

    public int getCurrentCapacity() {
        return data.size();
    }

   public void changeMaxCapacity(final int newCapacity) {
       if(!limitedSize)
           limitedSize = true;
        if (data.size() >= newCapacity) {
            final int cutOff = data.size() - newCapacity;
            for (int ¢ = 0; ¢ < cutOff; ++¢)
                data.remove(0);
        }
        maxCapacity = newCapacity;
    }
   
   public void disableCapacityLimit(){
       limitedSize = false;
   }

    public Table recievceKLastEntries(final int numOfEntries) {
        if (numOfEntries <= 0)
            return null;
        final Table $ = new Table(numOfEntries);
        final int position = numOfEntries > data.size() ? 0 : data.size() - numOfEntries;
        for (int ¢ = position; ¢ < data.size(); ++¢)
            $.addInfo(data.get(¢));
        return $;
    }

    public Table getLastEntry() {
        return recievceKLastEntries(1);
    }

}
