package org.openswing.swing.server;

import java.sql.*;
import javax.naming.*;
import javax.servlet.*;
import javax.servlet.http.*;
import javax.sql.*;


/**
 * <p>Title: OpenSwing Framework</p>
 * <p>Description: Database Connection Manager: it manager database connections, using a free connection pooler.
 * This class uses the JNDI context to retrieve a datasource.</p>
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
public class DataSourceConnection implements ConnectionSource {

  /** data source */
  private DataSource dataSource = null;


  public DataSourceConnection() {
  }


  /**
   * Initialize the connection pooler.
   * Called by ConnectionManager.initConnectionSource method.
   * @param context servlet context; used to retrieve database connection settings
   */
  public boolean initPooler(HttpServlet servlet) {
    try {
      // create JNDI context...
      InitialContext ic = new InitialContext();
      Context envCtx = (Context)ic.lookup("java:comp/env");
      dataSource = (DataSource)envCtx.lookup("jdbc/"+servlet.getInitParameter("dataSourceName"));
      return dataSource!=null;
    } catch(Throwable ex) {
     ex.printStackTrace();
     servlet.getServletContext().log("Error while creating data source",ex);
     return false;
    }
  }


  /**
   * @param context servlet context; used to retrieve database connection settings
   * @return new database connection
   */
  public Connection getConnection(ServletContext context) throws Exception {
    Connection conn = dataSource.getConnection();
    try { conn.setTransactionIsolation(java.sql.Connection.TRANSACTION_READ_COMMITTED); } catch (Throwable ex) {}
    try { conn.setAutoCommit(false); } catch (Throwable ex1) { }
    return conn;
  }


  /**
   * Release a database connection
   * @param conn database connection to release
   * @param context servlet context; used to retrieve database connection settings
   */
  public void releaseConnection(Connection conn,ServletContext context) {
    try {
      try {
        conn.rollback();
      }
      catch (Exception ex1) {
      }
      conn.close();
    }
    catch (SQLException ex) {
      context.log("Error while releasing the database connection",ex);
    }
  }



}
