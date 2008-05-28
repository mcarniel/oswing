package org.openswing.swing.table.client;

import java.text.*;
import java.util.*;

import java.awt.*;
import java.awt.datatransfer.*;
import java.awt.dnd.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.*;
import javax.swing.event.*;
import javax.swing.plaf.*;
import javax.swing.table.*;

import org.openswing.swing.client.*;
import org.openswing.swing.logger.client.*;
import org.openswing.swing.mdi.client.*;
import org.openswing.swing.message.receive.java.*;
import org.openswing.swing.message.send.java.*;
import org.openswing.swing.table.columns.client.*;
import org.openswing.swing.table.filter.client.*;
import org.openswing.swing.table.model.client.*;
import org.openswing.swing.table.profiles.java.*;
import org.openswing.swing.table.renderers.client.*;
import org.openswing.swing.util.client.*;
import org.openswing.swing.util.java.*;


/**
 * <p>Title: OpenSwing Framework</p>
 * <p>Description: Grid based on VOListTableModel.
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
public class Grid extends JTable
    implements QuickFilterListener, DragSourceListener, DropTargetListener, SearchControl {


  /** TableModel adapter, used to link ValueObjects to TableModel */
  private VOListAdapter modelAdapter = null;

  private VOListTableModel model = null;

  /** TableModel column properties (name, type, etc.) */
  private Column[] colProps = null;

  /** TableColumn linked to the grid */
  private TableColumn[] tableColumnModel = null;

  /** active cell background color */
  private Color activeCellBackgroundColor = ClientSettings.GRID_ACTIVE_CELL_BACKGROUND;

  /** flag used to enable column reordering */
  private boolean reorderingAllowed = true;

  /** flag used to enable column resizing */
  private boolean resizingAllowed = true;

  /** bottom panel included into the grid; used to view selected row numbers */
  private GridStatusPanel statusPanel = null;

  /** scroll pane which contains the grid */
  private JScrollPane scrollPane = null;

  /** column header border */
  private static final Border columnHeaderBorder = new BevelBorder(BevelBorder.RAISED);

  /** asceding order versus icon */
  private Icon ascSort = new ImageIcon(ClientUtils.getImage(ClientSettings.SORT_DOWN));

  /** desceding order versus icon */
  private Icon descSort = new ImageIcon(ClientUtils.getImage(ClientSettings.SORT_UP));

  /** flag used inside addNotify to set column headers and the toolbar state */
  private boolean firstTime = true;

  private Grids grids = null;

  private int fromColIndex;

  private int toColIndex;

  private GridController gridController = null;

  /** <code>true</code> if this is the locked grid */
  private boolean lockedGrid;

  /** drag source */
  private DragSource dragSource = new DragSource();

  /** grid identifier, used for DnD */
  private String gridId = null;

  /** drop gestures */
  private DropTarget dropTarget = null;
  private DropTarget dropTarget2 = null;

  /** flag used to define if row height can change for each row, according to image height included in a cell of grid; default value: <code>true</code> */
  private boolean rowHeightFixed = true;

  /** flag used to define if grid sorting operation must always invoke loadData method to retrieve a new list of v.o. or the grid must sort the current v.o. list without invoking loadData (only with the whole result set loaded); default value: <code>true</code> */
  private boolean orderWithLoadData = true;

  /** type of grid; possible values: Grid.MAIN_GRID, Grid.TOP_GRID, Grid.BOTTOM_GRID */
  private int gridType;

  /** main grid */
  public static final int MAIN_GRID = 0;

  /** top grid (optional), that contains the top locked rows */
  public static final int TOP_GRID = 1;

  /** bottom grid (optional), that contains the bottom locked rows */
  public static final int BOTTOM_GRID = 2;

  /** mouse listener applied to tabler headers to sort columns */
  private MouseListener sortingMouseListener = null;

  /** vertical scrollbar */
  private PaginationVerticalScrollbar vScrollbar;

  /** flag used to store a control key pressed event (in READONLY mode) */
  private boolean controlDown = false;

  /** flag used to disable column resizing, column moving and column visibility changing operations */
  private boolean hasColSpan = false;

  /** collection of pairs <column index as Integer,additional column related, as JPanel > */
  private Hashtable headerToAdditionalHeader = new Hashtable();

  /** collection of pairs <additional column related, as JPanel, java.util.List of column index as Integer, i.e. its related column headers > */
  private Hashtable additionalHeaderToHeaders = new Hashtable();

  /** collection of pairs < Pair object, Pair object >, where the former identify a cell, i.e. a [row index,column index] and the latter the cell's span width and height */
  private Hashtable cellSpans = new Hashtable();

  /** flag used to define if background and foreground colors must be setted according to GridController definition only in READONLY mode */
  private boolean colorsInReadOnlyMode = true;

  /** cursor to show on dragging */
  private Cursor dragCursor = null;

  /** search window manager */
  private SearchWindowManager searchWindowManager = null;

  private RightClickMouseListener rightClickMouseListener;

  /** attribute name of the column declared as expandable, i.e. user can click on it to expand cell to show an inner component; default value: 0 (first column) */
  private String expandableColumnAttributeName = null;

  /** define whether expanded rows in the past must be collapsed when expanding the current one; used only when "overwriteRowWhenExpanding" property is not null; default value: <code>false</code> */
  private boolean singleExpandableRow = false;

  /** define whether the row to show, when expanding the current one, must be showed over the current one on in a new row below it; used only when "overwriteRowWhenExpanding" property is not null; default value: <code>false</code> i.e. do not overwrite it*/
  private boolean overwriteRowWhenExpanding = false;

  /** define the controller that manages row expansion */
  private ExpandableRowController expandableRowController = null;

  /** expandable cell renderer */
  private ExpandableRenderer expandableRenderer = null;


  /**
   * Costructor called by GridControl: programmer never called directly this class.
   * @param colProps TableModel column properties (name, type, etc.)
   * @param statusPanel bottom panel included into the grid; used to view selected row numbers
   * @param fromColIndex column index from which to start to add TableColumn (related to colProps list)
   * @param toColIndex final column index of TableColumn objects to add (related to colProps list), index excluded
   * @param colorsInReadOnlyMode flag used to define if background and foreground colors must be setted according to GridController definition only in READONLY mode
   * @param model grid model
   * @param modelAdapter grid model adapter
   * @param gridController grid controller
   * @param lockedGrid locked grid (optional)
   * @param anchorLastColumn define if last column must be anchored to the right margin
   * @param gridType type of grid; possible values: Grid.MAIN_GRID, Grid.TOP_GRID, Grid.BOTTOM_GRID
   */
  public Grid(
      Grids grids,
      Column[] colProps,
      GridStatusPanel statusPanel,
      int fromColIndex,
      int toColIndex,
      boolean colorsInReadOnlyMode,
      VOListTableModel model,
      VOListAdapter modelAdapter,
      GridController gridController,
      boolean lockedGrid,
      boolean anchorLastColumn,
      int expandableColumn,
      boolean singleExpandableRow,
      boolean overwriteRowWhenExpanding,
      ExpandableRowController expandableRowController,
      int gridType) {
    super();
    this.grids = grids;
    this.modelAdapter = modelAdapter;
    this.model = model;
    this.colProps = colProps;
    this.statusPanel = statusPanel;
    this.fromColIndex = fromColIndex;
    this.toColIndex = toColIndex;
    this.colorsInReadOnlyMode = colorsInReadOnlyMode;
    this.gridController = gridController;
    this.lockedGrid = lockedGrid;
    this.singleExpandableRow = singleExpandableRow;
    this.overwriteRowWhenExpanding = overwriteRowWhenExpanding;
    this.expandableRowController = expandableRowController;
    this.setShowGrid(true);
    if (!anchorLastColumn)
      this.setAutoResizeMode(this.AUTO_RESIZE_OFF);
    this.setRowHeight(ClientSettings.CELL_HEIGHT);
    this.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
    this.setSurrendersFocusOnKeystroke(true);
    this.setBackground(ClientSettings.GRID_CELL_BACKGROUND);
    this.setForeground(ClientSettings.GRID_CELL_FOREGROUND);
    this.setSelectionBackground(ClientSettings.GRID_SELECTION_BACKGROUND);
    this.setSelectionForeground(ClientSettings.GRID_SELECTION_FOREGROUND);
    this.gridType = gridType;

    if (expandableColumn>=0)
      this.expandableColumnAttributeName = colProps[expandableColumn].getColumnName();


    try {
      this.setModel(model);

      setUI(new GridUI());

      dragCursor = Toolkit.getDefaultToolkit().createCustomCursor(
        ClientUtils.getImage("drag.gif"),
        new Point(15, 10),
        ClientSettings.getInstance().getResources().getResource("drag")
      );

      // set column types and column renderers/editors...
      prepareJTable();

      checkColumnSpans();

      // storing of TableColumn objects to the purpouse of reuse them in setVisibleColumns method...
      TableColumnModel colsModel = this.getColumnModel();
      tableColumnModel = new TableColumn[colsModel.getColumnCount()];
      for(int i=0;i<colsModel.getColumnCount();i++)
        tableColumnModel[i] = colsModel.getColumn(i);

      // set columns visibility...
      setVisibleColumns();

      // add mouse listener to capture righe mouse click event, used to show the popup menu...
      if (gridType==MAIN_GRID) {
        if (rightClickMouseListener==null)
          rightClickMouseListener = new RightClickMouseListener();

        this.addKeyListener(new KeyAdapter() {

          /**
           * Invoked when a key has been typed.
           * This event occurs when a key press is followed by a key release.
           */
          public void keyTyped(KeyEvent e) {
            if (Grid.this.grids.getCurrentNestedComponent()!=null) {
              Grid.this.grids.getCurrentNestedComponent().dispatchEvent(e);
              e.consume();
            }
          }

          /**
           * Invoked when a key has been pressed.
           */
          public void keyPressed(KeyEvent e) {
            if (Grid.this.grids.getCurrentNestedComponent()!=null) {
              Grid.this.grids.getCurrentNestedComponent().dispatchEvent(e);
              e.consume();
            }
          }

          /**
           * Invoked when a key has been released.
           */
          public void keyReleased(KeyEvent e) {
            if (Grid.this.grids.getCurrentNestedComponent()!=null) {
              Grid.this.grids.getCurrentNestedComponent().dispatchEvent(e);
              e.consume();
            }
          }

        });

        // add mouse listener to capture double click event in the selected row...
        MouseListener[] l = this.getMouseListeners();
        for(int i=0;i<l.length;i++)
          removeMouseListener(l[i]);

        this.addMouseListener(new MouseAdapter() {

          public void mousePressed(MouseEvent e) {
            Pair p = rowColumnAtPoint(e.getPoint());
            if (Grid.this.grids!=null &&
                Grid.this.grids.getMode()==Consts.READONLY &&
                Grid.this.expandableRowController!=null &&
                Grid.this.expandableRowController.isRowExpandable(Grid.this.grids.getVOListTableModel(),p.n1) &&
                p.n2>=Grid.this.modelAdapter.getFieldIndex(expandableColumnAttributeName) &&
                Grid.this.grids.isRowExpanded(p.n1)) {
                // force editing for expanded row
                Component c = Grid.this.grids.getComponentInCache(p.n1);

                if (c!=null) {
                  int row = rowAtPoint(e.getPoint());
                  int y = getRowHeight();
                  for(int i=0;i<row;i++)
                    y += getRowHeight(i);

                  int x = 13;
                  int expandableColIndex = Grid.this.modelAdapter.getFieldIndex(expandableColumnAttributeName);
                  for(int i=0;i<expandableColIndex;i++)
                    x += getColumnModel().getColumn(i).getWidth();

                  if (c.getParent()==null) {
                    ((JComponent)e.getSource()).remove(c);
                    ((JComponent)e.getSource()).add(c);

                    c.setBounds(x,y,c.getWidth(),c.getHeight());
                  }

                  x = e.getX()-x;
                  y = e.getY()-y;

                  if (x>=0 && y>=0) {
                    final Component c2 = SwingUtilities.getDeepestComponentAt(c,x,y);
                    if (c2!=null) {
                      Point pp = SwingUtilities.convertPoint(c,x,y,c2);
//                      System.out.println(pp+" "+c2);
                      Grid.this.grids.setCurrentNestedComponent(c2);
                      c2.dispatchEvent(
                        new MouseEvent(
                          c2,
                          e.getID(),
                          e.getWhen(),
                          e.getModifiers(),
                          pp.x,
                          pp.y,
                          e.getClickCount(),
                          e.isPopupTrigger()
                        )
                      );
                      e.consume();
                      return;
//                      c.repaint();
//                      ((Component)e.getSource()).repaint();
                    }
                  }
                }
            }
            Grid.this.grids.setCurrentNestedComponent(null);
          }


//          /**
//           * Invoked when the mouse enters a component.
//           */
//          public void mouseEntered(MouseEvent e) {
//            Pair p = rowColumnAtPoint(e.getPoint());
//            if (Grid.this.grids!=null &&
//                Grid.this.grids.getMode()==Consts.READONLY &&
//                Grid.this.expandableRowController!=null &&
//                Grid.this.expandableRowController.isRowExpandable(Grid.this.grids.getVOListTableModel(),p.n1) &&
//                p.n2>=Grid.this.modelAdapter.getFieldIndex(expandableColumnAttributeName) &&
//                Grid.this.grids.isRowExpanded(p.n1)) {
//                // force editing for expanded row
//                Component c = Grid.this.grids.getComponentInCache(p.n1);
//
//                if (c!=null) {
//                  int row = rowAtPoint(e.getPoint());
//                  int y = getRowHeight();
//                  for(int i=0;i<row;i++)
//                    y += getRowHeight(i);
//
//                  int x = 13;
//                  int expandableColIndex = Grid.this.modelAdapter.getFieldIndex(expandableColumnAttributeName);
//                  for(int i=0;i<expandableColIndex;i++)
//                    x += getColumnModel().getColumn(i).getWidth();
//
//                  if (c.getParent()==null) {
//                    ((JComponent)e.getSource()).remove(c);
//                    ((JComponent)e.getSource()).add(c);
//
//                    c.setBounds(x,y,c.getWidth(),c.getHeight());
//                  }
//
//                  x = e.getX()-x;
//                  y = e.getY()-y;
//
//                  if (x>=0 && y>=0) {
//                    Component c2 = SwingUtilities.getDeepestComponentAt(c,x,y);
//                    if (c2!=null) {
//                      Point pp = SwingUtilities.convertPoint(c,x,y,c2);
//                      c2.dispatchEvent(
//                        new MouseEvent(
//                          c2,
//                          e.getID(),
//                          e.getWhen(),
//                          e.getModifiers(),
//                          pp.x,
//                          pp.y,
//                          e.getClickCount(),
//                          e.isPopupTrigger()
//                        )
//                      );
//                      e.consume();
//                      return;
//                    }
//                  }
//                }
//            }
//            Grid.this.grids.setCurrentNestedComponent(null);
//          }
//
//          /**
//           * Invoked when the mouse exits a component.
//           */
//          public void mouseExited(MouseEvent e) {
//            Pair p = rowColumnAtPoint(e.getPoint());
//            if (Grid.this.grids!=null &&
//                Grid.this.grids.getMode()==Consts.READONLY &&
//                Grid.this.expandableRowController!=null &&
//                Grid.this.expandableRowController.isRowExpandable(Grid.this.grids.getVOListTableModel(),p.n1) &&
//                p.n2>=Grid.this.modelAdapter.getFieldIndex(expandableColumnAttributeName) &&
//                Grid.this.grids.isRowExpanded(p.n1)) {
//                // force editing for expanded row
//                Component c = Grid.this.grids.getComponentInCache(p.n1);
//
//                if (c!=null) {
//                  int row = rowAtPoint(e.getPoint());
//                  int y = getRowHeight();
//                  for(int i=0;i<row;i++)
//                    y += getRowHeight(i);
//
//                  int x = 13;
//                  int expandableColIndex = Grid.this.modelAdapter.getFieldIndex(expandableColumnAttributeName);
//                  for(int i=0;i<expandableColIndex;i++)
//                    x += getColumnModel().getColumn(i).getWidth();
//
//                  if (c.getParent()==null) {
//                    ((JComponent)e.getSource()).remove(c);
//                    ((JComponent)e.getSource()).add(c);
//
//                    c.setBounds(x,y,c.getWidth(),c.getHeight());
//                  }
//
//                  x = e.getX()-x;
//                  y = e.getY()-y;
//
//                  if (x>=0 && y>=0) {
//                    Component c2 = SwingUtilities.getDeepestComponentAt(c,x,y);
//                    if (c2!=null) {
//                      Point pp = SwingUtilities.convertPoint(c,x,y,c2);
//                      c2.dispatchEvent(
//                        new MouseEvent(
//                          c2,
//                          e.getID(),
//                          e.getWhen(),
//                          e.getModifiers(),
//                          pp.x,
//                          pp.y,
//                          e.getClickCount(),
//                          e.isPopupTrigger()
//                        )
//                      );
//                      e.consume();
//                      return;
//                    }
//                  }
//                }
//            }
//            Grid.this.grids.setCurrentNestedComponent(null);
//          }


          public void mouseReleased(MouseEvent e) {
            Pair p = rowColumnAtPoint(e.getPoint());
            if (Grid.this.grids!=null &&
                Grid.this.grids.getMode()==Consts.READONLY &&
                Grid.this.expandableRowController!=null &&
                Grid.this.expandableRowController.isRowExpandable(Grid.this.grids.getVOListTableModel(),p.n1) &&
                p.n2>=Grid.this.modelAdapter.getFieldIndex(expandableColumnAttributeName) &&
                Grid.this.grids.isRowExpanded(p.n1)) {
                // force editing for expanded row
                Component c = Grid.this.grids.getComponentInCache(p.n1);

                if (c!=null) {
                  int row = rowAtPoint(e.getPoint());
                  int y = getRowHeight();
                  for(int i=0;i<row;i++)
                    y += getRowHeight(i);

                  int x = 13;
                  int expandableColIndex = Grid.this.modelAdapter.getFieldIndex(expandableColumnAttributeName);
                  for(int i=0;i<expandableColIndex;i++)
                    x += getColumnModel().getColumn(i).getWidth();

                  if (c.getParent()==null) {
                    ((JComponent)e.getSource()).remove(c);
                    ((JComponent)e.getSource()).add(c);

                    c.setBounds(x,y,c.getWidth(),c.getHeight());
                  }

                  x = e.getX()-x;
                  y = e.getY()-y;

                  if (x>=0 && y>=0) {
                    final Component c2 = SwingUtilities.getDeepestComponentAt(c,x,y);
                    if (c2!=null) {
                      Point pp = SwingUtilities.convertPoint(c,x,y,c2);
                      c2.dispatchEvent(
                        new MouseEvent(
                          c2,
                          e.getID(),
                          e.getWhen(),
                          e.getModifiers(),
                          pp.x,
                          pp.y,
                          e.getClickCount(),
                          e.isPopupTrigger()
                        )
                      );
                      e.consume();
                      c.repaint();
                      ((Component)e.getSource()).repaint();
//                      SwingUtilities.invokeLater(new Runnable() {
//                        public void run() {
//                          c2.requestFocus();
//                        }
//                      });

                      return;
                    }
                  }
                }
            }
            Grid.this.grids.setCurrentNestedComponent(null);
          }


          public void mouseClicked(MouseEvent e) {
            Pair p = rowColumnAtPoint(e.getPoint());
            if (Grid.this.grids!=null &&
                Grid.this.grids.getMode()==Consts.READONLY &&
                Grid.this.expandableRowController!=null &&
                Grid.this.expandableRowController.isRowExpandable(Grid.this.grids.getVOListTableModel(),p.n1) &&
                p.n2>=Grid.this.modelAdapter.getFieldIndex(expandableColumnAttributeName) &&
                Grid.this.grids.isRowExpanded(p.n1)) {
                // force editing for expanded row
                Component c = Grid.this.grids.getComponentInCache(p.n1);

                if (c!=null) {
                  int row = rowAtPoint(e.getPoint());
                  int y = getRowHeight();
                  for(int i=0;i<row;i++)
                    y += getRowHeight(i);

                  int x = 13;
                  int expandableColIndex = Grid.this.modelAdapter.getFieldIndex(expandableColumnAttributeName);
                  for(int i=0;i<expandableColIndex;i++)
                    x += getColumnModel().getColumn(i).getWidth();

                  if (c.getParent()==null) {
                    ((JComponent)e.getSource()).remove(c);
                    ((JComponent)e.getSource()).add(c);

                    c.setBounds(x,y,c.getWidth(),c.getHeight());
                  }

                  x = e.getX()-x;
                  y = e.getY()-y;

                  if (x>=0 && y>=0) {
                    Component c2 = SwingUtilities.getDeepestComponentAt(c,x,y);
                    if (c2!=null) {
                      Point pp = SwingUtilities.convertPoint(c,x,y,c2);
                      c2.dispatchEvent(
                        new MouseEvent(
                          c2,
                          e.getID(),
                          e.getWhen(),
                          e.getModifiers(),
                          pp.x,
                          pp.y,
                          e.getClickCount(),
                          e.isPopupTrigger()
                        )
                      );
                      e.consume();
                      c.repaint();
                      ((Component)e.getSource()).repaint();
                      return;
                    }
                  }
                }
            }
            Grid.this.grids.setCurrentNestedComponent(null);

            if (e.getClickCount()==2 &&
                       SwingUtilities.isLeftMouseButton(e) &&
                       Grid.this.gridController!=null &&
                       getSelectedRow()!=-1 &&
                       Grid.this.model.getMode()==Consts.READONLY)
              // call grid controller method on another thread
              new Thread() {
                public void run() {
                  Grid.this.gridController.doubleClick(getSelectedRow(),Grid.this.model.getObjectForRow(getSelectedRow()));
                }
              }.start();
            else if (e.getClickCount()==1 &&
                     SwingUtilities.isLeftMouseButton(e) &&
                     Grid.this.gridController!=null &&
                     Grid.this.model.getMode()==Consts.READONLY &&
                     Grid.this.grids.getNavBar()!=null)
              Grid.this.grids.getNavBar().fireButtonPressedEvent(NavigatorBar.LEFT_MOUSE_BUTTON);
          }
        });


        for(int i=0;i<l.length;i++)
          addMouseListener(l[i]);


        this.addMouseListener(rightClickMouseListener);

        // add key listener to capture the ENTER pressed event in the selected row...
        this.addKeyListener(new KeyAdapter() {
          public void keyPressed(KeyEvent e) {

            if (!Grid.this.grids.isListenEvent()) {
              e.consume();
              return;
            }

            if (getMode()==Consts.READONLY)
              controlDown = e.isControlDown();
            else
              controlDown = false;

            // ENTER button pressed in the selected row: fire event to the grid controller...
            if (e.getKeyCode()==e.VK_ENTER &&
                Grid.this.gridController!=null &&
                getSelectedRow()!=-1 &&
                Grid.this.model.getMode()==Consts.READONLY)
              // call grid controller method on another thread
              new Thread() {
                public void run() {
                  Grid.this.gridController.enterButton(getSelectedRow(),Grid.this.model.getObjectForRow(getSelectedRow()));
                }
              }.start();

            // ENTER button pressed in the selected row: consule event (otherwise the grid will select the next row)...
            if (e.getKeyCode()==e.VK_ENTER)
              e.consume();

              // accelerator key pressed...
            if (e.getKeyCode()==ClientSettings.RELOAD_BUTTON_KEY.getKeyCode() &&
                e.getModifiers()+e.getModifiersEx()==ClientSettings.RELOAD_BUTTON_KEY.getModifiers() &&
                Grid.this.grids.getReloadButton()!=null &&
                Grid.this.grids.getReloadButton().isEnabled())
              Grid.this.grids.reload();
            else if (e.getKeyCode()==ClientSettings.FILTER_BUTTON_KEY.getKeyCode() &&
                     e.getModifiers()+e.getModifiersEx()==ClientSettings.FILTER_BUTTON_KEY.getModifiers() &&
                     Grid.this.grids.getFilterButton()!=null &&
                     Grid.this.grids.getFilterButton().isEnabled())
              Grid.this.grids.filterSort();
            else if (e.getKeyCode()==ClientSettings.SAVE_BUTTON_KEY.getKeyCode() &&
                     e.getModifiers()+e.getModifiersEx()==ClientSettings.SAVE_BUTTON_KEY.getModifiers() &&
                     Grid.this.grids.getSaveButton()!=null &&
                     Grid.this.grids.getSaveButton().isEnabled())
              Grid.this.grids.save();
            else if (e.getKeyCode()==ClientSettings.INSERT_BUTTON_KEY.getKeyCode() &&
                     e.getModifiers()+e.getModifiersEx()==ClientSettings.INSERT_BUTTON_KEY.getModifiers() &&
                     Grid.this.grids.getInsertButton()!=null &&
                     Grid.this.grids.getInsertButton().isEnabled())
              Grid.this.grids.insert();
            else if (e.getKeyCode()==ClientSettings.EDIT_BUTTON_KEY.getKeyCode() &&
                     e.getModifiers()+e.getModifiersEx()==ClientSettings.EDIT_BUTTON_KEY.getModifiers() &&
                     Grid.this.grids.getEditButton()!=null &&
                     Grid.this.grids.getEditButton().isEnabled())
              Grid.this.grids.edit();
            else if (e.getKeyCode()==ClientSettings.DELETE_BUTTON_KEY.getKeyCode() &&
                     e.getModifiers()+e.getModifiersEx()==ClientSettings.DELETE_BUTTON_KEY.getModifiers() &&
                     Grid.this.grids.getDeleteButton()!=null &&
                     Grid.this.grids.getDeleteButton().isEnabled())
              Grid.this.grids.delete();
            else if (e.getKeyCode()==ClientSettings.COPY_BUTTON_KEY.getKeyCode() &&
                     e.getModifiers()+e.getModifiersEx()==ClientSettings.COPY_BUTTON_KEY.getModifiers() &&
                     Grid.this.grids.getCopyButton()!=null &&
                Grid.this.grids.getCopyButton().isEnabled())
              Grid.this.grids.copy();
            else if (e.getKeyCode()==ClientSettings.EXPORT_BUTTON_KEY.getKeyCode() &&
                     e.getModifiers()+e.getModifiersEx()==ClientSettings.EXPORT_BUTTON_KEY.getModifiers() &&
                     Grid.this.grids.getExportButton()!=null &&
                Grid.this.grids.getExportButton().isEnabled())
              Grid.this.grids.export();
            else if (e.getKeyCode()==ClientSettings.IMPORT_BUTTON_KEY.getKeyCode() &&
                     e.getModifiers()+e.getModifiersEx()==ClientSettings.IMPORT_BUTTON_KEY.getModifiers() &&
                     Grid.this.grids.getImportButton()!=null &&
                Grid.this.grids.getImportButton().isEnabled())
              Grid.this.grids.importData();
            else if (e.getKeyCode()==ClientSettings.GRID_POPUP_KEY.getKeyCode() &&
                     e.getModifiers()+e.getModifiersEx()==ClientSettings.GRID_POPUP_KEY.getModifiers() &&
                     getColumnModel().getColumnCount()>0) {
              if (getSelectedColumn()==-1)
                setColumnSelectionInterval(0,0);
              if (getSelectedRow()==-1 && getRowCount()>0)
                setRowSelectionInterval(0,0);
              int row = getSelectedRow();
              if (row==-1)
                row = 0;
              int x = 0;
              for(int i=0;i<getSelectedColumn();i++)
                x += getColumnModel().getColumn(i).getWidth();
              showPopupMenu(
                  x+getColumnModel().getColumn(getSelectedColumn()).getWidth()/2,
                  calcHeightToRow(row)+getRowHeight(row)/2
              );
            }

            // (SHIFT) TAB pressed ...
            if (Grid.this.grids.getLockedGrid()!=null &&
                Grid.this.model.getRowCount()>0 &&
                getSelectedRow()!=-1 &&
                (e.getKeyCode()==e.VK_TAB || e.getKeyCode()==e.VK_RIGHT) &&
                e.getModifiers()!=e.SHIFT_MASK &&
                Grid.this.lockedGrid &&
                getSelectedColumn()==getColumnCount()-1) {
              // TAB pressed...
              e.consume();
              Grid.this.grids.setRowSelectionInterval(getSelectedRow(),getSelectedRow());
              Grid.this.grids.getGrid().setColumnSelectionInterval(0,0);
  //            ensureRowIsVisible(getSelectedRow());
              Grid.this.grids.getGrid().requestFocus();
            }
            else if (Grid.this.grids.getLockedGrid()!=null &&
                Grid.this.model.getRowCount()>0 &&
                getSelectedRow()<Grid.this.model.getRowCount()-1 &&
                (e.getKeyCode()==e.VK_TAB || e.getKeyCode()==e.VK_RIGHT) &&
                e.getModifiers()!=e.SHIFT_MASK &&
                !Grid.this.lockedGrid &&
                getSelectedColumn()==getColumnCount()-1) {
              // TAB pressed...
              e.consume();
              try {
                Grid.this.grids.setRowSelectionInterval(getSelectedRow() + 1, getSelectedRow() + 1);
              }
              catch (Exception ex) {
              }
              Grid.this.grids.getLockedGrid().setColumnSelectionInterval(0,0);
  //            ensureRowIsVisible(getSelectedRow());
              Grid.this.grids.getLockedGrid().requestFocus();
            }
            else if (Grid.this.grids.getLockedGrid()!=null &&
                Grid.this.model.getRowCount()>0 &&
                getSelectedRow()>0 &&
                (e.getModifiers()==e.SHIFT_MASK && e.getKeyCode()==e.VK_TAB || e.getKeyCode()==e.VK_LEFT) &&
                Grid.this.lockedGrid &&
                getSelectedColumn()==0) {
              // TAB pressed...
              e.consume();
              try {
                Grid.this.grids.getGrid().setColumnSelectionInterval(Grid.this.grids.getGrid().getColumnCount()-1,Grid.this.grids.getGrid().getColumnCount()-1);
                Grid.this.grids.setRowSelectionInterval(getSelectedRow()-1, getSelectedRow()-1);
  //              ensureRowIsVisible(getSelectedRow()-1);
              }
              catch (Exception ex1) {
              }
              Grid.this.grids.getGrid().requestFocus();
            }
            else if (Grid.this.grids.getLockedGrid()!=null &&
                Grid.this.model.getRowCount()>0 &&
                getSelectedRow()>=0 &&
                (e.getModifiers()==e.SHIFT_MASK && e.getKeyCode()==e.VK_TAB || e.getKeyCode()==e.VK_LEFT) &&
                !Grid.this.lockedGrid &&
                getSelectedColumn()==0) {
              // TAB pressed...
              e.consume();
              Grid.this.grids.setRowSelectionInterval(getSelectedRow(), getSelectedRow());
              Grid.this.grids.getLockedGrid().setColumnSelectionInterval(Grid.this.grids.getLockedGrid().getColumnCount()-1,Grid.this.grids.getLockedGrid().getColumnCount()-1);
              Grid.this.grids.getLockedGrid().requestFocus();
            }
            else if (Grid.this.grids.getLockedGrid()!=null &&
                Grid.this.model.getRowCount()>0 &&
                getSelectedRow()>=0 &&
                (e.getModifiers()==e.SHIFT_MASK && e.getKeyCode()==e.VK_TAB || e.getKeyCode()==e.VK_LEFT) &&
                !Grid.this.lockedGrid &&
                getSelectedColumn()>0) {
              // TAB pressed...
              Grid.this.grids.setRowSelectionInterval(getSelectedRow(), getSelectedRow());
            } else



            // (SHIFT) TAB pressed ...
            if (getMode()!=Consts.READONLY) {
              if (Grid.this.model.getRowCount()>0 &&
                  getSelectedRow()!=-1 &&
                  e.getKeyCode()==e.VK_TAB &&
                  e.getModifiers()!=e.SHIFT_MASK) {
                // TAB pressed and grid is not in read only mode...
                tabPressed(e);
              }
              else if (Grid.this.model.getRowCount()>0 &&
                       getSelectedRow()!=-1 &&
                       e.getKeyCode()==e.VK_TAB &&
                       e.getModifiers()==e.SHIFT_MASK) {
                // SHIFT+TAB pressed and grid is not in read only mode...
                shiftTabPressed(e);
              }
              else if (getMode()==Consts.INSERT &&
                       e.getKeyCode()==e.VK_DOWN &&
                       getSelectedRow()!=-1 &&
                       getSelectedRow()+1<Grid.this.grids.getMaxNumberOfRowsOnInsert() &&
                       getSelectedRow()+1==Grid.this.grids.getCurrentNumberOfNewRows()) {
                // user has pressed DOWN key and the grid is in INSERT mode and
                // the current selected row is the last inserted row and
                // grid is allowed to insert a new row (<max number of rows on insert...)
                Grid.this.grids.setCurrentNumberOfNewRows( Grid.this.grids.getCurrentNumberOfNewRows()+1 );

                // create a new v.o. and add it to the grid model...
                ValueObject vo = null;
                try {
                  vo = (ValueObject)Grid.this.model.getValueObjectType().getConstructor(new Class[0]).newInstance(new Object[0]);
                  Grid.this.model.insertObjectAt(vo,Grid.this.grids.getCurrentNumberOfNewRows()-1);
                  Grid.this.model.getChangedRowIndexes().add(new Integer(Grid.this.grids.getCurrentNumberOfNewRows()-1));
                }
                catch (Exception ex) {
                  ex.printStackTrace();
                }
                catch (Error er) {
                  er.printStackTrace();
                }

                try {
                  // fire create v.o. event to the grid controller: used to fill in the v.o. with default values...
                  Grid.this.gridController.createValueObject(vo);

                  // check if there exists a combo-box editable column to pre-set...
                  for(int i=0;i<Grid.this.colProps.length;i++)
                    if (Grid.this.colProps[i] instanceof ComboColumn &&
                        !((ComboColumn)Grid.this.colProps[i]).isNullAsDefaultValue())
                      Grid.this.model.setValueAt(
                        ((ComboColumn)Grid.this.colProps[i]).getDomain().getDomainPairList()[0].getCode(),
                        Grid.this.grids.getCurrentNumberOfNewRows()-1,
                        Grid.this.model.findColumn(((ComboColumn)Grid.this.colProps[i]).getColumnName())
                      );

                }
                catch (Throwable ex) {
                  Logger.error(this.getClass().getName(),"modeChanged","Error while constructing value object '"+Grid.this.model.getValueObjectType().getName()+"'",ex);
                }
                setRowSelectionInterval(Grid.this.grids.getCurrentNumberOfNewRows()-1,0);
                setColumnSelectionInterval(0,0);
                ensureRowIsVisible(Grid.this.grids.getCurrentNumberOfNewRows()-1);

                if (Grid.this.grids.getLockedGrid()!=null) {
                  Grid.this.grids.getLockedGrid().setRowSelectionInterval(Grid.this.grids.getCurrentNumberOfNewRows()-1,0);
                  Grid.this.grids.getLockedGrid().setColumnSelectionInterval(Grid.this.grids.getCurrentNumberOfNewRows()-1,0);
                  Grid.this.grids.getLockedGrid().ensureRowIsVisible(Grid.this.grids.getCurrentNumberOfNewRows()-1);
                  Grid.this.grids.getLockedGrid().editCellAt(Grid.this.grids.getCurrentNumberOfNewRows()-1,0);
                  Grid.this.grids.getLockedGrid().requestFocus();
                }
                else {
                  editCellAt(Grid.this.grids.getCurrentNumberOfNewRows()-1, 0);
                  requestFocus();
                }

                e.consume();
              }

              else if (getMode()==Consts.INSERT &&
                       e.getKeyCode()==e.VK_UP &&
                       getSelectedRow()>0 &&
                       getSelectedRow()+1==Grid.this.grids.getCurrentNumberOfNewRows()) {
                // user has pressed UP key and the grid is in INSERT mode and
                // the current selected row is NOT the first inserted and
                // the selected row is the last inserted
                // then check if the selected row's value object is null
                // if it's null, then remove the related row from the grid...

                try {
                  ValueObject voToRemove = Grid.this.model.getObjectForRow(getSelectedRow());
                  ValueObject clonedVO = (ValueObject) voToRemove.getClass().newInstance();
                  Grid.this.gridController.createValueObject(clonedVO);
                  boolean notNullValueFound = false;
                  Object value = null;
                  Object clonedValue = null;
                  for(int i=0;i<Grid.this.model.getColumnCount();i++) {
                    value = Grid.this.model.getValueAt(getSelectedRow(),i);
                    if (value!=null) {
                      // check if the value is the "default" value setted by createValueObject method,
                      // by comparing "cloned v.o." attribute with "v.o. to remove" attribute
                      clonedValue = clonedVO.getClass().getMethod("get"+Grid.this.model.getColumnName(i).toUpperCase().charAt(0)+Grid.this.model.getColumnName(i).substring(1),new Class[0]).invoke(clonedVO,new Object[0]);
                      if (!value.equals(clonedValue) &&
                          !(Grid.this.colProps[i] instanceof ComboColumn && !((ComboColumn)Grid.this.colProps[i]).isNullAsDefaultValue()) &&
                          !(value.equals("") && clonedValue==null)
                      ) {
                        notNullValueFound = true;
                        break;
                      }
                    }
                  }
                  if (!notNullValueFound) {
                    Grid.this.grids.setCurrentNumberOfNewRows( Grid.this.grids.getCurrentNumberOfNewRows()-1 );
                    Grid.this.model.removeObjectAt(Grid.this.grids.getCurrentNumberOfNewRows());
                    Grid.this.model.getChangedRowIndexes().remove(Grid.this.model.getChangedRowIndexes().size()-1);
                    setRowSelectionInterval(Grid.this.grids.getCurrentNumberOfNewRows()-1,0);
                    setColumnSelectionInterval(0,0);
                    ensureRowIsVisible(Grid.this.grids.getCurrentNumberOfNewRows()-1);

                    if (Grid.this.grids.getLockedGrid()!=null) {
                      Grid.this.grids.getLockedGrid().setRowSelectionInterval(Grid.this.grids.getCurrentNumberOfNewRows()-1,0);
                      Grid.this.grids.getLockedGrid().setColumnSelectionInterval(Grid.this.grids.getCurrentNumberOfNewRows()-1,0);
                      Grid.this.grids.getLockedGrid().ensureRowIsVisible(Grid.this.grids.getCurrentNumberOfNewRows()-1);
                      Grid.this.grids.getLockedGrid().editCellAt(Grid.this.grids.getCurrentNumberOfNewRows()-1,0);
                      Grid.this.grids.getLockedGrid().requestFocus();
                    }
                    else {
                      editCellAt(Grid.this.grids.getCurrentNumberOfNewRows()-1, 0);
                      requestFocus();
                    }

                    e.consume();
                  }
                }
                catch (Exception ex2) {
                  ex2.printStackTrace();
                }

              }

            } else

            // navigation button pressed...
            if (getMode()==Consts.READONLY &&
                Grid.this.grids.getNavBar()!=null &&
                (e.getKeyCode()==e.VK_UP ||
                 e.getKeyCode()==e.VK_DOWN ||
                 e.getKeyCode()==e.VK_PAGE_UP ||
                 e.getKeyCode()==e.VK_PAGE_DOWN)) {
              if (e.getKeyCode()==e.VK_UP && getSelectedRow()>0)
                  Grid.this.grids.getNavBar().prevButton_actionPerformed(new ActionEvent(this,ActionEvent.ACTION_PERFORMED,NavigatorBar.UP_KEY));
              else if (e.getKeyCode()==e.VK_UP && getSelectedRow()==0 && Grid.this.grids.getNavBar().isPrevButtonEnabled())
                Grid.this.grids.getNavBar().prevButton_actionPerformed(new ActionEvent(this,ActionEvent.ACTION_PERFORMED,NavigatorBar.UP_KEY));
              else if (e.getKeyCode()==e.VK_DOWN && getSelectedRow()<Grid.this.model.getRowCount()-1)
                Grid.this.grids.getNavBar().nextButton_actionPerformed(new ActionEvent(this,ActionEvent.ACTION_PERFORMED,NavigatorBar.DOWN_KEY));
              else if (e.getKeyCode()==e.VK_DOWN && getSelectedRow()==Grid.this.model.getRowCount()-1 && Grid.this.grids.getNavBar().isNextButtonEnabled())
                Grid.this.grids.getNavBar().nextButton_actionPerformed(new ActionEvent(this,ActionEvent.ACTION_PERFORMED,NavigatorBar.DOWN_KEY));
              else if (e.getKeyCode()==e.VK_PAGE_UP && getSelectedRow()>0) {
//                System.out.println(getSelectedRow()+1+" "+Grid.this.grids.getstartIndex()+" "+getRowCount());
                if (Grid.this.grids.getstartIndex()==0) {
                  if (getSelectedRow()+1<getRowCount()) {
                    setRowSelectionInterval(getSelectedRow()+2,getSelectedRow()+2);
                    ensureRowIsVisible(getSelectedRow());
                  }
                  else
                    SwingUtilities.invokeLater(new Runnable() {
                      public void run() {
                        if (getSelectedRow()+1<getRowCount()) {
                            java.awt.Rectangle r = getCellRect(getSelectedRow(), 0, false);
                            r = getCellRect(
                                getSelectedRow()+(r.y+getVisibleRect().height)/getRowHeight(),
                                0,
                                false
                            );
                            scrollRectToVisible(r);
                            setRowSelectionInterval(getSelectedRow()+2,getSelectedRow()+2);
                          }
                        else {
                          setRowSelectionInterval(getSelectedRow()-getVisibleRect().height/getRowHeight()+1,getSelectedRow()-getVisibleRect().height/getRowHeight()+1);
                          ensureRowIsVisible(getSelectedRow());
                        }
                      }
                    });
                }
                else {
                  int calculatedPos,newPos;
                  calculatedPos=getSelectedRow()-getVisibleRect().height/getRowHeight()+1;
                  newPos= calculatedPos<0 ? 0 : calculatedPos;
                  setRowSelectionInterval(newPos,newPos);
//                  setRowSelectionInterval(getSelectedRow()-getVisibleRect().height/getRowHeight()+1,getSelectedRow()-getVisibleRect().height/getRowHeight()+1);
                  ensureRowIsVisible(getSelectedRow());
                }

                if (getSelectedRow()<Grid.this.model.getRowCount()-1 || Grid.this.grids.isMoreRows())
                  Grid.this.grids.getNavBar().setLastRow(false);
                if (getSelectedRow()==0 && Grid.this.grids.getLastIndex()==Grid.this.model.getRowCount()-1)
                  Grid.this.grids.getNavBar().setFirstRow(true);
              }
              else if (e.getKeyCode()==e.VK_PAGE_UP && getSelectedRow()==0 && Grid.this.grids.getNavBar().isPrevButtonEnabled()) {
                if (Grid.this.grids.getLastIndex()==Grid.this.model.getRowCount() -1)
                  Grid.this.grids.getNavBar().setFirstRow(true);
                else
                  Grid.this.grids.getNavBar().prevButton_actionPerformed(new ActionEvent(this,ActionEvent.ACTION_PERFORMED,NavigatorBar.UP_KEY));
              } else if (e.getKeyCode()==e.VK_PAGE_DOWN && getSelectedRow()<Grid.this.model.getRowCount()-1 && Grid.this.grids.isMoreRows()) {
                java.awt.Rectangle r = getCellRect(getSelectedRow(), 0, false);
//                System.out.println("r.y="+r.y+" getVisibleRect().height="+getVisibleRect().height+" getRowHeight()="+getRowHeight()+" getRowMargin()="+getRowMargin());
                int delta = (r.y+getVisibleRect().height)/(getRowHeight())-1;
                setRowSelectionInterval(Math.min(Grid.this.model.getRowCount()-1,delta),Math.min(Grid.this.model.getRowCount()-1,delta));
                ensureRowIsVisible(getSelectedRow());
                if (getSelectedRow()>0)
                  Grid.this.grids.getNavBar().setFirstRow(false);
                if (getSelectedRow()==Grid.this.model.getRowCount()-1 && !Grid.this.grids.isMoreRows())
                  Grid.this.grids.getNavBar().setLastRow(true);
              }
              else if (e.getKeyCode()==e.VK_PAGE_DOWN && getSelectedRow()==Grid.this.model.getRowCount()-1 && Grid.this.grids.getNavBar().isNextButtonEnabled()) {
                if (!Grid.this.grids.isMoreRows())
                  Grid.this.grids.getNavBar().setLastRow(true);
                else
                  Grid.this.grids.getNavBar().nextButton_actionPerformed(null);
              }
              if (!(e.getKeyCode()==e.VK_PAGE_DOWN && !Grid.this.grids.isMoreRows()) &&
                  !(e.getKeyCode()==e.VK_PAGE_UP && Grid.this.grids.getstartIndex()==0))
                e.consume();
            }
            else if (Grid.this.lockedGrid && getMode()==Consts.READONLY && Grid.this.grids.getNavBar()==null &&
                (e.getKeyCode()==e.VK_PAGE_UP ||
                 e.getKeyCode()==e.VK_PAGE_DOWN)) {
              if (e.getKeyCode()==e.VK_PAGE_UP && getSelectedRow()>0) {
                java.awt.Rectangle r = getCellRect(getSelectedRow(), 0, false);
                int delta = (r.y-getVisibleRect().height)/(getRowHeight()+getRowMargin())+1;
                setRowSelectionInterval(Math.max(0,delta),Math.max(0,delta));
                ensureRowIsVisible(getSelectedRow());
              } else if (e.getKeyCode()==e.VK_PAGE_DOWN && getSelectedRow()<Grid.this.model.getRowCount()-1) {
                java.awt.Rectangle r = getCellRect(getSelectedRow(), 0, false);
                int delta = (r.y+getVisibleRect().height)/(getRowHeight()+getRowMargin());
                setRowSelectionInterval(Math.min(Grid.this.model.getRowCount()-1,delta),Math.min(Grid.this.model.getRowCount()-1,delta));
                ensureRowIsVisible(getSelectedRow());
              }
              e.consume();
            }

          }


          public void keyReleased(KeyEvent e) {
            if (getMode()==Consts.READONLY)
              controlDown = e.isControlDown();
            else
              controlDown = false;
          }

        });

      } // end if on gridType==MAIN_GRID...


      if (gridType==TOP_GRID) {
        // disable row selection for top grid...
        setRowSelectionAllowed(false);
        setCellSelectionEnabled(true);
        addFocusListener(new FocusAdapter() {
          public void focusLost(FocusEvent e) {
            if (getSelectedRow()!=-1)
              removeRowSelectionInterval(getSelectedRow(),getSelectedRow());
          }
        });
      }


      if (gridType==BOTTOM_GRID) {
        // disable row selection for bottom grid...
        setRowSelectionAllowed(false);
        setCellSelectionEnabled(true);
        addFocusListener(new FocusAdapter() {
          public void focusLost(FocusEvent e) {
            if (getSelectedRow()!=-1)
              removeRowSelectionInterval(getSelectedRow(),getSelectedRow());
          }
        });
      }



      // set column headers...
      prepareHeader();

      if (grids.getGridControl()!=null && !(gridType==MAIN_GRID && grids.getGridControl().getLockedRowsOnTop()==0 || gridType==TOP_GRID)) {
        if (getTableHeader()!=null) {
          getTableHeader().setVisible(false);
          setTableHeader(null);
        }
      }

    }
    catch (Throwable t) {
      Logger.error(this.getClass().getName(),"Grid","Error while constructing grid.",t);
    }
  }


  /**
   * Check additional column header spans.
   */
  private void checkColumnSpans() {
    int rest = 0;
    boolean disableAdditionalHeaders = false;
    hasColSpan = false;
    for(int i=0;i<colProps.length;i++) {
      if (colProps[i].getAdditionalHeaderColumnSpan()>0 && rest>0) {
        disableAdditionalHeaders = true;
        hasColSpan = false;
        Logger.error(this.getClass().getName(), "jbInit", "The specified attribute '"+colProps[i].getColumnName()+"' has an additional column header with col span >0 and incompatibile with previous col spans.",null);
        break;
      }
      else if (colProps[i].getAdditionalHeaderColumnSpan()>0 && rest==0) {
        rest = colProps[i].getAdditionalHeaderColumnSpan() - 1;
        hasColSpan = true;
      }
      else {
        if (rest>0)
          rest--;
      }
    }
    if (disableAdditionalHeaders)
      for(int i=0;i<colProps.length;i++)
        colProps[i].setAdditionalHeaderColumnSpan(0);

  }


  /**
   * Prepare a <code>JTable</code> for use with this model.
   * This method creates the necessary columns in the <code>JTable</code> and assigns
   * the appropriate cell renderers and editors (as provided by the attribute
   * adapter) for each column.
   */
  public final void prepareJTable() {
    TableColumnModel cmodel = this.getColumnModel();
    int cols = cmodel.getColumnCount();

    for(int i = 0; i < cols; i++) {
      TableColumn tc = cmodel.getColumn(i);
      if (i>=fromColIndex && i<toColIndex) {
        tc.setCellRenderer(modelAdapter.getCellRenderer(i));
        tc.setCellEditor(modelAdapter.getCellEditor(i));
      }

      int w = modelAdapter.getFieldPreferredWidth(i);
      if(w > 0)
        tc.setPreferredWidth(w);

      w = modelAdapter.getFieldMinWidth(i);
      if(w > 0)
        tc.setMinWidth(w);

      w = modelAdapter.getFieldMaxWidth(i);
      if(w > 0)
        tc.setMaxWidth(w);
    }
  }


  /**
   * Method redefined to store the current selected cell when user clicks directly in the cell with the mouse.
   */
    public boolean editCellAt(int row, int column, EventObject e) {
      if (grids==null)
        return false;
      if (grids!=null && grids.getMode()==Consts.READONLY) {

//        if (expandableRowController!=null &&
//            expandableRowController.isRowExpandable(grids.getVOListTableModel(),row) &&
//            column>=modelAdapter.getFieldIndex(expandableColumnAttributeName) &&
//            grids.isRowExpanded(row))
//          // force editing for expanded row
//          return true;

        for(int i=0;i<colProps.length;i++)
          if (colProps[i].getColumnType()==Column.TYPE_BUTTON && ((ButtonColumn)colProps[i]).isEnableInReadOnlyMode()) {
            return super.editCellAt(row, column, e);
          }
          else if (colProps[i].getColumnType()==Column.TYPE_CHECK && ((CheckBoxColumn)colProps[i]).isEnableInReadOnlyMode()) {
            return super.editCellAt(row, column, e);
          }

        return false;
      }
      setRowSelectionInterval(row,row);
      setColumnSelectionInterval(column,column);
      return super.editCellAt(row, column, e);
    }


