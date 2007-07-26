package org.openswing.swing.table.editors.client;

import java.awt.*;
import java.text.*;
import javax.swing.*;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;
import javax.swing.tree.*;
import java.util.Date;
import java.text.DateFormat;
import org.openswing.swing.client.*;
import org.openswing.swing.client.*;
import org.openswing.swing.client.*;

import org.openswing.swing.table.columns.client.Column;
import java.awt.event.*;
import org.openswing.swing.table.client.Grid;
import org.openswing.swing.util.client.*;


/**
 * <p>Title: OpenSwing Framework</p>
 * <p>Description: Column editor used to edit a date/date+time/time type cell.</p>
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
public class DateCellEditor extends AbstractCellEditor implements TableCellEditor {

  /** date input field: it contains a date editor + calendar button */
  private DateControl field = new DateControl() {

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

  /** flag used to set mandatory property of the cell */
  private boolean required;

  /** grid reference, used to reset focus after cell editing */
  private JTable table = null;

  /** selected row in the grid */
  private int selectedRow = -1;

  /** selected column in the grid */
  private int selectedCol = -1;


  /**
   * Constructor.
   * @param required flag used to set mandatory property of the cell
   * @param columnType column type; possible values: Column.TYPE_DATE, Column.TYPE_TIME, Column.TYPE_DATE_TIME
   */
  public DateCellEditor(boolean required,int columnType) {
    this.required = required;
    field.setDateType(columnType);
    field.getDateField().addKeyListener(new KeyAdapter() {

      public void keyPressed(KeyEvent e) {
        if (e.getKeyCode()==e.VK_TAB || e.getKeyCode()==e.VK_ENTER) {
          stopCellEditing();
          if (table!=null) {
            new Thread() {
              public void run() {
                yield();
                try {
                  sleep(500);
                }
                catch (Exception ex) {
                }
                table.requestFocus();
                try {
                  table.setColumnSelectionInterval(selectedCol + 1,selectedCol + 1);
                }
                catch (Exception ex1) {
                }
              }
            }.start();
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
    ((DateControl)field).focusLost(null);
    return(validate());
  }


  /**
   * Perform the validation.
   */
  private boolean validate() {
    fireEditingStopped();
    if (table!=null)
      table.requestFocus();
    return true;
  }


  public final Object getCellEditorValue() {
    return ((DateControl)field).getDate();
  }


  /**
   * Prepare the editor for a value.
   */
  private Component _prepareEditor(Object value) {
    if (value!=null && value instanceof Date) {
      field.setDate((Date)value);
    } else {
     field.setDate(null);
    }

    return field;
  }


  public final Component getTableCellEditorComponent(JTable table, Object value,
                                               boolean isSelected, int row,
                                               int column) {
    this.table = table;
    this.selectedRow = row;
    this.selectedCol = column;
    JComponent c = (JComponent)_prepareEditor(value);
    new Thread() {
      public void run() {
        yield();
        field.getDateField().requestFocus();
      }
    }.start();
    if (required) {
//      field.getDateField().setBorder(BorderFactory.createLineBorder(Consts.GRID_REQUIRED_CELL_BORDER));
      field.setBorder(BorderFactory.createLineBorder(ClientSettings.GRID_REQUIRED_CELL_BORDER));
//      field.setBorder(new CompoundBorder(new RequiredBorder(),field.getBorder()));
    }
    return c;
  }


  public Component getTreeCellEditorComponent(JTree tree, Object value,
                                              boolean isSelected,
                                              boolean expanded, boolean leaf,
                                              int row) {
    return(_prepareEditor(value));
  }

}
