package org.openswing.swing.customvo.server;

import java.sql.*;
import java.util.*;

import org.openswing.swing.customvo.java.*;
import org.openswing.swing.message.receive.java.*;
import org.openswing.swing.message.send.java.*;
import org.openswing.swing.server.*;


/**
 * <p>Title: OpenSwing Framework</p>
 * <p>Description: Utility class used to insert/update/delete records using java.sql.Connection object and a
 * dynamic description of value objects to manage.</p>
 * <p>Copyright: Copyright (C) 2006 Mauro Carniel</p>
 *
 * <p> This file is part of JAllInOne ERP/CRM application.
 * This application is free software; you can redistribute it and/or
 * modify it under the terms of the (LGPL) Lesser General Public
 * License as published by the Free Software Foundation;
 *
 *                GNU LESSER GENERAL PUBLIC LICENSE
 *                 Version 2.1, February 1999
 *
 * This application is distributed in the hope that it will be useful,
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
public class CustomValueObjectUtils {


  /**
   * Insert rows, starting from the list of maps provided.
   * @param customFields list of CustomFieldVO objects
   * @return Response
   */
  public static final ArrayList getCustomFields(
      Connection conn,
      String sql,
      ArrayList values
  ) throws Exception {
    PreparedStatement pstmt = null;
    ResultSet rset = null;
    CustomFieldVO vo = null;
    ArrayList fields = new ArrayList();
    try {
      HashSet tables = new HashSet();
      HashMap map = new HashMap();
      pstmt = conn.prepareStatement(sql);
      for(int i=0;i<values.size();i++)
        pstmt.setObject(i+1,values.get(i));
      rset = pstmt.executeQuery();
      ResultSetMetaData meta = rset.getMetaData();
      int sAttr = 0;
      int dAttr = 0;
      int nAttr = 0;
      StringBuffer aux = null;
      for(int i=0;i<meta.getColumnCount();i++) {
        vo = new CustomFieldVO();
        switch (meta.getColumnType(i+1)) {
          case Types.BIGINT:
          case Types.DECIMAL:
          case Types.DOUBLE:
          case Types.FLOAT:
          case Types.INTEGER:
          case Types.NUMERIC:
          case Types.REAL:
          case Types.SMALLINT:
            vo.setAttributeName("attributeNameN"+nAttr);
            vo.setAttributeType(vo.TYPE_NUM);
            vo.setDecimals(meta.getScale(i+1));
            vo.setIntegers(meta.getPrecision(i+1)-meta.getScale(i+1));
            nAttr++;
            break;
          case Types.CHAR:
            vo.setAttributeName("attributeNameS"+sAttr);
            vo.setAttributeType(vo.TYPE_CHAR);
            vo.setMaxChars(meta.getPrecision(i+1));
            sAttr++;
            break;
          case Types.DATE:
            vo.setAttributeName("attributeNameD"+dAttr);
            vo.setAttributeType(vo.TYPE_DATE);
            dAttr++;
            break;
          case Types.TIME:
            vo.setAttributeName("attributeNameD"+dAttr);
            vo.setAttributeType(vo.TYPE_TIME);
            dAttr++;
            break;
          case Types.TIMESTAMP:
            vo.setAttributeName("attributeNameD"+dAttr);
            vo.setAttributeType(vo.TYPE_DATE_TIME);
            dAttr++;
            break;
          default:
            vo.setAttributeName("attributeNameS"+sAttr);
            vo.setAttributeType(vo.TYPE_TEXT);
            if (meta.getPrecision(i+1)>2000)
              vo.setMaxChars(2000);
            else
              vo.setMaxChars(meta.getPrecision(i+1));
            sAttr++;
        }
        if (meta.getColumnDisplaySize(i+1)>20)
          vo.setColumnWidth(200);
        else if (meta.getColumnType(i+1)==Types.DATE ||
                 meta.getColumnType(i+1)==Types.TIME)
          vo.setColumnWidth(100);
        else if (meta.getColumnType(i+1)==Types.TIMESTAMP)
          vo.setColumnWidth(150);
        else
          vo.setColumnWidth(meta.getColumnDisplaySize(i+1)*20);

        aux = new StringBuffer(meta.getColumnName(i+1).replace('_',' ').toLowerCase());
        for(int t=0;t<aux.length();t++)
          if (t==0 || aux.charAt(t-1)==' ')
            aux.setCharAt(t,String.valueOf(aux.charAt(t)).toUpperCase().charAt(0));
        vo.setDescription(aux.toString());
        vo.setFieldName(meta.getColumnName(i+1));
        vo.setRequired(meta.isNullable(i+1)==meta.columnNoNulls);
        vo.setTableName(meta.getTableName(i+1));
        vo.setEditableOnInsert(true);
        vo.setEditableOnEdit(true);
        vo.setFilterable(true);
        vo.setSortable(true);
        tables.add(vo.getTableName());
        map.put(vo.getTableName()+"."+vo.getFieldName(),vo);
        fields.add(vo);
      }

      // searching for pk...
      ResultSet r = null;
      try {
        Iterator it = tables.iterator();
        String tableName = null;
        while (it.hasNext()) {
          tableName = it.next().toString();
          r = conn.getMetaData().getPrimaryKeys(null, null, tableName);
          while(r.next()) {
            vo = (CustomFieldVO)map.get(r.getString(3)+"."+r.getString(4));
            if (vo!=null) {
              vo.setPk(true);
              vo.setEditableOnEdit(false);
            }
          }
          r.close();
        }
      }
      catch (Exception ex2) {
        try { r.close(); } catch (Exception ee) {}
      }

    }
    catch (Exception ex) {
    }
    finally {
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
    return fields;
  }


  /**
   * Insert rows, starting from the list of maps provided.
   * @param customFields list of CustomFieldVO objects
   * @return Response
   */
  public static final Response getQuery(
      Connection conn,
      String baseSQL,
      ArrayList values,
      int action,
      int startIndex,
      Map filteredColumns,
      ArrayList currentSortedColumns,
      ArrayList currentSortedVersusColumns,
      ArrayList customFields,
      int blockSize,
      boolean logQuery
  ) throws Exception {
    HashMap attribute2dbField = new HashMap();
    CustomFieldVO vo = null;
    for(int i=0;i<customFields.size();i++) {
      vo = (CustomFieldVO)customFields.get(i);
      attribute2dbField.put(vo.getAttributeName(),vo.getTableName()+"."+vo.getFieldName());
      attribute2dbField.put(vo.getAttributeName(),vo.getFieldName());
    }

    return QueryUtil.getQuery(
      conn,
      baseSQL,
      values,
      attribute2dbField,
      CustomValueObject.class,
      "Y",
      "N",
      new GridParams(
        action,
        startIndex,
        filteredColumns,
        currentSortedColumns,
        currentSortedVersusColumns,
        new HashMap()
      ),
      blockSize,
      logQuery
    );
  }


  /**
   * Insert rows, starting from the list of maps provided.
   * @param customFields list of CustomFieldVO objects
   * @return Response
   */
  public static final Response getQuery(
      Connection conn,
      String baseSQL,
      ArrayList values,
      int action,
      int startIndex,
      Map filteredColumns,
      ArrayList currentSortedColumns,
      ArrayList currentSortedVersusColumns,
      ArrayList customFields,
      boolean logQuery
  ) throws Exception {
    HashMap attribute2dbField = new HashMap();
    CustomFieldVO vo = null;
    for(int i=0;i<customFields.size();i++) {
      vo = (CustomFieldVO)customFields.get(i);
      attribute2dbField.put(vo.getAttributeName(),vo.getTableName()+"."+vo.getFieldName());
    }

    return QueryUtil.getQuery(
      conn,
      baseSQL,
      values,
      attribute2dbField,
      CustomValueObject.class,
      "Y",
      "N",
      new GridParams(
        action,
        startIndex,
        filteredColumns,
        currentSortedColumns,
        currentSortedVersusColumns,
        new HashMap()
      ),
      logQuery
    );
  }





}
