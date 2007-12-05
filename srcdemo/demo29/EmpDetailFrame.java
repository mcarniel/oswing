package demo29;

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
import java.awt.event.ActionEvent;


/**
 * <p>Title: OpenSwing Framework</p>
 * <p>Description: Demo Application: detail employee frame composed of a grid control + Form panel.</p>
 * <p>Copyright: Copyright (C) 2006 Mauro Carniel</p>
 * <p> </p>
 * @author Mauro Carniel
 * @version 1.0
 */
public class EmpDetailFrame extends InternalFrame {

  GridControl grid3 = new GridControl();
  JPanel buttons3Panel = new JPanel();
  ReloadButton reloadButton3 = new ReloadButton();
  DeleteButton deleteButton3 = new DeleteButton();
  FlowLayout flowLayout3 = new FlowLayout();
  TextColumn colEmpCode = new TextColumn();
  TextColumn colFName = new TextColumn();
  InsertButton insertButton3 = new InsertButton();
  private Connection conn = null;
  ExportButton exportButton3 = new ExportButton();
  NavigatorBar navigatorBar3 = new NavigatorBar();
  TextColumn colLName = new TextColumn();
  TextColumn colDeptCode = new TextColumn();
  TextColumn colDeptDescr = new TextColumn();
  FilterButton filterButton3 = new FilterButton();


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

  JPanel gridPanel = new JPanel();
  JPanel formPanel = new JPanel();
  BorderLayout borderLayout2 = new BorderLayout();

  CodLookupControl controlLookup = new CodLookupControl();
  TextControl controlDeptDescr = new TextControl();
  CopyButton copyButton = new CopyButton();
  TextAreaControl controlNote = new TextAreaControl();
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
  NavigatorBar navigatorBar = new NavigatorBar();
  BorderLayout borderLayout3 = new BorderLayout();
  BorderLayout borderLayout4 = new BorderLayout();


  public EmpDetailFrame(Connection conn,EmpDetailController dataController) {
    try {
      this.conn = conn;
      jbInit();

      mainPanel.setFormController(dataController);

      WorkingDaysController gridController = new WorkingDaysController(conn);
      grid.setGridDataLocator(gridController);
      grid.setController(gridController);

      EmpGridController controller = new EmpGridController(conn,this);
      grid3.setController(controller);
      grid3.setGridDataLocator(controller);
      setTitle("Employees");


      setSize(750,680);
      setMinimumSize(new Dimension(750,600));

    }
    catch(Exception e) {
      e.printStackTrace();
    }
  }



