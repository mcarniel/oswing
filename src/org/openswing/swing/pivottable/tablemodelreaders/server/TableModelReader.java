package org.openswing.swing.pivottable.tablemodelreaders.server;

import javax.swing.table.TableModel;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.HashMap;
import org.openswing.swing.pivottable.java.InputFilter;

/**
 * <p>Title: OpenSwing Framework</p>
 * <p>Description: TableModel reader, used in PivotTableEngine to read data to analyze.</p>
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
public class TableModelReader implements Reader {

  /** TableModel to analyze */
  private TableModel model = null;

  /** current row to analyze */
  private int currentRow = -1;

  /** collection of pairs <column name,column index> */
  private HashMap indexes = new HashMap();


  /**
   * Create a reader of TableModel, used in PivotTableEngine.
   * @param model TableModel to analyze
   */
  public TableModelReader(TableModel model) {
    this.model = model;
    for(int i=0;i<model.getColumnCount();i++)
      indexes.put(getColumnName(i),new Integer(i));
  }


  /**
   * Create a reader of a list of ValueObjects, used in PivotTableEngine.
   * @param valueObjects list of ValueObjects to analyze
   * @param attributeNames nome of attributes defined within the ValueObjects, used to define the TableModel
   */
  public TableModelReader(ArrayList valueObjects,String[] attributeNames) {
    this(new VOTableModel(valueObjects,attributeNames));
  }


  /**
   * Initialize reading.
   * @param inputFilter optional input filter, used to skip rows
   * @return <code>true</code> if reader has correctly initialize data to read, <code>false</code> otherwise
   */
  public boolean initializeScrolling(InputFilter inputFilter) {
    currentRow = -1;
    return true;
  }


  /**
   * Get next row to read.
   * Note: this method can be called ONLY if "initializeScrolling" method has already been invoked.
   * @param inputFilter optional input filter, used to skip rows
   * @return <code>boolean</code> true if there exists a row, <code>false</code> if no other rows are available
   */
  public final boolean nextRow(InputFilter inputFilter) {
    currentRow++;
    Iterator it = null;
    boolean skipRow;
    while(currentRow<model.getRowCount() && inputFilter!=null) {
      it = inputFilter.getFilteredColumnNames();
      String colName = null;
      skipRow = false;
      while(it.hasNext()) {
        colName = it.next().toString();
        skipRow = inputFilter.skipRow(colName,getValueAt(((Integer)indexes.get(colName)).intValue()));
        if (skipRow)
          break;
      }
      if (!skipRow)
        break;
      currentRow++;
    }
    return currentRow<model.getRowCount();
  }


  /**
   * @param col column index
   * @return value stored at column index, related to current row
   */
  public final Object getValueAt(int col) {
    return model.getValueAt(currentRow,col);
  }


  /**
   * @return number of columns defined in TableModel
   */
  public final int getColumnCount() {
    return model.getColumnCount();
  }


  /**
   * @param column index
   * @return column name defined in TableModel, related to the specified column index
   */
  public final String getColumnName(int index) {
    return model.getColumnName(index);
  }


}
