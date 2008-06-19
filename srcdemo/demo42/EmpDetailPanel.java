package demo42;

import java.sql.*;

import java.awt.*;
import javax.swing.*;
import javax.swing.border.*;

import org.openswing.swing.client.*;
import org.openswing.swing.form.client.*;
import org.openswing.swing.lookup.client.*;
import org.openswing.swing.table.columns.client.*;


/**
 * <p>Title: OpenSwing Framework</p>
 * <p>Description: Emplooyee detail panel.</p>
 * <p>Copyright: Copyright (C) 2006 Mauro Carniel</p>
 * <p> </p>
 * @author Mauro Carniel
 * @version 1.0
 */
public class EmpDetailPanel extends JPanel {

  JPanel buttonsPanel = new JPanel();
  DateControl controlDate = new DateControl();
  GridBagLayout gridBagLayout1 = new GridBagLayout();
  LabelControl labelDate = new LabelControl();
  ComboBoxControl controlSex = new ComboBoxControl();
  LabelControl labelEmpCode = new LabelControl();
  CurrencyControl controlCurrency = new CurrencyControl();
  TextControl controlempCode = new TextControl();

  private Form mainPanel = new Form();
  FlowLayout flowLayout1 = new FlowLayout();
  EditButton editButton = new EditButton();
  ReloadButton reloadButton = new ReloadButton();
  SaveButton saveButton = new SaveButton();

  CodLookupControl controlLookup = new CodLookupControl();
  TextControl controlDeptDescr = new TextControl();
  private Connection conn = null;
  TitledBorder titledBorder1;
  TitledBorder titledBorder2;
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
  TitledBorder titledBorder3;
  TitledBorder titledBorder4;
  BorderLayout borderLayout1 = new BorderLayout();
  JPanel buttons2Panel = new JPanel();
  FlowLayout flowLayout2 = new FlowLayout();
  EditButton editButton1 = new EditButton();
  SaveButton saveButton1 = new SaveButton();
  ReloadButton reloadButton1 = new ReloadButton();
  GridControl grid = new GridControl();
  ComboColumn colDay = new ComboColumn();
  TimeColumn colStartMorningHour = new TimeColumn();
  TimeColumn colEndMorningHour = new TimeColumn();
  TimeColumn colStartAfternoonHour = new TimeColumn();
  TimeColumn colEndAfternoonHour = new TimeColumn();


  public EmpDetailPanel(Connection conn) {
    try {
      this.conn = conn;
      jbInit();


      WorkingDaysController gridController = new WorkingDaysController(conn);
      grid.setGridDataLocator(gridController);
      grid.setController(gridController);

      setPreferredSize(new Dimension(590,480));

    }
    catch(Exception e) {
      e.printStackTrace();
    }
  }



