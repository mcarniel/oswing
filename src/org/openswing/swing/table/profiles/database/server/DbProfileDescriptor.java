package org.openswing.swing.table.profiles.database.server;

import java.sql.*;
import java.util.*;

import org.openswing.swing.table.profiles.java.*;

/**
 * <p>Title: OpenSwing Framework</p>
 * <p>Description: Descriptor of the table related to grid profile storing, where the primary key is based on "functionId","username" and "id" attributes of the GridProfile.
 * The "id" can represent more than one database field.</p>
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
public interface DbProfileDescriptor {


  /**
   * @return name of the grid profile table.
   */
  public String getProfileTableName();

  /**
   * @return name of the database fields related to the "id" attribute of GridProfile
   */
  public String[] getIdFieldNames();

  /**
   * @return values related to a new "id" (attribute of GridProfile)
   */
  public Object[] getNewIdFieldValues(Connection conn,GridProfile profile) throws Throwable;


  /**
   * @return database field name related to the "description" attribute of GridProfile
   */
  public String getDescriptionFieldName();


  /**
   * @return database field name related to the "functionId" attribute of GridProfile
   */
  public String getFunctionIdFieldName();


  /**
   * @return database field name related to the "username" attribute of GridProfile
   */
  public String getUsernameFieldName();


  /**
   * @return database field name related to the "currentSortedColumns" attribute of GridProfile
   */
  public String getCurrentSortedColumnsFieldName();


  /**
   * @return database field name related to the "currentSortedVersusColumns" attribute of GridProfile
   */
  public String getCurrentSortedVersusColumnsFieldName();


  /**
   * @return database field name related to the "quickFilterValues" attribute of GridProfile
   */
  public String getQuickFilterValuesFieldName();


  /**
   * @return database field name related to the "columnsAttribute" attribute of GridProfile
   */
  public String getColumnsAttributeFieldName();


  /**
   * @return database field name related to the "columnsVisibility" attribute of GridProfile
   */
  public String getColumnsVisibilityFieldName();


  /**
   * @return database field name related to the "columnsWidth" attribute of GridProfile
   */
  public String getColumnsWidthFieldName();


  /**
   * @return database field name related to the "defaultProfile" attribute of GridProfile
   */
  public String getDefaultProfileFieldName();


  /**
   * Callback method used to fill in the insert SQL instruction with the specified collection of pairs <column name, column value>,
   * when executing the "storeUserProfile" method. For instance a CREATE_USER field or a CREATE_DATE field.
   */
  public Hashtable storeUserProfileOnInsert();


  /**
   * Callback method used to fill in the SET part of the update SQL instruction with the specified collection of pairs <column name, column value>,
   * when executing the "storeUserProfile" method. For instance an UPDATE_USER field or an UPDATE_DATE field.
   */
  public Hashtable storeUserProfileOnSetUpdate();


  /**
   * Callback method used to fill in the WHERE part of the update SQL instruction with the specified collection of pairs <column name, column value>,
   * when executing the "storeUserProfile" method.
   */
  public Hashtable storeUserProfileOnWhereUpdate();


  /**
   * Callback method used to fill in the WHERE part of the delete SQL instruction with the specified collection of pairs <column name, column value>,
   * when executing the "deleteUserProfile" method.
   */
  public Hashtable deleteUserProfile(String functionId,Object id);


  /**
   * Callback method used to fill in the WHERE part of the delete SQL instruction with the specified collection of pairs <column name, column value>,
   * when executing the "deleteAllGridProfiles" method.
   */
  public Hashtable deleteAllGridProfiles(String functionId);


}


