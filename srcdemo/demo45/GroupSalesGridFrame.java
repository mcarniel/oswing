package demo45;

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


/**
 * <p>Title: OpenSwing Framework</p>
 * <p>Description: Grid Frame for departments.</p>
 * <p>Copyright: Copyright (C) 2006 Mauro Carniel</p>
 * <p> </p>
 * @author Mauro Carniel
 * @version 1.0
 */
public class GroupSalesGridFrame extends InternalFrame {
  GridControl grid = new GridControl();
  JPanel buttonsPanel = new JPanel();
  ReloadButton reloadButton = new ReloadButton();
  FlowLayout flowLayout1 = new FlowLayout();
  DateColumn colSaleDate = new DateColumn();
  TextColumn colArea = new TextColumn();
  TextColumn colNote = new TextColumn();
  private Connection conn = null;
  EditButton editButton = new EditButton();
  SaveButton saveButton = new SaveButton();
  CurrencyColumn colTotalAmount = new CurrencyColumn();
  DecimalColumn colSalesNumber = new DecimalColumn();


  public GroupSalesGridFrame(Connection conn,GroupSalesFrameController controller) {
    this.conn = conn;
    try {
      jbInit();
      setSize(770,400);
      grid.setController(controller);
      grid.setGridDataLocator(controller);

      setTitle(ClientSettings.getInstance().getResources().getResource("group sales"));
      MDIFrame.add(this);
    }
    catch(Exception e) {
      e.printStackTrace();
    }
  }


  public void reloadData() {
    grid.reloadData();
  }


  private void jbInit() throws Exception {
    colSaleDate.setColumnName("saleDate");
    grid.setFunctionId("getGroupSales");
    buttonsPanel.setLayout(flowLayout1);
    flowLayout1.setAlignment(FlowLayout.LEFT);
    grid.setEditButton(editButton);
    grid.setReloadButton(reloadButton);
    grid.setSaveButton(saveButton);
    grid.setValueObjectClassName("demo45.GroupSalesVO");
    colArea.setColumnFilterable(true);
    colArea.setColumnName("area");
    colArea.setColumnSortable(true);
    colArea.setTrimText(true);
    colArea.setUpperCase(true);
    colNote.setColumnName("note");
    colNote.setEditableOnEdit(true);
    colNote.setColumnRequired(false);
    colNote.setPreferredWidth(300);
    colTotalAmount.setColumnDuplicable(true);
    colTotalAmount.setColumnName("totalAmount");
    colTotalAmount.setColumnRequired(true);
    colTotalAmount.setColumnSortable(true);
    colTotalAmount.setEditableOnEdit(true);
    colTotalAmount.setMaxWidth(150);

    colSalesNumber.setColumnDuplicable(true);
    colSalesNumber.setColumnName("salesNumber");
    colSalesNumber.setColumnRequired(true);
    colSalesNumber.setColumnSortable(true);
    colSalesNumber.setEditableOnEdit(true);
    colSalesNumber.setPreferredWidth(120);

    this.getContentPane().add(grid, BorderLayout.CENTER);
    this.getContentPane().add(buttonsPanel, BorderLayout.NORTH);
    buttonsPanel.add(editButton, null);
    buttonsPanel.add(reloadButton, null);
    buttonsPanel.add(saveButton, null);
    grid.getColumnContainer().add(colSaleDate, null);
    grid.getColumnContainer().add(colArea, null);
    grid.getColumnContainer().add(colTotalAmount, null);
    grid.getColumnContainer().add(colSalesNumber, null);
    grid.getColumnContainer().add(colNote, null);


  }


}

