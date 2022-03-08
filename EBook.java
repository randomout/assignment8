import java.io.IOException;
import java.io.Writer;
import java.util.Calendar;

/**
 * A Media type defining an EBook
 * 
 * @author Brian Cunningham
 * Date: 3/8/2022
 */
public class EBook extends Media {
  // rate used for rental calcuation
  private static final double EBOOK_RATE = 0.10;
  
  // chapters in ebook
  private int chapters;
  
  /**
   * Creates a new EBook with the given attributes
   * 
   * @param id The ID of the EBook
   * @param title The Title
   * @param yearPublished 4-digit year of publication
   * @param rented whether the media has been rented
   * @param chapters Number of chapters in the EBook
   */
  public EBook(int id, String title, int yearPublished, boolean rented, int chapters) {
    super(id, title, yearPublished, rented);
    this.chapters = chapters;
  }

  /**
   * Creates a new EBook object from the given comma-delimited string.  
   * The string must be in the form: "id,title,yearPublished,rented,chapters"
   * 
   * @param data A comma-delimited string containing the fields for the EBook instance.
   * @throws MediaCreationException if the EBook object could to be properly created from the data given
   */
  public EBook(String data) throws MediaCreationException {
    // call superclass and let it init id,title,yearPublished,rented
    super(data);

    // Split string and get chapter data
    String[] segements = data.split(",");
    
    if(segements.length < 5) {
      throw new MediaCreationException("Incorrect Ebook data format");
    }

    try {
      // set the chapter data from the parsed string
      this.chapters = Integer.parseInt(segements[4]);
    } catch( NumberFormatException e ) {
      throw new MediaCreationException("Unable to read media: " + e.getMessage());
    }
    
  }

  /**
   * Sets the number of chaptes for the EBook instance
   * @param chapters The number of chapters
   */
  public void setChapters(int chapters) {
    this.chapters = chapters;
  }

  /**
   * Gets the number of chapters of the EBook instanct
   * @return The number of chapters
   */
  public int getChapters() {
    return this.chapters;
  }

  /**
   * Gets a String containing additional info for the Ebook.  
   * @return A String description for number of chapters in the EBook
   */
  public String getAdditionalInfo() {
    return "chapters: " + this.chapters;
  }

  /**
   * Calculates the rental fee for the EBook instance, and returns it
   * @return The rental fee for the EBook instance
   */
  public double caculateRentalFee() {
    int currentYear = Calendar.getInstance().get(Calendar.YEAR);

    double fee = this.getChapters() * EBOOK_RATE;

    if(currentYear == this.getYearPublished()) {
      fee += 1.00;
    }

    return fee;
  }

  /**
   * Saves the EBook instance to the given writer.
   * @throws IOException if the EBook instance could not be written
   */
  public void save(Writer writer) throws IOException {
    super.save(writer);
    writer.write("," + this.chapters);
  }
  
}
