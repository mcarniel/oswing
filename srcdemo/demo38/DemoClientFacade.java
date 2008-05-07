package demo38;

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



  public void getUsers() {
    new UsersGridFrameController(conn);
  }



}
