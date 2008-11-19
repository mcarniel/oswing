package org.openswing.swing.export.server;

import java.util.*;


import org.openswing.swing.export.java.*;
import org.openswing.swing.table.java.*;
import org.openswing.swing.message.receive.java.*;
import org.openswing.swing.message.send.java.*;
import javax.naming.InitialContext;
import javax.ejb.Stateless;


/**
 * <p>Title: OpenSwing Framework</p>
 * <p>Description: Server side class used to perform the grid data export,
 * starting from the name of an EJB stateless session bean.</p>
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
@Stateless
public class EJBExportBean implements EJBExportRemote {


  /** initial context  */
  InitialContext initialContext;


  /**
   * Business logic to execute.
   */
  public final Response export(ExportOptions opt) throws Exception {
    initialContext = new InitialContext();
    Object obj = null;
    GridExportOptions gridOptions = null;
    for(int i=0;i<opt.getComponentsExportOptions().size();i++) {
      obj = opt.getComponentsExportOptions().get(i);
      if (obj instanceof GridExportOptions) {
        // redefine the grid data locator...
        gridOptions = (GridExportOptions)obj;
        ((EJBGridDataLocator)(gridOptions.getGridDataLocator())).setInitialContext(initialContext);
        gridOptions.getOtherGridParams().put("LOAD_ALL",Boolean.TRUE);
      }
    }

    byte[] doc = null;
    String docId = null;
    try {
      if (opt.getExportType().equals(opt.XLS_FORMAT)) {
        // generate the Excel document...
        doc = new ExportToExcel().getDocument(opt);
        // generate and return the document identifier...
        docId = "doc"+System.currentTimeMillis()+".xls";
      }
      else if (opt.getExportType().equals(opt.CSV_FORMAT1) || opt.getExportType().equals(opt.CSV_FORMAT2) ) {
        // generate the CSV document...
        doc = new ExportToCSV().getDocument(opt);
        // generate and return the document identifier...
        docId = "doc"+System.currentTimeMillis()+".csv";
      }
      else if (opt.getExportType().equals(opt.XML_FORMAT)) {
        // generate the XML document...
        doc = new ExportToXML().getDocument(opt);
        // generate and return the document identifier...
        docId = "doc"+System.currentTimeMillis()+".xml";
      }
      else if (opt.getExportType().equals(opt.XML_FORMAT_FAT)) {
        // generate the XML document...
        doc = new ExportToXMLFat().getDocument(opt);
        // generate and return the document identifier...
        docId = "doc"+System.currentTimeMillis()+".xml";
      }
      else if (opt.getExportType().equals(opt.HTML_FORMAT)) {
        // generate the HTML document...
        doc = new ExportToHTML().getDocument(opt);
        // generate and return the document identifier...
        docId = "doc"+System.currentTimeMillis()+".html";
      }
      else if (opt.getExportType().equals(opt.PDF_FORMAT)) {
        // generate the PDF document...
        doc = new ExportToPDF().getDocument(opt);
        // generate and return the document identifier...
        docId = "doc"+System.currentTimeMillis()+".pdf";
      }
      else if (opt.getExportType().equals(opt.RTF_FORMAT)) {
        // generate the RTF document...
        doc = new ExportToRTF().getDocument(opt);
        // generate and return the document identifier...
        docId = "doc"+System.currentTimeMillis()+".rtf";
      }
    }
    catch (Throwable ex) {
      new ErrorResponse(ex.getMessage());
    }
    return new BytesResponse(doc,docId);
  }


  /**
   * Set the InitialContext.
   * @param initialContext InitialContext to set
   */
  public void setInitialContext(InitialContext initialContext) {
    this.initialContext = initialContext;
  }





}
