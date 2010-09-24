package org.openswing.swing.table.permissions.java;

import java.io.*;
import java.util.*;


/**
 * <p>Title: OpenSwing Framework</p>
 * <p>Description: Grid permissions manager: it manages grid permissions fetching.</p>
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
public abstract class GridPermissionsManager {

  private String username = System.getProperty("user.name");


  /**
   * @return list of role identifiers associated to the specified user (Object[] values)
   * @throws Throwable throwed if fetching operation does not correctly accomplished
   */
  public abstract ArrayList getUserRoles() throws Throwable;


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
  public abstract GridPermissions getUserGridPermissions(String functionId,ArrayList userRoles,String[] columnAttributes,boolean[] columnsVisibility,boolean[] columnEditableInInsert,boolean[] columnsEditbleInEdit,boolean[] columnsMandatory) throws Throwable;


  /**
   * @return retrieve the "grid digest", i.e. a value that globally identify the current grid configuration; this digest is used to check if grid columns have been changed from last grid execution: in this case all grid permissions will be deleted
   * @throws Throwable throwed if fetching operation does not correctly accomplished
   * Note: this method returns null if no digest has been yet stored (i.e. this is the first time the grid is being viewed)
   */
  public abstract String getLastGridDigest(String functionId) throws Throwable;


  /**
   * @return retrieve the "grid digest", i.e. a value that globally identifies the current grid configuration; this digest is used to check if grid columns have been changed from last grid execution: in this case all grid permissions will be deleted
   * @throws Throwable throwed if fetching operation does not correctly accomplished
   */
  public final String getCurrentGridDigest(String[] columnNames,String functionId) throws Throwable {
    try {
      String colsName = "";
      for(int i=0;i<columnNames.length;i++)
        colsName += columnNames[i]+",";
      colsName += functionId;

      return colsName;
    } catch (Throwable ex){
      throw new IOException(ex.getMessage());
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
  public abstract void storeGridPermissionsDefaults(String functionId,String[] columnAttributes,String[] headerColumnNames,boolean[] columnsVisibility,boolean[] columnEditableInInsert,boolean[] columnsEditableInEdit,boolean[] columnsMandatory) throws Throwable;


  /**
   * Store the "grid digest", i.e. a value that globally identify the current grid configuration.
   * @throws Throwable throwed if storing operation does not correctly accomplished
   */
  public abstract void storeGridDigest(String functionId,String gridDigest) throws Throwable;


  /**
   * Delete all grid permissions for the specified grid identifier.
   * This method is automatically invoked if "grid digest" comparison lead to discover a grid change.
   * @throws Throwable throwed if storing operation does not correctly accomplished
   */
  public abstract void deleteAllGridPermissionsPerFunctionId(String functionId) throws Throwable;


  /**
   * @return current logged user
   */
  public final String getUsername() {
    return username;
  }


  /**
   * Set the current logged user.
   * @param username logged username
   */
  public final void setUsername(String username) {
    this.username = username;
  }



}
