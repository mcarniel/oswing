package org.openswing.swing.server;

import java.sql.*;
import javax.servlet.*;
import javax.servlet.http.*;


/**
 * <p>Title: OpenSwing Framework</p>
 * <p>Description: Database Connection Factory: it creates a ConnectionSource object as manager of database connections.</p>
 * This is a sngleton class: developers can directly refer this class to access to the database
 * from the rest of the application (server side).
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
public class ConnectionManager {

  /** database connection manager */
  private static ConnectionSource connectionSource = new NoConnectionSource();


  /**
   * @return <code>true</code> if the connection source has been correcly created, <code>false</code> otherwise
   */
  public static boolean isConnectionSourceCreated() {
    return connectionSource!=null;
  }


  /**
   * @param context servlet context; used to retrieve database connection settings
   * @return new database connection
   */
  public static Connection getConnection(ServletContext context) throws Exception {
    return connectionSource.getConnection(context);
  }


  /**
   * Release a database connection
   * @param conn database connection to release
   * @param context servlet context; used to retrieve database connection settings
   */
  public static void releaseConnection(Connection conn,ServletContext context) {
    connectionSource.releaseConnection(conn,context);
  }


  /**
   * Method called by the server controller (class Controller).
   * @param context servlet context; used to retrieve database connection settings
   * @param connectionSourceClassName database connection manager class name
   */
  public static void initConnectionSource(HttpServlet servlet,String connectionSourceClassName) {
    try {
      connectionSource = (ConnectionSource)Class.forName(connectionSourceClassName).newInstance();
      if (!connectionSource.initPooler(servlet))
        connectionSource = null;
    }
    catch (Throwable ex) {
      connectionSource = null;
      ex.printStackTrace();
      servlet.getServletContext().log("Error while creating connection source '"+connectionSourceClassName+"'",ex);
    }
  }


  /**
   * Method called by the server controller (class Controller).
   */
  public static void destroyConnectionSource() {
    connectionSource = null;
  }


}
