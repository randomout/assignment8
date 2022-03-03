import java.util.ArrayList;

import javax.swing.table.AbstractTableModel;

public class MediaTableModel extends AbstractTableModel {
  private ArrayList<Media> media;

  private String[] columnNames = {"ID", "Type", "Title", "Year Published", "Rented", "Additional Info"};

  public MediaTableModel() {
    this.media = null;
  }

  public void setMedia(ArrayList<Media> media) {
    this.media = media;
    fireTableDataChanged();
    fireTableStructureChanged();
  }

  public boolean isCellEditable(int row, int col) {
    return false;
  }

  public int getRowCount() {
    if(media == null || media.size() == 0) {
      return 1;
    }

    return media.size();
  }

  public int getColumnCount() {
    if(media == null || media.size() == 0) {
      return 1;
    } else {
      return 6;
    }

  }

  public String getColumnName(int column) {
    if(media == null || media.size() == 0) {
      return "";
    } else {
      return this.columnNames[column];
    }
  }

  public Object getValueAt(int row, int col) {
    if(media == null || media.size() == 0) {
      return "No media available - please load data first";
    } else {
      Media item = media.get(row);

      switch(col) {
        case 0:
          return item.getId();
        case 1: 
          return item.getClass().getSimpleName();
        case 2: 
          return item.getTitle();
        case 3:
          return item.getYearPublished();
        case 4:
          return item.isRented();
        case 5:
          return item.getAdditionalInfo();
        default:
          return null;
      }
    }

  }
  
}
