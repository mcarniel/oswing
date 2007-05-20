package org.openswing.swing.server;

import java.util.*;
import org.openswing.swing.message.send.java.FilterWhereClause;
import org.openswing.swing.logger.server.Logger;
import java.sql.*;
import javax.servlet.ServletContext;
import org.openswing.swing.message.receive.java.*;
import org.openswing.swing.message.send.java.GridParams;
import java.lang.reflect.*;
import java.math.BigDecimal;
import org.openswing.swing.internationalization.java.ResourcesFactory;


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
    try {
      if (filteredColumns.size() > 0) {
        if (baseSQL.toLowerCase().indexOf("where") == -1) {
          baseSQL += " WHERE ";
        }
        else
          baseSQL += " AND ";
        Iterator keys = filteredColumns.keySet().iterator();
        String attributeName = null;
        FilterWhereClause[] filterClauses = null;
        while (keys.hasNext()) {
          attributeName = keys.next().toString();
          filterClauses = (FilterWhereClause[]) filteredColumns.get(
              attributeName);
          baseSQL +=
              attributesMapping.get(attributeName) +
              filterClauses[0].getOperator() +
              "? AND ";
          values.add(filterClauses[0].getValue());
          attrNames.add(filterClauses[0].getAttributeName());
          if (filterClauses[1] != null) {
            baseSQL +=
                attributesMapping.get(attributeName) +
                filterClauses[1].getOperator() +
                "? AND ";
            values.add(filterClauses[1].getValue());
            attrNames.add(filterClauses[1].getAttributeName());
          }
        }
        baseSQL = baseSQL.substring(0, baseSQL.length() - 4);
      }

      if (currentSortedColumns.size() > 0) {
        if (baseSQL.toLowerCase().indexOf("order by") == -1) {
          baseSQL += " ORDER BY ";
        }
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
          userSessionPars.getUsername(),
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
    PreparedStatement pstmt = null;
    String params = "";
    try {
      // add filtering/ordering clauses...
      baseSQL = getSql(userSessionPars,baseSQL,values,gridParams.getFilteredColumns(),gridParams.getCurrentSortedColumns(),gridParams.getCurrentSortedVersusColumns(),attribute2dbField);

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
      for(int i=0;i<values.size();i++)
        pstmt.setObject(i+1,values.get(i));
      ResultSet rset = pstmt.executeQuery();
      long t2 = System.currentTimeMillis();

      // prepare setter methods of the v.o...
      ArrayList cols = getColumns(baseSQL);
      Method[] setterMethods = new Method[cols.size()];
      Method getter = null;
      for(int i=0;i<cols.size();i++) {
        attributeName = (String)field2Attribute.get(cols.get(i));
        if (attributeName==null) {
          return new ErrorResponse("Attribute not found in 'attribute2dbField' argument for database field '"+cols.get(i)+"'");
        }

        try {
          getter = valueObjectClass.getMethod(
            "get" +
            attributeName.substring(0, 1).
            toUpperCase() +
            attributeName.substring(1),
            new Class[0]
          );
        }
        catch (NoSuchMethodException ex2) {
          getter = valueObjectClass.getMethod("is"+attributeName.substring(0,1).toUpperCase()+attributeName.substring(1),new Class[0]);
        }
        setterMethods[i] = valueObjectClass.getMethod("set"+attributeName.substring(0,1).toUpperCase()+attributeName.substring(1),new Class[]{getter.getReturnType()});
      }

      int rowCount = 0;
      int resultSetLength = -1;
      int action = gridParams.getAction();
      int startPos = gridParams.getStartPos();
      if (rowsToRead==1) {
        // read a block of rows...

        if (action==GridParams.LAST_BLOCK_ACTION) {
          try {
            rset.last();
            resultSetLength = rset.getRow();
          }
          catch (SQLException ex4) {
            // last & getRow methods not supported!
            while(rset.next())
              rowCount++;
            resultSetLength = rowCount;
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

      ArrayList list = new ArrayList();
      Object vo = null;
      Object value = null;
      while (
          action==GridParams.LAST_BLOCK_ACTION && rset.previous() ||
          action==GridParams.NEXT_BLOCK_ACTION && rset.next() ||
          action==GridParams.PREVIOUS_BLOCK_ACTION && rset.previous()) {
        rowCount++;

        vo = valueObjectClass.newInstance();
        for(int i=0;i<cols.size();i++) {
          if (setterMethods[i].getParameterTypes()[0].equals(String.class))
            value = rset.getString(i+1);
          else if (setterMethods[i].getParameterTypes()[0].equals(Boolean.class) ||
                   setterMethods[i].getParameterTypes()[0].equals(boolean.class)) {
            value = rset.getString(i + 1);
            if (value!=null && value.equals(booleanTrueValue))
              value = Boolean.TRUE;
            else if (value!=null && value.equals(booleanFalseValue))
              value = Boolean.FALSE;
          }
          else if (setterMethods[i].getParameterTypes()[0].equals(BigDecimal.class))
            value = rset.getBigDecimal(i+1);
          else if (setterMethods[i].getParameterTypes()[0].equals(Double.class)) {
            value = rset.getBigDecimal(i+1);
            if (value!=null)
              value = new Double(((BigDecimal)value).doubleValue());
          }
          else if (setterMethods[i].getParameterTypes()[0].equals(Float.class)) {
            value = rset.getBigDecimal(i+1);
            if (value!=null)
              value = new Float(((BigDecimal)value).floatValue());
          }
          else if (setterMethods[i].getParameterTypes()[0].equals(Integer.class)) {
            value = rset.getBigDecimal(i+1);
            if (value!=null)
              value = new Integer(((BigDecimal)value).intValue());
          }
          else if (setterMethods[i].getParameterTypes()[0].equals(Long.class)) {
            value = rset.getBigDecimal(i+1);
            if (value!=null)
              value = new Long(((BigDecimal)value).longValue());
          }
          else if (setterMethods[i].getParameterTypes()[0].equals(java.util.Date.class) ||
                   setterMethods[i].getParameterTypes()[0].equals(java.sql.Date.class))
            value = rset.getDate(i+1);
          else if (setterMethods[i].getParameterTypes()[0].equals(java.sql.Timestamp.class))
            value = rset.getTimestamp(i+1);
          else
            value = rset.getObject(i+1);
          setterMethods[i].invoke(vo,new Object[]{value});
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
            userSessionPars.getUsername(),
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
      else
        return new VOListResponse(list,moreRows,resultSetLength);

    } catch (Throwable ex) {
      try {
       Logger.error(
           userSessionPars.getUsername(),
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
            userSessionPars.getUsername(),
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
   sql = sql.substring(sql.toLowerCase().indexOf("select")+7,sql.toLowerCase().indexOf("from"));
   StringTokenizer st = new StringTokenizer(sql,",");
   while(st.hasMoreTokens())
     list.add( st.nextToken().trim() );
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
     PreparedStatement pstmt = null;
     String params = "";
     String sql = null;
     try {
       // prepare getter methods of the v.o and the SQL statement...
       sql = "insert into "+tableName+"(";
       String sqlvalues = " values(";
       ArrayList values = new ArrayList();
       ArrayList whereValues = new ArrayList();

       Iterator it = attribute2dbField.keySet().iterator();
       Method getter = null;
       String attributeName = null;
       String field = null;
       Object value = null;
       int i=0;
       while(it.hasNext()) {
         attributeName = it.next().toString();
         field = (String)attribute2dbField.get(attributeName);
         try {
           getter = vo.getClass().getMethod(
             "get" +
             attributeName.substring(0, 1).toUpperCase() +
             attributeName.substring(1),
             new Class[0]
           );
         }
         catch (NoSuchMethodException ex2) {
           getter = vo.getClass().getMethod("is"+attributeName.substring(0,1).toUpperCase()+attributeName.substring(1),new Class[0]);
         }
         value = getter.invoke(vo,new Object[0]);
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
       for(i=0;i<values.size();i++)
         pstmt.setObject(i+1,values.get(i));
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
             userSessionPars.getUsername(),
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
             userSessionPars.getUsername(),
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
             userSessionPars.getUsername(),
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
    ArrayList whereValues = new ArrayList();
    long t2;
    try {
      ValueObject vo = null;
      for(int j=0;j<vos.size();j++) {
        vo = (ValueObject)vos.get(j);

        // prepare getter methods of the v.o and the SQL statement...
        sql.delete(0,sql.length());
        sqlvalues.delete(0,sqlvalues.length());
        sql.append("insert into ").append(tableName).append("(");
        sqlvalues.append(" values(");
        values.clear();
        whereValues.clear();

        it = attribute2dbField.keySet().iterator();
        i=0;
        while(it.hasNext()) {
          attributeName = it.next().toString();
          field = (String)attribute2dbField.get(attributeName);
          try {
            getter = vo.getClass().getMethod(
               "get" +
               attributeName.substring(0, 1).
               toUpperCase() +
               attributeName.substring(1),
               new Class[0]
            );
          }
          catch (NoSuchMethodException ex2) {
            getter = vo.getClass().getMethod(
               "is" +
               attributeName.substring(0, 1).
               toUpperCase() +
               attributeName.substring(1),
               new Class[0]
            );
          }
          value = getter.invoke(vo,new Object[0]);
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
        for(i=0;i<values.size();i++)
          pstmt.setObject(i+1,values.get(i));
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
              userSessionPars.getUsername(),
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
            userSessionPars.getUsername(),
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
            userSessionPars.getUsername(),
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
    PreparedStatement pstmt = null;
    String params = "";
    String sql = null;
    try {
      // prepare getter methods of the v.o and the SQL statement...
      sql = "update "+tableName+" set ";
      String where = " where ";
      ArrayList values = new ArrayList();
      ArrayList whereValues = new ArrayList();

      Iterator it = attribute2dbField.keySet().iterator();
      Method getter = null;
      String attributeName = null;
      String field = null;
      Object value = null;
      int i=0;
      while(it.hasNext()) {
        attributeName = it.next().toString();
        field = (String)attribute2dbField.get(attributeName);
        try {
          getter = newVO.getClass().getMethod(
            "get" +
            attributeName.substring(0, 1).
            toUpperCase() +
            attributeName.substring(1),
            new Class[0]
          );
        }
        catch (NoSuchMethodException ex2) {
          getter = newVO.getClass().getMethod(
             "is" +
             attributeName.substring(0, 1).
             toUpperCase() +
             attributeName.substring(1),
             new Class[0]
          );
        }
        value = getter.invoke(newVO,new Object[0]);
        if (value==null) {
          sql += field+"=null, ";
        }
        else {
          if (value instanceof Boolean) {
            if (((Boolean)value).booleanValue())
              value = booleanTrueValue;
            else
              value = booleanFalseValue;
          }

          sql += field+"=?, ";
          values.add(value);
        }

        value = getter.invoke(oldVO,new Object[0]);
        if (value==null) {
          where += field+" is null and ";
        }
        else {
          if (value instanceof Boolean) {
            if (((Boolean)value).booleanValue())
              value = booleanTrueValue;
            else
              value = booleanFalseValue;
          }

          where += field+"=? and ";
          whereValues.add(value);
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
      for(i=0;i<values.size();i++)
        pstmt.setObject(i+1,values.get(i));
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
            userSessionPars.getUsername(),
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
            userSessionPars.getUsername(),
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
            userSessionPars.getUsername(),
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

}
