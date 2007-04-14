package org.openswing.swing.table.columns.client;


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

  /** maximum value */
  private int maxValue = Integer.MAX_VALUE;

  /** minimum value */
  private int minValue = Integer.MIN_VALUE;

  /** flag used to set thousands symbol visibility */
  private boolean grouping = false;

  /** dynamic settings used to reset numeric editor properties for each grid row */
  private IntegerColumnSettings dynamicSettings = null;


  public IntegerColumn() { }


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


}
