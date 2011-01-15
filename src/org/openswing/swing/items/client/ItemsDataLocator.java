package org.openswing.swing.items.client;

import java.util.*;

import org.openswing.swing.message.receive.java.*;


/**
 * <p>Title: OpenSwing Framework</p>
 * <p>Description: Items Data locator, used for ComboBoxVOControl or ListVOControl.</p>
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
public abstract class ItemsDataLocator {

  /** parameters used to retrieve grid data (optional) */
  private Map itemsParams = new HashMap();


  public ItemsDataLocator() {}


  /**
   * Method called by items controller to fill in the combo-box or list.
   * @param valueObjectType type of value object associated to the items grid
   * @return list of value objects to fill in the items grid: VOListResponse if data fetching has success, ErrorResponse otherwise
   */
  public abstract Response loadData(
      Class valueObjectType
  );


  /**
   * @return items parameters
   */
  public Map getItemsParams() {
    return itemsParams;
  }

  /**
   * Set items parameters.
   * @param itemsParams items parameters
   */
  public void setItemsParams(Map itemsParams) {
    this.itemsParams = itemsParams;
  }



}
