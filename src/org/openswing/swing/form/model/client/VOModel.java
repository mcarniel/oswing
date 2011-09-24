package org.openswing.swing.form.model.client;

import java.beans.*;
import java.lang.reflect.*;
import java.util.*;

import org.openswing.swing.form.client.*;
import org.openswing.swing.logger.client.*;
import org.openswing.swing.message.receive.java.*;
import org.openswing.swing.util.client.ClientSettings;
import java.math.BigDecimal;


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

  /** collection of <attribute name,Method[] getters+setter> */
  private Hashtable voSetterMethods = new Hashtable();

  /** collection of <attribute name,Method[] getters> */
  private Hashtable voGetterMethods = new Hashtable();

  /** flag used to define if an inner v.o. must be automatically instantiated when a setter method is invoked */
  private boolean createInnerVO = true;

  /** linked Form */
  private Form form = null;

  /** last value object */
  private ValueObject oldValueObject = null;


  /**
   * Constructor.
   * @param valueObjectClass value object class associated to this data model
   * @param createInnerVO flag used to define if an inner v.o. must be automatically instantiated when a setter method is invoked
   * @throws java.lang.Exception if an error occours
   */
  public VOModel(Class valueObjectClass,boolean createInnerVO,Form form) throws Exception {
    this.valueObjectClass = valueObjectClass;
    this.createInnerVO = createInnerVO;
    this.form = form;

    // retrieve attribute properties...
    if (Beans.isDesignTime())
      return;
    analyzeClassFields("",new Method[0],valueObjectClass);
    setValueObject( (ValueObject) valueObjectClass.newInstance());
  }


  /**
   * Set the value object class that this model will use.
   * @param valueObjectClass value object class
   * @throws java.lang.Exception if an error occours
   */
  public final void setValueObject(Class valueObjectClass) throws Exception {
    this.valueObjectClass = valueObjectClass;
    this.valueObject = null;
    voSetterMethods.clear();
    voGetterMethods.clear();
    analyzeClassFields("",new Method[0],valueObjectClass);
    setValueObject( (ValueObject) valueObjectClass.newInstance());
  }


  /**
   * Define if an inner v.o. must be automatically instantiated when a setter method is invoked.
   * @param createInnerVO define if an inner v.o. must be automatically instantiated when a setter method is invoked
   */
  public final void setCreateInnerVO(boolean createInnerVO) {
    this.createInnerVO = createInnerVO;
  }


  /**
   * Analyze class fields and fill in "voSetterMethods","voGetterMethods","indexes",reverseIndexes" attributes.
   * @param prefix e.g. "attrx.attry."
   * @param parentMethods getter methods of parent v.o.
   * @param classType class to analyze
   */
  private void analyzeClassFields(String prefix,Method[] parentMethods,Class classType) {
    try {
      if (prefix.split("\\.").length>ClientSettings.MAX_NR_OF_LOOPS_IN_ANALYZE_VO)
        return;

      info = Introspector.getBeanInfo(classType);
      // retrieve attribute properties...
      PropertyDescriptor[] props = info.getPropertyDescriptors();
      for (int i = 0; i < props.length; i++) {

        if (props[i].getReadMethod()!=null &&
            props[i].getReadMethod().getParameterTypes().length==0) {
          // check if attribute must be ignored because it's not compatible...
          if (!(ValueObject.class.isAssignableFrom(props[i].getReadMethod().getReturnType()) ||
                Boolean.class.isAssignableFrom(props[i].getReadMethod().getReturnType()) ||
                Number.class.isAssignableFrom(props[i].getReadMethod().getReturnType()) ||
                Character.class.isAssignableFrom(props[i].getReadMethod().getReturnType()) ||
                byte[].class.isAssignableFrom(props[i].getReadMethod().getReturnType()) ||
                String.class.isAssignableFrom(props[i].getReadMethod().getReturnType()) ||
                java.util.Date.class.isAssignableFrom(props[i].getReadMethod().getReturnType()) ||
                Boolean.TYPE.isAssignableFrom(props[i].getReadMethod().getReturnType()) ||
                Short.TYPE.isAssignableFrom(props[i].getReadMethod().getReturnType()) ||
                Integer.TYPE.isAssignableFrom(props[i].getReadMethod().getReturnType()) ||
                Long.TYPE.isAssignableFrom(props[i].getReadMethod().getReturnType()) ||
                Float.TYPE.isAssignableFrom(props[i].getReadMethod().getReturnType()) ||
                Double.TYPE.isAssignableFrom(props[i].getReadMethod().getReturnType()) ||
                Character.TYPE.isAssignableFrom(props[i].getReadMethod().getReturnType()) ||
                ArrayList.class.isAssignableFrom(props[i].getReadMethod().getReturnType()) ||
                java.util.List.class.isAssignableFrom(props[i].getReadMethod().getReturnType())
          ))
            continue;
        }


        if (props[i].getName().substring(0,1).equals(props[i].getName().substring(0,1).toUpperCase())) {
          // fix of PropertyDescriptor bug: an attribute name "xXxxx" becomes "XXxxx" and this is not correct!
          try {
            Class c = classType;
            boolean attributeFound = false;
            while(!c.equals(Object.class)) {
              try {
                c.getDeclaredField(props[i].getName());
                attributeFound = true;
                break;
              }
              catch (Throwable ex2) {
                c = c.getSuperclass();
              }
            }
            if (!attributeFound) {
              // now trying to find an attribute having the first character in lower case (e.g. "xXxxx")
              String name = props[i].getName().substring(0,1).toLowerCase()+props[i].getName().substring(1);
              c = classType;
              while(!c.equals(Object.class)) {
                try {
                  c.getDeclaredField(name);
                  attributeFound = true;
                  break;
                }
                catch (Throwable ex2) {
                  c = c.getSuperclass();
                }
              }
              if (attributeFound)
                props[i].setName(name);
            }
          }
          catch (Throwable ex1) {
          }
        }

        if (props[i].getReadMethod()!=null &&
            props[i].getReadMethod().getParameterTypes().length==0 &&
            ValueObject.class.isAssignableFrom( props[i].getReadMethod().getReturnType() ) &&
            !form.getLazyInitializedAttributes().contains(props[i].getName())
        ) {
          Method[] newparentMethods = new Method[parentMethods.length+1];
          System.arraycopy(parentMethods,0,newparentMethods,0,parentMethods.length);
          newparentMethods[parentMethods.length] = props[i].getReadMethod();
          analyzeClassFields(prefix+props[i].getName()+".",newparentMethods,props[i].getReadMethod().getReturnType());
        }
        Method[] newparentMethods = new Method[parentMethods.length+1];
        System.arraycopy(parentMethods,0,newparentMethods,0,parentMethods.length);
        newparentMethods[parentMethods.length] = props[i].getReadMethod();
        voGetterMethods.put(prefix+props[i].getName(),newparentMethods);

        Method[] newparentMethods2 = new Method[parentMethods.length+1];
        System.arraycopy(parentMethods,0,newparentMethods2,0,parentMethods.length);
        newparentMethods2[parentMethods.length] = props[i].getWriteMethod();
        voSetterMethods.put(prefix+props[i].getName(),newparentMethods2);
      }

    }
    catch (Throwable ex) {
      Logger.error(this.getClass().getName(),"analyzeClassFields","Error on analyzing the object",ex);
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
      if (valueObject != null &&
          !valueObjectClass.isAssignableFrom(valueObject.getClass())) {
//          !valueObject.getClass().equals(valueObjectClass)) {
        throw new RuntimeException("The specified value object has not type '"+valueObjectClass.toString()+"'");
      }
      oldValueObject = this.valueObject;
      this.valueObject = valueObject;
      if (valueObject==null && oldValueObject!=null ||
          valueObject!=null && oldValueObject==null ||
          valueObject!=null && !valueObject.equals(oldValueObject)) {
        // fire value changed events...
        PropertyDescriptor prop = null;
        Method[] readMethods = null;
        Object oldValue = null;
        Object newValue = null;
        String attrName = null;
        Enumeration en = voGetterMethods.keys();
        Object obj = null;
        while(en.hasMoreElements()) {
          attrName = en.nextElement().toString();

          if(form.getLazyInitializedAttributes().contains(attrName))
            continue;

          readMethods = (Method[])voGetterMethods.get(attrName);
          if (readMethods != null) {
            try {
              obj = oldValueObject;
              if (obj!=null)
                for(int i=0;i<readMethods.length-1;i++) {
                  obj = readMethods[i].invoke(obj,new Object[0]);

                  // check if the inner v.o. is null...
                  if(obj == null) {
                    if (!createInnerVO)
                      break;
                    else
                      obj = (ValueObject)readMethods[i].getReturnType().newInstance();
                  }
                }

              oldValue = obj != null ? readMethods[readMethods.length - 1].invoke(obj, new Object[0]) : null;
              obj = valueObject;
              if (obj!=null)
                for(int i=0;i<readMethods.length-1;i++) {
                  obj = readMethods[i].invoke(obj,new Object[0]);

                  // check if the inner v.o. is null...
                  if(obj == null) {
                    if (!createInnerVO)
                      break;
                    else
                      obj = (ValueObject)readMethods[i].getReturnType().newInstance();
                  }
                }
              newValue = obj!=null?readMethods[readMethods.length-1].invoke(obj, new Object[0]):null;
            }
            catch (Exception ex) {
              String msg = "";
              try {
                msg = "Object: "+obj.getClass().getName()+"\n";
                for(int i=0;i<readMethods.length;i++)
                  msg +=
                      "Getter: "+
                      readMethods[i].getReturnType().getName()+" "+
                      readMethods[i].getName()+
                      "("+
                      (readMethods[i].getParameterTypes().length==1?readMethods[i].getParameterTypes()[0].getName():"")+
                      ")\n";
              }
              catch (Exception exx) {
              }
              Logger.error(this.getClass().getName(),"setValueObject","Error while setting the value object attribute '"+attrName+"'\n"+msg,ex);
              continue;
            }
          }
          if (newValue==null && oldValue!=null ||
              newValue!=null && oldValue==null ||
              newValue!=null && oldValue!=null && !newValue.equals(oldValue))
            fireValueChanged(attrName,oldValue,newValue);

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
   * @return type of the value associated to the specified attribute name
   */
  public final Class getAttributeType(String attributeName) {
    try {
      Method[] readMethods = (Method[])voGetterMethods.get(attributeName);
      if (readMethods != null) {
        return readMethods[readMethods.length-1].getReturnType();
      }
      else
        return null;
    }
    catch (Throwable ex) {
      Logger.error(this.getClass().getName(),"getAttributeType","Error while analyzing the value object attribute '"+attributeName+"'",ex);
    }
    return null;
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
        Method[] readMethods = (Method[])voGetterMethods.get(attributeName);
        Method[] writeMethods = (Method[])voSetterMethods.get(attributeName);
        String[] attrs = attributeName.split("\\.");

        if (readMethods != null) {
          Object obj = getValueObject();
          Object lastObj = null;
          if (obj!=null)
            for(int i=0;i<readMethods.length-1;i++) {
              lastObj = obj;
              obj = readMethods[i].invoke(obj,new Object[0]);

              // check if the inner v.o. is null...
              if(obj == null) {
                if (!createInnerVO)
                  return null;
                else {
                  obj = (ValueObject)readMethods[i].getReturnType().newInstance();
                  lastObj.getClass().getMethod(
                    "set"+attrs[i].substring(0,1).toUpperCase()+attrs[i].substring(1),
                    new Class[]{readMethods[i].getReturnType()}
                  ).invoke(lastObj,new Object[]{obj});
                }
              }
            }
          return obj!=null?readMethods[readMethods.length-1].invoke(obj, new Object[0]):null;
        }
      }
      catch (Throwable ex) {
        Logger.error(this.getClass().getName(),"getValue","Error while reading the value object attribute '"+attributeName+"'",ex);
      }
      return null;
  }


  /**
   * @param attributeName attribute name of the value object
   * @param valueobject value object used to fetch the attribute value
   * @return value value associated to the specified attribute name
   */
  public final Object getValue(String attributeName,ValueObject valueobject) {
      if (valueobject == null) {
        return null;
      }
      try {
        Method[] readMethods = (Method[])voGetterMethods.get(attributeName);
        if (readMethods != null) {
          Object obj = valueobject;
          if (obj!=null)
            for(int i=0;i<readMethods.length-1;i++) {
              obj = readMethods[i].invoke(obj,new Object[0]);

              // check if the inner v.o. is null...
              if(obj == null) {
                if (!createInnerVO)
                  return null;
                else
                  obj = (ValueObject)readMethods[i].getReturnType().newInstance();
              }
            }
          return obj!=null?readMethods[readMethods.length-1].invoke(obj, new Object[0]):null;
        }
      }
      catch (Throwable ex) {
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
        Method[] writeMethods = (Method[])voSetterMethods.get(attributeName);
        if (writeMethods != null) {
          Object oldValue = getValue(attributeName);
          if (value!=null) {
            if (writeMethods[writeMethods.length-1].getParameterTypes()[0].equals(java.sql.Date.class) &&
              value.getClass().equals(java.util.Date.class))
              value = new java.sql.Date(((java.util.Date)value).getTime());
            else if (writeMethods[writeMethods.length-1].getParameterTypes()[0].equals(Character.class) &&
              value.getClass().equals(String.class))
              value = value.toString().length()==0?null:new Character(value.toString().charAt(0));
            else if (writeMethods[writeMethods.length-1].getParameterTypes()[0].equals(java.sql.Timestamp.class) &&
              value.getClass().equals(java.util.Date.class))
              value = new java.sql.Timestamp(((java.util.Date)value).getTime());
            else if (writeMethods[writeMethods.length-1].getParameterTypes()[0].equals(java.sql.Timestamp.class) &&
              value.getClass().equals(java.sql.Date.class))
              value = new java.sql.Timestamp(((java.sql.Date)value).getTime());
            else if (writeMethods[writeMethods.length-1].getParameterTypes()[0].equals(Integer.class) &&
              value.getClass().equals(java.math.BigDecimal.class))
              value = new Integer(((java.math.BigDecimal)value).intValue());
            else if (writeMethods[writeMethods.length-1].getParameterTypes()[0].equals(Integer.TYPE) &&
              value.getClass().equals(java.math.BigDecimal.class))
              value = new Integer(((java.math.BigDecimal)value).intValue());
            else if (writeMethods[writeMethods.length-1].getParameterTypes()[0].equals(Long.class) &&
              value.getClass().equals(java.math.BigDecimal.class))
              value = new Long(((java.math.BigDecimal)value).longValue());
            else if (writeMethods[writeMethods.length-1].getParameterTypes()[0].equals(Long.TYPE) &&
              value.getClass().equals(java.math.BigDecimal.class))
              value = new Long(((java.math.BigDecimal)value).longValue());
            else if (writeMethods[writeMethods.length-1].getParameterTypes()[0].equals(Double.class) &&
              value.getClass().equals(java.math.BigDecimal.class))
              value = new Double(((java.math.BigDecimal)value).doubleValue());
            else if (writeMethods[writeMethods.length-1].getParameterTypes()[0].equals(Double.TYPE) &&
              value.getClass().equals(java.math.BigDecimal.class))
              value = new Double(((java.math.BigDecimal)value).doubleValue());
            else if (writeMethods[writeMethods.length-1].getParameterTypes()[0].equals(Float.class) &&
              value.getClass().equals(java.math.BigDecimal.class))
              value = new Float(((java.math.BigDecimal)value).floatValue());
            else if (writeMethods[writeMethods.length-1].getParameterTypes()[0].equals(Float.TYPE) &&
              value.getClass().equals(java.math.BigDecimal.class))
              value = new Float(((java.math.BigDecimal)value).floatValue());
            else if (writeMethods[writeMethods.length-1].getParameterTypes()[0].equals(Short.class) &&
              value.getClass().equals(java.math.BigDecimal.class))
              value = new Short(((java.math.BigDecimal)value).shortValue());
            else if (writeMethods[writeMethods.length-1].getParameterTypes()[0].equals(Short.TYPE) &&
              value.getClass().equals(java.math.BigDecimal.class))
              value = new Short(((java.math.BigDecimal)value).shortValue());
            else if (writeMethods[writeMethods.length-1].getParameterTypes()[0].equals(BigDecimal.class) &&
              value.getClass().equals(Double.class))
              value = new BigDecimal(((Double)value).doubleValue());
            else if (writeMethods[writeMethods.length-1].getParameterTypes()[0].equals(BigDecimal.class) &&
              value.getClass().equals(Long.class))
              value = new BigDecimal(((Long)value).longValue());
            else if (writeMethods[writeMethods.length-1].getParameterTypes()[0].equals(BigDecimal.class) &&
              value.getClass().equals(Float.class))
              value = new BigDecimal(((Float)value).floatValue());
            else if (writeMethods[writeMethods.length-1].getParameterTypes()[0].equals(BigDecimal.class) &&
              value.getClass().equals(Integer.class))
              value = new BigDecimal(((Integer)value).intValue());
            else if (writeMethods[writeMethods.length-1].getParameterTypes()[0].equals(BigDecimal.class) &&
              value.getClass().equals(Short.class))
              value = new BigDecimal(((Short)value).shortValue());
          }

          Object obj = getValueObject();
          if (obj!=null)
            for(int i=0;i<writeMethods.length-1;i++) {
              obj = writeMethods[i].invoke(obj,new Object[0]);

              // check if the inner v.o. is null...
              if(obj == null) {
                if (!createInnerVO)
                  return;
                else
                  obj = (ValueObject)writeMethods[i].getReturnType().newInstance();
              }
            }

          if (value==null && oldValue!=null ||
              value!=null && oldValue==null ||
              value!=null && oldValue!=null && !value.equals(oldValue)) {
            boolean isOk =
              form.getFormController()==null ?
              true :
              form.getFormController().validateControl(
                attributeName,
                oldValue,
                value
              );
            if (isOk) {
              writeMethods[writeMethods.length-1].invoke(obj, new Object[]{value});
              fireValueChanged(attributeName,oldValue,value);
            }
            else {
              form.pull(attributeName);
            }
          }
        }
      }
      catch (Exception ex) {
        Logger.error(this.getClass().getName(),"setValue","Error while writing the value object attribute '"+attributeName+"'.\n Maybe incompatible type?",ex);
      }
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
              ValueChangeListener vcl = (ValueChangeListener)valueChangeListeners.get(i);
              vcl.valueChanged(e);
          }
      }
  }


  /**
   * @return ValueObject old value object
   */
  public final ValueObject getOldValueObject() {
    return oldValueObject;
  }

}
