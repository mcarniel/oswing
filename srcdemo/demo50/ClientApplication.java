package demo50;

import java.util.*;

import org.openswing.swing.internationalization.java.EnglishOnlyResourceFactory;
import org.openswing.swing.domains.java.*;
import java.sql.*;
import org.openswing.swing.util.client.*;
import org.openswing.swing.permissions.java.ButtonsAuthorizations;
import org.openswing.swing.internationalization.java.*;
import javax.swing.UIManager;
import javax.swing.*;
import java.awt.event.KeyEvent;
import org.openswing.swing.util.java.Consts;


/**
 * <p>Title: OpenSwing Demo</p>
 * <p>Description: Used to start application from main method:
 * it creates a detail frame where selecting a file: it shows how to read/store byte[] into BLOB oracle db field.</p>
 * <p>Copyright: Copyright (C) 2006 Mauro Carniel</p>
 * <p> </p>
 * @author Mauro Carniel
 * @version 1.0
 */
public class ClientApplication {


  Connection conn = null;



  public ClientApplication() {

    Hashtable domains = new Hashtable();
    Properties props = new Properties();
    props.setProperty("fileDescription","File");
    props.setProperty("code","Code");
    props.setProperty("file","File");

    ButtonsAuthorizations auth = new ButtonsAuthorizations();
    auth.addButtonAuthorization("F1",true,false,true);

    ClientSettings clientSettings = new ClientSettings(
        new EnglishOnlyResourceFactory("$",props,false),
        domains,
        auth
    );


    ClientSettings.VIEW_MANDATORY_SYMBOL = true;
    ClientSettings.FILTER_PANEL_ON_GRID = true;
    ClientSettings.VIEW_BACKGROUND_SEL_COLOR = true;
    ClientSettings.SHOW_SORTING_ORDER = true;
    ClientSettings.SHOW_FOCUS_BORDER_ON_FORM = false;
    ClientSettings.SHOW_NAVIGATOR_BAR_IN_LOOKUP = true;
    ClientSettings.AS_TAB = KeyStroke.getKeyStroke(KeyEvent.VK_ENTER,0);

    createConnection();

    new DetailFrameController(conn);
  }


  public static void main(String[] argv) {
    new ClientApplication();
  }


  /**
   * Create the database connection (using Hypersonic DB - in memory) and initialize tables...
   */
  private void createConnection() {
    try {
      Class.forName("oracle.jdbc.driver.OracleDriver");
      conn = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:XE","system","manager");
      PreparedStatement stmt = null;
      try {
        stmt = conn.prepareStatement("drop table DEMO50");
        stmt.execute();
        stmt.close();
      }
      catch (Exception ex3) {
      }
      try {
        stmt = conn.prepareStatement("create table DEMO50(CODE VARCHAR2(20),MYFILE BLOB DEFAULT EMPTY_BLOB(),FILE_DESCRIPTION VARCHAR2(255),PRIMARY KEY(CODE))");
        stmt.execute();
        stmt.close();

      }
      catch (SQLException ex1) {
        ex1.printStackTrace();
      }
      finally {
        try {
          stmt.close();
        }
        catch (Exception ex2) {
        }
      }

      conn.commit();

    }
    catch (Exception ex) {
      ex.printStackTrace();
    }
  }


}
