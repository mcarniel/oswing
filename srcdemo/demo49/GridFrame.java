package demo49;

import java.io.*;
import java.text.*;
import java.util.*;

import java.awt.*;
import javax.swing.*;

import org.openswing.swing.message.receive.java.*;
import org.openswing.swing.pivottable.aggregators.java.*;
import org.openswing.swing.pivottable.client.*;
import org.openswing.swing.pivottable.functions.java.*;
import org.openswing.swing.pivottable.java.*;
import org.openswing.swing.pivottable.server.*;
import org.openswing.swing.pivottable.tablemodelreaders.server.*;
import org.openswing.swing.util.dataconverters.java.*;
import org.openswing.swing.client.*;


/**
 * <p>Title: OpenSwing Framework</p>
 * <p>Description: Grid Frame</p>
 * <p>Copyright: Copyright (C) 2006 Mauro Carniel</p>
 * <p> </p>
 * @author Mauro Carniel
 * @version 1.0
 */
public class GridFrame extends JFrame {

  PivotTable pivotTable = new PivotTable();
  JPanel buttonsPanel = new JPanel();
  FlowLayout flowLayout1 = new FlowLayout();
  ReloadButton reloadButton1 = new ReloadButton();
  ExportButton exportButton1 = new ExportButton();
  FilterButton filterButton1 = new FilterButton();


