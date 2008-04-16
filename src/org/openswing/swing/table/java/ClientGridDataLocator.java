package org.openswing.swing.table.java;

import java.util.*;

import org.openswing.swing.message.receive.java.*;


/**
 * <p>Title: OpenSwing Framework</p>
 * <p>Description: Class used to retrieve a block of rows for the grid,
 * without calling the server side.</p>
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
public class ClientGridDataLocator implements GridDataLocator {

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
        ArrayList rows = new ArrayList();
        Object o = valueObjectType.newInstance();
        for (int i = 0; i < valueObjectType.getMethods().length; i++) {
          if (valueObjectType.getMethods()[i].getName().startsWith("set")) {
            valueObjectType.getMethod(valueObjectType.getMethods()[i].getName(),valueObjectType.getMethods()[i].getParameterTypes()).invoke(o,new Object[]{getObject(valueObjectType.getMethods()[i].getParameterTypes()[0])});
          }
        }
        rows.add(o);
        VOListResponse r = new VOListResponse(rows,false,1);
        return r;
      }
      catch (Exception ex) {
        return new ErrorResponse("Error while loading data:\n"+ex.toString());
      }
    }


    /**
     * Method called by loadData: it fill in the table with demo values, without calling a server method.
     */
    private Object getObject(Class type) {
      if (type.equals(String.class))
        return "abc";
      if (type.equals(Date.class))
        return new Date();
      if (type.equals(Long.class) || type.equals(Long.TYPE))
        return new Long(1);
      if (type.equals(Integer.class) || type.equals(Integer.TYPE))
        return new Integer(1);
      if (type.equals(Short.class) || type.equals(Short.TYPE))
        return Short.valueOf("1");
      if (type.equals(Float.class) || type.equals(Float.TYPE))
        return new Float(1.2);
      if (type.equals(Double.class) || type.equals(Double.TYPE))
        return new Double(1.2);
      if (type.equals(java.math.BigDecimal.class))
        return new java.math.BigDecimal(1.2);
      if (type.equals(Boolean.class))
        return new Boolean(true);
      else
        return null;
    }



}
