package demo45;

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
public class UserRolesFrame extends InternalFrame {

  private Connection conn = null;
  GridControl usersGrid = new GridControl();
  GridControl userRolesGrid = new GridControl();
  JPanel topPanel = new JPanel();
  JPanel bottomPanel = new JPanel();
  BorderLayout borderLayout1 = new BorderLayout();
  BorderLayout borderLayout2 = new BorderLayout();
  JPanel usersButtonsPanel = new JPanel();
  JPanel userRolesButtonsPanel = new JPanel();
  FlowLayout flowLayout1 = new FlowLayout();
  FlowLayout flowLayout2 = new FlowLayout();
  TextColumn colUsername = new TextColumn();
  TextColumn colPassword = new TextColumn();
  TextColumn colDescription = new TextColumn();
  ComboColumn colRoleId = new ComboColumn();
  InsertButton insertButton = new InsertButton();
  ReloadButton reloadButton = new ReloadButton();
  EditButton editButton = new EditButton();
  SaveButton saveButton = new SaveButton();
  DeleteButton deleteButton = new DeleteButton();

  InsertButton insert2Button = new InsertButton();
  ReloadButton reload2Button = new ReloadButton();
  SaveButton save2Button = new SaveButton();
  DeleteButton delete2Button = new DeleteButton();

  JSplitPane splitPane = new JSplitPane();


  public UserRolesFrame(Connection conn,UserRolesController controller) {
    this.conn = conn;
    try {
      jbInit();
      setSize(580,500);
      userRolesGrid.setController(controller);
      userRolesGrid.setGridDataLocator(controller);
      userRolesGrid.setAutoLoadData(false);

      UsersController usersController = new UsersController(conn,this);
      usersGrid.setController(usersController);
      usersGrid.setGridDataLocator(usersController);
      MDIFrame.add(this);
      setTitle(ClientSettings.getInstance().getResources().getResource("users"));

    }
    catch(Exception e) {
      e.printStackTrace();
    }
  }


  public void reloadData() {
    usersGrid.reloadData();
  }


  private void jbInit() throws Exception {
    colRoleId.setDomainId("ROLES");
    colRoleId.setColumnName("roleId");
    colRoleId.setEditableOnInsert(true);
    colRoleId.setPreferredWidth(400);

    usersButtonsPanel.setLayout(flowLayout1);
    flowLayout1.setAlignment(FlowLayout.LEFT);
    usersGrid.setDeleteButton(deleteButton);
    usersGrid.setEditButton(editButton);
    usersGrid.setInsertButton(insertButton);
    usersGrid.setReloadButton(reloadButton);
    usersGrid.setSaveButton(saveButton);
    usersGrid.setValueObjectClassName("demo45.UserVO");

    userRolesGrid.setValueObjectClassName("demo45.UserRoleVO");
    userRolesGrid.setDeleteButton(delete2Button);
    userRolesGrid.setInsertButton(insert2Button);
    userRolesGrid.setReloadButton(reload2Button);
    userRolesGrid.setSaveButton(save2Button);

    userRolesButtonsPanel.setLayout(flowLayout2);
    flowLayout2.setAlignment(FlowLayout.LEFT);
    userRolesGrid.setDeleteButton(delete2Button);
    userRolesGrid.setInsertButton(insert2Button);
    userRolesGrid.setReloadButton(reload2Button);
    userRolesGrid.setSaveButton(save2Button);

    colUsername.setColumnFilterable(true);
    colUsername.setColumnName("username");
    colUsername.setColumnSortable(true);
    colUsername.setEditableOnInsert(true);
    colUsername.setMaxCharacters(5);
    colUsername.setTrimText(true);
    colUsername.setUpperCase(true);
    colDescription.setColumnName("description");
    colDescription.setColumnRequired(false);
    colDescription.setEditableOnEdit(true);
    colDescription.setEditableOnInsert(true);
    colDescription.setPreferredWidth(350);

    colPassword.setColumnName("password");
    colPassword.setEditableOnEdit(true);
    colPassword.setEditableOnInsert(true);
    colPassword.setPreferredWidth(120);
    colPassword.setEncryptText(true);

    splitPane.setOrientation(JSplitPane.VERTICAL_SPLIT);
    splitPane.setDividerLocation(300);
    splitPane.add(topPanel,JSplitPane.TOP);
    splitPane.add(bottomPanel,JSplitPane.BOTTOM);

    topPanel.setLayout(borderLayout1);
    bottomPanel.setLayout(borderLayout2);

    topPanel.add(usersButtonsPanel, BorderLayout.NORTH);
    topPanel.add(usersGrid, BorderLayout.CENTER);
    usersButtonsPanel.add(insertButton, null);
    usersButtonsPanel.add(editButton, null);
    usersButtonsPanel.add(reloadButton, null);
    usersButtonsPanel.add(saveButton, null);
    usersButtonsPanel.add(deleteButton, null);
    usersGrid.getColumnContainer().add(colUsername, null);
    usersGrid.getColumnContainer().add(colPassword, null);
    usersGrid.getColumnContainer().add(colDescription, null);

    bottomPanel.add(userRolesButtonsPanel, BorderLayout.NORTH);
    bottomPanel.add(userRolesGrid, BorderLayout.CENTER);
    userRolesButtonsPanel.add(insert2Button, null);
    userRolesButtonsPanel.add(reload2Button, null);
    userRolesButtonsPanel.add(save2Button, null);
    userRolesButtonsPanel.add(delete2Button, null);
    userRolesGrid.getColumnContainer().add(colRoleId, null);

    this.getContentPane().add(splitPane,BorderLayout.CENTER);
  }
  public GridControl getUserRolesGrid() {
    return userRolesGrid;
  }
  public GridControl getUsersGrid() {
    return usersGrid;
  }


  public void setButtonsEnabled(boolean enabled) {
    delete2Button.setEnabled(enabled);
    insert2Button.setEnabled(enabled);
    reload2Button.setEnabled(enabled);
    if (!enabled)
      save2Button.setEnabled(false);
  }


}

