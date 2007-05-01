package demo5.server;

import org.openswing.swing.server.*;
import javax.servlet.*;
import javax.servlet.http.*;
import java.io.*;
import java.util.*;
import org.openswing.swing.message.receive.java.*;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import org.openswing.swing.mdi.java.ApplicationFunction;
import org.openswing.swing.message.receive.java.UserAuthorizationsResponse;
import org.openswing.swing.permissions.java.ButtonsAuthorizations;
import org.openswing.swing.internationalization.java.ResourcesFactory;



/**
 * <p>Title: OpenSwing Framework</p>
 * <p>Description: Action class used to retrieve authorizations associated to user, in terms of buttons authorizations</p>
 * <p>Copyright: Copyright (C) 2006 Mauro Carniel</p>
 * <p> </p>
 * @author Mauro Carniel
 * @version 1.0
 */

public class DemoButtonAuthorizationsAction implements Action {

  public DemoButtonAuthorizationsAction() {
  }




  /**
   * @return request name
   */
  public final String getRequestName() {
    return "getButtonAuthorizations";
  }


  /**
   * Business logic to execute.
   */
  public final Response executeCommand(Object inputPar,UserSessionParameters userSessionPars,HttpServletRequest request, HttpServletResponse response,HttpSession userSession,ServletContext context) {
    if (userSessionPars.getUsername().equals("ADMIN")) {
      // all grants...
      ButtonsAuthorizations ba = new ButtonsAuthorizations();
      return new VOResponse(ba);
    }
    else {
      // limited grants...
      ButtonsAuthorizations ba = new ButtonsAuthorizations();
      ba.addButtonAuthorization("F1",false,true,false);
      return new VOResponse(ba);
    }


  }


}
