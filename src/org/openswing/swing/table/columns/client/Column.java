package org.openswing.swing.table.columns.client;

import java.beans.*;

import java.awt.*;
import javax.swing.*;
import javax.swing.table.*;

import org.openswing.swing.table.client.*;
import org.openswing.swing.util.client.*;
import org.openswing.swing.util.java.*;
import org.openswing.swing.message.send.java.FilterWhereClause;


/**
 * <p>Title: OpenSwing Framework</p>
 * <p>Description: Based column of the Grid component.
 * This class has not be used directly by the programmer: it's called by GridControl.</p>
 * <p>Copyright: Copyright (C) 2006 Mauro Carniel</p>
 *
 * <p> This file is part of OpenSwing Framework.
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the (LGPL) Lesser General Public
 * License as published by the Free Software Foundation;
 *
 *                GNU LESSER GENERAL PUBLIC LICENSE
 *                 Version 2.1, February 1999
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Library General Public License for more details.
 *
 * You should have received a copy of the GNU Library General Public
 * License along with this library; if not, write to the Free
 * Software Foundation, Inc., 675 Mass Ave, Cambridge, MA 02139, USA.
 *
 *       The author may be contacted at:
 *           maurocarniel@tin.it</p>
 *
 * @author Mauro Carniel
 * @version 1.0
 */
public class Column extends JPanel {

  /** column cell under the column header */
  private JPanel colRow = new JPanel();

  /** used to add column header and column cell */
  private GridBagLayout gridBagLayout1 = new GridBagLayout();

  /** column header */
  private JLabel colHeader = new JLabel();

  /** attribute name linked to this column */
  private String columnName = "columnname";

  /** column width */
  private int preferredWidth = 100;

  /** maximum column width */
  private int maxWidth = 500;

  /** minimum column width */
  private int minWidth = 0;

  /** flag to enable column editing on insert grid mode */
  private boolean editableOnInsert = false;

  /** flag to enable column editing on edit grid mode */
  private boolean editableOnEdit = false;

  /** flag to view the column */
  private boolean columnVisible = true;

  /** flag to enable column sorting by clicking on column header */
  private boolean columnSortable = false;

  /** flag to enable column filtering by clicking with right mouse key */
  private boolean columnFilterable = false;

  /** current sorting versus; 3 possible values: NO_SORTED, ASC_SORTED, DESC_SORTED (this property is managed only if "columnSortable" is true) */
  private String sortVersus = Consts.NO_SORTED;

  /** flag to enable column (in)visibility, by clicking with right mouse key */
  private boolean columnSelectable = true;

  /** flag to define mandatory of column values when the grid is on edit/insert mode */
  private boolean columnRequired = true;

  /** column type (used on design time) */
  private static Class designColumnType;

  /** sequence of columns sorting in ORDER BY */
  private int sortingOrder = 0;

  /** column header description */
  private String headerColumnName = null;

  /** column header horizontal alignment; default value: <code>SwingConstants.CENTER</code> */
  private int headerTextHorizontalAlignment = SwingConstants.CENTER;

  /** define if the cell column value is duplicated when user has clicked on COPY button */
  private boolean columnDuplicable = false;

  /** inner grid */
  private Grids table;

  /** header font; default value: ClientSettings.HEADER_FONT */
  private Font headerFont = ClientSettings.HEADER_FONT;

  /** header foreground color; default value: ClientSettings.HEADER_FOREGROUND_COLOR (which is null as default value, i.e. default JLabel foreground color) */
  private Color headerForegroundColor = ClientSettings.HEADER_FOREGROUND_COLOR;

  /** column text horizontal alignment */
  private int textAlignment = SwingConstants.LEFT;

  /** additional column header description */
  private String additionalHeaderColumnName = null;

  /** additional column header description; default value: 0 (i.e. it is not visible) */
  private int additionalHeaderColumnSpan = 0;

