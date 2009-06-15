package demo27;

import javax.swing.*;
import org.openswing.swing.client.*;
import java.awt.*;
import org.openswing.swing.table.columns.client.*;
import org.openswing.swing.lookup.client.LookupController;
import java.sql.*;
import java.awt.event.*;
import org.openswing.swing.table.java.*;
import org.openswing.swing.util.client.ClientSettings;
import org.openswing.swing.table.columns.client.ComboVOColumn;


/**
 * <p>Title: OpenSwing Framework</p>
 * <p>Description: Grid Frame</p>
 * <p>Copyright: Copyright (C) 2006 Mauro Carniel</p>
 * <p> </p>
 * @author Mauro Carniel
 * @version 1.0
 */
public class GridFrame extends JFrame {
  GridControl grid = new GridControl();
  JPanel buttonsPanel = new JPanel();
  ReloadButton reloadButton = new ReloadButton();
  DeleteButton deleteButton = new DeleteButton();
  FlowLayout flowLayout1 = new FlowLayout();
  TextColumn colCustomerCode = new TextColumn();
  TextColumn colName = new TextColumn();
  TextColumn colSurname = new TextColumn();
  ComboVOColumn colCity = new ComboVOColumn();
  TextColumn colZipCode = new TextColumn();
  TextColumn colAddress = new TextColumn();
  InsertButton insertButton = new InsertButton();
  private Connection conn = null;
  EditButton editButton = new EditButton();
  NavigatorBar navigatorBar1 = new NavigatorBar();
  ExportButton exportButton1 = new ExportButton();
  SaveButton saveButton1 = new SaveButton();


  public GridFrame(Connection conn,GridFrameController controller) {
    this.conn = conn;
    try {
      jbInit();
      setSize(750,400);
      grid.setController(controller);
      grid.setGridDataLocator(controller);

      setVisible(true);

    }
    catch(Exception e) {
      e.printStackTrace();
    }
  }


  public void reloadData() {
    grid.reloadData();
  }


  private void jbInit() throws Exception {
    buttonsPanel.setLayout(flowLayout1);
    flowLayout1.setAlignment(FlowLayout.LEFT);
    grid.setDefaultQuickFilterCriteria(org.openswing.swing.util.java.Consts.CONTAINS);
    grid.setDeleteButton(deleteButton);
    grid.setEditButton(editButton);
    grid.setExportButton(exportButton1);
    grid.setFunctionId("F1");
    grid.setInsertButton(insertButton);
    grid.setMaxSortedColumns(2);
    grid.setNavBar(navigatorBar1);
    grid.setReloadButton(reloadButton);
    grid.setSaveButton(saveButton1);
    grid.setValueObjectClassName("demo27.TestVO");
    grid.setOrderWithLoadData(false);
    grid.setAllowColumnsSortingInEdit(true);
    colCustomerCode.setColumnFilterable(true);
    colCustomerCode.setColumnName("customerCode");
    colCustomerCode.setColumnSortable(true);
    colCustomerCode.setEditableOnEdit(false);
    colCustomerCode.setEditableOnInsert(true);
    colCustomerCode.setPreferredWidth(90);
    colName.setColumnFilterable(true);
    colName.setColumnName("name");
    colName.setColumnSortable(true);
    colName.setEditableOnEdit(true);
    colName.setEditableOnInsert(true);
    colName.setPreferredWidth(80);
    colName.setSortVersus(org.openswing.swing.util.java.Consts.ASC_SORTED);
    colName.setSortingOrder(1);
    colSurname.setColumnName("surname");
    colSurname.setEditableOnEdit(true);
    colSurname.setEditableOnInsert(true);
    colSurname.setPreferredWidth(80);
    colCity.setColumnName("city");
    colCity.setEditableOnEdit(true);
    colCity.setEditableOnInsert(true);
    colCity.setPreferredWidth(250);
    colCity.setSortVersus(org.openswing.swing.util.java.Consts.ASC_SORTED);
    colCity.setSortingOrder(2);
    insertButton.setText("insertButton1");
    colAddress.setEditableOnEdit(true);
    colAddress.setColumnName("address");
    colAddress.setEditableOnInsert(true);
    colAddress.setPreferredWidth(200);
    this.getContentPane().add(grid, BorderLayout.CENTER);
    this.getContentPane().add(buttonsPanel, BorderLayout.NORTH);
    buttonsPanel.add(insertButton, null);
    buttonsPanel.add(editButton, null);
    buttonsPanel.add(reloadButton, null);
    buttonsPanel.add(saveButton1, null);
    buttonsPanel.add(deleteButton, null);
    buttonsPanel.add(navigatorBar1, null);
    buttonsPanel.add(exportButton1, null);
    grid.getColumnContainer().add(colCustomerCode, null);
    grid.getColumnContainer().add(colName, null);
    grid.getColumnContainer().add(colSurname, null);
    grid.getColumnContainer().add(colCity, null);
    grid.getColumnContainer().add(colZipCode, null);
    grid.getColumnContainer().add(colAddress, null);
    colZipCode.setColumnName("zipCode");
    colZipCode.setPreferredWidth(80);

    this.setTitle(ClientSettings.getInstance().getResources().getResource("customers"));


    DemoComboDataLocator comboLocator = new DemoComboDataLocator(conn);
    colCity.setComboDataLocator(comboLocator);
    colCity.setComboValueObjectClassName("demo27.TestComboVO");
    colCity.setAllColumnVisible(false);
    colCity.setVisibleColumn("city",true);
    colCity.setVisibleColumn("state",true);
    colCity.setPreferredWidthColumn("city",165);
    colCity.setPreferredWidthColumn("state",120);

    colCity.addCombo2ParentLink("state","state");
    colCity.addCombo2ParentLink("zipCode","zipCode");



  }
  public GridControl getGrid() {
    return grid;
  }




}
