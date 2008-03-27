package org.openswing.swing.mdi.client;

import java.util.*;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

import org.openswing.swing.internationalization.java.*;
import org.openswing.swing.util.client.*;


/**
 * <p>Title: OpenSwing Framework</p>
 * <p>Description: Dialog window used to change the language inside the MDI Frame.</p>
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
public class ChangeLanguageDialog extends JDialog {
  JPanel mainPanel = new JPanel();
  BorderLayout borderLayout2 = new BorderLayout();
  GridBagLayout gridBagLayout1 = new GridBagLayout();
  JScrollPane scrollPane = new JScrollPane();
  JButton changeButton = new JButton();
  JButton cancelButton = new JButton();
  JList langList = new JList();

  private MDIFrame frame = null;
  private MDIController controller = null;


  public ChangeLanguageDialog(MDIFrame frame,MDIController controller) {
    super(frame, ClientSettings.getInstance().getResources().getResource("change language"), true);
    this.frame = frame;
    this.controller = controller;
    try {
      jbInit();
      init(controller.getLanguages());
      setSize(300,200);
      ClientUtils.centerDialog(frame,this);
      setVisible(true);
    }
    catch(Exception ex) {
      ex.printStackTrace();
    }
  }


  /**
   * Initialize the list with the languages.
   */
  private void init(ArrayList languages) {
    DefaultListModel model = new DefaultListModel();
    for(int i=0;i<languages.size();i++)
      model.addElement(languages.get(i));
    langList.setModel(model);
    langList.revalidate();
    langList.setSelectedIndex(0);
  }



  private void jbInit() throws Exception {
    mainPanel.setLayout(gridBagLayout1);
    this.getContentPane().setLayout(borderLayout2);
    changeButton.setMnemonic(ClientSettings.getInstance().getResources().getResource("changemnemonic").charAt(0));
    changeButton.setText(ClientSettings.getInstance().getResources().getResource("change language"));
    changeButton.addActionListener(new ChangeLanguageDialog_changeButton_actionAdapter(this));
    cancelButton.setMnemonic(ClientSettings.getInstance().getResources().getResource("cancelmnemonic").charAt(0));
    cancelButton.setText(ClientSettings.getInstance().getResources().getResource("cancel"));
    cancelButton.addActionListener(new ChangeLanguageDialog_cancelButton_actionAdapter(this));
    langList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
    this.setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
    getContentPane().add(mainPanel, BorderLayout.CENTER);
    mainPanel.add(scrollPane,    new GridBagConstraints(0, 0, 1, 2, 1.0, 1.0
            ,GridBagConstraints.NORTHWEST, GridBagConstraints.BOTH, new Insets(5, 5, 5, 15), 0, 0));
    mainPanel.add(changeButton,     new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0
            ,GridBagConstraints.SOUTHWEST, GridBagConstraints.NONE, new Insets(30, 5, 5, 5), 0, 0));
    mainPanel.add(cancelButton,    new GridBagConstraints(1, 1, 1, 1, 0.0, 0.0
            ,GridBagConstraints.NORTHWEST, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 0, 0));
    scrollPane.getViewport().add(langList, null);
  }


  void cancelButton_actionPerformed(ActionEvent e) {
    setVisible(false);
  }


  void changeButton_actionPerformed(ActionEvent e) {
    if (langList.getSelectedIndex()==-1)
      return;
    Language lang = (Language)langList.getModel().getElementAt(langList.getSelectedIndex());
    ClientSettings.getInstance().setLanguage(lang.getLanguageId());
    setVisible(false);
    frame.setVisible(false);
    frame.dispose();
    try {
      if (ClientUtils.getServerURL() != null) {
        // if this parameter is setted, then this is a three-tier application that uses "org.openswing.swing.server.Controller" class on server-side
        ClientUtils.getData("changeLanguage", lang.getLanguageId());
      }
    }
    catch (Exception ex) {
    }
    new MDIFrame(controller);
  }


}



class ChangeLanguageDialog_cancelButton_actionAdapter implements java.awt.event.ActionListener {
  ChangeLanguageDialog adaptee;

  ChangeLanguageDialog_cancelButton_actionAdapter(ChangeLanguageDialog adaptee) {
    this.adaptee = adaptee;
  }
  public void actionPerformed(ActionEvent e) {
    adaptee.cancelButton_actionPerformed(e);
  }
}

class ChangeLanguageDialog_changeButton_actionAdapter implements java.awt.event.ActionListener {
  ChangeLanguageDialog adaptee;

  ChangeLanguageDialog_changeButton_actionAdapter(ChangeLanguageDialog adaptee) {
    this.adaptee = adaptee;
  }
  public void actionPerformed(ActionEvent e) {
    adaptee.changeButton_actionPerformed(e);
  }
}
