package org.openswing.swing.table.model.client;

import java.math.*;
import java.util.*;

import javax.swing.event.*;
import javax.swing.table.*;

import org.openswing.swing.logger.client.*;
import org.openswing.swing.message.receive.java.*;
import org.openswing.swing.table.columns.client.*;
import org.openswing.swing.util.java.*;


/**
 * <p>Title: OpenSwing Framework</p>
 * <p>Description: TableModel based on a ValueObject list.</p>
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
public class VOListTableModel extends AbstractTableModel {

  /** adapter which maps v.o. attribute names to TableModel columns */
  private VOListAdapter fieldAdapter;

  /** ValueObject list */
  private Vector data;

  /** flag which indicates if the TableModel is changed (add/update/delete row) */
  private boolean modified = false;

  /** current grid mode; default value: READONLY */
  private int mode = Consts.READONLY;

  /** TableModel events listener */
  private VOListTableModelListener modelListener = null;

  /** indexes of TableModel changed rows (Integer objects) */
  private ArrayList changedRows = new ArrayList();

  /** collection of value objects that have been changed; content: row index, original v.o. (before changes) */
  private Hashtable changedVOs = new Hashtable();


  /**
   * @param fieldAdapter adapter che collega modello dati e value object
   */
  public VOListTableModel(VOListAdapter fieldAdapter,VOListTableModelListener modelListener) {
    this.fieldAdapter = fieldAdapter;
    data = new Vector();
    this.modelListener = modelListener;

    addTableModelListener(new TableModelListener() {
      public void tableChanged(TableModelEvent e) {
        if (mode==Consts.EDIT) {
          Integer selRow = new Integer(e.getFirstRow());
          if (!changedRows.contains(selRow)) {
            changedRows.add(selRow);
          }
        }
      }
    });

  }


  /**
   * @param column index of the column
   * @return name of the attribute that corresponds to the given column
   */
  public final String getColumnName(int column) {
    return(fieldAdapter.getFieldName(column));
  }


  /**
   * @param column index of the column
   * @return column type
   */
  public final int getColumnType(int column) {
    return(fieldAdapter.getFieldType(column));
  }


  /**
   * Add a ValueObject to TableModel.
   * @param object <code>ValueObject</code> to add
   */
  public final void addObject(ValueObject object) {
    int index = data.size();
    data.addElement(object);
    modified = true;
    fireTableRowsInserted(index, index);
  }


  /**
   * Insert a ValueObject at the specified position in the data vector.
   * A new row will be inserted at the corresponding position in the table model.
   * @param object <code>ValueObject</code> to add
   * @param row row at which to insert the object
   */
  public final void insertObjectAt(ValueObject object, int row) {
    data.insertElementAt(object, row);
    modified = true;
    fireTableRowsInserted(row, row);
  }


  /**
   * Notify the model that the ValueObject at the given row has been updated.
   * @param row row of the updated object
   */
  public final void updateObjectAt(int row) {
    if((row < 0) || (row >= data.size()))
      throw new IllegalArgumentException("Row out of bounds");
    fireTableRowsUpdated(row, row);
  }


  /**
   * Remove all ValueObjects from the data vector.
   * All rows will be removed from the TableModel.
   */
  public final void clear() {
    data.removeAllElements();
    modified = true;
  }


  /**
   * Remove the ValueObject at the specified position from the data vector.
   * The row at the corresponding position in the table model will be removed.
   * @param row row of the object to remove
   */
  public final void removeObjectAt(int row) {
    data.removeElementAt(row);
    modified = true;
    fireTableRowsDeleted(row, row);
  }


  /**
   * @return number of objects in the data vector (e.g., the number of rows in the TableModel)
   */
  public final int getRowCount() {
    return(data.size());
  }


  /**
   * @return number of columns in this table model (e.g., the number of fields as reported by the attribute adapter)
   */
  public final int getColumnCount() {
    return(fieldAdapter.getFieldCount());
  }


  /**
   * @param row row index
   * @param column column index
   * @return Object at the specified coordinates
   */
  public final Object getValueAt(int row, int column) {
    if (data.size()-1<row)
      return null;
    ValueObject object = (ValueObject)data.elementAt(row);
    return(fieldAdapter.getField(object, column));
  }


  /**
   * @param row row index
   * @return <code>ValueObject</code> represented in the given row
   * @see #getRowForObject
   */
  public final ValueObject getObjectForRow(int row) {
    if (row<data.size() && row>=0)
      return((ValueObject)data.elementAt(row));
    else
      return null;
  }


  /**
   * @param object <code>ValueObject</code> to locate
   * @return row in the TableModel that represents the given object
   * @see #getObjectForRow
   */
  public final int getRowForObject(ValueObject object) {
    return(data.indexOf(object));
  }


  /**
   * @return <code>true</code> if modifications have been made, and <code>false</code> otherwise
   * @see #clearModified
   */
  public final boolean isModified() {
    return(modified);
  }


  /**
   * Reset the modification flag for this model.
   * @see #isModified
   */
  public final void clearModified() {
    modified = false;
  }


  /**
   * @param column column index
   * @return <code>Class</code> object for the specified column
   */
  public final Class getColumnClass(int column) {
    return(fieldAdapter.getFieldClass(column));
  }


  /**
   * Update TableModel with the ValueObject.
   * @param object new ValueObject
   * @param row row index
   */
  public final void updateObjectAt(ValueObject object, int row) {
    try {
      data.setElementAt(object, row);
    }
    catch(Exception ex) {
      Logger.error(this.getClass().getName(),"updateObjectAt","Error on updating ValueObject",ex);
    }
  }


  /**
   * @return ValueObject list
   */
  public final Vector getDataVector() {
    return(this.data);
  }


  /**
   * @param row row index
   * @param column column index
   * @return <code>true</code> if the cell at the given row and column is editable and
   * <code>false</code> otherwise. The editable state is based on FieldAdapter and on current grid edit mode.
   */
  public final boolean isCellEditable(int row, int column) {
    try {
      if (mode==Consts.READONLY) {
        Column col = fieldAdapter.getFieldColumn(column);
        if (col.getColumnType()==col.TYPE_BUTTON && ((ButtonColumn)col).isEnableInReadOnlyMode())
//          return true;
          return fieldAdapter.getTableContainer().isCellEditable(fieldAdapter.getGrids().getGridControl(),row,fieldAdapter.getFieldName(column));
        else if (col.getColumnType()==col.TYPE_CHECK && ((CheckBoxColumn)col).isEnableInReadOnlyMode())
//          return true;
          return fieldAdapter.getTableContainer().isCellEditable(fieldAdapter.getGrids().getGridControl(),row,fieldAdapter.getFieldName(column));
        return false;
      }
      else if (mode==Consts.INSERT) {
        if (row<changedRows.size())
//        return fieldAdapter.isFieldEditable(
//          mode,
//          row,
//          column
//        );
          return fieldAdapter.getTableContainer().isCellEditable(fieldAdapter.getGrids().getGridControl(),row,fieldAdapter.getFieldName(column));
        else
          return false;
      } else
//      return fieldAdapter.isFieldEditable(
//          mode,
//          row,
//          column
//      );
        return fieldAdapter.getTableContainer().isCellEditable(fieldAdapter.getGrids().getGridControl(),row,fieldAdapter.getFieldName(column));
    }
    catch (Exception ex) {
      Logger.error(this.getClass().getName(),"isCellEditable","Error defining cell editability",ex);
      return false;
    }
  }


  /**
   * Set the current edit grid mode; default value: READONLY.
   * @param mode current edit grid mode
   */
  public final void setMode(int mode) {
    if (mode!=Consts.READONLY && mode!=Consts.EDIT && mode!=Consts.INSERT)
      throw new UnsupportedOperationException("Mode not supported: "+mode);
    this.mode = mode;
    changedRows.clear();
    changedVOs.clear();
    if (mode==Consts.INSERT) {
      try {
        this.insertObjectAt((ValueObject) fieldAdapter.getValueObjectType().getConstructor(new Class[0]).newInstance(new Object[0]),0);
        changedRows.add(new Integer(0));
      }
      catch (Exception ex) {
        ex.printStackTrace();
      }
      catch (Error er) {
        er.printStackTrace();
      }
    }
    modelListener.modeChanged(mode);
  }


  /**
   * @return current edit grid mode
   */
  public final int getMode() {
    return mode;
  }


  /**
   * Set the value at the specified row and column in the TableModel.
   * @param row row index
   * @param column column index
   * @param value new object for the specified coordinates
   */
  public final void setValueAt(Object value, int row, int column) {
    setValue(value, row, column);
  }


  /**
   * Set the value at the specified row and column in the TableModel, only if:
   * - the grid is editable and the value is valid (i.e. GridController.validateCell method returns <code>true</code>)
   * - the grid is in read only mode
   * @param row row index
   * @param column column index
   * @param value new object for the specified coordinates
   * @return <code>true</code> if value being setted is valid, <code>false</code> otherwise
   */
  public final boolean setValue(Object value, int row, int column) {
    // store the original v.o. if grid mode is EDIT...
    try {
      if (mode == Consts.EDIT && !changedVOs.containsKey(new Integer(row))) {
        changedVOs.put(new Integer(row), getObjectForRow(row).clone());
      }
    }
    catch (CloneNotSupportedException ex) {
      Logger.error(this.getClass().getName(),"setValueAt","Error while duplicating the edited v.o.",ex);
    }

    // retrieve previous cell value ...
    Object oldValue = getValueAt(row,column);
    if (isCellEditable(row,column)) {
      if (oldValue!=null && oldValue instanceof Number && value instanceof Double) {
        oldValue = new Double(oldValue.toString());
      }
      if (value!=null && value instanceof BigDecimal && oldValue!=null && this.fieldAdapter.getFieldClass(column).equals(BigDecimal.class)) {
        value = new BigDecimal(value.toString()).setScale(((BigDecimal)oldValue).scale());
      }
      else if (value!=null && value instanceof BigDecimal &&
               (this.fieldAdapter.getFieldClass(column).equals(Integer.class) || this.fieldAdapter.getFieldClass(column).equals(Integer.TYPE))) {
        value = new Integer(value.toString());
      }
      else if (value!=null && value instanceof BigDecimal &&
               (this.fieldAdapter.getFieldClass(column).equals(Double.class) || this.fieldAdapter.getFieldClass(column).equals(Double.TYPE))) {
        value = new Double(value.toString());
      }
      else if (value!=null && value instanceof BigDecimal &&
               (this.fieldAdapter.getFieldClass(column).equals(Long.class) || this.fieldAdapter.getFieldClass(column).equals(Long.TYPE))) {
        value = new Long(value.toString());
      }
      else if (value!=null && value instanceof BigDecimal &&
               (this.fieldAdapter.getFieldClass(column).equals(Short.class) || this.fieldAdapter.getFieldClass(column).equals(Short.TYPE))) {
        value = new Short(value.toString());
      }
      else if (value!=null && value instanceof BigDecimal &&
               (this.fieldAdapter.getFieldClass(column).equals(Float.class) || this.fieldAdapter.getFieldClass(column).equals(Float.TYPE))) {
        value = new Float(value.toString());
      }
      if (oldValue==null && value!=null ||
          oldValue!=null && value==null ||
          oldValue!=null && !oldValue.equals(value)) {
        // validate new value...
        super.setValueAt(value, row, column);
        boolean isOk = fieldAdapter.getTableContainer().validateCell(
            row,
            fieldAdapter.getFieldName(column),
            oldValue,
            value);
        if (isOk) {
          ValueObject object = (ValueObject)data.elementAt(row);
          fieldAdapter.setField(object, column, value);
          modified = true;
          fireTableCellUpdated(row,column);
          return true;
        }
        else {
          return false;
        }
      } else {
//        ValueObject object = (ValueObject)data.elementAt(row);
//        fieldAdapter.setField(object, column, value);
//        modified = false;
//        fireTableCellUpdated(row,column);
        return true;
      }
    }
    else {
      super.setValueAt(value, row, column);
      ValueObject object = (ValueObject)data.elementAt(row);
      fieldAdapter.setField(object, column, value);
      modified = false;
      fireTableCellUpdated(row,column);
      return true;
    }
//      super.setValueAt(value, row, column);
  }


  /**
   * @return list of ValueObjects which have been changed (used when the grid is in INSERT/EDIT mode)
   */
  public final ArrayList getChangedRows() {
    ArrayList voList = new ArrayList();
    for(int i=0;i<changedRows.size();i++)
      voList.add( this.getObjectForRow( ((Integer)changedRows.get(i)).intValue() ) );
    return voList;
  }


  /**
   * @return list of indexes of TableModel changed rows (Integer objects)
   */
  public final ArrayList getChangedRowIndexes() {
    return changedRows;
  }


  /**
   * @return int[] row indexes related to ValueObjects which have been changed (used when the grid is in INSERT/EDIT mode)
   */
  public final int[] getChangedRowNumbers() {
    int[] rowNumbers = new int[this.changedRows.size()];
    for (int i=0; i<rowNumbers.length; i++)
      rowNumbers[i] = ((Integer) this.changedRows.get(i)).intValue();
    return(rowNumbers);
  }


  /**
   * Update ValueObjects into TableModel for the specified rows.
   * @param rowNumbers row indexes in TableModel
   * @param objects ValueObject to update into TableModel
   */
  public final void updateValueObjectsAt(int[] rowNumbers, ValueObject[] objects) {
    if (rowNumbers.length!=objects.length) {
      Logger.error(this.getClass().getName(),"updateValueObjectsAt","VaLueObjects length is not equals to rowNumbers length.",null);
      return;
    }
    for (int i=0; i<rowNumbers.length; i++)
      updateObjectAt(objects[i],rowNumbers[i]);
  }


  /**
   * @return ValueObject type
   */
  public final Class getValueObjectType() {
    return fieldAdapter.getValueObjectType();
  }


  /**
   * @return list of value objects that have been changed; content: list of original v.o. (before changes)
   */
  public final ArrayList getOldVOsChanged() {
    ArrayList voList = new ArrayList();
    for(int i=0;i<changedRows.size();i++)
      voList.add( changedVOs.get(changedRows.get(i)) );
    return voList;
  }


}
