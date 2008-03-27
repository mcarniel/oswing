package org.openswing.swing.table.java;

import java.lang.reflect.*;
import java.util.*;

import org.openswing.swing.message.receive.java.*;
import org.openswing.swing.message.send.java.*;


/**
 * <p>Title: OpenSwing Framework</p>
 * <p>Description: Class used to retrieve a block of rows for the grid,
 * by calling the server side, using the serverMethodName property and ClientUtils.getData method.</p>
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
public class ServerGridDataLocator implements GridDataLocator {

  /** method to call on the server side */
  private String serverMethodName = null;



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
  public Response loadData(
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
      Method m = Class.forName("org.openswing.swing.util.client.ClientUtils").getMethod("getData",new Class[]{String.class,Object.class});
      return (Response)m.invoke(null,new Object[]{serverMethodName,pars});
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



}
