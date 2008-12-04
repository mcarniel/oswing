package org.openswing.swing.pivottable.java;

import java.io.Serializable;
import java.util.ArrayList;
import org.openswing.swing.pivottable.functions.java.GenericFunction;


/**
 * <p>Title: OpenSwing Framework</p>
 * <p>Description: Column field node: contains its children nodes and the list of data field values; used in pivot table.</p>
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
public class ColGenericNode implements Serializable {

  /** node identifier, i.e. the nodes path */
  private GenericNodeKey vpath = null;

  /** data field values */
  private GenericFunction[] gf = new GenericFunction[0];

  /** list of GenericNode objects */
  private ArrayList childrenNodes = new ArrayList();

  /** flag used to define if current node is expanded */
  private boolean nodeExpanded = false;

  /** node value */
  private Object value = null;

  /** this is a root node */
  private boolean rootNode = false;

  /** depth level */
  private int level = 0;


  public ColGenericNode() {
    rootNode = true;
  }


  /**
   * @param userObject object stored inside this
   */
  public ColGenericNode(GenericNodeKey vpath,GenericFunction[] gf) {
    this.vpath = vpath;
    this.value = vpath.getLastNode();
    this.gf = gf;
  }


  /**
   * Add a child node to this.
   * @param childNode GenericNode to add
   */
  public final void add(ColGenericNode childNode) {
    childNode.setLevel(level+1);
    childrenNodes.add(childNode);
  }


  /**
   * Add a child node to this.
   * @param childNode GenericNode to add
   */
  public final void remove(ColGenericNode childNode) {
    childrenNodes.remove(childNode);
  }


  /**
   * @return object stored inside this
   */
  public final GenericFunction[] getGenericFunctions() {
    return gf;
  }


  /**
   * @return children number
   */
  public final int getChildrenCount() {
    return childrenNodes.size();
  }


  /**
   * @param index children index
   * @return children
   */
  public final ColGenericNode getChildren(int index) {
    return (ColGenericNode)childrenNodes.get(index);
  }


  /**
   * @return define if current node is expanded
   */
  public final boolean isNodeExpanded() {
    return nodeExpanded;
  }


  /**
   * Define if current node is expanded.
   * @param nodeExpanded define if current node is expanded
   */
  public final void setNodeExpanded(boolean nodeExpanded) {
    this.nodeExpanded = nodeExpanded;
  }


  public final boolean equals(Object obj) {
    if (obj==null || !(obj instanceof ColGenericNode))
      return false;
    return ((ColGenericNode)obj).vpath.equals(vpath);
  }


  public final int hashCode() {
    return vpath.hashCode();
  }


  /**
   * @return node value
   */
  public final Object getValue() {
    return value;
  }


  /**
   * @return <code>true</code> if this is a root node
   */
  public final boolean isRootNode() {
    return rootNode;
  }


  /**
   * @return depth level
   */
  public final int getLevel() {
    return level;
  }


  /**
   * Set the depth level.
   * @param level depth level
   */
  public final void setLevel(int level) {
    this.level = level;
  }






}
