package demo18.server;

import org.springframework.web.servlet.mvc.Controller;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.web.servlet.ModelAndView;
import org.openswing.springframework.web.servlet.view.OpenSwingViewResolver;
import java.util.ArrayList;
import org.openswing.swing.message.send.java.Command;
import org.openswing.springframework.web.servlet.handler.OpenSwingHandlerMapping;
import java.util.Map;
import org.openswing.swing.message.receive.java.*;
import java.util.HashSet;


/**
 * <p>Title: OpenSwing Framework</p>
 * <p>Description: Spring Controller used to check login data.</p>
 * <p>Copyright: Copyright (C) 2006 Mauro Carniel</p>
 * @version 1.0
 */
public class LoginController implements Controller {

  private String username;
  private String password;


  public ModelAndView handleRequest(HttpServletRequest request, HttpServletResponse response) {
    ModelAndView mav = new ModelAndView();
    Command command = (Command)request.getAttribute(OpenSwingHandlerMapping.COMMAND_ATTRIBUTE_NAME);
    Map credentials = (Map)command.getInputParam();
    Response res = null;
    if (credentials.get("username").equals(username) &&
        credentials.get("password").equals(password)) {
      res = new VOResponse(Boolean.TRUE);
      String sessionId = ""+System.currentTimeMillis()+Math.random();
      HashSet sessionIds = (HashSet)request.getSession().getServletContext().getAttribute(OpenSwingHandlerMapping.USERS_AUTHENTICATED);
      if (sessionIds==null) {
        sessionIds = new HashSet();
        request.getSession().getServletContext().setAttribute(OpenSwingHandlerMapping.USERS_AUTHENTICATED,sessionIds);
      }
      sessionIds.add(sessionId);

      res.setSessionId(sessionId);
    }
    else
      res = new ErrorResponse("Authentication failed!");

    mav.addObject(
      OpenSwingViewResolver.RESPONSE_PROPERTY_NAME,
      res
    );

    return mav;

  }


  public void setPassword(String password) {
    this.password = password;
  }
  public void setUsername(String username) {
    this.username = username;
  }
  public String getPassword() {
    return password;
  }
  public String getUsername() {
    return username;
  }


}
