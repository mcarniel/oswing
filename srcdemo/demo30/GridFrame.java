package demo30;

import javax.swing.*;
import org.openswing.swing.client.*;
import java.awt.*;
import org.openswing.swing.table.columns.client.*;
import org.openswing.swing.lookup.client.LookupController;
import java.sql.*;
import java.awt.event.*;
import org.openswing.swing.table.java.*;
import org.openswing.swing.util.client.ClientSettings;


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
  TextColumn colCity = new TextColumn();
  InsertButton insertButton = new InsertButton();
  private Connection conn = null;
  EditButton editButton = new EditButton();
  NavigatorBar navigatorBar1 = new NavigatorBar();
  ExportButton exportButton1 = new ExportButton();


  public GridFrame(Connection conn,GridFrameController controller) {
    this.conn = conn;
    try {
      jbInit();
      setSize(500,300);
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
    grid.setValueObjectClassName("demo30.TestVO");
    grid.setOrderWithLoadData(false);
    colCustomerCode.setColumnFilterable(true);
    colCustomerCode.setColumnName("customerCode");
    colCustomerCode.setColumnSortable(true);
    colName.setColumnFilterable(true);
    colName.setColumnName("name");
    colName.setColumnSortable(true);
    colName.setSortVersus(org.openswing.swing.util.java.Consts.ASC_SORTED);
    colName.setSortingOrder(1);
    colSurname.setColumnName("surname");
    colCity.setColumnName("city");
    colCity.setPreferredWidth(150);
    colCity.setSortVersus(org.openswing.swing.util.java.Consts.ASC_SORTED);
    colCity.setSortingOrder(2);
    insertButton.setText("insertButton1");
    this.getContentPane().add(grid, BorderLayout.CENTER);
    this.getContentPane().add(buttonsPanel, BorderLayout.NORTH);
    buttonsPanel.add(insertButton, null);
    buttonsPanel.add(editButton, null);
    buttonsPanel.add(reloadButton, null);
    buttonsPanel.add(deleteButton, null);
    buttonsPanel.add(navigatorBar1, null);
    buttonsPanel.add(exportButton1, null);
    grid.getColumnContainer().add(colCustomerCode, null);
    grid.getColumnContainer().add(colName, null);
    grid.getColumnContainer().add(colSurname, null);
    grid.getColumnContainer().add(colCity, null);

    this.setTitle(ClientSettings.getInstance().getResources().getResource("customers"));

//    grid.setLockedColumns(2);

//    grid.setIntercellSpacing(new Dimension(10,0));
//    grid.setRowMargin(2);

//    Font f = new Font(new JLabel().getFont().getName(),Font.BOLD,new JLabel().getFont().getSize());
//    colName.setHeaderFont(f);
//    colName.setHeaderForegroundColor(Color.red);

  }
  public GridControl getGrid() {
    return grid;
  }




}
