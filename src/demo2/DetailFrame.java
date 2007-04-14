package demo2;

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


/**
 * <p>Title: OpenSwing Framework</p>
 * <p>Description: Demo Application: detail frame containing several input controls.</p>
 * <p>Copyright: Copyright (C) 2006 Mauro Carniel</p>
 * <p> </p>
 * @author Mauro Carniel
 * @version 1.0
 */
public class DetailFrame extends InternalFrame {

  JPanel buttonsPanel = new JPanel();
  DateControl controlDate = new DateControl();
  GridBagLayout gridBagLayout1 = new GridBagLayout();
  CheckBoxControl controlCheck = new CheckBoxControl();
  LabelControl labelDate = new LabelControl();
  ComboBoxControl controlComboBox = new ComboBoxControl();
  LabelControl labelCombo = new LabelControl();
  CurrencyControl controlCurrency = new CurrencyControl();
  NumericControl controlNumeric = new NumericControl();
  TextControl controlText = new TextControl();
  LabelControl labelCheckBox = new LabelControl();

  private Form mainPanel = new Form();
  InsertButton insertButton = new InsertButton();
  FlowLayout flowLayout1 = new FlowLayout();
  EditButton editButton = new EditButton();
  ReloadButton reloadButton = new ReloadButton();
  SaveButton saveButton = new SaveButton();
  DeleteButton deleteButton = new DeleteButton();

  CodLookupControl controlLookup = new CodLookupControl();
  TextControl descrLookupControl = new TextControl();
  private Connection conn = null;
  CopyButton copyButton = new CopyButton();
  TextAreaControl controlTA = new TextAreaControl();
  TitledBorder titledBorder1;
  TitledBorder titledBorder2;


  public DetailFrame(Connection conn,FormController dataController) {
    try {
      this.conn = conn;
      jbInit();

      mainPanel.setFormController(dataController);


      setSize(590,440);

    }
    catch(Exception e) {
      e.printStackTrace();
    }
  }



  private void jbInit() throws Exception {
    titledBorder1 = new TitledBorder("");
    titledBorder2 = new TitledBorder("");
    mainPanel.setVOClassName("demo2.TestVO");
    mainPanel.setLayout(gridBagLayout1);
    labelCheckBox.setText("this text will be translated");
    labelDate.setText("date");
    labelCombo.setText("combobox");
    controlComboBox.setCanCopy(true);
    controlComboBox.setDomainId("ORDERSTATE");
    controlComboBox.setLinkLabel(labelCombo);
    controlComboBox.setRequired(false);
    controlCurrency.setCanCopy(true);
    controlCurrency.setDecimals(2);
    controlCurrency.setMaxValue(1000.0);
    controlCurrency.setMinValue(-1000.0);
    controlCurrency.setRequired(false);
    controlNumeric.setCanCopy(true);
    controlNumeric.setDecimals(3);
    controlNumeric.setRequired(true);
    controlText.setMaxCharacters(5);
    controlText.setTrimText(true);
    controlText.setUpperCase(true);
    controlText.setEnabledOnEdit(false);
    buttonsPanel.setLayout(flowLayout1);
    flowLayout1.setAlignment(FlowLayout.LEFT);
    mainPanel.setBorder(titledBorder1);
    mainPanel.setInsertButton(insertButton);
    mainPanel.setCopyButton(copyButton);
    mainPanel.setEditButton(editButton);
    mainPanel.setReloadButton(reloadButton);
    mainPanel.setDeleteButton(deleteButton);
    mainPanel.setSaveButton(saveButton);
    saveButton.setEnabled(false);
    controlLookup.setAttributeName("lookupValue");
    controlLookup.setCanCopy(true);
    controlLookup.setMaxCharacters(5);
    controlLookup.setRequired(true);
    descrLookupControl.setAttributeName("descrLookupValue");
    descrLookupControl.setCanCopy(true);
    descrLookupControl.setEnabledOnInsert(false);
    descrLookupControl.setEnabledOnEdit(false);
    controlDate.setCanCopy(true);
    controlDate.setRequired(false);
    copyButton.setText("copyButton1");
    controlCheck.setCanCopy(true);
    controlTA.setAttributeName("taValue");
    titledBorder1.setTitle("Title");
    titledBorder1.setTitleColor(Color.blue);
    this.getContentPane().add(buttonsPanel,  BorderLayout.NORTH);
    buttonsPanel.add(insertButton, null);
    buttonsPanel.add(copyButton, null);
    buttonsPanel.add(editButton, null);
    buttonsPanel.add(reloadButton, null);
    buttonsPanel.add(saveButton, null);
    buttonsPanel.add(deleteButton, null);
    this.getContentPane().add(mainPanel, BorderLayout.CENTER);
    mainPanel.add(controlDate,      new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0
            ,GridBagConstraints.NORTHWEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
    mainPanel.add(labelCheckBox,     new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0
            ,GridBagConstraints.NORTHWEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
    mainPanel.add(controlCheck,     new GridBagConstraints(1, 1, 1, 1, 0.0, 0.0
            ,GridBagConstraints.NORTHWEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
    mainPanel.add(labelDate,  new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0
            ,GridBagConstraints.NORTHWEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
    mainPanel.add(controlComboBox,    new GridBagConstraints(1, 2, 1, 1, 0.0, 0.0
            ,GridBagConstraints.NORTHWEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
    mainPanel.add(labelCombo,   new GridBagConstraints(0, 2, 1, 1, 0.0, 0.0
            ,GridBagConstraints.NORTHWEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
    mainPanel.add(controlCurrency,   new GridBagConstraints(1, 3, 1, 1, 0.0, 0.0
            ,GridBagConstraints.NORTHWEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
    mainPanel.add(controlNumeric,   new GridBagConstraints(1, 4, 1, 1, 0.0, 0.0
            ,GridBagConstraints.NORTHWEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
    mainPanel.add(controlText,     new GridBagConstraints(1, 6, 1, 1, 1.0, 0.0
            ,GridBagConstraints.NORTHWEST, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 0, 0));
    mainPanel.add(controlLookup,     new GridBagConstraints(0, 7, 1, 1, 0.0, 0.0
            ,GridBagConstraints.NORTHWEST, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 0), 0, 0));
    mainPanel.add(descrLookupControl,    new GridBagConstraints(1, 7, 1, 1, 1.0, 0.0
            ,GridBagConstraints.NORTHWEST, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 0, 0));
    mainPanel.add(controlTA,   new GridBagConstraints(0, 8, 2, 1, 1.0, 1.0
            ,GridBagConstraints.NORTHWEST, GridBagConstraints.BOTH, new Insets(5, 5, 5, 5), 0, 0));

    controlText.setAttributeName("stringValue");
    controlText.setRequired(true);
    controlDate.setAttributeName("dateValue");
    controlCheck.setAttributeName("checkValue");
    controlCurrency.setAttributeName("currencyValue");
    controlNumeric.setAttributeName("numericValue");
    controlComboBox.setAttributeName("comboValue");
//    controlDate.setDateType(Consts.TYPE_DATE_TIME);
//    controlDate.setTimeFormat(Resources.H_MM_AAA);


    LookupController lookupController = new DemoLookupController(conn);
    controlLookup.setLookupController(lookupController);


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


  public CurrencyControl getControlCurrency() {
    return controlCurrency;
  }


}
