package org.openswing.swing.table.client;


import java.io.*;
import java.text.*;
import java.util.*;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.filechooser.FileFilter;

import org.openswing.swing.client.*;
import org.openswing.swing.export.client.*;
import org.openswing.swing.export.java.*;
import org.openswing.swing.importdata.client.*;
import org.openswing.swing.importdata.java.*;
import org.openswing.swing.logger.client.*;
import org.openswing.swing.message.receive.java.*;
import org.openswing.swing.message.send.java.*;
import org.openswing.swing.table.columns.client.*;
import org.openswing.swing.table.filter.client.*;
import org.openswing.swing.table.java.*;
import org.openswing.swing.table.model.client.*;
import org.openswing.swing.util.client.*;
import org.openswing.swing.util.java.*;


/**
 * <p>Title: OpenSwing Framework</p>
 * <p>Description: Panel that contains the Grid object and a second Grid object, anchored to the left side of the panel (optional).
 * This class has not  be used directly by the programmer: it's called by GridControl.</p>
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
public class Grids extends JPanel implements VOListTableModelListener,DataController,NavigatorBarController {

  private GridBagLayout gridBagLayout1 = new GridBagLayout();

  /** locked grid, anchored to the left (optional) */
  private Grid lockedGrid = null;

  /** define where to anchor locked columns: to the left or to the right of the grid; default value: <code>true</code> i.e. to the left */
  private boolean anchorLockedColumnsToLeft = true;

  /** main grid */
  private Grid grid = null;

  /** number of locked columns (anchored to the left); default value = 0 */
  private int lockedColumns;

  /** TableModel linked to the grid */
  private VOListTableModel model = null;

  /** grid controller, used to listen grid events */
  private GridController gridController = null;

  /** button linked to the grid, used to set INSERT mode (insert new row on grid) */
  private InsertButton insertButton = null;

  /** button linked to the grid, used to export data */
  private ExportButton exportButton = null;

  /** button linked to the grid, used to import data */
  private ImportButton importButton = null;

  /** button linked to the grid, used to set INSERT mode (copy the previous row on grid) */
  private CopyButton copyButton = null;

  /** button linked to the grid, used to open the filtering/sorting dialog (only in READONLY mode) */
  private FilterButton filterButton = null;

  /** button linked to the grid, used to set EDIT mode (edit the selected row on grid) */
  private EditButton editButton = null;

  /** button linked to the grid, used to set READONLY mode and to reload the TableModel */
  private ReloadButton reloadButton = null;

  /** button linked to the grid, used to delete the selected rows */
  private DeleteButton deleteButton = null;

  /** button linked to the grid, used to commit the modified rows (on INSERT/EDIT mode) */
  private SaveButton saveButton = null;

  /** TableModel adapter, used to link ValueObjects to TableModel */
  private VOListAdapter modelAdapter = null;

  /** navigation bar (optional) */
  private NavigatorBar navBar = null;

  /** other grid parameters */
  private Map otherGridParams = new HashMap();

  /** grid data locator; can be set to "ClientGridDataLocator" or to "ServerGridDataLocator" */
  private GridDataLocator gridDataLocator = null;

  /** grid graphics control */
  private GridControl gridControl = null;

  /** identifier (functionId) associated to the container */
  private String functionId = null;

  /** flag used inside addNotify to set column headers and the toolbar state */
  private boolean firstTime = true;

  /** quick filter accessed by right mouse click on grid */
  private QuickFilterPanel filterPanel = null;

  /** popup menu accessed by right mouse click on grid */
  private GridPopup popup = new GridPopup();

  /** menu item for removing column filtering */
  private JMenuItem removefilterItem = new JMenuItem(
      ClientSettings.getInstance().getResources().getResource("Remove Filter"),
      new ImageIcon(ClientUtils.getImage("filter-undo.gif")));

  /** hashtable which contains the associations: attribute name, new FilterWhereClause[2] {FilterWhereClause,FilterWhereClause}) */
  private Map quickFilterValues = new HashMap();

  /** list of custom commands added to the popup menu accessed by right mouse click on grid */
  private ArrayList popupCommands = new ArrayList();

  /** last row index already loaded on grid (related to result set); -1 = no rows yet loaded */
  private int lastIndex = -1;

  /** first row index to load on grid (related to result set); -1 = no rows yet loaded */
  private int startIndex = 0;

  /** there are other rows in result set not yet loaded */
  private boolean moreRows;

  /** generic buttons */
  private ArrayList genericButtons = new ArrayList();

  /** current sorted columns */
  private ArrayList currentSortedColumns = new ArrayList();

  /** current sorted columns versus (Ascending/Descending) */
  private ArrayList currentSortedVersusColumns = new ArrayList();

  /** flag used to enable listening of vertical scrolling events */
  private boolean listenEvent = true;

  private GridStatusPanel statusPanel = null;

  /** TableModel column properties (name, type, etc.) */
  private Column[] colProps = null;

  /** collection of GenericButton objects to disable when the specified attribute will be setted to the specified value; pairs of type (GenericButton object, List of GenericButtonController objects) */
  private Hashtable buttonsToDisable = new Hashtable();

  /** maximum number of rows to insert by pressing "down" key; default value: 1 */
  private int maxNumberOfRowsOnInsert = 1;

  /** flag used to allow insert row (using DOWN key) in edit mode too; default value: <code>false</code> */
  private boolean allowInsertInEdit = false;

  /** current number of new rows  */
  private int currentNumberOfNewRows = 0;

  /** filter dialog */
  private FilterDialog filterDialog = null;

  /** type of grid; possible values: Grid.MAIN_GRID, Grid.TOP_GRID, Grid.BOTTOM_GRID */
  private int gridType;

  /** locked + std grids scrollpane */
  private JScrollPane scroll = null;

  /** maximum number of sorted columns */
  private int maxSortedColumns = 1;

  /** list of ActionListener objetcs related to the event "loading data completed" */
  private ArrayList loadDataCompletedListeners = new ArrayList();

  /** current expanded rows */
  private ArrayList expandedRows = new ArrayList();

  /** cache that contains inner components per row */
  private Hashtable cache = new Hashtable();

  /** current nested component that is expanded and has focus */
  private Component currentNestedComponent = null;

  /** current nested component row index */
  private int currentNestedComponentRow = -1;

  /** define where new rows must be added: <code>true</code> at the top of the grid or <code>false</code> at the bottom; default value: <code>true</code> */
  private boolean insertRowsOnTop = true;

  /** flag used to force the editing of one row only: the current selected row; default value: <code>false</code>, i.e. all rows are editable */
  private boolean editOnSingleRow = false;

  /** current selected row when grid is switched to EDIT mod and "editOnSingleRow" property is set to <code>true</code> */
  private int currentEditingRow = -1;

  /** selected row selected before reloading data on grid; used to reset selected row */
  private int selectedRowBeforeReloading = -1;

  /** last record number in result set; -1 as default value */
  private int totalResultSetLength = -1;

  /** block size; -1 if loading is not based on one page per time */
  private int blockSize = -1;

  /** current enabled button state, before GenericButton.setEnabled method calling */
  private HashMap currentValueButtons = new HashMap();

  /** collection of buttons binded to grid (InsertButton, EditButton, etc) */
  private HashSet bindedButtons = new HashSet();

