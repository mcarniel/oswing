package org.openswing.swing.table.columns.client;

import java.util.ArrayList;
import java.awt.event.ItemListener;
import javax.swing.SwingConstants;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableCellEditor;
import org.openswing.swing.table.client.GridController;
import org.openswing.swing.table.client.Grids;
import org.openswing.swing.table.renderers.client.CheckBoxTableCellRenderer;
import org.openswing.swing.table.editors.client.CheckBoxCellEditor;


/**
 * <p>Title: OpenSwing Framework</p>
 * <p>Description: Column of type check-box.
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
public class CheckBoxColumn extends Column {

  /** value used to select the check-box */
  private Object positiveValue = new Boolean(true);

  /** value used to deselect the check-box */
  private Object negativeValue = new Boolean(false);

  /** list of ItemListener object linked to the check-box */
  private ArrayList itemListeners = new ArrayList();

  /** flag used to indicate if the check-box is enabled also when the grid is in readonly mode; default value: <code>false</code> i.e. the check-box is enabled only in INSERT/EDIT modes, according to "editableOnEdit" and "editableOnInsert" properties */
  private boolean enableInReadOnlyMode = false;


  public CheckBoxColumn() {
    setTextAlignment(SwingConstants.CENTER);
  }


  /**
   * @return coumn type
   */
  public final int getColumnType() {
    return TYPE_CHECK;
  }


  /**
   * Add an ItemListener to the check-box.
   */
  public final void addItemListener(ItemListener listener) {
    itemListeners.add(listener);
  }


  /**
   * @return value used to select the check-box
   */
  public final Object getPositiveValue() {
    return positiveValue;
  }


  /**
   * Set the value used to select the check-box.
   * @param positiveValue value used to select the check-box
   */
  public final void setPositiveValue(Object positiveValue) {
    this.positiveValue = positiveValue;
  }


  /**
   * @return value used to deselect the check-box
   */
  public final Object getNegativeValue() {
    return negativeValue;
  }


  /**
   * Set the value used to deselect the check-box .
   * @param negativeValue value used to deselect the check-box
   */
  public final void setNegativeValue(Object negativeValue) {
    this.negativeValue = negativeValue;
  }


  /**
   * @return list of ItemListener object linked to the check-box
   */
  public ArrayList getItemListeners() {
    return itemListeners;
  }


  /**
   * @return indicate if the check-box is enabled also when the grid is in readonly mode; default value: <code>false</code> i.e. the check-box is enabled only in INSERT/EDIT modes, according to "editableOnEdit" and "editableOnInsert" properties
   */
  public final boolean isEnableInReadOnlyMode() {
    return enableInReadOnlyMode;
  }


  /**
   * Define if the check-box is enabled also when the grid is in readonly mode.
   * @param enableInReadOnlyMode flag used to indicate if the check-box is enabled also when the grid is in readonly mode; <code>false</code> means that the check-box is enabled only in INSERT/EDIT modes, according to "editableOnEdit" and "editableOnInsert" properties
   */
  public final void setEnableInReadOnlyMode(boolean enableInReadOnlyMode) {
    this.enableInReadOnlyMode = enableInReadOnlyMode;
  }


  /**
   * @return TableCellRenderer for this column
   */
  public final TableCellRenderer getCellRenderer(GridController tableContainer,Grids grids) {
    return new CheckBoxTableCellRenderer(
      tableContainer,
      getTextAlignment(),
      isEnableInReadOnlyMode()
    );
  }


  /**
   * @return TableCellEditor for this column
   */
  public final TableCellEditor getCellEditor(GridController tableContainer,Grids grids) {
    return new CheckBoxCellEditor(isColumnRequired(),getItemListeners());
  }


}
