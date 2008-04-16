package demo36;

import org.openswing.swing.mdi.client.*;
import org.apache.cayenne.access.DataContext;


/**
 * <p>Title: OpenSwing Framework</p>
 * <p>Description: Client Facade, called by the MDI Tree.</p>
 * <p>Copyright: Copyright (C) 2006 Mauro Carniel</p>
 * <p> </p>
 * @author Mauro Carniel
 * @version 1.0
 */
public class DemoClientFacade implements ClientFacade {

  private DataContext context = null;

  public DemoClientFacade(DataContext context) {
    this.context = context;
  }


  public void getTasks() {
    new TaskGridFrameController(context);
  }


  public void getDepts() {
    new DeptGridFrameController(context);
  }


  public void getEmps() {
    new EmpGridFrameController(context);
  }

}
