package org.openswing.swing.miscellaneous.client;

import java.awt.*;
import javax.swing.*;

import org.openswing.swing.miscellaneous.util.client.*;
import org.openswing.swing.util.client.*;


/**
 * <p>Title: OpenSwing Framework</p>
 * <p>Description: Frame that contains a tip of the day panel.</p>
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
public class TipFrame extends JFrame implements TipPanelContainer {

  /** tip panel to show */
  TipPanel tipPanel = null;


  /**
   * Constructor.
   * @param tipPanelContent TipPanel container
   */
  public TipFrame(TipPanelContent tipPanelContent) {
    try {
      tipPanel = new TipPanel(
          this,
          tipPanelContent
      );
      jbInit();
      setTitle(ClientSettings.getInstance().getResources().getResource("tip of the day"));
      setSize(600,300);
    }
    catch(Exception e) {
      e.printStackTrace();
    }
  }


  /**
   * Set tip image.
   * @param imageName tip image
   */
  public final void setImageName(String imageName) {
    if (tipPanel!=null)
      this.tipPanel.setImageName(imageName);
  }


  /**
   * @return tip image
   */
  public final String getImageName() {
    if (tipPanel!=null)
      return this.tipPanel.getImageName();
    else
      return null;
  }


  /**
   * @return used to define if the checkbox 'show 'tip of the day' after launching' must be showed
   */
  public final boolean isShowCheck() {
    if (tipPanel!=null)
      return tipPanel.isShowCheck();
    else
      return true;
  }


  /**
   * Used to define if the checkbox 'show 'tip of the day' after launching' must be showed.
   * @param showCheck used to define if the checkbox 'show 'tip of the day' after launching' must be showed
   */
  public final void setShowCheck(boolean showCheck) {
    if (tipPanel!=null)
      this.tipPanel.setShowCheck(showCheck);
  }


  /**
   * Method called by TipPanel to close TipPanel container.
   */
  public final void closeTipPanel() {
    this.setVisible(false);
    this.dispose();
  }


  /**
   * @return <code>true</code> to show tip frame when launching an application, <code>false</code> otherwise
   */
  public final boolean isShowToolTip() {
    return tipPanel.isShowToolTip();
  }


  private void jbInit() throws Exception {
    this.getContentPane().add(tipPanel, BorderLayout.CENTER);
  }

}
