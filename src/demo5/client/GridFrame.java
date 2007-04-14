package demo5.client;

import javax.swing.*;
import org.openswing.swing.client.*;
import java.awt.*;
import org.openswing.swing.table.columns.client.*;
import org.openswing.swing.lookup.client.LookupController;
import java.awt.event.*;
import org.openswing.swing.table.java.*;
import org.openswing.swing.mdi.client.InternalFrame;
import demo5.java.*;


/**
 * <p>Title: OpenSwing Framework</p>
 * <p>Description: Grid Frame</p>
 * <p>Copyright: Copyright (C) 2006 Mauro Carniel</p>
 * <p> </p>
 * @author Mauro Carniel
 * @version 1.0
 */
public class GridFrame extends InternalFrame {
  GridControl grid = new GridControl();
  JPanel buttonsPanel = new JPanel();
  ReloadButton reloadButton = new ReloadButton();
  DeleteButton deleteButton = new DeleteButton();
  FlowLayout flowLayout1 = new FlowLayout();
  TextColumn colText = new TextColumn();
  DecimalColumn colDecimal = new DecimalColumn();
  CurrencyColumn colCurrency = new CurrencyColumn();
  DateColumn colDate = new DateColumn();
  ComboColumn colCombo = new ComboColumn();
  TextColumn colLookup = new TextColumn();
  TextColumn textColumn1 = new TextColumn();
  CheckBoxColumn colCheck = new CheckBoxColumn();
  InsertButton insertButton = new InsertButton();
  ServerGridDataLocator dataLocator = new ServerGridDataLocator();
  NavigatorBar navigatorBar = new NavigatorBar();
  ExportButton exportButton1 = new ExportButton();

  public GridFrame(GridFrameController controller) {
    try {
      jbInit();
      setSize(750,300);
      grid.setController(controller);
      grid.setGridDataLocator(dataLocator);
      dataLocator.setServerMethodName("loadGrid");

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
    grid.setFunctionId("F1");
    grid.setInsertButton(insertButton);
    grid.setNavBar(navigatorBar);
    grid.setReloadButton(reloadButton);
    grid.setValueObjectClassName("demo5.java.TestVO");
    colText.setColumnFilterable(true);
    colText.setColumnName("stringValue");
    colText.setColumnSortable(true);
    colDecimal.setDecimals(2);
    colDecimal.setColumnName("numericValue");
    colCurrency.setColumnName("currencyValue");
    colCurrency.setDecimals(3);
    colDate.setColumnName("dateValue");
    colCombo.setDomainId("ORDERSTATE");
    colCombo.setColumnName("comboValue");
    colLookup.setColumnName("lookupValue");
    textColumn1.setColumnName("descrLookupValue");
    textColumn1.setPreferredWidth(150);
    colCheck.setColumnName("checkValue");
    insertButton.setText("insertButton1");
    this.getContentPane().add(grid, BorderLayout.CENTER);
    this.getContentPane().add(buttonsPanel, BorderLayout.NORTH);
    buttonsPanel.add(insertButton, null);
    buttonsPanel.add(reloadButton, null);
    buttonsPanel.add(deleteButton, null);
    buttonsPanel.add(exportButton1, null);
    buttonsPanel.add(navigatorBar, null);
    grid.getColumnContainer().add(colText, null);
    grid.getColumnContainer().add(colDecimal, null);
    grid.getColumnContainer().add(colCurrency, null);
    grid.getColumnContainer().add(colDate, null);
    grid.getColumnContainer().add(colCombo, null);
    grid.getColumnContainer().add(textColumn1, null);
    grid.getColumnContainer().add(colCheck, null);
    grid.getColumnContainer().add(colLookup, null);


  }




}

