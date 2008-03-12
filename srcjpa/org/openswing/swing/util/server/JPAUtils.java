package org.openswing.swing.util.server;

import java.beans.BeanDescriptor;
import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import org.openswing.swing.server.QueryUtil;
import org.openswing.swing.server.UserSessionParameters;
import org.openswing.swing.message.send.java.GridParams;
import org.openswing.swing.util.client.ClientSettings;
import java.util.Map;
import java.util.ArrayList;
import org.openswing.swing.message.receive.java.*;
import java.util.HashMap;
import java.util.List;
import java.util.Iterator;
import java.util.Arrays;
import javax.persistence.EntityManager;
import javax.persistence.Query;


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
public class JPAUtils {


  /**
   * @param valueObjectType value object type
   * @param baseSQL base SQL
   * @return value object alias, defined within "baseSQL" argument
   */
  private static String getValueObjectAlias(
    Class valueObjectType,
    String baseSQL
  ) throws Exception {
    String valueObjectAlias = valueObjectType.getName();
    if (valueObjectAlias.indexOf(".")!=-1)
      valueObjectAlias = valueObjectAlias.substring(valueObjectAlias.lastIndexOf(".")+1);
    String valueObjectName = valueObjectAlias;
    
    baseSQL = baseSQL.replace('\t',' ').replace('\n',' ').replace('\r',' ')+" ";
    int index = baseSQL.indexOf(" "+valueObjectName+" ")+valueObjectName.length()+2;
    valueObjectAlias = baseSQL.substring(index).trim();
    String[] tokens = valueObjectAlias.split(" ");
    for(int i=0;i<tokens.length;i++)
        if (tokens[i].length()>0)
            return tokens[i];
    return valueObjectAlias;
  }    


  /**
   * Apply filtering and sorting conditions to the specified baseSQL and return
   * a new baseSQL that contains those conditions too.
   * @param filteredColumns filtering conditions
   * @param currentSortedColumns sorting conditions (attribute names)
   * @param currentSortedVersusColumns sorting conditions (order versus)
   * @param valueObjectType value object type
   * @param baseSQL base SQL
   * @param paramValues parameters values, related to "?" in "baseSQL"
   * @param valueObjectAlias alias used to identify the value object to retrieve
   */
  public static String applyFiltersAndSorter(
    Map filteredColumns,
    ArrayList currentSortedColumns,
    ArrayList currentSortedVersusColumns,
    Class valueObjectType,
    String baseSQL,
    ArrayList paramValues,
    String valueObjectAlias
  ) throws Exception {
    BeanInfo info = Introspector.getBeanInfo(valueObjectType);
    Map attributesMap = new HashMap();
    Iterator it = filteredColumns.keySet().iterator();
    Object attrName = null;
    while(it.hasNext()) {
      attrName = it.next();
      attributesMap.put(attrName,valueObjectAlias+"."+attrName);
    }
    for(int i=0;i<currentSortedColumns.size();i++)
      attributesMap.put(currentSortedColumns.get(i),valueObjectAlias+"."+currentSortedColumns.get(i));;
        
    // append filtering and sorting conditions to the base SQL...
    ArrayList filterAttrNames = new ArrayList();
    return QueryUtil.getSql(
            new UserSessionParameters(),
            baseSQL,
            filterAttrNames,
            paramValues,
            filteredColumns,
            currentSortedColumns,
            currentSortedVersusColumns,
            attributesMap,
            true
    );
  }

  
  /**
   * Read the whole result set, by applying filtering and sorting conditions + query parameters.
   * @param filteredColumns filtering conditions
   * @param currentSortedColumns sorting conditions (attribute names)
   * @param currentSortedVersusColumns sorting conditions (order versus)
   * @param valueObjectType value object type
   * @param baseSQL base SQL
   * @param paramValues parameters values, related to "?" in "baseSQL" (optional)
   * @param em EntityManager
   */
  public static Response getAllFromQuery(
    Map filteredColumns,
    ArrayList currentSortedColumns,
    ArrayList currentSortedVersusColumns,
    Class valueObjectType,
    String baseSQL,
    Object[] paramValues,
    EntityManager em
  ) throws Exception {
    return getAllFromQuery(
         filteredColumns,
         currentSortedColumns,
         currentSortedVersusColumns,
         valueObjectType,
         baseSQL,
         paramValues,
         getValueObjectAlias(valueObjectType,baseSQL),
         em 
    );
  }  
  

