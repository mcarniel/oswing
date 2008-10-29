package org.openswing.swing.miscellaneous.client;

import java.awt.*;

import org.openswing.swing.mdi.client.*;
import org.openswing.swing.miscellaneous.util.client.*;
import org.openswing.swing.util.client.*;
import java.beans.*;


/**
 * <p>Title: OpenSwing Framework</p>
 * <p>Description: Internal frame that contains a tip of the day panel: it must be used with MDIFrame.</p>
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
public class TipInternalFrame extends InternalFrame implements TipPanelContainer {

  /** tip panel to show */
  TipPanel tipPanel = null;


  /**
   * Constructor.
   * @param tipPanelContent TipPanel container
   */
  public TipInternalFrame(TipPanelContent tipPanelContent) {
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
    try {
      closeFrame();
    }
    catch (PropertyVetoException ex) {
    }
  }


  /**
   * @return <code>true</code> to show tip frame when launching an application, <code>false</code> otherwise
   */
  public final boolean isShowToolTip() {
    return tipPanel.isShowToolTip();
  }


  /**
   * Set/unset check-box 'show 'tip of the day' after launching'.
   * @return <code>true</code> to show check-box 'show 'tip of the day' after launching', <code>false</code> to hide it
   */
  public final void setShowToolTip(boolean showToolTip) {
    tipPanel.setShowToolTip(showToolTip);
  }


  private void jbInit() throws Exception {
    this.getContentPane().add(tipPanel, BorderLayout.CENTER);
  }

}
