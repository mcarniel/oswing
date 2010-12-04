package org.openswing.swing.importdata.client;

import java.util.*;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.table.*;

import org.openswing.swing.client.*;
import org.openswing.swing.importdata.java.*;
import org.openswing.swing.table.client.*;
import org.openswing.swing.table.model.client.*;
import org.openswing.swing.util.client.*;


/**
 * <p>Title: OpenSwing Framework</p>
 * <p>Description: Import dialog window: used to read data in XLS/CSV format and import by mapping data to selected grid columns.</p>
 * <p>Copyright: Copyright (C) 2006 Mauro Carniel</p>
 *
 * <p> This file is part of OpenSwing Framework.
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the (LGPL) Lesser General Public
 * License as published by the Free Software Foundation;
 *
 *                GNU LESSER GENERAL PUBLIC LICENSE
 *                 Version 2.1, February 1999
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Library General Public License for more details.
 *
 * You should have received a copy of the GNU Library General Public
 * License along with this library; if not, write to the Free
 * Software Foundation, Inc., 675 Mass Ave, Cambridge, MA 02139, USA.
 *
 *       The author may be contacted at:
 *           maurocarniel@tin.it</p>
 *
 * @author Mauro Carniel
 * @version 1.0
 */
public class ImportDialog extends JDialog {

  JPanel buttonsPanel = new JPanel();
  JPanel mainPanel = new JPanel();
  JButton importButton = new JButton();
  FlowLayout flowLayout1 = new FlowLayout();
  JButton cancelButton = new JButton();
  GridBagLayout gridBagLayout1 = new GridBagLayout();
  JLabel colsLabel = new JLabel();
  JScrollPane scrollPane = new JScrollPane();
  JTable cols = new JTable();

  /** parent frame */
  private Window frame = null;

  /** grid */
  private Grids grids = null;
  JPanel importTypePanel = new JPanel();
  FlowLayout flowLayout2 = new FlowLayout();
  LabelControl labelImportType = new LabelControl();

  /** export format combo-box*/
  JComboBox controlImportType = new JComboBox(new Object[]{
    ImportOptions.XLS_FORMAT,
    ImportOptions.CSV_FORMAT1,
    ImportOptions.CSV_FORMAT2
  });


  /**
   * Constructor called by Grid.
   * @param frame parent frame
   * @param grid grid to import
   * @param colsVisible collection of grid columns currently visible
   */
  public ImportDialog(JFrame frame,Grids grids,HashSet colsVisible) {
    super(frame, ClientSettings.getInstance().getResources().getResource("grid import"), true);
    init(frame,grids,colsVisible);
  }


  /**
   * Constructor called by Grid.
   * @param frame parent frame
   * @param grid grid to import
   * @param colsVisible collection of grid columns currently visible
   */
  public ImportDialog(JDialog frame,Grids grids,HashSet colsVisible) {
    super(frame, ClientSettings.getInstance().getResources().getResource("grid import"), true);
    init(frame,grids,colsVisible);
  }


  /**
   * Constructor called by Grid.
   * @param frame parent frame
   * @param grid grid to import
   * @param colsVisible collection of grid columns currently visible
   */
  private void init(Window frame,Grids grids,HashSet colsVisible) {
    this.frame = frame;
    this.grids = grids;
    try {
      controlImportType.setSelectedIndex(0);
      jbInit();
      setSize(260,300);
      init(grids,colsVisible);
      ClientUtils.centerDialog(frame,this);
      setVisible(true);
    }
    catch(Exception ex) {
      ex.printStackTrace();
    }
  }


  /**
   * Initialize the grid.
   */
  private void init(Grids grids,HashSet colsVisible) {
    DefaultTableModel model = new DefaultTableModel() {

      public String getColumnName(int col) {
        if (col==0)
          return ClientSettings.getInstance().getResources().getResource("column");
        else if (col==1)
          return ClientSettings.getInstance().getResources().getResource("sel.");
        else
          return "";
      }

      public int getColumnCount() {
        return 4;
      }

      public Class getColumnClass(int col) {
        if (col==0)
          return String.class;
        else if (col==1)
          return Boolean.class;
        else if (col==2)
          return String.class;
        else
          return Integer.class;
      }

      public boolean isCellEditable(int row,int col) {
        return col==1;
      }

    };

    VOListTableModel gridModel = grids.getVOListTableModel();
    for(int i=0;i<gridModel.getColumnCount();i++)
      if (colsVisible.contains(gridModel.getColumnName(i)))
        model.addRow(new Object[]{
          grids.getHeaderColumnName(gridModel.getColumnName(i)),
          new Boolean(true),
          gridModel.getColumnName(i),
          new Integer(gridModel.getColumnType(i))
        });

    cols.setModel(model);
    cols.setRowHeight(ClientSettings.CELL_HEIGHT);
    cols.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
    cols.setSurrendersFocusOnKeystroke(true);
    cols.setBackground(ClientSettings.GRID_CELL_BACKGROUND);
    cols.setForeground(ClientSettings.GRID_CELL_FOREGROUND);
    cols.setSelectionBackground(ClientSettings.GRID_SELECTION_BACKGROUND);
    cols.setSelectionForeground(ClientSettings.GRID_SELECTION_FOREGROUND);
    cols.getColumnModel().removeColumn(cols.getColumnModel().getColumn(2));
    cols.getColumnModel().removeColumn(cols.getColumnModel().getColumn(2));
    cols.getColumnModel().getColumn(0).setPreferredWidth(180);
    cols.getColumnModel().getColumn(1).setPreferredWidth(40);
    cols.revalidate();
  }


