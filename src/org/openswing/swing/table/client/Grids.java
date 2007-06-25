package org.openswing.swing.table.client;


import java.util.*;
import java.util.List;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;
import javax.swing.table.*;

import org.openswing.swing.message.send.java.*;
import org.openswing.swing.message.receive.java.*;
import javax.swing.border.*;
import org.openswing.swing.table.columns.client.*;
import org.openswing.swing.table.model.client.*;
import org.openswing.swing.client.*;
import org.openswing.swing.logger.client.Logger;
import org.openswing.swing.table.filter.client.*;

import org.openswing.swing.util.client.*;
import org.openswing.swing.export.client.ExportDialog;
import org.openswing.swing.export.java.ExportOptions;
import org.openswing.swing.table.java.*;
import org.openswing.swing.util.client.ClientSettings;
import org.openswing.swing.form.client.Form;
import org.openswing.swing.export.java.ExportToExcel;
import org.openswing.swing.internationalization.java.*;
import org.openswing.swing.util.java.*;
import java.io.FileOutputStream;
import java.io.File;
import org.openswing.swing.table.editors.client.DomainCellEditor;
import java.lang.reflect.*;
import org.openswing.swing.export.java.ExportToCSV;
import org.openswing.swing.export.java.ExportToXML;
import org.openswing.swing.export.java.ExportToHTML;
import org.openswing.swing.table.renderers.client.ImageTableCellRenderer;
import org.openswing.swing.table.editors.client.ImageCellEditor;
import org.openswing.swing.export.java.ExportToPDF;
import org.openswing.swing.export.java.ExportToXMLFat;
import org.openswing.swing.export.java.ExportToRTF;


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
public class Grids extends JPanel implements VOListTableModelListener,DataController {

  private GridBagLayout gridBagLayout1 = new GridBagLayout();

  /** locked grid, anchored to the left (optional) */
  private Grid lockedGrid = null;

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
  private JPopupMenu popup = new JPopupMenu();

