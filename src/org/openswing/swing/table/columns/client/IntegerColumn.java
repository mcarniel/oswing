package org.openswing.swing.table.columns.client;

import javax.swing.*;
import javax.swing.table.*;

import org.openswing.swing.table.client.*;
import org.openswing.swing.table.editors.client.*;
import org.openswing.swing.table.renderers.client.*;
import org.openswing.swing.util.client.ClientSettings;


/**
 * <p>Title: OpenSwing Framework</p>
 * <p>Description: Column of type integer number:
 * it contains a numeric input field without decimal support.</p>
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
public class IntegerColumn extends Column {

  /** maximum number of digits */
  private int maxCharacters = 255;

  /** maximum value */
  private int maxValue = Integer.MAX_VALUE;

  /** minimum value; default value: 0 */
  private int minValue = 0;

  /** flag used to set thousands symbol visibility */
  private boolean grouping = false;

  /** dynamic settings used to reset numeric editor properties for each grid row */
  private IntegerColumnSettings dynamicSettings = null;

  /** component left margin, with respect to component container; defaut value: 0 */
  private int leftMargin = 0;

  /** component right margin, with respect to component container; defaut value: 2 */
  private int rightMargin = 2;

  /** component top margin, with respect to component container; defaut value: 0 */
  private int topMargin = 0;

  /** component bottom margin, with respect to component container; defaut value: 0 */
  private int bottomMargin = 0;

  /** flag used in grid to automatically select data in cell when editing cell; default value: ClientSettings.SELECT_DATA_IN_EDIT; <code>false</code>to do not select data stored cell; <code>true</code> to automatically select data already stored in cell */
  private boolean selectDataOnEdit = ClientSettings.SELECT_DATA_IN_EDITABLE_GRID;


  public IntegerColumn() {
    setTextAlignment(SwingConstants.RIGHT);
  }


  /**
   * @return tipo di colonna numerico intero (INTEGER_FORMAT)
   */
  public int getColumnType() {
    return TYPE_INT;
  }


  /**
   * @return max number of decimals: 0
   */
  public int getDecimals() {
    return 0;
  }


  /**
   * @return maximum value
   */
  public int getMaxValue() {
    return maxValue;
  }


  /**
   * Set maximum value.
   * @param maxValue maximum value
   */
  public void setMaxValue(int maxValue) {
    this.maxValue = maxValue;
  }


  /**
   * @return minimum value
   */
  public int getMinValue() {
    return minValue;
  }


  /**
   * Set minimum value.
   * @param minValue minimum value
   */
  public void setMinValue(int minValue) {
    this.minValue = minValue;
  }


  /**
   * Set thousands symbol visibility.
   * @param grouping thousands symbol visibility
   */
  public final void setGrouping(boolean grouping) {
    this.grouping = grouping;
  }


  /**
   * @return boolean thousands symbol visibility
   */
  public final boolean isGrouping() {
    return grouping;
  }


  /**
   * @return dynamic settings used to reset numeric editor properties for each grid row; default value = null (no dinamic settings)
   */
  public final IntegerColumnSettings getDynamicSettings() {
    return dynamicSettings;
  }


  /**
   * Set dynamic settings used to reset numeric editor properties for each grid row.
   * @param dynamicSettings dynamic settings used to reset numeric editor properties for each grid row
   */
  public final void setDynamicSettings(IntegerColumnSettings dynamicSettings) {
    this.dynamicSettings = dynamicSettings;
  }


  /**
   * @return component bottom margin, with respect to component container
   */
  public final int getBottomMargin() {
    return bottomMargin;
  }


  /**
   * @return component left margin, with respect to component container
   */
  public final int getLeftMargin() {
    return leftMargin;
  }


  /**
   * @return component right margin, with respect to component container
   */
  public final int getRightMargin() {
    return rightMargin;
  }


  /**
   * @return component top margin, with respect to component container
   */
  public final int getTopMargin() {
    return topMargin;
  }


  /**
   * Set component top margin, with respect to component container.
   * @param topMargin component top margin
   */
  public final void setTopMargin(int topMargin) {
    this.topMargin = topMargin;
  }


  /**
   * Set component right margin, with respect to component container.
   * @param rightMargin component right margin
   */
  public final void setRightMargin(int rightMargin) {
    this.rightMargin = rightMargin;
  }


  /**
   * Set component left margin, with respect to component container.
   * @param leftMargin component left margin
   */
  public final void setLeftMargin(int leftMargin) {
    this.leftMargin = leftMargin;
  }


  /**
   * Set component bottom margin, with respect to component container.
   * @param bottomMargin component bottom margin
   */
  public final void setBottomMargin(int bottomMargin) {
    this.bottomMargin = bottomMargin;
  }


  /**
   * @return <code>false</code>to do not select data stored cell; <code>true</code> to automatically select data already stored in cell
   */
  public final boolean isSelectDataOnEdit() {
    return selectDataOnEdit;
  }


  /**
   * Define if data stored in cell must be selected when cell is set in edit
   * @param selectDataOnEdit <code>false</code>to do not select data stored cell; <code>true</code> to automatically select data already stored in cell
   */
  public final void setSelectDataOnEdit(boolean selectDataOnEdit) {
    this.selectDataOnEdit = selectDataOnEdit;
  }


  /**
       * Set maximum number of characters.
       * @param maxCharacters maximum number of characters
       */
  public final void setMaxCharacters(int maxCharacters) {
    this.maxCharacters = maxCharacters;
  }

  /**
   * @return maximum number of digits
   */
  public final int getMaxCharacters() {
    return maxCharacters;
  }


  /**
   * @return TableCellRenderer for this column
   */
  public final TableCellRenderer getCellRenderer(GridController tableContainer,Grids grids) {
    return new NumericTableCellRenderer(
        0,
        isGrouping(),
        true,
        tableContainer,
        getDynamicSettings(),
        getTextAlignment(),
        leftMargin,
        rightMargin,
        topMargin,
        bottomMargin,
        getColumnName()
    );
  }


  /**
   * @return TableCellEditor for this column
   */
  public final TableCellEditor getCellEditor(GridController tableContainer,Grids grids) {
    return new NumericCellEditor(
        Column.TYPE_INT,
        0,
        isColumnRequired(),
        getMinValue(),
        getMaxValue(),
        getDynamicSettings(),
        selectDataOnEdit,
        maxCharacters
    );

  }


}
