import javax.swing.*;
import javax.swing.table.TableColumnModel;

import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.FileNotFoundException;
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
    mediaTable.setFillsViewportHeight(true);

    JScrollPane scrollPane = new JScrollPane(mediaTable);

    searchField = new JTextField(30);
    searchButton = new JButton("Search");
    clearButton = new JButton("Clear");

    ActionListener searchListener = new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        if(manager.getAllMedia().size() == 0) {
          // TODO: no media to search!
        }

        String searchText = searchField.getText();

        ArrayList<Media> media = manager.find(searchText);

        System.out.println(media.size());
        
        if(media.size() == 0) {
          //TODO: no media found
          return;
        }

        tableModel.setMedia(media);
        updateColumns();
      }
    };

    searchField.addActionListener(searchListener);
    searchButton.addActionListener(searchListener);

    clearButton.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        searchField.setText("");
        tableModel.setMedia(manager.getAllMedia());
        updateColumns();
      }
    });

    JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
    searchPanel.add(searchField);
    searchPanel.add(searchButton);
    searchPanel.add(clearButton);

    getContentPane().add(searchPanel, BorderLayout.PAGE_START);
    getContentPane().add(scrollPane, BorderLayout.CENTER);
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
        // TODO
        fnf.printStackTrace();
      } catch(MediaCreationException mc) {
        // TODO
        mc.printStackTrace();
      }
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

  private void closeApplication() {
    this.dispose();
  }
  
  public static void main(String[] args) {
    new MediaRentalSystem().setVisible(true);
  }
}
