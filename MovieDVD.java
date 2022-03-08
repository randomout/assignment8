import java.io.IOException;
import java.io.Writer;
import java.text.DecimalFormat;

/**
 * Media type defining a DVD Movie
 * 
 * @author Brian Cunningham
 * Date: 3/8/2022
 */
public class MovieDVD extends Media {
  // size of the movie in MB
  private double size;

  /**
   * Creats a new MovieDVD instance with the given attributes.
   * 
   * @param id The ID of the MovieDVD
   * @param title The Title
   * @param yearPublished 4-digit year of publication
   * @param rented whether the media has been rented
   * @param size size in MB of the MovieDVD
   */
  public MovieDVD(int id, String title, int yearPublished, boolean rented, double size) {
    super(id, title, yearPublished, rented);
    this.size = size;
  }

  /**
   * Creates a new MovieDVD object from the given comma-delimited string.  
   * The string must be in the form: "id,title,yearPublished,rented,size"
   * 
   * @param data A comma-delimited string containing the fields for the MovieDVD instance.
   * @throws MediaCreationException if the MovieDVD object could to be properly created from the data given
   */
  public MovieDVD(String data) throws MediaCreationException {
    // call superclass and let it init id,title,yearPublished,rented
    super(data);
    
    // split string and get size data
    String[] segements = data.split(",");

    if(segements.length < 5) {
      throw new MediaCreationException("Incorrect MovieDVD format");
    }

    try {
      // set size from the parsed string
      this.size = Double.parseDouble(segements[4]);
    } catch( NumberFormatException e ) {
      throw new MediaCreationException("Unable to read media: " + e.getMessage());
    }
    
  }

  /**
   * Sets the size of the MovieDVD instance
   * @param size The size (in MB) of the MovieDVD
   */
  public void setSize(double size) {
    this.size = size;
  }

  /**
   * Gets the size of the MovieDVD instance
   * @return The size (in MB) of the MoveDVD
   */
  public double getSize() {
    return this.size;
  }

  /**
   * Gets a String containing additional info for the MovieDVD.  
   * @return A String description for size in MB of the MovieDVD
   */

  public String getAdditionalInfo() {
    DecimalFormat format = new DecimalFormat("#0.00");
    return "size: " + format.format(this.size) + "MB";
  }

  /**
   * Saves the MovieDVD instance to the given writer.
   * @throws IOException if the MovieDVD instance could not be written
   */

  public void save(Writer writer) throws IOException {
    super.save(writer);
    writer.write("," + this.size);
  }

}
