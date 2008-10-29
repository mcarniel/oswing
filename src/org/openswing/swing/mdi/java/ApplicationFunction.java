package org.openswing.swing.mdi.java;

import javax.swing.tree.*;



/**
 * <p>Title: OpenSwing Framework</p>
 * <p>Description: the tree/menubar item (of the MDI Frame).
 * The server side must return a DefaultTreeModel that MUST contains ApplicationFunction objects.
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
public class ApplicationFunction extends DefaultMutableTreeNode {

  /** function identifier */
  private String functionId;

  /** image name  */
  private String iconName;

  /** method name in ClientFacade to execute */
  private String methodName;

  /** this node is a folder */
  private boolean isFolder;

  /** node description */
  private String description;

  /** this node is a separator */
  private boolean isSeparator;


  /**
   * Root node.
   */
  public ApplicationFunction() {
    super();
  }


  /**
   * Root node or separator.
   * @param isSeparator <code>true</code> to set a separator, <code>false</code> to define a root node
   */
  public ApplicationFunction(boolean isSeparator) {
    super();
    this.isSeparator = isSeparator;
  }


  /**
   * Constructor: a folder
   * @param nodeName description (already translated) to view in the tree node/menu item
   * @param iconName image name
   */
  public ApplicationFunction(String nodeName,String iconName) {
    super(nodeName);
    this.description = nodeName;
    this.iconName = iconName;
    isFolder = true;
  }


  /**
   * Constructor: a node function
   * @param nodeName description (already translated) to view in the tree node/menu item
   * @param functionId function identifier
   * @param iconName image name
   * @param methodName method name in ClientFacade to execute
   */
  public ApplicationFunction(String nodeName,String functionId,String iconName,String methodName) {
    super(nodeName);
    this.description = nodeName;
    this.functionId = functionId;
    this.iconName = iconName;
    this.methodName = methodName;
    isFolder = false;
  }


  /**
   * @return function identifier
   */
  public final String getFunctionId() {
    return functionId;
  }


  /**
   * @return image icon
   */
  public final String getIconName() {
    return iconName;
  }


  /**
   * @return method name in ClientFacade to execute
   */
  public final String getMethodName() {
    return methodName;
  }


  /**
   * @return this node is a folder
   */
  public final boolean isFolder() {
    return isFolder;
  }


  /**
   * @return node description
   */
  public final String getDescription() {
    return this.toString();
  }


  /**
   * Set node description.
   */
  public final void setDescription(String description) {
    this.description = description;
  }


  public String toString() {
    return description;
  }


  /**
   * @return this node is a separator
   */
  public final boolean isSeparator() {
    return isSeparator;
  }


}

