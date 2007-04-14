package org.openswing.swing.client;

import javax.swing.*;
import java.awt.event.*;
import java.util.*;

import org.openswing.swing.client.*;
import org.openswing.swing.util.client.ClientUtils;
import org.openswing.swing.util.client.ClientSettings;
import java.awt.Dimension;



/**
 * <p>Title: OpenSwing Framework</p>
 * <p>Description: Based button, derived by insert/edit/reload/save/delete buttons.</p>
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
public class GenericButton extends JButton {


  /** data controllers linked to this button*/
  protected List dataControllerList = new ArrayList(3);

  /** previous button state, before setEnabled method calling */
  private boolean oldValue = true;

  /** flag used to execute the action event in a separated thread (useful for heavy server executions); default value: false */
  private boolean executeAsThread = false;


  public GenericButton() {
    setPreferredSize(new Dimension(32,32));
    super.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        if (!executeAsThread)
          execute();
        else new Thread() {
            public void run() {
              execute();
            }
          }.start();
      }
    });
  }


  public GenericButton(ImageIcon imageIcon) {
    super(imageIcon);
    setPreferredSize(new Dimension(32,32));
    super.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        if (!executeAsThread)
          execute();
        else new Thread() {
            public void run() {
              execute();
            }
          }.start();
      }
    });
  }


  /**
   * Fire event for all data controller listeners.
   */
  private void execute() {
    try {
      for (int i = 0; i < dataControllerList.size(); i++) {
        executeOperation( (DataController) dataControllerList.get(i));
      }
    }
    catch (Exception ex) {
      ex.printStackTrace();
    }
  }


  /**
   * Add a listener to the button.
   * @param dataController data controller that listens the button press event
   */
  public final void addDataController(DataController dataController) {
    if (dataController!=null) {
      this.dataControllerList.add(dataController);
    }
  }


  /**
   * Remove a listener from the button.
   * @param dataController data controller
   */
  public final void removeDataController(DataController dataController) {
    if (dataController!=null) {
      dataControllerList.remove(dataController);
    }
  }


  /**
   * Operation to execute when the button is pressed.
   * The method must be overridden by the subclass button.
   * @param controller data controller that contains the execute operation logic
   */
  protected void executeOperation(DataController controller) throws Exception {
    throw(new Exception("The method must be overridden in the subclass"));
  }


  /**
   * Method used to change button state (enabled/disabled).
   * @param enabled <code>true</code> enable the button, <code>false</code> disable the button
   */
  public final void setEnabled(boolean enabled) {
    String functionId = null;
    DataController dataController = null;
    for(int i=0;i<dataControllerList.size();i++) {
      dataController = (DataController)dataControllerList.get(i);

      // check if there exists a button disabilitation policy for this button...
      if (dataController.isButtonDisabled(this)) {
        super.setEnabled(false);
        return;
      }

      functionId = dataController.getFunctionId();
      boolean buttonAuthorized = true;
      if (this instanceof InsertButton || this instanceof CopyButton)
        buttonAuthorized = ClientSettings.getInstance().getButtonsAuthorizations().isInsertEnabled(functionId);
      else if (this instanceof EditButton)
        buttonAuthorized = ClientSettings.getInstance().getButtonsAuthorizations().isEditEnabled(functionId);
      else if (this instanceof DeleteButton)
        buttonAuthorized = ClientSettings.getInstance().getButtonsAuthorizations().isDeleteEnabled(functionId);
      if (!buttonAuthorized && enabled) {
        enabled = false;
        break;
      }
    }

    oldValue = this.isEnabled();
    super.setEnabled(enabled);
  }


  /**
   * @return previous button state, before setEnabled method calling
   */
  public final boolean getOldValue() {
    return oldValue;
  }


  /**
   * @return flag used to execute the action event in a separated thread
   */
  public final boolean isExecuteAsThread() {
    return executeAsThread;
  }


  /**
   * Set to true to execute the action event in a separated thread.
   * @param executeAsThread flag used to execute the action event in a separated thread
   */
  public final void setExecuteAsThread(boolean executeAsThread) {
    this.executeAsThread = executeAsThread;
  }



}