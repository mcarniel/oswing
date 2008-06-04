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
public class OrdersGridFrame extends JFrame {
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


  public OrdersGridFrame(Connection conn,OrdersGridFrameController controller) {
    this.conn = conn;
    try {
      jbInit();
      setSize(550,550);
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
    grid.setExpandableColumn(0);
    grid.setExportButton(exportButton);
    grid.setNavBar(navigatorBar1);
    grid.setReloadButton(reloadButton);

    grid.setValueObjectClassName("demo40.OrderVO");
    colOrderNUmber.setColumnName("orderNumber");
//    colOrderNUmber.setTextAlignment(SwingConstants.LEFT);
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

    grid.setOverwriteRowWhenExpanding(false);
    grid.setSingleExpandableRow(true);
    grid.setExpandableRowController(new ExpandableRowController() {

      /**
       * @param model grid model
       * @param rowNum the current row number
       * @return <code>true</code> if the current row must be expanded, <code>false</code> otherwise
       */
      public boolean isRowExpandable(VOListTableModel model,int rowNum) {
        return ((OrderVO)model.getObjectForRow(rowNum)).isHasOrderRows();
      }


      /**
       * @param model grid model
       * @param rowNum the current row number
       * @return JComponent to show when expanding row; null to do not show anything
       */
      public JComponent getComponentToShow(VOListTableModel model,int rowNum) {
//        final Point pp = new Point(0,0);
//        final Rectangle rr = new Rectangle(0,0,0,0);
//
//        final JPanel p = new JPanel() {
//
//          public void setBounds(Rectangle r) {
//            super.setBounds(r);
//            rr.x = r.x;
//            rr.y = r.y;
//            rr.height = r.height;
//            rr.width = r.width;
//            repaint();
//          }
//
//          public void setBounds(int a,int b,int c,int d) {
//            super.setBounds(a,b,c,d);
//            rr.x = a;
//            rr.y = b;
//            rr.height = c;
//            rr.width = d;
//            repaint();
//          }
//
//
//          public void paint(Graphics g) {
//            super.paint(g);
//            g.drawString(""+getLocation(),5,20);
//            g.drawString(""+g.getClipBounds(),5,60);
//            g.drawString(""+rr,5,40);
//            g.drawString(""+pp,5,80);
//          }
//        };
//        p.setLayout(new BorderLayout(0,0));
//        p.setPreferredSize(new Dimension(300,100));
//        p.addMouseListener(new MouseAdapter() {
//          public void mouseClicked(MouseEvent e) {
//            pp.x=e.getPoint().x;
//            pp.y=e.getPoint().y;
//            p.repaint();
//          }
//        });




//        JPanel p = new JPanel();
//        p.setLayout(new BorderLayout(0,0));
//        p.setPreferredSize(new Dimension(300,100));
//        JTextArea t = new JTextArea(80,20) {
//
//          public void requestFocus() {
//            super.requestFocus();
//          }
//
//        };
//        t.setText("abc"+rowNum);
//        p.add(new JScrollPane(t),BorderLayout.CENTER);

        OrderVO vo = (OrderVO)model.getObjectForRow(rowNum);
        OrderRowsController c = new OrderRowsController(conn,vo);
        OrderRowsGridPanel p = new OrderRowsGridPanel(c,conn);
        p.setPreferredSize(new Dimension(300,400));
        return p;
      }

    });

  }
  public GridControl getGrid() {
    return grid;
  }


}

