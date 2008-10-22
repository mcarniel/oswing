package demo6;

import javax.swing.*;
import org.openswing.swing.client.*;
import java.awt.*;
import org.openswing.swing.table.columns.client.*;
import org.openswing.swing.lookup.client.LookupController;
import java.sql.*;
import java.awt.event.*;
import org.openswing.swing.table.java.*;
import org.openswing.swing.table.client.ListFilterController;
import org.openswing.swing.message.receive.java.*;
import java.util.*;


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


      // add combo-box filters...
      // Note that it is possible to create many instances of ListFilterController,
      // one for each column to filter OR define a unique instance for all attributes and use it to filter the current column,
      // according to "attributeName" attribute current value
      ColumnsFilter filter = new ColumnsFilter();
      grid.addListFilter("stringValue",filter);
      grid.addListFilter("comboValue",filter);
      grid.addListFilter("intValue",filter);
      grid.addListFilter("currencyValue",filter);

      colDate.setListFilter(filter);

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
    grid.setValueObjectClassName("demo6.TestVO");
    colText.setColumnFilterable(true);
    colText.setColumnName("stringValue");
    colText.setColumnSortable(true);
    colDecimal.setDecimals(2);
    colDecimal.setColumnName("numericValue");
    colCurrency.setColumnName("currencyValue");
    colCurrency.setDecimals(3);
    colCurrency.setColumnFilterable(true);
    colDate.setColumnName("dateValue");
    colDate.setColumnFilterable(true);
    colCombo.setDomainId("ORDERSTATE");
    colCombo.setColumnName("comboValue");
    colCombo.setColumnFilterable(true);
    colLookup.setColumnName("lookupValue");
    colLookup.setColumnFilterable(true);
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



  /**
   * <p>Description: Inner class used to collect items to fill in the list-filter.
   * Note that it is possible to create many instances of ListFilterController,
   * one for each column to filter OR define a unique instance for all attributes and use it to filter the current column,
   * according to "attributeName" attribute current value.</p>
   * <p>Copyright: Copyright (C) 2008 Mauro Carniel</p>
   * @version 1.0
   */
  class ColumnsFilter extends ListFilterController {


    /**
     * @param attributeName attribute name that identifies current column
   * @return Response VOListResponse that contains the list of items to show in the list-filter
     */
    public Response getListControlValues(String attributeName) {
      Statement stmt = null;
      ResultSet rset = null;
      try {
        ArrayList rows = new ArrayList();
        stmt = conn.createStatement();

        if (attributeName.equals("stringValue")) {
          rset = stmt.executeQuery("select distinct DEMO6.TEXT from DEMO6");
          while (rset.next()) {
            rows.add(rset.getObject(1));
          }
        }
        else if (attributeName.equals("comboValue")) {
          rset = stmt.executeQuery("select distinct DEMO6.COMBO from DEMO6");
          while (rset.next()) {
            rows.add(rset.getObject(1));
          }
        }
        else if (attributeName.equals("intValue")) {
          rset = stmt.executeQuery("select distinct DEMO6.INTNUM from DEMO6");
          while (rset.next()) {
            rows.add(rset.getObject(1));
          }
        }
        else if (attributeName.equals("currencyValue")) {
          rset = stmt.executeQuery("select distinct DEMO6.CURRNUM from DEMO6");
          while (rset.next()) {
            rows.add(rset.getObject(1));
          }
        }
        else if (attributeName.equals("dateValue")) {
          rset = stmt.executeQuery("select distinct DEMO6.THEDATE from DEMO6");
          while (rset.next()) {
            rows.add(rset.getObject(1));
          }
        }

        return new VOListResponse(rows, false, rows.size());
      }
      catch (Exception ex) {
        return new ErrorResponse(ex.getMessage());
      }
      finally {
        try {
          stmt.close();
        }
        catch (Exception ex1) {
        }
      }
    }

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
