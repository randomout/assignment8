/**
 * Exception representing a error searching for Media
 */
public class MediaNotFoundException extends Exception {
  /**
   * Creates a new MediaNotFoundException
   */
  public MediaNotFoundException() {
    super();
  }

  /**
   * Creates a new MediaNotFoundException with the given message
   * @param message The error message for the exception
   */
  public MediaNotFoundException(String message) {
    super(message);
  }
  
}
