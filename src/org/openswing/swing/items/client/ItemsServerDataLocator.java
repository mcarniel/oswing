package org.openswing.swing.items.client;

import org.openswing.swing.message.receive.java.*;
import org.openswing.swing.util.client.*;


/**
 * <p>Title: OpenSwing Framework</p>
 * <p>Description: Items Data Source that send requests to server side
 * when validating code and when opening items grid frame.</p>
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
public class ItemsServerDataLocator extends ItemsDataLocator {

  /** method called on server side to fetching rows to fill in the items list */
  private String itemsMethodName = null;


  public ItemsServerDataLocator() {}


  /**
   * @return method called on server side to fetching rows to fill in the items grid
   */
  public String getItemsMethodName() {
    return itemsMethodName;
  }


  /**
   * Set the method called on server side to fetching rows to fill in the items list.
   * @param itemsMethodName method called on server side to fetching rows to fill in the items list
   */
  public void setGridMethodName(String itemsMethodName) {
    this.itemsMethodName = itemsMethodName;
  }


  /**
   * Method called by items controller to fill in the items list.
   * @param valueObjectType type of value object associated to the items list
   * @return list of value objects to fill in the items grid: VOListResponse if data fetching has success, ErrorResponse otherwise
   */
  public Response loadData(
      Class valueObjectType) {

    return ClientUtils.getData(
        itemsMethodName,
        getItemsParams()
    );
  }


}