  /** menu item for removing column filtering */
  private JMenuItem removefilterItem = new JMenuItem(ClientSettings.getInstance().
      getResources().getResource("Remove Filter"),
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

  /** current number of new rows  */
  private int currentNumberOfNewRows = 0;

  /** filter dialog */
  private FilterDialog filterDialog = null;

  /** type of grid; possible values: Grid.MAIN_GRID, Grid.TOP_GRID, Grid.BOTTOM_GRID */
  private int gridType;

  /** locked + std grids scrollpane */
  private JScrollPane scroll = null;


  /**
   * Costructor called by GridControl: programmer never called directly this class.
   * @param valueObjectClassName ValueObject class name
   * @param colProps TableModel column properties (name, type, etc.)
   * @param controller grid controller, used to listen grid events
   * @param statusPanel bottom panel included into the grid; used to view selected row numbers
   * @param gridType type of grid; possible values: Grid.MAIN_GRID, Grid.TOP_GRID, Grid.BOTTOM_GRID
   */
  public Grids(
      GridControl gridControl,
      int lockedColumns,
      String valueObjectClassName,
      Column[] colProps,
      GridController gridController,
      GridStatusPanel statusPanel,
      GridDataLocator gridDataLocator,
      Map otherGridParams,
      int gridType) {
    this.gridControl = gridControl;
    this.lockedColumns = lockedColumns;
    this.colProps = colProps;
    this.gridController = gridController;
    this.statusPanel = statusPanel;
    this.gridDataLocator = gridDataLocator;
    this.otherGridParams = otherGridParams;
    this.gridType = gridType;


    try {
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
        lockedColumns,
        colProps.length,
        model,
        modelAdapter,
        gridController,
        false,
        gridType
    );
    if (lockedColumns>0) {
      for(int i=0;i<lockedColumns;i++)
        colProps[i].setColumnSelectable(false);
      this.lockedGrid = new Grid(
          this,
          colProps,
          statusPanel,
          0,
          lockedColumns,
          model,
          modelAdapter,
          gridController,
          true,
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


  private void jbInit() throws Exception {
    this.setLayout(gridBagLayout1);
    scroll = new JScrollPane(grid);

    if (lockedColumns>0) {
      lockedGrid.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
      lockedGrid.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
      JViewport viewport = new JViewport();
      viewport.setView(lockedGrid);
      viewport.setPreferredSize(lockedGrid.getPreferredSize());
      scroll.setRowHeaderView(viewport);
      if (lockedGrid.getTableHeader()!=null)
        scroll.setCorner(JScrollPane.UPPER_LEFT_CORNER, lockedGrid.getTableHeader());
      this.add(scroll,  new GridBagConstraints(0, 0, 1, 1, 0.0, 1.0
              ,GridBagConstraints.NORTHWEST, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
//      scroll.setMinimumSize(new Dimension(lockedGrid.getPreferredSize().width+5,lockedGrid.getPreferredSize().height));
    }
    this.add(scroll,   new GridBagConstraints(1, 0, 1, 1, 1.0, 1.0
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
   * Method called from reload method:
   * reload current data block. Data Loading is executed in a separated thread.
   */
  private final void reloadData() {
    if (!listenEvent)
      return;
    listenEvent = false;
    new Thread() {
      public void run() {
        boolean errorOnLoad = true;
        if (navBar!=null)
          navBar.setEnabled(false);
        if (reloadButton!=null)
          reloadButton.setEnabled(false);
        if (insertButton!=null)
          insertButton.setEnabled(false);
        if (exportButton!=null)
          exportButton.setEnabled(false);
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

        // reload data...
        errorOnLoad = ! loadData(GridParams.NEXT_BLOCK_ACTION);
        if (model.getRowCount()>0) {
          grid.setRowSelectionInterval(0,0);
          if (lockedGrid!=null)
            lockedGrid.setRowSelectionInterval(0,0);
        } else
          statusPanel.setText("");
        grid.ensureRowIsVisible(grid.getSelectedRow());
        if (lockedGrid!=null)
          lockedGrid.ensureRowIsVisible(grid.getSelectedRow());
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
          reloadButton.setEnabled(reloadButton.getOldValue());
        if (saveButton!=null)
          saveButton.setEnabled(saveButton.getOldValue());

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


        // fire loading data completed event...
        if (gridController!=null)
          gridController.loadDataCompleted(errorOnLoad);


        resetButtonsState();
      }
    }.start();
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
    grid.setMaxSortedColumns(maxSortedColumns);
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
        try {
          // retrieve value object of the inserting row...
          ValueObject vo = (ValueObject) model.getObjectForRow(0); // the first row is alsways the inserting row...
          // fire create v.o. event to the grid controller: used to fill in the v.o. with default values...
          gridController.createValueObject(vo);

          // check if there exists a combo-box editable column to pre-set...
          for(int i=0;i<colProps.length;i++)
            if (colProps[i] instanceof ComboColumn &&
                !((ComboColumn)colProps[i]).isNullAsDefaultValue())
              model.setValueAt(
                ((ComboColumn)colProps[i]).getDomain().getDomainPairList()[0].getCode(),
                0,
                model.findColumn(((ComboColumn)colProps[i]).getColumnName())
              );

        }
        catch (Throwable ex) {
          Logger.error(this.getClass().getName(),"modeChanged","Error while constructing value object '"+modelAdapter.getValueObjectType().getName()+"'",ex);
        }
        grid.setRowSelectionInterval(0,0);
        grid.setColumnSelectionInterval(0,0);
        grid.ensureRowIsVisible(0);

        if (lockedGrid!=null) {
          lockedGrid.setRowSelectionInterval(0,0);
          lockedGrid.setColumnSelectionInterval(0,0);
          lockedGrid.ensureRowIsVisible(0);
          lockedGrid.editCellAt(0,0);
          lockedGrid.requestFocus();
        }
        else {
          grid.editCellAt(0, 0);
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
   * @return exportbutton linked to grid
   */
  public ExportButton getExportButton() {
    return exportButton;
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
    if (editButton != null)
      editButton.addDataController(this);
  }


  /**
   * Set insert button linked to grid.
   * @param insertButton insert button linked to grid
   */
  public void setInsertButton(InsertButton insertButton) {
    if (this.insertButton != null)
      this.insertButton.removeDataController(this);
    this.insertButton = insertButton;
    if (insertButton != null)
      insertButton.addDataController(this);
  }


  /**
   * Set export button linked to grid.
   * @param exportButton export button linked to grid
   */
  public void setExportButton(ExportButton exportButton) {
    if (this.exportButton != null)
      this.exportButton.removeDataController(this);
    this.exportButton = exportButton;
    if (exportButton != null)
      exportButton.addDataController(this);
  }


  /**
   * Set copy button linked to grid.
   * @param copyButton copy button linked to grid
   */
  public void setCopyButton(CopyButton copyButton) {
    if (this.copyButton != null)
      this.copyButton.removeDataController(this);
    this.copyButton = copyButton;
    if (copyButton != null)
      copyButton.addDataController(this);
  }


  /**
   * Set filter button linked to grid.
   * @param filterButton filter button linked to grid
   */
  public void setFilterButton(FilterButton filterButton) {
    if (this.filterButton != null)
      this.filterButton.removeDataController(this);
    this.filterButton = filterButton;
    if (filterButton != null)
      filterButton.addDataController(this);
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
    if (reloadButton != null)
      reloadButton.addDataController(this);
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
    if (deleteButton != null)
      deleteButton.addDataController(this);
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
    if (saveButton != null)
      //imposta listener per nuovo pulsante
      saveButton.addDataController(this);
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
        if (getReloadButton()!=null)
          getReloadButton().setEnabled(false);
        if (getInsertButton()!=null)
          getInsertButton().setEnabled(false);
        if (getExportButton()!=null)
          getExportButton().setEnabled(false);
        if (getCopyButton()!=null)
          getCopyButton().setEnabled(false);
        if (getEditButton()!=null)
          getEditButton().setEnabled(false);
        setGenericButtonsEnabled(false);
        if (getDeleteButton()!=null)
          getDeleteButton().setEnabled(false);
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
          getReloadButton().setEnabled(getReloadButton().getOldValue());

        if (model.getRowCount()>0) {
          grid.setRowSelectionInterval(0,0);
          grid.ensureRowIsVisible(grid.getSelectedRow());
          if (lockedGrid!=null) {
            lockedGrid.setRowSelectionInterval(0,0);
            lockedGrid.ensureRowIsVisible(grid.getSelectedRow());
          }

          if ((getNavBar()!=null) && (lastIndex==model.getRowCount()-1))
            getNavBar().setFirstRow(true);
          if (getSaveButton()!=null)
            getSaveButton().setEnabled(getSaveButton().getOldValue());

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
  public final void nextRow(final NavigatorBar navBar) {
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
            if (getReloadButton()!=null)
              getReloadButton().setEnabled(false);
            if (getInsertButton()!=null)
              getInsertButton().setEnabled(false);
            if (getExportButton()!=null)
              getExportButton().setEnabled(false);
            if (getCopyButton()!=null)
              getCopyButton().setEnabled(false);
            if (getEditButton()!=null)
              getEditButton().setEnabled(false);
            setGenericButtonsEnabled(false);
            if (getDeleteButton()!=null)
              getDeleteButton().setEnabled(false);
            if (getSaveButton()!=null)
              getSaveButton().setEnabled(false);
            // reload data...
            startIndex = lastIndex+1;
            errorOnLoad = ! loadData(GridParams.NEXT_BLOCK_ACTION);
            if (model.getRowCount()>0) {
              grid.setRowSelectionInterval(0,0);
              if (lockedGrid!=null)
                lockedGrid.setRowSelectionInterval(0,0);
            }
            afterNextRow();
            if (getReloadButton()!=null)
              getReloadButton().setEnabled(getReloadButton().getOldValue());
            if (getSaveButton()!=null)
              getSaveButton().setEnabled(getSaveButton().getOldValue());
            listenEvent = true;

            // fire loading data completed event...
            if (gridController!=null)
              gridController.loadDataCompleted(errorOnLoad);

            resetButtonsState();

            // fire events related to navigator button pressed...
            if (navBar!=null) {
              navBar.fireButtonPressedEvent(NavigatorBar.NEXT_BUTTON);
            }
          }
        }.start();
      }
    } else {
      grid.setRowSelectionInterval(grid.getSelectedRow()+1,grid.getSelectedRow()+1);
      if (lockedGrid!=null)
        lockedGrid.setRowSelectionInterval(lockedGrid.getSelectedRow()+1,lockedGrid.getSelectedRow()+1);
      afterNextRow();

      // fire events related to navigator button pressed...
      if (navBar!=null) {
        navBar.fireButtonPressedEvent(NavigatorBar.NEXT_BUTTON);
      }

      listenEvent = true;
    }
  }


  /**
   * Method called from nextRow method.
   */
  private void afterNextRow() {
    grid.ensureRowIsVisible(grid.getSelectedRow());
    if (lockedGrid!=null) {
      lockedGrid.ensureRowIsVisible(lockedGrid.getSelectedRow());
    }
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
  public final void previousRow(final NavigatorBar navBar) {
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
          if (getReloadButton()!=null)
            getReloadButton().setEnabled(false);
          if (getInsertButton()!=null)
            getInsertButton().setEnabled(false);
          if (getExportButton()!=null)
            getExportButton().setEnabled(false);
          if (getCopyButton()!=null)
            getCopyButton().setEnabled(false);
          if (getEditButton()!=null)
            getEditButton().setEnabled(false);
          setGenericButtonsEnabled(false);
          if (getDeleteButton()!=null)
            getDeleteButton().setEnabled(false);
          if (getSaveButton()!=null)
            getSaveButton().setEnabled(false);
          // reload data...
//          startIndex = Math.max(0,startIndex + 1);
          errorOnLoad = !loadData(GridParams.PREVIOUS_BLOCK_ACTION);
          grid.revalidate();
          grid.repaint();
          if (lockedGrid!=null) {
            lockedGrid.revalidate();
            lockedGrid.repaint();
          }
          SwingUtilities.invokeLater(new Runnable(){
            public void run() {
              grid.setRowSelectionInterval(model.getRowCount()-1,model.getRowCount()-1);
              if (lockedGrid!=null) {
                lockedGrid.setRowSelectionInterval(model.getRowCount()-1,model.getRowCount()-1);
              }
              // fire events related to navigator button pressed...
              if (navBar!=null) {
                navBar.fireButtonPressedEvent(NavigatorBar.PREV_BUTTON);
              }
            }
          });
          afterPreviousRow();
          if (getReloadButton()!=null)
            getReloadButton().setEnabled(getReloadButton().getOldValue());
          if (getSaveButton()!=null)
            getSaveButton().setEnabled(getSaveButton().getOldValue());
          listenEvent = true;

          // fire loading data completed event...
          if (gridController!=null)
            gridController.loadDataCompleted(errorOnLoad);

          resetButtonsState();

        }
      }.start();
    } else {
      if (grid.getSelectedRow()>0) {
        grid.setRowSelectionInterval(grid.getSelectedRow() - 1, grid.getSelectedRow() - 1);
        if (lockedGrid!=null)
          lockedGrid.setRowSelectionInterval(lockedGrid.getSelectedRow() - 1, lockedGrid.getSelectedRow() - 1);
      }
      afterPreviousRow();

      // fire events related to navigator button pressed...
      if (navBar!=null) {
        navBar.fireButtonPressedEvent(NavigatorBar.PREV_BUTTON);
      }

      listenEvent = true;
    }
  }


  /**
   * Method called from previousRow method.
   */
  private void afterPreviousRow() {
    grid.ensureRowIsVisible(grid.getSelectedRow());
    if (lockedGrid!=null)
      lockedGrid.ensureRowIsVisible(lockedGrid.getSelectedRow());
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
        if (getReloadButton()!=null)
          getReloadButton().setEnabled(false);
        if (getInsertButton()!=null)
          getInsertButton().setEnabled(false);
        if (getExportButton()!=null)
          getExportButton().setEnabled(false);
        if (getCopyButton()!=null)
          getCopyButton().setEnabled(false);
        if (getEditButton()!=null)
          getEditButton().setEnabled(false);
        setGenericButtonsEnabled(false);
        if (getDeleteButton()!=null)
          getDeleteButton().setEnabled(false);
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
            if (lockedGrid!=null) {
              lockedGrid.revalidate();
              lockedGrid.repaint();
              lockedGrid.setRowSelectionInterval(model.getRowCount()-1,model.getRowCount()-1);
              lockedGrid.ensureRowIsVisible(lockedGrid.getSelectedRow());
            }

            // fire events related to navigator button pressed...
            if (navBar!=null) {
              navBar.fireButtonPressedEvent(NavigatorBar.LAST_BUTTON);
            }


          }
        });
        if (getReloadButton()!=null)
          getReloadButton().setEnabled(getReloadButton().getOldValue());
        if (getSaveButton()!=null)
          getSaveButton().setEnabled(getSaveButton().getOldValue());
        listenEvent = true;

        // fire loading data completed event...
        if (gridController!=null)
          gridController.loadDataCompleted(errorOnLoad);

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
    if (getMode()!=Consts.READONLY) {
      // view confirmation dialog...
      if (JOptionPane.showConfirmDialog(ClientUtils.getParentFrame(this),
                                    ClientSettings.getInstance().getResources().getResource("Cancel changes and reload data?"),
                                    ClientSettings.getInstance().getResources().getResource("Attention"),
                                    JOptionPane.YES_NO_OPTION)==JOptionPane.YES_OPTION)
        executeReload();
    } else if (getMode()==Consts.READONLY) {
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
      model.setMode(Consts.READONLY);
      grid.editingCanceled(null);
      if (lockedGrid!=null)
        lockedGrid.editingCanceled(null);
      reloadData();
      super.repaint();
      if (getSaveButton()!=null)
        getSaveButton().setEnabled(false);

      // reset toolbar state...
      if (getEditButton()!=null)
        getEditButton().setEnabled(getEditButton().getOldValue());
      if (getDeleteButton()!=null)
        getDeleteButton().setEnabled(getDeleteButton().getOldValue());
      if (getInsertButton()!=null)
//        getInsertButton().setEnabled(getInsertButton().getOldValue());
        getInsertButton().setEnabled(true);
      if (getExportButton()!=null)
        getExportButton().setEnabled(true);
      if (getCopyButton()!=null)
        getCopyButton().setEnabled(true);
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
   * This method fetch data and set them into the grid.
   * @param startIndex row index used to start data fetching
   * @param action action to execute on the startIndex; three possible action may be executed: GridCommand.NEXT_BLOCK_ACTION, GridCommand.PREVIOUS_BLOCK_ACTION, GridCommand.LAST_BLOCK_ACTION
   */
  private boolean loadData(int action) {
    boolean result = false;
    int selMode = grid.getSelectionModel().getSelectionMode();
    try {
//      if (startIndex+1<lastIndex-model.getRowCount() && action==GridParams.PREVIOUS_BLOCK_ACTION) {
//        startIndex = 0;
//        lastIndex = -1;
//        action = GridParams.NEXT_BLOCK_ACTION;
//      }
      grid.getSelectionModel().setSelectionMode(grid.getSelectionModel().SINGLE_SELECTION);
      if (gridType==Grid.MAIN_GRID)
        statusPanel.setText(ClientSettings.getInstance().getResources().getResource("Loading data..."));
      // data fetching is dispached to the grid controller...
      Response answer = gridDataLocator.loadData(
          action,
          startIndex,
          quickFilterValues,
          currentSortedColumns,
          currentSortedVersusColumns,
          modelAdapter.getValueObjectType(),
          otherGridParams
      );

      // crear table model...
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
          JOptionPane.showMessageDialog(
              ClientUtils.getParentFrame(this),
              ClientSettings.getInstance().getResources().getResource("Error while loading data")+":\n"+answer.getErrorMessage(),
              ClientSettings.getInstance().getResources().getResource("Loading Data Error"),
              JOptionPane.ERROR_MESSAGE
          );
      }
      else {
        ArrayList data = ((VOListResponse)answer).getRows();
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

          // fill in the table model with data fetched from the grid controller...
        for (int i = 0; i < data.size(); i++)
          model.addObject( (ValueObject) data.get(i));
        this.revalidate();
        this.repaint();
      }

      // update toolbar...
      if (getInsertButton()!=null)
        getInsertButton().setEnabled(true);
      if (getExportButton()!=null)
        getExportButton().setEnabled(true);
      if (getCopyButton()!=null)
        getCopyButton().setEnabled(model.getRowCount()>0);
      if (getEditButton()!=null)
        getEditButton().setEnabled(model.getRowCount()>0);
      setGenericButtonsEnabled(model.getRowCount()>0);
      if (getDeleteButton()!=null)
        getDeleteButton().setEnabled(model.getRowCount()>0);

      resetButtonsState();
      result = true;
    }
    catch(Throwable ex) {
      Logger.error(this.getClass().getName(), "loadData", "Error while fetching data.", ex);
      JOptionPane.showMessageDialog(
          ClientUtils.getParentFrame(this),
          ClientSettings.getInstance().getResources().getResource("Error while loading data"),
          ClientSettings.getInstance().getResources().getResource("Loading Data Error"),
          JOptionPane.ERROR_MESSAGE
      );
    }
    finally {
      grid.getSelectionModel().setSelectionMode(selMode);
//      MDIFrame.setBusy(false);
    }
    return result;
  }




  /**
   * Method called when user clicks on insert button.
   */
  public final void insert() {
    if (getMode()==Consts.READONLY) {
      if (!gridController.beforeInsertGrid(gridControl))
        return;
      model.setMode(Consts.INSERT);
      currentNumberOfNewRows = 1;
      if (getInsertButton()!=null)
        getInsertButton().setEnabled(false);
      if (getCopyButton()!=null)
        getCopyButton().setEnabled(false);
      if (getDeleteButton()!=null)
        getDeleteButton().setEnabled(false);
      if (getEditButton()!=null)
        getEditButton().setEnabled(false);
      setGenericButtonsEnabled(false);
      if (getReloadButton()!=null)
        getReloadButton().setEnabled(true);
      if (getSaveButton()!=null)
        getSaveButton().setEnabled(true);
      grid.setRowSelectionInterval(0,0);
      grid.setColumnSelectionInterval(0,0);
      if (lockedGrid!=null) {
        lockedGrid.setRowSelectionInterval(0,0);
        lockedGrid.setColumnSelectionInterval(0,0);
      }

      resetButtonsState();

      int col = 0;
      if (lockedGrid!=null) {
        while (col<lockedGrid.getColumnCount() && !lockedGrid.isCellEditable(lockedGrid.getSelectedRow(),col))
          col++;
        if (col<lockedGrid.getColumnCount())
          lockedGrid.setColumnSelectionInterval(col,col);
        if (lockedGrid.getSelectedColumn()!=-1)
          lockedGrid.editCellAt(lockedGrid.getSelectedRow(),lockedGrid.getSelectedColumn());
        lockedGrid.requestFocus();
      }

      if (lockedGrid==null || col==lockedGrid.getColumnCount()) {
        col = 0;
        while (col<grid.getColumnCount() && !grid.isCellEditable(grid.getSelectedRow(),col))
          col++;
        if (col<grid.getColumnCount())
          grid.setColumnSelectionInterval(col,col);
        grid.editCellAt(grid.getSelectedRow(),grid.getSelectedColumn());
        grid.requestFocus();
      }

    }
    else
      Logger.error(this.getClass().getName(),"insert","Setting grid to insert mode is not allowed: grid must be in read only mode.",null);
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

      ExportDialog d = new ExportDialog(
          ClientUtils.getParentFrame(this),
          this,
          colsVisible
      );
    }
    else
      Logger.error(this.getClass().getName(),"export","You cannot export data: grid must be in read only mode.",null);
  }




  /**
   * Method called when used has clicked on filter button.
   */
  public final void filterSort() {
    if (getMode()==Consts.READONLY) {
      if (filterDialog==null)
        filterDialog = new FilterDialog(colProps,this);
      filterDialog.init();
    }
  }


  /**
   * Method called by ExportDialog to export data.
   * @param exportColumns columns to export
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

    ExportOptions opt = new ExportOptions(
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
        ClientSettings.getInstance().getResources().getDateMask(Consts.TYPE_DATE),
        ClientSettings.getInstance().getResources().getDateMask(Consts.TYPE_TIME),
        ClientSettings.getInstance().getResources().getDateMask(Consts.TYPE_DATE_TIME),
        exportType,
        topRows,
        bottomRows
    );

    try {
      if (gridDataLocator instanceof ServerGridDataLocator) {
        // export data grid by calling the server side...
        if ( ( (ServerGridDataLocator) gridDataLocator).getServerMethodName() == null) {
          Logger.error(this.getClass().getName(), "export", "You cannot export data: 'serverMethodName' property in ServerGridDataLocator must be defined.", null);
          return;
        }

        opt.setServerMethodName( ( (ServerGridDataLocator) gridDataLocator).
                                getServerMethodName());
        Response response = ClientUtils.getData(
            "exportDataGrid",
            opt
            );
        if (response.isError()) {
          JOptionPane.showMessageDialog(
              ClientUtils.getParentFrame(this),
              ClientSettings.getInstance().getResources().getResource(response.getErrorMessage()),
              ClientSettings.getInstance().getResources().getResource("Error"),
              JOptionPane.WARNING_MESSAGE
          );
        }
        else
          ClientUtils.showDocument(((TextResponse)response).getMessage());

      }
      else {
        String fileName = System.getProperty("java.io.tmpdir").replace('\\','/');
        if (!fileName.endsWith("/"))
          fileName += "/";

        // export data grid directly in the client (Windows o.s. ONLY)...
        byte[] doc = null;

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
          doc = new ExportToPDF().getDocument(opt);
          // generate and return the document identifier...
          fileName += "doc"+System.currentTimeMillis()+".pdf";
        }
        else if (opt.getExportType().equals(opt.RTF_FORMAT)) {
          // generate the RTF document...
          doc = new ExportToRTF().getDocument(opt);
          // generate and return the document identifier...
          fileName += "doc"+System.currentTimeMillis()+".rtf";
        }

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
        if (getInsertButton() != null) {
          getInsertButton().setEnabled(false);
        }
        if (getExportButton() != null) {
          getExportButton().setEnabled(false);
        }
        if (getCopyButton() != null) {
          getCopyButton().setEnabled(false);
        }
        if (getDeleteButton() != null) {
          getDeleteButton().setEnabled(false);
        }
        if (getEditButton() != null) {
          getEditButton().setEnabled(false);
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
        for(int i=0;i<model.getColumnCount();i++) {
          attributeName = model.getColumnName(i);
          if (modelAdapter.isFieldDuplicable(i)) {
            attributeValue = vo.getClass().getMethod("get"+attributeName.substring(0,1).toUpperCase()+attributeName.substring(1),new Class[0]).invoke(vo,new Object[0]);
            modelAdapter.setField(model.getObjectForRow(0),i,attributeValue);
          }

        }

        // set focus on the first (new) row...
        grid.setRowSelectionInterval(0, 0);
        grid.setColumnSelectionInterval(0, 0);
        if (lockedGrid!=null) {
          lockedGrid.setRowSelectionInterval(0, 0);
          lockedGrid.setColumnSelectionInterval(0, 0);
        }

        resetButtonsState();

        int col = 0;
        if (lockedGrid!=null) {
          while (col<lockedGrid.getColumnCount() && !lockedGrid.isCellEditable(lockedGrid.getSelectedRow(),col))
            col++;
          if (col<lockedGrid.getColumnCount())
            lockedGrid.setColumnSelectionInterval(col,col);
          if (lockedGrid.getSelectedColumn()!=-1)
            lockedGrid.editCellAt(lockedGrid.getSelectedRow(),lockedGrid.getSelectedColumn());
          lockedGrid.requestFocus();

        }
        if (lockedGrid==null || col==lockedGrid.getColumnCount()) {
          col = 0;
          while (col<grid.getColumnCount() && !grid.isCellEditable(grid.getSelectedRow(),col))
            col++;
          if (col<grid.getColumnCount())
            grid.setColumnSelectionInterval(col,col);
          grid.editCellAt(grid.getSelectedRow(),grid.getSelectedColumn());
          grid.requestFocus();
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
      if (getInsertButton()!=null)
        getInsertButton().setEnabled(false);
      if (getExportButton()!=null)
        getExportButton().setEnabled(false);
      if (getCopyButton()!=null)
        getCopyButton().setEnabled(false);
      if (getDeleteButton()!=null)
        getDeleteButton().setEnabled(false);
      if (getEditButton()!=null)
        getEditButton().setEnabled(false);
      setGenericButtonsEnabled(false);
      if (getReloadButton()!=null)
        getReloadButton().setEnabled(true);
      if (getSaveButton()!=null)
        getSaveButton().setEnabled(true);
      if (grid.getSelectedRow()==-1) {
        grid.setRowSelectionInterval(0, 0);
        if (lockedGrid!=null)
          lockedGrid.setRowSelectionInterval(0, 0);
      }
      grid.setColumnSelectionInterval(0,0);
      if (lockedGrid!=null)
        lockedGrid.setColumnSelectionInterval(0, 0);

      resetButtonsState();

      if (lockedGrid!=null)
        lockedGrid.requestFocus();
      else
        grid.requestFocus();

      int col = 0;
      if (lockedGrid!=null) {
        while (col<lockedGrid.getColumnCount() && !lockedGrid.isCellEditable(lockedGrid.getSelectedRow(),col))
          col++;
        if (col<lockedGrid.getColumnCount())
          lockedGrid.setColumnSelectionInterval(col,col);
        if (lockedGrid.getSelectedColumn()!=-1)
          lockedGrid.editCellAt(lockedGrid.getSelectedRow(),lockedGrid.getSelectedColumn());
        lockedGrid.requestFocus();

      }
      if (lockedGrid==null || col==lockedGrid.getColumnCount()) {
        col = 0;
        while (col<grid.getColumnCount() && !grid.isCellEditable(grid.getSelectedRow(),col))
          col++;
        if (col<grid.getColumnCount())
          grid.setColumnSelectionInterval(col,col);
        grid.editCellAt(grid.getSelectedRow(),grid.getSelectedColumn());
        grid.requestFocus();
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
          JOptionPane.showConfirmDialog(
              ClientUtils.getParentFrame(this),
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
            JOptionPane.showMessageDialog(
                ClientUtils.getParentFrame(this),
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
          JOptionPane.showMessageDialog(
              ClientUtils.getParentFrame(this),
              ClientSettings.getInstance().getResources().getResource("Error while deleting rows.")+"\n"+ex.getMessage(),
              ClientSettings.getInstance().getResources().getResource("Deleting Error"),
              JOptionPane.ERROR_MESSAGE
          );
        }
      }
    }
    else
      Logger.error(this.getClass().getName(),"delete","Delete rows is not allowed: grid must be in read only mode.",null);
  }


  /**
   * Method called when user clicks on save button.
   * @return <code>true</code> if saving operation was correctly completed, <code>false</code> otherwise
   */
  public final boolean save() {
    int previousMode; // current grid mode...
    Response response = null;
    if (getMode()!=Consts.READONLY) {
      try {
        // all columns are validated: if a validation error occours then saving is interrupted...
        if (!validateRows())
          return false;
      }
      catch (Exception ex) {
        Logger.error(this.getClass().getName(), "save", "Error on grid validation.", ex);
      }
      try {
        // call grid controller to save data...
        previousMode = getMode();
        if (getMode()==Consts.INSERT) {
          ArrayList newRows = new ArrayList();
          int[] newRowsIndexes = new int[currentNumberOfNewRows];
          for(int i=0;i<currentNumberOfNewRows;i++)
            newRows.add( model.getObjectForRow(i) );
          response = gridController.insertRecords(newRowsIndexes,newRows);
        }
        else if (getMode()==Consts.EDIT)
          response = gridController.updateRecords(model.getChangedRowNumbers(), model.getOldVOsChanged(), model.getChangedRows());
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
              model.updateObjectAt((ValueObject)((VOListResponse)response).getRows().get(i),i);
            lastIndex = lastIndex+currentNumberOfNewRows;
            currentNumberOfNewRows = 0;
          } else {
            for(int i=0;i<model.getChangedRowNumbers().length;i++) {
              model.updateObjectAt((ValueObject)((VOListResponse)response).getRows().get(i),model.getChangedRowNumbers()[i]);
            }
          }

          model.setMode(Consts.READONLY);
          if (getReloadButton()!=null)
            getReloadButton().setEnabled(true);
          if (getSaveButton()!=null)
            getSaveButton().setEnabled(false);
          // reset toolbar buttons state...
          if (getEditButton()!=null)
            getEditButton().setEnabled(true);
          setGenericButtonsEnabled(true);
          if (getDeleteButton()!=null)
            getDeleteButton().setEnabled(true);
          if (getInsertButton()!=null)
            getInsertButton().setEnabled(getInsertButton().getOldValue());
          if (getExportButton()!=null)
            getExportButton().setEnabled(getExportButton().getOldValue());
          if (getCopyButton()!=null)
            getCopyButton().setEnabled(getCopyButton().getOldValue());

          // fire saving completed event...
          switch (previousMode) {
            case Consts.INSERT:
              this.gridController.afterInsertGrid(gridControl);
              break;
            case Consts.EDIT:
              this.gridController.afterEditGrid(gridControl);
              break;
          }

          resetButtonsState();

        } else {
          // saving operation throws an error: it will be viewed on a dialog...
          JOptionPane.showMessageDialog(
              ClientUtils.getParentFrame(this),
              ClientSettings.getInstance().getResources().getResource("Error while saving")+":\n"+ClientSettings.getInstance().getResources().getResource(response.getErrorMessage()),
              ClientSettings.getInstance().getResources().getResource("Saving Error"),
              JOptionPane.ERROR_MESSAGE
          );
        }
      }
      catch (Exception ex) {
        Logger.error(this.getClass().getName(), "save", "Error while saving.", ex);
        JOptionPane.showMessageDialog(
            ClientUtils.getParentFrame(this),
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
      if (getCopyButton()!=null)
        getCopyButton().setEnabled(false);
      if (getEditButton()!=null)
        getEditButton().setEnabled(false);
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
  public ArrayList getCurrentSortedColumns() {
    return currentSortedColumns;
  }
  public ArrayList getCurrentSortedVersusColumns() {
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


  /**
   * Set (dis)abilitation for whole generic buttons, according to the old (previous) state.
   */
  public void setGenericButtonsOldValue() {
    GenericButton button;
    for (int i=0; i<getGenericButtons().size(); i++) {
      button = (GenericButton) getGenericButtons().get(i);
      button.setEnabled(button.getOldValue());
    }
  }


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
            rows[i] = i;
        }
        else if (getMode() == Consts.EDIT)
          rows = model.getChangedRowNumbers();
        // all changed rows are validated...
        for (int i=0; i<rows.length; i++) {
          // all columns are validated (not only those that are editable)...
          for (int j=0; j<this.colProps.length; j++) {
            if (this.colProps[j].isColumnRequired())
              if ((model.getValueAt(i, j)==null) || (model.getValueAt(i, j).toString().equals(""))) {
                JOptionPane.showMessageDialog(ClientUtils.getParentFrame(this),
                                              ClientSettings.getInstance().getResources().getResource("A mandatory column is empty."),
                                              ClientSettings.getInstance().getResources().getResource("Value not valid"),
                                              JOptionPane.ERROR_MESSAGE);
                if (j<lockedColumns) {
                  lockedGrid.editCellAt(i, j);
                  lockedGrid.requestFocus();
                }
                else {
                  grid.editCellAt(i, j - lockedColumns);
                  grid.requestFocus();
                }
                return (false);
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

    grid.setRowSelectionInterval(0,0);
    grid.setRowSelectionInterval(startRow,endRow);
    grid.ensureRowIsVisible(startRow);
    if (lockedGrid!=null) {
      lockedGrid.setRowSelectionInterval(0,0);
      lockedGrid.setRowSelectionInterval(startRow,endRow);
      lockedGrid.ensureRowIsVisible(startRow);
    }

    onLoop = false;
  }


  public Grid getGrid() {
    return grid;
  }

  public Grid getLockedGrid() {
    return lockedGrid;
  }


  public int[] getSelectedRows() {
    return grid.getSelectedRows();
  }


  public int getSelectedRow() {
    return grid.getSelectedRow();
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


  public void finalize() {
    if (filterDialog!=null)
      filterDialog.dispose();
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


}


