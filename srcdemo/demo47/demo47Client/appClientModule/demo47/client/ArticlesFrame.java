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
public class ArticlesFrame extends InternalFrame {

  GridControl itemsGrid = new GridControl();
  BorderLayout borderLayout1 = new BorderLayout();
  JPanel itemsButtonsPanel = new JPanel();
  FlowLayout flowLayout1 = new FlowLayout();
  TextColumn colItemId = new TextColumn();
  CurrencyColumn colPrice = new CurrencyColumn();
  TextColumn colDescription = new TextColumn();
  InsertButton insertButton = new InsertButton();
  ReloadButton reloadButton = new ReloadButton();
  EditButton editButton = new EditButton();
  SaveButton saveButton = new SaveButton();
  DeleteButton deleteButton = new DeleteButton();
  NavigatorBar navigatorBar = new NavigatorBar();
  ExportButton exportButton = new ExportButton();
  

  public ArticlesFrame(ArticlesController controller) {
    try {
      jbInit();
      setSize(600,500);

      itemsGrid.setController(controller);
      itemsGrid.setGridDataLocator(controller);
      MDIFrame.add(this);
      setTitle("Customers");

    }
    catch(Exception e) {
      e.printStackTrace();
    }
  }


  public void reloadData() {
    itemsGrid.reloadData();
  }


  private void jbInit() throws Exception {
    itemsButtonsPanel.setLayout(flowLayout1);
    flowLayout1.setAlignment(FlowLayout.LEFT);
    itemsGrid.setDeleteButton(deleteButton);
    itemsGrid.setEditButton(editButton);
    itemsGrid.setInsertButton(insertButton);
    itemsGrid.setReloadButton(reloadButton);
    itemsGrid.setSaveButton(saveButton);
    itemsGrid.setExportButton(exportButton);
    itemsGrid.setValueObjectClassName("demo47.java.Doc05Articles");
    itemsGrid.setNavBar(navigatorBar);
    itemsGrid.setMaxNumberOfRowsOnInsert(50);
    
    colItemId.setColumnFilterable(true);
    colItemId.setColumnName("itemId");
    colItemId.setHeaderColumnName("Item Id");
    colItemId.setColumnSortable(true);
    colItemId.setEditableOnInsert(true);
    colItemId.setMaxCharacters(20);
    colItemId.setTrimText(true);
    colItemId.setUpperCase(true);
    colItemId.setSortingOrder(1);
    colItemId.setSortVersus(Consts.ASC_SORTED);

    colDescription.setColumnName("descrizione");
    colDescription.setHeaderColumnName("Description");
    colDescription.setEditableOnEdit(true);
    colDescription.setEditableOnInsert(true);
    colDescription.setPreferredWidth(250);

    colPrice.setColumnName("defaultUnitPrice");
    colPrice.setHeaderColumnName("Price");
    colPrice.setEditableOnEdit(true);
    colPrice.setEditableOnInsert(true);
    colPrice.setPreferredWidth(100);
    colPrice.setDecimals(2);
    
    this.getContentPane().add(itemsButtonsPanel,BorderLayout.NORTH);
    this.getContentPane().add(itemsGrid,BorderLayout.CENTER);

    itemsButtonsPanel.add(insertButton, null);
    itemsButtonsPanel.add(editButton, null);
    itemsButtonsPanel.add(reloadButton, null);
    itemsButtonsPanel.add(saveButton, null);
    itemsButtonsPanel.add(exportButton);
    itemsButtonsPanel.add(deleteButton, null);
    itemsButtonsPanel.add(navigatorBar, null);
    itemsGrid.add(colItemId, null);
    itemsGrid.add(colDescription, null);
    itemsGrid.add(colPrice, null);

  }
  
  
  
  public GridControl getitemsGrid() {
    return itemsGrid;
  }



}

