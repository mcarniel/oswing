package demo26;

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
  TextControl controlCustomerCode = new TextControl();
  GridBagLayout gridBagLayout1 = new GridBagLayout();
  TextControl controlName = new TextControl();
  LabelControl labelName = new LabelControl();
  ComboBoxVOControl controlCity = new ComboBoxVOControl();
  LabelControl labelCity = new LabelControl();
  TextControl controlZipCode = new TextControl();
  TextControl controlState = new TextControl();
  TextControl controlAddress = new TextControl();
  TextControl controlSurname = new TextControl();
  LabelControl labelSurname = new LabelControl();
  LabelControl labelCustomerCode = new LabelControl();
  LabelControl labelZipCode = new LabelControl();
  LabelControl labelState = new LabelControl();
  LabelControl labelAddress = new LabelControl();

  LabelControl labelPricelist = new LabelControl();
  LabelControl labelStartDate = new LabelControl();
  LabelControl labelEndDate = new LabelControl();
  LabelControl labelNote = new LabelControl();
  ListVOControl controlList = new ListVOControl();
  DateControl controlStartDate = new DateControl();
  DateControl controlEndDate = new DateControl();
  TextAreaControl controlNote = new TextAreaControl();

  private Form mainPanel = new Form();
  InsertButton insertButton = new InsertButton();
  FlowLayout flowLayout1 = new FlowLayout();
  EditButton editButton = new EditButton();
  ReloadButton reloadButton = new ReloadButton();
  SaveButton saveButton = new SaveButton();
  DeleteButton deleteButton = new DeleteButton();

  private Connection conn = null;
  CopyButton copyButton = new CopyButton();
  NavigatorBar navigatorBar = new NavigatorBar();


  public DetailFrame(Connection conn,DetailFrameController dataController) {
    try {
      this.conn = conn;
      jbInit();

      mainPanel.setFormController(dataController);

      // link the parent grid to the current Form...
      HashSet pk = new HashSet();
      pk.add("customerCode"); // pk for Form is based on one only attribute...
      mainPanel.linkGrid(dataController.getGridFrame().getGrid(),pk,true,true,true,navigatorBar);


      setSize(650,430);
      setVisible(true);
    }
    catch(Exception e) {
      e.printStackTrace();
    }
  }




  private void jbInit() throws Exception {
    this.setTitle(ClientSettings.getInstance().getResources().getResource("customer"));

    mainPanel.setVOClassName("demo26.DetailTestVO");
    mainPanel.setLayout(gridBagLayout1);
    labelSurname.setText("surname");
    labelName.setText("name");
    labelCity.setText("city");
    controlCity.setCanCopy(true);
    controlCity.setLinkLabel(labelCity);
    controlCity.setRequired(true);
    controlZipCode.setCanCopy(true);
    controlZipCode.setRequired(false);
    controlZipCode.setEnabledOnEdit(false);
    controlZipCode.setEnabledOnInsert(false);
    controlState.setEnabledOnEdit(false);
    controlState.setTextAlignment(SwingConstants.LEFT);
    controlState.setEnabledOnInsert(false);
    controlState.setCanCopy(true);
    controlState.setRequired(true);
    labelCustomerCode.setToolTipText("");
    labelCustomerCode.setText("customerCode");
    controlCustomerCode.setTrimText(true);
    controlCustomerCode.setUpperCase(true);
    controlCustomerCode.setEnabledOnEdit(false);
    buttonsPanel.setLayout(flowLayout1);
    flowLayout1.setAlignment(FlowLayout.LEFT);
    mainPanel.setInsertButton(insertButton);
    mainPanel.setCopyButton(copyButton);
    mainPanel.setEditButton(editButton);
    mainPanel.setReloadButton(reloadButton);
    mainPanel.setDeleteButton(deleteButton);
    mainPanel.setSaveButton(saveButton);
    saveButton.setEnabled(false);
    controlCustomerCode.setLinkLabel(labelCustomerCode);
    controlCustomerCode.setMaxCharacters(10);
    controlCustomerCode.setRequired(true);
    copyButton.setText("copyButton1");
    labelState.setText("state");
    labelZipCode.setText("zipCode");
    labelAddress.setText("address");
    controlSurname.setTextAlignment(SwingConstants.LEFT);
    this.getContentPane().add(buttonsPanel,  BorderLayout.NORTH);
    buttonsPanel.add(insertButton, null);
    buttonsPanel.add(copyButton, null);
    buttonsPanel.add(editButton, null);
    buttonsPanel.add(reloadButton, null);
    buttonsPanel.add(saveButton, null);
    buttonsPanel.add(deleteButton, null);
    buttonsPanel.add(navigatorBar, null);
    this.getContentPane().add(mainPanel, BorderLayout.CENTER);
    mainPanel.add(controlCustomerCode,               new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0
            ,GridBagConstraints.NORTHWEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
    mainPanel.add(controlCity,          new GridBagConstraints(1, 2, 3, 1, 0.0, 0.0
            ,GridBagConstraints.NORTHWEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
    mainPanel.add(labelCity,            new GridBagConstraints(0, 2, 1, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 0, 0));
    mainPanel.add(controlZipCode,           new GridBagConstraints(1, 3, 1, 1, 0.0, 0.0
            ,GridBagConstraints.NORTHWEST, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 0, 0));
    mainPanel.add(controlAddress,         new GridBagConstraints(1, 4, 3, 1, 0.0, 0.0
            ,GridBagConstraints.NORTHWEST, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 0, 0));
    mainPanel.add(controlSurname,          new GridBagConstraints(3, 1, 1, 1, 1.0, 0.0
            ,GridBagConstraints.NORTHWEST, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 0, 5), 0, 0));
    mainPanel.add(controlName,   new GridBagConstraints(1, 1, 1, 1, 0.0, 0.0
            ,GridBagConstraints.NORTHWEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
    mainPanel.add(labelSurname,  new GridBagConstraints(2, 1, 1, 1, 0.0, 0.0
            ,GridBagConstraints.NORTHWEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
    mainPanel.add(labelName,  new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0
            ,GridBagConstraints.NORTHWEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
    mainPanel.add(labelCustomerCode,  new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0
            ,GridBagConstraints.NORTHWEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
    mainPanel.add(labelZipCode,   new GridBagConstraints(0, 3, 1, 1, 0.0, 0.0
            ,GridBagConstraints.NORTHWEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
    mainPanel.add(controlState,  new GridBagConstraints(3, 3, 1, 1, 0.0, 0.0
            ,GridBagConstraints.NORTHWEST, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 0), 0, 0));
    mainPanel.add(labelState,  new GridBagConstraints(2, 3, 1, 1, 0.0, 0.0
            ,GridBagConstraints.NORTHWEST, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 0), 0, 0));
    mainPanel.add(labelAddress,  new GridBagConstraints(0, 4, 1, 1, 0.0, 0.0
            ,GridBagConstraints.NORTHWEST, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 0), 0, 0));

    mainPanel.add(labelPricelist,  new GridBagConstraints(0, 5, 1, 1, 0.0, 0.0
            ,GridBagConstraints.NORTHWEST, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 0), 0, 0));
    mainPanel.add(controlList,  new GridBagConstraints(1, 5, 1, 2, 1.0, 1.0
            ,GridBagConstraints.NORTHWEST, GridBagConstraints.BOTH, new Insets(5, 5, 5, 0), 0, 0));
    mainPanel.add(labelStartDate,  new GridBagConstraints(2, 5, 1, 1, 0.0, 0.0
            ,GridBagConstraints.NORTHWEST, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 0), 0, 0));
    mainPanel.add(labelEndDate,  new GridBagConstraints(2, 6, 1, 1, 0.0, 0.0
            ,GridBagConstraints.NORTHWEST, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 0), 0, 0));
    mainPanel.add(controlStartDate,  new GridBagConstraints(3, 5, 1, 1, 0.0, 0.0
            ,GridBagConstraints.NORTHWEST, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 0), 0, 0));
    mainPanel.add(controlEndDate,  new GridBagConstraints(3, 6, 1, 1, 0.0, 0.0
            ,GridBagConstraints.NORTHWEST, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 0), 0, 0));
    mainPanel.add(labelNote, new GridBagConstraints(0, 7, 1, 1, 0.0, 0.0
            ,GridBagConstraints.NORTHWEST, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 0), 0, 0));
    mainPanel.add(controlNote, new GridBagConstraints(1, 7, 3, 2, 1.0, 1.0
            ,GridBagConstraints.NORTHWEST, GridBagConstraints.BOTH, new Insets(5, 5, 5, 0), 0, 0));

    controlSurname.setAttributeName("surname");
    controlSurname.setRequired(true);
    controlCustomerCode.setAttributeName("customerCode");
    controlName.setAttributeName("name");
    controlAddress.setAttributeName("address");
    controlZipCode.setAttributeName("zipCode");
    controlState.setAttributeName("state");
    controlCity.setAttributeName("customerCity");
    controlCity.setForeignKeyAttributeName("city");

    DemoComboDataLocator comboLocator = new DemoComboDataLocator(conn);
    controlCity.setComboDataLocator(comboLocator);
    controlCity.setComboValueObjectClassName("demo26.TestComboVO");
    controlCity.setAllColumnVisible(false);
    controlCity.setVisibleColumn("city",true);
    controlCity.setVisibleColumn("state",true);
    controlCity.setPreferredWidthColumn("city",200);
    controlCity.setPreferredWidthColumn("state",120);

//    controlCity.addCombo2ParentLink("city","customerCity");
    controlCity.addCombo2ParentLink("state","state");
    controlCity.addCombo2ParentLink("zipCode","zipCode");


    labelPricelist.setText("pricelist");
    labelStartDate.setText("startDate");
    labelEndDate.setText("endDate");
    labelNote.setText("note");
    controlStartDate.setAttributeName("startDate");
    controlStartDate.setEnabledOnEdit(false);
    controlStartDate.setEnabledOnInsert(false);
    controlEndDate.setAttributeName("endDate");
    controlEndDate.setEnabledOnEdit(false);
    controlEndDate.setEnabledOnInsert(false);
    controlNote.setAttributeName("note");
    controlNote.setEnabledOnEdit(false);
    controlNote.setEnabledOnInsert(false);

    controlList.setAttributeName("pricelistCode");
    DemoListDataLocator listLocator = new DemoListDataLocator(conn);
    controlList.setListDataLocator(listLocator);
    controlList.setListValueObjectClassName("demo26.TestListVO");
    controlList.setAllColumnVisible(false);
    controlList.setVisibleColumn("pricelistCode",true);
    controlList.setVisibleColumn("description",true);
    controlList.setPreferredWidthColumn("pricelistCode",30);
    controlList.setPreferredWidthColumn("description",200);
    controlList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

    controlList.addList2ParentLink("startDate","startDate");
    controlList.addList2ParentLink("endDate","endDate");
    controlList.addList2ParentLink("note","note");

  }


  public Form getMainPanel() {
    return mainPanel;
  }


  public SaveButton getSaveButton() {
    return saveButton;
  }


  public DeleteButton getDeleteButton() {
    return deleteButton;
  }


  public EditButton getEditButton() {
    return editButton;
  }


}
