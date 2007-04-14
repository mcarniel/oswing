package demo11;

import java.util.*;

import org.openswing.swing.internationalization.java.EnglishOnlyResourceFactory;
import org.openswing.swing.domains.java.*;
import org.openswing.swing.util.client.*;
import org.openswing.swing.internationalization.java.*;


/**
 * <p>Title: OpenSwing Demo</p>
 * <p>Description: Used to start application from main method:
 * it creates a tree-table frame.</p>
 * <p>Copyright: Copyright (C) 2006 Mauro Carniel</p>
 * <p> </p>
 * @author Mauro Carniel
 * @version 1.0
 */
public class ClientApplication {


  public ClientApplication() {

    Hashtable domains = new Hashtable();
    Properties props = new Properties();
    props.put("itemCode","Item Code");
    props.put("description","Description");
    props.put("price","Price");
    props.put("qty","Qty");
    ClientSettings clientSettings = new ClientSettings(
        new EnglishOnlyResourceFactory("£",props,false),
        domains
    );

    new TreeGridFrameController();
  }


  public static void main(String[] argv) {
    new ClientApplication();
  }






}
