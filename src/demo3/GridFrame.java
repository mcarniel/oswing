package demo3;

import javax.swing.*;
import org.openswing.swing.client.*;
import java.awt.*;
import org.openswing.swing.table.columns.client.*;
import org.openswing.swing.lookup.client.LookupController;
import java.sql.*;
import java.awt.event.*;
import org.openswing.swing.table.java.*;


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
  DeleteButton deleteButton = new DeleteButton();
  FlowLayout flowLayout1 = new FlowLayout();
  TextColumn colText = new TextColumn();
  DecimalColumn colDecimal = new DecimalColumn();
  CurrencyColumn colCurrency = new CurrencyColumn();
  DateColumn colDate = new DateColumn();
  ComboColumn colCombo = new ComboColumn();
  CodLookupColumn colLookup = new CodLookupColumn();
  TextColumn textColumn1 = new TextColumn();
  CheckBoxColumn colCheck = new CheckBoxColumn();
  InsertButton insertButton = new InsertButton();
  private Connection conn = null;
  EditButton editButton = new EditButton();
  SaveButton saveButton = new SaveButton();
  CopyButton copyButton = new CopyButton();
  ExportButton exportButton = new ExportButton();
  ButtonColumn colButton = new ButtonColumn();

  public GridFrame(Connection conn,GridFrameController controller) {
    this.conn = conn;
    try {
      jbInit();
      setSize(750,300);
      grid.setController(controller);
      grid.setGridDataLocator(controller);

      LookupController lookupController = new DemoLookupController(conn);
      colLookup.setLookupController(lookupController);
//      grid.setLockedColumns(2);
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
    grid.setCopyButton(copyButton);
    grid.setDeleteButton(deleteButton);
    grid.setEditButton(editButton);
    grid.setExportButton(exportButton);
    grid.setInsertButton(insertButton);
    grid.setReloadButton(reloadButton);
    grid.setSaveButton(saveButton);
    grid.setValueObjectClassName("demo3.TestVO");
    colText.setColumnFilterable(true);
    colText.setColumnName("stringValue");
    colText.setColumnSortable(true);
    colText.setEditableOnInsert(true);
    colText.setMaxCharacters(5);
    colText.setTrimText(true);
    colText.setUpperCase(true);
    colDecimal.setDecimals(2);
    colDecimal.setColumnDuplicable(true);
    colDecimal.setColumnName("numericValue");
    colDecimal.setColumnRequired(false);
    colDecimal.setEditableOnEdit(true);
    colDecimal.setEditableOnInsert(true);
    colCurrency.setColumnDuplicable(true);
    colCurrency.setColumnName("currencyValue");
    colCurrency.setColumnRequired(false);
    colCurrency.setDecimals(3);
    colCurrency.setEditableOnEdit(true);
    colCurrency.setEditableOnInsert(true);
    colDate.setColumnDuplicable(true);
    colDate.setColumnName("dateValue");
    colDate.setColumnRequired(false);
    colDate.setEditableOnEdit(true);
    colDate.setEditableOnInsert(true);
    colCombo.setDomainId("ORDERSTATE");
    colCombo.setColumnDuplicable(true);
    colCombo.setColumnName("comboValue");
    colCombo.setColumnRequired(false);
    colCombo.setEditableOnEdit(true);
    colCombo.setEditableOnInsert(true);
    colLookup.setColumnDuplicable(true);
    colLookup.setColumnName("lookupValue");
    colLookup.setEditableOnEdit(true);
    colLookup.setEditableOnInsert(true);
    colLookup.setMaxCharacters(5);
    textColumn1.setColumnDuplicable(true);
    textColumn1.setColumnName("descrLookupValue");
    textColumn1.setColumnRequired(false);
    textColumn1.setPreferredWidth(150);
    colCheck.setColumnDuplicable(true);
    colCheck.setColumnName("checkValue");
    colCheck.setColumnRequired(false);
    colCheck.setEditableOnEdit(true);
    colCheck.setEditableOnInsert(true);
    insertButton.setText("insertButton1");
    editButton.setText("editButton1");
    saveButton.setText("saveButton1");
    colButton.setColumnName("button");
    colButton.setHeaderColumnName("button");
    colButton.setPreferredWidth(50);
    this.getContentPane().add(grid, BorderLayout.CENTER);
    this.getContentPane().add(buttonsPanel, BorderLayout.NORTH);
    buttonsPanel.add(insertButton, null);
    buttonsPanel.add(copyButton, null);
    buttonsPanel.add(editButton, null);
    buttonsPanel.add(reloadButton, null);
    buttonsPanel.add(saveButton, null);
    buttonsPanel.add(exportButton, null);
    buttonsPanel.add(deleteButton, null);
    grid.getColumnContainer().add(colText, null);
    grid.getColumnContainer().add(colDecimal, null);
    grid.getColumnContainer().add(colCurrency, null);
    grid.getColumnContainer().add(colDate, null);
    grid.getColumnContainer().add(colCombo, null);
    grid.getColumnContainer().add(colButton, null);
    grid.getColumnContainer().add(colLookup, null);
    grid.getColumnContainer().add(textColumn1, null);
    grid.getColumnContainer().add(colCheck, null);


  }


}

