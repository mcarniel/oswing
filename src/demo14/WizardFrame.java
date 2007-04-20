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


/**
 * <p>Title: OpenSwing Framework</p>
 * <p>Description: Wizard Frame: it shows three panes, used to zip or unzip file.
 * The ZIPWizardController class is uded to establish which pane to show when pressing "Back" or "Next" button.</p>
 * <p>Copyright: Copyright (C) 2006 Mauro Carniel</p>
 * <p> </p>
 * @author Mauro Carniel
 * @version 1.0
 */
public class WizardFrame extends JFrame {

  private WizardPanel wizardPanel = new WizardPanel();

  private DefaultMutableTreeNode dragNode = null;


  public WizardFrame() {
    try {
      jbInit();
      setTitle("Copy Files Wizard");
      setSize(600,400);

      wizardPanel.addPanel(new FirstPanel());
      wizardPanel.addPanel(new SecondPanel());
      wizardPanel.addPanel(new ThirdPanel());
      wizardPanel.setNavigationLogic(new ZIPWizardController());

      setVisible(true);
    }
    catch(Exception e) {
      e.printStackTrace();
    }
  }



  private void jbInit() throws Exception {
    this.getContentPane().add(wizardPanel, BorderLayout.CENTER);
  }


}

