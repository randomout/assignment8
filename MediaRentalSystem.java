import javax.swing.*;
import javax.swing.table.TableColumnModel;

import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.text.DecimalFormat;
import java.util.*;

public class MediaRentalSystem extends JFrame {
  private Manager manager;

  private JTable mediaTable;
  private MediaTableModel tableModel;

  private JTextField searchField;
  private JButton searchButton;
  private JButton clearButton;

  private JButton rentButton;

  private JFileChooser fileDialog;

  public MediaRentalSystem() {  
    this.manager = new Manager();
    this.tableModel = new MediaTableModel();
    this.fileDialog = new JFileChooser();
    this.fileDialog.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);

    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    setSize(800, 600);
    setTitle("Welcome to Media Rental System");

    JMenuBar menuBar = new JMenuBar();

    JMenu menu = new JMenu("Menu");

    JMenuItem load = new JMenuItem("Load Media...");
    load.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        loadMedia();
      }
    });

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
    
    mediaTable = new JTable(tableModel);
    mediaTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
    mediaTable.setFillsViewportHeight(true);

    JScrollPane scrollPane = new JScrollPane(mediaTable);

    searchField = new JTextField(30);
    searchButton = new JButton("Search");
    clearButton = new JButton("Clear");

    ActionListener searchListener = new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        searchMedia();
      }
    };

    searchField.addActionListener(searchListener);
    searchButton.addActionListener(searchListener);

    clearButton.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        clearSearch();
      }
    });

    rentButton = new JButton("Rent");
    rentButton.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        rentMedia();
      }
    });

    JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
    searchPanel.add(searchField);
    searchPanel.add(searchButton);
    searchPanel.add(clearButton);

    JPanel rentPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
    rentPanel.add(rentButton);

    Container contentPane = getContentPane();
    contentPane.add(searchPanel, BorderLayout.PAGE_START);
    contentPane.add(scrollPane, BorderLayout.CENTER);
    contentPane.add(rentPanel, BorderLayout.PAGE_END);
    
  }

  private void loadMedia() {
    int result = fileDialog.showDialog(this, "Open");

    if(result == JFileChooser.APPROVE_OPTION) {
      File file = fileDialog.getSelectedFile();

      try {
        manager.load(file);
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

  private void rentMedia() {
    if( manager.getAllMedia() == null || manager.getAllMedia().size() == 0 ) {
      JOptionPane.showMessageDialog(this, "No media loaded! Load media first!");
      return;
    }

    int selection = mediaTable.getSelectedRow();

    if(selection < 0) {
      JOptionPane.showMessageDialog(this, "No media selected! Select media to rent first!");
      return;
    }

    try {
      Media media = manager.get(selection);

      if(media.isRented()) {
        JOptionPane.showMessageDialog(this, "This media is already rented");
        return;
      }

      String message = "Rent item: " + media.getClass().getSimpleName() + "-" + media.getTitle() + "?";

      selection = JOptionPane.showConfirmDialog(this, message, "Rent Media", JOptionPane.YES_NO_OPTION);
  
      if(selection == JOptionPane.YES_OPTION) {
        manager.rent(media.getId());
        DecimalFormat format = new DecimalFormat("$#0.00");
        JOptionPane.showMessageDialog(this, "Media Rental Price: " + format.format(media.caculateRentalFee()));
      }
    } catch(MediaNotFoundException e) {
      JOptionPane.showMessageDialog(this, "Unable to select media: " + e.getMessage());
    } catch(MediaUpdateException e) {
      JOptionPane.showMessageDialog(this, "Unable to rent media: " + e.getMessage());
    }
  }

  private void updateColumns() {
    TableColumnModel model = mediaTable.getColumnModel();

    model.getColumn(0).setPreferredWidth(10);
    model.getColumn(1).setPreferredWidth(80);
    model.getColumn(2).setPreferredWidth(300);
    model.getColumn(3).setPreferredWidth(100);
    model.getColumn(4).setPreferredWidth(50);
    model.getColumn(5).setPreferredWidth(200);
  }

  private void searchMedia() {
    if(manager.getAllMedia().size() == 0) {
      JOptionPane.showMessageDialog(this, "No media to search! Load media first!");
    }

    String searchText = searchField.getText();

    ArrayList<Media> media = manager.find(searchText);

    System.out.println(media.size());
    
    if(media.size() == 0) {
      JOptionPane.showMessageDialog(this, "No media was found matching this title");
      return;
    }

    tableModel.setMedia(media);
    updateColumns();
  }

  private void clearSearch() {
    searchField.setText("");
    tableModel.setMedia(manager.getAllMedia());
    updateColumns();
  }

  private void closeApplication() {
    this.dispose();
  }
  
  public static void main(String[] args) {
    new MediaRentalSystem().setVisible(true);
  }
}
