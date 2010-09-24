package org.openswing.swing.table.permissions.database.server;

import java.util.*;


/**
 * <p>Title: OpenSwing Framework</p>
 * <p>Description: Descriptor of the table related to grid permissions fetching,
 * where the primary key is based on "functionId" and the role identifiers,
 * in order to create GridPermissions object.</p>
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
public interface DbPermissionsDescriptor {


  /**
   * @return name of the grid permissions table.
   */
  public String getGridPermissionsTableName();


  /**
   * @return database field names related to the role identifier attribute
   */
  public String[] getRoleIdFieldNamesInGridPermissionsTable();


  /**
   * @return database field name related to the "functionId" attribute of GridPermissions
   */
  public String getFunctionIdFieldNameInGridPermissionsTable();


  /**
   * @return database field name related to the "columnsAttribute" attribute of GridPermissions
   */
  public String getColumnsAttributeFieldNameInGridPermissionsTable();


  /**
   * @return database field name related to the "editableColumnsInInsert" attribute of GridPermissions
   */
  public String getEditableColumnsInInsertFieldNameInGridPermissionsTable();


  /**
   * @return database field name related to the "editableColumnsInEdit" attribute of GridPermissions
   */
  public String getEditableColumnsInEditFieldNameInGridPermissionsTable();


  /**
   * @return database field name related to the "columnsMandatory" attribute of GridPermissions
   */
  public String getColumnsMandatoryFieldNameInGridPermissionsTable();


  /**
   * @return database field name related to the "header column attribute"
   */
  public String getColumnsHeaderFieldNameInGridPermissionsTable();


  /**
   * @return database field name related to the "columnsVisibility" attribute of GridPermissions
   */
  public String getColumnsVisibilityFieldNameInGridPermissionsTable();


  /**
   * Callback method used to fill in the WHERE part of the delete SQL instruction with the specified collection of pairs <column name, column value>,
   * when executing the "deleteAllGridPermissionsPerFunctionId" method.
   */
  public Hashtable deleteAllGridPermissions(String functionId);






  /**
   * @return name of the user roles table.
   */
  public String getUserRolesTableName();


  /**
   * @return database field name related to the username
   */
  public String getUsernameFieldNameInUserRolesTable();


  /**
   * @return database field names related to the role identifier attribute
   */
  public String[] getRoleIdFieldNamesInUserRolesTable();


  /**
   * @return database field names in user roles table to add in where clause, when fetching user roles
   */
  public String[] getRolesWhereFieldNames();


  /**
   * @return values to set in where clause, when fetching user roles
   */
  public Object[] getRolesWhereValues();







  /**
   * @return name of the grid permissions table that stores default settings for a grid.
   */
  public String getGridPermissionsDefaultsTableName();


  /**
   * @return database field name related to grid permissions defaults table
   */
  public String getFunctionIdFieldNameInDefaultsTableName();


  /**
   * @return database pther field names in primary key of grid permissions defaults table
   */
  public String[] getOtherFieldNamesInDefaultsTableName();


  /**
   * @return database other field values in primary key related to grid permissions defaults table
   */
  public Object[] getOtherFieldValuesInDefaultsTableName();


  /**
   * @return database field name related to the "columnsAttribute" attribute of GridPermissions
   */
  public String getColumnsAttributeFieldNameInDefaultsTableName();


  /**
   * @return database field name related to the "currentSortedColumns" attribute of GridPermissions
   */
  public String getEditableColumnsInEditFieldNameInDefaultsTableName();


  /**
   * @return database field name related to the "currentSortedVersusColumns" attribute of GridPermissions
   */
  public String getEditableColumnsInInsertFieldNameInDefaultsTableName();


  /**
   * @return database field name related to the "quickFilterValues" attribute of GridPermissions
   */
  public String getColumnsMandatoryFieldNameInDefaultsTableName();


  /**
   * @return database field name related to the "columnsVisibility" attribute of GridPermissions
   */
  public String getColumnsVisibilityFieldNameInDefaultsTableName();

}


