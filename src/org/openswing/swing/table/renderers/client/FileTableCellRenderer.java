package org.openswing.swing.table.renderers.client;

import java.io.*;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.table.*;

import org.openswing.swing.client.*;
import org.openswing.swing.table.client.*;
import org.openswing.swing.util.client.*;
import org.openswing.swing.util.java.*;


/**
 * <p>Title: OpenSwing Framework</p>
 * <p>Description: Column renderer used for file upload/download column.</p>
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
public class FileTableCellRenderer extends DefaultTableCellRenderer {

  /** grid controller */
  private GridController gridController = null;

  /** attribute name associated to this column */
  private String attributeName = null;

  /** optional attribute name used to bind this attribute to the file name */
  private String fileNameAttributeName = null;

  /** cell renderer */
  private JPanel rend = new JPanel();

  /** file name */
  private JLabel fileName = new JLabel();

  /** upload button */
  private JButton upload = new JButton(ClientSettings.getInstance().getResources().getResource("upload file"));

  /** download button */
  private JButton download = new JButton(ClientSettings.getInstance().getResources().getResource("download file"));

  /** current cell content */
  private byte[] cellContent = null;

  /** grid control */
  private JTable table = null;


  /**
   * Constructor.
   * @param text button text
   * @param gridController grid controller
   * @param attributeName attribute name associated to this column
   */
  public FileTableCellRenderer(GridController gridController,String attributeName,boolean showUploadButton,boolean showDownloadButton,String fileNameAttributeName) {
    this.gridController = gridController;
    this.attributeName = attributeName;
    this.fileNameAttributeName = fileNameAttributeName;
    rend.setLayout(new GridBagLayout());
    upload.setEnabled(false);
    if (fileNameAttributeName!=null && !fileNameAttributeName.equals(""))
      rend.add(fileName, new GridBagConstraints(0, 0, 1, 1, 1.0, 0.0
            , GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL,
            new Insets(0, 0, 0, 0), 0, 0));
    if (showUploadButton)
      rend.add(upload, new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0
            , GridBagConstraints.WEST, GridBagConstraints.NONE,
            new Insets(0, 0, 0, 0), 0, 0));
    if (showDownloadButton)
      rend.add(download, new GridBagConstraints(2, 0, 1, 1, 0.0, 0.0
            , GridBagConstraints.WEST, GridBagConstraints.NONE,
            new Insets(0, 0, 0, 0), 0, 0));
    download.setOpaque(false);
    upload.setOpaque(false);
    fileName.setOpaque(false);
    rend.setFocusable(true);
    download.setPreferredSize(new Dimension(21, 20));
    download.setMinimumSize(new Dimension(100, 20));
    download.setMaximumSize(new Dimension(100, 20));
    upload.setPreferredSize(new Dimension(100, 20));
    upload.setMinimumSize(new Dimension(100, 20));
    upload.setMaximumSize(new Dimension(100, 20));
  }


  public Component getTableCellRendererComponent(JTable table, Object value,
                          boolean isSelected, boolean hasFocus, final int row, final int column) {

    if (this.table==null) {
      this.table = table;
      table.addMouseListener(new MouseAdapter() {
        public void mouseClicked(MouseEvent e) {
          int w = 0;
          for(int i=0;i<column;i++)
            w+= FileTableCellRenderer.this.table.getColumnModel().getColumn(i).getWidth();
          int delta = e.getX()-w;
          int downloadPosX = download.getLocation().x;
          if (e.getClickCount()==1 &&
              SwingUtilities.isLeftMouseButton(e) &&
              delta>downloadPosX &&
              delta<downloadPosX+download.getWidth()) {
            final JFileChooser f = new JFileChooser();
            f.setDialogTitle(ClientSettings.getInstance().getResources().getResource("download file"));
            f.setDialogType(f.SAVE_DIALOG);
            f.setApproveButtonText(ClientSettings.getInstance().getResources().getResource("download file"));
            int row = FileTableCellRenderer.this.table.rowAtPoint(e.getPoint());
            if (FileTableCellRenderer.this.fileNameAttributeName!=null && !FileTableCellRenderer.this.fileNameAttributeName.equals("")) {
              Object obj = ((Grid)FileTableCellRenderer.this.table).getGrids().getVOListTableModel().getField(row,FileTableCellRenderer.this.fileNameAttributeName);
              if (obj!=null)
                f.setSelectedFile(new File(obj.toString()));
            }
            int res = f.showSaveDialog(ClientUtils.getParentWindow(FileTableCellRenderer.this.table));
            if (res==f.APPROVE_OPTION) {
              try {
                File file = f.getSelectedFile();
                BufferedOutputStream out = new BufferedOutputStream(new FileOutputStream(file));

                cellContent = (byte[])((Grid)FileTableCellRenderer.this.table).getGrids().getVOListTableModel().getField(row,FileTableCellRenderer.this.attributeName);
                out.write(cellContent);
                out.close();
              }
              catch (Throwable ex) {
                OptionPane.showMessageDialog(ClientUtils.getParentWindow(FileTableCellRenderer.this.table),ex.getMessage(),ClientSettings.getInstance().getResources().getResource("Error while saving"),JOptionPane.ERROR_MESSAGE);
              }
            }
          }
        }
      });
    }


    this.cellContent = (byte[])value;
    download.setEnabled(cellContent!=null);
    upload.setEnabled(((Grid)table).getMode()!=Consts.READONLY);
    if (table instanceof Grid &&
        fileNameAttributeName!=null &&
        !fileNameAttributeName.equals("")) {
      Object obj = ((Grid)table).getGrids().getVOListTableModel().getField(row,fileNameAttributeName);
      fileName.setText( obj==null?"":obj.toString() );
    }

    if (hasFocus && table instanceof Grid) {
      if (ClientSettings.IGNORE_GRID_SELECTION_FOREGROUND)
        rend.setForeground(gridController.getForegroundColor(row,table.getModel().getColumnName(table.convertColumnIndexToModel(column)),value));
      else
        rend.setForeground(table.getSelectionForeground());

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
      rend.setBackground(new Color(
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
      rend.setBorder(BorderFactory.createLineBorder(table.getSelectionForeground()));
    } else if (isSelected && !hasFocus) {
      if (ClientSettings.IGNORE_GRID_SELECTION_FOREGROUND)
        rend.setForeground(gridController.getForegroundColor(row,table.getModel().getColumnName(table.convertColumnIndexToModel(column)),value));
      else
        rend.setForeground(table.getSelectionForeground());

      Color backColor = gridController.getBackgroundColor(row,table.getModel().getColumnName(table.convertColumnIndexToModel(column)),value);
      Color selColor = null;
      try {
        selColor = new Color(
            Math.min(255,
                     2 * table.getSelectionBackground().getRed() -
                     ClientSettings.GRID_CELL_BACKGROUND.getRed()),
            Math.min(255,
                     2 * table.getSelectionBackground().getGreen() -
                     ClientSettings.GRID_CELL_BACKGROUND.getGreen()),
            Math.min(255,
                     2 * table.getSelectionBackground().getBlue() -
                     ClientSettings.GRID_CELL_BACKGROUND.getBlue())
            );
      }
      catch (Exception ex1) {
        selColor = ( (Grid) table).getSelectionBackground();
      }
      rend.setBackground(new Color(
          (backColor.getRed()+selColor.getRed())/2,
          (backColor.getGreen()+selColor.getGreen())/2,
          (backColor.getBlue()+selColor.getBlue())/2
      ));

      rend.setBorder(BorderFactory.createRaisedBevelBorder());
    } else {
      rend.setForeground(gridController.getForegroundColor(row,table.getModel().getColumnName(table.convertColumnIndexToModel(column)),value));
      rend.setBorder(BorderFactory.createRaisedBevelBorder());
      if (((Grid)table).getMode()==Consts.READONLY || !((Grid)table).isColorsInReadOnlyMode())
        rend.setBackground(gridController.getBackgroundColor(row,table.getModel().getColumnName(table.convertColumnIndexToModel(column)),value));
      else {
        if (table.isCellEditable(row,column))
          rend.setBackground(ClientSettings.GRID_EDITABLE_CELL_BACKGROUND);
        else
          rend.setBackground(ClientSettings.GRID_NOT_EDITABLE_CELL_BACKGROUND);
      }
    }

    if (table instanceof Grid)
      rend.setToolTipText(gridController.getCellTooltip(row,((Grid)table).getVOListTableModel().getColumnName(table.convertColumnIndexToModel(column))));

    return rend;
  }


  public final void finalize() {
    gridController = null;
    rend = null;
  }



}
