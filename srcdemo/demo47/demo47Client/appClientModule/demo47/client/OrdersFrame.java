package demo47.client;

import javax.swing.*;
import org.openswing.swing.client.*;
import java.awt.*;
import org.openswing.swing.table.columns.client.*;
import org.openswing.swing.lookup.client.LookupController;
import java.awt.event.*;

import org.openswing.swing.table.java.*;
import org.openswing.swing.mdi.client.InternalFrame;
import org.openswing.swing.mdi.client.MDIFrame;
import org.openswing.swing.util.client.ClientSettings;
import org.openswing.swing.util.java.Consts;
import org.openswing.swing.table.client.GridController;


/**
 * <p>Title: OpenSwing Framework</p>
 * <p>Description: Grid Frame for orders.</p>
 * <p>Copyright: Copyright (C) 2006 Mauro Carniel</p>
 * <p> </p>
 * @author Mauro Carniel
 * @version 1.0
 */
public class OrdersFrame extends InternalFrame {

  GridControl grid = new GridControl();
  JPanel buttonsPanel = new JPanel();
  ReloadButton reloadButton = new ReloadButton();
  DeleteButton deleteButton = new DeleteButton();
  FlowLayout flowLayout1 = new FlowLayout();
  IntegerColumn colOrderNr = new IntegerColumn();
  IntegerColumn colOrderYear = new IntegerColumn();
  InsertButton insertButton = new InsertButton();
  ExportButton exportButton = new ExportButton();
  TextColumn colCustId = new TextColumn();
  TextColumn colCustDesc = new TextColumn();


  public OrdersFrame(OrdersController controller) {
    try {
      jbInit();
      setSize(550,400);
      grid.setController(controller);
      grid.setGridDataLocator(controller);

      setTitle("Orders");
      MDIFrame.add(this);
    }
    catch(Exception e) {
      e.printStackTrace();
    }
  }


  public void reloadData() {
    grid.reloadData();
  }

  
  public GridControl getGrid() {
	  return grid;
  } 

  private void jbInit() throws Exception {
    buttonsPanel.setLayout(flowLayout1);
    flowLayout1.setAlignment(FlowLayout.LEFT);
    grid.setDeleteButton(deleteButton);
    grid.setExportButton(exportButton);
    grid.setReloadButton(reloadButton);
    grid.setValueObjectClassName("demo47.java.Doc01Orders");
    colOrderNr.setColumnFilterable(true);
    colOrderNr.setColumnName("pk.docNumber");
    colOrderNr.setHeaderColumnName("Nr");
    colOrderNr.setColumnSortable(true);
    colOrderNr.setPreferredWidth(50);
    colOrderNr.setSortingOrder(2);
    colOrderNr.setSortVersus(Consts.DESC_SORTED);
    colOrderYear.setColumnName("pk.docYear");
    colOrderYear.setHeaderColumnName("Year");
    colOrderYear.setColumnFilterable(true);
    colOrderYear.setColumnSortable(true);
    colOrderYear.setPreferredWidth(50);
    colOrderYear.setSortingOrder(1);
    colOrderYear.setSortVersus(Consts.DESC_SORTED);
    colCustId.setColumnName("customerId.customerId");
    colCustId.setHeaderColumnName("Customer Id");
    colCustId.setColumnRequired(false);
    colCustId.setColumnSortable(true);
    colCustDesc.setColumnName("customerId.description");
    colCustDesc.setHeaderColumnName("Customer description");
    colCustDesc.setColumnSortable(true);
    colCustDesc.setPreferredWidth(300);

    this.getContentPane().add(grid, BorderLayout.CENTER);
    this.getContentPane().add(buttonsPanel, BorderLayout.NORTH);
    buttonsPanel.add(insertButton, null);
    buttonsPanel.add(reloadButton, null);
    buttonsPanel.add(exportButton, null);
    buttonsPanel.add(deleteButton, null);
    grid.add(colOrderYear, null);
    grid.add(colOrderNr, null);
    grid.add(colCustId, null);
    grid.add(colCustDesc, null);
    insertButton.addActionListener(new ActionListener() {

		@Override
		public void actionPerformed(ActionEvent e) {
			OrderController f = new OrderController(OrdersFrame.this,null);			
		}
    	
    });
  }


}

