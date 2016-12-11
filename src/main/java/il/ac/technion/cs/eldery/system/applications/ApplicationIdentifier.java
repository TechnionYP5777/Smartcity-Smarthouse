package il.ac.technion.cs.eldery.system.applications;

/** A class that stores information about the app
 * @author RON
 * @since 09-12-16 */
  // TODO: RON and ROY - implement this class
public class ApplicationIdentifier {
  String id;
  String path;
  public String getId() {
    return id;
  }
  public void setId(String id) {
    this.id = id;
  }
  public String getPath() {
    return path;
  }
  public void setPath(String path) {
    this.path = path;
  }
  @Override public int hashCode() {
    return 31 * (((id == null) ? 0 : id.hashCode()) + 31) + ((path == null) ? 0 : path.hashCode());
  }
  @Override public boolean equals(Object o) {
    if (o == this)
      return true;
    if (o == null || getClass() != o.getClass())
      return false;
    ApplicationIdentifier other = (ApplicationIdentifier) o;
    if (id == null) {
      if (other.id != null)
        return false;
    } else if (!id.equals(other.id))
      return false;
    if (path == null) {
      if (other.path != null)
        return false;
    } else if (!path.equals(other.path))
      return false;
    return true;
  }
  @Override public String toString() {
    return "ApplicationIdentifier [id=" + id + ", path=" + path + "]";
  }
}
