package org.openswing.swing.pivottable.java;

import org.openswing.swing.pivottable.functions.java.*;
import java.io.Serializable;
import java.text.*;
import java.text.DecimalFormat;


/**
 * <p>Title: OpenSwing Framework</p>
 * <p>Description: Data field descriptor, used when defining a pivot table.</p>
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
public class DataField implements Serializable {

  /** column name in original TableModel, that identify this field */
  private String columnName;

  /** column width, expressed in pixels */
  private int width;

  /** function to apply to this field; default value: SumFunction */
  private GenericFunction function = new SumFunction();

  /** column description */
  private String description;

  /** numeric formatter */
  private NumberFormat formatter = null;


  public DataField(String columnName,int width) {
    this(columnName,width,columnName);
  }


  public DataField(String columnName,int width,String description) {
    this.columnName = columnName;
    this.width = width;
    this.description = description;
  }


  public DataField(String columnName,int width,GenericFunction function) {
    this(columnName,width,columnName,function);
  }


  public DataField(String columnName,int width,String description,GenericFunction function) {
    this(columnName,width,description);
    this.function = function;
  }


  public String getColumnName() {
    return columnName;
  }
  public int getWidth() {
    return width;
  }
  public GenericFunction getFunction() {
    return function;
  }
  public String getDescription() {
    return description;
  }


  public final boolean equals(Object obj) {
    if (obj==null || !(obj instanceof DataField))
      return false;
    return
        ((DataField)obj).getColumnName().equals(getColumnName()) &&
        ((DataField)obj).getFunction().equals(getFunction());
  }


  public final int hashCode() {
    return getColumnName().hashCode()*getFunction().hashCode();
  }


  /**
   * @return numeric formatter
   */
  public final NumberFormat getFormatter() {
    return formatter;
  }


  /**
   * Set the numeric formatter.
   * @param formatter numeric formatter
   */
  public final void setFormatter(NumberFormat formatter) {
    this.formatter = formatter;
  }

}
