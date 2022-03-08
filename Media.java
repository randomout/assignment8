import java.io.*;
import java.util.NoSuchElementException;
import java.util.Scanner;

/**
 * Class that represents media to be rented
 * 
 * @author Brian Cunningham
 * Date: 3/8/2022
 */
public abstract class Media {
  // default rental fee
  private static final double RENTAL_FEE = 3.50;

  // types of media - used for loading media from files
  private static final String TYPE_EBOOK = "EBook";
  private static final String TYPE_MOVIE = "MovieDVD";
  private static final String TYPE_MUSIC = "MusicCD";

  // attributes
  private int id;
  private String title;
  private int yearPublished;
  private boolean rented;

  /**
   * Creates a new Media object from the given comma-delimited string.  
   * The string must be in the form: "id,title,yearPublished,rented"
   * 
   * @param data A comma-delimited string containing the fields for the Media instance.
   * @throws MediaCreationException if the Media object could to be properly created from the data given
   */
  public Media(String data) throws MediaCreationException {
    String[] segements = data.split(",");

    // ensure we have all elements in the string first
    if(segements.length < 4) {
      throw new MediaCreationException("Incorrect media data format");
    }
    
    try {
      // create/assign values from string
      this.id = Integer.parseInt(segements[0]);
      this.title = segements[1];
      this.yearPublished = Integer.parseInt(segements[2]);
      this.rented = Boolean.parseBoolean(segements[3]);
    } catch( Exception e ) {
      throw new MediaCreationException("Unable to read media: " + e.getMessage());
    }
  }

  /**
   * Creates a new Media instance with the given attributes
   * 
   * @param id The ID of the Media
   * @param title The Title
   * @param yearPublished 4-digit year of publication
   * @param rented whether the media has been rented
   */
  public Media(int id, String title, int yearPublished, boolean rented) {
    this.id = id;
    this.title = title;
    this.yearPublished = yearPublished;
    this.rented = rented;
  }

  /**
   * Gets the id of the Media instance
   * 
   * @return The Media's ID
   */
  public int getId() {
    return this.id;
  }

  /**
   * Sets the title for the Media instance
   * @param title The title of the Media
   */
  public void setTitle(String title) {
    this.title = title;
  }

  /**
   * Gets the title of the Media
   * @return The current title
   */
  public String getTitle() {
    return this.title;
  }

  /**
   * Sets the year of publication for the Media instance
   * @param yearPublished The 4-digit year of publication
   */
  public void setYearPublished(int yearPublished) {
    this.yearPublished = yearPublished;
  }

  /**
   * Gets the year of publication for the Media instance
   * @return the 4-digit year
   */
  public int getYearPublished() {
    return this.yearPublished;
  }

  /**
   * Sets the rented indicator on the Media instance
   * @param rented true if rented, false if not
   */
  public void setRented(boolean rented) {
    this.rented = rented;
  }

  /**
   * Returns current rental status of the Media instance
   * @return true if rented, false if not
   */
  public boolean isRented() {
    return this.rented;
  }

  /**
   * Calculates the rental fee for the Media instance. 
   * @return The default rental fee
   */
  public double caculateRentalFee() {
    return RENTAL_FEE;
  }

  /**
   * Gets a string containing any addition info about the media
   * @return A string containing additional info, or empty string if none
   */
  public String getAdditionalInfo() {
    return "";
  }

  /**
   * Saves the current values of the Media instance to the given Writer
   * @param writer The writer to write data do
   * @throws IOException If there was a problem writing the data
   */
  public void save(Writer writer) throws IOException{
    writer.write(
      this.id + "," +
      this.title + "," +
      this.yearPublished + "," +
      this.rented
    );
  }

  /**
   * Static method that creates a specific Media instance from the given file.  This method uses
   * the file's name to determine which type of Media instance to create, then reads the data 
   * from the file, setting the attributes of the instance from the data.
   * 
   * @param file The file the create Media from
   * @return A Media-based instance containing data read from the file
   * @throws MediaCreationException If a Media instance could not be created
   */
  public static final Media createMedia(File file) throws MediaCreationException {
    // filenames are in "type-id.txt" form, so get the type name form the file name
    String name = file.getName();
    String[] segments = name.split("-");

    // verify that the name is formatted correctly
    if(segments == null || segments.length == 0) {
      throw new MediaCreationException("Incorrect name format for media file: " + file.getName());
    }

    // get the type
    String type = segments[0];
    Media media = null;

    try {
      // Read the data from the file
      Scanner scanner = new Scanner(file);
      String data = scanner.nextLine();
      scanner.close();

      // figure out which Media type to create from the name parsed above
      switch(type) {
        case TYPE_EBOOK:  
          media =  new EBook(data);
          break;
        case TYPE_MUSIC:
          media =  new MusicCD(data);
          break;
        case TYPE_MOVIE:
          media =  new MovieDVD(data);
          break;
        default:
          throw new MediaCreationException("Could not determine media type");
      }
    } catch(FileNotFoundException e) {
      throw new MediaCreationException("Media file was not found: " + file.getName()) ;
    } catch(NoSuchElementException e) {
      throw new MediaCreationException("Could not read data from file: " + file.getName());
    }
    
    return media;
  }
}