//    /**
//     * Returns the component that is handling the editing session.
//     * If nothing is being edited, returns null.
//     *
//     * @return  Component handling editing session
//     */
//    public Component getEditorComponent() {
//      if (rowClicked!=-1 &&
//          columnClicked!=-1 &&
//          expandableRowController!=null &&
//          expandableRowController.isRowExpandable(grids.getVOListTableModel(),rowClicked) &&
//          columnClicked>=modelAdapter.getFieldIndex(expandableColumnAttributeName) &&
//          grids.isRowExpanded(rowClicked))
//        // force editing for expanded row
//        return grids.getComponentInCache(rowClicked);
//      return super.getEditorComponent();
//    }



    /**
     * Method called when TAB is pressed and grid is not in read only mode...
     */
    private void tabPressed(KeyEvent e) {
      int colIndex = getSelectedColumn();
      if (colIndex==-1) {
          e.consume();
          return;
      }

//      if (lockedGrid && colIndex+1==getColumnCount()) {
//        int row = getSelectedRow();
//        grids.getGrid().setRowSelectionInterval(
//          row>0?row-1:0,
//          row>0?row-1:0
//        );
//        grids.getGrid().setColumnSelectionInterval(0,0);
//        grids.getGrid().ensureRowIsVisible(grids.getGrid().getSelectedRow());
//        grids.getGrid().requestFocus();
//        return;
//      }
//
//      if (!lockedGrid && colIndex+1==getColumnCount() && grids.getLockedGrid()!=null) {
//        grids.getLockedGrid().requestFocus();
//        return;
//      }

      if (colIndex+1==getColumnCount() &&
          getSelectedRow()<model.getRowCount()-1) {
        colIndex = -1;
        setRowSelectionInterval(getSelectedRow()+1,getSelectedRow()+1);
        ensureRowIsVisible(getSelectedRow());
      }
      while(colIndex+1<getColumnCount() &&
            !isCellEditable(getSelectedRow(),colIndex+1)) {
        colIndex++;
      }
      if (colIndex+1<getColumnCount()) {
        ensureRowIsVisible(getSelectedRow());
        setColumnSelectionInterval(colIndex,colIndex);
        setRowSelectionInterval(getSelectedRow(),getSelectedRow());
      } else {
        if (getSelectedRow()<model.getRowCount()-1) {
          setRowSelectionInterval(getSelectedRow()+1,getSelectedRow()+1);
          setColumnSelectionInterval(0,0);
          ensureRowIsVisible(getSelectedRow());
          if (!isCellEditable(getSelectedRow(),0))
            tabPressed(e);
          else
            e.consume();
        }
        else {
          e.consume();
          return;
        }
      }
    }


    /**
     * Method called when SHIFT+TAB are pressed and grid is not in read only mode...
     */
    private void shiftTabPressed(KeyEvent e) {
      int colIndex = getSelectedColumn();
      if (colIndex==-1) {
        e.consume();
        return;
      }

//      if (!lockedGrid && colIndex==0 && grids.getLockedGrid()!=null) {
//        grids.getLockedGrid().setRowSelectionInterval(grids.getLockedGrid().getSelectedRow()+1,grids.getLockedGrid().getSelectedRow()+1);
//        grids.getLockedGrid().setColumnSelectionInterval(grids.getLockedGrid().getColumnCount()-1,grids.getLockedGrid().getColumnCount()-1);
//        grids.getLockedGrid().ensureRowIsVisible(grids.getLockedGrid().getSelectedRow());
//        grids.getLockedGrid().requestFocus();
//        return;
//      }
//
//      if (lockedGrid && colIndex==0) {
//        grids.getGrid().setRowSelectionInterval(grids.getGrid().getSelectedRow()+1,grids.getGrid().getSelectedRow()+1);
//        grids.getGrid().setColumnSelectionInterval(grids.getGrid().getColumnCount()-1,grids.getGrid().getColumnCount()-1);
//        grids.getGrid().ensureRowIsVisible(grids.getGrid().getSelectedRow());
//        grids.getGrid().requestFocus();
//        return;
//      }

      if (colIndex==0 && getSelectedRow()>0) {
        colIndex = getColumnCount()-1;
        setRowSelectionInterval(getSelectedRow()-1,getSelectedRow()-1);
        setColumnSelectionInterval(colIndex,colIndex);
      }
      while(colIndex>0 &&
            !isCellEditable(getSelectedRow(),colIndex-1)) {
        colIndex--;
      }

//      if (lockedGrid && colIndex==0) {
//        grids.getGrid().setRowSelectionInterval(grids.getGrid().getSelectedRow()+1,grids.getGrid().getSelectedRow()+1);
//        grids.getGrid().setColumnSelectionInterval(grids.getGrid().getColumnCount()-1,grids.getGrid().getColumnCount()-1);
//        grids.getGrid().ensureRowIsVisible(grids.getGrid().getSelectedRow());
//        grids.getGrid().requestFocus();
//        return;
//      }

      if (colIndex>=0) {
        setColumnSelectionInterval(colIndex,colIndex);
      }
      else {
        if (getSelectedRow()>0) {

//          if (lockedGrid && colIndex==0) {
//            grids.getGrid().setRowSelectionInterval(grids.getGrid().getSelectedRow()+1,grids.getGrid().getSelectedRow()+1);
//            grids.getGrid().setColumnSelectionInterval(grids.getGrid().getColumnCount()-1,grids.getGrid().getColumnCount()-1);
//            grids.getGrid().ensureRowIsVisible(grids.getGrid().getSelectedRow());
//            grids.getGrid().requestFocus();
//            return;
//          }

          setRowSelectionInterval(getSelectedRow()-1,getSelectedRow()-1);
          setColumnSelectionInterval(colIndex,colIndex);
          shiftTabPressed(e);
        } else {
          e.consume();
          return;
        }
      }
  }


  /**
   * Method called when user click on "previous page" button on the navigation bar:
   * it does clear table model, reload previous data block and select the last row of the block.
   */
  public final void previousPage(NavigatorBar navBar) {
    if (this.getMode()!=Consts.READONLY)
      return;
    vScrollbar.setValue(vScrollbar.getMinimum());
    scrollMov(vScrollbar,NavigatorBar.PREV_PG_BUTTON);
    if (getRowCount()>0)
      setRowSelectionInterval(0,0);

    // fire events related to navigator button pressed...
    if (navBar!=null) {
      navBar.fireButtonPressedEvent(NavigatorBar.PREV_PG_BUTTON);
    }
  }


  /**
   * Method called when user click on "next page" button on the navigation bar:
   * it does clear table model, reload next data block and select the first row of the block.
   * This operations are executed in a separated thread.
   */
  public final void nextPage(NavigatorBar navBar) {
    if (this.getMode()!=Consts.READONLY)
      return;
    vScrollbar.setValue(vScrollbar.getMaximum());
    scrollMov(vScrollbar,NavigatorBar.NEXT_PG_BUTTON);
    if (getRowCount()>0)
      setRowSelectionInterval(getRowCount()-1,getRowCount()-1);

    // fire events related to navigator button pressed...
    if (navBar!=null) {
      navBar.fireButtonPressedEvent(NavigatorBar.NEXT_PG_BUTTON);
    }
  }


  /**
   * Add vertical listener to capture grid vertical scrolling events.
   */
  private void createVerticalScrollBarListener(JScrollPane scrollPane) {
    vScrollbar = new PaginationVerticalScrollbar();
//    JScrollBar vScrollbar = vScrollbar=scrollPane.getVerticalScrollBar();
    scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS );
    vScrollbar.setUnitIncrement(getRowHeight());
    vScrollbar.setBlockIncrement(getRowHeight());
    VerticalScrollBarMouseListener vsListener = new VerticalScrollBarMouseListener();
