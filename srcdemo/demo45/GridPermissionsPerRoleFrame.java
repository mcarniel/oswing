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
public class GridPermissionsPerRoleFrame extends InternalFrame {

  GridControl grid = new GridControl();
  JPanel topPanel = new JPanel();
  JPanel filterPanel = new JPanel();
  JPanel buttonsPanel = new JPanel();
  GridBagLayout gridBagLayout1 = new GridBagLayout();
  GridBagLayout gridBagLayout2 = new GridBagLayout();
  ReloadButton reloadButton = new ReloadButton();
  FlowLayout flowLayout1 = new FlowLayout();
  TextColumn colDescr = new TextColumn();
  CheckBoxColumn colVis = new CheckBoxColumn();
  CheckBoxColumn colEII = new CheckBoxColumn();
  CheckBoxColumn colEIE = new CheckBoxColumn();
  CheckBoxColumn colReq = new CheckBoxColumn();
  private Connection conn = null;
  EditButton editButton = new EditButton();
  SaveButton saveButton = new SaveButton();
  ComboBoxControl controlRoles = new ComboBoxControl();
  ComboBoxControl controlFunctions = new ComboBoxControl();
  LabelControl labelRoles = new LabelControl();
  LabelControl labelFunctions = new LabelControl();


  public GridPermissionsPerRoleFrame(Connection conn,GridPermissionsPerRoleFrameController controller) {
    this.conn = conn;
    try {
      jbInit();
      setSize(600,600);
      grid.setController(controller);
      grid.setGridDataLocator(controller);
      grid.setAutoLoadData(false);

      MDIFrame.add(this);
      setTitle(ClientSettings.getInstance().getResources().getResource("grid permissions"));
    }
    catch(Exception e) {
      e.printStackTrace();
    }
  }


  public void reloadData() {
    grid.reloadData();
  }


  private void jbInit() throws Exception {
    topPanel.setLayout(gridBagLayout1);
    filterPanel.setLayout(gridBagLayout1);
    controlFunctions.addItemListener(new GridPermissionsPerRoleFrame_controlFunctions_itemAdapter(this));
    controlRoles.addItemListener(new GridPermissionsPerRoleFrame_controlRoles_itemAdapter(this));
    topPanel.add(filterPanel,              new GridBagConstraints(0, 0, 1, 1, 1.0, 0.0
            ,GridBagConstraints.NORTHWEST, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 0), 0, 0));
    topPanel.add(buttonsPanel,              new GridBagConstraints(0, 1, 1, 1, 1.0, 0.0
            ,GridBagConstraints.NORTHWEST, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 0), 0, 0));

    filterPanel.add(labelRoles,                 new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 0), 0, 0));
    filterPanel.add(labelFunctions,                 new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 0), 0, 0));
    filterPanel.add(controlFunctions,                new GridBagConstraints(1, 1, 1, 1, 1.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 0), 0, 0));
    filterPanel.add(controlRoles,  new GridBagConstraints(1, 0, 1, 1, 1.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 0), 0, 0));

    labelRoles.setLabel("roleId");
    labelFunctions.setLabel("functions");
    controlRoles.setDomainId("ROLES");
    controlFunctions.setDomainId("FUNCTIONS");
    buttonsPanel.setLayout(flowLayout1);
    flowLayout1.setAlignment(FlowLayout.LEFT);
    grid.setEditButton(editButton);
    grid.setReloadButton(reloadButton);
    grid.setSaveButton(saveButton);
    grid.setValueObjectClassName("demo45.GridPermissionsPerRoleVO");
    colDescr.setColumnFilterable(true);
    colDescr.setColumnName("description");
    colDescr.setColumnSortable(true);
    colDescr.setPreferredWidth(200);
    colVis.setColumnName("visible");
    colVis.setEditableOnEdit(true);
    colVis.setPreferredWidth(80);
    colEII.setColumnName("editableInIns");
    colEII.setEditableOnEdit(true);
    colEII.setPreferredWidth(80);
    colEIE.setColumnName("editableInEdit");
    colEIE.setEditableOnEdit(true);
    colEIE.setPreferredWidth(80);
    colReq.setColumnName("required");
    colReq.setEditableOnEdit(true);
    colReq.setPreferredWidth(80);
    this.getContentPane().add(grid, BorderLayout.CENTER);
    this.getContentPane().add(topPanel, BorderLayout.NORTH);
    buttonsPanel.add(editButton, null);
    buttonsPanel.add(reloadButton, null);
    buttonsPanel.add(saveButton, null);
    grid.getColumnContainer().add(colDescr, null);
    grid.getColumnContainer().add(colVis, null);
    grid.getColumnContainer().add(colEII, null);
    grid.getColumnContainer().add(colEIE, null);
    grid.getColumnContainer().add(colReq, null);


  }


  void controlFunctions_itemStateChanged(ItemEvent e) {
    maybeLoadData();
  }


  void controlRoles_itemStateChanged(ItemEvent e) {
    maybeLoadData();
  }


  private void maybeLoadData() {
    if (controlFunctions.getValue()!=null && controlRoles.getValue()!=null) {
      grid.reloadData();
    }
    else
      grid.clearData();
  }


  public String getFunctionId() {
    return (String)controlFunctions.getValue();
  }


  public String getRoleId() {
    return (String)controlRoles.getValue();
  }
  public GridControl getGrid() {
    return grid;
  }

}

class GridPermissionsPerRoleFrame_controlFunctions_itemAdapter implements java.awt.event.ItemListener {
  GridPermissionsPerRoleFrame adaptee;

  GridPermissionsPerRoleFrame_controlFunctions_itemAdapter(GridPermissionsPerRoleFrame adaptee) {
    this.adaptee = adaptee;
  }
  public void itemStateChanged(ItemEvent e) {
    adaptee.controlFunctions_itemStateChanged(e);
  }
}

class GridPermissionsPerRoleFrame_controlRoles_itemAdapter implements java.awt.event.ItemListener {
  GridPermissionsPerRoleFrame adaptee;

  GridPermissionsPerRoleFrame_controlRoles_itemAdapter(GridPermissionsPerRoleFrame adaptee) {
    this.adaptee = adaptee;
  }
  public void itemStateChanged(ItemEvent e) {
    adaptee.controlRoles_itemStateChanged(e);
  }
}

