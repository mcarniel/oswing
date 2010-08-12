package org.openswing.swing.tree.client;

import java.util.*;

import javax.swing.*;

import org.openswing.swing.message.receive.java.*;


/**
 * <p>Title: OpenSwing Framework</p>
 * <p>Description: Tree Data Source.</p>
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
public abstract class TreeDataLocator {

  /** parameters used to retrieve children nodes */
  private Map treeNodeParams = new HashMap();

  /** attribute name of the v.o. contained in UserObject that is used as node name */
  private String nodeNameAttribute = null;


  public TreeDataLocator() {}


  /**
   * Method called by the TreePanel to fill the tree.
   * @return a VOReponse containing a DefaultTreeModel object
   */
  public abstract Response getTreeModel(JTree tree);


  /**
   * @return parameters used to retrieve children nodes
   */
  public Map getTreeNodeParams() {
    return treeNodeParams;
  }


  /**
   * Set the parameters used to retrieve children nodes.
   * @param treeNodeParams parameters used to retrieve children nodes
   */
  public void setTreeNodeParams(Map treeNodeParams) {
    this.treeNodeParams = treeNodeParams;
  }


  /**
   * @return attribute name of the v.o. contained in UserObject that is used as node name
   */
  public String getNodeNameAttribute() {
    return nodeNameAttribute;
  }


  /**
   * Set the attribute name of the v.o. contained in UserObject that is used as node name.
   * @param nodeNameAttribute attribute name of the v.o. contained in UserObject that is used as node name
   */
  public void setNodeNameAttribute(String nodeNameAttribute) {
    this.nodeNameAttribute = nodeNameAttribute;
  }


  /**
   * Callback method invoked when the data loading is completed.
   * @param error <code>true</code> if data loading has terminated with errors, <code>false</code> otherwise
   */
  public void loadDataCompleted(boolean error) {
  }




}
