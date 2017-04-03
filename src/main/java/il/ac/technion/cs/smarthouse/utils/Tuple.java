package il.ac.technion.cs.smarthouse.utils;

/** @author Inbal Zukerman
 * @since 8.12.2016 */
public class Tuple<L, R> {
    public final L left;
    public final R right;

    public Tuple(final L left, final R right) {
        this.left = left;
        this.right = right;
    }

    @Override public int hashCode() {
        return 31 * ((left == null ? 0 : left.hashCode()) + 31) + (right == null ? 0 : right.hashCode());
    }

    @Override public boolean equals(final Object ¢) {
        return ¢ == this || ¢ != null && getClass() == ¢.getClass() && equals((Tuple<?, ?>) ¢);
    }

    public boolean equals(final Tuple<?, ?> ¢) {
        if (left == null) {
            if (¢.left != null)
                return false;
        } else if (!left.equals(¢.left))
            return false;
        if (right == null) {
            if (¢.right != null)
                return false;
        } else if (!right.equals(¢.right))
            return false;
        return true;
    }

}
