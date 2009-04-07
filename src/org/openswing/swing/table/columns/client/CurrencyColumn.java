package org.openswing.swing.table.columns.client;

import javax.swing.*;
import javax.swing.table.*;

import org.openswing.swing.table.client.*;
import org.openswing.swing.table.editors.client.*;
import org.openswing.swing.table.renderers.client.*;
import org.openswing.swing.util.client.*;


/**
 * <p>Title: OpenSwing Framework</p>
 * <p>Description: Column of type currency:
 * it contains a numeric input field with currency symbol, grouping symbol and decimal symbol.</p>
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
public class CurrencyColumn extends DecimalColumn {

  /** currency symbol; default value: ClientSettings.getInstance().getResources().getCurrencySymbol() */
  private String currencySymbol = ClientSettings.getInstance().getResources().getCurrencySymbol();

  /** flag used to define the default position of currency symbol in currency control/column: on the left or on the right of the numeric value; default value: <code>ClientSettings.CURRENCY_SYMBOL_ON_LEFT</code> i.e. on the left of the numeric value */
  private boolean currencySymbolOnLeft = ClientSettings.CURRENCY_SYMBOL_ON_LEFT;


  public CurrencyColumn() {
    setTextAlignment(SwingConstants.RIGHT);
  }


  /**
   * @return currency symbol
   */
  public final String getCurrencySymbol() {
    return currencySymbol;
  }


  /**
   * Set the currency symbol.
   * @param currencySymbol currency symbol
   */
  public final void setCurrencySymbol(String currencySymbol) {
    this.currencySymbol = currencySymbol;
  }




  /**
   * @return flag used to define the default position of currency symbol in currency control/column: on the left or on the right of the numeric value
   */
  public final boolean isCurrencySymbolOnLeft() {
    return currencySymbolOnLeft;
  }


  /**
   * Set the flag used to define the default position of currency symbol in currency control/column: on the left or on the right of the numeric value.
   * Default value: <code>ClientSettings.CURRENCY_SYMBOL_ON_LEFT</code> i.e. on the left of the numeric value.
   * @param currencySymbolOnLeft flag used to define the default position of currency symbol in currency control/column: on the left or on the right of the numeric value
   */
  public final void setCurrencySymbolOnLeft(boolean currencySymbolOnLeft) {
    this.currencySymbolOnLeft = currencySymbolOnLeft;
  }


  /**
   * @return column type
   */
  public int getColumnType() {
    return TYPE_CURRENCY;
  }


  /**
   * @return TableCellRenderer for this column
   */
  public final TableCellRenderer getCellRenderer(GridController tableContainer,Grids grids) {
    return new CurrencyTableCellRenderer(
        getDecimals(),
        isGrouping(),
        isHideZeroDigits(),
        isCurrencySymbolOnLeft(),
        getCurrencySymbol(),
        tableContainer,
        getDynamicSettings(),
        getTextAlignment(),
        getLeftMargin(),
        getRightMargin(),
        getTopMargin(),
        getBottomMargin(),
        getColumnName()
    );
  }


  /**
   * @return TableCellEditor for this column
   */
  public final TableCellEditor getCellEditor(GridController tableContainer,Grids grids) {
    return new CurrencyCellEditor(
        Column.TYPE_CURRENCY,
        getDecimals(),
        isColumnRequired(),
        isCurrencySymbolOnLeft(),
        getMinValue(),
        getMaxValue(),
        getCurrencySymbol(),
        getDynamicSettings(),
        isSelectDataOnEdit()
    );

  }



}
