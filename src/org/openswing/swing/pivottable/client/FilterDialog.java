package org.openswing.swing.pivottable.client;

import java.util.*;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

import org.openswing.swing.client.*;
import org.openswing.swing.pivottable.java.*;
import org.openswing.swing.util.client.*;

/**
 * <p>Title: OpenSwing Framework</p>
 * <p>Description: Filter dialog for pivot table.</p>
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
public class FilterDialog extends JDialog {

  JPanel mainPanel = new JPanel();
  BorderLayout borderLayout1 = new BorderLayout();
  JPanel buttonsPanel = new JPanel();
  JTabbedPane tabbedPane = new JTabbedPane();
  FlowLayout flowLayout1 = new FlowLayout();
  JButton okButton = new JButton();
  JButton cancelButton = new JButton();
  JPanel dataPanel = new JPanel();
  JPanel rowsPanel = new JPanel();
  JPanel colsPanel = new JPanel();
  JPanel filterPanel = new JPanel();
  FilterGridPanel dataGrid = null;
  FilterGridPanel colsGrid = null;
  FilterGridPanel rowsGrid = null;
  BorderLayout borderLayout2 = new BorderLayout();
  BorderLayout borderLayout3 = new BorderLayout();
  BorderLayout borderLayout4 = new BorderLayout();

  /** Pivot Table */
  private PivotTable table = null;

  /** Pivot Table parameters, used to create PivotTable content */
  private PivotTableParameters pars = new PivotTableParameters();



  public FilterDialog(JFrame parentComp,PivotTable table,PivotTableParameters pars) {
    super(parentComp, ClientSettings.getInstance().getResources().getResource("pivot table settings"), true);
    initDialog(table,pars);
  }


  public FilterDialog(JDialog parentComp,PivotTable table,PivotTableParameters pars) {
    super(parentComp, ClientSettings.getInstance().getResources().getResource("pivot table settings"), true);
    initDialog(table,pars);
  }

  private void initDialog(PivotTable table,PivotTableParameters pars) {
    this.table = table;
    this.pars = pars;
    try {
      init();
      jbInit();
      setDefaultCloseOperation(super.DISPOSE_ON_CLOSE);
      setSize(250,300);
      ClientUtils.centerDialog(ClientUtils.getParentWindow(table),this);
      setVisible(true);
    }
    catch(Exception ex) {
      ex.printStackTrace();
    }
  }


  private void init() {
    HashMap dataFields = new HashMap();
    ArrayList list = table.getAllDataFields();
    for(int i=0;i<list.size();i++)
      dataFields.put(
        ClientSettings.getInstance().getResources().getResource(((DataField)list.get(i)).getDescription()),
        list.get(i)
      );
    dataGrid = new FilterGridPanel(
      dataFields,
      pars.getDataFields()
    );

    HashMap colsFields = new HashMap();
    list = table.getAllColumnFields();
    for(int i=0;i<list.size();i++)
      colsFields.put(
        ClientSettings.getInstance().getResources().getResource(((ColumnField)list.get(i)).getDescription()),
        list.get(i)
      );
    colsGrid = new FilterGridPanel(
      colsFields,
      pars.getColumnFields()
    );

    HashMap rowsFields = new HashMap();
    list = table.getAllRowFields();
    for(int i=0;i<list.size();i++)
      rowsFields.put(
        ClientSettings.getInstance().getResources().getResource(((RowField)list.get(i)).getDescription()),
        list.get(i)
      );
    rowsGrid = new FilterGridPanel(
      rowsFields,
      pars.getRowFields()
    );
  }


  private void jbInit() throws Exception {
    mainPanel.setLayout(borderLayout1);
    buttonsPanel.setLayout(flowLayout1);
    okButton.setMnemonic(ClientSettings.getInstance().getResources().getResource("ok").charAt(0));
    okButton.setText(ClientSettings.getInstance().getResources().getResource("ok"));
    okButton.addActionListener(new FilterDialog_okButton_actionAdapter(this));
    cancelButton.setMnemonic(ClientSettings.getInstance().getResources().getResource("cancel").charAt(0));
    cancelButton.setText(ClientSettings.getInstance().getResources().getResource("cancel"));
    cancelButton.addActionListener(new FilterDialog_cancelButton_actionAdapter(this));
    flowLayout1.setAlignment(FlowLayout.CENTER);
    getContentPane().add(mainPanel);
    mainPanel.add(buttonsPanel,  BorderLayout.SOUTH);
    mainPanel.add(tabbedPane,  BorderLayout.CENTER);
    buttonsPanel.add(okButton, null);
    buttonsPanel.add(cancelButton, null);
    tabbedPane.add(dataPanel,   ClientSettings.getInstance().getResources().getResource("data fields"));
    tabbedPane.add(rowsPanel,   ClientSettings.getInstance().getResources().getResource("row fields"));
    tabbedPane.add(colsPanel,   ClientSettings.getInstance().getResources().getResource("column fields"));
//    tabbedPane.add(filterPanel,  ClientSettings.getInstance().getResources().getResource("filtering conditions"));

    dataPanel.setLayout(borderLayout2);
    dataPanel.add(dataGrid,  BorderLayout.CENTER);

    rowsPanel.setLayout(borderLayout3);
    rowsPanel.add(rowsGrid,  BorderLayout.CENTER);

    colsPanel.setLayout(borderLayout4);
    colsPanel.add(colsGrid,  BorderLayout.CENTER);
  }


  void okButton_actionPerformed(ActionEvent e) {
    if (dataGrid.getSelectedFields().size()==0) {
      OptionPane.showMessageDialog(
        ClientUtils.getParentWindow(table),
        "at least one data field must be selected",
        "Attention",
        JOptionPane.WARNING_MESSAGE
      );
      return;
    }
    rowsGrid.repaint();
    if (rowsGrid.getSelectedFields().size()==0) {
      OptionPane.showMessageDialog(
        ClientUtils.getParentWindow(table),
        "at least one row field must be selected",
        "Attention",
        JOptionPane.WARNING_MESSAGE
      );
      return;
    }
    colsGrid.repaint();
    if (colsGrid.getSelectedFields().size()==0) {
      OptionPane.showMessageDialog(
        ClientUtils.getParentWindow(table),
        "at least one column field must be selected",
        "Attention",
        JOptionPane.WARNING_MESSAGE
      );
      return;
    }

    pars.getDataFields().clear();
    pars.getDataFields().addAll(dataGrid.getSelectedFields());

    pars.getRowFields().clear();
    pars.getRowFields().addAll(rowsGrid.getSelectedFields());

    pars.getColumnFields().clear();
    pars.getColumnFields().addAll(colsGrid.getSelectedFields());

    table.compileDataInThread();
    setVisible(false);
  }


  void cancelButton_actionPerformed(ActionEvent e) {
    setVisible(false);
  }


}

class FilterDialog_okButton_actionAdapter implements java.awt.event.ActionListener {
  FilterDialog adaptee;

  FilterDialog_okButton_actionAdapter(FilterDialog adaptee) {
    this.adaptee = adaptee;
  }
  public void actionPerformed(ActionEvent e) {
    adaptee.okButton_actionPerformed(e);
  }
}

class FilterDialog_cancelButton_actionAdapter implements java.awt.event.ActionListener {
  FilterDialog adaptee;

  FilterDialog_cancelButton_actionAdapter(FilterDialog adaptee) {
    this.adaptee = adaptee;
  }
  public void actionPerformed(ActionEvent e) {
    adaptee.cancelButton_actionPerformed(e);
  }
}