  /** column header vertical alignment; default value: ClientSettings.HEADER_TEXT_VERTICAL_ALIGNMENT */
  private int headerTextVerticalAlignment = ClientSettings.HEADER_TEXT_VERTICAL_ALIGNMENT;

  /** list-filter to add to this column (optional) */
  private ListFilterController filter = null;

  /** default filter conditions to apply when the grid is showed */
  private FilterWhereClause[] defaultFilterValues = null;

  /** flag used to auto fit column size, according to text header; default value: <code>ClientSettings.AUTO_FIT_COLUMNS</code> */
  public boolean autoFitColumn = ClientSettings.AUTO_FIT_COLUMNS;

  public static final int TYPE_TEXT = 0;
  public static final int TYPE_DATE = Consts.TYPE_DATE;
  public static final int TYPE_DATE_TIME = Consts.TYPE_DATE_TIME;
  public static final int TYPE_TIME = Consts.TYPE_TIME;
  public static final int TYPE_INT = 4;
  public static final int TYPE_DEC = 5;
  public static final int TYPE_CHECK = 6;
  public static final int TYPE_COMBO = 7;
  public static final int TYPE_LOOKUP = 8;
  public static final int TYPE_PERC = 9;
  public static final int TYPE_CURRENCY = 10;
  public static final int TYPE_BUTTON = 11;
  public static final int TYPE_IMAGE = 12;
  public static final int TYPE_FORMATTED_TEXT = 13;
  public static final int TYPE_MULTI_LINE_TEXT = 14;
  public static final int TYPE_MULTIPLE_TYPE = 15;
  public static final int TYPE_PROGRESS_BAR = 16;
  public static final int TYPE_COMBO_VO = 17;
  public static final int TYPE_FILE = 18;
  public static final int TYPE_LINK = 19;
  public static final int TYPE_SPINNER_NUMBER = 20;
  public static final int TYPE_SPINNER_LIST = 21;


  public Column() {
    try {
      jbInit();
    }
    catch(Exception e) {
      e.printStackTrace();
    }
  }


