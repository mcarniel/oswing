package demo14;

import javax.swing.*;
import org.openswing.swing.util.client.*;
import org.openswing.swing.internationalization.java.*;
import org.openswing.swing.wizard.client.*;


/**
 * <p>Title: OpenSwing Demo</p>
 * <p>Description: Second panel to show inside the WizardPanel: it requires a zip file to decompress and
 * a directory onto which execute the decompression task.</p>
 * <p>Copyright: Copyright (C) 2006 Mauro Carniel</p>
 * <p> </p>
 * @author Mauro Carniel
 * @version 1.0
 */
public class SecondPanel extends WizardInnerPanel {


  public SecondPanel() {
  }


  /**
   * This method is automatically called by WizardPanel when the panel is showed:
   * it can be overrided to add custom logic that must be executed when the panel is showed.
   */
  public void init() {}


  public String getPanelId() {
    return "SECOND";
  }

}
