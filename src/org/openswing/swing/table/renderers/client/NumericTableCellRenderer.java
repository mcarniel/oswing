package org.openswing.swing.table.renderers.client;

import java.math.*;
import java.text.*;

import java.awt.*;
import javax.swing.*;
import javax.swing.table.*;

import org.openswing.swing.table.client.*;
import org.openswing.swing.table.columns.client.*;
import org.openswing.swing.util.client.*;
import org.openswing.swing.util.java.*;
import org.openswing.swing.table.model.client.VOListTableModel;


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
public class NumericTableCellRenderer implements TableCellRenderer {

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

  /** current JTable */
  private Grid grid = null;

  /** attribute name associated to this column */
  private String attributeName = null;

  /** flag used to define whether zero digits (after decimal point) must be hided/showed */
  protected boolean hideZeroDigits = false;

  /** component left margin, with respect to component container */
  protected int leftMargin = 0;

  /** component right margin, with respect to component container */
  protected int rightMargin = 0;

  /** component top margin, with respect to component container */
  protected int topMargin = 0;

  /** component bottom margin, with respect to component container */
  protected int bottomMargin = 0;

  /** cell content; used to support Substance LnF with editable cells in INSERT mode */
  private JTextField c = new JTextField();

  /**
   * Constructor.
   * @param decimals number of decimals
   * @param grouping flag used to enable grouping
   * @param hideZeroDigits flag used to define whether zero digits (after decimal point) must be hided/showed
   * @param gridController grid controller
   * @param dynamicSettings dynamic settings used to reset numeric editor properties for each grid row
   * @param attributeName attribute name associated to this column
   */
   public NumericTableCellRenderer(int decimals,boolean grouping,boolean hideZeroDigits,GridController gridController,
                                   IntegerColumnSettings dynamicSettings,int alignement,
                                   int leftMargin,int rightMargin,int topMargin,int bottomMargin,String attributeName) {
    this.decimals = decimals;
    this.grouping = false;
    this.gridController = gridController;
    this.dynamicSettings = dynamicSettings;
    this.attributeName = attributeName;
    this.hideZeroDigits = hideZeroDigits;
    this.leftMargin = leftMargin;
    this.rightMargin = rightMargin;
    this.topMargin = topMargin;
    this.bottomMargin = bottomMargin;
    c.setHorizontalAlignment(alignement);
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
        dec += hideZeroDigits?"#":"0";
      format = new DecimalFormat("#,##0."+dec,dfs);
    } else if (!grouping && decimals>0) {
      String dec = "";
      for(int i=0;i<decimals;i++)
        dec += hideZeroDigits?"#":"0";
      format = new DecimalFormat("0."+dec,dfs);
    }
    format.setGroupingUsed(grouping);
  }



  public Component getTableCellRendererComponent(JTable table, Object value,
                          boolean isSelected, boolean hasFocus, int row, int column) {
    this.row = row;
    this.grid = (Grid)table;

    setValue(value);
    //JComponent c = (JComponent)super.getTableCellRendererComponent(table, value,isSelected, hasFocus, row, column);

    if (defaultFont==null)
      defaultFont = c.getFont();

    if (isSelected && !hasFocus) {
      if (ClientSettings.IGNORE_GRID_SELECTION_FOREGROUND)
        c.setForeground(gridController.getForegroundColor(row,table.getModel().getColumnName(table.convertColumnIndexToModel(column)),value));
      else
        c.setForeground(table.getSelectionForeground());
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
      c.setForeground(table.getForeground());
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
      if (ClientSettings.IGNORE_GRID_SELECTION_FOREGROUND)
        c.setForeground(gridController.getForegroundColor(row,table.getModel().getColumnName(table.convertColumnIndexToModel(column)),value));
      else
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
      if (((Grid)table).getMode()==Consts.READONLY || !((Grid)table).isColorsInReadOnlyMode())
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
    c.setBorder(
      BorderFactory.createCompoundBorder(c.getBorder(),BorderFactory.createEmptyBorder(topMargin,leftMargin,bottomMargin,rightMargin))
    );

    c.setMinimumSize(new Dimension(grid.getColumnModel().getColumn(column).getWidth(), grid.getRowHeight(row)));
    c.setPreferredSize(new Dimension(grid.getColumnModel().getColumn(column).getWidth(), grid.getRowHeight(row)));
    c.setSize(grid.getColumnModel().getColumn(column).getWidth(), grid.getRowHeight(row));

    return c;
  }


  protected void setValue(Object value) {
    double val = 0.0;
    String s = VALUE_UNKNOWN;

    if (value==null) {
      c.setText(null);
      return;
    }


    if(value.getClass() == Double.class || value.getClass() == Double.TYPE) {
      val = ((Double)value).doubleValue();
    }
    else if(value.getClass() == Float.class || value.getClass() == Float.TYPE) {
      val = ((Float)value).doubleValue();
    }
    else if(value.getClass() == Short.class || value.getClass() == Short.TYPE) {
      val = ((Short)value).doubleValue();
    }
    else if(value.getClass() == Long.class || value.getClass() == Long.TYPE) {
      val = (double)((Long)value).longValue();
    }
    else if(value.getClass() == Integer.class || value.getClass() == Integer.TYPE) {
      val = ((Integer)value).doubleValue();
    }
    else if(value.getClass() == BigDecimal.class) {
      val = (double)((java.math.BigDecimal)value).doubleValue();
    }
    else {
      c.setText(VALUE_UNKNOWN);
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
    c.setText(s);
   }


   public final void finalize() {
     gridController = null;
     grid = null;
     dynamicSettings = null;
   }

}
