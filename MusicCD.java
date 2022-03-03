import java.util.Calendar;;

public class MusicCD extends Media {
  private static final double MUSIC_CD_RATE = 0.02;

  private int length;

  public MusicCD(int id, String title, int yearPublished, boolean rented, int length) {
    super(id, title, yearPublished, rented);
    this.length = length;
  }

  public MusicCD(String data) throws MediaCreationException{
    super(data);

    String[] segements = data.split(",");

    if(segements.length < 5) {
      throw new MediaCreationException("Incorrect MusicCD format");
    }

    this.length = Integer.parseInt(segements[4]);
  }

  public void setLength(int length) {
    this.length = length;
  }

  public int getLength() {
    return this.length;
  }

  public String getAdditionalInfo() {
    return "length: " + this.length + " minutes";
  }

  public double caculateRentalFee() {
    int currentYear = Calendar.getInstance().get(Calendar.YEAR);

    double fee = this.getLength() * MUSIC_CD_RATE;

    if(currentYear == this.getYearPublished()) {
      fee += 1.00;
    }

    return fee;
  }

  
}
