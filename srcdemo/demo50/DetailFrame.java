package demo50;

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
import javax.swing.border.*;
import javax.swing.text.MaskFormatter;
import org.openswing.swing.lookup.client.LookupListener;
import java.awt.event.*;
import org.openswing.swing.util.client.ClientUtils;
import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.*;


/**
 * <p>Title: OpenSwing Framework</p>
 * <p>Description: Demo Application: detail frame containing several input controls.</p>
 * <p>Copyright: Copyright (C) 2006 Mauro Carniel</p>
 * <p> </p>
 * @author Mauro Carniel
 * @version 1.0
 */
public class DetailFrame extends JFrame {

  JPanel buttonsPanel = new JPanel();
  GridBagLayout gridBagLayout1 = new GridBagLayout();
  LabelControl labelCode = new LabelControl();

  private Form mainPanel = new Form();
  InsertButton insertButton = new InsertButton();
  FlowLayout flowLayout1 = new FlowLayout();
  EditButton editButton = new EditButton();
  ReloadButton reloadButton = new ReloadButton();
  SaveButton saveButton = new SaveButton();

  private Connection conn = null;
  TextControl controlDescr = new TextControl();
  JButton buttonSel = new JButton();
  TextControl controlCode = new TextControl();
  LabelControl labelFile = new LabelControl();


  public DetailFrame(Connection conn,DetailFrameController dataController) {
    try {
      super.setDefaultCloseOperation(super.EXIT_ON_CLOSE);
      this.conn = conn;
      jbInit();

      mainPanel.setFormController(dataController);

      setSize(600,150);
      ClientUtils.centerFrame(this);
      setVisible(true);
    }
    catch(Exception e) {
      e.printStackTrace();
    }
  }




  private void jbInit() throws Exception {
    mainPanel.setVOClassName("demo50.DetailTestVO");
    mainPanel.setLayout(gridBagLayout1);
    labelCode.setText("code");
    buttonsPanel.setLayout(flowLayout1);
    flowLayout1.setAlignment(FlowLayout.LEFT);
    mainPanel.setInsertButton(insertButton);
    mainPanel.setEditButton(editButton);
    mainPanel.setReloadButton(reloadButton);
    mainPanel.setSaveButton(saveButton);
    saveButton.setEnabled(false);

    buttonSel.setText("...");
    buttonSel.addActionListener(new DetailFrame_buttonSel_actionAdapter(this));
    controlDescr.setAttributeName("fileDescription");
    controlDescr.setEnabledOnInsert(false);
    controlDescr.setEnabledOnEdit(false);
    controlCode.setAttributeName("stringValue");
    controlCode.setTrimText(true);
    controlCode.setUpperCase(true);
    controlCode.setEnabledOnEdit(false);
    labelFile.setText("file");
    this.getContentPane().add(buttonsPanel,  BorderLayout.NORTH);
    buttonsPanel.add(insertButton, null);
    buttonsPanel.add(editButton, null);
    buttonsPanel.add(reloadButton, null);
    buttonsPanel.add(saveButton, null);
    this.getContentPane().add(mainPanel, BorderLayout.CENTER);
    mainPanel.add(labelCode,                         new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0
            ,GridBagConstraints.NORTHWEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));

    mainPanel.add(controlDescr,               new GridBagConstraints(1, 1, 1, 1, 1.0, 1.0
            ,GridBagConstraints.NORTHWEST, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 0, 0));
    mainPanel.add(buttonSel,               new GridBagConstraints(2, 1, 1, 1, 0.0, 0.0
            ,GridBagConstraints.NORTHWEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
    mainPanel.add(controlCode,   new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0
            ,GridBagConstraints.NORTHWEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
    mainPanel.add(labelFile,  new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0
            ,GridBagConstraints.NORTHWEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));

  }


  void buttonSel_actionPerformed(ActionEvent e) {
    JFileChooser f = new JFileChooser(".");
    int res = f.showOpenDialog(this);
    if (res==f.APPROVE_OPTION && f.getSelectedFile()!=null) {
      BufferedInputStream in = null;
      try {
        byte[] bb = new byte[ (int) f.getSelectedFile().length()];
        in = new BufferedInputStream(new FileInputStream(f.getSelectedFile()));
        in.read(bb);
        DetailTestVO vo = (DetailTestVO)mainPanel.getVOModel().getValueObject();
        vo.setFile(bb);
        controlDescr.setText(f.getSelectedFile().getPath());
      }
      catch (Exception ex) {
        ex.printStackTrace();
      }
      finally {
        try {
          in.close();
        }
        catch (Exception ex1) {
        }
      }
    }
  }


  public Form getMainPanel() {
    return mainPanel;
  }


}

class DetailFrame_buttonSel_actionAdapter implements java.awt.event.ActionListener {
  DetailFrame adaptee;

  DetailFrame_buttonSel_actionAdapter(DetailFrame adaptee) {
    this.adaptee = adaptee;
  }
  public void actionPerformed(ActionEvent e) {
    adaptee.buttonSel_actionPerformed(e);
  }
}
