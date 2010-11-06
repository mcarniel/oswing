package org.openswing.swing.tree.client;

import javax.swing.*;

import org.openswing.swing.client.*;
import org.openswing.swing.message.receive.java.*;
import org.openswing.swing.util.client.*;


/**
 * <p>Title: OpenSwing Framework</p>
 * <p>Description: Tree Data Source that send a request to the server side
 * to fill in the tree.</p>
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
public class TreeServerDataLocator extends TreeDataLocator {

  /** method called on server side to retrieve the tree model */
  private String serverMethodName = null;


  public TreeServerDataLocator() {}


  /**
   * @return method called on server side to retrieve the tree model
   */
  public String getServerMethodName() {
    return serverMethodName;
  }


  /**
   * Set the method called on server side to retrieve the tree model.
   * @param serverMethodName method called on server side to retrieve the tree model
   */
  public void setServerMethodName(String serverMethodName) {
    this.serverMethodName = serverMethodName;
  }


  /**
   * Method called by the TreePanel to fill the tree.
   * @return a VOReponse containing a DefaultTreeModel object
   */
  public Response getTreeModel(JTree tree) {
    Response response = ClientUtils.getData(serverMethodName,super.getTreeNodeParams());
    if (response.isError()) {
      OptionPane.showMessageDialog(
          tree,
          response.getErrorMessage(),
          ClientSettings.getInstance().getResources().getResource("Error"),
          JOptionPane.WARNING_MESSAGE
      );
    }
    return response;
  }



}
