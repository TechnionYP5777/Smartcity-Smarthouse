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

    /**
     * Initializes a new Table without capacity limit
     */
    public Table () {
        limitedSize = false;
    }
    
    /**
     * @param maxCapacity - the capacity limit required
    * Initializes a new Table without capacity limit
    */
    public Table (final int maxCapacity) {
        limitedSize = true;
        this.maxCapacity = maxCapacity <= 0 ? 1 : maxCapacity;
    }

    /**
     * 
     * @param info - the new entry to add
     * Adds an entry to the table
     */
    public void addEntry(final Map<Object, Object> info) {
        if (limitedSize && data.size() == maxCapacity)
            data.remove(0);
        data.add(info);
    }

    /**
     * @return the max capacity limit if exists, -1 otherwise
     */
    public int getMaxCapacity() {
        return !limitedSize ? -1 : maxCapacity;
    }

    /**
     * @return the current capacity of the table (it's size)
     */
    public int getCurrentCapacity() {
        return data.size();
    }

    /**
     * 
     * @param newCapacity - the new capacity limit to enforce. 
     */
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
   
   /**
    * Disable the capacity limitation
    */
   public void disableCapacityLimit(){
       limitedSize = false;
   }

   /**
    * 
    * @param numOfEntries- how many entries (from the newest to oldest) to return
    * @return A new table with the required entries
    */
    public Table recievceKLastEntries(final int numOfEntries) {
        if (numOfEntries <= 0)
            return null;
        final Table $ = new Table(numOfEntries);
        final int position = numOfEntries > data.size() ? 0 : data.size() - numOfEntries;
        for (int ¢ = position; ¢ < data.size(); ++¢)
            $.addEntry(data.get(¢));
        return $;
    }

    /**
     * 
     * @return the last entry added to the table
     */
    public Table getLastEntry() {
        return recievceKLastEntries(1);
    }

}
