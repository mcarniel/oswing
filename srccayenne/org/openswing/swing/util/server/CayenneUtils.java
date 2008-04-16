package org.openswing.swing.util.server;

import java.util.*;

import org.apache.cayenne.access.DataContext;
import org.apache.cayenne.exp.Expression;
import org.apache.cayenne.exp.ExpressionFactory;
import org.apache.cayenne.query.SelectQuery;
import org.apache.cayenne.Persistent;
import org.apache.cayenne.DataObject;
import org.openswing.swing.message.receive.java.*;
import org.openswing.swing.message.send.java.*;
import org.openswing.swing.server.*;
import org.apache.cayenne.query.Query;
import org.openswing.swing.util.java.Consts;


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
public class CayenneUtils {


  /**
   * Apply filtering and sorting conditions to the specified base select query and return
   * a new query that contains those conditions too.
   * @param filteredColumns filtering conditions
   * @param currentSortedColumns sorting conditions (attribute names)
   * @param currentSortedVersusColumns sorting conditions (order versus)
   * @param query Cayenne SelectQuery object
   * @param additionalConditions conditions expressed as <attribute name,value>
   * @param context Cayenne data context
   */
  public static SelectQuery applyFiltersAndSorter(
    Map filteredColumns,
    ArrayList currentSortedColumns,
    ArrayList currentSortedVersusColumns,
    SelectQuery query,
    Map additionalConditions,
    DataContext context
  ) throws Exception {
    // append filtering conditions...
    query = query.queryWithParameters(additionalConditions);
    HashMap values = new HashMap();

    String baseSQL = "";
    Iterator keys = filteredColumns.keySet().iterator();
    String attributeName = null;
    FilterWhereClause[] filterClauses = null;
    int num = 1;
    while (keys.hasNext()) {
      attributeName = keys.next().toString();
      filterClauses = (FilterWhereClause[]) filteredColumns.get(attributeName);
      if (  filterClauses[0].getValue()!=null &&
          !(filterClauses[0].getOperator().equals(Consts.IS_NOT_NULL) || filterClauses[0].getOperator().equals(Consts.IS_NULL))) {
        if (filterClauses[0].getValue() instanceof ArrayList) {
          // name IN (...)
          // (name op value1 OR name op value2 OR ...)
          if (filterClauses[0].getOperator().equals(Consts.IN)) {
            // name IN (...)
            baseSQL +=
              attributeName +
              " " + filterClauses[0].getOperator() +
              " (";
            ArrayList inValues = (ArrayList)filterClauses[0].getValue();
            for(int j=0;j<inValues.size();j++) {
              baseSQL += " $n"+num+",";
              values.put("n"+num,inValues.get(j));
              num++;
            }
            baseSQL = baseSQL.substring(0,baseSQL.length()-1);
            baseSQL += ") AND ";
          }
          else {
            // (name op value1 OR name op value2 OR ...)
            baseSQL += "(";
            ArrayList inValues = (ArrayList)filterClauses[0].getValue();
            for(int j=0;j<inValues.size();j++) {
              baseSQL +=
                  attributeName +
                  " " + filterClauses[0].getOperator() +
                  " $n"+num+" OR ";
              values.put("n"+num,inValues.get(j));
              num++;
            }
            baseSQL = baseSQL.substring(0,baseSQL.length()-3);
            baseSQL += ") AND ";
          }
        } else {
          // name op value
          baseSQL +=
              attributeName +
              " " + filterClauses[0].getOperator() +
              " $n"+num+" AND ";
          values.put("n"+num,filterClauses[0].getValue());
          num++;
        }
      }
      else {
        // name IS NULL
        // name IS NOT NULL
        baseSQL +=
              attributeName +
              " " + filterClauses[0].getOperator() + " " +
              "AND ";
      }
      if (filterClauses[1] != null) {
        if (  filterClauses[1].getValue()!=null &&
            !(filterClauses[1].getOperator().equals(Consts.IS_NOT_NULL) || filterClauses[1].getOperator().equals(Consts.IS_NULL))) {
          if (filterClauses[1].getValue() instanceof ArrayList) {
            // name IN (...)
            // (name op value1 OR name op value2 OR ...)
            if (filterClauses[1].getOperator().equals(Consts.IN)) {
              // name IN (...)
              baseSQL +=
                attributeName +
                " " + filterClauses[1].getOperator() +
                " (";
              ArrayList inValues = (ArrayList)filterClauses[1].getValue();
              for(int j=0;j<inValues.size();j++) {
                baseSQL += " $n"+num+",";
                values.put("n"+num,inValues.get(j));
                num++;
              }
              baseSQL = baseSQL.substring(0,baseSQL.length()-1);
              baseSQL += ") AND ";
            }
            else {
              // (name op value1 OR name op value2 OR ...)
              baseSQL += "(";
              ArrayList inValues = (ArrayList)filterClauses[1].getValue();
              for(int j=0;j<inValues.size();j++) {
                baseSQL +=
                    attributeName +
                    " " + filterClauses[1].getOperator() +
                    " $n"+num+" OR ";
                values.put("n"+num,inValues.get(j));
              }
              baseSQL = baseSQL.substring(0,baseSQL.length()-3);
              baseSQL += ") AND ";
            }
          } else {
            // name op value
            baseSQL +=
                attributeName +
                " " + filterClauses[1].getOperator() +
                " $n"+num+" AND ";
            values.put("n"+num,filterClauses[1].getValue());
          }
        }
        else {
          // name IS NULL
          // name IS NOT NULL
          baseSQL +=
                attributeName +
                " " + filterClauses[1].getOperator() + " " +
                "AND ";
        }
      }
    }
    if (baseSQL.length()>4) {
      baseSQL = baseSQL.substring(0, baseSQL.length() - 4);
      query.andQualifier(Expression.fromString(baseSQL).expWithParameters(values));
    }

    for(int i=0;i<currentSortedColumns.size();i++)
      query.addOrdering(currentSortedColumns.get(i).toString(),currentSortedVersusColumns.get(i).toString().equalsIgnoreCase("ASC"));

    return query;
  }


