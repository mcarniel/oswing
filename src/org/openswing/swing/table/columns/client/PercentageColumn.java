package org.openswing.swing.table.columns.client;

import javax.swing.*;
import javax.swing.table.*;

import org.openswing.swing.table.client.*;
import org.openswing.swing.table.editors.client.*;
import org.openswing.swing.table.renderers.client.*;


/**
 * <p>Title: OpenSwing Framework</p>
 * <p>Description: Column of type percentage (decimal) number:
 * it contains a numeric input field to digit numbers on percentage format.</p>
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
public class PercentageColumn extends DecimalColumn {

  public PercentageColumn() {
    super.setMaxValue(100);
    super.setMinValue(0);
    super.setDecimals(0);
    setTextAlignment(SwingConstants.RIGHT);
  }


  /**
   * @return column type
   */
  public int getColumnType() {
    return TYPE_PERC;
  }


  /**
   * @return TableCellRenderer for this column
   */
  public final TableCellRenderer getCellRenderer(GridController tableContainer,Grids grids) {
    return new NumericTableCellRenderer(
        getDecimals(),
        isGrouping(),
        isHideZeroDigits(),
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
    return new NumericCellEditor(
        Column.TYPE_PERC,
        getDecimals(),
        isColumnRequired(),
        getMinValue(),
        getMaxValue(),
        getDynamicSettings(),
        isSelectDataOnEdit(),
        getMaxCharacters()
    );
  }


}
