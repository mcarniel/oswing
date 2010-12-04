package org.openswing.swing.client;

import java.lang.reflect.*;
import java.math.*;
import java.sql.*;
import java.util.*;
import java.util.Date;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

import org.openswing.swing.form.client.*;
import org.openswing.swing.items.client.*;
import org.openswing.swing.logger.client.*;
import org.openswing.swing.message.receive.java.*;
import org.openswing.swing.table.columns.client.*;
import org.openswing.swing.util.client.*;
import org.openswing.swing.util.java.*;
import java.text.SimpleDateFormat;


/**
 * <p>Title: OpenSwing Framework</p>
 * <p>Description: Combo box input control: its items are retrieved through the combo box controller, that returns a list of value object;
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
public class ComboBoxVOControl extends BaseInputControl implements InputControl,ItemsParent,SearchControl {

  static {
    UIManager.put("ComboBox.disabledForeground", UIManager.get("ComboBox.foreground"));
    UIManager.put("ComboBox.disabledBackground", UIManager.get("TextField.inactiveBackground"));
    UIManager.put("ComboBox.selectionForeground", Color.black );
    UIManager.put("ComboBox.selectionBackground", ClientSettings.BACKGROUND_SEL_COLOR );
  }

  /** combo box */
  private JComboBox combo = new JComboBox();

  /** mapping between items v.o. attributes and items container v.o. attributes */
  private ItemsMapper itemsMapper = new ItemsMapper();

  /** items data source */
  private ItemsDataLocator itemsDataLocator = null;

  /** items value object */
  private ValueObject itemsVO = null;

  /** columns associated to lookup grid */
  private Column[] colProperties = new Column[0];

  /** combo box model */
  private DefaultComboBoxModel model = new DefaultComboBoxModel();

  /** flag used to set visibility on all columns of lookup grid; default "false"  */
  private boolean allColumnVisible = false;

  /** default preferredWidth for all columns of lookup grid; default 100 pixels */
  private int allColumnPreferredWidth = 100;

  /** background color when the input control is enabled */
  private Color enabledBackColor = combo.getBackground();

  /** collection of pairs <v.o. attribute name,Method object, related to the attribute getter method> */
  private Hashtable getters = new Hashtable();

  /** define if in insert mode combo box has no item selected; default value: <code>false</code> i.e. the first item is pre-selected */
  private boolean nullAsDefaultValue = false;

  /** flag used in addNotify method to retrieve items */
  private boolean itemsLoaded = false;

  /** Form container (optional) */
  private Form form = null;

  /** attribute name in the combo-box v.o. that identify the attribute name in the v.o. of the combo-box container; as default value this attribute is null; null means that "attributeName" property will be used to identify the v.o. in the combo-box, i.e. the attribute names in the combo-box v.o. and in the container v.o. must have the same name */
  private String foreignKeyAttributeName;


  /**
   * Contructor.
   */
  public ComboBoxVOControl() {
    this.setLayout(new GridBagLayout());
    this.add(combo, new GridBagConstraints(0, 0, 1, 1, 1.0, 1.0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));

//    setLayout(new FlowLayout(FlowLayout.LEFT, 0, 0));
//    add(combo);
    setOpaque(false);
    combo.addKeyListener(new KeyAdapter() {
      public void keyPressed(KeyEvent e) {
        if (e.getKeyCode()==e.VK_CANCEL || e.getKeyCode()==e.VK_BACK_SPACE || e.getKeyCode()==e.VK_DELETE)
          combo.setSelectedIndex(-1);
      }
    });

    initListeners();

    // intercepts key events listened by combo box...1
    combo.setKeySelectionManager(new JComboBox.KeySelectionManager() {
        public int selectionForKey(char aKey, ComboBoxModel aModel) {
            return -1;
        }
    });

    if (ClientSettings.TEXT_ORIENTATION!=null)
        setComponentOrientation(ClientSettings.TEXT_ORIENTATION);

    new SearchWindowManager(this);
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
          combo.setModel(model);
          combo.revalidate();
          combo.repaint();
          combo.setSelectedIndex(-1);
        }
      }


      ItemRenderer rend = new ItemRenderer();
      rend.init(getters,colProperties,1,0,0,0);
      combo.setRenderer(rend);

      combo.setMinimumSize(new Dimension((int)this.getPreferredSize().getWidth(),(int)new JTextField().getPreferredSize().getHeight()));
      combo.setPreferredSize(new Dimension((int)this.getPreferredSize().getWidth(),(int)new JTextField().getPreferredSize().getHeight()));
      combo.setSize(new Dimension((int)this.getPreferredSize().getWidth(),(int)new JTextField().getPreferredSize().getHeight()));

      combo.addItemListener(new ItemListener() {
        public void itemStateChanged(ItemEvent e) {
          // update v.o. container...
          if (e.getStateChange()==e.SELECTED && (form==null || form.getMode()!=Consts.READONLY)) {
            itemsVO = (ValueObject)e.getItem();
            updateParentModel(ComboBoxVOControl.this);
          }
        }
      });

      // set Form object in the lookup container...
      form = ClientUtils.getLinkedForm(this);

    }


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
        combo.setModel(model);
        combo.revalidate();
        combo.repaint();
        combo.setSelectedIndex(-1);
      }
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
           Object obj;
           if (itemsAttributeName.indexOf(".")>0) {
               itemsMethodName = "get" + String.valueOf(Character.toUpperCase(itemsAttributeName.charAt(0))) + itemsAttributeName.substring(1,itemsAttributeName.indexOf("."));
               itemsMethod = itemsVO.getClass().getMethod(itemsMethodName, new Class[0]);
               obj = itemsMethod.invoke(itemsVO, new Object[0]);
               itemsMethod = (Method)getters.get(itemsAttributeName);
           } else {
               itemsMethodName = "get" + String.valueOf(Character.toUpperCase(itemsAttributeName.charAt(0))) + itemsAttributeName.substring(1);
               itemsMethod = itemsVO.getClass().getMethod(itemsMethodName, new Class[0]);
               obj = itemsVO;
           }
           if (!itemsMapper.setParentAttribute(
                 itemsParent,
                 itemsAttributeName,
                 itemsMethod.getReturnType(),
                 itemsMethod.invoke(obj, new Object[0])
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
   * Recursive method invoked by initItemsVO() method to analyze value object and retrieve all getter methods.
   * @param vosAlreadyProcessed collection of inner v.o. already processed
   * @param getterMethods list of getter methods already retrieved
   * @param clazz class to analize in order to fetch its getter methods
   */
  private void analyzeVO(String prefix,ArrayList attributes,ArrayList getterMethods,Class clazz) throws Throwable{
    if (prefix.split("\\.").length>ClientSettings.MAX_NR_OF_LOOPS_IN_ANALYZE_VO)
      return;
    String attributeName = null;

    Method[] methods = clazz.getMethods();
    for(int i=0;i<methods.length;i++) {
      if ((methods[i].getName().startsWith("get") ||
           methods[i].getName().startsWith("is"))&&
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
      {
        if (methods[i].getName().startsWith("get"))
          attributeName = methods[i].getName().substring(3);
        else
          attributeName = methods[i].getName().substring(2);
        if (attributeName.length()>1)
          attributeName = attributeName.substring(0,1).toLowerCase()+attributeName.substring(1);
        attributes.add(prefix+attributeName);
        getterMethods.add(methods[i]);
      }
      else if (methods[i].getName().startsWith("get") &&
               methods[i].getParameterTypes().length==0 &&
               ValueObject.class.isAssignableFrom(methods[i].getReturnType())) {
        attributeName = methods[i].getName().substring(3);
        if (attributeName.length()>1)
          attributeName = attributeName.substring(0,1).toLowerCase()+attributeName.substring(1);
        analyzeVO(prefix+attributeName+".",attributes,getterMethods,methods[i].getReturnType());
      }
    }
  }


  /**
   * Method called by setComboValueObjectClassName:
   * - it creates an empty combo v.o
   * - it initializes combo column properties.
   * @param itemsValueObjectClassName combo value object class name
   */
  private void initItemsVO(String itemsValueObjectClassName) {
    try {
      ArrayList attributeNames = new ArrayList();
      ArrayList getterMethods = new ArrayList();
      analyzeVO("",attributeNames,getterMethods,Class.forName(itemsValueObjectClassName));

      this.itemsVO = (ValueObject) Class.forName(itemsValueObjectClassName).getConstructor(new Class[0]).newInstance(new Object[0]);
      this.colProperties = new Column[getterMethods.size()];

      Class colType = null;
      for(int i=0;i<getterMethods.size();i++) {
          colType = ((Method)getterMethods.get(i)).getReturnType();
          if (colType.equals(String.class))
            colProperties[i] = new TextColumn();
          else if (colType.equals(Integer.class) || colType.equals(Long.class) || colType.equals(Short.class) ||
                   colType.equals(Integer.TYPE)  || colType.equals(Long.TYPE)  || colType.equals(Short.TYPE))
            colProperties[i] = new IntegerColumn();
          else if (colType.equals(BigDecimal.class) ||
                   colType.equals(Double.class) || colType.equals(Float.class) ||
                   colType.equals(Double.TYPE)  || colType.equals(Float.TYPE))
            colProperties[i] = new DecimalColumn();
          else if (colType.equals(Boolean.class))
            colProperties[i] = new CheckBoxColumn();
          else if (colType.equals(Date.class))
            colProperties[i] = new DateColumn();
          else if (colType.equals(java.sql.Date.class))
            colProperties[i] = new DateColumn();
          else if (colType.equals(Timestamp.class))
            colProperties[i] = new DateColumn();

          colProperties[i].setColumnName(((String)attributeNames.get(i)));
          if (colProperties[i].getHeaderColumnName().equals("columnname"))
            colProperties[i].setHeaderColumnName(String.valueOf(colProperties[i].getColumnName().charAt(0)).toUpperCase()+colProperties[i].getColumnName().substring(1));
          colProperties[i].setColumnVisible(this.allColumnVisible);
          colProperties[i].setPreferredWidth(this.allColumnPreferredWidth);
          getters.put(
            colProperties[i].getColumnName(),
            getterMethods.get(i)
          );
      }

    }
    catch (Throwable ex) {
      ex.printStackTrace();
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
   * @return attribute name in the combo-box v.o. that identify the combo-box item
   */
  private String getFKAttributeName() {
    return
        foreignKeyAttributeName==null || foreignKeyAttributeName.equals("") ?
        getAttributeName() :
        foreignKeyAttributeName;
  }


  /**
   * Select the combo item related to the specified code.
   * @param code used to retrieve the corresponding item and to select that item in the combo
   */
  public final void setValue(Object code) {
    if (code==null) {
      combo.setSelectedIndex( -1);
      return;
    }
    if (getFKAttributeName()==null)
      return;
    Object obj = null;
    try {
      String fkAttributeName = getFKAttributeName();
      for (int i = 0; i < model.getSize(); i++) {
        obj = model.getElementAt(i);
        Method fkMethod = (Method) getters.get(fkAttributeName);
        if (fkAttributeName.indexOf(".")>0) {
          String fkMethodName = "get" + String.valueOf(Character.toUpperCase(fkAttributeName.charAt(0))) + fkAttributeName.substring(1,fkAttributeName.indexOf("."));;
          fkMethod = obj.getClass().getMethod(fkMethodName, new Class[0]);
          obj = fkMethod.invoke(obj, new Object[0]);
          fkMethod = (Method)getters.get(fkAttributeName);
        }
        obj = fkMethod.invoke(
          obj,
          new Object[0]
        );
        if (code.equals(obj)) {
          combo.setSelectedIndex(i);
          return;
        }
      }
    }
    catch (Throwable ex) {
      ex.printStackTrace();
      combo.setSelectedIndex(-1);
    }
    combo.setSelectedIndex(-1);
  }


  /**
   * @return code related to the selected combo item; it return null if no item is selected
   */
  public final Object getValue() {
    try {
      Object obj = model.getElementAt(combo.getSelectedIndex());
      String fkAttributeName = getFKAttributeName();
      Method fkMethod = (Method) getters.get(fkAttributeName);
      if (fkAttributeName.indexOf(".")>0) {
          String fkMethodName = "get" + String.valueOf(Character.toUpperCase(fkAttributeName.charAt(0))) + fkAttributeName.substring(1,fkAttributeName.indexOf("."));;
          fkMethod = obj.getClass().getMethod(fkMethodName, new Class[0]);
          obj = fkMethod.invoke(obj, new Object[0]);
          fkMethod = (Method)getters.get(fkAttributeName);
      }
      if (obj==null)
        return null;
      return fkMethod.invoke(
        obj,
        new Object[0]
      );
    }
    catch (Throwable ex) {
      ex.printStackTrace();
      return null;
    }
  }


  /**
   * @return combo box
   */
  public final JComboBox getComboBox() {
    return combo;
  }


  public void setEnabled(boolean enabled) {
    combo.setEnabled(enabled);
    combo.setFocusable(enabled || ClientSettings.DISABLED_INPUT_CONTROLS_FOCUSABLE);
    if (!enabled)
      combo.setBackground((Color)UIManager.get("TextField.inactiveBackground"));
  }


  /**
   * @return current input control abilitation
   */
  public final boolean isEnabled() {
    try {
      return combo.isEnabled();
    }
    catch (Exception ex) {
      return false;
    }
  }


  /**
   * @return component inside this whose contains the value
   */
  public JComponent getBindingComponent() {
    return combo;
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
      combo.addFocusListener(l);
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
      combo.removeFocusListener(l);
    }
    catch (Exception ex) {
    }
  }


  /**
   * Adds the specified action listener to receive
   * action events from this textfield.
   *
   * @param l the action listener to be added
   */
  public final void addActionListener(ActionListener l) {
    try {
      combo.addActionListener(l);
    }
    catch (Exception ex) {
    }
  }


  /**
   * Removes the specified action listener so that it no longer
   * receives action events from this textfield.
   *
   * @param l the action listener to be removed
   */
  public final void removeActionListener(ActionListener l) {
    try {
      combo.removeActionListener(l);
    }
    catch (Exception ex) {
    }
  }


  /**
   * Adds an <code>ItemListener</code>.
   * <p>
   * <code>aListener</code> will receive one or two <code>ItemEvent</code>s when
   * the selected item changes.
   *
   * @param aListener the <code>ItemListener</code> that is to be notified
   * @see #setSelectedItem
   */
  public final void addItemListener(ItemListener alistener) {
    try {
      combo.addItemListener(alistener);
    }
    catch (Exception ex) {
    }
  }


  /** Removes an <code>ItemListener</code>.
   *
   * @param aListener  the <code>ItemListener</code> to remove
   */
  public final void removeItemListener(ItemListener alistener) {
    try {
      combo.removeItemListener(alistener);
    }
    catch (Exception ex) {
    }
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
   * @return the selected index in the input control
   */
  public final int getSelectedIndex() {
    return combo.getSelectedIndex();
  }


  /**
   * Set the selected index.
   */
  public final void setSelectedIndex(int index) {
    combo.setSelectedIndex(index);
  }


  /**
   * @return total rows count in the input control
   */
  public final int getRowCount() {
    return combo.getItemCount();
  }


  /**
   * @return the element at the specified index, converted in String format
   */
  public final String getValueAt(int index) {
    if (combo.getItemAt(index)==null)
      return "";

    ValueObject vo = (ValueObject)combo.getItemAt(index);
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
    return combo;
  }


  /**
   * @return <code>true</code> if the input control is in read only mode (so search is enabled), <code>false</code> otherwise
   */
  public final boolean isReadOnly() {
    return isEnabled();
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
    combo.setComponentOrientation(o);
  }


  /**
   * @return component orientation
   */
  public final ComponentOrientation getTextOrientation() {
    try {
      return combo.getComponentOrientation();
    }
    catch (Exception ex) {
      return null;
    }
  }


}
