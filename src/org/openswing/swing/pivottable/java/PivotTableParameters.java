package org.openswing.swing.pivottable.java;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * <p>Title: OpenSwing Framework</p>
 * <p>Description: Pivot table parameters, used by PivotTableEngine.</p>
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
public class PivotTableParameters implements Serializable {

  /** list of RowField objects, related to row fields in Pivot Table */
  private ArrayList rowFields = new ArrayList();

  /** list of ColumnField objects, related to column fields in Pivot Table */
  private ArrayList columnFields = new ArrayList();

  /** list of DataField objects, related to data fields in Pivot Table */
  private ArrayList dataFields = new ArrayList();

  /** optional row filter, used to skip rows from original data model */
  private InputFilter inputFilter = null;


  public PivotTableParameters() {
  }


  /**
   * @return list of ColumnField objects, related to column fields in Pivot Table
   */
  public final ArrayList getColumnFields() {
    return columnFields;
  }


  /**
   * @return list of DataField objects, related to data fields in Pivot Table
   */
  public final ArrayList getDataFields() {
    return dataFields;
  }


  /**
   * @return list of RowField objects, related to row fields in Pivot Table
   */
  public final ArrayList getRowFields() {
    return rowFields;
  }


  /**
   * @return optional row filter, used to skip rows from original data model
   */
  public final InputFilter getInputFilter() {
    return inputFilter;
  }


  /**
   * Set the optional row filter, used to skip rows from original data model.
   * @param rowFilter row filter, used to skip rows from original data model
   */
  public final void setInputFilter(InputFilter inputFilter) {
    this.inputFilter = inputFilter;
  }


}
