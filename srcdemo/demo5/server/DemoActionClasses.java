package demo5.server;

import org.openswing.swing.server.ActionsCollection;
import org.openswing.swing.server.Action;

/**
 * <p>Title: OpenSwing Framework</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (C) 2006 Mauro Carniel</p>
 * <p> </p>
 * @author Mauro Carniel
 * @version 1.0
 */

public class DemoActionClasses extends ActionsCollection {

  public DemoActionClasses() {
    Action a = null;
    a = new DemoButtonAuthorizationsAction(); put(a.getRequestName(),a);
    a = new DemoFunctionAuthorizationsAction(); put(a.getRequestName(),a);
    a = new DemoLoginAction(); put(a.getRequestName(),a);
    a = new DetailDeleteAction(); put(a.getRequestName(),a);
    a = new DetailInsertAction(); put(a.getRequestName(),a);
    a = new DetailLoadAction(); put(a.getRequestName(),a);
    a = new DetailUpdateAction(); put(a.getRequestName(),a);
    a = new GridDeleteAction(); put(a.getRequestName(),a);
    a = new GridLoadAction(); put(a.getRequestName(),a);
    a = new LookupGridAction(); put(a.getRequestName(),a);
    a = new LookupValidationAction(); put(a.getRequestName(),a);
  }


}