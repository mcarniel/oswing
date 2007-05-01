package org.openswing.swing.util.server;

import org.openswing.swing.server.QueryUtil;
import org.openswing.swing.server.UserSessionParameters;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.type.Type;
import org.hibernate.Hibernate;
import org.hibernate.Query;
import org.openswing.swing.message.send.java.GridParams;
import org.hibernate.ScrollableResults;
import org.openswing.swing.util.client.ClientSettings;
import org.hibernate.metadata.ClassMetadata;
import java.util.Map;
import java.util.ArrayList;
import org.openswing.swing.message.receive.java.*;
import java.util.HashMap;
import java.util.List;
import java.util.Iterator;
import java.util.Arrays;


/**
 * <p>Title: OpenSwing Framework</p>
 * <p>Description: Helper class containing some utility methods useful when retrieving data for a grid.</p>
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
public class HibernateUtils {


  /**
   * Apply filtering and sorting conditions to the specified baseSQL and return
   * a new baseSQL that contains those conditions too.
   * @param filteredColumns filtering conditions
   * @param currentSortedColumns sorting conditions (attribute names)
   * @param currentSortedVersusColumns sorting conditions (order versus)
   * @param valueObjectType value object type
   * @param baseSQL base SQL
   * @param paramValues parameters values, related to "?" in "baseSQL"
   * @param paramTypes parameters types, related to "?" in "baseSQL"
   * @param tableName table name related to baseSQL and v.o.
   * @param sessions SessionFactory
   */
  public static String applyFiltersAndSorter(
    Map filteredColumns,
    ArrayList currentSortedColumns,
    ArrayList currentSortedVersusColumns,
    Class valueObjectType,
    String baseSQL,
    ArrayList paramValues,
    ArrayList paramTypes,
    String tableName,
    SessionFactory sessions
  ) throws Exception {
    // fill in "attributesMap" according to attributes defined in xxx.hdm.xml...
    ClassMetadata meta = sessions.getClassMetadata(valueObjectType);
    String[] attrNames = meta.getPropertyNames();
    Map attributesMap = new HashMap();
    for(int i=0;i<attrNames.length;i++)
      attributesMap.put(attrNames[i],tableName+"."+attrNames[i]);

    // append filtering and sorting conditions to the base SQL...
    ArrayList filterAttrNames = new ArrayList();
    baseSQL = QueryUtil.getSql(new UserSessionParameters(),baseSQL,filterAttrNames,paramValues,filteredColumns,currentSortedColumns,currentSortedVersusColumns,attributesMap);

    for(int i=0;i<filterAttrNames.size();i++) {
      paramTypes.add(meta.getPropertyType(filterAttrNames.get(i).toString()));
    }

    return baseSQL;
  }


  /**
   * Read the whole result set, by applying filtering and sorting conditions + query parameters.
   * @param filteredColumns filtering conditions
   * @param currentSortedColumns sorting conditions (attribute names)
   * @param currentSortedVersusColumns sorting conditions (order versus)
   * @param valueObjectType value object type
   * @param baseSQL base SQL
   * @param paramValues parameters values, related to "?" in "baseSQL" (optional)
   * @param paramTypes parameters types, related to "?" in "baseSQL" (optional)
   * @param tableName table name related to baseSQL and v.o.
   * @param sessions SessionFactory
   * @param sess Session
   */
  public static Response getAllFromQuery(
    Map filteredColumns,
    ArrayList currentSortedColumns,
    ArrayList currentSortedVersusColumns,
    Class valueObjectType,
    String baseSQL,
    Object[] paramValues,
    Type[] paramTypes,
    String tableName,
    SessionFactory sessions,
    Session sess
  ) throws Exception {

    ArrayList values = new ArrayList();
    values.addAll(Arrays.asList(paramValues));
    ArrayList types = new ArrayList();
    types.addAll(Arrays.asList(paramTypes));
    baseSQL = applyFiltersAndSorter(
        filteredColumns,
        currentSortedColumns,
        currentSortedVersusColumns,
        valueObjectType,
        baseSQL,
        values,
        types,
        tableName,
        sessions
    );

    ArrayList gridList = new ArrayList();
    boolean moreRows = false;
    int resultSetLength = -1;

    // read the whole result set...
    List list = sess.createQuery(baseSQL).setParameters(values.toArray(),(Type[])types.toArray(new Type[types.size()])).list();
    gridList.addAll(list);
    resultSetLength = gridList.size();
    return new VOListResponse(gridList,moreRows,resultSetLength);
  }


  /**
   * Read a block of records from the result set, by applying filtering and sorting conditions + query parameters.
   * @param action fetching versus: PREVIOUS_BLOCK_ACTION, NEXT_BLOCK_ACTION or LAST_BLOCK_ACTION
   * @param startPos start position of data fetching in result set
   * @param blockSize number of records to read
   * @param filteredColumns filtering conditions
   * @param currentSortedColumns sorting conditions (attribute names)
   * @param currentSortedVersusColumns sorting conditions (order versus)
   * @param valueObjectType value object type
   * @param baseSQL base SQL
   * @param paramValues parameters values, related to "?" in "baseSQL" (optional)
   * @param paramTypes parameters types, related to "?" in "baseSQL" (optional)
   * @param tableName table name related to baseSQL and v.o.
   * @param sessions SessionFactory
   * @param sess Session
   */
  public static Response getBlockFromQuery(
    int action,
    int startIndex,
    int blockSize,
    Map filteredColumns,
    ArrayList currentSortedColumns,
    ArrayList currentSortedVersusColumns,
    Class valueObjectType,
    String baseSQL,
    Object[] paramValues,
    Type[] paramTypes,
    String tableName,
    SessionFactory sessions,
    Session sess
  ) throws Exception {

    ArrayList values = new ArrayList();
    values.addAll(Arrays.asList(paramValues));
    ArrayList types = new ArrayList();
    types.addAll(Arrays.asList(paramTypes));
    baseSQL = applyFiltersAndSorter(
        filteredColumns,
        currentSortedColumns,
        currentSortedVersusColumns,
        valueObjectType,
        baseSQL,
        values,
        types,
        tableName,
        sessions
    );

    return getBlockFromQuery(
      action,
      startIndex,
      blockSize,
      sess.createQuery(baseSQL).setParameters(values.toArray(),(Type[])types.toArray(new Type[types.size()])),
      sess
    );


//    // read a block of records...
//    ArrayList gridList = new ArrayList();
//    boolean moreRows = false;
//    int resultSetLength = -1;
//    int rowCount = 0;
//    Query q = null;
//    if (action==GridParams.LAST_BLOCK_ACTION) {
//      // last block requested: the whole result set will be loaded, to determine the result set length
//      q = sess.createQuery(baseSQL).setParameters(values.toArray(),(Type[])types.toArray(new Type[types.size()]));
//      Iterator it = q.iterate();
//      while(it.hasNext()) {
//        rowCount++;
//        it.next();
//      }
//      resultSetLength = rowCount;
//      action = GridParams.NEXT_BLOCK_ACTION;
//      startIndex = Math.max(rowCount-blockSize,0);
//      rowCount = 0;
//      q.setFirstResult(startIndex);
//      q.setMaxResults(blockSize);
//    } else {
//      if (action==GridParams.PREVIOUS_BLOCK_ACTION) {
//        action = GridParams.NEXT_BLOCK_ACTION;
//        startIndex = Math.max(startIndex-blockSize,0);
//      }
//      q = sess.createQuery(baseSQL).setParameters(values.toArray(),(Type[])types.toArray(new Type[types.size()]));
//      q.setFirstResult(startIndex);
//      q.setMaxResults(blockSize+1);
//    }
//
//    List list = q.list();
//    gridList.addAll(list);
//    if (gridList.size()>blockSize) {
//      gridList.remove(gridList.size() - 1);
//      moreRows = true;
//    }
//    if (resultSetLength==-1)
//      resultSetLength = gridList.size();
//
//    return new VOListResponse(gridList,moreRows,resultSetLength);
  }


  /**
   * Read a block of records from the result set, starting from a Query object.
   * @param action fetching versus: PREVIOUS_BLOCK_ACTION, NEXT_BLOCK_ACTION or LAST_BLOCK_ACTION
   * @param startPos start position of data fetching in result set
   * @param blockSize number of records to read
   * @param query Query object
   * @param sess Session
   */
  public static Response getBlockFromQuery(
    int action,
    int startIndex,
    int blockSize,
    Query query,
    Session sess
  ) throws Exception {

    // read a block of records...
    ArrayList gridList = new ArrayList();
    boolean moreRows = false;
    int resultSetLength = -1;
    int rowCount = 0;
    if (action==GridParams.LAST_BLOCK_ACTION) {
      // last block requested: the whole result set will be loaded, to determine the result set length
      Iterator it = query.iterate();
      while(it.hasNext()) {
        rowCount++;
        it.next();
      }
      resultSetLength = rowCount;
      action = GridParams.NEXT_BLOCK_ACTION;
      startIndex = Math.max(rowCount-blockSize,0);
      rowCount = 0;
      query.setFirstResult(startIndex);
      query.setMaxResults(blockSize);
    } else {
      if (action==GridParams.PREVIOUS_BLOCK_ACTION) {
        action = GridParams.NEXT_BLOCK_ACTION;
        startIndex = Math.max(startIndex-blockSize,0);
      }
      query.setFirstResult(startIndex);
      query.setMaxResults(blockSize+1);
    }

    List list = query.list();
    gridList.addAll(list);
    if (gridList.size()>blockSize) {
      gridList.remove(gridList.size() - 1);
      moreRows = true;
    }
    if (resultSetLength==-1)
      resultSetLength = gridList.size();

    return new VOListResponse(gridList,moreRows,resultSetLength);
  }


}
