package org.openswing.swing.table.editors.client;

import java.util.*;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.table.*;

import org.openswing.swing.domains.java.*;
import org.openswing.swing.util.client.*;
import org.openswing.swing.table.client.Grid;
import org.openswing.swing.util.java.Consts;


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

  /** current selected index; stored as attribute to allow the use of the same item description for multiple items */
  private int selIndex = -1;

  /** combo-box inside the editable cell */
  private JComboBox field = getComboBox();


  public JComboBox getComboBox() {
      return new JComboBox() {

        private KeyEvent oldEv = null;

        public boolean processKeyBinding(KeyStroke ks, KeyEvent e, int condition, boolean pressed) {
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


      public void setSelectedIndex(int i) {
        super.setSelectedIndex(i);
        selIndex = i;
      }

    };
  }

  /** domain linked to the combo-box */
  private Domain domain = null;

  /** list of couples (code, description) */
  private DomainPair[] pairs = null;

  /** flag sed to set mandatory property of the cell */
  private boolean required;

  /** combo container */
  private JPanel p = new JPanel();

  /** define if description in combo items must be translated */
  private boolean translateItemDescriptions;

  /** component orientation */
  private ComponentOrientation compOrientation;

  /** item listeners */
  private ArrayList itemListeners = null;


  /**
   * Constructor.
   * @param domain domain linked to the combo-box
   * @param translateItemDescriptions define if description in combo items must be translated
   * @param required flag sed to set mandatory property of the cell
   */
  public DomainCellEditor(Domain domain,boolean translateItemDescriptions,boolean required,
                          ComponentOrientation orientation,ArrayList itemListeners) {
    this.domain = domain;
    this.translateItemDescriptions = translateItemDescriptions;
    this.required = required;
    this.itemListeners = itemListeners;
    pairs = domain.getDomainPairList();
    DefaultComboBoxModel model = new DefaultComboBoxModel();
    for(int i=0;i<pairs.length;i++)
      if (translateItemDescriptions)
        model.addElement(ClientSettings.getInstance().getResources().getResource(pairs[i].getDescription()));
      else
        model.addElement(pairs[i].getDescription());

    this.compOrientation = orientation;
    if (orientation!=null)
      field.setComponentOrientation(orientation);

    field.setModel(model);
    for(int i=0;i<itemListeners.size();i++)
      field.addItemListener((ItemListener)itemListeners.get(i));
    p.setOpaque(true);
    p.setLayout(new GridBagLayout());
    p.add(field,      new GridBagConstraints(0, 0, 1, 1, 1.0, 1.0
          ,GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(0,0,0,0), 0, 0));
  }


  /**
   * Method invoked by ComboColumn, when Domain is changed after grid is already showed.
   * @param domain new Domain to set
   */
  public final void setDomain(Domain domain) {
    this.domain = domain;

    pairs = domain.getDomainPairList();
    DefaultComboBoxModel model = new DefaultComboBoxModel();
    for(int i=0;i<pairs.length;i++)
      if (translateItemDescriptions)
        model.addElement(ClientSettings.getInstance().getResources().getResource(pairs[i].getDescription()));
      else
        model.addElement(pairs[i].getDescription());

    field = getComboBox();

    if (compOrientation!=null)
      field.setComponentOrientation(compOrientation);

    field.setModel(model);
    for(int i=0;i<itemListeners.size();i++)
      field.addItemListener((ItemListener)itemListeners.get(i));

    p = new JPanel();
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
    if (selIndex==-1)
      selIndex = field.getSelectedIndex();
    if (selIndex==-1)
      return null;

    return pairs[selIndex].getCode();
  }


  /**
   * Prepare the editor for a value.
   */
  private final Component _prepareEditor(Object value) {
    DomainPair pair = domain.getDomainPair(value);
    if (pair!=null) {
      for(int i=0;i<pairs.length;i++)
          if (pair.getDescription().equals(pairs[i].getDescription())) {
            field.setSelectedIndex(i);
            break;
          }
    }
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
//    if (table instanceof Grid &&
//        ((Grid)table).getMode()==Consts.INSERT &&
//        value==null &&
//        ClientSettings.PRESET_LAST_VALUE_IN_COMBO_COLUMN &&
//        ((Grid)table).getGrids().getCurrentNumberOfNewRows()>1) {
//      if ( ((Grid)table).getGrids().isInsertRowsOnTop() )
//        value = table.getValueAt(row+1,column);
//      else
//        value = table.getValueAt(row-1,column);
//    }


    this.table = table;
    if (required) {
      field.setBorder(BorderFactory.createLineBorder(ClientSettings.GRID_REQUIRED_CELL_BORDER));
//      field.setBorder(new CompoundBorder(new RequiredBorder(),field.getBorder()));
    }

    _prepareEditor(value);

    return p;
  }


  public final void finalize() {
    table = null;
    field = null;
  }


}


