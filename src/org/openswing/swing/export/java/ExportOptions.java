package org.openswing.swing.export.java;

import java.io.*;
import java.util.*;

import org.openswing.swing.table.java.*;
import org.openswing.swing.message.send.java.FilterWhereClause;
import org.openswing.swing.util.java.Consts;


/**
 * <p>Title: OpenSwing Framework</p>
 * <p>Description: Export information needed to export data related, for instance to a grid) on the server side.</p>
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
public class ExportOptions implements Serializable {

  /** date format */
  private String dateFormat = null;

  /** time format */
  private String timeFormat = null;

  /** date+time format */
  private String dateTimeFormat = null;

  /** export format */
  private String exportType = null;

  public static final String XLS_FORMAT = "XLS";
  public static final String CSV_FORMAT1 = "CSV (,)";
  public static final String CSV_FORMAT2 = "CSV (;)";
  public static final String XML_FORMAT = "XML (small format)";
  public static final String XML_FORMAT_FAT = "XML (large format)";
  public static final String HTML_FORMAT = "HTML";
  public static final String PDF_FORMAT = "PDF";
  public static final String RTF_FORMAT = "RTF";

  /** list of ComponentExportOptions objects, related to descriptors for exporting data */
  private ArrayList componentsExportOptions = new ArrayList();


  /**
   * Method called by Grid.
   * @param exportColumns columns to export
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
   * @param dateFormat date format
   * @param timeFormat time format
   * @param dateTimeFormat date+time format
   * @param exportType export format
   * @param topRows list of locked rows at the top of the grid
   * @param bottomRows list of locked rows at the bottom of the grid
   */
  public ExportOptions(
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
      String dateFormat,
      String timeFormat,
      String dateTimeFormat,
      String exportType,
      ArrayList topRows,
      ArrayList bottomRows
   ) {
    this.componentsExportOptions.add(
      new GridExportOptions(
        exportColumns,
        exportAttrColumns,
        filteredColumns,
        currentSortedColumns,
        currentSortedVersusColumns,
        otherGridParams,
        maxRows,
        valueObjectType,
        gridDataLocator,
        columnsWidth,
        columnsType,
        attributeDescriptions,
        topRows,
        bottomRows
      )
    );
    this.dateFormat = dateFormat;
    this.timeFormat = timeFormat;
    this.dateTimeFormat = dateTimeFormat;
    this.exportType = exportType;
  }


  /**
   * @return date format
   */
  public final String getDateFormat() {
    return dateFormat;
  }

  /**
   * @return time format
   */
  public final String getTimeFormat() {
    return timeFormat;
  }


  /**
   * @return date+time format
   */
  public final String getDateTimeFormat() {
    return dateTimeFormat;
  }


  /**
   * @return export format
   */
  public final String getExportType() {
    return exportType;
  }


  /**
   * @return list of component descriptors for exporting task
   */
  public final ArrayList getComponentsExportOptions() {
    return componentsExportOptions;
  }


  /**
   * Add a component descriptor for exporting task.
   */
  public final void addComponentExportOptions(ComponentExportOptions options) {
    this.componentsExportOptions.add(options);
  }


}
