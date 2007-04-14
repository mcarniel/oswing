package org.openswing.swing.form.model.client;

import org.openswing.swing.client.*;
import java.lang.reflect.Method;
import java.beans.*;
import org.openswing.swing.message.receive.java.ValueObject;
import org.openswing.swing.logger.client.Logger;
import java.beans.PropertyDescriptor;
import java.util.ArrayList;


/**
 * <p>Title: OpenSwing Framework</p>
 * <p>Description: Data model linked to a Form panel.</p>
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
public class VOModel {

  /** value object class associated to this data model */
  private Class valueObjectClass = null;

  /** valueObject info */
  private BeanInfo info;

  /** value object stored inside the model */
  private ValueObject valueObject = null;

  /** value changed listeners */
  private ArrayList valueChangeListeners = new ArrayList();

  /** list of attribute names */
  private ArrayList attributeNames = new ArrayList();


  /**
   * Constructor.
   * @param valueObjectClass value object class associated to this data model
   * @throws java.lang.Exception if an error occours
   */
  public VOModel(Class valueObjectClass) throws Exception {
    this.valueObjectClass = valueObjectClass;
    info = Introspector.getBeanInfo(valueObjectClass);

    // retrieve attribute properties...
    PropertyDescriptor[] props = info.getPropertyDescriptors();
    for (int i = 0; i < props.length; i++) {
      attributeNames.add(props[i].getName());
//      addField(new MetaData(props[i].getName(),
//                              props[i].getPropertyType(),
//                              props[i].getDisplayName()));
    }

    try {
      setValueObject( (ValueObject)valueObjectClass.newInstance() );
    }
    catch (Throwable ex) {
      Logger.error(this.getClass().getName(),"VOModel","Error on storing the value object",ex);
    }
  }


  /**
   * @return value object class associated to this data model
   */
  public final Class getValueObjectType() {
    return valueObjectClass;
  }


  /**
   * Set the value object that this model will use.
   * @param valueObject value object to store
   */
  public final void setValueObject(ValueObject valueObject) {
      if (valueObject != null && !valueObject.getClass().equals(valueObjectClass)) {
        throw new RuntimeException("The specified value object has not type '"+valueObjectClass.toString()+"'");
      }
      Object oldValueObject = this.valueObject;
      this.valueObject = valueObject;
      if (valueObject==null && oldValueObject!=null ||
          valueObject!=null && oldValueObject==null ||
        valueObject!=null && !valueObject.equals(oldValueObject)) {
        // fire value changed events...
        PropertyDescriptor prop = null;
        Method readMethod = null;
        Object oldValue = null;
        Object newValue = null;
        for (int i = 0; i < attributeNames.size(); i++) {
          prop = getPropertyDescriptor(attributeNames.get(i).toString());
          readMethod = prop.getReadMethod();
          if (readMethod != null) {
            try {
              oldValue = oldValueObject!=null?readMethod.invoke(oldValueObject, new Object[0]):null;
              newValue = valueObject!=null?readMethod.invoke(valueObject, new Object[0]):null;
            }
            catch (Exception ex) {
              Logger.error(this.getClass().getName(),"setValueObject","Error while setting the value object attribute '"+attributeNames.get(i)+"'",ex);
              continue;
            }
          }
          if (newValue==null && oldValue!=null ||
              newValue!=null && oldValue==null ||
              newValue!=null && oldValue==null && !newValue.equals(oldValue))
            fireValueChanged(attributeNames.get(i).toString(),oldValue,newValue);
        }
      }
  }


  /**
   * @return value object stored inside the model
   */
  public final ValueObject getValueObject() {
    return valueObject;
  }


  /**
   * @param attributeName attribute name of the value object
   * @return value value associated to the specified attribute name
   */
  public final Object getValue(String attributeName) {
      if (getValueObject() == null) {
        return null;
      }
      try {
        PropertyDescriptor prop = getPropertyDescriptor(attributeName);
        Method readMethod = prop.getReadMethod();
        if (readMethod != null) {
              return readMethod.invoke(getValueObject(), new Object[0]);
            }
        }
      catch (Exception ex) {
        Logger.error(this.getClass().getName(),"getValue","Error while reading the value object attribute '"+attributeName+"'",ex);
      }
      return null;
  }


  /**
   * Set a value in the value object for an attribute name.
   * @param attributeName attribute name of the value object
   * @return value value to set for the specified attribute name
   */
  public final void setValue(String attributeName,Object value) {
      if (getValueObject() == null) {
          return;
      }
      try {
        PropertyDescriptor prop = getPropertyDescriptor(attributeName);
        Method writeMethod = prop.getWriteMethod();
        if (writeMethod != null) {
          Object oldValue = getValue(attributeName);
          if (value!=null) {
            if (writeMethod.getParameterTypes()[0].equals(java.sql.Date.class) &&
              value.getClass().equals(java.util.Date.class))
              value = new java.sql.Date(((java.util.Date)value).getTime());
            else if (writeMethod.getParameterTypes()[0].equals(java.sql.Timestamp.class) &&
              value.getClass().equals(java.util.Date.class))
              value = new java.sql.Timestamp(((java.util.Date)value).getTime());
            else if (writeMethod.getParameterTypes()[0].equals(java.sql.Timestamp.class) &&
              value.getClass().equals(java.sql.Date.class))
              value = new java.sql.Timestamp(((java.sql.Date)value).getTime());
          }
          writeMethod.invoke(getValueObject(), new Object[] {value});
          if (value==null && oldValue!=null ||
              value!=null && oldValue==null ||
              value!=null && oldValue==null && !value.equals(oldValue))
            fireValueChanged(attributeName,oldValue,value);
        }
      }
      catch (Exception ex) {
        Logger.error(this.getClass().getName(),"setValue","Error while writing the value object attribute '"+attributeName+"'",ex);
      }
  }


  /**
   * Method called inside getValue/setValue methods.
   * @param attributeName attribute name of the value object
   * @return property descriptor
   */
  private PropertyDescriptor getPropertyDescriptor(String attributeName) {
    PropertyDescriptor pd = null;
    PropertyDescriptor[] desc = info.getPropertyDescriptors();
    for (int i = 0; i < desc.length; i++) {
      if (attributeName.equals(desc[i].getName())) {
          pd = desc[i];
          break;
      }
    }
    return pd;
  }


  /**
   * Method used to add a value changed listener.
   * @param listener value changed listener to add
   */
  public final void addValueChangeListener(ValueChangeListener listener) {
    valueChangeListeners.add(listener);
  }


  /**
   * Method used to remove a value changed listener.
   * @param listener value changed listener to remove
   */
  public final void removeValueChangeListener(ValueChangeListener listener) {
    valueChangeListeners.remove(listener);
  }


  /**
   * @return list of value changed listeners added to this model
   */
  public final ValueChangeListener[] getValueChangeListeners() {
    return (ValueChangeListener[])valueChangeListeners.toArray(new ValueChangeListener[valueChangeListeners.size()]);
  }


  /**
   * Method called by setValue and setValueObject methods to fire value changed events.
   * @param attributeName attribute name
   * @param oldValue old value of the attribute
   * @param newValue new value of the attribute
   */
  private void fireValueChanged(String attributeName,Object oldValue,Object newValue) {
      ValueChangeListener[] formListeners = getValueChangeListeners();
      ValueChangeEvent e = new ValueChangeEvent(this,attributeName,oldValue,newValue);
      if (valueChangeListeners != null) {
          for (int i = 0; i < valueChangeListeners.size(); i++) {
              ValueChangeListener vcl = (ValueChangeListener)
                  valueChangeListeners.get(i);
              vcl.valueChanged(e);
          }
      }
  }



}