  private void jbInit() throws Exception {
    buttons3Panel.setLayout(flowLayout3);
    flowLayout3.setAlignment(FlowLayout.LEFT);
    grid3.setDeleteButton(deleteButton3);
    grid3.setExportButton(exportButton3);
    grid3.setFilterButton(filterButton3);
    grid3.setFunctionId("getEmployees");
    grid3.setInsertButton(null);
    grid3.setNavBar(navigatorBar3);
    grid3.setReloadButton(reloadButton3);
    grid3.setValueObjectClassName("demo29.GridEmpVO");
    colEmpCode.setColumnFilterable(true);
    colEmpCode.setColumnName("empCode");
    colEmpCode.setColumnSortable(false);
    colFName.setColumnFilterable(true);
    colFName.setColumnName("firstName");
    colFName.setColumnSortable(true);
    colFName.setPreferredWidth(150);
    insertButton3.setText("insertButton1");
    insertButton3.addActionListener(new EmpDetailFrame_insertButton_actionAdapter(this));
    exportButton3.setText("exportButton1");
    colLName.setColumnFilterable(true);
    colLName.setColumnName("lastName");
    colLName.setPreferredWidth(150);
    colDeptCode.setColumnFilterable(true);
    colDeptCode.setColumnName("deptCode");
    colDeptCode.setPreferredWidth(70);
    colDeptDescr.setColumnName("deptDescription");
    colDeptDescr.setPreferredWidth(200);
    gridPanel.setLayout(borderLayout3);
    this.getContentPane().setLayout(borderLayout4);
    gridPanel.setPreferredSize(new Dimension(700,150));
    gridPanel.add(grid3, BorderLayout.CENTER);
    gridPanel.add(buttons3Panel,  BorderLayout.NORTH);
    buttons3Panel.add(insertButton3, null);
    buttons3Panel.add(reloadButton3, null);
    buttons3Panel.add(deleteButton3, null);
    buttons3Panel.add(exportButton3, null);
    buttons3Panel.add(filterButton3, null);
    buttons3Panel.add(navigatorBar3, null);
    grid3.getColumnContainer().add(colEmpCode, null);
    grid3.getColumnContainer().add(colFName, null);
    grid3.getColumnContainer().add(colLName, null);
    grid3.getColumnContainer().add(colDeptCode, null);
    grid3.getColumnContainer().add(colDeptDescr, null);




//    setTitle("Employee");
    titledBorder1 = new TitledBorder("");
    titledBorder2 = new TitledBorder("");
    titledBorder3 = new TitledBorder("");
    titledBorder4 = new TitledBorder("");
    mainPanel.setVOClassName("demo29.EmpVO");
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
    controlLookup.setAttributeName("deptCode");
    controlDeptDescr.setAttributeName("deptDescription");
    controlDeptDescr.setCanCopy(true);
    controlDeptDescr.setEnabledOnInsert(false);
    controlDeptDescr.setEnabledOnEdit(false);
    controlDate.setCanCopy(true);
    controlDate.setRequired(false);
    controlDate.setAttributeName("hireDate");
    copyButton.setText("copyButton1");
    controlNote.setAttributeName("note");
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
    grid.setValueObjectClassName("demo29.WorkingDayVO");
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
    buttonsPanel.add(insertButton, null);
    buttonsPanel.add(copyButton, null);
    buttonsPanel.add(editButton, null);
    buttonsPanel.add(reloadButton, null);
    buttonsPanel.add(saveButton, null);
    buttonsPanel.add(deleteButton, null);
    buttonsPanel.add(navigatorBar, null);
    mainPanel.add(controlCurrency,               new GridBagConstraints(1, 2, 5, 1, 0.0, 0.0
            ,GridBagConstraints.NORTHWEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
    mainPanel.add(controlDeptDescr,                   new GridBagConstraints(4, 2, 3, 1, 1.0, 0.0
            ,GridBagConstraints.NORTHWEST, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 0, 5), 0, 0));
    mainPanel.add(controlNote,                new GridBagConstraints(0, 6, 7, 1, 1.0, 1.0
            ,GridBagConstraints.NORTHWEST, GridBagConstraints.BOTH, new Insets(5, 5, 5, 5), 0, 0));
    mainPanel.add(labelEmpCode,            new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0
            ,GridBagConstraints.NORTHWEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
    mainPanel.add(controlempCode,             new GridBagConstraints(1, 0, 3, 1, 1.0, 0.0
            ,GridBagConstraints.NORTHWEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 0), 0, 0));
    mainPanel.add(labelDate,            new GridBagConstraints(0, 3, 1, 1, 0.0, 0.0
            ,GridBagConstraints.NORTHWEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
    mainPanel.add(labelFName,             new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
    mainPanel.add(controlDate,           new GridBagConstraints(1, 3, 4, 1, 0.0, 0.0
            ,GridBagConstraints.NORTHWEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 0), 0, 0));
    mainPanel.add(controlFName,              new GridBagConstraints(1, 1, 1, 1, 1.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 0, 0));
    mainPanel.add(labelLName,           new GridBagConstraints(2, 1, 1, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 0), 0, 0));
    mainPanel.add(controlLName,           new GridBagConstraints(3, 1, 3, 1, 1.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 0, 0));
    mainPanel.add(labelSalary,        new GridBagConstraints(0, 2, 1, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
    mainPanel.add(labelSex,        new GridBagConstraints(2, 0, 1, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
    mainPanel.add(controlLookup,        new GridBagConstraints(3, 2, 1, 1, 1.0, 0.0
            ,GridBagConstraints.NORTHWEST, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 0), 0, 0));
    mainPanel.add(controlCodTask,        new GridBagConstraints(3, 3, 1, 1, 1.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 0), 0, 0));
    mainPanel.add(controlTaskDescr,       new GridBagConstraints(4, 3, 3, 1, 1.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 0, 0));
    mainPanel.add(labelDeptCode,     new GridBagConstraints(2, 2, 1, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
    mainPanel.add(labelTask,       new GridBagConstraints(2, 3, 1, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
    mainPanel.add(controlSex,   new GridBagConstraints(3, 0, 1, 1, 0.0, 0.0
            ,GridBagConstraints.NORTHWEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
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

    formPanel.setLayout(borderLayout2);
    formPanel.add(buttonsPanel, BorderLayout.NORTH);
    formPanel.add(mainPanel, BorderLayout.CENTER);

    this.getContentPane().add(gridPanel, BorderLayout.NORTH);
    this.getContentPane().add(formPanel, BorderLayout.CENTER);
    this.getContentPane().add(whPanel, BorderLayout.SOUTH);


    LookupController lookupController = new DeptLookupController(conn);
    controlLookup.setLookupController(lookupController);

    LookupController lookupController2 = new TaskLookupController(conn);
    controlCodTask.setLookupController(lookupController2);

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



  void insertButton_actionPerformed(ActionEvent e) {
//    new EmpDetailFrameController(this,null,conn);

  }
  public GridControl getParentGrid() {
    return grid3;
  }
  public NavigatorBar getNavigatorBar() {
    return navigatorBar;
  }



}


class EmpDetailFrame_insertButton_actionAdapter implements java.awt.event.ActionListener {
  EmpDetailFrame adaptee;

  EmpDetailFrame_insertButton_actionAdapter(EmpDetailFrame adaptee) {
    this.adaptee = adaptee;
  }
  public void actionPerformed(ActionEvent e) {
    adaptee.insertButton_actionPerformed(e);
  }
}
