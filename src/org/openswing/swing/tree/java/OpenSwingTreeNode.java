package org.openswing.swing.tree.java;

import java.io.*;

import javax.swing.tree.*;


/**
 * <p>Title: OpenSwing Framework</p>
 * <p>Description: This class inherits from DefaultMutableTreeNode: it allows to store userObject too.
 * In fact, serObject is not transient, so it cannot be send through the net using java 1.5/1.6 and Hessian.</p>
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
public class OpenSwingTreeNode extends DefaultMutableTreeNode implements Serializable {

  private Object clonedUserObject;


  /**
   * Creates a tree node that has no parent and no children, but which
   * allows children.
   */
  public OpenSwingTreeNode() {
    this(null);
  }


  /**
   * Creates a tree node with no parent, no children, but which allows
   * children, and initializes it with the specified user object.
   *
   * @param userObject an Object provided by the user that constitutes
   *                   the node's data
   */
  public OpenSwingTreeNode(Object userObject) {
    this(userObject, true);
  }


  /**
   * Creates a tree node with no parent, no children, initialized with
   * the specified user object, and that allows children only if
   * specified.
   *
   * @param userObject an Object provided by the user that constitutes
   *        the node's data
   * @param allowsChildren if true, the node is allowed to have child
   *        nodes -- otherwise, it is always a leaf node
   */
  public OpenSwingTreeNode(Object userObject, boolean allowsChildren) {
    super(userObject,allowsChildren);
    this.clonedUserObject = userObject;
  }


  /**
   * Sets the user object for this node to <code>userObject</code>.
   *
   * @param	userObject	the Object that constitutes this node's
   *                          user-specified data
   * @see	#getUserObject
   * @see	#toString
   */
  public final void setUserObject(Object userObject) {
    clonedUserObject = userObject;
    super.setUserObject(userObject);
  }


  /**
   * Returns this node's user object.
   *
   * @return	the Object stored at this node by the user
   * @see	#setUserObject
   * @see	#toString
   */
  public final Object getUserObject() {
    return clonedUserObject;
  }


  public final String toString() {
      if (getUserObject() == null) {
          return null;
      } else {
          return getUserObject().toString();
      }
  }



}
