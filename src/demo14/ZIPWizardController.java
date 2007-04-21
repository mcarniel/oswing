package demo14;

import javax.swing.*;
import org.openswing.swing.client.*;
import java.awt.*;
import org.openswing.swing.tree.client.*;
import java.awt.event.*;
import org.openswing.swing.util.client.ClientSettings;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreePath;
import org.openswing.swing.wizard.client.WizardPanel;
import org.openswing.swing.wizard.client.WizardController;


/**
 * <p>Title: OpenSwing Framework</p>
 * <p>Description: Wizard controller: it defines which panel to show.</p>
 * <p>Copyright: Copyright (C) 2006 Mauro Carniel</p>
 * <p> </p>
 * @author Mauro Carniel
 * @version 1.0
 */
public class ZIPWizardController extends WizardController {


  public ZIPWizardController() {
  }


  /**
   * Method called by WizardPanel when showing the panel the first time
   * @param wizard wizard panel
   * @return panel identifier of the panel to show
   */
  public String getFirstPanelId(WizardPanel wizard) {
    return "FIRST";
  }


  /**
   * Method called by WizardPanel to check which panel is the last one
   * @param wizard wizard panel
   * @return panel identifier of the last panel
   */
  public String getLastPanelId(WizardPanel wizard) {
    if ( !((FirstPanel)wizard.getPanel("FIRST")).getRadioButtonZip().isSelected() )
      return "SECOND";
    else
      return "THIRD";
  }


  /**
   * Method called by WizardPanel when when pressing "Back" button
   * @param wizard wizard panel
   * @return panel identifier of the panel to show
   */
  public String getBackPanelId(WizardPanel wizard) {
    if (wizard.getCurrentVisiblePanel().getPanelId().equals("SECOND"))
      return "FIRST";
    else if (wizard.getCurrentVisiblePanel().getPanelId().equals("THIRD"))
      return "FIRST";
    else
      return null;
  }


  /**
   * Method called by WizardPanel when pressing "Next" button
   * @param wizard wizard panel
   * @return panel identifier of the panel to show
   */
  public String getNextPanelId(WizardPanel wizard) {
    if (wizard.getCurrentVisiblePanel().getPanelId().equals("FIRST")) {
      if ( !((FirstPanel)wizard.getPanel("FIRST")).getRadioButtonZip().isSelected() )
        return "SECOND";
      else
        return "THIRD";
    }
    else
      return null;
  }


}
