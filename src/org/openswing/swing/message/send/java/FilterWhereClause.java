package org.openswing.swing.message.send.java;

import java.io.*;


/**
 * <p>Title: OpenSwing Framework</p>
 * <p>Description: Filter where clause, used to set a WHERE condition for an attribute.
 * This object is passed to the server side through GridParams object.</p>
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
public class FilterWhereClause implements Serializable {

  /** attribute name */
  private String attributeName;

  /** operator; possible values: =, is null, is not null, <, >, <=, >= */
  private String operator;

  /** attribute value */
  private Object value;


  public FilterWhereClause() {}


  /**
   * @param attributeName attribute name
   * @param operator operator; possible values: =, is null, is not null, <, >, <=, >=
   * @param value attribute value
   */
  public FilterWhereClause(String attributeName,String operator,Object value) {
    this.attributeName = attributeName;
    this.operator = operator;
    this.value = value;
  }


  /**
   * @return attribute value
   */
  public final String getAttributeName() {
    return attributeName;
  }


  /**
   * @return operator; possible values: =, is null, is not null, <, >, <=, >=
   */
  public final String getOperator() {
    return operator;
  }


  /**
   * @return attribute value
   */
  public final Object getValue() {
    return value;
  }


  public final boolean equals(Object o) {
    if (!(o instanceof FilterWhereClause))
      return false;
    FilterWhereClause o2 = (FilterWhereClause)o;
    return attributeName.equals(o2.getAttributeName()) &&
           operator.equals(o2.getOperator()) &&
           (value==null && o2.getValue()==null || value!=null && value.equals(o2.getValue()));

  }
  public void setAttributeName(String attributeName) {
    this.attributeName = attributeName;
  }
  public void setOperator(String operator) {
    this.operator = operator;
  }
  public void setValue(Object value) {
    this.value = value;
  }


}
