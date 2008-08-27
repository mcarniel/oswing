package org.openswing.swing.server;

import java.util.*;
import javax.servlet.*;
import javax.servlet.http.*;

import org.openswing.swing.message.receive.java.*;


/**
 * <p>Title: OpenSwing Framework</p>
 * <p>Description: Action class for login operation: it does nothing, login is always successfully executed.</p>
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
public class NoLoginAction extends LoginAction {



  /**
   * Login operation.
   */
  public Response executeCommand(Object inputPar,UserSessionParameters userSessionPars,HttpServletRequest request, HttpServletResponse response,HttpSession userSession,ServletContext context) {
    Response r = new VOResponse(new Boolean(true));
    SessionIdGenerator generator = (SessionIdGenerator)context.getAttribute(Controller.SESSION_ID_GENERATOR);
    r.setSessionId(generator.getSessionId(request,response,userSession,context));

    Hashtable userSessions = (Hashtable)context.getAttribute(Controller.USER_SESSIONS);
    HashSet authenticatedIds = (HashSet)context.getAttribute(Controller.SESSION_IDS);
    if (userSessionPars!=null) {
      userSessions.remove(userSessionPars.getSessionId());
      authenticatedIds.remove(userSessionPars.getSessionId());
    }
    userSessionPars = new UserSessionParameters();
    userSessionPars.setSessionId(r.getSessionId());
    userSessionPars.setUsername("UNDEFINED");
    userSessions.put(r.getSessionId(),userSessionPars);
    userSessionPars.setLanguageId("UNDEFINED");

    authenticatedIds.add(r.getSessionId());

    return r;
  }


}
