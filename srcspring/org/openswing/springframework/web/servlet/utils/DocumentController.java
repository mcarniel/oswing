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
import java.io.*;
import javax.servlet.ServletContext;


/**
 * <p>Title: OpenSwing Framework</p>
 * <p>Description: Spring Controller used to returna document previously stored in user session.</p>
 * <p>Copyright: Copyright (C) 2006 Mauro Carniel</p>
 * @version 1.0
 */
public class DocumentController implements Controller {


  public ModelAndView handleRequest(final HttpServletRequest request, final HttpServletResponse response) {
    ModelAndView mav = new ModelAndView();
    final Command command = (Command)request.getAttribute(OpenSwingHandlerMapping.COMMAND_ATTRIBUTE_NAME);
    String docId = command.getInputParam().toString();
    byte[] bytes = (byte[])request.getSession().getServletContext().getAttribute(docId);
    new TimeoutDocIdThread(request.getSession().getServletContext(),docId);

    if (docId.endsWith(".xls")) {
      response.setContentType("application/vnd.ms-excel");
    }
    else if (docId.endsWith(".xls") || docId.endsWith(".csv")) {
      response.setContentType("application/vnd.ms-excel");
    }
    else if (docId.endsWith(".pdf")) {
      response.setContentType("application/pdf");
    }
    else if (docId.endsWith(".rtf")) {
      response.setContentType("application/rtf");
    }
    else if (docId.endsWith(".html")) {
      response.setContentType("text/html");
    }
    else if (docId.endsWith(".xml")) {
      response.setContentType("text/xml");
    }

    try {
      OutputStream out = response.getOutputStream();
      out.write(bytes);
      out.close();
    }
    catch (IOException ex) {
      return null;
    }

    return null;
  }


  /**
   * <p>Title: OpenSwing Framework</p>
   * <p>Description: Inner class< used to remove the document identifier from the context:
   * this is done after 5 minutes because some browser will call twice the same document request.</p>
   */
  class TimeoutDocIdThread extends Thread {

    /** document identifier to remove */
    private String docId;

    private ServletContext context = null;

    public TimeoutDocIdThread(ServletContext context,String docId) {
      this.context = context;
      this.docId = docId;
      start();
    }

    public void run() {
      try {
        sleep(300000);
      }
      catch (InterruptedException ex) {
      }
      try {
        context.removeAttribute(docId);
      }
      catch (Exception ex1) {
      }
    }

  }


}
