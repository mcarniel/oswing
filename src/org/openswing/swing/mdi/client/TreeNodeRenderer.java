package org.openswing.swing.mdi.client;

import java.awt.*;
import javax.swing.*;
import javax.swing.tree.*;

import org.openswing.swing.mdi.java.*;
import org.openswing.swing.util.client.*;


/**
 * <p>Title: OpenSwing Framework</p>
 * <p>Description: Tree Node Renderer.
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

public class TreeNodeRenderer extends DefaultTreeCellRenderer {

  /** default icon for a function node */
  private ImageIcon defaultFunctionIcon = null;

  /** default icon for a folder node */
  private ImageIcon defaultFolderIcon = null;

  /** functions tree */
  private JTree tree = null;


  /**
   * Costructor called by TreeMenu.
   * @param tree functions tree
   */
  public TreeNodeRenderer(JTree tree) {
    try {
      this.tree = tree;
      this.defaultFunctionIcon = new ImageIcon( ClientUtils.getImage(ClientSettings.PERC_TREE_NODE));
      this.defaultFolderIcon = new ImageIcon( ClientUtils.getImage(ClientSettings.PERC_TREE_FOLDER));
      this.setOpaque(false);
      this.setBackgroundNonSelectionColor(new java.awt.Color(0,0,0,0));
    } catch (Exception ex) {
      ex.printStackTrace();
    }
  }


  /*
   * View a tree node.
   */
  public Component getTreeCellRendererComponent(
    JTree tree,
    Object value,
    boolean sel,
    boolean expanded,
    boolean leaf,
    int row,
    boolean hasFocus) {
    try {
      super.getTreeCellRendererComponent(tree, value, sel,expanded, leaf, row,hasFocus);
      if (value instanceof ApplicationFunction) {
        ApplicationFunction node = (ApplicationFunction)value;
        String iconName = node.getIconName();
        if(iconName!=null)
          setIcon(new ImageIcon( ClientUtils.getImage(iconName) ));
        else if(!node.isFolder())
          setIcon(defaultFunctionIcon);
        else
          setIcon(defaultFolderIcon);
        JLabel l = (JLabel)this;
        if (ClientSettings.SHOW_TOOLTIP_IN_TREEMENU) {
          l.setToolTipText(node.getTooltipText());
        }
      }
      else
          setIcon(defaultFolderIcon);
    } catch (Exception ex) {
      ex.printStackTrace();
    }
    this.setOpaque(false);
    return this;
  }

}


