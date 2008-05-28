package org.openswing.swing.table.columns.client;

import javax.swing.*;
import javax.swing.table.*;

import org.openswing.swing.table.client.*;
import org.openswing.swing.table.editors.client.*;
import org.openswing.swing.table.renderers.client.*;


/**
 * <p>Title: OpenSwing Framework</p>
 * <p>Description: Column of type decimal number:
 * it contains a numeric input field with decimal support.</p>
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
public class DecimalColumn extends Column {

  /** maximum value */
  private double maxValue = Integer.MAX_VALUE;

  /** minimum value; default value: 0 */
  private double minValue = 0;

  /** maximum number of decimals */
  private int decimals = 0;

  /** flag used to set thousands symbol visibility */
  private boolean grouping = false;

  /** dynamic settings used to reset numeric editor properties for each grid row */
  private DecimalColumnSettings dynamicSettings = null;


  public DecimalColumn() {
    setTextAlignment(SwingConstants.RIGHT);
  }


  /**
   * @return column type
   */
  public int getColumnType() {
    return TYPE_DEC;
  }


  /**
   * @return maximum number of decimals
   */
  public int getDecimals() {
    return decimals;
  }


  /**
   * Set maximum number of decimals.
   * @param decimals maximum number of decimals
   */
  public void setDecimals(int decimals) {
    this.decimals = decimals;
  }


  /**
   * @return maximum value
   */
  public double getMaxValue() {
    return maxValue;
  }


  /**
   * Set maximum value.
   * @param maxValue maximum value
   */
  public void setMaxValue(double maxValue) {
    this.maxValue = maxValue;
  }


  /**
   * @return minimum value
   */
  public double getMinValue() {
    return minValue;
  }


  /**
   * Set minimum value.
   * @param minValue minimum value
   */
  public void setMinValue(double minValue) {
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
  public final DecimalColumnSettings getDynamicSettings() {
    return dynamicSettings;
  }


  /**
   * Set dynamic settings used to reset numeric editor properties for each grid row.
   * @param dynamicSettings dynamic settings used to reset numeric editor properties for each grid row
   */
  public final void setDynamicSettings(DecimalColumnSettings dynamicSettings) {
    this.dynamicSettings = dynamicSettings;
  }


  /**
   * @return TableCellRenderer for this column
   */
  public TableCellRenderer getCellRenderer(GridController tableContainer,Grids grids) {
    return new NumericTableCellRenderer(
        getDecimals(),
        isGrouping(),
        tableContainer,
        getDynamicSettings(),
        getTextAlignment(),
        getColumnName()
    );
  }


  /**
   * @return TableCellEditor for this column
   */
  public TableCellEditor getCellEditor(GridController tableContainer,Grids grids) {
    return new NumericCellEditor(
        Column.TYPE_DEC,
        getDecimals(),
        isColumnRequired(),
        getMinValue(),
        getMaxValue(),
        getDynamicSettings()
    );
  }


}
