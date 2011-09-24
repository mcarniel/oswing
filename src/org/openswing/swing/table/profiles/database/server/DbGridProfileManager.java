package org.openswing.swing.table.profiles.database.server;

import java.io.*;
import java.math.*;
import java.sql.*;
import java.text.*;
import java.util.*;
import java.util.Date;

import org.openswing.swing.message.send.java.*;
import org.openswing.swing.table.profiles.java.*;
import org.openswing.swing.util.java.Consts;


/**
 * <p>Title: OpenSwing Framework</p>
 * <p>Description: Grid profile manager: it manages grid profile storing and fetching.
 * This implementation is based on database tables: it stores and retrieves user profiles from a table filtered by "username".
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
public class DbGridProfileManager extends GridProfileManager {

  /** connection manager */
  private DbConnectionSource dbConnectionSource = null;
  private DbActiveProfileDescriptor dbActiveProfileDescriptor = null;
  private DbDigestDescriptor dbDigestDescriptor = null;
  private DbProfileDescriptor dbProfileDescriptor = null;
  private SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");


  public DbGridProfileManager(
      DbConnectionSource dbConnectionSource,
      DbActiveProfileDescriptor dbActiveProfileDescriptor,
      DbDigestDescriptor dbDigestDescriptor,
      DbProfileDescriptor dbProfileDescriptor) {
    this.dbConnectionSource = dbConnectionSource;
    this.dbActiveProfileDescriptor = dbActiveProfileDescriptor;
    this.dbDigestDescriptor = dbDigestDescriptor;
    this.dbProfileDescriptor = dbProfileDescriptor;
  }


  /**
   * @param functionId identifier (functionId) associated to the grid
   * @return list of GridProfileDescription objects
   * @throws Throwable throwed if fetching operation does not correctly accomplished
   */
  public ArrayList getUserProfiles(final String functionId) throws Throwable {
    Connection conn = null;
    PreparedStatement pstmt = null;
    ResultSet rset = null;
    try {
      conn = dbConnectionSource.getConnection();
      String idFields = "";
      for(int i=0;i<dbProfileDescriptor.getIdFieldNames().length;i++)
        idFields += dbProfileDescriptor.getIdFieldNames()[i]+",";
      pstmt = conn.prepareStatement(
        "select "+
        idFields+
        dbProfileDescriptor.getDescriptionFieldName()+","+
        dbProfileDescriptor.getDefaultProfileFieldName()+" "+
        "from "+dbProfileDescriptor.getProfileTableName()+" "+
        "where "+dbProfileDescriptor.getFunctionIdFieldName()+"=? "+
        "and "+dbProfileDescriptor.getUsernameFieldName()+"=? "+
        "order by "+dbProfileDescriptor.getDescriptionFieldName()
      );
      pstmt.setString(1,functionId);
      pstmt.setString(2,getUsername());
      rset = pstmt.executeQuery();

      ArrayList profiles = new ArrayList();
      Object[] id = null;
      while(rset.next()) {
        id = new Object[rset.getMetaData().getColumnCount()-2];
        for(int i=0;i<id.length;i++)
          id[i] = rset.getObject(i+1);
        profiles.add(new GridProfileDescription(
          id,
          rset.getString(id.length+1),
          rset.getString(id.length+2).equals("Y")
        ));
      }
      return profiles;
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
   * @return default user profile; null if the default profile has not been yet stored
   * @throws Throwable throwed if fetching operation does not correctly accomplished
   */
  public final GridProfile getDefaultProfile(final String functionId) throws Throwable {
    Connection conn = null;
    PreparedStatement pstmt = null;
    ResultSet rset = null;
    try {
      conn = dbConnectionSource.getConnection();
      String idFields = "";
      for(int i=0;i<dbProfileDescriptor.getIdFieldNames().length;i++)
        idFields += dbProfileDescriptor.getIdFieldNames()[i]+",";
      pstmt = conn.prepareStatement(
        "select "+
        dbProfileDescriptor.getColumnsAttributeFieldName()+","+
        dbProfileDescriptor.getColumnsVisibilityFieldName()+","+
        dbProfileDescriptor.getColumnsWidthFieldName()+","+
        dbProfileDescriptor.getCurrentSortedColumnsFieldName()+","+
        dbProfileDescriptor.getCurrentSortedVersusColumnsFieldName()+","+
        dbProfileDescriptor.getDefaultProfileFieldName()+","+
        dbProfileDescriptor.getDescriptionFieldName()+","+
        dbProfileDescriptor.getFunctionIdFieldName()+","+
        idFields+
        dbProfileDescriptor.getQuickFilterValuesFieldName()+","+
        dbProfileDescriptor.getUsernameFieldName()+" "+
        "from "+dbProfileDescriptor.getProfileTableName()+" "+
        "where "+dbProfileDescriptor.getFunctionIdFieldName()+"=? "+
        "and "+dbProfileDescriptor.getUsernameFieldName()+"=? "+
        "and "+dbProfileDescriptor.getDefaultProfileFieldName()+"='Y'"
      );
      pstmt.setString(1,functionId);
      pstmt.setString(2,getUsername());
      rset = pstmt.executeQuery();

      ArrayList profiles = new ArrayList();
      Object[] ids = null;
      GridProfile profile = null;
      if(rset.next()) {
        return getUserProfileFromResultSet(rset);
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


  /**
   * @param id grid profile identifier
   * @return user profile
   * @throws Throwable throwed if fetching operation does not correctly accomplished
   */
  public GridProfile getUserProfile(String functionId,Object id) throws Throwable {
    Connection conn = null;
    PreparedStatement pstmt = null;
    ResultSet rset = null;
    try {
      conn = dbConnectionSource.getConnection();
      String idFields = "";
      String idWhereFields = "";
      Object[] idWhereFieldValues = (Object[])id;
      for(int i=0;i<dbProfileDescriptor.getIdFieldNames().length;i++) {
        idFields += dbProfileDescriptor.getIdFieldNames()[i]+",";
        idWhereFields += dbProfileDescriptor.getIdFieldNames()[i]+"=? and ";
      }
      idWhereFields = idWhereFields.substring(0,idWhereFields.length()-4);

      pstmt = conn.prepareStatement(
        "select "+
        dbProfileDescriptor.getColumnsAttributeFieldName()+","+
        dbProfileDescriptor.getColumnsVisibilityFieldName()+","+
        dbProfileDescriptor.getColumnsWidthFieldName()+","+
        dbProfileDescriptor.getCurrentSortedColumnsFieldName()+","+
        dbProfileDescriptor.getCurrentSortedVersusColumnsFieldName()+","+
        dbProfileDescriptor.getDefaultProfileFieldName()+","+
        dbProfileDescriptor.getDescriptionFieldName()+","+
        dbProfileDescriptor.getFunctionIdFieldName()+","+
        idFields+
        dbProfileDescriptor.getQuickFilterValuesFieldName()+","+
        dbProfileDescriptor.getUsernameFieldName()+" "+
        "from "+dbProfileDescriptor.getProfileTableName()+" "+
        "where "+dbProfileDescriptor.getFunctionIdFieldName()+"=? "+
        "and "+idWhereFields
      );
      pstmt.setString(1,functionId);

      for(int i=0;i<idWhereFieldValues.length;i++)
        pstmt.setObject(i+2,idWhereFieldValues[i]);
      rset = pstmt.executeQuery();

      if(rset.next()) {
        return getUserProfileFromResultSet(rset);
      }
      else
        throw new IOException("Record not found: "+id);
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
     * @param rset ResultSet having a record related to a GridProfile
     * @return GridProfile created from the record of the ResultSet
     */
  private GridProfile getUserProfileFromResultSet(ResultSet rset) throws Throwable {
    /*
      dbProfileDescriptor.getColumnsAttributeFieldName()+","+
      dbProfileDescriptor.getColumnsVisibilityFieldName()+","+
      dbProfileDescriptor.getColumnsWidthFieldName()+","+
      dbProfileDescriptor.getCurrentSortedColumnsFieldName()+","+
      dbProfileDescriptor.getCurrentSortedVersusColumnsFieldName()+","+
      dbProfileDescriptor.getDefaultProfileFieldName()+","+
      dbProfileDescriptor.getDescriptionFieldName()+","+
      dbProfileDescriptor.getFunctionIdFieldName()+","+
      idFields+
      dbProfileDescriptor.getQuickFilterValuesFieldName()+","+
      dbProfileDescriptor.getUsernameFieldName()+" "+
    */

    String description = rset.getString(7);
    String functionId = rset.getString(8);
    String username = rset.getString(rset.getMetaData().getColumnCount());

    String aux = rset.getString(4); // currentSortedColumns
    ArrayList currentSortedColumns = new ArrayList();
    StringTokenizer st = new StringTokenizer(aux,",");
    while(st.hasMoreTokens())
      currentSortedColumns.add( st.nextToken() );

    aux = rset.getString(5); // currentSortedVersusColumns
    ArrayList currentSortedVersusColumns = new ArrayList();
    st = new StringTokenizer(aux,",");
    while(st.hasMoreTokens())
      currentSortedVersusColumns.add( st.nextToken() );

    aux = rset.getString(1); // columnsAttribute
    st = new StringTokenizer(aux,",");
    int cols = st.countTokens();
    String[] columnsAttribute = new String[cols];
    int i = 0;
    while(st.hasMoreTokens())
      columnsAttribute[i++] = st.nextToken();

    aux = rset.getString(2); // columnsVisibility
    boolean[] columnsVisibility = new boolean[cols];
    st = new StringTokenizer(aux,",");
    i = 0;
    while(st.hasMoreTokens())
      columnsVisibility[i++] = st.nextToken().toLowerCase().equals("true");

    aux = rset.getString(3); // columnsWidth
    int[] columnsWidth = new int[cols];
    st = new StringTokenizer(aux,",");
    i = 0;
    while(st.hasMoreTokens())
      columnsWidth[i++] = Integer.parseInt( st.nextToken().trim() );

    aux = rset.getString(6); // defaultProfile
    boolean defaultProfile = aux.equals("Y");

    // id...
    Object[] id = new Object[rset.getMetaData().getColumnCount()-10];
    for(i=9;i<=rset.getMetaData().getColumnCount()-2;i++)
      id[i-9] = rset.getObject(i);

    // quickFilterValues
    HashMap quickFilterValues = new HashMap(); // hashtable which contains the associations: attribute name, new FilterWhereClause[2] {FilterWhereClause,FilterWhereClause})
    String attributeName = null;
    String operator1,typevalue1,operator2,typevalue2;
    Object value1,value2;
    FilterWhereClause[] filter = new FilterWhereClause[2];
    String filters = rset.getString(rset.getMetaData().getColumnCount()-1);
    StringTokenizer f = new StringTokenizer(filters,"\n");
    ArrayList values = null;
    String[] tokens = null;
    while(f.hasMoreTokens()) { // f = attribute name,operator1,typevalue1,value1a\tvalue1b\tvalue1c...[,operator2,typevalue2,value2...]
      st = new StringTokenizer(f.nextToken(),",");
      filter = new FilterWhereClause[2];
      attributeName = st.nextToken();
      operator1 = st.nextToken();
      typevalue1 = st.nextToken();
      value1 = st.nextToken();

      tokens = value1.toString().split("\t");
      if (tokens.length==1 && !operator1.equals(Consts.IN)) {
        if (typevalue1.equals("D"))
          value1 = sdf.parse(value1.toString());
        else if (typevalue1.equals("N"))
          value1 = new BigDecimal(value1.toString());
      }
      else {
        values = new ArrayList();
        for(int j=0;j<tokens.length;j++)
          if (typevalue1.equals("D"))
            values.add(sdf.parse(tokens[j]));
          else if (typevalue1.equals("N"))
            values.add(new BigDecimal(tokens[j]));
          else
            values.add(tokens[j]);
        value1 = values;
      }
      filter[0] = new FilterWhereClause(attributeName,operator1,value1);

      if (st.hasMoreTokens()) {
        operator2 = st.nextToken();
        typevalue2 = st.nextToken();
        value2 = st.nextToken();

        tokens = value2.toString().split("\t");
        if (tokens.length==1 && !operator2.equals(Consts.IN)) {
          if (typevalue2.equals("D"))
            value1 = sdf.parse(value2.toString());
          else if (typevalue2.equals("N"))
            value1 = new BigDecimal(value2.toString());
        }
        else {
          values = new ArrayList();
          for(int j=0;j<tokens.length;j++)
            if (typevalue1.equals("D"))
              values.add(sdf.parse(tokens[j]));
            else if (typevalue1.equals("N"))
              values.add(new BigDecimal(tokens[j]));
            else
              values.add(tokens[j]);
          value2 = values;
        }

        filter[1] = new FilterWhereClause(attributeName,operator2,value2);
      }
      else
        filter[1] = null;

      quickFilterValues.put( attributeName,filter );
    }

    return new GridProfile(
      id,
      description, // description
      functionId, // functionId
      username, // username
      currentSortedColumns, // currentSortedColumns
      currentSortedVersusColumns, // currentSortedVersusColumns
      quickFilterValues, // quickFilterValues
      columnsAttribute, // columnsAttribute
      columnsVisibility, // columnsVisibility
      columnsWidth, // columnsWidth
      defaultProfile // defaultProfile
    );
  }


  /**
   * Store the specified grid profile.
   * @param profile profile to store
   * @return profile id
   * @throws Throwable throwed if storing operation does not correctly accomplished
   * Note: if profile.getId() is null then this method must define id property.
   */
  public Object storeUserProfile(GridProfile profile) throws Throwable {
    Connection conn = null;
    PreparedStatement pstmt = null;
    ResultSet rset = null;
    try {
      conn = dbConnectionSource.getConnection();

      if (profile.getId()==null || ((Object[])profile.getId())[0]==null)
        profile.setId( dbProfileDescriptor.getNewIdFieldValues(conn,profile) );

      String idFields = "";
      String idSetFields = "";
      String idWhereFields = "";
      String pkQuestions = "";
      for(int i=0;i<dbProfileDescriptor.getIdFieldNames().length;i++) {
        idFields += dbProfileDescriptor.getIdFieldNames()[i]+",";
        idSetFields += dbProfileDescriptor.getIdFieldNames()[i]+"=?,";
        idWhereFields += dbProfileDescriptor.getIdFieldNames()[i]+"=? and ";
        pkQuestions += "?,";
      }
      idWhereFields = idWhereFields.substring(0,idWhereFields.length()-4);
      Object[] idValues = (Object[])profile.getId();

      String currentSortedColumns = "";
      for(int i=0;i<profile.getCurrentSortedColumns().size();i++)
        currentSortedColumns += profile.getCurrentSortedColumns().get(i)+",";
      if (currentSortedColumns.length()>0)
        currentSortedColumns = currentSortedColumns.substring(0,currentSortedColumns.length()-1);

      String currentSortedVersusColumns = "";
      for(int i=0;i<profile.getCurrentSortedVersusColumns().size();i++)
        currentSortedVersusColumns += profile.getCurrentSortedVersusColumns().get(i)+",";
      if (currentSortedVersusColumns.length()>0)
        currentSortedVersusColumns = currentSortedVersusColumns.substring(0,currentSortedVersusColumns.length()-1);

      String columnsAttribute = "";
      for(int i=0;i<profile.getColumnsAttribute().length;i++)
        columnsAttribute += profile.getColumnsAttribute()[i]+",";
      if (columnsAttribute.length()>0)
        columnsAttribute = columnsAttribute.substring(0,columnsAttribute.length()-1);

      String columnsVisibility = "";
      for(int i=0;i<profile.getColumnsVisibility().length;i++)
        columnsVisibility += profile.getColumnsVisibility()[i]+",";
      if (columnsVisibility.length()>0)
        columnsVisibility = columnsVisibility.substring(0,columnsVisibility.length()-1);

      String columnsWidth = "";
      for(int i=0;i<profile.getColumnsWidth().length;i++)
        columnsWidth += profile.getColumnsWidth()[i]+",";
      if (columnsWidth.length()>0)
        columnsWidth = columnsWidth.substring(0,columnsWidth.length()-1);

      Iterator it = profile.getQuickFilterValues().keySet().iterator();
      String aux = "";
      FilterWhereClause[] filter = null;
      String attributeName = null;
      String filters = "";
      while(it.hasNext()) {
        attributeName = it.next().toString();
        filter = (FilterWhereClause[])profile.getQuickFilterValues().get(attributeName);
        aux = attributeName+","+filter[0].getOperator()+",";
        if (filter[0].getValue()!=null) {

          if (filter[0].getOperator().equals(Consts.IN) || filter[0].getValue() instanceof ArrayList) {
            ArrayList values = (ArrayList)filter[0].getValue();
            if (values.size()>0)
              if (values.get(0) instanceof Date)
                aux += "D,";
              else if (values.get(0) instanceof Number)
                aux += "N,";
              else
                aux += "T,";

            for(int j=0;j<values.size();j++)
              if (values.get(j) instanceof Date)
                aux += sdf.format( values.get(j) )+"\t";
              else if (values.get(j) instanceof Number)
                aux += values.get(j)+"\t";
              else
                aux += values.get(j)+"\t";
          }
          else {
            if (filter[0].getValue() instanceof Date)
              aux += "D,"+sdf.format( filter[0].getValue() );
            else if (filter[0].getValue() instanceof Number)
              aux += "N,"+filter[0].getValue();
            else
              aux += "T,"+filter[0].getValue();
          }
        }

        if (filter[1]!=null) {
          aux += ","+filter[1].getOperator()+",";
          if (filter[1].getValue()!=null) {
            if (filter[1].getOperator().equals(Consts.IN) || filter[1].getValue() instanceof ArrayList) {
              ArrayList values = (ArrayList)filter[1].getValue();
              if (values.size()>0)
                if (values.get(0) instanceof Date)
                  aux += "D,";
                else if (values.get(0) instanceof Number)
                  aux += "N,";
                else
                  aux += "T,";

              for(int j=0;j<values.size();j++)
                if (values.get(j) instanceof Date)
                  aux += sdf.format( values.get(j) )+"\t";
                else if (values.get(j) instanceof Number)
                  aux += values.get(j)+"\t";
                else
                  aux += values.get(j)+"\t";
            }
            else {
              if (filter[1].getValue() instanceof Date)
                aux += "D,"+sdf.format( filter[1].getValue() );
              else if (filter[1].getValue() instanceof Number)
                aux += "N,"+filter[1].getValue();
              else
                aux += "T,"+filter[1].getValue();
            }
          }
        }
        filters += aux+"\n";
      }

      Hashtable h1 = dbProfileDescriptor.storeUserProfileOnSetUpdate();
      Hashtable h2 = dbProfileDescriptor.storeUserProfileOnWhereUpdate();
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
        "update "+dbProfileDescriptor.getProfileTableName()+" "+
        "set "+
        dbProfileDescriptor.getColumnsAttributeFieldName()+"=?,"+
        dbProfileDescriptor.getColumnsVisibilityFieldName()+"=?,"+
        dbProfileDescriptor.getColumnsWidthFieldName()+"=?,"+
        dbProfileDescriptor.getCurrentSortedColumnsFieldName()+"=?,"+
        dbProfileDescriptor.getCurrentSortedVersusColumnsFieldName()+"=?,"+
        dbProfileDescriptor.getDefaultProfileFieldName()+"=?,"+
        dbProfileDescriptor.getDescriptionFieldName()+"=?,"+
        dbProfileDescriptor.getFunctionIdFieldName()+"=?,"+
        idSetFields+
        dbProfileDescriptor.getQuickFilterValuesFieldName()+"=?,"+
        dbProfileDescriptor.getUsernameFieldName()+"=? "+
        set+" "+
        "where "+idWhereFields+where
      );
      int len1 = 8+idValues.length;
      int len2 = 8+idValues.length+2+setValues.size();

      pstmt.setString(7,profile.getDescription());
      pstmt.setString(8,profile.getFunctionId());
      pstmt.setString(len1+2,profile.getUsername());
      for(int i=0;i<idValues.length;i++)
        pstmt.setObject(i+9,idValues[i]);
      pstmt.setString(4,currentSortedColumns);
      pstmt.setString(5,currentSortedVersusColumns);
      pstmt.setString(1,columnsAttribute);
      pstmt.setString(2,columnsVisibility);
      pstmt.setString(3,columnsWidth);
      pstmt.setString(6,profile.isDefaultProfile()?"Y":"N");
      pstmt.setString(len1+1,filters);
      for(int i=0;i<setValues.size();i++)
        pstmt.setObject(i+len1+2,setValues.get(i));
      for(int i=0;i<idValues.length;i++)
        pstmt.setObject(i+len2+1,idValues[i]);
      for(int i=0;i<whereValues.size();i++)
        pstmt.setObject(i+len2+idValues.length+1,whereValues.get(i));

      int num = pstmt.executeUpdate();

      if (num==0) {

        Hashtable h = dbProfileDescriptor.storeUserProfileOnInsert();
        String ins = "";
        en = h.keys();
        field = null;
        ArrayList insValues = new ArrayList();
        while(en.hasMoreElements()) {
          field = en.nextElement().toString();
          pkQuestions += "?,";
          ins += ","+field;
          insValues.add(h.get(field));
        }

        pstmt = conn.prepareStatement(
          "insert into "+dbProfileDescriptor.getProfileTableName()+"("+
          dbProfileDescriptor.getColumnsAttributeFieldName()+","+
          dbProfileDescriptor.getColumnsVisibilityFieldName()+","+
          dbProfileDescriptor.getColumnsWidthFieldName()+","+
          dbProfileDescriptor.getCurrentSortedColumnsFieldName()+","+
          dbProfileDescriptor.getCurrentSortedVersusColumnsFieldName()+","+
          dbProfileDescriptor.getDefaultProfileFieldName()+","+
          dbProfileDescriptor.getDescriptionFieldName()+","+
          dbProfileDescriptor.getFunctionIdFieldName()+","+
          idFields+
          dbProfileDescriptor.getQuickFilterValuesFieldName()+","+
          dbProfileDescriptor.getUsernameFieldName()+
          ins+") "+
          "values("+pkQuestions+"?,?,?,?,?,?,?,?,?,?)"
        );
        for(int i=0;i<idValues.length;i++)
          pstmt.setObject(i+9,idValues[i]);
        pstmt.setString(7,profile.getDescription());
        pstmt.setString(8,profile.getFunctionId());
        pstmt.setString(8+idValues.length+2,profile.getUsername());
        pstmt.setString(4,currentSortedColumns);
        pstmt.setString(5,currentSortedVersusColumns);
        pstmt.setString(1,columnsAttribute);
        pstmt.setString(2,columnsVisibility);
        pstmt.setString(3,columnsWidth);
        pstmt.setString(6,profile.isDefaultProfile()?"Y":"N");
        pstmt.setString(8+idValues.length+1,filters);

        for(int i=0;i<insValues.size();i++)
          pstmt.setObject(i+8+idValues.length+3,insValues.get(i));

        pstmt.execute();
      }

      conn.commit();
      return profile.getId();
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
   * Delete the specified grid profile.
   * @param id grid profile identifier
   * @throws Throwable throwed if deleting operation does not correctly accomplished
   */
  public void deleteUserProfile(String functionId,Object id) throws Throwable {
    Connection conn = null;
    PreparedStatement pstmt = null;
    try {
      conn = dbConnectionSource.getConnection();
      String idFields = "";
      for(int i=0;i<dbProfileDescriptor.getIdFieldNames().length;i++)
        idFields += dbProfileDescriptor.getIdFieldNames()[i]+"=? and ";
      idFields = idFields.substring(0,idFields.length()-4);

      Hashtable h = dbProfileDescriptor.deleteUserProfile(functionId,id);
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
        "delete from "+dbProfileDescriptor.getProfileTableName()+" "+
        "where "+idFields+where
      );
      Object[] idValues = (Object[])id;
      for(int i=0;i<idValues.length;i++)
        pstmt.setObject(i+1,idValues[i]);

      for(int i=0;i<whereValues.size();i++)
        pstmt.setObject(i+idValues.length+1,whereValues.get(i));

      int num = pstmt.executeUpdate();

      if (num==0)
        throw new IOException("Record not found: "+id);
      else
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
   * Delete all grid profiles, independently from the current user.
   * This method is automatically invoked if "grid digest" comparison lead to discover a grid change: in this case all grid profiles must be removed.
   * @param functionId identifier (functionId) associated to the grid
   * @throws Throwable throwed if deleting operation does not correctly accomplished
   */
  public void deleteAllGridProfiles(String functionId) throws Throwable {
    Connection conn = null;
    PreparedStatement pstmt = null;
    try {
      conn = dbConnectionSource.getConnection();

      Hashtable h = dbProfileDescriptor.deleteAllGridProfiles(functionId);
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
        "delete from "+dbProfileDescriptor.getProfileTableName()+" "+
        "where "+dbProfileDescriptor.getFunctionIdFieldName()+"=? "+where
      );
      pstmt.setString(1,functionId);

      for(int i=0;i<whereValues.size();i++)
        pstmt.setObject(i+2,whereValues.get(i));

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
   * @return retrieve the "grid digest", i.e. a value that globally identify the current grid configuration; this digest is used to check if grid columns have been changed from last grid execution: in this case all grid profiles will be deleted
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


  /**
   * @return retrieve the last profile identifier in action
   * @throws Throwable throwed if fetching operation does not correctly accomplished
   * Note: this method returns null if no profile identifier has been yet stored (i.e. this is the first time the grid is being viewed)
   */
  public Object getLastGridProfileId(String functionId) throws Throwable {
    Connection conn = null;
    PreparedStatement pstmt = null;
    ResultSet rset = null;
    try {
      conn = dbConnectionSource.getConnection();
      String idFields = "";
      for(int i=0;i<dbActiveProfileDescriptor.getIdFieldNames().length;i++)
        idFields += dbActiveProfileDescriptor.getIdFieldNames()[i]+",";
      idFields = idFields.substring(0,idFields.length()-1);

      pstmt = conn.prepareStatement(
        "select "+idFields+" "+
        "from "+dbActiveProfileDescriptor.getActiveProfileTableName()+" "+
        "where "+dbActiveProfileDescriptor.getFunctionIdFieldName()+"=? "+
        "and "+dbActiveProfileDescriptor.getUsernameFieldName()+"=? "
      );
      pstmt.setString(1,functionId);
      pstmt.setString(2,getUsername());
      rset = pstmt.executeQuery();

      if(rset.next()) {
        Object[] id = new Object[dbActiveProfileDescriptor.getIdFieldNames().length];
        for(int i=0;i<id.length;i++)
          id[i] = rset.getObject(i+1);
        return id;
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


  /**
   * Store the current profile identifier in action.
   * @throws Throwable throwed if storing operation does not correctly accomplished
   */
  public void storeGridProfileId(String functionId,Object id) throws Throwable {
    Connection conn = null;
    PreparedStatement pstmt = null;
    try {
      conn = dbConnectionSource.getConnection();
      String idFields = "";
      String idSetFields = "";
      String idQuestions = "";
      for(int i=0;i<dbActiveProfileDescriptor.getIdFieldNames().length;i++) {
        idFields += dbActiveProfileDescriptor.getIdFieldNames()[i]+",";
        idSetFields += dbActiveProfileDescriptor.getIdFieldNames()[i]+"=?,";
        idQuestions += "?,";
      }
      idSetFields = idSetFields.substring(0,idSetFields.length()-1);

      Hashtable h1 = dbActiveProfileDescriptor.storeGridProfileIdOnSetUpdate();
      Hashtable h2 = dbActiveProfileDescriptor.storeGridProfileIdOnWhereUpdate();
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
        "update "+dbActiveProfileDescriptor.getActiveProfileTableName()+" "+
        "set "+idSetFields+" "+set+" "+
        "where "+dbActiveProfileDescriptor.getFunctionIdFieldName()+"=? "+
        "and "+dbActiveProfileDescriptor.getUsernameFieldName()+"=? "+where
      );

      Object[] idValues = (Object[])id;
      for(int i=0;i<idValues.length;i++)
        pstmt.setObject(i+1,idValues[i]);

      for(int i=0;i<setValues.size();i++)
        pstmt.setObject(i+idValues.length+1,setValues.get(i));

      pstmt.setString(idValues.length+1+setValues.size(),functionId);
      pstmt.setString(idValues.length+2+setValues.size(),getUsername());

      for(int i=0;i<whereValues.size();i++)
        pstmt.setObject(i+setValues.size()+idValues.length+3,whereValues.get(i));

      int num = pstmt.executeUpdate();

      if (num==0) {

        Hashtable h = dbActiveProfileDescriptor.storeGridProfileIdOnInsert();
        String ins = "";
        en = h.keys();
        field = null;
        ArrayList insValues = new ArrayList();
        while(en.hasMoreElements()) {
          field = en.nextElement().toString();
          idQuestions += "?,";
          ins += ","+field;
          insValues.add(h.get(field));
        }

        pstmt = conn.prepareStatement(
          "insert into "+dbActiveProfileDescriptor.getActiveProfileTableName()+"("+
          idFields+
          dbActiveProfileDescriptor.getFunctionIdFieldName()+","+
          dbActiveProfileDescriptor.getUsernameFieldName()+ins+") "+
          "values("+idQuestions+"?,?)"
        );
        for(int i=0;i<idValues.length;i++)
          pstmt.setObject(i+1,idValues[i]);
        pstmt.setString(idValues.length+1,functionId);
        pstmt.setString(idValues.length+2,getUsername());

        for(int i=0;i<insValues.size();i++)
          pstmt.setObject(i+idValues.length+3,insValues.get(i));

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
   * Delete all grid profiles identifiers for the current user.
   * This method is automatically invoked if "grid digest" comparison lead to discover a grid change.
   * @throws Throwable throwed if storing operation does not correctly accomplished
   */
  public void deleteAllGridProfileIds(String functionId) throws Throwable {
    Connection conn = null;
    PreparedStatement pstmt = null;
    try {
      conn = dbConnectionSource.getConnection();

      Hashtable h = dbActiveProfileDescriptor.deleteAllGridProfileIds(functionId);
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
        "delete from "+dbActiveProfileDescriptor.getActiveProfileTableName()+" "+
        "where "+dbActiveProfileDescriptor.getFunctionIdFieldName()+"=? "+where
      );
      pstmt.setString(1,functionId);

      for(int i=0;i<whereValues.size();i++)
        pstmt.setObject(i+2,whereValues.get(i));

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



}
