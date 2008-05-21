package demo24;

import java.util.*;

import org.openswing.swing.internationalization.java.EnglishOnlyResourceFactory;
import org.openswing.swing.domains.java.*;
import java.sql.*;
import org.openswing.swing.util.client.*;
import org.openswing.swing.internationalization.java.*;


/**
 * <p>Title: OpenSwing Demo</p>
 * <p>Description: Used to start application from main method:
 * it creates an editable grid frame with colored cells and cells span.
 * Grid control includes a lookup column having a lookup controller with "onInvalidCode" property setted to LookupControoller.ON_INVALID_CODE_RESTORE_FOCUS.</p>
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
    props.setProperty("this text will be translated","This text will be translated");
    props.setProperty("date","Date");
    props.setProperty("combobox","Combobox");
    props.setProperty("opened","Opened");
    props.setProperty("suspended","Suspended");
    props.setProperty("delivered","Delivered");
    props.setProperty("closed","Closed");
    props.setProperty("radio button","Radio Button");
    props.setProperty("stringValue","Text");
    props.setProperty("dateValue","Date");
    props.setProperty("checkValue","CheckBox");
    props.setProperty("radioButtonValue","RadioButton");
    props.setProperty("comboValue","ComboBox");
    props.setProperty("currencyValue","Currency");
    props.setProperty("numericValue","Number");
    props.setProperty("lookupValue","Lookup Code");
    props.setProperty("descrLookupValue","Lookup Description");
    props.setProperty("button","Button");
    props.setProperty("formattedTextValue","Formatted Text");
    props.setProperty("intValue","Integer Value");
    props.setProperty("multiLineTextValue","Multi Line Text Value");
    props.setProperty("...","...");
    ClientSettings clientSettings = new ClientSettings(
        new EnglishOnlyResourceFactory("£",props,true),
        domains
    );

    Domain orderStateDomain = new Domain("ORDERSTATE");
    orderStateDomain.addDomainPair("O","opened");
    orderStateDomain.addDomainPair("S","suspended");
    orderStateDomain.addDomainPair("D","delivered");
    orderStateDomain.addDomainPair("ABC","closed");
    domains.put(
      orderStateDomain.getDomainId(),
      orderStateDomain
    );
    ClientSettings.ALLOW_OR_OPERATOR = false;
    ClientSettings.INCLUDE_IN_OPERATOR = false;

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
        stmt = conn.prepareStatement("create table DEMO24(TEXT VARCHAR,FORMATTED_TEXT VARCHAR,DECNUM DECIMAL(10,2),CURRNUM DECIMAL(10,2),THEDATE DATE,COMBO VARCHAR,CHECK_BOX CHAR(1),RADIO CHAR(1),CODE VARCHAR,PROGRESSIVE DECIMAL(5,0),INT_VALUE NUMERIC(6),PRIMARY KEY(TEXT))");
        stmt.execute();
        stmt.close();

        stmt = conn.prepareStatement("create table DEMO24_LOOKUP(PROGRESSIVE DECIMAL(15,0),CODE VARCHAR,DESCRCODE VARCHAR,PRIMARY KEY(PROGRESSIVE))");
        stmt.execute();

        for(int i=0;i<50;i++) {
          stmt.close();
          stmt = conn.prepareStatement("insert into DEMO24 values('ABC"+i+"','"+(i%9)+"23-22-4444',"+12+i+0.33+","+1234+i+0.560+",?,'ABC','Y','Y','A"+i+"',"+i+","+i+")");
          stmt.setObject(1,new java.sql.Date(System.currentTimeMillis()+86400000*i));
          stmt.execute();
        }

        for(int i=0;i<900;i=i+1) {
          stmt.close();
          stmt = conn.prepareStatement("insert into DEMO24_LOOKUP values("+i+",'A"+i+"','ABCDEF"+String.valueOf(i)+"')");
          stmt.execute();
          stmt.close();
          if (i%2==0) {
            stmt = conn.prepareStatement("insert into DEMO24_LOOKUP values("+(i+1)+",'A"+i+"','ABCDEF"+String.valueOf(i+1)+"')");
            i++;
          }
          stmt.execute();
        }

      }
      catch (SQLException ex1) {
        ex1.printStackTrace();
      }
      finally {
        try {
          stmt.close();
        }
        catch (SQLException ex2) {
        }
      }

      conn.commit();

    }
    catch (Exception ex) {
      ex.printStackTrace();
    }
  }




}
