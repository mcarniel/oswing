package demo3;

import java.math.*;
import java.sql.*;
import java.util.*;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.text.*;

import org.openswing.swing.client.*;
import org.openswing.swing.lookup.client.*;
import org.openswing.swing.message.receive.java.*;
import org.openswing.swing.table.client.*;
import org.openswing.swing.table.columns.client.*;
import org.openswing.swing.table.java.*;
import java.text.ParseException;


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
  NavigatorBar navigatorBar1 = new NavigatorBar();
  FormattedTextColumn colFormattedText = new FormattedTextColumn();
  IntegerColumn colInt = new IntegerColumn();
  MultiLineTextColumn colMultiLine = new MultiLineTextColumn();
  FilterButton filterButton = new FilterButton();


  public GridFrame(Connection conn,GridFrameController controller) {
    this.conn = conn;
    try {
      jbInit();
      setSize(750,600);
      grid.setController(controller);
      grid.setGridDataLocator(controller);

      LookupController lookupController = new DemoLookupController(conn);
      colLookup.setLookupController(lookupController);

      // set top grid content, i.e. the first row...
      grid.setTopGridDataLocator(new GridDataLocator() {

        public Response loadData(
            int action,
            int startIndex,
            Map filteredColumns,
            ArrayList currentSortedColumns,
            ArrayList currentSortedVersusColumns,
            Class valueObjectType,
            Map otherGridParams) {
          ArrayList rows = new ArrayList();
          TestVO vo = new TestVO();
          vo.setDateValue(new java.sql.Date(System.currentTimeMillis()));
          vo.setStringValue("This is a locked row");
          rows.add(vo);
          return new VOListResponse(rows,false,rows.size());
        }

      });


      // set bottom grid content, i.e. the last two rows...
      grid.setBottomGridDataLocator(new GridDataLocator() {

        public Response loadData(
            int action,
            int startIndex,
            Map filteredColumns,
            ArrayList currentSortedColumns,
            ArrayList currentSortedVersusColumns,
            Class valueObjectType,
            Map otherGridParams) {
          ArrayList rows = getTotals();
          return new VOListResponse(rows,false,rows.size());
        }

      });

      grid.setBottomGridController(new GridController() {

        /**
         * Method used to define the background color for each cell of the grid.
         * @param rowNumber selected row index
         * @param attributedName attribute name related to the column currently selected
         * @param value object contained in the selected cell
         * @return background color of the selected cell
         */
        public Color getBackgroundColor(int row,String attributedName,Object value) {
          return new Color(220,220,220);
        }

        /**
         * Method used to define the font to use for each cell of the grid.
         * @param rowNumber selected row index
         * @param attributeName attribute name related to the column currently selected
         * @param value object contained in the selected cell
         * @param defaultFont default font currently in used with this column
         * @return font to use for the current cell; null means default font usage; default value: null
         */
        public Font getFont(int row,String attributeName,Object value,Font defaultFont) {
          if (attributeName.equals("currencyValue") || attributeName.equals("numericValue"))
            return new Font(defaultFont.getFontName(),Font.BOLD,defaultFont.getSize());
          else
            return null;
        }

      });


      setVisible(true);

    }
    catch(Exception e) {
      e.printStackTrace();
    }
  }


  /**
   * @return calculate totals for numeric and currency colums and return one row having those totals.
   */
  private ArrayList getTotals() {
    ArrayList rows = new ArrayList();

    TestVO vo = new TestVO();
    vo.setDateValue(new java.sql.Date(System.currentTimeMillis()));
    vo.setStringValue("Total currencies");
    BigDecimal tot = new BigDecimal(0);
    BigDecimal tot2 = new BigDecimal(0);
    TestVO testVO = null;
    for(int i=0;i<grid.getVOListTableModel().getRowCount();i++) {
      testVO = (TestVO)grid.getVOListTableModel().getObjectForRow(i);
      if (testVO.getCurrencyValue()!=null)
        tot = tot.add(testVO.getCurrencyValue());
      if (testVO.getNumericValue()!=null)
        tot2 = tot2.add(testVO.getNumericValue());
    }
    vo.setCurrencyValue(tot);
    vo.setNumericValue(tot2);
    rows.add(vo);

    return rows;
  }



  public void reloadData() {
    grid.reloadData();
  }


  private void jbInit() throws Exception {
    grid.setFunctionId("F1");

    grid.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);

    buttonsPanel.setLayout(flowLayout1);
    flowLayout1.setAlignment(FlowLayout.LEFT);
    grid.setCopyButton(copyButton);
    grid.setDeleteButton(deleteButton);
    grid.setEditButton(editButton);
    grid.setExportButton(exportButton);
    grid.setInsertButton(insertButton);
    grid.setNavBar(navigatorBar1);
    grid.setReloadButton(reloadButton);
    grid.setFilterButton(filterButton);
    grid.setRowHeight(40);
    grid.setSaveButton(saveButton);

