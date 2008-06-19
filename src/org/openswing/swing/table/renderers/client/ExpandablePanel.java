package org.openswing.swing.table.renderers.client;

import java.awt.*;
import javax.swing.*;

import org.openswing.swing.table.client.*;


/**
 * <p>Title: OpenSwing Framework</p>
 * <p>Description: panel to show as cell renderer. Used to show within a cell renderer in case of expandable rows.</p>
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
public class ExpandablePanel extends JPanel {

  /** parent grid */
  private Grid grid = null;

  /** parent grid */
  private Component nestedComponent = null;


  public ExpandablePanel(Grid grid,Component nestedComponent,Component panel) {
    this.grid = grid;
    this.nestedComponent = nestedComponent;
    setLayout(new BorderLayout());
    add(panel,BorderLayout.CENTER);
    setSize(panel.getSize());
    setPreferredSize(panel.getPreferredSize());
  }


  /**
   * @return nested component
   */
  public final Component getNestedComponent() {
    return nestedComponent;
  }


  /**
   * Gets the parent of this component.
   * @return the parent container of this component
   * @since JDK1.0
   */
  public final Container getParent() {
    try {
      Object p = super.getParent();
      if (p==null && grid != null)
        return grid;
      else
        return super.getParent();
    }
    catch (Exception ex) {
      return super.getParent();
    }
//    return super.getParent();
  }


}
