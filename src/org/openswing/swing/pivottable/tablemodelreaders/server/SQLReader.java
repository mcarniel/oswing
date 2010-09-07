package org.openswing.swing.pivottable.tablemodelreaders.server;

import javax.swing.table.TableModel;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.HashMap;
import org.openswing.swing.pivottable.java.InputFilter;
import java.sql.*;
import java.util.Map;
import org.openswing.swing.logger.server.*;


/**
 * <p>Title: OpenSwing Framework</p>
 * <p>Description: SQL reader, used in PivotTableEngine to read data to analyze.</p>
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
public class SQLReader implements Reader {

  /** select SQL used to retrieve data to analyze */
  private String sql = null;

  /** bind variables used with SQL */
  private ArrayList bindVariables = null;

  /** SQL connection to use to retrieve data */
  private Connection conn = null;

  /** collection of pairs <column name,column index> */
  private HashMap indexes = new HashMap();

  /** attribute names */
  private String[] attributeNames = null;

  /** collaction of pairs <attribute name,db column name> */
  private Map attributesMap = null;

  /** collaction of pairs <db column name,attribute name> */
  private Map reverseAttributesMap = new HashMap();

  /** PreparedStatement used to esecute SQL */
  private PreparedStatement pstmt = null;

  /** PreparedStatement used to esecute SQL */
  private ResultSet rset = null;


  /**
   * Create a reader of TableModel, used in PivotTableEngine.
   * @param sql select SQL used to retrieve data to analyze
   * @param bindVariables bind variables used with SQL
   * @param conn SQL connection to use to retrieve data
   * @param attributesMap collection of pairs <attribute name,db column name>
   */
  public SQLReader(String sql,ArrayList bindVariables,Connection conn,Map attributesMap) {
    this.sql = sql;
    this.bindVariables = bindVariables;
    this.conn = conn;
    this.attributesMap = attributesMap;

    Iterator it = attributesMap.keySet().iterator();
    String colName = null;
    while(it.hasNext()) {
      colName = it.next().toString();
      this.reverseAttributesMap.put(attributesMap.get(colName),colName);
    }
  }


  /**
   * Create a reader of TableModel, used in PivotTableEngine.
   * @param sql select SQL used to retrieve data to analyze
   * @param bindVariables bind variables used with SQL
   * @param conn SQL connection to use to retrieve data
   * @param attributesMap collaction of pairs <attribute name,db column name>
   */
  public SQLReader(String sql,Connection conn,Map attributesMap) {
    this(sql,new ArrayList(),conn,attributesMap);
  }


  /**
   * Initialize reading.
   * @param inputFilter optional input filter, used to skip rows
   * @return <code>true</code> if reader has correctly initialize data to read, <code>false</code> otherwise
   */
  public final boolean initializeScrolling(InputFilter inputFilter) {
    try {
      ArrayList additionalBindVars = new ArrayList();
      String script = sql;
      String whereClause = inputFilter.getWhereClause(attributesMap,additionalBindVars);
      if (whereClause.length()>0) {
        int w = sql.replace('\n',' ').toLowerCase().indexOf(" where ");
        int o = sql.replace('\n',' ').toLowerCase().indexOf(" order by ");
        if (w==-1 && o==-1)
          script = sql+" WHERE "+whereClause;
        else if (w==-1 && o!=-1)
          script = sql.substring(0,o)+" WHERE "+whereClause+sql.substring(o);
        else if (w!=-1 && o!=-1)
          script = sql.substring(0,o)+" AND "+whereClause+sql.substring(o);
      }
      pstmt = conn.prepareStatement(script);

      for(int i=0;i<bindVariables.size();i++)
        pstmt.setObject(i+1,bindVariables.get(i));
      for(int i=0;i<additionalBindVars.size();i++)
        pstmt.setObject(bindVariables.size()+i+1,additionalBindVars.get(i));

      rset = pstmt.executeQuery();

      String colName = null;
      attributeNames = new String[rset.getMetaData().getColumnCount()];
      for(int i=0;i<rset.getMetaData().getColumnCount();i++) {
        colName = (String)reverseAttributesMap.get(rset.getMetaData().getColumnName(i+1));
        attributeNames[i] = colName;
        indexes.put(colName,new Integer(i));
      }

      return true;
    }
    catch (Exception ex) {
      Logger.error(null,this.getClass().getName(),"initializeScrolling",ex.getMessage(),ex);
      return false;
    }
  }


  /**
   * Get next row to read.
   * Note: this method can be called ONLY if "initializeScrolling" method has already been invoked.
   * @param inputFilter optional input filter, used to skip rows
   * @return <code>boolean</code> true if there exists a row, <code>false</code> if no other rows are available
   */
  public final boolean nextRow(InputFilter inputFilter) {
    boolean moreRows;
    try {
      moreRows = rset.next();
    }
    catch (Exception ex) {
      moreRows = false;
    }
    if (!moreRows) {
      try {
        rset.close();
      }
      catch (Exception ex1) {
      }
      try {
        pstmt.close();
      }
      catch (Exception ex1) {
      }
    }
    return moreRows;
  }


  /**
   * @param col column index
   * @return value stored at column index, related to current row
   */
  public final Object getValueAt(int col) {
    try {
      return rset.getObject(col + 1);
    }
    catch (SQLException ex) {
      return null;
    }
  }


  /**
   * @return number of columns defined in TableModel
   */
  public final int getColumnCount() {
    return attributeNames.length;
  }


  /**
   * @param column index
   * @return column name defined in TableModel, related to the specified column index
   */
  public final String getColumnName(int index) {
    return attributeNames[index];
  }


}
