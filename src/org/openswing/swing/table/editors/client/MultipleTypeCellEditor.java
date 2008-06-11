package org.openswing.swing.table.editors.client;

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
 * <p>Description: Column editor used to edit a combo-box, linked to a domain type column.</p>
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
public class MultipleTypeCellEditor extends AbstractCellEditor implements TableCellEditor {


  /** table */
  private JTable table = null;

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

  /** flag sed to set mandatory property of the cell */
  private boolean required;

  /** combo container */
  private JPanel p = new JPanel();

  /** current input control */
  private InputControl ic = null;


  /**
   * Constructor.
   * @param domain domain linked to the combo-box
   * @param required flag sed to set mandatory property of the cell
   */
  public MultipleTypeCellEditor(GridController gridController,TypeController typeController,String attributeName,GridControl grid,boolean required) {
    this.gridController = gridController;
    this.typeController = typeController;
    this.attributeName = attributeName;
    this.grid = grid;
    this.required = required;
    p.setOpaque(true);
    p.setLayout(new GridBagLayout());
//    p.add(field,      new GridBagConstraints(0, 0, 1, 1, 1.0, 1.0
//          ,GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(0,0,0,0), 0, 0));
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
    return ic.getValue();
  }


  /**
   * Prepare the editor for a value.
   */
  private final InputControl _prepareEditor() {
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


  public final Component getTableCellEditorComponent(JTable table, Object value,
                                               boolean isSelected, int row,
                                               int column) {
    this.table = table;
    this.row = row;
    ic = _prepareEditor();
    ic.setValue(value);
    JComponent c = null;
    if (ic instanceof BaseInputControl)
      c = ((BaseInputControl)ic).getBindingComponent();
    else
      c = (JComponent)ic;

    if (required) {
      c.setBorder(BorderFactory.createLineBorder(ClientSettings.GRID_REQUIRED_CELL_BORDER));
//      field.setBorder(new CompoundBorder(new RequiredBorder(),field.getBorder()));
    }

    p.removeAll();
    if (c instanceof JComboBox)
      p.add(c,      new GridBagConstraints(0, 0, 1, 1, 1.0, 1.0
            ,GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(0,0,0,0), 0, 0));
    else if (ic instanceof CodLookupControl) {
      p.add(c,      new GridBagConstraints(0, 0, 1, 1, 1.0, 1.0
            ,GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(0,0,0,0), 0, 0));

      p.add(((JComponent)ic).getComponent(1),      new GridBagConstraints(1, 0, 0, 1, 1.0, 1.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0,0,0,0), 0, 0));
    }
    else
        p.add((JComponent)ic,      new GridBagConstraints(0, 0, 1, 1, 1.0, 1.0
              ,GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(0,0,0,0), 0, 0));


    return p;
  }


  public final void finalize() {
    table = null;
    gridController = null;
    typeController = null;
    grid = null;
    inputControls.clear();
  }


}


