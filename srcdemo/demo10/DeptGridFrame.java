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
  JPanel buttonsPanel = new JPanel();
  ReloadButton reloadButton = new ReloadButton();
  DeleteButton deleteButton = new DeleteButton();
  FlowLayout flowLayout1 = new FlowLayout();
  TextColumn colDeptCode = new TextColumn();
  TextColumn colDescription = new TextColumn();
  InsertButton insertButton = new InsertButton();
  private Connection conn = null;
  EditButton editButton = new EditButton();
  SaveButton saveButton = new SaveButton();
  CopyButton copyButton = new CopyButton();
  ExportButton exportButton = new ExportButton();
  FilterButton filterButton = new FilterButton();
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
    buttonsPanel.setLayout(flowLayout1);
    flowLayout1.setAlignment(FlowLayout.LEFT);
    grid.setCopyButton(copyButton);
    grid.setDeleteButton(deleteButton);
    grid.setEditButton(editButton);
    grid.setExportButton(exportButton);
    grid.setMaxNumberOfRowsOnInsert(10);
    grid.setInsertButton(insertButton);
    grid.setReloadButton(reloadButton);
    grid.setSaveButton(saveButton);
    grid.setFilterButton(filterButton);
    grid.setValueObjectClassName("demo10.DeptVO");
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
    colDescription.setSortVersus(org.openswing.swing.util.java.Consts.ASC_SORTED);
    insertButton.setText("insertButton1");
    editButton.setText("editButton1");
    saveButton.setText("saveButton1");
    colAddress.setColumnDuplicable(true);
    colAddress.setColumnName("address");
    colAddress.setColumnRequired(false);
    colAddress.setColumnSortable(true);
    colAddress.setEditableOnEdit(true);
    colAddress.setEditableOnInsert(true);
    colAddress.setMaxWidth(200);
    colAddress.setPreferredWidth(400);
    this.setUniqueInstance(true);
    this.getContentPane().add(grid, BorderLayout.CENTER);
    this.getContentPane().add(buttonsPanel, BorderLayout.NORTH);
    buttonsPanel.add(insertButton, null);
    buttonsPanel.add(copyButton, null);
    buttonsPanel.add(editButton, null);
    buttonsPanel.add(reloadButton, null);
    buttonsPanel.add(saveButton, null);
    buttonsPanel.add(filterButton, null);
    buttonsPanel.add(exportButton, null);
    buttonsPanel.add(deleteButton, null);
    grid.getColumnContainer().add(colDeptCode, null);
    grid.getColumnContainer().add(colDescription, null);
    grid.getColumnContainer().add(colAddress, null);


  }


  public GridControl getGrid() {
    return grid;
  }

}

