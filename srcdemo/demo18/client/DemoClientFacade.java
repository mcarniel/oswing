package demo18.client;

import org.openswing.swing.mdi.client.*;
import java.sql.Connection;

/**
 * <p>Title: OpenSwing Framework</p>
 * <p>Description: Client Facade, called by the MDI Tree.</p>
 * <p>Copyright: Copyright (C) 2006 Mauro Carniel</p>
 * <p> </p>
 * @author Mauro Carniel
 * @version 1.0
 */
public class DemoClientFacade implements ClientFacade {


  public DemoClientFacade() {
  }



  public void getEmployees() {
    new EmpGridFrameController();
  }


  public void getDepts() {
    new DeptFrameController();
  }


  public void getTasks() {
    new TaskGridFrameController();
  }




}
