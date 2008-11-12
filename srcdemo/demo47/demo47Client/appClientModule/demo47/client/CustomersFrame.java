package demo47.client;

import javax.swing.*;
import org.openswing.swing.client.*;
import java.awt.*;
import org.openswing.swing.table.columns.client.*;
import org.openswing.swing.lookup.client.LookupController;
import java.sql.*;
import java.awt.event.*;
import org.openswing.swing.table.java.*;
import org.openswing.swing.mdi.client.InternalFrame;
import org.openswing.swing.mdi.client.MDIFrame;
import org.openswing.swing.util.client.ClientSettings;
import org.openswing.swing.util.java.Consts;


/**
 * <p>Title: OpenSwing Framework</p>
 * <p>Description: Grid Frame for tasks.</p>
 * <p>Copyright: Copyright (C) 2006 Mauro Carniel</p>
 * <p> </p>
 * @author Mauro Carniel
 * @version 1.0
 */
public class CustomersFrame extends InternalFrame {

  GridControl customersGrid = new GridControl();
  GridControl pricelistsGrid = new GridControl();
  JPanel topPanel = new JPanel();
  JPanel bottomPanel = new JPanel();
  BorderLayout borderLayout1 = new BorderLayout();
  BorderLayout borderLayout2 = new BorderLayout();
  JPanel customersButtonsPanel = new JPanel();
  JPanel pricelistsButtonsPanel = new JPanel();
  FlowLayout flowLayout1 = new FlowLayout();
  FlowLayout flowLayout2 = new FlowLayout();
  TextColumn colCustomerId = new TextColumn();
  TextColumn colAddress = new TextColumn();
  TextColumn colDescription = new TextColumn();
  TextColumn colPricelistId = new TextColumn();
  TextColumn colPricelistDescription = new TextColumn();
  InsertButton insertButton = new InsertButton();
  ReloadButton reloadButton = new ReloadButton();
  EditButton editButton = new EditButton();
  SaveButton saveButton = new SaveButton();
  DeleteButton deleteButton = new DeleteButton();
  NavigatorBar navigatorBar = new NavigatorBar();
  ExportButton exportButton = new ExportButton();
  
  InsertButton insert2Button = new InsertButton();
  ReloadButton reload2Button = new ReloadButton();
  SaveButton save2Button = new SaveButton();
  EditButton edit2Button = new EditButton();
  DeleteButton delete2Button = new DeleteButton();

  JSplitPane splitPane = new JSplitPane();


  public CustomersFrame(CustomersController controller) {
    try {
      jbInit();
      setSize(680,550);
      PricelistsController pricelistsController = new PricelistsController(this);
      pricelistsGrid.setController(pricelistsController);
      pricelistsGrid.setGridDataLocator(pricelistsController);
      pricelistsGrid.setAutoLoadData(false);

      customersGrid.setController(controller);
      customersGrid.setGridDataLocator(controller);
      MDIFrame.add(this);
      setTitle("Customers");

    }
    catch(Exception e) {
      e.printStackTrace();
    }
  }


  public void reloadData() {
    customersGrid.reloadData();
  }


