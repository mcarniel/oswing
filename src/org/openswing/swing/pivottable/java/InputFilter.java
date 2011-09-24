package org.openswing.swing.pivottable.java;

import javax.swing.table.TableModel;
import java.io.Serializable;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.Date;
import java.util.Map;
import java.util.Iterator;

/**
 * <p>Title: OpenSwing Framework</p>
 * <p>Description: Row filter used within pivot table to skip rows from original data model.</p>
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
public class InputFilter implements Serializable {

  /** collection of pairs <column name,operators: ArrayList of String> */
  private HashMap operators = new HashMap();

  /** collection of pairs <column name,values: ArrayList of Objects> */
  private HashMap filterValues = new HashMap();


  public static final String EQ = "=";
  public static final String NEQ = "<>";
  public static final String LT = "<";
  public static final String LE = "<=";
  public static final String GT = ">";
  public static final String GE = ">=";
  public static final String IS_NULL = "IS NULL";
  public static final String IS_NOT_NULL = "IS NOT NULL";


  /**
   * Add an "equals to" filter condition to input data to analyze.
   * @param columnName column name
   * @param value filter value
   */
  public final void addEqualsFilter(String columnName,Object value) {
    ArrayList ops = (ArrayList)operators.get(columnName);
    ArrayList vals  = (ArrayList)filterValues.get(columnName);
    if (ops==null) {
      ops = new ArrayList();
      vals = new ArrayList();
      operators.put(columnName,ops);
      filterValues.put(columnName,vals);
    }
    ops.add(EQ);
    vals.add(value);
  }


  /**
   * Add an "NOT equals to" filter condition to input data to analyze.
   * @param columnName column name
   * @param value filter value
   */
  public final void addNotEqualsFilter(String columnName,Object value) {
    ArrayList ops = (ArrayList)operators.get(columnName);
    ArrayList vals  = (ArrayList)filterValues.get(columnName);
    if (ops==null) {
      ops = new ArrayList();
      vals = new ArrayList();
      operators.put(columnName,ops);
      filterValues.put(columnName,vals);
    }
    ops.add(NEQ);
    vals.add(value);
  }


  /**
   * Add an "is null" filter condition to input data to analyze.
   * @param columnName column name
   * @param value filter value
   */
  public final void addIsNullFilter(String columnName,Object value) {
    ArrayList ops = (ArrayList)operators.get(columnName);
    ArrayList vals  = (ArrayList)filterValues.get(columnName);
    if (ops==null) {
      ops = new ArrayList();
      vals = new ArrayList();
      operators.put(columnName,ops);
      filterValues.put(columnName,vals);
    }
    ops.add(IS_NULL);
    vals.add(value);
  }


  /**
   * Add an "is NOT null" filter condition to input data to analyze.
   * @param columnName column name
   * @param value filter value
   */
  public final void addIsNotNullFilter(String columnName,Object value) {
    ArrayList ops = (ArrayList)operators.get(columnName);
    ArrayList vals  = (ArrayList)filterValues.get(columnName);
    if (ops==null) {
      ops = new ArrayList();
      vals = new ArrayList();
      operators.put(columnName,ops);
      filterValues.put(columnName,vals);
    }
    ops.add(IS_NOT_NULL);
    vals.add(value);
  }


  /**
   * Add an "less than" filter condition to input data to analyze.
   * @param columnName column name
   * @param value filter value
   */
  public final void addLessThanFilter(String columnName,Object value) {
    ArrayList ops = (ArrayList)operators.get(columnName);
    ArrayList vals  = (ArrayList)filterValues.get(columnName);
    if (ops==null) {
      ops = new ArrayList();
      vals = new ArrayList();
      operators.put(columnName,ops);
      filterValues.put(columnName,vals);
    }
    ops.add(LT);
    vals.add(value);
  }


  /**
   * Add an "less or equals to" filter condition to input data to analyze.
   * @param columnName column name
   * @param value filter value
   */
  public final void addLessOrEqualsToFilter(String columnName,Object value) {
    ArrayList ops = (ArrayList)operators.get(columnName);
    ArrayList vals  = (ArrayList)filterValues.get(columnName);
    if (ops==null) {
      ops = new ArrayList();
      vals = new ArrayList();
      operators.put(columnName,ops);
      filterValues.put(columnName,vals);
    }
    ops.add(LE);
    vals.add(value);
  }


  /**
   * Add an "greater than" filter condition to input data to analyze.
   * @param columnName column name
   * @param value filter value
   */
  public final void addGreaterThanFilter(String columnName,Object value) {
    ArrayList ops = (ArrayList)operators.get(columnName);
    ArrayList vals  = (ArrayList)filterValues.get(columnName);
    if (ops==null) {
      ops = new ArrayList();
      vals = new ArrayList();
      operators.put(columnName,ops);
      filterValues.put(columnName,vals);
    }
    ops.add(GT);
    vals.add(value);
  }


  /**
   * Add an "greater or equals to" filter condition to input data to analyze.
   * @param columnName column name
   * @param value filter value
   */
  public final void addGreaterOrEqualsToFilter(String columnName,Object value) {
    ArrayList ops = (ArrayList)operators.get(columnName);
    ArrayList vals  = (ArrayList)filterValues.get(columnName);
    if (ops==null) {
      ops = new ArrayList();
      vals = new ArrayList();
      operators.put(columnName,ops);
      filterValues.put(columnName,vals);
    }
    ops.add(GE);
    vals.add(value);
  }


  /**
   * Remove filter conditions for the specified column name.
   * @param columnName column name
   */
  public final void removeFilters(String columnName) {
    operators.remove(columnName);
    filterValues.remove(columnName);
  }


  /**
   * Invoked by Reader class to skip rows.
   * @param columnName column name under analysis
   * @param value current value for the specified column
   * @return <code>true</code> if row must be skipped, <code>false</code> otherwise
   */
  public final boolean skipRow(String columnName,Object value) {
    ArrayList ops = (ArrayList)operators.get(columnName);
    if (ops==null)
      return false;
    ArrayList vals  = (ArrayList)filterValues.get(columnName);
    boolean skip = false;
    for(int i=0;i<ops.size();i++) {
      if (ops.get(i).equals(EQ)) {
        if (value==null && vals.get(i)==null) {
          //return false;
        }
        else if (value!=null && vals.get(i)==null)
          return true;
        else if (value==null && vals.get(i)!=null)
          return true;
        else
          skip = !value.equals(vals.get(i));
        if (skip)
          return true;
      }
      else if (ops.get(i).equals(NEQ)) {
        if (value==null && vals.get(i)==null)
          return true;
        else if (value!=null && vals.get(i)==null) {
          //return false;
        }
        else if (value==null && vals.get(i)!=null) {
          //return false;
        }
        else
          skip = value.equals(vals.get(i));
        if (skip)
          return true;
      }
      else if (ops.get(i).equals(IS_NULL)) {
        skip = value!=null;
        if (skip)
          return true;
      }
      else if (ops.get(i).equals(IS_NOT_NULL)) {
        skip = value==null;
        if (skip)
          return true;
      }
      else if (ops.get(i).equals(LT)) {
        if (value==null)
          return true;
        else if (value instanceof Number) {
          skip = ((Number)value).doubleValue()>=((Number)vals.get(i)).doubleValue();
          if (skip)
            return true;
        }
        else if (value instanceof Date) {
          skip = ((Date)value).compareTo((Date)vals.get(i))>=0;
          if (skip)
            return true;
        }
        else
          skip = value.toString().compareTo(vals.get(i).toString())>=0;
        if (skip)
          return true;
      }
      else if (ops.get(i).equals(LE)) {
        if (value==null)
          return true;
        else if (value instanceof Number) {
          skip = ((Number)value).doubleValue()>((Number)vals.get(i)).doubleValue();
          if (skip)
            return true;
        }
        else if (value instanceof Date) {
          skip = ((Date)value).compareTo((Date)vals.get(i))>0;
          if (skip)
            return true;
        }
        else
          skip = value.toString().compareTo(vals.get(i).toString())>0;
        if (skip)
          return true;
      }
      else if (ops.get(i).equals(GT)) {
        if (value==null)
          return true;
        else if (value instanceof Number) {
          skip = ((Number)value).doubleValue()<=((Number)vals.get(i)).doubleValue();
          if (skip)
            return true;
        }
        else if (value instanceof Date) {
          skip = ((Date)value).compareTo((Date)vals.get(i))<=0;
          if (skip)
            return true;
        }
        else
          skip = value.toString().compareTo(vals.get(i).toString())<=0;
        if (skip)
          return true;
      }
      else if (ops.get(i).equals(GE)) {
        if (value==null)
          return true;
        else if (value instanceof Number) {
          skip = ((Number)value).doubleValue()<((Number)vals.get(i)).doubleValue();
          if (skip)
            return true;
        }
        else if (value instanceof Date) {
          skip = ((Date)value).compareTo((Date)vals.get(i))<0;
          if (skip)
            return true;
        }
        else
          skip = value.toString().compareTo(vals.get(i).toString())<0;
        if (skip)
          return true;
      }
    }
    return false;
  }


  /**
   * @return Iterator related to column names having filters
   */
  public final Iterator getFilteredColumnNames() {
    return operators.keySet().iterator();
  }


  /**
   * @param attributesMap collection of pairs <columnName,database field>
   * @param bindVariables list of values binded to "?" variables specified in return WHERE value
   * @return WHERE clause that can be appended to a SQL query used to fetch TableModel
   */
  public final String getWhereClause(Map attributesMap,ArrayList bindVariables) {
    StringBuffer sb = new StringBuffer();
    Iterator it = getFilteredColumnNames();
    String columnName = null;
    String dbColumnName = null;
    ArrayList ops = null;
    ArrayList vals  = null;
    while(it.hasNext()) {
      columnName = it.next().toString();
      dbColumnName = (String)attributesMap.get(columnName);
      if (dbColumnName==null)
        continue;
      ops = (ArrayList)operators.get(columnName);
      vals  = (ArrayList)filterValues.get(columnName);
      if (ops!=null) {
        for(int i=0;i<ops.size();i++) {
          sb.append(dbColumnName).append(" ").append(ops.get(i));
          if (!ops.get(i).equals(IS_NULL) &&
              !ops.get(i).equals(IS_NOT_NULL)) {
            sb.append(" ? AND ");
            bindVariables.add(vals.get(i));
          }
        }
      }
    }
    if (sb.length()>0) {
      return "("+sb.toString().substring(0,sb.toString().length()-4)+")";
    }
    else
      return "";
  }


}
