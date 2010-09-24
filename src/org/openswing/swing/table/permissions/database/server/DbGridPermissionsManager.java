package org.openswing.swing.table.permissions.database.server;

import java.io.*;
import java.math.*;
import java.sql.*;
import java.text.*;
import java.util.*;
import java.util.Date;

import org.openswing.swing.message.send.java.*;
import org.openswing.swing.table.permissions.java.*;
import org.openswing.swing.util.java.Consts;


/**
 * <p>Title: OpenSwing Framework</p>
 * <p>Description: Grid permissions manager: it manages the fetching of grid permissions.
 * This implementation is based on database tables: it stores and retrieves user roles from a table and after that
 * the permissions from a second table filtered by user roles and grid identifier.
 * </p>
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
public class DbGridPermissionsManager extends GridPermissionsManager {

  /** connection manager */
  private DbConnectionSource dbConnectionSource = null;
  private DbDigestDescriptor dbDigestDescriptor = null;
  private DbPermissionsDescriptor dbPermissionsDescriptor = null;


  public DbGridPermissionsManager(
      DbConnectionSource dbConnectionSource,
      DbDigestDescriptor dbDigestDescriptor,
      DbPermissionsDescriptor dbPermissionsDescriptor) {
    this.dbConnectionSource = dbConnectionSource;
    this.dbDigestDescriptor = dbDigestDescriptor;
    this.dbPermissionsDescriptor = dbPermissionsDescriptor;
  }


  /**
   * @param username username to use to fetch associated roles
   * @return list of role identifiers associated to the specified user (Object[] values)
   * @throws Throwable throwed if fetching operation does not correctly accomplished
   */
  public final ArrayList getUserRoles() throws Throwable {
    Connection conn = null;
    PreparedStatement pstmt = null;
    ResultSet rset = null;
    try {
      conn = dbConnectionSource.getConnection();
      String roleIdFields = "";
      for(int i=0;i<dbPermissionsDescriptor.getRoleIdFieldNamesInUserRolesTable().length;i++)
        roleIdFields += dbPermissionsDescriptor.getRoleIdFieldNamesInUserRolesTable()[i]+",";
      roleIdFields = roleIdFields.substring(0,roleIdFields.length()-1);
      String sql =
        "select "+
        roleIdFields+" "+
        "from "+dbPermissionsDescriptor.getUserRolesTableName()+" "+
        "where "+
        dbPermissionsDescriptor.getUsernameFieldNameInUserRolesTable()+"=? ";
      for(int i=0;i<dbPermissionsDescriptor.getRolesWhereFieldNames().length;i++)
        sql += " and "+dbPermissionsDescriptor.getRolesWhereFieldNames()[i]+"=? ";
      pstmt = conn.prepareStatement(sql);
      pstmt.setString(1,getUsername());
      for(int i=0;i<dbPermissionsDescriptor.getRolesWhereValues().length;i++)
        pstmt.setObject(i+2,dbPermissionsDescriptor.getRolesWhereValues()[i]);
      rset = pstmt.executeQuery();

      ArrayList roleIds = new ArrayList();
      Object[] roleId = null;
      while(rset.next()) {
        roleId = new Object[rset.getMetaData().getColumnCount()];
        for(int i=0;i<roleId.length;i++)
          roleId[i] = rset.getObject(i+1);
        roleIds.add(roleId);
      }
      return roleIds;
    }
    finally {
      try {
        if (rset!=null)
          rset.close();
      }
      catch (Exception ex) {
      }
      try {
        if (pstmt!=null)
          pstmt.close();
      }
      catch (Exception ex) {
      }
      try {
        if (conn != null) {
          dbConnectionSource.releaseConnection(conn);
        }
      }
      catch (Exception ex1) {
      }
    }
  }


  /**
   * @param functionId identifier (functionId) associated to the grid
   * @param userRoles list of role identifiers associated to the specified user
   * @param columnAttributes list of attribute names, that identify columns
   * @param columnsVisibility define which columns are visible
   * @param columnEditableInInsert define which columns are editable on insert; used to correctly define GridPermissions content: a column will be marked as NOT editable if currently editable but NOT the inverse
   * @param columnEditableInEdit define which columns are editable on edit; used to correctly define GridPermissions content: a column will be marked as NOT editable if currently editable but NOT the inverse
   * @param columnsMandatory define which columns are required on insert/edit mode; used to correctly define GridPermissions content: a column will be marked as required if currently not required but NOT the inverse
   * @return GridPermissions object, built starting from user roles for the specified grid identifier
   * @throws Throwable throwed if fetching operation does not correctly accomplished
   */
  public final GridPermissions getUserGridPermissions(String functionId,ArrayList userRoles,String[] columnAttributes,boolean[] columnsVisibility,boolean[] columnEditableInInsert,boolean[] columnsEditableInEdit,boolean[] columnsMandatory) throws Throwable {
    Connection conn = null;
    PreparedStatement pstmt = null;
    ResultSet rset = null;
    try {
      conn = dbConnectionSource.getConnection();
      String sql =
        "select "+
        dbPermissionsDescriptor.getColumnsAttributeFieldNameInGridPermissionsTable()+","+
        dbPermissionsDescriptor.getEditableColumnsInInsertFieldNameInGridPermissionsTable()+","+
        dbPermissionsDescriptor.getEditableColumnsInEditFieldNameInGridPermissionsTable()+","+
        dbPermissionsDescriptor.getColumnsMandatoryFieldNameInGridPermissionsTable()+","+
        dbPermissionsDescriptor.getColumnsVisibilityFieldNameInGridPermissionsTable()+" "+
        "from "+dbPermissionsDescriptor.getGridPermissionsTableName()+" "+
        "where "+dbPermissionsDescriptor.getFunctionIdFieldNameInGridPermissionsTable()+"=? ";

      for(int j=0;j<userRoles.size();j++) {
        sql += " and (";
        for(int i=0;i<dbPermissionsDescriptor.getRoleIdFieldNamesInGridPermissionsTable().length;i++)
          sql += dbPermissionsDescriptor.getRoleIdFieldNamesInGridPermissionsTable()[i]+"=? or ";
        sql = sql.substring(0,sql.length()-3);
        sql += ") ";
      }

      pstmt = conn.prepareStatement(sql);
      pstmt.setString(1,functionId);
      Object[] roleId =null;
      int count = 2;
      for(int j=0;j<userRoles.size();j++) {
        roleId = (Object[])userRoles.get(j);
        for(int i=0;i<roleId.length;i++) {
          pstmt.setObject(count, roleId[i]);
          count++;
        }
      }
      rset = pstmt.executeQuery();

      GridPermissions permissions = new GridPermissions(
        functionId,
        getUsername(),
        (String[])columnAttributes.clone(),
        (boolean[])columnsVisibility.clone(),
        (boolean[])columnEditableInInsert.clone(),
        (boolean[])columnsEditableInEdit.clone(),
        (boolean[])columnsMandatory.clone()
      );

      String[] aux = null;
      while(rset.next()) {
        aux = rset.getString(1).split(","); // columnAttributes
        for(int i=0;i<aux.length;i++)
          permissions.getColumnsAttribute()[i] = aux[i];

        aux = rset.getString(2).split(","); // editableColumnsInInsertFieldName
        for(int i=0;i<aux.length;i++)
          permissions.getColumnsEditabilityInInsert()[i] = permissions.getColumnsEditabilityInInsert()[i] && aux[i].equals("true");

        aux = rset.getString(3).split(","); // editableColumnsInEdit
        for(int i=0;i<aux.length;i++)
          permissions.getColumnsEditabilityInEdit()[i] = permissions.getColumnsEditabilityInEdit()[i] && aux[i].equals("true");

        aux = rset.getString(4).split(","); // columnsMandatory
        for(int i=0;i<aux.length;i++)
          permissions.getColumnsMandatory()[i] = permissions.getColumnsMandatory()[i] || aux[i].equals("true");

        aux = rset.getString(5).split(","); // columnsVisibility
        for(int i=0;i<aux.length;i++)
          permissions.getColumnsVisibility()[i] = aux[i].equals("true");

      }

      return permissions;
    }
    finally {
      try {
        if (rset!=null)
          rset.close();
      }
      catch (Exception ex) {
      }
      try {
        if (pstmt!=null)
          pstmt.close();
      }
      catch (Exception ex) {
      }
      try {
        if (conn != null) {
          dbConnectionSource.releaseConnection(conn);
        }
      }
      catch (Exception ex1) {
      }
    }
  }


  /**
   * Store in grid permissions defaults table.
   * @param functionId identifier (functionId) associated to the grid
   * @param columnAttributes list of attribute names, that identify columns
   * @param headerColumnNames list of keys for columns, that will be translated
   * @param columnsVisibility define which columns are visible
   * @param columnEditableInInsert define which columns are editable on insert; used to correctly define GridPermissions content: a column will be marked as NOT editable if currently editable but NOT the inverse
   * @param columnEditableInEdit define which columns are editable on edit; used to correctly define GridPermissions content: a column will be marked as NOT editable if currently editable but NOT the inverse
   * @param columnsMandatory define which columns are required on insert/edit mode; used to correctly define GridPermissions content: a column will be marked as required if currently not required but NOT the inverse
   * @throws Throwable throwed if storing operation does not correctly accomplished
   */
  public void storeGridPermissionsDefaults(String functionId,String[] columnAttributes,String[] headerColumnNames,boolean[] columnsVisibility,boolean[] columnEditableInInsert,boolean[] columnsEditableInEdit,boolean[] columnsMandatory) throws Throwable {
    Connection conn = null;
    PreparedStatement pstmt = null;
    ResultSet rset = null;
    try {
      conn = dbConnectionSource.getConnection();

      String sql =
          "update "+dbPermissionsDescriptor.getGridPermissionsDefaultsTableName()+" set "+
          dbPermissionsDescriptor.getColumnsAttributeFieldNameInDefaultsTableName()+"=?, "+
          dbPermissionsDescriptor.getColumnsMandatoryFieldNameInDefaultsTableName()+"=?, "+
          dbPermissionsDescriptor.getColumnsVisibilityFieldNameInDefaultsTableName()+"=?, "+
          dbPermissionsDescriptor.getEditableColumnsInEditFieldNameInDefaultsTableName()+"=?, "+
          dbPermissionsDescriptor.getEditableColumnsInInsertFieldNameInDefaultsTableName()+"=?, "+
          dbPermissionsDescriptor.getColumnsHeaderFieldNameInGridPermissionsTable()+"=? "+
          " where "+
          dbPermissionsDescriptor.getFunctionIdFieldNameInDefaultsTableName()+"=? ";
      for(int i=0;i<dbPermissionsDescriptor.getOtherFieldNamesInDefaultsTableName().length;i++)
        sql += " and "+dbPermissionsDescriptor.getOtherFieldNamesInDefaultsTableName()[i]+"=? ";

      pstmt = conn.prepareStatement(sql);

      String aux = "";
      for(int i=0;i<columnAttributes.length;i++)
        aux += columnAttributes[i]+",";
      aux = aux.substring(0,aux.length()-1);
      pstmt.setString(1,aux);

      aux = "";
      for(int i=0;i<columnAttributes.length;i++)
        aux += (columnsMandatory[i]?"true":"false")+",";
      aux = aux.substring(0,aux.length()-1);
      pstmt.setString(2,aux);

      aux = "";
      for(int i=0;i<columnAttributes.length;i++)
        aux += (columnsVisibility[i]?"true":"false")+",";
      aux = aux.substring(0,aux.length()-1);
      pstmt.setString(3,aux);

      aux = "";
      for(int i=0;i<columnAttributes.length;i++)
        aux += (columnsEditableInEdit[i]?"true":"false")+",";
      aux = aux.substring(0,aux.length()-1);
      pstmt.setString(4,aux);

      aux = "";
      for(int i=0;i<columnAttributes.length;i++)
        aux += (columnEditableInInsert[i]?"true":"false")+",";
      aux = aux.substring(0,aux.length()-1);
      pstmt.setString(5,aux);

      aux = "";
      for(int i=0;i<columnAttributes.length;i++)
        aux += headerColumnNames[i]+",";
      aux = aux.substring(0,aux.length()-1);
      pstmt.setString(6,aux);

      pstmt.setString(7,functionId);
      for(int i=0;i<dbPermissionsDescriptor.getOtherFieldValuesInDefaultsTableName().length;i++)
        pstmt.setObject(i+8,dbPermissionsDescriptor.getOtherFieldValuesInDefaultsTableName()[i]);

      int num = pstmt.executeUpdate();
      if (num==0) {
        sql =
          "insert into "+dbPermissionsDescriptor.getGridPermissionsDefaultsTableName()+"("+
          dbPermissionsDescriptor.getColumnsAttributeFieldNameInDefaultsTableName()+","+
          dbPermissionsDescriptor.getColumnsMandatoryFieldNameInDefaultsTableName()+","+
          dbPermissionsDescriptor.getColumnsVisibilityFieldNameInDefaultsTableName()+","+
          dbPermissionsDescriptor.getEditableColumnsInEditFieldNameInDefaultsTableName()+","+
          dbPermissionsDescriptor.getEditableColumnsInInsertFieldNameInDefaultsTableName()+","+
           dbPermissionsDescriptor.getColumnsHeaderFieldNameInGridPermissionsTable()+","+
          dbPermissionsDescriptor.getFunctionIdFieldNameInDefaultsTableName();
        for(int i=0;i<dbPermissionsDescriptor.getOtherFieldNamesInDefaultsTableName().length;i++)
          sql += ","+dbPermissionsDescriptor.getOtherFieldNamesInDefaultsTableName()[i];
        sql += ") values(?,?,?,?,?,?,?";
        for(int i=0;i<dbPermissionsDescriptor.getOtherFieldNamesInDefaultsTableName().length;i++)
          sql += ",?";
        sql += ")";

        pstmt = conn.prepareStatement(sql);

        aux = "";
        for(int i=0;i<columnAttributes.length;i++)
          aux += columnAttributes[i]+",";
        aux = aux.substring(0,aux.length()-1);
        pstmt.setString(1,aux);

        aux = "";
        for(int i=0;i<columnAttributes.length;i++)
          aux += (columnsMandatory[i]?"true":"false")+",";
        aux = aux.substring(0,aux.length()-1);
        pstmt.setString(2,aux);

        aux = "";
        for(int i=0;i<columnAttributes.length;i++)
          aux += (columnsVisibility[i]?"true":"false")+",";
        aux = aux.substring(0,aux.length()-1);
        pstmt.setString(3,aux);

        aux = "";
        for(int i=0;i<columnAttributes.length;i++)
          aux += (columnsEditableInEdit[i]?"true":"false")+",";
        aux = aux.substring(0,aux.length()-1);
        pstmt.setString(4,aux);

        aux = "";
        for(int i=0;i<columnAttributes.length;i++)
          aux += (columnEditableInInsert[i]?"true":"false")+",";
        aux = aux.substring(0,aux.length()-1);
        pstmt.setString(5,aux);

        aux = "";
        for(int i=0;i<columnAttributes.length;i++)
          aux += headerColumnNames[i]+",";
        aux = aux.substring(0,aux.length()-1);
        pstmt.setString(6,aux);

        pstmt.setString(7,functionId);
        for(int i=0;i<dbPermissionsDescriptor.getOtherFieldValuesInDefaultsTableName().length;i++)
          pstmt.setObject(i+8,dbPermissionsDescriptor.getOtherFieldValuesInDefaultsTableName()[i]);

        pstmt.execute();
      }

      conn.commit();

    }
    catch (Throwable t) {
      throw new IOException(t.getMessage());
    }
    finally {
      try {
        if (rset!=null)
          rset.close();
      }
      catch (Exception ex) {
      }
      try {
        if (pstmt!=null)
          pstmt.close();
      }
      catch (Exception ex) {
      }
      try {
        if (conn != null) {
          dbConnectionSource.releaseConnection(conn);
        }
      }
      catch (Exception ex1) {
      }
    }

  }


  /**
   * Delete all grid permissions for the specified grid identifier, independently from the current user.
   * Grid permissions defaults are removed too.
   * This method is automatically invoked if "grid digest" comparison lead to discover a grid change: in this case all grid permissions must be removed.
   * @param functionId identifier (functionId) associated to the grid
   * @throws Throwable throwed if deleting operation does not correctly accomplished
   */
  public void deleteAllGridPermissionsPerFunctionId(String functionId) throws Throwable {
    Connection conn = null;
    PreparedStatement pstmt = null;
    try {
      conn = dbConnectionSource.getConnection();

      // remove grid permissions...
      Hashtable h = dbPermissionsDescriptor.deleteAllGridPermissions(functionId);
      String where = " and ";
      Enumeration en = h.keys();
      String field = null;
      ArrayList whereValues = new ArrayList();
      while(en.hasMoreElements()) {
        field = en.nextElement().toString();
        where += field+"=? and ";
        whereValues.add(h.get(field));
      }
      if (where.length()>5)
        where = where.substring(0,where.length()-4);
      else
        where = "";

      pstmt = conn.prepareStatement(
        "delete from "+dbPermissionsDescriptor.getGridPermissionsTableName()+" "+
        "where "+dbPermissionsDescriptor.getFunctionIdFieldNameInGridPermissionsTable()+"=? "+where
      );
      pstmt.setString(1,functionId);

      for(int i=0;i<whereValues.size();i++)
        pstmt.setObject(i+2,whereValues.get(i));

      pstmt.execute();

      // remove grid permissions defaults...
      String sql =
        "delete from "+dbPermissionsDescriptor.getGridPermissionsDefaultsTableName()+" "+
        "where "+dbPermissionsDescriptor.getFunctionIdFieldNameInDefaultsTableName()+"=? ";
      for(int i=0;i<dbPermissionsDescriptor.getOtherFieldNamesInDefaultsTableName().length;i++)
        sql += " and "+dbPermissionsDescriptor.getOtherFieldNamesInDefaultsTableName()[i]+"=? ";
      pstmt = conn.prepareStatement(sql);
      pstmt.setString(1,functionId);
      for(int i=0;i<whereValues.size();i++)
        pstmt.setObject(i+2,dbPermissionsDescriptor.getOtherFieldValuesInDefaultsTableName()[i]);
      pstmt.execute();

      conn.commit();
    }
    finally {
      try {
        if (pstmt!=null)
          pstmt.close();
      }
      catch (Exception ex) {
      }
      try {
        if (conn != null) {
          dbConnectionSource.releaseConnection(conn);
        }
      }
      catch (Exception ex1) {
      }
    }
  }


  /**
   * Store the "grid digest", i.e. a value that globally identify the current grid configuration.
   * @throws Throwable throwed if storing operation does not correctly accomplished
   */
  public void storeGridDigest(String functionId,String gridDigest) throws Throwable {
    Connection conn = null;
    PreparedStatement pstmt = null;
    try {
      conn = dbConnectionSource.getConnection();

      Hashtable h1 = dbDigestDescriptor.storeGridDigestOnSetUpdate();
      Hashtable h2 = dbDigestDescriptor.storeGridDigestOnWhereUpdate();
      String where = " and ";
      Enumeration en = h2.keys();
      String field = null;
      ArrayList whereValues = new ArrayList();
      while(en.hasMoreElements()) {
        field = en.nextElement().toString();
        where += field+"=? and ";
        whereValues.add(h2.get(field));
      }
      if (where.length()>5)
        where = where.substring(0,where.length()-4);
      else
        where = "";

      String set = ",";
      en = h1.keys();
      field = null;
      ArrayList setValues = new ArrayList();
      while(en.hasMoreElements()) {
        field = en.nextElement().toString();
        set += field+"=?,";
        setValues.add(h1.get(field));
      }
      if (set.length()>1)
        set = set.substring(0,set.length()-1);
      else
        set = "";

      pstmt = conn.prepareStatement(
        "update "+dbDigestDescriptor.getDigestTableName()+" "+
        "set "+dbDigestDescriptor.getDigestFieldName()+"=? "+set+" "+
        "where "+dbDigestDescriptor.getFunctionIdFieldName()+"=? "+where
      );
      pstmt.setString(1,gridDigest);
      pstmt.setString(2+setValues.size(),functionId);

      for(int i=0;i<setValues.size();i++)
        pstmt.setObject(i+2,setValues.get(i));
      for(int i=0;i<whereValues.size();i++)
        pstmt.setObject(i+setValues.size()+3,whereValues.get(i));

      int num = pstmt.executeUpdate();

      if (num==0) {

        Hashtable h = dbDigestDescriptor.storeGridDigestOnInsert();
        String ins = "";
        en = h.keys();
        field = null;
        String pkQuestions = "";
        ArrayList insValues = new ArrayList();
        while(en.hasMoreElements()) {
          field = en.nextElement().toString();
          pkQuestions += "?,";
          ins += ","+field;
          insValues.add(h.get(field));
        }

        pstmt = conn.prepareStatement(
          "insert into "+dbDigestDescriptor.getDigestTableName()+"("+
          dbDigestDescriptor.getDigestFieldName()+","+
          dbDigestDescriptor.getFunctionIdFieldName()+ins+") "+
          "values("+pkQuestions+"?,?)"
        );
        pstmt.setString(1,gridDigest);
        pstmt.setString(2,functionId);

        for(int i=0;i<insValues.size();i++)
          pstmt.setObject(i+3,insValues.get(i));
        pstmt.execute();
      }
      conn.commit();
    }
    finally {
      try {
        if (pstmt!=null)
          pstmt.close();
      }
      catch (Exception ex) {
      }
      try {
        if (conn != null) {
          dbConnectionSource.releaseConnection(conn);
        }
      }
      catch (Exception ex1) {
      }
    }
  }


  /**
   * @return retrieve the "grid digest", i.e. a value that globally identify the current grid configuration; this digest is used to check if grid columns have been changed from last grid execution: in this case all grid permissions will be deleted
   * @throws Throwable throwed if fetching operation does not correctly accomplished
   * Note: this method returns null if no digest has been yet stored (i.e. this is the first time the grid is being viewed)
   */
  public String getLastGridDigest(String functionId) throws Throwable {
    Connection conn = null;
    PreparedStatement pstmt = null;
    ResultSet rset = null;
    try {
      conn = dbConnectionSource.getConnection();
      pstmt = conn.prepareStatement(
        "select "+
        dbDigestDescriptor.getDigestFieldName()+" "+
        "from "+dbDigestDescriptor.getDigestTableName()+" "+
        "where "+dbDigestDescriptor.getFunctionIdFieldName()+"=? "
      );
      pstmt.setString(1,functionId);
      rset = pstmt.executeQuery();

      if(rset.next()) {
        return rset.getString(1);
      }
      return null;
    }
    finally {
      try {
        if (rset!=null)
          rset.close();
      }
      catch (Exception ex) {
      }
      try {
        if (pstmt!=null)
          pstmt.close();
      }
      catch (Exception ex) {
      }
      try {
        if (conn != null) {
          dbConnectionSource.releaseConnection(conn);
        }
      }
      catch (Exception ex1) {
      }
    }
  }



}
