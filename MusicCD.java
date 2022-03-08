import java.io.IOException;
import java.io.Writer;
import java.util.Calendar;;

/**
 * A Media type defining a Music CD.
 * 
 * @author Brian Cunningham
 * Date: 3/8/2022
 */
public class MusicCD extends Media {
  // rate used for calculating rental fee
  private static final double MUSIC_CD_RATE = 0.02;

  // the length of the cd in minutes
  private int length;

  /**
   * Creates a new MusicCD instance with the given attributes
   * @param id The ID of the MusicCD
   * @param title The Title
   * @param yearPublished 4-digit year of publication
   * @param rented whether the media has been rented
   * @param length The length of the MusicCD in minutes
   */
  public MusicCD(int id, String title, int yearPublished, boolean rented, int length) {
    super(id, title, yearPublished, rented);
    this.length = length;
  }

  /**
   * Creates a new MusicCD object from the given comma-delimited string.  
   * The string must be in the form: "id,title,yearPublished,rented,minutes"
   * 
   * @param data A comma-delimited string containing the fields for the MusicCD instance.
   * @throws MediaCreationException if the MusicCD object could to be properly created from the data given
   */
  public MusicCD(String data) throws MediaCreationException{
    // call superclass and let it init id,title,yearPublished,rented
    super(data);

    // split string and get minutes data
    String[] segements = data.split(",");

    if(segements.length < 5) {
      throw new MediaCreationException("Incorrect MusicCD format");
    }

    try {
      // set the minutes data from the parsed string
      this.length = Integer.parseInt(segements[4]);
    } catch( NumberFormatException e ) {
      throw new MediaCreationException("Unable to read media: " + e.getMessage());
    }
  }

  /**
   * Sets the length of the MusicCD instance
   * @param length The length in minutes
   */
  public void setLength(int length) {
    this.length = length;
  }

  /**
   * Gets the length (in minutes) of the MusicCD instance
   * @return The lenght of the MusicCD
   */
  public int getLength() {
    return this.length;
  }

  /**
   * Gets a String containing additional info for the MusicCD.  
   * @return A String description for number of minutes in the MusicCD
   */
  public String getAdditionalInfo() {
    return "length: " + this.length + " minutes";
  }

  /**
   * Calculates the rental fee for the MusicCD instance, and returns it
   * @return The rental fee for the MusicCD instance
   */
  public double caculateRentalFee() {
    int currentYear = Calendar.getInstance().get(Calendar.YEAR);

    double fee = this.getLength() * MUSIC_CD_RATE;

    if(currentYear == this.getYearPublished()) {
      fee += 1.00;
    }

    return fee;
  }

  /**
   * Saves the MusicCD instance to the given writer.
   * @throws IOException if the MusicCD instance could not be written
   */
  public void save(Writer writer) throws IOException {
    super.save(writer);
    writer.write("," + this.length);
  }

  
}
