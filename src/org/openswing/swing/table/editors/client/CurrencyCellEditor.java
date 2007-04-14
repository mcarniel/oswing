package org.openswing.swing.table.editors.client;

import javax.swing.*;
import java.awt.*;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;
import javax.swing.tree.*;

import org.openswing.swing.client.*;
import org.openswing.swing.table.columns.client.Column;
import org.openswing.swing.table.columns.client.IntegerColumnSettings;



/**
 * <p>Title: OpenSwing Framework</p>
 * <p>Description: Column editor used for currency type columns.</p>
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
public class CurrencyCellEditor extends NumericCellEditor {


  /**
   * Constructor.
   * @param colType column type; possible values: Column.TYPE_INT, Column.TYPE_DEC, Column.TYPE_CURRENCY, Column.TYPE_PERC
   * @param decimals number of decimals
   * @param required flag used to set mandatory property of the cell
   * @param currencySymbol currency symbol
   * @param dynamicSettings dynamic settings (object of type CurrencyColumnSettings) used to reset currency editor properties for each grid row
   */
  public CurrencyCellEditor(int colType, int decimals, boolean required, double minValue, double maxValue,String currencySymbol,IntegerColumnSettings dynamicSettings) {
    super(colType, decimals, required, minValue, maxValue, dynamicSettings);
    ((CurrencyControl)field).setCurrencySymbol(currencySymbol);
  }


}
