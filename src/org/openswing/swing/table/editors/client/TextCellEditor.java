package org.openswing.swing.table.editors.client;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.table.*;

import org.openswing.swing.client.*;
import org.openswing.swing.util.client.*;


/**
 * <p>Title: OpenSwing Framework</p>
 * <p>Description: Column editor used for text type columns..</p>
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
public class TextCellEditor extends AbstractCellEditor implements TableCellEditor {

  /** text input field */
  private TextControl field = new TextControl() {

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


  /** password text input field */
  private JPasswordField passwdField = new JPasswordField();

  /** maximum number of characters */
  private int maxCharacters = -1;

  /** flag used to set mandatory property of the cell */
  private boolean required;

  /** flag used to encrypt text */
  private boolean encryptText;

  /** table */
  private JTable table = null;

  /** current selected row */
  private int row = -1;

  /** current selected column */
  private int col = -1;

  /** flag used in grid to automatically select data in cell when editing cell; default value: ClientSettings.SELECT_DATA_IN_EDIT; <code>false</code>to do not select data stored cell; <code>true</code> to automatically select data already stored in cell */
  private boolean selectDataOnEdit = ClientSettings.SELECT_DATA_IN_EDITABLE_GRID;


  /**
   * Constructor used for password fields.
   */
  public TextCellEditor(int maxCharacters,boolean required) {
    this.required = required;
    this.maxCharacters = maxCharacters;
    this.encryptText = true;
  }


  public TextCellEditor(int maxCharacters,boolean required,boolean rPadding,boolean trimText,boolean upperCase,
                        boolean selectDataOnEdit,ComponentOrientation orientation) {
    this.required = required;
    this.maxCharacters = maxCharacters;
    this.selectDataOnEdit = selectDataOnEdit;
    field.setColumns(maxCharacters);
    field.setMaxCharacters(maxCharacters);
    field.setRpadding(rPadding);
    field.setTrimText(trimText);
    field.setUpperCase(upperCase);

    if (orientation!=null)
      field.setComponentOrientation(orientation);

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
    if (!encryptText) {
      ok = field==null || field.getText().length()<=maxCharacters;
    }
    else {
      ok = passwdField.getText().length()<=maxCharacters;
    }

    if(ok)
      fireEditingStopped();

    return ok;
  }


  public Object getCellEditorValue() {
    if (!encryptText) {
      if (field!=null && field.getText().length()>maxCharacters)
        field.setText(field.getText().substring(0,maxCharacters));
      return field.getText();
    }
    else {
      if (passwdField.getText().length()>maxCharacters)
        passwdField.setText(passwdField.getText().substring(0,maxCharacters));
      return passwdField.getText();
    }
  }


  public Component getTableCellEditorComponent(JTable table, Object value,
                                               boolean isSelected, int row,
                                               int column) {

    this.table = table;
    this.row = row;
    this.col = column;

    if (!encryptText) {
      field.setText((String)value);

      if (selectDataOnEdit)
        field.select(0,field.getText().length());

      if (required) {
        field.setBorder(BorderFactory.createLineBorder(ClientSettings.GRID_REQUIRED_CELL_BORDER));
//      field.setBorder(new CompoundBorder(new RequiredBorder(),field.getBorder()));
      }

      return field;
    }
    else {
      passwdField.setText((String)value);

      if (selectDataOnEdit)
        field.select(0,field.getText().length()-1);

      if (required) {
        passwdField.setBorder(BorderFactory.createLineBorder(ClientSettings.GRID_REQUIRED_CELL_BORDER));
//      field.setBorder(new CompoundBorder(new RequiredBorder(),field.getBorder()));
      }

      return passwdField;
    }
  }


  public final void finalize() {
    passwdField = null;
    table = null;
    field = null;
  }


}


