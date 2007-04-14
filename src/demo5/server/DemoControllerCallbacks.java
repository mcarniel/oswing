package demo5.server;

import org.openswing.swing.server.*;
import javax.servlet.ServletContext;
import java.sql.*;


/**
 * <p>Title: OpenSwing Framework</p>
 * <p>Description: Controller callbacks class.</p>
 * <p>Copyright: Copyright (C) 2006 Mauro Carniel</p>
 * <p> </p>
 * @author Mauro Carniel
 * @version 1.0
 */

public class DemoControllerCallbacks extends ControllerCallbacks {

  public DemoControllerCallbacks() {
  }


  /**
   * Method called by the init method of Controller class, as last instruction.
   */
  public void afterInit(ServletContext context) {
    createConnection(context);
  }


  /**
   * Create the database connection (using Hypersonic DB - in memory) and initialize tables...
   */
  private void createConnection(ServletContext context) {
    Connection conn = null;
    PreparedStatement stmt = null;
    try {
      conn = ConnectionManager.getConnection(context);
      try {
        stmt = conn.prepareStatement("create table DEMO5(TEXT VARCHAR,DECNUM DECIMAL(10,2),CURRNUM DECIMAL(10,2),THEDATE DATE,COMBO VARCHAR,CHECK CHAR(1),RADIO CHAR(1),CODE VARCHAR,TA VARCHAR,PRIMARY KEY(TEXT))");
        stmt.execute();
        stmt.close();

        stmt = conn.prepareStatement("create table DEMO5_LOOKUP(CODE VARCHAR,DESCRCODE VARCHAR,PRIMARY KEY(CODE))");
        stmt.execute();

        for(int i=0;i<200;i++) {
          stmt.close();
          stmt = conn.prepareStatement("insert into DEMO5 values('ABC"+i+"',"+12+i+0.333+","+1234+i+0.56+",?,'ABC','Y','Y','A"+i+"','AAAAAA"+i+"')");
          stmt.setObject(1,new java.sql.Date(System.currentTimeMillis()+86400000*i));
          stmt.execute();
        }

        for(int i=0;i<100;i++) {
          stmt.close();
          stmt = conn.prepareStatement("insert into DEMO5_LOOKUP values('A"+i+"','ABCDEF"+String.valueOf((char)(i+78))+"')");
          stmt.execute();
        }

        stmt.close();
        stmt = conn.prepareStatement("create table DEMO5_USERS(USERNAME VARCHAR,PASSWORD VARCHAR,LANGUAGE_ID VARCHAR,PRIMARY KEY(USERNAME))");
        stmt.execute();
        stmt.close();
        stmt = conn.prepareStatement("insert into DEMO5_USERS values('ADMIN','ADMIN','EN')");
        stmt.execute();
        stmt.close();
        stmt = conn.prepareStatement("insert into DEMO5_USERS values('MAURO','MAURO','IT')");
        stmt.execute();

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
    finally {
      try {
        ConnectionManager.releaseConnection(conn, context);
      }
      catch (Exception ex3) {
      }
    }
  }



}