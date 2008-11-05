package org.openswing.swing.table.profiles.java;

import java.io.*;
import java.util.*;


/**
 * <p>Title: OpenSwing Framework</p>
 * <p>Description: Grid profile manager: it manages grid profile storing and fetching.</p>
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
public abstract class GridProfileManager {

  private String username = System.getProperty("user.name");


  /**
   * @param functionId identifier (functionId) associated to the grid
   * @param username profile owner, i.e. the username of the current logged user
   * @return list of GridProfileDescription objects
   * @throws Throwable throwed if fetching operation does not correctly accomplished
   */
  public abstract ArrayList getUserProfiles(String functionId) throws Throwable;


  /**
   * @param id grid profile identifier
   * @return user profile
   * @throws Throwable throwed if fetching operation does not correctly accomplished
   */
  public abstract GridProfile getUserProfile(String functionId,Object id) throws Throwable;


  /**
   * @return default user profile
   * @throws Throwable throwed if fetching operation does not correctly accomplished
   */
  public abstract GridProfile getDefaultProfile(String functionId) throws Throwable;


  /**
   * Store the specified grid profile.
   * @param profile profile to store
   * @return profile id
   * @throws Throwable throwed if storing operation does not correctly accomplished
   * Note: if profile.getId() is null then this method must define id property.
   */
  public abstract Object storeUserProfile(GridProfile profile) throws Throwable;


  /**
   * Delete the specified grid profile.
   * @param id grid profile identifier
   * @throws Throwable throwed if deleting operation does not correctly accomplished
   */
  public abstract void deleteUserProfile(String functionId,Object id) throws Throwable;


  /**
   * Delete all grid profiles, independently from the current user.
   * This method is automatically invoked if "grid digest" comparison lead to discover a grid change: in this case all grid profiles must be removed.
   * @param functionId identifier (functionId) associated to the grid
   * @throws Throwable throwed if deleting operation does not correctly accomplished
   */
  public abstract void deleteAllGridProfiles(String functionId) throws Throwable;


  /**
   * @return retrieve the "grid digest", i.e. a value that globally identify the current grid configuration; this digest is used to check if grid columns have been changed from last grid execution: in this case all grid profiles will be deleted
   * @throws Throwable throwed if fetching operation does not correctly accomplished
   * Note: this method returns null if no digest has been yet stored (i.e. this is the first time the grid is being viewed)
   */
  public abstract String getLastGridDigest(String functionId) throws Throwable;


  /**
   * Store the "grid digest", i.e. a value that globally identify the current grid configuration.
   * @throws Throwable throwed if storing operation does not correctly accomplished
   */
  public abstract void storeGridDigest(String functionId,String gridDigest) throws Throwable;


  /**
   * @return retrieve the "grid digest", i.e. a value that globally identify the current grid configuration; this digest is used to check if grid columns have been changed from last grid execution: in this case all grid profiles will be deleted
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
   * @return retrieve the last profile identifier in action
   * @throws Throwable throwed if fetching operation does not correctly accomplished
   * Note: this method returns null if no profile identifier has been yet stored (i.e. this is the first time the grid is being viewed)
   */
  public abstract Object getLastGridProfileId(String functionId) throws Throwable;


  /**
   * Store the current profile identifier in action.
   * @throws Throwable throwed if storing operation does not correctly accomplished
   */
  public abstract void storeGridProfileId(String functionId,Object id) throws Throwable;


  /**
   * Delete all grid profiles identifiers for the current user.
   * This method is automatically invoked if "grid digest" comparison lead to discover a grid change.
   * @throws Throwable throwed if storing operation does not correctly accomplished
   */
  public abstract void deleteAllGridProfileIds(String functionId) throws Throwable;


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
