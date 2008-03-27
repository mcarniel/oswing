package org.openswing.swing.table.profiles.database.server;

import java.util.*;

/**
 * <p>Title: OpenSwing Framework</p>
 * <p>Description: Descriptor of the table related to active profile storing, where the primary key is based on "functionId" and "username" attributes of the GridProfile.
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
public interface DbActiveProfileDescriptor {


  /**
   * @return name of the active profile table.
   */
  public String getActiveProfileTableName();


  /**
   * @return database field name related to the "functionId" attribute of GridProfile
   */
  public String getFunctionIdFieldName();


  /**
   * @return database field name related to the "username" attribute of GridProfile
   */
  public String getUsernameFieldName();


  /**
   * @return name of the database fields related to the "id" attribute of GridProfile
   */
  public String[] getIdFieldNames();


  /**
   * Callback method used to fill  in the insert SQL instruction with the specified collection of pairs <column name, column value>,
   * when executing the "storeGridProfileId" method. For instance a CREATE_USER field or a CREATE_DATE field.
   */
  public Hashtable storeGridProfileIdOnInsert();


  /**
   * Callback method used to fill  in the SET part of the update SQL instruction with the specified collection of pairs <column name, column value>,
   * when executing the "storeGridProfileId" method. For instance an UPDATE_USER field or an UPDATE_DATE field.
   */
  public Hashtable storeGridProfileIdOnSetUpdate();


  /**
   * Callback method used to fill  in the WHERE part of the update SQL instruction with the specified collection of pairs <column name, column value>,
   * when executing the "storeGridProfileId" method.
   */
  public Hashtable storeGridProfileIdOnWhereUpdate();


  /**
   * Callback method used to fill  in the WHERE part of the delete SQL instruction with the specified collection of pairs <column name, column value>,
   * when executing the "deleteAllGridProfileIds" method.
   */
  public Hashtable deleteAllGridProfileIds(String functionId);


}
