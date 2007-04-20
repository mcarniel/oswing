package demo14;

import javax.swing.*;
import org.openswing.swing.util.client.*;
import org.openswing.swing.internationalization.java.*;
import org.openswing.swing.wizard.client.*;
import java.awt.*;


/**
 * <p>Title: OpenSwing Demo</p>
 * <p>Description: First panel to show inside the WizardPanel.</p>
 * <p>Copyright: Copyright (C) 2006 Mauro Carniel</p>
 * <p> </p>
 * @author Mauro Carniel
 * @version 1.0
 */
public class FirstPanel extends WizardInnerPanel {

  GridBagLayout gridBagLayout1 = new GridBagLayout();
  JLabel labelTitle = new JLabel();
  JRadioButton radioButtonZip = new JRadioButton();
  JRadioButton radioButtonUnzip = new JRadioButton();
  ButtonGroup buttonGroup1 = new ButtonGroup();


  public FirstPanel() {
    try {
      jbInit();
    }
    catch(Exception e) {
      e.printStackTrace();
    }
  }


  public String getPanelId() {
    return "FIRST";
  }


  /**
   * This method is automatically called by WizardPanel when the panel is showed:
   * it can be overrided to add custom logic that must be executed when the panel is showed.
   */
  public void init() {}


  private void jbInit() throws Exception {
    labelTitle.setFont(new java.awt.Font("Dialog", 1, 12));
    labelTitle.setText("Select which operation to perform");
    this.setLayout(gridBagLayout1);
    radioButtonZip.setSelected(true);
    radioButtonZip.setText("compress files to a zip file");
    radioButtonUnzip.setActionCommand("jRadioButton2");
    radioButtonUnzip.setText("decompress a zip file");
    this.add(labelTitle,  new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0
            ,GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 10, 0), 0, 0));
    this.add(radioButtonZip,  new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
    this.add(radioButtonUnzip,   new GridBagConstraints(0, 2, 1, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
    buttonGroup1.add(radioButtonZip);
    buttonGroup1.add(radioButtonUnzip);
  }
  public JRadioButton getRadioButtonZip() {
    return radioButtonZip;
  }

}