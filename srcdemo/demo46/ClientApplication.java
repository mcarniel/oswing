package demo46;

import java.util.*;

import org.openswing.swing.internationalization.java.EnglishOnlyResourceFactory;
import org.openswing.swing.domains.java.*;
import java.sql.*;
import org.openswing.swing.util.client.*;
import org.openswing.swing.internationalization.java.*;


/**
 * <p>Title: OpenSwing Demo</p>
 * <p>Description: Used to start application from main method:
 * it creates an editable grid frame whose value object is of CustomValueObject type and whose content is fetched
 * starting from a SQL script defined at run time.
 * It shows also how to render images inside a button column.</p>
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
    props.setProperty("sql to execute to fill in the grid","SQL to execute to fill in the grid");
    props.setProperty("load data","Load data");
    props.setProperty("dynamic grid","Dynamic Grid");
    props.setProperty("filter panel","Filter Panel");
    ClientSettings clientSettings = new ClientSettings(
        new EnglishOnlyResourceFactory("£",props,false),
        domains
    );
    ClientSettings.ALLOW_OR_OPERATOR = false;
    ClientSettings.INCLUDE_IN_OPERATOR = false;

    createConnection();

    new FilterFrame(conn);
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
        stmt = conn.prepareStatement("create table ORDERS(YEAR NUMERIC(4),ORDER_NR NUMERIC(4),CUSTOMER VARCHAR(20),ORDER_DATE DATE,TOTAL_AMOUNT DECIMAL(20,2),PRIMARY KEY(YEAR,ORDER_NR))");
        stmt.execute();

        for(int i=0;i<200;i++) {
          stmt.close();
          stmt = conn.prepareStatement("insert into ORDERS values(2008,"+(i+1)+",'CUST"+i+"',?,"+(1200+i*10)+")");
          stmt.setObject(1,new java.sql.Date(System.currentTimeMillis()-86400000*i));
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
