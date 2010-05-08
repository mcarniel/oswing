package org.openswing.swing.table.renderers.client;

import java.awt.*;
import javax.swing.*;
import javax.swing.table.*;

import org.openswing.swing.table.client.*;
import org.openswing.swing.util.client.*;
import org.openswing.swing.util.java.*;
import org.openswing.swing.client.LinkButton;
import java.awt.event.MouseListener;
import java.awt.event.MouseEvent;

/**
 * <p>Title: OpenSwing Framework</p>
 * <p>Description: Column renderer used to format a link type column.</p>
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
public class LinkTableCellRenderer extends DefaultTableCellRenderer {

  /** cell content */
  private LinkButton rend = new LinkButton();

  /** grid controller */
  private GridController gridController = null;

  /** default font */
  private Font defaultFont = null;

  /** attribute name linked to the text of this link, used to bind this link to a row of GridControl's value object */
  public String attributeName = null;

  /** attribute name linked to the text of this link, used to bind this link to a row of GridControl's value object */
  public String uriAttributeName = null;


  /**
   * Constructor.
   * @param gridController grid controller
   * @param alignement horizontal alignement
   * @param attributeName attribute name associated to this column
   */
  public LinkTableCellRenderer(GridController gridController,int alignement,String uriAttributeName,
                               ComponentOrientation orientation,String attributeName) {
    this.gridController = gridController;
    rend.setTextAlignment(alignement);
    this.uriAttributeName = uriAttributeName;
    this.attributeName = attributeName;

    if (orientation!=null)
        setComponentOrientation(orientation);
  }


  public Component getTableCellRendererComponent(JTable table, Object value,
                          boolean isSelected, boolean hasFocus, int row, int column) {

    if (defaultFont==null)
      defaultFont = rend.getFont();
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
//      rend.setBorder(BorderFactory.createLineBorder(table.getSelectionForeground()));
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

    } else {
      rend.setForeground(gridController.getForegroundColor(row,table.getModel().getColumnName(table.convertColumnIndexToModel(column)),value));
//      if (!enableInReadOnlyMode && (((Grid)table).getMode()==Consts.READONLY || !((Grid)table).isColorsInReadOnlyMode()))
//        rend.setBackground(gridController.getBackgroundColor(row,table.getModel().getColumnName(table.convertColumnIndexToModel(column)),value));
//      else {
//        if (enableInReadOnlyMode || table.isCellEditable(row,column)) {
//          rend.setBackground(gridController.getBackgroundColor(row,table.getModel().getColumnName(table.convertColumnIndexToModel(column)),value));
//        }
//        else
//          rend.setBackground(ClientSettings.GRID_NOT_EDITABLE_CELL_BACKGROUND);
//      }

      if (table.isCellEditable(row,column)) {
        rend.setBackground(gridController.getBackgroundColor(row,table.getModel().getColumnName(table.convertColumnIndexToModel(column)),value));
      }
      else
        rend.setBackground(ClientSettings.GRID_NOT_EDITABLE_CELL_BACKGROUND);
    }

    Font f = gridController.getFont(row,table.getModel().getColumnName(table.convertColumnIndexToModel(column)),value,defaultFont);
    if (f!=null)
      rend.setFont(f);
    else
      rend.setFont(defaultFont);

    if (table instanceof Grid) {
      rend.setToolTipText(gridController.getCellTooltip(row,((Grid)table).getVOListTableModel().getColumnName(table.convertColumnIndexToModel(column))));
      rend.setLabel( (String)((Grid)table).getVOListTableModel().getField(row,attributeName) );
      rend.setUri( (String)((Grid)table).getVOListTableModel().getField(row,uriAttributeName) );

    }
    return rend;
  }


  public final void finalize() {
    gridController = null;
  }


}