//    vScrollbar.addMouseListener(vsListener);

    vScrollbar.getDecrButton().addMouseListener(vsListener);

    vScrollbar.getIncrButton().addMouseListener(vsListener);

    if (vScrollbar.getPrevPgButton()!=null)
      vScrollbar.getPrevPgButton().addMouseListener(new MouseAdapter()  {
        public void mousePressed(MouseEvent e) {
          if(!SwingUtilities.isLeftMouseButton(e))
            return;
          previousPage(grids.getGridControl()==null?null:grids.getGridControl().getNavBar());
        }
      });

    if (vScrollbar.getNextPgButton()!=null)
      vScrollbar.getNextPgButton().addMouseListener(new MouseAdapter()  {
        public void mousePressed(MouseEvent e) {
          if(!SwingUtilities.isLeftMouseButton(e))
            return;
          nextPage(grids.getGridControl()==null?null:grids.getGridControl().getNavBar());
        }
      });


    // listener added to intercept the event mouse wheel event finished and repaint grid content
    // grid repainting is needed to ensure that all cell borders are correctly repaints after scrolling grid
    vScrollbar.addAdjustmentListener(new AdjustmentListener() {
      public void adjustmentValueChanged(AdjustmentEvent e) {
        repaint();
      }
    });


    if (gridType==MAIN_GRID)
      scrollPane.setVerticalScrollBar(vScrollbar);
  }


  /**
   * Add mouse listener in vertical scrollbar (to capture mouse click events in scrollbar buttons).
   */
  class VerticalScrollBarMouseListener extends MouseAdapter {

    private int oldValue = -1;

    public void mousePressed(MouseEvent e){
    }


    public void mouseReleased(MouseEvent e){
      if (javax.swing.SwingUtilities.isLeftMouseButton(e)){
        int row1 = getVisibleRect().y/getRowHeight();
        int row2 = getVisibleRect().height/getRowHeight();
        int row3 =  getSelectedRow();
        if (row3<row1)
          row3 = row1;
        else if (row3>row1+row2)
          row3 = row1+row2;
        else if (e.getSource().equals(vScrollbar.getDecrButton()) && row3>0)
          row3 = row3-1;
        else if (e.getSource().equals(vScrollbar.getIncrButton()) && row3+1<getRowCount())
          row3 = row3+1;
        setRowSelectionInterval(row3,row3);
        if (row3<row1 || row3>row1+row2)
          ensureRowIsVisible(row3);
      }
    }

  }


  /**
   * Method called on scrolling grid: grid must fetch a new block of rows.
   * @param pageButton null if no page button has been pressed, NavigatorBar.PREV_PG_BUTTON or NavigatorBar.NEXT_PG_BUTTON otherwise
   */
  private void scrollMov(JScrollBar scrollBar,String pageButton){
    if (getMode()!=Consts.READONLY)
      return;

    if (!grids.isListenEvent())
      return;

    int scrollableZoneSize = scrollBar.getMaximum()-scrollBar.getVisibleAmount();
    if ((scrollBar.getValue() >= scrollableZoneSize/4*3) &&
        grids.isMoreRows() &&
        (pageButton==null || NavigatorBar.NEXT_PG_BUTTON.equals(pageButton))) {
      // scrolling table over 75% of rows (near the end)
      setRowSelectionInterval(model.getRowCount()-1,model.getRowCount()-1);
      grids.nextRow(
        grids.getGridControl()==null?null:grids.getGridControl().getNavBar(),
        new ActionEvent(this,ActionEvent.ACTION_PERFORMED,NavigatorBar.NEXT_PG_BUTTON)
      );
      setRowSelectionInterval(model.getRowCount()/2,model.getRowCount()/2);
      ensureRowIsVisible(getSelectedRow());
    } else if ((scrollBar.getValue() <= scrollableZoneSize/4) &&
               grids.getLastIndex()+1 > model.getRowCount() &&
               (pageButton==null || NavigatorBar.PREV_PG_BUTTON.equals(pageButton))) {
      // scrolling table on 25% of rows (near the beginning)
      setRowSelectionInterval(0,0);
      grids.previousRow(
        grids.getGridControl()==null?null:grids.getGridControl().getNavBar(),
        new ActionEvent(this,ActionEvent.ACTION_PERFORMED,NavigatorBar.PREV_PG_BUTTON)
      );
      setRowSelectionInterval(model.getRowCount()/2,model.getRowCount()/2);
      ensureRowIsVisible(getSelectedRow());
    }
  }


  /**
   * Set column headers.
   */
  private void prepareHeader() {
    for(int i=0;i<tableColumnModel.length;i++) {
      tableColumnModel[i].setHeaderRenderer(new TableColumnHeaderRenderer(
          this.colProps[i].getColumnName(),
          this.colProps[i].getHeaderTextAlignment(),
          this.colProps[i].getHeaderFont(),
          this.colProps[i].getHeaderForegroundColor()
      ));
    }
    JTableHeader th = this.getTableHeader();
    th.setPreferredSize(new Dimension(th.getPreferredSize().width,ClientSettings.HEADER_HEIGHT));
    th.setReorderingAllowed(reorderingAllowed);
    th.setResizingAllowed(resizingAllowed);
    sortingMouseListener = new MouseAdapter() {
      public void mouseClicked(MouseEvent e) {

        // mouse click event is ignored if grid is not in read only mode or is not enabled...
        if(!isEnabled() || model.getMode()!=Consts.READONLY)
          return;

        TableColumnModel columnModel = getColumnModel();
        int viewColumnIndex = columnModel.getColumnIndexAtX(e.getX());
        int modelColumnIndex = convertColumnIndexToModel(viewColumnIndex);


        if(e.getClickCount() == 1 && modelColumnIndex != -1) {
          boolean ok = refreshIconsAndSort(modelColumnIndex);

          if (grids.getGridControl()!=null && ok && gridType==Grid.TOP_GRID) {
            if (lockedGrid)
              grids.getGridControl().getTable().getLockedGrid().getSortingMouseListener().mouseClicked(e);
            else
              grids.getGridControl().getTable().getGrid().getSortingMouseListener().mouseClicked(e);
          }

          return;
        }
      }
    };

    th.addMouseListener(sortingMouseListener);
    setOrderList();
  }


  /**
   * Method called when user clicks on a column header to sort the column:
   * it will control if the specified column is sortable and after that, it will reload data on grid,
   * according to the new sorting settings; sorting icons will be updated.
   * @param modelColumnIndex column model index of the column to sort
   */
  private boolean refreshIconsAndSort(int modelColumnIndex) {
    try {
      Column colProp = colProps[modelColumnIndex];
      if (!colProp.isColumnSortable()) {
        // view warning message ...
        JOptionPane.showMessageDialog(
            ClientUtils.getParentFrame(this),
            ClientSettings.getInstance().getResources().getResource("This column is not sorteable"),
            ClientSettings.getInstance().getResources().getResource("Sorting not allowed"),
            JOptionPane.WARNING_MESSAGE
        );
        return false;
      }

      // (max) sorting order of columns...
      int maxSortingOrder = -1;

      // determine the number of current sorted columns...
      int currentColSorted = 0;
      for(int i=0;i<colProps.length;i++)
        if (colProps[i].isColumnSortable() &&
            !colProps[i].getSortVersus().equals(Consts.NO_SORTED)) {
          currentColSorted++;
          maxSortingOrder = Math.max(maxSortingOrder,colProps[i].getSortingOrder());
        }

      // determine the versus for the specified column (if sorting is applicable)...
      String currentSortVersus = colProp.getSortVersus();
      if (currentSortVersus.equals(Consts.NO_SORTED))
        currentSortVersus = Consts.ASC_SORTED;
      else if (currentSortVersus.equals(Consts.ASC_SORTED))
        currentSortVersus = Consts.DESC_SORTED;
      else if (currentSortVersus.equals(Consts.DESC_SORTED))
        currentSortVersus = Consts.NO_SORTED;

      // determine if it's possible to sort by the specified column:
      if (colProp.getSortVersus().equals(Consts.NO_SORTED) &&
          !currentSortVersus.equals(Consts.NO_SORTED) &&
          currentColSorted==grids.getMaxSortedColumns() &&
          grids.getMaxSortedColumns()==1) {
        // only one column per time is allowed to be sorted and
        // user has clicked on another one column:
        // the previous sorted column will be set NO_SORTED...
        for(int i=0;i<colProps.length;i++)
          if (colProps[i].isColumnSortable() &&
              !colProps[i].getSortVersus().equals(Consts.NO_SORTED)) {
            colProps[i].setSortVersus(Consts.NO_SORTED);
            colProps[i].setSortingOrder(0);
            break;
          }
        maxSortingOrder = 0;
      }
      else if (colProp.getSortVersus().equals(Consts.NO_SORTED) &&
          !currentSortVersus.equals(Consts.NO_SORTED) &&
          currentColSorted>=grids.getMaxSortedColumns()) {
        // view warning message...
        JOptionPane.showMessageDialog(
            ClientUtils.getParentFrame(this),
            ClientSettings.getInstance().getResources().getResource("Maximum number of sorted columns"),
            ClientSettings.getInstance().getResources().getResource("Sorting not applicable"),
            JOptionPane.WARNING_MESSAGE
        );
        return false;
      }

      colProp.setSortVersus(currentSortVersus);
      if (currentSortVersus.equals(Consts.NO_SORTED))
        colProp.setSortingOrder(0);
      else if (colProp.getSortingOrder()==0)
        colProp.setSortingOrder(maxSortingOrder+1);

      // redefine sortingOrder property for each (already) sorted column...
      if (maxSortingOrder!=-1) {
        Integer[] aux = new Integer[Math.max(maxSortingOrder+1,colProps.length+1)];
        for(int i=0;i<colProps.length;i++)
          if (colProps[i].isColumnSortable() &&
              !colProps[i].getSortVersus().equals(Consts.NO_SORTED)) {
            aux[colProps[i].getSortingOrder()] = new Integer(i);
          }
        int pos = 1;
        for(int i=0;i<aux.length;i++)
          if (aux[i]!=null) {
            colProps[aux[i].intValue()].setSortingOrder(pos);
            pos++;
          }
      }

      if (this.getTableHeader()!=null)
        getTableHeader().repaint();
      setOrderList();

      if (orderWithLoadData ||
          grids.getLastIndex()-grids.getVOListTableModel().getRowCount() >0 ||
          grids.isMoreRows()
      )
        grids.reload();
      else
        internalSorting();
      return true;
    }
    catch (Exception ex) {
      Logger.error(this.getClass().getName(),"refreshIconsAndSort","Error while loading data on sorting",ex);
      return false;
    }
  }


  /**
   * Sort the list of value objects already loaded, according to current column sorting settings.
   */
  private void internalSorting() {
    Vector list = getVOListTableModel().getDataVector();
    final Collator collator = Collator.getInstance(grids.getDefaultLocale());

    Collections.sort(list,new Comparator() {

      public int compare(Object o1, Object o2) {
        int colIndex,sign;
        Object val1,val2;
        for(int i=0;i<grids.getCurrentSortedColumns().size();i++) {
          colIndex = modelAdapter.getFieldIndex(grids.getCurrentSortedColumns().get(i).toString());
          val1 = modelAdapter.getField((ValueObject)o1,colIndex);
          val2 = modelAdapter.getField((ValueObject)o2,colIndex);
          sign = grids.getCurrentSortedVersusColumns().get(i).equals(Consts.ASC_SORTED)?+1:-1;
          if (val1==null && val2==null)
            continue;
          else if (val1==null && val2!=null)
            return -1*sign;
          else if (val1!=null && val2==null)
            return +1*sign;
          else {
            if (val1 instanceof java.util.Date) {
              if (((java.util.Date)val1).getTime()<((java.util.Date)val2).getTime())
                return -1*sign;
              else if (((java.util.Date)val1).getTime()>((java.util.Date)val2).getTime())
                return +1*sign;
            }
            else if (val1 instanceof Number) {
              if (((Number)val1).doubleValue()<((Number)val2).doubleValue())
                return -1*sign;
              else if (((Number)val1).doubleValue()>((Number)val2).doubleValue())
                return +1*sign;
            }
            else {
//              if (val1.toString().compareTo(val2.toString())<0)
//                return -1*sign;
//              else if (val1.toString().compareTo(val2.toString())>0)
//                return +1*sign;

              if (collator.compare(val1.toString(),val2.toString())<0)
                return -1*sign;
              else if (collator.compare(val1.toString(),val2.toString())>0)
                return +1*sign;
            }
          }
        }
        return 0;
      }

      public boolean equals(Object obj) {
        return obj.equals(this);
      }

    });
    this.repaint();
    if (grids.getLockedGrid()!=null)
      grids.getLockedGrid().repaint();
  }


  /**
   * Method called when the grid will be view, to correctly initialize columns which are declared sorted.
   */
  private void setOrderList() {
    Integer[] aux = new Integer[colProps.length+1];
    for(int i=0;i<colProps.length;i++)
      if (!colProps[i].getSortVersus().equals(Consts.NO_SORTED) &&
          colProps[i].getSortingOrder()<aux.length)
        aux[colProps[i].getSortingOrder()] = new Integer(i);
//        aux[colProps[i].getSortingOrder()>aux.length?aux.length-1:colProps[i].getSortingOrder()] = new Integer(i);

    grids.getCurrentSortedColumns().clear();
    grids.getCurrentSortedVersusColumns().clear();

    for(int i=0;i<aux.length;i++)
      if (aux[i]!=null) {
        if (colProps[ ( (Integer) aux[i]).intValue()].getSortVersus().equals(Consts.ASC_SORTED)) {
          grids.getCurrentSortedColumns().add(colProps[ ( (Integer) aux[i]).intValue()].getColumnName());
          grids.getCurrentSortedVersusColumns().add(Consts.ASC_SORTED);
        }
        else if (colProps[ ( (Integer) aux[i]).intValue()].getSortVersus().equals(Consts.DESC_SORTED)) {
          grids.getCurrentSortedColumns().add(colProps[ ( (Integer) aux[i]).intValue()].getColumnName());
          grids.getCurrentSortedVersusColumns().add(Consts.DESC_SORTED);
        }
      }
  }


  /**
   * Configure quick filter.
   */
  private void configQuickFilter(int x,int y) {
    int modelColIndex = this.getSelectedColumn()==-1 ? -1 : this.getColumnModel().getColumn(this.getSelectedColumn()).getModelIndex();

    if (modelColIndex==-1) {
      Point tablexy=this.getLocationOnScreen();
      Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
      int xOverflow = x+(int)tablexy.getX()+grids.getPopup().getWidth()-(int)screenSize.getWidth();
      int popupX = xOverflow>0?x-xOverflow-20:x;
      modelColIndex = this.getColumnModel().getColumnIndexAtX(popupX);
    }

    if (modelColIndex!=-1 &&
        colProps[modelColIndex].isColumnFilterable()) {// &&
//        colProps[modelColIndex].getColumnType()!=Column.TYPE_CHECK) {
      int filterType=grids.getFilterPanel().FILTER_TYPE_VALUE;
      switch (colProps[modelColIndex].getColumnType()) {
        case Column.TYPE_DATE :
        case Column.TYPE_DATE_TIME :
        case Column.TYPE_TIME :
          filterType=grids.getFilterPanel().FILTER_TYPE_RANGE;
        break;
        default:
          filterType=grids.getFilterPanel().FILTER_TYPE_VALUE;
      }
      if (grids.getQuickFilterValues().get(colProps[modelColIndex].getColumnName())!=null)
        grids.getRemovefilterItem().setVisible(true);
       else
        grids.getRemovefilterItem().setVisible(false);

       Object initialValue = this.getSelectedRow()>=0?this.getModel().getValueAt(this.getSelectedRow(),modelColIndex):null;
       if (gridController!=null)
         initialValue = gridController.getInitialQuickFilterValue(colProps[modelColIndex].getColumnName(),initialValue);

       QuickFilterPanel quickFilterPanel = new QuickFilterPanel(
          grids.getDefaultQuickFilterCriteria(),
          this,
          filterType,
          colProps[modelColIndex],
          initialValue
       );
       quickFilterPanel.setBackground(grids.getRemovefilterItem().getBackground());
       grids.setFilterPanel(quickFilterPanel);

       grids.getFilterPanel().setVisible(colProps[modelColIndex].isColumnFilterable());
    } else
      grids.setFilterPanel(null);
  }


  /**
   * <p>Title: Open Swing</p>
   * <p>Description: Inner class used to set the column header content,
   * according to sorting settings.</p>
   * <p>Copyright: Copyright (C) 2006 Mauro Carniel</p>
   * @author Mauro Carniel
   * @version 1.0
   */
  class TableColumnHeaderRenderer extends JPanel implements TableCellRenderer {

    private Icon sortIcon;
    private JLabel l_text, l_icon;

    public TableColumnHeaderRenderer(String attributeName,int headerTextAlignment,Font headerFont,Color headerColor) {
      setOpaque(false);
      setBorder(columnHeaderBorder);
      setLayout(new BorderLayout(0, 0));
      l_text = new JLabel();
      if (headerFont!=null)
        l_text.setFont(headerFont);
      if (headerColor!=null)
        l_text.setForeground(headerColor);
      l_text.setVerticalTextPosition(SwingConstants.CENTER);
      l_text.setHorizontalAlignment(headerTextAlignment);
      l_text.setOpaque(false);
      add(l_text, BorderLayout.CENTER);
      l_icon = new JLabel("");
      if (headerFont!=null)
        l_icon.setFont(headerFont);
      if (headerColor!=null)
        l_icon.setForeground(headerColor);
      l_icon.setOpaque(false);
      add(l_icon, BorderLayout.EAST);
      setToolTipText(ClientSettings.getInstance().getResources().getResource(
         gridController.getHeaderTooltip(attributeName)
      ));
    }


    public void setIcon(Icon icon,int sortingOrder) {
      if (sortingOrder>0 && ClientSettings.SHOW_SORTING_ORDER)
        l_icon.setText(String.valueOf(sortingOrder));
      else
        l_icon.setText("");
      l_icon.setIcon(icon);
    }


    public Component getTableCellRendererComponent(JTable table,
                                                   Object value,
                                                   boolean isSelected,
                                                   boolean hasFocus,
                                                   int row,
                                                   int viewColumnIndex) {
     int columnModelIndex = table.convertColumnIndexToModel(viewColumnIndex);
     setIcon(null,0);
     for(int i=0;i<grids.getCurrentSortedColumns().size();i++)
       if (grids.getCurrentSortedColumns().get(i).equals(colProps[columnModelIndex].getColumnName())) {
         // current column is sorted...
         if (grids.getCurrentSortedVersusColumns().get(i).equals(Consts.ASC_SORTED))
           setIcon(ascSort,i+1);
         else
           setIcon(descSort,i+1);
       }
      l_text.setText(value.toString());
      return this;
    }



  } //TableColumnHeaderRenderer


  /**
   * Method called when user right clicks with the mouse on the grid: it will view the popup menu.
   */
  private void showPopupMenu(final int x,final int y) {
    try {
      Point tmpTablexy = null;
      try {
        tmpTablexy = this.getLocationOnScreen();
      }
      catch (Exception ex1) {
        tmpTablexy = this.getLocation();
      }
      final Point tablexy = tmpTablexy;
      final Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();

      grids.getPopup().removeAll();

      if (getMode()==Consts.READONLY) {
        // menu items to show only in read only mode:

        // add quick filter to popup menu...
        configQuickFilter(x,y);
        if (grids.getFilterPanel()!=null) {
          grids.getFilterPanel().setParentPopup(grids.getPopup());
          grids.getPopup().add(grids.getFilterPanel());
          // add menu item to remove filter setting on the popup menu...
          grids.getRemovefilterItem().setToolTipText(ClientSettings.getInstance().getResources().getResource("Remove Filter"));
          grids.getRemovefilterItem().removeAll();
          grids.getRemovefilterItem().addActionListener(new ActionListener() {
          public void actionPerformed(ActionEvent e) {
              int modelColIndex = getSelectedColumn()==-1 ? -1 :getColumnModel().getColumn(getSelectedColumn()).getModelIndex();
              if (modelColIndex==-1) {
                int xOverflow = x+(int)tablexy.getX()+grids.getPopup().getWidth()-(int)screenSize.getWidth();
                int popupX = xOverflow>0?x-xOverflow-20:x;
                modelColIndex = getColumnModel().getColumnIndexAtX(popupX);
              }

              if (modelColIndex!=-1) {
                removeQuickFilter(modelAdapter.getFieldName(modelColIndex));
              }
            }
          });
          grids.getPopup().add(grids.getRemovefilterItem());

          grids.getPopup().add(new JSeparator());
        }

        // add optional command to popup menu...
        for(int i=0;i<grids.getPopupCommands().size();i++)
          grids.getPopup().add((JMenuItem)grids.getPopupCommands().get(i));
        if (grids.getPopupCommands().size()>0)
          grids.getPopup().add(new JSeparator());

      } // end menu items to show only in read only mode


      // add "select all" and "deselect all" commands in case of check-box column...
      int modelColIndex = getSelectedColumn()==-1 ? -1 :getColumnModel().getColumn(getSelectedColumn()).getModelIndex();
      if (modelColIndex==-1) {
      int xOverflow = x+(int)tablexy.getX()+grids.getPopup().getWidth()-(int)screenSize.getWidth();
      int popupX = xOverflow>0?x-xOverflow-20:x;
      modelColIndex = getColumnModel().getColumnIndexAtX(popupX);
      }
      if (modelColIndex!=-1 &&
          colProps[modelColIndex].getColumnType()==Column.TYPE_CHECK &&
          ((CheckBoxColumn)colProps[modelColIndex]).isShowDeSelectAllInPopupMenu() &&
          (getMode()!=Consts.READONLY || ((CheckBoxColumn)colProps[modelColIndex]).isEnableInReadOnlyMode())
          ) {

        JMenuItem selMenu = new JMenuItem(
          ClientSettings.getInstance().getResources().getResource("select all"),
          ClientSettings.getInstance().getResources().getResource("select all").charAt(0)
        );
        final int index = modelColIndex;
        selMenu.addActionListener(new ActionListener() {
          public void actionPerformed(ActionEvent e) {
            for(int i=0;i<model.getRowCount();i++)
              if (((CheckBoxColumn)colProps[index]).isDeSelectAllCells() || model.isCellEditable(i,index))
                model.setValue(
                  ((CheckBoxColumn)colProps[index]).getPositiveValue(),
                  i,
                  index
                );
          }
        });
        grids.getPopup().add(selMenu);

        JMenuItem deselMenu = new JMenuItem(
          ClientSettings.getInstance().getResources().getResource("deselect all"),
          ClientSettings.getInstance().getResources().getResource("deselect all").charAt(0)
        );
        deselMenu.addActionListener(new ActionListener() {
          public void actionPerformed(ActionEvent e) {
            for(int i=0;i<model.getRowCount();i++)
              if (((CheckBoxColumn)colProps[index]).isDeSelectAllCells() || model.isCellEditable(i,index))
                model.setValue(
                  ((CheckBoxColumn)colProps[index]).getNegativeValue(),
                  i,
                  index
                );
          }
        });
        grids.getPopup().add(deselMenu);

        if (getMode()==Consts.READONLY)
          grids.getPopup().add(new JSeparator());
      }


      if (getMode()==Consts.READONLY) {
        // menu items to show only in read only mode:

        // add selectable columns to popup menu...
        JCheckBoxMenuItem cbMenuItem;
        for(int i=0;i<colProps.length;i++)
          if (colProps[i].isColumnSelectable()) {
            if (colProps[i].getHeaderColumnName()!=null &&
                colProps[i].getHeaderColumnName().length()>0)
              cbMenuItem = new JCheckBoxMenuItem(ClientSettings.getInstance().getResources().getResource(colProps[i].getHeaderColumnName()));
            else
              cbMenuItem = new JCheckBoxMenuItem(ClientSettings.getInstance().getResources().getResource(colProps[i].getColumnName()));
            if (colProps[i].isColumnVisible())
              cbMenuItem.setState(true);
            cbMenuItem.addActionListener(new CheckboxMenuItem(i));
            grids.getPopup().add(cbMenuItem);
          }
      } // end menu items to show only in read only mode

      if (grids.getPopup().getComponentCount()>0) {
        int xOverflow = x+(int)tablexy.getX()+grids.getPopup().getWidth()-(int)screenSize.getWidth();
        int yOverflow = y+(int)tablexy.getY()+grids.getPopup().getHeight()-(int)screenSize.getHeight();
        int popupX = xOverflow>0?x-xOverflow-20:x;
        int popupY = yOverflow>0?y-yOverflow-20:y;
        grids.getPopup().show(this,popupX,popupY);
      }
    }
    catch (Throwable ex) {
      Logger.error(this.getClass().getName(),"showPopupMenu","Error while constructing grids.getPopup() menu.",ex);
      ex.printStackTrace();
    }
  }


  class CheckboxMenuItem implements ActionListener {

    private int columnIndex = -1;

    public CheckboxMenuItem(int columnIndex) {
      this.columnIndex = columnIndex;
    }

    public void actionPerformed(ActionEvent e) {
      colProps[columnIndex].setColumnVisible(!colProps[columnIndex].isColumnVisible());
      if (fromColIndex<=columnIndex && columnIndex<toColIndex)
        setVisibleColumn(columnIndex,colProps[columnIndex].isColumnVisible());
      else if (grids!=null) {
        if (!lockedGrid)
          grids.getLockedGrid().setVisibleColumn(columnIndex,colProps[columnIndex].isColumnVisible());
        else
          grids.getGrid().setVisibleColumn(columnIndex,colProps[columnIndex].isColumnVisible());
      }
    }
  }


  /**
   * Set column visibility for all grid columns.
   */
  private final void setVisibleColumns() {
    // remove all columns...
    TableColumnModel colsModel = this.getColumnModel();
    while(colsModel.getColumnCount()>0)
      colsModel.removeColumn(colsModel.getColumn(0));
    // add visible columns only...
    for(int i=fromColIndex;i<toColIndex;i++) {
      if (colProps[i].isColumnVisible()) {
        this.addColumn(tableColumnModel[i]);
      }
    }
  }


  /**
   * Set (in)visibility state of the specified column.
   * @param columnModelIndex column model index
   * @param colVisible (in)visibility state for the specified column
   */
  public final void setVisibleColumn(int columnModelIndex,boolean colVisible) {
    colProps[columnModelIndex].setColumnVisible(colVisible);
    TableColumnModel colsModel = this.getColumnModel();
    int columnViewIndex = this.convertColumnIndexToView(columnModelIndex);
    if (!colVisible && columnViewIndex!=-1)
      colsModel.removeColumn(colsModel.getColumn(columnViewIndex));
    else if (colVisible && columnViewIndex==-1) {
      setVisibleColumns();
    }

    // top grid is defined and this is the std grid...
    if (grids.getGridControl()!=null && grids.getGridControl().getLockedRowsOnTop()>0 &&
        gridType==MAIN_GRID &&
        grids.getGridControl().getTopTable()!=null){
      grids.getGridControl().getTopTable().getGrid().setVisibleColumn(columnModelIndex,colVisible);
      if (grids.getGridControl().getTopTable().getLockedGrid()!=null)
        grids.getGridControl().getTopTable().getLockedGrid().setVisibleColumn(columnModelIndex,colVisible);
    }

    // bottom grid is defined and this is the std grid...
    if (grids.getGridControl()!=null && grids.getGridControl().getLockedRowsOnBottom()>0 &&
        gridType==MAIN_GRID &&
        grids.getGridControl().getBottomTable()!=null){
      grids.getGridControl().getBottomTable().getGrid().setVisibleColumn(columnModelIndex,colVisible);
      if (grids.getGridControl().getBottomTable().getLockedGrid()!=null)
        grids.getGridControl().getBottomTable().getLockedGrid().setVisibleColumn(columnModelIndex,colVisible);
    }

    if (hasColSpan) {
      repaintAdditionalColumnHeaders(columnModelIndex,colVisible?colProps[columnModelIndex].getPreferredWidth():0);
    }
  }


  /**
   * @return table model of the grid
   */
  public final VOListTableModel getVOListTableModel() {
    return model;
  }


  /**
   * Called whenever the value of the selection changes.
   * @param e grid selection event
   */
  public final void valueChanged(ListSelectionEvent e) {
    super.valueChanged(e);

    if (gridType!=MAIN_GRID)
      return;

    if (model!=null && getMode()==Consts.INSERT) {
      // if grid is in insert mode then row selection is always forced to the inserted rows...
      if (getSelectedRow()>=grids.getCurrentNumberOfNewRows()) {
        setRowSelectionInterval(
            Math.max(0,grids.getCurrentNumberOfNewRows()-1),
            Math.max(0,grids.getCurrentNumberOfNewRows()-1)
        );
        grids.setRowSelectionInterval(
            Math.max(0,grids.getCurrentNumberOfNewRows()-1),
            Math.max(0,grids.getCurrentNumberOfNewRows()-1)
        );
        return;
      }
    }

    if (grids!=null && grids.getLockedGrid()!=null) {
      if (!controlDown || getSelectionModel().getSelectionMode()==ListSelectionModel.SINGLE_SELECTION)
        grids.setRowSelectionInterval(this.getSelectedRow(),this.getSelectedRow());
      else if (controlDown && getSelectionModel().getSelectionMode()==ListSelectionModel.SINGLE_INTERVAL_SELECTION) {
        getSelectionModel().addSelectionInterval(this.getSelectedRow(),this.getSelectedRow());
      }
      else {
        addRowSelectionInterval(this.getSelectedRow(),this.getSelectedRow());
      }
    }
//    else if (grids!=null && grids.getLockedGrid()==null) {
//      if (!controlDown || getSelectionModel().getSelectionMode()==ListSelectionModel.SINGLE_SELECTION)
//        grids.setRowSelectionInterval(this.getSelectedRow(),this.getSelectedRow());
//      else if (controlDown && getSelectionModel().getSelectionMode()==ListSelectionModel.SINGLE_INTERVAL_SELECTION) {
//        addRowSelectionInterval(this.getSelectedRow(),this.getSelectedRow());
//      }
//      else {
//        addRowSelectionInterval(this.getSelectedRow(),this.getSelectedRow());
//      }
//    }

    // skip additional events...
    if (e.getValueIsAdjusting())
      return;

    if (model!=null && getMode()==Consts.READONLY) {
      if (this.getSelectedRows().length>1) {
        // toolbar disabilitation...
        if (grids.getNavBar()!=null)
          grids.getNavBar().setEnabled(false);
        if (grids.getEditButton()!=null)
          grids.getEditButton().setEnabled(false);
        grids.setGenericButtonsEnabled(false);
      }
      else {
        if (grids.getEditButton() != null)
          grids.getEditButton().setEnabled(true);
        grids.setGenericButtonsEnabled(true);
      }
    }

    if (gridController!=null &&
        this.getSelectedRows().length==1
        ) {
      // fire cell selected event...
      int columnIndex = this.getSelectedColumn();
      if (columnIndex!=-1)
        columnIndex = this.convertColumnIndexToModel(columnIndex);
      else
        columnIndex = 0;
      gridController.selectedCell(
          this.getSelectedRow(), columnIndex,
          model.getColumnName(columnIndex),
          model.getObjectForRow(this.getSelectedRow())
      );
      // update navigation toolbar state...
      if (getMode()==Consts.READONLY && (grids.getNavBar()!=null)) {
        if (grids.getstartIndex()==0 &&
            this.getSelectedRow()==0)
          grids.getNavBar().setFirstRow(true);
        else
          grids.getNavBar().setFirstRow(false);
        if (!grids.isMoreRows() && this.getSelectedRow()==model.getRowCount()-1)
          grids.getNavBar().setLastRow(true);
        else
          grids.getNavBar().setLastRow(false);
      }
    }

    if (statusPanel!=null) {
      // update status bar content...
      if (this.getSelectedRows().length==0)
        statusPanel.setText("");
      else if (this.getSelectedRows().length==1)
        statusPanel.setText(ClientSettings.getInstance().getResources().getResource("Selected Row")+": "+(grids.getCurrentNumberOfNewRows()+grids.getLastIndex()-model.getRowCount()+this.getSelectedRow()+2));
      else if (this.getSelectedRows().length>1 && getMode()==Consts.READONLY) {
        String rows = "";
        for(int i=0;i<this.getSelectedRows().length;i++)
          rows += (this.getSelectedRows()[i]+2+grids.getCurrentNumberOfNewRows()+grids.getLastIndex()-model.getRowCount())+", ";
        rows = rows.substring(0,rows.length()-2);
        statusPanel.setText(ClientSettings.getInstance().getResources().getResource("Selected Rows")+": "+rows);
      }
      else statusPanel.setText("");
    }

    try {
      ensureRowIsVisible(getSelectedRow());
    }
    catch (Exception ex) {
    }

    // fire row selected event...
    if (gridController!=null && this.getSelectedRow()!=-1)
      gridController.rowChanged(this.getSelectedRow());

  }


  /**
   * Method overrided to ensure that the current selected cell is always visible.
   * @param e ListSelectionEvent
   */
  public void columnSelectionChanged(ListSelectionEvent e) {
    super.columnSelectionChanged(e);

    Rectangle r = this.getVisibleRect();
    int row = getSelectedRow();
    try {
      if (this.rowAtPoint(new Point(r.x, r.y)) > row) {
        row = this.rowAtPoint(new Point(r.x, r.y + r.height-1));
        setRowSelectionInterval(row, row);
      }
      else if (this.rowAtPoint(new Point(r.x, r.y + r.height-1)) < row) {
        row = this.rowAtPoint(new Point(r.x, r.y));
        setRowSelectionInterval(row, row);
      }
    }
    catch (Exception ex) {
    }
    ensureRowIsVisible(row);

    revalidate();
    repaint();
    if (this.getTableHeader()!=null) {
      getTableHeader().revalidate();
      getTableHeader().repaint();
    }

  }


  /**
   * @return background color for the selected cell
   */
  public final Color getActiveCellBackgroundColor() {
    return activeCellBackgroundColor;
  }


  /**
   * Set background color for the selected cell.
   * @param activeCellBackgroundColor background color for the selected cell
   */
  public final void setActiveCellBackgroundColor(Color activeCellBackgroundColor) {
    this.activeCellBackgroundColor = activeCellBackgroundColor;
  }


  /**
   * @return current grid mode; possible values: Consts.READ_ONLY, Consts.EDIT, Consts.INSERT
   */
  public final int getMode() {
    return model.getMode();
  }


  /**
   * Set if it is allowed to reorder columns.
   * @param reorderingAllowed flag used to set columns reordering
   */
  public final void setReorderingAllowed(boolean reorderingAllowed) {
    this.reorderingAllowed = reorderingAllowed;
    if (this.getTableHeader()!=null)
      this.getTableHeader().setReorderingAllowed(reorderingAllowed);
  }


  /**
   * Set if it is allowed to resize columns.
   * @param reorderingAllowed flag used to set columns resizing
   */
  public final void setResizingAllowed(boolean resizingAllowed) {
    this.resizingAllowed = resizingAllowed;
    if (this.getTableHeader()!=null)
      this.getTableHeader().setResizingAllowed(resizingAllowed);
  }


  /**
   * This method override the super class method, to fix a bug.
   * @param selRow selected row
   */
  public final void ensureRowIsVisible(int selRow) {
    java.awt.Rectangle r = getCellRect(this.getSelectedRow(),this.getSelectedColumn()==-1?0:this.getSelectedColumn(), true);
    scrollRectToVisible(r);
  }


  /**
   * This method override the super class mathod to add a vertical scrollbar listener and right mouse click event on the scroll bar...
   */
  protected void configureEnclosingScrollPane() {
    Container p = getParent();

    // retrieve container scrollpane and add to it a vertical scrollbar listener...
    if (p instanceof JViewport) {
      Container gp = p.getParent();
      if (gp instanceof JScrollPane) {
        scrollPane = (JScrollPane)gp;
        if (rightClickMouseListener==null)
          rightClickMouseListener = new RightClickMouseListener();
        try {
          scrollPane.removeMouseListener(rightClickMouseListener);
        }
        catch (Exception ex) {
        }
        scrollPane.addMouseListener(rightClickMouseListener);
        createVerticalScrollBarListener(scrollPane);

        JViewport viewport = scrollPane.getViewport();
        if (viewport == null || viewport.getView() != this) {
           return;
        }

        // add column headers and optionally additional column headers...
        JPanel topPanel = new JPanel();
        topPanel.setLayout(new GridBagLayout());
        JTableHeader th = getTableHeader();
        if (th!=null)
          topPanel.add(th,
                       new GridBagConstraints(0, 1, 1, 1, 1.0, 0.0
                                              , GridBagConstraints.WEST,
                                              GridBagConstraints.BOTH,
                                              new Insets(0, 0, 0, 0), 0, 0));


        if (hasColSpan &&
             (gridType==Grid.MAIN_GRID && grids.getGridControl()==null ||
              gridType==Grid.MAIN_GRID && grids.getGridControl()!=null && grids.getGridControl().getTopTable()==null ||
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
              if (colProps[i].getHeaderTextAlignment()==SwingConstants.CENTER)
                hp.setLayout(new FlowLayout(FlowLayout.CENTER,0,0));
              else if (colProps[i].getHeaderTextAlignment()==SwingConstants.LEFT)
                hp.setLayout(new FlowLayout(FlowLayout.LEFT,0,0));
              else if (colProps[i].getHeaderTextAlignment()==SwingConstants.RIGHT)
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
              additionalHeaderToHeaders.put(hp,list);
              headerToAdditionalHeader.put(new Integer(i),hp);
            }
            else {
              w += colProps[i].getPreferredWidth();
              rest--;
              ArrayList list = (ArrayList)additionalHeaderToHeaders.get(hp);
              if (list!=null)
                list.add(new Integer(i));
              headerToAdditionalHeader.put(new Integer(i),hp);
            }
            if (rest==0 && h!=null) {
              hp.setPreferredSize(new Dimension(w,ClientSettings.HEADER_HEIGHT));
              if (i>=fromColIndex && i<toColIndex) {
                additionalColHeaders.add(hp,null);
              }
            }
          }

          topPanel.add(additionalColHeaders,
                         new GridBagConstraints(0, 0, 1, 1, 1.0, 0.0
                                                , GridBagConstraints.WEST,
                                                GridBagConstraints.BOTH,
                                                new Insets(0, 0, 0, 0), 0, 0));
        }

        scrollPane.setColumnHeaderView(topPanel);
        //  scrollPane.getViewport().setBackingStoreEnabled(true);
        Border border = scrollPane.getBorder();
        if (border == null || border instanceof UIResource) {
            scrollPane.setBorder(UIManager.getBorder("Table.scrollPaneBorder"));
        }

      }

      if (gridType==MAIN_GRID)
         searchWindowManager = new SearchWindowManager(this);

    }
  }


  /**
   * @return <code>true</code> if search window is currently visible, <code>false</code> otherwise
   */
  public final boolean isSearchWindowVisible() {
    return searchWindowManager!=null && searchWindowManager.isSearchWindowVisible();
  }





  /**
   * <p>Title: Open Swing</p>
   * <p>Description: Inner class used to listen right click mouse events, to view the grids.getPopup() menu.</p>
   * <p>Copyright: Copyright (c) 2006 Mauro Carniel</p>
   * @author Mauro Carniel
   * @version 1.0
   */
  class RightClickMouseListener extends MouseAdapter {

    public void mouseClicked(MouseEvent e) {
      if (e.getClickCount()==1 &&
          SwingUtilities.isRightMouseButton(e)
//          getSelectedColumn()!=-1 &&
          ) {

        if (Grid.this.columnAtPoint(e.getPoint())>=0)
          Grid.this.setColumnSelectionInterval(
              Grid.this.columnAtPoint(e.getPoint()),
              Grid.this.columnAtPoint(e.getPoint())
          );
        if (model.getRowCount()>0 && Grid.this.rowAtPoint(e.getPoint())>=0)
          if (Grid.this.getSelectedRows()!=null && Grid.this.getSelectedRows().length<=1)
            Grid.this.setRowSelectionInterval(
                Grid.this.rowAtPoint(e.getPoint()),
                Grid.this.rowAtPoint(e.getPoint())
            );
//        if (Grid.this.rowAtPoint(e.getPoint())>=0)
          showPopupMenu(e.getX(),e.getY());
      }
    }
  }


  /**
   * Method called by quick filter panel to filter the specified column.
   * @param colProps column properties
   * @param value1 value used in the filter column (as unique filtering value or as the miminum value in a range filtering)
   * @param value2 (optional) value used in the filter column (as the maximum value in a range filtering)
   */
  public void filter(Column colProps,Object value1,Object value2) {
    if(colProps==null) return;

    if (gridController!=null) {
      value1 = gridController.beforeFilterGrid(colProps.getColumnName(),value1);
      if (value2!=null)
        value2 = gridController.beforeFilterGrid(colProps.getColumnName(),value2);
    }

    FilterWhereClause[] values = null;

    if (value2!=null)
      grids.getQuickFilterValues().put(
          colProps.getColumnName(),
          values = new FilterWhereClause[] {
            new FilterWhereClause(colProps.getColumnName(),">=",value1),
            new FilterWhereClause(colProps.getColumnName(),value2 instanceof java.util.Date?"<":"<=",value2)
          }
      );
    else {
       int colType=colProps.getColumnType();
       if ((colType==Column.TYPE_TEXT ||
            colType==Column.TYPE_LOOKUP) &&
            value1!=null && (value1.toString().endsWith("%") || value1.toString().startsWith("%")))
        grids.getQuickFilterValues().put(
            colProps.getColumnName(),
            values = new FilterWhereClause[] {
              new FilterWhereClause(colProps.getColumnName(),ClientSettings.LIKE,value1),
              null
            }
        );
      else
        grids.getQuickFilterValues().put(colProps.getColumnName(),values = new FilterWhereClause[] {
          new FilterWhereClause(colProps.getColumnName(),"=",value1),
          null
        });

    }

    // set to a lower border the filtered column...
    int gridIndex = -1;
    for(int i=0;i<getModel().getColumnCount();i++) {
      try {
        gridIndex = this.convertColumnIndexToView(i);
        if (gridIndex!=-1) {
          if (colProps.getColumnName().equals(getVOListTableModel().getColumnName(i)) && this.getTableHeader()!=null)
            ((TableColumnHeaderRenderer)this.getTableHeader().getColumnModel().getColumn(gridIndex).getHeaderRenderer()).setBorder(BorderFactory.createLoweredBevelBorder());
        }
      }
      catch (Exception ex) {
        ex.printStackTrace();
      }
    }


    grids.reload();

    grids.getRemovefilterItem().setVisible(true);
    grids.getPopup().setVisible(false);
    grids.getPopup().setVisible(true);
    grids.getFilterPanel().requestFocus();

  }


  /**
   * Method invoked by FilterPanel to repaint icons in column headers,
   * according to sorting/filtering conditions just applied.
   */
  public final void updateColumnHeaderIcons() {
    // set to a lower/upper border the filtered column...
    int gridIndex = -1;
    FilterWhereClause[] where = null;
    for(int i=0;i<getModel().getColumnCount();i++) {
      try {
        gridIndex = this.convertColumnIndexToView(i);
        if (gridIndex!=-1 && this.getTableHeader()!=null) {

          where = (FilterWhereClause[])grids.getQuickFilterValues().get( getVOListTableModel().getColumnName(i) );
          if (where!=null)
            ((TableColumnHeaderRenderer)this.getTableHeader().getColumnModel().getColumn(gridIndex).getHeaderRenderer()).setBorder(BorderFactory.createLoweredBevelBorder());
          else
            ((TableColumnHeaderRenderer)this.getTableHeader().getColumnModel().getColumn(gridIndex).getHeaderRenderer()).setBorder(BorderFactory.createRaisedBevelBorder());
        }
      }
      catch (Exception ex) {
        ex.printStackTrace();
      }
    }
  }


  /**
   * Remove filter from the specified column.
   * @param attributeName attribute name that identifies the column
   */
  public void removeQuickFilter(String attributeName) {
    // remove filter from the specified column...
    FilterWhereClause[] values = (FilterWhereClause[])grids.getQuickFilterValues().remove(attributeName);
    if (values==null)
      return;

    // set to a upper border the filtered column...
    int gridIndex = -1;
    for(int i=0;i<getModel().getColumnCount();i++) {
      try {
        gridIndex = this.convertColumnIndexToView(i);
        if (gridIndex!=-1) {
          if (attributeName.equals(getVOListTableModel().getColumnName(i)) &&
              this.getTableHeader()!=null)
            ((TableColumnHeaderRenderer)this.getTableHeader().getColumnModel().getColumn(gridIndex).getHeaderRenderer()).setBorder(BorderFactory.createRaisedBevelBorder());
        }
      }
      catch (Exception ex) {
        ex.printStackTrace();
      }
    }

    grids.reload();
  }


  /**
  * Method called by JVM when viewing this class.
  */
  public final void addNotify() {
    super.addNotify();
    if (firstTime && gridType==MAIN_GRID) {
      // this flag allows only one execution of the following instructions...
      firstTime = false;

      dropTarget = new DropTarget(scrollPane, this);
      dropTarget2 = new DropTarget(this, this);

      // set descriptions into the column headers...
      for(int i=0;i<tableColumnModel.length;i++) {
        tableColumnModel[i].setHeaderValue(ClientSettings.getInstance().getResources().getResource(colProps[i].getHeaderColumnName()));
      }

    }
  }


  /**
   * @return <code>true</code> if TableModel is changed, <code>false</code> otherwise
   */
  public boolean isDataModified() {
    boolean modified = false;
    if (getMode()==Consts.INSERT)
      modified = true;
    else if (getMode()==Consts.EDIT) {
      if (stopCellEditing())
        modified = true;
      else
        modified = (getVOListTableModel().getChangedRows().size() > 0);
    }
    return(modified);
  }


  /**
   * @return <code>true</code> if current editing cell is in valid state, <code>false</code> otherwise
   */
  public final boolean stopCellEditing() {
    int row = getEditingRow();
    int col = getEditingColumn();

    if ((row>=0) && (col>=0))
      return(getCellEditor(row, col).stopCellEditing());
    else
      return true;
  }


  /**
   * Add a command to the grids.getPopup() menu.
   * @param item command to add in the grids.getPopup() menu
   */
  public final void addPopupCommand(JMenuItem item) {
    grids.getPopupCommands().add(item);
  }



  /**
   * @param columnName column name
   * @return int column index of the specified column name
   */
  public int getColumnIndex(String columnName) {
    return(this.modelAdapter.getFieldIndex(columnName));
  }


  /**
   * @param rowHeightFixed define if row height can change for each row, according to image height included in a cell of grid; default value: <code>true</code>
   */
  public final void setRowHeightFixed(boolean rowHeightFixed) {
    this.rowHeightFixed = rowHeightFixed;
  }


  /**
   * @return define if row height can change for each row, according to image height included in a cell of grid; default value: <code>true</code>
   */
  public final boolean isRowHeightFixed() {
    return rowHeightFixed;
  }


  /**
   * Used to define if grid sorting operation must always invoke loadData method to retrieve a new list of v.o. or
   * the grid must sort the current v.o. list without invoking loadData (only with the whole result set loaded).
   * @param orderWithLoadData flag used to define if grid sorting operation must always invoke loadData method to retrieve a new list of v.o. or the grid must sort the current v.o. list without invoking loadData (only with the whole result set loaded)
   */
  public final void setOrderWithLoadData(boolean orderWithLoadData) {
    this.orderWithLoadData = orderWithLoadData;
  }


  /**
   * @return mouse listener applied to tabler headers to sort columns
   */
  public final MouseListener getSortingMouseListener() {
    return sortingMouseListener;
  }


  /********************************************************************
   *
   *             DRAG 'N DROP MANAGEMENTS METHODS
   *
   ********************************************************************/



  /************************************************************
   * DRAG MANAGEMENT
   ************************************************************/



  /**
   * Enable drag onto the grid.
   * @param gridId grid identifier
   */
  public final void enableDrag(String gridId) {
    try {
      this.gridId = gridId;
      dragSource.createDefaultDragGestureRecognizer(
          this,
          DnDConstants.ACTION_MOVE,
          new DragGestureAdapter(this)
      );
    }
    catch (Exception ex) {
      ex.printStackTrace();
    }
  }


  class DragGestureAdapter implements DragGestureListener {

    private DragSourceListener dragListener = null;

    public DragGestureAdapter(DragSourceListener dragListener) {
      this.dragListener = dragListener;
    }


    /**
     * A drag gesture has been initiated.
     */
    public final void dragGestureRecognized( DragGestureEvent event) {
      if (gridController.dragEnabled()) {
        setCursor(dragCursor);
        int[] selIndexes = Grid.this.getSelectedRows();
        if ( selIndexes.length>0 ){
          try {
            MDIFrame.getInstance().setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
          }
          catch (Exception ex) {
          }
          dragSource.startDrag (event, DragSource.DefaultMoveDrop, new StringSelection(gridId), dragListener);
        }
      }
      else
        // drag interrupted...
        event.getSourceAsDragGestureRecognizer().resetRecognizer();
    }

  }


  /**
   * This message goes to DragSourceListener, informing it that the dragging has entered the DropSite
   */
  public final void dragEnter (DragSourceDragEvent event) {
    setCursor(Cursor.getDefaultCursor());
    gridController.dragEnter();
  }


  /**
   * This message goes to DragSourceListener, informing it that the dragging has exited the DropSite.
   */
  public final void dragExit (DragSourceEvent event) {
    setCursor(Cursor.getDefaultCursor());
    gridController.dragExit();
  }


  /**
   * This message goes to DragSourceListener, informing it that the dragging is currently ocurring over the DropSite.
   */
  public final void dragOver (DragSourceDragEvent event) {
    setCursor(dragCursor);
    gridController.dragOver();
  }


  /**
   * This method is invoked when the user changes the dropAction.
   */
  public final void dropActionChanged ( DragSourceDragEvent event) { }


  /**
   * This message goes to DragSourceListener, informing it that the dragging has ended.
   */
  public final void dragDropEnd (DragSourceDropEvent event) {
    gridController.dragDropEnd();
  }



  /************************************************************
   * DROP MANAGEMENT
   ************************************************************/

  /**
   * This method is invoked when you are dragging over the DropSite.
   */
  public final void dragEnter (DropTargetDragEvent event) {
    event.acceptDrag (DnDConstants.ACTION_MOVE);
    gridController.dropEnter();
  }


  /**
   * This method is invoked when you are exit the DropSite without dropping.
   */
  public final void dragExit (DropTargetEvent event) {
    try {
      gridController.dropExit();
    }
    catch (Exception ex) {
      ex.printStackTrace();
    }

  }

  /**
   * This method is invoked when a drag operation is going on.
   */
  public final void dragOver (DropTargetDragEvent event) {
    int row = rowAtPoint(event.getLocation());
    gridController.dropOver(row);
  }


  /**
   * This method is invoked when a drop event has occurred.
   */
  public final void drop(DropTargetDropEvent event) {
    try {
      setCursor(Cursor.getDefaultCursor());
      Transferable transferable = event.getTransferable();
      if (transferable.isDataFlavorSupported (DataFlavor.stringFlavor)){

        if (gridController.dropEnabled((String)transferable.getTransferData ( DataFlavor.stringFlavor))) {
          event.acceptDrop(DnDConstants.ACTION_MOVE);
          event.getDropTargetContext().dropComplete(true);
        }
        else
          event.rejectDrop();

      } else{
        event.rejectDrop();
      }
    }
    catch (Exception ex) {
      ex.printStackTrace();
      event.rejectDrop();
    } finally {
      try {
        MDIFrame.getInstance().setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
      }
      catch (Exception ex) {
      }

    }
  }

  /**
   * This method is invoked if the use modifies the current drop gesture.
   */
  public final void dropActionChanged ( DropTargetDragEvent event ) {
    gridController.dropActionChanged();
  }



  /**
   * Returns the default column model object, which is
   * a <code>DefaultTableColumnModel</code>.  A subclass can override this
   * method to return a different column model object.
   *
   * This method has been overrided to allow "moveColumn" event interception:
   * this event is useful to avoid column moving with additional column headers.
   *
   * @return the default column model object
   * @see javax.swing.table.DefaultTableColumnModel
   */
  protected TableColumnModel createDefaultColumnModel() {
     return new DefaultTableColumnModel() {

       /**
        * Moves the column and heading at <code>columnIndex</code> to
        * <code>newIndex</code>.  The old column at <code>columnIndex</code>
        * will now be found at <code>newIndex</code>.  The column
        * that used to be at <code>newIndex</code> is shifted
        * left or right to make room.  This will not move any columns if
        * <code>columnIndex</code> equals <code>newIndex</code>.  This method
        * also posts a <code>columnMoved</code> event to its listeners.
        *
        * @param	index			        the index of column to be moved
        * @param	newIndex			new index to move the column
        * @exception IllegalArgumentException	if <code>column</code> or
        * 						<code>newIndex</code>
        *						are not in the valid range
        */
       public void moveColumn(int index, int newIndex) {
         if (hasColSpan &&
            (gridType==TOP_GRID ||
             grids.getGridControl()!=null && grids.getGridControl().getTopTable()==null && gridType==MAIN_GRID ||
             grids.getGridControl()==null)) {
           try {
             JPanel hp1 = (JPanel)headerToAdditionalHeader.get(new Integer(index));
             JPanel hp2 = (JPanel)headerToAdditionalHeader.get(new Integer(newIndex));
             if (hp1!=null && hp2!=null && hp1.equals(hp2)) {
               super.moveColumn(index, newIndex);
               return;
             }
             else
               return;
           }
           catch (Exception ex) {
             return;
           }
         }
         super.moveColumn(index, newIndex);
       }

     };
  }


  /**
   * Method called my JTable when moving a column.
   */
  public final void columnMoved(TableColumnModelEvent e) {
    super.columnMoved(e);
    if (gridType==TOP_GRID && grids.getGridControl()!=null && grids.getGridControl().getTopTable()!=null){
      if (lockedGrid)
        grids.getGridControl().getTable().getLockedGrid().moveColumn(e.getFromIndex(),e.getToIndex());
      else
        grids.getGridControl().getTable().getGrid().moveColumn(e.getFromIndex(),e.getToIndex());
      if (grids.getGridControl().getBottomTable()!=null) {
        if (lockedGrid)
          grids.getGridControl().getBottomTable().getLockedGrid().moveColumn(e.getFromIndex(),e.getToIndex());
        else
          grids.getGridControl().getBottomTable().getGrid().moveColumn(e.getFromIndex(),e.getToIndex());
      }
    }

    if (grids.getGridControl()!=null &&
        grids.getGridControl().getLockedRowsOnTop()==0 &&
        grids.getGridControl().getBottomTable()!=null &&
        gridType==MAIN_GRID){
      if (lockedGrid)
        grids.getGridControl().getBottomTable().getLockedGrid().moveColumn(e.getFromIndex(),e.getToIndex());
      else
        grids.getGridControl().getBottomTable().getGrid().moveColumn(e.getFromIndex(),e.getToIndex());
    }
  }


  /**
   * Repaint additional column headers, based in the specified column index.
   * @param index column index (related to the model)
   * @param width current column width
   */
  private void repaintAdditionalColumnHeaders(int index,int width) {
    try {
      JPanel hp = (JPanel)headerToAdditionalHeader.get(new Integer(index));
      if (hp!=null) {
          ArrayList list = (ArrayList)additionalHeaderToHeaders.get(hp);
          int w = 0;
          for(int i=0;i<list.size();i++)
            if (i!=index)
              w += colProps[((Integer)list.get(i)).intValue()].getPreferredWidth();
            else
              w += width;
          hp.setPreferredSize(new Dimension(w,hp.getSize().height));
          hp.revalidate();
          hp.getParent().validate();
      }
    }
    catch (Exception ex) {
    }
  }


  /**
   * Method called by JTable when resizing a column.
   */
  public final void columnMarginChanged(ChangeEvent e) {
    // check if there is an additional column header...
    try {
      TableColumn resizingColumn = getTableHeader().getResizingColumn();
      if (resizingColumn != null){
        int index = resizingColumn.getModelIndex();
        colProps[index].setPreferredWidth(resizingColumn.getWidth());
        repaintAdditionalColumnHeaders(index,resizingColumn.getWidth());
      }
    }
    catch (Exception ex) {
    }

    try {
      TableColumn resizingColumn = getTableHeader().getResizingColumn();
      if (resizingColumn != null){
        int index = resizingColumn.getModelIndex();
        colProps[index].setPreferredWidth(resizingColumn.getWidth());
        JPanel hp = (JPanel)headerToAdditionalHeader.get(new Integer(index));
        if (hp!=null) {
            ArrayList list = (ArrayList)additionalHeaderToHeaders.get(hp);
            int w = 0;
            for(int i=0;i<list.size();i++)
              w += colProps[((Integer)list.get(i)).intValue()].getPreferredWidth();
            hp.setPreferredSize(new Dimension(w,hp.getSize().height));
            hp.revalidate();
            hp.getParent().validate();
        }
      }
    }
    catch (Exception ex) {
    }

    super.columnMarginChanged(e);
    try {
      if (gridType==TOP_GRID && grids.getGridControl()!=null && grids.getGridControl().getTopTable()!=null){
        TableColumn resizingColumn = getTableHeader().getResizingColumn();
        if (resizingColumn != null){
          int index = resizingColumn.getModelIndex();
          colProps[index].setPreferredWidth(resizingColumn.getWidth());
          index = convertColumnIndexToView(index);
          if (lockedGrid)
            grids.getGridControl().getTable().getLockedGrid().getColumnModel().getColumn(index).setPreferredWidth(resizingColumn.getPreferredWidth());
          else
            grids.getGridControl().getTable().getGrid().getColumnModel().getColumn(index).setPreferredWidth(resizingColumn.getPreferredWidth());

          if (grids.getGridControl().getBottomTable()!=null) {
            if (lockedGrid)
              grids.getGridControl().getBottomTable().getLockedGrid().getColumnModel().getColumn(index).setPreferredWidth(resizingColumn.getPreferredWidth());
            else
              grids.getGridControl().getBottomTable().getGrid().getColumnModel().getColumn(index).setPreferredWidth(resizingColumn.getPreferredWidth());
          }
        }

      }

      if (grids.getGridControl()!=null &&
          grids.getGridControl().getLockedRowsOnTop()==0 &&
          grids.getGridControl().getLockedRowsOnBottom()>0 &&
          gridType==MAIN_GRID &&
          grids.getGridControl().getBottomTable()!=null){
        TableColumn resizingColumn = getTableHeader().getResizingColumn();
        int index = resizingColumn.getModelIndex();
        index = convertColumnIndexToView(index);

        if (lockedGrid)
          grids.getGridControl().getBottomTable().getLockedGrid().getColumnModel().getColumn(index).setPreferredWidth(resizingColumn.getPreferredWidth());
        else
          grids.getGridControl().getBottomTable().getGrid().getColumnModel().getColumn(index).setPreferredWidth(resizingColumn.getPreferredWidth());
      }


    }
    catch (Exception ex) {
    }

    try {
      // recalculate column with...
      int width = 0;
      for(int i=0;i<tableHeader.getColumnModel().getColumnCount();i++)
        width += tableHeader.getColumnModel().getColumn(i).getWidth();
      tableHeader.setPreferredSize(new Dimension(width,ClientSettings.HEADER_HEIGHT));

      // also to other grids???
    }
    catch (Exception ex) {
    }
  }


  /**
   * Define if background and foreground colors must be setted according to GridController definition only in READONLY mode.
   * @param colorsInReadOnlyMode <code>false</code> to enable background and foreground colors to be setted according to GridController definition in all grid modes; <code>true</code> to enable background and foreground colors to be setted according to GridController definition only in READONLY mode
   */
  public final void setColorsInReadOnlyMode(boolean colorsInReadOnlyMode) {
    this.colorsInReadOnlyMode = colorsInReadOnlyMode;
  }


  /**
   * @return define if background and foreground colors must be setted according to GridController definition only in READONLY mode
   */
  public final boolean isColorsInReadOnlyMode() {
    return colorsInReadOnlyMode;
  }


  public final boolean hasColSpan() {
    return hasColSpan;
  }


  /**
   * Set the cell span for the specified range of cells.
   * @param rows row indexes that identify the cells to merge
   * @param columns column indexes that identify the cells to merge
   * @return <code>true</code> if merge operation is allowed, <code>false</code> if the cells range is invalid
   */
  public final boolean mergeCells(int[] rows,int[] columns) {
//    if (isOutOfBounds(rows, columns))
//      return false;
    for (int i=0;i<rows.length;i++) {
      for (int j=0;j<columns.length;j++) {
        if (cellSpans.get(new Pair(
              rows[0] + i,
              columns[0] + j
        ))!=null)
          return false;
      }
    }

    for (int i=0,ii=0;i<rows.length;i++,ii--)
      for (int j=0,jj=0;j<columns.length;j++,jj--)
        cellSpans.put(
          new Pair(rows[0] + i,columns[0] + j),
          new Pair(ii,jj)
        );

    cellSpans.put(
      new Pair(rows[0],columns[0]),
      new Pair(rows.length,columns.length)
    );

    return true;
  }


  /**
   * Remove the cell span for the specified range of cells.
   * @param rows row indexes that identify the cells to merge
   * @param columns column indexes that identify the cells to merge
   * @return <code>true</code> if merge operation is allowed, <code>false</code> if the cells range is invalid
   */
  public final boolean removeMergedCells(int[] rows,int[] columns) {
    for (int i=0;i<rows.length;i++) {
      for (int j=0;j<columns.length;j++) {
        if (cellSpans.get(new Pair(
              rows[0] + i,
              columns[0] + j
        ))==null)
          return false;
      }
    }

    for (int i=0,ii=0;i<rows.length;i++,ii--)
      for (int j=0,jj=0;j<columns.length;j++,jj--)
        cellSpans.remove(
          new Pair(rows[0] + i,columns[0] + j)
        );

    cellSpans.remove(
      new Pair(rows[0],columns[0])
    );

    return true;
  }


  /**
   * Method invoked by mergeCells to check cells merging consistency.
   */
  private boolean isOutOfBounds(int row, int column) {
    if (row<0 || model.getRowCount()<=row || column < 0 || model.getColumnCount()<=column)
      return true;
    else
      return false;
  }


  /**
   * Method invoked by mergeCells to check cells merging consistency.
   */
  private boolean isOutOfBounds(int[] rows, int[] columns) {
    for (int i=0;i<rows.length;i++)
      if (rows[i] < 0 || model.getRowCount()<=rows[i])
        return true;

    for (int i=0;i<columns.length;i++)
      if (columns[i] < 0 || model.getColumnCount()<=columns[i])
        return true;

    return false;
  }


  /**
   * @return <code>true</code> if the specified cell is visible (according to cells span settings), <code>false</code> otherwise
   */

  public final boolean isVisible(int row, int column) {
    if (isOutOfBounds(row,column))
      return false;

    Pair p = (Pair)cellSpans.get(new Pair(row,column));
    if (p==null)
      return true;

    if (p.getN1()<1 || p.getN2()<1)
      return false;

    return true;
  }


  /**
   * @return cell span
   */
  public final Pair getSpan(int row, int column) {
    if (isOutOfBounds(row, column))
      return new Pair(1,1);

    Pair p = (Pair)cellSpans.get(new Pair(row,column));
    if (p==null)
      return new Pair(1,1);

    return p;
  }


  /**
   * This method has been overrided to support cells span: it returns the cell span (width and height),
   * according to the specified cell current visibility.
   * Note: a cell can be invisible if it is contained inside a spanned cell.
   */
