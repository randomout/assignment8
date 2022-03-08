import javax.swing.*;
import javax.swing.table.TableColumnModel;

import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.text.DecimalFormat;
import java.util.*;

/**
 * MediaRentalSystem is a GUI application that allow for the display, 
 * search and rental of various media.  After loading data via the menu option, user is presented with a 
 * list of media in a table view that can be selected and rented.  A search bar is also provided to allow for 
 * searching for specific media by title.
 * 
 * @author Brian Cunningham
 * Date: 3/8/2022
 */
public class MediaRentalSystem extends JFrame {
  // Manager instance for managing media
  private Manager manager;

  // table view components
  private JTable mediaTable;
  private MediaTableModel tableModel;

  // search components
  private JTextField searchField;
  private JButton searchButton;
  private JButton clearButton;

  // rental 
  private JButton rentButton;

  // file dialog for loading media data
  private JFileChooser fileDialog;

  /**
   * Creates new MediaRentalSystem application instance
   */
  public MediaRentalSystem() {  
    // init 
    this.manager = new Manager();
    this.tableModel = new MediaTableModel();
    this.fileDialog = new JFileChooser();
    this.fileDialog.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);

    // set size, title, default close behavior
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    setSize(800, 600);
    setTitle("Welcome to Media Rental System");

    // create menu
    JMenuBar menuBar = new JMenuBar();
    JMenu menu = new JMenu("Menu");

    // create load option for menu
    JMenuItem load = new JMenuItem("Load Media...");
    load.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        loadMedia();
      }
    });

    // create exit option for menu
    JMenuItem exit = new JMenuItem("Exit");
    exit.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        closeApplication();
      }
    });

    menu.add(load);
    menu.addSeparator();
    menu.add(exit);

    menuBar.add(menu);

    setJMenuBar(menuBar);
    
    // create media table
    mediaTable = new JTable(tableModel);
    mediaTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
    mediaTable.setFillsViewportHeight(true);

    JScrollPane scrollPane = new JScrollPane(mediaTable);

    // create search components
    searchField = new JTextField(30);
    searchButton = new JButton("Search");
    clearButton = new JButton("Clear");

    // add listener for searching
    ActionListener searchListener = new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        searchMedia();
      }
    };

    searchField.addActionListener(searchListener);
    searchButton.addActionListener(searchListener);

    // add listener for clearing search
    clearButton.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        clearSearch();
      }
    });

    // create rent button and listener
    rentButton = new JButton("Rent");
    rentButton.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        rentMedia();
      }
    });

    // create search panel and add components
    JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
    searchPanel.add(searchField);
    searchPanel.add(searchButton);
    searchPanel.add(clearButton);

    // create rent panel and add button
    JPanel rentPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
    rentPanel.add(rentButton);

    // add everything to main window
    Container contentPane = getContentPane();
    contentPane.add(searchPanel, BorderLayout.PAGE_START);
    contentPane.add(scrollPane, BorderLayout.CENTER);
    contentPane.add(rentPanel, BorderLayout.PAGE_END);
    
  }

  /**
   * Loads media when user select the menu option.  
   * Presents user with file dialog to select directory, then 
   * calls manager and updates table with loaded data.
   */
  private void loadMedia() {
    // show file dialog
    int result = fileDialog.showDialog(this, "Open");

    // if user chose a directory...
    if(result == JFileChooser.APPROVE_OPTION) {
      // get the selected directory...
      File file = fileDialog.getSelectedFile();

      try {
        // have manager load files in directory...
        manager.load(file);

        // update table with loaded data
        tableModel.setMedia(manager.getAllMedia());

        updateColumns();
      }  catch(FileNotFoundException fnf) {
        JOptionPane.showMessageDialog(this, fnf.getMessage());
        fnf.printStackTrace();
      } catch(MediaCreationException mc) {
        JOptionPane.showMessageDialog(this, mc.getMessage());
        mc.printStackTrace();
      }
    }
  }

  /**
   * Handles user hitting 'rent' in UI, calling manager to handle renting media
   */
  private void rentMedia() {
    // ensure we have media to select first
    if( manager.getAllMedia() == null || manager.getAllMedia().size() == 0 ) {
      JOptionPane.showMessageDialog(this, "No media loaded! Load media first!");
      return;
    }

    // get the selection from the table
    int selection = mediaTable.getSelectedRow();

    // verify we actually have a selection
    if(selection < 0) {
      JOptionPane.showMessageDialog(this, "No media selected! Select media to rent first!");
      return;
    }

    try {
      // get the selected item from the manager
      Media media = manager.get(selection);

      // check that is hasn't already been rented first
      if(media.isRented()) {
        JOptionPane.showMessageDialog(this, "This media is already rented");
        return;
      }

      // confirm rental 
      String message = "Rent item: " + media.getClass().getSimpleName() + "-" + media.getTitle() + "?";
      selection = JOptionPane.showConfirmDialog(this, message, "Rent Media", JOptionPane.YES_NO_OPTION);
  
      // if confirmed...
      if(selection == JOptionPane.YES_OPTION) {
        // tell manager to rent media (and get fee)
        double fee = manager.rent(media.getId());
        // show user rental fee
        DecimalFormat format = new DecimalFormat("$#0.00");
        JOptionPane.showMessageDialog(this, "Media Rental Price: " + format.format(fee));
      }
    } catch(MediaNotFoundException e) {
      JOptionPane.showMessageDialog(this, "Unable to select media: " + e.getMessage());
    } catch(MediaUpdateException e) {
      JOptionPane.showMessageDialog(this, "Unable to rent media: " + e.getMessage());
    }
  }

  /**
   * Updates column sizes.  This ensures that columns have proper spacing when showing media data.
   */
  private void updateColumns() {
    TableColumnModel model = mediaTable.getColumnModel();

    model.getColumn(0).setPreferredWidth(10);
    model.getColumn(1).setPreferredWidth(80);
    model.getColumn(2).setPreferredWidth(300);
    model.getColumn(3).setPreferredWidth(100);
    model.getColumn(4).setPreferredWidth(50);
    model.getColumn(5).setPreferredWidth(200);
  }

  /**
   * handles searching of media from UI actions.  
   */
  private void searchMedia() {
    // verify we have media to search first
    if(manager.getAllMedia().size() == 0) {
      JOptionPane.showMessageDialog(this, "No media to search! Load media first!");
    }

    // get the text to search for
    String searchText = searchField.getText();

    // have manager search for the title text
    ArrayList<Media> media = manager.find(searchText);

    // if we didn't find anything, preset user with a dialog
    if(media.size() == 0) {
      JOptionPane.showMessageDialog(this, "No media was found matching this title");
      return;
    }

    // update the table to show only items found in search
    tableModel.setMedia(media);
    updateColumns();
  }

  /**
   * Handle clearing of search
   */
  private void clearSearch() {
    // clear search field
    searchField.setText("");

    // update table to show all media items
    tableModel.setMedia(manager.getAllMedia());
    updateColumns();
  }

  /**
   * handles closing the application from the exit menu
   */
  private void closeApplication() {
    this.dispose();
  }
  
  /**
   * main method/application start
   * @param args not used
   */
  public static void main(String[] args) {
    // create new MediaRentalSystem and show it
    new MediaRentalSystem().setVisible(true);
  }
}
