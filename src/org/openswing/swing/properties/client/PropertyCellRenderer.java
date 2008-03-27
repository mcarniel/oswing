package org.openswing.swing.properties.client;

import java.awt.*;
import javax.swing.*;
import javax.swing.table.*;

import org.openswing.swing.client.*;
import org.openswing.swing.util.client.*;


/**
 * <p>Title: OpenSwing Framework</p>
 * <p>Description: Cell renderer used by the PropertyGridControl.</p>
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
public class PropertyCellRenderer extends DefaultTableCellRenderer {


  public PropertyCellRenderer() {
  }


  // implements javax.swing.table.TableCellRenderer
  /**
   *
   * Returns the default table cell renderer.
   *
   * @param table  the <code>JTable</code>
   * @param value  the value to assign to the cell at
   *			<code>[row, column]</code>
   * @param isSelected true if cell is selected
   * @param hasFocus true if cell has focus
   * @param row  the row of the cell to render
   * @param column the column of the cell to render
   * @return the default table cell renderer
   */
  public final Component getTableCellRendererComponent(JTable table, Object value,
                        boolean isSelected, boolean hasFocus, int row, int column) {
    PropertyGridModel model = (PropertyGridModel)table.getModel();
    InputControl ic = model.getInputControl(row);
    ic.setEnabled(false);

    Color back = null;
    if (model.isCellEditable(row,column))
      back = ClientSettings.GRID_EDITABLE_CELL_BACKGROUND;
    else
      back = ClientSettings.GRID_CELL_BACKGROUND;

    if (ic instanceof BaseInputControl) {
      ((BaseInputControl)ic).getBindingComponent().setBorder(BorderFactory.createEmptyBorder());
      if (isSelected)
        ((BaseInputControl)ic).getBindingComponent().setBackground(ClientSettings.GRID_SELECTION_BACKGROUND);
      else
        ((BaseInputControl)ic).getBindingComponent().setBackground(back);
    }
    else {
      if (isSelected)
        ((Component)ic).setBackground(ClientSettings.GRID_SELECTION_BACKGROUND);
      else
        ((Component)ic).setBackground(back);
    }
    if (isSelected)
      ((Component)ic).setBackground(ClientSettings.GRID_SELECTION_BACKGROUND);
    else
      ((Component)ic).setBackground(back);

    ic.setValue(value);
    return (Component)ic;
  }

}
