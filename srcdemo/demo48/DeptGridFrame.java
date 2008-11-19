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
import org.openswing.swing.mdi.client.MDIFrame;
import org.openswing.swing.util.client.ClientSettings;


/**
 * <p>Title: OpenSwing Framework</p>
 * <p>Description: Grid Frame for departments.</p>
 * <p>Copyright: Copyright (C) 2006 Mauro Carniel</p>
 * <p> </p>
 * @author Mauro Carniel
 * @version 1.0
 */
public class DeptGridFrame extends InternalFrame {
  GridControl grid = new GridControl();
  TextColumn colDeptCode = new TextColumn();
  TextColumn colDescription = new TextColumn();
  private Connection conn = null;
  TextColumn colAddress = new TextColumn();


  public DeptGridFrame(Connection conn,DeptFrameController controller) {
    this.conn = conn;
    try {
      jbInit();
      setSize(750,300);
      grid.setController(controller);
      grid.setGridDataLocator(controller);

      setTitle(ClientSettings.getInstance().getResources().getResource("departments"));
      MDIFrame.add(this);
    }
    catch(Exception e) {
      e.printStackTrace();
    }
  }


  public void reloadData() {
    grid.reloadData();
  }


  private void jbInit() throws Exception {
    grid.setFunctionId("getDepts");
//    grid.setCopyButton(ClientSettings.MDI_TOOLBAR.getCopyButton());
//    grid.setDeleteButton(ClientSettings.MDI_TOOLBAR.getDeleteButton());
//    grid.setEditButton(ClientSettings.MDI_TOOLBAR.getEditButton());
//    grid.setExportButton(ClientSettings.MDI_TOOLBAR.getExportButton());
//    grid.setInsertButton(ClientSettings.MDI_TOOLBAR.getInsertButton());
//    grid.setReloadButton(ClientSettings.MDI_TOOLBAR.getReloadButton());
//    grid.setSaveButton(ClientSettings.MDI_TOOLBAR.getSaveButton());

    ClientSettings.MDI_TOOLBAR.bindGrid(grid);
    grid.setMaxNumberOfRowsOnInsert(10);

    grid.setValueObjectClassName("demo48.DeptVO");
    colDeptCode.setColumnFilterable(true);
    colDeptCode.setColumnName("deptCode");
    colDeptCode.setColumnSortable(true);
    colDeptCode.setEditableOnInsert(true);
    colDeptCode.setMaxCharacters(5);
    colDeptCode.setTrimText(true);
    colDeptCode.setUpperCase(true);
    colDescription.setColumnDuplicable(true);
    colDescription.setColumnName("description");
    colDescription.setColumnSortable(true);
    colDescription.setEditableOnEdit(true);
    colDescription.setEditableOnInsert(true);
    colDescription.setPreferredWidth(200);
    colAddress.setColumnDuplicable(true);
    colAddress.setColumnName("address");
    colAddress.setColumnRequired(false);
    colAddress.setColumnSortable(true);
    colAddress.setEditableOnEdit(true);
    colAddress.setEditableOnInsert(true);
    colAddress.setMaxWidth(200);
    colAddress.setPreferredWidth(400);
    this.getContentPane().add(grid, BorderLayout.CENTER);
    grid.getColumnContainer().add(colDeptCode, null);
    grid.getColumnContainer().add(colDescription, null);
    grid.getColumnContainer().add(colAddress, null);


  }


}

