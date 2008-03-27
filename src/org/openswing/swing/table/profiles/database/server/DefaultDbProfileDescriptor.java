package org.openswing.swing.table.profiles.database.server;

import java.sql.*;
import java.util.*;

import org.openswing.swing.table.profiles.java.*;

/**
 * <p>Title: OpenSwing Framework</p>
 * <p>Description: Default descriptor of the table related to grid profile storing, where the primary key is based on "functionId","username" and "id" attributes of the GridProfile.
 * The "id" can represent more than one database field.</p>
 * it is based on a table having the following structure:
 * CREATE TABLE PROFILES(ID VARCHAR,FUNCTION_ID VARCHAR,USERNAME VARCHAR,DESCRIPTION VARCHAR,SORTED_COLS VARCHAR,SORTED_VERSUS VARCHAR,FILTERS VARCHAR,COLS_POS VARCHAR,COLS_VIS VARCHAR,COLS_WIDTH VARCHAR,IS_DEFAULT CHAR(1),PRIMARY KEY(ID))
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
public class DefaultDbProfileDescriptor implements DbProfileDescriptor {


  private static final String PROFILES = "PROFILES";
  private static final String FUNCTION_ID = "FUNCTION_ID";
  private static final String USERNAME = "USERNAME";
  private static final String[] ID = new String[]{"ID"};
  private static final String DESCRIPTION = "DESCRIPTION";
  private static final String SORTED_COLS = "SORTED_COLS";
  private static final String SORTED_VERSUS = "SORTED_VERSUS";
  private static final String FILTERS = "FILTERS";
  private static final String COLS_POS = "COLS_POS";
  private static final String COLS_VIS = "COLS_VIS";
  private static final String COLS_WIDTH = "COLS_WIDTH";
  private static final String IS_DEFAULT = "IS_DEFAULT";
  private static final Hashtable EMPTY = new Hashtable();


  /**
   * @return name of the grid profile table.
   */
  public String getProfileTableName() {
    return PROFILES;
  }

  /**
   * @return name of the database fields related to the "id" attribute of GridProfile
   */
  public String[] getIdFieldNames() {
    return ID;
  }

  /**
   * @return values related to a new "id" (attribute of GridProfile)
   */
  public Object[] getNewIdFieldValues(Connection conn,GridProfile profile) throws Throwable {
    PreparedStatement pstmt = null;
    ResultSet rset = null;
    try {
      pstmt = conn.prepareStatement("select max(ID) from "+PROFILES);
      rset = pstmt.executeQuery();
      if (rset.next())
        return new Object[]{new Long(rset.getLong(1)+1)};
      else
        return new Object[]{new Long(1)};
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
    }
  }


  /**
   * @return database field name related to the "description" attribute of GridProfile
   */
  public String getDescriptionFieldName() {
    return DESCRIPTION;
  }


  /**
   * @return database field name related to the "functionId" attribute of GridProfile
   */
  public String getFunctionIdFieldName() {
    return FUNCTION_ID;
  }


  /**
   * @return database field name related to the "username" attribute of GridProfile
   */
  public String getUsernameFieldName() {
    return USERNAME;
  }


  /**
   * @return database field name related to the "currentSortedColumns" attribute of GridProfile
   */
  public String getCurrentSortedColumnsFieldName() {
    return SORTED_COLS;
  }


  /**
   * @return database field name related to the "currentSortedVersusColumns" attribute of GridProfile
   */
  public String getCurrentSortedVersusColumnsFieldName() {
    return SORTED_VERSUS;
  }


  /**
   * @return database field name related to the "quickFilterValues" attribute of GridProfile
   */
  public String getQuickFilterValuesFieldName() {
    return FILTERS;
  }


  /**
   * @return database field name related to the "columnsAttribute" attribute of GridProfile
   */
  public String getColumnsAttributeFieldName() {
    return COLS_POS;
  }


  /**
   * @return database field name related to the "columnsVisibility" attribute of GridProfile
   */
  public String getColumnsVisibilityFieldName() {
    return COLS_VIS;
  }


  /**
   * @return database field name related to the "columnsWidth" attribute of GridProfile
   */
  public String getColumnsWidthFieldName() {
    return COLS_WIDTH;
  }


  /**
   * @return database field name related to the "defaultProfile" attribute of GridProfile
   */
  public String getDefaultProfileFieldName() {
    return IS_DEFAULT;
  }


  /**
   * Callback method used to fill in the insert SQL instruction with the specified collection of pairs <column name, column value>,
   * when executing the "storeUserProfile" method. For instance a CREATE_USER field or a CREATE_DATE field.
   */
  public Hashtable storeUserProfileOnInsert() {
    return EMPTY;
  }


  /**
   * Callback method used to fill in the SET part of the update SQL instruction with the specified collection of pairs <column name, column value>,
   * when executing the "storeUserProfile" method. For instance an UPDATE_USER field or an UPDATE_DATE field.
   */
  public Hashtable storeUserProfileOnSetUpdate() {
    return EMPTY;
  }


  /**
   * Callback method used to fill in the WHERE part of the update SQL instruction with the specified collection of pairs <column name, column value>,
   * when executing the "storeUserProfile" method.
   */
  public Hashtable storeUserProfileOnWhereUpdate() {
    return EMPTY;
  }


  /**
   * Callback method used to fill in the WHERE part of the delete SQL instruction with the specified collection of pairs <column name, column value>,
   * when executing the "deleteUserProfile" method.
   */
  public Hashtable deleteUserProfile(String functionId,Object id) {
    return EMPTY;
  }



  /**
   * Callback method used to fill in the WHERE part of the delete SQL instruction with the specified collection of pairs <column name, column value>,
   * when executing the "deleteAllGridProfiles" method.
   */
  public Hashtable deleteAllGridProfiles(String functionId) {
    return EMPTY;
  }


}
