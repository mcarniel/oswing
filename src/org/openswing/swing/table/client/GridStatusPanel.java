package org.openswing.swing.table.client;

import java.awt.*;
import javax.swing.*;
import javax.swing.border.*;

import org.openswing.swing.util.client.*;



/**
 * <p>Title: OpenSwing Framework</p>
 * <p>Description: Grid Status Panel, viewed at the bottom of the grid (the panel is optional).
 * It shows:
 * - current selected rows
 * - current page number (only in case loading one page per time)
 * - total number of pages (optionally)
 * - current applied grid profile (optionally)
 * </p>
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

  /** text contained in page panel */
  private JLabel pageLabel = new JLabel();

  /** applied grid profile */
  private JLabel profileLabel = new JLabel();

  /** status panel */
  private JLabel statusPanel = new JLabel();

  /** applied grid profile panel */
  private JLabel profilePanel = new JLabel();

  /** page panel */
  private JLabel pagePanel = new JLabel();


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
    Border border = BorderFactory.createLoweredBevelBorder();
    statusPanel.setLayout(new BorderLayout());
    statusPanel.add(statusLabel, BorderLayout.CENTER);
    statusPanel.setBorder(border);
    if (!ClientSettings.SHOW_PAGE_NUMBER_IN_GRID)
      this.add(statusPanel, BorderLayout.CENTER);
    else {
      this.add(statusPanel, BorderLayout.WEST);
      statusPanel.setMinimumSize(new Dimension(170,statusPanel.getHeight()));
      statusPanel.setPreferredSize(new Dimension(170,statusPanel.getHeight()));
      Border border2 = BorderFactory.createLoweredBevelBorder();
      pagePanel.setLayout(new BorderLayout());
      pagePanel.setMinimumSize(new Dimension(100,statusPanel.getHeight()));
      pagePanel.setPreferredSize(new Dimension(100,statusPanel.getHeight()));
      pagePanel.add(pageLabel, BorderLayout.CENTER);
      pagePanel.setBorder(border2);
      this.add(pagePanel, BorderLayout.CENTER);
      pageLabel.setText(" ");
    }

    if (ClientSettings.getInstance().GRID_PROFILE_MANAGER!=null) {
      Border border2 = BorderFactory.createLoweredBevelBorder();
      profilePanel.setLayout(new BorderLayout());
      profilePanel.setMinimumSize(new Dimension(100,statusPanel.getHeight()));
      profilePanel.setPreferredSize(new Dimension(100,statusPanel.getHeight()));
      profilePanel.add(profileLabel, BorderLayout.CENTER);
      profilePanel.setBorder(border2);
      this.add(profilePanel, BorderLayout.EAST);
      profileLabel.setText(" ");
    }

  }


  /**
   * Set the text on the status panel
   * @param text text to view in the status panel
   */
  public final void setText(String text) {
    statusLabel.setText(text);
  }


  /**
   * Set the text on the page panel
   * @param text text to view in the page panel
   */
  public final void setPage(String page) {
    pageLabel.setText(page);
  }


  /**
   * Set the profile description on the status panel
   * @param description profile description
   */
  public final void setProfile(String description) {
    profileLabel.setText(description);
    profilePanel.revalidate();
    profilePanel.repaint();
    this.revalidate();
    this.repaint();
  }

}
