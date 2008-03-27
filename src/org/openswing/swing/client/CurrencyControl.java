package org.openswing.swing.client;

import java.beans.*;
import java.text.*;

import org.openswing.swing.util.client.*;


/**
 * <p>Title: OpenSwing Framework</p>
 * <p>Description: Currency (numeric) input control.</p>
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
public class CurrencyControl extends NumericControl {

  /** currency symbol; default value: ClientSettings.getInstance().getResources().getCurrencySymbol() */
  private String currencySymbol = ClientSettings.getInstance().getResources().getCurrencySymbol();

  /** decimal symbol; default value: ClientSettings.getInstance().getResources().getDecimalSymbol() */
  private char decimalSymbol = ClientSettings.getInstance().getResources().getDecimalSymbol();

  /** grouping symbol; default value: ClientSettings.getInstance().getResources().getGroupingSymbol() */
  private char groupingSymbol = ClientSettings.getInstance().getResources().getGroupingSymbol();


  /**
   * Constructor.
   */
  public CurrencyControl() {
    this(10);
  }


  /**
   * Constructor.
   * @param columns number of visibile characters
   */
  public CurrencyControl(int columns) {
    super(columns);
    setGrouping(true);
  }


  /**
   * Method called to set the numeric format.
   */
  protected void setFormat() {
    DecimalFormatSymbols dfs = new DecimalFormatSymbols();
    dfs.setGroupingSeparator(groupingSymbol);
    dfs.setDecimalSeparator(decimalSymbol);

    if (!isGrouping() && getDecimals()==0)
      format = new DecimalFormat(currencySymbol+" "+"#");
    else if (isGrouping() && getDecimals()==0)
      format = new DecimalFormat(currencySymbol+" "+"#,###",dfs);
    else if (isGrouping() && getDecimals()>0) {
      String dec = "";
      for(int i=0;i<getDecimals();i++)
        dec += "#";
      format = new DecimalFormat(currencySymbol+" "+"#,###."+dec,dfs);
    }
    else if (!isGrouping() && getDecimals()>0) {
      String dec = "";
      for(int i=0;i<getDecimals();i++)
        dec += "#";
      format = new DecimalFormat(currencySymbol+" "+"#."+dec,dfs);
    }
    format.setGroupingUsed(isGrouping());
    nullValue = currencySymbol+" ";
    setText("");
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
    Object val = getValue();
    this.currencySymbol = currencySymbol;
    if (!Beans.isDesignTime()) {
      setFormat();
      setValue(val);
    }
  }


  /**
   * @return decimal symbol
   */
  public final char getDecimalSymbol() {
    return decimalSymbol;
  }


  /**
   * @return grouping symbol
   */
  public final char getGroupingSymbol() {
    return groupingSymbol;
  }


  /**
   * Set decimal symbol.
   * @param decimalSymbol decimal symbol
   */
  public final void setDecimalSymbol(char decimalSymbol) {
    Object val = getValue();
    this.decimalSymbol = decimalSymbol;
    if (!Beans.isDesignTime()) {
      setFormat();
      setValue(val);
    }
  }


  /**
   * Set grouping symbol.
   * @param groupingSymbol grouping symbol
   */
  public final void setGroupingSymbol(char groupingSymbol) {
    Object val = getValue();
    this.groupingSymbol = groupingSymbol;
    if (!Beans.isDesignTime()) {
      setFormat();
      setValue(val);
    }
  }


  /**
   * Set maximum number of decimals.
   * @param decimals maximum number of decimals
   */
  public final void setDecimals(int decimals) {
    Object val = getValue();
    super.setDecimals(decimals);
    setFormat();
    if (!Beans.isDesignTime()) {
      setValue(val);
    }
  }



}