/*
  public Rectangle getCellRect(int row, int column, boolean includeSpacing) {
    Rectangle sRect = super.getCellRect(row,column,includeSpacing);
    if (row<0 ||
        column<0 ||
        model.getRowCount()<=row ||
        model.getColumnCount()<=column)
      return sRect;

    if (!isVisible(row,column)) {
      int temp_row    = row;
      int temp_column = column;
      row    += getSpan(temp_row,temp_column).getN1();
      column += getSpan(temp_row,temp_column).getN2();
    }
    Pair n = getSpan(row,column);

    int index = 0;
    int columnMargin = getColumnModel().getColumnMargin();
    Rectangle cellFrame = new Rectangle();
    int aCellHeight = rowHeight + rowMargin;
    cellFrame.y = row * aCellHeight;
    cellFrame.height = n.getN1()*aCellHeight;

    Enumeration enumeration = getColumnModel().getColumns();
    while (enumeration.hasMoreElements()) {
      TableColumn aColumn = (TableColumn)enumeration.nextElement();
      cellFrame.width = aColumn.getWidth() + columnMargin;
      if (index == column) break;
      cellFrame.x += cellFrame.width;
      index++;
    }
    for (int i=0;i< n.getN2()-1;i++) {
      TableColumn aColumn = (TableColumn)enumeration.nextElement();
      cellFrame.width += aColumn.getWidth() + columnMargin;
    }

    if (!includeSpacing) {
      Dimension spacing = getIntercellSpacing();
      cellFrame.setBounds(cellFrame.x +      spacing.width/2,
                          cellFrame.y +      spacing.height/2,
                          cellFrame.width -  spacing.width,
                          cellFrame.height - spacing.height);
    }
    return cellFrame;
  }
*/


  private int calcHeightToRow(int row) {
    int y = 0;
    for(int i=0;i<row;i++)
      y += getRowHeight(i);
    return y;
  }



  public Rectangle getCellRect(int row, int column, boolean includeSpacing) {
    Rectangle r = new Rectangle();
    boolean valid = true;

    if (row>=0 &&
        column>=0 &&
        row<model.getRowCount() &&
        column<model.getColumnCount() &&
        !isVisible(row,column)) {
      int temp_row    = row;
      int temp_column = column;
      row    += getSpan(temp_row,temp_column).getN1();
      column += getSpan(temp_row,temp_column).getN2();
    }
    Pair n = getSpan(row,column);


    if (row < 0) {
        // y = height = 0;
        valid = false;
    }
    else if (row >= getRowCount()) {
        r.y = getHeight();
        valid = false;
    }
    else {
        r.height = n.getN1()*getRowHeight(row);
        r.y = calcHeightToRow(row);
    }

    if (column < 0) {
        if( !getComponentOrientation().isLeftToRight() ) {
            r.x = getWidth();
        }
        // otherwise, x = width = 0;
        valid = false;
    }
    else if (column >= getColumnCount()) {
        if( getComponentOrientation().isLeftToRight() ) {
            r.x = getWidth();
        }
        // otherwise, x = width = 0;
        valid = false;
    }
    else {
        TableColumnModel cm = getColumnModel();
        if( getComponentOrientation().isLeftToRight() ) {
            for(int i = 0; i < column; i++) {
                r.x += cm.getColumn(i).getWidth();
            }
        } else {
            for(int i = cm.getColumnCount()-1; i > column; i--) {
                r.x += cm.getColumn(i).getWidth();
            }
        }

        r.width = 0;
        for (int i=column;i< column+n.getN2();i++) {
          r.width += cm.getColumn(i).getWidth();
        }

//        r.width = cm.getColumn(column).getWidth();
    }

    if (valid && !includeSpacing) {
        int rm = getRowMargin();
        int cm = getColumnModel().getColumnMargin();
        // This is not the same as grow(), it rounds differently.
        r.setBounds(r.x + cm/2, r.y + rm/2, r.width - cm, r.height - rm);
    }

    return r;
  }


  /**
   * Utility method invoked by rowAtPoint.
   */
  private Pair rowColumnAtPoint(Point point) {
    Pair retValue = new Pair(-1,-1);
    int h=0;
    int row = 0;
    for(row=0;row<getRowCount();row++) {
      if (h+getRowHeight(row)>point.y)
        break;
      else {
        h += getRowHeight(row);
      }
    }
//    int row = point.y / getRowHeight();
    if (row<0 || model.getRowCount()<=row)
      return retValue;

    int column = getColumnModel().getColumnIndexAtX(point.x);

    if (isVisible(row,column)) {
      retValue.setN1(row);
      retValue.setN2(column);
      return retValue;
    }
    retValue.setN2(column + getSpan(row,column).getN2());
    retValue.setN1(row + getSpan(row,column).getN1());
    return retValue;
  }


  /**
   * Method overrided to calculate row index according to the cell visibility (that is dependent from the cells span)
   */
  public final int rowAtPoint(Point point) {
    return rowColumnAtPoint(point).getN1();
  }


  /**
   * Method overrided to calculate column index according to the cell visibility (that is dependent from the cells span)
   */
  public final int columnAtPoint(Point point) {
    return rowColumnAtPoint(point).getN2();
  }


  /**
   * Invoked when editing is finished. The changes are saved and the
   * editor is discarded.
   * <p>
   * Application code will not use these methods explicitly, they
   * are used internally by JTable.
   *
   * @param  e  the event received
   * @see CellEditorListener
   *
   * This method has been overriden to restore focus in the just edited cell when its value is not valid.
   */
  public void editingStopped(ChangeEvent e) {
      // Take in the new value
      TableCellEditor editor = getCellEditor();
      if (editor != null) {
          Object value = editor.getCellEditorValue();
          boolean ok = model.setValue(value, editingRow, convertColumnIndexToModel(editingColumn));
          final int row = editingRow;
          final int col = editingColumn;
          removeEditor();
          if (!ok) {
            SwingUtilities.invokeLater(new Runnable() {
              public void run() {
                editCellAt(row,col);
              }
            });
          }
      }
  }


  /**
   * Method invoked by Grid.applyProfile to reset column visibility, position, width.
   */
  public final void resetColumns(GridProfile profile) {
    String[] attrs = profile.getColumnsAttribute();
    Hashtable aux = new Hashtable();
    for(int i=0;i<attrs.length;i++)
      aux.put(attrs[i],new Integer(i-fromColIndex));
    Hashtable map = new Hashtable();
    for(int i=fromColIndex;i<toColIndex;i++) {
      map.put(
        aux.get(colProps[i].getColumnName()),
        new Integer(i)
      );
    }
    // remove all columns...
    TableColumnModel colsModel = this.getColumnModel();
    while(colsModel.getColumnCount()>0)
      colsModel.removeColumn(colsModel.getColumn(0));

    // add visible columns only...
    int index;
    for(int i=0;i<map.size();i++) {
      index = ((Integer)map.get(new Integer(i))).intValue();
      if (colProps[index].isColumnVisible()) {
        this.addColumn(tableColumnModel[index]);
        tableColumnModel[index].setPreferredWidth(profile.getColumnsWidth()[index]);
      }
    }
  }


  /**
   * @return the selected index in the input control
   */
  public final int getSelectedIndex() {
    return getSelectedRow();
  }


  /**
   * Set the selected index.
   */
  public final void setSelectedIndex(int index) {
    setRowSelectionInterval(index,index);
  }


  /**
   * @return total rows count in the input control
   */
  public final int getRowCount() {
    return model==null?0:model.getRowCount();
  }


  /**
   * @return the element at the specified index, converted in String format
   */
  public final String getValueAt(int index) {
      if (getSelectedColumn() == -1) {
        return "";
      }
    try {
      Object obj = getValueAt(index,getSelectedColumn());
      if (obj!=null && obj instanceof Number)
        return obj.toString();
      JLabel l = (JLabel)getCellRenderer(index,getSelectedColumn()).getTableCellRendererComponent(
        this,
        getValueAt(index,getSelectedColumn()),
        false,
        false,
        index,
        getSelectedColumn()
      );
      return l.getText();
    }
    catch (Exception ex) {
    }
    return getValueAt(index,getSelectedColumn())==null?"":getValueAt(index,getSelectedColumn()).toString();
  }


  /**
   * @return combo control
   */
  public final JComponent getComponent() {
    return this;
  }


  /**
   * @return <code>true</code> if the input control is in read only mode (so search is enabled), <code>false</code> otherwise
   */
  public final boolean isReadOnly() {
    return getMode()==Consts.READONLY;
  }


  /**
   * @return attribute name of the column declared as expandable, i.e. user can click on it to expand cell to show an inner component
   */
  public final String getExpandableColumnAttributeName() {
    return expandableColumnAttributeName;
  }


  /**
   * @return define whether expanded rows in the past must be collapsed when expanding the current one; used only when "expandableColumn" property is not null
   */
  public final boolean isSingleExpandableRow() {
    return singleExpandableRow;
  }


  /**
   * @return controller that manages row expansion
   */
  public final ExpandableRowController getExpandableRowController() {
    return expandableRowController;
  }


  /**
   * @return define whether the row to show, when expanding the current one, must be showed over the current one on in a new row below it; used only when "overwriteRowWhenExpanding" property is not null
   */
  public final boolean isOverwriteRowWhenExpanding() {
    return overwriteRowWhenExpanding;
  }


