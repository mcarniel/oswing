package org.openswing.swing.mdi.client;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeSelectionModel;

import java.lang.reflect.*;
import org.openswing.swing.client.*;
import org.openswing.swing.table.renderers.client.*;

import org.openswing.swing.client.*;
import org.openswing.swing.client.*;

import org.openswing.swing.util.client.*;
import org.openswing.swing.permissions.java.*;
import org.openswing.swing.mdi.java.ApplicationFunction;
import org.openswing.swing.internationalization.java.*;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;
import org.openswing.swing.util.client.ClientUtils;
import java.util.ArrayList;


/**
 * <p>Title: OpenSwing Framework</p>
 * <p>Description: Panel that contains the application functions, as a tree (in the MDI Frame).
 * </p>
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
public class TreeMenu extends JPanel {

  private BorderLayout borderLayout1 = new BorderLayout();
  private Tree menuTree = new Tree();
  private JScrollPane treeScrollPane = new JScrollPane(menuTree);
  private JPanel titlePanel = new JPanel();
  private JLabel titleLabel = new JLabel();

  private JButton lockPanel;
  private Icon lockImage;
  private Icon unlockImage;
  private boolean locked;
  private GridBagLayout gridBagLayout1 = new GridBagLayout();
  JLabel findLabel = new JLabel();
  JTextField findTF = new JTextField();

  /** tree nodes path used to find a function */
  private TreePath currentPath = null;


  /**
   * Constructor invoked by the MDIFrame.
   * @param functions DefaultTreeModel object that MUST contains ApplicationFunction objects
   */
  public TreeMenu(DefaultTreeModel functions) {
    createTree(functions);
    setBorder(BorderFactory.createBevelBorder(BevelBorder.RAISED));
    try {
      jbInit();

      MouseListener ml = new MouseAdapter() {
        public void mouseClicked(MouseEvent e) {
           if(e.getClickCount() == 2)
              treeDoubleClick(e);
        }
      };
      KeyListener kl = new java.awt.event.KeyAdapter() {
        public void keyPressed(KeyEvent e) {
          if (e.getKeyCode()==KeyEvent.VK_SPACE)
            treeKeyPressed(e);
        }
      };
      menuTree.addMouseListener(ml);
      menuTree.addKeyListener(kl);




    }
    catch(Exception e) {
      e.printStackTrace();
    }
  }


  public void setEnabled(boolean enabled) {
    menuTree.setEnabled(enabled);
  }


  private void jbInit() throws Exception {
    lockImage = new ImageIcon(ClientUtils.getImage(ClientSettings.LOCK_ON));
    unlockImage = new ImageIcon(ClientUtils.getImage(ClientSettings.LOCK_OFF));

    lockPanel = new JButton(lockImage);
    lockPanel.setBorder(BorderFactory.createEmptyBorder());
    lockPanel.addMouseListener(new MouseAdapter() {
      public void mouseClicked(MouseEvent e) {
        lockPanel_mouseClicked(e);
      }
    });
    setLocked(true);

    this.setLayout(borderLayout1);
    titleLabel.setFont(new Font(titleLabel.getFont().getFontName(), Font.BOLD, titleLabel.getFont().getSize()));
    titleLabel.setText(ClientSettings.getInstance().getResources().getResource("Functions"));
    titleLabel.setPreferredSize(new Dimension(100,25));
    titlePanel.setLayout(gridBagLayout1);
    findLabel.setText(ClientSettings.getInstance().getResources().getResource("Find Function"));
    findTF.setText("");
    findTF.setToolTipText(ClientSettings.getInstance().getResources().getResource("Press ENTER to find function"));
    findTF.setColumns(15);
    findTF.addKeyListener(new java.awt.event.KeyAdapter() {
      public void keyTyped(KeyEvent e) {
        findTF_keyTyped(e);
      }
    });
    findTF.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        findTF_actionPerformed(e);
      }
    });
    titlePanel.add(titleLabel,        new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
    titlePanel.add(lockPanel,         new GridBagConstraints(1, 1, 1, 1, 1.0, 1.0
            ,GridBagConstraints.EAST, GridBagConstraints.VERTICAL, new Insets(5, 10, 0, 10), 0, 0));
    titlePanel.add(findLabel,  new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
    titlePanel.add(findTF,     new GridBagConstraints(1, 0, 1, 1, 1.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 0, 5, 5), 100, 0));
    this.add(treeScrollPane,  BorderLayout.CENTER);
    this.add(titlePanel, BorderLayout.NORTH);

    findTF.requestFocus();

  }


  void lockPanel_mouseClicked(MouseEvent e) {
    setLocked(! locked);
  }


  public boolean isLocked() {
    return(locked);
  }


  public void setLocked(boolean value) {
    this.locked = value;
    if (locked)
      lockPanel.setIcon(lockImage);
    else
      lockPanel.setIcon(unlockImage);
  }


  /**
   * Create the tree, baed on the DefaultTreeModel object.
   * @param functions ApplicationFunctions objects, organized as a tree
   */
  private void createTree(DefaultTreeModel functions) {
    try {
      // associo un renderer per le icone dei nodi dell'albero...
      menuTree.setModel(functions);
      menuTree.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
      TreeNodeRenderer renderer = new TreeNodeRenderer(menuTree);
      menuTree.setCellRenderer(renderer);
      menuTree.revalidate();
    } catch (Throwable ex) {
      ex.printStackTrace();
    }
  }


  /**
   * Method called when the user double clicks the selected tree node: it calls executeFunction method.
   */
  private void treeDoubleClick(MouseEvent e) {
    try {
      int selRow = menuTree.getRowForLocation(e.getX(), e.getY());
      javax.swing.tree.TreePath selPath = menuTree.getPathForLocation(e.getX(), e.getY());
      ApplicationFunction node = (ApplicationFunction)(selPath.getPathComponent(selPath.getPathCount()-1));
      if (node.getFunctionId()!=null)
        executeFunction(node);

    } catch (Exception ex) {
      ex.printStackTrace();
    }
  }


  /**
   * Method called when the user press SPACE key in the selected tree node: it calls executeFunction method.
   */
  private void treeKeyPressed(KeyEvent e) {
    try {
      javax.swing.tree.TreePath selPath = menuTree.getPathForRow(menuTree.getSelectionRows()[0]);
      ApplicationFunction node = (ApplicationFunction)(selPath.getPathComponent(selPath.getPathCount()-1));
      executeFunction(node);
    } catch (Exception ex) {
      ex.printStackTrace();
    }
  }


  /**
   * Method called by treeKeyPressed or treeDoubleClick method: execute the corrisponding method in the ClientFacade.
   */
  private void executeFunction(final ApplicationFunction node) {
    if (node.isFolder())
      return;
    ClientUtils.fireBusyEvent(true);
    new Thread() {
      public void run() {
        try {
          try {
            MDIFrame.getClientFacade().getClass().getMethod(node.getMethodName(),new Class[0]).invoke(MDIFrame.getClientFacade(),new Object[0]);
            ClientUtils.fireBusyEvent(false);
          }
          catch (NoSuchMethodException ex1) {
            MDIFrame.getClientFacade().getClass().getMethod(node.getMethodName(),new Class[]{String.class}).invoke(MDIFrame.getClientFacade(),new Object[]{node.getFunctionId()});
            ClientUtils.fireBusyEvent(false);
          }
        }
        catch (Throwable ex) {
          ClientUtils.fireBusyEvent(false);
          ex.printStackTrace();

          JOptionPane.showMessageDialog(
              ClientUtils.getParentFrame(TreeMenu.this),
              ClientSettings.getInstance().getResources().getResource("Error while executing function")+" '"+node.getMethodName()+"'",
              ClientSettings.getInstance().getResources().getResource("Error"),
              JOptionPane.WARNING_MESSAGE
          );
        }
      }
    }.start();
  }


  void findTF_actionPerformed(ActionEvent e) {
    if (currentPath==null)
      currentPath = new TreePath(new Object[]{(TreeNode)menuTree.getModel().getRoot()});
    TreeNode currentNode = (TreeNode)currentPath.getLastPathComponent();
    for(int i=0;i<currentNode.getChildCount();i++)
      if (findNextFunction(currentPath,currentNode.getChildAt(i),findTF.getText().toUpperCase().trim()))
        return;
    try {
      while (currentNode.getParent() != null) {
        Object[] newTreePath = new Object[currentPath.getPathCount()-1];
        System.arraycopy(currentPath.getPath(),0,newTreePath,0,currentPath.getPathCount()-1);
        currentPath = new TreePath(newTreePath);
        for (int i = currentNode.getParent().getIndex(currentNode)+1;i<currentNode.getParent().getChildCount(); i++) {
          if (findNextFunction(currentPath, currentNode.getParent().getChildAt(i),findTF.getText().toUpperCase().trim())) {
            return;
          }
        }
        currentNode = currentNode.getParent();
      }
    }
    catch (Exception ex) {
    }
    if (!currentPath.getLastPathComponent().equals(menuTree.getModel().getRoot())) {
      currentPath = new TreePath(new Object[] { (TreeNode) menuTree.getModel().getRoot()});
      findTF_actionPerformed(null);
    }
  }


  /**
   * Find the next function in the tree menu, that matches the specified pattern.
   * @param thePath current tree nodes path
   * @param currentNode node to analyze and to expand
   * @param pattern pattern used to match functions
   * @return <code>true</code> if a funciton is found, <code>false</code> otherwise
   */
  private boolean findNextFunction(TreePath thePath,TreeNode currentNode,String pattern) {
    Object[] newTreePath = new Object[thePath.getPathCount()+1];
    System.arraycopy(thePath.getPath(),0,newTreePath,0,thePath.getPathCount());
    newTreePath[newTreePath.length-1] = currentNode;
    TreePath path = new TreePath(newTreePath);

    if (currentNode.toString().toUpperCase().indexOf(pattern)!=-1) {
      menuTree.setSelectionPath(path);
      currentPath = path;
      return true;
    }
    for(int i=0;i<currentNode.getChildCount();i++)
      if (findNextFunction(path,currentNode.getChildAt(i),pattern))
        return true;
    return false;
  }


  void findTF_keyTyped(KeyEvent e) {
    if (e.getKeyChar()=='\n' && e.isControlDown() && menuTree.getSelectionPath()!=null)
      treeKeyPressed(null);
  }
  public JTextField getFindTF() {
    return findTF;
  }



}

