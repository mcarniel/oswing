package demo25;

import javax.swing.*;
import org.openswing.swing.client.*;
import java.awt.*;
import org.openswing.swing.table.columns.client.*;
import org.openswing.swing.lookup.client.LookupController;
import java.sql.*;
import java.awt.event.*;
import org.openswing.swing.table.java.*;


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
  TextColumn colCity = new TextColumn();
  ProgressBarColumn colPopulation = new ProgressBarColumn();
  InsertButton insertButton = new InsertButton();
  private Connection conn = null;
  EditButton editButton = new EditButton();
  NavigatorBar navigatorBar1 = new NavigatorBar();
  ExportButton exportButton1 = new ExportButton();


  public GridFrame(Connection conn,GridFrameController controller) {
    this.conn = conn;
    try {
      jbInit();
      setSize(450,300);
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
    grid.setValueObjectClassName("demo25.TestVO");
    grid.setOrderWithLoadData(false);
    colCity.setColumnFilterable(true);
    colCity.setColumnName("city");
    colCity.setColumnSortable(true);
    colCity.setPreferredWidth(200);

    colPopulation.setColumnFilterable(true);
    colPopulation.setColumnName("population");
    colPopulation.setColumnSortable(true);
    colPopulation.setPreferredWidth(180);
    colPopulation.setMaxValue(10000000);
    colPopulation.setColor(Color.red);

    insertButton.setText("insertButton1");
    this.getContentPane().add(grid, BorderLayout.CENTER);
    this.getContentPane().add(buttonsPanel, BorderLayout.NORTH);
    buttonsPanel.add(insertButton, null);
    buttonsPanel.add(editButton, null);
    buttonsPanel.add(reloadButton, null);
    buttonsPanel.add(deleteButton, null);
    buttonsPanel.add(navigatorBar1, null);
    buttonsPanel.add(exportButton1, null);
    grid.getColumnContainer().add(colCity, null);
    grid.getColumnContainer().add(colPopulation, null);
  }
  public GridControl getGrid() {
    return grid;
  }




}
