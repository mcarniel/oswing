package demo7;

import javax.swing.*;
import org.openswing.swing.client.*;
import java.awt.*;
import org.openswing.swing.table.columns.client.*;
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
  TextColumn colLookup = new TextColumn();
  TextColumn textColumn1 = new TextColumn();
  CheckBoxColumn colCheck = new CheckBoxColumn();
  InsertButton insertButton = new InsertButton();
  private Connection conn = null;
  EditButton editButton = new EditButton();

  public GridFrame(Connection conn,GridFrameController controller) {
    this.conn = conn;
    try {
      jbInit();
      setSize(750,300);
      grid.setController(controller);
      grid.setGridDataLocator(controller);

      setLocation(
          Toolkit.getDefaultToolkit().getScreenSize().width/2-200,
          Toolkit.getDefaultToolkit().getScreenSize().height/2-200
      );

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
    grid.setDeleteButton(deleteButton);
    grid.setEditButton(editButton);
    grid.setFunctionId("F1");
    grid.setInsertButton(null);
    grid.setReloadButton(reloadButton);
    grid.setValueObjectClassName("demo7.TestVO");
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
    insertButton.addActionListener(new GridFrame_insertButton_actionAdapter(this));
    this.getContentPane().add(grid, BorderLayout.CENTER);
    this.getContentPane().add(buttonsPanel, BorderLayout.NORTH);
    buttonsPanel.add(insertButton, null);
    buttonsPanel.add(editButton, null);
    buttonsPanel.add(reloadButton, null);
    buttonsPanel.add(deleteButton, null);
    grid.getColumnContainer().add(colText, null);
    grid.getColumnContainer().add(colDecimal, null);
    grid.getColumnContainer().add(colCurrency, null);
    grid.getColumnContainer().add(colDate, null);
    grid.getColumnContainer().add(colCombo, null);
    grid.getColumnContainer().add(textColumn1, null);
    grid.getColumnContainer().add(colCheck, null);
    grid.getColumnContainer().add(colLookup, null);


  }


  void insertButton_actionPerformed(ActionEvent e) {
    new DetailFrameController(this,null,conn);

  }


}

class GridFrame_insertButton_actionAdapter implements java.awt.event.ActionListener {
  GridFrame adaptee;

  GridFrame_insertButton_actionAdapter(GridFrame adaptee) {
    this.adaptee = adaptee;
  }
  public void actionPerformed(ActionEvent e) {
    adaptee.insertButton_actionPerformed(e);
  }
}