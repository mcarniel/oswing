package org.openswing.springframework.web.servlet.utils;

import org.springframework.web.servlet.mvc.Controller;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.web.servlet.ModelAndView;
import org.openswing.springframework.web.servlet.view.OpenSwingViewResolver;
import java.util.ArrayList;
import org.openswing.swing.message.send.java.Command;
import org.openswing.springframework.web.servlet.handler.OpenSwingHandlerMapping;
import org.openswing.swing.export.java.ExportOptions;
import org.openswing.swing.table.java.GridDataLocator;
import org.openswing.swing.message.receive.java.Response;
import java.util.Map;
import org.openswing.swing.export.java.*;
import org.openswing.swing.message.receive.java.ErrorResponse;
import org.openswing.swing.message.receive.java.TextResponse;
import org.openswing.swing.message.send.java.GridParams;
import java.util.Hashtable;
import java.util.Enumeration;
import java.util.HashMap;
import org.openswing.swing.message.send.java.Command;
import org.springframework.context.ApplicationContext;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;


/**
 * <p>Title: OpenSwing Framework</p>
 * <p>Description: Spring Controller used to export data.</p>
 * <p>Copyright: Copyright (C) 2006 Mauro Carniel</p>
 * @version 1.0
 */
public class ExportController implements Controller {


  public ModelAndView handleRequest(HttpServletRequest request,HttpServletResponse response) {
    ModelAndView mav = new ModelAndView();
    Command command = (Command)request.getAttribute(OpenSwingHandlerMapping.COMMAND_ATTRIBUTE_NAME);
    ExportOptions opt = (ExportOptions)command.getInputParam();

    Object obj = null;
    for(int i=0;i<opt.getComponentsExportOptions().size();i++) {
      obj = opt.getComponentsExportOptions().get(i);
      if (obj instanceof GridExportOptions) {
        exportGrid((GridExportOptions)obj,command,request,response);
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
      ex.printStackTrace();
      new ErrorResponse(ex.getMessage());
    }
    request.getSession().getServletContext().setAttribute(docId,doc);


    mav.addObject(
      OpenSwingViewResolver.RESPONSE_PROPERTY_NAME,
      new TextResponse(docId)
    );
    return mav;
  }

  private void exportGrid(final GridExportOptions opt,final Command command,final HttpServletRequest request, final HttpServletResponse response) {
    ApplicationContext context = ((ApplicationContext)request.getAttribute("org.springframework.web.servlet.DispatcherServlet.CONTEXT"));
    final Controller controller = (Controller)context.getBean(opt.getServerMethodName());

    // redefine the grid data locator...
    GridDataLocator newDataLocator = new GridDataLocator() {

      public Response loadData(
          int gridAction,
          int startIndex,
          Map filteredColumns,
          ArrayList currentSortedColumns,
          ArrayList currentSortedVersusColumns,
          Class valueObjectType,
          Map otherGridParams) {
        request.setAttribute(
          OpenSwingHandlerMapping.COMMAND_ATTRIBUTE_NAME,new Command(
            command.getSessionId(),
            opt.getServerMethodName(),
            new GridParams(
               gridAction,
               startIndex,
               filteredColumns,
               currentSortedColumns,
               currentSortedVersusColumns,
               otherGridParams
            )
        ));
        try {
          return (Response) controller.handleRequest(request, response).getModel().get(OpenSwingViewResolver.RESPONSE_PROPERTY_NAME);
        }
        catch (Exception ex) {
          return new ErrorResponse(ex.toString());
        }
      }

    };
    opt.setGridDataLocator(newDataLocator);

  }



}
