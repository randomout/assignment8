import java.text.DecimalFormat;

public class MovieDVD extends Media {
  private double size;

  public MovieDVD(int id, String title, int yearPublished, boolean rented, double size) {
    super(id, title, yearPublished, rented);
    this.size = size;
  }

  public MovieDVD(String data) throws MediaCreationException {
    super(data);
    
    String[] segements = data.split(",");

    if(segements.length < 5) {
      throw new MediaCreationException("Incorrect MovieDVD format");
    }

    this.size = Double.parseDouble(segements[4]);
  }

  public void setSize(double size) {
    this.size = size;
  }

  public double getSize() {
    return this.size;
  }

  public String getAdditionalInfo() {
    DecimalFormat format = new DecimalFormat("#0.00");
    return "size: " + format.format(this.size) + "MB";
  }
  
}