  /**
   * Read the whole result set, by applying filtering and sorting conditions + query parameters.
   * @param filteredColumns filtering conditions
   * @param currentSortedColumns sorting conditions (attribute names)
   * @param currentSortedVersusColumns sorting conditions (order versus)
   * @param valueObjectType value object type
   * @param query Cayenne SelectQuery object
   * @param additionalConditions conditions expressed as <attribute name,value>
   * @param context Cayenne data context
   */
  public static Response getAllFromQuery(
    Map filteredColumns,
    ArrayList currentSortedColumns,
    ArrayList currentSortedVersusColumns,
    Class valueObjectType,
    SelectQuery query,
    Map additionalConditions,
    DataContext context
  ) throws Exception {

    query = applyFiltersAndSorter(
        filteredColumns,
        currentSortedColumns,
        currentSortedVersusColumns,
        query,
        additionalConditions,
        context
    );

    ArrayList gridList = new ArrayList();
    boolean moreRows = false;
    int resultSetLength = -1;

    // read the whole result set...
    List list = context.performQuery(query);
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
   * @param query Cayenne SelectQuery object
   * @param additionalConditions conditions expressed as <attribute name,value>
   * @param tableName table name related to baseSQL and v.o.
   * @param context Cayenne data context
   */
  public static Response getBlockFromQuery(
    int action,
    int startIndex,
    int blockSize,
    Map filteredColumns,
    ArrayList currentSortedColumns,
    ArrayList currentSortedVersusColumns,
    SelectQuery query,
    Map additionalConditions,
    DataContext context
  ) throws Exception {
    query = applyFiltersAndSorter(
        filteredColumns,
        currentSortedColumns,
        currentSortedVersusColumns,
        query,
        additionalConditions,
        context
    );
    return getBlockFromQuery(
      action,
      startIndex,
      blockSize,
      query,
      context
    );
  }


  /**
   * Read a block of records from the result set, starting from a Query object.
   * @param action fetching versus: PREVIOUS_BLOCK_ACTION, NEXT_BLOCK_ACTION or LAST_BLOCK_ACTION
   * @param startPos start position of data fetching in result set
   * @param blockSize number of records to read
   * @param query Query object
   * @param context Cayenne data context
   */
  public static Response getBlockFromQuery(
    int action,
    int startIndex,
    int blockSize,
    SelectQuery query,
    DataContext context
  ) throws Exception {

    // read a block of records...
    ArrayList gridList = new ArrayList();
    boolean moreRows = false;
    int resultSetLength = -1;
    int rowCount = 0;
    List list = null;
    if (action==GridParams.LAST_BLOCK_ACTION) {
      // last block requested: the whole result set will be loaded, to determine the result set length
      list = context.performQuery(query);
      resultSetLength = list.size();
      startIndex = Math.max(rowCount-blockSize,0);
      for(int i=startIndex;i<resultSetLength;i++)
        gridList.add(list.get(i));
      return new VOListResponse(gridList,false,resultSetLength);
    } else {
      if (action==GridParams.PREVIOUS_BLOCK_ACTION) {
        action = GridParams.NEXT_BLOCK_ACTION;
        startIndex = Math.max(startIndex-blockSize,0);
      }
    }

    // read a block of data...
    query.setFetchLimit(startIndex+blockSize+1);
    list = context.performQuery(query);
    for(int i=startIndex;i<list.size();i++) {
      if (gridList.size()==blockSize) {
        moreRows = true;
        break;
      }
      gridList.add(list.get(i));
    }

    if (resultSetLength==-1)
      resultSetLength = gridList.size();
    return new VOListResponse(gridList,moreRows,resultSetLength);
  }


}