  /**
   * Read the whole result set, by applying filtering and sorting conditions + query parameters.
   * @param filteredColumns filtering conditions
   * @param currentSortedColumns sorting conditions (attribute names)
   * @param currentSortedVersusColumns sorting conditions (order versus)
   * @param valueObjectType value object type
   * @param baseSQL base SQL
   * @param paramValues parameters values, related to "?" in "baseSQL" (optional)
   * @param valueObjectAlias alias used to identify the value object to retrieve
   * @param em EntityManager
   */
  public static Response getAllFromQuery(
    Map filteredColumns,
    ArrayList currentSortedColumns,
    ArrayList currentSortedVersusColumns,
    Class valueObjectType,
    String baseSQL,
    Object[] paramValues,
    String valueObjectAlias,
    EntityManager em
  ) throws Exception {

    ArrayList values = new ArrayList();
    values.addAll(Arrays.asList(paramValues));
    baseSQL = applyFiltersAndSorter(
        filteredColumns,
        currentSortedColumns,
        currentSortedVersusColumns,
        valueObjectType,
        baseSQL,
        values,
        valueObjectAlias
    );

    ArrayList gridList = new ArrayList();
    boolean moreRows = false;
    int resultSetLength = -1;

    // read the whole result set...
    Query q = em.createQuery(baseSQL);
    for(int i=0;i<values.size();i++)
      q.setParameter(i+1,values.get(i));
    List list = q.getResultList();
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
   * @param valueObjectAlias alias used to identify the value object to retrieve
   * @param em EntityManager
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
    EntityManager em
  ) throws Exception {
    return getBlockFromQuery(
         action,
         startIndex,
         blockSize,
         filteredColumns,
         currentSortedColumns,
         currentSortedVersusColumns,
         valueObjectType,
         baseSQL,
         paramValues,
         getValueObjectAlias(valueObjectType,baseSQL),
         em 
    );
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
   * @param valueObjectAlias alias used to identify the value object to retrieve
   * @param em EntityManager
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
    String valueObjectAlias,
    EntityManager em
  ) throws Exception {

    ArrayList values = new ArrayList();
    values.addAll(Arrays.asList(paramValues));
    baseSQL = applyFiltersAndSorter(
        filteredColumns,
        currentSortedColumns,
        currentSortedVersusColumns,
        valueObjectType,
        baseSQL,
        values,
        valueObjectAlias
    );

    Query q = em.createQuery(baseSQL);
    for(int i=0;i<values.size();i++)
      q.setParameter(i+1,values.get(i));
    
    return getBlockFromQuery(
      action,
      startIndex,
      blockSize,
      q
    );
  }


  /**
   * Read a block of records from the result set, starting from a Query object.
   * @param action fetching versus: PREVIOUS_BLOCK_ACTION, NEXT_BLOCK_ACTION or LAST_BLOCK_ACTION
   * @param startPos start position of data fetching in result set
   * @param blockSize number of records to read
   * @param query Query object
   */
  public static Response getBlockFromQuery(
    int action,
    int startIndex,
    int blockSize,
    Query query
  ) throws Exception {

    // read a block of records...
    ArrayList gridList = new ArrayList();
    boolean moreRows = false;
    int resultSetLength = -1;
    int rowCount = 0;
    if (action==GridParams.LAST_BLOCK_ACTION) {
      // last block requested: the whole result set will be loaded, to determine the result set length
      List list = query.getResultList();
      rowCount = list.size();
      resultSetLength = rowCount;
      action = GridParams.NEXT_BLOCK_ACTION;
      startIndex = Math.max(rowCount-blockSize,0);
      for(int i=startIndex;i<list.size();i++)
        gridList.add(list.get(i));
      list.clear();
      return new VOListResponse(gridList,false,resultSetLength);    
    } else {
      if (action==GridParams.PREVIOUS_BLOCK_ACTION) {
        action = GridParams.NEXT_BLOCK_ACTION;
        startIndex = Math.max(startIndex-blockSize,0);
      }
      query.setFirstResult(startIndex);
      query.setMaxResults(blockSize+1);
    }

    List list = query.getResultList();
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
