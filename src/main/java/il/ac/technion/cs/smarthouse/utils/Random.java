package il.ac.technion.cs.smarthouse.utils;

public class Random {
    private static java.util.Random r = new java.util.Random();

    /** Generates a random id of the following form: XX:XX:XX:XX where X can be
     * any hex value.
     * @return randomly generated id */
    public static String sensorId() {
        final String $[] = new String[4];

        for (int i = 0; i < 4; ++i) {
            final String s = Integer.toHexString(r.nextInt(256));
            $[i] = s.length() == 2 ? s : "0" + s;
        }

        return String.join(":", $).toUpperCase();
    }
}
