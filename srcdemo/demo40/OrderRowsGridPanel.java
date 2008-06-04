package demo40;

import javax.swing.*;
import org.openswing.swing.client.*;
import java.awt.*;
import org.openswing.swing.table.columns.client.*;
import java.util.*;
import org.openswing.swing.util.java.Consts;
import org.openswing.swing.table.model.client.VOListTableModel;
import java.sql.Connection;

/**
 * <p>Title: OpenSwing Framework</p>
 * <p>Description: Panel that contains a grid for order rows.</p>
 * <p>Copyright: Copyright (C) 2008 Mauro Carniel</p>
 * @version 1.0
 */
public class OrderRowsGridPanel extends JPanel {
  GridControl grid = new GridControl();
  BorderLayout borderLayout1 = new BorderLayout();
  JPanel buttonsPanel = new JPanel();
  FlowLayout flowLayout1 = new FlowLayout();
  ReloadButton reloadButton1 = new ReloadButton();
  ExportButton exportButton1 = new ExportButton();
  NavigatorBar navigatorBar1 = new NavigatorBar();
  TextColumn colItemCode = new TextColumn();
  TextColumn colItemDescr = new TextColumn();
  IntegerColumn colQty = new IntegerColumn();
  CurrencyColumn colPrice = new CurrencyColumn();
  private Connection conn = null;


  public OrderRowsGridPanel(OrderRowsController controller,Connection conn) {
    this.conn = conn;
    try {
      jbInit();
      grid.setController(controller);
      grid.setGridDataLocator(controller);
    }
    catch(Exception e) {
      e.printStackTrace();
    }
  }


  private void jbInit() throws Exception {
    this.setLayout(borderLayout1);
    buttonsPanel.setLayout(flowLayout1);
    flowLayout1.setAlignment(FlowLayout.LEFT);
    grid.setFilterPanelOnGridPolicy(Consts.FILTER_PANEL_ON_GRID_CLOSE_ON_EXIT);
    grid.setMaxNumberOfRowsOnInsert(2);
    grid.setNavBar(navigatorBar1);
    grid.setExportButton(exportButton1);
    grid.setReloadButton(reloadButton1);
    grid.setValueObjectClassName("demo40.OrderRowVO");
    grid.setVisibleStatusPanel(false);
    colItemCode.setColumnName("itemId");
    colItemCode.setColumnFilterable(true);
    colItemCode.setColumnSortable(true);
    colItemCode.setHeaderColumnName("Item Id");
    colItemCode.setPreferredWidth(80);
    colItemDescr.setColumnName("itemDescription");
    colItemDescr.setColumnFilterable(true);
    colItemDescr.setColumnSortable(true);
    colItemDescr.setHeaderColumnName("Description");
    colItemDescr.setPreferredWidth(210);
    colQty.setColumnName("qty");
    colQty.setHeaderColumnName("Qty");
    colQty.setPreferredWidth(50);
    colPrice.setColumnName("price");
    colPrice.setHeaderColumnName("Price");
    colPrice.setPreferredWidth(90);
    this.setBorder(BorderFactory.createRaisedBevelBorder());
    this.add(grid,  BorderLayout.CENTER);
    this.add(buttonsPanel, BorderLayout.NORTH);
    buttonsPanel.add(reloadButton1, null);
    buttonsPanel.add(exportButton1, null);
    buttonsPanel.add(navigatorBar1, null);
    grid.getColumnContainer().add(colItemCode, null);
    grid.getColumnContainer().add(colItemDescr, null);
    grid.getColumnContainer().add(colQty, null);
    grid.getColumnContainer().add(colPrice, null);


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
        OrderRowVO vo = (OrderRowVO)model.getObjectForRow(rowNum);
        OrderRowDiscountsController c = new OrderRowDiscountsController(conn,vo);
        OrderRowDiscountsGridPanel p = new OrderRowDiscountsGridPanel(c);
        p.setPreferredSize(new Dimension(300,200));
        return p;
      }

    });

  }



}
