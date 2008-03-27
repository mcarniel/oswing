package org.openswing.swing.properties.client;

import java.awt.*;
import javax.swing.*;
import javax.swing.table.*;

import org.openswing.swing.client.*;
import org.openswing.swing.util.client.*;


/**
 * <p>Title: OpenSwing Framework</p>
 * <p>Description: Cell editor used by the PropertyGridControl.</p>
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
public class PropertyCellEditor extends AbstractCellEditor implements TableCellEditor {

  /** current editor */
  private InputControl ic = null;


  public PropertyCellEditor() {
  }


  /**
   * Returns the value contained in the editor.
   * @return the value contained in the editor
   */
  public final Object getCellEditorValue() {
    return ic.getValue();
  }


  /**
   *  Sets an initial <code>value</code> for the editor.  This will cause
   *  the editor to <code>stopEditing</code> and lose any partially
   *  edited value if the editor is editing when this method is called. <p>
   *
   *  Returns the component that should be added to the client's
   *  <code>Component</code> hierarchy.  Once installed in the client's
   *  hierarchy this component will then be able to draw and receive
   *  user input.
   *
   * @param	table		the <code>JTable</code> that is asking the
   *				editor to edit; can be <code>null</code>
   * @param	value		the value of the cell to be edited; it is
   *				up to the specific editor to interpret
   *				and draw the value.  For example, if value is
   *				the string "true", it could be rendered as a
   *				string or it could be rendered as a check
   *				box that is checked.  <code>null</code>
   *				is a valid value
   * @param	isSelected	true if the cell is to be rendered with
   *				highlighting
   * @param	row     	the row of the cell being edited
   * @param	column  	the column of the cell being edited
   * @return	the component for editing
   */
  public final Component getTableCellEditorComponent(JTable table, Object value,
                                        boolean isSelected,
                                        int row, int column) {
    PropertyGridModel model = (PropertyGridModel)table.getModel();
    ic = model.getInputControl(row);
    ic.setEnabled(true);

    Color back = ClientSettings.GRID_EDITABLE_CELL_BACKGROUND;

    if (ic instanceof BaseInputControl) {
      ((BaseInputControl)ic).getBindingComponent().setBackground(back);
    }
    else {
      ((Component)ic).setBackground(back);
    }

    ic.setValue(value);
    if (ic instanceof BaseInputControl)
      ((BaseInputControl)ic).getBindingComponent().setBorder(BorderFactory.createLoweredBevelBorder());
    return (Component)ic;
  }



}
