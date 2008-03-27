package org.openswing.swing.properties.client;

import org.openswing.swing.client.*;

/**
 * <p>Title: OpenSwing Framework</p>
 * <p>Description: Controller for a PropertyGridControl: it contains a callback method invoked for loading data (property values) on grid.</p>
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
public class PropertyGridController {


  /**
   * Callback method invoked by the PropertyGridControl for loading data (property values).
   */
  public void loadData(PropertyGridControl grid) { }


  /**
   * Callback method invoked by the PropertyGridControl for saving data the first time.
   * @param grid PropertyGridControl
   * @param changedRowNumbers rows indexes, related to changed rows
   * Note: you can obtain the current value of a property by means of the method: PropertyGridControl.getPropertyValue
   * @return <code>true</code> if the insert operation has been correctly performed, <code>false</code> otherwise
   */
  public boolean insertRecords(PropertyGridControl grid,int[] changedRowNumbers) {
    return false;
  }


  /**
   * Callback method invoked by the PropertyGridControl for saving data the first time.
   * @param grid PropertyGridControl
   * @param changedRowNumbers rows indexes, related to changed rows
   * Note: you can obtain the current value of a property by means of the method: PropertyGridControl.getPropertyValue
   * Note: you can obtain the previous value of a property by means of the method: PropertyGridControl.getOldPropertyValue
   * @return <code>true</code> if the insert operation has been correctly performed, <code>false</code> otherwise
   */
  public boolean updateRecords(PropertyGridControl grid,int[] changedRowNumbers) {
    return false;
  }


}