  private void jbInit() throws Exception {
    buttonsPanel.setLayout(flowLayout1);
    mainPanel.setBorder(BorderFactory.createEtchedBorder());
    mainPanel.setLayout(gridBagLayout1);
    buttonsPanel.setBorder(BorderFactory.createEtchedBorder());
    importButton.setMnemonic(ClientSettings.getInstance().getResources().getResource("importmnemonic").charAt(0));
    importButton.setText(ClientSettings.getInstance().getResources().getResource("import"));
    importButton.addActionListener(new ImportDialog_importButton_actionAdapter(this));
    cancelButton.setMnemonic(ClientSettings.getInstance().getResources().getResource("cancelmnemonic").charAt(0));
    cancelButton.setText(ClientSettings.getInstance().getResources().getResource("cancel"));
    cancelButton.addActionListener(new ImportDialog_cancelButton_actionAdapter(this));
    colsLabel.setText(ClientSettings.getInstance().getResources().getResource("columns to import"));
    this.setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
    importTypePanel.setLayout(flowLayout2);
    labelImportType.setLabel("import type");
    flowLayout2.setAlignment(FlowLayout.LEFT);
    getContentPane().add(buttonsPanel,  BorderLayout.SOUTH);
    this.getContentPane().add(mainPanel,  BorderLayout.CENTER);
    buttonsPanel.add(importButton, null);
    buttonsPanel.add(cancelButton, null);
    mainPanel.add(colsLabel,  new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0
            ,GridBagConstraints.NORTHWEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
    mainPanel.add(scrollPane,  new GridBagConstraints(0, 1, 1, 1, 1.0, 1.0
            ,GridBagConstraints.NORTHWEST, GridBagConstraints.BOTH, new Insets(0, 5, 5, 5), 0, 0));
    this.getContentPane().add(importTypePanel, BorderLayout.NORTH);
    scrollPane.getViewport().add(cols, null);
    importTypePanel.add(labelImportType, null);
    importTypePanel.add(controlImportType, null);
  }


  void importButton_actionPerformed(ActionEvent e) {
    final ArrayList columnTypes = new ArrayList();
    final ArrayList importAttrColumns = new ArrayList();
    for(int i=0;i<cols.getRowCount();i++)
      if (((Boolean)cols.getValueAt(i,1)).booleanValue()) {
        columnTypes.add( cols.getModel().getValueAt(i,3) );
        importAttrColumns.add( cols.getModel().getValueAt(i,2) );
      }
    if (columnTypes.size()==0) {
      OptionPane.showMessageDialog(
          frame,
          ClientSettings.getInstance().getResources().getResource("you must select at least one column"),
          ClientSettings.getInstance().getResources().getResource("Error"),
          JOptionPane.WARNING_MESSAGE
      );
      return;
    }

    // import data...
    new Thread() {
      public void run() {
        importButton.setEnabled(false);
        grids.importData(columnTypes,importAttrColumns,(String)controlImportType.getSelectedItem());
        setVisible(false);
      }
    }.start();

  }

  void cancelButton_actionPerformed(ActionEvent e) {
    setVisible(false);
  }

}

class ImportDialog_importButton_actionAdapter implements java.awt.event.ActionListener {
  ImportDialog adaptee;

  ImportDialog_importButton_actionAdapter(ImportDialog adaptee) {
    this.adaptee = adaptee;
  }
  public void actionPerformed(ActionEvent e) {
    adaptee.importButton_actionPerformed(e);
  }
}

class ImportDialog_cancelButton_actionAdapter implements java.awt.event.ActionListener {
  ImportDialog adaptee;

  ImportDialog_cancelButton_actionAdapter(ImportDialog adaptee) {
    this.adaptee = adaptee;
  }
  public void actionPerformed(ActionEvent e) {
    adaptee.cancelButton_actionPerformed(e);
  }
}