  private void jbInit() throws Exception {
	pricelistsGrid.setCreateInnerVO(true);
	  
    colPricelistId.setColumnName("pk.pricelistId");
    colPricelistId.setHeaderColumnName("Pricelist Id");
    colPricelistId.setEditableOnInsert(true);
    colPricelistId.setPreferredWidth(100);
    colPricelistId.setColumnFilterable(true);
    colPricelistId.setColumnSortable(true);
    colPricelistId.setSortingOrder(1);
    colPricelistId.setSortVersus(Consts.ASC_SORTED);
    colPricelistId.setMaxCharacters(5);
    colPricelistId.setTrimText(true);
    colPricelistId.setUpperCase(true);

    colPricelistDescription.setColumnName("description");
    colPricelistDescription.setHeaderColumnName("Description");
    colPricelistDescription.setEditableOnInsert(true);
    colPricelistDescription.setEditableOnEdit(true);
    colPricelistDescription.setPreferredWidth(300);
    colPricelistDescription.setColumnFilterable(true);
    colPricelistDescription.setColumnSortable(true);
    
    customersButtonsPanel.setLayout(flowLayout1);
    flowLayout1.setAlignment(FlowLayout.LEFT);
    customersGrid.setDeleteButton(deleteButton);
    customersGrid.setEditButton(editButton);
    customersGrid.setInsertButton(insertButton);
    customersGrid.setReloadButton(reloadButton);
    customersGrid.setSaveButton(saveButton);
    customersGrid.setExportButton(exportButton);
    customersGrid.setValueObjectClassName("demo47.java.Doc02Customers");
    customersGrid.setNavBar(navigatorBar);
    customersGrid.setMaxNumberOfRowsOnInsert(50);

    pricelistsGrid.setValueObjectClassName("demo47.java.Doc03Pricelists");
    pricelistsGrid.setDeleteButton(delete2Button);
    pricelistsGrid.setInsertButton(insert2Button);
    pricelistsGrid.setReloadButton(reload2Button);
    pricelistsGrid.setSaveButton(save2Button);
    pricelistsGrid.setEditButton(edit2Button);
    
    pricelistsButtonsPanel.setLayout(flowLayout2);
    flowLayout2.setAlignment(FlowLayout.LEFT);


    colCustomerId.setColumnFilterable(true);
    colCustomerId.setColumnName("customerId");
    colCustomerId.setHeaderColumnName("Customer Id");
    colCustomerId.setColumnSortable(true);
    colCustomerId.setEditableOnInsert(true);
    colCustomerId.setMaxCharacters(20);
    colCustomerId.setTrimText(true);
    colCustomerId.setUpperCase(true);
    colCustomerId.setSortingOrder(1);
    colCustomerId.setSortVersus(Consts.ASC_SORTED);
    
    colDescription.setColumnName("description");
    colDescription.setHeaderColumnName("Description");
    colDescription.setEditableOnEdit(true);
    colDescription.setEditableOnInsert(true);
    colDescription.setPreferredWidth(200);

    colAddress.setColumnName("address");
    colAddress.setHeaderColumnName("Address");
    colAddress.setEditableOnEdit(true);
    colAddress.setEditableOnInsert(true);
    colAddress.setPreferredWidth(250);
    
    splitPane.setOrientation(JSplitPane.VERTICAL_SPLIT);
    splitPane.setDividerLocation(300);
    splitPane.add(topPanel,JSplitPane.TOP);
    splitPane.add(bottomPanel,JSplitPane.BOTTOM);

    topPanel.setLayout(borderLayout1);
    bottomPanel.setLayout(borderLayout2);

    topPanel.add(customersButtonsPanel, BorderLayout.NORTH);
    topPanel.add(customersGrid, BorderLayout.CENTER);
    customersButtonsPanel.add(insertButton, null);
    customersButtonsPanel.add(editButton, null);
    customersButtonsPanel.add(reloadButton, null);
    customersButtonsPanel.add(saveButton, null);
    customersButtonsPanel.add(exportButton);
    customersButtonsPanel.add(deleteButton, null);
    customersButtonsPanel.add(navigatorBar, null);
    customersGrid.add(colCustomerId, null);
    customersGrid.add(colDescription, null);
    customersGrid.add(colAddress, null);

    bottomPanel.add(pricelistsButtonsPanel, BorderLayout.NORTH);
    bottomPanel.add(pricelistsGrid, BorderLayout.CENTER);
    pricelistsButtonsPanel.add(insert2Button, null);
    pricelistsButtonsPanel.add(edit2Button, null);
    pricelistsButtonsPanel.add(reload2Button, null);
    pricelistsButtonsPanel.add(save2Button, null);
    pricelistsButtonsPanel.add(delete2Button, null);
    pricelistsGrid.add(colPricelistId, null);
    pricelistsGrid.add(colPricelistDescription, null);
    pricelistsGrid.setMaxNumberOfRowsOnInsert(50);    
    this.getContentPane().add(splitPane,BorderLayout.CENTER);
  }
  
  
  public GridControl getPricelistsGrid() {
    return pricelistsGrid;
  }
  
  
  public GridControl getCustomersGrid() {
    return customersGrid;
  }


  public void setButtonsEnabled(boolean enabled) {
    delete2Button.setEnabled(enabled);
    insert2Button.setEnabled(enabled);
    reload2Button.setEnabled(enabled);
    if (!enabled)
      save2Button.setEnabled(false);
  }


}

