package org.openswing.swing.properties.client;

import java.util.*;

import javax.swing.table.*;

import org.openswing.swing.client.*;
import org.openswing.swing.util.client.*;
import org.openswing.swing.util.java.*;


/**
 * <p>Title: OpenSwing Framework</p>
 * <p>Description: TableModel used by PropertyGridControl.</p>
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
public class PropertyGridModel extends DefaultTableModel {

  /** current grid mode; default value: READONLY */
  private int mode = Consts.READONLY;

  /** collection of pairs <row index: Integer,old value: Object> */
  private Hashtable changedValues = new Hashtable();

  /** changed row indexes, expressed as a collection of Integer */
  private HashSet changedRows = new HashSet();


  public PropertyGridModel() {
    super(
      new String[]{
        ClientSettings.getInstance().getResources().getResource("property name"),
        ClientSettings.getInstance().getResources().getResource("property value"),
        "",
        ""
      },
      0
    );
  }


  /**
   * @return <code>true</code> if the current grid mode is EDIT and the column is the property value column, <code>false</code> otherwise
   */
  public final boolean isCellEditable(int row, int column) {
    if (column==1 && (mode==Consts.EDIT || mode==Consts.INSERT))
      return true;
    return false;
  }


  /**
   * @return current grid mode
   */
  public final int getMode() {
    return mode;
  }


  /**
   * Set grid mode.
   * @param mode grid mode; possibile values: READONLY, INSERT, EDIT
   */
  public final void setMode(int mode) {
    this.mode = mode;
    changedRows.clear();
    changedValues.clear();
    if (mode==Consts.INSERT) {
      Object defValue = null;
      for(int i=0;i<getRowCount();i++) {
        defValue = getDefaultValue(i);
        setValueAt(defValue,i,1);
      }
    }
  }


  /**
   * @param rowIndex row index
   * @return default value to use (in INSERT mode) for the property value related to the specified row index
   */
  public final Object getDefaultValue(int rowIndex) {
    return getValueAt(rowIndex,3);
  }


  /**
   * @param rowIndex row index
   * @return Component to use for the property value related to the specified row index
   */
  public final InputControl getInputControl(int rowIndex) {
    return (InputControl)getValueAt(rowIndex,2);
  }


  /**
   * @param rowIndex row index
   * @return <code>true</code> if the property is mandatory, <code>false</code> otherwise
   */
  public final boolean isRequired(int rowIndex) {
    return getInputControl(rowIndex).isRequired();
  }


  /**
   * @param rowIndex row index
   * @return attribute name, related to the specified row index
   */
  public final String getAttributeName(int rowIndex) {
    return getInputControl(rowIndex).getAttributeName();
  }


  /**
   * @param rowIndex row index in the model
   * @param value property value to set
   */
  public final void setPropertyValue(int rowIndex,Object value) {
    setValueAt(value,rowIndex,1);
  }


  /**
   * @param rowIndex row index in the model
   * @return property value related to the specified attribute name
   */
  public final Object getPropertyValue(int rowIndex) {
    return getValueAt(rowIndex,1);
  }


  /**
   * @return old value stored for the specified property value
   */
  public final Object getOldPropertyValue(int rowIndex) {
    return changedValues.get(new Integer(rowIndex));
  }


  /**
   * @param rowIndex row index in the model
   * @return user object related to the specified row index
   */
  public final Object getUserObject(int rowIndex) {
    return getValueAt(rowIndex,4);
  }


  /**
   * @param attributeName attribute name that identify a row
   * @return row index related to the specified attribute name
   */
  public final int findRow(String attributeName) {
    for(int i=0;i<getRowCount();i++)
      if (attributeName.equals(getInputControl(i).getAttributeName()))
        return i;
    return -1;
  }


  /**
   * @return list of row indexes, related to chnaged properties
   */
  public final int[] getChangedRowNumbers() {
    int[] rows = new int[changedRows.size()];
    Iterator it = changedRows.iterator();
    int i=0;
    while(it.hasNext())
      rows[i++] = ((Integer)it.next()).intValue();
    return rows;
  }


  /**
   * Sets the object value for the cell at <code>column</code> and
   * <code>row</code>.  <code>aValue</code> is the new value.  This method
   * will generate a <code>tableChanged</code> notification.
   *
   * @param   aValue          the new value; this can be null
   * @param   row             the row whose value is to be changed
   * @param   column          the column whose value is to be changed
   * @exception  ArrayIndexOutOfBoundsException  if an invalid row or
   *               column was given
   */
  public final void setValueAt(Object aValue, int row, int column) {
    Object oldValue = getValueAt(row, column);
    if (oldValue!=null && aValue==null ||
        oldValue==null && aValue!=null ||
        oldValue!=null && !oldValue.equals(aValue))
      if (!changedRows.contains(new Integer(row))) {
        changedRows.add(new Integer(row));
        if (oldValue!=null)
          changedValues.put(new Integer(row),oldValue);
      }
    super.setValueAt(aValue, row, column);
  }


}
