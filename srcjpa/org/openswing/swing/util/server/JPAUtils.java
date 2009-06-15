package org.openswing.swing.util.server;

import java.beans.*;
import java.util.*;
import javax.persistence.*;

import org.openswing.swing.message.receive.java.*;
import org.openswing.swing.message.send.java.*;
import org.openswing.swing.server.*;


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
    String valueObjectAlias = valueObjectType.getName(); // e.g. "xxx.yyy.ZZZ"
    if (valueObjectAlias.indexOf(".")!=-1)
      valueObjectAlias = valueObjectAlias.substring(valueObjectAlias.lastIndexOf(".")+1);
    String valueObjectName = valueObjectAlias; // e.g. "ZZZ"

    baseSQL = baseSQL.replace('\t',' ').replace('\n',' ').replace('\r',' ')+" "; // e.g. " xxx.yyy.ZZZ " or " xxx.yyy.ZZZ alias " or " ZZZ " or " ZZZ alias " or " select ... from xxx.yyy.ZZZ alias where ..." or " select ... from xxx.yyy.ZZZ where ..." 
    int index = baseSQL.indexOf(" "+valueObjectName+" ");
    if (index==-1)
      index = baseSQL.indexOf(valueObjectName+" ");
    else
      index++;
    index += valueObjectName.length()+1;
    valueObjectAlias = baseSQL.substring(index).trim(); // e.g. "" or "alias" or "" or "alias" or "alias where ..." or "where ..."
    String[] tokens = valueObjectAlias.split(" "); // e.g. [""] or ["alias"] or [""] or ["alias"] or ["alias,"where","..."] or ["where","..."]
    for (int i = 0; i < tokens.length; i++)
      if (tokens[i].length() > 0) {
        return tokens[i];
    }
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
      attributesMap.put(currentSortedColumns.get(i),valueObjectAlias+"."+currentSortedColumns.get(i));

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
   * Apply filtering and sorting conditions to the specified baseSQL and return
   * a new baseSQL that contains those conditions too.
   * SQL is expressed using more argument, each one without the related keyword (select, from, ...).
   *
   * Example: following query
   *
   * select customer_code,corporate_name from companies order by customer_code asc
   *
   * become an invokation of getSql:
   *
   * getSql(userSessionPars,"customer_code,corporate_name","companies","","customer_code asc","","",...);
   *
   * @param filteredColumns filtering conditions
   * @param currentSortedColumns sorting conditions (attribute names)
   * @param currentSortedVersusColumns sorting conditions (order versus)
   * @param valueObjectType value object type
   * @param select list of fields for select statement
   * @param from list of tables for from statement
   * @param where where statement; may be null
   * @param group group by statement; may be null
   * @param having having statement; may be null
   * @param order list of fields for order by statement; may be null
   * @param paramValues parameters values, related to "?" in "baseSQL"
   * @param valueObjectAlias alias used to identify the value object to retrieve
   */
  public static String applyFiltersAndSorter(
    Map filteredColumns,
    ArrayList currentSortedColumns,
    ArrayList currentSortedVersusColumns,
    Class valueObjectType,
    String select,
    String from,
    String where,
    String group,
    String having,
    String order,
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
      attributesMap.put(currentSortedColumns.get(i),valueObjectAlias+"."+currentSortedColumns.get(i));

    // append filtering and sorting conditions to the base SQL...
    ArrayList filterAttrNames = new ArrayList();
    return QueryUtil.getSql(
            new UserSessionParameters(),
            select,
            from,
            where,
            group,
            having,
            order,
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
   * @param em EntityManager
   * @param logger Log4J logger to use to log query parameters
   */
  public static Response getAllFromQuery(
    Map filteredColumns,
    ArrayList currentSortedColumns,
    ArrayList currentSortedVersusColumns,
    Class valueObjectType,
    String baseSQL,
    Object[] paramValues,
    EntityManager em,
    org.apache.log4j.Logger logger
  ) throws Exception {
    return getAllFromQuery(
         filteredColumns,
         currentSortedColumns,
         currentSortedVersusColumns,
         valueObjectType,
         baseSQL,
         paramValues,
         getValueObjectAlias(valueObjectType,baseSQL),
         em,
         logger
    );
  }


  /**
   * Read the whole result set, by applying filtering and sorting conditions + query parameters.
   * SQL is expressed using more argument, each one without the related keyword (select, from, ...).
   *
   * Example: following query
   *
   * select customer_code,corporate_name from companies order by customer_code asc
   *
   * become an invokation of getSql:
   *
   * getSql(userSessionPars,"customer_code,corporate_name","companies","","customer_code asc","","",...);
   *
   * @param filteredColumns filtering conditions
   * @param currentSortedColumns sorting conditions (attribute names)
   * @param currentSortedVersusColumns sorting conditions (order versus)
   * @param valueObjectType value object type
   * @param select list of fields for select statement
   * @param from list of tables for from statement
   * @param where where statement; may be null
   * @param group group by statement; may be null
   * @param having having statement; may be null
   * @param order list of fields for order by statement; may be null
   * @param paramValues parameters values, related to "?" in "baseSQL" (optional)
   * @param em EntityManager
   */
  public static Response getAllFromQuery(
    Map filteredColumns,
    ArrayList currentSortedColumns,
    ArrayList currentSortedVersusColumns,
    Class valueObjectType,
    String select,
    String from,
    String where,
    String group,
    String having,
    String order,
    Object[] paramValues,
    EntityManager em
  ) throws Exception {
    return getAllFromQuery(
         filteredColumns,
         currentSortedColumns,
         currentSortedVersusColumns,
         valueObjectType,
         select,
         from,
         where,
         group,
         having,
         order,
         paramValues,
         getValueObjectAlias(valueObjectType,from),
         em
    );
  }


  /**
   * Read the whole result set, by applying filtering and sorting conditions + query parameters.
   * SQL is expressed using more argument, each one without the related keyword (select, from, ...).
   *
   * Example: following query
   *
   * select customer_code,corporate_name from companies order by customer_code asc
   *
   * become an invokation of getSql:
   *
   * getSql(userSessionPars,"customer_code,corporate_name","companies","","customer_code asc","","",...);
   *
   * @param filteredColumns filtering conditions
   * @param currentSortedColumns sorting conditions (attribute names)
   * @param currentSortedVersusColumns sorting conditions (order versus)
   * @param valueObjectType value object type
   * @param select list of fields for select statement
   * @param from list of tables for from statement
   * @param where where statement; may be null
   * @param group group by statement; may be null
   * @param having having statement; may be null
   * @param order list of fields for order by statement; may be null
   * @param paramValues parameters values, related to "?" in "baseSQL" (optional)
   * @param em EntityManager
   * @param logger Log4J logger to use to log query parameters
   */
  public static Response getAllFromQuery(
    Map filteredColumns,
    ArrayList currentSortedColumns,
    ArrayList currentSortedVersusColumns,
    Class valueObjectType,
    String select,
    String from,
    String where,
    String group,
    String having,
    String order,
    Object[] paramValues,
    EntityManager em,
    org.apache.log4j.Logger logger
  ) throws Exception {
    return getAllFromQuery(
         filteredColumns,
         currentSortedColumns,
         currentSortedVersusColumns,
         valueObjectType,
         select,
         from,
         where,
         group,
         having,
         order,
         paramValues,
         getValueObjectAlias(valueObjectType,from),
         em,
         logger
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
    return getAllFromQuery(
      filteredColumns,
      currentSortedColumns,
      currentSortedVersusColumns,
      valueObjectType,
      baseSQL,
      paramValues,
      valueObjectAlias,
      em,
      null
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
   * @param logger Log4J logger to use to log query parameters
   */
  public static Response getAllFromQuery(
    Map filteredColumns,
    ArrayList currentSortedColumns,
    ArrayList currentSortedVersusColumns,
    Class valueObjectType,
    String baseSQL,
    Object[] paramValues,
    String valueObjectAlias,
    EntityManager em,
    org.apache.log4j.Logger logger
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
    if (logger!=null)
      logParams(logger,values);
    return new VOListResponse(gridList,moreRows,resultSetLength);
  }


  /**
   * Read the whole result set, by applying filtering and sorting conditions + query parameters.
   * SQL is expressed using more argument, each one without the related keyword (select, from, ...).
   *
   * Example: following query
   *
   * select customer_code,corporate_name from companies order by customer_code asc
   *
   * become an invokation of getSql:
   *
   * getSql(userSessionPars,"customer_code,corporate_name","companies","","customer_code asc","","",...);
   *
   * @param filteredColumns filtering conditions
   * @param currentSortedColumns sorting conditions (attribute names)
   * @param currentSortedVersusColumns sorting conditions (order versus)
   * @param valueObjectType value object type
   * @param select list of fields for select statement
   * @param from list of tables for from statement
   * @param where where statement; may be null
   * @param group group by statement; may be null
   * @param having having statement; may be null
   * @param order list of fields for order by statement; may be null
   * @param paramValues parameters values, related to "?" in "baseSQL" (optional)
   * @param valueObjectAlias alias used to identify the value object to retrieve
   * @param em EntityManager
   */
  public static Response getAllFromQuery(
    Map filteredColumns,
    ArrayList currentSortedColumns,
    ArrayList currentSortedVersusColumns,
    Class valueObjectType,
    String select,
    String from,
    String where,
    String group,
    String having,
    String order,
    Object[] paramValues,
    String valueObjectAlias,
    EntityManager em
  ) throws Exception {

    return getAllFromQuery(
      filteredColumns,
      currentSortedColumns,
      currentSortedVersusColumns,
      valueObjectType,
      select,
      from,
      where,
      group,
      having,
      order,
      paramValues,
      valueObjectAlias,
      em,
      null
    );
  }


  /**
   * Read the whole result set, by applying filtering and sorting conditions + query parameters.
   * SQL is expressed using more argument, each one without the related keyword (select, from, ...).
   *
   * Example: following query
   *
   * select customer_code,corporate_name from companies order by customer_code asc
   *
   * become an invokation of getSql:
   *
   * getSql(userSessionPars,"customer_code,corporate_name","companies","","customer_code asc","","",...);
   *
   * @param filteredColumns filtering conditions
   * @param currentSortedColumns sorting conditions (attribute names)
   * @param currentSortedVersusColumns sorting conditions (order versus)
   * @param valueObjectType value object type
   * @param select list of fields for select statement
   * @param from list of tables for from statement
   * @param where where statement; may be null
   * @param group group by statement; may be null
   * @param having having statement; may be null
   * @param order list of fields for order by statement; may be null
   * @param paramValues parameters values, related to "?" in "baseSQL" (optional)
   * @param valueObjectAlias alias used to identify the value object to retrieve
   * @param em EntityManager
   * @param logger Log4J logger to use to log query parameters
   */
  public static Response getAllFromQuery(
    Map filteredColumns,
    ArrayList currentSortedColumns,
    ArrayList currentSortedVersusColumns,
    Class valueObjectType,
    String select,
    String from,
    String where,
    String group,
    String having,
    String order,
    Object[] paramValues,
    String valueObjectAlias,
    EntityManager em,
    org.apache.log4j.Logger logger
  ) throws Exception {

    ArrayList values = new ArrayList();
    values.addAll(Arrays.asList(paramValues));
    String baseSQL = applyFiltersAndSorter(
        filteredColumns,
        currentSortedColumns,
        currentSortedVersusColumns,
        valueObjectType,
        select,
        from,
        where,
        group,
        having,
        order,
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
    if (logger!=null)
      logParams(logger,values);
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
   * @param logger Log4J logger to use to log query parameters
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
    EntityManager em,
    org.apache.log4j.Logger logger
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
         em,
         logger
    );
  }
  

  /**
   * Read a block of records from the result set, by applying filtering and sorting conditions + query parameters.
   * SQL is expressed using more argument, each one without the related keyword (select, from, ...).
   *
   * Example: following query
   *
   * select customer_code,corporate_name from companies order by customer_code asc
   *
   * become an invokation of getSql:
   *
   * getSql(userSessionPars,"customer_code,corporate_name","companies","","customer_code asc","","",...);
   *
   * @param action fetching versus: PREVIOUS_BLOCK_ACTION, NEXT_BLOCK_ACTION or LAST_BLOCK_ACTION
   * @param startPos start position of data fetching in result set
   * @param blockSize number of records to read
   * @param filteredColumns filtering conditions
   * @param currentSortedColumns sorting conditions (attribute names)
   * @param currentSortedVersusColumns sorting conditions (order versus)
   * @param valueObjectType value object type
   * @param select list of fields for select statement
   * @param from list of tables for from statement
   * @param where where statement; may be null
   * @param group group by statement; may be null
   * @param having having statement; may be null
   * @param order list of fields for order by statement; may be null
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
    String select,
    String from,
    String where,
    String group,
    String having,
    String order,
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
         select,
         from,
         where,
         group,
         having,
         order,
         paramValues,
         getValueObjectAlias(valueObjectType,from),
         em
    );
  }


  /**
   * Read a block of records from the result set, by applying filtering and sorting conditions + query parameters.
   * SQL is expressed using more argument, each one without the related keyword (select, from, ...).
   *
   * Example: following query
   *
   * select customer_code,corporate_name from companies order by customer_code asc
   *
   * become an invokation of getSql:
   *
   * getSql(userSessionPars,"customer_code,corporate_name","companies","","customer_code asc","","",...);
   *
   * @param action fetching versus: PREVIOUS_BLOCK_ACTION, NEXT_BLOCK_ACTION or LAST_BLOCK_ACTION
   * @param startPos start position of data fetching in result set
   * @param blockSize number of records to read
   * @param filteredColumns filtering conditions
   * @param currentSortedColumns sorting conditions (attribute names)
   * @param currentSortedVersusColumns sorting conditions (order versus)
   * @param valueObjectType value object type
   * @param select list of fields for select statement
   * @param from list of tables for from statement
   * @param where where statement; may be null
   * @param group group by statement; may be null
   * @param having having statement; may be null
   * @param order list of fields for order by statement; may be null
   * @param paramValues parameters values, related to "?" in "baseSQL" (optional)
   * @param valueObjectAlias alias used to identify the value object to retrieve
   * @param em EntityManager
   * @param logger Log4J logger to use to log query parameters
   */
  public static Response getBlockFromQuery(
    int action,
    int startIndex,
    int blockSize,
    Map filteredColumns,
    ArrayList currentSortedColumns,
    ArrayList currentSortedVersusColumns,
    Class valueObjectType,
    String select,
    String from,
    String where,
    String group,
    String having,
    String order,
    Object[] paramValues,
    EntityManager em,
    org.apache.log4j.Logger logger
  ) throws Exception {
    return getBlockFromQuery(
         action,
         startIndex,
         blockSize,
         filteredColumns,
         currentSortedColumns,
         currentSortedVersusColumns,
         valueObjectType,
         select,
         from,
         where,
         group,
         having,
         order,
         paramValues,
         getValueObjectAlias(valueObjectType,from),
         em,
         logger
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
      valueObjectAlias,
      em,
      null
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
   * @param logger Log4J logger to use to log query parameters
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
    EntityManager em,
    org.apache.log4j.Logger logger
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

    Response res = getBlockFromQuery(
      action,
      startIndex,
      blockSize,
      q
    );
    if (logger!=null)
      logParams(logger,values);
    return res;
  }


  /**
   * Read a block of records from the result set, by applying filtering and sorting conditions + query parameters.
   * SQL is expressed using more argument, each one without the related keyword (select, from, ...).
   *
   * Example: following query
   *
   * select customer_code,corporate_name from companies order by customer_code asc
   *
   * become an invokation of getSql:
   *
   * getSql(userSessionPars,"customer_code,corporate_name","companies","","customer_code asc","","",...);
   *
   * @param action fetching versus: PREVIOUS_BLOCK_ACTION, NEXT_BLOCK_ACTION or LAST_BLOCK_ACTION
   * @param startPos start position of data fetching in result set
   * @param blockSize number of records to read
   * @param filteredColumns filtering conditions
   * @param currentSortedColumns sorting conditions (attribute names)
   * @param currentSortedVersusColumns sorting conditions (order versus)
   * @param valueObjectType value object type
   * @param select list of fields for select statement
   * @param from list of tables for from statement
   * @param where where statement; may be null
   * @param group group by statement; may be null
   * @param having having statement; may be null
   * @param order list of fields for order by statement; may be null
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
    String select,
    String from,
    String where,
    String group,
    String having,
    String order,
    Object[] paramValues,
    String valueObjectAlias,
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
      select,
      from,
      where,
      group,
      having,
      order,
      paramValues,
      valueObjectAlias,
      em,
      null
    );
  }


  /**
   * Read a block of records from the result set, by applying filtering and sorting conditions + query parameters.
   * SQL is expressed using more argument, each one without the related keyword (select, from, ...).
   *
   * Example: following query
   *
   * select customer_code,corporate_name from companies order by customer_code asc
   *
   * become an invokation of getSql:
   *
   * getSql(userSessionPars,"customer_code,corporate_name","companies","","customer_code asc","","",...);
   *
   * @param action fetching versus: PREVIOUS_BLOCK_ACTION, NEXT_BLOCK_ACTION or LAST_BLOCK_ACTION
   * @param startPos start position of data fetching in result set
   * @param blockSize number of records to read
   * @param filteredColumns filtering conditions
   * @param currentSortedColumns sorting conditions (attribute names)
   * @param currentSortedVersusColumns sorting conditions (order versus)
   * @param valueObjectType value object type
   * @param select list of fields for select statement
   * @param from list of tables for from statement
   * @param where where statement; may be null
   * @param group group by statement; may be null
   * @param having having statement; may be null
   * @param order list of fields for order by statement; may be null
   * @param paramValues parameters values, related to "?" in "baseSQL" (optional)
   * @param valueObjectAlias alias used to identify the value object to retrieve
   * @param em EntityManager
   * @param logger Log4J logger to use to log query parameters
   */
  public static Response getBlockFromQuery(
    int action,
    int startIndex,
    int blockSize,
    Map filteredColumns,
    ArrayList currentSortedColumns,
    ArrayList currentSortedVersusColumns,
    Class valueObjectType,
    String select,
    String from,
    String where,
    String group,
    String having,
    String order,
    Object[] paramValues,
    String valueObjectAlias,
    EntityManager em,
    org.apache.log4j.Logger logger
  ) throws Exception {

    ArrayList values = new ArrayList();
    values.addAll(Arrays.asList(paramValues));
    String baseSQL = applyFiltersAndSorter(
        filteredColumns,
        currentSortedColumns,
        currentSortedVersusColumns,
        valueObjectType,
        select,
        from,
        where,
        group,
        having,
        order,
        values,
        valueObjectAlias
    );

    Query q = em.createQuery(baseSQL);
    for(int i=0;i<values.size();i++)
      q.setParameter(i+1,values.get(i));

    Response res = getBlockFromQuery(
      action,
      startIndex,
      blockSize,
      q
    );
    if (logger!=null)
      logParams(logger,values);
    return res;
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
      List list = null;
      if (query.getClass().getName().equals("org.hibernate.ejb.QueryImpl")) {
	// Query is implemented by Hibernate: 
	// use Hibernate API to move cursor at the end of the result set...
	try {
    		Object hibernateQuery = query.getClass().getMethod("getHibernateQuery", new Class[0]).invoke(query, new Object[0]);
    		Object scrollableResults = hibernateQuery.getClass().getMethod("scroll", new Class[0]).invoke(hibernateQuery, new Object[0]);
    		scrollableResults.getClass().getMethod("last", new Class[0]).invoke(scrollableResults, new Object[0]);
    		Integer num = (Integer)scrollableResults.getClass().getMethod("getRowNumber", new Class[0]).invoke(scrollableResults, new Object[0]);
    		rowCount = num.intValue();
    		scrollableResults.getClass().getMethod("close", new Class[0]).invoke(scrollableResults, new Object[0]);
	} catch(Throwable t) {
	        list = query.getResultList();
	        resultSetLength = list.size();
	}            
      }
      else {
	      list = query.getResultList();
      	      rowCount = list.size();
      }
      
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
      if(startIndex<0)
        startIndex = 0;
        
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


  private static void logParams(org.apache.log4j.Logger logger,ArrayList paramValues) {
    if (paramValues.size()>0) {
      StringBuffer sb = new StringBuffer();
      for(int i=0;i<paramValues.size();i++)
        if (paramValues.get(i)!=null)
          sb.append("PARAM ").append(i+1).append(": '").append(paramValues.get(i)).append("' ").append(paramValues.get(i).getClass().getName()).append("\n");
        else
          sb.append("PARAM ").append(i+1).append(": null\n");
      logger.info(sb.toString());
    }
  }

}
