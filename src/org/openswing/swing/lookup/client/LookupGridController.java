package org.openswing.swing.lookup.client;

import org.openswing.swing.table.client.GridController;
import org.openswing.swing.message.receive.java.ValueObject;
import org.openswing.swing.table.model.client.VOListTableModel;
import org.openswing.swing.client.GridControl;
import org.openswing.swing.table.client.Grids;


/**
 * <p>Title: OpenSwing Framework</p>
 * <p>Description: Lookup grid controller.
 * This class is used within LookupController to listen for events fired by lookup grid frame,
 * such as double click events and ENTER key pressing.</p>
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
public class LookupGridController extends GridController {

  /** lookup controller */
  protected LookupController lookupController = null;

  /** lookup container (whose value object will be updated when grid row has been selected) */
  protected LookupParent lookupParent = null;

  /** lookup grid model */
  protected VOListTableModel model = null;

  /** inner grid component */
  protected Grids grid = null;


  /**
   * Method called by lookup controller when user clicks on the lookup button.
   * @param lookupController lookup controller
   * @param lookupParent lookup container (whose value object will be updated when grid row has been selected)
   * @param model lookup grid model
   * @param grid inner grid component
   */
  public final void init(LookupController lookupController,LookupParent lookupParent,VOListTableModel model,Grids grid) {
    this.lookupController = lookupController;
    this.lookupParent = lookupParent;
    this.model = model;
    this.grid = grid;
  }


  /**
   * This method cannot be overrided: it forwards the event to "doubleClick" method.
   */
  public final void enterButton(int rowNumber,ValueObject valueObject) {
    doubleClick(rowNumber,valueObject);
  }


  /**
   * This method cannot be overrided: it forwards the event to "doubleClick" method of LookupController class.
   */
  public final void doubleClick(int rowNumber,ValueObject valueObject) {
    lookupController.doubleClick(rowNumber,lookupParent);
  }


  /**
   * @param gridcontrol it is always null: do not use it!
   * @param row selected row index
   * @param attributeName attribute name that identifies the selected grid column
   * @return <code>true</code> if the selected cell is editable, <code>false</code> otherwise
   */
  public final boolean isCellEditable(GridControl gridcontrol,int row,String attributeName) {
    return grid.isFieldEditable(row,attributeName);
  }


}
