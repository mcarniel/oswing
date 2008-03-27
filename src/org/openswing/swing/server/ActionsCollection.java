package org.openswing.swing.server;

import java.util.*;
import javax.servlet.*;
import javax.servlet.http.*;

import org.openswing.swing.export.server.*;
import org.openswing.swing.message.receive.java.*;


/**
 * <p>Title: OpenSwing Framework</p>
 * <p>Description: This is the collection of Action classes.
 * This class is initialized by the server controller (class Controller).</p>
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
public class ActionsCollection extends Hashtable {


  public ActionsCollection() {
    put("changeLanguage",new Action() {

      public String getRequestName() {
        return "changeLanguage";
      }

      public Response executeCommand(Object inputPar,UserSessionParameters userSessionPars,HttpServletRequest request, HttpServletResponse response,HttpSession userSession,ServletContext context) {
        userSessionPars.setLanguageId(inputPar.toString());
        return new VOResponse(Boolean.TRUE);
      }

    });


    put("closeApplication",new Action() {

      public String getRequestName() {
        return "closeApplication";
      }

      public Response executeCommand(Object inputPar,UserSessionParameters userSessionPars,HttpServletRequest request, HttpServletResponse response,HttpSession userSession,ServletContext context) {
        HashSet authenticatedIds = (HashSet)context.getAttribute(Controller.SESSION_IDS);
        authenticatedIds.remove(userSessionPars.getSessionId());
        Hashtable userSessions = (Hashtable)context.getAttribute(Controller.USER_SESSIONS);
        userSessions.remove(userSessionPars.getSessionId());

        return new VOResponse(Boolean.TRUE);
      }

    });


    put("databaseAlreadyExixts",new Action() {

      public final String getRequestName() {
        return "databaseAlreadyExixts";
      }

      public final Response executeCommand(Object inputPar,UserSessionParameters userSessionPars,HttpServletRequest request, HttpServletResponse response,HttpSession userSession,ServletContext context) {
        return new VOResponse(new Boolean(ConnectionManager.isConnectionSourceCreated()));
      }

    });


    put("exportDataGrid",new ExportAction());
  }



}
