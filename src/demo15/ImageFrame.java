package demo15;


import javax.swing.*;
import java.awt.*;
import org.openswing.swing.client.*;
import java.util.*;
import org.openswing.swing.util.client.ClientSettings;
import org.openswing.swing.internationalization.java.EnglishOnlyResourceFactory;
import org.openswing.swing.form.model.client.VOModel;
import org.openswing.swing.form.client.Form;
import org.openswing.swing.form.client.FormController;
import org.openswing.swing.util.java.Consts;
import java.sql.*;
import org.openswing.swing.message.receive.java.*;
import org.openswing.swing.lookup.client.LookupController;
import org.openswing.swing.lookup.client.LookupDataLocator;
import org.openswing.swing.internationalization.java.Resources;
import org.openswing.swing.mdi.client.InternalFrame;
import javax.swing.border.*;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.io.File;


/**
 * <p>Title: OpenSwing Framework</p>
 * <p>Description: Image detail frame.</p>
 * @author Mauro Carniel
 * @version 1.0
 */
public class ImageFrame extends JFrame {

  private Form mainPanel = new Form();
  BorderLayout borderLayout1 = new BorderLayout();
  JPanel buttonsPanel = new JPanel();
  FlowLayout flowLayout1 = new FlowLayout();
  EditButton editButton1 = new EditButton();
  ReloadButton reloadButton1 = new ReloadButton();
  SaveButton saveButton1 = new SaveButton();
  GridBagLayout gridBagLayout1 = new GridBagLayout();
  LabelControl labelImageName = new LabelControl();
  TextControl controlImageName = new TextControl();
  ImageControl controlImage = new ImageControl();


  public ImageFrame(FormController dataController) {
      try {
        mainPanel.setFormController(dataController);
        setSize(590,440);
        jbInit();

        controlImage.addActionListener(new ActionListener() {
          public void actionPerformed(ActionEvent e) {
            File f = new File(e.getActionCommand());
            controlImageName.setValue(f.getName());
          }
        });

      }
      catch(Exception e) {
        e.printStackTrace();
      }

  }


  public Form getMainPanel() {
    return mainPanel;
  }


  public ImageFrame() {
    try {
      jbInit();
    }
    catch(Exception e) {
      e.printStackTrace();
    }
  }


  private void jbInit() throws Exception {
    this.getContentPane().setLayout(borderLayout1);
    buttonsPanel.setLayout(flowLayout1);
    flowLayout1.setAlignment(FlowLayout.LEFT);
    mainPanel.setEditButton(editButton1);
    mainPanel.setReloadButton(reloadButton1);
    mainPanel.setSaveButton(saveButton1);
    mainPanel.setVOClassName("demo15.ImageVO");
    mainPanel.setLayout(gridBagLayout1);
    labelImageName.setLabel("image name");
    controlImageName.setAttributeName("imageName");
    controlImageName.setRequired(true);
    controlImageName.setEnabledOnInsert(false);
    controlImageName.setEnabledOnEdit(false);
    controlImage.setAttributeName("image");
    controlImage.setRequired(true);
    this.getContentPane().add(buttonsPanel,  BorderLayout.NORTH);
    this.getContentPane().add(mainPanel, BorderLayout.CENTER);
    mainPanel.add(labelImageName,  new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
    buttonsPanel.add(editButton1, null);
    buttonsPanel.add(saveButton1, null);
    buttonsPanel.add(reloadButton1, null);
    mainPanel.add(controlImageName,   new GridBagConstraints(1, 0, 1, 1, 1.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 0, 0));
    mainPanel.add(controlImage,   new GridBagConstraints(0, 1, 2, 1, 1.0, 1.0
            ,GridBagConstraints.NORTHWEST, GridBagConstraints.BOTH, new Insets(5, 5, 5, 5), 0, 0));
  }


}
