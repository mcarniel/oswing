package org.openswing.swing.tree.client;

import java.util.*;

import java.awt.*;
import java.awt.datatransfer.*;
import java.awt.dnd.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.text.*;
import javax.swing.tree.*;

import org.openswing.swing.message.receive.java.*;
import org.openswing.swing.tree.java.*;
import org.openswing.swing.util.client.*;
import javax.swing.event.TreeSelectionListener;
import javax.swing.event.TreeWillExpandListener;
import javax.swing.event.TreeExpansionListener;
import javax.swing.event.TreeModelListener;
import javax.swing.event.TreeModelEvent;

/**
 * <p>Title: OpenSwing Framework</p>
 * <p>Description: Panel that contains an expandable tree.
*  Nodes may be dragged inside the tree if enabledDrag method is invoked.</p>
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
public class TreePanel extends JPanel implements DragSourceListener, DropTargetListener, SearchControl {

  static {
    UIManager.getDefaults().put("Tree.selectionBackground",new javax.swing.plaf.ColorUIResource(ClientSettings.TREE_SELECTION_BACKGROUND));
    UIManager.getDefaults().put("Tree.selectionForeground",new javax.swing.plaf.ColorUIResource(ClientSettings.TREE_SELECTION_FOREGROUND));
  }

  /** current checked nodes (i.e. nodes having selected the associated check-box) */
  private HashSet checkedNodes = new HashSet();

  /** tree selection background color */
  private Color selectionBackground = ClientSettings.TREE_SELECTION_BACKGROUND;

  /** tree selection foreground color */
  private Color selectionForeground = ClientSettings.TREE_SELECTION_FOREGROUND;

  /** expandable tree */
  private JTree tree = new JTree(new OpenSwingTreeNode()){

      public TreePath getNextMatch(String prefix, int startingRow,
                                 Position.Bias bias) {
        try {
          return super.getNextMatch(prefix, startingRow, bias);
        }
        catch (Exception ex) {
          return null;
        }
      }


      public void setModel(TreeModel model) {
        super.setModel(model);
        try {
          if (checkedNodes != null) {
            checkedNodes.clear();
          }
        }
        catch (Exception ex) {
        }
      }

    };


  /** tree container */
  private JTable table = new JTable();

  /** tree model */
  private DefaultTreeModel treeModel = new DefaultTreeModel(new OpenSwingTreeNode());

  /** tree container */
  private JScrollPane treePane = new JScrollPane();

  /** data source used to fill in the tree */
  private TreeDataLocator treeDataLocator;

  /** tree controller: it manages tree events */
  private TreeController treeController;

  /** pop-up menu related to right mouse click on a tree node (optional) */
  private JPopupMenu popup = new JPopupMenu();

  /** collection of pairs: menu item description (not yet translated), menu item object; used to change the menu item abilitation */
  private Hashtable menuItems = new Hashtable();

  /** flag used inside addNotify method */
  private boolean firstTime = true;

  /** image icon used for leaves; default value: as for folders */
  private String leavesImageName = ClientSettings.getInstance().PERC_TREE_FOLDER;

  /** define if tree will be filled on viewing this panel; default value: true */
  private boolean loadWhenVisibile = true;

  /** define if all tree nodes must be expanded after loading */
  private boolean expandAllNodes = false;

  /** folder node image name */
  private String folderIconName = ClientSettings.PERC_TREE_FOLDER;

  /** drag source */
  private DragSource dragSource = null;

  /** drop gestures */
  private DropTarget dropTarget = null;

  /** drag 'n' drop listener */
  private TreeDragNDropListener dndListener = null;

  /** tree identifier, used for DnD */
  private String treeId = null;

  /** attribute name that contains the icon image name; default value: null; if defined, this attribute overrides "folderIcon"/"leafIcon" values */
  private String iconAttributeName;

  /** attribute name that contains the tool tip text for the node; default value: null */
  private String tooltipAttributeName;

  /** search manager */
  private SearchWindowManager searchWindowManager;

  /**  Determines whether or not the root node from the <code>TreeModel</code> is visible; default value: <code>true</code> */
  private boolean rootVisible = true;

  /** tree selection mode; allowed values: <code>TreeSelectionModel.SINGLE_TREE_SELECTION</code>,<code>TreeSelectionModel.CONTIGUOUS_TREE_SELECTION</code> or <code>TreeSelectionModel.DISCONTIGUOUS_TREE_SELECTION</code>. Default value:  <code>TreeSelectionModel.SINGLE_TREE_SELECTION </code> */
  private int selectionMode = TreeSelectionModel.SINGLE_TREE_SELECTION;

  /** tree row height */
  private int rowHeight = tree.getRowHeight();

  /** specifies whether the node handles should be displayed; default value: <code>true</code> */
  private boolean showsRootHandles = true;

  /** list of KeyListener objects added to the tree */
  private ArrayList keyListeners = new ArrayList();

  /** list of MouseListener objects added to the tree */
  private ArrayList mouseListeners = new ArrayList();

  /** cursor to show on dragging */
  private Cursor dragCursor = null;

  /** define if a check-box must be showed for each node; default value: <code>false</code> */
  private boolean showCheckBoxes = false;

  /** define if a check-box must be showed for leaves nodes too; default value: <code>true</code> */
  private boolean showCheckBoxesOnLeaves = true;

  /** list of ItemListener objects added to the check-box tree */
  private ArrayList itemListeners = new ArrayList();

  /** define if root node must be automatically expanded when "expandAllNodes" property is set to <code>false</code>; default value: <code>true</code> */
  private boolean expandRoot = true;

  /** flag used to mark the state "tree content changed" */
  private boolean treeChanged = false;


  /**
   * Constructor.
   */
  public TreePanel() {
    try {
      tree.setRootVisible(rootVisible);
      tree.setShowsRootHandles(showsRootHandles);
      jbInit();
      dragCursor = Toolkit.getDefaultToolkit().createCustomCursor(
        ClientUtils.getImage("drag.gif"),
        new Point(15, 10),
        ClientSettings.getInstance().getResources().getResource("drag")
      );
    }
    catch (Exception ex) {
      ex.printStackTrace();
    }
  }

  public void addNotify() {
    super.addNotify();
    if (firstTime) {
      if (loadWhenVisibile) {
        firstTime = false;
        SwingUtilities.invokeLater(new Runnable() {
          public void run() {
            createTree();
          }
        });
      }
      else
        recreateTree();
    }
  }

  /**
   * Force tree reloading.
   */
  public final void reloadTree() {
   SwingUtilities.invokeLater(new Runnable() {
      public void run() {
        if (!loadWhenVisibile && firstTime) {
          firstTime = false;
          createTree();
          if (expandAllNodes)
            expandAllNodes();
          else if (expandRoot && rootVisible)
            tree.expandRow(0);
        }
        else {

          ClientUtils.fireBusyEvent(true);
          try {
            ClientUtils.getParentWindow(TreePanel.this).setCursor(new java.awt.Cursor(java.awt.Cursor.WAIT_CURSOR));
            ClientUtils.getParentWindow(TreePanel.this).getToolkit().sync();
          }
          catch (Exception ex) {
          }

          Response response = null;
          try {
            response = treeDataLocator.getTreeModel(tree);
          }
          finally {
            try {
              ClientUtils.getParentWindow(TreePanel.this).setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
              ClientUtils.getParentWindow(TreePanel.this).getToolkit().sync();
            }
            catch (Exception ex1) {
            }
            ClientUtils.fireBusyEvent(false);
          }

          if (response.isError())
            treeModel = new DefaultTreeModel(new OpenSwingTreeNode());
          else
            treeModel = (DefaultTreeModel) ( (VOResponse) response).getVo();
          tree.setModel(treeModel);
          treeDataLocator.loadDataCompleted(response.isError());
          tree.revalidate();
          if (expandAllNodes)
            expandAllNodes();
          else if (expandRoot && rootVisible)
            tree.expandRow(0);
        }
      }
    });
  }


  /**
   * Expand a tree node.
   * @param index index of node to expand
   */
  public final void expandNode(int index) {
    tree.expandRow(index);
  }


  /**
   * Collapse a tree node.
   * @param index index of node to collapse
   */
  public final void collapseNode(int index) {
    tree.collapseRow(index);
  }


  /**
   * Expand all tree nodes.
   */
  public final void expandAllNodes() {
    int i = 0;
    try {
      while (i < tree.getRowCount()) {
        tree.expandRow(i++);
      }
    }
    catch (Exception ex) {
    }
  }


  /**
   * Collapse all tree nodes.
   */
  public final void collapseAllNodes() {
    int i = tree.getRowCount()-1;
    while (i >0)
      tree.collapseRow(i--);
  }


  void jbInit() throws Exception {
    this.setLayout(new java.awt.BorderLayout());
    treePane.getViewport().add(tree, null);
    this.add(treePane, BorderLayout.CENTER);
  }

  /**
   * Redraw the tree. Used when the tree model has been modified.
   */
  public final void repaintTree() {
    TreePath selPath = tree.getSelectionPath();
    treePane.getViewport().remove(tree);

    MouseMotionListener[] mml = null;
    MouseWheelListener[] mwl = null;
    TreeSelectionListener[] tsl = null;
    TreeWillExpandListener[] twl = null;
    TreeExpansionListener[] tel = null;
    if (tree!=null) {
      mml = tree.getMouseMotionListeners();
      mwl = tree.getMouseWheelListeners();
      tsl = tree.getTreeSelectionListeners();
      twl = tree.getTreeWillExpandListeners();
      tel = tree.getTreeExpansionListeners();
    }

    tree = new JTree(treeModel) {

      public TreePath getNextMatch(String prefix, int startingRow,
                                 Position.Bias bias) {
        try {
          return super.getNextMatch(prefix, startingRow, bias);
        }
        catch (Exception ex) {
          return null;
        }
      }

      public void setModel(TreeModel model) {
        super.setModel(model);
        try {
          if (checkedNodes != null) {
            checkedNodes.clear();
          }
        }
        catch (Exception ex) {
        }
      }

    };

    searchWindowManager = new SearchWindowManager(this);

    for(int i=0;i<keyListeners.size();i++)
      tree.addKeyListener((KeyListener)keyListeners.get(i));
    for(int i=0;i<mouseListeners.size();i++)
      tree.addMouseListener((MouseListener)mouseListeners.get(i));

    if (mml!=null)
       for(int i=0;i<mml.length;i++)
         tree.addMouseMotionListener(mml[i]);
     if (mwl!=null)
       for(int i=0;i<mwl.length;i++)
         tree.addMouseWheelListener(mwl[i]);
    if (tsl!=null)
       for(int i=0;i<tsl.length;i++)
         tree.addTreeSelectionListener(tsl[i]);
    if (twl!=null)
       for(int i=0;i<twl.length;i++)
         tree.addTreeWillExpandListener(twl[i]);
    if (tel!=null)
       for(int i=0;i<tel.length;i++)
         tree.addTreeExpansionListener(tel[i]);



    if (treeId != null && dndListener != null)
      enableDrag(treeId, dndListener);

    recreateTree();
    tree.repaint();
    try {
      tree.setSelectionPath(selPath.getParentPath());
    }
    catch (Exception ex) {
    }
    try {
      tree.setSelectionPath(selPath);
    }
    catch (Exception ex) {
    }
  }

  /**
   * Remove all nodes (expept the root node) from the tree.
   */
  public final void clearTree() {
    treeModel = new DefaultTreeModel(new OpenSwingTreeNode());
    repaintTree();
  }

  /**
   * Fill in the tree.
   */
  private void createTree() {
    ClientUtils.fireBusyEvent(true);
    try {
      ClientUtils.getParentWindow(TreePanel.this).setCursor(new java.awt.Cursor(java.awt.Cursor.WAIT_CURSOR));
      ClientUtils.getParentWindow(TreePanel.this).getToolkit().sync();
    }
    catch (Exception ex) {
    }

    Response response = null;
    try {
      response = treeDataLocator.getTreeModel(tree);
    }
    finally {
      try {
        ClientUtils.getParentWindow(TreePanel.this).setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        ClientUtils.getParentWindow(TreePanel.this).getToolkit().sync();
      }
      catch (Exception ex1) {
      }
      ClientUtils.fireBusyEvent(false);
    }

    if (response.isError())
      treeModel = new DefaultTreeModel(new OpenSwingTreeNode());
    else
      treeModel = (DefaultTreeModel) ( (VOResponse) response).getVo();
    recreateTree();
    treeDataLocator.loadDataCompleted(response.isError());

    if (expandAllNodes)
      expandAllNodes();
    else if (expandRoot && rootVisible)
      tree.expandRow(0);
  }

  private void recreateTree() {
    try {
      TreeNodeRenderer renderer = new TreeNodeRenderer(
        this,
        folderIconName,
        leavesImageName,
        iconAttributeName,
        tooltipAttributeName
      );
      tree.setCellRenderer(renderer);

    }
    catch (Exception ex) {
      ex.printStackTrace();
    }

    tree.setRootVisible(rootVisible);
    tree.setToolTipText("");
    tree.setModel(treeModel);
    tree.revalidate();

    if (searchWindowManager == null)
      searchWindowManager = new SearchWindowManager(this);

    tree.getSelectionModel().setSelectionMode(selectionMode);
    tree.setRowHeight(rowHeight);
    tree.setShowsRootHandles(showsRootHandles);

    tree.setSize(new Dimension( (int)this.getPreferredSize().getWidth() / 2,
                               (int)this.getPreferredSize().getHeight()));

    treePane.getViewport().add(tree);
    treePane.setBorder(BorderFactory.createCompoundBorder(
        BorderFactory.createRaisedBevelBorder(),
        BorderFactory.createLoweredBevelBorder()
        ));
    treePane.setAutoscrolls(true);

    tree.setMinimumSize(new Dimension(0, 200));

    dropTarget = new DropTarget(tree, this);

    MouseListener ml = new MouseAdapter() {
      public void mouseClicked(MouseEvent e) {
        if (e.getClickCount() == 1 && SwingUtilities.isLeftMouseButton(e))
          treeLeftClick(e, tree);
        else if (e.getClickCount() == 1 && SwingUtilities.isRightMouseButton(e))
          treeRightClick(e, tree);
        if (e.getClickCount() == 2)
          treeDoubleClick(e, tree);
      }
    };
    tree.addMouseListener(ml);

    treeChanged = false;
    treeModel.addTreeModelListener(new TreeModelListener() {

      public void treeNodesChanged(TreeModelEvent e) {
        treeChanged = true;
      }

      public void treeNodesInserted(TreeModelEvent e) {
        treeChanged = true;
      }

      public void treeNodesRemoved(TreeModelEvent e) {
        treeChanged = true;
      }

      public void treeStructureChanged(TreeModelEvent e) {
        treeChanged = true;
      }

    });
  }

  /**
   * @return selected node or null if no node is selected
   */
  public final DefaultMutableTreeNode getSelectedNode() {
    try {
      javax.swing.tree.TreePath selPath = tree.getSelectionPath();
      if (selPath != null)
        return (DefaultMutableTreeNode) (selPath.getPathComponent(selPath.
            getPathCount() - 1));
      else
        return null;
    }
    catch (Exception ex) {
      ex.printStackTrace();
      return null;
    }
  }

  /**
   * Select a node in the tree.
   * @param node tree node to select
   */
  public final void setSelectedNode(DefaultMutableTreeNode node) {
    try {
      tree.setSelectionPath(new TreePath(node.getPath()));
    }
    catch (Exception ex) {
    }
  }

  /**
   * Method called when user has double clicked.
   * @param e double click event
   * @param tree tree
   */
  public final void treeDoubleClick(MouseEvent e, JTree tree) {
    try {
      int selRow = tree.getRowForLocation(e.getX(), e.getY());
      javax.swing.tree.TreePath selPath = tree.getPathForLocation(e.getX(),
          e.getY());
      if (selPath != null) {
        DefaultMutableTreeNode node = (DefaultMutableTreeNode) (selPath.
            getPathComponent(selPath.getPathCount() - 1));
        treeController.doubleClick(node);

      }
    }
    catch (Exception ex) {
      ex.printStackTrace();
    }
  }

  /**
   * Method called when user has clicked on the left mouse button.
   * @param e left mouse button click event
   * @param tree tree
   */
  public final void treeLeftClick(MouseEvent e, JTree tree) {
    try {
      int selRow = tree.getRowForLocation(e.getX(), e.getY());
      javax.swing.tree.TreePath selPath = tree.getPathForLocation(e.getX(),
          e.getY());
      if (selPath != null) {
        DefaultMutableTreeNode node = (DefaultMutableTreeNode) (selPath.
            getPathComponent(selPath.getPathCount() - 1));
        treeController.leftClick(node);

      }
    }
    catch (Exception ex) {
      ex.printStackTrace();
    }
  }

  public void setEnabled(boolean enabled) {
    super.setEnabled(enabled);
    treePane.setEnabled(enabled);
  }

  /**
   * Method called when user has clicked on the right mouse button.
   * @param e right mouse button click event
   * @param tree tree
   */
  public final void treeRightClick(MouseEvent e, JTree tree) {
    try {
      if (this.selectionMode!=TreeSelectionModel.SINGLE_TREE_SELECTION &&
          tree.getSelectionRows().length>1) {
        int selRow = tree.getRowForLocation(e.getX(), e.getY());
//        javax.swing.tree.TreePath selPath = tree.getPathForLocation(e.getX(),e.getY());
//        tree.setSelectionPath(selPath);

        if (selRow!=-1 && popup.getComponentCount() > 0 && treePane.isEnabled()) {
          TreePath[] selPaths = tree.getSelectionPaths();
          DefaultMutableTreeNode node = null;
          boolean ok = true;
          for(int i=0;i<selPaths.length;i++) {
            node = (DefaultMutableTreeNode) (selPaths[i].getPathComponent(selPaths[i].getPathCount() - 1));
            ok = treeController.rightClick(node);
            if (!ok)
                break;
          }
          if (ok) {
            // visualizzazione del menu' a pop-up associato al nodo dell'albero,
            // SOLO se il metodo rightClick ha ritornato valore "true" e c'e almeno un elemento nel menu' a pop-up
            popup.show(e.getComponent(), e.getX(), e.getY());
          }
        }
      }
      else {
        int selRow = tree.getRowForLocation(e.getX(), e.getY());
        javax.swing.tree.TreePath selPath = tree.getPathForLocation(e.getX(),e.getY());
        tree.setSelectionPath(selPath);
        if (selPath != null) {
          DefaultMutableTreeNode node = (DefaultMutableTreeNode) (selPath.getPathComponent(selPath.getPathCount() - 1));
          if (treeController.rightClick(node) && popup.getComponentCount() > 0 &&
              treePane.isEnabled()) {
            // visualizzazione del menu' a pop-up associato al nodo dell'albero,
            // SOLO se il metodo rightClick ha ritornato valore "true" e c'e almeno un elemento nel menu' a pop-up
            popup.show(e.getComponent(), e.getX(), e.getY());
          }

        }
      }

    }
    catch (Exception ex) {
      ex.printStackTrace();
    }
  }


  /**
   * Add a pop-up menu item.
   * @param menuName menu item description (not yet translated)
   * @param mnemonic mnemonic value
   * @param enabled flag used to set menu item abilitation
   * @param menuListener listener used to capture menu item selection
   */
  public final void addPopupMenuItem(String menuName, char mnemonic,
                                     boolean enabled,
                                     ActionListener menuListener) {
    JMenuItem cbMenuItem = new JMenuItem(ClientSettings.getInstance().
                                         getResources().getResource(menuName));
    cbMenuItem.setMnemonic(mnemonic);
    cbMenuItem.setEnabled(enabled);
    cbMenuItem.addActionListener(menuListener);
    popup.add(cbMenuItem);
    menuItems.put(menuName, cbMenuItem);
  }


  /**
   * Add a pop-up menu item to a parent menu item.
   * @param menuName menu item description (not yet translated)
   * @param parentMenuName
   * @param mnemonic mnemonic value
   * @param enabled flag used to set menu item abilitation
   * @param menuListener listener used to capture menu item selection
   */
  public final void addPopupMenuItem(String menuName, String parentMenuName,char mnemonic,
                                     boolean enabled,
                                     ActionListener menuListener) {
    JMenuItem cbMenuItem = new JMenuItem(ClientSettings.getInstance().
                                         getResources().getResource(menuName));
    cbMenuItem.setMnemonic(mnemonic);
    cbMenuItem.setEnabled(enabled);
    cbMenuItem.addActionListener(menuListener);
    JMenuItem parentItem = (JMenuItem)menuItems.get(parentMenuName);
    if (parentItem!=null) {
      parentItem.add(cbMenuItem);
      menuItems.put(menuName, cbMenuItem);
    }
  }


  /**
   * Add a separator to the  pop-up menu.
   */
  public final void addPopupSeparator() {
    popup.add(new JSeparator());
  }


  /**
   * Set menu item abilitation.
   * @param menuName menu item description (not yet translated)
   * @param enabled flag used to enable the menu item
   */
  public final void setMenuItemEnabled(String menuName, boolean enabled) {
    JMenuItem menu = (JMenuItem) menuItems.get(menuName);
    if (menu != null)
      menu.setEnabled(enabled);
  }


  /**
   * Set menu item visibility.
   * @param menuName menu item description (not yet translated)
   * @param visible flag used to hide/show the menu item
   */
  public final void setMenuItemVisible(String menuName, boolean visible) {
    JMenuItem menu = (JMenuItem) menuItems.get(menuName);
    if (menu != null)
      menu.setVisible(visible);
  }


  /**
   * @return tree controller: it manages tree events
   */
  public final TreeController getTreeController() {
    return treeController;
  }

  /**
   * @return data source used to fill in the tree
   */
  public final TreeDataLocator getTreeDataLocator() {
    return treeDataLocator;
  }

  /**
   * Set the data source used to fill in the tree
   * @param treeDataLocator data source used to fill in the tree
   */
  public final void setTreeDataLocator(TreeDataLocator treeDataLocator) {
    this.treeDataLocator = treeDataLocator;
  }

  /**
   * Set the tree controller: it manages tree events.
   * @param treeController tree controller: it manages tree events.
   */
  public final void setTreeController(TreeController treeController) {
    this.treeController = treeController;
  }

  /**
   * @return image icon used for leaves
   */
  public final String getLeavesImageName() {
    return leavesImageName;
  }

  /**
   * Set image icon used for leaves.
   * @param leavesImageName image icon used for leaves
   */
  public final void setLeavesImageName(String leavesImageName) {
    this.leavesImageName = leavesImageName;
  }

  /**
   * @return define if tree will be filled on viewing this panel
   */
  public final boolean isLoadWhenVisibile() {
    return loadWhenVisibile;
  }

  /**
   * Define if tree will be filled on viewing this panel.
   * @param loadWhenVisibile define if tree will be filled on viewing this panel
   */
  public final void setLoadWhenVisibile(boolean loadWhenVisibile) {
    this.loadWhenVisibile = loadWhenVisibile;
  }

  /**
   * @return boolean define if all tree nodes must be expanded after loading
   */
  public final boolean isExpandAllNodes() {
    return expandAllNodes;
  }

  /**
   * Define if all tree nodes must be expanded after loading.
   * @param expandAllNodes boolean define if all tree nodes must be expanded after loading
   */
  public final void setExpandAllNodes(boolean expandAllNodes) {
    this.expandAllNodes = expandAllNodes;
  }

  /**
   * @return folder icon name
   */
  public final String getFolderIconName() {
    return folderIconName;
  }

  /**
   * Set the folder icon name.
   * @param treeFolderName folder icon name
   */
  public final void setFolderIconName(String folderIconName) {
    this.folderIconName = folderIconName;
  }

  /**
   * @return tree
   */
  public final JTree getTree() {
    return tree;
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
  public final void enableDrag(String treeId, TreeDragNDropListener dndListener) {
    try {
      dragSource = new DragSource();
      dragSource.createDefaultDragGestureRecognizer(
          tree,
          DnDConstants.ACTION_MOVE,
          new DragGestureAdapter(this)
      );
    }
    catch (Exception ex) {
      ex.printStackTrace();
    }
    this.treeId = treeId;
    this.dndListener = dndListener;
  }

  class DragGestureAdapter
      implements DragGestureListener {

    private DragSourceListener dragListener = null;

    public DragGestureAdapter(DragSourceListener dragListener) {
      this.dragListener = dragListener;
    }

    /**
     * A drag gesture has been initiated.
     */
    public final void dragGestureRecognized(DragGestureEvent event) {
      if (dndListener.dragEnabled()) {
        tree.setCursor(dragCursor);
        dragSource.startDrag(event, DragSource.DefaultMoveDrop,
                             new StringSelection(treeId), dragListener);
      }
      else
        // drag interrupted...
        event.getSourceAsDragGestureRecognizer().resetRecognizer();
    }

  }

  /**
   * This message goes to DragSourceListener, informing it that the dragging has entered the DropSite
   */
  public final void dragEnter(DragSourceDragEvent event) {
    tree.setCursor(Cursor.getDefaultCursor());
    dndListener.dragEnter();
  }

  /**
   * This message goes to DragSourceListener, informing it that the dragging has exited the DropSite.
   */
  public final void dragExit(DragSourceEvent event) {
    tree.setCursor(Cursor.getDefaultCursor());
    dndListener.dragExit();
  }

  /**
   * This message goes to DragSourceListener, informing it that the dragging is currently ocurring over the DropSite.
   */
  public final void dragOver(DragSourceDragEvent e) {
    tree.setCursor(dragCursor);
    Point loc = tree.getLocationOnScreen();
    int row = (e.getY()-loc.y)/tree.getRowHeight();
    int firstVisibleRow = tree.getVisibleRect().y/tree.getRowHeight();
    int lastVisibleRow = (tree.getVisibleRect().y+tree.getVisibleRect().height)/tree.getRowHeight();
    if (row<=firstVisibleRow+1)
      tree.scrollRowToVisible(row-1);
    else if (row>=lastVisibleRow-1)
      tree.scrollRowToVisible(row+1);

    dndListener.dragOver();
  }

  /**
   * This method is invoked when the user changes the dropAction.
   */
  public final void dropActionChanged(DragSourceDragEvent event) {}

  /**
   * This message goes to DragSourceListener, informing it that the dragging has ended.
   */
  public final void dragDropEnd(DragSourceDropEvent event) {
    dndListener.dragDropEnd();
  }

  /************************************************************
   * DROP MANAGEMENT
   ************************************************************/

  /**
   * This method is invoked when you are dragging over the DropSite.
   */
  public final void dragEnter(DropTargetDragEvent event) {
    event.acceptDrag(DnDConstants.ACTION_MOVE);
    dndListener.dropEnter();
  }

  /**
   * This method is invoked when you are exit the DropSite without dropping.
   */
  public final void dragExit(DropTargetEvent event) {
    try {
      dndListener.dropExit();
    }
    catch (Exception ex) {
      ex.printStackTrace();
    }

  }

  /**
   * This method is invoked when a drag operation is going on.
   */
  public final void dragOver(DropTargetDragEvent event) {
    dndListener.dropOver();
  }

  /**
   * This method is invoked when a drop event has occurred.
   */
  public final void drop(DropTargetDropEvent event) {
    try {
      tree.setCursor(Cursor.getDefaultCursor());

      Transferable transferable = event.getTransferable();
      DefaultMutableTreeNode node = (DefaultMutableTreeNode) tree.
          getClosestPathForLocation(event.getLocation().x,
                                    event.getLocation().y).getLastPathComponent();

      if (node != null &&
          transferable.isDataFlavorSupported(DataFlavor.stringFlavor)) {
        if (dndListener.dropEnabled(node,
                                    (String) transferable.getTransferData(DataFlavor.
            stringFlavor))) {
          event.acceptDrop(DnDConstants.ACTION_MOVE);
          event.getDropTargetContext().dropComplete(true);
        }
        else
          event.rejectDrop();

      }
      else {
        event.rejectDrop();
      }
    }
    catch (Exception ex) {
      ex.printStackTrace();
      event.rejectDrop();
    }
    finally {
    }
  }

  /**
   * This method is invoked if the use modifies the current drop gesture.
   */
  public final void dropActionChanged(DropTargetDragEvent event) {
    dndListener.dropActionChanged();
  }


  /************************************************************
   * END DROP MANAGEMENT
   ************************************************************/




  /**
   * Set the attribute name that contains the icon name; default value: null; if defined, this attribute overrides "folderIcon"/"leafIcon" values.
   * @param iconAttributeName attribute name that contains the icon name
   */
  public final void setIconAttributeName(String iconAttributeName) {
    this.iconAttributeName = iconAttributeName;
  }

  /**
   * @return attribute name that contains the icon name (optional)
   */
  public final String getIconAttributeName() {
    return iconAttributeName;
  }

  /**
   * @return attribute name that contains the tool tip text for the node
   */
  public final String getTooltipAttributeName() {
    return tooltipAttributeName;
  }

  /**
   * Set the attribute name that contains the tool tip text for the node; default value: null.
   * @param tooltipAttributeName attribute name that contains the tool tip text for the node
   */
  public final void setTooltipAttributeName(String tooltipAttributeName) {
    this.tooltipAttributeName = tooltipAttributeName;
  }

  /**
   * @return the selected index in the input control
   */
  public final int getSelectedIndex() {
    if (tree.getSelectionRows() == null)
      return -1;
    if (tree.getSelectionRows().length > 0)
      return tree.getSelectionRows()[0];
    else
      return -1;
  }

  /**
   * Set the selected index.
   */
  public final void setSelectedIndex(int index) {
    tree.setSelectionRow(index);
    try {
      tree.scrollRowToVisible(index);
    }
    catch (Exception ex) {
    }
  }

  /**
   * @return total rows count in the input control
   */
  public final int getRowCount() {
    return tree.getRowCount();
  }

  /**
   * @return the element at the specified index, converted in String format
   */
  public final String getValueAt(int index) {
    try {
      JPanel p = (JPanel)tree.getCellRenderer().getTreeCellRendererComponent(
          tree,
          tree.getPathForRow(index).getLastPathComponent(),
          false,
          false,
          tree.getModel().isLeaf(tree.getPathForRow(index).getLastPathComponent()),
          index,
          false
          );
      JLabel l = null;
      if (p.getComponent(0) instanceof JLabel)
        l = (JLabel)p.getComponent(0);
      else if (p.getComponent(1) instanceof JLabel)
        l = (JLabel)p.getComponent(1);
      if (l!=null)
        return l.getText();
      else
        return "";
    }
    catch (Exception ex) {
      Object obj = tree.getPathForRow(index).getLastPathComponent();
      return obj == null ? "" : obj.toString();
    }
  }

  /**
   * @return combo control
   */
  public final JComponent getComponent() {
    return tree;
  }

  /**1
   * @return <code>true</code> if the input control is in read only mode (so search is enabled), <code>false</code> otherwise
   */
  public final boolean isReadOnly() {
    return true;
  }


  /**
   * @return <code>true</code> to disable key listening on input control (for instance, in case of nested grids), <code>false</code> to listen for key events
   */
  public final boolean disableListener() {
    return false;
  }


  /**
   * Returns true if the root node of the tree is displayed.
   *
   * @return true if the root node of the tree is displayed
   * @see #rootVisible
   */
  public final boolean isRootVisible() {
    return rootVisible;
  }

  /**
   * Determines whether or not the root node from
   * the <code>TreeModel</code> is visible.
   *
   * @param rootVisible true if the root node of the tree is to be displayed
   * @see #rootVisible
   * @beaninfo
   *        bound: true
   *  description: Whether or not the root node
   *               from the TreeModel is visible.
   */
  public final void setRootVisible(boolean rootVisible) {
    this.rootVisible = rootVisible;
    tree.setRootVisible(rootVisible);
  }

  /**
   * Sets the selection model, which must be one of TreeSelectionModel.SINGLE_TREE_SELECTION,
   * TreeSelectionModel.CONTIGUOUS_TREE_SELECTION or TreeSelectionModel.DISCONTIGUOUS_TREE_SELECTION.
   * <p>
   * This may change the selection if the current selection is not valid
   * for the new mode. For example, if three TreePaths are
   * selected when the mode is changed to <code>TreeSelectionModel.SINGLE_TREE_SELECTION</code>,
   * only one TreePath will remain selected. It is up to the particular
   * implementation to decide what TreePath remains selected.
   */
  public final void setSelectionMode(int selectionMode) {
    this.selectionMode = selectionMode;
    tree.getSelectionModel().setSelectionMode(selectionMode);
    tree.setRowHeight(0);
  }

  /**
   * Returns the current selection mode, one of
   * <code>TreeSelectionModel.SINGLE_TREE_SELECTION</code>,
   * <code>TreeSelectionModel.CONTIGUOUS_TREE_SELECTION</code> or
   * <code>TreeSelectionModel.DISCONTIGUOUS_TREE_SELECTION</code>.
   */
  public final int getSelectionMode() {
    return selectionMode;
  }

  /**
   * Sets the height of each cell, in pixels.  If the specified value
   * is less than or equal to zero the current cell renderer is
   * queried for each row's height.
   *
   * @param rowHeight the height of each cell, in pixels
   * @beaninfo
   *        bound: true
   *  description: The height of each cell.
   */
  public final void setRowHeight(int rowHeight) {
    this.rowHeight = rowHeight;
    tree.setRowHeight(rowHeight);
  }

  /**
   * Returns the height of each row.  If the returned value is less than
   * or equal to 0 the height for each row is determined by the
   * renderer.
   *
   */
  public final int getRowHeight() {
    return rowHeight;
  }

  /**
   * Sets the value of the <code>showsRootHandles</code> property,
   * which specifies whether the node handles should be displayed.
   * The default value of this property depends on the constructor
   * used to create the <code>JTree</code>.
   * Some look and feels might not support handles;
   * they will ignore this property.
   * @param newValue <code>true</code> if root handles should be displayed;
   *                 otherwise, <code>false</code>
   * @see #showsRootHandles
   * @see #getShowsRootHandles
   * @beaninfo bound: true
   *  description: Whether the node handles are to be displayed.
   */
  public final void setShowsRootHandles(boolean showsRootHandles) {
    this.showsRootHandles = showsRootHandles;
    tree.setShowsRootHandles(showsRootHandles);
  }

  /**
   * Returns the value of the <code>showsRootHandles</code> property.
   * @return the value of the <code>showsRootHandles</code> property
   * @see #showsRootHandles
   */
  public final boolean getShowsRootHandles() {
    return showsRootHandles;
  }

  /**
   * Scrolls the item identified by row until it is displayed. The minimum
   * of amount of scrolling necessary to bring the row into view
   * is performed. Only works when this <code>JTree</code> is contained in a
   * <code>JScrollPane</code>.
   *
   * @param row  an integer specifying the row to scroll, where 0 is the
   *             first row in the display
   */
  public final void scrollRowToVisible(int row) {
    tree.scrollRowToVisible(row);
  }


  /**
   * Selects the node at the specified row in the display.
   *
   * @param row  the row to select, where 0 is the first row in
   *             the display
   */
  public final void setSelectionRow(int row) {
    tree.setSelectionRow(row);
  }


  /**
   * Selects the nodes corresponding to each of the specified rows
   * in the display. If a particular element of <code>rows</code> is
   * < 0 or >= <code>getRowCount</code>, it will be ignored.
   * If none of the elements
   * in <code>rows</code> are valid rows, the selection will
   * be cleared. That is it will be as if <code>clearSelection</code>
   * was invoked.
   *
   * @param rows  an array of ints specifying the rows to select,
   *              where 0 indicates the first row in the display
   */
  public final void setSelectionRows(int[] rows) {
    tree.setSelectionRows(rows);
  }

  /**
   * Selects the node identified by the specified path. If any
   * component of the path is hidden (under a collapsed node), and
   * <code>getExpandsSelectedPaths</code> is true it is
   * exposed (made viewable).
   *
   * @param path the <code>TreePath</code> specifying the node to select
   */
  public final void setSelectionPath(TreePath path) {
    tree.setSelectionPath(path);
  }

  /**
   * Selects the nodes identified by the specified array of paths.
   * If any component in any of the paths is hidden (under a collapsed
   * node), and <code>getExpandsSelectedPaths</code> is true
   * it is exposed (made viewable).
   *
   * @param paths an array of <code>TreePath</code> objects that specifies
   *		the nodes to select
   */
  public final void setSelectionPaths(TreePath[] paths) {
    tree.setSelectionPaths(paths);
  }


  /**
   * Makes sure all the path components in path are expanded (except
   * for the last path component) and scrolls so that the
   * node identified by the path is displayed. Only works when this
   * <code>JTree</code> is contained in a <code>JScrollPane</code>.
   *
   * @param path  the <code>TreePath</code> identifying the node to
   * 		bring into view
   */
  public final void scrollPathToVisible(TreePath path) {
    tree.scrollPathToVisible(path);
  }


  /**
   * Returns the path to the first selected node.
   *
   * @return the <code>TreePath</code> for the first selected node,
   *		or <code>null</code> if nothing is currently selected
   */
  public final TreePath getSelectionPath() {
      return tree.getSelectionPath();
  }


  /**
   * Returns the paths of all selected values.
   *
   * @return an array of <code>TreePath</code> objects indicating the selected
   *         nodes, or <code>null</code> if nothing is currently selected
   */
  public final TreePath[] getSelectionPaths() {
    return tree.getSelectionPaths();
  }


  /**
   * Returns all of the currently selected rows. This method is simply
   * forwarded to the <code>TreeSelectionModel</code>.
   * If nothing is selected <code>null</code> or an empty array will
   * be returned, based on the <code>TreeSelectionModel</code>
   * implementation.
   *
   * @return an array of integers that identifies all currently selected rows
   *         where 0 is the first row in the display
   */
  public final int[] getSelectionRows() {
    return tree.getSelectionRows();
  }


  /**
   * Set tree selection background color
   * @param selectionBackground tree selection background color
   */
  public final void setSelectionBackground(Color selectionBackground) {
    this.selectionBackground = selectionBackground;
    UIManager.getDefaults().put("Tree.selectionBackground",new javax.swing.plaf.ColorUIResource(selectionBackground));
    tree.revalidate();
  }


  /**
   * Set tree selection foreground color
   * @param selectionForeground tree selection foreground color
   */
  public final void setSelectionForeground(Color selectionForeground) {
    this.selectionForeground = selectionForeground;
    UIManager.getDefaults().put("Tree.selectionForeground",new javax.swing.plaf.ColorUIResource(selectionForeground));
    tree.revalidate();
  }


  /**
   * @return tree selection background color
   */
  public final Color getSelectionBackground() {
    return selectionBackground;
  }


  /**
   * @return tree selection foreground color
   */
  public final Color getSelectionForeground() {
    return selectionForeground;
  }


  /**
   * Revalidate tree content.
   */
  public final void revalidateTree() {
    TreePath[] paths = tree.getSelectionPaths();
    boolean asksAllowsChildren = treeModel.asksAllowsChildren();
    treeModel = new DefaultTreeModel((DefaultMutableTreeNode)tree.getModel().getRoot(),asksAllowsChildren);
    tree.setModel(treeModel);
    if (paths!=null)
      tree.setSelectionPaths(paths);
  }


  /**
   * Add a key listener to the tree.
   * @param listener KeyListener to add
   */
  public final void addKeyListener(KeyListener listener) {
    keyListeners.add(listener);
  }


  /**
   * Remove a key listener from the tree.
   * @param listener KeyListener to remove
   */
  public final void removeKeyListener(KeyListener listener) {
    keyListeners.remove(listener);
  }


  /**
   * Add a mouse listener to the tree.
   * @param listener MouseListener to add
   */
  public final void addMouseListener(MouseListener listener) {
    mouseListeners.add(listener);
  }


  /**
   * Remove a mouse listener from the tree.
   * @param listener MouseListener to remove
   */
  public final void removeMouseListener(MouseListener listener) {
    mouseListeners.remove(listener);
  }


  /**
   * Add an Item Listener to the tree, that listen for check-box selections.
   * @param listener ItemListener to add
   */
  public final void addItemListener(ItemListener listener) {
    itemListeners.add(listener);
  }


  /**
   * Remove an ItemListener from the tree.
   * @param listener ItemListener to remove
   */
  public final void removeItemListener(ItemListener listener) {
     itemListeners.remove(listener);
  }


  /**
   * @return ItemListener objects added to this check-box tree
   */
  public final ItemListener[] getItemListeners() {
     return (ItemListener[])itemListeners.toArray(new ItemListener[itemListeners.size()]);
  }


  /**
   * @return define if a check-box must be showed for each node
   */
  public final boolean isShowCheckBoxes() {
    return showCheckBoxes;
  }


  /**
   * Define if a check-box must be showed for each node.
   * @param showCheckBoxes define if a check-box must be showed for each node
   */
  public final void setShowCheckBoxes(boolean showCheckBoxes) {
    this.showCheckBoxes = showCheckBoxes;
  }


  /**
   * @return current checked nodes (i.e. nodes having selected the associated check-box)
   */
  public final HashSet getCheckedNodes() {
    return checkedNodes;
  }


  /**
   * Set current checked nodes (i.e. nodes having selected the associated check-box).
   * @param checkedNodes current checked nodes (i.e. nodes having selected the associated check-box)
   */
  public void setCheckedNodes(HashSet checkedNodes) {
    this.checkedNodes = checkedNodes;
  }


  /**
   * @return define if a check-box must be showed for leaves nodes too
   */
  public final boolean isShowCheckBoxesOnLeaves() {
    return showCheckBoxesOnLeaves;
  }


  /**
   * Define if a check-box must be showed for leaves nodes too.
   * @param showCheckBoxesOnLeaves define if a check-box must be showed for leaves nodes too
   */
  public final void setShowCheckBoxesOnLeaves(boolean showCheckBoxesOnLeaves) {
    this.showCheckBoxesOnLeaves = showCheckBoxesOnLeaves;
  }


  /**
   * @return retrieve current leaves having their check-boxes selected
   */
  public final HashSet getCheckedLeaves() {
    HashSet set = new HashSet();
    if (showCheckBoxesOnLeaves) {
      Iterator it = checkedNodes.iterator();
      TreeNode node = null;
      while(it.hasNext()) {
        node = (TreeNode)it.next();
        if (checkedNodes.contains(node) && node.isLeaf())
          set.add(node);
      }
    }
    else {
      Iterator it = checkedNodes.iterator();
      TreeNode node = null;
      while(it.hasNext()) {
        node = (TreeNode)it.next();
        if (checkedNodes.contains(node)) {
          for(int i=0;i<node.getChildCount();i++)
            if (node.getChildAt(i).isLeaf())
              set.add(node.getChildAt(i));
        }
      }
    }
    return set;
  }


  /**
   * @return define if root node must be automatically expanded when "expandAllNodes" property is set to <code>false</code>
   */
  public final boolean isExpandRoot() {
    return expandRoot;
  }


  /**
   * Define if root node must be automatically expanded when "expandAllNodes" property is set to <code>false</code>; default value: <code>true</code>.
   * @param expandRoot define if root node must be automatically expanded when "expandAllNodes" property is set to <code>false</code>
   */
  public final void setExpandRoot(boolean expandRoot) {
    this.expandRoot = expandRoot;
  }


  /**
   * @return mark the state "tree content changed"
   */
  public final boolean isChanged() {
    return treeChanged;
  }


  /**
   * Method invoked by SearchWindowManager when the specified "textToSeach" pattern has not matchings in the current content
   * of binded control.
   * This callback can be used to retrieve additional data into control and to search inside new data.
   * @param textToSeach patten of text to search
   * @return -1 if no additional data is available, otherwise the row index of data just added that satify the pattern
   */
  public final int search(String textToSeach) {
    return -1;
  }


  /**
   * @return cursor to show on dragging
   */
  public final Cursor getDragCursor() {
    return dragCursor;
  }


  /**
   * Set the cursor to show on dragging.
   * @param dragCursor cursor to show on dragging
   */
  public final void setDragCursor(Cursor dragCursor) {
    this.dragCursor = dragCursor;
  }


}

