package org.openswing.swing.table.editors.client;

import java.lang.reflect.*;
import java.util.*;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.table.*;

import org.openswing.swing.items.client.*;
import org.openswing.swing.logger.client.*;
import org.openswing.swing.message.receive.java.*;
import org.openswing.swing.table.client.*;
import org.openswing.swing.table.columns.client.*;
import org.openswing.swing.util.client.*;


/**
 * <p>Title: OpenSwing Framework</p>
 * <p>Description: Column editor used to edit a ComboVOControl, linked to a list of value objects.</p>
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
public class ComboBoxVOCellEditor extends AbstractCellEditor implements TableCellEditor,ItemsParent {


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

  /** flag sed to set mandatory property of the cell */
  private boolean required;

  /** combo container */
  private JPanel p = new JPanel();

  /** combo box model */
  private DefaultComboBoxModel model = new DefaultComboBoxModel();

  /** items data source */
  private ItemsDataLocator itemsDataLocator = null;

  /** items value object */
  private ValueObject itemsVO = null;

  /** columns associated to lookup grid */
  private Column[] colProperties = new Column[0];

  /** flag used to set visibility on all columns of lookup grid; default "false"  */
  private boolean allColumnVisible = false;

  /** default preferredWidth for all columns of lookup grid; default 100 pixels */
  private int allColumnPreferredWidth = 100;

  /** collection of pairs <v.o. attribute name,Method object, related to the attribute getter method> */
  private Hashtable getters = new Hashtable();

  /** value objects list  */
  private ArrayList items = null;

  /** cell content */
  private ItemRenderer rend = new ItemRenderer();

  private String attributeName;

  /** current row in edit */
  private int row = -1;

  /** mapping between items v.o. attributes and items container v.o. attributes */
  private ItemsMapper itemsMapper = new ItemsMapper();

  /** attribute name in the combo-box v.o. that identify the attribute name in the v.o. of the combo-box container; as default value this attribute is null; null means that "attributeName" property will be used to identify the v.o. in the combo-box, i.e. the attribute names in the combo-box v.o. and in the container v.o. must have the same name */
  private String foreignKeyAttributeName;

  /**
   * @return attribute name in the combo-box v.o. that identify the combo-box item
   */
  private String getFKAttributeName() {
    return
        foreignKeyAttributeName==null || foreignKeyAttributeName.equals("") ?
        attributeName :
        foreignKeyAttributeName;
  }

  static {
    UIManager.put("ComboBox.disabledForeground", UIManager.get("ComboBox.foreground"));
    UIManager.put("ComboBox.disabledBackground", UIManager.get("TextField.inactiveBackground"));
    UIManager.put("ComboBox.selectionForeground", Color.black );
    UIManager.put("ComboBox.selectionBackground", ClientSettings.BACKGROUND_SEL_COLOR );
  }


  /**
   * Constructor.
   * @param domain domain linked to the combo-box
   * @param required flag sed to set mandatory property of the cell
   */
  public ComboBoxVOCellEditor(
      ItemsMapper itemsMapper,
      ItemsDataLocator itemsDataLocator,
      String attributeName,
      ValueObject itemsVO,
      Column[] colProperties,
      boolean allColumnVisible,
      int allColumnPreferredWidth,
      Hashtable getters,
      boolean required,
      ArrayList itemListeners,
      String foreignKeyAttributeName,
      int leftMargin,int rightMargin,int topMargin,int bottomMargin,
      ComponentOrientation orientation
  ) {
    this.itemsMapper = itemsMapper;
    this.itemsDataLocator = itemsDataLocator;
    this.attributeName = attributeName;
    this.itemsVO = itemsVO;
    this.colProperties = colProperties;
    this.allColumnVisible = allColumnVisible;
    this.allColumnPreferredWidth = allColumnPreferredWidth;
    this.getters = getters;
    this.required = required;
    this.foreignKeyAttributeName = foreignKeyAttributeName;

    if (orientation!=null)
      field.setComponentOrientation(orientation);

    if (itemsDataLocator!=null && itemsVO!=null) {
      Response res = itemsDataLocator.loadData(itemsVO.getClass());
      if (!res.isError()) {
        java.util.List items = ((VOListResponse)res).getRows();
        for(int i=0;i<items.size();i++) {
          model.addElement(items.get(i));
        }
        field.setModel(model);
        field.revalidate();
        field.repaint();
        field.setSelectedIndex(-1);
      }
    }


    ItemRenderer rend = new ItemRenderer();
    rend.init(getters,colProperties,leftMargin,rightMargin,topMargin,bottomMargin);
    field.setRenderer(rend);

    field.addItemListener(new ItemListener() {
      public void itemStateChanged(ItemEvent e) {
        // update v.o. container...
        if (e.getStateChange()==e.SELECTED) {
          ComboBoxVOCellEditor.this.itemsVO = (ValueObject)e.getItem();
          updateParentModel(ComboBoxVOCellEditor.this);
        }
      }
    });


    for(int i=0;i<itemListeners.size();i++)
      field.addItemListener((ItemListener)itemListeners.get(i));
    p.setOpaque(true);
    p.setLayout(new GridBagLayout());
    p.add(field,      new GridBagConstraints(0, 0, 1, 1, 1.0, 1.0
          ,GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(0,0,0,0), 0, 0));
  }


  /**
   * Method used to reload items in combo-box.
   */
  public final void reloadItems() {
    if (itemsDataLocator!=null && itemsVO!=null) {
      Response res = itemsDataLocator.loadData(itemsVO.getClass());
      if (!res.isError()) {
        java.util.List items = ((VOListResponse)res).getRows();
        model = new DefaultComboBoxModel();
        for(int i=0;i<items.size();i++) {
          model.addElement(items.get(i));
        }
        field.setModel(model);
        field.revalidate();
        field.repaint();
        field.setSelectedIndex(-1);
      }
    }
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

    try {
      return ( (Method) getters.get(getFKAttributeName())).invoke(
        model.getElementAt(selIndex),
        new Object[0]
      );
    }
    catch (Throwable ex) {
      return null;
    }
  }


  /**
   * Prepare the editor for a value.
   */
  private final Component _prepareEditor(Object value) {
    if (value==null)
      field.setSelectedIndex(-1);
    if (getFKAttributeName()!=null) {
      Object obj = null;
      try {
        field.setSelectedIndex(-1);
        for (int i = 0; i < model.getSize(); i++) {
          obj = ( (Method) getters.get(getFKAttributeName())).invoke(
              model.getElementAt(i),
              new Object[0]
          );
          if (value.equals(obj)) {
            field.setSelectedIndex(i);
            break;
          }
        }
      }
      catch (Throwable ex) {
        field.setSelectedIndex(-1);
      }
    }

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
    this.row = row;
    if (required) {
      field.setBorder(BorderFactory.createLineBorder(ClientSettings.GRID_REQUIRED_CELL_BORDER));
//      field.setBorder(new CompoundBorder(new RequiredBorder(),field.getBorder()));
    }

    _prepareEditor(value);

    return p;
  }



  /**
   * Update the value object of the items parent container, only for attributes defined in ItemsMapper.
   * @param lookupParent lookup container
   */
  private void updateParentModel(ItemsParent itemsParent) {
    if (itemsVO!=null && itemsMapper!=null) {
      try {
        // update items container vo from items vo values...
        Enumeration itemsAttributes = itemsMapper.getItemsChangedAttributes();
        String itemsAttributeName, itemsMethodName;
        Method itemsMethod;
        String attrName = null;
        while (itemsAttributes.hasMoreElements()) {
          itemsAttributeName = (String) itemsAttributes.nextElement();
          if (itemsAttributeName.length()==0) {
            // there has been defined a link between the whole items v.o. and an attribute in the container v.o.
            // related to an inner v.o.
            if (!itemsMapper.setParentAttribute(
                itemsParent,
                itemsAttributeName,
                itemsVO.getClass(),
                itemsVO))
              Logger.error(this.getClass().getName(), "updateParentModel", "Error while setting items container value object.", null);
         }
         else {
           itemsMethodName = "get" + String.valueOf(Character.toUpperCase(itemsAttributeName.charAt(0))) + itemsAttributeName.substring(1);
           itemsMethod = itemsVO.getClass().getMethod(itemsMethodName, new Class[0]);
           if (!itemsMapper.setParentAttribute(
                 itemsParent,
                 itemsAttributeName,
                 itemsMethod.getReturnType(),
                 itemsMethod.invoke(itemsVO, new Object[0])
           ))
             Logger.error(this.getClass().getName(),"updateParentModel","Error while setting items container value object.",null);
         }
        }

      }
      catch (Exception ex) {
        ex.printStackTrace();
      }
      catch (Error er) {
        er.printStackTrace();
      }

    } else {
      Logger.error(this.getClass().getName(),"updateParentModel","You must set 'itemsMapper' property",null);
    }
  }


  public ValueObject getValueObject() {
    if (row==-1)
      return null;
    return ((Grid)table).getVOListTableModel().getObjectForRow(row);
  }


  /**
   * setValue
   *
   * @param attributeName String
   * @param value Object
   */
  public void setValue(String attributeName, Object value) {
    if (row!=-1)
//      ((Grid)table).getVOListTableModel().setValueAt(value,row,((Grid)table).getColumnIndex(attributeName));
      ((Grid)table).getVOListTableModel().setField(row,attributeName,value);
  }


  public final void finalize() {
    colProperties = null;
    table = null;
    field = null;
    itemsMapper = null;
    itemsDataLocator = null;
    rend = null;
  }


}