  public GridFrame() {
//  public GridFrame(ArrayList vos) {
    try {
      jbInit();
      setSize(750,600);
      setTitle("Pivot Table for sales");
      setDefaultCloseOperation(this.EXIT_ON_CLOSE);

      CSVFileReader reader = new CSVFileReader(
          new File("orders.txt"),
          new String[]{
            "orderDate",
            "category",
            "subCategory",
            "country",
            "region",
            "agent",
            "item",
            "sellQty",
            "sellAmount"
          },
          new DataConverter[]{
            new StringToDateConverter(new SimpleDateFormat("dd/MM/yyyy")),
            new DataConverter(),
            new DataConverter(),
            new DataConverter(),
            new DataConverter(),
            new DataConverter(),
            new DataConverter(),
            new StringToDoubleConverter(),
            new StringToDoubleConverter()
          }
      );

//      TableModelReader reader = new TableModelReader(
//          vos,
//          new String[]{"category","subCategory","item","sellQty","sellAmount","orderDate"}
//      );

      final PivotTableEngine engine = new PivotTableEngine(reader);

      pivotTable.setController(new PivotTableController(){

        public Response getPivotTableModel(PivotTableParameters pars) {
          long t = System.currentTimeMillis();
          Response res = engine.getPivotTableModel(pars);
          System.out.println("Analysis time: "+(System.currentTimeMillis()-t)+"ms");
          return res;
        }

      });

      RowField rowFieldCategory = new RowField("category",100);
      RowField rowFieldSubCategory = new RowField("subCategory",100);
      RowField rowFieldItem = new RowField("item",100);
      RowField rowFieldAgent = new RowField("agent",150);
      RowField rowFieldCountry = new RowField("country",100);
      RowField rowFieldRegion = new RowField("region",100);

      pivotTable.getAllRowFields().add(rowFieldCategory);
      pivotTable.getAllRowFields().add(rowFieldSubCategory);
      pivotTable.getAllRowFields().add(rowFieldItem);
      pivotTable.getAllRowFields().add(rowFieldAgent);
      pivotTable.getAllRowFields().add(rowFieldCountry);
      pivotTable.getAllRowFields().add(rowFieldRegion);

      pivotTable.addRowField(rowFieldCategory);
      pivotTable.addRowField(rowFieldSubCategory);
      pivotTable.addRowField(rowFieldItem);

      ColumnField columnFieldYear = new ColumnField("orderDate","year",new YearAggregator());
      ColumnField columnFieldQuarter = new ColumnField("orderDate","quarter",new QuarterAggregator());
      ColumnField columnFieldAgent = new ColumnField("agent");
      ColumnField columnFieldCountry = new ColumnField("country");
      ColumnField columnFieldRegion = new ColumnField("region");

      pivotTable.getAllColumnFields().add(columnFieldYear);
      pivotTable.getAllColumnFields().add(columnFieldQuarter);
      pivotTable.getAllColumnFields().add(columnFieldAgent);
      pivotTable.getAllColumnFields().add(columnFieldCountry);
      pivotTable.getAllColumnFields().add(columnFieldRegion);

      pivotTable.addColumnField(columnFieldYear);
      pivotTable.addColumnField(columnFieldQuarter);

      DataField dataFieldSellQty = new DataField("sellQty",50,"sellQty",new SumFunction()); // sum function...
      NumberFormat nf = NumberFormat.getCurrencyInstance();
      nf.setCurrency(Currency.getInstance(Locale.ITALY));
      nf.setMaximumFractionDigits(2);
      nf.setMinimumFractionDigits(2);
      nf.setGroupingUsed(true);
      DataField dataFieldSellAmount = new DataField("sellAmount",80,"sells",new SumFunction());  // sum function...
      dataFieldSellAmount.setFormatter(nf);

      pivotTable.getAllDataFields().add(dataFieldSellQty);
      pivotTable.getAllDataFields().add(dataFieldSellAmount);

      pivotTable.addDataField(dataFieldSellQty);
      pivotTable.addDataField(dataFieldSellAmount);

      pivotTable.setDataFieldRenderer(new DataFieldRenderer() {

        /**
         * @param currentColor current color to set
         * @param rowPath GenericNodeKey row fields path that identify current row
         * @param colPath GenericNodeKey column fields path that identify current column
         * @param value value to show in the specified cell
         * @param row current row
         * @param col current column
         * @return Color background color to set
         */
        public Color getBackgroundColor(Color currentColor,GenericNodeKey rowPath,GenericNodeKey colPath,Object value,int row,int col) {
          if (rowPath.getPath().length<pivotTable.getPivotTableParameters().getRowFields().size() ||
              colPath.getPath().length<pivotTable.getPivotTableParameters().getColumnFields().size()+1) {
            int c = 200+rowPath.getPath().length*colPath.getPath().length*5;
            return new Color(c,c,c);
          }
          return currentColor;
        }


        /**
         * @param currentColor current color to set
         * @param rowPath GenericNodeKey row fields path that identify current row
         * @param colPath GenericNodeKey column fields path that identify current column
         * @param value value to show in the specified cell
         * @param row current row
         * @param col current column
         * @return Color foreground color to set
         */
        public Color getForegroundColor(Color currentColor,GenericNodeKey rowPath,GenericNodeKey colPath,Object value,int row,int col) {
          return currentColor;
        }


        /**
         * @param currentFont current font to set
         * @param rowPath GenericNodeKey row fields path that identify current row
         * @param colPath GenericNodeKey column fields path that identify current column
         * @param value value to show in the specified cell
         * @param row current row
         * @param col current column
         * @return font to set
         */
        public Font getFont(Font currentFont,GenericNodeKey rowPath,GenericNodeKey colPath,Object value,int row,int col) {
          if (rowPath.getPath().length<pivotTable.getPivotTableParameters().getRowFields().size() ||
              colPath.getPath().length<pivotTable.getPivotTableParameters().getColumnFields().size()+1)
            return new Font(currentFont.getFontName(),Font.BOLD,currentFont.getSize());
          return currentFont;
        }

      });

      setVisible(true);
    }
    catch(Exception e) {
      e.printStackTrace();
    }
  }


  private void jbInit() throws Exception {
    buttonsPanel.setLayout(flowLayout1);
    flowLayout1.setAlignment(FlowLayout.LEFT);
    pivotTable.setExportButton(exportButton1);
    pivotTable.setFilterButton(filterButton1);
    pivotTable.setReloadButton(reloadButton1);
    this.getContentPane().add(pivotTable, BorderLayout.CENTER);
    this.getContentPane().add(buttonsPanel, BorderLayout.NORTH);
    buttonsPanel.add(reloadButton1, null);
    buttonsPanel.add(exportButton1, null);
    buttonsPanel.add(filterButton1, null);
  }




}

