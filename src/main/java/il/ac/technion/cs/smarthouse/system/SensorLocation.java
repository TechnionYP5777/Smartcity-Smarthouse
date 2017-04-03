package il.ac.technion.cs.smarthouse.system;

/** @author Inbal Zukerman
 * @since Dec 23, 2016 The location of a sensor Includes the following options:
 *        {@link #UNDEFINED}, {@link #KITCHEN}, {@link #DINING_ROOM},
 *        {@link #PORCH}, {@link #TV_ROOM}, {@link #BEDROOM}, {@link #BATHROOM},
 *        {@link #STUDY}, {@link #YARD}, {@link #LIVING_ROOM} ,
 *        {@link #BASEMENT}, {@link #GARAGE} */

public enum SensorLocation {
    /** the location of the sensor was not updated yet */
    UNDEFINED,
    /** the sensor is located in the kitchen */
    KITCHEN,
    /** the sensor is located in the dining room */
    DINING_ROOM,
    /** the sensor is located in the bedroom */
    BEDROOM,
    /** the sensor is located in the bathroom */
    BATHROOM,
    /** the sensor is located in the living room */
    LIVING_ROOM,
    /** the sensor is located in the TV room */
    TV_ROOM,
    /** the sensor is located in the study */
    STUDY,
    /** the sensor is located in the yard */
    YARD,
    /** the sensor is located in the porch */
    PORCH,
    /** the sensor is located in the basement */
    BASEMENT,
    HALLWAY,
    /** the sensor is located in the garage */
    GARAGE;

    public static SensorLocation fromString(final String val) {
        for (final SensorLocation $ : SensorLocation.values())
            if (($ + "").toLowerCase().equals(val.toLowerCase()))
                return $;

        return null;
    }
}
