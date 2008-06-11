package org.openswing.swing.table.renderers.client;

import java.text.*;

import org.openswing.swing.table.client.*;
import org.openswing.swing.table.columns.client.*;
import org.openswing.swing.util.client.*;


/**
 * <p>Title: OpenSwing Framework</p>
 * <p>Description: Column renderer used to format a currency type column.</p>
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
public class CurrencyTableCellRenderer extends NumericTableCellRenderer {

  /** currency symbol */
  private String currencySymbol = null;


  /**
   * Constructor.
   * @param decimals number of decimals
   * @param grouping flag used to enable grouping
   * @param currencySymbol currency symbol
   * @param gridController grid controller
   * @param dynamicSettings dynamic settings used to reset numeric editor properties for each grid row
   * @param attributeName attribute name associated to this column
   */
  public CurrencyTableCellRenderer(int decimals, boolean grouping,boolean hideZeroDigits,
                                   String currencySymbol,GridController gridController,IntegerColumnSettings dynamicSettings,
                                   int alignement,int leftMargin,int rightMargin,int topMargin,int bottomMargin,String attributeName) {
    super(decimals,grouping,hideZeroDigits,gridController,dynamicSettings,alignement,leftMargin,rightMargin,topMargin,bottomMargin,attributeName);
    this.currencySymbol = currencySymbol;
    setFormat(decimals,grouping);
  }


  /**
   * Set cell format.
   */
  protected void setFormat(int decimals, boolean grouping) {
    DecimalFormatSymbols dfs = new DecimalFormatSymbols();
    dfs.setGroupingSeparator(ClientSettings.getInstance().getResources().getGroupingSymbol());
    dfs.setDecimalSeparator(ClientSettings.getInstance().getResources().getDecimalSymbol());

    // currency cell format...
    if (!grouping && decimals==0)
      format = new DecimalFormat(currencySymbol+" "+"0");
    else if (grouping && decimals==0)
      format = new DecimalFormat(currencySymbol+" "+"#,##0",dfs);
    else if (grouping && decimals>0) {
      String dec = "";
      for(int i=0;i<decimals;i++)
        dec += hideZeroDigits?"#":"0";
      format = new DecimalFormat(currencySymbol+" "+"#,##0."+dec,dfs);
    }
    else if (!grouping && decimals>0) {
      String dec = "";
      for(int i=0;i<decimals;i++)
        dec += hideZeroDigits?"#":"0";
      format = new DecimalFormat(currencySymbol+" "+"0."+dec,dfs);
    }

    format.setGroupingUsed(grouping);
  }


  /**
   * Set currency symbol.
   * @param currencySymbol currency symbol
   */
  public final void setCurrencySymbol(String currencySymbol) {
    this.currencySymbol = currencySymbol;
  }


}
