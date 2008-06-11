package org.openswing.swing.table.editors.client;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.table.*;

import org.openswing.swing.client.*;
import org.openswing.swing.util.client.*;


/**
 * <p>Title: OpenSwing Framework</p>
 * <p>Description: Column editor used for multi line text type columns..</p>
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
public class MultiLineTextCellEditor extends AbstractCellEditor implements TableCellEditor {

  /** multi line text input field */
  private TextAreaControl field = new TextAreaControl() {

    private KeyEvent oldEv = null;

      public boolean processKeyBinding(KeyStroke ks, KeyEvent e,
                                          int condition, boolean pressed) {
        if (e.getSource()!=null && e.getSource() instanceof org.openswing.swing.table.client.Grid) {
          try {
            if (oldEv==null || !e.equals(oldEv)) {
              oldEv = e;
              field.processKeyEvent(e);
              oldEv = null;
            }
          }
          catch (Exception ex) {
          }
        }
        else if (e.getKeyChar()=='\t' || e.getKeyChar()=='\n')
          stopCellEditing();
         return true;
      }

  };

  /** maximum number of characters */
  private int maxCharacters = -1;

  /** flag used to set mandatory property of the cell */
  private boolean required;

  /** table */
  private JTable table = null;

  /** current selected row */
  private int row = -1;

  /** current selected column */
  private int col = -1;


  public MultiLineTextCellEditor(int maxCharacters,boolean required) {
    this.required = required;
    this.maxCharacters = maxCharacters;
    ((JTextArea)field.getBindingComponent()).setLineWrap(true);
    ((JTextArea)field.getBindingComponent()).setWrapStyleWord(true);
    ((JTextArea)field.getBindingComponent()).setOpaque(true);

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
    boolean ok;
    String text = (String)field.getValue();
    if (text==null)
      text = "";
    ok = field==null || text.length()<=maxCharacters;
    if(ok)
      fireEditingStopped();

    return ok;
  }


  public Object getCellEditorValue() {
    String text = (String)field.getValue();
    if (text==null)
      text = "";
    if (field!=null && text.length()>maxCharacters)
      field.setText(text.substring(0,maxCharacters));
    text = (String)field.getValue();
    if (text==null)
      text = "";
    return text;
  }


  public Component getTableCellEditorComponent(JTable table, Object value,
                                               boolean isSelected, int row,
                                               int column) {

    this.table = table;
    this.row = row;
    this.col = column;

    field.setText((String)value);
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


