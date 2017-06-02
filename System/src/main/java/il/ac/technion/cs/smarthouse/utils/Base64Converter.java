package il.ac.technion.cs.smarthouse.utils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.Base64;

public enum Base64Converter {
    ;
    /** Read the object from Base64 string. */
    public static Object fromString(final String s) throws IOException, ClassNotFoundException {
        final byte[] data = Base64.getDecoder().decode(s);
        try (ObjectInputStream ois = new ObjectInputStream(new ByteArrayInputStream(data))) {
            return ois.readObject();
        }
    }

    /** Write the object to a Base64 string. */
    public static String toString(final Serializable o) throws IOException {
        final ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try (ObjectOutputStream oos = new ObjectOutputStream(baos)) {
            oos.writeObject(o);
            return Base64.getEncoder().encodeToString(baos.toByteArray());
        }
    }
}
