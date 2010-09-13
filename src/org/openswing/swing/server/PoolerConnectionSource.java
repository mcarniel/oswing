package org.openswing.swing.server;

import java.io.*;
import java.sql.*;
import java.util.*;
import javax.servlet.*;
import javax.servlet.http.*;

import com.rp.database.*;


/**
 * <p>Title: OpenSwing Framework</p>
 * <p>Description: Database Connection Manager: it manager database connections, using a free connection pooler.
 * This class requires a "pooler.ini" file having the following properties:
 * </p>
 * <ul>
 * <li>"logLevel" - possible values: debug = 4, info = 3, warn = 2, error = 1, fatal = 0
 * <li>"driverClass" - JDBC driver class name
 * <li>"maxCount" - maximum number of opened connections inside the pooler
 * <li>"minCount" - minimum number of opened connections inside the pooler
 * <li>"user" - database username
 * <li>"password" - database password
 * <li>"url" - JDBC database connection URL
 * <li>"loginTimeout" - timeout (in seconds) on database login ("0" no timeout)
 * <li>"holdTimeout" - timemout (in seconds) on holding opened a connection
 * <li>"waitTimeout" - timeout (in milliseconds) on waiting an available connection
 * </ul>
 * <p>The following are pre-defined:</p>
 * <ul>
 * <li>"logLevel" = 1
 * <li>"maxCount" = 5
 * <li>"minCount" = 1
 * <li>"loginTimeout" = 0
 * <li>"holdTimeout" = 1000
 * <li>"waitTimeout" = 10000
 * </ul>
 * <p>Copyright: Copyright (C) 2006 Mauro Carniel</p>
 *
 * <p> This file is part of OpenSwing Framework.
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the (LGPL) Lesser General Public
 * License as published by the Free Software Foundation;
 *
 *                GNU LESSER GENERAL PUBLIC LICENSE
 *                 Version 2.1, February 1999
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Library General Public License for more details.
 *
 * You should have received a copy of the GNU Library General Public
 * License along with this library; if not, write to the Free
 * Software Foundation, Inc., 675 Mass Ave, Cambridge, MA 02139, USA.
 *
 *       The author may be contacted at:
 *           maurocarniel@tin.it</p>
 *
 * @author Mauro Carniel
 * @version 1.0
 */
public class PoolerConnectionSource implements ConnectionSource {

  /** pooler */
  private RPDataSource pool = null;


  public PoolerConnectionSource() {
  }


  /**
   * Create a "pooler.ini" file with mandatory parameters only.
   * @param driverClass JDBC driver class name
   * @param user database username
   * @param password database password
   * @param url JDBC database connection URL
   */
  public final void saveProperties(ServletContext context,String driverClass,String user,String password,String url) {
    try {
      Properties props = new Properties();
      props.setProperty("driverClass", driverClass);
      props.setProperty("user", user);
      props.setProperty("password", password);
      props.setProperty("url", url);
      props.save(new FileOutputStream(this.getClass().getResource("/").getPath().replaceAll("%20"," ")+"pooler.ini"), "POOLER PROPERTIES");
    }
    catch (Throwable ex) {
      context.log("Error while creating connection pooler",ex);
    }
  }



  /**
   * Initialize the connection pooler.
   * Called by ConnectionManager.initConnectionSource method.
   * @param context servlet context; used to retrieve database connection settings
   */
  public boolean initPooler(HttpServlet servlet) {
    try {
      // load file "pooler.ini" config properties
      Properties props = new Properties();
      props.load(new FileInputStream(this.getClass().getResource("/").getPath().replaceAll("%20"," ")+"pooler.ini"));

      if (props.getProperty("logLevel")==null || props.getProperty("logLevel").equals(""))
        props.setProperty("logLevel","1");
      props.setProperty("autoCommit", "false");
      if (props.getProperty("maxCount")==null || props.getProperty("maxCount").equals(""))
        props.setProperty("maxCount","50");
      if (props.getProperty("minCount")==null || props.getProperty("minCount").equals(""))
        props.setProperty("minCount","1");
      if (props.getProperty("loginTimeout")==null || props.getProperty("loginTimeout").equals(""))
        props.setProperty("loginTimeout","0");
      if (props.getProperty("holdTimeout")==null || props.getProperty("holdTimeout").equals(""))
        props.setProperty("holdTimeout","1000");
      if (props.getProperty("waitTimeout")==null || props.getProperty("waitTimeout").equals(""))
        props.setProperty("waitTimeout","10000");
      props.setProperty("statementCacheSize", "10");

      //p = new RPDataSource(p, log); //use this instead of the next line to turn on logging
      pool = new RPDataSource(props);
      return true;
    }
    catch (FileNotFoundException ex) {
      try {
        if ("true".equals(servlet.getInitParameter("suppressInitPoolerError")))
          return false;
      }
      catch (Exception exx) {}
      ex.printStackTrace();
      servlet.getServletContext().log("Error while creating connection pooler: file not found in\n"+new File("pooler.ini").getAbsolutePath(),ex);
      return false;
    }
    catch (Throwable ex) {
      ex.printStackTrace();
      servlet.getServletContext().log("Error while creating connection pooler",ex);
      return false;
    }
  }


  /**
   * @param context servlet context; used to retrieve database connection settings
   * @return new database connection
   */
  public Connection getConnection(ServletContext context) throws Exception {
    Connection conn = pool.getConnection();
    try { conn.setTransactionIsolation(java.sql.Connection.TRANSACTION_READ_COMMITTED); } catch (Exception ex) {}
    conn.setAutoCommit(false);
    return conn;
  }


  /**
   * Release a database connection
   * @param conn database connection to release
   * @param context servlet context; used to retrieve database connection settings
   */
  public void releaseConnection(Connection conn,ServletContext context) {
    try {
      conn.rollback();
      conn.close();
    }
    catch (SQLException ex) {
      context.log("Error while releasing the database connection",ex);
    }
  }


}
