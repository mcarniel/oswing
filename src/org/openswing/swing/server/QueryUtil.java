package org.openswing.swing.server;

import java.lang.reflect.*;
import java.math.*;
import java.sql.*;
import java.util.*;
import javax.servlet.*;

import org.openswing.swing.internationalization.java.*;
import org.openswing.swing.logger.server.*;
import org.openswing.swing.message.receive.java.*;
import org.openswing.swing.message.send.java.*;
import org.openswing.swing.util.java.*;
import java.io.BufferedInputStream;


/**
 * <p>Title: OpenSwing Framework</p>
 * <p>Description: Utility (singleton) class used with queries.</p>
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
public class QueryUtil {


  /**
   * @param baseSQL SQL to change by adding filter and order clauses
   * @param values binding values related to baseSQL
   * @param filteredColumns columns to add in the WHERE clause
   * @param currentSortedColumns columns to add in the ORDER clause
   * @param currentSortedVersusColumns ordering versus
   * @param attributesMapping collection of pairs attributeName, corresponding database column (table.column)
   * @return baseSQL + filtering and ordering conditions
   */
  public static String getSql(
      UserSessionParameters userSessionPars,
      String baseSQL,
      ArrayList values,
      Map filteredColumns,
      ArrayList currentSortedColumns,
      ArrayList currentSortedVersusColumns,
      Map attributesMapping
  ) {
    return getSql(
      userSessionPars,
      baseSQL,
      new ArrayList(),
      values,
      filteredColumns,
      currentSortedColumns,
      currentSortedVersusColumns,
      attributesMapping
    );
  }


  /**
   * This constructor can be useful when combining OpenSwing with Hibernate, to retrieve attribute names too.
   * @param baseSQL SQL to change by adding filter and order clauses
   * @param attrNames attribute names related to filter values
   * @param values binding values related to baseSQL
   * @param filteredColumns columns to add in the WHERE clause
   * @param currentSortedColumns columns to add in the ORDER clause
   * @param currentSortedVersusColumns ordering versus
   * @param attributesMapping collection of pairs attributeName, corresponding database column (table.column)
   * @return baseSQL + filtering and ordering conditions
   */
  public static String getSql(
      UserSessionParameters userSessionPars,
      String baseSQL,
      ArrayList attrNames,
      ArrayList values,
      Map filteredColumns,
      ArrayList currentSortedColumns,
      ArrayList currentSortedVersusColumns,
      Map attributesMapping
  ) {
    return getSql(
      userSessionPars,
      baseSQL,
      attrNames,
      values,
      filteredColumns,
      currentSortedColumns,
      currentSortedVersusColumns,
      attributesMapping,
      false
    );
  }


  /**
   * This constructor can be useful when combining OpenSwing with Hibernate, to retrieve attribute names too.
   * SQL is expressed using more argument, each one without the related keyword (select, from, ...).
   *
   * Example: following query
   *
   * select customer_code,corporate_name from companies order by customer_code asc
   *
   * become an invokation of getSql:
   *
   * getSql(userSessionPars,"customer_code,corporate_name","companies","","customer_code asc","","",...);
   *
   *
   * Example: following query
   *
   * select customer_code,customername from
   * (select customer_code,corporate_name as customername from companies
   * union
   * select customer_code,name as customername from privates)
   * order by customer_code asc
   *
   * become an invokation of getSql:
   *
   * getSql(userSessionPars,"customer_code,customername","(select customer_code,corporate_name as customername from companies union select customer_code,name as customername from privates)","","customer_code asc","","",...);
   *
   * @param select list of fields for select statement
   * @param from list of tables for from statement
   * @param where where statement; may be null
   * @param group group by statement; may be null
   * @param having having statement; may be null
   * @param order list of fields for order by statement; may be null
   * @param values binding values related to baseSQL
   * @param filteredColumns columns to add in the WHERE clause
   * @param currentSortedColumns columns to add in the ORDER clause
   * @param currentSortedVersusColumns ordering versus
   * @param attributesMapping collection of pairs attributeName, corresponding database column (table.column)
   * @return baseSQL + filtering and ordering conditions
   */
  public static String getSql(
      UserSessionParameters userSessionPars,
      String select,
      String from,
      String where,
      String group,
      String having,
      String order,
      ArrayList values,
      Map filteredColumns,
      ArrayList currentSortedColumns,
      ArrayList currentSortedVersusColumns,
      Map attributesMapping
  ) {
    return getSql(
      userSessionPars,
      select,
      from,
      where,
      group,
      having,
      order,
      new ArrayList(),
      values,
      filteredColumns,
      currentSortedColumns,
      currentSortedVersusColumns,
      attributesMapping
    );
  }


  /**
   * This constructor can be useful when combining OpenSwing with Hibernate, to retrieve attribute names too.
   * SQL is expressed using more argument, each one without the related keyword (select, from, ...).
   *
   * Example: following query
   *
   * select customer_code,corporate_name from companies order by customer_code asc
   *
   * become an invokation of getSql:
   *
   * getSql(userSessionPars,"customer_code,corporate_name","companies","","customer_code asc","","",...);
   *
   *
   * Example: following query
   *
   * select customer_code,customername from
   * (select customer_code,corporate_name as customername from companies
   * union
   * select customer_code,name as customername from privates)
   * order by customer_code asc
   *
   * become an invokation of getSql:
   *
   * getSql(userSessionPars,"customer_code,customername","(select customer_code,corporate_name as customername from companies union select customer_code,name as customername from privates)","","customer_code asc","","",...);
   *
   * @param select list of fields for select statement
   * @param from list of tables for from statement
   * @param where where statement; may be null
   * @param group group by statement; may be null
   * @param having having statement; may be null
   * @param order list of fields for order by statement; may be null
   * @param attrNames attribute names related to filter values
   * @param values binding values related to baseSQL
   * @param filteredColumns columns to add in the WHERE clause
   * @param currentSortedColumns columns to add in the ORDER clause
   * @param currentSortedVersusColumns ordering versus
   * @param attributesMapping collection of pairs attributeName, corresponding database column (table.column)
   * @return baseSQL + filtering and ordering conditions
   */
  public static String getSql(
      UserSessionParameters userSessionPars,
      String select,
      String from,
      String where,
      String group,
      String having,
      String order,
      ArrayList attrNames,
      ArrayList values,
      Map filteredColumns,
      ArrayList currentSortedColumns,
      ArrayList currentSortedVersusColumns,
      Map attributesMapping
  ) {
    return getSql(
      userSessionPars,
      select,
      from,
      where,
      group,
      having,
      order,
      attrNames,
      values,
      filteredColumns,
      currentSortedColumns,
      currentSortedVersusColumns,
      attributesMapping,
      false
    );
  }


  /**
   * This constructor can be useful when combining OpenSwing with Hibernate, to retrieve attribute names too.
   * SQL is expressed using more argument, each one without the related keyword (select, from, ...).
   *
   * Example: following query
   *
   * select customer_code,corporate_name from companies order by customer_code asc
   *
   * become an invokation of getSql:
   *
   * getSql(userSessionPars,"customer_code,corporate_name","companies","","customer_code asc","","",...);
   *
   *
   * Example: following query
   *
   * select customer_code,customername from
   * (select customer_code,corporate_name as customername from companies
   * union
   * select customer_code,name as customername from privates)
   * order by customer_code asc
   *
   * become an invokation of getSql:
   *
   * getSql(userSessionPars,"customer_code,customername","(select customer_code,corporate_name as customername from companies union select customer_code,name as customername from privates)","","customer_code asc","","",...);
   *
   * @param select list of fields for select statement
   * @param from list of tables for from statement
   * @param where where statement; may be null
   * @param group group by statement; may be null
   * @param having having statement; may be null
   * @param order list of fields for order by statement; may be null
   * @param attrNames attribute names related to filter values
   * @param values binding values related to baseSQL
   * @param filteredColumns columns to add in the WHERE clause
   * @param currentSortedColumns columns to add in the ORDER clause
   * @param currentSortedVersusColumns ordering versus
   * @param attributesMapping collection of pairs attributeName, corresponding database column (table.column)
   * @param isJPAsyntax flag
   * @return baseSQL + filtering and ordering conditions
   */
  public static String getSql(
      UserSessionParameters userSessionPars,
      String select,
      String from,
      String where,
      String group,
      String having,
      String order,
      ArrayList attrNames,
      ArrayList values,
      Map filteredColumns,
      ArrayList currentSortedColumns,
      ArrayList currentSortedVersusColumns,
      Map attributesMapping,
      boolean isJPAsyntax
  ) {
    String baseSQL = "";
    if (select!=null && select.trim().length()>0)
      baseSQL += "SELECT "+select;
    baseSQL += " FROM "+from;
    try {
      if (where!=null && !where.trim().equals(""))
        baseSQL += " WHERE "+where;

      int num = values.size()+1;
      if (filteredColumns.size() > 0) {
        if (where!=null && !where.trim().equals(""))
          baseSQL += " AND ";
        else
          baseSQL += " WHERE ";

        Iterator keys = filteredColumns.keySet().iterator();
        String attributeName = null;
        FilterWhereClause[] filterClauses = null;
        while (keys.hasNext()) {
          attributeName = keys.next().toString();
          filterClauses = (FilterWhereClause[]) filteredColumns.get(attributeName);
          if (  filterClauses[0].getValue()!=null &&
              !(filterClauses[0].getOperator().equals(Consts.IS_NOT_NULL) || filterClauses[0].getOperator().equals(Consts.IS_NULL))) {
            if (filterClauses[0].getValue() instanceof ArrayList) {
              // name IN (...)
              // name NOT IN (...)
              // (name op value1 OR name op value2 OR ...)
              if (filterClauses[0].getOperator().equals(Consts.IN) ||
                  filterClauses[0].getOperator().equals(Consts.NOT_IN)) {
                // name IN (...)
                // name NOT IN (...)
                baseSQL +=
                  attributesMapping.get(attributeName) +
                  " " + filterClauses[0].getOperator() +
                  " (";
                ArrayList inValues = (ArrayList)filterClauses[0].getValue();
                for(int j=0;j<inValues.size();j++) {
                  baseSQL += " ?"+(isJPAsyntax?String.valueOf(num++):"")+",";
                  values.add(inValues.get(j));
                }
                baseSQL = baseSQL.substring(0,baseSQL.length()-1);
                baseSQL += ") AND ";
              }
              else {
                // (name op value1 OR name op value2 OR ...)
                int i =0; // start index
                boolean isExamineNull = false;
                baseSQL += "(";
                ArrayList inValues = (ArrayList)filterClauses[0].getValue();
                if ( filterClauses[0].getOperator().equals(Consts.EQ) &&
                     inValues.get(0).equals(Consts.IS_NULL)) {
                  i =1;
                  isExamineNull = true;
                  baseSQL += "("+ attributesMapping.get(attributeName) + " IS NULL) OR (";
                }
                if ( filterClauses[0].getOperator().equals(Consts.NEQ) &&
                     inValues.get(0).equals(Consts.IS_NULL)) {
                  i =1;
                  isExamineNull = true;
                  baseSQL += "("+ attributesMapping.get(attributeName) + " IS NOT NULL) OR (";
                }
                for(int j=i;j<inValues.size();j++) {
                  baseSQL +=
                    attributesMapping.get(attributeName) +
                    " " + filterClauses[0].getOperator() +
                    " ?"+(isJPAsyntax?String.valueOf(num++):"")+" OR ";
                    values.add(inValues.get(j));
                }
                baseSQL = baseSQL.substring(0,baseSQL.length()-3);
                if (isExamineNull)
                  baseSQL += ")";
                baseSQL += ") AND ";
              }
            } else {

/*
              else {
                // (name op value1 OR name op value2 OR ...)
                baseSQL += "(";
                ArrayList inValues = (ArrayList)filterClauses[0].getValue();
                for(int j=0;j<inValues.size();j++) {
                  baseSQL +=
                      attributesMapping.get(attributeName) +
                      " " + filterClauses[0].getOperator() +
                      " ?"+(isJPAsyntax?String.valueOf(num++):"")+" OR ";
                  values.add(inValues.get(j));
                }
                baseSQL = baseSQL.substring(0,baseSQL.length()-3);
                baseSQL += ") AND ";
              }
            } else {
 */
              // name op value
              baseSQL +=
                  attributesMapping.get(attributeName) +
                  " " + filterClauses[0].getOperator() +
                  " ?"+(isJPAsyntax?String.valueOf(num++):"")+" AND ";
              values.add(filterClauses[0].getValue());
            }
          }
          else {
            // name IS NULL
            // name IS NOT NULL
            baseSQL +=
                  attributesMapping.get(attributeName) +
                  " " + filterClauses[0].getOperator() + " " +
                  "AND ";
          }
          attrNames.add(filterClauses[0].getAttributeName());
          if (filterClauses[1] != null) {
            if (  filterClauses[1].getValue()!=null &&
                !(filterClauses[1].getOperator().equals(Consts.IS_NOT_NULL) || filterClauses[1].getOperator().equals(Consts.IS_NULL))) {
              if (filterClauses[1].getValue() instanceof ArrayList) {
                // name IN (...)
                // name NOT IN (...)
                // (name op value1 OR name op value2 OR ...)
                if (filterClauses[1].getOperator().equals(Consts.IN) ||
                    filterClauses[1].getOperator().equals(Consts.NOT_IN)) {
                  // name IN (...)
                  // name NOT IN (...)
                  baseSQL +=
                    attributesMapping.get(attributeName) +
                    " " + filterClauses[1].getOperator() +
                    " (";
                  ArrayList inValues = (ArrayList)filterClauses[1].getValue();
                  for(int j=0;j<inValues.size();j++) {
                    baseSQL += " ?"+(isJPAsyntax?String.valueOf(num++):"")+",";
                    values.add(inValues.get(j));
                  }
                  baseSQL = baseSQL.substring(0,baseSQL.length()-1);
                  baseSQL += ") AND ";
                }
                else {
                  // (name op value1 OR name op value2 OR ...)
                  baseSQL += "(";
                  ArrayList inValues = (ArrayList)filterClauses[1].getValue();
                  for(int j=0;j<inValues.size();j++) {
                    baseSQL +=
                        attributesMapping.get(attributeName) +
                        " " + filterClauses[1].getOperator() +
                        " ?"+(isJPAsyntax?String.valueOf(num++):"")+" OR ";
                    values.add(inValues.get(j));
                  }
                  baseSQL = baseSQL.substring(0,baseSQL.length()-3);
                  baseSQL += ") AND ";
                }
              } else {
                // name op value
                baseSQL +=
                    attributesMapping.get(attributeName) +
                    " " + filterClauses[1].getOperator() +
                    " ?"+(isJPAsyntax?String.valueOf(num++):"")+" AND ";
                values.add(filterClauses[1].getValue());
              }
            }
            else {
              // name IS NULL
              // name IS NOT NULL
              baseSQL +=
                    attributesMapping.get(attributeName) +
                    " " + filterClauses[1].getOperator() + " " +
                    "AND ";
            }
            attrNames.add(filterClauses[1].getAttributeName());
          }
        }
        baseSQL = baseSQL.substring(0, baseSQL.length() - 4);
      }

      if (group!=null && !group.trim().equals(""))
        baseSQL += " GROUP BY "+group;
      if (having!=null && !having.trim().equals(""))
        baseSQL += " HAVING "+having;
      if (order!=null && !order.trim().equals(""))
        baseSQL += " ORDER BY "+order;

      if (currentSortedColumns.size() > 0) {
        if (order!=null && !order.trim().equals(""))
          baseSQL += ", ";
        else
        baseSQL += " ORDER BY ";

        for (int i = 0; i < currentSortedColumns.size(); i++) {
          baseSQL +=
              (attributesMapping.get(currentSortedColumns.get(i))==null?currentSortedColumns.get(i):attributesMapping.get(currentSortedColumns.get(i))) +
              " " +
              currentSortedVersusColumns.get(i) + ", ";
        }
        baseSQL = baseSQL.substring(0, baseSQL.length() - 2);
      }

    }
    catch (Throwable ex) {
      Logger.error(
          userSessionPars!=null?userSessionPars.getUsername():null,
          "org.openswing.swing.server.QueryUtil",
          "getSql",
          "Error while composing the SQL:\n"+ex.getMessage(),
          ex
      );
    }

    return baseSQL;
  }


  /**
   * This constructor can be useful when combining OpenSwing with Hibernate, to retrieve attribute names too.
   * @param baseSQL SQL to change by adding filter and order clauses
   * @param attrNames attribute names related to filter values
   * @param values binding values related to baseSQL
   * @param filteredColumns columns to add in the WHERE clause
   * @param currentSortedColumns columns to add in the ORDER clause
   * @param currentSortedVersusColumns ordering versus
   * @param attributesMapping collection of pairs attributeName, corresponding database column (table.column)
   * @param isJPAsyntax flag
   * @return baseSQL + filtering and ordering conditions

   */
  public static String getSql(
      UserSessionParameters userSessionPars,
      String baseSQL,
      ArrayList attrNames,
      ArrayList values,
      Map filteredColumns,
      ArrayList currentSortedColumns,
      ArrayList currentSortedVersusColumns,
      Map attributesMapping,
      boolean isJPAsyntax
  ) {
    try {
      baseSQL = " "+baseSQL.replace('\n',' ').replace('\r',' ')+" ";
      String lowerSQL = baseSQL.toLowerCase();
      int s1 = lowerSQL.indexOf("select "); // may be "" or null for a base SQL written for JPA/ORM layer
      int f1 = lowerSQL.indexOf(" from ");
      int w1 = lowerSQL.indexOf(" where ");
      int g1 = lowerSQL.indexOf(" group by ");
      int h1 = lowerSQL.indexOf(" having ");
      int o1 = lowerSQL.indexOf(" order by ");
      int s2,f2,w2,g2,h2,o2;

      if (o1==-1) {
        o1 = baseSQL.length()-1;
        o2 = o1;
      }
      else {
        o2 = o1+10;
      }
      if (h1==-1) {
        h1 = o1;
        h2 = h1;
      }
      else {
        h2 = h1+8;
      }
      if (g1==-1) {
        g1 = h1;
        g2 = g1;
      }
      else {
        g2 = g1+10;
      }
      if (w1==-1) {
        w1 = g1;
        w2 = w1;
      }
      else {
        w2 = w1+7;
      }
      f2 = f1+6;
      if (s1==-1) {
        s1 = f1;
        s2 = s1;
      }
      else {
        s2 = s1+7;
      }

      return getSql(
        userSessionPars,
        baseSQL.substring(s2,f1).trim(), // select
        baseSQL.substring(f2,w1).trim(), // from
        baseSQL.substring(w2,g1).trim(), // where
        baseSQL.substring(g2,h1).trim(), // group by
        baseSQL.substring(h2,o1).trim(), // having
        baseSQL.substring(o2).trim(), // order by
        attrNames,
        values,
        filteredColumns,
        currentSortedColumns,
        currentSortedVersusColumns,
        attributesMapping,
        isJPAsyntax
      );


    }
    catch (Throwable ex) {
      Logger.error(
          userSessionPars!=null?userSessionPars.getUsername():null,
          "org.openswing.swing.server.QueryUtil",
          "getSql",
          "Error while composing the SQL:\n"+ex.getMessage(),
          ex
      );
    }

    return baseSQL;
  }


  /**
   * This method read the WHOLE result set.
   * @param baseSQL SQL to change by adding filter and order clauses
   * @param values binding values related to baseSQL
   * @param attribute2dbField collection of pairs attributeName, corresponding database column (table.column) - for ALL fields is the select clause
   * @param valueObjectClass value object class to use to generate the result
   * @param booleanTrueValue read value to interpret as true
   * @param booleanFalseValue read value to interpret as false
   * @param context servlet context; this may be null
   * @param gridParams grid parameters (filtering/ordering settings, starting row to read, read versus)
   * @param logQuery <code>true</code> to log the query, <code>false</code> to no log the query
   * @return a list of value objects (in VOListResponse object) or an error response
   */
  public static Response getQuery(
      Connection conn,
      String baseSQL,
      ArrayList values,
      Map attribute2dbField,
      Class valueObjectClass,
      String booleanTrueValue,
      String booleanFalseValue,
      GridParams gridParams,
      boolean logQuery
  ) throws Exception {
    return getQuery(
      conn,
      new UserSessionParameters(),
      baseSQL,
      values,
      attribute2dbField,
      valueObjectClass,
      booleanTrueValue,
      booleanFalseValue,
      null,
      gridParams,
      -1,
      0,
      logQuery
    );
  }


  /**
   * This method read the WHOLE result set.
   * SQL is expressed using more argument, each one without the related keyword (select, from, ...).
   *
   * Example: following query
   *
   * select customer_code,corporate_name from companies order by customer_code asc
   *
   * become an invokation of getSql:
   *
   * getSql(userSessionPars,"customer_code,corporate_name","companies","","customer_code asc","","",...);
   *
   *
   * Example: following query
   *
   * select customer_code,customername from
   * (select customer_code,corporate_name as customername from companies
   * union
   * select customer_code,name as customername from privates)
   * order by customer_code asc
   *
   * become an invokation of getSql:
   *
   * getSql(userSessionPars,"customer_code,customername","(select customer_code,corporate_name as customername from companies union select customer_code,name as customername from privates)","","customer_code asc","","",...);
   *
   * @param select list of fields for select statement
   * @param from list of tables for from statement
   * @param where where statement; may be null
   * @param group group by statement; may be null
   * @param having having statement; may be null
   * @param order list of fields for order by statement; may be null
   * @param values binding values related to baseSQL
   * @param attribute2dbField collection of pairs attributeName, corresponding database column (table.column) - for ALL fields is the select clause
   * @param valueObjectClass value object class to use to generate the result
   * @param booleanTrueValue read value to interpret as true
   * @param booleanFalseValue read value to interpret as false
   * @param context servlet context; this may be null
   * @param gridParams grid parameters (filtering/ordering settings, starting row to read, read versus)
   * @param logQuery <code>true</code> to log the query, <code>false</code> to no log the query
   * @return a list of value objects (in VOListResponse object) or an error response
   */
  public static Response getQuery(
      Connection conn,
      String select,
      String from,
      String where,
      String group,
      String having,
      String order,
      ArrayList values,
      Map attribute2dbField,
      Class valueObjectClass,
      String booleanTrueValue,
      String booleanFalseValue,
      GridParams gridParams,
      boolean logQuery
  ) throws Exception {
    return getQuery(
      conn,
      new UserSessionParameters(),
      select,
      from,
      where,
      group,
      having,
      order,
      values,
      attribute2dbField,
      valueObjectClass,
      booleanTrueValue,
      booleanFalseValue,
      null,
      gridParams,
      -1,
      0,
      logQuery
    );
  }


  /**
   * This method read the WHOLE result set.
   * @param baseSQL SQL to change by adding filter and order clauses
   * @param values binding values related to baseSQL
   * @param attribute2dbField collection of pairs attributeName, corresponding database column (table.column) - for ALL fields is the select clause
   * @param valueObjectClass value object class to use to generate the result
   * @param booleanTrueValue read value to interpret as true
   * @param booleanFalseValue read value to interpret as false
   * @param context servlet context; this may be null
   * @param gridParams grid parameters (filtering/ordering settings, starting row to read, read versus)
   * @param logQuery <code>true</code> to log the query, <code>false</code> to no log the query
   * @return a list of value objects (in VOListResponse object) or an error response
   */
  public static Response getQuery(
      Connection conn,
      UserSessionParameters userSessionPars,
      String baseSQL,
      ArrayList values,
      Map attribute2dbField,
      Class valueObjectClass,
      String booleanTrueValue,
      String booleanFalseValue,
      ServletContext context,
      GridParams gridParams,
      boolean logQuery
  ) throws Exception {
    return getQuery(
      conn,
      userSessionPars,
      baseSQL,
      values,
      attribute2dbField,
      valueObjectClass,
      booleanTrueValue,
      booleanFalseValue,
      context,
      gridParams,
      -1,
      0,
      logQuery
    );
  }


  /**
   * This method read the WHOLE result set.
   * SQL is expressed using more argument, each one without the related keyword (select, from, ...).
   *
   * Example: following query
   *
   * select customer_code,corporate_name from companies order by customer_code asc
   *
   * become an invokation of getSql:
   *
   * getSql(userSessionPars,"customer_code,corporate_name","companies","","customer_code asc","","",...);
   *
   *
   * Example: following query
   *
   * select customer_code,customername from
   * (select customer_code,corporate_name as customername from companies
   * union
   * select customer_code,name as customername from privates)
   * order by customer_code asc
   *
   * become an invokation of getSql:
   *
   * getSql(userSessionPars,"customer_code,customername","(select customer_code,corporate_name as customername from companies union select customer_code,name as customername from privates)","","customer_code asc","","",...);
   *
   * @param select list of fields for select statement
   * @param from list of tables for from statement
   * @param where where statement; may be null
   * @param group group by statement; may be null
   * @param having having statement; may be null
   * @param order list of fields for order by statement; may be null
   * @param values binding values related to baseSQL
   * @param attribute2dbField collection of pairs attributeName, corresponding database column (table.column) - for ALL fields is the select clause
   * @param valueObjectClass value object class to use to generate the result
   * @param booleanTrueValue read value to interpret as true
   * @param booleanFalseValue read value to interpret as false
   * @param context servlet context; this may be null
   * @param gridParams grid parameters (filtering/ordering settings, starting row to read, read versus)
   * @param logQuery <code>true</code> to log the query, <code>false</code> to no log the query
   * @return a list of value objects (in VOListResponse object) or an error response
   */
  public static Response getQuery(
      Connection conn,
      UserSessionParameters userSessionPars,
      String select,
      String from,
      String where,
      String group,
      String having,
      String order,
      ArrayList values,
      Map attribute2dbField,
      Class valueObjectClass,
      String booleanTrueValue,
      String booleanFalseValue,
      ServletContext context,
      GridParams gridParams,
      boolean logQuery
  ) throws Exception {
    return getQuery(
      conn,
      userSessionPars,
      select,
      from,
      where,
      group,
      having,
      order,
      values,
      attribute2dbField,
      valueObjectClass,
      booleanTrueValue,
      booleanFalseValue,
      context,
      gridParams,
      -1,
      0,
      logQuery
    );
  }


  /**
   * This method read a block of record from the result set.
   * @param baseSQL SQL to change by adding filter and order clauses
   * @param values binding values related to baseSQL
   * @param attribute2dbField collection of pairs attributeName, corresponding database column (table.column) - for ALL fields is the select clause
   * @param valueObjectClass value object class to use to generate the result
   * @param booleanTrueValue read value to interpret as true
   * @param booleanFalseValue read value to interpret as false
   * @param gridParams grid parameters (filtering/ordering settings, starting row to read, read versus)
   * @param blockSize number of rows to read
   * @param logQuery <code>true</code> to log the query, <code>false</code> to no log the query
   * @return a list of value objects  (in VOListResponse object) or an error response
   */
  public static Response getQuery(
      Connection conn,
      String baseSQL,
      ArrayList values,
      Map attribute2dbField,
      Class valueObjectClass,
      String booleanTrueValue,
      String booleanFalseValue,
      GridParams gridParams,
      int blockSize,
      boolean logQuery
  ) throws Exception {
    return getQuery(
      conn,
      new UserSessionParameters(),
      baseSQL,
      values,
      attribute2dbField,
      valueObjectClass,
      booleanTrueValue,
      booleanFalseValue,
      null,
      gridParams,
      blockSize,
      1,
      logQuery
    );
  }


  /**
   * This method read a block of record from the result set.
   * SQL is expressed using more argument, each one without the related keyword (select, from, ...).
   *
   * Example: following query
   *
   * select customer_code,corporate_name from companies order by customer_code asc
   *
   * become an invokation of getSql:
   *
   * getSql(userSessionPars,"customer_code,corporate_name","companies","","customer_code asc","","",...);
   *
   *
   * Example: following query
   *
   * select customer_code,customername from
   * (select customer_code,corporate_name as customername from companies
   * union
   * select customer_code,name as customername from privates)
   * order by customer_code asc
   *
   * become an invokation of getSql:
   *
   * getSql(userSessionPars,"customer_code,customername","(select customer_code,corporate_name as customername from companies union select customer_code,name as customername from privates)","","customer_code asc","","",...);
   *
   * @param select list of fields for select statement
   * @param from list of tables for from statement
   * @param where where statement; may be null
   * @param group group by statement; may be null
   * @param having having statement; may be null
   * @param order list of fields for order by statement; may be null
   * @param values binding values related to baseSQL
   * @param attribute2dbField collection of pairs attributeName, corresponding database column (table.column) - for ALL fields is the select clause
   * @param valueObjectClass value object class to use to generate the result
   * @param booleanTrueValue read value to interpret as true
   * @param booleanFalseValue read value to interpret as false
   * @param gridParams grid parameters (filtering/ordering settings, starting row to read, read versus)
   * @param blockSize number of rows to read
   * @param logQuery <code>true</code> to log the query, <code>false</code> to no log the query
   * @return a list of value objects  (in VOListResponse object) or an error response
   */
  public static Response getQuery(
      Connection conn,
      String select,
      String from,
      String where,
      String group,
      String having,
      String order,
      ArrayList values,
      Map attribute2dbField,
      Class valueObjectClass,
      String booleanTrueValue,
      String booleanFalseValue,
      GridParams gridParams,
      int blockSize,
      boolean logQuery
  ) throws Exception {
    return getQuery(
      conn,
      new UserSessionParameters(),
      select,
      from,
      where,
      group,
      having,
      order,
      values,
      attribute2dbField,
      valueObjectClass,
      booleanTrueValue,
      booleanFalseValue,
      null,
      gridParams,
      blockSize,
      1,
      logQuery
    );
  }


  /**
   * This method read a block of record from the result set.
   * @param baseSQL SQL to change by adding filter and order clauses
   * @param values binding values related to baseSQL
   * @param attribute2dbField collection of pairs attributeName, corresponding database column (table.column) - for ALL fields is the select clause
   * @param valueObjectClass value object class to use to generate the result
   * @param booleanTrueValue read value to interpret as true
   * @param booleanFalseValue read value to interpret as false
   * @param context servlet context; this may be null
   * @param gridParams grid parameters (filtering/ordering settings, starting row to read, read versus)
   * @param blockSize number of rows to read
   * @param logQuery <code>true</code> to log the query, <code>false</code> to no log the query
   * @return a list of value objects  (in VOListResponse object) or an error response
   */
  public static Response getQuery(
      Connection conn,
      UserSessionParameters userSessionPars,
      String baseSQL,
      ArrayList values,
      Map attribute2dbField,
      Class valueObjectClass,
      String booleanTrueValue,
      String booleanFalseValue,
      ServletContext context,
      GridParams gridParams,
      int blockSize,
      boolean logQuery
  ) throws Exception {
    return getQuery(
      conn,
      userSessionPars,
      baseSQL,
      values,
      attribute2dbField,
      valueObjectClass,
      booleanTrueValue,
      booleanFalseValue,
      context,
      gridParams,
      blockSize,
      1,
      logQuery
    );
  }


  /**
   * This method read a block of record from the result set.
   * @param baseSQL SQL to change by adding filter and order clauses
   * @param values binding values related to baseSQL
   * @param attribute2dbField collection of pairs attributeName, corresponding database column (table.column) - for ALL fields is the select clause
   * @param valueObjectClass value object class to use to generate the result
   * @param booleanTrueValue read value to interpret as true
   * @param booleanFalseValue read value to interpret as false
   * @param context servlet context; this may be null
   * @param gridParams grid parameters (filtering/ordering settings, starting row to read, read versus)
   * @param blockSize number of rows to read
   * @param logQuery <code>true</code> to log the query, <code>false</code> to no log the query
   * @param fetchTotalResultSetLength fetch all result set length; be careful: this task could slow down the data retrieval
   * @return a list of value objects  (in VOListResponse object) or an error response
   */
  public static Response getQuery(
      Connection conn,
      UserSessionParameters userSessionPars,
      String baseSQL,
      ArrayList values,
      Map attribute2dbField,
      Class valueObjectClass,
      String booleanTrueValue,
      String booleanFalseValue,
      ServletContext context,
      GridParams gridParams,
      int blockSize,
      boolean logQuery,
      boolean fetchTotalResultSetLength
  ) throws Exception {
    return getQuery(
      conn,
      userSessionPars,
      baseSQL,
      values,
      attribute2dbField,
      valueObjectClass,
      booleanTrueValue,
      booleanFalseValue,
      context,
      gridParams,
      blockSize,
      1,
      logQuery,
      fetchTotalResultSetLength
    );
  }


  /**
   * This method read a block of record from the result set.
   * SQL is expressed using more argument, each one without the related keyword (select, from, ...).
   *
   * Example: following query
   *
   * select customer_code,corporate_name from companies order by customer_code asc
   *
   * become an invokation of getSql:
   *
   * getSql(userSessionPars,"customer_code,corporate_name","companies","","customer_code asc","","",...);
   *
   *
   * Example: following query
   *
   * select customer_code,customername from
   * (select customer_code,corporate_name as customername from companies
   * union
   * select customer_code,name as customername from privates)
   * order by customer_code asc
   *
   * become an invokation of getSql:
   *
   * getSql(userSessionPars,"customer_code,customername","(select customer_code,corporate_name as customername from companies union select customer_code,name as customername from privates)","","customer_code asc","","",...);
   *
   * @param select list of fields for select statement
   * @param from list of tables for from statement
   * @param where where statement; may be null
   * @param group group by statement; may be null
   * @param having having statement; may be null
   * @param order list of fields for order by statement; may be null
   * @param values binding values related to baseSQL
   * @param attribute2dbField collection of pairs attributeName, corresponding database column (table.column) - for ALL fields is the select clause
   * @param valueObjectClass value object class to use to generate the result
   * @param booleanTrueValue read value to interpret as true
   * @param booleanFalseValue read value to interpret as false
   * @param context servlet context; this may be null
   * @param gridParams grid parameters (filtering/ordering settings, starting row to read, read versus)
   * @param blockSize number of rows to read
   * @param logQuery <code>true</code> to log the query, <code>false</code> to no log the query
   * @return a list of value objects  (in VOListResponse object) or an error response
   */
  public static Response getQuery(
      Connection conn,
      UserSessionParameters userSessionPars,
      String select,
      String from,
      String where,
      String group,
      String having,
      String order,
      ArrayList values,
      Map attribute2dbField,
      Class valueObjectClass,
      String booleanTrueValue,
      String booleanFalseValue,
      ServletContext context,
      GridParams gridParams,
      int blockSize,
      boolean logQuery
  ) throws Exception {
    return getQuery(
      conn,
      userSessionPars,
      select,
      from,
      where,
      group,
      having,
      order,
      values,
      attribute2dbField,
      valueObjectClass,
      booleanTrueValue,
      booleanFalseValue,
      context,
      gridParams,
      blockSize,
      1,
      logQuery
    );
  }


  /**
   * @param baseSQL SQL to change by adding filter and order clauses
   * @param values binding values related to baseSQL
   * @param attribute2dbField collection of pairs attributeName, corresponding database column (table.column) - for ALL fields is the select clause
   * @param valueObjectClass value object class to use to generate the result
   * @param booleanTrueValue read value to interpret as true
   * @param booleanFalseValue read value to interpret as false
   * @param logQuery <code>true</code> to log the query, <code>false</code> to no log the query
   * @return one value object (in VOResponse object) or an error response
   */
  public static Response getQuery(
      Connection conn,
      String baseSQL,
      ArrayList values,
      Map attribute2dbField,
      Class valueObjectClass,
      String booleanTrueValue,
      String booleanFalseValue,
      boolean logQuery
  ) throws Exception {
    return getQuery(
        conn,
        new UserSessionParameters(),
        baseSQL,
        values,
        attribute2dbField,
        valueObjectClass,
        booleanTrueValue,
        booleanFalseValue,
        null,
        new GridParams(),
        -1,
        2,
        logQuery
    );
  }


  /**
   * SQL is expressed using more argument, each one without the related keyword (select, from, ...).
   *
   * Example: following query
   *
   * select customer_code,corporate_name from companies order by customer_code asc
   *
   * become an invokation of getSql:
   *
   * getSql(userSessionPars,"customer_code,corporate_name","companies","","customer_code asc","","",...);
   *
   *
   * Example: following query
   *
   * select customer_code,customername from
   * (select customer_code,corporate_name as customername from companies
   * union
   * select customer_code,name as customername from privates)
   * order by customer_code asc
   *
   * become an invokation of getSql:
   *
   * getSql(userSessionPars,"customer_code,customername","(select customer_code,corporate_name as customername from companies union select customer_code,name as customername from privates)","","customer_code asc","","",...);
   *
   * @param select list of fields for select statement
   * @param from list of tables for from statement
   * @param where where statement; may be null
   * @param group group by statement; may be null
   * @param having having statement; may be null
   * @param order list of fields for order by statement; may be null
   * @param values binding values related to baseSQL
   * @param attribute2dbField collection of pairs attributeName, corresponding database column (table.column) - for ALL fields is the select clause
   * @param valueObjectClass value object class to use to generate the result
   * @param booleanTrueValue read value to interpret as true
   * @param booleanFalseValue read value to interpret as false
   * @param logQuery <code>true</code> to log the query, <code>false</code> to no log the query
   * @return one value object (in VOResponse object) or an error response
   */
  public static Response getQuery(
      Connection conn,
      String select,
      String from,
      String where,
      String group,
      String having,
      String order,
      ArrayList values,
      Map attribute2dbField,
      Class valueObjectClass,
      String booleanTrueValue,
      String booleanFalseValue,
      boolean logQuery
  ) throws Exception {
    return getQuery(
        conn,
        new UserSessionParameters(),
        select,
        from,
        where,
        group,
        having,
        order,
        values,
        attribute2dbField,
        valueObjectClass,
        booleanTrueValue,
        booleanFalseValue,
        null,
        new GridParams(),
        -1,
        2,
        logQuery
    );
  }


  /**
   * @param baseSQL SQL to change by adding filter and order clauses
   * @param values binding values related to baseSQL
   * @param attribute2dbField collection of pairs attributeName, corresponding database column (table.column) - for ALL fields is the select clause
   * @param valueObjectClass value object class to use to generate the result
   * @param booleanTrueValue read value to interpret as true
   * @param booleanFalseValue read value to interpret as false
   * @param context servlet context; this may be null
   * @param logQuery <code>true</code> to log the query, <code>false</code> to no log the query
   * @return one value object (in VOResponse object) or an error response
   */
  public static Response getQuery(
      Connection conn,
      UserSessionParameters userSessionPars,
      String baseSQL,
      ArrayList values,
      Map attribute2dbField,
      Class valueObjectClass,
      String booleanTrueValue,
      String booleanFalseValue,
      ServletContext context,
      boolean logQuery
  ) throws Exception {
    return getQuery(
        conn,
        userSessionPars,
        baseSQL,
        values,
        attribute2dbField,
        valueObjectClass,
        booleanTrueValue,
        booleanFalseValue,
        context,
        new GridParams(),
        -1,
        2,
        logQuery
    );
  }


  /**
   * SQL is expressed using more argument, each one without the related keyword (select, from, ...).
   *
   * Example: following query
   *
   * select customer_code,corporate_name from companies order by customer_code asc
   *
   * become an invokation of getSql:
   *
   * getSql(userSessionPars,"customer_code,corporate_name","companies","","customer_code asc","","",...);
   *
   *
   * Example: following query
   *
   * select customer_code,customername from
   * (select customer_code,corporate_name as customername from companies
   * union
   * select customer_code,name as customername from privates)
   * order by customer_code asc
   *
   * become an invokation of getSql:
   *
   * getSql(userSessionPars,"customer_code,customername","(select customer_code,corporate_name as customername from companies union select customer_code,name as customername from privates)","","customer_code asc","","",...);
   *
   * @param select list of fields for select statement
   * @param from list of tables for from statement
   * @param where where statement; may be null
   * @param group group by statement; may be null
   * @param having having statement; may be null
   * @param order list of fields for order by statement; may be null
   * @param values binding values related to baseSQL
   * @param attribute2dbField collection of pairs attributeName, corresponding database column (table.column) - for ALL fields is the select clause
   * @param valueObjectClass value object class to use to generate the result
   * @param booleanTrueValue read value to interpret as true
   * @param booleanFalseValue read value to interpret as false
   * @param context servlet context; this may be null
   * @param logQuery <code>true</code> to log the query, <code>false</code> to no log the query
   * @return one value object (in VOResponse object) or an error response
   */
  public static Response getQuery(
      Connection conn,
      UserSessionParameters userSessionPars,
      String select,
      String from,
      String where,
      String group,
      String having,
      String order,
      ArrayList values,
      Map attribute2dbField,
      Class valueObjectClass,
      String booleanTrueValue,
      String booleanFalseValue,
      ServletContext context,
      boolean logQuery
  ) throws Exception {
    return getQuery(
        conn,
        userSessionPars,
        select,
        from,
        where,
        group,
        having,
        order,
        values,
        attribute2dbField,
        valueObjectClass,
        booleanTrueValue,
        booleanFalseValue,
        context,
        new GridParams(),
        -1,
        2,
        logQuery
    );
  }


  /**
   * @param baseSQL SQL to change by adding filter and order clauses
   * @param values binding values related to baseSQL
   * @param attribute2dbField collection of pairs attributeName, corresponding database column (table.column) - for ALL fields is the select clause
   * @param valueObjectClass value object class to use to generate the result
   * @param booleanTrueValue read value to interpret as true
   * @param booleanFalseValue read value to interpret as false
   * @param context servlet context; this may be null
   * @param gridParams grid parameters (filtering/ordering settings, starting row to read, read versus)
   * @param blockSize number of rows to read
   * @param rowsToRead 0 = all rows, 1 = a block of rows, 2 = only one row
   * @param logQuery <code>true</code> to log the query, <code>false</code> to no log the query
   * @return a list of value objects or an error response
   */
  private static Response getQuery(
      Connection conn,
      UserSessionParameters userSessionPars,
      String baseSQL,
      ArrayList values,
      Map attribute2dbField,
      Class valueObjectClass,
      String booleanTrueValue,
      String booleanFalseValue,
      ServletContext context,
      GridParams gridParams,
      int blockSize,
      int rowsToRead,
      boolean logQuery
  ) throws Exception {
    baseSQL = getSql(
      userSessionPars,
      baseSQL,
      values,
      gridParams.getFilteredColumns(),
      gridParams.getCurrentSortedColumns(),
      gridParams.getCurrentSortedVersusColumns(),
      attribute2dbField
    );


    return getQuery(
      conn,
      userSessionPars,
      baseSQL,
      null,
      null,
      null,
      null,
      null,
      null,
      values,
      attribute2dbField,
      valueObjectClass,
      booleanTrueValue,
      booleanFalseValue,
      context,
      blockSize,
      rowsToRead,
      logQuery,
      gridParams.getAction(),
      gridParams.getStartPos(),
      false
    );
  }


  /**
   * @param baseSQL SQL to change by adding filter and order clauses
   * @param values binding values related to baseSQL
   * @param attribute2dbField collection of pairs attributeName, corresponding database column (table.column) - for ALL fields is the select clause
   * @param valueObjectClass value object class to use to generate the result
   * @param booleanTrueValue read value to interpret as true
   * @param booleanFalseValue read value to interpret as false
   * @param context servlet context; this may be null
   * @param gridParams grid parameters (filtering/ordering settings, starting row to read, read versus)
   * @param blockSize number of rows to read
   * @param rowsToRead 0 = all rows, 1 = a block of rows, 2 = only one row
   * @param logQuery <code>true</code> to log the query, <code>false</code> to no log the query
   * @param fetchTotalResultSetLength fetch all result set length; be careful: this task could slow down the data retrieval
   * @return a list of value objects or an error response
   */
  private static Response getQuery(
      Connection conn,
      UserSessionParameters userSessionPars,
      String baseSQL,
      ArrayList values,
      Map attribute2dbField,
      Class valueObjectClass,
      String booleanTrueValue,
      String booleanFalseValue,
      ServletContext context,
      GridParams gridParams,
      int blockSize,
      int rowsToRead,
      boolean logQuery,
      boolean fetchTotalResultSetLength
  ) throws Exception {
    baseSQL = getSql(
      userSessionPars,
      baseSQL,
      values,
      gridParams.getFilteredColumns(),
      gridParams.getCurrentSortedColumns(),
      gridParams.getCurrentSortedVersusColumns(),
      attribute2dbField
    );


    return getQuery(
      conn,
      userSessionPars,
      baseSQL,
      null,
      null,
      null,
      null,
      null,
      null,
      values,
      attribute2dbField,
      valueObjectClass,
      booleanTrueValue,
      booleanFalseValue,
      context,
      blockSize,
      rowsToRead,
      logQuery,
      gridParams.getAction(),
      gridParams.getStartPos(),
      fetchTotalResultSetLength
    );
  }


  /**
   * SQL is expressed using more argument, each one without the related keyword (select, from, ...).
   *
   * Example: following query
   *
   * select customer_code,corporate_name from companies order by customer_code asc
   *
   * become an invokation of getSql:
   *
   * getSql(userSessionPars,"customer_code,corporate_name","companies","","customer_code asc","","",...);
   *
   *
   * Example: following query
   *
   * select customer_code,customername from
   * (select customer_code,corporate_name as customername from companies
   * union
   * select customer_code,name as customername from privates)
   * order by customer_code asc
   *
   * become an invokation of getSql:
   *
   * getSql(userSessionPars,"customer_code,customername","(select customer_code,corporate_name as customername from companies union select customer_code,name as customername from privates)","","customer_code asc","","",...);
   *
   * @param select list of fields for select statement
   * @param from list of tables for from statement
   * @param where where statement; may be null
   * @param group group by statement; may be null
   * @param having having statement; may be null
   * @param order list of fields for order by statement; may be null
   * @param values binding values related to baseSQL
   * @param attribute2dbField collection of pairs attributeName, corresponding database column (table.column) - for ALL fields is the select clause
   * @param valueObjectClass value object class to use to generate the result
   * @param booleanTrueValue read value to interpret as true
   * @param booleanFalseValue read value to interpret as false
   * @param context servlet context; this may be null
   * @param gridParams grid parameters (filtering/ordering settings, starting row to read, read versus)
   * @param blockSize number of rows to read
   * @param rowsToRead 0 = all rows, 1 = a block of rows, 2 = only one row
   * @param logQuery <code>true</code> to log the query, <code>false</code> to no log the query
   * @return a list of value objects or an error response
   */
  private static Response getQuery(
      Connection conn,
      UserSessionParameters userSessionPars,
      String select,
      String from,
      String where,
      String group,
      String having,
      String order,
      ArrayList values,
      Map attribute2dbField,
      Class valueObjectClass,
      String booleanTrueValue,
      String booleanFalseValue,
      ServletContext context,
      GridParams gridParams,
      int blockSize,
      int rowsToRead,
      boolean logQuery
  ) throws Exception {
    // add filtering/ordering clauses...
    String baseSQL = getSql(
      userSessionPars,
      select,
      from,
      where,
      group,
      having,
      order,
      values,
      gridParams.getFilteredColumns(),
      gridParams.getCurrentSortedColumns(),
      gridParams.getCurrentSortedVersusColumns(),
      attribute2dbField
    );
    return getQuery(
      conn,
      userSessionPars,
      null,
      select,
      from,
      where,
      group,
      having,
      order,
      values,
      attribute2dbField,
      valueObjectClass,
      booleanTrueValue,
      booleanFalseValue,
      context,
      blockSize,
      rowsToRead,
      logQuery,
      gridParams.getAction(),
      gridParams.getStartPos(),
      false
    );
  }


  /**
   * SQL is expressed using more argument, each one without the related keyword (select, from, ...).
   *
   * Example: following query
   *
   * select customer_code,corporate_name from companies order by customer_code asc
   *
   * become an invokation of getSql:
   *
   * getSql(userSessionPars,"customer_code,corporate_name","companies","","customer_code asc","","",...);
   *
   *
   * Example: following query
   *
   * select customer_code,customername from
   * (select customer_code,corporate_name as customername from companies
   * union
   * select customer_code,name as customername from privates)
   * order by customer_code asc
   *
   * become an invokation of getSql:
   *
   * getSql(userSessionPars,"customer_code,customername","(select customer_code,corporate_name as customername from companies union select customer_code,name as customername from privates)","","customer_code asc","","",...);
   *
   * @param select list of fields for select statement
   * @param from list of tables for from statement
   * @param where where statement; may be null
   * @param group group by statement; may be null
   * @param having having statement; may be null
   * @param order list of fields for order by statement; may be null
   * @param values binding values related to baseSQL
   * @param attribute2dbField collection of pairs attributeName, corresponding database column (table.column) - for ALL fields is the select clause
   * @param valueObjectClass value object class to use to generate the result
   * @param booleanTrueValue read value to interpret as true
   * @param booleanFalseValue read value to interpret as false
   * @param context servlet context; this may be null
   * @param gridParams grid parameters (filtering/ordering settings, starting row to read, read versus)
   * @param blockSize number of rows to read
   * @param rowsToRead 0 = all rows, 1 = a block of rows, 2 = only one row
   * @param logQuery <code>true</code> to log the query, <code>false</code> to no log the query
   * @param fetchTotalResultSetLength fetch all result set length; be careful: this task could slow down the data retrieval
   * @return a list of value objects or an error response
   */
  private static Response getQuery(
      Connection conn,
      UserSessionParameters userSessionPars,
      String select,
      String from,
      String where,
      String group,
      String having,
      String order,
      ArrayList values,
      Map attribute2dbField,
      Class valueObjectClass,
      String booleanTrueValue,
      String booleanFalseValue,
      ServletContext context,
      GridParams gridParams,
      int blockSize,
      int rowsToRead,
      boolean logQuery,
      boolean fetchTotalResultSetLength
  ) throws Exception {
    // add filtering/ordering clauses...
    String baseSQL = getSql(
      userSessionPars,
      select,
      from,
      where,
      group,
      having,
      order,
      values,
      gridParams.getFilteredColumns(),
      gridParams.getCurrentSortedColumns(),
      gridParams.getCurrentSortedVersusColumns(),
      attribute2dbField
    );
    return getQuery(
      conn,
      userSessionPars,
      null,
      select,
      from,
      where,
      group,
      having,
      order,
      values,
      attribute2dbField,
      valueObjectClass,
      booleanTrueValue,
      booleanFalseValue,
      context,
      blockSize,
      rowsToRead,
      logQuery,
      gridParams.getAction(),
      gridParams.getStartPos(),
      fetchTotalResultSetLength
    );
  }


  /**
   * @param baseSQL SQL that already contains filtering and sorting conditions
   * @param values binding values related to baseSQL
   * @param attribute2dbField collection of pairs attributeName, corresponding database column (table.column) - for ALL fields is the select clause
   * @param valueObjectClass value object class to use to generate the result
   * @param booleanTrueValue read value to interpret as true
   * @param booleanFalseValue read value to interpret as false
   * @param context servlet context; this may be null
   * @param blockSize number of rows to read
   * @param rowsToRead 0 = all rows, 1 = a block of rows, 2 = only one row
   * @param logQuery <code>true</code> to log the query, <code>false</code> to no log the query
   * @param fetchTotalResultSetLength fetch all result set length; be careful: this task could slow down the data retrieval
   * @return a list of value objects or an error response
   */
  private static Response getQuery(
      Connection conn,
      UserSessionParameters userSessionPars,
      String baseSQL,
      String select,
      String from,
      String where,
      String group,
      String having,
      String order,
      ArrayList values,
      Map attribute2dbField,
      Class valueObjectClass,
      String booleanTrueValue,
      String booleanFalseValue,
      ServletContext context,
      int blockSize,
      int rowsToRead,
      boolean logQuery,
      int action,
      int startPos,
      boolean fetchTotalResultSetLength
  ) throws Exception {
    PreparedStatement pstmt = null;
    String params = "";
    if (baseSQL==null)
      baseSQL =
        "SELECT "+select+" "+
        "FROM "+from+" "+
        (where==null  || where.equals("") ?"":("WHERE "+where+" "))+
        (group==null  || group.equals("") ?"":("GROUP BY "+group+" "))+
        (having==null || having.equals("")?"":("HAVING "+having+" "))+
        (order==null  || order.equals("") ?"":("ORDER BY "+order));
    else
      select = baseSQL.substring(baseSQL.toLowerCase().indexOf("select ")+7,baseSQL.toLowerCase().indexOf(" from ")).replace('\n',' ').replace('\r',' ').trim();
    try {

      // prepare the collection of pairs database column (table.column), attributeName - for ALL fields is the select clause
      Iterator it = attribute2dbField.keySet().iterator();
      String attributeName = null;
      HashMap field2Attribute = new HashMap();
      while(it.hasNext()) {
        attributeName = it.next().toString();
        field2Attribute.put(attribute2dbField.get(attributeName),attributeName);
      }
      // prepare the SQL statement...
      long t1 = System.currentTimeMillis();
      pstmt = conn.prepareStatement(baseSQL);
      for(int i=0;i<values.size();i++) {
        if (values.get(i)!=null && values.get(i).getClass().getName().equals("java.util.Date"))
          values.set(i,new java.sql.Date(((java.util.Date)values.get(i)).getTime()));
        pstmt.setObject(i+1,values.get(i));
      }
      ResultSet rset = pstmt.executeQuery();
      long t2 = System.currentTimeMillis();

      // prepare setter methods of the v.o...
      ArrayList cols = getColumns(select);
      Method[] setterMethods = new Method[cols.size()];
      Method getter = null;
      Method setter = null;
      String aName = null;
      ArrayList[] getters = new ArrayList[cols.size()];
      ArrayList[] setters = new ArrayList[cols.size()];
      Class clazz = null;
      for(int i=0;i<cols.size();i++) {
        attributeName = (String)field2Attribute.get(cols.get(i));
        if (attributeName==null) {
          return new ErrorResponse("Attribute not found in 'attribute2dbField' argument for database field '"+cols.get(i)+"'");
        }

        aName = attributeName;
        getters[i] = new ArrayList(); // list of Methods objects (getters for accessing inner vos)
        setters[i] = new ArrayList(); // list of Methods objects (setters for instantiating inner vos)
        clazz = valueObjectClass;

        // check if the specified attribute is a composed attribute and there exist inner v.o. to instantiate...
        while(aName.indexOf(".")!=-1) {
          try {
            getter = clazz.getMethod(
              "get" +
              aName.substring(0, 1).
              toUpperCase() +
              aName.substring(1,aName.indexOf(".")),
              new Class[0]
            );
          }
          catch (NoSuchMethodException ex2) {
            getter = clazz.getMethod("is"+aName.substring(0,1).toUpperCase()+aName.substring(1,aName.indexOf(".")),new Class[0]);
          }
          setter = clazz.getMethod("set"+aName.substring(0,1).toUpperCase()+aName.substring(1,aName.indexOf(".")),new Class[]{getter.getReturnType()});
          aName = aName.substring(aName.indexOf(".")+1);
          clazz = getter.getReturnType();
          getters[i].add(getter);
          setters[i].add(setter);
        }

        try {
          getter = clazz.getMethod(
            "get" +
            aName.substring(0, 1).
            toUpperCase() +
            aName.substring(1),
            new Class[0]
          );
        }
        catch (NoSuchMethodException ex2) {
          getter = clazz.getMethod("is"+aName.substring(0,1).toUpperCase()+aName.substring(1),new Class[0]);
        }

        setterMethods[i] = clazz.getMethod("set"+aName.substring(0,1).toUpperCase()+aName.substring(1),new Class[]{getter.getReturnType()});
      }

      int rowCount = 0;
      int resultSetLength = -1;
      int totalResultSetLength = -1;
      if (rowsToRead==1) {
        // read a block of rows...

        if (action==GridParams.LAST_BLOCK_ACTION) {
          try {
            rset.last();
            resultSetLength = rset.getRow();
            totalResultSetLength = resultSetLength;
          }
          catch (SQLException ex4) {
            // last & getRow methods not supported!
            while(rset.next())
              rowCount++;
            resultSetLength = rowCount;
            totalResultSetLength = resultSetLength;
            action = GridParams.NEXT_BLOCK_ACTION;
            startPos = Math.max(rowCount-blockSize,0);
            rowCount = 0;
            rset.close();
            rset = pstmt.executeQuery();
            int i=0;
            while(i<startPos) {
              rset.next();
              i++;
            }
          }
        } else {
          try {
            rset.absolute(startPos);
          }
          catch (SQLException ex3) {
            if (action==GridParams.PREVIOUS_BLOCK_ACTION) {
              action = GridParams.NEXT_BLOCK_ACTION;
              startPos = Math.max(startPos-blockSize,0);
            }
            // absolute method not supported!
            int i=0;
            while(i<startPos) {
              rset.next();
              i++;
            }
          }
        }
      }
      else if (rowsToRead==0) {
        // load all rows...
        action = GridParams.NEXT_BLOCK_ACTION;
      }

      ArrayList list = new ArrayList();
      Object value = null;
      Object vo = null;
      while (
          action==GridParams.LAST_BLOCK_ACTION && rset.previous() ||
          action==GridParams.NEXT_BLOCK_ACTION && rset.next() ||
          action==GridParams.PREVIOUS_BLOCK_ACTION && rset.previous()) {
        rowCount++;

        vo = valueObjectClass.newInstance();
        Object currentVO = null;
        Object innerVO = null;
        for(int i=0;i<cols.size();i++) {
          currentVO = vo;
          for(int j=0;j<getters[i].size();j++) {
            if (((Method)getters[i].get(j)).invoke(currentVO,new Object[0])==null) {
              innerVO = ((Method)getters[i].get(j)).getReturnType().newInstance();  // instantiate the inner v.o.
              ((Method)setters[i].get(j)).invoke(currentVO,new Object[]{ innerVO });
              currentVO = innerVO;
            }
            else
              currentVO = ((Method)getters[i].get(j)).invoke(currentVO,new Object[0]);
          }

          Class parType = setterMethods[i].getParameterTypes()[0];
          if (parType.equals(String.class))
            value = rset.getString(i+1);
          else if (parType.equals(Boolean.class) ||
                   parType.equals(boolean.class)) {
            value = rset.getString(i + 1);
            if (value!=null && value.equals(booleanTrueValue))
              value = Boolean.TRUE;
            else if (value!=null && value.equals(booleanFalseValue))
              value = Boolean.FALSE;
          }
          else if (parType.equals(BigDecimal.class))
            value = rset.getBigDecimal(i+1);
          else if (parType.equals(Double.class) || parType==Double.TYPE) {
            value = rset.getBigDecimal(i+1);
            if (value!=null)
              value = new Double(((BigDecimal)value).doubleValue());
          }
          else if (parType.equals(Float.class) || parType==Float.TYPE) {
            value = rset.getBigDecimal(i+1);
            if (value!=null)
              value = new Float(((BigDecimal)value).floatValue());
          }
          else if (parType.equals(Integer.class) || parType==Integer.TYPE) {
            value = rset.getBigDecimal(i+1);
            if (value!=null)
              value = new Integer(((BigDecimal)value).intValue());
          }
          else if (parType.equals(Long.class) || parType==Long.TYPE) {
            value = rset.getBigDecimal(i+1);
            if (value!=null)
              value = new Long(((BigDecimal)value).longValue());
          }
          else if (parType.equals(Short.class) || parType==Short.TYPE) {
            value = rset.getBigDecimal(i+1);
            if (value!=null)
              value = new Long(((BigDecimal)value).longValue());
          }
          else if (parType.equals(java.util.Date.class) ||
                   parType.equals(java.sql.Date.class))
            value = rset.getDate(i+1);
          else if (parType.equals(java.sql.Timestamp.class))
            value = rset.getTimestamp(i+1);
          else
            value = rset.getObject(i+1);

          try {
            if (setterMethods[i].getParameterTypes()[0].equals(byte[].class) &&
                value!=null &&
                value instanceof Blob){
              Blob b = (Blob)value;
              BufferedInputStream in = null;
              try {
                in = new BufferedInputStream(b.getBinaryStream());
                byte[] bb = new byte[10000];
                byte[] bytes = new byte[0];
                byte[] aux = null;
                int len = 0;
                while((len=in.read(bb))>0) {
                  aux = new byte[bytes.length+len];
                  System.arraycopy(bytes,0,aux,0,bytes.length);
                  System.arraycopy(bb,0,aux,bytes.length,len);
                  bytes = aux;
                }
                value = bytes;
              }
              catch (Exception ex7) {
                ex7.printStackTrace();
                value = null;
              }
              finally {
                try {
                  if (in != null) {
                    in.close();
                  }
                }
                catch (Exception ex8) {
                }
              }

            }
            setterMethods[i].invoke(currentVO, new Object[] {value});
          }
          catch (IllegalArgumentException ex5) {
            try {
              if (value!=null && !value.getClass().getName().equals(parType.getName()))
                Logger.error(
                    userSessionPars!=null?userSessionPars.getUsername():null,
                    "org.openswing.swing.server.QueryUtil",
                    "getQuery",
                    "Error while executing the SQL:\n"+
                    baseSQL+"\n"+
                    params+"\n"+
                    "Incompatible type found between value read ("+value.getClass().getName()+") and value expected ("+parType.getName()+") in setter '"+setterMethods[i].getName()+"'.",
                    null
                );
            }
            catch (Throwable ex6) {

            }
            throw ex5;
          }
        }

        list.add(vo);

        if (rowCount==blockSize && rowsToRead==1)
          break;
      }
      boolean moreRows = false;
      if (rowsToRead==1) {
        if (action == GridParams.NEXT_BLOCK_ACTION && rset.next())
          moreRows = true;
        else if (action==GridParams.PREVIOUS_BLOCK_ACTION && rset.previous())
          moreRows = true;
      }


      if (fetchTotalResultSetLength) {
        try {
          rset.last();
          totalResultSetLength = rset.getRow();
        }
        catch (SQLException ex4) {
          // last & getRow methods not supported!
          while(rset.next())
            rowCount++;
          totalResultSetLength = startPos+rowCount+(!moreRows?0:1);
        }
      }


      if (resultSetLength==-1)
        resultSetLength = list.size();

      long t3 = System.currentTimeMillis();
      for(int i=0;i<values.size();i++) {
        params += "Param. n."+(i+1)+" - Value = ";
        if (values.get(i)==null)
          params += "null\n";
        else if (values.get(i) instanceof Number || values.get(i) instanceof java.util.Date)
          params += values.get(i)+"\n";
        else
          params += "'"+values.get(i)+"'\n";
      }
      if (logQuery)
        Logger.debug(
            userSessionPars!=null?userSessionPars.getUsername():null,
            "org.openswing.swing.server.QueryUtil",
            "getQuery",
            "Execute SQL:\n"+
            baseSQL+"\n"+
            params+"\n"+
            list.size()+" rows read\n"+
            "Parsing Query Time: "+(t2-t1)+"ms\n"+
            "Reading Query Time: "+(t3-t2)+"ms\n"
        );

      if (rowsToRead==2) {
        if (list.size()==0)
          return new ErrorResponse("Record not found.");
        else
          return new VOResponse(list.get(0));
      }
      else {
        VOListResponse res = new VOListResponse(list,moreRows,resultSetLength);
        res.setTotalAmountOfRows(totalResultSetLength);
        return res;
      }
    } catch (Throwable ex) {
      try {
       Logger.error(
           userSessionPars!=null?userSessionPars.getUsername():null,
           "org.openswing.swing.server.QueryUtil",
           "getQuery",
           "Error while executing the SQL:\n"+
           baseSQL+"\n"+
           params+"\n"+
           ex.getMessage(),
           ex
       );
      } catch (Exception exx) {
        Logger.error(
            userSessionPars!=null?userSessionPars.getUsername():null,
            "org.openswing.swing.server.QueryUtil",
            "getQuery",
            "Error while executing the SQL:\n"+ex.getMessage(),
            ex
        );
      }
     return new ErrorResponse(ex.getMessage());
    }
    finally {
      try {
        if (pstmt!=null)
          pstmt.close();
      }
      catch (SQLException ex1) {
      }
    }
 }


 /**
  * Method called by getQuery method to retrieve all db fields in select clause.
  * @param sql query to execute
  * @return list of db fields
  */
 private static ArrayList getColumns(String sql) {
   ArrayList list = new ArrayList();
   String token = null;
   int comma = 0,lastIndex = 0;
   int parenthesis = 0;
   while((comma=sql.indexOf(",",lastIndex))>0) {
     token = sql.substring(lastIndex,comma).trim();
     parenthesis = parenthesis + new StringTokenizer(" "+token+" ","(").countTokens();
     parenthesis = parenthesis - new StringTokenizer(" "+token+" ",")").countTokens();
     if (parenthesis>0) {
       lastIndex = comma+1;
       continue;
     }
     parenthesis = 0;

     if (token.indexOf(" ")!=-1)
       token = token.substring(token.lastIndexOf(" ")+1);
     list.add( token );

     lastIndex = comma+1;
   }

   token = sql.substring(lastIndex).trim();
   if (token.indexOf(" ")!=-1)
     token = token.substring(token.lastIndexOf(" ")+1);
   list.add( token );

   return list;
 }


 /**
  * This method execute an insert on a table, by means of the value object and a subset of its fields: all field related to that table.
  * @param vo value object to use on insert
  * @param tableName table name to use on insert
  * @param attribute2dbField collection of pairs attributeName, corresponding database column (table.column) - for ALL fields related to the specified table
  * @param booleanTrueValue value to interpret as true
  * @param booleanFalseValue value to interpret as false
  * @param logSQL <code>true</code> to log the SQL, <code>false</code> to no log the SQL
  * @return the insert response
  */
 public static Response insertTable(
     Connection conn,
     ValueObject vo,
     String tableName,
     Map attribute2dbField,
     String booleanTrueValue,
     String booleanFalseValue,
     boolean logSQL
 ) throws Exception {
    return insertTable(
      conn,
      new UserSessionParameters(),
      vo,
      tableName,
      attribute2dbField,
      booleanTrueValue,
      booleanFalseValue,
      null,
      logSQL
  );
 }


 /**
  * This method esecute an insert on a table, by means of the value object and a subset of its fields: all field related to that table.
  * @param vo value object to use on insert
  * @param tableName table name to use on insert
  * @param attribute2dbField collection of pairs attributeName, corresponding database column (table.column) - for ALL fields related to the specified table
  * @param booleanTrueValue value to interpret as true
  * @param booleanFalseValue value to interpret as false
  * @param context servlet context; this may be null
  * @param logSQL <code>true</code> to log the SQL, <code>false</code> to no log the SQL
  * @return the insert response
  */
 public static Response insertTable(
     Connection conn,
     UserSessionParameters userSessionPars,
     ValueObject vo,
     String tableName,
     Map attribute2dbField,
     String booleanTrueValue,
     String booleanFalseValue,

     ServletContext context,
     boolean logSQL
 ) throws Exception {
    return insertTable(
      conn,
      userSessionPars,
      vo,
      tableName,
      attribute2dbField,
      booleanTrueValue,
      booleanFalseValue,
      new HashMap(),
      null,
      logSQL
  );
 }


   /**
    * This method esecute an insert on a table, by means of the value object and a subset of its fields: all field related to that table.
    * @param vo value object to use on insert
    * @param tableName table name to use on insert
    * @param attribute2dbField collection of pairs attributeName, corresponding database column (table.column) - for ALL fields related to the specified table
    * @param booleanTrueValue value to interpret as true
    * @param booleanFalseValue value to interpret as false
    * @param fieldsValues collection of pairs <database column,value> to include in INSERT clause, without extracting values from the value object; e.g. CREATE_DATE, CREATE_USER, etc.
    * @param context servlet context; this may be null
    * @param logSQL <code>true</code> to log the SQL, <code>false</code> to no log the SQL
    * @return the insert response
    */
   public static Response insertTable(
       Connection conn,
       UserSessionParameters userSessionPars,
       ValueObject vo,
       String tableName,
       Map attribute2dbField,
       String booleanTrueValue,
       String booleanFalseValue,
       Map fieldsValues,
       ServletContext context,
       boolean logSQL
   ) throws Exception {
     PreparedStatement pstmt = null;
     String params = "";
     String sql = null;
     String aName = null;
     Class clazz = null;
     try {
       // prepare getter methods of the v.o and the SQL statement...
       sql = "insert into "+tableName+"(";
       String sqlvalues = " values(";
       ArrayList values = new ArrayList();

       String field = null;
       Iterator it = fieldsValues.keySet().iterator();
       while(it.hasNext()) {
         field = it.next().toString();
         sql += field+",";
         sqlvalues += "?,";
         values.add(fieldsValues.get(field));
       }

       it = attribute2dbField.keySet().iterator();
       Method getter = null;
       String attributeName = null;
       Object value = null;
       int i=0;
       while(it.hasNext()) {
         attributeName = it.next().toString();
         field = (String)attribute2dbField.get(attributeName);
         aName = attributeName;
         clazz = vo.getClass();
         value = vo;



//         try {
//           getter = vo.getClass().getMethod(
//             "get" +
//             attributeName.substring(0, 1).toUpperCase() +
//             attributeName.substring(1),
//             new Class[0]
//           );
//         }
//         catch (NoSuchMethodException ex2) {
//           getter = vo.getClass().getMethod("is"+attributeName.substring(0,1).toUpperCase()+attributeName.substring(1),new Class[0]);
//         }
//         value = getter.invoke(vo,new Object[0]);

         // check if the specified attribute is a composed attribute and there exist inner v.o. to instantiate...
         while(aName.indexOf(".")!=-1) {
           try {
             getter = clazz.getMethod(
               "get" +
               aName.substring(0, 1).
               toUpperCase() +
               aName.substring(1,aName.indexOf(".")),
               new Class[0]
             );
           }
           catch (NoSuchMethodException ex2) {
             getter = clazz.getMethod("is"+aName.substring(0,1).toUpperCase()+aName.substring(1,aName.indexOf(".")),new Class[0]);
           }
           aName = aName.substring(aName.indexOf(".")+1);
           clazz = getter.getReturnType();
           if (value!=null)
             value = getter.invoke(value,new Object[0]);
           else
             break;
         }

         try {
           getter = clazz.getMethod(
             "get" +
             aName.substring(0, 1).
             toUpperCase() +
             aName.substring(1),
             new Class[0]
           );
         }
         catch (NoSuchMethodException ex2) {
           getter = clazz.getMethod("is"+aName.substring(0,1).toUpperCase()+aName.substring(1),new Class[0]);
         }
         if (value!=null)
           value = getter.invoke(value,new Object[0]);


         if (value!=null) {
           if (value instanceof Boolean) {
             if (((Boolean)value).booleanValue())
               value = booleanTrueValue;
             else
               value = booleanFalseValue;
           }
           sql += field+",";
           sqlvalues += "?,";
           values.add(value);
         }
       }

       sqlvalues = sqlvalues.substring(0,sqlvalues.length()-1); // remove the last ", "
       sql = sql.substring(0,sql.length()-1)+")"; // remove the last ", "
       sql += sqlvalues+")";

       // esecute the SQL statement...
       long t1 = System.currentTimeMillis();
       pstmt = conn.prepareStatement(sql);
       for(i=0;i<values.size();i++) {
         if (values.get(i)!=null && values.get(i).getClass().getName().equals("java.util.Date"))
           values.set(i,new java.sql.Date(((java.util.Date)values.get(i)).getTime()));
         pstmt.setObject(i+1,values.get(i));
       }
       pstmt.execute();

       long t2 = System.currentTimeMillis();
       for(i=0;i<values.size();i++) {
         params += "Param. n."+(i+1)+" - Value = ";
         if (values.get(i) instanceof Number || values.get(i) instanceof java.util.Date)
           params += values.get(i)+"\n";
         else
           params += "'"+values.get(i)+"'\n";
       }

       if (logSQL)
         Logger.debug(
             userSessionPars!=null?userSessionPars.getUsername():null,
             "org.openswing.swing.server.QueryUtil",
             "insertTable",
             "Execute SQL:\n"+
             sql+"\n"+
             params+"\n"+
             "Inserting Time: "+(t2-t1)+"ms\n"
         );

      return new VOResponse(vo);

     } catch (Throwable ex) {
       try {
         Logger.error(
             userSessionPars!=null?userSessionPars.getUsername():null,
             "org.openswing.swing.server.QueryUtil",
             "insertTable",
             "Error while executing the SQL:\n"+
             sql+"\n"+
             params+"\n"+
             ex.getMessage(),
             ex
         );
       }
       catch (Exception exx) {
         Logger.error(
             userSessionPars!=null?userSessionPars.getUsername():null,
             "org.openswing.swing.server.QueryUtil",
             "insertTable",
             "Error while executing the SQL:\n"+ex.getMessage(),
             ex
         );
       }
       return new ErrorResponse(ex.getMessage());
     }
     finally {
       try {
         pstmt.close();
       }
       catch (SQLException ex1) {
       }
     }
  }


  /**
   * This method esecute many insert on a table, by means of a list of value objects and a subset of its fields: all field related to that table.
   * @param vos list of ValueObject's to use on insert operations
   * @param tableName table name to use on insert
   * @param attribute2dbField collection of pairs attributeName, corresponding database column (table.column) - for ALL fields related to the specified table
   * @param booleanTrueValue value to interpret as true
   * @param booleanFalseValue value to interpret as false
   * @param logSQL <code>true</code> to log the SQL, <code>false</code> to no log the SQL
   * @return the insert response (VOListResponse object or an ErrorResponse object)
   */
  public static Response insertTable(
      Connection conn,
      ArrayList vos,
      String tableName,
      Map attribute2dbField,
      String booleanTrueValue,
      String booleanFalseValue,
      boolean logSQL
  ) throws Exception {
    return insertTable(
      conn,
      new UserSessionParameters(),
      vos,
      tableName,
      attribute2dbField,
      booleanTrueValue,
      booleanFalseValue,
      null,
      logSQL
    );
  }


  /**
   * This method esecute many insert on a table, by means of a list of value objects and a subset of its fields: all field related to that table.
   * @param vos list of ValueObject's to use on insert operations
   * @param tableName table name to use on insert
   * @param attribute2dbField collection of pairs attributeName, corresponding database column (table.column) - for ALL fields related to the specified table
   * @param booleanTrueValue value to interpret as true
   * @param booleanFalseValue value to interpret as false
   * @param context servlet context; this may be null
   * @param logSQL <code>true</code> to log the SQL, <code>false</code> to no log the SQL
   * @return the insert response (VOListResponse object or an ErrorResponse object)
   */
  public static Response insertTable(
      Connection conn,
      UserSessionParameters userSessionPars,
      ArrayList vos,
      String tableName,
      Map attribute2dbField,
      String booleanTrueValue,
      String booleanFalseValue,
      ServletContext context,
      boolean logSQL
  ) throws Exception {
    return insertTable(
      conn,
      userSessionPars,
      vos,
      tableName,
      attribute2dbField,
      booleanTrueValue,
      booleanFalseValue,
      new HashMap(),
      null,
      logSQL
    );
  }

  /**
   * This method esecute many insert on a table, by means of a list of value objects and a subset of its fields: all field related to that table.
   * @param vos list of ValueObject's to use on insert operations
   * @param tableName table name to use on insert
   * @param attribute2dbField collection of pairs attributeName, corresponding database column (table.column) - for ALL fields related to the specified table
   * @param booleanTrueValue value to interpret as true
   * @param booleanFalseValue value to interpret as false
   * @param fieldsValues collection of pairs <database column,value> to include in INSERT clause, without extracting values from the value object; e.g. CREATE_DATE, CREATE_USER, etc.
   * @param context servlet context; this may be null
   * @param logSQL <code>true</code> to log the SQL, <code>false</code> to no log the SQL
   * @return the insert response (VOListResponse object or an ErrorResponse object)
   */
  public static Response insertTable(
      Connection conn,
      UserSessionParameters userSessionPars,
      ArrayList vos,
      String tableName,
      Map attribute2dbField,
      String booleanTrueValue,
      String booleanFalseValue,
      Map fieldsValues,
      ServletContext context,
      boolean logSQL
  ) throws Exception {
    PreparedStatement pstmt = null;
    StringBuffer params = new StringBuffer();
    StringBuffer sql = new StringBuffer();
    StringBuffer sqlvalues = new StringBuffer();
    int i=0;
    Method getter = null;
    String attributeName = null;
    String field = null;
    Object value = null;
    Iterator it = null;
    ArrayList values = new ArrayList();
    long t2;
    try {
      ValueObject vo = null;
      String aName = null;
      Class clazz = null;
      for(int j=0;j<vos.size();j++) {
        vo = (ValueObject)vos.get(j);

        // prepare getter methods of the v.o and the SQL statement...
        sql.delete(0,sql.length());
        sqlvalues.delete(0,sqlvalues.length());
        sql.append("insert into ").append(tableName).append("(");
        sqlvalues.append(" values(");
        values.clear();

        it = fieldsValues.keySet().iterator();
        while(it.hasNext()) {
          field = it.next().toString();
          sql.append(field).append(",");
          sqlvalues.append("?,");
          values.add(fieldsValues.get(field));
        }

        it = attribute2dbField.keySet().iterator();
        i=0;
        while(it.hasNext()) {
          attributeName = it.next().toString();
          field = (String)attribute2dbField.get(attributeName);
          aName = attributeName;
          clazz = vo.getClass();
          value = vo;

//          try {
//            getter = vo.getClass().getMethod(
//               "get" +
//               attributeName.substring(0, 1).
//               toUpperCase() +
//               attributeName.substring(1),
//               new Class[0]
//            );
//          }
//          catch (NoSuchMethodException ex2) {
//            getter = vo.getClass().getMethod(
//               "is" +
//               attributeName.substring(0, 1).
//               toUpperCase() +
//               attributeName.substring(1),
//               new Class[0]
//            );
//          }
//          value = getter.invoke(vo,new Object[0]);

          // check if the specified attribute is a composed attribute and there exist inner v.o. to instantiate...
          while(aName.indexOf(".")!=-1) {
            try {
              getter = clazz.getMethod(
                "get" +
                aName.substring(0, 1).
                toUpperCase() +
                aName.substring(1,aName.indexOf(".")),
                new Class[0]
              );
            }
            catch (NoSuchMethodException ex2) {
              getter = clazz.getMethod("is"+aName.substring(0,1).toUpperCase()+aName.substring(1,aName.indexOf(".")),new Class[0]);
            }
            aName = aName.substring(aName.indexOf(".")+1);
            clazz = getter.getReturnType();
            if (value!=null)
              value = getter.invoke(value,new Object[0]);
            else
              break;
          }

          try {
            getter = clazz.getMethod(
              "get" +
              aName.substring(0, 1).
              toUpperCase() +
              aName.substring(1),
              new Class[0]
            );
          }
          catch (NoSuchMethodException ex2) {
            getter = clazz.getMethod("is"+aName.substring(0,1).toUpperCase()+aName.substring(1),new Class[0]);
          }
          if (value!=null)
            value = getter.invoke(value,new Object[0]);

          if (value!=null) {
            if (value instanceof Boolean) {
              if (((Boolean)value).booleanValue())
                value = booleanTrueValue;
              else
                value = booleanFalseValue;
            }
            sql.append(field).append(",");
            sqlvalues.append("?,");
            values.add(value);
          }
        }

        sqlvalues.delete(sqlvalues.length()-1,sqlvalues.length()); // remove the last ", "
        sql.delete(sql.length()-1,sql.length()).append(")"); // remove the last ", "
        sql.append(sqlvalues).append(")");

        // esecute the SQL statement...
        long t1 = System.currentTimeMillis();
        pstmt = conn.prepareStatement(sql.toString());
        for(i=0;i<values.size();i++) {
          if (values.get(i)!=null && values.get(i).getClass().getName().equals("java.util.Date"))
            values.set(i,new java.sql.Date(((java.util.Date)values.get(i)).getTime()));
          pstmt.setObject(i+1,values.get(i));
        }
        pstmt.execute();
        pstmt.close();

        t2 = System.currentTimeMillis();
        params.delete(0,params.length());
        for(i=0;i<values.size();i++) {
          params.append("Param. n.").append(String.valueOf(i+1)).append(" - Value = ");
          if (values.get(i) instanceof Number || values.get(i) instanceof java.util.Date)
            params.append(values.get(i)).append("\n");
          else
            params.append("'").append(values.get(i)).append("'\n");
        }

        if (logSQL)
          Logger.debug(
              userSessionPars!=null?userSessionPars.getUsername():null,
              "org.openswing.swing.server.QueryUtil",
              "insertTable",
              "Execute SQL:\n"+
              sql+"\n"+
              params+"\n"+
              "Inserting Time: "+(t2-t1)+"ms\n"
          );

      }
      return new VOListResponse(vos,false,vos.size());

    } catch (Throwable ex) {
      try {
        Logger.error(
            userSessionPars!=null?userSessionPars.getUsername():null,
            "org.openswing.swing.server.QueryUtil",
            "insertTable",
            "Error while executing the SQL:\n"+
            sql+"\n"+
            params+"\n"+
            ex.getMessage(),
            ex
        );
      }
      catch (Exception exx) {
        try {
          if (pstmt!=null)
            pstmt.close();
        }
        catch (Exception ex1) {
        }
        Logger.error(
            userSessionPars!=null?userSessionPars.getUsername():null,
            "org.openswing.swing.server.QueryUtil",
            "insertTable",
            "Error while executing the SQL:\n"+ex.getMessage(),
            ex
        );
      }
     return new ErrorResponse(ex.getMessage());
    }
 }


 /**
  * This method esecute an update on a table, by means of the value object and a subset of its fields: all field related to that table.
  * The update operation verifies if the record is yet the same as when the v.o. was read (concurrent access resolution).
  * @param pkAttributes v.o. attributes related to the primary key of the table
  * @param oldVO previous value object to use on the where clause
  * @param newVO new value object to use on update
  * @param tableName table name to use on update
  * @param attribute2dbField collection of pairs attributeName, corresponding database column (table.column) - for ALL fields related to the specified table
  * @param booleanTrueValue value to interpret as true
  * @param booleanFalseValue value to interpret as false
  * @param logSQL <code>true</code> to log the SQL, <code>false</code> to no log the SQL
  * @return the update response
  */
 public static Response updateTable(
     Connection conn,
     HashSet pkAttributes,
     ValueObject oldVO,
     ValueObject newVO,
     String tableName,
     Map attribute2dbField,
     String booleanTrueValue,
     String booleanFalseValue,
     boolean logSQL
 ) throws Exception {
    return updateTable(
      conn,
      new UserSessionParameters(),
      pkAttributes,
      oldVO,
      newVO,
      tableName,
      attribute2dbField,
      booleanTrueValue,
      booleanFalseValue,
      null,
      logSQL
  );
 }


 /**
  * This method esecute an update on a table, by means of the value object and a subset of its fields: all field related to that table.
  * The update operation verifies if the record is yet the same as when the v.o. was read (concurrent access resolution).
  * @param pkAttributes v.o. attributes related to the primary key of the table
  * @param oldVO previous value object to use on the where clause
  * @param newVO new value object to use on update
  * @param tableName table name to use on update
  * @param attribute2dbField collection of pairs attributeName, corresponding database column (table.column) - for ALL fields related to the specified table
  * @param booleanTrueValue value to interpret as true
  * @param booleanFalseValue value to interpret as false
  * @param context servlet context; this may be null
  * @param logSQL <code>true</code> to log the SQL, <code>false</code> to no log the SQL
  * @return the update response
  */
 public static Response updateTable(
     Connection conn,
     UserSessionParameters userSessionPars,
     HashSet pkAttributes,
     ValueObject oldVO,
     ValueObject newVO,
     String tableName,
     Map attribute2dbField,
     String booleanTrueValue,
     String booleanFalseValue,
     ServletContext context,
     boolean logSQL
  ) throws Exception {
      return updateTable(
        conn,
        new UserSessionParameters(),
        pkAttributes,
        oldVO,
        newVO,
        tableName,
        attribute2dbField,
        booleanTrueValue,
        booleanFalseValue,
        new HashMap(),
        null,
        logSQL
      );
  }

  /**
   * This method esecute an update on a table, by means of the value object and a subset of its fields: all field related to that table.
   * The update operation verifies if the record is yet the same as when the v.o. was read (concurrent access resolution).
   * @param pkAttributes v.o. attributes related to the primary key of the table
   * @param oldVO previous value object to use on the where clause
   * @param newVO new value object to use on update
   * @param tableName table name to use on update
   * @param attribute2dbField collection of pairs attributeName, corresponding database column (table.column) - for ALL fields related to the specified table
   * @param booleanTrueValue value to interpret as true
   * @param booleanFalseValue value to interpret as false
   * @param fieldsValuesToNotCompare collection of pairs <database column,value> to include in SET clause, without compare them in the WHERE clause (e.g. LAST_UPDATE_DATE, LAST_UPDATE_USER, etc.)
   * @param context servlet context; this may be null
   * @param logSQL <code>true</code> to log the SQL, <code>false</code> to no log the SQL
   * @return the update response
   */
  public static Response updateTable(
      Connection conn,
      UserSessionParameters userSessionPars,
      HashSet pkAttributes,
      ValueObject oldVO,
      ValueObject newVO,
      String tableName,
      Map attribute2dbField,
      String booleanTrueValue,
      String booleanFalseValue,
      Map fieldsValuesToNotCompare,
      ServletContext context,
      boolean logSQL
  ) throws Exception {
    PreparedStatement pstmt = null;
    String params = "";
    String sql = null;
    try {
      // prepare getter methods of the v.o and the SQL statement...
      sql = "update "+tableName+" set ";
      String where = " where ";
      ArrayList values = new ArrayList();
      ArrayList whereValues = new ArrayList();

      String field = null;
      Iterator it = fieldsValuesToNotCompare.keySet().iterator();
      while(it.hasNext()) {
        field = it.next().toString();
        sql += field+"=?,";
        values.add(fieldsValuesToNotCompare.get(field));
      }

      it = attribute2dbField.keySet().iterator();
      Method getter = null;
      String attributeName = null;
      int i=0;
      String aName = null;
      Class clazz = null;
      Object newObj = null;
      Object oldObj = null;
      while(it.hasNext()) {
        attributeName = it.next().toString();
        field = (String)attribute2dbField.get(attributeName);
        aName = attributeName;
        clazz = newVO.getClass();
        newObj = newVO;
        oldObj = oldVO;

        // check if the specified attribute is a composed attribute and there exist inner v.o. to instantiate...
        while(aName.indexOf(".")!=-1) {
          try {
            getter = clazz.getMethod(
              "get" +
              aName.substring(0, 1).
              toUpperCase() +
              aName.substring(1,aName.indexOf(".")),
              new Class[0]
            );
          }
          catch (NoSuchMethodException ex2) {
            getter = clazz.getMethod("is"+aName.substring(0,1).toUpperCase()+aName.substring(1,aName.indexOf(".")),new Class[0]);
          }
          aName = aName.substring(aName.indexOf(".")+1);
          clazz = getter.getReturnType();
          if (newObj!=null)
            newObj = getter.invoke(newObj,new Object[0]);
          if (oldObj!=null)
            oldObj = getter.invoke(oldObj,new Object[0]);
        }

        try {
          getter = clazz.getMethod(
            "get" +
            aName.substring(0, 1).
            toUpperCase() +
            aName.substring(1),
            new Class[0]
          );
        }
        catch (NoSuchMethodException ex2) {
          getter = clazz.getMethod("is"+aName.substring(0,1).toUpperCase()+aName.substring(1),new Class[0]);
        }
        if (newObj!=null)
          newObj = getter.invoke(newObj,new Object[0]);
        if (oldObj!=null)
          oldObj = getter.invoke(oldObj,new Object[0]);

        if (newObj==null) {
          sql += field+"=null, ";
        }
        else {
          if (newObj instanceof Boolean) {
            if (((Boolean)newObj).booleanValue())
              newObj = booleanTrueValue;
            else
              newObj = booleanFalseValue;
          }

          if (!pkAttributes.contains(attributeName)) {
            sql += field+"=?, ";
            values.add(newObj);
          }
        }

        if (oldObj==null) {
          where += field+" is null and ";
        }
        else {
          if (oldObj instanceof Boolean) {
            if (((Boolean)oldObj).booleanValue())
              oldObj = booleanTrueValue;
            else
              oldObj = booleanFalseValue;
          }
          else if (newObj instanceof byte[]) {
            // do not compare the BLOB field...
            continue;
          }

          where += field+"=? and ";
          whereValues.add(oldObj);
        }
      }

      sql = sql.substring(0,sql.length()-2); // remove the last ", "
      sql += where;
      values.addAll(whereValues);

      // add the primary key...
      it = pkAttributes.iterator();
      while(it.hasNext()) {
        attributeName = it.next().toString();
        field = (String)attribute2dbField.get(attributeName);
        try {
          getter = oldVO.getClass().getMethod(
            "get" +
            attributeName.substring(0, 1).
            toUpperCase() +
            attributeName.substring(1),
            new Class[0]
          );
        }
        catch (NoSuchMethodException ex3) {
          getter = oldVO.getClass().getMethod(
             "is" +
             attributeName.substring(0, 1).
             toUpperCase() +
             attributeName.substring(1),
             new Class[0]
          );
        }
        sql += field+"=? and ";
        values.add(getter.invoke(oldVO,new Object[0]));
      }
      sql = sql.substring(0,sql.length()-4); // remove the last "and "

      // esecute the SQL statement...
      long t1 = System.currentTimeMillis();
      pstmt = conn.prepareStatement(sql);
      for(i=0;i<values.size();i++) {
        if (values.get(i)!=null && values.get(i).getClass().getName().equals("java.util.Date"))
          values.set(i,new java.sql.Date(((java.util.Date)values.get(i)).getTime()));
        pstmt.setObject(i+1,values.get(i));
      }
      int rowsUpdated = pstmt.executeUpdate();

      long t2 = System.currentTimeMillis();
      for(i=0;i<values.size();i++) {
        params += "Param. n."+(i+1)+" - Value = ";
        if (values.get(i)==null)
          params += "null\n";
        else if (values.get(i) instanceof Number || values.get(i) instanceof java.util.Date)
          params += values.get(i)+"\n";
        else
          params += "'"+values.get(i)+"'\n";
      }

      if (logSQL)
        Logger.debug(
            userSessionPars!=null?userSessionPars.getUsername():null,
            "org.openswing.swing.server.QueryUtil",
            "updateTable",
            "Execute SQL:\n"+
            sql+"\n"+
            params+"\n"+
            rowsUpdated+" rows updated\n"+
            "Updating Time: "+(t2-t1)+"ms\n"
        );

      if (rowsUpdated==0) {
        // retrieve internationalization settings (Resources object)...
        String msg = "Updating not performed: the record was previously updated.";
        if (context!=null) {
          ResourcesFactory factory = (ResourcesFactory)context.getAttribute(Controller.RESOURCES_FACTORY);
          msg = factory.getResources(userSessionPars.getLanguageId()).getResource(msg);
        }
        return new ErrorResponse(msg);
      }
      else
        return new VOResponse(newVO);

    } catch (Throwable ex) {
      try {
        Logger.error(
            userSessionPars!=null?userSessionPars.getUsername():null,
            "org.openswing.swing.server.QueryUtil",
            "updateTable",
            "Error while executing the SQL:\n"+
            sql+"\n"+
            params+"\n"+
            ex.getMessage(),
            ex
        );
      }
      catch (Exception exx) {
        Logger.error(
            userSessionPars!=null?userSessionPars.getUsername():null,
            "org.openswing.swing.server.QueryUtil",
            "updateTable",
            "Error while executing the SQL:\n"+ex.getMessage(),
            ex
        );
      }
     return new ErrorResponse(ex.getMessage());
    }
    finally {
      try {
        pstmt.close();
      }
      catch (SQLException ex1) {
      }
    }
 }


   /**
    * This method can be used to convert a list of Object[], one for each record already read,
    * in a list of ValueObjects.
    * @param attributes list of attribute names of the specified value object, exactly one for each fields is the select clause (i.e. one for each elements of Object[])
    * @param valueObjectClass value object class to use to generate the result
    * @param rows list of Object[] elements, one for each record already read
    * @param moreRows <code>true</code> if there are still other records to read after these ones, <code>false</code> if no other records ara available
    * @return a list of value objects or an error response
    */
   public static Response getQuery(
       String[] attributes,
       Class valueObjectClass,
       List rows,
       boolean moreRows
   ) throws Exception {
     try {
       Object[] row = null;

       Method[] setterMethods = new Method[attributes.length];
       Method getter = null;
       Method setter = null;
       String aName = null;
       ArrayList[] getters = new ArrayList[attributes.length];
       ArrayList[] setters = new ArrayList[attributes.length];
       Class clazz = null;
       Object vo = null;
       Object value = null;

       for(int i=0;i<attributes.length;i++) {
         aName = attributes[i];
         getters[i] = new ArrayList(); // list of Methods objects (getters for accessing inner vos)
         setters[i] = new ArrayList(); // list of Methods objects (setters for instantiating inner vos)
         clazz = valueObjectClass;

         // check if the specified attribute is a composed attribute and there exist inner v.o. to instantiate...
         while(aName.indexOf(".")!=-1) {
           try {
             getter = clazz.getMethod(
               "get" +
               aName.substring(0, 1).
               toUpperCase() +
               aName.substring(1,aName.indexOf(".")),
               new Class[0]
             );
           }
           catch (NoSuchMethodException ex2) {
             getter = clazz.getMethod("is"+aName.substring(0,1).toUpperCase()+aName.substring(1,aName.indexOf(".")),new Class[0]);
           }
           setter = clazz.getMethod("set"+aName.substring(0,1).toUpperCase()+aName.substring(1,aName.indexOf(".")),new Class[]{getter.getReturnType()});
           aName = aName.substring(aName.indexOf(".")+1);
           clazz = getter.getReturnType();
           getters[i].add(getter);
           setters[i].add(setter);
         }

         try {
           getter = clazz.getMethod(
             "get" +
             aName.substring(0, 1).
             toUpperCase() +
             aName.substring(1),
             new Class[0]
           );
         }
         catch (NoSuchMethodException ex2) {
           getter = clazz.getMethod("is"+aName.substring(0,1).toUpperCase()+aName.substring(1),new Class[0]);
         }

         setterMethods[i] = clazz.getMethod("set"+aName.substring(0,1).toUpperCase()+aName.substring(1),new Class[]{getter.getReturnType()});
       }


       ArrayList list = new ArrayList();
       while(rows.size()>0) {
         row = (Object[])rows.remove(0);
         if (row.length!=attributes.length) {
           String msg = "row.length ("+row.length+") is not equals to attributes.length ("+attributes.length+")";
           return new ErrorResponse(msg);
         }

         vo = valueObjectClass.newInstance();
         Object currentVO = null;
         Object innerVO = null;
         for(int i=0;i<row.length;i++) {
           currentVO = vo;
           for(int j=0;j<getters[i].size();j++) {
             if (((Method)getters[i].get(j)).invoke(currentVO,new Object[0])==null) {
               innerVO = ((Method)getters[i].get(j)).getReturnType().newInstance();  // instantiate the inner v.o.
               ((Method)setters[i].get(j)).invoke(currentVO,new Object[]{ innerVO });
               currentVO = innerVO;
             }
             else
               currentVO = ((Method)getters[i].get(j)).invoke(currentVO,new Object[0]);
           }

           Class parType = setterMethods[i].getParameterTypes()[0];
           if (parType.equals(String.class))
             value = row[i];
//           else if (parType.equals(Boolean.class) ||
//                    parType.equals(boolean.class)) {
//             value = row[i];
//             if (value!=null && value.equals(booleanTrueValue))
//               value = Boolean.TRUE;
//             else if (value!=null && value.equals(booleanFalseValue))
//               value = Boolean.FALSE;
//           }
           else if (parType.equals(BigDecimal.class))
             value = row[i];
           else if (parType.equals(Double.class) || parType==Double.TYPE) {
             value = row[i];
             if (value!=null)
               value = new Double(((BigDecimal)value).doubleValue());
           }
           else if (parType.equals(Float.class) || parType==Float.TYPE) {
             value = row[i];
             if (value!=null)
               value = new Float(((BigDecimal)value).floatValue());
           }
           else if (parType.equals(Integer.class) || parType==Integer.TYPE) {
             value = row[i];
             if (value!=null)
               value = new Integer(((BigDecimal)value).intValue());
           }
           else if (parType.equals(Long.class) || parType==Long.TYPE) {
             value = row[i];
             if (value!=null)
               value = new Long(((BigDecimal)value).longValue());
           }
           else if (parType.equals(Short.class) || parType==Short.TYPE) {
             value = row[i];
             if (value!=null)
               value = new Long(((BigDecimal)value).longValue());
           }
           else if (parType.equals(java.util.Date.class) ||
                    parType.equals(java.sql.Date.class))
             value = row[i];
           else if (parType.equals(java.sql.Timestamp.class))
             value = row[i];
           else
             value = row[i];

           try {
             setterMethods[i].invoke(currentVO, new Object[] {value});
           }
           catch (IllegalArgumentException ex5) {
               if (value!=null && !value.getClass().getName().equals(parType.getClass().getName()))
                 throw new Exception(
                     "Incompatible type found between value read ("+value.getClass().getName()+") and value expected ("+parType.getClass().getName()+") in setter '"+setterMethods[i].getName()+"'."
                 );
           }
         } // end for

         list.add(vo);

       } // end while on rows
       return new VOListResponse(list,moreRows,list.size());

     } catch (Throwable ex) {
      return new ErrorResponse(ex.getMessage());
     }
  }


  /**
   * This method can be used to convert a single record Object[] to a ValueObject.
   * @param attributes list of attribute names of the specified value object, exactly one for each fields is the select clause (i.e. one for each elements of Object[])
   * @param valueObjectClass value object class to use to generate the result
   * @param row Object[], related to the record already read
   * @param booleanTrueValue read value to interpret as true
   * @param booleanFalseValue read value to interpret as false
   * @param context servlet context; this may be null
   * @return value object or an error response
   */
  public static Response getQuery(
      String[] attributes,
      Class valueObjectClass,
      Object[] row
  ) throws Exception {
    try {
      Method[] setterMethods = new Method[attributes.length];
      Method getter = null;
      Method setter = null;
      String aName = null;
      ArrayList[] getters = new ArrayList[attributes.length];
      ArrayList[] setters = new ArrayList[attributes.length];
      Class clazz = null;
      Object vo = null;
      Object value = null;

      for(int i=0;i<attributes.length;i++) {
        aName = attributes[i];
        getters[i] = new ArrayList(); // list of Methods objects (getters for accessing inner vos)
        setters[i] = new ArrayList(); // list of Methods objects (setters for instantiating inner vos)
        clazz = valueObjectClass;

        // check if the specified attribute is a composed attribute and there exist inner v.o. to instantiate...
        while(aName.indexOf(".")!=-1) {
          try {
            getter = clazz.getMethod(
              "get" +
              aName.substring(0, 1).
              toUpperCase() +
              aName.substring(1,aName.indexOf(".")),
              new Class[0]
            );
          }
          catch (NoSuchMethodException ex2) {
            getter = clazz.getMethod("is"+aName.substring(0,1).toUpperCase()+aName.substring(1,aName.indexOf(".")),new Class[0]);
          }
          setter = clazz.getMethod("set"+aName.substring(0,1).toUpperCase()+aName.substring(1,aName.indexOf(".")),new Class[]{getter.getReturnType()});
          aName = aName.substring(aName.indexOf(".")+1);
          clazz = getter.getReturnType();
          getters[i].add(getter);
          setters[i].add(setter);
        }

        try {
          getter = clazz.getMethod(
            "get" +
            aName.substring(0, 1).
            toUpperCase() +
            aName.substring(1),
            new Class[0]
          );
        }
        catch (NoSuchMethodException ex2) {
          getter = clazz.getMethod("is"+aName.substring(0,1).toUpperCase()+aName.substring(1),new Class[0]);
        }

        setterMethods[i] = clazz.getMethod("set"+aName.substring(0,1).toUpperCase()+aName.substring(1),new Class[]{getter.getReturnType()});
      }


      if (row.length!=attributes.length) {
        String msg = "row.length ("+row.length+") is not equals to attributes.length ("+attributes.length+")";
        return new ErrorResponse(msg);
      }

      vo = valueObjectClass.newInstance();
      Object currentVO = null;
      Object innerVO = null;
      for(int i=0;i<row.length;i++) {
        currentVO = vo;
        for(int j=0;j<getters[i].size();j++) {
          if (((Method)getters[i].get(j)).invoke(currentVO,new Object[0])==null) {
            innerVO = ((Method)getters[i].get(j)).getReturnType().newInstance();  // instantiate the inner v.o.
            ((Method)setters[i].get(j)).invoke(currentVO,new Object[]{ innerVO });
            currentVO = innerVO;
          }
          else
            currentVO = ((Method)getters[i].get(j)).invoke(currentVO,new Object[0]);
        }

        Class parType = setterMethods[i].getParameterTypes()[0];
        if (parType.equals(String.class))
          value = row[i];
//        else if (parType.equals(Boolean.class) ||
//                 parType.equals(boolean.class)) {
//          value = row[i];
//          if (value!=null && value.equals(booleanTrueValue))
//            value = Boolean.TRUE;
//          else if (value!=null && value.equals(booleanFalseValue))
//            value = Boolean.FALSE;
//        }
        else if (parType.equals(BigDecimal.class))
          value = row[i];
        else if (parType.equals(Double.class) || parType==Double.TYPE) {
          value = row[i];
          if (value!=null)
            value = new Double(((BigDecimal)value).doubleValue());
        }
        else if (parType.equals(Float.class) || parType==Float.TYPE) {
          value = row[i];
          if (value!=null)
            value = new Float(((BigDecimal)value).floatValue());
        }
        else if (parType.equals(Integer.class) || parType==Integer.TYPE) {
          value = row[i];
          if (value!=null)
            value = new Integer(((BigDecimal)value).intValue());
        }
        else if (parType.equals(Long.class) || parType==Long.TYPE) {
          value = row[i];
          if (value!=null)
            value = new Long(((BigDecimal)value).longValue());
        }
        else if (parType.equals(Short.class) || parType==Short.TYPE) {
          value = row[i];
          if (value!=null)
            value = new Long(((BigDecimal)value).longValue());
        }
        else if (parType.equals(java.util.Date.class) ||
                 parType.equals(java.sql.Date.class))
          value = row[i];
        else if (parType.equals(java.sql.Timestamp.class))
          value = row[i];
        else
          value = row[i];

        try {
          setterMethods[i].invoke(currentVO, new Object[] {value});
        }
        catch (IllegalArgumentException ex5) {
          throw new Exception(
                  "Error while converting Object[] to value object:\n"+
                  "Incompatible type found between value read ("+value.getClass().getName()+") and value expected ("+parType.getClass().getName()+") in setter '"+setterMethods[i].getName()+"'."
          );
        }
      } // end for

      return new VOResponse(vo);

    } catch (Throwable ex) {
     return new ErrorResponse(ex.getMessage());
    }
 }



}
