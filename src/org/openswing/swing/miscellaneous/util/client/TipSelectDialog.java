package org.openswing.swing.miscellaneous.util.client;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

import org.openswing.swing.client.*;
import org.openswing.swing.mdi.client.*;
import org.openswing.swing.miscellaneous.client.TipPanelContent;
import org.openswing.swing.util.client.*;



/**
 * <p>Title: OpenSwing Framework</p>
 * <p>Description: Dialog used to select a tip.</p>
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
class TipSelectDialog extends JDialog {


  JPanel filterPanel = new JPanel();
  JPanel buttonsPane = new JPanel();
  FlowLayout flowLayout1 = new FlowLayout();
  JScrollPane scrollPane = new JScrollPane();
  JList list = new JList();
  GridBagLayout gridBagLayout1 = new GridBagLayout();
  LabelControl labelFilter = new LabelControl();
  TextControl controlFilter = new TextControl();
  LabelControl labelTitle = new LabelControl();
  JButton okButton = new JButton();
  JButton cancelButton = new JButton();

  private final TipPanelContent tipPanelContent;
  private int tipIndex = -1;


  public TipSelectDialog(TipPanelContent tipPanelContent) {
    super(MDIFrame.getInstance(),ClientSettings.getInstance().getResources().getResource("select tip"),true);
    this.tipPanelContent = tipPanelContent;
    try {
      jbInit();
      init();
      setSize(300,300);
      setDefaultCloseOperation(DISPOSE_ON_CLOSE);
      ClientUtils.centerDialog(MDIFrame.getInstance(),this);
      setVisible(true);
    }
    catch(Exception ex) {
      ex.printStackTrace();
    }
  }

  public int getTipIndex() {
      return tipIndex;
  }


  private void init() {
    list.setSelectionForeground((Color)UIManager.get("TextField.foreground"));
    list.setBackground( (Color) UIManager.get("TextField.background"));
    list.setSelectionBackground(ClientSettings.BACKGROUND_SEL_COLOR);
    DefaultListModel model = new DefaultListModel();
    for(int i=0; i<tipPanelContent.getTitles().length; i++) {
      model.addElement(ClientSettings.getInstance().getResources().getResource(tipPanelContent.getTitles()[i]));
    }
    list.setModel(model);

    final long t = System.currentTimeMillis();
    controlFilter.addKeyListener(new KeyAdapter() {

      public void keyReleased(KeyEvent e) {
        if (controlFilter.getText().length()>0) {
          for(int i=0;i<list.getModel().getSize();i++)
            if ( list.getModel().getElementAt(i).toString().toLowerCase().startsWith(controlFilter.getText().toLowerCase()) ) {
              list.setSelectedIndex(i);
              break;
            }
        }
        if (e.getKeyCode()==e.VK_ESCAPE) {
          tipIndex = -1;
          setVisible(false);
        }
        if (e.getKeyCode()==e.VK_ENTER && System.currentTimeMillis()>t+1000) {
          tipIndex = list.getSelectedIndex();
          setVisible(false);
        }
      }
    });

  }



  private void jbInit() throws Exception {
    filterPanel.setLayout(gridBagLayout1);
    buttonsPane.setLayout(flowLayout1);
    list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
    list.addMouseListener(new TipSelectDialog_list_mouseAdapter(this));
    labelFilter.setLabel("tip name");
    labelTitle.setLabel("tips list");
    labelTitle.setTextAlignment(SwingConstants.LEFT);
    okButton.setText(ClientSettings.getInstance().getResources().getResource("ok"));
    okButton.setMnemonic(okButton.getText().charAt(0));
    okButton.addActionListener(new TipSelectDialog_okButton_actionAdapter(this));
    cancelButton.setText(ClientSettings.getInstance().getResources().getResource("cancel"));
    cancelButton.setMnemonic(cancelButton.getText().charAt(0));
    cancelButton.addActionListener(new TipSelectDialog_cancelButton_actionAdapter(this));
    getContentPane().add(filterPanel,  BorderLayout.NORTH);
    this.getContentPane().add(buttonsPane,  BorderLayout.SOUTH);
    this.getContentPane().add(scrollPane,  BorderLayout.CENTER);
    scrollPane.getViewport().add(list, null);
    filterPanel.add(labelFilter,   new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 5, 0, 5), 0, 0));
    filterPanel.add(controlFilter,    new GridBagConstraints(1, 0, 1, 1, 1.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 0, 0));
    filterPanel.add(labelTitle,  new GridBagConstraints(0, 1, 2, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(10, 5, 0, 5), 0, 0));
    buttonsPane.add(okButton, null);
    buttonsPane.add(cancelButton, null);
  }


  void okButton_actionPerformed(ActionEvent e) {
    tipIndex = list.getSelectedIndex();
    if (list.getSelectedIndex()==-1)
      return;
    setVisible(false);
  }


  void cancelButton_actionPerformed(ActionEvent e) {
    tipIndex = -1;
    setVisible(false);
  }

  void list_mouseClicked(MouseEvent e) {
    if (e.getClickCount()==2) {
      tipIndex = list.getSelectedIndex();
      setVisible(false);
    }
  }


}
class TipSelectDialog_okButton_actionAdapter implements java.awt.event.ActionListener {
  TipSelectDialog adaptee;

  TipSelectDialog_okButton_actionAdapter(TipSelectDialog adaptee) {
    this.adaptee = adaptee;
  }
  public void actionPerformed(ActionEvent e) {
    adaptee.okButton_actionPerformed(e);
  }
}

class TipSelectDialog_cancelButton_actionAdapter implements java.awt.event.ActionListener {
  TipSelectDialog adaptee;

  TipSelectDialog_cancelButton_actionAdapter(TipSelectDialog adaptee) {
    this.adaptee = adaptee;
  }
  public void actionPerformed(ActionEvent e) {
    adaptee.cancelButton_actionPerformed(e);
  }
}

class TipSelectDialog_list_mouseAdapter extends java.awt.event.MouseAdapter {
  TipSelectDialog adaptee;

  TipSelectDialog_list_mouseAdapter(TipSelectDialog adaptee) {
    this.adaptee = adaptee;
  }
  public void mouseClicked(MouseEvent e) {
    adaptee.list_mouseClicked(e);
  }
}
