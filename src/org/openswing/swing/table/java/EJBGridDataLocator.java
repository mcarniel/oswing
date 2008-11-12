package org.openswing.swing.table.java;

import java.lang.reflect.*;
import java.util.*;

import org.openswing.swing.message.receive.java.*;
import org.openswing.swing.message.send.java.*;
import javax.naming.InitialContext;


/**
 * <p>Title: OpenSwing Framework</p>
 * <p>Description: Class used to retrieve data for grid,
 * by calling the EJB Session Bean on the server side.</p>
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
public class EJBGridDataLocator implements GridDataLocator {

  /** method to call on the server side */
  private String serverMethodName = null;

  /** ejb remote interface to call on the server side */
  private String ejbName = null;

  /** initial context */
  public transient InitialContext initialContext;


  /**
   * Method invoked by the grid to load a block or rows.
   * @param action fetching versus: PREVIOUS_BLOCK_ACTION, NEXT_BLOCK_ACTION or LAST_BLOCK_ACTION
   * @param startPos start position of data fetching in result set
   * @param filteredColumns filtered columns
   * @param currentSortedColumns sorted columns
   * @param currentSortedVersusColumns ordering versus of sorted columns
   * @param valueObjectType v.o. type
   * @param otherGridParams other grid parameters
   * @return response from the server: an object of type VOListResponse if data loading was successfully completed, or an ErrorResponse onject if some error occours
   */
  public final Response loadData(
      int action,
      int startIndex,
      Map filteredColumns,
      ArrayList currentSortedColumns,
      ArrayList currentSortedVersusColumns,
      Class valueObjectType,
      Map otherGridParams) {
    try {
      GridParams pars = new GridParams(
          action,
          startIndex,
          filteredColumns,
          currentSortedColumns,
          currentSortedVersusColumns,
          otherGridParams
      );
      Object obj = initialContext.lookup(ejbName);
      Method m = obj.getClass().getMethod(serverMethodName,new Class[]{GridParams.class});
      return (Response)m.invoke(obj,new Object[]{pars});
    }
    catch (Throwable ex) {
      return new ErrorResponse("Error while loading data:\n"+ex.toString());
    }
  }


  /**
   * @return method to call on the server side
   */
  public final String getServerMethodName() {
    return serverMethodName;
  }


  /**
   * Set the method to call on the server side.
   * @param serverMethodName method to call on the server side
   */
  public final void setServerMethodName(String serverMethodName) {
    this.serverMethodName = serverMethodName;
  }


  /**
   * @return ejb remote interface to call on the server side
   */
  public final String getEjbName() {
    return ejbName;
  }


  /**
   * Set the ejb remote interface name to call on the server side.
   * @param ejbName ejb remote interface name
   */
  public final void setEjbName(String ejbName) {
    this.ejbName = ejbName;
  }


  /**
   * @return initial context
   */
  public final InitialContext getInitialContext() {
    return initialContext;
  }


  /**
   * Set the initial context.
   * @param initialContext initial context
   */
  public final void setInitialContext(InitialContext initialContext) {
    this.initialContext = initialContext;
  }



}
