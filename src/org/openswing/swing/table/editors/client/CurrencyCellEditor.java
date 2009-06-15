package org.openswing.swing.table.editors.client;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.table.*;

import org.openswing.swing.client.*;
import org.openswing.swing.table.columns.client.*;
import org.openswing.swing.util.client.*;


/**
 * <p>Title: OpenSwing Framework</p>
 * <p>Description: Column editor used for currency type columns.</p>
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
public class CurrencyCellEditor extends AbstractCellEditor implements TableCellEditor {

  /** flag used to set mandatory property of the cell */
  private boolean required;

  /** numeric control */
  protected CurrencyControl field;

  /** dynamic settings used to reset numeric editor properties for each grid row */
  private IntegerColumnSettings dynamicSettings = null;

  /** table */
  private JTable table = null;

  /** current selected row */
  private int row = -1;

  /** current selected column */
  private int col = -1;

  /** flag used in grid to automatically select data in cell when editing cell; default value: ClientSettings.SELECT_DATA_IN_EDIT; <code>false</code>to do not select data stored cell; <code>true</code> to automatically select data already stored in cell */
  private boolean selectDataOnEdit = ClientSettings.SELECT_DATA_IN_EDITABLE_GRID;


  /**
   * Constructor.
   * @param colType column type; possible values: Column.TYPE_INT, Column.TYPE_DEC, Column.TYPE_CURRENCY, Column.TYPE_PERC
   * @param decimals number of decimals
   * @param required flag used to set mandatory property of the cell
   * @param currencySymbolOnLeft flag used to define the default position of currency symbol in currency control/column: on the left or on the right of the numeric value
   * @param currencySymbol currency symbol
   * @param dynamicSettings dynamic settings (object of type CurrencyColumnSettings) used to reset currency editor properties for each grid row
   */
  public CurrencyCellEditor(int colType, int decimals, boolean required, boolean currencySymbolOnLeft, double minValue, double maxValue,String currencySymbol,IntegerColumnSettings dynamicSettings,boolean selectDataOnEdit) {
    field = new CurrencyControl() {

        public boolean processKeyBinding(KeyStroke ks, KeyEvent e,
                                            int condition, boolean pressed) {
          if (e.getSource()!=null && e.getSource() instanceof org.openswing.swing.table.client.Grid) {
            field.processKeyEvent(e);
          }
          else if (e.getKeyChar()=='\t' || e.getKeyChar()=='\n')
            stopCellEditing();
          return true;
        }

    };

    field.setCurrencySymbol(currencySymbol);
    field.setCurrencySymbolOnLeft(currencySymbolOnLeft);

    this.field.setDecimals(decimals);
    this.required = required;
    this.dynamicSettings = dynamicSettings;
    this.selectDataOnEdit = selectDataOnEdit;
    field.setMinValue(minValue);
    field.setMaxValue(maxValue);
    field.addKeyListener(new KeyAdapter() {

      public void keyPressed(KeyEvent e) {
        if (e.getKeyCode()==e.VK_TAB) {
          stopCellEditing();
          table.requestFocus();
          try {
            table.setColumnSelectionInterval(col + 1, col + 1);
          }
          catch (Exception ex) {
          }
        }
      }

    });
  }


  public Component getTableCellEditorComponent(JTable table, Object value,
                                               boolean isSelected, int row,
                                               int column) {

    this.table = table;
    this.row = row;
    this.col = column;

    JComponent c = (JComponent)_prepareEditor(value);
    if (required) {
      c.setBorder(BorderFactory.createLineBorder(ClientSettings.GRID_REQUIRED_CELL_BORDER));
//      c.setBorder(new CompoundBorder(new RequiredBorder(),c.getBorder()));
    }
    return c;
  }


  /**
   * Stop cell editing. This method stops cell editing (effectively committing the edit) only if the data entered is validated successfully.
   * @return <code>true</code> if cell editing may stop, and <code>false</code> otherwise.
   */
  public final boolean stopCellEditing() {
    return validate();
  }


  /**
   * Perform the validation.
   */
  private boolean validate() {
//    boolean ok = field.validateInput();
    boolean ok = true;
    if(ok)
      fireEditingStopped();
    return ok;
  }


  /**
   * @return current value, as a <code>Double</code>
   */
  public Object getCellEditorValue() {
//    field.validateInput();
    if (field.getText()==null || field.getText().length()==0)
      return null;
    return field.getValue();
  }


  /**
   * Prepare the editor for a value.
   */
  private Component _prepareEditor(Object value) {
    if (row!=-1 && dynamicSettings!=null) {
      if (dynamicSettings instanceof DecimalColumnSettings)
        field.setDecimals(((DecimalColumnSettings)dynamicSettings).getDecimals(row));
      field.setMinValue(dynamicSettings.getMinValue(row));
      field.setMaxValue(dynamicSettings.getMaxValue(row));
      if (dynamicSettings instanceof CurrencyColumnSettings)
        field.setCurrencySymbol(((CurrencyColumnSettings)dynamicSettings).getCurrencySymbol(row));
    }
    if(value!=null && value.getClass().getSuperclass() == Number.class) {
      field.setText(((Number)value).toString());

      if (selectDataOnEdit)
        field.select(0,field.getText().length());

    }
    else
      field.setText(null);
    return field;
  }


  public Component getTreeCellEditorComponent(JTree tree, Object value,
                                              boolean isSelected,
                                              boolean expanded, boolean leaf,
                                              int row) {
    this.row = row;
    return _prepareEditor(value);
  }


  public final void finalize() {
    dynamicSettings = null;
    table = null;
    field = null;
  }



}
