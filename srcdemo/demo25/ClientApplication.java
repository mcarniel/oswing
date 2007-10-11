package demo25;

import java.util.*;

import org.openswing.swing.internationalization.java.EnglishOnlyResourceFactory;
import org.openswing.swing.domains.java.*;
import java.sql.*;
import org.openswing.swing.util.client.*;
import org.openswing.swing.permissions.java.ButtonsAuthorizations;
import org.openswing.swing.internationalization.java.*;
import javax.swing.UIManager;
import javax.swing.*;


/**
 * <p>Title: OpenSwing Demo</p>
 * <p>Description: Used to start application from main method:
 * it creates a grid frame and a detail frame accessed by double click on the grid.
 * Both on grid and detail it is showed a progress bar.</p>
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
    props.setProperty("population","Polulation");
    props.setProperty("city","City");
    props.setProperty("state","State");
    props.setProperty("population at ","Population at ");

    ButtonsAuthorizations auth = new ButtonsAuthorizations();
    auth.addButtonAuthorization("F1",true,false,true);

    ClientSettings clientSettings = new ClientSettings(
        new EnglishOnlyResourceFactory("£",props,false),
        domains,
        auth
    );
    ClientSettings.VIEW_MANDATORY_SYMBOL = true;
    ClientSettings.FILTER_PANEL_ON_GRID = true;
    ClientSettings.VIEW_BACKGROUND_SEL_COLOR = true;
    ClientSettings.SHOW_SORTING_ORDER = true;
//    ClientSettings.LOOK_AND_FEEL_CLASS_NAME = "com.jgoodies.looks.plastic.PlasticXPLookAndFeel";
//com.jgoodies.looks.plastic.PlasticLookAndFeel
//com.jgoodies.looks.plastic.Plastic3DLookAndFeel

    createConnection();

    new GridFrameController(conn);
  }


  public static void main(String[] argv) {
    new ClientApplication();
  }


  /**
   * Create the database connection (using Hypersonic DB - in memory) and initialize tables...
   */
  private void createConnection() {
    try {
      Class.forName("org.hsqldb.jdbcDriver");
      conn = DriverManager.getConnection("jdbc:hsqldb:mem:"+"a"+Math.random(),"sa","");
      Statement stmt = null;
      try {
        stmt = conn.createStatement();
        stmt.execute("create table DEMO25(CITY VARCHAR,STATE VARCHAR,POPULATION DECIMAL(15,0),PRIMARY KEY(CITY))");
        stmt.execute("insert into DEMO25 values('Washington','District of Columbia',3730000)");
        stmt.execute("insert into DEMO25 values('Atlanta','Georgia',2280000)");
        stmt.execute("insert into DEMO25 values('Boston','Massachusetts',2820000)");
        stmt.execute("insert into DEMO25 values('Chicago','Illinois',8116000)");
        stmt.execute("insert into DEMO25 values('Dallas','Texas',3650000)");
        stmt.execute("insert into DEMO25 values('Detroit','Michigan',4600000)");
        stmt.execute("insert into DEMO25 values('Houston','Texas',3600000)");
        stmt.execute("insert into DEMO25 values('Los Angeles','California',8300000)");
        stmt.execute("insert into DEMO25 values('New York','New York',8470000)");
        stmt.execute("insert into DEMO25 values('San Francisco','California',5880000)");
        stmt.execute("insert into DEMO25 values('San Diego','California',2220000)");
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
