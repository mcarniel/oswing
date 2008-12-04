package org.openswing.swing.pivottable.tablemodelreaders.server;

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
public interface Reader {


  /**
   * Initialize reading.
   * @param inputFilter optional input filter, used to skip rows
   * @return <code>true</code> if reader has correctly initialize data to read, <code>false</code> otherwise
   */
  public boolean initializeScrolling(InputFilter inputFilter);


  /**
   * Get next row to read.
   * Note: this method can be called ONLY if "initializeScrolling" method has already been invoked.
   * @param inputFilter optional input filter, used to skip rows
   * @return <code>boolean</code> true itf there exists a row, <code>false</code> if no other rows are available
   */
  public boolean nextRow(InputFilter inputFilter);


  /**
   * @param col column index
   * @return value stored at column index, related to current row
   */
  public Object getValueAt(int col);


  /**
   * @return number of columns defined in TableModel
   */
  public int getColumnCount();

  /**
   * @param column index
   * @return column name defined in TableModel, related to the specified column index
   */
  public String getColumnName(int index);



}
