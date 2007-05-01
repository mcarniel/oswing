package demo17;

import org.openswing.swing.mdi.client.*;
import org.hibernate.SessionFactory;


/**
 * <p>Title: OpenSwing Framework</p>
 * <p>Description: Client Facade, called by the MDI Tree.</p>
 * <p>Copyright: Copyright (C) 2006 Mauro Carniel</p>
 * <p> </p>
 * @author Mauro Carniel
 * @version 1.0
 */
public class DemoClientFacade implements ClientFacade {

  private SessionFactory sessions = null;

  public DemoClientFacade(SessionFactory sessions) {
    this.sessions = sessions;
  }


  public void getTasks() {
    new TaskGridFrameController(sessions);
  }




}
