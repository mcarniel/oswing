package org.openswing.swing.table.renderers.client;

import java.util.*;

import java.awt.*;
import javax.swing.*;
import javax.swing.table.*;

import org.openswing.swing.client.*;
import org.openswing.swing.table.client.*;
import org.openswing.swing.table.columns.client.*;
import org.openswing.swing.util.client.*;
import org.openswing.swing.util.java.*;


/**
 * <p>Title: OpenSwing Framework</p>
 * <p>Description: Column renderer used to format multiple type column.</p>
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
public class MultipleTypeTableCellRenderer extends DefaultTableCellRenderer {

  /** grid controller */
  private GridController gridController = null;

  /** mutiple type controller */
  private TypeController typeController = null;

  /** current editing row*/
  private int row = -1;

  /** default font */
  private Font defaultFont = null;

  /** attribute name that identify this column */
  private String attributeName;

  /** grid control that contains this column renderer */
  private GridControl grid;

  /** cache of input controls already created and reusable according to cell's data type */
  private Hashtable inputControls = new Hashtable();

  private JPanel p = new JPanel();

  /** used instead of a combo-box */
  private JLabel l = new JLabel();

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
   * @param decimals number of decimals
   * @param grouping flag used to enable grouping
   * @param gridController grid controller
   * @param dynamicSettings dynamic settings used to reset numeric editor properties for each grid row
   * @param attributeName attribute name associated to this column
   */
  public MultipleTypeTableCellRenderer(GridController gridController,TypeController typeController,int alignement,
                                       int leftMargin,int rightMargin,int topMargin,int bottomMargin,String attributeName,GridControl grid) {
    this.gridController = gridController;
    this.typeController = typeController;
    setHorizontalAlignment(alignement);
    this.leftMargin = leftMargin;
    this.rightMargin = rightMargin;
    this.topMargin = topMargin;
    this.bottomMargin = bottomMargin;
    this.attributeName = attributeName;
    this.grid = grid;
    p.setLayout(new GridBagLayout());
  }


  /**
   * Create an input control, according to the data type for the specified row, defined in TypeController class.
   * @return input control
   */
  private InputControl prepareRenderer() {
    InputControl ic = typeController.getAdditionalProperties(row,attributeName,grid);
    if (ic!=null) {
      return ic;
    }
    else {
      int type = typeController.getCellType(row,attributeName,grid);
      ic = (InputControl)inputControls.get(new Integer(type));
      if (ic==null) {
        switch(type) {
          case Column.TYPE_TEXT: ic = new TextControl(); break;
          case Column.TYPE_DATE: ic = new DateControl(); break;
          case Column.TYPE_DATE_TIME: ic = new DateControl(); ((DateControl)ic).setDateType(Consts.TYPE_DATE_TIME); break;
          case Column.TYPE_TIME: ic = new DateControl(); ((DateControl)ic).setDateType(Consts.TYPE_TIME); break;
          case Column.TYPE_INT: ic = new NumericControl(); break;
          case Column.TYPE_DEC: ic = new NumericControl(); ((NumericControl)ic).setDecimals(5); break;
          case Column.TYPE_CHECK: ic = new CheckBoxControl(); break;
          case Column.TYPE_PERC: ic = new NumericControl(); ((NumericControl)ic).setMaxValue(100); break;
          case Column.TYPE_CURRENCY: ic = new CurrencyControl(); break;
          case Column.TYPE_IMAGE: ic = new ImageControl(); break;
          case Column.TYPE_MULTI_LINE_TEXT: ic = new TextAreaControl(); break;
          default: ic = new TextControl();
        }
        inputControls.put(new Integer(type),ic);
      }
      return ic;
    }
  }


  public Component getTableCellRendererComponent(JTable table, Object value,
                          boolean isSelected, boolean hasFocus, int row, int column) {
    try {
      this.row = row;
      InputControl ic = prepareRenderer();
      ic.setValue(value);
      JComponent c = null;
      if (ic instanceof BaseInputControl)
        c = ((BaseInputControl)ic).getBindingComponent();
      else
        c = (JComponent)ic;

      if (c instanceof JComboBox) {
        l.setText(((JComboBox)c).getSelectedItem().toString());
        p.removeAll();
        p.add(l,      new GridBagConstraints(0, 0, 1, 1, 1.0, 1.0
             ,GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0,0,0,0), 0, 0));
        c = p;
      }

      if (defaultFont==null)
        defaultFont = table.getFont();

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
        if (ClientSettings.IGNORE_GRID_SELECTION_FOREGROUND)
          c.setForeground(gridController.getForegroundColor(row,table.getModel().getColumnName(table.convertColumnIndexToModel(column)),value));
        else
          c.setForeground(table.getSelectionForeground());

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

//      if (table instanceof Grid)
//        c.setToolTipText(gridController.getCellTooltip(row,((Grid)table).getVOListTableModel().getColumnName(table.convertColumnIndexToModel(column))));

      c.setBorder(
        BorderFactory.createCompoundBorder(c.getBorder(),BorderFactory.createEmptyBorder(topMargin,leftMargin,bottomMargin,rightMargin))
      );
      return c;
    }
    catch (Exception ex) {
      ex.printStackTrace();
      return new JLabel();
    }
  }


  public final void finalize() {
    gridController = null;
    grid = null;
    typeController = null;
    inputControls.clear();;
  }


}
