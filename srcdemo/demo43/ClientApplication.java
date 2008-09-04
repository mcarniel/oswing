package demo43;

import java.util.*;

import org.openswing.swing.internationalization.java.EnglishOnlyResourceFactory;
import org.openswing.swing.util.client.*;
import org.openswing.swing.internationalization.java.*;


/**
 * <p>Title: OpenSwing Demo</p>
 * <p>Description: Used to start application from main method:
 * it creates an editable grid frame for uploading/downloding of files.</p>
 * <p>Copyright: Copyright (C) 2006 Mauro Carniel</p>
 * <p> </p>
 * @author Mauro Carniel
 * @version 1.0
 */
public class ClientApplication {


  public ClientApplication() {

//    java.util.Enumeration en = System.getProperties().keys();
//    while(en.hasMoreElements()) {
//      String k = en.nextElement().toString();
//      System.out.println(k+"="+System.getProperty(k));
//    }

    Hashtable domains = new Hashtable();
    Properties props = new Properties();
    props.setProperty("file","File");
    props.setProperty("uploadDate","Upload Date");

    ClientSettings clientSettings = new ClientSettings(
        new EnglishOnlyResourceFactory("£",props,true),
        domains
    );

    new FileGridFrameController();
  }


  public static void main(String[] argv) {
    new ClientApplication();
  }



}