//    grid.setLockedColumns(2);

    grid.setLockedRowsOnTop(1);

    grid.setLockedRowsOnBottom(1);

    grid.setValueObjectClassName("demo3.TestVO");
    colText.setColumnName("stringValue");
    colText.setColumnSortable(true);
    colText.setEditableOnInsert(true);
    colText.setSortVersus(org.openswing.swing.util.java.Consts.ASC_SORTED);
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
    colCurrency.setGrouping(true);
    colDate.setColumnDuplicable(true);
    colDate.setColumnFilterable(true);
    colDate.setColumnName("dateValue");
    colDate.setColumnRequired(false);
    colDate.setEditableOnEdit(true);
    colDate.setEditableOnInsert(true);

    Calendar cal = Calendar.getInstance();
    cal.set(cal.YEAR,1983);
    cal.set(cal.MONTH,1);
    cal.set(cal.DAY_OF_MONTH,24);

    colDate.setDefaultDate(cal);
    colCombo.setDomainId("ORDERSTATE");
    colCombo.setColumnDuplicable(true);
    colCombo.setColumnName("comboValue");
    colCombo.setColumnRequired(false);
    colCombo.setEditableOnEdit(true);
    colCombo.setEditableOnInsert(true);
    colCombo.setColumnFilterable(true);
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
    colCheck.setPositiveValue("Y");
    colCheck.setNegativeValue("N");
    colCheck.setColumnFilterable(true);
    colCheck.setColumnName("checkValue");
    colCheck.setColumnRequired(false);
    colCheck.setEditableOnEdit(true);
    colCheck.setEditableOnInsert(true);
    colCheck.setAllowNullValue(true);
    insertButton.setText("insertButton1");
    editButton.setText("editButton1");
    saveButton.setText("saveButton1");
    colButton.setColumnName("button");
    colButton.setEditableOnEdit(true);
    colButton.setHeaderColumnName("button");
    colButton.setPreferredWidth(50);
    colFormattedText.setColumnFilterable(true);
    colFormattedText.setColumnName("formattedTextValue");
    colFormattedText.setColumnSortable(false);
    colFormattedText.setEditableOnEdit(true);
    colFormattedText.setEditableOnInsert(true);

//    MaskFormatter mask = new MaskFormatter("###-##-####");
//    mask.setValidCharacters("0123456789");

    JFormattedTextField.AbstractFormatter formatter = new JFormattedTextField.AbstractFormatter() {

      /**
       * Parses <code>text</code> returning an arbitrary Object. Some
       * formatters may return null.
       *
       * @throws ParseException if there is an error in the conversion
       * @param text String to convert
       * @return Object representation of text
       */
      public Object stringToValue(String text) throws ParseException {
        if (text==null || text.equals(""))
          return null;
        String t = "";
        for(int i=0;i<text.length();i++)
          if (Character.isDigit(text.charAt(i)))
            t += text.charAt(i);
          else
            if (!( text.charAt(i)=='-' && (i==3 || i==6) ))
                throw new ParseException("Invalid pattern!",i);
        return new BigDecimal(t);
      }

      /**
       * Returns the string value to display for <code>value</code>.
       *
       * @throws ParseException if there is an error in the conversion
       * @param value Value to convert
       * @return String representation of value
       */
      public String valueToString(Object value) throws ParseException {
        if (value==null)
          return null;
        String t = value.toString();
        if (t.length()!=9)
          throw new ParseException("Invalid pattern!",t.length()-1);
        t = t.substring(0,3)+"-"+t.substring(3,5)+"-"+t.substring(5);
        return t;
      }

    };

    colFormattedText.setFormatter(formatter);

    colInt.setColumnFilterable(false);
    colInt.setColumnName("intValue");
    colInt.setColumnSortable(false);
    colInt.setEditableOnEdit(true);
    colInt.setEditableOnInsert(true);

    colMultiLine.setColumnName("multiLineTextValue");
    colMultiLine.setEditableOnEdit(true);
    colMultiLine.setEditableOnInsert(true);
    colMultiLine.setPreferredWidth(150);
    this.getContentPane().add(grid, BorderLayout.CENTER);
    this.getContentPane().add(buttonsPanel, BorderLayout.NORTH);
    buttonsPanel.add(insertButton, null);
    buttonsPanel.add(copyButton, null);
    buttonsPanel.add(editButton, null);
    buttonsPanel.add(reloadButton, null);
    buttonsPanel.add(saveButton, null);
    buttonsPanel.add(exportButton, null);
    buttonsPanel.add(deleteButton, null);
    buttonsPanel.add(filterButton,null);
    buttonsPanel.add(navigatorBar1, null);
    grid.getColumnContainer().add(colText, null);
    grid.getColumnContainer().add(colDecimal, null);
    grid.getColumnContainer().add(colMultiLine, null);
    grid.getColumnContainer().add(colInt, null);
    grid.getColumnContainer().add(colCurrency, null);
    grid.getColumnContainer().add(colFormattedText, null);
    grid.getColumnContainer().add(colDate, null);
    grid.getColumnContainer().add(colCombo, null);
    grid.getColumnContainer().add(colButton, null);
    grid.getColumnContainer().add(colLookup, null);
    grid.getColumnContainer().add(textColumn1, null);
    grid.getColumnContainer().add(colCheck, null);

    colButton.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {

      }
    });


  }
  public GridControl getGrid() {
    return grid;
  }



}

