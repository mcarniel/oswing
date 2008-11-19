package demo48;

import javax.swing.*;
import org.openswing.swing.client.*;
import java.awt.*;
import org.openswing.swing.table.columns.client.*;
import org.openswing.swing.lookup.client.LookupController;
import java.sql.*;
import java.awt.event.*;
import org.openswing.swing.table.java.*;
import org.openswing.swing.mdi.client.InternalFrame;
import org.openswing.swing.util.client.*;


/**
 * <p>Title: OpenSwing Framework</p>
 * <p>Description: Grid Frame</p>
 * <p>Copyright: Copyright (C) 2006 Mauro Carniel</p>
 * <p> </p>
 * @author Mauro Carniel
 * @version 1.0
 */
public class EmpGridFrame extends InternalFrame {
  GridControl grid = new GridControl();
  TextColumn colEmpCode = new TextColumn();
  TextColumn colFName = new TextColumn();
  private Connection conn = null;
  TextColumn colLName = new TextColumn();
  TextColumn colDeptCode = new TextColumn();
  TextColumn colDeptDescr = new TextColumn();


  public EmpGridFrame(Connection conn,EmpGridFrameController controller) {
    this.conn = conn;
    try {
      jbInit();
      setSize(770,300);
      grid.setController(controller);
      grid.setGridDataLocator(controller);
      setTitle("Employees");
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
    grid.setDeleteButton(ClientSettings.MDI_TOOLBAR.getDeleteButton());
    grid.setExportButton(ClientSettings.MDI_TOOLBAR.getExportButton());
    grid.setFilterButton(ClientSettings.MDI_TOOLBAR.getFilterButton());
    grid.setInsertButton(ClientSettings.MDI_TOOLBAR.getInsertButton());
    grid.setFunctionId("getEmployees");
    grid.setNavBar(ClientSettings.MDI_TOOLBAR.getNavigatorBar());
    grid.setReloadButton(ClientSettings.MDI_TOOLBAR.getReloadButton());
    grid.setValueObjectClassName("demo48.GridEmpVO");
    colEmpCode.setColumnFilterable(true);
    colEmpCode.setColumnName("empCode");
    colEmpCode.setColumnSortable(false);
    colFName.setColumnFilterable(true);
    colFName.setColumnName("firstName");
    colFName.setColumnSortable(true);
    colFName.setPreferredWidth(150);
    colLName.setColumnFilterable(true);
    colLName.setColumnName("lastName");
    colLName.setPreferredWidth(150);
    colDeptCode.setColumnFilterable(true);
    colDeptCode.setColumnName("deptCode");
    colDeptCode.setPreferredWidth(70);
    colDeptDescr.setColumnName("deptDescription");
    colDeptDescr.setPreferredWidth(200);
    this.getContentPane().add(grid, BorderLayout.CENTER);
    grid.getColumnContainer().add(colEmpCode, null);
    grid.getColumnContainer().add(colFName, null);
    grid.getColumnContainer().add(colLName, null);
    grid.getColumnContainer().add(colDeptCode, null);
    grid.getColumnContainer().add(colDeptDescr, null);


  }

  public GridControl getGrid() {
    return grid;
  }


}
