package demo17;

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
import org.openswing.swing.table.columns.client.*;


/**
 * <p>Title: OpenSwing Framework</p>
 * <p>Description: Demo Application: detail department frame.</p>
 * <p>Copyright: Copyright (C) 2006 Mauro Carniel</p>
 * <p> </p>
 * @author Mauro Carniel
 * @version 1.0
 */
public class DeptDetailFrame extends InternalFrame {

  JPanel buttonsPanel = new JPanel();
  TextControl controlAddress = new TextControl();
  GridBagLayout gridBagLayout1 = new GridBagLayout();
  LabelControl labelAddress = new LabelControl();
  TextControl controlCity = new TextControl();
  LabelControl labelDeptCode = new LabelControl();
  TextControl controlState = new TextControl();
  TextControl controlDeptCode = new TextControl();

  private Form mainPanel = new Form();
  InsertButton insertButton = new InsertButton();
  FlowLayout flowLayout1 = new FlowLayout();
  EditButton editButton = new EditButton();
  ReloadButton reloadButton = new ReloadButton();
  SaveButton saveButton = new SaveButton();
  DeleteButton deleteButton = new DeleteButton();

  CopyButton copyButton = new CopyButton();
  TextControl controlCountry = new TextControl();
  LabelControl labelDeptDescr = new LabelControl();
  TextControl controlDescr = new TextControl();
  LabelControl labelCity = new LabelControl();
  LabelControl labelCountry = new LabelControl();
  BorderLayout borderLayout1 = new BorderLayout();
  JPanel buttons2Panel = new JPanel();
  FlowLayout flowLayout2 = new FlowLayout();
  NavigatorBar navigatorBar = new NavigatorBar();
  LabelControl labelState = new LabelControl();


  public DeptDetailFrame(DeptDetailFrameController dataController) {
    try {
      jbInit();

      mainPanel.setFormController(dataController);

      // link the parent grid to the current Form...
      HashSet pk = new HashSet();
      pk.add("deptCode"); // pk for Form is based on one only attribute...
      mainPanel.linkGrid(dataController.getGridFrame().getGrid(),pk,true,true,true,navigatorBar);

      setSize(590,200);
      setMinimumSize(new Dimension(590,200));
      setMaximumSize(new Dimension(590,200));
    }
    catch(Exception e) {
      e.printStackTrace();
    }
  }



  private void jbInit() throws Exception {
    setTitle(ClientSettings.getInstance().getResources().getResource("department"));
    mainPanel.setVOClassName("demo17.DeptVO");
    mainPanel.setLayout(gridBagLayout1);
    labelAddress.setText("address");
    labelDeptCode.setText("deptCode");
    controlCity.setCanCopy(true);
    controlCity.setAttributeName("city");
    controlCity.setLinkLabel(labelCity);
    controlCity.setRequired(true);
    controlState.setCanCopy(true);
    controlState.setRequired(false);
    controlState.setAttributeName("state");
    controlDeptCode.setMaxCharacters(5);
    controlDeptCode.setTrimText(true);
    controlDeptCode.setUpperCase(true);
    controlDeptCode.setEnabledOnEdit(false);
    controlDeptCode.setAttributeName("deptCode");
    buttonsPanel.setLayout(flowLayout1);
    flowLayout1.setAlignment(FlowLayout.LEFT);
    mainPanel.setInsertButton(insertButton);
    mainPanel.setCopyButton(copyButton);
    mainPanel.setEditButton(editButton);
    mainPanel.setReloadButton(reloadButton);
    mainPanel.setDeleteButton(deleteButton);
    mainPanel.setSaveButton(saveButton);
    saveButton.setEnabled(false);
    controlAddress.setCanCopy(true);
    controlAddress.setRequired(false);
    controlAddress.setAttributeName("address");
    copyButton.setText("copyButton1");
    controlCountry.setAttributeName("country");
    labelDeptDescr.setRequestFocusEnabled(true);
    labelDeptDescr.setLabel("description");
    labelDeptDescr.setText("description");
    controlDescr.setRequired(true);
    controlDescr.setAttributeName("description");
    labelCity.setRequestFocusEnabled(true);
    labelCity.setText("city");
    labelCountry.setText("country");
    buttons2Panel.setLayout(flowLayout2);
    flowLayout2.setAlignment(FlowLayout.LEFT);
    labelState.setText("state");
    this.getContentPane().add(buttonsPanel, BorderLayout.NORTH);
    buttonsPanel.add(insertButton, null);
    buttonsPanel.add(copyButton, null);
    buttonsPanel.add(editButton, null);
    buttonsPanel.add(reloadButton, null);
    buttonsPanel.add(saveButton, null);
    buttonsPanel.add(deleteButton, null);
    buttonsPanel.add(navigatorBar, null);
    this.getContentPane().add(mainPanel, BorderLayout.CENTER);
    mainPanel.add(controlDeptCode,                       new GridBagConstraints(1, 0, 1, 1, 1.0, 0.0
            ,GridBagConstraints.NORTHWEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 0), 0, 0));
    mainPanel.add(controlCountry,                          new GridBagConstraints(3, 2, 4, 1, 0.0, 0.0
            ,GridBagConstraints.NORTHWEST, GridBagConstraints.BOTH, new Insets(5, 5, 5, 5), 0, 0));
    mainPanel.add(labelDeptCode,                     new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0
            ,GridBagConstraints.NORTHWEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
    mainPanel.add(labelCity,                  new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
    mainPanel.add(labelDeptDescr,       new GridBagConstraints(2, 0, 1, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
    mainPanel.add(controlDescr,    new GridBagConstraints(3, 0, 2, 1, 1.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 0, 0));
    mainPanel.add(controlCity,    new GridBagConstraints(1, 1, 1, 1, 0.0, 0.0
            ,GridBagConstraints.NORTHWEST, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 0, 0));
    mainPanel.add(labelAddress,   new GridBagConstraints(2, 1, 1, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
    mainPanel.add(controlAddress,    new GridBagConstraints(3, 1, 2, 1, 1.0, 0.0
            ,GridBagConstraints.NORTHWEST, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 0), 0, 0));
    mainPanel.add(controlState,    new GridBagConstraints(1, 2, 1, 1, 0.0, 0.0
            ,GridBagConstraints.NORTHWEST, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 0), 0, 0));
    mainPanel.add(labelCountry,  new GridBagConstraints(2, 2, 1, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
    mainPanel.add(labelState,  new GridBagConstraints(0, 2, 1, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
    controlDeptCode.setAttributeName("deptCode");
    controlDeptCode.setRequired(true);

  }


  public Form getMainPanel() {
    return mainPanel;
  }





}
