package demo12;

import java.util.*;

import org.openswing.swing.internationalization.java.EnglishOnlyResourceFactory;
import org.openswing.swing.util.client.*;
import org.openswing.swing.internationalization.java.*;


/**
 * <p>Title: OpenSwing Demo</p>
 * <p>Description: Used to start application from main method:
 * it creates a tree frame where nodes are draggable.</p>
 * <p>Copyright: Copyright (C) 2006 Mauro Carniel</p>
 * <p> </p>
 * @author Mauro Carniel
 * @version 1.0
 */
public class ClientApplication {


  public ClientApplication() {

    Hashtable domains = new Hashtable();
    Properties props = new Properties();
    props.put("rename node","Rename node");
    props.put("new description:","New description:");
    props.put("move node","Move node");
    props.put("copy node","Copy node");
    props.put("cancel","Cancel");
    props.put("node dropped","Node dropped");
    props.put("which operation?","Which operation?");

    ClientSettings clientSettings = new ClientSettings(
        new EnglishOnlyResourceFactory("£",props,true),
        domains
    );

    new TreeFrameController();
  }


  public static void main(String[] argv) {
    new ClientApplication();
  }





}
