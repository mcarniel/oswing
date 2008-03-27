package org.openswing.swing.tree.client;

import java.awt.event.MouseListener;
import java.awt.event.KeyListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.SwingUtilities;
import javax.swing.tree.TreeNode;

/**
 * <p>Title: OpenSwing Framework</p>
 * <p>Description: Tree node renderer listeners, for mouse and key events.</p>
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
public class TreeNodeRendererListener implements MouseListener,KeyListener {

  /** tree panel that uses this renderer */
  private TreePanel treePanel;


  public TreeNodeRendererListener(TreePanel treePanel) {
    this.treePanel = treePanel;
  }


  /**
   * keyPressed
   *
   * @param e KeyEvent
   */
  public void keyPressed(KeyEvent e) {
  }


  /**
   * keyReleased
   *
   * @param e KeyEvent
   */
  public void keyReleased(KeyEvent e) {
    if (treePanel.isEnabled()) {
      DefaultMutableTreeNode node = treePanel.getSelectedNode();
      if (e.getKeyChar()==' ' && node!=null) {
        if (treePanel.isShowCheckBoxesOnLeaves() || !node.isLeaf())
          checkChanged(node);
      }
    }
  }


  /**
   * keyTyped
   *
   * @param e KeyEvent
   */
  public void keyTyped(KeyEvent e) {
  }

  /**
   * mouseClicked
   *
   * @param e MouseEvent
   */
  public void mouseClicked(MouseEvent e) {
    if (treePanel.isEnabled() && SwingUtilities.isLeftMouseButton(e) && e.getClickCount()==1) {
      try {
        DefaultMutableTreeNode node = (DefaultMutableTreeNode)treePanel.getTree().getPathForLocation(e.getX(), e.getY()).getLastPathComponent();
        if (treePanel.isShowCheckBoxesOnLeaves() || !node.isLeaf())
          checkChanged(node);
      }
      catch (Exception ex) {
      }
    }
  }


  /**
   * mouseEntered
   *
   * @param e MouseEvent
   */
  public void mouseEntered(MouseEvent e) {
  }

  /**
   * mouseExited
   *
   * @param e MouseEvent
   */
  public void mouseExited(MouseEvent e) {
  }

  /**
   * mousePressed
   *
   * @param e MouseEvent
   */
  public void mousePressed(MouseEvent e) {
  }

  /**
   * mouseReleased
   *
   * @param e MouseEvent
   */
  public void mouseReleased(MouseEvent e) {
  }


  /**
   * Changed check-box selection value.
   * @param node node that contains the check-box
   */
  private void checkChanged(DefaultMutableTreeNode node) {
    if (treePanel.getCheckedNodes().contains(node)) {
      treePanel.getCheckedNodes().remove(node);
      updateCheckboxesOnSubTree(node,false);
      while((node=(DefaultMutableTreeNode)node.getParent())!=null)
        if (areAllDeselectedCheckBox(node))
          treePanel.getCheckedNodes().remove(node);
    }
    else {
      treePanel.getCheckedNodes().add(node);
      updateCheckboxesOnSubTree(node,true);
    }
    treePanel.getTree().repaint();
  }



  private boolean areAllDeselectedCheckBox(TreeNode node) {
    for(int i=0;i<node.getChildCount();i++) {
      if (treePanel.getCheckedNodes().contains(node.getChildAt(i)) ||
          node.getChildAt(i).isLeaf() && !treePanel.isShowCheckBoxes())
        return false;
    }
    return true;
  }


  private void updateCheckboxesOnSubTree(TreeNode node,boolean sel) {
    if (sel)
      treePanel.getCheckedNodes().add(node);
    else
      treePanel.getCheckedNodes().remove(node);
    for(int i=0;i<node.getChildCount();i++)
      updateCheckboxesOnSubTree(node.getChildAt(i),sel);
  }


}
