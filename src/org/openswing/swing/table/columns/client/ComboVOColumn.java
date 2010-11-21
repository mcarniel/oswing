package org.openswing.swing.table.columns.client;

import java.lang.reflect.*;
import java.math.*;
import java.sql.*;
import java.util.*;
import java.util.Date;

import java.awt.event.*;
import javax.swing.table.*;

import org.openswing.swing.items.client.*;
import org.openswing.swing.logger.client.*;
import org.openswing.swing.message.receive.java.*;
import org.openswing.swing.table.client.*;
import org.openswing.swing.table.editors.client.*;
import org.openswing.swing.table.renderers.client.*;
import java.awt.ComponentOrientation;
import org.openswing.swing.util.client.ClientSettings;


/**
 * <p>Title: OpenSwing Framework</p>
 * <p>Description: Column of type combo-box: it contains a combo box showing a list of value objects.
 * Its items are retrieved through the combo box controller, that returns a list of value object;
 * for each value object there exists a row in the combo box: v.o. attributes can be mapped as columns in an item.</p>
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
public class ComboVOColumn extends Column {

  /** combo item listeners */
  private ArrayList itemListeners = new ArrayList();

  /** define if in insert mode combo box has no item selected; default value: <code>false</code> i.e. the first item is pre-selected */
  private boolean nullAsDefaultValue = false;

  /** mapping between items v.o. attributes and items container v.o. attributes */
  private ItemsMapper itemsMapper = new ItemsMapper();

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

  /** component left margin, with respect to component container; defaut value: 2 */
  private int leftMargin = 2;

  /** component right margin, with respect to component container; defaut value: 0 */
  private int rightMargin = 0;

  /** component top margin, with respect to component container; defaut value: 0 */
  private int topMargin = 0;

  /** component bottom margin, with respect to component container; defaut value: 0 */
  private int bottomMargin = 0;

  /** attribute name in the combo-box v.o. that identify the attribute name in the v.o. of the combo-box container; as default value this attribute is null; null means that "attributeName" property will be used to identify the v.o. in the combo-box, i.e. the attribute names in the combo-box v.o. and in the container v.o. must have the same name */
  private String foreignKeyAttributeName;

  /** component orientation */
  private ComponentOrientation orientation = ClientSettings.TEXT_ORIENTATION;

  /** ComboVOTableCellRenderer (cell renderer), one for each grid controller (top locked rows, bottom locked rows, etc.) */
  private HashMap renderers = new HashMap();

  /** ComboBoxVOCellEditor (cell editor), one for each grid controller (top locked rows, bottom locked rows, etc.) */
  private HashMap editors = new HashMap();


  public ComboVOColumn() { }


  /**
   * @return column type
   */
  public int getColumnType() {
    return TYPE_COMBO_VO;
  }


  /**
   * Add an ItemListener to the combo.
   * @param listener ItemListener to add
   */
  public final void addItemListener(ItemListener listener) {
    itemListeners.add(listener);
  }


  /**
   * Remove an ItemListener from the combo.
   * @param listener ItemListener to remove
   */
  public final void removeItemListener(ItemListener listener) {
    itemListeners.remove(listener);
  }


  /**
   * @return ItemListener objects
   */
  public final ArrayList getItemListeners() {
    return itemListeners;
  }


  /**
   * @return define if in insert mode combo box has no item selected
   */
  public final boolean isNullAsDefaultValue() {
    return nullAsDefaultValue;
  }


  /**
   * Define if in insert mode combo box has no item selected.
   * @param nullAsDefaultValue define if in insert mode combo box has no item selected
   */
  public final void setNullAsDefaultValue(boolean nullAsDefaultValue) {
    this.nullAsDefaultValue = nullAsDefaultValue;
  }



  /**
   * Set column visibility in the combo box grid frame.
   * @param comboAttributeName attribute name that identifies the item column
   * @param visible column visibility state
   */
  public final void setVisibleColumn(String comboAttributeName, boolean visible) {
    try {
      Column infoTemp;
      int visibleIndex = -1;
      int index = -1;
      for (int i = 0; i < colProperties.length; i++) {
        if (colProperties[i].isVisible())
          visibleIndex = i;
        if (colProperties[i].getColumnName().equals(comboAttributeName)) {
          colProperties[i].setColumnVisible(visible);
          colProperties[i].setColumnSelectable(visible);
          index = i;
          break;
        }
      }
      if (visible) {
        if (visibleIndex==-1)
          visibleIndex=0;
        else if ((visibleIndex-1) < colProperties.length)
          visibleIndex++;
        if ( (index > -1) && (index != visibleIndex)) {
          infoTemp = colProperties[index];
          colProperties[index] = colProperties[visibleIndex];
          colProperties[visibleIndex] = infoTemp;
        }
      }
    }
    catch (Exception ex) {
      ex.printStackTrace();
    }
  }


  /**
   * Add a link from an attribute of the combo box v.o. to an attribute of the combo box container v.o.
   * @param comboAttributeName attribute of the combo box v.o.
   * @param parentAttributeName attribute of the combo box container v.o.
   */
  public final void addCombo2ParentLink(String comboAttributeName,String parentAttributeName) {
    itemsMapper.addItem2ParentLink(comboAttributeName,parentAttributeName);
  }


  /**
   * Add a link from the whole combo box value object to an equivalent inner v.o. included in the container v.o.
   * @param parentAttributeName attribute of the combo box container v.o., related to an inner v.o. having the same type of the combo box v.o.
   */
  public final void addCombo2ParentLink(String parentAttributeName) {
    itemsMapper.addItem2ParentLink("",parentAttributeName);
  }


  /**
   * @return combo box data locator
   */
  public final ItemsDataLocator getComboDataLocator() {
    return itemsDataLocator;
  }


  /**
   * Set combo box data locator.
   * @param comboDataLocator combo box data locator
   */
  public final void setComboDataLocator(ItemsDataLocator comboDataLocator) {
    this.itemsDataLocator = comboDataLocator;
  }



  /**
   * Set value object class name associated to the combo box: this method calls initItemsVO method.
   * @param itemsValeuObjectClassName value object class name associated to the combo box
   */
  public final void setComboValueObjectClassName(String comboValueObjectClassName) {
    initItemsVO(comboValueObjectClassName);
  }


  /**
   * Method called by setComboValueObjectClassName:
   * - it creates an empty combo v.o
   * - it initializes combo column properties.
   * @param itemsValueObjectClassName combo value object class name
   */
  private void initItemsVO(String itemsValueObjectClassName) {
    try {
      this.itemsVO = (ValueObject) Class.forName(itemsValueObjectClassName).getConstructor(new Class[0]).newInstance(new Object[0]);

      Method[] methods = itemsVO.getClass().getMethods();
      int count = 0;
      for(int i=0;i<methods.length;i++) {
        if (methods[i].getName().startsWith("get") &&
            methods[i].getParameterTypes().length==0 &&
            ( methods[i].getReturnType().equals(String.class) ||
              methods[i].getReturnType().equals(java.math.BigDecimal.class) ||
              methods[i].getReturnType().equals(java.util.Date.class) ||
              methods[i].getReturnType().equals(java.sql.Date.class) ||
              methods[i].getReturnType().equals(java.sql.Timestamp.class) ||
              methods[i].getReturnType().equals(Integer.class) ||
              methods[i].getReturnType().equals(Long.class) ||
              methods[i].getReturnType().equals(Short.class) ||
              methods[i].getReturnType().equals(Double.class) ||
              methods[i].getReturnType().equals(Float.class) ||
              methods[i].getReturnType().equals(Integer.TYPE) ||
              methods[i].getReturnType().equals(Long.TYPE) ||
              methods[i].getReturnType().equals(Short.TYPE) ||
              methods[i].getReturnType().equals(Double.TYPE) ||
              methods[i].getReturnType().equals(Float.TYPE) ||
              methods[i].getReturnType().equals(Boolean.class))
        )
          count++;
      }
      String[] attributeNames = new String[count];
      this.colProperties = new Column[count];
      count = 0;
      Class colType = null;
      for(int i=0;i<methods.length;i++) {
        if (methods[i].getName().startsWith("get") &&
            methods[i].getParameterTypes().length==0 &&
            ( methods[i].getReturnType().equals(String.class) ||
              methods[i].getReturnType().equals(java.math.BigDecimal.class) ||
              methods[i].getReturnType().equals(java.util.Date.class) ||
              methods[i].getReturnType().equals(java.sql.Date.class) ||
              methods[i].getReturnType().equals(java.sql.Timestamp.class) ||
              methods[i].getReturnType().equals(Integer.class) ||
              methods[i].getReturnType().equals(Long.class) ||
              methods[i].getReturnType().equals(Short.class) ||
              methods[i].getReturnType().equals(Double.class) ||
              methods[i].getReturnType().equals(Float.class) ||
              methods[i].getReturnType().equals(Integer.TYPE) ||
              methods[i].getReturnType().equals(Long.TYPE) ||
              methods[i].getReturnType().equals(Short.TYPE) ||
              methods[i].getReturnType().equals(Double.TYPE) ||
              methods[i].getReturnType().equals(Float.TYPE) ||
              methods[i].getReturnType().equals(Boolean.class))
        ) {
          attributeNames[count] = methods[i].getName().substring(3);
          if (attributeNames[count].length()>1)
            attributeNames[count] = attributeNames[count].substring(0,1).toLowerCase()+attributeNames[count].substring(1);
          colType = methods[i].getReturnType();
          if (colType.equals(String.class))
            colProperties[count] = new TextColumn();
          else if (colType.equals(Integer.class) || colType.equals(Long.class) || colType.equals(Short.class) ||
                   colType.equals(Integer.TYPE)  || colType.equals(Long.TYPE)  || colType.equals(Short.TYPE))
            colProperties[count] = new IntegerColumn();
          else if (colType.equals(BigDecimal.class) || colType.equals(Double.class) || colType.equals(Float.class) ||
                                                       colType.equals(Double.TYPE)  || colType.equals(Float.TYPE))
            colProperties[count] = new DecimalColumn();
          else if (colType.equals(Boolean.class))
            colProperties[count] = new CheckBoxColumn();
          else if (colType.equals(Date.class))
            colProperties[count] = new DateColumn();
          else if (colType.equals(java.sql.Date.class))
            colProperties[count] = new DateColumn();
          else if (colType.equals(Timestamp.class))
            colProperties[count] = new DateColumn();

          colProperties[count].setColumnName(attributeNames[count]);
          if (colProperties[count].getHeaderColumnName().equals("columnname"))
            colProperties[count].setHeaderColumnName(String.valueOf(attributeNames[count].charAt(0)).toUpperCase()+attributeNames[count].substring(1));
          colProperties[count].setColumnVisible(this.allColumnVisible);
          colProperties[count].setPreferredWidth(this.allColumnPreferredWidth);
          getters.put(
            colProperties[count].getColumnName(),
            methods[i]
          );
          count++;
        }
      }

    }
    catch (Exception ex) {
      ex.printStackTrace();
      this.itemsVO = null;
    }
    catch (Error er) {
      er.printStackTrace();
      this.itemsVO = null;
    }
  }


  /**
   * @return columns visibility
   */
  public final boolean isAllColumnVisible() {
    return this.allColumnVisible;
  }


  /**
   * Set column visibility for the whole columns of the items grid frame.
   * @param visible columns visibility
   */
  public final void setAllColumnVisible(boolean visible) {
    this.allColumnVisible = visible;
    for(int i=0; i<colProperties.length; i++) {
      colProperties[i].setColumnVisible(visible);
      colProperties[i].setColumnSelectable(visible);
    }
  }


  /**
   * @return columns width
   */
  public final int getAllColumnPreferredWidth() {
    return this.allColumnPreferredWidth;
  }


  /**
   * Set columns width for the whole columns of the items grid frame.
   * @param preferredWidth columns width
   */
  public final void setAllColumnPreferredWidth(int preferredWidth) {
    this.allColumnPreferredWidth = preferredWidth;
    for(int i=0; i<colProperties.length; i++)
      colProperties[i].setPreferredWidth(preferredWidth);
  }


  /**
   * Set column width in the items grid frame.
   * @param itemsAttributeName attribute name that identifies the grid column
   * @param preferredWidth column width
   */
  public final void setPreferredWidthColumn(String itemsAttributeName,int preferredWidth) {
    for(int i=0;i<colProperties.length;i++)
      if (colProperties[i].getColumnName().equals(itemsAttributeName)) {
        colProperties[i].setPreferredWidth(preferredWidth);
        return;
      }
    Logger.error(this.getClass().getName(),"setPreferredWidthColumn","The attribute '"+(itemsAttributeName==null?"null":"'"+itemsAttributeName+"'")+"' does not exist.",null);
  }
  public Column[] getColProperties() {
    return colProperties;
  }
  public Hashtable getGetters() {
    return getters;
  }
  public ValueObject getItemsVO() {
    return itemsVO;
  }
  public ItemsMapper getItemsMapper() {
    return itemsMapper;
  }


  /**
   * @return component bottom margin, with respect to component container
   */
  public final int getBottomMargin() {
    return bottomMargin;
  }


  /**
   * @return component left margin, with respect to component container
   */
  public final int getLeftMargin() {
    return leftMargin;
  }


  /**
   * @return component right margin, with respect to component container
   */
  public final int getRightMargin() {
    return rightMargin;
  }


  /**
   * @return component top margin, with respect to component container
   */
  public final int getTopMargin() {
    return topMargin;
  }


  /**
   * Set component top margin, with respect to component container.
   * @param topMargin component top margin
   */
  public final void setTopMargin(int topMargin) {
    this.topMargin = topMargin;
  }


  /**
   * Set component right margin, with respect to component container.
   * @param rightMargin component right margin
   */
  public final void setRightMargin(int rightMargin) {
    this.rightMargin = rightMargin;
  }


  /**
   * Set component left margin, with respect to component container.
   * @param leftMargin component left margin
   */
  public final void setLeftMargin(int leftMargin) {
    this.leftMargin = leftMargin;
  }


  /**
   * Set component bottom margin, with respect to component container.
   * @param bottomMargin component bottom margin
   */
  public final void setBottomMargin(int bottomMargin) {
    this.bottomMargin = bottomMargin;
  }


  /**
   * @return attribute name in the combo-box v.o. that identify the attribute name in the v.o. of the combo-box container
   */
  public final String getForeignKeyAttributeName() {
    return foreignKeyAttributeName;
  }


  /**
   * Set the attribute name in the combo-box v.o. that identify the attribute name in the v.o. of the combo-box container.
   * As default value this attribute is null.
   * Null means that "attributeName" property will be used to identify the v.o. in the combo-box, i.e. the attribute names in the combo-box v.o. and in the container v.o. must have the same name.
   * @param foreignKeyAttributeName String
   */
  public final void setForeignKeyAttributeName(String foreignKeyAttributeName) {
    this.foreignKeyAttributeName = foreignKeyAttributeName;
  }


  /**
   * Method used to reload items in combo-box.
   */
  public final void reloadItems() {
    ComboVOTableCellRenderer renderer = null;
    ComboBoxVOCellEditor editor = null;
    Iterator it = renderers.values().iterator();
    while(it.hasNext()) {
      renderer = (ComboVOTableCellRenderer)it.next();
      renderer.reloadItems();
    }
    it = editors.values().iterator();
    while(it.hasNext()) {
      editor = (ComboBoxVOCellEditor)it.next();
      editor.reloadItems();
    }
  }


  /**
   * Set the component orientation: from left to right or from right to left.
   * @param orientation component orientation
   */
  public final void setTextOrientation(ComponentOrientation orientation) {
    this.orientation = orientation;
  }


  /**
   * @return component orientation
   */
  public final ComponentOrientation getTextOrientation() {
      return orientation;
  }


  /**
   * @return TableCellRenderer for this column
   */
  public final TableCellRenderer getCellRenderer(GridController tableContainer,Grids grids) {
    ComboVOTableCellRenderer renderer = (ComboVOTableCellRenderer)renderers.get(tableContainer.toString());
    if (renderer==null) {
      renderer = new ComboVOTableCellRenderer(
        getComboDataLocator(),
        getColumnName(),
        getItemsVO(),
        getColProperties(),
        isAllColumnVisible(),
        getAllColumnPreferredWidth(),
        getGetters(),
        tableContainer,
        leftMargin,
        rightMargin,
        topMargin,
        bottomMargin,
        getTextOrientation(),
        getForeignKeyAttributeName()
      );
      renderers.put(tableContainer.toString(),renderer);
    }
    return renderer;
  }


  /**
   * @return TableCellEditor for this column
   */
  public final TableCellEditor getCellEditor(GridController tableContainer,Grids grids) {
    ComboBoxVOCellEditor editor = (ComboBoxVOCellEditor)editors.get(tableContainer.toString());
    if (editor==null) {
      editor = new ComboBoxVOCellEditor(
        getItemsMapper(),
        getComboDataLocator(),
        getColumnName(),
        getItemsVO(),
        getColProperties(),
        isAllColumnVisible(),
        getAllColumnPreferredWidth(),
        getGetters(),
        isColumnRequired(),
        getItemListeners(),
        getForeignKeyAttributeName(),
        leftMargin,
        rightMargin,
        topMargin,
        bottomMargin,
        getTextOrientation()
      );
      editors.put(tableContainer.toString(),editor);
    }
    return editor;
  }


}
