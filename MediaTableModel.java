import java.util.ArrayList;

import javax.swing.table.AbstractTableModel;

/**
 * AbstractTableModel impelementation used for displaying Media data in a JTable
 */
public class MediaTableModel extends AbstractTableModel {
  // the media for the table
  private ArrayList<Media> media;

  private String[] columnNames = {"ID", "Type", "Title", "Year Published", "Rented", "Additional Info"};

  /**
   * Creates a new MediaTableModel
   */
  public MediaTableModel() {
    this.media = null;
  }

  /**
   * Sets the media to be used in the MediaTableModel
   * @param media An ArrayList of Media objects to display
   */
  public void setMedia(ArrayList<Media> media) {
    this.media = media;
    fireTableDataChanged();
    fireTableStructureChanged();
  }

  /**
   * Indicates if a cell is editable - all are non-editable in this model
   * @return false for all cells
   */
  public boolean isCellEditable(int row, int col) {
    return false;
  }

  /**
   * Gets the number of rows in the model (based on media provided to setMedia())
   * @return The number of rows in the model
   */
  public int getRowCount() {
    if(media == null || media.size() == 0) {
      return 1;
    }

    return media.size();
  }

  /**
   * Gets the column count for the model.  
   * @return The number of columns in the model.
   */
  public int getColumnCount() {
    if(media == null || media.size() == 0) {
      return 1;
    } else {
      return 6;
    }

  }

  /**
   * Gets the column name
   * @param column the column number to get the name for
   * @return The name for the column
   */
  public String getColumnName(int column) {
    if(media == null || media.size() == 0) {
      return "";
    } else {
      return this.columnNames[column];
    }
  }

  /**
   * Gets the value at the specified row/col
   * @param row the row in the table
   * @param col the column in the table
   * @return the value at the provided row/col
   */
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
