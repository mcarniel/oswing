package demo9;

import javax.swing.*;
import org.openswing.swing.client.*;
import java.awt.*;
import org.openswing.swing.table.columns.client.*;
import org.openswing.swing.lookup.client.LookupController;
import java.sql.*;
import java.awt.event.*;
import org.openswing.swing.table.java.*;
import java.awt.event.ActionEvent;
import javax.swing.ListSelectionModel;
import java.util.ArrayList;
import org.openswing.swing.message.send.java.FilterWhereClause;
import org.openswing.swing.util.java.Consts;


/**
 * <p>Title: OpenSwing Framework</p>
 * <p>Description: Grid Frame where the user can insert more than one row at a time.</p>
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
  IntegerColumn colInt = new IntegerColumn();
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
  FilterButton filterButton = new FilterButton();
  JPanel topPanel = new JPanel();
  MultiLookupControl multiLookupControl1 = new MultiLookupControl(new LookupController(),new String[] {"lookupValue","descrLookupValue"}," - ");
  GridBagLayout gridBagLayout1 = new GridBagLayout();
  JButton searchButton = new JButton();


  public GridFrame(Connection conn,GridFrameController controller) {
    super.setDefaultCloseOperation(super.EXIT_ON_CLOSE);
    this.conn = conn;
    try {
      jbInit();
      setSize(750,500);
      grid.setController(controller);
      grid.setGridDataLocator(controller);

      LookupController lookupController = new DemoLookupController(conn);
      colLookup.setLookupController(lookupController);

      // setup multi code lookup...
      multiLookupControl1.setController(new DemoLookupController(conn));


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
    grid.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
    grid.setHeaderHeight(40);
    buttonsPanel.setLayout(flowLayout1);
    flowLayout1.setAlignment(FlowLayout.LEFT);
    grid.setMaxSortedColumns(1);
    grid.setCopyButton(copyButton);
    grid.setDeleteButton(deleteButton);
    grid.setEditButton(editButton);
    grid.setInsertRowsOnTop(false);
    grid.setExportButton(exportButton);
    grid.setMaxNumberOfRowsOnInsert(50);
    grid.setAllowInsertInEdit(true);
    grid.setInsertButton(insertButton);
    grid.setFilterButton(filterButton);
    grid.setReloadButton(reloadButton);
    grid.setSaveButton(saveButton);
    grid.setValueObjectClassName("demo9.TestVO");
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

    colInt.setColumnFilterable(true);
    colInt.setColumnSortable(true);
    colInt.setColumnDuplicable(true);
    colInt.setColumnName("intValue");
    colInt.setColumnRequired(false);
    colInt.setEditableOnEdit(true);
    colInt.setEditableOnInsert(true);

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
    colLookup.setPreferredWidth(60);
    colLookup.setEditableOnEdit(true);
    colLookup.setEditableOnInsert(true);
    colLookup.setMaxCharacters(5);
    textColumn1.setColumnDuplicable(true);
    textColumn1.setColumnName("descrLookupValue");
    textColumn1.setColumnRequired(false);
    textColumn1.setPreferredWidth(150);
    colCheck.setColumnDuplicable(true);
    colCheck.setColumnName("checkValue");
    colCheck.setEnableInReadOnlyMode(true);
    colCheck.setColumnRequired(false);
    colCheck.setEditableOnEdit(true);
    colCheck.setEditableOnInsert(true);
    insertButton.setText("insertButton1");
    editButton.setText("editButton1");
    saveButton.setText("saveButton1");
    colButton.setColumnName("button");
    colButton.setHeaderColumnName("button");
    colButton.setPreferredWidth(50);
    colButton.setEditableOnEdit(true);
    colButton.setEditableOnInsert(true);
    colButton.setEnableInReadOnlyMode(true);


//    colButton.setIconName("chiuso.gif");
//    colButton.setText("");

    colButton.setShowAttributeValue(true);


    topPanel.setLayout(gridBagLayout1);
    searchButton.setMnemonic('S');
    searchButton.setText("Search");
    searchButton.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        searchButton_actionPerformed(e);
      }
    });
    this.getContentPane().add(grid, BorderLayout.CENTER);
    buttonsPanel.add(insertButton, null);
    buttonsPanel.add(copyButton, null);
    buttonsPanel.add(editButton, null);
    buttonsPanel.add(reloadButton, null);
    buttonsPanel.add(saveButton, null);
    buttonsPanel.add(exportButton, null);
    buttonsPanel.add(deleteButton, null);
    buttonsPanel.add(filterButton, null);
    topPanel.add(searchButton,  new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0
            ,GridBagConstraints.NORTHEAST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
    this.getContentPane().add(topPanel, BorderLayout.NORTH);
    topPanel.add(multiLookupControl1,    new GridBagConstraints(0, 0, 1, 1, 1.0, 1.0
            ,GridBagConstraints.NORTHWEST, GridBagConstraints.BOTH, new Insets(5, 5, 0, 5), 0, 0));
    topPanel.add(buttonsPanel,    new GridBagConstraints(0, 2, 1, 1, 1.0, 1.0
            ,GridBagConstraints.NORTHWEST, GridBagConstraints.BOTH, new Insets(5, 5, 5, 5), 0, 0));
    grid.getColumnContainer().add(colText, null);
    grid.getColumnContainer().add(colInt, null);
    grid.getColumnContainer().add(colDecimal, null);
    grid.getColumnContainer().add(colCurrency, null);
    grid.getColumnContainer().add(colDate, null);
    grid.getColumnContainer().add(colCombo, null);
    grid.getColumnContainer().add(colButton, null);
    grid.getColumnContainer().add(colLookup, null);
    grid.getColumnContainer().add(textColumn1, null);
    grid.getColumnContainer().add(colCheck, null);
    colButton.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        // do something...
        System.out.println("button pressed");
      }
    });

  }
  public GridControl getGrid() {
    return grid;
  }

  void searchButton_actionPerformed(ActionEvent e) {
    ArrayList vos = multiLookupControl1.getSelectedVOs();
    if (vos.size()==0)
      grid.getQuickFilterValues().remove("lookupValue");
    else {
      ArrayList list = new ArrayList();
      TestLookupVO vo = null;
      for(int i=0;i<vos.size();i++) {
        vo = (TestLookupVO)vos.get(i);
        list.add(vo.getLookupValue());
      }
      FilterWhereClause f = new FilterWhereClause("lookupValue",Consts.IN,list);
      grid.getQuickFilterValues().put("lookupValue",new FilterWhereClause[]{f,null});
    }
    grid.reloadData();
  }





}

