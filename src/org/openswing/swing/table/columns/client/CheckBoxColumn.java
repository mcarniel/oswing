package org.openswing.swing.table.columns.client;

import java.util.*;

import java.awt.event.*;
import javax.swing.*;
import javax.swing.table.*;

import org.openswing.swing.table.client.*;
import org.openswing.swing.table.editors.client.*;
import org.openswing.swing.table.renderers.client.*;


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

  /** value used to select the check-box; default value: Boolean.TRUE */
  private Object positiveValue = new Boolean(true);

  /** value used to deselect the check-box; default value: Boolean.FALSE */
  private Object negativeValue = new Boolean(false);

  /** list of ItemListener object linked to the check-box */
  private ArrayList itemListeners = new ArrayList();

  /** flag used to indicate if the check-box is enabled also when the grid is in readonly mode; default value: <code>false</code> i.e. the check-box is enabled only in INSERT/EDIT modes, according to "editableOnEdit" and "editableOnInsert" properties */
  private boolean enableInReadOnlyMode = false;

  /** define if null value is alloed (i.e. distinct from Boolean.FALSE value); default value: <code>false</code> */
  private boolean allowNullValue = false;

  /** define whether "select all" and "deselect all" menu items have to be added to popup menu when right clicking on this check-box column; default value: <code>false</code> */
  private boolean showDeSelectAllInPopupMenu = false;

  /** define whether "select all" and "deselect all" commands must be applied on editable cells or on all cells, independently from cells editability; default value: <code>false</code>, i.e. do not select all cells independently from cells editability; note: this flag is used only when "showDeSelectAllInPopupMenu" property is set to <code>true</code> */
  private boolean deSelectAllCells = false;


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
      isEnableInReadOnlyMode(),
      isAllowNullValue(),
      getColumnName(),
      positiveValue,
      negativeValue
    );
  }


  /**
   * @return TableCellEditor for this column
   */
  public final TableCellEditor getCellEditor(GridController tableContainer,Grids grids) {
    return new CheckBoxCellEditor(
      grids,
      isColumnRequired(),
      getItemListeners(),
      isAllowNullValue(),
      positiveValue,
      negativeValue
    );
  }


  /**
   * @return define if null value is alloed (i.e. distinct from Boolean.FALSE value)
   */
  public final boolean isAllowNullValue() {
    return allowNullValue;
  }


  /**
   * Define if null value is alloed (i.e. distinct from Boolean.FALSE value)
   * @param allowNullValue define if null value is alloed (i.e. distinct from Boolean.FALSE value)
   */
  public final void setAllowNullValue(boolean allowNullValue) {
    this.allowNullValue = allowNullValue;
  }


  /**
   * @return define whether "select all" and "deselect all" commands must be applied on editable cells or on all cells, independently from cells editability; default value: <code>false</code>, i.e. do not select all cells independently from cells editability
   */
  public final boolean isDeSelectAllCells() {
    return deSelectAllCells;
  }


  /**
   * @return define whether "select all" and "deselect all" menu items have to be added to popup menu when right clicking on this check-box column
   */
  public final boolean isShowDeSelectAllInPopupMenu() {
    return showDeSelectAllInPopupMenu;
  }


  /**
   * Define whether "select all" and "deselect all" menu items have to be added to popup menu when right clicking on this check-box column.
   * Default value: <code>false</code>
   * @param showDeSelectAllInPopupMenu define whether "select all" and "deselect all" menu items have to be added to popup menu when right clicking on this check-box column
   */
  public final void setShowDeSelectAllInPopupMenu(boolean showDeSelectAllInPopupMenu) {
    this.showDeSelectAllInPopupMenu = showDeSelectAllInPopupMenu;
  }


  /**
   * Define whether "select all" and "deselect all" commands must be applied on editable cells or on all cells, independently from cells editability; default value: <code>false</code>, i.e. do not select all cells independently from cells editability.
   * Note: this flag is used only when "showDeSelectAllInPopupMenu" property is set to <code>true</code>.
   * @param deSelectAllCells define whether "select all" and "deselect all" commands must be applied on editable cells or on all cells, independently from cells editability; default value: <code>false</code>, i.e. do not select all cells independently from cells editability
   */
  public final void setDeSelectAllCells(boolean deSelectAllCells) {
    this.deSelectAllCells = deSelectAllCells;
  }


}
