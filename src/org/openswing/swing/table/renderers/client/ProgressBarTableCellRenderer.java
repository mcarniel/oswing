package org.openswing.swing.table.renderers.client;

import java.util.*;

import java.awt.*;
import javax.swing.*;
import javax.swing.table.*;

import org.openswing.swing.miscellaneous.client.*;
import org.openswing.swing.table.client.*;
import org.openswing.swing.util.client.*;
import org.openswing.swing.util.java.*;


/**
 * <p>Title: OpenSwing Framework</p>
 * <p>Description: Renderer used to show a progress bar inside the column's cell.</p>
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
public class ProgressBarTableCellRenderer extends DefaultTableCellRenderer {


  /** grid container */
  private GridController gridContainer = null;

  private ArrayList coloredBands;
  private double minValue;
  private double maxValue;
  private boolean showAllBands;
  private Color currentColor;

  /** attribute name associated to this column */
  private String attributeName = null;


  /**
   * Constructor.
   * @param gridContainer grid container
   * @param attributeName attribute name associated to this column
   */
  public ProgressBarTableCellRenderer(
      GridController gridContainer,
      ArrayList coloredBands,
      double minValue,
      double maxValue,
      boolean showAllBands,
      Color currentColor,
      String attributeName
  ) {
    this.gridContainer = gridContainer;
    this.coloredBands = coloredBands;
    this.minValue = minValue;
    this.maxValue = maxValue;
    this.showAllBands = showAllBands;
    this.currentColor = currentColor;
    this.attributeName = attributeName;
  }


  public Component getTableCellRendererComponent(JTable table, Object value,
                        boolean isSelected, boolean hasFocus, int row, int column) {
    ProgressBar rend = new ProgressBar();
    rend.setColor(currentColor);
    rend.setShowAllBands(showAllBands);
    rend.setMinValue(minValue);
    rend.setMaxValue(maxValue);
    Object[] band = null;
    for(int i=0;i<coloredBands.size();i++) {
      band = (Object[])coloredBands.get(i);
      rend.addColoredBand(
        ((Double)band[0]).doubleValue(),
        ((Double)band[1]).doubleValue(),
        (Color)band[2]
      );
    }
//    rend.setOpaque(true);
    if (value!=null && value instanceof Number)
      rend.setValue(  ((Number)value).doubleValue() );
    rend.repaint();

    if (hasFocus && table instanceof Grid) {
//      rend.setBackground(((Grid)table).getActiveCellBackgroundColor());
//      Color selColor = ((Grid)table).getActiveCellBackgroundColor();
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
      Color backColor = gridContainer.getBackgroundColor(row,table.getModel().getColumnName(table.convertColumnIndexToModel(column)),value);
      rend.setBackground(new Color(
          (backColor.getRed()+selColor.getRed())/2,
          (backColor.getGreen()+selColor.getGreen())/2,
          (backColor.getBlue()+selColor.getBlue())/2,
          200
      ));
    } else if (isSelected && !hasFocus) {
      if (ClientSettings.IGNORE_GRID_SELECTION_FOREGROUND)
        rend.setForeground(gridContainer.getForegroundColor(row,table.getModel().getColumnName(table.convertColumnIndexToModel(column)),value));
      else
        rend.setForeground(table.getSelectionForeground());

//      rend.setBackground(table.getSelectionBackground());
      Color backColor = gridContainer.getBackgroundColor(row,table.getModel().getColumnName(table.convertColumnIndexToModel(column)),value);
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
      if (((Grid)table).getMode()==Consts.READONLY || !((Grid)table).isColorsInReadOnlyMode())
        rend.setBackground(gridContainer.getBackgroundColor(row,table.getModel().getColumnName(table.convertColumnIndexToModel(column)),value));
      else {
        if (table.isCellEditable(row,column))
          rend.setBackground(ClientSettings.GRID_EDITABLE_CELL_BACKGROUND);
        else
          rend.setBackground(ClientSettings.GRID_NOT_EDITABLE_CELL_BACKGROUND);
      }
    }

    rend.setPreferredSize(new Dimension(table.getColumnModel().getColumn(column).getWidth(),table.getHeight()));

    if (table instanceof Grid)
      rend.setToolTipText(gridContainer.getCellTooltip(row,((Grid)table).getVOListTableModel().getColumnName(table.convertColumnIndexToModel(column))));

    return rend;
  }


  public final void finalize() {
    gridContainer = null;
  }


}
