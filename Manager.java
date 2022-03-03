import java.util.ArrayList;
import java.util.Comparator;
import java.io.*;

public class Manager {

  private ArrayList<Media> media;

  private File directory = null;

  public Manager() {
    this.media = new ArrayList<Media>();
  }

  public void load(File directory) throws FileNotFoundException, MediaCreationException {
    this.directory = directory;

    File files[] = this.directory.listFiles();

    if(files == null || files.length == 0) {
      this.directory = null;
      throw new FileNotFoundException("No media files found in directory: " + directory.getAbsolutePath());
    }

    try {
      this.media = new ArrayList<Media>();

      for(File file: files) {
        Media item = Media.createMedia(file);      
        this.media.add(item);
      }

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

  public void save(Media media) throws FileNotFoundException {
    if(this.directory == null) {
      throw new FileNotFoundException("No directory available to save");
    }

    File file = new File(this.directory, media.getClass().getSimpleName() + "-" + media.getId() + ".txt");
    PrintWriter out = new PrintWriter(file);

    out.println(media.toString());
    out.close();
  }

  public void add(Media media) throws MediaUpdateException {
    try {
      save(media);
      this.media.add(media);
    } catch(FileNotFoundException e) {
      throw new MediaUpdateException("Unable to add media: " + e.getMessage());
    }
  }
  
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

  public ArrayList<Media> getAllMedia() {
    return this.media;
  }

  public double rent(int id) throws MediaNotFoundException, MediaUpdateException {
    Media rental = null;
    
    for(Media item: this.media) {
      if(item.getId() == id) {
        rental = item;
        break;
      }
    }

    if(rental == null) {
      throw new MediaNotFoundException("could not find media for id: " + id);
    }

    rental.setRented(true);

    try {
      save(rental);
    } catch( FileNotFoundException e ) {
      throw new MediaUpdateException("Unable to update media: " + e.getMessage());
    }
    

    return 0.0;
  }
}
