package org.openswing.swing.export.server;

import org.openswing.swing.server.*;
import javax.servlet.http.*;
import javax.servlet.ServletContext;
import org.openswing.swing.message.receive.java.*;
import org.openswing.swing.export.java.ExportOptions;
import org.openswing.swing.export.java.ExportToExcel;
import org.openswing.swing.table.java.GridDataLocator;
import java.util.*;
import org.openswing.swing.message.send.java.GridParams;
import org.openswing.swing.logger.server.Logger;


/**
 * <p>Title: OpenSwing Framework</p>
 * <p>Description: Action class used to perform the grid data export.</p>
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
public class ExportAction implements Action {

  public ExportAction() {
  }


  /**
   * @return request name
   */
  public final String getRequestName() {
    return "exportDataGrid";
  }


  /**
   * Business logic to execute.
   */
  public final Response executeCommand(Object inputPar,final UserSessionParameters userSessionPars,final HttpServletRequest request,final HttpServletResponse response,final HttpSession userSession,final ServletContext context) {
    final ExportOptions opt = (ExportOptions)inputPar;

    // retrive the grid action class...
    final ActionsCollection actions = (ActionsCollection)context.getAttribute(Controller.ACTION_CLASSES);
    final Action action = (Action)actions.get(opt.getServerMethodName());

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
        return action.executeCommand(
            new GridParams(
              gridAction,
              startIndex,
              filteredColumns,
              currentSortedColumns,
              currentSortedVersusColumns,
              otherGridParams
            ),
            userSessionPars,
            request,
            response,
            userSession,
            context
        );
      }

    };
    opt.setGridDataLocator(newDataLocator);

    // generate the Excel document...
    byte[] doc = null;
    try {
      doc = new ExportToExcel().getDocument(opt);
    }
    catch (Throwable ex) {
      Logger.error(
          userSessionPars.getUsername(),
          this.getClass().getName(),
          "executeCommand",
          "Error while exporting data:\n"+ex.getMessage(),
          ex
      );
      new ErrorResponse(ex.getMessage());
    }

    // generate and return the document identifier...
    String docId = "doc"+System.currentTimeMillis()+".xls";

    context.setAttribute(docId,doc);


    return new TextResponse(docId);


  }





}