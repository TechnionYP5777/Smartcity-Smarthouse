package Utils;

/** @author Inbal Zukerman
 * @since 8.12.2016 */
public class Tuple<L, R> {
  private L left;
  private R right;

  public Tuple(final L left, final R right) {
    this.left = left;
    this.right = right;
  }

  public void setLeft(final L newLeft) {
    this.left = newLeft;
  }

  public L getLeft() {
    return this.left;
  }

  public void setRight(final R newRight) {
    this.right = newRight;
  }

  public R getRight() {
    return this.right;
  }

  // TODO: unchecked warning @inbalzukerman
  @SuppressWarnings("unchecked") @Override public boolean equals(final Object ¢) {
    return ¢ instanceof Tuple && this.left.equals(((Tuple<L, R>) ¢).getLeft()) && this.right.equals(((Tuple<L, R>) ¢).getRight());
  }

  @Override public int hashCode() {
    // TODO Auto-generated method stub
    return super.hashCode();
  }
}
