package org.openswing.swing.table.permissions.database.server;

import java.sql.*;
import java.util.*;

import org.openswing.swing.table.permissions.java.*;

/**
 * <p>Title: OpenSwing Framework</p>
 * <p>Description: Default descriptor of the table related to grid permissions storing,
 * where the primary key is based on "functionId",role identifier attributes of the GridPermissions.
 * The "id" can represent more than one database field.</p>
 * it is based on a table having the following structure:
 * CREATE TABLE GRID_PERMISSIONS(FUNCTION_ID VARCHAR,ROLE_ID VARCHAR,COLS_POS VARCHAR,EDIT_COLS_IN_INS VARCHAR,EDIT_COLS_IN_EDIT VARCHAR,REQUIRED_COLS VARCHAR,COLS_VIS VARCHAR,PRIMARY KEY(FUNCTION_ID,ROLE_ID))
 *
 * Morever, a second table is needed, to map username with its roles:
 * CREATE TABLE USER_ROLES(USERNAME VARCHAR,ROLE_ID VARCHAR,PRIMARY KEY(USERNAME,ROLE_ID))
 *
 * Finally, a third table is needed, to store default settings for grid, in order to externally define roles in GRID_PERMISSIONS table:
 * CREATE TABLE GRID_PERMISSIONS_DEFS(FUNCTION_ID VARCHAR,COLS_POS VARCHAR,EDIT_COLS_IN_INS VARCHAR,EDIT_COLS_IN_EDIT VARCHAR,REQUIRED_COLS VARCHAR,COLS_VIS VARCHAR,PRIMARY KEY(FUNCTION_ID))
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
public class DefaultDbPermissionsDescriptor implements DbPermissionsDescriptor {


  private static final String GRID_PERMISSIONS = "GRID_PERMISSIONS";
  private static final String USER_ROLES = "USER_ROLES";
  private static final String GRID_PERMISSIONS_DEFS = "GRID_PERMISSIONS_DEFS";
  private static final String FUNCTION_ID = "FUNCTION_ID";
  private static final String USERNAME = "USERNAME";
  private static final String[] ROLE_ID = new String[]{"ROLE_ID"};
  private static final String COLS_POS = "COLS_POS";
  private static final String EDIT_COLS_IN_EDIT = "EDIT_COLS_IN_EDIT";
  private static final String EDIT_COLS_IN_INS = "EDIT_COLS_IN_INS";
  private static final String REQUIRED_COLS = "REQUIRED_COLS";
  private static final String COLS_VIS = "COLS_VIS";
  private static final String COLS_NAME = "COLS_NAME";
  private static final Hashtable EMPTY = new Hashtable();


  /**
   * @return name of the grid permissions table.
   */
  public String getGridPermissionsTableName() {
    return GRID_PERMISSIONS;
  }


  /**
   * @return database field names related to the role identifier attribute
   */
  public String[] getRoleIdFieldNamesInGridPermissionsTable() {
    return ROLE_ID;
  }


  /**
   * @return database field name related to the "functionId" attribute of GridPermissions
   */
  public String getFunctionIdFieldNameInGridPermissionsTable() {
    return FUNCTION_ID;
  }


  /**
   * @return database field name related to the "columnsAttribute" attribute of GridProfile
   */
  public String getColumnsAttributeFieldNameInGridPermissionsTable() {
    return COLS_POS;
  }


  /**
   * @return database field name related to the "currentSortedColumns" attribute of GridPermissions
   */
  public String getEditableColumnsInEditFieldNameInGridPermissionsTable() {
    return EDIT_COLS_IN_EDIT;
  }


  /**
   * @return database field name related to the "currentSortedVersusColumns" attribute of GridPermissions
   */
  public String getEditableColumnsInInsertFieldNameInGridPermissionsTable() {
    return EDIT_COLS_IN_INS;
  }


  /**
   * @return database field name related to the "quickFilterValues" attribute of GridPermissions
   */
  public String getColumnsMandatoryFieldNameInGridPermissionsTable() {
    return REQUIRED_COLS;
  }


  /**
   * @return database field name related to the "columnsVisibility" attribute of GridPermissions
   */
  public String getColumnsVisibilityFieldNameInGridPermissionsTable() {
    return COLS_VIS;
  }


  /**
   * @return database field name related to the "header column attribute"
   */
  public String getColumnsHeaderFieldNameInGridPermissionsTable() {
    return COLS_NAME;
  }


  /**
   * Callback method used to fill in the WHERE part of the delete SQL instruction with the specified collection of pairs <column name, column value>,
   * when executing the "deleteAllGridPermissions" method.
   */
  public Hashtable deleteAllGridPermissions(String functionId) {
    return EMPTY;
  }






  /**
   * @return name of the user roles table.
   */
  public String getUserRolesTableName() {
    return USER_ROLES;
  }


  /**
   * @return database field name related to the username
   */
  public String getUsernameFieldNameInUserRolesTable() {
    return USERNAME;
  }


  /**
   * @return database field names related to the role identifier attribute
   */
  public String[] getRoleIdFieldNamesInUserRolesTable() {
    return ROLE_ID;
  }


  /**
   * @return database field names in user roles table to add in where clause, when fetching user roles
   */
  public String[] getRolesWhereFieldNames() {
    return new String[0];
  }


  /**
   * @return values to set in where clause, when fetching user roles
   */
  public Object[] getRolesWhereValues() {
    return new Object[0];
  }






  /**
   * @return name of the grid permissions table that stores default settings for a grid.
   */
  public String getGridPermissionsDefaultsTableName() {
    return GRID_PERMISSIONS_DEFS;
  }


  /**
   * @return database field name related to the "functionId" attribute of GridPermissions
   */
  public String getFunctionIdFieldNameInDefaultsTableName() {
    return FUNCTION_ID;
  }


  /**
   * @return database pther field names in primary key of grid permissions defaults table
   */
  public String[] getOtherFieldNamesInDefaultsTableName() {
    return new String[0];
  }


  /**
   * @return database other field values in primary key related to grid permissions defaults table
   */
  public Object[] getOtherFieldValuesInDefaultsTableName() {
    return new Object[0];
  }


  /**
   * @return database field name related to the "columnsAttribute" attribute of GridProfile
   */
  public String getColumnsAttributeFieldNameInDefaultsTableName() {
    return COLS_POS;
  }


  /**
   * @return database field name related to the "currentSortedColumns" attribute of GridPermissions
   */
  public String getEditableColumnsInEditFieldNameInDefaultsTableName() {
    return EDIT_COLS_IN_EDIT;
  }


  /**
   * @return database field name related to the "currentSortedVersusColumns" attribute of GridPermissions
   */
  public String getEditableColumnsInInsertFieldNameInDefaultsTableName() {
    return EDIT_COLS_IN_INS;
  }


  /**
   * @return database field name related to the "quickFilterValues" attribute of GridPermissions
   */
  public String getColumnsMandatoryFieldNameInDefaultsTableName() {
    return REQUIRED_COLS;
  }


  /**
   * @return database field name related to the "columnsVisibility" attribute of GridPermissions
   */
  public String getColumnsVisibilityFieldNameInDefaultsTableName() {
    return COLS_VIS;
  }


}
