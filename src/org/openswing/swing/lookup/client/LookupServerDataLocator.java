package org.openswing.swing.lookup.client;

import java.util.*;

import javax.swing.*;
import javax.swing.tree.*;

import org.openswing.swing.message.receive.java.*;
import org.openswing.swing.message.send.java.*;
import org.openswing.swing.tree.client.*;
import org.openswing.swing.tree.java.*;
import org.openswing.swing.util.client.*;


/**
 * <p>Title: OpenSwing Framework</p>
 * <p>Description: Lookup Data Source that send requests to server side
 * when validating code and when opening lookup grid frame.</p>
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
public class LookupServerDataLocator extends LookupDataLocator {

  /** method called on server side to validate code */
  private String validationMethodName = null;

  /** method called on server side to fetching rows to fill in the lookup grid */
  private String gridMethodName = null;

  /** tree data locator */
  private TreeDataLocator treeDataLocator = null;


  public LookupServerDataLocator() {}


  /**
   * Method called by lookup controller when validating code.
   * @param code code to validate
   * @return code validation response: VOListResponse if code validation has success, ErrorResponse otherwise
   */
  public Response validateCode(String code) {
    return ClientUtils.getData(validationMethodName,new LookupValidationParams(code,getLookupValidationParameters()));
  }


  /**
   * @return method called on server side
   */
  public final String getValidationMethodName() {
    return validationMethodName;
  }


  /**
   * Set the method called on server side.
   * @param serverMethodName method called on server side
   */
  public final void setValidationMethodName(String validationMethodName) {
    this.validationMethodName = validationMethodName;
  }


  /**
   * @return method called on server side to fetching rows to fill in the lookup grid
   */
  public String getGridMethodName() {
    return gridMethodName;
  }


  /**
   * Set the method called on server side to fetching rows to fill in the lookup grid.
   * @param gridMethodName method called on server side to fetching rows to fill in the lookup grid
   */
  public void setGridMethodName(String gridMethodName) {
    this.gridMethodName = gridMethodName;
  }


  /**
   * Method called by lookup controller when user clicks on lookup button.
   * @param action fetching versus: PREVIOUS_BLOCK_ACTION, NEXT_BLOCK_ACTION or LAST_BLOCK_ACTION
   * @param startIndex current index row on grid to use to start fetching data
   * @param filteredColumns filtered columns
   * @param currentSortedColumns sorted columns
   * @param currentSortedVersusColumns ordering versus of sorted columns
   * @param valueObjectType type of value object associated to the lookup grid
   * @return list of value objects to fill in the lookup grid: VOListResponse if data fetching has success, ErrorResponse otherwise
   */
  public Response loadData(
      int action,
      int startIndex,
      Map filteredColumns,
      ArrayList currentSortedColumns,
      ArrayList currentSortedVersusColumns,
      Class valueObjectType) {

    return ClientUtils.getData(
        gridMethodName,
        new GridParams(
          action,
          startIndex,
          filteredColumns,
          currentSortedColumns,
          currentSortedVersusColumns,
          getLookupFrameParams())
    );
  }


  /**
   * Method called by the TreePanel to fill the tree.
   * @return tree model
   */
  public final Response getTreeModel(JTree tree) {
    if (treeDataLocator==null)
      return new VOResponse(new DefaultTreeModel(new OpenSwingTreeNode()));
    else
      return treeDataLocator.getTreeModel(tree);
  }


  /**
   * @return tree data locator
   */
  public final TreeDataLocator getTreeDataLocator() {
    return treeDataLocator;
  }


  /**
   * Set the tree data locator.
   * @param treeDataLocator tree data locator
   */
  public final void setTreeDataLocator(TreeDataLocator treeDataLocator) {
    this.treeDataLocator = treeDataLocator;
  }



}
