package demo5.server;

import org.openswing.swing.server.*;
import javax.servlet.*;
import javax.servlet.http.*;
import java.io.*;
import java.util.*;
import org.openswing.swing.message.receive.java.*;
import java.sql.*;


/**
 * <p>Title: OpenSwing Framework</p>
 * <p>Description: Logic Action Class.</p>
 * <p>Copyright: Copyright (C) 2006 Mauro Carniel</p>
 * <p> </p>
 * @author Mauro Carniel
 * @version 1.0
 */

public class DemoLoginAction extends LoginAction {


  public DemoLoginAction() {
  }


  /**
   * Login operation.
   */
  public final Response executeCommand(Object inputPar,UserSessionParameters userSessionPars,HttpServletRequest request, HttpServletResponse response,HttpSession userSession,ServletContext context) {
    PreparedStatement stmt = null;
    Connection conn = null;
    try {
      String username = ((String[])inputPar)[0];
      String password = ((String[])inputPar)[1];

      conn = ConnectionManager.getConnection(context);
      stmt = conn.prepareStatement("select LANGUAGE_ID from DEMO5_USERS where USERNAME='"+username.toUpperCase()+"' and PASSWORD='"+password.toUpperCase()+"'");
      ResultSet rset = stmt.executeQuery();
      String languageId = null;
      if (rset.next()) {

        languageId = rset.getString(1);
        TextResponse tr = new TextResponse(languageId);
        SessionIdGenerator gen = (SessionIdGenerator)context.getAttribute(Controller.SESSION_ID_GENERATOR);
        tr.setSessionId(gen.getSessionId(request,response,userSession,context));

        Hashtable userSessions = (Hashtable)context.getAttribute(Controller.USER_SESSIONS);
        HashSet authenticatedIds = (HashSet)context.getAttribute(Controller.SESSION_IDS);
        if (userSessionPars!=null) {
          userSessions.remove(userSessionPars.getSessionId());
          authenticatedIds.remove(userSessionPars.getSessionId());
        }
        userSessionPars = new UserSessionParameters();
        userSessionPars.setSessionId(tr.getSessionId());
        userSessionPars.setUsername(username);
        userSessions.put(tr.getSessionId(),userSessionPars);
        userSessionPars.setLanguageId(languageId);

        authenticatedIds.add(tr.getSessionId());

        return tr;
        }
      else
        return new ErrorResponse("User not valid");
    } catch (Exception ex1) {
      ex1.printStackTrace();
      return new ErrorResponse(ex1.getMessage());
    } finally {
      try {
        stmt.close();
      }
      catch (Exception ex) {
      }
      try {
        ConnectionManager.releaseConnection(conn,context);
      }
      catch (Exception ex2) {
      }
    }

  }


}