package org.openswing.swing.client;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.beans.Beans;
import org.openswing.swing.client.*;
import org.openswing.swing.domains.java.*;

import java.awt.Container;
import org.openswing.swing.form.client.Form;
import org.openswing.swing.util.client.*;
import org.openswing.swing.message.receive.java.ValueObject;
import org.openswing.swing.items.client.*;
import java.lang.reflect.Method;
import org.openswing.swing.table.columns.client.Column;
import org.openswing.swing.table.columns.client.*;
import java.math.BigDecimal;
import java.util.Date;
import java.sql.Timestamp;
import org.openswing.swing.message.receive.java.Response;
import java.util.ArrayList;
import org.openswing.swing.message.receive.java.VOListResponse;
import org.openswing.swing.logger.client.Logger;
import java.util.Hashtable;
import java.lang.reflect.*;
import java.util.Enumeration;
import org.openswing.swing.util.java.Consts;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.ListSelectionEvent;
import java.beans.Introspector;
import java.beans.BeanInfo;
import java.beans.*;
import java.text.SimpleDateFormat;


/**
 * <p>Title: OpenSwing Framework</p>
 * <p>Description: List input control: its items are retrieved through the items controller, that returns a list of value objects;
 * for each value object there is a row in the list control: v.o. attributes can be mapped as columns in an item.</p>
 * If this control is linked to a Form, then ListSelectionModel.SINGLE_SELECTION_MODE is allowed:
 * other selection modes are not permitted. The list control v.o. is then mapped to Form container v.o.
 * If this control is not linked to a Form, then it is possible to select more than one item.
 * </p>
 * <p>Copyright: Copyright (C) 2007 Mauro Carniel</p>
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
public class ListVOControl extends BaseInputControl implements InputControl,ItemsParent,SearchControl {


  /** list */
  private JList list = new JList();

  /** mapping between items v.o. attributes and items container v.o. attributes */
  private ItemsMapper itemsMapper = new ItemsMapper();

  /** items data source */
  private ItemsDataLocator itemsDataLocator = null;

  /** items value object */
  private ValueObject itemsVO = null;

  /** columns associated to lookup grid */
  private Column[] colProperties = new Column[0];

  /** list model */
  private DefaultListModel model = new DefaultListModel();

  /** flag used to set visibility on all columns of lookup grid; default "false"  */
  private boolean allColumnVisible = false;

  /** default preferredWidth for all columns of lookup grid; default 100 pixels */
  private int allColumnPreferredWidth = 100;

  /** collection of pairs <v.o. attribute name,Method object, related to the attribute getter method> */
  private Hashtable getters = new Hashtable();

  /** define if in insert mode list has no item selected; default value: <code>false</code> i.e. the first item is pre-selected */
  private boolean nullAsDefaultValue = false;

  /** flag used in addNotify method to retrieve items */
  private boolean itemsLoaded = false;

  /** Form container (optional) */
  private Form form = null;

  /** attribute name in the list v.o. that identify the attribute name in the v.o. of the list container; as default value this attribute is null; null means that "attributeName" property will be used to identify the v.o. in the list, i.e. the attribute names in the list v.o. and in the container v.o. must have the same name */
  private String foreignKeyAttributeName;


  /**
   * Contructor.
   */
  public ListVOControl() {
    this.setLayout(new GridBagLayout());
    this.add(new JScrollPane(list), new GridBagConstraints(0, 0, 1, 1, 1.0, 1.0, GridBagConstraints.WEST, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
    setOpaque(false);
    list.addKeyListener(new KeyAdapter() {
      public void keyPressed(KeyEvent e) {
        if (e.getKeyCode()==e.VK_CANCEL || e.getKeyCode()==e.VK_BACK_SPACE || e.getKeyCode()==e.VK_DELETE)
          list.clearSelection();
      }
    });
    list.setSelectionForeground((Color)UIManager.get("TextField.foreground"));

    initListeners();
    getBindingComponent().addFocusListener(new FocusListener() {

      public void focusGained(FocusEvent e) {
        if (ClientSettings.VIEW_BACKGROUND_SEL_COLOR && isEnabled()) {
          getBindingComponent().setBackground( (Color) UIManager.get("TextField.background") );
        }
      }

      public void focusLost(FocusEvent e) {
      }

    });

    new SearchWindowManager(this);

    if (ClientSettings.TEXT_ORIENTATION!=null)
        setComponentOrientation(ClientSettings.TEXT_ORIENTATION);
  }


  /**
   * Reset list content, by invoking ItemsDataLocator.loadData() method again.
   */
  public final void reloadItems() {
    if (itemsDataLocator!=null && itemsVO!=null) {
      Response res = itemsDataLocator.loadData(itemsVO.getClass());
      if (!res.isError()) {
        model.clear();
        java.util.List items = ((VOListResponse)res).getRows();
        for(int i=0;i<items.size();i++) {
          model.addElement(items.get(i));
        }
        list.setModel(model);
        list.revalidate();
        list.repaint();
        list.setSelectedIndex(-1);
      }
    }
  }


  /**
   * Retrieve items.
   */
  public final void addNotify() {
    super.addNotify();
    if (!itemsLoaded) {
      itemsLoaded = true;
      if (itemsDataLocator!=null && itemsVO!=null) {
        Response res = itemsDataLocator.loadData(itemsVO.getClass());
        if (!res.isError()) {
          java.util.List items = ((VOListResponse)res).getRows();
          for(int i=0;i<items.size();i++) {
            model.addElement(items.get(i));
          }
          list.setModel(model);
          list.revalidate();
          list.repaint();
          list.setSelectedIndex(-1);
        }
      }


      ItemRenderer rend = new ItemRenderer();
      rend.init(getters,colProperties,1,0,0,0);
      list.setCellRenderer(rend);

      list.addListSelectionListener(new ListSelectionListener() {
        public void valueChanged(ListSelectionEvent e) {
          // update v.o. container ONLY IF single selection model is setted...
          if (e.getValueIsAdjusting() &&
              (form==null || form.getMode()!=Consts.READONLY) &&
              list.getSelectionMode()==ListSelectionModel.SINGLE_SELECTION) {
            itemsVO = (ValueObject)model.get(e.getFirstIndex());
            updateParentModel(ListVOControl.this);
          }
        }
      });

      // set Form object in the lookup container...
      form = ClientUtils.getLinkedForm(this);

    }


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
            else if (form!=null) {
               attrName = (String)itemsMapper.getParentAttributeName(itemsAttributeName);
               if (attrName!=null && form!=null)
                 form.pull(attrName);
             }
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
           else if (form!=null) {
              attrName = (String)itemsMapper.getParentAttributeName(itemsAttributeName);
              if (attrName!=null && form!=null)
                form.pull(attrName);
            }
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



  /**
   * Set column visibility in the list grid frame.
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
   * Add a link from an attribute of the list v.o. to an attribute of the list container v.o.
   * @param comboAttributeName attribute of the list v.o.
   * @param parentAttributeName attribute of the list container v.o.
   */
  public final void addList2ParentLink(String comboAttributeName,String parentAttributeName) {
    itemsMapper.addItem2ParentLink(comboAttributeName,parentAttributeName);
  }


  /**
   * Add a link from the whole list value object to an equivalent inner v.o. included in the container v.o.
   * @param parentAttributeName attribute of the list container v.o., related to an inner v.o. having the same type of the list v.o.
   */
  public final void addList2ParentLink(String parentAttributeName) {
    itemsMapper.addItem2ParentLink("",parentAttributeName);
  }


  /**
   * @return list data locator
   */
  public final ItemsDataLocator getListDataLocator() {
    return itemsDataLocator;
  }


  /**
   * Set list data locator.
   * @param comboDataLocator list data locator
   */
  public final void setListDataLocator(ItemsDataLocator comboDataLocator) {
    this.itemsDataLocator = comboDataLocator;
  }



  /**
   * Set value object class name associated to the list: this method calls initItemsVO method.
   * @param itemsValeuObjectClassName value object class name associated to the list
   */
  public final void setListValueObjectClassName(String comboValueObjectClassName) {
    initItemsVO(comboValueObjectClassName);
  }


  /**
   * Method called by setComboValueObjectClassName:
   * - it creates an empty list v.o
   * - it initializes list column properties.
   * @param itemsValueObjectClassName list value object class name
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
              methods[i].getReturnType().equals(Double.class) ||
              methods[i].getReturnType().equals(Float.class) ||
              methods[i].getReturnType().equals(Short.class) ||
              methods[i].getReturnType().equals(Integer.TYPE) ||
              methods[i].getReturnType().equals(Long.TYPE) ||
              methods[i].getReturnType().equals(Double.TYPE) ||
              methods[i].getReturnType().equals(Float.TYPE) ||
              methods[i].getReturnType().equals(Short.TYPE) ||
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
              methods[i].getReturnType().equals(Double.class) ||
              methods[i].getReturnType().equals(Float.class) ||
              methods[i].getReturnType().equals(Short.class) ||
              methods[i].getReturnType().equals(Integer.TYPE) ||
              methods[i].getReturnType().equals(Long.TYPE) ||
              methods[i].getReturnType().equals(Double.TYPE) ||
              methods[i].getReturnType().equals(Float.TYPE) ||
              methods[i].getReturnType().equals(Short.TYPE) ||
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
          else if (colType.equals(BigDecimal.class) ||
                   colType.equals(Double.class) || colType.equals(Float.class) ||
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


  /**
   * Select an item from the list starting from the specified code (with ListSelectionModel.SINGLE_SELECTION) or
   * select a list of items starting from the specified java.util.List of codes (without ListSelectionModel.SINGLE_SELECTION) or
   * @param code used to retrieve the corresponding item and to select that item in the list (with ListSelectionModel.SINGLE_SELECTION)
   * or
   * java.util.List of codes (without ListSelectionModel.SINGLE_SELECTION) when this input control is not linked to a Form
   * or
   * java.util.List of list's value objects (without ListSelectionModel.SINGLE_SELECTION) when this input control is linked to a Form
   */
  public final void setValue(Object code) {
    if (list.getSelectionMode()==ListSelectionModel.SINGLE_SELECTION) {
      if (code==null)
        list.setSelectedIndex(-1);
      if (getFKAttributeName()==null)
        return;
      Object obj = null;
      try {
        for (int i = 0; i < model.getSize(); i++) {
          obj = ( (Method) getters.get(getFKAttributeName())).invoke(
              model.getElementAt(i),
              new Object[0]
          );
          if (code.equals(obj)) {
            list.setSelectedIndex(i);
            try {
              list.scrollRectToVisible(list.getCellBounds(i, i));
              list.repaint();
            }
            catch (Exception ex) {
            }
            return;
          }
        }
      }
      catch (Throwable ex) {
        list.setSelectedIndex(-1);
      }
      list.setSelectedIndex(-1);
    }
    else if (form!=null) {
      // this input control has been linked to a Form: attribute name will be ignored...
      if (code==null || code instanceof java.util.List && ((java.util.List)code).size()==0) {
        list.getSelectionModel().clearSelection();
        return;
      }

      if (code instanceof java.util.List) {
        java.util.List codes = (java.util.List)code;
        list.getSelectionModel().clearSelection();
        Object obj = null;
        for(int j=0;j<codes.size();j++)
          try {
            for (int i = 0; i < model.getSize(); i++) {
              obj = model.getElementAt(i);
              if (voequals(codes.get(j),obj)) {
                list.getSelectionModel().addSelectionInterval(i,i);
                try {
                  list.scrollRectToVisible(list.getCellBounds(i, i));
                  list.repaint();
                }
                catch (Exception ex) {
                }
                break;
              }
            }
          }
          catch (Throwable ex) {
          }
      }
      else
        Logger.error(this.getClass().getName(), "setValue", "You must specify a java.util.List argument type", null);
    }
    else {
      if (code==null || code instanceof java.util.List && ((java.util.List)code).size()==0) {
        list.getSelectionModel().clearSelection();
        return;
      }
      if (getFKAttributeName()==null)
        return;

      if (code instanceof java.util.List) {
        java.util.List codes = (java.util.List)code;
        list.getSelectionModel().clearSelection();
        Object obj = null;
        for(int j=0;j<codes.size();j++)
          try {
            for (int i = 0; i < model.getSize(); i++) {
              obj = ( (Method) getters.get(getFKAttributeName())).invoke(
                  model.getElementAt(i),
                  new Object[0]
              );
              if (codes.get(j).equals(obj)) {
                list.getSelectionModel().addSelectionInterval(i,i);
                try {
                  list.scrollRectToVisible(list.getCellBounds(i, i));
                  list.repaint();
                }
                catch (Exception ex) {
                }
                break;
              }
            }
          }
          catch (Throwable ex) {
          }
      }
      else
        Logger.error(this.getClass().getName(), "setValue", "You must specify a java.util.List argument type", null);
    }
  }


  private boolean voequals(Object vo1,Object vo2) {
    if (!vo1.getClass().equals(vo2.getClass()))
      return false;
    try {
      BeanInfo info = Introspector.getBeanInfo(vo1.getClass());
      MethodDescriptor[] m = info.getMethodDescriptors();
      Object o1,o2;
      for(int i=0;i<m.length;i++)
        if (m[i].getMethod().getName().startsWith("get") && m[i].getMethod().getParameterTypes().length==0 ||
            m[i].getMethod().getName().startsWith("is") && m[i].getMethod().getParameterTypes().length==0) {
          o1 = m[i].getMethod().invoke(vo1,new Object[0]);
          o2 = m[i].getMethod().invoke(vo2,new Object[0]);
          if (o1==null && o2!=null || o1!=null && o2==null)
            return false;
          if (o1!=null && o2!=null && !o1.equals(o2))
            return false;
        }
      return true;
    }
    catch (Throwable ex) {
      return false;
    }
  }


  /**
   * @return in case of ListSelectionModel.SINGLE_SELECTION: the code related to the selected list item; it return null if no item is selected;
   * a java.util.List of codes in case of MULTIPLE_INTERVAL_SELECTION/SINGLE_INTERVAL_SELECTION if this control is not linked to a Form
   * a java.util.List of value objects in case of MULTIPLE_INTERVAL_SELECTION/SINGLE_INTERVAL_SELECTION if this control is linked to a Form
   */
  public final Object getValue() {
    if (list.getSelectionMode()==ListSelectionModel.SINGLE_SELECTION) {
      if (getFKAttributeName()==null)
        return null;
      if (list.getSelectedIndex()==-1)
        return null;
      try {
        return ( (Method) getters.get(getFKAttributeName())).invoke(
          model.getElementAt(list.getSelectedIndex()),
          new Object[0]
        );
      }
      catch (Throwable ex) {
        return null;
      }
    }
    else if (form!=null) {
      // this input control is linked to a form: attribute name will be ignored...
      if (list.getSelectedIndex()==-1)
        return new ArrayList();
      ArrayList vos = new ArrayList();
      int[] values = list.getSelectedIndices();
      for(int j=0;j<values.length;j++)
        vos.add(model.getElementAt(values[j]));
      return vos;
    }
    else {
      // form is null and attribute name is related to the attribute in the list control v.o. that identity the code to extract and to add to the returned list of codes...
      if (getFKAttributeName()==null || list.getSelectedIndex()==-1)
        return new ArrayList();
      if (getFKAttributeName()!=null && getters.get(getFKAttributeName())==null) {
        Logger.error(this.getClass().getName(), "setValue", "There is not an attribute named '"+getFKAttributeName()+"' in the selected value object of type '"+itemsVO.getClass()+"'", null);
        return new ArrayList();
      }
      ArrayList codes = new ArrayList();
      int[] values = list.getSelectedIndices();
      for(int j=0;j<values.length;j++)
        try {
          codes.add(
            ((Method)getters.get(getFKAttributeName())).invoke(
              model.getElementAt(values[j]),
              new Object[0]
            )
          );
        }
        catch (Throwable ex) {
          ex.printStackTrace();
        }

      return codes;
    }
  }


  /**
   * @return component inside this whose contains the value
   */
  public JComponent getBindingComponent() {
    return list;
  }


  /**
   * Adds the specified focus listener to receive focus events from
   * this component when this component gains input focus.
   * If listener <code>l</code> is <code>null</code>,
   * no exception is thrown and no action is performed.
   *
   * @param    l   the focus listener
   * @see      java.awt.event.FocusEvent
   * @see      java.awt.event.FocusListener
   * @see      #removeFocusListener
   * @see      #getFocusListeners
   * @since    JDK1.1
   */
  public final void addFocusListener(FocusListener l) {
    try {
      list.addFocusListener(l);
    }
    catch (Exception ex) {
    }
  }


  /**
   * Removes the specified focus listener so that it no longer
   * receives focus events from this component. This method performs
   * no function, nor does it throw an exception, if the listener
   * specified by the argument was not previously added to this component.
   * If listener <code>l</code> is <code>null</code>,
   * no exception is thrown and no action is performed.
   *
   * @param    l   the focus listener
   * @see      java.awt.event.FocusEvent
   * @see      java.awt.event.FocusListener
   * @see      #addFocusListener
   * @see      #getFocusListeners
   * @since    JDK1.1
   */
  public final void removeFocusListener(FocusListener l) {
    try {
      list.removeFocusListener(l);
    }
    catch (Exception ex) {
    }
  }


  /**
   * @return define if in insert mode list has no item selected
   */
  public final boolean isNullAsDefaultValue() {
    return nullAsDefaultValue;
  }


  /**
   * Define if in insert mode list has no item selected.
   * @param nullAsDefaultValue define if in insert mode list has no item selected
   */
  public final void setNullAsDefaultValue(boolean nullAsDefaultValue) {
    this.nullAsDefaultValue = nullAsDefaultValue;
  }



  /**
   * Method called by ItemsController to update parent v.o.
   * @param attributeName attribute name in the parent v.o. that must be updated
   * @param value updated value
   */
  public void setValue(String attributeName,Object value) {
    if (form!=null)
      form.getVOModel().setValue(attributeName,value);
  }


  /**
   * @return parent value object
   */
  public ValueObject getValueObject() {
    return form==null?null:form.getVOModel().getValueObject();
  }


  /**
   * Select the list item related to the specified index.
   * @param index index to retrieve the corresponding item and to select that item in the list
   */
  public final void setSelectedIndex(int index) {
    list.setSelectedIndex(index);
    try {
      list.scrollRectToVisible(list.getCellBounds(index, index));
    }
    catch (Exception ex) {
    }
  }


  /**
   * Clears the selection - after calling this method
   * <code>isSelectionEmpty</code> will return true.
   * This is a convenience method that just delegates to the
   * <code>selectionModel</code>.
   *
   * @see ListSelectionModel#clearSelection
   * @see #isSelectionEmpty
   * @see #addListSelectionListener
   */
  public final void clearSelection() {
    list.clearSelection();
  }


  /**
   * Select the list item related to the specified index.
   * @param indices interval of indices to retrieve the corresponding item and to select that item in the list
   */
  public final void setSelectedIndices(int[] indices) {
    list.setSelectedIndices(indices);
    try {
      list.scrollRectToVisible(list.getCellBounds(indices[0], indices[0]));
    }
    catch (Exception ex) {
    }
  }


  /**
   * Returns the fixed cell height value -- the value specified by setting
   * the <code>fixedCellHeight</code> property,
   * rather than that calculated from the list elements.
   *
   * @return the fixed cell height, in pixels
   * @see #setFixedCellHeight
   */
  public final int getFixedCellHeight() {
    return list.getFixedCellHeight();
  }


  /**
   * Sets the height of every cell in the list.  If <code>height</code>
   * is -1, cell
   * heights are computed by applying <code>getPreferredSize</code>
   * to the <code>cellRenderer</code> component for each list element.
   * <p>
   * The default value of this property is -1.
   * <p>
   * This is a JavaBeans bound property.
   *
   * @param height an integer giving the height, in pixels, for all cells
   *        in this list
   * @see #getPrototypeCellValue
   * @see #setFixedCellWidth
   * @see JComponent#addPropertyChangeListener
   * @beaninfo
   *       bound: true
   *   attribute: visualUpdate true
   * description: Defines a fixed cell height when greater than zero.
   */
  public final void setFixedCellHeight(int height) {
    list.setFixedCellHeight(height);
  }



  /**
   * Returns the fixed cell width value -- the value specified by setting
   * the <code>fixedCellWidth</code> property, rather than that calculated
   * from the list elements.
   *
   * @return the fixed cell width
   * @see #setFixedCellWidth
   */
  public final int getFixedCellWidth() {
    return list.getFixedCellWidth();
  }


  /**
   * Sets the width of every cell in the list.  If <code>width</code> is -1,
   * cell widths are computed by applying <code>getPreferredSize</code>
   * to the <code>cellRenderer</code> component for each list element.
   * <p>
   * The default value of this property is -1.
   * <p>
   * This is a JavaBeans bound property.
   *
   * @param width   the width, in pixels, for all cells in this list
   * @see #getPrototypeCellValue
   * @see #setFixedCellWidth
   * @see JComponent#addPropertyChangeListener
   * @beaninfo
   *       bound: true
   *   attribute: visualUpdate true
   * description: Defines a fixed cell width when greater than zero.
   */
  public final void setFixedCellWidth(int width) {
    list.setFixedCellWidth(width);
  }


  /**
   * Returns the preferred number of visible rows.
   *
   * @return an integer indicating the preferred number of rows to display
   *         without using a scroll bar
   * @see #setVisibleRowCount
   */
  public final int getVisibleRowCount() {
    return list.getVisibleRowCount();
  }


  /**
   * Sets the preferred number of rows in the list that can be displayed
   * without a scrollbar, as determined by the nearest
   * <code>JViewport</code> ancestor, if any.
   * The value of this property only affects the value of
   * the <code>JList</code>'s <code>preferredScrollableViewportSize</code>.
   * <p>
   * The default value of this property is 8.
   * <p>
   * This is a JavaBeans bound property.
   *
   * @param visibleRowCount  an integer specifying the preferred number of
   *                         visible rows
   * @see #getVisibleRowCount
   * @see JComponent#getVisibleRect
   * @see JViewport
   * @beaninfo
   *       bound: true
   *   attribute: visualUpdate true
   * description: The preferred number of cells that can be displayed without a scroll bar.
   */
  public final void setVisibleRowCount(int visibleRowCount) {
    list.setVisibleRowCount(visibleRowCount);
  }


  /**
   * Returns <code>JList.VERTICAL</code> if the layout is a single
   * column of cells, or <code>JList.VERTICAL_WRAP</code> if the layout
   * is "newspaper style" with the content flowing vertically then
   * horizontally or <code>JList.HORIZONTAL_WRAP</code> if the layout is
   * "newspaper style" with the content flowing horizontally then
   * vertically.
   *
   * @return the value of the layoutOrientation property
   * @see #setLayoutOrientation
   * @since 1.4
   */
  public final int getLayoutOrientation() {
      return list.getLayoutOrientation();
  }


  /**
   * Defines the way list cells are layed out. Consider a <code>JList</code>
   * with four cells, this can be layed out in one of the following ways:
   * <pre>
   *   0
   *   1
   *   2
   *   3
   * </pre>
   * <pre>
   *   0  1
   *   2  3
   * </pre>
   * <pre>
   *   0  2
   *   1  3
   * </pre>
   * <p>
   * These correspond to the following values:
   *
   * <table border="1"
   *  summary="Describes layouts VERTICAL, HORIZONTAL_WRAP, and VERTICAL_WRAP">
   *   <tr><th><p align="left">Value</p></th><th><p align="left">Description</p></th></tr>
   *   <tr><td><code>JList.VERTICAL</code>
   *       <td>The cells should be layed out vertically in one column.
   *   <tr><td><code>JList.HORIZONTAL_WRAP</code>
   *       <td>The cells should be layed out horizontally, wrapping to
   *           a new row as necessary.  The number
   *           of rows to use will either be defined by
   *           <code>getVisibleRowCount</code> if > 0, otherwise the
   *           number of rows will be determined by the width of the
   *           <code>JList</code>.
   *   <tr><td><code>JList.VERTICAL_WRAP</code>
   *       <td>The cells should be layed out vertically, wrapping to a
   *           new column as necessary.  The number
   *           of rows to use will either be defined by
   *           <code>getVisibleRowCount</code> if > 0, otherwise the
   *           number of rows will be determined by the height of the
   *           <code>JList</code>.
   *  </table>
   * The default value of this property is <code>JList.VERTICAL</code>.
   * <p>
   * This will throw an <code>IllegalArgumentException</code> if
   * <code>layoutOrientation</code> is not one of
   * <code>JList.HORIZONTAL_WRAP</code> or <code>JList.VERTICAL</code> or
   * <code>JList.VERTICAL_WRAP</code>
   *
   * @param layoutOrientation New orientation, one of
   *        <code>JList.HORIZONTAL_WRAP</code>,  <code>JList.VERTICAL</code>
   *        or <code>JList.VERTICAL_WRAP</code>.
   * @see #getLayoutOrientation
   * @see #setVisibleRowCount
   * @see #getScrollableTracksViewportHeight
   * @since 1.4
   * @beaninfo
   *       bound: true
   *   attribute: visualUpdate true
   * description: Defines the way list cells are layed out.
   *        enum: VERTICAL JList.VERTICAL
   *              HORIZONTAL_WRAP JList.HORIZONTAL_WRAP
   *              VERTICAL_WRAP JList.VERTICAL_WRAP
   */
  public final void setLayoutOrientation(int layoutOrientation) {
    list.setLayoutOrientation(layoutOrientation);
  }


  /**
   * Determines whether single-item or multiple-item
   * selections are allowed.
   * The following <code>selectionMode</code> values are allowed:
   * <ul>
   * <li> <code>ListSelectionModel.SINGLE_SELECTION</code>
   *   Only one list index can be selected at a time.  In this
   *   mode the <code>setSelectionInterval</code> and
   *   <code>addSelectionInterval</code>
   *   methods are equivalent, and only the second index
   *   argument is used.
   * <li> <code>ListSelectionModel.SINGLE_INTERVAL_SELECTION</code>
   *   One contiguous index interval can be selected at a time.
   *   In this mode <code>setSelectionInterval</code> and
   *   <code>addSelectionInterval</code>
   *   are equivalent.
   * <li> <code>ListSelectionModel.MULTIPLE_INTERVAL_SELECTION</code>
   *   In this mode, there's no restriction on what can be selected.
   *   This is the default.
   * </ul>
   *
   * @param selectionMode an integer specifying the type of selections
   *                         that are permissible
   * @see #getSelectionMode
   * @beaninfo
   * description: The selection mode.
   *        enum: SINGLE_SELECTION            ListSelectionModel.SINGLE_SELECTION
   *              SINGLE_INTERVAL_SELECTION   ListSelectionModel.SINGLE_INTERVAL_SELECTION
   *              MULTIPLE_INTERVAL_SELECTION ListSelectionModel.MULTIPLE_INTERVAL_SELECTION
   */
  public final void setSelectionMode(int selectionMode) {
    list.setSelectionMode(selectionMode);
  }


  /**
   * Returns whether single-item or multiple-item selections are allowed.
   *
   * @return the value of the <code>selectionMode</code> property
   * @see #setSelectionMode
   */
  public final int getSelectionMode() {
    return list.getSelectionMode();
  }


  /**
   * Sets the data model's <code>isAdjusting</code> property to true,
   * so that a single event will be generated when all of the selection
   * events have finished (for example, when the mouse is being
   * dragged over the list in selection mode).
   *
   * @param b the boolean value for the property value
   * @see ListSelectionModel#setValueIsAdjusting
   */
  public final void setValueIsAdjusting(boolean b) {
    list.setValueIsAdjusting(b);
  }


  /**
   * Returns the value of the data model's <code>isAdjusting</code> property.
   * This value is true if multiple changes are being made.
   *
   * @return true if multiple selection-changes are occurring, as
   *         when the mouse is being dragged over the list
   * @see ListSelectionModel#getValueIsAdjusting
   */
  public final boolean getValueIsAdjusting() {
    return list.getValueIsAdjusting();
  }


  /**
   * Returns the selection foreground color.
   *
   * @return the <code>Color</code> object for the foreground property
   * @see #setSelectionForeground
   * @see #setSelectionBackground
   */
  public Color getSelectionForeground() {
      return list.getSelectionForeground();
  }


  /**
   * Sets the foreground color for selected cells.  Cell renderers
   * can use this color to render text and graphics for selected
   * cells.
   * <p>
   * The default value of this property is defined by the look
   * and feel implementation.
   * <p>
   * This is a JavaBeans bound property.
   *
   * @param selectionForeground  the <code>Color</code> to use in the foreground
   *                             for selected list items
   * @see #getSelectionForeground
   * @see #setSelectionBackground
   * @see #setForeground
   * @see #setBackground
   * @see #setFont
   * @beaninfo
   *       bound: true
   *   attribute: visualUpdate true
   * description: The foreground color of selected cells.
   */
  public final void setSelectionForeground(Color selectionForeground) {
    list.setSelectionForeground(selectionForeground);
  }


  /**
   * Returns the background color for selected cells.
   *
   * @return the <code>Color</code> used for the background of
   * selected list items
   * @see #setSelectionBackground
   * @see #setSelectionForeground
   */
  public final Color getSelectionBackground() {
    return list.getSelectionBackground();
  }


  /**
   * Sets the background color for selected cells.  Cell renderers
   * can use this color to the fill selected cells.
   * <p>
   * The default value of this property is defined by the look
   * and feel implementation.
   * <p>
   * This is a JavaBeans bound property.
   *
   * @param selectionBackground  the <code>Color</code> to use for the
   *                             background of selected cells
   * @see #getSelectionBackground
   * @see #setSelectionForeground
   * @see #setForeground
   * @see #setBackground
   * @see #setFont
   * @beaninfo
   *       bound: true
   *   attribute: visualUpdate true
   * description: The background color of selected cells.
   */
  public void setSelectionBackground(Color selectionBackground) {
    list.setSelectionBackground(selectionBackground);
  }


  /**
   * Adds a listener to the list that's notified each time a change
   * to the selection occurs.  Listeners added directly to the
   * <code>JList</code>
   * will have their <code>ListSelectionEvent.getSource() ==
   * this JList</code>
   * (instead of the <code>ListSelectionModel</code>).
   *
   * @param listener the <code>ListSelectionListener</code> to add
   * @see #getSelectionModel
   * @see #getListSelectionListeners
   */
  public final void addListSelectionListener(ListSelectionListener listener) {
    list.addListSelectionListener(listener);
  }


  /**
   * Removes a listener from the list that's notified each time a
   * change to the selection occurs.
   *
   * @param listener the <code>ListSelectionListener</code> to remove
   * @see #addListSelectionListener
   * @see #getSelectionModel
   */
  public final void removeListSelectionListener(ListSelectionListener listener) {
    list.removeListSelectionListener(listener);
  }


  public void setEnabled(boolean enabled) {
    list.setEnabled(enabled);
    list.setFocusable(enabled || ClientSettings.DISABLED_INPUT_CONTROLS_FOCUSABLE);
    if (enabled) {
      list.setBackground( (Color) UIManager.get("TextField.background"));
      list.setSelectionBackground(ClientSettings.BACKGROUND_SEL_COLOR);
    }
    else {
      list.setBackground( (Color) UIManager.get("TextField.inactiveBackground"));
      try {
        list.setSelectionBackground(new Color(
            list.getBackground().getRed() - 10,
            list.getBackground().getGreen() - 10,
            list.getBackground().getBlue() - 10
            ));
      }
      catch (Exception ex) {
        list.setSelectionBackground(Color.lightGray);
      }
    }
  }


  /**
   * @return current input control abilitation
   */
  public final boolean isEnabled() {
    try {
      return list.isEnabled();
    }
    catch (Exception ex) {
      return false;
    }
  }


  /**
   * @return the selected index in the input control
   */
  public final int getSelectedIndex() {
    return list.getSelectedIndex();
  }


  /**
   * @return total rows count in the input control
   */
  public final int getRowCount() {
    return list.getModel().getSize();
  }


  /**
   * @return the element at the specified index, converted in String format
   */
  public final String getValueAt(int index) {
    if (model.get(index)==null)
      return "";

    ValueObject vo = (ValueObject)model.get(index);
    Object val = null;
    String valS = "";
    SimpleDateFormat sdf = new SimpleDateFormat(ClientSettings.getInstance().getResources().getDateMask(Consts.TYPE_DATE));
    for(int i=0;i<colProperties.length;i++) {
      if (colProperties[i].isColumnVisible()) {
        try {
          val = ( (Method) getters.get(colProperties[i].getColumnName())).invoke(vo, new Object[0]);
          if (val != null) {
            if (colProperties[i] instanceof DateColumn) {
              valS += sdf.format( (java.util.Date) val)+" ";
            }
            else
              valS += val.toString()+" ";
          }
          else
            valS += " ";
        }
        catch (Exception ex) {}
      }
    }
    return valS;

  }


  /**
   * @return combo control
   */
  public final JComponent getComponent() {
    return list;
  }


  /**
   * @return <code>true</code> if the input control is in read only mode (so search is enabled), <code>false</code> otherwise
   */
  public final boolean isReadOnly() {
    return isEnabled();
  }



  /**
   * @return attribute name in the list v.o. that identify the attribute name in the v.o. of the list container
   */
  public final String getForeignKeyAttributeName() {
    return foreignKeyAttributeName;
  }


  /**
   * Set the attribute name in the list v.o. that identify the attribute name in the v.o. of the list container.
   * As default value this attribute is null.
   * Null means that "attributeName" property will be used to identify the v.o. in the list, i.e. the attribute names in the list v.o. and in the container v.o. must have the same name.
   * @param foreignKeyAttributeName String
   */
  public final void setForeignKeyAttributeName(String foreignKeyAttributeName) {
    this.foreignKeyAttributeName = foreignKeyAttributeName;
  }


  /**
   * @return attribute name in thelist v.o. that identify the list item
   */
  private String getFKAttributeName() {
    return
        foreignKeyAttributeName==null || foreignKeyAttributeName.equals("") ?
        getAttributeName() :
        foreignKeyAttributeName;
  }


  /**
   * @return <code>true</code> to disable key listening on input control (for instance, in case of nested grids), <code>false</code> to listen for key events
   */
  public final boolean disableListener() {
    return false;
  }


  /**
   * Method invoked by SearchWindowManager when the specified "textToSeach" pattern has not matchings in the current content
   * of binded control.
   * This callback can be used to retrieve additional data into control and to search inside new data.
   * @param textToSeach patten of text to search
   * @return -1 if no additional data is available, otherwise the row index of data just added that satify the pattern
   */
  public final int search(String textToSeach) {
    return -1;
  }


  /**
   * Set the component orientation: from left to right or from right to left.
   * @param o component orientation
   */
  public final void setTextOrientation(ComponentOrientation o) {
    list.setComponentOrientation(o);
  }


  /**
   * @return component orientation
   */
  public final ComponentOrientation getTextOrientation() {
    try {
      return list.getComponentOrientation();
    }
    catch (Exception ex) {
      return null;
    }
  }

}