//  /** collection of old abilitation state for GenericButton objects binded to grid: <GenericButton, Boolean> */
//  private HashMap oldGenericButtonsState = new HashMap();


  /**
   * Costructor called by GridControl: programmer never called directly this class.
   * @param valueObjectClassName ValueObject class name
   * @param colProps TableModel column properties (name, type, etc.)
   * @param controller grid controller, used to listen grid events
   * @param statusPanel bottom panel included into the grid; used to view selected row numbers
   * @param colorsInReadOnlyMode flag used to define if background and foreground colors must be setted according to GridController definition only in READONLY mode
   * @param popupCommands list of custom commands added to the popup menu accessed by right mouse click on grid
   * @param anchorLastColumn define if last column must be anchored to the right margin of the grid
   * @param gridType type of grid; possible values: Grid.MAIN_GRID, Grid.TOP_GRID, Grid.BOTTOM_GRID
   */
  public Grids(
      GridControl gridControl,
      int lockedColumns,
      boolean anchorLockedColumnsToLeft,
      String valueObjectClassName,
      Column[] colProps,
      GridController gridController,
      GridStatusPanel statusPanel,
      GridDataLocator gridDataLocator,
      Map otherGridParams,
      boolean colorsInReadOnlyMode,
      ArrayList popupCommands,
      boolean anchorLastColumn,
      int expandableColumn,
      boolean singleExpandableRow,
      boolean overwriteRowWhenExpanding,
      ExpandableRowController expandableRowController,
      HashMap comboFilters,
      int headerHeight,
      boolean searchAdditionalRows,
      boolean allowColumnsSortingInEdit,
      int gridType
  ) {
    this.gridControl = gridControl;
    this.lockedColumns = lockedColumns;
    this.anchorLockedColumnsToLeft = anchorLockedColumnsToLeft;
    this.colProps = colProps;
    this.gridController = gridController;
    this.statusPanel = statusPanel;
    this.gridDataLocator = gridDataLocator;
    this.otherGridParams = otherGridParams;
    this.popupCommands = popupCommands;
    this.gridType = gridType;

    try {
      for(int i=0;i<colProps.length;i++)
        if (colProps[i].isAutoFitColumn())
          colProps[i].setPreferredWidth(Toolkit.getDefaultToolkit().getFontMetrics(ClientSettings.HEADER_FONT==null?new JLabel().getFont():ClientSettings.HEADER_FONT).stringWidth(ClientSettings.getInstance().getResources().getResource(colProps[i].getHeaderColumnName()))+10);


      // construction of data model adapter linked to the grid...
      modelAdapter = new VOListAdapter(Class.forName(valueObjectClassName),gridController,colProps,this);
      model = new VOListTableModel(modelAdapter,this);
    }
    catch (ClassNotFoundException ex) {
      Logger.error(this.getClass().getName(),"Grids","Error while constructing grids: value object '"+valueObjectClassName+"' doesn't exist.",ex);
    }
    catch (Throwable t) {
      Logger.error(this.getClass().getName(),"Grids","Error while constructing ",t);
    }

    this.grid = new Grid(
        this,
        colProps,
        statusPanel,
        anchorLockedColumnsToLeft?lockedColumns:0,
        anchorLockedColumnsToLeft?colProps.length:colProps.length-lockedColumns,
        colorsInReadOnlyMode,
        model,
        modelAdapter,
        gridController,
        false,
        anchorLastColumn,
        expandableColumn,
        singleExpandableRow,
        overwriteRowWhenExpanding,
        expandableRowController,
        comboFilters,
        headerHeight,
        searchAdditionalRows,
        allowColumnsSortingInEdit,
        gridType
    );

    if (lockedColumns>0) {
      if (anchorLockedColumnsToLeft)
        for(int i=0;i<lockedColumns;i++)
          colProps[i].setColumnSelectable(false);
      else
        for(int i=colProps.length-lockedColumns;i<colProps.length;i++)
          colProps[i].setColumnSelectable(false);
      this.lockedGrid = new Grid(
          this,
          colProps,
          statusPanel,
          anchorLockedColumnsToLeft?0:colProps.length-lockedColumns,
          anchorLockedColumnsToLeft?lockedColumns:colProps.length,
          colorsInReadOnlyMode,
          model,
          modelAdapter,
          gridController,
          true,
          anchorLastColumn,
          expandableColumn,
          singleExpandableRow,
          overwriteRowWhenExpanding,
          expandableRowController,
          comboFilters,
          headerHeight,
          searchAdditionalRows,
          allowColumnsSortingInEdit,
          gridType
      );
      this.lockedGrid.setReorderingAllowed(false);
      this.lockedGrid.setResizingAllowed(false);

      grid.addFocusListener(new FocusAdapter() {
        public void focusGained(FocusEvent e) {
          lockedGrid.stopCellEditing();
        }
      });

      lockedGrid.addFocusListener(new FocusAdapter() {
        public void focusGained(FocusEvent e) {
          grid.stopCellEditing();
        }
      });

    }
    try {
      jbInit();
      setupScrollBars();
    }
    catch(Exception e) {
      e.printStackTrace();
    }


    setNavBar(new NavigatorBar());

    removefilterItem.setAccelerator(ClientSettings.REMOVE_FILTER_KEY);

  }


  /**
   * Setup vertical/horizontal scrollbars, according to grid type.
   */
  private void setupScrollBars() {
    if (gridControl!=null && gridControl.getLockedRowsOnTop()>0 &&
        gridControl.getLockedRowsOnBottom()==0 &&
        gridType==Grid.TOP_GRID)
      // no bottom grid defined and this is the top grid...
      scroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
    else if (gridControl!=null && gridControl.getLockedRowsOnTop()==0 &&
        gridControl.getLockedRowsOnBottom()>0 &&
        gridType==Grid.MAIN_GRID)
      // no top grid defined and bottom grid defined and this is the main grid...
      scroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
    else if (gridControl!=null && gridControl.getLockedRowsOnTop()>0 &&
             gridControl.getLockedRowsOnBottom()>0 &&
             gridType!=Grid.BOTTOM_GRID) {
      // top and bottom grids are defined and this is not the bottom grid...
      scroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
    }

    if (gridControl!=null && gridControl.getLockedRowsOnTop()>0 && gridType==Grid.TOP_GRID) {
      scroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
      scroll.setVerticalScrollBar(new JScrollBar() {
        public void paint(Graphics g) { } // no vertical scrollbar is visible...
      });
    }
    if (gridControl!=null && gridControl.getLockedRowsOnBottom()>0 && gridType==Grid.BOTTOM_GRID) {
      scroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
      scroll.setVerticalScrollBar(new JScrollBar() {
        public void paint(Graphics g) { } // no vertical scrollbar is visible...
      });
    }

//    if (gridControl.getLockedRowsOnTop()>0 || gridControl.getLockedRowsOnBottom()>0)
      scroll.setBorder(BorderFactory.createEmptyBorder());

    // top or bottom grid is defined and this is not the std grid...
    if (gridControl!=null && (gridControl.getLockedRowsOnTop()>0 || gridControl.getLockedRowsOnBottom()>0) && gridType!=Grid.MAIN_GRID){
      // add a horizontal scrollback listener to this horizontal scrollbar...
      scroll.getHorizontalScrollBar().addAdjustmentListener(
        new AdjustmentListener(){
         public void adjustmentValueChanged(AdjustmentEvent e){
           if (gridControl.getTable().getScroll().getHorizontalScrollBar().getValue() != e.getValue())
             gridControl.getTable().getScroll().getHorizontalScrollBar().setValue(e.getValue());
         };
      });
    }

    // top or bottom grid is defined and this is the std grid...
    if (gridControl!=null && (gridControl.getLockedRowsOnTop()>0 || gridControl.getLockedRowsOnBottom()>0) && gridType==Grid.MAIN_GRID){
      // add a horizontal scrollback listener to the horizontal scrollbar of the std grid...
      scroll.getHorizontalScrollBar().addAdjustmentListener(
        new AdjustmentListener(){
         public void adjustmentValueChanged(AdjustmentEvent e){
           if (getGridControl()!=null && getGridControl().getTopTable()!=null && getGridControl().getTopTable().getScroll().getHorizontalScrollBar().getValue() != e.getValue())
             getGridControl().getTopTable().getScroll().getHorizontalScrollBar().setValue(e.getValue());

           if (getGridControl()!=null && getGridControl().getBottomTable()!=null && getGridControl().getBottomTable().getScroll().getHorizontalScrollBar().getValue() != e.getValue())
             getGridControl().getBottomTable().getScroll().getHorizontalScrollBar().setValue(e.getValue());

         };
      });
    }


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
    if (currentValue==null) {
      if (button instanceof InsertButton)
        return getMode()==Consts.READONLY;
      else if (button instanceof EditButton)
        return getMode()==Consts.READONLY;
      else if (button instanceof CopyButton)
        return getMode()==Consts.READONLY;
      else if (button instanceof SaveButton)
        return getMode()!=Consts.READONLY;
      return true;
    }
    else
      return currentValue.booleanValue();
  }


  private void jbInit() throws Exception {
    this.setLayout(gridBagLayout1);

    scroll = new JScrollPane(grid);

    if (lockedColumns>0) {
      lockedGrid.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
      lockedGrid.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
//      final JViewport viewport = new JViewport();
//      viewport.setView(lockedGrid);
//      viewport.setPreferredSize(lockedGrid.getPreferredSize());
//      if (anchorLockedColumnsToLeft)
//        scroll.setRowHeaderView(viewport);

      final JScrollPane lockedscroll = new JScrollPane(lockedGrid);
      lockedscroll.setBorder(BorderFactory.createEmptyBorder());
      lockedscroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
      SwingUtilities.invokeLater(new Runnable() {
        public void run() {
          lockedscroll.getVerticalScrollBar().hide();
          scroll.getVerticalScrollBar().addAdjustmentListener(new AdjustmentListener() {
            public void adjustmentValueChanged(AdjustmentEvent e) {
              listenEvent = false;
              lockedscroll.getVerticalScrollBar().setValue(scroll.getVerticalScrollBar().getValue());
              listenEvent = true;
            }
          });
          lockedscroll.getVerticalScrollBar().addAdjustmentListener(new AdjustmentListener() {
            public void adjustmentValueChanged(AdjustmentEvent e) {
              if (listenEvent)
                scroll.getVerticalScrollBar().setValue(lockedscroll.getVerticalScrollBar().getValue());
            }
          });
          lockedscroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_NEVER);
        }
      });


      if (lockedGrid.getTableHeader()!=null) {
        JPanel headerPanel = new JPanel();
        headerPanel.setLayout(new GridLayout(lockedGrid.hasColSpan()?2:1,1));

        if (lockedGrid.hasColSpan() &&
           (gridType==Grid.MAIN_GRID && gridControl==null ||
            gridType==Grid.MAIN_GRID && gridControl!=null && gridControl.getTopTable()==null ||
            gridType==Grid.TOP_GRID)) {
          // add additional header columns...
          JPanel additionalColHeaders = new JPanel();
          additionalColHeaders.setLayout(new FlowLayout(FlowLayout.LEFT,0,0));
          int span = 0;
          int w = 0;
          int rest = 0;
          JLabel h = null;
          JPanel hp = null;
          Color backcolor = new JButton().getBackground();
          for(int i=0;i<colProps.length;i++) {
            if (colProps[i].getAdditionalHeaderColumnSpan()>0) {
              span = colProps[i].getAdditionalHeaderColumnSpan();
              hp = new JPanel();
              if (colProps[i].getHeaderTextHorizontalAlignment()==SwingConstants.CENTER)
                hp.setLayout(new FlowLayout(FlowLayout.CENTER,0,0));
              else if (colProps[i].getHeaderTextHorizontalAlignment()==SwingConstants.LEFT)
                hp.setLayout(new FlowLayout(FlowLayout.LEFT,0,0));
              else if (colProps[i].getHeaderTextHorizontalAlignment()==SwingConstants.RIGHT)
                hp.setLayout(new FlowLayout(FlowLayout.RIGHT,0,0));
              hp.setBorder(BorderFactory.createRaisedBevelBorder());
              hp.setBackground(backcolor);
              h = new JLabel();
              if (colProps[i].getHeaderFont()!=null)
                h.setFont(colProps[i].getHeaderFont());
              if (colProps[i].getHeaderForegroundColor()!=null)
                h.setForeground(colProps[i].getHeaderForegroundColor());

              hp.add(h,null);
              h.setText(ClientSettings.getInstance().getResources().getResource(colProps[i].getAdditionalHeaderColumnName()));
              rest = span-1;
              w = colProps[i].getPreferredWidth();
              ArrayList list = new ArrayList();
              list.add(new Integer(i));
            }
            else {
              w += colProps[i].getPreferredWidth();
              rest--;
            }
            if (rest==0 && h!=null) {
              hp.setPreferredSize(new Dimension(w,ClientSettings.HEADER_HEIGHT));
              if (i<lockedColumns) {
                additionalColHeaders.add(hp,null);
              }
            }
          }
          headerPanel.add(additionalColHeaders);
        } // end section on additional header columns...


        headerPanel.add(lockedGrid.getTableHeader());
//        if (anchorLockedColumnsToLeft) {
//          scroll.setCorner(JScrollPane.UPPER_LEFT_CORNER, headerPanel);

//          scroll.getVerticalScrollBar().addAdjustmentListener(new AdjustmentListener() {
//            public void adjustmentValueChanged(AdjustmentEvent e) {
//              listenEvent = false;
//              ((JScrollPane)viewport.getParent().getParent()).getVerticalScrollBar().setValue(scroll.getVerticalScrollBar().getValue());
//              listenEvent = true;
//            }
//          });
//          ((JScrollPane)viewport.getParent().getParent()).getVerticalScrollBar().addAdjustmentListener(new AdjustmentListener() {
//            public void adjustmentValueChanged(AdjustmentEvent e) {
//              if (listenEvent)
//                scroll.getVerticalScrollBar().setValue(((JScrollPane)viewport.getParent().getParent()).getVerticalScrollBar().getValue());
//            }
//          });

//        }
//        else {

//        this.add(headerPanel,  new GridBagConstraints(anchorLockedColumnsToLeft?0:2, 0, 1, 1, 0.0, 0.0
//                ,GridBagConstraints.NORTHWEST, GridBagConstraints.VERTICAL, new Insets(1, 0, scroll.getHorizontalScrollBar().getPreferredSize().height, 0), lockedGrid.getPreferredSize().width, 0));
        this.add(lockedscroll,  new GridBagConstraints(anchorLockedColumnsToLeft?0:2, 1, 1, 1, 0.0, 1.0
                ,GridBagConstraints.NORTHWEST, GridBagConstraints.VERTICAL, new Insets(0, 0, scroll.getHorizontalScrollBar().getPreferredSize().height, 0), lockedGrid.getPreferredSize().width-4, 0));

      }
      else
        this.add(lockedscroll,  new GridBagConstraints(anchorLockedColumnsToLeft?0:2, 1, 1, 1, 0.0, 1.0
            ,GridBagConstraints.NORTHWEST, GridBagConstraints.VERTICAL, new Insets(0, 0, scroll.getHorizontalScrollBar().getPreferredSize().height, 0), lockedGrid.getPreferredSize().width-4, 0));
    } // end if on lockedColumns>0

    this.add(scroll,   new GridBagConstraints(1, 1, 1, 1, 1.0, 1.0
            ,GridBagConstraints.NORTHWEST, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));

  }


  /**
   * Define if column reordering is allowed.
   * @param reorderingAllowed allows column reordering
   */
  public final void setReorderingAllowed(boolean reorderingAllowed) {
    grid.setReorderingAllowed(reorderingAllowed);
  }


  /**
   * Define if column resizing is allowed.
   * @param resizingAllowed allows column resizing
   */
  public final void setResizingAllowed(boolean resizingAllowed) {
    grid.setResizingAllowed(resizingAllowed);
  }


  /**
   * Set grid mode.
   * @param mode grid mode; possibile values: READONLY, INSERT, EDIT
   */
  public final void setMode(int mode) {
    model.setMode(mode);
  }


  /**
   * Set grid selection mode.
   * @param selectionMode grid selection mode
   */
  public final void setSelectionMode(int selectionMode) {
    if (lockedColumns>0)
      // multiple selection mode not allowed when there are locked columns...
      return;
    grid.setSelectionMode(selectionMode);
    if (lockedGrid!=null)
      lockedGrid.setSelectionMode(selectionMode);
  }


  /**
   * Remove cell editing, if needed.
   */
  public final void transferFocus() {
    try {
      if (grid.getCellEditor() != null) {
        grid.getCellEditor().stopCellEditing();
      }
      if (lockedGrid!=null && lockedGrid.getCellEditor()!=null)
        lockedGrid.getCellEditor().stopCellEditing();
    }
    catch (Exception ex) {
    }
    super.transferFocus();
  }


  /**
   * Clear grid content.
   */
  public final void clearData() {
    // crear table model...
    model.clear();
    while(expandedRows.size()>0)
      collapseRow(((Integer)expandedRows.get(0)).intValue());
    cache.clear();
    this.revalidate();
    grid.revalidate();
    grid.repaint();
    if (lockedGrid!=null) {
      lockedGrid.revalidate();
      lockedGrid.repaint();
    }
    moreRows = false;
    startIndex = -1;
    lastIndex = -1;
  }


  /**
   * Method called from reload method: reload current data block.
   * Data Loading is executed in a separated thread.
   * After data loading, the old selected row is selected again.
   * Method called from reload method:
   * reload current data block. Data Loading is executed in a separated thread.
   */
  public final void reloadDataFromStart() {
    if (model.getRowCount()>0) {
      setRowSelectionInterval(0,0);
      startIndex = 0;
    }
    else
      startIndex = -1;

    reload();
  }


  /**
   * Method called from reload method: reload current data block.
   * Data Loading is executed in a separated thread.
   * After data loading, the old selected row is selected again.
   */
  private final void reloadData() {
    if (!listenEvent)
      return;
    listenEvent = false;
    new LoadDataThread().start();
  }


  /**
   * Set the functionId identifier, associated to the container
   * @param functionId identifier associated to the container
   */
  public final void setFunctionId(String functionId) {
    this.functionId = functionId;
  }


  /**
   * Set navigation bar.
   * @param navBar navigation bar
   */
  public final void setNavBar(NavigatorBar navBar) {
    if (navBar!=null) {
      this.navBar = navBar;
      navBar.initNavigator(this);
      bindedButtons.add(navBar);
    }
  }


  /**
   * @return other grid parameters
   */
  public final Map getOtherGridParams() {
    return otherGridParams;
  }


  /**
   * Set other grid parameters.
   * @param otherGridParams other grid parameters
   */
  public final void setOtherGridParams(Map otherGridParams) {
    this.otherGridParams = otherGridParams;
  }


  /**
   * Set maximum number of sorted columns.
   * @param maxSortedColumns maximum number of sorted columns
   */
  public final void setMaxSortedColumns(int maxSortedColumns) {
    this.maxSortedColumns = maxSortedColumns;
  }


  /**
   * @return maximum number of sorted columns
   */
  public final int getMaxSortedColumns() {
    return this.maxSortedColumns;
  }


  /**
   * @return table model of the grid
   */
  public final VOListTableModel getVOListTableModel() {
    return grid.getVOListTableModel();
  }


  /**
   * @return current grid mode; possible values: Consts.READ_ONLY, Consts.EDIT, Consts.INSERT
   */
  public final int getMode() {
    return grid.getMode();
  }


  /**
  * Method called on changing grid mode.
  * @param mode current edit grid mode; possible values: READONLY, EDIT,INSERT
  */
  public final void modeChanged(int mode) {
    if (model.getRowCount()>0) {
      if (mode==Consts.READONLY) {
        int row = grid.getEditingRow();
        int col = grid.getEditingColumn();
        if (row!=-1 && col!=-1)
          grid.getCellEditor(row, col).stopCellEditing();
        else if (lockedGrid!=null) {
          col = lockedGrid.getEditingColumn();
          if (row!=-1 && col!=-1)
            lockedGrid.getCellEditor(row, col).stopCellEditing();
        }
        if (lockedGrid!=null)
          lockedGrid.requestFocus();
        else
          grid.requestFocus();

      }
      else if (mode==Consts.INSERT) {
        int rowToSel = 0;
        if (!isInsertRowsOnTop())
          rowToSel = model.getRowCount()-1;

        try {
          // retrieve value object of the inserting row...
          ValueObject vo = (ValueObject) model.getObjectForRow(rowToSel); // the first row is alsways the inserting row...
          // fire create v.o. event to the grid controller: used to fill in the v.o. with default values...
          gridController.createValueObject(vo);

          // check if there exists a combo-box editable column to pre-set...
          for(int i=0;i<colProps.length;i++)
            if (colProps[i] instanceof ComboColumn &&
                !((ComboColumn)colProps[i]).isNullAsDefaultValue()) {

              Object value = ((ComboColumn)colProps[i]).getDomain().getDomainPairList()[0].getCode();
              int col = model.findColumn(((ComboColumn)colProps[i]).getColumnName());
//              if (ClientSettings.PRESET_LAST_VALUE_IN_COMBO_COLUMN &&
//                getCurrentNumberOfNewRows()>1) {
//                value = getValueAt(
//                  isInsertRowsOnTop()?getCurrentNumberOfNewRows()-2:getVOListTableModel().getRowCount()-2,
//                  col
//                );
//              }

              model.setValueAt(
                value,
                rowToSel,
                col
              );
            }
        }
        catch (Throwable ex) {
          Logger.error(this.getClass().getName(),"modeChanged","Error while constructing value object '"+modelAdapter.getValueObjectType().getName()+"'",ex);
        }
        grid.setRowSelectionInterval(rowToSel,rowToSel);
        grid.setColumnSelectionInterval(0,0);
        grid.ensureRowIsVisible(rowToSel);

        if (lockedGrid!=null) {
//          lockedGrid.setRowSelectionInterval(rowToSel,rowToSel);
//          lockedGrid.setColumnSelectionInterval(0,0);
//          lockedGrid.ensureRowIsVisible(rowToSel);
          lockedGrid.editCellAt(rowToSel,0);
          lockedGrid.requestFocus();
        }
        else {
          grid.editCellAt(rowToSel, 0);
          grid.requestFocus();
        }
      }
      else if (mode==Consts.EDIT) {
        if (grid.getSelectedRow()==-1) {
          if (lockedGrid==null) {
            grid.editCellAt(0,0);
            grid.requestFocus();
          }
          else {
            lockedGrid.editCellAt(0,0);
            lockedGrid.requestFocus();
          }
        } else {
          if (lockedGrid==null) {
            grid.editCellAt(grid.getSelectedRow(),grid.getSelectedColumn()==-1?0:grid.getSelectedColumn());
            grid.requestFocus();
          }
          else {
            lockedGrid.editCellAt(lockedGrid.getSelectedRow(),lockedGrid.getSelectedColumn()==-1?0:lockedGrid.getSelectedColumn());
            lockedGrid.requestFocus();
          }
          currentEditingRow = grid.getSelectedRow();
        }
      }

      // enable navigation bar only if grid is in read only mode...
      if (navBar!=null) {
        if (mode == Consts.READONLY)
          navBar.setEnabled(true);
        else
          navBar.setEnabled(false);
      }
    } else
      // there are no records on grid: disable navigation bar...
      if (navBar!=null)
        navBar.setEnabled(false);
    this.revalidate();
    this.repaint();
    if (mode==Consts.READONLY) {
      grid.setRowSelectionAllowed(true);
      if (lockedGrid!=null)
        lockedGrid.setRowSelectionAllowed(true);
    } else {
      grid.setRowSelectionAllowed(false);
      if (lockedGrid!=null)
        lockedGrid.setRowSelectionAllowed(false);
    }

    resetButtonsState();
    if (gridController!=null)
      gridController.modeChanged(mode);
  }




  /**
   * @param rowNumber TableModel row index
   * @param attributeName attribute name that identifies a grid column
   * @return <code>true</code> means that the cell having the specified row and column index is editable, <code>false</code> otherwise
   */
  public final boolean isFieldEditable(int rowNumber,String attributeName) {
    return modelAdapter.isFieldEditable(getMode(),rowNumber,modelAdapter.getFieldIndex(attributeName));
  }




  /**
   * @return identifier (functionId) associated to the container
   */
  public final String getFunctionId() {
    return functionId;
  }


  /**
   * @return insert button linked to grid
   */
  public InsertButton getInsertButton() {
    return insertButton;
  }


  /**
   * @return export button linked to grid
   */
  public ExportButton getExportButton() {
    return exportButton;
  }


  /**
   * @return import button linked to grid
   */
  public ImportButton getImportButton() {
    return importButton;
  }


  /**
   * @return copy button linked to grid
   */
  public CopyButton getCopyButton() {
    return copyButton;
  }


  /**
   * @return edit button linked to grid
   */
  public EditButton getEditButton() {
    return editButton;
  }


  /**
   * Set edit button linked to grid.
   * @param editButton edit button linked to grid
   */
  public void setEditButton(EditButton editButton) {
    if (this.editButton != null)
      this.editButton.removeDataController(this);
    this.editButton = editButton;
    if (editButton != null) {
      editButton.addDataController(this);
      bindedButtons.add(editButton);
      editButton.setEnabled(true);
    }
  }


  /**
   * Set insert button linked to grid.
   * @param insertButton insert button linked to grid
   */
  public void setInsertButton(InsertButton insertButton) {
    if (this.insertButton != null)
      this.insertButton.removeDataController(this);
    this.insertButton = insertButton;
    if (insertButton != null) {
      insertButton.addDataController(this);
      bindedButtons.add(insertButton);
      insertButton.setEnabled(true);
    }
  }


  /**
   * Set export button linked to grid.
   * @param exportButton export button linked to grid
   */
  public void setExportButton(ExportButton exportButton) {
    if (this.exportButton != null)
      this.exportButton.removeDataController(this);
    this.exportButton = exportButton;
    if (exportButton != null) {
      exportButton.addDataController(this);
      bindedButtons.add(exportButton);
      exportButton.setEnabled(true);
    }
  }


  /**
   * Set import button linked to grid.
   * @param importButton import button linked to grid
   */
  public void setImportButton(ImportButton importButton) {
    if (this.importButton != null)
      this.importButton.removeDataController(this);
    this.importButton = importButton;
    if (importButton != null) {
      importButton.addDataController(this);
      bindedButtons.add(importButton);
      importButton.setEnabled(true);
    }
  }


  /**
   * Set copy button linked to grid.
   * @param copyButton copy button linked to grid
   */
  public void setCopyButton(CopyButton copyButton) {
    if (this.copyButton != null)
      this.copyButton.removeDataController(this);
    this.copyButton = copyButton;
    if (copyButton != null) {
      copyButton.addDataController(this);
      bindedButtons.add(copyButton);
      copyButton.setEnabled(true);
    }
  }


  /**
   * Set filter button linked to grid.
   * @param filterButton filter button linked to grid
   */
  public void setFilterButton(FilterButton filterButton) {
    if (this.filterButton != null)
      this.filterButton.removeDataController(this);
    this.filterButton = filterButton;
    if (filterButton != null) {
      filterButton.addDataController(this);
      bindedButtons.add(filterButton);
      filterButton.setEnabled(true);
    }
  }


  /**
   * @return filter button linked to grid
   */
  public FilterButton getFilterButton() {
    return filterButton;
  }


  /**
   * @return reload button linked to grid
   */
  public ReloadButton getReloadButton() {
    return reloadButton;
  }


  /**
   * Set reload button linked to grid.
   * @param reloadButton reload button linked to grid
   */
  public void setReloadButton(ReloadButton reloadButton) {
    if (this.reloadButton != null)
      this.reloadButton.removeDataController(this);
    this.reloadButton = reloadButton;
    if (reloadButton != null) {
      reloadButton.addDataController(this);
      bindedButtons.add(reloadButton);
      reloadButton.setEnabled(true);
    }
  }


  /**
   * @return delete button linked to grid.
   */
  public DeleteButton getDeleteButton() {
    return deleteButton;
  }


  /**
   * Set delete button linked to grid.
   * @param deleteButton delete button linked to grid
   */
  public void setDeleteButton(DeleteButton deleteButton) {
    if (this.deleteButton != null)
      this.deleteButton.removeDataController(this);
    this.deleteButton = deleteButton;
    if (deleteButton != null) {
      deleteButton.addDataController(this);
      bindedButtons.add(deleteButton);
      deleteButton.setEnabled(true);
    }
  }


  /**
   * @return save button linked to grid.
   */
  public SaveButton getSaveButton() {
    return saveButton;
  }


  /**
   * Set save button linked to grid.
   * @param saveButton save button linked to grid
   */
  public void setSaveButton(SaveButton saveButton) {
    if (this.saveButton != null)
      this.saveButton.removeDataController(this);
    this.saveButton = saveButton;
    if (saveButton != null) {
      saveButton.addDataController(this);
      bindedButtons.add(saveButton);
      saveButton.setEnabled(true);
    }
  }


  /**
   * Add an optional button.
   * @param button generic button
   */
  public final void addGenericButton(GenericButton button) {
    if (button!=null) {
      this.genericButtons.add(button);
      button.addDataController(this);
    }
  }


  /**
   * Remove an optional button.
   * @param button generic button
   */
  public final void removeGenericButton(GenericButton button) {
    if (button!=null)
      this.genericButtons.remove(button);
  }



  /**
   * @return navigation bar (optional)
   */
  public final NavigatorBar getNavBar() {
    return navBar;
  }


  /**
   * Method called when user click on "first" button on the navigation bar:
   * it will clear table model, reload data and select the first row of the block.
   * This operations are executed in a separated thread.
   */
  public final void firstRow(final NavigatorBar navBar) {
    if (!listenEvent)
      return;
    if (getMode()!=Consts.READONLY) {
      return;
    }
    listenEvent = false;
    new Thread() {
      public void run() {
        boolean errorOnLoad = true;
        if (getNavBar()!=null)
          getNavBar().setEnabled(false);

        if (getReloadButton()!=null) {
          getReloadButton().setEnabled(false);
        }
        if (getInsertButton()!=null) {
          getInsertButton().setEnabled(false);
        }
        if (getExportButton()!=null) {
          getExportButton().setEnabled(false);
        }
        if (getImportButton() != null) {
          getImportButton().setEnabled(false);
        }
        if (getCopyButton()!=null) {
          getCopyButton().setEnabled(false);
        }
        if (getDeleteButton()!=null) {
          getDeleteButton().setEnabled(false);
        }
        if (getEditButton()!=null) {
          getEditButton().setEnabled(false);
        }
        if (getFilterButton()!=null) {
          getFilterButton().setEnabled(false);
        }
        if (getSaveButton()!=null)
          getSaveButton().setEnabled(false);

        // data reloading (from the beginning)...
        startIndex = 0;
        errorOnLoad = ! loadData(GridParams.NEXT_BLOCK_ACTION);
        if (getNavBar()!=null)
          getNavBar().setEnabled(false);
        lastIndex = model.getRowCount()-1;
        if (getNavBar()!=null) {
          if (lastIndex <= 0)
            getNavBar().setFirstRow(true);
          else
            getNavBar().setFirstRow(false);
          if (model.getRowCount() <= 1 && !moreRows
          )
            getNavBar().setLastRow(true);
          else
            getNavBar().setLastRow(false);
        }


        if (getReloadButton()!=null)
          getReloadButton().setEnabled(true);

        if (model.getRowCount()>0) {
          grid.setRowSelectionInterval(0,0);
          grid.ensureRowIsVisible(grid.getSelectedRow());
          if (lockedGrid!=null) {
//            lockedGrid.setRowSelectionInterval(0,0);
//            lockedGrid.ensureRowIsVisible(grid.getSelectedRow());
          }

          if ((getNavBar()!=null) && (lastIndex==model.getRowCount()-1))
            getNavBar().setFirstRow(true);

          // set focus inside the grid...
          if (lockedGrid==null)
            grid.requestFocus();
          else
            lockedGrid.requestFocus();
          listenEvent = true;
        }
        // fire loading data completed event...
        if (gridController!=null)
          gridController.loadDataCompleted(errorOnLoad);

        if (!errorOnLoad)
          for(int i=0;i<loadDataCompletedListeners.size();i++)
            ((ActionListener)loadDataCompletedListeners.get(i)).actionPerformed(new ActionEvent(this,ActionEvent.ACTION_PERFORMED,"Load Data Completed"));

        resetButtonsState();

        // fire events related to navigator button pressed...
        if (navBar!=null) {
          navBar.fireButtonPressedEvent(NavigatorBar.FIRST_BUTTON);
        }
      }
    }.start();
  }


  /**
   * Method called when user click on "next" button on the navigation bar:
   * it may clear table model, reload next data block and select the first row of the block.
   * This operations are executed in a separated thread.
   */
  public final void nextRow(final NavigatorBar navBar,final ActionEvent e) {
    if (getMode()!=Consts.READONLY)
      return;
    if (!listenEvent)
      return;
    if (grid.getSelectedRow()!=-1 && grid.getSelectedRow()==model.getRowCount()-1) {
      if (moreRows) {
        listenEvent = false;
        new Thread() {
          public void run() {
            boolean errorOnLoad = true;
            if (getNavBar()!=null)
              getNavBar().setEnabled(false);

            if (getReloadButton()!=null) {
              getReloadButton().setEnabled(false);
            }
            if (getInsertButton()!=null) {
              getInsertButton().setEnabled(false);
            }
            if (getExportButton()!=null) {
              getExportButton().setEnabled(false);
            }
            if (getImportButton() != null) {
              getImportButton().setEnabled(false);
            }
            if (getCopyButton()!=null) {
              getCopyButton().setEnabled(false);
            }
            if (getDeleteButton()!=null) {
              getDeleteButton().setEnabled(false);
            }
            if (getEditButton()!=null) {
              getEditButton().setEnabled(false);
            }
            if (getFilterButton()!=null) {
              getFilterButton().setEnabled(false);
            }
            if (getSaveButton()!=null)
              getSaveButton().setEnabled(false);

            // reload data...
            startIndex = lastIndex+1;
            errorOnLoad = ! loadData(GridParams.NEXT_BLOCK_ACTION);
            if (model.getRowCount()>0) {
              grid.setRowSelectionInterval(0,0);
//              if (lockedGrid!=null)
//                lockedGrid.setRowSelectionInterval(0,0);
            }
            afterNextRow();
            if (getReloadButton()!=null)
              getReloadButton().setEnabled(true);
            listenEvent = true;

            // fire loading data completed event...
            if (gridController!=null)
              gridController.loadDataCompleted(errorOnLoad);

            if (!errorOnLoad)
              for(int i=0;i<loadDataCompletedListeners.size();i++)
                ((ActionListener)loadDataCompletedListeners.get(i)).actionPerformed(new ActionEvent(this,ActionEvent.ACTION_PERFORMED,"Load Data Completed"));

            resetButtonsState();

            // fire events related to navigator button pressed...
            if (navBar!=null && e!=null) {
              navBar.fireButtonPressedEvent(e.getActionCommand());
            }
          }
        }.start();
      }
    } else {
      grid.setRowSelectionInterval(grid.getSelectedRow()+1,grid.getSelectedRow()+1);
//      if (lockedGrid!=null)
//        lockedGrid.setRowSelectionInterval(lockedGrid.getSelectedRow(),lockedGrid.getSelectedRow());
      afterNextRow();

      // fire events related to navigator button pressed...
      if (navBar!=null && e!=null) {
        navBar.fireButtonPressedEvent(e.getActionCommand());
      }

      listenEvent = true;
    }
  }


  /**
   * Method called from nextRow method.
   */
  private void afterNextRow() {
    grid.ensureRowIsVisible(grid.getSelectedRow());
//    if (lockedGrid!=null) {
//      lockedGrid.ensureRowIsVisible(lockedGrid.getSelectedRow());
//    }
    if (getNavBar()!=null) {
      if (grid.getSelectedRow() != -1 && !moreRows &&
          grid.getSelectedRow() == model.getRowCount() - 1)
        getNavBar().setLastRow(true);
      else
        getNavBar().setLastRow(false);
      if (grid.getSelectedRow() > 0 || lastIndex > model.getRowCount() - 1)
        getNavBar().setFirstRow(false);
      else
        getNavBar().setFirstRow(true);
    }
  }


  /**
   * Method called when user click on "previous page" button on the navigation bar:
   * it does clear table model, reload previous data block and select the last row of the block.
   */
  public final void previousPage(NavigatorBar navBar) {
    grid.previousPage(navBar);
  }


  /**
   * Method called when user click on "next page" button on the navigation bar:
   * it does clear table model, reload next data block and select the first row of the block.
   * This operations are executed in a separated thread.
   */
  public final void nextPage(NavigatorBar navBar) {
    grid.nextPage(navBar);
  }


  /**
   * Method called when user click on "previous" button on the navigation bar:
   * it may clear table model, reload previous data block and select the last row of the block.
   * This operations are executed in a separated thread.
   */
  public final void previousRow(final NavigatorBar navBar,final ActionEvent e) {
    if (getMode()!=Consts.READONLY)
      return;
    if (!listenEvent)
      return;
    if (grid.getSelectedRow()==0 && lastIndex+1>model.getRowCount()) {
      listenEvent = false;
      new Thread() {
        public void run() {
          boolean errorOnLoad = true;
          if (getNavBar()!=null)
            getNavBar().setEnabled(false);

          if (getReloadButton()!=null) {
            getReloadButton().setEnabled(false);
          }
          if (getInsertButton()!=null) {
            getInsertButton().setEnabled(false);
          }
          if (getExportButton()!=null) {
            getExportButton().setEnabled(false);
          }
          if (getImportButton() != null) {
            getImportButton().setEnabled(false);
          }
          if (getCopyButton()!=null) {
            getCopyButton().setEnabled(false);
          }
          if (getDeleteButton()!=null) {
            getDeleteButton().setEnabled(false);
          }
          if (getEditButton()!=null) {
            getEditButton().setEnabled(false);
          }
          if (getFilterButton()!=null) {
            getFilterButton().setEnabled(false);
          }
          if (getSaveButton()!=null)
            getSaveButton().setEnabled(false);

          // reload data...
//          startIndex = Math.max(0,startIndex + 1);
          errorOnLoad = !loadData(GridParams.PREVIOUS_BLOCK_ACTION);
          grid.revalidate();
          grid.repaint();
//          if (lockedGrid!=null) {
//            lockedGrid.revalidate();
//            lockedGrid.repaint();
//          }
          SwingUtilities.invokeLater(new Runnable(){
            public void run() {
              grid.setRowSelectionInterval(model.getRowCount()-1,model.getRowCount()-1);
//              if (lockedGrid!=null) {
//                lockedGrid.setRowSelectionInterval(model.getRowCount(),model.getRowCount());
//              }
              // fire events related to navigator button pressed...
              if (navBar!=null && e!=null) {
                navBar.fireButtonPressedEvent(e.getActionCommand());
              }
            }
          });
          afterPreviousRow();
          if (getReloadButton()!=null)
            getReloadButton().setEnabled(true);
          listenEvent = true;

          // fire loading data completed event...
          if (gridController!=null)
            gridController.loadDataCompleted(errorOnLoad);

          if (!errorOnLoad)
            for(int i=0;i<loadDataCompletedListeners.size();i++)
              ((ActionListener)loadDataCompletedListeners.get(i)).actionPerformed(new ActionEvent(this,ActionEvent.ACTION_PERFORMED,"Load Data Completed"));

          resetButtonsState();

        }
      }.start();
    } else {
      if (grid.getSelectedRow()>0) {
        grid.setRowSelectionInterval(grid.getSelectedRow() - 1, grid.getSelectedRow() - 1);
//        if (lockedGrid!=null)
//          lockedGrid.setRowSelectionInterval(lockedGrid.getSelectedRow(), lockedGrid.getSelectedRow());
      }
      afterPreviousRow();

      // fire events related to navigator button pressed...
      if (navBar!=null && e!=null) {
        navBar.fireButtonPressedEvent(e.getActionCommand());
      }

      listenEvent = true;
    }
  }


  /**
   * Method called from previousRow method.
   */
  private void afterPreviousRow() {
    grid.ensureRowIsVisible(grid.getSelectedRow());
//    if (lockedGrid!=null)
//      lockedGrid.ensureRowIsVisible(lockedGrid.getSelectedRow());
    if (getNavBar()!=null) {
      if (grid.getSelectedRow() == 0 &&
          lastIndex + 1 <= model.getRowCount())
        getNavBar().setFirstRow(true);
      else
        getNavBar().setFirstRow(false);
      if (grid.getSelectedRow() < model.getRowCount() - 1 || moreRows)
        getNavBar().setLastRow(false);
      else
        getNavBar().setLastRow(true);
    }
  }


  /**
   * Method called when user click on "last" button on the navigation bar:
   * it may clear table model, reload the last data block and select the last row of the block.
   * This operations are executed in a separated thread.
   */
  public final void lastRow(final NavigatorBar navBar) {
    if (!listenEvent)
      return;
    if (getMode()!=Consts.READONLY)
      return;
    listenEvent = false;
    new Thread() {
      public void run() {
        boolean errorOnLoad = true;
        if (getNavBar()!=null)
          getNavBar().setEnabled(false);

        if (getReloadButton()!=null) {
          getReloadButton().setEnabled(false);
        }
        if (getInsertButton()!=null) {
          getInsertButton().setEnabled(false);
        }
        if (getExportButton()!=null) {
          getExportButton().setEnabled(false);
        }
        if (getImportButton() != null) {
          getImportButton().setEnabled(false);
        }
        if (getCopyButton()!=null) {
          getCopyButton().setEnabled(false);
        }
        if (getDeleteButton()!=null) {
          getDeleteButton().setEnabled(false);
        }
        if (getEditButton()!=null) {
          getEditButton().setEnabled(false);
        }
        if (getFilterButton()!=null) {
          getFilterButton().setEnabled(false);
        }
        if (getSaveButton()!=null)
          getSaveButton().setEnabled(false);

        // reload data...
        startIndex = -1;
        errorOnLoad = !loadData(GridParams.LAST_BLOCK_ACTION);
        if (getNavBar()!=null) {
          getNavBar().setLastRow(true);
          if (model.getRowCount() == 1 &&
              lastIndex + 1 == model.getRowCount())
            getNavBar().setFirstRow(true);
          else
            getNavBar().setFirstRow(false);
        }
        SwingUtilities.invokeLater(new Runnable() {
          public void run() {
            grid.revalidate();
            grid.repaint();
            grid.setRowSelectionInterval(model.getRowCount()-1,model.getRowCount()-1);
            grid.ensureRowIsVisible(grid.getSelectedRow());
//            if (lockedGrid!=null) {
//              lockedGrid.revalidate();
//              lockedGrid.repaint();
//              lockedGrid.setRowSelectionInterval(model.getRowCount()-1,model.getRowCount()-1);
//              lockedGrid.ensureRowIsVisible(lockedGrid.getSelectedRow());
//            }

            // fire events related to navigator button pressed...
            if (navBar!=null) {
              navBar.fireButtonPressedEvent(NavigatorBar.LAST_BUTTON);
            }


          }
        });
        if (getReloadButton()!=null)
          getReloadButton().setEnabled(true);
        listenEvent = true;

        // fire loading data completed event...
        if (gridController!=null)
          gridController.loadDataCompleted(errorOnLoad);

        if (!errorOnLoad)
          for(int i=0;i<loadDataCompletedListeners.size();i++)
            ((ActionListener)loadDataCompletedListeners.get(i)).actionPerformed(new ActionEvent(this,ActionEvent.ACTION_PERFORMED,"Load Data Completed"));

        resetButtonsState();

      }
    }.start();
  }



  /**
   * Method called by reload button:
   * - sets READONLY mode
   * - reloads TableModel, by calling executeReload method
   */
  public final void reload() {
    if (ClientSettings.MDI_TOOLBAR!=null &&
        !ClientSettings.MDI_TOOLBAR.containsDataController(this))
      ClientSettings.MDI_TOOLBAR.setDataController(this);

    if (getMode()!=Consts.READONLY) {
      // view confirmation dialog...
      boolean ok = true;
      if (gridControl!=null && gridControl.isShowWarnMessageBeforeReloading())
        ok = OptionPane.showConfirmDialog(
          this,
          ClientSettings.getInstance().getResources().getResource("Cancel changes and reload data?"),
          ClientSettings.getInstance().getResources().getResource("Attention"),
          JOptionPane.YES_NO_OPTION
        )==JOptionPane.YES_OPTION;

      if (ok)
        executeReload();
    } else if (getMode()==Consts.READONLY) {

      while(expandedRows.size()>0)
        collapseRow(((Integer)expandedRows.get(0)).intValue());
      currentNestedComponent = null;
      currentNestedComponentRow = -1;

      if (grid.getTableHeader()!=null)
        grid.getTableHeader().repaint();
      if (lockedGrid!=null && lockedGrid.getTableHeader()!=null)
        lockedGrid.getTableHeader().repaint();
      reloadData();
    }
  }


  /**
   * Reload TableModel.
   */
  public boolean executeReload() {
    try {
      grid.editingCanceled(null);
      if (lockedGrid!=null)
        lockedGrid.editingCanceled(null);
      model.setMode(Consts.READONLY);
      reloadData();
      super.repaint();
      if (getSaveButton()!=null)
        getSaveButton().setEnabled(false);

      // reset toolbar state...
      if (getEditButton()!=null)
        getEditButton().setEnabled(model.getRowCount()>0);
      if (getDeleteButton()!=null)
        getDeleteButton().setEnabled(model.getRowCount()>0);
      if (getInsertButton()!=null)
        getInsertButton().setEnabled(true);
      if (getExportButton()!=null)
        getExportButton().setEnabled(model.getRowCount()>0);
      if (getImportButton()!=null)
        getImportButton().setEnabled(true);
      if (getCopyButton()!=null)
        getCopyButton().setEnabled(model.getRowCount()>0);
      if (getFilterButton()!=null)
//        getFilterButton().setEnabled(model.getRowCount()>0);
        getFilterButton().setEnabled(true);
      this.gridController.afterReloadGrid();

      resetButtonsState();
      return true;
    }
    catch (Exception ex) {
      ex.printStackTrace();
    }
    return false;
  }


  /**
   * Method invoked by NavigatorBar to change page to load.
   * @param pageNr pabe number to load
   */
  public final void loadPage(int pageNr) {
    if (blockSize!=-1 &&
        (moreRows || startIndex>0)) {
      startIndex = blockSize*(pageNr-1);
      if (!listenEvent)
        return;
      listenEvent = false;
      new LoadDataThread().start();
    }
  }


  /**
   * This method fetch data and set them into the grid.
   * @param startIndex row index used to start data fetching
   * @param action action to execute on the startIndex; three possible action may be executed: GridCommand.NEXT_BLOCK_ACTION, GridCommand.PREVIOUS_BLOCK_ACTION, GridCommand.LAST_BLOCK_ACTION
   */
  private boolean loadData(final int action) {
    boolean result = false;
    int selMode = ListSelectionModel.SINGLE_SELECTION;
    try {
      selMode = grid.getSelectionModel().getSelectionMode();
    }
    catch (Exception ex2) {
    }
    try {

      try {
        SwingUtilities.invokeAndWait(new Runnable() {
          public void run() {
            // execute on EventQueue thread...
            if (grid!=null&& grid.getSelectionModel()!=null)
              grid.getSelectionModel().setSelectionMode(grid.getSelectionModel().SINGLE_SELECTION);
            if (gridType==Grid.MAIN_GRID) {
              statusPanel.setText(ClientSettings.getInstance().getResources().getResource("Loading data..."));
              statusPanel.setPage("");
            }

            // data fetching is dispached to the grid controller...
            ClientUtils.fireBusyEvent(true);
            try {
              ClientUtils.getParentWindow(Grids.this).setCursor(new java.awt.Cursor(java.awt.Cursor.WAIT_CURSOR));
              ClientUtils.getParentWindow(Grids.this).getToolkit().sync();
            }
            catch (Exception ex3) {
            }
          }
        });
      }
      catch (Exception ex) {
      }

      final Response answer;
      try {
        if (startIndex<0)
          startIndex = 0;
        if (gridDataLocator==null || modelAdapter==null)
          return false;
        answer = gridDataLocator.loadData(
            action,
            startIndex,
            quickFilterValues,
            currentSortedColumns,
            currentSortedVersusColumns,
            modelAdapter.getValueObjectType(),
            otherGridParams
        );
      }
      finally {
        try {
          SwingUtilities.invokeAndWait(new Runnable() {
            public void run() {
              // execute on EventQueue thread...
              try {
                ClientUtils.getParentWindow(Grids.this).setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
                ClientUtils.getParentWindow(Grids.this).getToolkit().sync();
              }
              catch (Exception ex2) {
              }
              ClientUtils.fireBusyEvent(false);
            }
          });
        }
        catch (Exception ex) {
        }
      }

      // clear table model...

      try {
        SwingUtilities.invokeAndWait(new Runnable() {
          public void run() {
            // execute on EventQueue thread...
            model.clear();
            grid.revalidate();
            grid.repaint();
            if (lockedGrid!=null) {
              lockedGrid.revalidate();
              lockedGrid.repaint();
            }

            if (answer==null || answer instanceof ErrorResponse) {
              lastIndex = -1;
              moreRows = false;
              if (answer!=null)
                OptionPane.showMessageDialog(
                    Grids.this,
                    ClientSettings.getInstance().getResources().getResource("Error while loading data")+":\n"+answer.getErrorMessage(),
                    ClientSettings.getInstance().getResources().getResource("Loading Data Error"),
                    JOptionPane.ERROR_MESSAGE
                );
            }
            else {
              java.util.List data = ((VOListResponse)answer).getRows();
              moreRows = ((VOListResponse)answer).isMoreRows();
              if (action == GridParams.NEXT_BLOCK_ACTION)
                lastIndex = startIndex + data.size() - 1;
              else if (action == GridParams.PREVIOUS_BLOCK_ACTION) {
                startIndex = startIndex - data.size();
                lastIndex = startIndex + data.size() - 1; // lastIndex - data.size();
              }
              else if (action == GridParams.LAST_BLOCK_ACTION) {
                lastIndex = ((VOListResponse)answer).getResultSetLength() - 1;
                startIndex = lastIndex - data.size() + 1;
              }
              grid.clearSelection();
              if (lockedGrid!=null) {
                lockedGrid.clearSelection();
              }


              // 21/05/2011: fix needed to clone all inner objects in the list,
              // otherwise a change in a value object on the client side
              // will involve all inner objects that are the same instance
              ValueObject vo = null;
              for(int i=0;i<data.size();i++) {
                vo = (ValueObject)data.get(i);
                try {
                  data.set(i, ((ValueObject)vo).clone());
                } catch (Throwable e) {
                  Logger.error(this.getClass().getName(), "loadData", e.getMessage(), e);
                }
              }

                // fill in the table model with data fetched from the grid controller...
              try {
                for (int i = 0; i < data.size(); i++) {
                  model.addObject( (ValueObject) data.get(i));
                }
              }
              catch (ClassCastException ex1) {
                Logger.error(this.getClass().getName(), "loadData", "Error while fetching data: value object is not an instance of ValueObject class.",null);
                throw ex1;
              }

              while(expandedRows.size()>0)
                collapseRow(((Integer)expandedRows.get(0)).intValue());
              cache.clear();

              // update status bar content...
              if (model.getRowCount()==0) {
                statusPanel.setPage("");
                totalResultSetLength = -1;
                if (getNavBar()!=null) {
                  if (startIndex>0 && blockSize>0)
                    getNavBar().updatePageNumber(startIndex/blockSize+1);
                  else
                    getNavBar().updatePageNumber(0);
                }
              }
              else {
                if (!((VOListResponse)answer).isMoreRows() && startIndex==0) {
                  // the whole resultset has been loaded...
//            String page = ClientSettings.getInstance().getResources().getResource("page")+" 1 "+ClientSettings.getInstance().getResources().getResource("of")+" 1";
//            statusPanel.setPage(page);
                  statusPanel.setPage("");
                  if (getNavBar()!=null)
                    getNavBar().updatePageNumber(0);
                  totalResultSetLength = model.getRowCount();
                }
                else {
                  // only a block of data has been loaded...
//            String page = ClientSettings.getInstance().getResources().getResource("page")+" "+(getLastIndex()/model.getRowCount()+1);
//            if (blockSize==-1)
//              blockSize = model.getRowCount();
//            if ( ((VOListResponse)answer).getTotalAmountOfRows()>0) {
//              page += " "+ClientSettings.getInstance().getResources().getResource("of")+" "+(((VOListResponse)answer).getTotalAmountOfRows()/model.getRowCount());
//              totalResultSetLength = ((VOListResponse)answer).getTotalAmountOfRows();
//            }
//            else {
//              totalResultSetLength = -1;
//            }
//            statusPanel.setPage(page);
//            if (getNavBar()!=null)
//              getNavBar().updatePageNumber(getLastIndex()/model.getRowCount()+1);

                  if (blockSize==-1)
                    blockSize = model.getRowCount();
                  String page = ClientSettings.getInstance().getResources().getResource("page")+" "+(getLastIndex()/blockSize+1);
                  if ( ((VOListResponse)answer).getTotalAmountOfRows()>0) {
                    page += " "+ClientSettings.getInstance().getResources().getResource("of")+" "+((((VOListResponse)answer).getTotalAmountOfRows()%blockSize==0?0:1)+((VOListResponse)answer).getTotalAmountOfRows()/blockSize);
                    totalResultSetLength = ((VOListResponse)answer).getTotalAmountOfRows();
                  }
                  else {
                    totalResultSetLength = -1;
                  }
                  statusPanel.setPage(page);
                  if (getNavBar()!=null)
                    getNavBar().updatePageNumber(getLastIndex()/blockSize+1);

                }
              }

              grid.revalidate();
              grid.repaint();

              Grids.this.revalidate();
              Grids.this.repaint();
            }

            // update toolbar...
            if (getInsertButton()!=null)
              getInsertButton().setEnabled(true);
            if (getExportButton()!=null)
              getExportButton().setEnabled(model.getRowCount()>0);
            if (getImportButton()!=null)
              getImportButton().setEnabled(true);
            if (getCopyButton()!=null)
              getCopyButton().setEnabled(model.getRowCount()>0);
            if (getEditButton()!=null)
              getEditButton().setEnabled(model.getRowCount()>0);
            if (getFilterButton()!=null)
//              getFilterButton().setEnabled(model.getRowCount()>0);
              getFilterButton().setEnabled(true);
            setGenericButtonsEnabled(model.getRowCount()>0);
            if (getDeleteButton()!=null)
              getDeleteButton().setEnabled(model.getRowCount()>0);

            resetButtonsState();

          }
        });
      }
      catch (Exception ex) {
      }

      result = true;
    }
    catch(Throwable ex) {
      Logger.error(this.getClass().getName(), "loadData", "Error while fetching data.", ex);
      SwingUtilities.invokeLater(new Runnable(){
        public void run() {
          OptionPane.showMessageDialog(
              Grids.this,
              ClientSettings.getInstance().getResources().getResource("Error while loading data"),
              ClientSettings.getInstance().getResources().getResource("Loading Data Error"),
              JOptionPane.ERROR_MESSAGE
          );
        }
      });

    }
    finally {
      try {
        grid.getSelectionModel().setSelectionMode(selMode);
//      MDIFrame.setBusy(false);
      }
      catch (Exception ex1) {
      }
    }
    return result;
  }




  /**
   * Method called when user clicks on insert button.
   */
  public final void insert() {
    try {
      if (getMode()==Consts.READONLY) {
        if (!gridController.beforeInsertGrid(gridControl))
          return;
        currentNumberOfNewRows = 1;
        model.setMode(Consts.INSERT);
        if (getInsertButton()!=null) {
          getInsertButton().setEnabled(false);
        }
        if (getExportButton()!=null) {
          getExportButton().setEnabled(false);
        }
        if (getImportButton() != null) {
          getImportButton().setEnabled(false);
        }
        if (getCopyButton()!=null) {
          getCopyButton().setEnabled(false);
        }
        if (getDeleteButton()!=null) {
          getDeleteButton().setEnabled(false);
        }
        if (getEditButton()!=null) {
          getEditButton().setEnabled(false);
        }
        if (getFilterButton()!=null) {
          getFilterButton().setEnabled(false);
        }
        setGenericButtonsEnabled(false);
        if (getReloadButton()!=null)
          getReloadButton().setEnabled(true);
        if (getSaveButton()!=null)
          getSaveButton().setEnabled(true);

        int rowToSel = 0;
        if (!isInsertRowsOnTop())
          rowToSel = model.getRowCount()-1;

        grid.setRowSelectionInterval(rowToSel,rowToSel);
        grid.setColumnSelectionInterval(0,0);
  //      if (lockedGrid!=null) {
  //        lockedGrid.setRowSelectionInterval(rowToSel,rowToSel);
  //        lockedGrid.setColumnSelectionInterval(0,0);
  //      }

        resetButtonsState();

        int col = 0;
        if (lockedGrid!=null) {
          if (ClientSettings.FIRST_CELL_RECEIVE_FOCUS) {
            while (col < lockedGrid.getColumnCount() && !lockedGrid.isCellEditable(rowToSel, col))
              col++;
            if (col < lockedGrid.getColumnCount())
              lockedGrid.setColumnSelectionInterval(col, col);
            if (lockedGrid.getSelectedColumn() != -1)
              lockedGrid.editCellAt(rowToSel, lockedGrid.getSelectedColumn());
            lockedGrid.requestFocus();
          }
          else {
            col = lockedGrid.getColumnCount()-1;
            while (col>=0 && !grid.isCellEditable(grid.getSelectedRow(),col))
              col--;
            if (col>=0)
              grid.setColumnSelectionInterval(col,col);
            grid.editCellAt(grid.getSelectedRow(),grid.getSelectedColumn());
            grid.requestFocus();
          }
        }

        if (lockedGrid==null || col==lockedGrid.getColumnCount()) {
          if (ClientSettings.FIRST_CELL_RECEIVE_FOCUS) {
            col = 0;
            while (col<grid.getColumnCount() && !grid.isCellEditable(rowToSel,col))
              col++;
            if (col<grid.getColumnCount())
              grid.setColumnSelectionInterval(col,col);
            if (!grid.hasFocus())
              grid.requestFocus();
            if (grid.getEditingRow()==rowToSel && grid.getEditingColumn()==grid.getSelectedColumn()) {
              grid.stopCellEditing();
            }
            grid.editCellAt(rowToSel,grid.getSelectedColumn());
            if (!grid.hasFocus())
              grid.requestFocus();
          }
          else {
            col = grid.getColumnCount()-1;
            while (col>=0 && !grid.isCellEditable(grid.getSelectedRow(),col))
              col--;
            if (col>=0)
              grid.setColumnSelectionInterval(col,col);
            if (grid.getEditingRow()==rowToSel && grid.getEditingColumn()==grid.getSelectedColumn()) {
              grid.stopCellEditing();
            }
            grid.editCellAt(grid.getSelectedRow(),grid.getSelectedColumn());
            if (!grid.hasFocus())
              grid.requestFocus();
          }
        }

      }
      else
        Logger.error(this.getClass().getName(),"insert","Setting grid to insert mode is not allowed: grid must be in read only mode.",null);
    }
    catch(Throwable t) {
      Logger.error(this.getClass().getName(),"insert",t.getMessage(),t);
    }
  }


  /**
   * Method called when user clicks on export button.
   */
  public final void export() {
    if (getMode()==Consts.READONLY) {
      HashSet colsVisible = new HashSet();
      for(int i=0;i<colProps.length;i++)
        if (colProps[i].isVisible())
          colsVisible.add(colProps[i].getColumnName());

      Window parentComp = ClientUtils.getParentWindow(this);
      ExportDialog d = null;
      if (parentComp instanceof JFrame)
        d = new ExportDialog(
            (JFrame)parentComp,
            this,
            colsVisible
        );
      else
        d = new ExportDialog(
          (JDialog)parentComp,
          this,
          colsVisible
      );
    }
    else
      Logger.error(this.getClass().getName(),"export","You cannot export data: grid must be in read only mode.",null);
  }


  /**
   * Method called when user clicks on import button.
   */
  public final void importData() {
    if (getMode()==Consts.READONLY) {
      HashSet colsVisible = new HashSet();
      for(int i=0;i<colProps.length;i++)
        if (colProps[i].isVisible())
          colsVisible.add(colProps[i].getColumnName());

      ImportDialog d = null;
      Window parentComp = ClientUtils.getParentWindow(this);
      if (parentComp instanceof JFrame)
        d = new ImportDialog(
            (JFrame)parentComp,
            this,
            colsVisible
        );
      else
        d = new ImportDialog(
            (JDialog)parentComp,
            this,
            colsVisible
        );

    }
    else
      Logger.error(this.getClass().getName(),"export","You cannot import data: grid must be in read only mode.",null);
  }



  /**
   * Method called when user has clicked on filter button.
   */
  public final void filterSort() {
    if (getMode()==Consts.READONLY) {
      if (filterDialog==null) {
        Window parentComp = ClientUtils.getParentWindow(grid);
        if (parentComp instanceof JFrame)
          filterDialog = new FilterDialog((JFrame)parentComp,colProps, this);
        else
          filterDialog = new FilterDialog((JDialog)parentComp,colProps, this);
      }
      filterDialog.init();
    }
  }


  /**
   * @return GridExportOptions object created, starting from current visible columns
   */
  public final GridExportOptions getDefaultGridExportOptions() {
    HashSet colsVisible = new HashSet();
    for(int i=0;i<colProps.length;i++)
      if (colProps[i].isVisible())
        colsVisible.add(colProps[i].getColumnName());
    return getDefaultGridExportOptions(colsVisible);
  }


  /**
   * @param attributesToExport attributes to export
   * @return GridExportOptions object created, starting from specified attributes to export
   */
  public final GridExportOptions getDefaultGridExportOptions(HashSet attributesToExport) {
    // create export options...
    Hashtable columnsWidth  = new Hashtable(colProps.length);
    Hashtable columnsType  = new Hashtable(colProps.length);
    for(int i=0;i<colProps.length;i++) {
      columnsWidth.put(colProps[i].getColumnName(),new Integer(colProps[i].getPreferredWidth()));
      columnsType.put(colProps[i].getColumnName(),new Integer(colProps[i].getColumnType()));
    }

    ArrayList topRows = new ArrayList();
    if (gridControl!=null && gridControl.getTopTable()!=null)
      topRows.addAll( gridControl.getTopTable().getVOListTableModel().getDataVector() );

    ArrayList bottomRows = new ArrayList();
    if (gridControl!=null && gridControl.getBottomTable()!=null)
      bottomRows.addAll( gridControl.getBottomTable().getVOListTableModel().getDataVector() );

    Hashtable attributeDescriptions = new Hashtable();
    for(int i=0;i<colProps.length;i++)
        attributeDescriptions.put(colProps[i].getColumnName(),ClientSettings.getInstance().getResources().getResource(colProps[i].getHeaderColumnName()));

    ArrayList exportColumns = new ArrayList();
    ArrayList exportAttrColumns = new ArrayList();
    for(int i=0;i<model.getColumnCount();i++)
      if (attributesToExport.contains(model.getColumnName(i))) {
        exportColumns.add( getHeaderColumnName(model.getColumnName(i)) );
        exportAttrColumns.add ( model.getColumnName(i) );
      }

    return new GridExportOptions(
        exportColumns,
        exportAttrColumns,
        quickFilterValues,
        currentSortedColumns,
        currentSortedVersusColumns,
        otherGridParams,
        ClientSettings.MAX_EXPORTABLE_ROWS,
        modelAdapter.getValueObjectType(),
        gridDataLocator,
        columnsWidth,
        columnsType,
        attributeDescriptions,
        topRows,
        bottomRows
    );
  }


  /**
   * Method called by ExportDialog to export data.
   * @param exportColumns column identifiers related to columns to export
   * @param exportAttrColumns attribute names related to the columns to export
   */
  public final void export(ArrayList exportColumns,ArrayList exportAttrColumns,String exportType) {
    // create export options...
    Hashtable columnsWidth  = new Hashtable(colProps.length);
    Hashtable columnsType  = new Hashtable(colProps.length);
    for(int i=0;i<colProps.length;i++) {
      columnsWidth.put(colProps[i].getColumnName(),new Integer(colProps[i].getPreferredWidth()));
      columnsType.put(colProps[i].getColumnName(),new Integer(colProps[i].getColumnType()));
    }

    ArrayList topRows = new ArrayList();
    if (gridControl!=null && gridControl.getTopTable()!=null)
      topRows.addAll( gridControl.getTopTable().getVOListTableModel().getDataVector() );

    ArrayList bottomRows = new ArrayList();
    if (gridControl!=null && gridControl.getBottomTable()!=null)
      bottomRows.addAll( gridControl.getBottomTable().getVOListTableModel().getDataVector() );

    Hashtable attributeDescriptions = new Hashtable();
    for(int i=0;i<colProps.length;i++)
        attributeDescriptions.put(colProps[i].getColumnName(),ClientSettings.getInstance().getResources().getResource(colProps[i].getHeaderColumnName()));

    ExportOptions opt = new ExportOptions(
        new GridExportOptions(
          exportColumns,
          exportAttrColumns,
          quickFilterValues,
          currentSortedColumns,
          currentSortedVersusColumns,
          otherGridParams,
          ClientSettings.MAX_EXPORTABLE_ROWS,
          modelAdapter.getValueObjectType(),
          gridDataLocator,
          columnsWidth,
          columnsType,
          attributeDescriptions,
          topRows,
          bottomRows
        ),
        ClientSettings.getInstance().getResources().getDateMask(Consts.TYPE_DATE),
        ClientSettings.getInstance().getResources().getDateMask(Consts.TYPE_TIME),
        ClientSettings.getInstance().getResources().getDateMask(Consts.TYPE_DATE_TIME),
        exportType,
        ClientSettings.EXPORT_TO_PDF_ADAPTER,
        ClientSettings.EXPORT_TO_RTF_ADAPTER
    );
    GridExportOptions gridExportOptions = (GridExportOptions)opt.getComponentsExportOptions().get(0);
    gridExportOptions.setShowFilteringConditions(ClientSettings.SHOW_FILTERING_CONDITIONS_IN_EXPORT);
    if (ClientSettings.SHOW_FRAME_TITLE_IN_EXPORT) {
      JInternalFrame intFrame = ClientUtils.getParentInternalFrame(this);
      if (intFrame!=null) {
       if (!intFrame.getTitle().equals(""))
        gridExportOptions.setTitle(intFrame.getTitle());
      }
      else {
        Window frame = ClientUtils.getParentWindow(this);
        if (frame!=null) {
          if (frame instanceof JFrame && !((JFrame)frame).getTitle().equals(""))
            gridExportOptions.setTitle(((JFrame)frame).getTitle());
          else if (frame instanceof JDialog && !((JDialog)frame).getTitle().equals(""))
            gridExportOptions.setTitle(((JDialog)frame).getTitle());
        }
      }
    }
    gridController.exportGrid(opt);

    try {
      if (gridDataLocator instanceof ServerGridDataLocator) {
        // export data grid by calling the server side...
        if ( ( (ServerGridDataLocator) gridDataLocator).getServerMethodName() == null) {
          Logger.error(this.getClass().getName(), "export", "You cannot export data: 'serverMethodName' property in ServerGridDataLocator must be defined.", null);
          return;
        }

        gridExportOptions.getOtherGridParams().put("LOAD_ALL",Boolean.TRUE);
        gridExportOptions.setServerMethodName( ( (ServerGridDataLocator) gridDataLocator).getServerMethodName());
        Response response = ClientUtils.getData(
            "exportDataGrid",
            opt
        );
        gridExportOptions.getOtherGridParams().remove("LOAD_ALL");
        if (response.isError()) {
          OptionPane.showMessageDialog(
              this,
              ClientSettings.getInstance().getResources().getResource(response.getErrorMessage()),
              ClientSettings.getInstance().getResources().getResource("Error"),
              JOptionPane.WARNING_MESSAGE
          );
        }
        else
          ClientUtils.showDocument(((TextResponse)response).getMessage());

      }
      if (gridDataLocator instanceof EJBGridDataLocator) {
        // export data grid by calling the EJB on the server side...
        if ( ((EJBGridDataLocator)gridDataLocator).getServerMethodName() == null) {
          Logger.error(this.getClass().getName(), "export", "You cannot export data: 'serverMethodName' property in EJBGridDataLocator must be defined.", null);
          return;
        }
        if ( ((EJBGridDataLocator)gridDataLocator).getInitialContext() == null) {
          Logger.error(this.getClass().getName(), "export", "You cannot export data: 'initialContext' property in EJBGridDataLocator must be defined.", null);
          return;
        }
        if ( ((EJBGridDataLocator)gridDataLocator).getEjbName() == null) {
          Logger.error(this.getClass().getName(), "export", "You cannot export data: 'ejbName' property in EJBGridDataLocator must be defined.", null);
          return;
        }

        Object obj = ((EJBGridDataLocator)gridDataLocator).getInitialContext().lookup(ClientSettings.EJB_EXPORT_BEAN_NAME);
        Response response = (Response)obj.getClass().getMethod("export",new Class[]{ExportOptions.class}).invoke(obj,new Object[]{opt});
        if (response.isError()) {
          OptionPane.showMessageDialog(
              this,
              ClientSettings.getInstance().getResources().getResource(response.getErrorMessage()),
              ClientSettings.getInstance().getResources().getResource("Error"),
              JOptionPane.WARNING_MESSAGE
          );
        }
        else {
          byte[] bytes = ((BytesResponse)response).getBytes();
          String fileName = ((BytesResponse)response).getFileName();
          FileOutputStream out = new FileOutputStream(fileName);
          out.write(bytes);
          out.close();

          ClientUtils.displayURL("file://"+fileName);
        }
      }
      else {
        String fileName = System.getProperty("java.io.tmpdir").replace('\\','/');
        if (!fileName.endsWith("/"))
          fileName += "/";

        // export data grid directly in the client (Windows o.s. ONLY)...
        byte[] doc = null;
        gridExportOptions.getOtherGridParams().put("LOAD_ALL",Boolean.TRUE);

        if (opt.getExportType().equals(opt.XLS_FORMAT)) {
          // generate the Excel document...
          doc = new ExportToExcel().getDocument(opt);
          fileName += "doc"+System.currentTimeMillis()+".xls";
        }
        else if (opt.getExportType().equals(opt.CSV_FORMAT1) || opt.getExportType().equals(opt.CSV_FORMAT2) ) {
          // generate the CSV document...
          doc = new ExportToCSV().getDocument(opt);
          // generate and return the document identifier...
          fileName += "doc"+System.currentTimeMillis()+".csv";
        }
        else if (opt.getExportType().equals(opt.XML_FORMAT)) {
          // generate the XML document...
          doc = new ExportToXML().getDocument(opt);
          // generate and return the document identifier...
          fileName += "doc"+System.currentTimeMillis()+".xml";
        }
        else if (opt.getExportType().equals(opt.XML_FORMAT_FAT)) {
          // generate the XML document...
          doc = new ExportToXMLFat().getDocument(opt);
          // generate and return the document identifier...
          fileName += "doc"+System.currentTimeMillis()+".xml";
        }
        else if (opt.getExportType().equals(opt.HTML_FORMAT)) {
          // generate the HTML document...
          doc = new ExportToHTML().getDocument(opt);
          // generate and return the document identifier...
          fileName += "doc"+System.currentTimeMillis()+".html";
        }
        else if (opt.getExportType().equals(opt.PDF_FORMAT)) {
          // generate the PDF document...
          doc = ExportToPDFFactory.createExportToPDFDocument(opt);
          // generate and return the document identifier...
          fileName += "doc"+System.currentTimeMillis()+".pdf";
        }
        else if (opt.getExportType().equals(opt.RTF_FORMAT)) {
          // generate the RTF document...
          doc = ExportToRTFFactory.createExportToRTFDocument(opt);
          // generate and return the document identifier...
          fileName += "doc"+System.currentTimeMillis()+".rtf";
        }

        gridExportOptions.getOtherGridParams().remove("LOAD_ALL");
        FileOutputStream out = new FileOutputStream(fileName);
        out.write(doc);
        out.close();

        ClientUtils.displayURL("file://"+fileName);
//        new File(fileName).delete();

//        String dir = new File(System.getProperty("user.home")).getAbsolutePath();
//        String cmd =
//            dir.substring(0,dir.indexOf(":")+1)+
//            "/Programmi/Internet Explorer/IEXPLORE.EXE file://"+
//            fileName;
//
//        Runtime.getRuntime().exec(cmd);

      }
    }
    catch (Throwable ex) {
      Logger.error(this.getClass().getName(), "export", "Error while exporting data:\n"+ex.getMessage(), ex);
    }
  }


  /**
   * Method called by ImportDialog to import data.
   * @param columnTypes column types
   * @param importAttrColumns attribute names related to the columns to import
   */
  public final void importData(ArrayList columnTypes,ArrayList importAttrColumns,String importType) {
    JFileChooser f = new JFileChooser();
    f.setDialogTitle(ClientSettings.getInstance().getResources().getResource("file to import"));
    f.setApproveButtonText(ClientSettings.getInstance().getResources().getResource("import"));
    f.setApproveButtonMnemonic(ClientSettings.getInstance().getResources().getResource("importmnemonic").charAt(0));
    if (importType.equals(ImportOptions.XLS_FORMAT)) {
      f.setFileFilter(new javax.swing.filechooser.FileFilter() {

        public boolean accept(File f) {
          return f.isDirectory() || f.getName().toLowerCase().endsWith(".xls");
        }

        public String getDescription() {
          return "Excel documents (*.xls)";
        }

      });
    }
    else if (importType.equals(ImportOptions.CSV_FORMAT1) || importType.equals(ImportOptions.CSV_FORMAT2))
      f.setFileFilter(new javax.swing.filechooser.FileFilter() {

        public boolean accept(File f) {
          return f.isDirectory() || f.getName().toLowerCase().endsWith(".csv") || f.getName().toLowerCase().endsWith(".txt");
        }

        public String getDescription() {
          return "Comma separated documents (*.csv;*.txt)";
        }

      });

    int res = f.showOpenDialog(ClientUtils.getParentWindow(this));
    if (res==f.APPROVE_OPTION) {
      File file = f.getSelectedFile();
      InputStream in = null;
      try {
        in = new FileInputStream(file);
      }
      catch (Exception ex2) {
        Logger.error(this.getClass().getName(), "import", "Error while importing data:\n"+ex2.getMessage(), ex2);
        OptionPane.showMessageDialog(
          this,
          ClientSettings.getInstance().getResources().getResource("Error while importing data")+":\n"+ex2.getMessage(),
          "Error",
          JOptionPane.ERROR_MESSAGE
        );
        return;
      }
      ArrayList rows = null;
      try {
        if (importType.equals(ImportOptions.XLS_FORMAT))

          // import the Excel document...
          rows = new ImportFromExcel().importData(importAttrColumns.size(),in);
        else if (importType.equals(ImportOptions.CSV_FORMAT1) || importType.equals(ImportOptions.CSV_FORMAT2)) {
          int[] colTypes = new int[columnTypes.size()];
          for(int i=0;i<columnTypes.size();i++)
            colTypes[i] = ((Integer)columnTypes.get(i)).intValue();

          SimpleDateFormat sdfdate = new SimpleDateFormat(ClientSettings.getInstance().getResources().getDateMask(Consts.TYPE_DATE));
          SimpleDateFormat sdftime = new SimpleDateFormat(ClientSettings.getInstance().getResources().getDateMask(Consts.TYPE_TIME));
          SimpleDateFormat sdfdatetime = new SimpleDateFormat(ClientSettings.getInstance().getResources().getDateMask(Consts.TYPE_DATE_TIME));

          rows = new ImportFromCSV(importType,sdfdate,sdftime,sdfdatetime).importData(colTypes,importAttrColumns,in);
        }
      } catch (Throwable ex) {
        Logger.error(this.getClass().getName(), "import",
                     "Error while importing data:\n" + ex.getMessage(), ex);
        OptionPane.showMessageDialog(
          this,
          ClientSettings.getInstance().getResources().getResource("Error while importing data")+":\n"+ex.getMessage(),
          "Error",
          JOptionPane.ERROR_MESSAGE
        );
        return;
      }
      finally {
        try {
          if (in != null)
            in.close();
        }
        catch (Exception ex1) {
        }
      }
      try {

        ValueObject vo = null;
        String attrName = null;
        Object[] row = null;
        ArrayList vos = new ArrayList();
        int[] rowNumbers = new int[rows.size()];
        while(rows.size()>0) {
          rowNumbers[rowNumbers.length-rows.size()] = rowNumbers.length-rows.size();
          row = (Object[])rows.get(0);
          rows.remove(0);
          vo = (ValueObject)model.getValueObjectType().newInstance();
          gridController.createValueObject(vo);
          model.insertObjectAt(vo,0);
          for(int j=0;j<importAttrColumns.size();j++) {
            attrName = importAttrColumns.get(j).toString();
            model.setValueAt(row[j],0,modelAdapter.getFieldIndex(attrName));
          }
          vos.add(vo);
        }

        if (maxNumberOfRowsOnInsert==1) {
          Response result = null;
          ArrayList singleVO = new ArrayList();
          int[] singleIndex = new int[] {0};
          for(int j=0;j<rowNumbers.length;j++) {
            singleIndex[0] = rowNumbers[j];
            singleVO.clear();
            singleVO.add(vos.get(j));
            result = gridController.insertRecords(singleIndex,singleVO);
            if (result.isError()) {
              OptionPane.showMessageDialog(
                this,
                ClientSettings.getInstance().getResources().getResource("Error while importing data")+":\n"+result.getErrorMessage(),
                "Error",
                JOptionPane.ERROR_MESSAGE
              );
              for(int i=j;i<rowNumbers.length;i++)
                model.removeObjectAt(0);
              lastIndex = lastIndex+j;
              return;
            }
          }
          lastIndex = lastIndex+rowNumbers.length;
          OptionPane.showMessageDialog(
            this,
            ClientSettings.getInstance().getResources().getResource("import completed"),
            "import",
            JOptionPane.INFORMATION_MESSAGE
          );
        }
        else {
          Response result = gridController.insertRecords(rowNumbers,vos);
          if (result.isError()) {
            OptionPane.showMessageDialog(
              this,
              ClientSettings.getInstance().getResources().getResource("Error while importing data")+":\n"+result.getErrorMessage(),
              "Error",
              JOptionPane.ERROR_MESSAGE
            );
            for(int i=0;i<rowNumbers.length;i++)
              model.removeObjectAt(0);
          }
          else {
            lastIndex = lastIndex+rowNumbers.length;
            OptionPane.showMessageDialog(
              this,
              ClientSettings.getInstance().getResources().getResource("import completed"),
              "import",
              JOptionPane.INFORMATION_MESSAGE
            );
          }
        }
      }
      catch (Throwable ex) {
        Logger.error(this.getClass().getName(), "import",
                     "Error while importing data:\n" + ex.getMessage(), ex);
        OptionPane.showMessageDialog(
          this,
          ClientSettings.getInstance().getResources().getResource("Error while importing data")+":\n"+ex.getMessage(),
          "Error",
          JOptionPane.ERROR_MESSAGE
        );
      }
    }
  }


  /**
   * Method called when user clicks on copy button.
   */
  public final void copy() {
    if (getMode()==Consts.READONLY) {
      try {
        if (!gridController.beforeCopyGrid(gridControl))
        return;

        // duplicate the current v.o...
        ValueObject vo = (ValueObject)( (ValueObject) model.getObjectForRow(grid.getSelectedRow())).clone();
        currentNumberOfNewRows = 1;

        // create a new empty v.o...
        model.setMode(Consts.INSERT);
        if (getInsertButton()!=null) {
          getInsertButton().setEnabled(false);
        }
        if (getExportButton()!=null) {
          getExportButton().setEnabled(false);
        }
        if (getImportButton() != null) {
          getImportButton().setEnabled(false);
        }
        if (getCopyButton()!=null) {
          getCopyButton().setEnabled(false);
        }
        if (getDeleteButton()!=null) {
          getDeleteButton().setEnabled(false);
        }
        if (getEditButton()!=null) {
          getEditButton().setEnabled(false);
        }
        if (getFilterButton()!=null) {
          getFilterButton().setEnabled(false);
        }
        setGenericButtonsEnabled(false);
        if (getReloadButton() != null) {
          getReloadButton().setEnabled(true);
        }
        if (getSaveButton() != null) {
          getSaveButton().setEnabled(true);
        }

        // fill in the new v.o. with the duplicable attributes of the cloned v.o...
        String attributeName = null;
        Object attributeValue = null;
        int newRowIndex = isInsertRowsOnTop() ? 0 : model.getRowCount()-1;
        for(int i=0;i<model.getColumnCount();i++) {
          attributeName = model.getColumnName(i);
          if (modelAdapter.isFieldDuplicable(i)) {
            attributeValue = modelAdapter.getField(vo,i);
//            attributeValue = vo.getClass().getMethod("get"+attributeName.substring(0,1).toUpperCase()+attributeName.substring(1),new Class[0]).invoke(vo,new Object[0]);
            modelAdapter.setField(model.getObjectForRow(newRowIndex),i,attributeValue);
          }

        }

        // set focus on the first (new) row...
        grid.setRowSelectionInterval(0, 0);
        grid.setColumnSelectionInterval(0, 0);
//        if (lockedGrid!=null) {
//          lockedGrid.setRowSelectionInterval(0, 0);
//          lockedGrid.setColumnSelectionInterval(0, 0);
//        }

        resetButtonsState();

        int col = 0;
        if (lockedGrid!=null) {
          if (ClientSettings.FIRST_CELL_RECEIVE_FOCUS) {
            while (col<lockedGrid.getColumnCount() && !lockedGrid.isCellEditable(lockedGrid.getSelectedRow(),col))
              col++;
            if (col<lockedGrid.getColumnCount())
              lockedGrid.setColumnSelectionInterval(col,col);
            if (lockedGrid.getSelectedColumn()!=-1) {
              grid.setCellEditor(null);
              lockedGrid.editCellAt(lockedGrid.getSelectedRow(),lockedGrid.getSelectedColumn());
            }
            lockedGrid.requestFocus();
          }
          else {
            col = lockedGrid.getColumnCount()-1;
            while (col>=0 && !grid.isCellEditable(grid.getSelectedRow(),col))
              col--;
            if (col>=0)
              grid.setColumnSelectionInterval(col,col);
            grid.setCellEditor(null);
            grid.editCellAt(grid.getSelectedRow(),grid.getSelectedColumn());
            grid.requestFocus();
          }
        }
        if (lockedGrid==null || col==lockedGrid.getColumnCount()) {
          if (ClientSettings.FIRST_CELL_RECEIVE_FOCUS) {
            col = 0;
            while (col<grid.getColumnCount() && !grid.isCellEditable(grid.getSelectedRow(),col))
              col++;
            if (col<grid.getColumnCount())
              grid.setColumnSelectionInterval(col,col);
            grid.setCellEditor(null);
            grid.editCellAt(grid.getSelectedRow(),grid.getSelectedColumn());
            grid.requestFocus();
          }
          else {
            col = grid.getColumnCount()-1;
            while (col>=0 && !grid.isCellEditable(grid.getSelectedRow(),col))
              col--;
            if (col>=0)
              grid.setColumnSelectionInterval(col,col);
            grid.setCellEditor(null);
            grid.editCellAt(grid.getSelectedRow(),grid.getSelectedColumn());
            grid.requestFocus();
          }
        }
      }
      catch (Throwable ex) {
        Logger.error(this.getClass().getName(),"copy","Error on duplicating a v.o.",ex);
      }
    }
    else
      Logger.error(this.getClass().getName(),"copy","Setting grid to insert mode is not allowed: grid must be in read only mode.",null);
  }


  /**
   * Method called when user clicks on edit button.
   */
  public final void edit() {
    if (getMode()==Consts.READONLY) {
      if (!gridController.beforeEditGrid(gridControl))
        return;

      model.setMode(Consts.EDIT);
      if (getInsertButton()!=null) {
        getInsertButton().setEnabled(false);
      }
      if (getExportButton()!=null) {
        getExportButton().setEnabled(false);
      }
      if (getImportButton() != null) {
        getImportButton().setEnabled(false);
      }
      if (getCopyButton()!=null) {
        getCopyButton().setEnabled(false);
      }
      if (getDeleteButton()!=null) {
        getDeleteButton().setEnabled(false);
      }
      if (getEditButton()!=null) {
        getEditButton().setEnabled(false);
      }
      if (getFilterButton()!=null) {
        getFilterButton().setEnabled(false);
      }
      setGenericButtonsEnabled(false);
      if (getReloadButton()!=null)
        getReloadButton().setEnabled(true);
      if (getSaveButton()!=null)
        getSaveButton().setEnabled(true);
      if (getVOListTableModel().getRowCount()>0) {
        if (grid.getSelectedRow()==-1) {
          grid.setRowSelectionInterval(0, 0);
//          if (lockedGrid!=null)
//            lockedGrid.setRowSelectionInterval(0, 0);
        }
        grid.setColumnSelectionInterval(0,0);
//        if (lockedGrid!=null)
//          lockedGrid.setColumnSelectionInterval(0, 0);
      }

      resetButtonsState();

      if (lockedGrid!=null)
        lockedGrid.requestFocus();
      else
        grid.requestFocus();

      int col = 0;
      if (lockedGrid!=null) {
        if (ClientSettings.FIRST_CELL_RECEIVE_FOCUS) {
          while (col<lockedGrid.getColumnCount() && !lockedGrid.isCellEditable(lockedGrid.getSelectedRow(),col))
            col++;
          if (col<lockedGrid.getColumnCount())
            lockedGrid.setColumnSelectionInterval(col,col);
          if (lockedGrid.getSelectedColumn()!=-1)
            lockedGrid.editCellAt(lockedGrid.getSelectedRow(),lockedGrid.getSelectedColumn());
          lockedGrid.requestFocus();
        }
        else {
          col = lockedGrid.getColumnCount()-1;
          while (col>=0 && !lockedGrid.isCellEditable(lockedGrid.getSelectedRow(),col))
            col--;
          if (col>=0)
            lockedGrid.setColumnSelectionInterval(col,col);
          if (lockedGrid.getSelectedColumn()!=-1)
            lockedGrid.editCellAt(lockedGrid.getSelectedRow(),lockedGrid.getSelectedColumn());
          lockedGrid.requestFocus();
        }
      }
      if (lockedGrid==null || col==lockedGrid.getColumnCount()) {
        if (ClientSettings.FIRST_CELL_RECEIVE_FOCUS) {
          col = 0;
          while (col<grid.getColumnCount() && !grid.isCellEditable(grid.getSelectedRow(),col))
            col++;
          if (col<grid.getColumnCount())
            grid.setColumnSelectionInterval(col,col);
          grid.editCellAt(grid.getSelectedRow(),grid.getSelectedColumn());
          grid.requestFocus();
        }
        else {
          col = grid.getColumnCount()-1;
          while (col>=0 && !grid.isCellEditable(grid.getSelectedRow(),col))
            col--;
          if (col>=0)
            grid.setColumnSelectionInterval(col,col);
          grid.editCellAt(grid.getSelectedRow(),grid.getSelectedColumn());
          grid.requestFocus();
        }
      }

    }
    else
      Logger.error(this.getClass().getName(),"edit","Setting grid to edit mode is not allowed: grid must be in read only mode.",null);
  }


  /**
   * Method called when user clicks on delete button.
   */
  public final void delete() {
    if (getMode()==Consts.READONLY) {
      if (!gridController.beforeDeleteGrid(gridControl))
        return;

      int[] selRows = grid.getSelectedRows();
      if (selRows.length==0) {
      }
      else if (
          OptionPane.showConfirmDialog(
              this,
              ClientSettings.getInstance().getResources().getResource("Delete Rows?"),
              ClientSettings.getInstance().getResources().getResource("Delete Confirmation"),
              JOptionPane.YES_NO_OPTION)==JOptionPane.YES_OPTION) {
        ArrayList vos = new ArrayList();
        for(int i=0;i<selRows.length;i++)
          vos.add(model.getObjectForRow(selRows[i]));

        try {
          Response response = gridController.deleteRecords(vos);
          if (!response.isError()) {
//            reloadData();
            Arrays.sort(selRows);
            int i=0;
            while(i<selRows.length) {
              model.removeObjectAt(selRows[i]-i);
              i++;
            }
            lastIndex = lastIndex-selRows.length;

            //funzione call back avvenuta cancellazione dati
            this.gridController.afterDeleteGrid();
          } else {
            // mostra il messaggio di errore di cancellazione non riuscita...
            OptionPane.showMessageDialog(
                this,
                ClientSettings.getInstance().getResources().getResource("Error while deleting rows.")+"\n"+
                ClientSettings.getInstance().getResources().getResource(response.getErrorMessage()),
                ClientSettings.getInstance().getResources().getResource("Deleting Error"),
                JOptionPane.ERROR_MESSAGE
            );
          }
        }
        catch (Throwable ex) {
          Logger.error(this.getClass().getName(),
            "delete","Error while deleting rows.",ex);

          // mostra il messaggio di errore di salvataggio non riuscito...
          OptionPane.showMessageDialog(
              this,
              ClientSettings.getInstance().getResources().getResource("Error while deleting rows.")+"\n"+ex.getMessage(),
              ClientSettings.getInstance().getResources().getResource("Deleting Error"),
              JOptionPane.ERROR_MESSAGE
          );
        }
      }
    }
    else {
      if ((getMode()==Consts.INSERT || getMode()==Consts.EDIT) &&
          getCurrentNumberOfNewRows()>0 && grid.getSelectedRow()!=-1) {

        grid.stopCellEditing();

        int row = grid.getSelectedRow();
        setCurrentNumberOfNewRows( getCurrentNumberOfNewRows()-1 );
        model.removeObjectAt(row);
        model.getChangedRowIndexes().remove(new Integer(row));
        if (model.getRowCount()>0 && row>0)
          row--;
        else if (model.getRowCount()>0)
          row = 0;
        else
          row = -1;

        if (row!=-1) {
          setRowSelectionInterval(row,row);
          grid.setColumnSelectionInterval(0,0);
          grid.ensureRowIsVisible(row);

          if (getLockedGrid()!=null) {
//            getLockedGrid().setRowSelectionInterval(row,row);
//            getLockedGrid().setColumnSelectionInterval(0,0);
//            getLockedGrid().ensureRowIsVisible(row);
            getLockedGrid().editCellAt(row,0);
            getLockedGrid().requestFocus();
          }
          else {
            grid.editCellAt(row, 0);
            requestFocus();
          }
        }


        if (getDeleteButton()!=null &&
            getMode()==Consts.INSERT &&
            getCurrentNumberOfNewRows()==1) {
          getDeleteButton().setEnabled(false);
        }
        else if (getDeleteButton()!=null &&
                 getMode()==Consts.EDIT &&
                 getCurrentNumberOfNewRows()==0) {
          getDeleteButton().setEnabled(false);
        }

      }
      else
        Logger.error(this.getClass().getName(),"delete","Delete rows is not allowed: grid must be in read only mode.",null);
    }
  }


  /**
   * Method called when user clicks on save button.
   * @return <code>true</code> if saving operation was correctly completed, <code>false</code> otherwise
   */
  public final boolean save() {
    if (grid.getCellEditor()!=null)
      grid.getCellEditor().stopCellEditing();
    if (lockedGrid!=null && lockedGrid.getCellEditor()!=null)
      lockedGrid.getCellEditor().stopCellEditing();

    int previousMode; // current grid mode...
    Response response = null;
    if (getMode()!=Consts.READONLY) {
      try {
        // check whether all columns are valid: if a validation error occours then saving operation is interrupted...
        if (!validateRows())
          return false;
      }
      catch (Exception ex) {
        Logger.error(this.getClass().getName(), "save", "Error on grid validation.", ex);
      }
      int[] newRowsIndexes = new int[currentNumberOfNewRows];
      try {
        // call grid controller to save data...
        previousMode = getMode();
        if (getMode()==Consts.INSERT) {
          ArrayList newRows = new ArrayList();
          for(int i=0;i<currentNumberOfNewRows;i++)
            if (isInsertRowsOnTop()) {
              newRowsIndexes[i] = i;
              newRows.add(model.getObjectForRow(newRowsIndexes[i]));
            }
            else {
              newRowsIndexes[i] = model.getRowCount()-currentNumberOfNewRows+i;
              newRows.add(model.getObjectForRow(newRowsIndexes[i]));
            }
          if (!gridController.beforeSaveDataInInsert(gridControl, newRowsIndexes,newRows))
            return false;
          response = gridController.insertRecords(newRowsIndexes,newRows);
        }
        else if (getMode()==Consts.EDIT) {
//          if (currentNumberOfNewRows>0) {
//            ArrayList newRows = new ArrayList();
//            for(int i=0;i<currentNumberOfNewRows;i++)
//              if (isInsertRowsOnTop()) {
//                newRowsIndexes[i] = i;
//                newRows.add(model.getObjectForRow(newRowsIndexes[i]));
//              }
//              else {
//                newRowsIndexes[i] = model.getRowCount()-currentNumberOfNewRows+i;
//                newRows.add(model.getObjectForRow(newRowsIndexes[i]));
//              }
//            response = gridController.insertRecords(newRowsIndexes,newRows);
//            if (response.isError()) {
//              // saving operation throws an error: it will be viewed on a dialog...
//              OptionPane.showMessageDialog(
//                  ClientUtils.getParentWindow(this),
//                  ClientSettings.getInstance().getResources().getResource("Error while saving")+":\n"+ClientSettings.getInstance().getResources().getResource(response.getErrorMessage()),
//                  ClientSettings.getInstance().getResources().getResource("Saving Error"),
//                  JOptionPane.ERROR_MESSAGE
//              );
//              return !response.isError();
//            }
//          }

          if (!gridController.beforeSaveDataInEdit(gridControl, model.getChangedRowNumbers(), model.getOldVOsChanged(), model.getChangedRows()))
            return false;
          response = gridController.updateRecords(model.getChangedRowNumbers(), model.getOldVOsChanged(), model.getChangedRows());
        }
        if (!response.isError()) {
          try {
            // patch inserted to disable image cell editor...
            int row = grid.getEditingRow();
            int col = grid.getEditingColumn();
            if (colProps[col].getColumnType()==Column.TYPE_IMAGE && row>=0 && col>=0) {
              grid.setVisibleColumn(col,false);
              grid.setVisibleColumn(col,true);
            }
          }
          catch (Exception ex1) {
          }
          if (getMode()==Consts.INSERT) {
            for(int i=0;i<currentNumberOfNewRows;i++)
              model.updateObjectAt((ValueObject)((VOListResponse)response).getRows().get(i),newRowsIndexes[i]);
            lastIndex = lastIndex+currentNumberOfNewRows;
          } else {
            for(int i=0;i<currentNumberOfNewRows;i++)
              model.updateObjectAt((ValueObject)((VOListResponse)response).getRows().get(i),newRowsIndexes[i]);
            for(int i=0;i<model.getChangedRowNumbers().length;i++)
              model.updateObjectAt((ValueObject)((VOListResponse)response).getRows().get(i),model.getChangedRowNumbers()[i]);
          }
          currentNumberOfNewRows = 0;

          model.setMode(Consts.READONLY);
          if (getReloadButton()!=null)
            getReloadButton().setEnabled(true);
          if (getSaveButton()!=null)
            getSaveButton().setEnabled(false);
          // reset toolbar buttons state...
          if (getEditButton()!=null)
            getEditButton().setEnabled(model.getRowCount()>0);
          if (getCopyButton()!=null)
            getCopyButton().setEnabled(model.getRowCount()>0);
          setGenericButtonsEnabled(true);
          if (getDeleteButton()!=null)
            getDeleteButton().setEnabled(model.getRowCount()>0);
          if (getInsertButton()!=null)
            getInsertButton().setEnabled(true);
          if (getExportButton()!=null)
            getExportButton().setEnabled(model.getRowCount()>0);
          if (getImportButton()!=null)
            getImportButton().setEnabled(true);
          if (getFilterButton()!=null)
//            getFilterButton().setEnabled(model.getRowCount()>0);
           getFilterButton().setEnabled(true);

            // +MC move here otherwise "afterInsertGrid" or "afterEditGrid" callback methods
            // are not able to change buttons states
          resetButtonsState();

          // fire saving completed event...
          switch (previousMode) {
            case Consts.INSERT:
              this.gridController.afterInsertGrid(gridControl);
              break;
            case Consts.EDIT:
              this.gridController.afterEditGrid(gridControl);
              break;
          }

          // -MC 05/06/2009: move before
          //resetButtonsState();

        } else {
          // saving operation throws an error: it will be viewed on a dialog...
          OptionPane.showMessageDialog(
              this,
              ClientSettings.getInstance().getResources().getResource("Error while saving")+":\n"+ClientSettings.getInstance().getResources().getResource(response.getErrorMessage()),
              ClientSettings.getInstance().getResources().getResource("Saving Error"),
              JOptionPane.ERROR_MESSAGE
          );
        }
      }
      catch (Exception ex) {
        Logger.error(this.getClass().getName(), "save", "Error while saving.", ex);
        OptionPane.showMessageDialog(
            this,
            ClientSettings.getInstance().getResources().getResource("Error while saving")+":\n"+ex.getMessage(),
            ClientSettings.getInstance().getResources().getResource("Saving Error"),
            JOptionPane.ERROR_MESSAGE
        );
      }
    }
    else
      Logger.error(this.getClass().getName(),"save","Saving data is not allowed in read only mode.",null);
    return !response.isError();
  }



  /**
  * Method called by JVM when viewing this class.
  */
  public final void addNotify() {
    super.addNotify();
    if (firstTime) {
      // this flag allows only one execution of the following instructions...
      firstTime = false;


      // disable all toolbar buttons, until table model is loaded...
      if (getSaveButton()!=null)
        getSaveButton().setEnabled(false);
      if (getInsertButton()!=null)
        getInsertButton().setEnabled(false);
      if (getExportButton()!=null)
        getExportButton().setEnabled(false);
      if (getImportButton()!=null)
        getImportButton().setEnabled(false);
      if (getCopyButton()!=null)
        getCopyButton().setEnabled(false);
      if (getEditButton()!=null)
        getEditButton().setEnabled(false);
      if (gridControl!=null && gridControl.isAutoLoadData()) {
        if (getFilterButton()!=null)
          getFilterButton().setEnabled(false);
      }
      setGenericButtonsEnabled(false);
      if (getDeleteButton()!=null)
        getDeleteButton().setEnabled(false);

      resetButtonsState();
    }
  }


  public Map getQuickFilterValues() {
    return quickFilterValues;
  }


  public JMenuItem getRemovefilterItem() {
    return removefilterItem;
  }


  public JPopupMenu getPopup() {
    return popup;
  }


  public QuickFilterPanel getFilterPanel() {
    return filterPanel;
  }
  public boolean isMoreRows() {
    return moreRows;
  }
  public int getLastIndex() {
    return lastIndex;
  }


  /**
   * @return current sorted columns
   */
  public final ArrayList getCurrentSortedColumns() {
    return currentSortedColumns;
  }


  /**
   * @return current sorted columns versus (Ascending/Descending)
   */
  public final ArrayList getCurrentSortedVersusColumns() {
    return currentSortedVersusColumns;
  }


  public void setFilterPanel(QuickFilterPanel filterPanel) {
    this.filterPanel = filterPanel;
  }
  public ArrayList getPopupCommands() {
    return popupCommands;
  }


  public ArrayList getGenericButtons() {
    return genericButtons;
  }


  public boolean isListenEvent() {
    return listenEvent;
  }


  public void setListenEvent(boolean listenEvent) {
    this.listenEvent = listenEvent;
  }


  /**
   * Set (dis)abilitation for whole generic buttons.
   * This method is automatically called when changing grid mode:
   * generic buttons are enabled only in read only mode.
   * @param enabled (dis)abilitation for whole generic buttons.
   */
  public void setGenericButtonsEnabled(boolean enabled) {
    for (int i=0; i<getGenericButtons().size(); i++)
      ((GenericButton) getGenericButtons().get(i)).setEnabled(enabled);
  }


