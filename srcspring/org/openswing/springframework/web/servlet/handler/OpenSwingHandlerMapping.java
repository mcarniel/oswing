package org.openswing.springframework.web.servlet.handler;

import org.springframework.web.servlet.handler.AbstractHandlerMapping;
import javax.servlet.http.HttpServletRequest;
import java.io.ObjectInputStream;
import org.openswing.swing.message.send.java.Command;
import org.springframework.web.servlet.HandlerExecutionChain;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.ui.ModelMap;
import javax.servlet.http.HttpServletResponse;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;
import org.springframework.web.servlet.ModelAndView;
import org.openswing.swing.message.receive.java.Response;
import org.openswing.springframework.web.servlet.view.OpenSwingViewResolver;


/**
 * <p>Title: OpenSwing Framework</p>
 * <p>Description: Handler mapping used when combining OpenSwing client layer to Spring server layer:
 * this handler manages HTTP requests by giving back a bean name according to Command.getMethodName() value.
 * It is similar to ControllerClassNameHandlerMapping.
 * It always includes an Interceptor (in first position) that extract Command object from the request input stream
 * and stores it as request attribute.</p>
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
public class OpenSwingHandlerMapping extends AbstractHandlerMapping {

  /** attribute name to store as request attribute for the Command object extract from the request input stream; default value = "command"; default value = COMMAND_ATTRIBUTE_NAME */
  public static final String COMMAND_ATTRIBUTE_NAME = "COMMAND_ATTRIBUTE_NAME";

  /** attribute name in ServletContext used to store an HashSet of correctly authenticated users (used in combination with SessionCheckInterceptor) */
  public static final String USERS_AUTHENTICATED = "USERS_AUTHENTICATED";

  /** interceptors cache */
  private HandlerInterceptor[] newinterceptors = null;


  /**
   * Initialize the specified interceptors, adapting them where necessary.
   * Clear interceptors cache.
   * @see #setInterceptors
   * @see #adaptInterceptor
   */
  protected final void initInterceptors() {
    super.initInterceptors();
    newinterceptors = null;
  }


  /**
   * Build a HandlerExecutionChain for the given handler, including applicable interceptors.
   * <p>The default implementation builds a standard HandlerExecutionChain with
   * the given handler and this handler mapping's common interceptors. Subclasses may
   * override this in order to extend/rearrange the list of interceptors.
   * The first interceptor is always the inner class "CommandInterceptor".
   * Finally fills in the interceptors cache.
   *
   * @param handler the resolved handler instance (never <code>null</code>)
   * @param request current HTTP request
   * @return the HandlerExecutionChain (never <code>null</code>)
   * @see #getAdaptedInterceptors()
   */
  protected final HandlerExecutionChain getHandlerExecutionChain(Object handler, HttpServletRequest request) {
    if (newinterceptors==null) {
      HandlerInterceptor[] interceptors = getAdaptedInterceptors();
      if (interceptors==null)
        interceptors = new HandlerInterceptor[0];
      newinterceptors = interceptors;
      if (interceptors.length==0 || !(interceptors[0] instanceof CommandInterceptor)) {
        newinterceptors = new HandlerInterceptor[interceptors.length+1];
        newinterceptors[0] = new CommandInterceptor();
        System.arraycopy(interceptors,0,newinterceptors,1,interceptors.length);
      }
    }
    return new HandlerExecutionChain(handler,newinterceptors);
  }


  /**
   * Look up a handler for the given request, returning <code>null</code> if no
   * specific one is found. This method is called by <code>getHandler<code>;
   * a <code>null</code> return value will lead to the default handler, if one is set.
   * @param request current HTTP request
   * @return the corresponding handler instance, or <code>null</code> if none found
   * @throws Exception if there is an internal error
   */
  protected final Object getHandlerInternal(HttpServletRequest request) throws Exception {
    // read the client request...
    Command command = (Command)request.getAttribute(COMMAND_ATTRIBUTE_NAME);
    if (command!=null)
      return command.getMethodName();

    ObjectInputStream ois = new ObjectInputStream(request.getInputStream());
    command = (Command) ois.readObject();
    ois.close();
    request.setAttribute(COMMAND_ATTRIBUTE_NAME,command);
    return command.getMethodName();
  }


  /**
   * <p>Title: OpenSwing Framework</p>
   * <p>Description: Inner class used to include the session identifier in the response.</p>
   * <p>Copyright: Copyright (C) 2006 Mauro Carniel</p>
   * @version 1.0
   */
  class CommandInterceptor extends HandlerInterceptorAdapter {


    /**
     * Set session identifier to the answer.
     */
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView model) throws Exception {
      Command command = (Command)request.getAttribute(COMMAND_ATTRIBUTE_NAME);

      Object answer = model.getModel().get(OpenSwingViewResolver.RESPONSE_PROPERTY_NAME);
      if (answer==null)
        answer = model.getModel().values().iterator().next();
      if (answer!=null && answer instanceof Response) {
        String sessionId = command.getSessionId();
        if (sessionId==null)
          sessionId = ""+System.currentTimeMillis()+Math.random();
        if (((Response)answer).getSessionId()==null)
          ((Response)answer).setSessionId(sessionId);
        model.setView(new OpenSwingViewResolver());
      }

    }


  }


}
