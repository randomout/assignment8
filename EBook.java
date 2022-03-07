import java.io.IOException;
import java.io.Writer;
import java.util.Calendar;

public class EBook extends Media {
  private static final double EBOOK_RATE = 0.10;
  
  private int chapters;
  
  public EBook(int id, String title, int yearPublished, boolean rented, int chapters) {
    super(id, title, yearPublished, rented);
    this.chapters = chapters;
  }

  public EBook(String data) throws MediaCreationException {
    super(data);

    String[] segements = data.split(",");

    if(segements.length < 5) {
      throw new MediaCreationException("Incorrect Ebook data format");
    }

    try {
      this.chapters = Integer.parseInt(segements[4]);
    } catch( NumberFormatException e ) {
      throw new MediaCreationException("Unable to read media: " + e.getMessage());
    }
    
  }

  public void setChapters(int chapters) {
    this.chapters = chapters;
  }

  public int getChapters() {
    return this.chapters;
  }

  public String getAdditionalInfo() {
    return "chapters: " + this.chapters;
  }

  public double caculateRentalFee() {
    int currentYear = Calendar.getInstance().get(Calendar.YEAR);

    double fee = this.getChapters() * EBOOK_RATE;

    if(currentYear == this.getYearPublished()) {
      fee += 1.00;
    }

    return fee;
  }

  public void save(Writer writer) throws IOException {
    super.save(writer);
    writer.write("," + this.chapters);
  }
  
}
