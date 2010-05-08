package org.openswing.swing.table.renderers.client;

import java.awt.*;
import javax.swing.*;
import javax.swing.table.*;

import org.openswing.swing.table.client.*;
import org.openswing.swing.util.client.*;
import org.openswing.swing.util.java.*;


/**
 * <p>Title: OpenSwing Framework</p>
 * <p>Description: Column renderer used to format a text type column.</p>
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
public class FormattedTextTableCellRenderer extends DefaultTableCellRenderer {

  /** grid controller */
  private GridController gridController = null;

  /** default font */
  private Font defaultFont = null;

  /** attribute name associated to this column */
  private String attributeName = null;

  /** formatter box */
  private JFormattedTextField formattedBox = null;

  /** component container */
  private JPanel container = new JPanel();

  /** component left margin, with respect to component container */
  private int leftMargin = 0;

  /** component right margin, with respect to component container */
  private int rightMargin = 0;

  /** component top margin, with respect to component container */
  private int topMargin = 0;

  /** component bottom margin, with respect to component container */
  private int bottomMargin = 0;


  /**
   * Constructor.
   * @param gridController grid controller
   * @param encryptText flag used to view "*" symbols instead of the real text
   * @param attributeName attribute name associated to this column
   */
  public FormattedTextTableCellRenderer(GridController gridController,JFormattedTextField formattedBox,
                                        int leftMargin,int rightMargin,int topMargin,int bottomMargin,int alignement,String attributeName) {
    this.gridController = gridController;
    this.formattedBox = formattedBox;
    this.leftMargin = leftMargin;
    this.rightMargin = rightMargin;
    this.topMargin = topMargin;
    this.bottomMargin = bottomMargin;
    this.attributeName = attributeName;
    setHorizontalAlignment(alignement);
  }


  public java.awt.Component getTableCellRendererComponent(JTable table, Object value,
                          boolean isSelected, boolean hasFocus, int row, int column) {
    JComponent c = (JComponent)super.getTableCellRendererComponent(table, value,isSelected, hasFocus, row, column);

    if (defaultFont==null)
      defaultFont = ((JLabel)c).getFont();

    if (hasFocus && table instanceof Grid) {
      if (ClientSettings.IGNORE_GRID_SELECTION_FOREGROUND)
        c.setForeground(gridController.getForegroundColor(row,table.getModel().getColumnName(table.convertColumnIndexToModel(column)),value));
      else
        c.setForeground(table.getSelectionForeground());

//      c.setBackground(((Grid)table).getActiveCellBackgroundColor());
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
      c.setBackground(new Color(
          (backColor.getRed()+selColor.getRed())/2,
          (backColor.getGreen()+selColor.getGreen())/2,
          (backColor.getBlue()+selColor.getBlue())/2,
          200
      ));
      c.setBorder(BorderFactory.createLineBorder(table.getSelectionForeground()));
    } else if (isSelected && !hasFocus) {
      if (ClientSettings.IGNORE_GRID_SELECTION_FOREGROUND)
        c.setForeground(gridController.getForegroundColor(row,table.getModel().getColumnName(table.convertColumnIndexToModel(column)),value));
      else
        c.setForeground(table.getSelectionForeground());

//      c.setBackground(table.getSelectionBackground());
      Color backColor = gridController.getBackgroundColor(row,table.getModel().getColumnName(table.convertColumnIndexToModel(column)),value);
      Color selColor = null;
      try {
        selColor = new Color(
            Math.min(255,2 * table.getSelectionBackground().getRed() -
            ClientSettings.GRID_CELL_BACKGROUND.getRed()),
            Math.min(255,2 * table.getSelectionBackground().getGreen() -
            ClientSettings.GRID_CELL_BACKGROUND.getGreen()),
            Math.min(255,2 * table.getSelectionBackground().getBlue() -
            ClientSettings.GRID_CELL_BACKGROUND.getBlue())
            );
      }
      catch (Exception ex) {
        selColor = table.getSelectionForeground();
      }
      c.setBackground(new Color(
          (backColor.getRed()+selColor.getRed())/2,
          (backColor.getGreen()+selColor.getGreen())/2,
          (backColor.getBlue()+selColor.getBlue())/2
      ));
      c.setBorder(BorderFactory.createEmptyBorder());
    }
    else {
      c.setForeground(gridController.getForegroundColor(row,table.getModel().getColumnName(table.convertColumnIndexToModel(column)),value));
      c.setBorder(BorderFactory.createEmptyBorder());
      if (((Grid)table).getMode()==Consts.READONLY || !((Grid)table).isColorsInReadOnlyMode())
        c.setBackground(gridController.getBackgroundColor(row,table.getModel().getColumnName(table.convertColumnIndexToModel(column)),value));
      else {
        if (table.isCellEditable(row,column))
          c.setBackground(ClientSettings.GRID_EDITABLE_CELL_BACKGROUND);
        else
          c.setBackground(ClientSettings.GRID_NOT_EDITABLE_CELL_BACKGROUND);
      }
    }

    formattedBox.setValue(value);
    ((JLabel)c).setText(formattedBox.getText());

    Font f = gridController.getFont(row,table.getModel().getColumnName(table.convertColumnIndexToModel(column)),value,defaultFont);
    if (f!=null)
      c.setFont(f);
    else
      c.setFont(defaultFont);

    if (table instanceof Grid)
      c.setToolTipText(gridController.getCellTooltip(row,((Grid)table).getVOListTableModel().getColumnName(table.convertColumnIndexToModel(column))));

    c.setBorder(
      BorderFactory.createCompoundBorder(c.getBorder(),BorderFactory.createEmptyBorder(topMargin,leftMargin,bottomMargin,rightMargin))
    );
    return c;
  }


  public final void finalize() {
    gridController = null;
  }


}
