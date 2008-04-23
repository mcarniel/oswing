package demo36;

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
 * <p>Description: Demo Application: detail employee frame.</p>
 * <p>Copyright: Copyright (C) 2006 Mauro Carniel</p>
 * <p> </p>
 * @author Mauro Carniel
 * @version 1.0
 */
public class EmpDetailFrame extends InternalFrame {

  JPanel buttonsPanel = new JPanel();
  DateControl controlDate = new DateControl();
  GridBagLayout gridBagLayout1 = new GridBagLayout();
  LabelControl labelDate = new LabelControl();
  ComboBoxControl controlSex = new ComboBoxControl();
  LabelControl labelEmpCode = new LabelControl();
  CurrencyControl controlCurrency = new CurrencyControl();
  TextControl controlempCode = new TextControl();

  private Form mainPanel = new Form();
  InsertButton insertButton = new InsertButton();
  FlowLayout flowLayout1 = new FlowLayout();
  EditButton editButton = new EditButton();
  ReloadButton reloadButton = new ReloadButton();
  SaveButton saveButton = new SaveButton();
  DeleteButton deleteButton = new DeleteButton();

  CodLookupControl controlLookup = new CodLookupControl();
  TextControl controlDeptDescr = new TextControl();
  CopyButton copyButton = new CopyButton();
  TextAreaControl controlNote = new TextAreaControl();
  LabelControl labelFName = new LabelControl();
  LabelControl labelLName = new LabelControl();
  TextControl controlFName = new TextControl();
  TextControl controlLName = new TextControl();
  LabelControl labelSex = new LabelControl();
  LabelControl labelSalary = new LabelControl();
  LabelControl labelTask = new LabelControl();
  LabelControl labelDeptCode = new LabelControl();
  CodLookupControl controlCodTask = new CodLookupControl();
  TextControl controlTaskDescr = new TextControl();
  JPanel whPanel = new JPanel();
  BorderLayout borderLayout1 = new BorderLayout();

  NavigatorBar navigatorBar = new NavigatorBar();
  private EmpDetailFrameController dataController = null;


  public EmpDetailFrame(EmpDetailFrameController dataController) {
    this.dataController = dataController;
    try {
      jbInit();

      mainPanel.setFormController(dataController);

      // link the parent grid to the current Form...
      HashSet pk = new HashSet();
      pk.add("empCode"); // pk for Form is based on one only attribute...
      mainPanel.linkGrid(dataController.getGridFrame().getGrid(),pk,true,true,true,navigatorBar);

      setSize(590,600);
      setMinimumSize(new Dimension(590,600));

      mainPanel.setCreateInnerVO(false);

    }
    catch(Exception e) {
      e.printStackTrace();
    }
  }



