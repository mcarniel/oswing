package demo10;

import javax.swing.*;
import org.openswing.swing.client.*;
import java.awt.*;
import org.openswing.swing.table.columns.client.*;
import org.openswing.swing.lookup.client.LookupController;
import java.sql.*;
import java.awt.event.*;
import org.openswing.swing.table.java.*;
import org.openswing.swing.mdi.client.InternalFrame;
import javax.swing.border.TitledBorder;


/**
 * <p>Title: OpenSwing Framework</p>
 * <p>Description: Grid Frame</p>
 * <p>Copyright: Copyright (C) 2006 Mauro Carniel</p>
 * <p> </p>
 * @author Mauro Carniel
 * @version 1.0
 */
public class EmpGridFrame2 extends InternalFrame {
  GridControl grid = new GridControl();
  JPanel buttonsPanel = new JPanel();
  ReloadButton reloadButton = new ReloadButton();
  DeleteButton deleteButton = new DeleteButton();
  FlowLayout flowLayout1 = new FlowLayout();
  TextColumn colEmpCode = new TextColumn();
  TextColumn colFName = new TextColumn();
  InsertButton insertButton = new InsertButton();
  private Connection conn = null;
  ExportButton exportButton1 = new ExportButton();
  NavigatorBar navigatorBar1 = new NavigatorBar();
  TextColumn colLName = new TextColumn();
  TextColumn colDeptDescr = new TextColumn();
  FilterButton filterButton1 = new FilterButton();
  EditButton editButton1 = new EditButton();
  SaveButton saveButton1 = new SaveButton();
  CodLookupColumn colDeptCode = new CodLookupColumn();
  CodLookupColumn colCodTask = new CodLookupColumn();
  TextColumn colTaskDescr = new TextColumn();

  JPanel whPanel = new JPanel();
  TitledBorder titledBorder3;
  BorderLayout borderLayout1 = new BorderLayout();
  JPanel buttons2Panel = new JPanel();
  FlowLayout flowLayout2 = new FlowLayout();
  EditButton editButton2 = new EditButton();
  SaveButton saveButton2 = new SaveButton();
  ReloadButton reloadButton2 = new ReloadButton();
  GridControl grid2 = new GridControl();
  ComboColumn colDay = new ComboColumn();
  TimeColumn colStartMorningHour = new TimeColumn();
  TimeColumn colEndMorningHour = new TimeColumn();
  TimeColumn colStartAfternoonHour = new TimeColumn();
  TimeColumn colEndAfternoonHour = new TimeColumn();
  NavigatorBar navigatorBar2 = new NavigatorBar();
  JSplitPane splitPane = new JSplitPane();
  JPanel mainPanel = new JPanel();
  BorderLayout borderLayout = new BorderLayout();


  public EmpGridFrame2(Connection conn,EmpGridFrameController2 controller) {
    this.conn = conn;
    try {
      jbInit();
      setSize(770,550);
      grid.setController(controller);
      grid.setGridDataLocator(controller);
      setTitle("Employees in editable grid");

      WorkingDaysController gridController = new WorkingDaysController(conn);
      grid2.setGridDataLocator(gridController);
      grid2.setController(gridController);

    }
    catch(Exception e) {
      e.printStackTrace();
    }
  }


  public void reloadData() {
    grid.reloadData();
  }


