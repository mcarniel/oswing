package demo27;

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
    props.setProperty("customers","Customers");
    props.setProperty("customerCode","Customer Code");
    props.setProperty("address","Address");
    props.setProperty("city","City");
    props.setProperty("zipCode","Zip Code");
    props.setProperty("name","Name");
    props.setProperty("surname","Surname");
    props.setProperty("state","State");

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
        stmt = conn.prepareStatement("create table CUSTOMERS(CUSTOMER_CODE VARCHAR,NAME VARCHAR,SURNAME VARCHAR,CITY VARCHAR,ADDRESS VARCHAR,STATE VARCHAR,ZIP_CODE VARCHAR,PRIMARY KEY(CUSTOMER_CODE))");
        stmt.execute();
        stmt.close();

        stmt = conn.prepareStatement("create table CITIES(CITY VARCHAR,ZIP_CODE VARCHAR,STATE VARCHAR,PRIMARY KEY(CITY))");
        stmt.execute();
        stmt.close();

        stmt = conn.prepareStatement("insert into CITIES(CITY,ZIP_CODE,STATE) values('Pasiano','33087','Italy')");
        stmt.execute();
        stmt = conn.prepareStatement("insert into CITIES(CITY,ZIP_CODE,STATE) values('Pordenone','33170','Italy')");
        stmt.execute();
        stmt = conn.prepareStatement("insert into CITIES(CITY,ZIP_CODE,STATE) values('Keilalahdentie','02150','Finland')");
        stmt.execute();
        stmt = conn.prepareStatement("insert into CITIES(CITY,ZIP_CODE,STATE) values('Santa Clara','95054','California')");
        stmt.execute();
        stmt = conn.prepareStatement("insert into CUSTOMERS(CUSTOMER_CODE,NAME,SURNAME,CITY,ADDRESS,STATE,ZIP_CODE) values('C1','Mauro','Carniel','Pasiano','xxx','Italy','33087')");
        stmt.execute();
        stmt = conn.prepareStatement("insert into CUSTOMERS(CUSTOMER_CODE,NAME,SURNAME,CITY,ADDRESS,STATE,ZIP_CODE) values('C2','John','Doe','Santa Clara','12 Bond St.','California','95054')");
        stmt.execute();
        stmt = conn.prepareStatement("insert into CUSTOMERS(CUSTOMER_CODE,NAME,SURNAME,CITY,ADDRESS,STATE,ZIP_CODE) values('C3','William','Smith','Santa Clara','15 Fifth Av.','California','95054')");
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
