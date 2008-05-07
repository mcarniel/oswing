package demo38;

import java.sql.*;

import java.awt.*;
import javax.swing.*;

import org.openswing.swing.client.*;
import org.openswing.swing.mdi.client.*;
import org.openswing.swing.table.columns.client.*;


/**
 * <p>Title: OpenSwing Framework</p>
 * <p>Description: Grid Frame</p>
 * <p>Copyright: Copyright (C) 2006 Mauro Carniel</p>
 * <p> </p>
 * @author Mauro Carniel
 * @version 1.0
 */
public class UsersGridFrame extends InternalFrame {
  GridControl grid = new GridControl();
  JPanel buttonsPanel = new JPanel();
  ReloadButton reloadButton = new ReloadButton();
  DeleteButton deleteButton = new DeleteButton();
  FlowLayout flowLayout1 = new FlowLayout();
  TextColumn colUsername = new TextColumn();
  TextColumn colPassword = new TextColumn();
  TextColumn colDescription = new TextColumn();
  InsertButton insertButton = new InsertButton();
  SaveButton saveButton = new SaveButton();
  EditButton editButton = new EditButton();
  private Connection conn = null;
  ExportButton exportButton1 = new ExportButton();
  NavigatorBar navigatorBar1 = new NavigatorBar();



  public UsersGridFrame(Connection conn,UsersGridFrameController controller) {
    this.conn = conn;
    try {
      jbInit();
      setSize(750,300);
      grid.setController(controller);
      grid.setGridDataLocator(controller);

    }
    catch(Exception e) {
      e.printStackTrace();
    }
  }


  public void reloadData() {
    grid.reloadData();
  }


  private void jbInit() throws Exception {
    setTitle("users");
    buttonsPanel.setLayout(flowLayout1);
    flowLayout1.setAlignment(FlowLayout.LEFT);
    this.setSize(new Dimension(500, 300));
    grid.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
    grid.setDeleteButton(deleteButton);
    grid.setExportButton(exportButton1);
    grid.setFunctionId("USERS");
    grid.setInsertButton(null);
    grid.setNavBar(navigatorBar1);
    grid.setReloadButton(reloadButton);
    grid.setInsertButton(insertButton);
    grid.setValueObjectClassName("demo38.UserVO");
    colUsername.setColumnFilterable(true);
    colUsername.setColumnName("username");
    colUsername.setUpperCase(true);
    colUsername.setTrimText(true);
    colUsername.setColumnSortable(true);
    colPassword.setColumnName("password");
    colPassword.setEncryptText(true);
    colDescription.setColumnName("description");
    colDescription.setPreferredWidth(350);
    colDescription.setEditableOnEdit(true);
    colDescription.setEditableOnInsert(true);
    colUsername.setEditableOnEdit(false);
    colUsername.setEditableOnInsert(true);
    colPassword.setEditableOnEdit(true);
    colPassword.setEditableOnInsert(true);
    insertButton.setText("insertButton1");
    grid.setSaveButton(saveButton);
    grid.setEditButton(editButton);
    exportButton1.setText("exportButton1");
    this.getContentPane().add(grid, BorderLayout.CENTER);
    this.getContentPane().add(buttonsPanel, BorderLayout.NORTH);
    buttonsPanel.add(insertButton, null);
    buttonsPanel.add(editButton, null);
    buttonsPanel.add(saveButton, null);
    buttonsPanel.add(reloadButton, null);
    buttonsPanel.add(deleteButton, null);
    buttonsPanel.add(exportButton1, null);
    buttonsPanel.add(navigatorBar1, null);

    grid.getColumnContainer().add(colUsername, null);
    grid.getColumnContainer().add(colPassword, null);
    grid.getColumnContainer().add(colDescription, null);
  }
  public GridControl getGrid() {
    return grid;
  }



}


