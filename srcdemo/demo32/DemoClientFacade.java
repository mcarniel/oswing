package demo32;

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


  public void newEmail() {
    InternalFrame f = new InternalFrame();
    f.setResizable(false);
    f.setIconifiable(false);
    f.setMaximizable(false);
    f.setSize(300,200);
    f.setTitle("Function1");
    MDIFrame.add(f,true);
  }


  public void replyToEmail() {
    InternalFrame f = new InternalFrame();
    f.setSize(300,200);
    f.setTitle("Function2");
    MDIFrame.add(f);
  }


  public void sendReceive() {
    InternalFrame f = new InternalFrame();
    f.setSize(300,200);
    f.setTitle("Function3");
    MDIFrame.add(f);
  }


  public void options() {
    InternalFrame f = new InternalFrame();
    f.setSize(300,200);
    f.setTitle("Function3");
    MDIFrame.add(f);
  }



}
