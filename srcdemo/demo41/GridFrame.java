package demo41;

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
  IntegerColumn colProdId = new IntegerColumn();
  TextColumn colName = new TextColumn();
  ComboVOColumn colUnitName = new ComboVOColumn();
  InsertButton insertButton = new InsertButton();
  private Connection conn = null;
  EditButton editButton = new EditButton();
  NavigatorBar navigatorBar1 = new NavigatorBar();
  ExportButton exportButton1 = new ExportButton();
  SaveButton saveButton1 = new SaveButton();


  public GridFrame(Connection conn,GridFrameController controller) {
    super.setDefaultCloseOperation(super.EXIT_ON_CLOSE);
    this.conn = conn;
    try {
      jbInit();
      setSize(550,400);
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
    navigatorBar1.setShowPaginationButtons(false);
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
    grid.setValueObjectClassName("demo41.Products");
    grid.setOrderWithLoadData(false);
    colProdId.setColumnFilterable(true);
    colProdId.setColumnRequired(false);
    colProdId.setColumnName("id");
    colProdId.setColumnSortable(true);
    colProdId.setEditableOnEdit(false);
    colProdId.setEditableOnInsert(false);
    colProdId.setPreferredWidth(80);
    colName.setColumnFilterable(true);
    colName.setColumnName("pname");
    colName.setColumnSortable(true);
    colName.setEditableOnEdit(true);
    colName.setEditableOnInsert(true);
    colName.setPreferredWidth(150);
    colName.setSortVersus(org.openswing.swing.util.java.Consts.ASC_SORTED);
    colName.setSortingOrder(1);
    colUnitName.setColumnName("units.unitName");
    colUnitName.setEditableOnEdit(true);
    colUnitName.setEditableOnInsert(true);
    colUnitName.setPreferredWidth(200);
    insertButton.setText("insertButton1");
    this.getContentPane().add(grid, BorderLayout.CENTER);
    this.getContentPane().add(buttonsPanel, BorderLayout.NORTH);
    buttonsPanel.add(insertButton, null);
    buttonsPanel.add(editButton, null);
    buttonsPanel.add(reloadButton, null);
    buttonsPanel.add(saveButton1, null);
    buttonsPanel.add(deleteButton, null);
    buttonsPanel.add(navigatorBar1, null);
    buttonsPanel.add(exportButton1, null);
    grid.getColumnContainer().add(colProdId, null);
    grid.getColumnContainer().add(colName, null);
    grid.getColumnContainer().add(colUnitName, null);
    this.setTitle(ClientSettings.getInstance().getResources().getResource("products"));

    UnitsDataLocator comboLocator = new UnitsDataLocator(conn);
    colUnitName.setComboDataLocator(comboLocator);
    colUnitName.setComboValueObjectClassName("demo41.Units");
    colUnitName.setAllColumnVisible(false);
    colUnitName.setVisibleColumn("id",true);
    colUnitName.setVisibleColumn("unitName",true);
    colUnitName.setPreferredWidthColumn("id",50);
    colUnitName.setPreferredWidthColumn("unitName",140);

    colUnitName.addCombo2ParentLink("id","units.id");
    colUnitName.setForeignKeyAttributeName("unitName");

  }


  public GridControl getGrid() {
    return grid;
  }




}
