import java.io.*;
import java.util.NoSuchElementException;
import java.util.Scanner;

public abstract class Media {
  private static final double RENTAL_FEE = 3.50;

  private static final String TYPE_EBOOK = "EBook";
  private static final String TYPE_MOVIE = "MovieDVD";
  private static final String TYPE_MUSIC = "MusicCD";

  private int id;
  private String title;
  private int yearPublished;
  private boolean rented;

  public Media(String data) throws MediaCreationException {
    String[] segements = data.split(",");

    if(segements.length < 4) {
      throw new MediaCreationException("Incorrect media data format");
    }

    this.id = Integer.parseInt(segements[0]);
    this.title = segements[1];
    this.yearPublished = Integer.parseInt(segements[2]);
    this.rented = Boolean.parseBoolean(segements[3]);
  }

  public Media(int id, String title, int yearPublished, boolean rented) {
    this.id = id;
    this.title = title;
    this.yearPublished = yearPublished;
    this.rented = rented;
  }

  public int getId() {
    return this.id;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public String getTitle() {
    return this.title;
  }

  public void setYearPublished(int yearPublished) {
    this.yearPublished = yearPublished;
  }

  public int getYearPublished() {
    return this.yearPublished;
  }

  public void setRented(boolean rented) {
    this.rented = rented;
  }

  public boolean isRented() {
    return this.rented;
  }

  public double caculateRentalFee() {
    return RENTAL_FEE;
  }

  public String getAdditionalInfo() {
    return "";
  }

  public String toString() {
    return "id: " + this.id + ", " +
      "title: " + this.title + ", " +
      "year published: " + this.yearPublished + ", " +
      "rented: " + this.rented;
  }

  public static final Media createMedia(File file) throws MediaCreationException {
    String name = file.getName();
    String[] segments = name.split("-");

    if(segments == null || segments.length == 0) {
      throw new MediaCreationException("Incorrect name format for media file: " + file.getName());
    }

    String type = segments[0];
    Media media = null;

    try {
      Scanner scanner = new Scanner(file);
      String data = scanner.nextLine();
      scanner.close();

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

      System.out.println(media);

    } catch(FileNotFoundException e) {
      throw new MediaCreationException("Media file was not found: " + file.getName()) ;
    } catch(NoSuchElementException e) {
      throw new MediaCreationException("Could not read data from file: " + file.getName());
    }
    
    return media;
  }
}