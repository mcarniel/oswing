package org.openswing.swing.tree.client;

import java.awt.event.*;
import java.awt.Dimension;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreeSelectionModel;
import javax.swing.tree.DefaultTreeCellRenderer;
import java.awt.Component;
import java.awt.Insets;
import java.lang.reflect.*;
import java.awt.event.MouseEvent;
import javax.swing.*;
import java.util.Vector;
import org.openswing.swing.util.client.ClientUtils;
import org.openswing.swing.util.client.ClientSettings;
import javax.swing.tree.TreeNode;
import org.openswing.swing.message.receive.java.ValueObject;
import org.openswing.swing.logger.client.Logger;


/**
 * <p>Title: OpenSwing Framework</p>
 * <p>Description: Tree node renderer.</p>
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
public class TreeNodeRenderer extends DefaultTreeCellRenderer {

  ImageIcon folderIcon = null;
  ImageIcon leafIcon = null;
  TreePanel treePanel;


  /**
   * Costructor.
   * @param tree node container
   */
  public TreeNodeRenderer(TreePanel treePanel,String folderIconName,String leavesImageName) {
    try {
      this.treePanel = treePanel;
      folderIcon = new ImageIcon(ClientUtils.getImage(folderIconName));
      leafIcon = new ImageIcon(ClientUtils.getImage(leavesImageName));
      this.setOpaque(false);
      this.setBackgroundNonSelectionColor(new java.awt.Color(0,0,0,0));

    } catch (Exception ex) {
      ex.printStackTrace();
    }
  }




  public Component getTreeCellRendererComponent(JTree tree,
                                                Object value,
                                                boolean sel,
                                                boolean expanded,
                                                boolean leaf,
                                                int row,
                                                boolean hasFocus) {
    try {
      super.getTreeCellRendererComponent(tree, value, sel,expanded, leaf, row,hasFocus);
      if (leaf)
        setIcon(leafIcon);
      else
        setIcon(folderIcon);

    } catch (Exception ex) {
      ex.printStackTrace();
    }
    JLabel l = (JLabel)this;
//    setBounds(0,0,350,l.getHeight());

    try {
      DefaultMutableTreeNode node = (DefaultMutableTreeNode) value;
      ValueObject vo = (ValueObject) node.getUserObject();
      if (vo!=null) {
        String attributeName = treePanel.getTreeDataLocator().getNodeNameAttribute();

        Method getter = vo.getClass().getMethod("get"+attributeName.substring(0,1).toUpperCase()+attributeName.substring(1),new Class[0]);
        value = getter.invoke(vo,new Object[0]);
        if (value!=null) {
          l.setText(value.toString());
        }
      }

    }
    catch (Throwable ex1) {
      Logger.error(this.getClass().getName(),"getTreeCellRendererComponent","ValueObject expected inside the node of type DefaultMutableTreeNode",ex1);
    }

    return l;
  }

}
