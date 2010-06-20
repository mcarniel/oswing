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

  /** export to PDF adapter */
  private ExportToPDFCallbacks exportToPDFAdapter = null;

  /** export to RTF adapter */
  private ExportToRTFCallbacks exportToRTFAdapter = null;


  /**
   * Method called by Grid.
   * @param gridOpts grid export options
   * @param dateFormat date format
   * @param timeFormat time format
   * @param dateTimeFormat date+time format
   * @param exportType export format
   * @param export to PDF adapter
   * @param export to RTF adapter
   */
  public ExportOptions(
      GridExportOptions gridOpts,
      String dateFormat,
      String timeFormat,
      String dateTimeFormat,
      String exportType,
      ExportToPDFCallbacks exportToPDFAdapter,
      ExportToRTFCallbacks exportToRTFAdapter
   ) {
    this.componentsExportOptions.add(gridOpts);
    this.dateFormat = dateFormat;
    this.timeFormat = timeFormat;
    this.dateTimeFormat = dateTimeFormat;
    this.exportType = exportType;
    this.exportToPDFAdapter = exportToPDFAdapter;
    this.exportToRTFAdapter = exportToRTFAdapter;
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


  /**
   * @return adapter for exporting to PDF format
   */
  public final ExportToPDFCallbacks getExportToPDFAdapter() {
    return exportToPDFAdapter;
  }


  /**
   * @return adapter for exporting to RTF format
   */
  public final ExportToRTFCallbacks getExportToRTFAdapter() {
    return exportToRTFAdapter;
  }


}