  private void jbInit() throws Exception {
    titledBorder1 = new TitledBorder("");
    titledBorder2 = new TitledBorder("");
    titledBorder3 = new TitledBorder("");
    titledBorder4 = new TitledBorder("");
    mainPanel.setVOClassName("demo42.EmpVO");
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
    mainPanel.setBorder(titledBorder1);
    mainPanel.setEditButton(editButton);
    mainPanel.setReloadButton(reloadButton);
    mainPanel.setSaveButton(saveButton);
    saveButton.setEnabled(false);
    controlLookup.setAttributeName("");
    controlLookup.setCanCopy(true);
    controlLookup.setMaxCharacters(5);
    controlLookup.setRequired(true);
    controlLookup.setAttributeName("deptCode");
    controlDeptDescr.setAttributeName("deptDescription");
    controlDeptDescr.setCanCopy(true);
    controlDeptDescr.setEnabledOnInsert(false);
    controlDeptDescr.setEnabledOnEdit(false);
    controlDate.setCanCopy(true);
    controlDate.setRequired(false);
    controlDate.setAttributeName("hireDate");
    titledBorder1.setTitle("Title");
    titledBorder1.setTitleColor(Color.blue);
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
    controlCodTask.setAttributeName("taskCode");
    controlTaskDescr.setAttributeName("taskDescription");
    controlTaskDescr.setEnabledOnInsert(false);
    controlTaskDescr.setEnabledOnEdit(false);
    whPanel.setBorder(titledBorder2);
    whPanel.setPreferredSize(new Dimension(132, 250));
    whPanel.setLayout(borderLayout1);
    titledBorder2.setTitleColor(Color.blue);
    titledBorder2.setTitle("Working hours");
    buttons2Panel.setLayout(flowLayout2);
    flowLayout2.setAlignment(FlowLayout.LEFT);
    editButton1.setText("editButton1");
    saveButton1.setText("saveButton1");
    reloadButton1.setText("reloadButton1");
    grid.setEditButton(editButton1);
    grid.setReloadButton(reloadButton1);
    grid.setSaveButton(saveButton1);
    grid.setValueObjectClassName("demo42.WorkingDayVO");
    grid.setAutoLoadData(false);
    grid.setVisibleStatusPanel(false);
    colDay.setDomainId("DAYS");
    colDay.setColumnName("day");
    colDay.setColumnSortable(false);
    colDay.setSortVersus(org.openswing.swing.util.java.Consts.ASC_SORTED);
    colDay.setSortingOrder(1);
    colStartMorningHour.setColumnName("startMorningHour");
    colStartMorningHour.setColumnRequired(false);
    colStartMorningHour.setEditableOnEdit(true);
    colEndMorningHour.setColumnName("endMorningHour");
    colEndMorningHour.setColumnRequired(false);
    colEndMorningHour.setEditableOnEdit(true);
    colStartAfternoonHour.setColumnName("startAfternoonHour");
    colStartAfternoonHour.setColumnRequired(false);
    colStartAfternoonHour.setEditableOnEdit(true);
    colEndAfternoonHour.setColumnName("endAfternoonHour");
    colEndAfternoonHour.setColumnRequired(false);
    colEndAfternoonHour.setEditableOnEdit(true);
    this.setLayout(new BorderLayout());
    this.add(buttonsPanel, BorderLayout.NORTH);
    buttonsPanel.add(editButton, null);
    buttonsPanel.add(reloadButton, null);
    buttonsPanel.add(saveButton, null);
    this.add(mainPanel, BorderLayout.CENTER);
    mainPanel.add(controlempCode,              new GridBagConstraints(1, 0, 3, 1, 1.0, 0.0
            ,GridBagConstraints.NORTHWEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 0), 0, 0));
    mainPanel.add(controlSex,       new GridBagConstraints(4, 0, 1, 1, 0.0, 0.0
            ,GridBagConstraints.NORTHWEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
    mainPanel.add(controlFName,               new GridBagConstraints(1, 1, 1, 1, 1.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 0, 0));
    mainPanel.add(controlLName,            new GridBagConstraints(3, 1, 3, 1, 1.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 0, 0));
    mainPanel.add(controlCurrency,                new GridBagConstraints(1, 2, 4, 1, 0.0, 0.0
            ,GridBagConstraints.NORTHWEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 0), 0, 0));
    mainPanel.add(controlDate,    new GridBagConstraints(3, 2, 2, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 0), 50, 0));
    mainPanel.add(controlLookup,        new GridBagConstraints(1, 4, 1, 1, 1.0, 0.0
            ,GridBagConstraints.NORTHWEST, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 0), 0, 0));
    mainPanel.add(controlCodTask,        new GridBagConstraints(1, 5, 1, 2, 1.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 0), 0, 0));
    mainPanel.add(controlDeptDescr,                   new GridBagConstraints(2, 4, 3, 2, 1.0, 0.0
            ,GridBagConstraints.NORTHWEST, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 0, 5), 0, 0));
    mainPanel.add(controlTaskDescr,       new GridBagConstraints(2, 6, 3, 1, 1.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 0, 0));
    mainPanel.add(labelDate,   new GridBagConstraints(2, 2, 1, 1, 0.0, 0.0
            ,GridBagConstraints.NORTHWEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
    mainPanel.add(labelLName,            new GridBagConstraints(2, 1, 1, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 0), 0, 0));
    mainPanel.add(labelEmpCode,             new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0
            ,GridBagConstraints.NORTHWEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
    mainPanel.add(labelFName,              new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
    mainPanel.add(labelSalary,         new GridBagConstraints(0, 2, 1, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
    mainPanel.add(labelSex,         new GridBagConstraints(2, 0, 1, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
    mainPanel.add(labelTask,         new GridBagConstraints(0, 5, 1, 2, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
    mainPanel.add(labelDeptCode,        new GridBagConstraints(0, 4, 1, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
    this.add(whPanel, BorderLayout.SOUTH);
    whPanel.add(buttons2Panel, BorderLayout.NORTH);
    buttons2Panel.add(editButton1, null);
    buttons2Panel.add(reloadButton1, null);
    buttons2Panel.add(saveButton1, null);
    whPanel.add(grid,  BorderLayout.CENTER);
    grid.getColumnContainer().add(colDay, null);
    grid.getColumnContainer().add(colStartMorningHour, null);
    grid.getColumnContainer().add(colEndMorningHour, null);
    grid.getColumnContainer().add(colStartAfternoonHour, null);
    grid.getColumnContainer().add(colEndAfternoonHour, null);

    controlempCode.setAttributeName("empCode");
    controlempCode.setRequired(true);
//    controlDate.setDateType(Consts.TYPE_DATE_TIME);
//    controlDate.setTimeFormat(Resources.H_MM_AAA);



    LookupController lookupController = new DeptLookupController(conn);
    controlLookup.setLookupController(lookupController);
    controlLookup.setAutoCompletitionWaitTime(1000);

    LookupController lookupController2 = new TaskLookupController(conn);
    controlCodTask.setLookupController(lookupController2);
    controlCodTask.setAutoCompletitionWaitTime(1000);

  }


  public Form getMainPanel() {
    return mainPanel;
  }


  public SaveButton getSaveButton() {
    return saveButton;
  }


  public EditButton getEditButton() {
    return editButton;
  }


  public CurrencyControl getControlCurrency() {
    return controlCurrency;
  }


  public GridControl getGrid() {
    return grid;
  }


  public void setEnableGridButtons(boolean enabled) {
    if (enabled) {
      editButton1.setEnabled(enabled);
      reloadButton1.setEnabled(enabled);
    }
    else {
      editButton1.setEnabled(enabled);
      saveButton1.setEnabled(enabled);
      reloadButton1.setEnabled(enabled);
    }
  }


  /**
   * Callback method invoked by closeFrame.
   * @return <code>true</code> allows the closing operation to continue, <code>false</code> the closing operation will be interrupted
   */
  protected boolean beforeCloseFrame() {
//    grid = null;
    return true;
  }


}
