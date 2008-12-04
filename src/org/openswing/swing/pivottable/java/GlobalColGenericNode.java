package org.openswing.swing.pivottable.java;

import java.io.Serializable;
import java.util.ArrayList;


/**
 * <p>Title: OpenSwing Framework</p>
 * <p>Description: Column field node: contains its children nodes; used in pivot table.</p>
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
public class GlobalColGenericNode implements Serializable {

  /** node identifier, i.e. the nodes path */
  private GenericNodeKey vpath = null;

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


  public GlobalColGenericNode() {
    rootNode = true;
  }


  /**
   * @param userObject object stored inside this
   */
  public GlobalColGenericNode(GenericNodeKey vpath) {
    this.vpath = vpath;
    this.value = vpath.getLastNode();
  }


  /**
   * Add a child node to this.
   * @param childNode GenericNode to add
   */
  public final void add(GlobalColGenericNode childNode) {
    childNode.setLevel(level+1);
    boolean added = false;
    for(int i=0;i<childrenNodes.size();i++)
      if (isLessThan(
        childNode.getValue(),
        ((GlobalColGenericNode)childrenNodes.get(i)).getValue()
      )) {
        childrenNodes.add(i,childNode);
        added = true;
        break;
      }
    if (!added)
      childrenNodes.add(childNode);
  }


  private boolean isLessThan(Object o1,Object o2) {
    if (o1==null || o2==null)
      return true;
    if (o1 instanceof Number)
      return ((Number)o1).doubleValue()<((Number)o2).doubleValue();
    else if (o1 instanceof java.util.Date)
      return ((java.util.Date)o1).compareTo((java.util.Date)o2)<0;
    else
    return o1.toString().compareTo(o2.toString())<0;
  }


  /**
   * Add a child node to this.
   * @param childNode GenericNode to add
   */
  public final void remove(GlobalColGenericNode childNode) {
    childrenNodes.remove(childNode);
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
  public final GlobalColGenericNode getChildren(int index) {
    return (GlobalColGenericNode)childrenNodes.get(index);
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
    if (obj==null || !(obj instanceof GlobalColGenericNode))
      return false;
    return ((GlobalColGenericNode)obj).vpath.equals(vpath);
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