  private void jbInit() throws Exception {
    splitPane.setOrientation(JSplitPane.VERTICAL_SPLIT);
    splitPane.setDividerLocation(300);

    titledBorder3 = new TitledBorder("");
    buttonsPanel.setLayout(flowLayout1);
    flowLayout1.setAlignment(FlowLayout.LEFT);
    grid.setDeleteButton(deleteButton);
    grid.setEditButton(editButton1);
    grid.setExportButton(exportButton1);
    grid.setFilterButton(filterButton1);
    grid.setFunctionId("getEmployees2");
    grid.setInsertButton(insertButton);
//    grid.setNavBar(navigatorBar1);
    grid.setReloadButton(reloadButton);
    grid.setSaveButton(saveButton1);
    grid.setValueObjectClassName("demo10.EmpVO");
    colEmpCode.setColumnFilterable(true);
    colEmpCode.setColumnName("empCode");
    colEmpCode.setColumnSortable(false);
    colEmpCode.setEditableOnInsert(true);
    colFName.setColumnFilterable(true);
    colFName.setColumnName("firstName");
    colFName.setColumnSortable(true);
    colFName.setEditableOnEdit(true);
    colFName.setEditableOnInsert(true);
    colFName.setPreferredWidth(150);
    insertButton.setText("insertButton1");
    exportButton1.setText("exportButton1");
    colLName.setColumnFilterable(true);
    colLName.setEditableOnEdit(true);
    colLName.setEditableOnInsert(true);
    colLName.setColumnName("lastName");
    colLName.setPreferredWidth(150);
    colDeptDescr.setColumnName("deptDescription");
    colDeptDescr.setPreferredWidth(200);
    filterButton1.setText("filterButton1");
    colDeptCode.setColumnName("deptCode");
    colDeptCode.setMaxCharacters(20);
    colDeptCode.setEditableOnEdit(true);
    colDeptCode.setEditableOnInsert(true);
    colDeptCode.setPreferredWidth(80);
    colCodTask.setMaxCharacters(20);
    colCodTask.setEditableOnEdit(true);
    colCodTask.setEditableOnInsert(true);
    colCodTask.setPreferredWidth(80);
    colCodTask.setColumnName("taskCode");
    colTaskDescr.setColumnName("taskDescription");
    colTaskDescr.setPreferredWidth(150);

    splitPane.add(mainPanel,JSplitPane.TOP);
    splitPane.add(whPanel,JSplitPane.BOTTOM);
    this.getContentPane().add(splitPane, BorderLayout.CENTER);
    mainPanel.setLayout(borderLayout);
    mainPanel.add(grid, BorderLayout.CENTER);
    mainPanel.add(buttonsPanel, BorderLayout.NORTH);
    buttonsPanel.add(insertButton, null);
    buttonsPanel.add(editButton1, null);
    buttonsPanel.add(saveButton1, null);
    buttonsPanel.add(reloadButton, null);
    buttonsPanel.add(deleteButton, null);
    buttonsPanel.add(exportButton1, null);
    buttonsPanel.add(filterButton1, null);
    buttonsPanel.add(navigatorBar1, null);
    grid.getColumnContainer().add(colEmpCode, null);
    grid.getColumnContainer().add(colFName, null);
    grid.getColumnContainer().add(colLName, null);
    grid.getColumnContainer().add(colDeptCode, null);
    grid.getColumnContainer().add(colDeptDescr, null);
    grid.getColumnContainer().add(colCodTask, null);
    grid.getColumnContainer().add(colTaskDescr, null);

    LookupController lookupController = new DeptLookupController(conn);
    colDeptCode.setLookupController(lookupController);
    colDeptCode.setAutoCompletitionWaitTime(1000);

    LookupController lookupController2 = new TaskLookupController(conn);
    colCodTask.setLookupController(lookupController2);
    colCodTask.setAutoCompletitionWaitTime(1000);





    whPanel.setBorder(titledBorder3);
    whPanel.setLayout(borderLayout1);
    titledBorder3.setTitleColor(Color.blue);
    titledBorder3.setTitle("Working hours");
    buttons2Panel.setLayout(flowLayout2);
    flowLayout2.setAlignment(FlowLayout.LEFT);
    grid2.setEditButton(editButton2);
    grid2.setReloadButton(reloadButton2);
    grid2.setSaveButton(saveButton2);
    grid2.setValueObjectClassName("demo10.WorkingDayVO");
    grid2.setAutoLoadData(false);
    grid2.setVisibleStatusPanel(false);
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

    whPanel.add(buttons2Panel, BorderLayout.NORTH);
    buttons2Panel.add(editButton2, null);
    buttons2Panel.add(reloadButton2, null);
    buttons2Panel.add(saveButton2, null);
    whPanel.add(grid2,  BorderLayout.CENTER);
    grid2.getColumnContainer().add(colDay, null);
    grid2.getColumnContainer().add(colStartMorningHour, null);
    grid2.getColumnContainer().add(colEndMorningHour, null);
    grid2.getColumnContainer().add(colStartAfternoonHour, null);
    grid2.getColumnContainer().add(colEndAfternoonHour, null);

//    grid.setAnchorLastColumn(true);
    grid.setLockedColumns(2);
//    grid.setAnchorLockedColumnsToLeft(false);

  }


  public GridControl getGrid() {
    return grid;
  }


  public GridControl getWDGrid() {
    return grid2;
  }


  public void setEnableGridButtons(boolean enabled) {
    if (enabled) {
      editButton2.setEnabled(enabled);
      reloadButton2.setEnabled(enabled);
    }
    else {
      editButton2.setEnabled(enabled);
      saveButton2.setEnabled(enabled);
      reloadButton2.setEnabled(enabled);
    }
  }



}

