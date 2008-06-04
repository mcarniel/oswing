package demo40;

import javax.swing.*;
import org.openswing.swing.client.*;
import java.awt.*;
import org.openswing.swing.table.columns.client.*;
import java.util.*;
import org.openswing.swing.util.java.Consts;

/**
 * <p>Title: OpenSwing Framework</p>
 * <p>Description: Panel that contains a grid for order rows.</p>
 * <p>Copyright: Copyright (C) 2008 Mauro Carniel</p>
 * @version 1.0
 */
public class OrderRowDiscountsGridPanel extends JPanel {
  GridControl grid = new GridControl();
  BorderLayout borderLayout1 = new BorderLayout();
  JPanel buttonsPanel = new JPanel();
  FlowLayout flowLayout1 = new FlowLayout();
  ReloadButton reloadButton1 = new ReloadButton();
  ExportButton exportButton1 = new ExportButton();
  TextColumn colDiscountCode = new TextColumn();
  TextColumn colDiscountDescr = new TextColumn();
  CurrencyColumn colValue = new CurrencyColumn();
  InsertButton insertButton1 = new InsertButton();
  EditButton editButton1 = new EditButton();
  DeleteButton deleteButton1 = new DeleteButton();
  SaveButton saveButton1 = new SaveButton();


  public OrderRowDiscountsGridPanel(OrderRowDiscountsController controller) {
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
    grid.setMaxNumberOfRowsOnInsert(10);
    grid.setInsertButton(insertButton1);
    grid.setDeleteButton(deleteButton1);
    grid.setEditButton(editButton1);
    grid.setExportButton(exportButton1);
    grid.setReloadButton(reloadButton1);
    grid.setSaveButton(saveButton1);
    grid.setValueObjectClassName("demo40.OrderRowDiscountVO");
    grid.setVisibleStatusPanel(false);
    colDiscountCode.setColumnName("discountCode");
    colDiscountCode.setColumnFilterable(true);
    colDiscountCode.setColumnSortable(true);
    colDiscountCode.setEditableOnInsert(true);
    colDiscountCode.setHeaderColumnName("Discount Code");
    colDiscountCode.setPreferredWidth(90);
    colDiscountDescr.setColumnName("discountDescription");
    colDiscountDescr.setColumnFilterable(true);
    colDiscountDescr.setColumnSortable(true);
    colDiscountDescr.setEditableOnEdit(true);
    colDiscountDescr.setEditableOnInsert(true);
    colDiscountDescr.setHeaderColumnName("Description");
    colDiscountDescr.setPreferredWidth(200);
    colValue.setColumnName("discountValue");
    colValue.setEditableOnEdit(true);
    colValue.setEditableOnInsert(true);
    colValue.setHeaderColumnName("Value");
    colValue.setPreferredWidth(100);
    this.setBorder(BorderFactory.createRaisedBevelBorder());
    this.add(grid,  BorderLayout.CENTER);
    this.add(buttonsPanel, BorderLayout.NORTH);
    buttonsPanel.add(insertButton1, null);
    buttonsPanel.add(editButton1, null);
    buttonsPanel.add(saveButton1, null);
    buttonsPanel.add(reloadButton1, null);
    buttonsPanel.add(deleteButton1, null);
    buttonsPanel.add(exportButton1, null);
    grid.getColumnContainer().add(colDiscountCode, null);
    grid.getColumnContainer().add(colDiscountDescr, null);
    grid.getColumnContainer().add(colValue, null);
  }

}
