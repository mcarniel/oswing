package demo18.client;

import javax.swing.*;
import org.openswing.swing.client.*;
import java.awt.*;
import org.openswing.swing.table.columns.client.*;
import org.openswing.swing.lookup.client.LookupController;
import java.awt.event.*;
import org.openswing.swing.table.java.*;
import org.openswing.swing.mdi.client.InternalFrame;


/**
 * <p>Title: OpenSwing Framework</p>
 * <p>Description: Grid Frame</p>
 * <p>Copyright: Copyright (C) 2006 Mauro Carniel</p>
 * <p> </p>
 * @author Mauro Carniel
 * @version 1.0
 */
public class EmpGridFrame extends InternalFrame {
  GridControl grid = new GridControl();
  JPanel buttonsPanel = new JPanel();
  ReloadButton reloadButton = new ReloadButton();
  DeleteButton deleteButton = new DeleteButton();
  FlowLayout flowLayout1 = new FlowLayout();
  TextColumn colEmpCode = new TextColumn();
  TextColumn colFName = new TextColumn();
  InsertButton insertButton = new InsertButton();
  ExportButton exportButton1 = new ExportButton();
  NavigatorBar navigatorBar1 = new NavigatorBar();
  TextColumn colLName = new TextColumn();
  TextColumn colDeptCode = new TextColumn();
  TextColumn colDeptDescr = new TextColumn();

  private ServerGridDataLocator serverDataLocator = new ServerGridDataLocator();


  public EmpGridFrame(EmpGridFrameController controller) {
    try {
      jbInit();
      setSize(750,300);
      grid.setController(controller);
      grid.setGridDataLocator(serverDataLocator);
      serverDataLocator.setServerMethodName("loadEmps");
      setTitle("Employees");
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
    grid.setDeleteButton(deleteButton);
    grid.setExportButton(exportButton1);
    grid.setFunctionId("getEmployees");
    grid.setInsertButton(null);
    grid.setNavBar(navigatorBar1);
    grid.setReloadButton(reloadButton);
    grid.setValueObjectClassName("demo18.java.GridEmpVO");
    colEmpCode.setColumnName("empCode");
    colEmpCode.setColumnSortable(false);
    colFName.setColumnName("firstName");
    colFName.setColumnSortable(true);
    colFName.setPreferredWidth(150);
    insertButton.setText("insertButton1");
    insertButton.addActionListener(new EmpGridFrame_insertButton_actionAdapter(this));
    exportButton1.setText("exportButton1");
    colLName.setColumnName("lastName");
    colLName.setPreferredWidth(150);
    colDeptCode.setColumnFilterable(true);
    colDeptCode.setColumnName("deptCode");
    colDeptCode.setPreferredWidth(70);
    colDeptDescr.setColumnName("deptDescription");
    colDeptDescr.setPreferredWidth(200);
    this.getContentPane().add(grid, BorderLayout.CENTER);
    this.getContentPane().add(buttonsPanel, BorderLayout.NORTH);
    buttonsPanel.add(insertButton, null);
    buttonsPanel.add(reloadButton, null);
    buttonsPanel.add(deleteButton, null);
    buttonsPanel.add(exportButton1, null);
    buttonsPanel.add(navigatorBar1, null);
    grid.getColumnContainer().add(colEmpCode, null);
    grid.getColumnContainer().add(colFName, null);
    grid.getColumnContainer().add(colLName, null);
    grid.getColumnContainer().add(colDeptCode, null);
    grid.getColumnContainer().add(colDeptDescr, null);


  }


  void insertButton_actionPerformed(ActionEvent e) {
    new EmpDetailFrameController(this,null);

  }


}

class EmpGridFrame_insertButton_actionAdapter implements java.awt.event.ActionListener {
  EmpGridFrame adaptee;

  EmpGridFrame_insertButton_actionAdapter(EmpGridFrame adaptee) {
    this.adaptee = adaptee;
  }
  public void actionPerformed(ActionEvent e) {
    adaptee.insertButton_actionPerformed(e);
  }
}
