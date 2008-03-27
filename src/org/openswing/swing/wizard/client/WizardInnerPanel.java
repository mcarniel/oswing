package org.openswing.swing.wizard.client;

import javax.swing.*;


/**
 * <p>Title: OpenSwing Framework</p>
 * <p>Description: Panel that will be inserted into the WizardPanel.
 * It is automatically showed by WizardPanel, when pressing "Back"/"Next" buttons,
 * according to navigation logic defined in WizardController class.
 *
 * A programmer must define the getPanelId() method, used in WizardController to define which panel to show.
 * A programmer could define the getImageName() method, used in WizardPanel to show an image at the left of this panel.
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
public class WizardInnerPanel extends JPanel {


  public WizardInnerPanel() {
  }


  /**
   * This method is automatically called by WizardPanel when the panel is showed:
   * it can be overrided to add custom logic that must be executed when the panel is showed.
   */
  public void init() {}


  /**
   * This method must be overrided.
   * @return panel identifier
   */
  public String getPanelId() {
    return "";
  }


  /**
   * This method could be overrided.
   * @return image name; null if no image is required
   */
  public String getImageName() {
    return null;
  }


}
