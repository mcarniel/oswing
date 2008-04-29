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
import java.awt.BorderLayout;
import javax.swing.tree.TreeCellEditor;
import java.util.EventObject;
import javax.swing.event.CellEditorListener;
import java.awt.Color;
import java.awt.Graphics;
import javax.swing.plaf.basic.BasicGraphicsUtils;
import java.util.ArrayList;
import java.awt.FontMetrics;

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

  /** default folder icon */
  private ImageIcon folderIcon = null;

  /** default leaf icon */
  private ImageIcon leafIcon = null;

  /** tree panel that uses this renderer */
  private TreePanel treePanel;

  /** attribute name that contains the icon image name; default value: null; if defined, this attribute overrides "folderIcon"/"leafIcon" values */
  private String iconAttributeName;

  /** attribute name that contains the tool tip text for the node; default value: null */
  private String tooltipAttributeName;

  /** check-box showed when treePanel.isShowCheckBoxes is true */
  private CheckBoxLabel checkBox = new CheckBoxLabel();

  /** panel that contains check-box, image and description */
  private JPanel panel = new JPanel();


  /**
   * Costructor.
   * @param tree node container
   */
  public TreeNodeRenderer(TreePanel treePanel,String folderIconName,String leavesImageName,String iconAttributeName,String tooltipAttributeName) {
    try {
      this.treePanel = treePanel;
      this.iconAttributeName = iconAttributeName;
      this.tooltipAttributeName = tooltipAttributeName;
      folderIcon = new ImageIcon(ClientUtils.getImage(folderIconName));
      leafIcon = new ImageIcon(ClientUtils.getImage(leavesImageName));
      panel.setOpaque(false);
      this.setOpaque(false);
      checkBox.setOpaque(false);

      panel.setLayout(new BorderLayout(0, 0));
      panel.add(this, BorderLayout.CENTER);
      if (treePanel.isShowCheckBoxes()) {
        checkBox.setSize(14,14);
        checkBox.setPreferredSize(new Dimension(14,14));
        panel.add(checkBox, BorderLayout.BEFORE_LINE_BEGINS);

        KeyListener kl= null;
        ArrayList toRemove = new ArrayList();
        for(int i=0;i<treePanel.getTree().getKeyListeners().length;i++) {
          kl = treePanel.getTree().getKeyListeners()[i];
          if (kl instanceof TreeNodeRendererListener)
            toRemove.add(kl);
        }
        for(int i=0;i<toRemove.size();i++)
          treePanel.getTree().removeKeyListener((KeyListener)toRemove.get(i));

        MouseListener ml= null;
        toRemove.clear();
        for(int i=0;i<treePanel.getTree().getMouseListeners().length;i++) {
          ml = treePanel.getTree().getMouseListeners()[i];
          if (ml instanceof TreeNodeRendererListener)
            toRemove.add(ml);
        }
        for(int i=0;i<toRemove.size();i++)
          treePanel.getTree().removeMouseListener((MouseListener)toRemove.get(i));

        TreeNodeRendererListener l = new TreeNodeRendererListener(treePanel);
        treePanel.getTree().addKeyListener(l);
        treePanel.getTree().addMouseListener(l);
      }
      this.setBackgroundNonSelectionColor(new java.awt.Color(0,0,0,0));

    } catch (Exception ex) {
      ex.printStackTrace();
    }
  }


  private boolean isGrayCheckBox(TreeNode node) {
    boolean gray = false;
    if (treePanel.getCheckedNodes().contains(node))
      for(int i=0;i<node.getChildCount();i++) {
        if (!treePanel.getCheckedNodes().contains(node.getChildAt(i)) && treePanel.isShowCheckBoxes() ||
            !treePanel.getCheckedNodes().contains(node.getChildAt(i)) && !node.getChildAt(i).isLeaf() && !treePanel.isShowCheckBoxes())
          return true;
        if (!node.getChildAt(i).isLeaf() || treePanel.isShowCheckBoxes())
          gray = isGrayCheckBox(node.getChildAt(i));
        if (gray)
          return true;
      }
    return gray;
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
      checkBox.setEnabled(treePanel.isEnabled());

      if (iconAttributeName==null) {
        if (leaf)
          setIcon(leafIcon);
        else
          setIcon(folderIcon);
      }
    } catch (Exception ex) {
      ex.printStackTrace();
    }
    JLabel l = (JLabel)this;
    if (value!=null && value.toString()!=null) {
      FontMetrics fm = l.getFontMetrics(l.getFont());
      int w = 0;
      try {
        w = fm.stringWidth(value.toString());
      }
      catch (Exception ex2) {
      }
      setBounds(0,0,w,l.getHeight());
    }

    DefaultMutableTreeNode node = null;
    ValueObject vo = null;
    try {
      node = (DefaultMutableTreeNode) value;

      if (treePanel.isShowCheckBoxes()) {
        checkBox.setSelected(treePanel.getCheckedNodes().contains(node));
        if (leaf && !treePanel.isShowCheckBoxesOnLeaves())
          checkBox.setVisible(false);
        else {
          checkBox.setVisible(true);
          checkBox.setGray( isGrayCheckBox(node) );
        }
      }
    }
    catch (ClassCastException ex1) {
      Logger.error(this.getClass().getName(),"getTreeCellRendererComponent","Expected a node of type DefaultMutableTreeNode or some subclass",null);
    }
    try {
      vo = (ValueObject) node.getUserObject();
    }
    catch (ClassCastException ex1) {
      Logger.error(this.getClass().getName(),"getTreeCellRendererComponent","ValueObject expected inside the node of type DefaultMutableTreeNode",null);
    }
    try {
      if (vo!=null) {
        String attributeName = treePanel.getTreeDataLocator().getNodeNameAttribute();
        if (attributeName!=null) {
          Method getter = vo.getClass().getMethod("get"+attributeName.substring(0,1).toUpperCase()+attributeName.substring(1),new Class[0]);
          value = getter.invoke(vo,new Object[0]);
          if (value!=null) {
            l.setText(value.toString());
          }
          else
            l.setText("");
        }

        if (iconAttributeName!=null) {
          Method getter = vo.getClass().getMethod("get"+iconAttributeName.substring(0,1).toUpperCase()+iconAttributeName.substring(1),new Class[0]);
          value = getter.invoke(vo,new Object[0]);
          if (value!=null) {
            if (value instanceof byte[])
              setIcon(new ImageIcon((byte[])value));
            else if (value instanceof String)
              setIcon(new ImageIcon(ClientUtils.getImage(value.toString())));
          }
          else if (leaf)
            setIcon(leafIcon);
          else
            setIcon(folderIcon);

        }


        if (tooltipAttributeName!=null) {
          Method getter = vo.getClass().getMethod("get"+tooltipAttributeName.substring(0,1).toUpperCase()+tooltipAttributeName.substring(1),new Class[0]);
          value = getter.invoke(vo,new Object[0]);
          if (value!=null && value instanceof String) {
            l.setToolTipText(value.toString());
            panel.setToolTipText(value.toString());
          }
          else if (value==null) {
            l.setToolTipText("");
            panel.setToolTipText("");
          }
        }


      }

    }
    catch (Throwable ex1) {
      Logger.error(this.getClass().getName(),"getTreeCellRendererComponent","ValueObject expected inside the node of type DefaultMutableTreeNode",ex1);
    }

    return panel;
  }


  /**
   * <p>Title: OpenSwing Framework</p>
   * <p>Description: Inner class used to render the check-box.</p>
   * <p>Copyright: Copyright (C) 2006 Mauro Carniel</p>
   * <p> </p>
   * @author Mauro Carniel
   * @version 1.0
   */
  class CheckBoxLabel extends JLabel {

    private boolean sel;

    private boolean gray;

    public void setSelected(boolean sel) {
      this.sel = sel;
      repaint();
    }

    public void setGray(boolean gray) {
      this.gray = gray;
      repaint();
    }

    public void paintComponent(Graphics g) {
      super.paintComponent(g);
      g.translate((int)this.getWidth()/2-6,this.getHeight()/2-5);
      BasicGraphicsUtils.drawLoweredBezel(g,0,0,12,12,Color.darkGray,Color.black,Color.white,Color.gray);
      if (gray) {
        g.setColor(Color.lightGray);
        g.fillRect(1,1,10,10);
      }
      if (sel) {
        g.setColor(Color.black);
        g.drawLine(3,5,5,7);
        g.drawLine(3,6,5,8);
        g.drawLine(3,7,5,9);
        g.drawLine(6,6,9,3);
        g.drawLine(6,7,9,4);
        g.drawLine(6,8,9,5);
      }
    }

  }



}
