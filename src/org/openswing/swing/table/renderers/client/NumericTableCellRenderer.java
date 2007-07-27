package org.openswing.swing.table.renderers.client;

import java.awt.*;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import javax.swing.*;
import javax.swing.table.*;
import java.math.BigDecimal;
import org.openswing.swing.client.*;
import org.openswing.swing.table.client.*;
import org.openswing.swing.client.*;

import org.openswing.swing.table.columns.client.Column;
import org.openswing.swing.util.client.*;
import org.openswing.swing.internationalization.java.*;
import org.openswing.swing.util.java.*;
import org.openswing.swing.table.columns.client.IntegerColumnSettings;
import org.openswing.swing.table.columns.client.DecimalColumnSettings;
import org.openswing.swing.table.columns.client.CurrencyColumnSettings;


/**
 * <p>Title: OpenSwing Framework</p>
 * <p>Description: Column renderer used to format a numeric type column.</p>
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
public class NumericTableCellRenderer extends DefaultTableCellRenderer {

  /** representation of the "unknown value"; a value that is either of the wrong type or for which there is no available format */
  public static final String VALUE_UNKNOWN = "???";

  /** flag used to enable grouping */
  private boolean grouping;

  /** number of decimals */
  private int decimals;

  /** grid controller */
  private GridController gridController = null;

  /** cell format */
  protected DecimalFormat format = null;

  /** dynamic settings used to reset numeric renderer properties for each grid row */
  private IntegerColumnSettings dynamicSettings = null;

  /** current editing row*/
  private int row = -1;

  /** default font */
  private Font defaultFont = null;


  /**
   * Constructor.
   * @param decimals number of decimals
   * @param grouping flag used to enable grouping
   * @param gridController grid controller
   * @param dynamicSettings dynamic settings used to reset numeric editor properties for each grid row
   */
  public NumericTableCellRenderer(int decimals,boolean grouping,GridController gridController,IntegerColumnSettings dynamicSettings,int alignement) {
    this.decimals = decimals;
    this.grouping = false;
    this.gridController = gridController;
    this.dynamicSettings = dynamicSettings;
    setHorizontalAlignment(alignement);
    setFormat(decimals,grouping);
  }


  /**
   * Set cell format.
   */
  protected void setFormat(int decimals, boolean grouping) {
    // decimal cell format...
    DecimalFormatSymbols dfs = new DecimalFormatSymbols();
    dfs.setGroupingSeparator(ClientSettings.getInstance().getResources().getGroupingSymbol());
    dfs.setDecimalSeparator(ClientSettings.getInstance().getResources().getDecimalSymbol());

    if (!grouping && decimals==0)
      format = new DecimalFormat("#");
    else if (grouping && decimals==0)
      format = new DecimalFormat("#,###",dfs);
    else if (grouping && decimals>0) {
      String dec = "";
      for(int i=0;i<decimals;i++)
        dec += "#";
      format = new DecimalFormat("#,###."+dec,dfs);
    } else if (!grouping && decimals>0) {
      String dec = "";
      for(int i=0;i<decimals;i++)
        dec += "#";
      format = new DecimalFormat("#."+dec,dfs);
    }
    format.setGroupingUsed(grouping);
  }



  public Component getTableCellRendererComponent(JTable table, Object value,
                          boolean isSelected, boolean hasFocus, int row, int column) {
    this.row = row;
    JComponent c = (JComponent)super.getTableCellRendererComponent(table, value,isSelected, hasFocus, row, column);

    if (defaultFont==null)
      defaultFont = ((JLabel)c).getFont();

    if (isSelected && !hasFocus) {
      ((JLabel)c).setForeground(table.getSelectionForeground());
//      c.setBackground(table.getSelectionBackground());
      Color backColor = gridController.getBackgroundColor(row,table.getModel().getColumnName(table.convertColumnIndexToModel(column)),value);
      Color selColor = table.getSelectionBackground();
      c.setBackground(new Color(
          (backColor.getRed()+selColor.getRed())/2,
          (backColor.getGreen()+selColor.getGreen())/2,
          (backColor.getBlue()+selColor.getBlue())/2
      ));
    }
    else {
      c.setBackground(table.getBackground());
      ((JLabel)c).setForeground(table.getForeground());
    }
    if (hasFocus && table instanceof Grid) {
//      c.setBackground(((Grid)table).getActiveCellBackgroundColor());
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
      Color backColor = gridController.getBackgroundColor(row,table.getModel().getColumnName(table.convertColumnIndexToModel(column)),value);
      c.setBackground(new Color(
          (backColor.getRed()+selColor.getRed())/2,
          (backColor.getGreen()+selColor.getGreen())/2,
          (backColor.getBlue()+selColor.getBlue())/2,
          200
      ));
      c.setBorder(BorderFactory.createLineBorder(table.getSelectionForeground()));
    }
    else if (isSelected && !hasFocus) {
      c.setForeground(table.getSelectionForeground());
//      c.setBackground(table.getSelectionBackground());
      Color backColor = gridController.getBackgroundColor(row,table.getModel().getColumnName(table.convertColumnIndexToModel(column)),value);
//      Color selColor = table.getSelectionBackground();
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
      if (((Grid)table).getMode()==Consts.READONLY)
        c.setBackground(gridController.getBackgroundColor(row,table.getModel().getColumnName(table.convertColumnIndexToModel(column)),value));
      else {
        if (table.isCellEditable(row,column))
          c.setBackground(ClientSettings.GRID_EDITABLE_CELL_BACKGROUND);
        else
          c.setBackground(ClientSettings.GRID_NOT_EDITABLE_CELL_BACKGROUND);
      }
    }


    Font f = gridController.getFont(row,table.getModel().getColumnName(table.convertColumnIndexToModel(column)),value,defaultFont);
    if (f!=null)
      c.setFont(f);
    else
      c.setFont(defaultFont);

    if (table instanceof Grid)
      c.setToolTipText(gridController.getCellTooltip(row,((Grid)table).getVOListTableModel().getColumnName(table.convertColumnIndexToModel(column))));

    return c;
  }


  protected void setValue(Object value) {
    double val = 0.0;
    String s = VALUE_UNKNOWN;

    if (value==null) {
      setText(null);
      return;
    }


    if(value.getClass() == Double.class) {
      val = ((Double)value).doubleValue();
    }
    else if(value.getClass() == Long.class) {
      val = (double)((Long)value).longValue();
    }
    else if(value.getClass() == Integer.class) {
      val = ((Integer)value).doubleValue();
    }
    else if(value.getClass() == BigDecimal.class) {
      val = (double)((java.math.BigDecimal)value).doubleValue();
    }
    else {
      setText(VALUE_UNKNOWN);
      return;
    }

    if (row!=-1 && dynamicSettings!=null) {
      // reset numeric formatter...
      if (this instanceof CurrencyTableCellRenderer && dynamicSettings instanceof CurrencyColumnSettings)
        ((CurrencyTableCellRenderer)this).setCurrencySymbol( ((CurrencyColumnSettings)dynamicSettings).getCurrencySymbol(row) );

      if (dynamicSettings instanceof DecimalColumnSettings)
        setFormat(((DecimalColumnSettings)dynamicSettings).getDecimals(row),dynamicSettings.isGrouping(row));
      else
        setFormat(0,dynamicSettings.isGrouping(row));
    }


    s = format.format(val);
    setText(s);
   }



}