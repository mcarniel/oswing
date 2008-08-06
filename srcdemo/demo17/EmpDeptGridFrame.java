package demo17;

import javax.swing.*;
import org.openswing.swing.client.*;
import java.awt.*;
import org.openswing.swing.table.columns.client.*;
import org.openswing.swing.mdi.client.InternalFrame;
import org.openswing.swing.mdi.client.MDIFrame;
import org.openswing.swing.util.client.ClientSettings;
import java.awt.event.*;


/**
 * <p>Title: OpenSwing Framework</p>
 * <p>Description: Grid Frame for Departments.</p>
 * <p>Copyright: Copyright (C) 2006 Mauro Carniel</p>
 * <p> </p>
 * @author Mauro Carniel
 * @version 1.0
 */
public class EmpDeptGridFrame extends InternalFrame {
  GridControl grid = new GridControl();
  JPanel buttonsPanel = new JPanel();
  ReloadButton reloadButton = new ReloadButton();
  FlowLayout flowLayout1 = new FlowLayout();
  TextColumn colDeptCode = new TextColumn();
  TextColumn colDescription = new TextColumn();
  TextColumn colEmpCode = new TextColumn();
  ExportButton exportButton = new ExportButton();
  NavigatorBar navigatorBar1 = new NavigatorBar();
  private EmpDeptGridFrameController controller = null;


  public EmpDeptGridFrame(EmpDeptGridFrameController controller) {
    this.controller = controller;
    try {
      jbInit();
      setSize(500,300);
      grid.setController(controller);
      grid.setGridDataLocator(controller);
      MDIFrame.add(this);
      setTitle(ClientSettings.getInstance().getResources().getResource("employees and departments"));

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
    grid.setExportButton(exportButton);
    grid.setNavBar(navigatorBar1);
    grid.setReloadButton(reloadButton);
    grid.setValueObjectClassName("demo17.EmpDeptVO");
    colDeptCode.setColumnFilterable(true);
    colDeptCode.setColumnName("deptCode");
    colDeptCode.setColumnSortable(true);
    colDeptCode.setEditableOnInsert(true);
    colDeptCode.setMaxCharacters(5);
    colDeptCode.setTrimText(true);
    colDeptCode.setUpperCase(true);

    colEmpCode.setColumnFilterable(true);
    colEmpCode.setColumnName("empCode");
    colEmpCode.setColumnSortable(true);
    colEmpCode.setEditableOnInsert(true);
    colEmpCode.setMaxCharacters(5);
    colEmpCode.setTrimText(true);
    colEmpCode.setUpperCase(true);

    colDescription.setColumnDuplicable(true);
    colDescription.setColumnFilterable(true);
    colDescription.setColumnName("description");
    colDescription.setColumnSortable(true);
    colDescription.setEditableOnEdit(true);
    colDescription.setEditableOnInsert(true);
    colDescription.setPreferredWidth(350);

    this.getContentPane().add(grid, BorderLayout.CENTER);
    this.getContentPane().add(buttonsPanel, BorderLayout.NORTH);
    buttonsPanel.add(reloadButton, null);
    buttonsPanel.add(exportButton, null);
    buttonsPanel.add(navigatorBar1, null);
    grid.getColumnContainer().add(colEmpCode, null);
    grid.getColumnContainer().add(colDeptCode, null);
    grid.getColumnContainer().add(colDescription, null);


  }
  public GridControl getGrid() {
    return grid;
  }


}


