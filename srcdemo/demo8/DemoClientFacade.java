package demo8;

import org.openswing.swing.mdi.client.*;

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


  /**
   * This method creates an internal frame that has inside a gantt control.
   */
  public void getGantt() {
    GanttFrame f = new GanttFrame();
    MDIFrame.add(f);
  }



}
