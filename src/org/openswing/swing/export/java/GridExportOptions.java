package org.openswing.swing.export.java;

import java.io.*;
import java.util.*;

import org.openswing.swing.table.java.*;
import org.openswing.swing.message.send.java.FilterWhereClause;
import org.openswing.swing.util.java.Consts;


/**
 * <p>Title: OpenSwing Framework</p>
 * <p>Description: Export information needed to export data related to a grid on the server side.</p>
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
public class GridExportOptions extends ComponentExportOptions implements Serializable {

  /** columns to export */
  private ArrayList exportColumns = null;

  /** attribute names related to the columns to export */
  private ArrayList exportAttrColumns = null;

  /** filtered columns; collection of pairs: attributeName, FilterWhereClause[2] */
  private Map filteredColumns = null;

  /** sorted columns */
  private ArrayList currentSortedColumns = new ArrayList();

  /** ordering versus of sorted columns */
  private ArrayList currentSortedVersusColumns = new ArrayList();

  /** other grid parameters */
  private Map otherGridParams = null;

  /** server method name to invoke on the server */
  private String serverMethodName = null;

  /** maximum number of rows to export */
  private int maxRows;

  /** grid data locator */
  private GridDataLocator gridDataLocator = null;

  /** columns width */
  private Hashtable columnsWidth = null;

  /** columns type */
  private Hashtable columnsType = null;

  /** valueObjectType v.o. type */
  private Class valueObjectType = null;

  public static final int TYPE_TEXT = 0;
  public static final int TYPE_DATE = 1;
  public static final int TYPE_DATE_TIME = 2;
  public static final int TYPE_TIME = 3;
  public static final int TYPE_INT = 4;
  public static final int TYPE_DEC = 5;
  public static final int TYPE_CHECK = 6;
  public static final int TYPE_COMBO = 7;
  public static final int TYPE_LOOKUP = 8;
  public static final int TYPE_PERC = 9;
  public static final int TYPE_CURRENCY = 10;

  /** list of locked rows at the top of the grid */
  private ArrayList topRows = new ArrayList();

  /** list of locked rows at the bottom of the grid */
  private ArrayList bottomRows = new ArrayList();

  /** flag used to add a filter panel on top of the grid, in order to show filtering conditions; this pane is visibile only whether there is at least one filtering condition applied; default value: <code>ClientSettings.SHOW_FILTERING_CONDITIONS_IN_EXPORT</code> */
  private boolean showFilteringConditions = false;

  /** export document title (optional) */
  private String title = null;

  /** collection of pairs <attribute name, translation> */
  private Hashtable attributeDescriptions = null;

  /** callbacks to invoke when exporting grid */
  private GridExportCallbacks callbacks = null;


  /**
   * Method called by Grid.
   * @param exportColumns column identifiers related to columns to export
   * @param exportAttrColumns attribute names related to the columns to export
   * @param filteredColumns  filtered columns; collection of pairs: attributeName, FilterWhereClause[2]
   * @param currentSortedColumns sorted columns
   * @param currentSortedVersusColumns ordering versus of sorted columns
   * @param otherGridParams other grid parameters
   * @param maxRows maximum number of rows to export
   * @param valueObjectType v.o. type
   * @param gridDataLocator grid data locator
   * @param columnsWidth columns width
   * @param columnsType columns type
   * @param attributeDescriptions collection of pairs <attribute name, translation>
   * @param topRows list of locked rows at the top of the grid
   * @param bottomRows list of locked rows at the bottom of the grid
   */
  public GridExportOptions(
      ArrayList exportColumns,
      ArrayList exportAttrColumns,
      Map filteredColumns,
      ArrayList currentSortedColumns,
      ArrayList currentSortedVersusColumns,
      Map otherGridParams,
      int maxRows,
      Class valueObjectType,
      GridDataLocator gridDataLocator,
      Hashtable columnsWidth,
      Hashtable columnsType,
      Hashtable attributeDescriptions,
      ArrayList topRows,
      ArrayList bottomRows
   ) {
    this.exportColumns = exportColumns;
    this.exportAttrColumns = exportAttrColumns;
    this.filteredColumns = filteredColumns;
    this.currentSortedColumns = currentSortedColumns;
    this.currentSortedVersusColumns = currentSortedVersusColumns;
    this.otherGridParams = otherGridParams;
    this.maxRows = maxRows;
    this.valueObjectType = valueObjectType;
    this.gridDataLocator = gridDataLocator;
    this.columnsWidth = columnsWidth;
    this.columnsType = columnsType;
    this.attributeDescriptions = attributeDescriptions;
    this.topRows = topRows;
    this.bottomRows = bottomRows;
  }


  /**
   * @return attribute names related to the columns to export
   */
  public final ArrayList getExportAttrColumns() {
    return exportAttrColumns;
  }

  /**
   * @return columns to export
   */
  public final ArrayList getExportColumns() {
    return exportColumns;
  }


  /**
   * @return sorted columns
   */
  public final ArrayList getCurrentSortedColumns() {
    return currentSortedColumns;
  }

  /**
   * @return ordering versus of sorted columns
   */
  public final ArrayList getCurrentSortedVersusColumns() {
    return currentSortedVersusColumns;
  }


  /**
   * @return other grid parameters
   */
  public final Map getOtherGridParams() {
    return otherGridParams;
  }


  /**
   * @return filteredColumns; collection of pairs: attributeName, FilterWhereClause[2]
   */
  public final Map getFilteredColumns() {
    return filteredColumns;
  }


  /**
   * @return server method name to invoke on the server
   */
  public final String getServerMethodName() {
    return serverMethodName;
  }


  /**
   * @return maximum number of rows to export
   */
  public final int getMaxRows() {
    return maxRows;
  }


  /**
   * Set the server method name to invoke on the server.
   * @param serverMethodName server method name to invoke on the server
   */
  public final void setServerMethodName(String serverMethodName) {
    this.serverMethodName = serverMethodName;
  }


  /**
   * @return grid data locator
   */
  public final GridDataLocator getGridDataLocator() {
    return gridDataLocator;
  }


  /**
   * Set the grid data locator.
   * @param gridDataLocator grid data locator
   */
  public final void setGridDataLocator(GridDataLocator gridDataLocator) {
    this.gridDataLocator = gridDataLocator;
  }


  /**
   * @return columns type
   */
  public final Hashtable getColumnsType() {
    return columnsType;
  }


  /**
   * @return columns width
   */
  public final Hashtable getColumnsWidth() {
    return columnsWidth;
  }


  /**
   * @return valueObjectType v.o. type
   */
  public final Class getValueObjectType() {
    return valueObjectType;
  }


  /**
   * @return list of locked rows at the top of the grid
   */
  public final ArrayList getTopRows() {
    return topRows;
  }


  /**
   * @return list of locked rows at the bottom of the grid
   */
  public final ArrayList getBottomRows() {
    return bottomRows;
  }


  /**
   * Used to add a filter panel on top of the grid, in order to show filtering conditions.
   * This pane is visibile only whether "showFilteringConditions" is set to <code>true</code> and there is at least one filtering condition applied.
   * @param showFilteringConditions used to add a filter panel on top of the grid, in order to show filtering conditions
   */
  public final void setShowFilteringConditions(boolean showFilteringConditions) {
    this.showFilteringConditions = showFilteringConditions;
  }


  /**
   * @return export document title (optional)
   */
  public final String getTitle() {
    return title;
  }


  /**
   * Set the export document title.
   * @param title export document title
   */
  public final void setTitle(String title) {
    this.title = title;
  }


  public String[] getFilteringConditions() {
    if (showFilteringConditions && filteredColumns.size()>0) {
      ArrayList filters = new ArrayList();
      Iterator it = filteredColumns.keySet().iterator();
      FilterWhereClause[] filter = null;
      String attr = null;
      Object val = null;
      String aux = null;
      while(it.hasNext()) {
        attr = it.next().toString();
        filter = (FilterWhereClause[])filteredColumns.get(attr);

        val = filter[0].getValue();
        if(val==null)
          aux = "";
        else if (val instanceof ArrayList) {
          ArrayList list = (ArrayList)val;
          aux = "(";
          for(int i=0;i<list.size();i++)
            aux += (list.get(i)==null?"":list.get(i).toString())+",";
          if (list.size()>0)
            aux = aux.substring(0,aux.length()-1);
          aux += ")";
        }
        filters.add(attributeDescriptions.get(filter[0].getAttributeName())+" "+filter[0].getOperator()+" "+aux);

        if (filter[1]!=null) {
          val = filter[1].getValue();
          if(val==null)
            aux = "";
          else if (val instanceof ArrayList) {
            ArrayList list = (ArrayList)val;
            aux = "(";
            for(int i=0;i<list.size();i++)
              aux += (list.get(i)==null?"":list.get(i).toString())+",";
            if (list.size()>0)
              aux = aux.substring(0,aux.length()-1);
            aux += ")";
          }
          filters.add(attributeDescriptions.get(filter[1].getAttributeName())+" "+filter[1].getOperator()+" "+aux);
        }
      }
      return (String[])filters.toArray(new String[filters.size()]);
    }
    else
      return null;
  }


  /**
   * @param callbacks callbacks to invoke when exporting grid
   */
  public final GridExportCallbacks getCallbacks() {
    return callbacks;
  }


  /**
   * Set callbacks callbacks to invoke when exporting grid.
   * @param callbacks callbacks to invoke when exporting grid
   */
  public final void setCallbacks(GridExportCallbacks callbacks) {
    this.callbacks = callbacks;
  }


}
