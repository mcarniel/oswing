package org.openswing.swing.table.client;

import javax.swing.*;
import java.awt.BorderLayout;
import javax.swing.border.*;
import org.openswing.swing.client.*;



/**
 * <p>Title: OpenSwing Framework</p>
 * <p>Description: Grid Status Panel, viewed at the bottom of the grid (the panel is optional).</p>
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
public class GridStatusPanel extends JPanel {

  /** text contained in the status panel */
  private JLabel statusLabel = new JLabel();


  public GridStatusPanel() {
    try {
      jbInit();
    }
    catch(Exception e) {
      e.printStackTrace();
    }
  }


  private void jbInit() throws Exception {
    this.setLayout(new BorderLayout());
    this.add(statusLabel, BorderLayout.CENTER);
    Border border = BorderFactory.createLoweredBevelBorder();
    this.setBorder(border);
  }


  /**
   * Set the text on the status panel
   * @param text text to view in the status panel
   */
  public final void setText(String text) {
    statusLabel.setText(text);
  }


}
