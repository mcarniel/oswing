package org.openswing.swing.customvo.client;

import java.math.*;
import java.sql.*;
import java.util.*;

import org.openswing.swing.customvo.java.*;
import org.openswing.swing.message.receive.java.*;
import org.openswing.swing.table.client.*;
import org.openswing.swing.table.java.*;


/**
 * <p>Title: OpenSwing Framework</p>
 * <p>Description: Grid controller used to manage custom grid.</p>
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
public abstract class CustomGridControlController extends GridController implements GridDataLocator {

  /** list of CustomFieldVO ojbjects, related to grid columns */
  protected ArrayList customFields = null;

  /** collection of pairs <tablename,HashMap of pairs [attribute name,db field] > */
  private HashMap attributesPerTable = new HashMap();

  /** collection of pairs <tablename,HashSet of attributes that compose the primary key for the table > */
  private HashMap pkPerTable = new HashMap();


  public CustomGridControlController(ArrayList customFields) {
    this.customFields = customFields;
    CustomFieldVO vo = null;
    HashMap map = null;
    HashSet pk = null;
    for(int i=0;i<customFields.size();i++) {
      vo = (CustomFieldVO)customFields.get(i);
      map = (HashMap)attributesPerTable.get(vo.getTableName());
      pk = (HashSet)pkPerTable.get(vo.getTableName());
      if (map==null) {
        map = new HashMap();
        attributesPerTable.put(vo.getTableName(),map);
      }
      if (pk==null) {
        pk = new HashSet();
        pkPerTable.put(vo.getTableName(),pk);
      }
      map.put(vo.getAttributeName(),vo.getFieldName());
      if (vo.isPk())
        pk.add(vo.getAttributeName());
    }
  }


  /**
   * @param tableName table name
   * @return collection of pairs <attribute name,db field>, related to the specified table name
   */
  public final HashMap getAttributesMappingPerTable(String tableName) {
    return (HashMap)attributesPerTable.get(tableName);
  }


  /**
   * @param tableName table name
   * @return collection of attributes that compose the primary key of the specified table
   */
  public final HashSet getPrimaryKeyPerTable(String tableName) {
    return (HashSet)pkPerTable.get(tableName);
  }


  /**
   * @return list of CustomFieldVO ojbjects, related to grid columns
   */
  public final ArrayList getCustomFields() {
    return customFields;
  }


  /**
   * Callback method invoked when the user has clicked on the insert button
   * @param valueObject empty value object just created: the user can manage it to fill some attribute values
   */
  public final void createValueObject(ValueObject valueObject) throws Exception {
    CustomFieldVO vo = null;
    for(int i=0;i<customFields.size();i++) {
      vo = (CustomFieldVO)customFields.get(i);
      if ((vo.getAttributeType()==vo.TYPE_DATE ||
           vo.getAttributeType()==vo.TYPE_DATE_TIME ||
           vo.getAttributeType()==vo.TYPE_TIME) &&
           vo.getDefaultValueDate()!=null)
        valueObject.getClass().getMethod("set"+vo.getAttributeName().substring(0,1).toUpperCase()+vo.getAttributeName().substring(1),new Class[]{Timestamp.class}).invoke(valueObject,new Object[]{vo.getDefaultValueDate()});
      else if (vo.getAttributeType()==vo.TYPE_TEXT &&
               vo.getDefaultValueText()!=null)
        valueObject.getClass().getMethod("set"+vo.getAttributeName().substring(0,1).toUpperCase()+vo.getAttributeName().substring(1),new Class[]{String.class}).invoke(valueObject,new Object[]{vo.getDefaultValueText()});
      else if (vo.getAttributeType()==vo.TYPE_NUM &&
               vo.getDefaultValueNum()!=null)
        valueObject.getClass().getMethod("set"+vo.getAttributeName().substring(0,1).toUpperCase()+vo.getAttributeName().substring(1),new Class[]{BigDecimal.class}).invoke(valueObject,new Object[]{vo.getDefaultValueNum()});
    }
  }


}
