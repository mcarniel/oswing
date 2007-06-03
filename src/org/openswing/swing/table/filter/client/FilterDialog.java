package org.openswing.swing.table.filter.client;

import org.openswing.swing.table.columns.client.*;
import org.openswing.swing.table.client.GridController;
import org.openswing.swing.domains.java.*;
import org.openswing.swing.logger.client.Logger;
import org.openswing.swing.client.*;


import java.util.*;
import java.math.*;
import java.awt.*;
import javax.swing.*;
import javax.swing.table.*;
import java.awt.event.*;
import java.sql.Types;
import org.openswing.swing.util.client.*;
import org.openswing.swing.internationalization.java.*;
import org.openswing.swing.client.CheckBoxControl;
import org.openswing.swing.util.client.ClientSettings;
import org.openswing.swing.util.java.Consts;
import org.openswing.swing.message.send.java.FilterWhereClause;
import org.openswing.swing.table.client.Grids;


/**
 * <p>Title: OpenSwing Framework</p>
 * <p>Description: Filter dialog, associated to the grid and showed when the user clicks on filter button on the toolbar.</p>
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
public class FilterDialog extends JDialog {

  /** filter panel */
  private FilterPanel panel = null;


  /**
   * Constructor called by a grid control to apply filtering/sorting conditions.
   * @param colProperties column properties
   * @param gridOrder list or order clauses to apply to the grid
   * @param gridFilter list of filter clauses to apply to the grid
   * @param grid grid control
   */
  public FilterDialog(Column[] colProperties,Grids grid) {
    super(ClientUtils.getParentFrame(grid),ClientSettings.getInstance().getResources().getResource("filtering and sorting settings"),true);
    try {
      panel = new FilterPanel(colProperties,grid);
      jbInit();
      setSize(300,400);
      setDefaultCloseOperation(JDialog.HIDE_ON_CLOSE);
      ClientUtils.centerDialog(ClientUtils.getParentFrame(grid),this);
    }
    catch(Exception e) {
      e.printStackTrace();
    }
  }


  public final void init() {
    panel.init();
    setVisible(true);
  }


  private void jbInit() throws Exception {
    this.getContentPane().add(panel, BorderLayout.CENTER);
  }

}
