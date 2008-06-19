package org.openswing.swing.client;

import org.openswing.swing.message.receive.java.ValueObject;
import org.openswing.swing.table.model.client.VOListTableModel;
import javax.swing.JComponent;
import java.awt.Container;
import java.awt.Component;


/**
 * <p>Title: OpenSwing Framework</p>
 * <p>Description: controller used to manage row expansion in grid control.</p>
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
public class ExpandableRowController {


  public ExpandableRowController() {
  }


  /**
   * @param model grid model
   * @param rowNum the current row number
   * @return <code>true</code> if the current row must be expanded, <code>false</code> otherwise
   */
  public boolean isRowExpandable(VOListTableModel model,int rowNum) {
    return false;
  }


  /**
   * @param model grid model
   * @param rowNum the current row number
   * @return JComponent to show when expanding row; null to do not show anything
   */
  public JComponent getComponentToShow(VOListTableModel model,int rowNum) {
    return null;
  }


  /**
   * @param model grid model
   * @param rowNum the current row number that is just collapsed
   * @param showedComponent component to remove
   * @return <code>true</code> to detach the component from internal cache; <code>false</code> to store the collapsed component in cache
   * This callback method is automatically invoked when the component must be collapsed and can be used also
   * to perfom additional operations to correctly dispose the component.
   * When returned value is <code>true</code>, the grid removes the component from cache, so reducing the amount of memory required by the application;
   * a drawback is that re-expansion of the same row requires to recreate the component, i.e. the invokation of "getComponentToShow" method again.
   * When returned value is <code>false</code>, the grid mantains the component into its internal cache, this will increase
   * the amount of memory required by the application, but avoid to re-create the component for future expansions of the same row,
   * i.e. "getComponentToShow" method is not invoked twice for the same row.
   */
  public boolean removeShowedComponent(VOListTableModel model,int rowNum,JComponent showedComponent) {
    return false;
  }


  /**
   * @param showedComponent component currently showed
   * @return component that will receive focus when showing frame; null to do not set focus automatically
   */
  public Component getFocusableComponent(JComponent showedComponent) {
    return null;
  }


}
