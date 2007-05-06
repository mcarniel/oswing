package org.openswing.springframework.web.servlet.handler;

import org.springframework.web.servlet.handler.AbstractHandlerMapping;
import javax.servlet.http.HttpServletRequest;
import org.openswing.swing.message.send.java.Command;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;
import javax.servlet.http.HttpServletResponse;
import java.io.ObjectInputStream;
import org.openswing.swing.message.receive.java.ErrorResponse;
import java.io.ObjectOutputStream;
import java.util.HashSet;


/**
 * <p>Title: OpenSwing Framework</p>
 * <p>Description: Handler interceptor used to check if Command object contains a session identifier:
 * only HTTP requests having a session identifier are allowed to be processed (except when Command.getMethodName() = "login").</p>
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
public class SessionCheckInterceptor extends HandlerInterceptorAdapter {

  /** value of Command.getMethodName() that is recognized as a request to login, so it is always accepted */
  private String loginMethodName;


  public final boolean preHandle(
          HttpServletRequest request,
          HttpServletResponse response,
          Object handler) throws Exception {

    Command command = (Command)request.getAttribute(OpenSwingHandlerMapping.COMMAND_ATTRIBUTE_NAME);

    HashSet sessionIds = (HashSet)request.getSession().getServletContext().getAttribute(OpenSwingHandlerMapping.USERS_AUTHENTICATED);
    if (sessionIds==null) {
      sessionIds = new HashSet();
      request.getSession().getServletContext().setAttribute(OpenSwingHandlerMapping.USERS_AUTHENTICATED,sessionIds);
    }

    boolean ok =
        command!=null &&
        (command.getMethodName().equals(loginMethodName) || command.getSessionId()!=null && sessionIds.contains(command.getSessionId()));
    if (!ok) {
      ErrorResponse answer = new ErrorResponse("Cannot process the request: authentication needed!");
      ObjectOutputStream oos = new ObjectOutputStream(response.getOutputStream());
      oos.writeObject(answer);
      oos.close();
    }
    return ok;
  }


  /**
   * @return value of Command.getMethodName() that is recognized as a request to login, so it is always accepted
   */
  public final String getLoginMethodName() {
    return loginMethodName;
  }


  /**
   * Set the value of Command.getMethodName() that is recognized as a request to login, so it is always accepted.
   * @param loginMethodName value of Command.getMethodName() that is recognized as a request to login, so it is always accepted
   */
  public final void setLoginMethodName(String loginMethodName) {
    this.loginMethodName = loginMethodName;
  }


}