  private void jbInit() throws Exception {
    this.setLayout(gridBagLayout1);
    colRow.setBackground(Color.white);
    colRow.setBorder(BorderFactory.createEtchedBorder());
    colRow.setPreferredSize(new Dimension(preferredWidth, ClientSettings.CELL_HEIGHT));
    colHeader.setPreferredSize(new Dimension(preferredWidth, ClientSettings.HEADER_HEIGHT));
    colHeader.setText(columnName);
    colHeader.setBorder(BorderFactory.createRaisedBevelBorder());
    this.add(colHeader,  new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0
            ,GridBagConstraints.NORTH, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
    this.add(colRow,     new GridBagConstraints(0, 2, 1, 1, 1.0, 1.0
            ,GridBagConstraints.NORTHWEST, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
  }


  /**
   * @return column type
   */
  public int getColumnType() {
    throw new UnsupportedOperationException("This method must be override in "+this.getClass().getName()+" class.");
  }


  /**
   * Set attribute name linked to this column.
   * @param columnName attribute name of the value object associated to the grid
   */
  public final void setColumnName(String columnName) {
    this.columnName = columnName;
    if (headerColumnName==null || headerColumnName.equals("")) {
      colHeader.setText(columnName);
      additionalHeaderColumnName = columnName;
    }
  }


  /**
   * @return attribute name of the value object associated to the grid
   */
  public final String getColumnName() {
    try {
      designColumnType = this.getClass();
    }
    catch (Throwable ex) {
    }
    return columnName;
  }


  /**
   * Set column width.
   * @param preferredWidth column width
   */
  public final void setPreferredWidth(int preferredWidth) {
    this.preferredWidth = preferredWidth;

    colHeader.setPreferredSize(new Dimension(preferredWidth,ClientSettings.HEADER_HEIGHT));
    colRow.setPreferredSize(new Dimension(preferredWidth, ClientSettings.CELL_HEIGHT));
  }


  /**
   * @return column width
   */
  public final int getPreferredWidth() {
    return preferredWidth;
  }


  /**
   * Set maximum column width.
   * @param maxWidth maximum column width
   */
  public final void setMaxWidth(int maxWidth) {
    this.maxWidth = maxWidth;
  }


  /**
   * @return maximum column width
   */
  public final int getMaxWidth() {
    return maxWidth;
  }


  /**
   * Set minimum column width.
   * @param minWidth minimum column width
   */
  public final void setMinWidth(int minWidth) {
    this.minWidth = minWidth;
  }


  /**
   * @return minimum column width
   */
  public final int getMinWidth() {
    return minWidth;
  }


  /**
   * Set column editing on insert grid mode (this setting is used only if is not set a cell editing manager oin the grid).
   * @param editableOnEdit column editing on insert grid mode
   */
  public final void setEditableOnInsert(boolean editableOnInsert) {
    this.editableOnInsert = editableOnInsert;
  }


  /**
   * @return column editing on insert grid mode
   */
  public boolean isEditableOnInsert() {
    return editableOnInsert;
  }


  /**
   * Set column editing on edit grid mode (this setting is used only if is not set a cell editing manager oin the grid).
   * @param editableOnEdit column editing on edit grid mode
   */
  public final void setEditableOnEdit(boolean editableOnEdit) {
    this.editableOnEdit = editableOnEdit;
  }


  /**
   * @return column editing on edit grid mode
   */
  public boolean isEditableOnEdit() {
    return editableOnEdit;
  }


  /**
   * @return mandatory of column values when the grid is on edit/insert mode
   */
  public final  boolean isColumnRequired() {
    return columnRequired;
  }


  /**
   * Set mandatory of column values when the grid is on edit/insert mode.
   * @param columnRequired mandatory of column values when the grid is on edit/insert mode
   */
  public final void setColumnRequired(boolean columnRequired) {
    this.columnRequired = columnRequired;
  }


  /**
   * Set column visibility.
   * @param columnVisibile column visibility
   */
  public final void setColumnVisible(boolean columnVisible) {
    this.columnVisible = columnVisible;
    super.setVisible(columnVisible);
  }


  /**
   * @return column visibility
   */
  public final boolean isColumnVisible() {
    return columnVisible;
  }


  /**
   * Set column sorting abilitation by clicking on column header.
   * @param columnSortable column sorting is enabled
   */
  public final void setColumnSortable(boolean columnSortable) {
    this.columnSortable = columnSortable;
  }


  /**
   * @return column sorting is enabled
   */
  public final boolean isColumnSortable() {
    return columnSortable;
  }


  /**
   * Set column filtering.
   * @param columnFilterable column filtering is enabled
   */
  public final void setColumnFilterable(boolean columnFilterable) {
    this.columnFilterable = columnFilterable;
  }


  /**
   * @return column filtering is enabled
   */
  public final boolean isColumnFilterable() {
    return columnFilterable;
  }


  /**
   * Set current sorting versus; 3 possible values: NO_SORTED, ASC_SORTED, DESC_SORTED (this property is managed only if "columnSortable" is true).
   * @param sortVersus current sorting versus
   */
  public final void setSortVersus(String sortVersus) {
    this.sortVersus = sortVersus;
  }


  /**
   * @return current sorting versus; 3 possible values: NO_SORTED, ASC_SORTED, DESC_SORTED (this property is managed only if "columnSortable" is true)
   */
  public final String getSortVersus() {
    return sortVersus;
  }


  /**
   * Set if column (in)visibility state can be changed by user.
   * @param columnSelectable column (in)visibility state can be changed by user
   */
  public final void setColumnSelectable(boolean columnSelectable) {
    this.columnSelectable = columnSelectable;
  }


  /**
   * @return <code>true</code> if column (in)visibility state can be changed by user
   */
  public final boolean isColumnSelectable() {
    return columnSelectable;
  }




  /**
   * @return colunm type
   */
  public static Class getDesignColumnType() {
    return designColumnType;
  }


  /**
   * @return order of sorting columns in ORDER BY
   */
  public final int getSortingOrder() {
    return sortingOrder;
  }


  /**
   * Set order of sorting columns in ORDER BY.
   * @param sortingOrder order of sorting columns in ORDER BY
   */
  public final void setSortingOrder(int sortingOrder) {
    this.sortingOrder = sortingOrder;
  }


  /**
   * @return String header column name
   */
  public final String getHeaderColumnName() {
    return (headerColumnName==null || headerColumnName.equals("")?getColumnName():headerColumnName);
  }


  /**
   * Set header column name.
   * @param headerColumnName header column name
   */
  public final void setHeaderColumnName(String headerColumnName) {
    this.headerColumnName = headerColumnName;
    if (headerColumnName!=null && !headerColumnName.equals("")) {
      colHeader.setText(headerColumnName);
      additionalHeaderColumnName = headerColumnName;
    }
    else {
      colHeader.setText(columnName);
      additionalHeaderColumnName = columnName;
    }
  }


  /**
   * @return column header horizontal alignment
   */
  public final int getHeaderTextHorizontalAlignment() {
    return this.headerTextHorizontalAlignment;
  }


  /**
   * Set column header horizontal alignement.
   * @param alignment column header horizontal alignement; possible values: "SwingConstants.LEFT", "SwingConstants.RIGHT", "SwingConstants.CENTER".
   */
  public final void setHeaderTextHorizontalAlignment(int alignment) {
    this.headerTextHorizontalAlignment = alignment;

    try {
      if (!Beans.isDesignTime())
        return;
      this.remove(colHeader);
      int align = GridBagConstraints.NORTHWEST;
      if (alignment==SwingConstants.RIGHT)
        align = GridBagConstraints.NORTHEAST;
      else if (alignment==SwingConstants.CENTER)
        align = GridBagConstraints.NORTH;

      this.add(colHeader, new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0
                                                 , align,
                                                 GridBagConstraints.HORIZONTAL,
                                                 new Insets(0, 0, 0, 0), 0, 0));

      this.revalidate();
      this.repaint();
    }
    catch (Exception ex) {
    }
  }


  /**
   * @return column header horizontal alignment
   */
  public final int getHeaderTextVerticalAlignment() {
    return this.headerTextVerticalAlignment;
  }


  /**
   * Set column header vertical alignement.
   * @param alignment column header vertical alignement; possible values: "SwingConstants.TOP", "SwingConstants.CENTER", "SwingConstants.BOTTOM".
   */
  public final void setHeaderTextVerticalAlignment(int alignment) {
    this.headerTextVerticalAlignment = alignment;
  }


  /**
   * @return define if the cell column value is duplicated when user has clicked on COPY button
   */
  public final boolean isColumnDuplicable() {
    return columnDuplicable;
  }


  /**
   * Define if the cell column value is duplicated when user has clicked on COPY button.
   * @param columnDuplicable define if the cell column value is duplicated when user has clicked on COPY button
   */
  public final void setColumnDuplicable(boolean columnDuplicable) {
    this.columnDuplicable = columnDuplicable;
  }


  /**
   * @return table linked to this column
   */
  public final Grids getTable() {
    return table;
  }


  /**
   * Set table linked to this column
   */
  public final void setTable(Grids table) {
    this.table = table;
  }


  /**
   * Set column header font.
   */
  public final void setHeaderFont(Font headerFont) {
    this.headerFont = headerFont;
    if (headerFont!=null && Beans.isDesignTime()) {
      colHeader.setFont(headerFont);
    }
  }


  /**
   * @return column header font
   */
  public final Font getHeaderFont() {
    return this.headerFont;
  }


  /**
   * Set foreground color for column header.
   */
  public final void setHeaderForegroundColor(Color headerForegroundColor) {
    this.headerForegroundColor = headerForegroundColor;
    if (headerForegroundColor!=null && Beans.isDesignTime()) {
      colHeader.setForeground(headerForegroundColor);
    }
  }


  /**
   * @return foreground color for column header
   */
  public final Color getHeaderForegroundColor() {
    return this.headerForegroundColor;
  }


  /**
   * @return column text horizontal alignment
   */
  public final int getTextAlignment() {
    return this.textAlignment;
  }


  /**
   * Set column text horizontal alignement.
   * @param alignment column text horizontal alignement; possible values: "SwingConstants.LEFT", "SwingConstants.RIGHT", "SwingConstants.CENTER".
   */
  public final void setTextAlignment(int alignment) {
    this.textAlignment = alignment;
  }


  /**
   * @return additional column header description
   */
  public final String getAdditionalHeaderColumnName() {
    return additionalHeaderColumnName;
  }


  /**
   * @return additional column header description
   */
  public final int getAdditionalHeaderColumnSpan() {
    return additionalHeaderColumnSpan;
  }


  /**
   * Set the additional column header description.
   * @param additionalHeaderColumnName additional column header description
   */
  public final void setAdditionalHeaderColumnName(String additionalHeaderColumnName) {
    this.additionalHeaderColumnName = additionalHeaderColumnName;
  }


  /**
   * Set the additional column header description.
   * @param additionalHeaderColumnSpan additional column header description
   */
  public final void setAdditionalHeaderColumnSpan(int additionalHeaderColumnSpan) {
    this.additionalHeaderColumnSpan = additionalHeaderColumnSpan;
  }


  /**
   * @return TableCellRenderer for this column
   */
  public TableCellRenderer getCellRenderer(GridController tableContainer,Grids grids) {
    return new DefaultTableCellRenderer();
  }


  /**
   * @return TableCellEditor for this column
   */
  public TableCellEditor getCellEditor(GridController tableContainer,Grids grids) {
    return new DefaultTableCellEditor();
  }


  /**
   * Add a list-filter for the specified column, showed in the quick filter panel.
   * @param attributeName attribute name that identifies the column having a list-filter to remove
   */
  public final void setListFilter(ListFilterController filter) {
    this.filter = filter;
  }


  /**
   * @return list-filter for the specified column, showed in the quick filter panel
   */
  public final ListFilterController getListFilter() {
    return this.filter;
  }



  /**
   * @return flag used to auto fit column size, according to text header
   */
  public final boolean isAutoFitColumn() {
    return autoFitColumn;
  }


  /**
   * Set auto fit column size, according to text headers.
   * Default value: <code>ClientSettings.AUTO_FIT_COLUMNS</code>
   * @param autoFitColumns  used to auto fit columns size, according to text header
   */
  public final void setAutoFitColumn(boolean autoFitColumn) {
    this.autoFitColumn = autoFitColumn;
  }


  /**
   * @return default filter conditions to apply when the grid is showed
   */
  public final FilterWhereClause[] getDefaultFilterValues() {
    return defaultFilterValues;
  }


  /**
   * Set the default filter conditions to apply when the grid is showed.
   * @param defaultFilterValues default filter conditions to apply when the grid is showed (a two elements length array is required)
   */
  public final void setDefaultFilterValues(FilterWhereClause firstFilterValue,FilterWhereClause optionalSecondFilterValue) {
    this.defaultFilterValues = new FilterWhereClause[]{firstFilterValue,optionalSecondFilterValue};
  }





  /**
   * <p>Title: OpenSwing Framework</p>
   * <p>Description: Inner class used in getCellEditor() method.</p>
   * <p>Copyright: Copyright (C) 2006 Mauro Carniel</p>
   * @author Mauro Carniel
   * @version 1.0
   */
  class DefaultTableCellEditor extends DefaultCellEditor implements TableCellEditor {


    public DefaultTableCellEditor() {
      super(new JTextField());
    }

  }



}
