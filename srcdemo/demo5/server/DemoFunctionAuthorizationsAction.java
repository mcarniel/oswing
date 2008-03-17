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
import org.openswing.swing.tree.java.OpenSwingTreeNode;


/**
 * <p>Title: OpenSwing Framework</p>
 * <p>Description: Action class used to retrieve authorizations associated to user, in terms of functions menu authorizations</p>
 * <p>Copyright: Copyright (C) 2006 Mauro Carniel</p>
 * <p> </p>
 * @author Mauro Carniel
 * @version 1.0
 */

public class DemoFunctionAuthorizationsAction implements Action {

  public DemoFunctionAuthorizationsAction() {
  }




  /**
   * @return request name
   */
  public final String getRequestName() {
    return "getFunctionAuthorizations";
  }


  /**
   * Business logic to execute.
   */
  public final Response executeCommand(Object inputPar,UserSessionParameters userSessionPars,HttpServletRequest request, HttpServletResponse response,HttpSession userSession,ServletContext context) {
    // retrieve internationalization settings (Resources object)...
    ResourcesFactory factory = (ResourcesFactory)context.getAttribute(Controller.RESOURCES_FACTORY);
    String langId = userSessionPars.getLanguageId();

    if (userSessionPars.getUsername().equals("ADMIN")) {
      // all grants...
      DefaultMutableTreeNode root = new OpenSwingTreeNode();
      DefaultTreeModel model = new DefaultTreeModel(root);
      ApplicationFunction n1 = new ApplicationFunction(factory.getResources(langId).getResource("folder1"),null);
      ApplicationFunction n2 = new ApplicationFunction(factory.getResources(langId).getResource("folder2"),null);
      ApplicationFunction n3 = new ApplicationFunction(factory.getResources(langId).getResource("folder3"),null);
      ApplicationFunction n11 = new ApplicationFunction(factory.getResources(langId).getResource("function1"),"F1",null,"getF1");
      ApplicationFunction n21 = new ApplicationFunction(factory.getResources(langId).getResource("function2"),"F2",null,"getF2");
      ApplicationFunction n22 = new ApplicationFunction(factory.getResources(langId).getResource("function3"),"F3",null,"getF3");
      ApplicationFunction n31 = new ApplicationFunction(factory.getResources(langId).getResource("folder31"),null);
      ApplicationFunction n311 = new ApplicationFunction(factory.getResources(langId).getResource("function4"),"F4",null,"getF4");
      ApplicationFunction n312 = new ApplicationFunction(factory.getResources(langId).getResource("function5"),"F5",null,"getF5");
      n1.add(n11);
      n2.add(n21);
      n2.add(n22);
      n3.add(n31);
      n31.add(n311);
      n31.add(n312);
      root.add(n1);
      root.add(n2);
      root.add(n3);

      return new VOResponse(model);
    }
    else {
      // limited grants...
      DefaultMutableTreeNode root = new OpenSwingTreeNode();
      DefaultTreeModel model = new DefaultTreeModel(root);
      ApplicationFunction n1 = new ApplicationFunction(factory.getResources(langId).getResource("function1"),"F1",null,"getF1");
      root.add(n1);

      return new VOResponse(model);
    }


  }


}
