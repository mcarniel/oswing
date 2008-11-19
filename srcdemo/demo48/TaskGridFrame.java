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
 * <p>Description: Grid Frame for tasks.</p>
 * <p>Copyright: Copyright (C) 2006 Mauro Carniel</p>
 * <p> </p>
 * @author Mauro Carniel
 * @version 1.0
 */
public class TaskGridFrame extends InternalFrame {
  GridControl grid = new GridControl();
  TextColumn colDeptCode = new TextColumn();
  TextColumn colDescription = new TextColumn();
  private Connection conn = null;


  public TaskGridFrame(Connection conn,TaskGridFrameController controller) {
    this.conn = conn;
    try {
      jbInit();
      setSize(500,300);
      grid.setController(controller);
      grid.setGridDataLocator(controller);
      setTitle(ClientSettings.getInstance().getResources().getResource("tasks"));
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
    grid.setFunctionId("getTasks");
//    grid.setImportButton(ClientSettings.MDI_TOOLBAR.getImportButton());
//    grid.setCopyButton(ClientSettings.MDI_TOOLBAR.getCopyButton());
//    grid.setDeleteButton(ClientSettings.MDI_TOOLBAR.getDeleteButton());
//    grid.setEditButton(ClientSettings.MDI_TOOLBAR.getEditButton());
//    grid.setExportButton(ClientSettings.MDI_TOOLBAR.getExportButton());
//    grid.setInsertButton(ClientSettings.MDI_TOOLBAR.getInsertButton());
//    grid.setReloadButton(ClientSettings.MDI_TOOLBAR.getReloadButton());
//    grid.setSaveButton(ClientSettings.MDI_TOOLBAR.getSaveButton());
    ClientSettings.MDI_TOOLBAR.bindGrid(grid);

    grid.setValueObjectClassName("demo48.TaskVO");
    colDeptCode.setColumnFilterable(true);
    colDeptCode.setColumnName("taskCode");
    colDeptCode.setColumnSortable(true);
    colDeptCode.setEditableOnInsert(true);
    colDeptCode.setMaxCharacters(5);
    colDeptCode.setTrimText(true);
    colDeptCode.setUpperCase(true);
    colDescription.setColumnDuplicable(true);
    colDescription.setColumnName("description");
    colDescription.setEditableOnEdit(true);
    colDescription.setEditableOnInsert(true);
    colDescription.setPreferredWidth(350);
    this.getContentPane().add(grid, BorderLayout.CENTER);
    grid.getColumnContainer().add(colDeptCode, null);
    grid.getColumnContainer().add(colDescription, null);


  }


}

