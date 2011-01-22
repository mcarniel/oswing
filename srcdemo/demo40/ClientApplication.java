package demo40;

import java.util.*;

import org.openswing.swing.internationalization.java.EnglishOnlyResourceFactory;
import org.openswing.swing.domains.java.*;
import java.sql.*;
import org.openswing.swing.util.client.*;
import org.openswing.swing.internationalization.java.*;


/**
 * <p>Title: OpenSwing Demo</p>
 * <p>Description: Used to start application from main method:
 * it creates an editable grid frame.</p>
 * <p>Copyright: Copyright (C) 2006 Mauro Carniel</p>
 * <p> </p>
 * @author Mauro Carniel
 * @version 1.0
 */
public class ClientApplication {


  Connection conn = null;



  public ClientApplication() {

    Hashtable domains = new Hashtable();

    Domain state = new Domain("ORDER_STATE");
    state.addDomainPair("O","Open");
    state.addDomainPair("C","Closed");
    domains.put(state.getDomainId(),state);

    Properties props = new Properties();
    props.setProperty("...","...");
    ClientSettings clientSettings = new ClientSettings(
        new EnglishOnlyResourceFactory("$",props,false),
        domains
    );
    ClientSettings.FILTER_PANEL_ON_GRID = true;

    ClientSettings.ALLOW_OR_OPERATOR = false;
    ClientSettings.INCLUDE_IN_OPERATOR = false;

    createConnection();


    new OrdersGridFrameController(conn);
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
        stmt = conn.prepareStatement("create table ORDERS(ORDER_NUMBER NUMERIC(6),ORDER_YEAR NUMERIC(4),ORDER_DATE DATE,CUSTOMER_ID VARCHAR,TOTAL NUMERIC(12,2),ORDER_STATE CHAR(1),PRIMARY KEY(ORDER_NUMBER,ORDER_YEAR))");
        stmt.execute();
        stmt.close();

        for(int i=1;i<=5;i++) {
          stmt = conn.prepareStatement("insert into ORDERS values("+i+",2007,?,'C"+i+"',1000,'O')");
          stmt.setObject(1,new java.sql.Date(System.currentTimeMillis()+86400000*i));
          stmt.execute();
        }

        for(int i=1;i<=100;i++) {
          stmt = conn.prepareStatement("insert into ORDERS values("+i+",2008,?,'C"+i+"',1000,'C')");
          stmt.setObject(1,new java.sql.Date(System.currentTimeMillis()+86400000*i));
          stmt.execute();
        }

        stmt.close();
        stmt = conn.prepareStatement("create table ORDER_ROWS(ORDER_NUMBER NUMERIC(6),ORDER_YEAR NUMERIC(4),ROW_NUMBER NUMERIC(6),ITEM_ID VARCHAR,ITEM_DESCRIPTION VARCHAR,PRICE NUMERIC(12,2),QTY NUMERIC(4),PRIMARY KEY(ORDER_NUMBER,ORDER_YEAR,ROW_NUMBER))");
        stmt.execute();
        stmt.close();

        for(int i=1;i<=5;i++)
          for(int r=1;r<=5;r++) {
            stmt = conn.prepareStatement("insert into ORDER_ROWS values("+i+",2007,"+r+",'C"+i+""+r+"','Item C"+i+""+r+" description',1000,1)");
            stmt.execute();
          }

        stmt.close();
        stmt = conn.prepareStatement("create table ORDER_ROW_DISCOUNTS(ORDER_NUMBER NUMERIC(6),ORDER_YEAR NUMERIC(4),ROW_NUMBER NUMERIC(6),DISCOUNT_CODE VARCHAR,DISCOUNT_DESCRIPTION VARCHAR,DISCOUNT_VALUE NUMERIC(12,2),PRIMARY KEY(ORDER_NUMBER,ORDER_YEAR,ROW_NUMBER,DISCOUNT_CODE))");
        stmt.execute();
        stmt.close();

        for(int i=1;i<=5;i++)
          for(int r=1;r<=5;r++)
            for(int d=1;d<=3;d++) {
              stmt = conn.prepareStatement("insert into ORDER_ROW_DISCOUNTS values("+i+",2007,"+r+",'D"+i+""+r+""+d+"','Discount "+d+"',10)");
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
