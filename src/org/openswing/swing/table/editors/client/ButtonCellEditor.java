package org.openswing.swing.table.editors.client;

import java.util.*;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.table.*;

import org.openswing.swing.util.client.*;


/**
 * <p>Title: OpenSwing Framework</p>
 * <p>Description: Column editor used to press a button (button type column).</p>
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
public class ButtonCellEditor extends AbstractCellEditor implements TableCellEditor {

  /** button inside the editable cell */
  private JButton field = new JButton();

  /** flag used to indicate that attribute value will be showed as button text; default value: <code>false</code> i.e. the button text is defined by the "text" property */
  private boolean showAttributeValue;

  /** current value */
  private Object value = null;


  /**
   * Constructor.
   * @param text button text
   * @param actionListeners list of ActionListeners linked to the button
   */
  public ButtonCellEditor(String text,boolean showAttributeValue,ArrayList actionListeners,Icon icon) {
    this.showAttributeValue = showAttributeValue;
    if (!showAttributeValue)
      this.field.setText(ClientSettings.getInstance().getResources().getResource(text));
    if (icon!=null)
      field.setIcon(icon);
    for(int i=0;i<actionListeners.size();i++)
      field.addActionListener((ActionListener)actionListeners.get(i));
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
  private final boolean validate() {
    fireEditingStopped();
    return true;
  }


  public final Object getCellEditorValue() {
    return value;
  }


  /**
   * Prepare the editor for a value.
   */
  private final Component _prepareEditor(Object value) {
    this.value = value;
    if (showAttributeValue) {
      if (value!=null && value instanceof byte[])
        field.setIcon(new ImageIcon((byte[])value));
      else if (value!=null && value instanceof Icon)
        field.setIcon((Icon)value);
      else if (value!=null)
        field.setText(value.toString());
      else {
        field.setText("");
        field.setIcon(null);
      }
    }
    return field;
  }


  public final Component getTableCellEditorComponent(JTable table, Object value,
                                               boolean isSelected, int row,
                                               int column) {
    return _prepareEditor(value);
  }


}


