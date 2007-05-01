package demo14;

import javax.swing.*;
import org.openswing.swing.util.client.*;
import org.openswing.swing.internationalization.java.*;
import org.openswing.swing.wizard.client.*;
import java.awt.*;
import java.awt.event.*;


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
  GridBagLayout gridBagLayout1 = new GridBagLayout();
  JLabel labelFilesToCompress = new JLabel();
  JLabel zipLabel = new JLabel();
  JTextField controlZip = new JTextField();
  JButton buttonSel = new JButton();
  JTextField controlFolder = new JTextField();
  JButton buttonFolder = new JButton();


  public ThirdPanel() {
    try {
      jbInit();
    }
    catch(Exception e) {
      e.printStackTrace();
    }
  }


  /**
   * This method is automatically called by WizardPanel when the panel is showed:
   * it can be overrided to add custom logic that must be executed when the panel is showed.
   */
  public void init() {}


  public String getPanelId() {
    return "THIRD";
  }
  private void jbInit() throws Exception {
    labelFilesToCompress.setText("Folder to compress");
    this.setLayout(gridBagLayout1);
    zipLabel.setText("Zip File ");
    controlZip.setText("");
    buttonSel.setText("...");
    buttonSel.addActionListener(new ThirdPanel_buttonSel_actionAdapter(this));
    controlFolder.setText("");
    buttonFolder.setText("...");
    buttonFolder.addActionListener(new ThirdPanel_buttonFolder_actionAdapter(this));
    this.add(labelFilesToCompress,       new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
    this.add(zipLabel,      new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(15, 5, 5, 0), 0, 0));
    this.add(controlZip,       new GridBagConstraints(1, 1, 1, 1, 1.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(15, 5, 5, 5), 0, 0));
    this.add(buttonSel,    new GridBagConstraints(2, 1, 1, 1, 0.0, 0.0
            ,GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(15, 0, 0, 5), 0, 0));
    this.add(controlFolder,   new GridBagConstraints(1, 0, 1, 1, 1.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 0, 0));
    this.add(buttonFolder,   new GridBagConstraints(2, 0, 1, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 0, 5, 5), 0, 0));
  }


  void buttonSel_actionPerformed(ActionEvent e) {
    JFileChooser f = new JFileChooser();
    f.setFileSelectionMode(f.DIRECTORIES_ONLY);
    int s = f.showDialog(null,"Select");
    if (s==f.APPROVE_OPTION && f.getSelectedFile().isDirectory())
      controlZip.setText(f.getSelectedFile().getAbsolutePath());

  }


  void buttonFolder_actionPerformed(ActionEvent e) {
    JFileChooser f = new JFileChooser();
    f.setFileSelectionMode(f.DIRECTORIES_ONLY);
    int s = f.showDialog(null,"Select");
    if (s==f.APPROVE_OPTION && f.getSelectedFile().isDirectory())
      controlFolder.setText(f.getSelectedFile().getAbsolutePath());

  }


  /**
   * This method could be overrided.
   * @return image name; null if no image is required
   */
  public String getImageName() {
    return "setup3.gif";
  }

}

class ThirdPanel_buttonSel_actionAdapter implements java.awt.event.ActionListener {
  ThirdPanel adaptee;

  ThirdPanel_buttonSel_actionAdapter(ThirdPanel adaptee) {
    this.adaptee = adaptee;
  }
  public void actionPerformed(ActionEvent e) {
    adaptee.buttonSel_actionPerformed(e);
  }
}

class ThirdPanel_buttonFolder_actionAdapter implements java.awt.event.ActionListener {
  ThirdPanel adaptee;

  ThirdPanel_buttonFolder_actionAdapter(ThirdPanel adaptee) {
    this.adaptee = adaptee;
  }
  public void actionPerformed(ActionEvent e) {
    adaptee.buttonFolder_actionPerformed(e);
  }
}