//  public boolean isFocusable() {
//    if (grids.getCurrentNestedComponent()!=null)
//      return false;
//    return super.isFocusable();
//  }




  /**
   * @param row row number
   * @param col column niumber in grid
   * @return TableCellRenderer associated to the specified cell
   */
  public final TableCellRenderer getCellRenderer(int row, int col) {
    TableCellRenderer rend = super.getCellRenderer(row,col);
    int expandableColumn = modelAdapter.getFieldIndex(expandableColumnAttributeName);

    if (expandableRowController!=null &&
        expandableColumnAttributeName!=null &&
        expandableRenderer==null) {
      expandableRenderer = new ExpandableRenderer(this,grids,expandableColumn,modelAdapter);
      setRowHeightFixed(false);
    }

    if (expandableRenderer!=null) {
      expandableRenderer.setDefaultCellRenderer(rend);
      return expandableRenderer;
    }

    return rend;
  }


  /**
   * <p>Title: OpenSwing Framework</p>
   * <p>Description: Inner class that represent a couple of integers.</p>
   * <p>Copyright: Copyright (C) 2006 Mauro Carniel</p>
   * @author Mauro Carniel
   * @version 1.0
   */
  class Pair extends Object {

    private int n1;
    private int n2;

    public Pair(int n1,int n2) {
      this.n1 = n1;
      this.n2 = n2;
    }


    public int getN1() {
      return n1;
    }


    public int getN2() {
      return n2;
    }


    public void setN1(int n1) {
      this.n1=n1;
    }


    public void setN2(int n2) {
      this.n2=n2;
    }


    public final int hashCode() {
      return n1*n2;
    }


    public boolean equals(Object obj) {
      return
          obj!=null &&
          obj instanceof Pair &&
          ((Pair)obj).getN1()==n1 &&
          ((Pair)obj).getN2()==n2;
    }



  }



}
