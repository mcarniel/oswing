package demo40;

import javax.swing.*;
import org.openswing.swing.client.*;
import java.awt.*;
import org.openswing.swing.table.columns.client.*;
import org.openswing.swing.lookup.client.LookupController;
import java.sql.*;
import java.awt.event.*;
import org.openswing.swing.table.java.*;
import org.openswing.swing.message.receive.java.Response;
import java.util.Map;
import java.util.ArrayList;
import org.openswing.swing.message.receive.java.VOListResponse;
import java.math.BigDecimal;
import org.openswing.swing.table.client.GridController;
import javax.swing.text.MaskFormatter;
import javax.swing.text.DefaultFormatter;
import java.text.ParseException;
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
public class GridFrame extends JFrame {
  GridControl grid = new GridControl();
  JPanel buttonsPanel = new JPanel();
  ReloadButton reloadButton = new ReloadButton();
  FlowLayout flowLayout1 = new FlowLayout();
  IntegerColumn colOrderNUmber = new IntegerColumn();
  IntegerColumn colYear = new IntegerColumn();
  TextColumn colCustomerId = new TextColumn();
  CurrencyColumn colTotal = new CurrencyColumn();
  private Connection conn = null;
  ExportButton exportButton = new ExportButton();
  NavigatorBar navigatorBar1 = new NavigatorBar();
  DateColumn colOrderDate = new DateColumn();


  public GridFrame(Connection conn,GridFrameController controller) {
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
    buttonsPanel.setLayout(flowLayout1);
    flowLayout1.setAlignment(FlowLayout.LEFT);
    grid.setExpandableColumn(1);
    grid.setExportButton(exportButton);
    grid.setNavBar(navigatorBar1);
    grid.setReloadButton(reloadButton);

    grid.setValueObjectClassName("demo40.OrderVO");
    colOrderNUmber.setColumnName("orderNumber");
    colOrderNUmber.setColumnSortable(true);
    colOrderNUmber.setHeaderColumnName("Order Nr.");
    colOrderNUmber.setPreferredWidth(70);
    colOrderNUmber.setSortingOrder(2);
    colOrderNUmber.setSortVersus(org.openswing.swing.util.java.Consts.ASC_SORTED);
    colYear.setColumnName("orderYear");
    colYear.setSortingOrder(1);
    colYear.setColumnSortable(true);
    colYear.setHeaderColumnName("Year");
    colYear.setPreferredWidth(60);
    colYear.setSortVersus(org.openswing.swing.util.java.Consts.ASC_SORTED);
    colCustomerId.setColumnName("customerId");
    colCustomerId.setHeaderColumnName("Customer Id");
    colCustomerId.setPreferredWidth(150);
    colTotal.setColumnName("total");
    colTotal.setGrouping(true);
    colTotal.setHeaderColumnName("Total Amount");
    colOrderDate.setColumnName("orderDate");
    colOrderDate.setHeaderColumnName("Order Date");
    this.getContentPane().add(grid, BorderLayout.CENTER);
    this.getContentPane().add(buttonsPanel, BorderLayout.NORTH);
    buttonsPanel.add(reloadButton, null);
    buttonsPanel.add(exportButton, null);
    buttonsPanel.add(navigatorBar1, null);
    grid.getColumnContainer().add(colYear, null);
    grid.getColumnContainer().add(colOrderNUmber, null);
    grid.getColumnContainer().add(colOrderDate, null);
    grid.getColumnContainer().add(colCustomerId, null);
    grid.getColumnContainer().add(colTotal, null);

    grid.setExpandableRowController(new ExpandableRowController() {

      /**
       * @param model grid model
       * @param vo value object related to the current row
       * @param rowNum the current row number
       * @return <code>true</code> if the current row must be expanded, <code>false</code> otherwise
       */
      public boolean isRowExpandable(VOListTableModel model,ValueObject vo,int rowNum) {
        return ((OrderVO)vo).isHasOrderRows();
      }


      /**
       * @param model grid model
       * @param vo value object related to the current row
       * @param rowNum the current row number
       * @return JComponent to show when expanding row; null to do not show anything
       */
      public JComponent getComponentToShow(VOListTableModel model,ValueObject vo,int rowNum) {
        return null;
      }

    });

  }
  public GridControl getGrid() {
    return grid;
  }


}