//  /**
//   * Set (dis)abilitation for whole generic buttons, according to the old (previous) state.
//   */
//  public void setGenericButtonsOldValue() {
//    GenericButton button;
//    for (int i=0; i<getGenericButtons().size(); i++) {
//      button = (GenericButton) getGenericButtons().get(i);
//      button.setEnabled(getOldValue(button));
//    }
//  }


  /**
   * Execute a validation on changed rows.
   * @return <code>true</code> if all changed rows are in a valid state, <code>false</code> otherwise
   */
  public final boolean validateRows() {
    try {
      if (getMode()!=Consts.READONLY) {
        // validate pending changes...
        if (!grid.stopCellEditing())
          // pending invalid changes found...
          return false;

        if (lockedGrid!=null && !lockedGrid.stopCellEditing())
          // pending invalid changes found...
          return false;

        int[] rows = new int[0];
        if (getMode() == Consts.INSERT) {
          rows = new int[currentNumberOfNewRows];
          for(int i=0;i<currentNumberOfNewRows;i++)
            if (isInsertRowsOnTop())
              rows[i] = i;
            else
              rows[i] = model.getRowCount()-currentNumberOfNewRows+i;
        }
        else if (getMode() == Consts.EDIT)
          rows = model.getChangedRowNumbers();
        // all changed rows are validated...
        for (int i=0; i<rows.length; i++) {
          // all columns are validated (not only those that are editable)...
          for (int j=0; j<this.colProps.length; j++) {
            if (this.colProps[j].getColumnType()==colProps[j].TYPE_LOOKUP &&
              ((CodLookupColumn)this.colProps[j]).getLookupController()!=null &&
              !((CodLookupColumn)this.colProps[j]).getLookupController().isCodeValid())
              return false;
            else if (this.colProps[j].isColumnRequired())
              if ((model.getValueAt(rows[i], j)==null) || (model.getValueAt(rows[i], j).toString().equals(""))) {
                OptionPane.showMessageDialog(this,
                                              ClientSettings.getInstance().getResources().getResource("A mandatory column is empty.")+": "+ClientSettings.getInstance().getResources().getResource(this.colProps[j].getHeaderColumnName()),
                                              ClientSettings.getInstance().getResources().getResource("Value not valid"),
                                              JOptionPane.ERROR_MESSAGE);
                if (anchorLockedColumnsToLeft && j<lockedColumns) {
                  lockedGrid.editCellAt(rows[i], j);
                  lockedGrid.requestFocus();
                }
                else if (!anchorLockedColumnsToLeft && lockedColumns>0 && j>=this.colProps.length-lockedColumns) {
                  lockedGrid.editCellAt(rows[i], j-this.colProps.length-lockedColumns);
                  lockedGrid.requestFocus();
                }
                else {
                  grid.editCellAt(i, j - lockedColumns);
                  grid.requestFocus();
                }
                return false;
              }
          }
        }
        return(true);
      }
      else
        // validation skipped because grid is in read only mode...
        return true;
    }
    catch (Throwable ex) {
      Logger.error(this.getClass().getName(), "validate", "Error on validating columns.", ex);
      return(false);
    }
  }


  public GridControl getGridControl() {
    return gridControl;
  }


  private boolean onLoop = false;


  /**
   * Set row selection interval.
   * @param startRow first selected row index
   * @param endRow last selected row index
   */
  public void setRowSelectionInterval(int startRow,int endRow) {
    if (startRow==-1 || endRow==-1)
      return;

    if (onLoop)
      return;
    onLoop = true;

//    grid.setRowSelectionInterval(0,0);
    grid.setRowSelectionInterval(startRow,endRow);
    grid.ensureRowIsVisible(startRow);
    if (lockedGrid!=null) {
//      lockedGrid.setRowSelectionInterval(0,0);
//      lockedGrid.setRowSelectionInterval(startRow,endRow);
//      lockedGrid.ensureRowIsVisible(startRow);
    }

    onLoop = false;
  }


  public Grid getGrid() {
    return grid;
  }

  public Grid getLockedGrid() {
    return lockedGrid;
  }


  /**
   * Returns the indices of all selected rows.
   *
   * @return an array of integers containing the indices of all selected rows,
   *         or an empty array if no row is selected
   * @see #getSelectedRow
   */
  public int[] getSelectedRows() {
    return grid.getSelectedRows();
  }


  public int getSelectedRow() {
    return grid==null?-1:grid.getSelectedRow();
  }


  public int getSelectedColumn() {
    return grid==null?-1:grid.getSelectedColumn();
  }


  /**
   * @return default value that could be set in the quick filter criteria; values allowed: Consts.EQUALS,Consts.CONTAINS,Consts.STARTS_WITH,Consts.ENDS_WITH
   */
  public final int getDefaultQuickFilterCriteria() {
    return gridControl==null?ClientSettings.DEFAULT_QUICK_FILTER_CRITERIA:gridControl.getDefaultQuickFilterCriteria();
  }



  /**
   * Enable drag onto the grid.
   * @param gridId grid identifier
   */
  public final void enableDrag(String gridId) {
    grid.enableDrag(gridId);
    if (lockedGrid!=null)
      lockedGrid.enableDrag(gridId);
  }


  /**
   * Define GenericButton objects linked to this that will be disabled (independently from the grid mode)
   * when the specified attribute will be set to the specified value.
   * @param buttons collections GenericButton objects linked to this that have to be disabled
   * @param buttonController interface that defines button disabilitation, according to some custom policy
   */
  public final void addButtonsNotEnabled(HashSet buttons,GenericButtonController buttonController) {
    Iterator it = buttons.iterator();
    GenericButton genericButton = null;
    ArrayList list = null;
    while(it.hasNext()) {
      genericButton = (GenericButton)it.next();
      list = (ArrayList)buttonsToDisable.get(genericButton);
      if (list==null) {
        list = new ArrayList();
        buttonsToDisable.put(genericButton,list);
      }
      list.add(buttonController);
    }
  }


  /**
   * Method called by GenericButton.setEnabled method to check if the button must be disabled.
   * @param button button whose abilitation must be checked
   * @return <code>true</code> if no policy is defined in the grid for the specified button, <code>false</code> if there exists a disabilitation policy for the specified button (through addButtonsNotEnabledOnState grid method)
   */
  public final boolean isButtonDisabled(GenericButton button) {
    ArrayList list = (ArrayList)buttonsToDisable.get(button);
    if (list!=null) {
      GenericButtonController buttonController = null;
      for(int i=0;i<list.size();i++) {
        buttonController = (GenericButtonController)list.get(i);
        if (buttonController.isButtonDisabled(button))
          return true;
      }
    }
    return false;
  }


  /**
   * Method automatically called by the Grid to check buttons disabilitation.
   */
  public final void resetButtonsState() {
    if (insertButton!=null && isButtonDisabled(insertButton))
      insertButton.setEnabled(false);
    if (copyButton!=null && isButtonDisabled(copyButton))
      copyButton.setEnabled(false);
    if (editButton!=null && isButtonDisabled(editButton))
      editButton.setEnabled(false);
    if (reloadButton!=null && isButtonDisabled(reloadButton))
      reloadButton.setEnabled(false);
    if (saveButton!=null && isButtonDisabled(saveButton))
      saveButton.setEnabled(false);
    if (deleteButton!=null && isButtonDisabled(deleteButton))
      deleteButton.setEnabled(false);
    if (exportButton!=null && isButtonDisabled(exportButton))
      exportButton.setEnabled(false);
    if (importButton!=null && isButtonDisabled(importButton))
      importButton.setEnabled(false);
  }


  /**
   * @return maximum number of rows to insert by pressing "down" key; default value: 1
   */
  public final int getMaxNumberOfRowsOnInsert() {
    return maxNumberOfRowsOnInsert;
  }


  /**
   * Set the maximum number of rows to insert by pressing "down" key; default value: 1.
   * @param maxNumberOfRowsOnInsert maximum number of rows to insert by pressing "down" key
   */
  public final void setMaxNumberOfRowsOnInsert(int maxNumberOfRowsOnInsert) {
    this.maxNumberOfRowsOnInsert = maxNumberOfRowsOnInsert;
  }


  /**
   * @return allow insert row (using DOWN key) in edit mode too; default value: <code>false</code>
   */
  public final boolean isAllowInsertInEdit() {
    return allowInsertInEdit;
  }


  /**
   * Allow insert row (using DOWN key) in edit mode too; default value: <code>false</code>
   * @param allowInsertInEdit allow insert row (using DOWN key) in edit mode too
   */
  public final void setAllowInsertInEdit(boolean allowInsertInEdit) {
    this.allowInsertInEdit = allowInsertInEdit;
  }



  /**
   * @return define where new rows must be added: <code>true</code> at the top of the grid or <code>false</code> at the bottom
   */
  public final boolean isInsertRowsOnTop() {
    return insertRowsOnTop;
  }


  /**
   * Define where new rows must be added: <code>true</code> at the top of the grid or <code>false</code> at the bottom; default value: <code>true</code>
   * @param insertRowsOnTop define where new rows must be added: <code>true</code> at the top of the grid or <code>false</code> at the bottom
   */
  public final void setInsertRowsOnTop(boolean insertRowsOnTop) {
    this.insertRowsOnTop = insertRowsOnTop;
  }


  /**
   * @return current number of new rows
   */
  public final int getCurrentNumberOfNewRows() {
    return currentNumberOfNewRows;
  }


  /**
   * Set the current number of new rows.
   * @param currentSortedColumns current number of new rows
   */
  public void setCurrentNumberOfNewRows(int currentNumberOfNewRows) {
    this.currentNumberOfNewRows = currentNumberOfNewRows;
  }


  /**
   * Show/hide a column.
   * Do not invoke this method before grid is being visible.
   * @param attributeName attribute name that identities the column
   * @param visible <code>true</code> to show column; <code>false</code> to hide it
   */
  public final void setVisibleColumn(String attributeName,boolean colVisible) {
    int columnModelIndex = modelAdapter.getFieldIndex(attributeName);
    grid.setVisibleColumn(columnModelIndex,colVisible);
  }


  /**
   * @param attributeName attribute name related to a visible column
   * @return header column name if the specified attribute is found; attributeName otherwise
   */
  public final String getHeaderColumnName(String attributeName) {
    for(int i=0;i<colProps.length;i++)
      if (colProps[i].getColumnName().equals(attributeName))
        return ClientSettings.getInstance().getResources().getResource( colProps[i].getHeaderColumnName() );
    return attributeName;
  }


  /**
   * Method invoked by FilterPanel to repaint icons in column headers,
   * according to sorting/filtering conditions just applied.
   */
  public final void updateColumnHeaderIcons() {
    grid.updateColumnHeaderIcons();
    if (lockedGrid!=null)
      lockedGrid.updateColumnHeaderIcons();
  }


  public final void finalize() {
    if (filterDialog!=null)
      filterDialog.dispose();

    try {
      if (grid==null)
        return;
      grid.finalize();
      if (lockedGrid!=null)
        lockedGrid.finalize();

      FocusListener[] fl = getFocusListeners();
      for (int i = 0; i < fl.length; i++) {
        this.removeFocusListener(fl[i]);
      }
      MouseListener[] ml = getMouseListeners();
      for (int i = 0; i < ml.length; i++) {
        this.removeMouseListener(ml[i]);
      }
      KeyListener[] ll = getKeyListeners();
      for (int i = 0; i < ll.length; i++) {
        this.removeKeyListener(ll[i]);
      }

      lockedGrid = null;
      grid = null;
      model = null;
      gridController = null;
      insertButton = null;
      exportButton = null;
      importButton = null;
      copyButton = null;
      filterButton = null;
      editButton = null;
      reloadButton = null;
      deleteButton = null;
      saveButton = null;
      modelAdapter = null;
      navBar = null;
      gridDataLocator = null;
      gridControl = null;
      filterPanel = null;
      popup = null;
      removefilterItem = null;
      popupCommands = null;
      genericButtons = null;
      statusPanel = null;
      colProps = null;
      buttonsToDisable = null;
      filterDialog = null;
      loadDataCompletedListeners = null;
      expandedRows = null;
      cache = null;
      currentNestedComponent = null;
    }
    catch (Exception ex) {
      ex.printStackTrace();
    }
  }


  /**
   * @return locked + std grid scrollpane
   */
  public final JScrollPane getScroll() {
    return scroll;
  }


  /**
   * @return first row index to load on grid (related to result set); -1 = no rows yet loaded
   */
  public final int getstartIndex() {
    return startIndex;
  }


  /**
   * Sets the <code>rowMargin</code> and the <code>columnMargin</code> --
   * the height and width of the space between cells -- to
   * <code>intercellSpacing</code>.
   *
   * @param   intercellSpacing        a <code>Dimension</code>
   *					specifying the new width
   *					and height between cells
   * @see     #getIntercellSpacing
   * @beaninfo
   *  description: The spacing between the cells,
   *               drawn in the background color of the JTable.
   */
  public void setIntercellSpacing(Dimension spacing) {
    grid.setIntercellSpacing(spacing);
    if (lockedGrid!=null)
      lockedGrid.setIntercellSpacing(spacing);
  }


  /**
   * Sets the amount of empty space between cells in adjacent rows.
   *
   * @param  rowMargin  the number of pixels between cells in a row
   * @see     #getRowMargin
   * @beaninfo
   *  bound: true
   *  description: The amount of space between cells.
   */
  public void setRowMargin(int rowMargin) {
    grid.setRowMargin(rowMargin);
    if (lockedGrid!=null)
      lockedGrid.setRowMargin(rowMargin);
  }


  /**
   * Set the cell span for the specified range of cells.
   * @param rows row indexes that identify the cells to merge
   * @param columns column indexes that identify the cells to merge
   * @return <code>true</code> if merge operation is allowed, <code>false</code> if the cells range is invalid
   */
  public final boolean mergeCells(int[] rows,int[] columns) {
    return grid.mergeCells(rows,columns);
  }


  /**
   * Define if background and foreground colors must be setted according to GridController definition only in READONLY mode.
   * @param colorsInReadOnlyMode <code>false</code> to enable background and foreground colors to be setted according to GridController definition in all grid modes; <code>true</code> to enable background and foreground colors to be setted according to GridController definition only in READONLY mode
   */
  public final void setColorsInReadOnlyMode(boolean colorsInReadOnlyMode) {
    grid.setColorsInReadOnlyMode(colorsInReadOnlyMode);
  }


  /**
   * Add a "load data completed" listener.
   */
  public final void addLoadDataCompletedListener(ActionListener listener) {
    loadDataCompletedListeners.add(listener);
  }


  /**
   * Remove a "load data completed" listener.
   */
  public final void removeLoadDataCompletedListener(ActionListener listener) {
    loadDataCompletedListeners.remove(listener);
  }


  /**
   * @return grid controller, used to listen grid events
   */
  public final GridController getGridController() {
    return gridController;
  }


  /**
   * @param row row number
   * @return <code>true</code> if specified row is currently expanded, <code>false</code> otherwise
   */
  public final boolean isRowExpanded(int row) {
    return expandedRows.contains(new Integer(row));
  }


  /**
   * @param row row number
   * @return component already cached
   */
  public final Component getComponentInCache(int row) {
    return (Component)cache.get(new Integer(row));
  }


  /**
   * @param row row number
   * @return component already cached
   */
  public final void putComponentInCache(int row,Component comp) {
    cache.put(new Integer(row),comp);
  }


  /**
   * Removce component from cache.
   * @param row row number
   */
  public final void removeComponentInCache(int row) {
    cache.remove(new Integer(row));
  }


  /**
   * Expand specified row.
   * This command will be performed only if "expandableRowController" property is setted and row is not yet expanded
   * @param row row number
   */
  public final void expandRow(int row) {
    if (!expandedRows.contains(new Integer(row)))
     expandedRows.add(new Integer(row));
  }


  /**
   * Collapse specified row.
   * This command will be performed only if "expandableRowController" property is setted and row is not yet collapsed
   * @param row row number
   */
  public final void collapseRow(int row) {
    if (expandedRows.contains(new Integer(row)))
     expandedRows.remove(new Integer(row));
    currentNestedComponent = null;
  }


  /**
   * Collapse all expanded rows.
   * This command will be performed only if "expandableRowController" property is setted and row is not yet collapsed
   * @param row row number
   */
  public final void collapseAllRows() {
    int row = -1;
    while(expandedRows.size()>0) {
      row = ((Integer)expandedRows.get(0)).intValue();
      collapseRow(row);
      grid.collapseRow(row);
    }
    currentNestedComponent = null;
    currentNestedComponentRow = -1;
  }


  /**
   * Adds the rows from <code>index0</code> to <code>index1</code>, inclusive, to
   * the current selection.
   *
   * @exception IllegalArgumentException      if <code>index0</code> or <code>index1</code>
   *                                          lie outside [0, <code>getRowCount()</code>-1]
   * @param   index0 one end of the interval
   * @param   index1 the other end of the interval
   */
  public final void addRowSelectionInterval(int index0, int index1) {
    grid.addRowSelectionInterval(index0,index1);
  }


  /**
   * Deselects the rows from <code>index0</code> to <code>index1</code>, inclusive.
   *
   * @exception IllegalArgumentException      if <code>index0</code> or
   *						<code>index1</code> lie outside
   *                                          [0, <code>getRowCount()</code>-1]
   * @param   index0 one end of the interval
   * @param   index1 the other end of the interval
   */
  public final void removeRowSelectionInterval(int index0, int index1) {
    grid.removeRowSelectionInterval(index0,index1);
  }



  /**
   * Deselects all selected columns and rows.
   */
  public final void clearSelection() {
    grid.clearSelection();
  }


  /**
   * @return <code>true</code> is there is at least one row currently expanded; <code>false</code> if no row is currently expanded
   */
  public final boolean isAnyRowExpanded() {
  return !expandedRows.isEmpty();
  }


  /**
   * @return current nested component that is expanded and has focus
   */
  public final Component getCurrentNestedComponent() {
    return currentNestedComponent;
  }


  /**
   * @return current nested component row index
   */
  public final int getCurrentNestedComponentRow() {
    return currentNestedComponentRow;
  }


  /**
   * @return current nested component that is expanded and has focus
   */
  public final void setCurrentNestedComponent(int currentNestedComponentRow,Component currentNestedComponent) {
    this.currentNestedComponentRow = currentNestedComponentRow;
    this.currentNestedComponent = currentNestedComponent;
  }


  /**
   * @return force the editing of one row only: the current selected row: <code>false</code> all rows are editable, <code>true</code> edit is allowed only on current selected row
   */
  public final boolean isEditOnSingleRow() {
    return editOnSingleRow;
  }


  /**
   * Define if cell editing is allows on one row only or on all rows: <code>false</code> all rows are editable, <code>true</code> edit is allowed only on current selected row
   * @param editOnSingleRow <code>false</code> all rows are editable, <code>true</code> edit is allowed only on current selected row
   */
  public final void setEditOnSingleRow(boolean editOnSingleRow) {
    this.editOnSingleRow = editOnSingleRow;
  }


  /**
   * @return current selected row when grid is switched to EDIT mod and "editOnSingleRow" property is set to <code>true</code>
   */
  public final int getCurrentEditingRow() {
    return currentEditingRow;
  }


  /**
   * @return last record number in result set; -1 as default value
   */
  public final int getTotalResultSetLength() {
    return totalResultSetLength;
  }


  /**
   * @return block size; -1 if loading is not based on one page per time
   */
  public final int getBlockSize() {
    return blockSize;
  }


  /**
   * @return collection of buttons binded to grid (InsertButton, EditButton, etc)
   */
  public final HashSet getBindedButtons() {
    return bindedButtons;
  }


  /**
   * @param columnIndex column index, related to check-box, link button or button
   * @return indicate if the check-box is enabled also when the grid is in readonly mode; default value: <code>false</code> i.e. the check-box is enabled only in INSERT/EDIT modes, according to "editableOnEdit" and "editableOnInsert" properties
   */
  public final boolean isEnableInReadOnlyMode(int columnIndex) {
    if (colProps[columnIndex].getColumnType()==Column.TYPE_BUTTON)
      return ((ButtonColumn)colProps[columnIndex]).isEnableInReadOnlyMode();
    else if (colProps[columnIndex].getColumnType()==Column.TYPE_LINK)
      return true;
    else if (colProps[columnIndex].getColumnType()==Column.TYPE_CHECK)
      return ((CheckBoxColumn)colProps[columnIndex]).isEnableInReadOnlyMode();
    return false;
  }


  /**
   * Clean up cells content for the specified row, for each field or editable only cells.
   * Note: this method can be invoked only in INSERT/EDIT modes.
   * @param cleanUpAlsoNotEditableCells define if all cells must be clean up
   */
  public final void cleanUp(int row,boolean cleanUpAlsoNotEditableCells) {
    if (getMode()!=Consts.INSERT && getMode()!=Consts.EDIT) {
      Logger.error(this.getClass().getName(), "cleanUp", "You are not allowed to clean up fields in READONLY mode.", null);
    }
    else {
      for(int i=0;i<colProps.length;i++) {
        if (cleanUpAlsoNotEditableCells ||
            getMode()==Consts.INSERT && colProps[i].isEditableOnInsert() ||
            getMode()==Consts.EDIT && colProps[i].isEditableOnEdit())
          model.setField(row,colProps[i].getColumnName(),null);
      }
    }
  }








  /**
   * <p>Title: OpenSwing Framework</p>
   * <p>Description: JPopupMenu overrided to set focus on grid when the popup is being closed.</p>
   * <p>Copyright: Copyright (C) 2006 Mauro Carniel</p>
   * @author Mauro Carniel
   * @version 1.0
   */
  class GridPopup extends JPopupMenu {


    public final void setVisible(boolean v) {
      super.setVisible(v);
      if (!v && grid!=null && !grid.hasFocus())
        grid.requestFocus();
    }


  }






  /**
   * <p>Title: OpenSwing Framework</p>
   * <p>Description: Inner class used to load data in asynchronous way.</p>
   * <p>Copyright: Copyright (C) 2008 Mauro Carniel</p>
   * @author Mauro Carniel
   * @version 1.0
   */
  class LoadDataThread extends Thread {

    private boolean errorOnLoad = true;

    public void run() {

      try {
        SwingUtilities.invokeAndWait(new Runnable() {
          public void run() {
            if (navBar!=null)
              navBar.setEnabled(false);
            if (reloadButton!=null)
              reloadButton.setEnabled(false);
            if (insertButton!=null)
              insertButton.setEnabled(false);
            if (exportButton!=null)
              exportButton.setEnabled(false);
            if (importButton!=null)
              importButton.setEnabled(false);
            if (copyButton!=null)
              copyButton.setEnabled(false);
            if (editButton!=null)
              editButton.setEnabled(false);
            setGenericButtonsEnabled(false);
            if (deleteButton!=null)
              deleteButton.setEnabled(false);
            if (saveButton!=null)
              saveButton.setEnabled(false);
//        startIndex = Math.max(0,lastIndex-model.getRowCount()+1);
          }
        });
      }
      catch (Exception ex) {
      }


      // reload data...
      currentNumberOfNewRows = 0;
      repaint();
      selectedRowBeforeReloading = getSelectedRow();
      errorOnLoad = !loadData(GridParams.NEXT_BLOCK_ACTION);

      if (model.getRowCount()>0) {
        if (selectedRowBeforeReloading==-1)
          selectedRowBeforeReloading = 0;
        else if (selectedRowBeforeReloading>model.getRowCount()-1)
          selectedRowBeforeReloading = model.getRowCount()-1;

        SwingUtilities.invokeLater(new Runnable() {
          public void run() {
            if (selectedRowBeforeReloading==-1)
              return;
            grid.setRowSelectionInterval(selectedRowBeforeReloading,selectedRowBeforeReloading);
//            if (lockedGrid!=null)
//              lockedGrid.setRowSelectionInterval(selectedRowBeforeReloading,selectedRowBeforeReloading);
            grid.ensureRowIsVisible(selectedRowBeforeReloading);
//            if (lockedGrid!=null)
//              lockedGrid.ensureRowIsVisible(selectedRowBeforeReloading);


            // fire loading data completed event...
            if (gridController!=null)
              gridController.loadDataCompleted(errorOnLoad);

            if (!errorOnLoad)
              for(int i=0;i<loadDataCompletedListeners.size();i++)
                ((ActionListener)loadDataCompletedListeners.get(i)).actionPerformed(new ActionEvent(this,ActionEvent.ACTION_PERFORMED,"Load Data Completed"));

          }
        });
      } else {
        statusPanel.setText("");
        selectedRowBeforeReloading = 0;

        // fire loading data completed event...
        if (gridController!=null)
          gridController.loadDataCompleted(errorOnLoad);

        if (!errorOnLoad)
          for(int i=0;i<loadDataCompletedListeners.size();i++)
            ((ActionListener)loadDataCompletedListeners.get(i)).actionPerformed(new ActionEvent(this,ActionEvent.ACTION_PERFORMED,"Load Data Completed"));
      }
//        grid.ensureRowIsVisible(grid.getSelectedRow());
//        if (lockedGrid!=null)
//          lockedGrid.ensureRowIsVisible(grid.getSelectedRow());

      try {
        SwingUtilities.invokeAndWait(new Runnable() {
          public void run() {
            if (navBar!=null) {
              if (grid.getSelectedRow() != -1 && !moreRows &&
                  grid.getSelectedRow() == model.getRowCount() - 1 ||
                  model.getRowCount() == 0)
                navBar.setLastRow(true);
              else
                navBar.setLastRow(false);
              if (grid.getSelectedRow() > 0 || lastIndex > model.getRowCount() - 1)
                navBar.setFirstRow(false);
              else
                navBar.setFirstRow(true);
            }
            if (reloadButton!=null)
              reloadButton.setEnabled(true);
            if (saveButton!=null)
              saveButton.setEnabled(false);

            grid.revalidate();
            grid.repaint();
            if (grid.getTableHeader()!=null) {
              grid.getTableHeader().revalidate();
              grid.getTableHeader().repaint();
            }
            if (lockedGrid!=null) {
              lockedGrid.revalidate();
              lockedGrid.repaint();
              if (lockedGrid.getTableHeader()!=null) {
                lockedGrid.getTableHeader().revalidate();
                lockedGrid.getTableHeader().repaint();
              }
            }
          }
        });
      }
      catch (Exception ex) {
      }




      listenEvent = true;


      if (gridType==Grid.MAIN_GRID) {
        // load top grid if it exists...
        if (getGridControl()!=null && getGridControl().getTopTable()!=null){

          if (getGridControl().getTopGridDataLocator()==null) {
            Logger.error(this.getClass().getName(), "reloadData", "'topGridDataLocator' property was not defined for grid control", null);
            getGridControl().setTopGridDataLocator(new GridDataLocator() {

              public Response loadData(
                  int action,
                  int startIndex,
                  Map filteredColumns,
                  ArrayList currentSortedColumns,
                  ArrayList currentSortedVersusColumns,
                  Class valueObjectType,
                  Map otherGridParams) {
                ArrayList rows = new ArrayList();
                for(int i=0;i<getGridControl().getLockedRowsOnTop();i++)
                  try {
                    rows.add( Class.forName(getGridControl().getValueObjectClassName()).newInstance() );
                  }
                  catch (Throwable t) {
                    Logger.error(this.getClass().getName(), "reloadData", "Error while attempting to fill in the top grid", t);
                  }
                return new VOListResponse(rows,false,rows.size());
              }

            });
          }

          getGridControl().getTopTable().reload();
          getGridControl().getTopTable().revalidate();
          getGridControl().getTopTable().repaint();
        }

        // load bottom grid if it exists...
        if (getGridControl()!=null && getGridControl().getBottomTable()!=null){

          if (getGridControl().getBottomGridDataLocator()==null) {
            Logger.error(this.getClass().getName(), "reloadData", "'bottomGridDataLocator' property was not defined for grid control", null);
            getGridControl().setBottomGridDataLocator(new GridDataLocator() {

              public Response loadData(
                  int action,
                  int startIndex,
                  Map filteredColumns,
                  ArrayList currentSortedColumns,
                  ArrayList currentSortedVersusColumns,
                  Class valueObjectType,
                  Map otherGridParams) {
                ArrayList rows = new ArrayList();
                for(int i=0;i<getGridControl().getLockedRowsOnBottom();i++)
                  try {
                    rows.add( Class.forName(getGridControl().getValueObjectClassName()).newInstance() );
                  }
                  catch (Throwable t) {
                    Logger.error(this.getClass().getName(), "reloadData", "Error while attempting to fill in the bottom grid", t);
                  }
                return new VOListResponse(rows,false,rows.size());
              }

            });
          }

          getGridControl().getBottomTable().reload();
          getGridControl().getBottomTable().revalidate();
          getGridControl().getBottomTable().repaint();
        }
      }

      resetButtonsState();
    }
  }





  /**
   * This method fetches and appends additional rows that satisfy specified criteria.
   * @param attributeName attribute used to filter data
   * @param textToSearch text to search
   * @return first added row that satify specified criteria
   */
  public final int retrieveAdditionalRows(String attributeName,String textToSearch) {
    java.util.List data = null;
    try {
      Object value = gridController.beforeRetrieveAdditionalRows(attributeName,textToSearch);

      // data fetching is dispached to the grid controller...
      ClientUtils.fireBusyEvent(true);
      try {
        ClientUtils.getParentWindow(this).setCursor(new java.awt.Cursor(java.awt.Cursor.WAIT_CURSOR));
        ClientUtils.getParentWindow(this).getToolkit().sync();
      }
      catch (Exception ex3) {
      }

      FilterWhereClause[] filter = new FilterWhereClause[] {
          new FilterWhereClause(attributeName,ClientSettings.LIKE,value),
          null
      };
      quickFilterValues.put(attributeName,filter);

      Response answer = null;
      try {
        answer = gridDataLocator.loadData(
            GridParams.NEXT_BLOCK_ACTION,
            0,
            quickFilterValues,
            currentSortedColumns,
            currentSortedVersusColumns,
            modelAdapter.getValueObjectType(),
            otherGridParams
        );
      }
      finally {
        try {
          ClientUtils.getParentWindow(this).setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
          ClientUtils.getParentWindow(this).getToolkit().sync();
        }
        catch (Exception ex2) {
        }
        ClientUtils.fireBusyEvent(false);
        quickFilterValues.remove(attributeName);
      }

      if (answer==null || answer instanceof ErrorResponse) {
        if (answer!=null)
          Logger.error(this.getClass().getName(), "loadData", "Error while fetching data:\n"+answer.getErrorMessage(),null);
      }
      else {
        data = ((VOListResponse)answer).getRows();
        lastIndex += data.size();
        startIndex = lastIndex+1;

          // append to table model the data fetched from the grid controller...
        try {
          for (int i = 0; i < data.size(); i++) {
            model.addObject( (ValueObject) data.get(i));
          }
        }
        catch (ClassCastException ex1) {
          Logger.error(this.getClass().getName(), "loadData", "Error while fetching data: value object is not an instance of ValueObject class.",null);
          throw ex1;
        }

       // update status bar content...
        if (model.getRowCount()==0) {
          statusPanel.setPage("");
          totalResultSetLength = -1;
          if (getNavBar()!=null) {
            if (startIndex>0 && blockSize>0)
              getNavBar().updatePageNumber(startIndex/blockSize+1);
            else
              getNavBar().updatePageNumber(0);
          }
        }
        else {
          if (!((VOListResponse)answer).isMoreRows() && startIndex==0) {
            statusPanel.setPage("");
            if (getNavBar()!=null)
              getNavBar().updatePageNumber(0);
            totalResultSetLength = model.getRowCount();
          }
          else {
            // only a block of data has been loaded...
            if (blockSize==-1)
              blockSize = model.getRowCount();
            String page = ClientSettings.getInstance().getResources().getResource("page")+" "+(getLastIndex()/blockSize+1);
            if ( ((VOListResponse)answer).getTotalAmountOfRows()>0) {
              page += " "+ClientSettings.getInstance().getResources().getResource("of")+" "+(((VOListResponse)answer).getTotalAmountOfRows()/blockSize);
              totalResultSetLength = ((VOListResponse)answer).getTotalAmountOfRows();
            }
            else {
              totalResultSetLength = -1;
            }
            statusPanel.setPage(page);
            if (getNavBar()!=null)
              getNavBar().updatePageNumber(getLastIndex()/blockSize+1);

          }
        }

        grid.revalidate();
        grid.repaint();

        this.revalidate();
        this.repaint();
      }

    }
    catch(Throwable ex) {
      Logger.error(this.getClass().getName(), "loadData", "Error while fetching data.", ex);
    }
    if (data.size()>0)
      return getVOListTableModel().getRowCount()-data.size();
    return -1;
  }


  /**
   * @return define where to anchor locked columns: to the left or to the right of the grid; default value: <code>true</code> i.e. to the left
   */
  public final boolean isAnchorLockedColumnsToLeft() {
    return anchorLockedColumnsToLeft;
  }




}


