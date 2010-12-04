package org.openswing.swing.table.editors.client;

import java.io.*;
import java.util.*;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.filechooser.FileFilter;
import javax.swing.table.*;

import org.openswing.swing.client.*;
import org.openswing.swing.logger.client.*;
import org.openswing.swing.mdi.client.*;
import org.openswing.swing.table.client.*;
import org.openswing.swing.util.client.*;
import org.openswing.swing.util.java.Consts;


/**
 * <p>Title: OpenSwing Framework</p>
 * <p>Description: Column editor used to import/download a file from/to local file system.</p>
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
public class FileCellEditor extends AbstractCellEditor implements TableCellEditor {

  /** flag used to define if a file import button must be showed; default value: <code>true</code> */
  private boolean showUploadButton = true;

  /** flag used to define if a file export button must be showed; default value: <code>true</code> */
  private boolean showDownloadButton = true;

  /** optional attribute name used to bind this attribute to the file name */
  private String fileNameAttributeName = null;

  /** attribute name associated to this column */
  private String attributeName = null;

  /** cell editor */
  private JPanel cell = new JPanel();

  /** table */
  private JTable table = null;

  /** current row index */
  private int row = -1;

  /** current column index */
  private int col = -1;

  /** file name */
  private JLabel fileName = new JLabel();

  /** upload button */
  private JButton upload = new JButton(ClientSettings.getInstance().getResources().getResource("upload file"));

  /** download button */
  private JButton download = new JButton(ClientSettings.getInstance().getResources().getResource("download file"));

  /** current cell content */
  private byte[] cellContent = null;

  /** grid controller */
  private GridController gridController = null;


  /**
   * Constructor.
   * @param text button text
   * @param actionListeners list of ActionListeners linked to the button
   */
  public FileCellEditor(GridController gridController,final FileFilter fileFilter,boolean showUploadButton,boolean showDownloadButton,String attributeName,String fileNameAttributeName) {
    this.gridController = gridController;
    this.showUploadButton = showUploadButton;
    this.showDownloadButton = showDownloadButton;
    this.attributeName = attributeName;
    this.fileNameAttributeName = fileNameAttributeName;

    cell.setLayout(new GridBagLayout());
    if (fileNameAttributeName!=null && !fileNameAttributeName.equals(""))
      cell.add(fileName, new GridBagConstraints(0, 0, 1, 1, 1.0, 0.0
            , GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL,
            new Insets(0, 0, 0, 0), 0, 0));
    if (showUploadButton)
      cell.add(upload, new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0
            , GridBagConstraints.WEST, GridBagConstraints.NONE,
            new Insets(0, 0, 0, 0), 0, 0));
    if (showDownloadButton)
      cell.add(download, new GridBagConstraints(2, 0, 1, 1, 0.0, 0.0
            , GridBagConstraints.WEST, GridBagConstraints.NONE,
            new Insets(0, 0, 0, 0), 0, 0));

    cell.setFocusable(true);
    download.setFocusable(true);
    upload.setFocusable(true);

    download.setPreferredSize(new Dimension(21, 20));
    download.setMinimumSize(new Dimension(100, 20));
    download.setMaximumSize(new Dimension(100, 20));
    upload.setPreferredSize(new Dimension(100, 20));
    upload.setMinimumSize(new Dimension(100, 20));
    upload.setMaximumSize(new Dimension(100, 20));
    upload.addActionListener(new ActionListener() {

      public void actionPerformed(ActionEvent e) {
        JFileChooser f = new JFileChooser();
        f.setDialogTitle(ClientSettings.getInstance().getResources().getResource("upload file"));
        f.setDialogType(f.OPEN_DIALOG);
        f.setApproveButtonText(ClientSettings.getInstance().getResources().getResource("upload file"));
        f.setFileSelectionMode(f.FILES_ONLY);
        if (fileFilter!=null)
          f.setFileFilter(fileFilter);
        int res = f.showOpenDialog(ClientUtils.getParentWindow(table));
        if (res==f.APPROVE_OPTION && table!=null && table instanceof Grid) {
          // file selected: it will be set into cell...
          try {
            if (FileCellEditor.this.fileNameAttributeName!=null && !FileCellEditor.this.fileNameAttributeName.equals("")) {
              ((Grid)table).getVOListTableModel().setField(row,FileCellEditor.this.fileNameAttributeName,f.getSelectedFile().getName());
              fileName.setText(f.getSelectedFile().getName());
            }
            BufferedInputStream in = new BufferedInputStream(new FileInputStream(f.getSelectedFile()));
            byte[] bytes = new byte[(int)f.getSelectedFile().length()];
            in.read(bytes);
            in.close();
            table.getModel().setValueAt(bytes,row,col);
            cellContent = bytes;
            download.setEnabled(true);
          }
          catch (Exception ex) {
            OptionPane.showMessageDialog(ClientUtils.getParentWindow(table),"Error",ex.getMessage(),JOptionPane.ERROR_MESSAGE);
          }
        }
      }

    });

    download.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        final JFileChooser f = new JFileChooser();
        f.setDialogTitle(ClientSettings.getInstance().getResources().getResource("download file"));
        f.setDialogType(f.SAVE_DIALOG);
        f.setApproveButtonText(ClientSettings.getInstance().getResources().getResource("download file"));
        if (FileCellEditor.this.fileNameAttributeName!=null && !FileCellEditor.this.fileNameAttributeName.equals("")) {
          Object obj = ((Grid)table).getGrids().getVOListTableModel().getField(row,FileCellEditor.this.fileNameAttributeName);
          if (obj!=null)
            f.setSelectedFile(new File(obj.toString()));
        }
        int res = f.showSaveDialog(ClientUtils.getParentWindow(table));
        if (res==f.APPROVE_OPTION) {
          try {
            File file = f.getSelectedFile();
            BufferedOutputStream out = new BufferedOutputStream(new FileOutputStream(file));

            cellContent = (byte[])((Grid)FileCellEditor.this.table).getGrids().getVOListTableModel().getField(row,FileCellEditor.this.attributeName);

            out.write(cellContent);
            out.close();
          }
          catch (Throwable ex) {
            OptionPane.showMessageDialog(ClientUtils.getParentWindow(table),ex.getMessage(),ClientSettings.getInstance().getResources().getResource("Error while saving"),JOptionPane.ERROR_MESSAGE);
          }
        }
      }
    });

  }


  /**
   * Stop cell editing. This method stops cell editing (effectively committing the edit) only if the data entered is validated successfully.
   * @return <code>true</code> if cell editing may stop, and <code>false</code> otherwise.
   */
  public final boolean stopCellEditing() {
    fireEditingStopped();
    return true;
  }


  public final Object getCellEditorValue() {
    return cellContent;
  }


  public final Component getTableCellEditorComponent(JTable table, Object value,
                                               boolean isSelected, int row,
                                               int column) {
    this.table = table;
    this.row = row;
    this.col = column;
    this.cellContent = (byte[])value;
    download.setEnabled(cellContent!=null);
    download.setOpaque(false);
    upload.setOpaque(false);
    fileName.setOpaque(false);

    upload.setEnabled(((Grid)table).getMode()!=Consts.READONLY);



    if (isSelected && table instanceof Grid) {
      Color selColor = null;
      try {
        selColor = new Color(
            Math.min(255,
                     2 * ( (Grid) table).getActiveCellBackgroundColor().getRed() -
                     ClientSettings.GRID_CELL_BACKGROUND.getRed()),
            Math.min(255,
                     2 * ( (Grid) table).getActiveCellBackgroundColor().getGreen() -
                     ClientSettings.GRID_CELL_BACKGROUND.getGreen()),
            Math.min(255,
                     2 * ( (Grid) table).getActiveCellBackgroundColor().getBlue() -
                     ClientSettings.GRID_CELL_BACKGROUND.getBlue())
            );
      }
      catch (Exception ex1) {
        selColor = ( (Grid) table).getActiveCellBackgroundColor();
      }
      Color backColor = gridController.getBackgroundColor(row,table.getModel().getColumnName(table.convertColumnIndexToModel(column)),value);
      cell.setBackground(new Color(
          (backColor.getRed()+selColor.getRed())/2,
          (backColor.getGreen()+selColor.getGreen())/2,
          (backColor.getBlue()+selColor.getBlue())/2
      ));
      download.setBackground(new Color(
          (backColor.getRed()+selColor.getRed())/2,
          (backColor.getGreen()+selColor.getGreen())/2,
          (backColor.getBlue()+selColor.getBlue())/2
      ));
      upload.setBackground(new Color(
          (backColor.getRed()+selColor.getRed())/2,
          (backColor.getGreen()+selColor.getGreen())/2,
          (backColor.getBlue()+selColor.getBlue())/2
      ));
      cell.setBorder(BorderFactory.createLineBorder(table.getSelectionForeground()));
//    } else if (isSelected && !hasFocus) {
//      Color backColor = gridController.getBackgroundColor(row,table.getModel().getColumnName(table.convertColumnIndexToModel(column)),value);
//      Color selColor = null;
//      try {
//        selColor = new Color(
//            Math.min(255,
//                     2 * table.getSelectionBackground().getRed() -
//                     ClientSettings.GRID_CELL_BACKGROUND.getRed()),
//            Math.min(255,
//                     2 * table.getSelectionBackground().getGreen() -
//                     ClientSettings.GRID_CELL_BACKGROUND.getGreen()),
//            Math.min(255,
//                     2 * table.getSelectionBackground().getBlue() -
//                     ClientSettings.GRID_CELL_BACKGROUND.getBlue())
//            );
//      }
//      catch (Exception ex1) {
//        selColor = ( (Grid) table).getSelectionBackground();
//      }
//      cell.setBackground(new Color(
//          (backColor.getRed()+selColor.getRed())/2,
//          (backColor.getGreen()+selColor.getGreen())/2,
//          (backColor.getBlue()+selColor.getBlue())/2
//      ));
//
//      cell.setBorder(BorderFactory.createRaisedBevelBorder());
    } else {
      cell.setForeground(gridController.getForegroundColor(row,table.getModel().getColumnName(table.convertColumnIndexToModel(column)),value));
      cell.setBorder(BorderFactory.createRaisedBevelBorder());
      if (((Grid)table).getMode()==Consts.READONLY || !((Grid)table).isColorsInReadOnlyMode())
        cell.setBackground(gridController.getBackgroundColor(row,table.getModel().getColumnName(table.convertColumnIndexToModel(column)),value));
      else {
        if (table.isCellEditable(row,column))
          cell.setBackground(ClientSettings.GRID_EDITABLE_CELL_BACKGROUND);
        else
          cell.setBackground(ClientSettings.GRID_NOT_EDITABLE_CELL_BACKGROUND);
      }
    }



    if (table instanceof Grid &&
        fileNameAttributeName!=null &&
        !fileNameAttributeName.equals("")) {
      Object obj = ((Grid)table).getGrids().getVOListTableModel().getField(row,fileNameAttributeName);
      fileName.setText( obj==null?"":obj.toString() );
    }

    return cell;
  }


  public final void finalize() {
    table = null;
  }


}


