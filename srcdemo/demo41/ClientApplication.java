package demo41;

import java.util.*;

import org.openswing.swing.internationalization.java.EnglishOnlyResourceFactory;
import org.openswing.swing.domains.java.*;
import java.sql.*;
import org.openswing.swing.util.client.*;
import org.openswing.swing.permissions.java.ButtonsAuthorizations;
import org.openswing.swing.internationalization.java.*;
import javax.swing.UIManager;
import javax.swing.*;
import org.openswing.swing.table.profiles.client.FileGridProfileManager;


/**
 * <p>Title: OpenSwing Demo</p>
 * <p>Description: Used to start application from main method:
 * it creates a grid frame.
 * This demo shows how to use a ComboBoxVOColumn.</p>
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
    props.setProperty("units","Units");
    props.setProperty("units.id","Unit Id");
    props.setProperty("id","Product Id");
    props.setProperty("pname","Product Name");
    props.setProperty("units.unitName","Unit");
    props.setProperty("products","Products");


    ButtonsAuthorizations auth = new ButtonsAuthorizations();
    auth.addButtonAuthorization("F1",true,true,true);

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
      PreparedStatement stmt = null;
      try {
        stmt = conn.prepareStatement("create table PRODUCTS(ID NUMERIC,PNAME VARCHAR,ID_UNIT NUMERIC,PRIMARY KEY(ID))");
        stmt.execute();
        stmt.close();

        stmt = conn.prepareStatement("create table UNITS(ID NUMERIC,UNIT_NAME VARCHAR,PRIMARY KEY(ID))");
        stmt.execute();
        stmt.close();

        stmt = conn.prepareStatement("insert into UNITS values(1,'PZ')");
        stmt.execute();
        stmt.close();
        stmt = conn.prepareStatement("insert into UNITS values(2,'MT')");
        stmt.execute();
        stmt.close();
        stmt = conn.prepareStatement("insert into UNITS values(3,'KG')");
        stmt.execute();
        stmt.close();
        stmt = conn.prepareStatement("insert into PRODUCTS values(1,'Milk',1)");
        stmt.execute();
        stmt.close();
        stmt = conn.prepareStatement("insert into PRODUCTS values(2,'Butter',1)");
        stmt.execute();
        stmt.close();
        stmt = conn.prepareStatement("insert into PRODUCTS values(3,'Sugar',2)");
        stmt.execute();
        stmt.close();
        stmt = conn.prepareStatement("insert into PRODUCTS values(4,'Steak',3)");
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


  private String getCode(int len,int num) {
    String code = String.valueOf(num);
    for(int i=code.length();i<len;i++)
      code = "0"+code;
    return code;
  }


}
