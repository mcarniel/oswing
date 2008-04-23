package org.openswing.swing.lookup.client;

import java.util.*;

import javax.swing.*;

import org.openswing.swing.table.client.*;


/**
 * <p>Title: OpenSwing Framework</p>
 * <p>Description: Filter panel showed on top of the lookup grid
 * that can be extended to create a custom filter.
 * Usually a programmer has to:
 * - first override init() callback method to preset panel content and
 * - use getQuickFilterValues method to define filtering conditions and
 * - finally invoke reload/clearData methods to reload/clear grid content
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
public class CustomFilterPanel extends JPanel {

  /** grid onto apply filter */
  protected Grids grid;

  /** define if grid has to automatically load data when opening lookup frame; default value: <code>false</code> */
  private boolean autoLoadData = false;


  /**
   * This method is automatically invoked by lookup controller when this panel is showed:
   * it fill in "grid" property.
   * @param grid lookup grid
   */
  public final void setLookupGrid(Grids grid) {
    this.grid = grid;
  }


  /**
   * Shortcut to force grid reloading.
   */
  public final void reload() {
    grid.reload();
  }


  /**
   * Shortcut to clear grid content.
   */
  public final void clearData() {
    grid.clearData();
  }


  /**
   * @return shortcut to an hashtable which contains the associations: attribute name, new FilterWhereClause[2] {FilterWhereClause,FilterWhereClause})
   */
  public final Map getQuickFilterValues() {
    return grid.getQuickFilterValues();
  }


  /**
   * @return define if grid has to automatically load data when opening lookup frame
   */
  public final boolean isAutoLoadData() {
    return autoLoadData;
  }


  /**
   * Define if grid has to automatically load data when opening lookup frame.
   * @param autoLoadData define if grid has to automatically load data when opening lookup frame
   */
  public final void setAutoLoadData(boolean autoLoadData) {
    this.autoLoadData = autoLoadData;
  }


  /**
   * Callback method invoked by LookupController after filled in "grid" property and before showing panel.
   * A programmer can override this method to preset panel content according to grid properties.
   */
  public void init() { }


}