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

  public EmpGridFrame2(Connection conn,EmpGridFrameController2 controller) {
    this.conn = conn;
    try {
      jbInit();
      setSize(770,300);
      grid.setController(controller);
      grid.setGridDataLocator(controller);
      setTitle("Employees in editable grid");
    }
    catch(Exception e) {
      e.printStackTrace();
    }
  }


  public void reloadData() {
    grid.reloadData();
  }


  private void jbInit() throws Exception {
    grid.setAnchorLastColumn(true);
    buttonsPanel.setLayout(flowLayout1);
    flowLayout1.setAlignment(FlowLayout.LEFT);
    grid.setDeleteButton(deleteButton);
    grid.setEditButton(editButton1);
    grid.setExportButton(exportButton1);
    grid.setFilterButton(filterButton1);
    grid.setFunctionId("getEmployees2");
    grid.setInsertButton(insertButton);
    grid.setNavBar(navigatorBar1);
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
    this.getContentPane().add(grid, BorderLayout.CENTER);
    this.getContentPane().add(buttonsPanel, BorderLayout.NORTH);
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

    LookupController lookupController2 = new TaskLookupController(conn);
    colCodTask.setLookupController(lookupController2);

  }


  public GridControl getGrid() {
    return grid;
  }


}

