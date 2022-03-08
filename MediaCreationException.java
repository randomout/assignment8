/**
 * Exception representing an error in loading/creating a Media instance
 */
public class MediaCreationException extends Exception {
  /**
   * Creates new MediaCreationException
   */
  public MediaCreationException() {
    super();
  }

  /**
   * Creates new MediaCreationException with the givne message
   * @param message The error message for the exception
   */
  public MediaCreationException(String message) {
    super(message);
  }
  
}
