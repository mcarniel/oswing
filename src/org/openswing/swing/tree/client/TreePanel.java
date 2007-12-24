package org.openswing.swing.tree.client;

import java.lang.reflect.Method;
import java.lang.reflect.Constructor;
import java.awt.*;
import java.util.*;
import java.awt.event.*;
import javax.swing.event.*;
import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.*;
import javax.swing.tree.*;
import org.openswing.swing.util.client.ClientSettings;
import org.openswing.swing.message.receive.java.Response;
import org.openswing.swing.message.receive.java.VOResponse;
import org.openswing.swing.message.receive.java.ValueObject;
import java.lang.reflect.*;
import java.awt.dnd.*;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.DataFlavor;
import org.openswing.swing.util.client.SearchWindowManager;
import org.openswing.swing.util.client.SearchControl;


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

  /** expandable tree */
  private JTree tree = new JTree(new DefaultMutableTreeNode());

  /** tree container */
  private JTable table = new JTable();

  /** tree model */
  private DefaultTreeModel treeModel;

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



  /**
   * Constructor.
   */
  public TreePanel() {
    try {
      jbInit();
    }
    catch(Exception ex) {
      ex.printStackTrace();
    }
  }


  public void addNotify() {
    super.addNotify();
    if (firstTime && loadWhenVisibile) {
      firstTime = false;
      new Thread() {
        public void run() {
          createTree();
        }
      }.start();
    }
  }


  /**
   * Force tree reloading.
   */
  public final void reloadTree() {
    new Thread() {
      public void run() {
        if (!loadWhenVisibile && firstTime) {
          firstTime = false;
          createTree();
          if (expandAllNodes)
            expandAllNodes();
        }
        else {
          Response response = treeDataLocator.getTreeModel(tree);
          if (response.isError())
            treeModel = new DefaultTreeModel(new DefaultMutableTreeNode());
          else
            treeModel = (DefaultTreeModel)((VOResponse)response).getVo();
          tree.setModel(treeModel);
          tree.revalidate();
          if (expandAllNodes)
            expandAllNodes();
        }
      }
    }.start();
  }


  /**
   * Expand all tree nodes.
   */
  private final void expandAllNodes() {
    int i=0;
    while(i<tree.getRowCount())
      tree.expandRow(i++);
  }


  void jbInit() throws Exception {
    this.setLayout(new java.awt.BorderLayout());
    treePane.getViewport().add(tree,null);
    this.add(treePane,BorderLayout.CENTER);
  }


  /**
   * Redraw the tree. Used when the tree model has been modified.
   */
  public final void repaintTree() {
    TreePath selPath = tree.getSelectionPath();
    treePane.getViewport().remove(tree);
    tree = new JTree(treeModel);

    searchWindowManager = new SearchWindowManager(this);

    if (treeId!=null && dndListener!=null)
        enableDrag(treeId,dndListener);

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
    treeModel = new DefaultTreeModel(new DefaultMutableTreeNode());
    repaintTree();
  }


  /**
   * Fill in the tree.
   */
  private void createTree() {
    Response response = treeDataLocator.getTreeModel(tree);
    if (response.isError())
      treeModel = new DefaultTreeModel(new DefaultMutableTreeNode());
    else
      treeModel = (DefaultTreeModel)((VOResponse)response).getVo();
    recreateTree();
    if (expandAllNodes)
      expandAllNodes();
  }


  private void recreateTree() {
    try {
      TreeNodeRenderer renderer = new TreeNodeRenderer(this,folderIconName,leavesImageName,iconAttributeName,tooltipAttributeName);
      tree.setCellRenderer(renderer);
    } catch (Exception ex) {
      ex.printStackTrace();
    }

    tree.setToolTipText("");
    tree.setModel(treeModel);
    tree.revalidate();

    if (searchWindowManager==null)
      searchWindowManager = new SearchWindowManager(this);

    tree.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
    tree.setShowsRootHandles(true);

    tree.setSize(new Dimension((int)this.getPreferredSize().getWidth()/2,
                               (int)this.getPreferredSize().getHeight()));

    treePane.getViewport().add(tree);
    treePane.setBorder(BorderFactory.createCompoundBorder(
                         BorderFactory.createRaisedBevelBorder(),
                         BorderFactory.createLoweredBevelBorder()
                        ));
    treePane.setAutoscrolls(true);

    tree.setMinimumSize(new Dimension(0,200));

    dropTarget = new DropTarget(tree, this);

    MouseListener ml = new MouseAdapter() {
      public void mouseClicked(MouseEvent e) {
        if(e.getClickCount() == 1 && SwingUtilities.isLeftMouseButton(e))
          treeLeftClick(e, tree);
        else if(e.getClickCount() == 1 && SwingUtilities.isRightMouseButton(e))
          treeRightClick(e, tree);
        if(e.getClickCount() == 2)
          treeDoubleClick(e, tree);
      }
    };
    tree.addMouseListener(ml);
  }


  /**
   * @return selected node or null if no node is selected
   */
  public final DefaultMutableTreeNode getSelectedNode() {
    try {
      javax.swing.tree.TreePath selPath = tree.getSelectionPath();
      if (selPath != null)
        return (DefaultMutableTreeNode)(selPath.getPathComponent(selPath.getPathCount()-1));
      else
        return null;
    } catch (Exception ex) {
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
    } catch (Exception ex) {
    }
  }



  /**
   * Method called when user has double clicked.
   * @param e double click event
   * @param tree tree
   */
  public final void treeDoubleClick(MouseEvent e,JTree tree) {
    try {
      int selRow = tree.getRowForLocation(e.getX(), e.getY());
      javax.swing.tree.TreePath selPath = tree.getPathForLocation(e.getX(), e.getY());
      if (selPath != null) {
        DefaultMutableTreeNode node = (DefaultMutableTreeNode)(selPath.getPathComponent(selPath.getPathCount()-1));
        treeController.doubleClick(node);

      }
    } catch (Exception ex) {
      ex.printStackTrace();
    }
  }


  /**
   * Method called when user has clicked on the left mouse button.
   * @param e left mouse button click event
   * @param tree tree
   */
  public final void treeLeftClick(MouseEvent e,JTree tree) {
    try {
      int selRow = tree.getRowForLocation(e.getX(), e.getY());
      javax.swing.tree.TreePath selPath = tree.getPathForLocation(e.getX(), e.getY());
      if (selPath != null) {
        DefaultMutableTreeNode node = (DefaultMutableTreeNode)(selPath.getPathComponent(selPath.getPathCount()-1));
        treeController.leftClick(node);

      }
    } catch (Exception ex) {
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
  public final void treeRightClick(MouseEvent e,JTree tree) {
    try {
      int selRow = tree.getRowForLocation(e.getX(), e.getY());
      javax.swing.tree.TreePath selPath = tree.getPathForLocation(e.getX(), e.getY());
      if (selPath != null) {
        DefaultMutableTreeNode node = (DefaultMutableTreeNode)(selPath.getPathComponent(selPath.getPathCount()-1));
        if (treeController.rightClick(node) && popup.getComponentCount()>0 && treePane.isEnabled()) {
          // visualizzazione del menu' a pop-up associato al nodo dell'albero,
          // SOLO se il metodo rightClick ha ritornato valore "true" e c'e almeno un elemento nel menu' a pop-up
          popup.show(e.getComponent(), e.getX(), e.getY());
        }

      }
    } catch (Exception ex) {
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
  public final void addPopupMenuItem(String menuName, char mnemonic,boolean enabled,ActionListener menuListener) {
    JMenuItem cbMenuItem = new JMenuItem(ClientSettings.getInstance().getResources().getResource(menuName));
    cbMenuItem.setMnemonic(mnemonic);
    cbMenuItem.setEnabled(enabled);
    cbMenuItem.addActionListener(menuListener);
    popup.add(cbMenuItem);
    menuItems.put(menuName,cbMenuItem);
  }


  /**
   * Set menu item abilitation.
   * @param menuName menu item description (not yet translated)
   * @param enabled flag used to enable the menu item
   */
  public final void setMenuItemEnabled(String menuName,boolean enabled) {
    JMenuItem menu = (JMenuItem)menuItems.get(menuName);
    if (menu!=null)
      menu.setEnabled(enabled);
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
  public final void enableDrag(String treeId,TreeDragNDropListener dndListener) {
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


  class DragGestureAdapter implements DragGestureListener {

    private DragSourceListener dragListener = null;

    public DragGestureAdapter(DragSourceListener dragListener) {
      this.dragListener = dragListener;
    }


    /**
     * A drag gesture has been initiated.
     */
    public final void dragGestureRecognized(DragGestureEvent event) {
      if (dndListener.dragEnabled()) {
        dragSource.startDrag (event, DragSource.DefaultMoveDrop, new StringSelection(treeId), dragListener);
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
    dndListener.dragEnter();
  }


  /**
   * This message goes to DragSourceListener, informing it that the dragging has exited the DropSite.
   */
  public final void dragExit (DragSourceEvent event) {
    dndListener.dragExit();
  }


  /**
   * This message goes to DragSourceListener, informing it that the dragging is currently ocurring over the DropSite.
   */
  public final void dragOver (DragSourceDragEvent event) {
    dndListener.dragOver();
  }


  /**
   * This method is invoked when the user changes the dropAction.
   */
  public final void dropActionChanged ( DragSourceDragEvent event) { }


  /**
   * This message goes to DragSourceListener, informing it that the dragging has ended.
   */
  public final void dragDropEnd (DragSourceDropEvent event) {
    dndListener.dragDropEnd();
  }



  /************************************************************
   * DROP MANAGEMENT
   ************************************************************/

  /**
   * This method is invoked when you are dragging over the DropSite.
   */
  public final void dragEnter (DropTargetDragEvent event) {
    event.acceptDrag (DnDConstants.ACTION_MOVE);
    dndListener.dropEnter();
  }


  /**
   * This method is invoked when you are exit the DropSite without dropping.
   */
  public final void dragExit (DropTargetEvent event) {
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
  public final void dragOver (DropTargetDragEvent event) {
    dndListener.dropOver();
  }


  /**
   * This method is invoked when a drop event has occurred.
   */
  public final void drop(DropTargetDropEvent event) {
    try {
      Transferable transferable = event.getTransferable();
      DefaultMutableTreeNode node = (DefaultMutableTreeNode)tree.getClosestPathForLocation(event.getLocation().x,event.getLocation().y).getLastPathComponent();

      if (node!=null && transferable.isDataFlavorSupported (DataFlavor.stringFlavor)){
        if (dndListener.dropEnabled(node,(String)transferable.getTransferData ( DataFlavor.stringFlavor))) {
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
    }
  }

  /**
   * This method is invoked if the use modifies the current drop gesture.
   */
  public final void dropActionChanged ( DropTargetDragEvent event ) {
    dndListener.dropActionChanged();
  }



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
    if (tree.getSelectionRows()==null)
      return -1;
    if (tree.getSelectionRows().length>0)
      return tree.getSelectionRows()[0];
    else
      return -1;
  }


  /**
   * Set the selected index.
   */
  public final void setSelectedIndex(int index) {
    tree.setSelectionRow(index);
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
      JLabel l = (JLabel)tree.getCellRenderer().getTreeCellRendererComponent(
          tree,
          tree.getPathForRow(index).getLastPathComponent(),
          false,
          false,
          tree.getModel().isLeaf(tree.getPathForRow(index).getLastPathComponent()),
          index,
          false
      );
      return l.getText();
    }
    catch (Exception ex) {
      Object obj = tree.getPathForRow(index).getLastPathComponent();
      return obj==null?"":obj.toString();
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


}