  private void jbInit() throws Exception {
    mainPanel.setCreateInnerVO(true);
    setTitle("Employee");
    mainPanel.setVOClassName("demo36.EmpVO");
    mainPanel.setLayout(gridBagLayout1);
    labelDate.setText("hire date");
    labelEmpCode.setText("empCode");
    controlSex.setCanCopy(true);
    controlSex.setAttributeName("sex");
    controlSex.setDomainId("SEX");
    controlSex.setLinkLabel(labelSex);
    controlSex.setRequired(true);
    controlCurrency.setCanCopy(true);
    controlCurrency.setDecimals(2);
    controlCurrency.setMaxValue(1000.0);
    controlCurrency.setMinValue(-1000.0);
    controlCurrency.setRequired(false);
    controlCurrency.setAttributeName("salary");
    controlempCode.setMaxCharacters(5);
    controlempCode.setTrimText(true);
    controlempCode.setUpperCase(true);
    controlempCode.setEnabledOnEdit(false);
    controlempCode.setAttributeName("empCode");
    buttonsPanel.setLayout(flowLayout1);
    flowLayout1.setAlignment(FlowLayout.LEFT);
    mainPanel.setInsertButton(insertButton);
    mainPanel.setCopyButton(copyButton);
    mainPanel.setEditButton(editButton);
    mainPanel.setReloadButton(reloadButton);
    mainPanel.setDeleteButton(deleteButton);
    mainPanel.setSaveButton(saveButton);
    saveButton.setEnabled(false);
    controlLookup.setAttributeName("");
    controlLookup.setCanCopy(true);
    controlLookup.setMaxCharacters(5);
    controlLookup.setRequired(true);
    controlLookup.setAttributeName("dept.deptCode");
    controlDeptDescr.setAttributeName("dept.description");
    controlDeptDescr.setCanCopy(true);
    controlDeptDescr.setEnabledOnInsert(false);
    controlDeptDescr.setEnabledOnEdit(false);
    controlDate.setCanCopy(true);
    controlDate.setRequired(false);
    controlDate.setAttributeName("hireDate");
    copyButton.setText("copyButton1");
    controlNote.setAttributeName("note");
    labelFName.setRequestFocusEnabled(true);
    labelFName.setText("firstName");
    labelLName.setText("lastName");
    controlFName.setLinkLabel(labelFName);
    controlFName.setRequired(true);
    controlFName.setAttributeName("firstName");
    controlLName.setRequired(true);
    controlLName.setAttributeName("lastName");
    labelSex.setRequestFocusEnabled(true);
    labelSex.setText("sex");
    labelSalary.setText("salary");
    labelTask.setText("task");
    labelDeptCode.setText("department");
    controlCodTask.setMaxCharacters(5);
    controlCodTask.setRequired(true);
    controlCodTask.setAttributeName("task.taskCode");
    controlTaskDescr.setAttributeName("task.description");
    controlTaskDescr.setEnabledOnInsert(false);
    controlTaskDescr.setEnabledOnEdit(false);
    whPanel.setPreferredSize(new Dimension(132, 250));
    whPanel.setLayout(borderLayout1);
    this.getContentPane().add(buttonsPanel, BorderLayout.NORTH);
    buttonsPanel.add(insertButton, null);
    buttonsPanel.add(copyButton, null);
    buttonsPanel.add(editButton, null);
    buttonsPanel.add(reloadButton, null);
    buttonsPanel.add(saveButton, null);
    buttonsPanel.add(deleteButton, null);
    buttonsPanel.add(navigatorBar, null);
    this.getContentPane().add(mainPanel, BorderLayout.CENTER);
    mainPanel.add(controlempCode,            new GridBagConstraints(1, 0, 3, 1, 1.0, 0.0
            ,GridBagConstraints.NORTHWEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 0), 0, 0));
    mainPanel.add(controlSex,     new GridBagConstraints(4, 0, 1, 1, 0.0, 0.0
            ,GridBagConstraints.NORTHWEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
    mainPanel.add(controlFName,             new GridBagConstraints(1, 1, 1, 1, 1.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 0, 0));
    mainPanel.add(labelLName,          new GridBagConstraints(2, 1, 1, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 0), 0, 0));
    mainPanel.add(controlLName,          new GridBagConstraints(3, 1, 3, 1, 1.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 0, 0));
    mainPanel.add(controlCurrency,              new GridBagConstraints(1, 2, 5, 1, 0.0, 0.0
            ,GridBagConstraints.NORTHWEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
    mainPanel.add(controlDate,          new GridBagConstraints(1, 3, 4, 1, 0.0, 0.0
            ,GridBagConstraints.NORTHWEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 0), 0, 0));
    mainPanel.add(controlLookup,      new GridBagConstraints(1, 4, 1, 1, 1.0, 0.0
            ,GridBagConstraints.NORTHWEST, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 0), 0, 0));
    mainPanel.add(controlDeptDescr,                 new GridBagConstraints(2, 4, 3, 2, 1.0, 0.0
            ,GridBagConstraints.NORTHWEST, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 0, 5), 0, 0));
    mainPanel.add(controlCodTask,      new GridBagConstraints(1, 5, 1, 2, 1.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 0), 0, 0));
    mainPanel.add(controlTaskDescr,     new GridBagConstraints(2, 6, 3, 1, 1.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 0, 0));
    mainPanel.add(controlNote,              new GridBagConstraints(0, 7, 6, 1, 1.0, 1.0
            ,GridBagConstraints.NORTHWEST, GridBagConstraints.BOTH, new Insets(5, 5, 5, 5), 0, 0));
    mainPanel.add(labelEmpCode,           new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0
            ,GridBagConstraints.NORTHWEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
    mainPanel.add(labelDate,           new GridBagConstraints(0, 3, 1, 1, 0.0, 0.0
            ,GridBagConstraints.NORTHWEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
    mainPanel.add(labelFName,            new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
    mainPanel.add(labelSalary,       new GridBagConstraints(0, 2, 1, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
    mainPanel.add(labelSex,       new GridBagConstraints(2, 0, 1, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
    mainPanel.add(labelTask,       new GridBagConstraints(0, 5, 1, 2, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
    mainPanel.add(labelDeptCode,      new GridBagConstraints(0, 4, 1, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
    this.getContentPane().add(whPanel, BorderLayout.SOUTH);
    controlempCode.setAttributeName("empCode");
    controlempCode.setRequired(true);
//    controlDate.setDateType(Consts.TYPE_DATE_TIME);
//    controlDate.setTimeFormat(Resources.H_MM_AAA);



    LookupController lookupController = new DeptLookupController(dataController.getContext());
    controlLookup.setLookupController(lookupController);

    LookupController lookupController2 = new TaskLookupController(dataController.getContext());
    controlCodTask.setLookupController(lookupController2);

  }


  public Form getMainPanel() {
    return mainPanel;
  }



}
