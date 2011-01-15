package demo14;

import javax.swing.*;
import org.openswing.swing.client.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.tree.DefaultMutableTreeNode;
import org.openswing.swing.wizard.client.WizardPanel;
import org.openswing.swing.util.client.ClientUtils;


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
      super.setDefaultCloseOperation(super.EXIT_ON_CLOSE);
      jbInit();
      setTitle("Zip/Unzip Files Wizard");
      setSize(750,400);
      ClientUtils.centerFrame(this);

      IntroPanel introPanel = new IntroPanel();
      ZIPWizardController controller = new ZIPWizardController(wizardPanel);

      // add a listener to the "ok" radio button in the licence agreement panel to enable "next" button...
      introPanel.addOkRadioButtonItemListener(controller);

      wizardPanel.addPanel(introPanel);
      wizardPanel.addPanel(new FirstPanel());
      wizardPanel.addPanel(new SecondPanel());
      wizardPanel.addPanel(new ThirdPanel());
      wizardPanel.setNavigationLogic(controller);
//      wizardPanel.setImageName("setup.gif");
      wizardPanel.addActionListener(new ActionListener() {
        public void actionPerformed(ActionEvent e) {
          if (e.getSource().equals(wizardPanel.getCancelButton()) && wizardPanel.getCurrentVisiblePanel().getPanelId().equals("INTRO"))
            System.exit(0);
          else if (e.getSource().equals(wizardPanel.getCancelButton()) && wizardPanel.getCurrentVisiblePanel().getPanelId().equals("FIRST"))
            System.exit(0);
          else if (e.getSource().equals(wizardPanel.getCancelButton()) && !wizardPanel.getCurrentVisiblePanel().getPanelId().equals("FIRST")) {
            new ExecutionEngine(wizardPanel,WizardFrame.this);
          }
        }
      });


      setVisible(true);

      // used to disable "next" button when showing intro panel (licence panel)...
      wizardPanel.getNextButton().setEnabled(false);
    }
    catch(Exception e) {
      e.printStackTrace();
    }
  }




  private void jbInit() throws Exception {
    this.getContentPane().add(wizardPanel, BorderLayout.CENTER);
  }


}

