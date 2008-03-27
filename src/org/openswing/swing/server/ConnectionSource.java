package org.openswing.swing.server;

import java.sql.*;
import javax.servlet.*;
import javax.servlet.http.*;


/**
 * <p>Title: OpenSwing Framework</p>
 * <p>Description: Singleton class.
 * Database Connection Manager: it manages database connections.</p>
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
public interface ConnectionSource {


  /**
   * @param context servlet context; used to retrieve database connection settings
   * @return new database connection
   */
  public Connection getConnection(ServletContext context) throws Exception;


  /**
   * Release a database connection
   * @param conn database connection to release
   * @param context servlet context; used to retrieve database connection settings
   */
  public void releaseConnection(Connection conn,ServletContext context);


  /**
   * Initialize the connection pooler.
   * Called by ConnectionManager.initConnectionSource method.
   * @param context servlet context; used to retrieve database connection settings
   */
  public boolean initPooler(HttpServlet servlet);

}
