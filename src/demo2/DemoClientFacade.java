package demo2;

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

  private Connection conn = null;

  public DemoClientFacade(Connection conn) {
    this.conn = conn;
  }



  public void getF1() {
    new GridFrameController(conn);
  }


  public void getF2() {
    InternalFrame f = new InternalFrame();
    f.setSize(300,200);
    f.setTitle("Function2");
    MDIFrame.add(f);
  }


  public void getF3() {
    InternalFrame f = new InternalFrame();
    f.setSize(300,200);
    f.setTitle("Function3");
    MDIFrame.add(f);
  }


  public void getF4() {
    InternalFrame f = new InternalFrame();
    f.setSize(300,200);
    f.setTitle("Function4");
    MDIFrame.add(f);
  }


  public void getF5() {
    InternalFrame f = new InternalFrame();
    f.setSize(300,200);
    f.setTitle("Function5");
    MDIFrame.add(f);
  }

}