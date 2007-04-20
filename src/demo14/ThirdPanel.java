package demo14;

import javax.swing.*;
import org.openswing.swing.util.client.*;
import org.openswing.swing.internationalization.java.*;
import org.openswing.swing.wizard.client.*;


/**
 * <p>Title: OpenSwing Demo</p>
 * <p>Description: Third panel to show inside the WizardPanel: it requires a list of files to compress and
 * a path+file name to use to create the zip file.</p>
 * <p>Copyright: Copyright (C) 2006 Mauro Carniel</p>
 * <p> </p>
 * @author Mauro Carniel
 * @version 1.0
 */
public class ThirdPanel extends WizardInnerPanel {


  public ThirdPanel() {
  }


  /**
   * This method is automatically called by WizardPanel when the panel is showed:
   * it can be overrided to add custom logic that must be executed when the panel is showed.
   */
  public void init() {}


  public String getPanelId() {
    return "THIRD";
  }

}
