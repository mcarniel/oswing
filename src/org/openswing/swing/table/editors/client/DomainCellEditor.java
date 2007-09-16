package org.openswing.swing.table.editors.client;

import java.awt.*;
import java.text.*;
import javax.swing.*;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;
import javax.swing.tree.*;
import org.openswing.swing.client.*;
import org.openswing.swing.domains.java.*;
import org.openswing.swing.domains.java.*;

import org.openswing.swing.util.client.*;
import java.util.ArrayList;
import java.awt.event.ItemListener;
import java.awt.event.KeyEvent;


/**
 * <p>Title: OpenSwing Framework</p>
 * <p>Description: Column editor used to edit a combo-box, linked to a domain type column.</p>
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
public class DomainCellEditor extends AbstractCellEditor implements TableCellEditor {


  /** table */
  private JTable table = null;

  /** combo-box inside the editable cell */
  private JComboBox field = new JComboBox() {

    private KeyEvent oldEv = null;

    public boolean processKeyBinding(KeyStroke ks, KeyEvent e,
                                        int condition, boolean pressed) {
      if (e.getSource()!=null && e.getSource() instanceof org.openswing.swing.table.client.Grid) {
        try {
          if (oldEv==null || !e.equals(oldEv)) {
            oldEv = e;
            field.processKeyEvent(e);
            oldEv = null;
          }
        }
        catch (Exception ex) {
        }
      }
      else if (e.getKeyChar()=='\t' || e.getKeyChar()=='\n') {
        stopCellEditing();
        try {
          if (!table.hasFocus())
            table.requestFocus();
          if (table.getSelectedRow()!=-1)
            table.setRowSelectionInterval(table.getSelectedRow(), table.getSelectedRow());
          table.setColumnSelectionInterval(table.getSelectedColumn() + 1, table.getSelectedColumn() + 1);
        }
        catch (Exception ex) {
        }
      }
      return true;
    }

  };

  /** domain linked to the combo-box */
  private Domain domain = null;

  /** list of couples (code, description) */
  private DomainPair[] pairs = null;

  /** flag sed to set mandatory property of the cell */
  private boolean required;

  /** combo container */
  private JPanel p = new JPanel();


  /**
   * Constructor.
   * @param domain domain linked to the combo-box
   * @param required flag sed to set mandatory property of the cell
   */
  public DomainCellEditor(Domain domain,boolean required,ArrayList itemListeners) {
    this.domain = domain;
    this.required = required;
    pairs = domain.getDomainPairList();
    DefaultComboBoxModel model = new DefaultComboBoxModel();
    for(int i=0;i<pairs.length;i++)
      model.addElement(ClientSettings.getInstance().getResources().getResource(pairs[i].getDescription()));
    field.setModel(model);
    for(int i=0;i<itemListeners.size();i++)
      field.addItemListener((ItemListener)itemListeners.get(i));
    p.setOpaque(true);
    p.setLayout(new GridBagLayout());
    p.add(field,      new GridBagConstraints(0, 0, 1, 1, 1.0, 1.0
          ,GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(0,0,0,0), 0, 0));
  }


  /**
   * Stop cell editing. This method stops cell editing (effectively committing the edit) only if the data entered is validated successfully.
   * @return <code>true</code> if cell editing may stop, and <code>false</code> otherwise.
   */
  public final boolean stopCellEditing() {
    return validate();
  }


  /**
   * Perform the validation.
   */
  private final boolean validate() {
    fireEditingStopped();
    return true;
  }


  public final Object getCellEditorValue() {
    int selIndex = field.getSelectedIndex();
    if (selIndex==-1)
      return null;
    return pairs[selIndex].getCode();
  }


  /**
   * Prepare the editor for a value.
   */
  private final Component _prepareEditor(Object value) {
    DomainPair pair = domain.getDomainPair(value);
    if (pair!=null)
      field.setSelectedItem(ClientSettings.getInstance().getResources().getResource(pair.getDescription()));
    else
      field.setSelectedIndex(-1);
    SwingUtilities.invokeLater(new Runnable() {
      public void run() {
        if (!field.hasFocus()) {
          field.requestFocus();
        }
      }
    });
    return field;
  }


  public final Component getTableCellEditorComponent(JTable table, Object value,
                                               boolean isSelected, int row,
                                               int column) {
    this.table = table;
    if (required) {
      field.setBorder(BorderFactory.createLineBorder(ClientSettings.GRID_REQUIRED_CELL_BORDER));
//      field.setBorder(new CompoundBorder(new RequiredBorder(),field.getBorder()));
    }

    _prepareEditor(value);

    return p;
  }


}


