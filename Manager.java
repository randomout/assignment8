import java.util.ArrayList;
import java.util.Comparator;
import java.io.*;

/**
 * Manager handles the management of various Media objects, 
 * providing loading, saving, searching and renting functionality.
 * 
 * @author Brian Cunningham
 * Date: 3/8/2022
 */
public class Manager {
  // media to manage
  private ArrayList<Media> media;

  // directory that data was initially read from. Used when saving updates.
  private File directory = null;

  /**
   * Creates a new Manager instance
   */
  public Manager() {
    this.media = new ArrayList<Media>();
  }

  /**
   * Loads Media data from the given directory.  All media loaded is stored in the manager for used.
   * Directory provided is also used to save updates/changes to media that was loaded.
   * 
   * @param directory The File instance for the directory to load.
   * @throws FileNotFoundException If no files were found in the given directory.
   * @throws MediaCreationException If Media could not be created from the given directory.
   */
  public void load(File directory) throws FileNotFoundException, MediaCreationException {
    // store directory for saving updates
    this.directory = directory;

    // get the list of files from directory
    File files[] = this.directory.listFiles();

    // if no files throw exception
    if(files == null || files.length == 0) {
      this.directory = null;
      throw new FileNotFoundException("No media files found in directory: " + directory.getAbsolutePath());
    }

    try {
      this.media = new ArrayList<Media>();

      // read each file from directory and create new Media instances from them
      for(File file: files) {
        Media item = Media.createMedia(file);      
        this.media.add(item);
      }

      // sort everything by id
      this.media.sort(new Comparator<Media>() {
        public int compare(Media a, Media b) {
          return Integer.compare(a.getId(), b.getId());
        }
      });
    } catch(MediaCreationException e) {
      this.media = null;
      throw e;
    }

  }

  /**
   * Saves a given Media object.  This object will be saved in the previous directory that was used
   * for load()
   * 
   * @param media The Media to save
   * @throws FileNotFoundException If the directory has not be set yet
   * @throws IOException If there was an issue saving the Media to file
   */
  public void save(Media media) throws FileNotFoundException, IOException {
    if(this.directory == null) {
      throw new FileNotFoundException("No directory available to save");
    }

    File file = new File(this.directory, media.getClass().getSimpleName() + "-" + media.getId() + ".txt");
    PrintWriter out = new PrintWriter(file);

    media.save(out);

    out.close();
  }

  /**
   * Adds a new Media instance to the Manager.  The media will be automatically saved to the directory 
   * that was used for load()
   * @param media The Media object to save
   * @throws MediaUpdateException If the Media object could not be saved
   */
  public void add(Media media) throws MediaUpdateException {
    try {
      save(media);
      this.media.add(media);
    } catch( FileNotFoundException e ) {
      throw new MediaUpdateException("Unable to add media: " + e.getMessage());
    } catch( IOException e ) {
      throw new MediaUpdateException("Unable to add media: " + e.getMessage());
    }
  }
  
  /**
   * Searches the current collection of Media objects for the given title, returning 
   * any that match;
   * 
   * @param title The title to search for
   * @return An ArrayList containing all Media with titles that match the given String
   */
  public ArrayList<Media> find(String title) {
    ArrayList<Media> matches = new ArrayList<Media>();

    System.out.println("find: " + title.toLowerCase());
    

    for(Media item: this.media) {
      System.out.println("title: " + item.getTitle().toLowerCase());

      if(item.getTitle().toLowerCase().contains(title.toLowerCase())) {
        matches.add(item);
      }
    }

    return matches;
  }

  /**
   * Gets all the current media in the Manager instance
   * @return An ArrayList containing all the Media
   */
  public ArrayList<Media> getAllMedia() {
    return this.media;
  }

  /**
   * Gets the Media object at the given index.
   * 
   * @param index The index of the Media object to get
   * @return The Media object at the given index
   * @throws MediaNotFoundException If the Media object could not be found
   */
  public Media get(int index) throws MediaNotFoundException {
    if(this.media != null && this.media.size() > 0) {
      return this.media.get(index);
    }

    throw new MediaNotFoundException("No media available at index: " + index);
  }

  /**
   * Rents the Media object with the given ID. Will set the 'rented' flag to 'true' on the Media item 
   * and save to the directory given in load().  
   * 
   * Returns the rental fee for the rented Media object.
   *
   * @param id The id of the Media object
   * @return The rental fee for the Media object
   * @throws MediaNotFoundException If the given ID could not be found
   * @throws MediaUpdateException If the Media could not be properly saved
   */
  public double rent(int id) throws MediaNotFoundException, MediaUpdateException {
    Media rental = null;
    
    // search media for matching ID
    for(Media item: this.media) {
      if(item.getId() == id) {
        rental = item;
        break;
      }
    }

    // throw exception if it couldn't be found
    if(rental == null) {
      throw new MediaNotFoundException("could not find media for id: " + id);
    }

    // set rental status
    rental.setRented(true);

    // save the media
    try {
      save(rental);
    } catch( FileNotFoundException e ) {
      throw new MediaUpdateException("Unable to update media: " + e.getMessage());
    } catch( IOException e ) {
      throw new MediaUpdateException("Unable to update media: " + e.getMessage());
    }
    

    return rental.caculateRentalFee();
  }
}
