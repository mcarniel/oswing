package org.openswing.swing.pivottable.client;

import java.beans.*;
import java.io.*;
import java.text.*;
import java.util.*;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.table.*;

import org.openswing.swing.client.*;
import org.openswing.swing.export.java.*;
import org.openswing.swing.logger.client.*;
import org.openswing.swing.message.receive.java.*;
import org.openswing.swing.pivottable.cellspantable.client.*;
import org.openswing.swing.pivottable.java.*;
import org.openswing.swing.util.client.*;
import org.openswing.swing.util.java.*;
import org.openswing.swing.pivottable.java.GenericNodeKey;


/**
 * <p>Title: OpenSwing Framework</p>
 * <p>Description: pivot table.</p>
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
public final class PivotTable extends JPanel implements DataController,DraggableButtonListener {

  /** flag used in addNotify method */
  private boolean firstTime = true;

  /** flag used to indicate that Pivot Table is not yet updated with current row/column fields */
  private boolean pivotTableChanged = false;

  GridBagLayout gridBagLayout1 = new GridBagLayout();

  public static final String DATA_PANEL = "DATA_PANEL";
  public static final String ROWS_PANEL = "ROWS_PANEL";
  public static final String COLUMNS_PANEL = "COLUMNS_PANEL";

  /** panel that hosts data fields */
  JPanel dataPanel = new JPanel();

  /** panel that hosts row fields */
  JPanel rowsPanel = new JPanel();

  /** panel that hosts column fields */
  JPanel columnsPanel = new JPanel();

  /** data table */
  JTable dataTable = new JTable();

  /** rows table */
  CellSpanTable rowsTable = new CellSpanTable();

  /** columns table */
  CellSpanTable colsTable = new CellSpanTable();

  /** scroll pane used by data table */
  JScrollPane dataScrollPane = new JScrollPane();

  /** scroll pane used by rows table */
  JScrollPane rowsScrollPane = new JScrollPane();

  /** scroll pane used by cols table */
  JScrollPane colsScrollPane = new JScrollPane();

  FlowLayout flowLayout1 = new FlowLayout();

  FlowLayout flowLayout3 = new FlowLayout();

  /** collection of pairs <row index,RowGenericNode objects related to current expandable/collapsable row field> */
  private HashMap currentExpandableRowFields = new HashMap();

  /** collection of pairs <column index,GlobalColGenericNode objects related to current expandable/collapsable row field> */
  private HashMap currentExpandableColFields = new HashMap();

  /** controller used by PivotTable to retrieve the PivotTableModel */
  private PivotTableController controller = null;

  /** model of Pivot Table */
  private PivotTableModel pivotTableModel = null;

  /** Pivot Table parameters, used to create PivotTable content */
  private PivotTableParameters pars = new PivotTableParameters();

  /** date formatter */
  private SimpleDateFormat sdf = null;

  /** color for fields panel */
  private Color backPanel = new JPanel().getBackground();

  /** export button (optional) */
  private ExportButton exportButton = null;

  /** reload button (optional) */
  private ReloadButton reloadButton = null;

  /** filter button (optional) */
  private FilterButton filterButton = null;

  /** binded buttons */
  private HashSet bindedButtons = new HashSet();

  /** identifier (functionId) associated to the container */
  private String functionId = null;

  /** current enabled button state, before GenericButton.setEnabled method calling */
  private HashMap currentValueButtons = new HashMap();

  /** data fields decleared for this pivot table; they are not applied, but are only showed in filter dialog */
  private ArrayList allDataFields = new ArrayList();

  /** row fields decleared for this pivot table; they are not applied, but are only showed in filter dialog */
  private ArrayList allRowFields = new ArrayList();

  /** column fields decleared for this pivot table; they are not applied, but are only showed in filter dialog */
  private ArrayList allColumnFields = new ArrayList();

  /** flag used to autocompile Pivot table when showing it; default value: <code>true</code> */
  private boolean autoCompile = true;

  /** (optional) renderer used to set background/foreground color for each data field cell and related font */
  private DataFieldRenderer dataFieldRenderer = null;


  public PivotTable() {
    try {
      jbInit();
      sdf = new SimpleDateFormat(ClientSettings.getInstance().getResources().getDateMask(Consts.TYPE_DATE));
    }
    catch(Exception e) {
      e.printStackTrace();
    }
  }


  private void jbInit() throws Exception {
    this.setLayout(gridBagLayout1);
    rowsPanel.setLayout(null);
    columnsPanel.setLayout(null);
    flowLayout1.setAlignment(FlowLayout.LEFT);
    rowsTable.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
    colsTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
    dataTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
    dataTable.setShowGrid(true);
    dataPanel.setLayout(flowLayout3);
    flowLayout3.setAlignment(FlowLayout.LEFT);
    rowsScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
    rowsScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_NEVER);
    dataScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
    dataScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
    colsScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
    colsScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_NEVER);
    this.add(rowsPanel,        new GridBagConstraints(0, 3, 1, 1, 0.0, 0.0
            ,GridBagConstraints.NORTHWEST, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
    this.add(dataScrollPane,      new GridBagConstraints(1, 4, 1, 1, 1.0, 1.0
            ,GridBagConstraints.NORTHWEST, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
    this.add(columnsPanel,     new GridBagConstraints(0, 2, 1, 1, 0.0, 0.0
            ,GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
    this.add(rowsScrollPane,         new GridBagConstraints(0, 4, 1, 1, 0.0, 1.0
            ,GridBagConstraints.NORTHWEST, GridBagConstraints.BOTH, new Insets(0, 0, dataScrollPane.getHorizontalScrollBar().getPreferredSize().height, 0), 0, 0));
    this.add(colsScrollPane,        new GridBagConstraints(1, 2, 1, 2, 1.0, 0.0
            ,GridBagConstraints.NORTHWEST, GridBagConstraints.BOTH, new Insets(0, 0, 0, dataScrollPane.getVerticalScrollBar().getPreferredSize().width), 0, 0));
    this.add(dataPanel,    new GridBagConstraints(0, 1, 2, 1, 0.0, 0.0
            ,GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));

    dataScrollPane.getViewport().add(dataTable, null);
    rowsScrollPane.getViewport().add(rowsTable, null);
    colsScrollPane.getViewport().add(colsTable, null);

    dataScrollPane.getHorizontalScrollBar().addAdjustmentListener(new AdjustmentListener() {

      /**
       * Listen for horizontal scrolling, in order to scroll colsTable too.
       */
      public void adjustmentValueChanged(AdjustmentEvent e) {
        colsScrollPane.getHorizontalScrollBar().setValue(e.getValue());
      }

    });

    dataScrollPane.getVerticalScrollBar().addAdjustmentListener(new AdjustmentListener() {

      /**
       * Listen for vertical scrolling, in order to scroll rowsTable too.
       */
      public void adjustmentValueChanged(AdjustmentEvent e) {
        rowsScrollPane.getVerticalScrollBar().setValue(e.getValue());
      }

    });

  }


  /**
   * Method automatically invoked when this panel is showed.
   */
  public final void addNotify() {
    super.addNotify();
    if (!Beans.isDesignTime() && firstTime) {
      firstTime = false;
      SwingUtilities.invokeLater(new Runnable() {
        public void run() {
          prepareContent();
        }
      });
    }
  }


  /**
   * @return Pivot Table parameters, used to create PivotTable content
   */
  public final PivotTableParameters getPivotTableParameters() {
    return pars;
  }


  private void prepareContent() {
      try {
        backPanel = new Color(backPanel.getRed() + 10, backPanel.getGreen() + 10,backPanel.getBlue() + 10);
      }
      catch (Exception ex) {
      }

      rowsTable.setRowHeight(ClientSettings.CELL_HEIGHT);
      rowsTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
      rowsTable.setSurrendersFocusOnKeystroke(true);
      rowsTable.setForeground(ClientSettings.GRID_CELL_FOREGROUND);
      rowsTable.setRowSelectionAllowed(false);
      rowsTable.setColumnSelectionAllowed(false);
      rowsTable.setBackground(new JButton().getBackground());
      rowsScrollPane.setBorder(BorderFactory.createEmptyBorder());
      colsScrollPane.setBorder(BorderFactory.createEmptyBorder());
      dataScrollPane.setBorder(BorderFactory.createEmptyBorder());

      colsTable.setRowHeight(ClientSettings.HEADER_HEIGHT);
      colsTable.setRowSelectionAllowed(false);
      colsTable.setColumnSelectionAllowed(false);
      colsTable.setBackground(new JButton().getBackground());

      dataTable.setRowHeight(ClientSettings.CELL_HEIGHT);
      dataTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
      dataTable.setSurrendersFocusOnKeystroke(true);
      dataTable.setBackground(ClientSettings.GRID_CELL_BACKGROUND);
      dataTable.setForeground(ClientSettings.GRID_CELL_FOREGROUND);
      dataTable.setSelectionBackground(ClientSettings.GRID_SELECTION_BACKGROUND);
      dataTable.setSelectionForeground(ClientSettings.GRID_SELECTION_FOREGROUND);
      dataTable.setShowGrid(true);
      dataTable.setRowSelectionAllowed(false);
      dataTable.setColumnSelectionAllowed(false);

      rowsTable.addMouseListener(new MouseAdapter() {
        public void mouseClicked(MouseEvent e) {
          processRowClick(rowsTable.getSelectedRow(),rowsTable.getSelectedColumn());
        }
      });

      colsTable.addMouseListener(new MouseAdapter() {
        public void mouseClicked(MouseEvent e) {
          processColClick(colsTable.getSelectedRow(),colsTable.getSelectedColumn());
        }
      });

      dataTable.addMouseListener(new MouseAdapter() {
        public void mouseClicked(final MouseEvent e) {
          if (e.getClickCount()==1 && SwingUtilities.isRightMouseButton(e)) {
            JPopupMenu menu = new JPopupMenu();
            JMenuItem expAllMenu = new JMenuItem(
              ClientSettings.getInstance().getResources().getResource("expand all"),
              ClientSettings.getInstance().getResources().getResource("expand all").charAt(0)
            );
            expAllMenu.addActionListener(new ActionListener() {
              public void actionPerformed(ActionEvent e) {
                expandAll();
              }
            });
            menu.add(expAllMenu);
            JMenuItem collapseAllMenu = new JMenuItem(
              ClientSettings.getInstance().getResources().getResource("collapse all"),
              ClientSettings.getInstance().getResources().getResource("collapse all").charAt(0)
            );
            collapseAllMenu.addActionListener(new ActionListener() {
              public void actionPerformed(ActionEvent e) {
                collapseAll();
              }
            });
            menu.add(collapseAllMenu);

            menu.show(dataTable,e.getX(),e.getY());
          }
        }
      });

      if (autoCompile)
        compileDataInThread();
  }


  /**
   * Set the controller used by PivotTable to retrieve the PivotTableModel
   * @param controller controller used by PivotTable to retrieve the PivotTableModel
   */
  public final void setController(PivotTableController controller) {
    this.controller = controller;
  }


  /**
   * Add a column name as row field in Pivot Table.
   * @param rowField row field
   */
  public final void addRowField(RowField rowField) {
    pars.getRowFields().add(rowField);
    setPivotTableChanged(true);
  }


  /**
   * Add a column name as row field in Pivot Table.
   * @param index column index to use to insert row field
   * @param rowField row field
   */
  public final void addRowField(int index,RowField rowField) {
    pars.getRowFields().add(index,rowField);
    setPivotTableChanged(true);
  }


  /**
   * Remove a column name as row field in Pivot Table.
   * @param rowField row field
   */
  public final void removeRowField(RowField rowField) {
    int index = pars.getRowFields().indexOf(rowField);
    pars.getRowFields().remove(index);
    if (index!=-1) {
      setPivotTableChanged(true);
    }
  }


  /**
   * Add a column name as column field in Pivot Table.
   * @param columnField column field
   */
  public final void addColumnField(ColumnField columnField) {
    pars.getColumnFields().add(columnField);
    setPivotTableChanged(true);
  }


  /**
   * Add a column name as column field in Pivot Table.
   * @param index column index to use to insert column field
   * @param columnField column field
   */
  public final void addColumnField(int index,ColumnField columnField) {
    pars.getColumnFields().add(index,columnField);
    setPivotTableChanged(true);
  }


  /**
   * Remove a column name as column field in Pivot Table.
   * @param columnField column field
   */
  public final void removeColumnField(ColumnField columnField) {
    pars.getColumnFields().remove(columnField);
    setPivotTableChanged(true);
  }


  /**
   * Add a column name as data field in Pivot Table.
   * @param dataField data field
   */
  public final void addDataField(DataField dataField) {
    pars.getDataFields().add(dataField);
    setPivotTableChanged(true);
  }


  /**
   * Add a column name as data field in Pivot Table.
   * @param index column index to use to insert data field
   * @param dataField data field
   */
  public final void addDataField(int index,DataField dataField) {
    pars.getDataFields().add(index,dataField);
    setPivotTableChanged(true);
  }


  /**
   * Remove a column name as data field in Pivot Table.
   * @param dataField data field
   */
  public final void removeDataField(DataField dataField) {
    int index = pars.getDataFields().indexOf(dataField);
    pars.getDataFields().remove(index);
    if (index!=-1) {
      setPivotTableChanged(true);
    }
  }


  /**
   * Method invoked to re-analyze data model and show data in Pivot Table.
   * Data analysis is perfomed in a separated thread, in order to avoid to block application usage.
   */
  public final void compileDataInThread() {
    SwingUtilities.invokeLater(new Runnable() {
      public void run() {
        try {
          setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
          Toolkit.getDefaultToolkit().sync();
          compileData();
        }
        catch (Throwable ex) {
        }
        finally {
          setCursor(Cursor.getDefaultCursor());
          Toolkit.getDefaultToolkit().sync();
        }
      }
    });
  }


  public final void dragEventFired(DraggableButtonEvent e) {
    ArrayList list = null;
    if (e.getPanelId().equals(ROWS_PANEL))
      list = pars.getRowFields();
    else if (e.getPanelId().equals(COLUMNS_PANEL))
      list = pars.getColumnFields();
    else if (e.getPanelId().equals(DATA_PANEL))
      list = pars.getDataFields();

    if (e.getTarget()<e.getSource()) {
      Object obj = list.remove(e.getSource());
      list.add(e.getTarget(),obj);
    }
    else {
      Object obj = list.get(e.getSource());
      if (e.getTarget()==list.size()-1)
        list.add(obj);
      else
        list.add(e.getTarget()+1,obj);
      list.remove(e.getSource());
    }
    compileDataInThread();
  }


  private void removeAllButtons(JPanel panel) {
    DraggableButton b = null;
    while(panel.getComponentCount()>0) {
      b = (DraggableButton)panel.getComponent(0);
      b.removeDraggableButtonListener(this);
      panel.remove(0);
    }
  }


  /**
   * Method invoked to re-analyze data model and show data in Pivot Table.
   */
  public final void compileData() {
    if (firstTime) {
      Logger.error(this.getClass().getName(),"compileData","compileData method cannot be invoked before showing grid: Pivot Table cannot be created.",null);
      return;
    }
    if (controller==null) {
      Logger.error(this.getClass().getName(),"compileData","setController method has not been invoked: Pivot Table cannot be created.",null);
      return;
    }

    ClientUtils.fireBusyEvent(true);
    Response res = null;
    try {
      res = controller.getPivotTableModel(pars);
    }
    catch (Throwable ex) {
      Logger.error(this.getClass().getName(),"compileData",res.getErrorMessage(),null);
      return;
    }
    finally {
      ClientUtils.fireBusyEvent(false);
    }
    if (res.isError()) {
      Logger.error(this.getClass().getName(),"compileData",res.getErrorMessage(),null);
      return;
    }
    currentExpandableRowFields.clear();
    currentExpandableColFields.clear();

    pivotTableModel = (PivotTableModel)((VOResponse)res).getVo();

    // define data panel content...
    DraggableButton button = null;

    removeAllButtons(dataPanel);
    dataPanel.setLayout(new FlowLayout(FlowLayout.LEFT,0,0));
    for(int i=0;i<pars.getDataFields().size();i++) {
      final DataField dataField = (DataField)pars.getDataFields().get(i);
      button = new DraggableButton(DATA_PANEL,i);
      button.setBackground(backPanel);
      button.add(new JLabel(ClientSettings.getInstance().getResources().getResource(dataField.getDescription())));
      button.addDraggableButtonListener(this);
      button.setBorder(BorderFactory.createRaisedBevelBorder());
      button.setSize(new Dimension(button.getPreferredSize().width,ClientSettings.CELL_HEIGHT));
      final int index = i;
      button.addMouseListener(new MouseAdapter() {
        public void mouseClicked(MouseEvent e) {
          dataFieldClicked(dataField,index,e.getClickCount());
        }
      });
      dataPanel.add(button,null);
    }
    dataPanel.revalidate();
    dataPanel.repaint();

    // define rows panel content...
    removeAllButtons(rowsPanel);
    int w = 1;
    for(int i=0;i<pars.getRowFields().size();i++) {
      final RowField rowField = (RowField)pars.getRowFields().get(i);
      button = new DraggableButton(ROWS_PANEL,i);
      button.setBackground(backPanel);
      button.setLayout(new GridBagLayout());
      button.add(new JLabel(ClientSettings.getInstance().getResources().getResource(rowField.getDescription())),    new GridBagConstraints(0, 1, 1, 1, 1.0, 1.0
              ,GridBagConstraints.CENTER, GridBagConstraints.CENTER, new Insets(0, 0, 0, 0), 0, 0));
      button.addDraggableButtonListener(this);
      button.setBorder(BorderFactory.createRaisedBevelBorder());
      final int index = i;
      button.addMouseListener(new MouseAdapter() {
        public void mouseClicked(MouseEvent e) {
          rowFieldClicked(rowField,index,e.getClickCount());
        }
      });
      button.setBounds(new Rectangle(w-1, 1, rowField.getWidth()-2+(i==pars.getRowFields().size()-1?1:0), ClientSettings.HEADER_HEIGHT-1));
      w += rowField.getWidth()-1;
      rowsPanel.add(button, null);
    }
    rowsPanel.setSize(new Dimension(w,ClientSettings.HEADER_HEIGHT));
    rowsPanel.setPreferredSize(new Dimension(w,ClientSettings.HEADER_HEIGHT));
    rowsPanel.setMinimumSize(new Dimension(w,ClientSettings.HEADER_HEIGHT));
    rowsPanel.revalidate();
    rowsPanel.repaint();

    // define columns panel content...
    removeAllButtons(columnsPanel);
    columnsPanel.setSize(new Dimension(rowsPanel.getPreferredSize().width,ClientSettings.HEADER_HEIGHT*pars.getColumnFields().size()+1));
    columnsPanel.setPreferredSize(new Dimension(rowsPanel.getPreferredSize().width,ClientSettings.HEADER_HEIGHT*pars.getColumnFields().size()+1));
    columnsPanel.setMinimumSize(new Dimension(rowsPanel.getPreferredSize().width,ClientSettings.HEADER_HEIGHT*pars.getColumnFields().size()+1));
    for(int i=0;i<pars.getColumnFields().size();i++) {
      final ColumnField columnField = (ColumnField)pars.getColumnFields().get(i);
      button = new DraggableButton(COLUMNS_PANEL,i);
      button.setBackground(backPanel);
      button.setSize(button.getWidth(),ClientSettings.HEADER_HEIGHT);
      button.setLayout(new GridBagLayout());
      button.add(new JLabel(ClientSettings.getInstance().getResources().getResource(columnField.getDescription())),    new GridBagConstraints(0, 1, 1, 1, 1.0, 1.0
              ,GridBagConstraints.CENTER, GridBagConstraints.CENTER, new Insets(0, 0, 0, 0), 0, 0));
      button.addDraggableButtonListener(this);
      button.setBorder(BorderFactory.createRaisedBevelBorder());
      final int index = i;
      button.addMouseListener(new MouseAdapter() {
        public void mouseClicked(MouseEvent e) {
          columnFieldClicked(columnField,index,e.getClickCount());
        }
      });
      button.setBounds(new Rectangle(columnsPanel.getPreferredSize().width-button.getPreferredSize().width-10-1, i*ClientSettings.HEADER_HEIGHT, button.getPreferredSize().width+10, ClientSettings.HEADER_HEIGHT+(i==pars.getColumnFields().size()-1?1:0)));
      columnsPanel.add(button,null);
    }
    columnsPanel.revalidate();
    columnsPanel.repaint();

    buildPivotTable();

    setPivotTableChanged(false);
  }


  /**
   * Set current state of pivotTableChanged flag
   */
  private void setPivotTableChanged(boolean pivotTableChanged) {
    this.pivotTableChanged = pivotTableChanged;
  }


  /**
   * Redefine table columns visibility, according to current row/column fields visibility.
   */
  private void buildPivotTable() {
    currentExpandableRowFields.clear();
    currentExpandableColFields.clear();

    // define TableModel for all tables...
    new RowFieldsTableModel();
    new ColumnFieldsTableModel();
    dataTable.setModel(new DataFieldsTableModel());
    rowsTable.setShowGrid(false);
    colsTable.setShowGrid(false);

    int rowsNum = pars.getColumnFields().size() + 1;
    colsTable.setSize(new Dimension(dataTable.getWidth(),rowsNum*ClientSettings.HEADER_HEIGHT+1));
    colsTable.setMinimumSize(new Dimension(dataTable.getWidth(),rowsNum*ClientSettings.HEADER_HEIGHT+1));
    colsScrollPane.setSize(new Dimension(dataTable.getWidth(),rowsNum*ClientSettings.HEADER_HEIGHT+1));
    colsScrollPane.setMinimumSize(new Dimension(dataTable.getWidth(),rowsNum*ClientSettings.HEADER_HEIGHT+1));

    // remove headers from all tables...
    JTableHeader th = rowsTable.getTableHeader();
    th.setPreferredSize(new Dimension(th.getPreferredSize().width,0));
    th.setReorderingAllowed(false);
    th.setResizingAllowed(false);
    th = colsTable.getTableHeader();
    th.setPreferredSize(new Dimension(th.getPreferredSize().width,0));
    th.setReorderingAllowed(false);
    th.setResizingAllowed(false);
    th = dataTable.getTableHeader();
    th.setPreferredSize(new Dimension(th.getPreferredSize().width,0));
    th.setReorderingAllowed(false);
    th.setResizingAllowed(false);

    // define columns width for all tables...
    for(int i=0;i<pars.getRowFields().size();i++) {
      rowsTable.getColumnModel().getColumn(i).setPreferredWidth(((RowField)pars.getRowFields().get(i)).getWidth());
      rowsTable.getColumnModel().getColumn(i).setCellRenderer(new RowsHeaderRenderer());
    }
    int k = 0;
    for(int i=0;i<colsTable.getColumnModel().getColumnCount();i++) {
      colsTable.getColumnModel().getColumn(i).setPreferredWidth(((DataField)pars.getDataFields().get(k)).getWidth());
      colsTable.getColumnModel().getColumn(i).setCellRenderer(new ColsHeaderRenderer());
      k++;
      if (k==pars.getDataFields().size())
        k = 0;
    }
    k=0;
    DefaultTableCellRenderer dr = new DataFieldsRenderer();
    for(int i=0;i<dataTable.getColumnModel().getColumnCount();i++) {
      dataTable.getColumnModel().getColumn(i).setPreferredWidth(((DataField)pars.getDataFields().get(k)).getWidth());
      dataTable.getColumnModel().getColumn(i).setCellRenderer(dr);
      k++;
      if (k==pars.getDataFields().size())
        k = 0;
    }

    // repaint tables content...
    rowsTable.revalidate();
    rowsTable.repaint();
    colsTable.revalidate();
    colsTable.repaint();
    dataTable.revalidate();
    dataTable.repaint();
    revalidate();
    repaint();

    if (dataTable.getRowCount()>0) {
      dataTable.setRowSelectionInterval(0,0);
      dataTable.setColumnSelectionInterval(0,0);
    }
  }



  private void rowFieldClicked(RowField rowField,int index,int clickCount) {
    if (clickCount==2 && pars.getRowFields().size()>1) {
      pars.getRowFields().remove(rowField);
      compileDataInThread();
    }
  }


  private void columnFieldClicked(ColumnField columnField,int index,int clickCount) {
    if (clickCount==2 && pars.getColumnFields().size()>1) {
      pars.getColumnFields().remove(columnField);
      compileDataInThread();
    }
  }


  private void dataFieldClicked(DataField dataField,int index,int clickCount) {
    if (clickCount==2 && pars.getDataFields().size()>1) {
      pars.getDataFields().remove(dataField);
      compileDataInThread();
    }
  }


  /**
   * Set the row filter, used to skip rows from original data model.
   * @param rowFilter row filter
   */
  public final void setInputFilter(InputFilter rowFilter) {
    pars.setInputFilter(rowFilter);
  }


  /**
   * Expand all row/column fields.
   */
  private void expandAll() {
    int level = 1;
    boolean containsChildren = true;
    while(containsChildren) {
      containsChildren = false;
      for(int i=0;i<pivotTableModel.getHRoot().getChildrenCount();i++)
        containsChildren = containsChildren || setExpansionStateNode(pivotTableModel.getHRoot().getChildren(i),true,level);
      level++;
      buildPivotTable();
    }
    level = 0;
    containsChildren = true;
    while(containsChildren) {
      containsChildren = false;
      for(int i=0;i<pivotTableModel.getVRoot().getChildrenCount();i++) {
        containsChildren = containsChildren || setExpansionStateNode(pivotTableModel.getVRoot().getChildren(i),true,level);
        buildPivotTable();
      }
      level++;
    }
  }


  /**
   * Collapse all row/column fields.
   */
  private void collapseAll() {
    currentExpandableColFields.clear();
    currentExpandableRowFields.clear();

    for(int i=0;i<pivotTableModel.getHRoot().getChildrenCount();i++)
      setExpansionStateNode(pivotTableModel.getHRoot().getChildren(i),false,-1);
    for(int i=0;i<pivotTableModel.getVRoot().getChildrenCount();i++)
      setExpansionStateNode(pivotTableModel.getVRoot().getChildren(i),false,-1);

    buildPivotTable();
  }


  /**
   * Collapse all row nodes.
   */
  private boolean setExpansionStateNode(RowGenericNode node,boolean expanded,int level) {
    node.setNodeExpanded(expanded);
    boolean containsChildren = false;
    if (node.getLevel()<=level || level==-1)
      for(int i=0;i<node.getChildrenCount();i++)
        containsChildren = containsChildren || setExpansionStateNode(node.getChildren(i),expanded,level);
    return containsChildren;
  }


  /**
   * Collapse column nodes.
   */
  private boolean setExpansionStateNode(GlobalColGenericNode node,boolean expanded,int level) {
    node.setNodeExpanded(expanded);
    boolean containsChildren = false;
    if (node.getLevel()<=level || level==-1)
      for(int i=0;i<node.getChildrenCount();i++)
        containsChildren = containsChildren || setExpansionStateNode(node.getChildren(i),expanded,level);
    return containsChildren;
  }



  /**
   * <p>Title: OpenSwing Framework</p>
   * <p>Description: Inner class used to define row fields content.</p>
   * <p>Copyright: Copyright (C) 2008 Mauro Carniel</p>
   * @version 1.0
   */
  class RowFieldsTableModel extends AbstractTableModel {

    ArrayList rows = new ArrayList();

    public RowFieldsTableModel() {
      rowsTable.setModel(this);
      rowsTable.setGridSize(new Dimension(pars.getRowFields().size(),0));
      processNode(rows,pivotTableModel.getHRoot(),0);
    }


    private void processNode(ArrayList rows,RowGenericNode parentNode,int pos) {
      Object[] row = null;
      RowGenericNode n = null;
      int oldIndex = -1;
      int[] rowCellsSpan = null;
      int[] colCellsSpan = null;
      for(int i=0;i<parentNode.getChildrenCount();i++) {
        row = new Object[pars.getRowFields().size()];
        n = (RowGenericNode)parentNode.getChildren(i);
        row[pos] = formatValue(n.getValue());
        rows.add(row);
        oldIndex = rows.size()-1;
        rowsTable.addRow();
        currentExpandableRowFields.put(new Integer(oldIndex),n);
        if (n.isNodeExpanded()) {
          n.setNodeExpanded(true);

          processNode(rows,n,pos+1);
          colCellsSpan = new int[pars.getRowFields().size()-pos-1];
          for(int j=0;j<colCellsSpan.length;j++)
            colCellsSpan[j] = pos+1+j;
          rowsTable.combine(new int[]{oldIndex},colCellsSpan);

          colCellsSpan = new int[]{pos};
        }
        else {
          colCellsSpan = new int[pars.getRowFields().size()-pos];
          for(int j=0;j<colCellsSpan.length;j++)
            colCellsSpan[j] = pos+j;
        }

        rowCellsSpan = new int[rows.size()-oldIndex];
        for(int j=0;j<rowCellsSpan.length;j++)
          rowCellsSpan[j] = oldIndex+j;
        rowsTable.combine(rowCellsSpan,colCellsSpan);
      }
    }


    public int getColumnCount() {
      return pars.getRowFields().size();
    }


    public int getRowCount() {
      return rows.size();
    }


    public Object getValueAt(int rowIndex, int columnIndex) {
      return ((Object[])rows.get(rowIndex))[columnIndex];
    }

  }


  private void processRowClick(int row,int col) {
    if (col==pars.getRowFields().size()-1)
      return;
    if (currentExpandableRowFields.get(new Integer(row))!=null) {
      RowGenericNode n = (RowGenericNode)currentExpandableRowFields.remove(new Integer(row));
      n.setNodeExpanded(!n.isNodeExpanded());
      buildPivotTable();
    }
  }



  /**
   * <p>Title: OpenSwing Framework</p>
   * <p>Description: Inner class used to define column fields content.</p>
   * <p>Copyright: Copyright (C) 2008 Mauro Carniel</p>
   * @version 1.0
   */
  class ColumnFieldsTableModel extends AbstractTableModel {

    ArrayList cols = new ArrayList();

    public ColumnFieldsTableModel() {
//      colsTable.setModel(this);
      colsTable.setGridSize(new Dimension(0,pars.getColumnFields().size()+1));
      processNode(cols,pivotTableModel.getVRoot(),0);
      colsTable.setModel(this);
    }


    private void processNode(ArrayList cols,GlobalColGenericNode parentNode,int pos) {
      Object[] col = null;
      GlobalColGenericNode n = null;
      int oldIndex = -1;
      int[] rowCellsSpan = null;
      int[] colCellsSpan = null;
      for(int i=0;i<parentNode.getChildrenCount();i++) {
        col = new Object[pars.getColumnFields().size()+1];
        n = (GlobalColGenericNode)parentNode.getChildren(i);
        col[pos] = formatValue(n.getValue());
        col[pars.getColumnFields().size()] = ClientSettings.getInstance().getResources().getResource( ((DataField)pars.getDataFields().get(0)).getDescription() );
        cols.add(col);
        oldIndex = cols.size()-1;
        colsTable.addColumn();
        currentExpandableColFields.put(new Integer(oldIndex),n);

        for(int k=1;k<pars.getDataFields().size();k++) {
          col = new Object[pars.getColumnFields().size()+1];
          col[pars.getColumnFields().size()] = ClientSettings.getInstance().getResources().getResource( ((DataField)pars.getDataFields().get(k)).getDescription() );
          cols.add(col);
          colsTable.addColumn();
        }



        if (n.isNodeExpanded()) {
          n.setNodeExpanded(true);
          processNode(cols,n,pos+1);

          rowCellsSpan = new int[pars.getColumnFields().size()-pos-1];
          for(int j=0;j<rowCellsSpan.length;j++)
            rowCellsSpan[j] = pos+1+j;
          colCellsSpan = new int[pars.getDataFields().size()];
          for(int j=0;j<colCellsSpan.length;j++)
            colCellsSpan[j] = oldIndex+j;
          colsTable.combine(rowCellsSpan,colCellsSpan);

          rowCellsSpan = new int[]{pos};
        }
        else {
          rowCellsSpan = new int[pars.getColumnFields().size()-pos];
          for(int j=0;j<rowCellsSpan.length;j++)
            rowCellsSpan[j] = pos+j;
        }
        colCellsSpan = new int[cols.size()-oldIndex];
        for(int j=0;j<colCellsSpan.length;j++)
          colCellsSpan[j] = oldIndex+j;
        colsTable.combine(rowCellsSpan,colCellsSpan);
      }
    }


    public int getColumnCount() {
      return cols.size();
    }


    public int getRowCount() {
      return pars.getColumnFields().size() + 1;
    }


    public Object getValueAt(int rowIndex, int columnIndex) {
      return ((Object[])cols.get(columnIndex))[rowIndex];
    }

  } // end inner class


  private Object formatValue(Object obj) {
    if (obj!=null) {
      if (obj instanceof java.util.Date)
        return sdf.format( (java.util.Date)obj );
      else if (obj instanceof Number) {
        Number num = (Number)obj;
        if (num.longValue()==num.doubleValue())
          return new Long((num.longValue()));
        else
          return num;
      }
      else
        return obj;
    }
    else
      return obj;
  }


  private void processColClick(int row,int col) {
    if (row==pars.getColumnFields().size())
      return;
    if (currentExpandableColFields.get(new Integer(col))!=null) {
      GlobalColGenericNode n = (GlobalColGenericNode)currentExpandableColFields.remove(new Integer(col));
      n.setNodeExpanded(!n.isNodeExpanded());
      buildPivotTable();
    }
  }


  public FilterButton getFilterButton() {
    return filterButton;
  }
  public void setFilterButton(FilterButton filterButton) {
    this.filterButton = filterButton;
    filterButton.addDataController(this);
  }
  public ExportButton getExportButton() {
    return exportButton;
  }
  public void setExportButton(ExportButton exportButton) {
    this.exportButton = exportButton;
    exportButton.addDataController(this);
  }
  public ReloadButton getReloadButton() {
    return reloadButton;
  }
  public void setReloadButton(ReloadButton reloadButton) {
    this.reloadButton = reloadButton;
    reloadButton.addDataController(this);
  }


  public final void copy() {
  }


  public final void delete() {
  }


  public final void edit() {
  }


  /**
   * Invoked when pressing export button.
   */
  public final void export() {
    try {
      ExportOptions opt = new ExportOptions(
          null,
          ClientSettings.getInstance().getResources().getDateMask(Consts.TYPE_DATE),
          ClientSettings.getInstance().getResources().getDateMask(Consts.TYPE_TIME),
          ClientSettings.getInstance().getResources().getDateMask(Consts.TYPE_DATE_TIME),
          ExportOptions.XLS_FORMAT,
          ClientSettings.EXPORT_TO_PDF_ADAPTER,
          ClientSettings.EXPORT_TO_RTF_ADAPTER
      );
      ArrayList comps = opt.getComponentsExportOptions();
      comps.remove(0);

      Object[][] cells = new Object[colsTable.getRowCount()+rowsTable.getRowCount()][rowsTable.getColumnCount()+colsTable.getColumnCount()];
      for(int i=0;i<pars.getColumnFields().size();i++)
        cells[i][rowsTable.getColumnCount()-1] = ClientSettings.getInstance().getResources().getResource( ((ColumnField)pars.getColumnFields().get(i)).getDescription() );
      for(int i=0;i<colsTable.getRowCount();i++)
        for(int j=0;j<colsTable.getColumnCount();j++)
          cells[i][rowsTable.getColumnCount()+j] = colsTable.getValueAt(i,j);
      for(int i=0;i<dataTable.getRowCount();i++)
        for(int j=0;j<dataTable.getColumnCount();j++)
          cells[colsTable.getRowCount()+i][rowsTable.getColumnCount()+j] = dataTable.getValueAt(i,j);
      for(int i=0;i<rowsTable.getRowCount();i++)
        for(int j=0;j<rowsTable.getColumnCount();j++)
          cells[colsTable.getRowCount()+i][j] = rowsTable.getValueAt(i,j);
      for(int i=0;i<pars.getRowFields().size();i++)
        cells[colsTable.getRowCount()-1][i] = ClientSettings.getInstance().getResources().getResource( ((RowField)pars.getRowFields().get(i)).getDescription() );

      ComponentExportOptions comp = new ComponentExportOptions();
      comp.setCellsContent(cells);
      opt.addComponentExportOptions(comp);

      String fileName = System.getProperty("java.io.tmpdir").replace('\\','/');
      if (!fileName.endsWith("/"))
        fileName += "/";

      // export data grid directly in the client (Windows o.s. ONLY)...
      byte[] doc = null;

      // generate the Excel document...
      doc = new ExportToExcel().getDocument(opt);
      fileName += "doc"+System.currentTimeMillis()+".xls";

      FileOutputStream out = new FileOutputStream(fileName);
      out.write(doc);
      out.close();

      ClientUtils.displayURL("file://"+fileName);
    }
    catch (Throwable t) {
      Logger.error(this.getClass().getName(),"export","Error on exporting data from Pivot table:\n"+t.toString(),t);
    }
  }


  /**
   * Invoked when pressing filter button.
   */
  public final void filterSort() {
    Window parentComp = ClientUtils.getParentWindow(this);
    if (parentComp instanceof JFrame)
      new FilterDialog((JFrame)parentComp,this,pars);
    else
      new FilterDialog((JDialog)parentComp,this,pars);
  }


  public final HashSet getBindedButtons() {
    return bindedButtons;
  }


  /**
   * Set current enabled value of button.
   * @param button generic button that fires this event
   * @param currentValue current enabled value
   */
  public final void setCurrentValue(GenericButton button,boolean currentValue) {
    currentValueButtons.put(button,new Boolean(currentValue));
  }


  /**
   * @param button generic button
   */
  public final boolean getCurrentValue(GenericButton button) {
    Boolean currentValue = (Boolean)currentValueButtons.get(button);
    if (currentValue==null)
      return true;
    else
      return currentValue.booleanValue();
  }


  /**
   * Set the functionId identifier, associated to the container
   * @param functionId identifier associated to the container
   */
  public final void setFunctionId(String functionId) {
    this.functionId = functionId;
  }


  /**
   * @return identifier (functionId) associated to the container
   */
  public final String getFunctionId() {
    return functionId;
  }


  public final void importData() {
  }


  public final void insert() {
  }


  public final boolean isButtonDisabled(GenericButton button) {
    return false;
  }


  /**
   * Invoked when pressing reload button.
   */
  public final void reload() {
    compileDataInThread();
  }


  public final boolean save() {
    return false;
  }


  /**
   * @return column fields decleared for this pivot table; they are not applied, but are only showed in filter dialog
   */
  public final ArrayList getAllColumnFields() {
    return allColumnFields;
  }


  /**
   * @return data fields decleared for this pivot table; they are not applied, but are only showed in filter dialog
   */
  public final ArrayList getAllDataFields() {
    return allDataFields;
  }


  /**
   * @return row fields decleared for this pivot table; they are not applied, but are only showed in filter dialog
   */
  public final ArrayList getAllRowFields() {
    return allRowFields;
  }


  /**
   * @return flag used to autocompile Pivot table when showing it
   */
  public final boolean isAutoCompile() {
    return autoCompile;
  }


  /**
   * Set whether autocompile Pivot table when showing it.
   * @param autoCompile flag used to autocompile Pivot table when showing it
   */
  public final void setAutoCompile(boolean autoCompile) {
    this.autoCompile = autoCompile;
  }


  /**
   * @return (optional) renderer used to set background/foreground color for each data field cell and related font
   */
  public final DataFieldRenderer getDataFieldRenderer() {
    return dataFieldRenderer;
  }


  /**
   * Set the renderer used to set background/foreground color for each data field cell and related font.
   * @param dataFieldRenderer renderer used to set background/foreground color for each data field cell and related font
   */
  public final void setDataFieldRenderer(DataFieldRenderer dataFieldRenderer) {
    this.dataFieldRenderer = dataFieldRenderer;
  }








  /**
   * <p>Title: OpenSwing Framework</p>
   * <p>Description: Inner class used to define data fields content.</p>
   * <p>Copyright: Copyright (C) 2008 Mauro Carniel</p>
   * @version 1.0
   */
  class DataFieldsTableModel extends AbstractTableModel {


    ArrayList rows = new ArrayList();
    int colIndex;

    public DataFieldsTableModel() {
      processHNode(rows,pivotTableModel.getHRoot(),0);
      dataTable.setModel(this);
    }


    private void processHNode(ArrayList rows,RowGenericNode parentNode,int pos) {
      Object[] row = null;
      RowGenericNode n = null;
      int oldIndex = -1;
      for(int i=0;i<parentNode.getChildrenCount();i++) {
        n = (RowGenericNode)parentNode.getChildren(i);
        row = new Object[colsTable.getColumnCount()];
        colIndex = 0;
        processVNode(row,n.getVtreeNodes(),pivotTableModel.getVRoot(),new GenericNodeKey());

        rows.add(row);
        oldIndex = rows.size()-1;
        if (n.isNodeExpanded()) {
          n.setNodeExpanded(true);
          processHNode(rows,n,pos+1);
        }
      }
    }


    private void processVNode(Object[] row,HashMap dataValues,GlobalColGenericNode n,GenericNodeKey key) {
      GlobalColGenericNode vn = null;
      ColGenericNode obj = null;
      Number num = null;
      GenericNodeKey currentKey = null;
      Object obj1,obj2;
      for(int i=0;i<n.getChildrenCount();i++) {
        vn = (GlobalColGenericNode)n.getChildren(i);
        currentKey = key.appendKey(vn.getValue());
        obj = (ColGenericNode)dataValues.get(currentKey);

        colIndex = 0;
        for(int x=0;x<currentKey.getPath().length;x++)
          for (int y = colIndex; y < colsTable.getModel().getColumnCount(); y++) {
            obj1 = currentKey.getPath()[x];
            obj2 = colsTable.getModel().getValueAt(x,y);
            if (obj1!=null && obj2!=null) {
              if (obj1 instanceof Number && obj2 instanceof Number &&
                  ((Number)obj1).doubleValue()==((Number)obj2).doubleValue() ||
                  obj1.equals(obj2)) {
                colIndex = y;
                break;
              }
            }
          }

        if (obj!=null) {
          for(int k=0;k<pars.getDataFields().size();k++) {
            num = obj.getGenericFunctions()[k].getValue();
            if (num==null)
              row[colIndex++] = num;
            else if (num.longValue()==num.doubleValue())
              row[colIndex++] = new Long(num.longValue());
            else
              row[colIndex++] = num;
          }
        }

        if (vn.isNodeExpanded()) {
          vn.setNodeExpanded(true);
          processVNode(row,dataValues,vn,key.appendKey(vn.getValue()));
        }
      }
    }


    public int getColumnCount() {
      if (rows.size()>0)
        return ((Object[])rows.get(0)).length;
      else
        return 0;
    }


    public int getRowCount() {
      return rows.size();
    }


    public Object getValueAt(int rowIndex, int columnIndex) {
      return ((Object[])rows.get(rowIndex))[columnIndex];
    }


  } // end inner class





  /**
   * <p>Title: OpenSwing Framework</p>
   * <p>Description: TableCellRenderer used for column fields.</p>
   * <p>Copyright: Copyright (C) 2008 Mauro Carniel</p>
   * @version 1.0
   */
  class ColsHeaderRenderer extends DefaultTableCellRenderer {

    JPanel b = new JPanel() {

      public void paint(Graphics g) {
        super.paint(g);
        if (t!=null && col!=-1 && row!=-1) {
          GlobalColGenericNode n = (GlobalColGenericNode)currentExpandableColFields.get(new Integer(col));
          if (n!=null &&
              row==n.getLevel()-1 &&
              row<pars.getColumnFields().size()-1) {
            if (!n.isNodeExpanded()) {
              g.drawLine(7,11,11,11);
              g.drawLine(9,11-2,9,11+2);
            }
            else {
              g.drawLine(7,11,11,11);
            }
            g.setColor(Color.lightGray);
            g.drawRect(5,11-4,8,8);
          }
        }
      }

    };
    JLabel l = new JLabel();
    JTable t = null;
    int col = -1;
    int row = -1;

    public ColsHeaderRenderer() {
      b.setBackground(new JButton().getBackground());
      b.setBorder(BorderFactory.createRaisedBevelBorder());
      b.setSize(b.getWidth(),ClientSettings.HEADER_HEIGHT);
      b.setLayout(new GridBagLayout());
      b.add(l,    new GridBagConstraints(0, 1, 1, 1, 1.0, 1.0
              ,GridBagConstraints.NORTHWEST, GridBagConstraints.CENTER, new Insets(2, 16, 0, 0), 0, 0));
    }

    public Component getTableCellRendererComponent(JTable table, Object value,
                      boolean isSelected, boolean hasFocus, int row, int column) {
      this.t = table;
      this.col = column;
      this.row = row;
      l.setText(value==null?"":value.toString());
      return b;
    }

  } // end inner class




  /**
   * <p>Title: OpenSwing Framework</p>
   * <p>Description: TableCellRenderer used for row fields.</p>
   * <p>Copyright: Copyright (C) 2008 Mauro Carniel</p>
   * @version 1.0
   */
  class RowsHeaderRenderer extends DefaultTableCellRenderer {

      JPanel b = new JPanel() {

        public void paint(Graphics g) {
          super.paint(g);
          if (t!=null && col!=-1 && row!=-1) {
            RowGenericNode n = (RowGenericNode)currentExpandableRowFields.get(new Integer(row));
            if (n!=null &&
                col==n.getLevel()-1 &&
                col<pars.getRowFields().size()-1) {
              if (!n.isNodeExpanded()) {
                g.drawLine(7,9,11,9);
                g.drawLine(9,9-2,9,9+2);
              }
              else {
                g.drawLine(7,9,11,9);
              }
              g.setColor(Color.lightGray);
              g.drawRect(5,9-4,8,8);
            }
          }
        }

      };
      JLabel l = new JLabel();
      JTable t = null;
      int col = -1;
      int row = -1;


      public RowsHeaderRenderer() {
        b.setBackground(new JButton().getBackground());
        b.setBorder(BorderFactory.createRaisedBevelBorder());
        b.setSize(b.getWidth(),ClientSettings.HEADER_HEIGHT);
        b.setLayout(new GridBagLayout());
        b.add(l,    new GridBagConstraints(0, 1, 1, 1, 1.0, 1.0
                ,GridBagConstraints.NORTHWEST, GridBagConstraints.NONE, new Insets(0, 16, 0, 0), 0, 0));
      }

      public Component getTableCellRendererComponent(JTable table, Object value,
                        boolean isSelected, boolean hasFocus, int row, int column) {
        this.t = table;
        this.col = column;
        this.row = row;
        l.setText(value==null?"":value.toString());
        return b;
      }


  } // end inner class




  /**
   * <p>Title: OpenSwing Framework</p>
   * <p>Description: TableCellRenderer used for row fields.</p>
   * <p>Copyright: Copyright (C) 2008 Mauro Carniel</p>
   * @version 1.0
   */
  class DataFieldsRenderer extends DefaultTableCellRenderer {

    private boolean hasFocus = false;
    private JLabel l = new JLabel();
    private Font defaultFont = l.getFont();
    private JPanel p = new JPanel() {

      public final void paint(Graphics g) {
        super.paint(g);
        if (hasFocus) {
          g.drawRect(0,0,getWidth()-1,getHeight()-1);
        }
      }

    };

    private DataFieldsRenderer() {
      l.setHorizontalAlignment(SwingConstants.RIGHT);
      p.setLayout(new FlowLayout(FlowLayout.RIGHT,0,0));
      p.add(l,null);
      p.setBackground(ClientSettings.GRID_CELL_BACKGROUND);
      l.setForeground(ClientSettings.GRID_CELL_FOREGROUND);
    }

    public Component getTableCellRendererComponent(JTable table, Object value,
                            boolean isSelected, boolean hasFocus, int row, int column) {
      this.hasFocus = hasFocus;
      if (value==null)
        l.setText("");
      else {
        DataField df = (DataField)pars.getDataFields().get(column%pars.getDataFields().size());
        if (df.getFormatter()==null)
          l.setText(value.toString());
        else
          l.setText( df.getFormatter().format(((Number)value).doubleValue()) );
        if (dataFieldRenderer!=null) {
          GenericNodeKey rowPath = new GenericNodeKey();
          GenericNodeKey colPath = new GenericNodeKey();

          int kk = rowsTable.getColumnCount()-1;
          while(kk>=0 && rowsTable.getValueAt(row,kk)==null)
            kk--;
          if (kk>=0)
            rowPath = rowPath.appendKey(rowsTable.getValueAt(row,kk));

          int  k = row;
          for(int i=kk-1;i>=0;i--) {
            while(k>=0 && rowsTable.getValueAt(k,i)==null)
              k--;
            if (k>=0)
              rowPath = rowPath.appendKey(rowsTable.getValueAt(k,i));
            else
              k = 0;
          }

          colPath = colPath.appendKey(colsTable.getValueAt(colsTable.getRowCount()-1,column));
          kk = column;
          while(kk%pars.getDataFields().size()>0 && colsTable.getValueAt(colsTable.getRowCount()-2,kk)==null)
            kk--;
          if (colsTable.getValueAt(colsTable.getRowCount()-2,kk)!=null)
            colPath = colPath.appendKey(colsTable.getValueAt(colsTable.getRowCount()-2,kk));

          k = colsTable.getRowCount()-3;
          while(k>=0) {
            while(kk>=0 && colsTable.getValueAt(k,kk)==null)
              kk--;
            if (kk>=0)
              colPath = colPath.appendKey(colsTable.getValueAt(k,kk));
            k--;
          }


//          k = colsTable.getRowCount()-2;
//          kk = column;
//          while(k>=0) {
//            while(kk%pars.getDataFields().size()>=0 && colsTable.getValueAt(k,kk)==null)
//              kk--;
//            if (kk%pars.getDataFields().size()>=0)
//              colPath = colPath.appendKey(colsTable.getValueAt(k,kk));
//            k--;
//          }


          p.setBackground(
            dataFieldRenderer.getBackgroundColor(
              ClientSettings.GRID_CELL_BACKGROUND,
              rowPath,
              colPath,
              value,
              row,
              column
            )
          );
          p.setForeground(
            dataFieldRenderer.getForegroundColor(
              ClientSettings.GRID_CELL_FOREGROUND,
              rowPath,
              colPath,
              value,
              row,
              column
            )
          );
          l.setFont(
            dataFieldRenderer.getFont(
              defaultFont,
              rowPath,
              colPath,
              value,
              row,
              column
            )
          );
        }
      }
      return p;
    }

  } // end inner class


  /**
   * Expand all rows fields, starting from the specified row index.
   * @param row row index to expand
   */
  public final void expandRow(int row) {
      int r = 0;
      if (row>0) {
        int oldr = 0;
        for(int i=0;i<row;i++)
          if (rowsTable.rowAtPoint(new Point(5,i*dataTable.getRowHeight()))!=oldr) {
            oldr = rowsTable.rowAtPoint(new Point(5, i * dataTable.getRowHeight()));
            r++;
          }
      }
      setExpansionStateNode(pivotTableModel.getHRoot().getChildren(r),true,r);
      buildPivotTable();
  }


  /**
   * Collapse all rows fields, starting from the specified row index.
   * @param row row index to collapse
   */
  public final void collapseRow(int row) {
    int r = 0;
    if (row>0) {
      int oldr = 0;
      for(int i=0;i<row;i++)
        if (rowsTable.rowAtPoint(new Point(5,i*dataTable.getRowHeight()))!=oldr) {
          oldr = rowsTable.rowAtPoint(new Point(5, i * dataTable.getRowHeight()));
          r++;
        }
    }
    setExpansionStateNode(pivotTableModel.getHRoot().getChildren(r),false,r);
    buildPivotTable();
  }


  /**
   * Expand all column fields, starting from the specified column index.
   * @param column column index to expand
   */
  public final void expandColumn(int column) {
    int c = 0;
    if (column>0) {
      int oldc = 0;
      int w = 0;
      for(int i=0;i<column;i++) {
       if (colsTable.columnAtPoint(new Point(w, 5)) != oldc) {
          oldc = colsTable.columnAtPoint(new Point(w, 5));
          c++;
       }
       w += dataTable.getColumnModel().getColumn(i).getWidth();
      }
      if (c>=pivotTableModel.getVRoot().getChildrenCount())
        c = pivotTableModel.getVRoot().getChildrenCount()-1;
    }
    setExpansionStateNode(pivotTableModel.getVRoot().getChildren(c),true,c);
    buildPivotTable();
  }


  /**
   * Collapse all column fields, starting from the specified column index.
   * @param column column index to collapse
   */
  public final void collapseColumn(int column) {
    int c = 0;
    if (column>0) {
      int oldc = 0;
      int w = 0;
      for(int i=0;i<column;i++) {
        if (colsTable.columnAtPoint(new Point(w, 5)) != oldc) {
          oldc = colsTable.columnAtPoint(new Point( w, 5));
          c++;
        }
        w += dataTable.getColumnModel().getColumn(i).getWidth();
      }
      if (c>=pivotTableModel.getVRoot().getChildrenCount())
        c = pivotTableModel.getVRoot().getChildrenCount()-1;
    }
    setExpansionStateNode(pivotTableModel.getVRoot().getChildren(c),false,c);
    buildPivotTable();
  }


  /**
   * @return current selected row in data table
   */
  public final int getSelectedRow() {
    return dataTable.getSelectedRow();
  }


  /**
   * @return current selected column in data table
   */
  public final int getSelectedColumn() {
    return dataTable.getSelectedColumn();
  }


}
