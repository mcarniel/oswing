package org.openswing.swing.util.server;

import java.util.*;

import org.hibernate.*;
import org.hibernate.metadata.*;
import org.hibernate.type.*;
import org.openswing.swing.message.receive.java.*;
import org.openswing.swing.message.send.java.*;
import org.openswing.swing.server.*;
import java.math.BigDecimal;
import java.beans.*;
import java.math.BigInteger;
import org.hibernate.FetchMode;
import org.hibernate.criterion.Restrictions;
import org.openswing.swing.util.java.Consts;
import org.hibernate.criterion.Order;


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
    return applyFiltersAndSorter(
      new HashMap(),
      filteredColumns,
      currentSortedColumns,
      currentSortedVersusColumns,
      valueObjectType,
      baseSQL,
      paramValues,
      paramTypes,
      tableName,
      sessions
    );
  }


  /**
   * Fill in "attributesMap" according to attributes defined in xxx.hdm.xml.
   */
  private static void fillInMetaData(
      SessionFactory sessions,
      String tableName,
      String prefix,
      ClassMetadata meta,
      Map attributesMap,
      Map attributesTypeMap,
      String lastAttrName
  ) {
    if (meta!=null) {
      String[] attrNames = meta.getPropertyNames();
      for(int i=0;i<attrNames.length;i++) {
        attributesMap.put(prefix+attrNames[i],tableName+"."+attrNames[i]);
        attributesTypeMap.put(prefix+attrNames[i],meta.getPropertyType(attrNames[i]));
/*
        if (meta.getPropertyType(attrNames[i]) instanceof org.hibernate.type.EntityType &&
            !prefix.equals(attrNames[i]+".")) {
          ClassMetadata submeta = sessions.getClassMetadata(((org.hibernate.type.EntityType)meta.getPropertyType(attrNames[i])).getReturnedClass());
          fillInMetaData(
            sessions,
            tableName+"."+attrNames[i],
            prefix+prefix+attrNames[i]+".",
            submeta,
            attributesMap,
            attributesTypeMap
          );
        }
*/
      if (meta.getPropertyType(attrNames[i]) instanceof org.hibernate.type.EntityType &&
          !lastAttrName.equals(meta.getEntityName()+"."+attrNames[i])) {
        ClassMetadata submeta =
        sessions.getClassMetadata(((org.hibernate.type.EntityType)meta.getPropertyType(attrNames[i])).getReturnedClass());
        fillInMetaData(
          sessions,
          tableName+"."+attrNames[i],
//          prefix+prefix+attrNames[i]+".",
// -MC 29/11/2008:
          prefix+attrNames[i]+".",
          submeta,
          attributesMap,
          attributesTypeMap,
          meta.getEntityName()+"."+attrNames[i]
        );
      }

/*
 if (meta.getPropertyType(attrNames[i]) instanceof org.hibernate.type.EntityType) {
   if (!prefix.equals(attrNames[i]+".")) {
     ClassMetadata submeta =
       sessions.getClassMetadata(((org.hibernate.type.EntityType)meta.getPropertyType(attrNames[i])).getReturnedClass());
     fillInMetaData(
       sessions,
       tableName+"."+attrNames[i],
       prefix+prefix+attrNames[i]+".",
       submeta,
       attributesMap
     );
   }
 }
*/

      }

      attributesMap.put(prefix+meta.getIdentifierPropertyName(),tableName+"."+meta.getIdentifierPropertyName());
      attributesTypeMap.put(prefix+meta.getIdentifierPropertyName(),meta.getPropertyType(meta.getIdentifierPropertyName()));
/*
      if (meta.getPropertyType(meta.getIdentifierPropertyName()) instanceof org.hibernate.type.EntityType &&
          !prefix.equals(meta.getIdentifierPropertyName()+".")) {
        ClassMetadata submeta = sessions.getClassMetadata(((org.hibernate.type.EntityType)meta.getPropertyType(meta.getIdentifierPropertyName())).getReturnedClass());
        fillInMetaData(
          sessions,
          ((org.hibernate.type.EntityType)meta.getPropertyType(meta.getIdentifierPropertyName())).getReturnedClass().getName(),
          prefix+prefix+meta.getIdentifierPropertyName()+".",
          submeta,
          attributesMap,
          attributesTypeMap
        );
      }
*/
      if (meta.getPropertyType(meta.getIdentifierPropertyName()) instanceof org.hibernate.type.EntityType &&
          !lastAttrName.equals(meta.getEntityName()+"."+meta.getIdentifierPropertyName())) {
        ClassMetadata submeta =
        sessions.getClassMetadata(((org.hibernate.type.EntityType)meta.getPropertyType(meta.getIdentifierPropertyName())).getReturnedClass());
        fillInMetaData(
          sessions,
          ((org.hibernate.type.EntityType)
        meta.getPropertyType(meta.getIdentifierPropertyName())).getReturnedClass().getName(),
          prefix+prefix+meta.getIdentifierPropertyName()+".",
          submeta,
          attributesMap,
          attributesTypeMap,
          meta.getEntityName()+"."+meta.getIdentifierPropertyName()
        );
      }


    }
  }


  /**
   * Apply filtering and sorting conditions to the specified baseSQL and return
   * a new baseSQL that contains those conditions too.
   * @param decodedAttributes collection of pairs <value object attribute name,attribute defined in HSQL query>; if not specified, this method tries to automatically fetch mappings
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
    Map decodedAttributes,
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

//    Iterator it = filteredColumns.keySet().iterator();
//    String gridAttr,hsqlAttr;
//    HashSet toRemove = new HashSet();
//    HashMap toAdd = new HashMap();
//    while(it.hasNext()) {
//      gridAttr = it.next().toString();
//      hsqlAttr = (String)decodedAttributes.get(gridAttr);
//      if (hsqlAttr!=null) {
//        toAdd.put(hsqlAttr,filteredColumns.get(gridAttr));
//        toRemove.add(gridAttr);
//      }
//    }
//    filteredColumns.putAll(toAdd);
//    it = toRemove.iterator();
//    while(it.hasNext())
//      filteredColumns.remove(it.next());
//
//    for(int i=0;i<currentSortedColumns.size();i++) {
//      gridAttr = currentSortedColumns.get(i).toString();
//      hsqlAttr = (String)decodedAttributes.get(gridAttr);
//      if (hsqlAttr!=null) {
//        currentSortedColumns.set(i,hsqlAttr);
//      }
//    }


    // fill in "attributesMap" according to attributes defined in xxx.hdm.xml...
    ClassMetadata meta = sessions.getClassMetadata(valueObjectType);
    Map propDescriptors = new HashMap();
    Map attributesMap = new HashMap();
    Map attributesTypeMap = new HashMap();
    if (meta!=null) {
      fillInMetaData(sessions,tableName,"",meta,attributesMap,attributesTypeMap,"");
      attributesMap.put(meta.getIdentifierPropertyName(),tableName+"."+meta.getIdentifierPropertyName());
    }
    else {
      PropertyDescriptor[] p = Introspector.getBeanInfo(valueObjectType).getPropertyDescriptors();
      for(int i=0;i<p.length;i++)
        propDescriptors.put(p[i].getName(),p[i].getPropertyType());
    }
    attributesMap.putAll(decodedAttributes);

    // append filtering and sorting conditions to the base SQL...
    ArrayList filterAttrNames = new ArrayList();
    baseSQL = QueryUtil.getSql(new UserSessionParameters(),baseSQL,filterAttrNames,paramValues,filteredColumns,currentSortedColumns,currentSortedVersusColumns,attributesMap);

    FilterWhereClause[] where = null;
    for(int i=0;i<filterAttrNames.size();i++) {
      where = (FilterWhereClause[])filteredColumns.get(filterAttrNames.get(i));

      if (where[0].getValue()!=null && where[0].getValue() instanceof List) {
        for(int j=0;j<((List)where[0].getValue()).size();j++)
          paramTypes.add(getPropertyType(attributesTypeMap,meta,filterAttrNames.get(i).toString(),propDescriptors));
      }
      else
        paramTypes.add(getPropertyType(attributesTypeMap,meta,filterAttrNames.get(i).toString(),propDescriptors));

//      if (where[1]!=null) {
//        if (where[1].getValue()!=null && where[1].getValue() instanceof List) {
//          for(int j=0;j<((List)where[1].getValue()).size();j++)
//            paramTypes.add(getPropertyType(attributesTypeMap,meta,filterAttrNames.get(i).toString(),propDescriptors));
//        }
//        else
//          paramTypes.add(getPropertyType(attributesTypeMap,meta,filterAttrNames.get(i).toString(),propDescriptors));
//      }
    }

    return baseSQL;
  }


  /**
   * Apply filtering and sorting conditions to the specified baseSQL and return
   * a new baseSQL that contains those conditions too.
   * SQL is expressed using more argument, each one without the related keyword (select, from, ...).
   *
   * Example: following query
   *
   * select customer_code,corporate_name from companiesVO order by customer_code asc
   *
   * become an invokation of getSql:
   *
   * getSql(userSessionPars,"customer_code,corporate_name","companiesVO","","customer_code asc","","",...);
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
   * @param paramTypes parameters types, related to "?" in "baseSQL"
   * @param tableName table name related to baseSQL and v.o.
   * @param sessions SessionFactory
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
    ArrayList paramTypes,
    String tableName,
    SessionFactory sessions
  ) throws Exception {
    return applyFiltersAndSorter(
        new HashMap(),
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
        paramTypes,
        tableName,
        sessions
      );
  }


  /**
   * Apply filtering and sorting conditions to the specified baseSQL and return
   * a new baseSQL that contains those conditions too.
   * SQL is expressed using more argument, each one without the related keyword (select, from, ...).
   *
   * Example: following query
   *
   * select customer_code,corporate_name from companiesVO order by customer_code asc
   *
   * become an invokation of getSql:
   *
   * getSql(userSessionPars,"customer_code,corporate_name","companiesVO","","customer_code asc","","",...);
   *
   * If decodedAttributes is filled, then baseSQL can contains a HSQL query.
   * @param decodedAttributes collection of pairs <value object attribute name,attribute defined in HSQL query>
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
   * @param paramTypes parameters types, related to "?" in "baseSQL"
   * @param tableName table name related to baseSQL and v.o.
   * @param sessions SessionFactory
   */
  public static String applyFiltersAndSorter(
    Map decodedAttributes,
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
    ArrayList paramTypes,
    String tableName,
    SessionFactory sessions
  ) throws Exception {

//    Iterator it = filteredColumns.keySet().iterator();
//    String gridAttr,hsqlAttr;
//    HashSet toRemove = new HashSet();
//    HashMap toAdd = new HashMap();
//    while(it.hasNext()) {
//      gridAttr = it.next().toString();
//      hsqlAttr = (String)decodedAttributes.get(gridAttr);
//      if (hsqlAttr!=null) {
//        toAdd.put(hsqlAttr,filteredColumns.get(gridAttr));
//        toRemove.add(gridAttr);
//      }
//    }
//    filteredColumns.putAll(toAdd);
//    it = toRemove.iterator();
//    while(it.hasNext())
//      filteredColumns.remove(it.next());
//
//    for(int i=0;i<currentSortedColumns.size();i++) {
//      gridAttr = currentSortedColumns.get(i).toString();
//      hsqlAttr = (String)decodedAttributes.get(gridAttr);
//      if (hsqlAttr!=null) {
//        currentSortedColumns.set(i,hsqlAttr);
//      }
//    }

    // fill in "attributesMap" according to attributes defined in xxx.hdm.xml...
    ClassMetadata meta = sessions.getClassMetadata(valueObjectType);
    Map attributesMap = new HashMap();
    Map attributesTypeMap = new HashMap();
    Map propDescriptors = new HashMap();
    if (meta!=null) {
      fillInMetaData(sessions,tableName,"",meta,attributesMap,attributesTypeMap,"");
      attributesMap.put(meta.getIdentifierPropertyName(),tableName+"."+meta.getIdentifierPropertyName());
    }
    else {
      PropertyDescriptor[] p = Introspector.getBeanInfo(valueObjectType).getPropertyDescriptors();
      for(int i=0;i<p.length;i++)
        propDescriptors.put(p[i].getName(),p[i].getPropertyType());
    }
    attributesMap.putAll(decodedAttributes);

    // append filtering and sorting conditions to the base SQL...
    ArrayList filterAttrNames = new ArrayList();
    String baseSQL = QueryUtil.getSql(
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
      attributesMap
    );

    FilterWhereClause[] whereC = null;
    for(int i=0;i<filterAttrNames.size();i++) {
      whereC = (FilterWhereClause[])filteredColumns.get(filterAttrNames.get(i));

      if (whereC[0].getValue()!=null && whereC[0].getValue() instanceof List) {
        for(int j=0;j<((List)whereC[0].getValue()).size();j++)
          paramTypes.add(getPropertyType(attributesTypeMap,meta,filterAttrNames.get(i).toString(),propDescriptors));
      }
      else
        paramTypes.add(getPropertyType(attributesTypeMap,meta,filterAttrNames.get(i).toString(),propDescriptors));

//      if (whereC[1]!=null) {
//        if (whereC[1].getValue()!=null && whereC[1].getValue() instanceof List) {
//          for(int j=0;j<((List)whereC[1].getValue()).size();j++)
//            paramTypes.add(getPropertyType(attributesTypeMap,meta,filterAttrNames.get(i).toString(),propDescriptors));
//        }
//        else
//          paramTypes.add(getPropertyType(attributesTypeMap,meta,filterAttrNames.get(i).toString(),propDescriptors));
//      }
    }


    return baseSQL;
  }


  /**
   * Read the whole result set, by applying filtering and sorting conditions + query parameters.
   * @param decodedAttributes collection of pairs <value object attribute name,attribute defined in HSQL query>
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
    Map decodedAttributes,
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
        decodedAttributes,
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
    return getAllFromQuery(
    new HashMap(),
    filteredColumns,
    currentSortedColumns,
    currentSortedVersusColumns,
    valueObjectType,
    baseSQL,
    paramValues,
    paramTypes,
    tableName,
    sessions,
    sess
    );
  }


  /**
   * Read the whole result set, by applying filtering and sorting conditions + query parameters.
   * SQL is expressed using more argument, each one without the related keyword (select, from, ...).
   *
   * Example: following query
   *
   * select customer_code,corporate_name from companiesVO order by customer_code asc
   *
   * become an invokation of getSql:
   *
   * getSql(userSessionPars,"customer_code,corporate_name","companiesVO","","customer_code asc","","",...);
   *
   * @param decodedAttributes collection of pairs <value object attribute name,attribute defined in HSQL query>
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
   * @param paramTypes parameters types, related to "?" in "baseSQL" (optional)
   * @param tableName table name related to baseSQL and v.o.
   * @param sessions SessionFactory
   * @param sess Session
   */
  public static Response getAllFromQuery(
    Map decodedAttributes,
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
    Type[] paramTypes,
    String tableName,
    SessionFactory sessions,
    Session sess
  ) throws Exception {

    ArrayList values = new ArrayList();
    values.addAll(Arrays.asList(paramValues));
    ArrayList types = new ArrayList();
    types.addAll(Arrays.asList(paramTypes));
    String baseSQL = applyFiltersAndSorter(
        decodedAttributes,
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
   * Read the whole result set, by applying filtering and sorting conditions + query parameters.
   * SQL is expressed using more argument, each one without the related keyword (select, from, ...).
   *
   * Example: following query
   *
   * select customer_code,corporate_name from companiesVO order by customer_code asc
   *
   * become an invokation of getSql:
   *
   * getSql(userSessionPars,"customer_code,corporate_name","companiesVO","","customer_code asc","","",...);
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
    String select,
    String from,
    String where,
    String group,
    String having,
    String order,
    Object[] paramValues,
    Type[] paramTypes,
    String tableName,
    SessionFactory sessions,
    Session sess
  ) throws Exception {
  return getAllFromQuery(
    new HashMap(),
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
    paramTypes,
    tableName,
    sessions,
    sess
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
    return getBlockFromQuery(
        new HashMap(),
        action,
        startIndex,
        blockSize,
        filteredColumns,
        currentSortedColumns,
        currentSortedVersusColumns,
        valueObjectType,
        baseSQL,
        paramValues,
        paramTypes,
        tableName,
        sessions,
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
   * Read a block of records from the result set, by applying filtering and sorting conditions + query parameters.
   * @param decodedAttributes collection of pairs <value object attribute name,attribute defined in HSQL query>
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
    Map decodedAttributes,
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
        decodedAttributes,
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
      valueObjectType,
      action,
      startIndex,
      blockSize,
      sess.createQuery(baseSQL).setParameters(values.toArray(),(Type[])types.toArray(new Type[types.size()])),
      sess
    );
  }


  /**
   * Read a block of records from the result set, by applying filtering and sorting conditions + query parameters.
   * SQL is expressed using more argument, each one without the related keyword (select, from, ...).
   *
   * Example: following query
   *
   * select customer_code,corporate_name from companiesVO order by customer_code asc
   *
   * become an invokation of getSql:
   *
   * getSql(userSessionPars,"customer_code,corporate_name","companiesVO","","customer_code asc","","",...);
   *
   * @param decodedAttributes collection of pairs <value object attribute name,attribute defined in HSQL query>
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
   * @param paramTypes parameters types, related to "?" in "baseSQL" (optional)
   * @param tableName table name related to baseSQL and v.o.
   * @param sessions SessionFactory
   * @param sess Session
   */
  public static Response getBlockFromQuery(
    Map decodedAttributes,
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
    Type[] paramTypes,
    String tableName,
    SessionFactory sessions,
    Session sess
  ) throws Exception {

    ArrayList values = new ArrayList();
    values.addAll(Arrays.asList(paramValues));
    ArrayList types = new ArrayList();
    types.addAll(Arrays.asList(paramTypes));
    String baseSQL = applyFiltersAndSorter(
        decodedAttributes,
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
        types,
        tableName,
        sessions
    );

    return getBlockFromQuery(
      valueObjectType,
      action,
      startIndex,
      blockSize,
      sess.createQuery(baseSQL).setParameters(values.toArray(),(Type[])types.toArray(new Type[types.size()])),
      sess
    );
  }


  /**
   * Read a block of records from the result set, by applying filtering and sorting conditions + query parameters.
   * SQL is expressed using more argument, each one without the related keyword (select, from, ...).
   *
   * Example: following query
   *
   * select customer_code,corporate_name from companiesVO order by customer_code asc
   *
   * become an invokation of getSql:
   *
   * getSql(userSessionPars,"customer_code,corporate_name","companiesVO","","customer_code asc","","",...);
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
    String select,
    String from,
    String where,
    String group,
    String having,
    String order,
    Object[] paramValues,
    Type[] paramTypes,
    String tableName,
    SessionFactory sessions,
    Session sess
  ) throws Exception {
    return getBlockFromQuery(
      new HashMap(),
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
      paramTypes,
      tableName,
      sessions,
      sess
    );
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
    Class valueObjectClass,
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

    if (gridList.size()>0 && gridList.get(0) instanceof Object[]) {
      return QueryUtil.getQuery(
        query.getReturnAliases(),
        valueObjectClass,
        gridList,
        moreRows
      );

    }

    return new VOListResponse(gridList,moreRows,resultSetLength);
  }


  /**
   * Read a block of records from the result set, starting from the specified value object.
   * @param valueObjectClass value object type
   * @param filteredColumns filtering conditions
   * @param currentSortedColumns sorting conditions (attribute names)
   * @param currentSortedVersusColumns sorting conditions (order versus)
   * @param action fetching versus: PREVIOUS_BLOCK_ACTION, NEXT_BLOCK_ACTION or LAST_BLOCK_ACTION
   * @param startPos start position of data fetching in result set
   * @param blockSize number of records to read
   * @param fetchMode FetchMode object, used to specificy how to retrieve inner objects; e.g. FetchMode.INNER
   * @param sess Session
   */
  public static Response getBlockFromClass(
    Class valueObjectClass,
    Map filteredColumns,
    ArrayList currentSortedColumns,
    ArrayList currentSortedVersusColumns,
    int action,
    int startIndex,
    int blockSize,
    FetchMode fetchMode,
    Session sess
  ) throws Exception {

    // apply filtering conditions...
    Criteria criteria = sess.createCriteria(valueObjectClass).setFetchMode("permissions", fetchMode);

    return getBlockFromCriteria(
      filteredColumns,
      currentSortedColumns,
      currentSortedVersusColumns,
      action,
      startIndex,
      blockSize,
      criteria,
      sess
    );
  }


  /**
   * Read a block of records from the result set, starting from the specified Criteria.
   * @param filteredColumns filtering conditions
   * @param currentSortedColumns sorting conditions (attribute names)
   * @param currentSortedVersusColumns sorting conditions (order versus)
   * @param action fetching versus: PREVIOUS_BLOCK_ACTION, NEXT_BLOCK_ACTION or LAST_BLOCK_ACTION
   * @param startPos start position of data fetching in result set
   * @param blockSize number of records to read
   * @param criteria Criteria object to execute
   * @param sess Session
   */
  public static Response getBlockFromCriteria(
    Map filteredColumns,
    ArrayList currentSortedColumns,
    ArrayList currentSortedVersusColumns,
    int action,
    int startIndex,
    int blockSize,
    Criteria criteria,
    Session sess
  ) throws Exception {
    Iterator keys = filteredColumns.keySet().iterator();
    String attributeName = null;
    FilterWhereClause[] filterClauses = null;
    while (keys.hasNext()) {
      attributeName = keys.next().toString();
      filterClauses = (FilterWhereClause[]) filteredColumns.get(attributeName);
      if (  filterClauses[0].getValue()!=null &&
          !(filterClauses[0].getOperator().equals(Consts.IS_NOT_NULL) || filterClauses[0].getOperator().equals(Consts.IS_NULL))) {
        if (filterClauses[0].getValue() instanceof ArrayList) {
          // name IN (...)
          // (name op value1 OR name op value2 OR ...)
          ArrayList inValues = (ArrayList)filterClauses[0].getValue();
          criteria = criteria.add( Restrictions.in(attributeName,inValues) );
        } else {
          // name op value
          if (filterClauses[0].getOperator().equals(Consts.EQ))
            criteria = criteria.add( Restrictions.eq(attributeName,filterClauses[0].getValue()) );
          else if (filterClauses[0].getOperator().equals(Consts.NEQ))
            criteria = criteria.add( Restrictions.ne(attributeName,filterClauses[0].getValue()) );
          else if (filterClauses[0].getOperator().equals(Consts.LE))
            criteria = criteria.add( Restrictions.le(attributeName,filterClauses[0].getValue()) );
          else if (filterClauses[0].getOperator().equals(Consts.LT))
            criteria = criteria.add( Restrictions.lt(attributeName,filterClauses[0].getValue()) );
          else if (filterClauses[0].getOperator().equals(Consts.GE))
            criteria = criteria.add( Restrictions.ge(attributeName,filterClauses[0].getValue()) );
          else if (filterClauses[0].getOperator().equals(Consts.GT))
            criteria = criteria.add( Restrictions.gt(attributeName,filterClauses[0].getValue()) );
          else if (filterClauses[0].getOperator().equals(Consts.LIKE))
            criteria = criteria.add( Restrictions.like(attributeName,filterClauses[0].getValue()) );
        }
      }
      else {
        // name IS NULL
        // name IS NOT NULL
        if (filterClauses[0].getOperator().equals(Consts.IS_NULL))
          criteria = criteria.add( Restrictions.isNull(attributeName) );
        else if (filterClauses[0].getOperator().equals(Consts.IS_NOT_NULL))
          criteria = criteria.add( Restrictions.isNotNull(attributeName) );
      }
      if (filterClauses[1] != null) {
        if (  filterClauses[1].getValue()!=null &&
            !(filterClauses[1].getOperator().equals(Consts.IS_NOT_NULL) || filterClauses[1].getOperator().equals(Consts.IS_NULL))) {
          if (filterClauses[1].getValue() instanceof ArrayList) {
            // name IN (...)
            // (name op value1 OR name op value2 OR ...)
            ArrayList inValues = (ArrayList)filterClauses[1].getValue();
            criteria = criteria.add( Restrictions.in(attributeName,inValues) );
          } else {
            // name op value
            if (filterClauses[1].getOperator().equals(Consts.EQ))
              criteria = criteria.add( Restrictions.eq(attributeName,filterClauses[1].getValue()) );
            else if (filterClauses[1].getOperator().equals(Consts.NEQ))
              criteria = criteria.add( Restrictions.ne(attributeName,filterClauses[1].getValue()) );
            else if (filterClauses[1].getOperator().equals(Consts.LE))
              criteria = criteria.add( Restrictions.le(attributeName,filterClauses[1].getValue()) );
            else if (filterClauses[1].getOperator().equals(Consts.LT))
              criteria = criteria.add( Restrictions.lt(attributeName,filterClauses[1].getValue()) );
            else if (filterClauses[1].getOperator().equals(Consts.GE))
              criteria = criteria.add( Restrictions.ge(attributeName,filterClauses[1].getValue()) );
            else if (filterClauses[1].getOperator().equals(Consts.GT))
              criteria = criteria.add( Restrictions.gt(attributeName,filterClauses[1].getValue()) );
            else if (filterClauses[1].getOperator().equals(Consts.LIKE))
              criteria = criteria.add( Restrictions.like(attributeName,filterClauses[1].getValue()) );
          }
        }
        else {
          // name IS NULL
          // name IS NOT NULL
          if (filterClauses[1].getOperator().equals(Consts.IS_NULL))
            criteria = criteria.add( Restrictions.isNull(attributeName) );
          else if (filterClauses[1].getOperator().equals(Consts.IS_NOT_NULL))
            criteria = criteria.add( Restrictions.isNotNull(attributeName) );
        }
      }
    }

    // applying ordering conditions...
    for(int i=0;i<currentSortedColumns.size();i++)
      if (currentSortedVersusColumns.get(i).equals(Consts.ASC_SORTED))
        criteria = criteria.addOrder( Order.asc(currentSortedColumns.get(i).toString()) );
      else
        criteria = criteria.addOrder( Order.desc(currentSortedColumns.get(i).toString()) );

    // read a block of records...
    ArrayList gridList = new ArrayList();
    boolean moreRows = false;
    int resultSetLength = -1;
    int rowCount = 0;
    List list = null;
    if (action==GridParams.LAST_BLOCK_ACTION) {
      // last block requested: the whole result set will be loaded, to determine the result set length
      List tmp = criteria.list();
      rowCount = tmp.size();

      resultSetLength = rowCount;
      action = GridParams.NEXT_BLOCK_ACTION;
      startIndex = Math.max(rowCount-blockSize,0);
      rowCount = 0;

      list = new ArrayList();
      for(int i=startIndex;i<startIndex+blockSize;i++)
        list.add(tmp.get(i));
    } else {
      if (action==GridParams.PREVIOUS_BLOCK_ACTION) {
        action = GridParams.NEXT_BLOCK_ACTION;
        startIndex = Math.max(startIndex-blockSize,0);
      }
      criteria.setFirstResult(startIndex);
      criteria.setMaxResults(blockSize+1);
      list = criteria.list();
    }
    gridList.addAll(list);
    if (gridList.size()>blockSize) {
      gridList.remove(gridList.size() - 1);
      moreRows = true;
    }
    if (resultSetLength==-1)
      resultSetLength = gridList.size();

//    if (gridList.size()>0 && gridList.get(0) instanceof Object[]) {
//      return QueryUtil.getQuery(
//        query.getReturnAliases(),
//        valueObjectClass,
//        gridList,
//        moreRows
//      );
//
//    }

    return new VOListResponse(gridList,moreRows,resultSetLength);
  }


  /**
   * Read all records from the result set, starting from the specified value object.
   * @param valueObjectClass value object type
   * @param filteredColumns filtering conditions
   * @param currentSortedColumns sorting conditions (attribute names)
   * @param currentSortedVersusColumns sorting conditions (order versus)
   * @param fetchMode FetchMode object, used to specificy how to retrieve inner objects; e.g. FetchMode.INNER
   * @param sess Session
   */
  public static Response getAllFromClass(
    Class valueObjectClass,
    Map filteredColumns,
    ArrayList currentSortedColumns,
    ArrayList currentSortedVersusColumns,
    FetchMode fetchMode,
    Session sess
  ) throws Exception {

    // apply filtering conditions...
    Criteria criteria = sess.createCriteria(valueObjectClass).setFetchMode("permissions", fetchMode);

    return getAllFromCriteria(
      filteredColumns,
      currentSortedColumns,
      currentSortedVersusColumns,
      criteria,
      sess
    );
  }


  /**
   * Read all records from the result set, starting from the specified Criteria.
   * @param valueObjectClass value object type
   * @param filteredColumns filtering conditions
   * @param currentSortedColumns sorting conditions (attribute names)
   * @param currentSortedVersusColumns sorting conditions (order versus)
   * @param fetchMode FetchMode object, used to specificy how to retrieve inner objects; e.g. FetchMode.INNER
   * @param sess Session
   */
  public static Response getAllFromCriteria(
    Map filteredColumns,
    ArrayList currentSortedColumns,
    ArrayList currentSortedVersusColumns,
    Criteria criteria,
    Session sess
  ) throws Exception {
    Iterator keys = filteredColumns.keySet().iterator();
    String attributeName = null;
    FilterWhereClause[] filterClauses = null;
    while (keys.hasNext()) {
      attributeName = keys.next().toString();
      filterClauses = (FilterWhereClause[]) filteredColumns.get(attributeName);
      if (  filterClauses[0].getValue()!=null &&
          !(filterClauses[0].getOperator().equals(Consts.IS_NOT_NULL) || filterClauses[0].getOperator().equals(Consts.IS_NULL))) {
        if (filterClauses[0].getValue() instanceof ArrayList) {
          // name IN (...)
          // (name op value1 OR name op value2 OR ...)
          ArrayList inValues = (ArrayList)filterClauses[0].getValue();
          criteria = criteria.add( Restrictions.in(attributeName,inValues) );
        } else {
          // name op value
          if (filterClauses[0].getOperator().equals(Consts.EQ))
            criteria = criteria.add( Restrictions.eq(attributeName,filterClauses[0].getValue()) );
          else if (filterClauses[0].getOperator().equals(Consts.NEQ))
            criteria = criteria.add( Restrictions.ne(attributeName,filterClauses[0].getValue()) );
          else if (filterClauses[0].getOperator().equals(Consts.LE))
            criteria = criteria.add( Restrictions.le(attributeName,filterClauses[0].getValue()) );
          else if (filterClauses[0].getOperator().equals(Consts.LT))
            criteria = criteria.add( Restrictions.lt(attributeName,filterClauses[0].getValue()) );
          else if (filterClauses[0].getOperator().equals(Consts.GE))
            criteria = criteria.add( Restrictions.ge(attributeName,filterClauses[0].getValue()) );
          else if (filterClauses[0].getOperator().equals(Consts.GT))
            criteria = criteria.add( Restrictions.gt(attributeName,filterClauses[0].getValue()) );
          else if (filterClauses[0].getOperator().equals(Consts.LIKE))
            criteria = criteria.add( Restrictions.like(attributeName,filterClauses[0].getValue()) );
        }
      }
      else {
        // name IS NULL
        // name IS NOT NULL
        if (filterClauses[0].getOperator().equals(Consts.IS_NULL))
          criteria = criteria.add( Restrictions.isNull(attributeName) );
        else if (filterClauses[0].getOperator().equals(Consts.IS_NOT_NULL))
          criteria = criteria.add( Restrictions.isNotNull(attributeName) );
      }
      if (filterClauses[1] != null) {
        if (  filterClauses[1].getValue()!=null &&
            !(filterClauses[1].getOperator().equals(Consts.IS_NOT_NULL) || filterClauses[1].getOperator().equals(Consts.IS_NULL))) {
          if (filterClauses[1].getValue() instanceof ArrayList) {
            // name IN (...)
            // (name op value1 OR name op value2 OR ...)
            ArrayList inValues = (ArrayList)filterClauses[1].getValue();
            criteria = criteria.add( Restrictions.in(attributeName,inValues) );
          } else {
            // name op value
            if (filterClauses[1].getOperator().equals(Consts.EQ))
              criteria = criteria.add( Restrictions.eq(attributeName,filterClauses[1].getValue()) );
            else if (filterClauses[1].getOperator().equals(Consts.NEQ))
              criteria = criteria.add( Restrictions.ne(attributeName,filterClauses[1].getValue()) );
            else if (filterClauses[1].getOperator().equals(Consts.LE))
              criteria = criteria.add( Restrictions.le(attributeName,filterClauses[1].getValue()) );
            else if (filterClauses[1].getOperator().equals(Consts.LT))
              criteria = criteria.add( Restrictions.lt(attributeName,filterClauses[1].getValue()) );
            else if (filterClauses[1].getOperator().equals(Consts.GE))
              criteria = criteria.add( Restrictions.ge(attributeName,filterClauses[1].getValue()) );
            else if (filterClauses[1].getOperator().equals(Consts.GT))
              criteria = criteria.add( Restrictions.gt(attributeName,filterClauses[1].getValue()) );
            else if (filterClauses[1].getOperator().equals(Consts.LIKE))
              criteria = criteria.add( Restrictions.like(attributeName,filterClauses[1].getValue()) );
          }
        }
        else {
          // name IS NULL
          // name IS NOT NULL
          if (filterClauses[1].getOperator().equals(Consts.IS_NULL))
            criteria = criteria.add( Restrictions.isNull(attributeName) );
          else if (filterClauses[1].getOperator().equals(Consts.IS_NOT_NULL))
            criteria = criteria.add( Restrictions.isNotNull(attributeName) );
        }
      }
    }

    // applying ordering conditions...
    for(int i=0;i<currentSortedColumns.size();i++)
      if (currentSortedVersusColumns.get(i).equals(Consts.ASC_SORTED))
        criteria = criteria.addOrder( Order.asc(currentSortedColumns.get(i).toString()) );
      else
        criteria = criteria.addOrder( Order.desc(currentSortedColumns.get(i).toString()) );

    // read a block of records...
    ArrayList gridList = new ArrayList();
    boolean moreRows = false;
    List list = criteria.list();
    int rowCount = list.size();
    int resultSetLength = rowCount;

    gridList.addAll(list);

//    if (gridList.size()>0 && gridList.get(0) instanceof Object[]) {
//      return QueryUtil.getQuery(
//        query.getReturnAliases(),
//        valueObjectClass,
//        gridList,
//        moreRows
//      );
//
//    }

    return new VOListResponse(gridList,moreRows,resultSetLength);
  }


  private static org.hibernate.type.Type getPropertyType(Map attributesTypeMap,ClassMetadata meta,String attrName,Map propDescriptors) {
    if (attributesTypeMap!=null) {
      org.hibernate.type.Type type = (org.hibernate.type.Type)attributesTypeMap.get(attrName);
      if (type!=null)
        return type;
    }
    if (meta!=null)
      return meta.getPropertyType(attrName);
    Class clazz = (Class)propDescriptors.get(attrName);
    if (clazz==null)
      return new org.hibernate.type.StringType();
    else if (clazz.equals(BigDecimal.class))
      return new org.hibernate.type.BigDecimalType();
    else if (clazz.equals(Double.class))
      return new org.hibernate.type.DoubleType();
    else if (clazz.equals(Long.class))
      return new org.hibernate.type.LongType();
    else if (clazz.equals(BigInteger.class))
      return new org.hibernate.type.BigIntegerType();
    else if (clazz.equals(Integer.class))
      return new org.hibernate.type.IntegerType();
    else if (clazz.equals(Float.class))
      return new org.hibernate.type.FloatType();
    else if (clazz.equals(Short.class))
      return new org.hibernate.type.ShortType();
    else if (clazz.equals(java.util.Date.class))
      return new org.hibernate.type.DateType();
    else if (clazz.equals(java.sql.Date.class))
      return new org.hibernate.type.DateType();
    else if (clazz.equals(java.sql.Timestamp.class))
      return new org.hibernate.type.TimestampType();
    else
      return new org.hibernate.type.StringType();
  }


}
