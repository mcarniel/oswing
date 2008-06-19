package demo42;

import javax.swing.*;
import org.openswing.swing.client.*;
import java.awt.*;
import org.openswing.swing.table.columns.client.*;
import org.openswing.swing.lookup.client.LookupController;
import java.sql.*;
import java.awt.event.*;
import org.openswing.swing.table.java.*;
import org.openswing.swing.mdi.client.InternalFrame;
import org.openswing.swing.table.model.client.VOListTableModel;
import org.openswing.swing.message.receive.java.ValueObject;


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
  private Connection conn = null;
  ExportButton exportButton1 = new ExportButton();
  NavigatorBar navigatorBar1 = new NavigatorBar();
  TextColumn colLName = new TextColumn();
  TextColumn colDeptCode = new TextColumn();
  TextColumn colDeptDescr = new TextColumn();
  FilterButton filterButton1 = new FilterButton();

  public EmpGridFrame(Connection conn,EmpGridFrameController controller) {
    this.conn = conn;
    try {
      jbInit();
      setSize(900,700);
      grid.setController(controller);
      grid.setGridDataLocator(controller);
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
    grid.setAnchorLastColumn(true);
    buttonsPanel.setLayout(flowLayout1);
    flowLayout1.setAlignment(FlowLayout.LEFT);
    grid.setDeleteButton(deleteButton);
    grid.setExportButton(exportButton1);
    grid.setFilterButton(filterButton1);
    grid.setFunctionId("getEmployees");
    grid.setInsertButton(null);
    grid.setNavBar(navigatorBar1);
    grid.setReloadButton(reloadButton);
    grid.setValueObjectClassName("demo42.GridEmpVO");
    colEmpCode.setColumnFilterable(true);
    colEmpCode.setColumnName("empCode");
    colEmpCode.setColumnSortable(false);
    colFName.setColumnFilterable(true);
    colFName.setColumnName("firstName");
    colFName.setColumnSortable(true);
    colFName.setPreferredWidth(150);
    exportButton1.setText("exportButton1");
    colLName.setColumnFilterable(true);
    colLName.setColumnName("lastName");
    colLName.setPreferredWidth(150);
    colDeptCode.setColumnFilterable(true);
    colDeptCode.setColumnName("deptCode");
    colDeptCode.setPreferredWidth(70);
    colDeptDescr.setColumnName("deptDescription");
    colDeptDescr.setPreferredWidth(200);
    filterButton1.setText("filterButton1");
    this.getContentPane().add(grid, BorderLayout.CENTER);
    this.getContentPane().add(buttonsPanel, BorderLayout.NORTH);
    buttonsPanel.add(reloadButton, null);
    buttonsPanel.add(deleteButton, null);
    buttonsPanel.add(exportButton1, null);
    buttonsPanel.add(filterButton1, null);
    buttonsPanel.add(navigatorBar1, null);
    grid.getColumnContainer().add(colEmpCode, null);
    grid.getColumnContainer().add(colFName, null);
    grid.getColumnContainer().add(colLName, null);
    grid.getColumnContainer().add(colDeptCode, null);
    grid.getColumnContainer().add(colDeptDescr, null);

    grid.setOverwriteRowWhenExpanding(false);
    grid.setSingleExpandableRow(true);
    grid.setExpandableRowController(new ExpandableRowController() {

      /**
       * @param model grid model
       * @param rowNum the current row number
       * @return <code>true</code> if the current row must be expanded, <code>false</code> otherwise
       */
      public boolean isRowExpandable(VOListTableModel model,int rowNum) {
        return true;
      }


      /**
       * @param model grid model
       * @param rowNum the current row number
       * @return JComponent to show when expanding row; null to do not show anything
       */
      public JComponent getComponentToShow(VOListTableModel model,int rowNum) {
        GridEmpVO gridVO = (GridEmpVO)model.getObjectForRow(rowNum);
        EmpDetailPanelController c = new EmpDetailPanelController(gridVO,conn);
        EmpDetailPanel p = c.getPanel();
        p.setPreferredSize(new Dimension(590,480));
        p.setBorder(BorderFactory.createEtchedBorder());
        return p;
      }


      /**
       * @param showedComponent component currently showed
       * @return component that will receive focus when showing frame; null to do not set focus automatically
       */
      public Component getFocusableComponent(JComponent showedComponent) {
        EmpDetailPanel p = (EmpDetailPanel)showedComponent;
        return p.getMainPanel();
      }


    });


  }


  public GridControl getGrid() {
    return grid;
  }


  /**
   * Callback method invoked by closeFrame.
   * @return <code>true</code> allows the closing operation to continue, <code>false</code> the closing operation will be interrupted
   */
  protected boolean beforeCloseFrame() {
//    grid = null;
    return true;
  }


}

