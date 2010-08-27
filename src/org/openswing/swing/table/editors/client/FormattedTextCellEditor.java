package org.openswing.swing.table.editors.client;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.table.*;

import org.openswing.swing.util.client.*;
import java.text.*;
import java.lang.reflect.Proxy;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.*;
import org.openswing.swing.table.columns.client.FormattedTextColumn;

/**
 * <p>Title: OpenSwing Framework</p>
 * <p>Description: Column editor used for formatted text type columns..</p>
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
public class FormattedTextCellEditor extends AbstractCellEditor implements TableCellEditor {

  /** text input field */
  private JFormattedTextField field;

  /** flag used to set mandatory property of the cell */
  private boolean required;

  /** table */
  private JTable table = null;

  /** current selected row */
  private int row = -1;

  /** current selected column */
  private int col = -1;


  /**
   * Constructor used for password fields.
   */
  public FormattedTextCellEditor(final JFormattedTextField _field,boolean required,final FormattedTextColumn column) {
/*
    try {
      InvocationHandler handler = new InvocationHandler() {
        public Object invoke(Object proxy, Method method, Object[] args) {
          try {
            System.out.println(method.getName()+": "+(args!=null && args.length>0?args[0]:null));
            return method.invoke(_field, args);
          }
          catch (Throwable ex) {
            return null;
          }
        }
      };
      Class proxyClass = Proxy.getProxyClass(JFormattedTextField.class.getClassLoader(), new Class[] {JFormattedTextField.class});
      this.field = (JFormattedTextField) proxyClass.getConstructor(new Class[] {InvocationHandler.class}).newInstance(new Object[] {handler});
    }
    catch (Throwable ex) {
      this.field = _field;
    }
*/
    //this.field = new JFormattedTextField( _field.getFormatter());
    this.field = _field;
    this.required = required;
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
        else
        if (e.getKeyCode()==e.VK_UP) {
          stopCellEditing();
          //table.requestFocus();
          try {
            e.consume();
//            if (table.getSelectedRow()>0)
//              table.setRowSelectionInterval(table.getSelectedRow()-1,table.getSelectedRow()-1);
            table.editCellAt(table.getSelectedRow(),table.getSelectedColumn());
          }
          catch (Exception ex) {
          }
        }
        else if (e.getKeyCode()==e.VK_DOWN) {
          stopCellEditing();
          //table.requestFocus();
          try {
            e.consume();
//            if (table.getSelectedRow()<table.getRowCount())
//              table.setRowSelectionInterval(table.getSelectedRow()+1,table.getSelectedRow()+1);
            table.editCellAt(table.getSelectedRow(),table.getSelectedColumn());
          }
          catch (Exception ex) {
          }
        }
        else if (e.getKeyCode()==e.VK_ENTER) {
          stopCellEditing();
          table.requestFocus();
        }
        else if (e.getKeyCode()==e.VK_F2) {
          stopCellEditing();
          table.editCellAt(table.getSelectedRow(),table.getSelectedColumn());
        }
      }

    });

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
    boolean ok = true;
    try {
      field.setValue(field.getFormatter().stringToValue(field.getText()));
    }
    catch (ParseException ex) {
//      ok = false;
      field.setValue(null);
    }
    if (field.getText()!=null && field.getValue()==null)
      ok = false;
    if(!ok)
      field.setText(null);
    fireEditingStopped();


    return ok;
  }


  public Object getCellEditorValue() {
    return field.getValue();
  }


  public Component getTableCellEditorComponent(JTable table, Object value,
                                               boolean isSelected, int row,
                                               int column) {

    this.table = table;
    this.row = row;
    this.col = column;

//    try {
//      field.commitEdit();
//    }
//    catch (ParseException ex) {
//    }
    field.setValue(value);
    if (required) {
      field.setBorder(BorderFactory.createLineBorder(ClientSettings.GRID_REQUIRED_CELL_BORDER));
//      field.setBorder(new CompoundBorder(new RequiredBorder(),field.getBorder()));
    }

    return field;
  }


  public final void finalize() {
    table = null;
    field = null;
  }


}


