package org.openswing.swing.wizard.client;

/**
 * <p>Title: OpenSwing Framework</p>
 * <p>Description: This class defines the navigation logic used to show a WizardInnerPanel into the WizardPanel,
 * when pressing "Back"/"Next" buttons.
 *
 * A programmer can define the method getFirstPanelId() to specify which panel must be showed first.
 * If this class is not derived, then panels are showed in the same order used to add them to the WizardPanel.
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
public class WizardController {

  public WizardController() {
  }


  /**
   * Method called by WizardPanel when showing the panel the first time
   * @param wizard wizard panel
   * @return panel identifier of the panel to show
   */
  public String getFirstPanelId(WizardPanel wizard) {
    if (wizard.getPanels().length>0)
      return wizard.getPanels()[0].getPanelId();
    else
      return null;
  }


  /**
   * Method called by WizardPanel to check which panel is the last one
   * @param wizard wizard panel
   * @return panel identifier of the last panel
   */
  public String getLastPanelId(WizardPanel wizard) {
    if (wizard.getPanels().length>0)
      return wizard.getPanels()[wizard.getPanels().length-1].getPanelId();
    else
      return null;
  }


  /**
   * Method called by WizardPanel when when pressing "Back" button
   * @param wizard wizard panel
   * @return panel identifier of the panel to show
   */
  public String getBackPanelId(WizardPanel wizard) {
    if (wizard.getPanels().length>0)
      for(int i=1;i<wizard.getPanels().length;i++)
        if (wizard.getPanels()[i].equals(wizard.getCurrentVisiblePanel()))
          return wizard.getPanels()[i-1].getPanelId();
    return null;
  }


  /**
   * Method called by WizardPanel when pressing "Next" button
   * @param wizard wizard panel
   * @return panel identifier of the panel to show
   */
  public String getNextPanelId(WizardPanel wizard) {
    if (wizard.getPanels().length>0)
      for(int i=0;i<wizard.getPanels().length-1;i++)
        if (wizard.getPanels()[i].equals(wizard.getCurrentVisiblePanel()))
          return wizard.getPanels()[i+1].getPanelId();
    return null;
  }



}