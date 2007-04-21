package demo14;

import javax.swing.*;
import org.openswing.swing.util.client.*;
import org.openswing.swing.internationalization.java.*;
import org.openswing.swing.wizard.client.*;
import java.awt.*;
import java.awt.event.*;


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
  GridBagLayout gridBagLayout1 = new GridBagLayout();
  JLabel labelZipFile = new JLabel();
  JTextField controlFileName = new JTextField();
  JButton buttonSelect = new JButton();
  JLabel labelDestPath = new JLabel();
  JTextField controlDestPath = new JTextField();
  JButton buttonDestPath = new JButton();


  public SecondPanel() {
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
    return "SECOND";
  }


  private void jbInit() throws Exception {
    labelZipFile.setToolTipText("");
    labelZipFile.setText("File to unzip");
    this.setLayout(gridBagLayout1);
    controlFileName.setToolTipText("");
    controlFileName.setText("");
    controlFileName.setColumns(20);
    buttonSelect.setText("...");
    buttonSelect.addActionListener(new SecondPanel_buttonSelect_actionAdapter(this));
    labelDestPath.setText("Path where unzip");
    controlDestPath.setText("");
    controlDestPath.setColumns(20);
    buttonDestPath.setText("...");
    buttonDestPath.addActionListener(new SecondPanel_buttonDestPath_actionAdapter(this));
    this.add(labelZipFile,    new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 25, 0, 5), 0, 0));
    this.add(controlFileName,    new GridBagConstraints(1, 0, 1, 1, 1.0, 0.0
            ,GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(5, 0, 5, 0), 0, 0));
    this.add(buttonSelect,   new GridBagConstraints(2, 0, 1, 1, 0.0, 0.0
            ,GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 5, 0, 25), 0, 0));
    this.add(labelDestPath,    new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 25, 0, 5), 0, 0));
    this.add(controlDestPath,   new GridBagConstraints(1, 1, 1, 1, 0.0, 0.0
            ,GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(5, 0, 5, 0), 0, 0));
    this.add(buttonDestPath,  new GridBagConstraints(2, 1, 1, 1, 0.0, 0.0
            ,GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 5, 0, 25), 0, 0));
  }


  void buttonSelect_actionPerformed(ActionEvent e) {
    JFileChooser f = new JFileChooser();
    f.setFileSelectionMode(f.FILES_ONLY);
    int s = f.showDialog(null,"Select");
    if (s==f.APPROVE_OPTION && f.getSelectedFile().isFile())
      controlFileName.setText(f.getSelectedFile().getAbsolutePath());
  }


  void buttonDestPath_actionPerformed(ActionEvent e) {
    JFileChooser f = new JFileChooser();
    f.setFileSelectionMode(f.DIRECTORIES_ONLY);
    int s = f.showDialog(null,"Select");
    if (s==f.APPROVE_OPTION && f.getSelectedFile().isDirectory())
      controlDestPath.setText(f.getSelectedFile().getAbsolutePath());
  }


  public JTextField getControlDestPath() {
    return controlDestPath;
  }


  public JTextField getControlFileName() {
    return controlFileName;
  }



  /**
   * This method could be overrided.
   * @return image name; null if no image is required
   */
  public String getImageName() {
    return "setup2.gif";
  }


}

class SecondPanel_buttonSelect_actionAdapter implements java.awt.event.ActionListener {
  SecondPanel adaptee;

  SecondPanel_buttonSelect_actionAdapter(SecondPanel adaptee) {
    this.adaptee = adaptee;
  }
  public void actionPerformed(ActionEvent e) {
    adaptee.buttonSelect_actionPerformed(e);
  }
}

class SecondPanel_buttonDestPath_actionAdapter implements java.awt.event.ActionListener {
  SecondPanel adaptee;

  SecondPanel_buttonDestPath_actionAdapter(SecondPanel adaptee) {
    this.adaptee = adaptee;
  }
  public void actionPerformed(ActionEvent e) {
    adaptee.buttonDestPath_actionPerformed(e);
  }
}
