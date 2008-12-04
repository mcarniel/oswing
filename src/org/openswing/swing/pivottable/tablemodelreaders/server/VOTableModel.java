package org.openswing.swing.pivottable.tablemodelreaders.server;

import java.util.*;

import javax.swing.event.*;
import javax.swing.table.*;
import java.lang.reflect.Method;
import org.openswing.swing.message.receive.java.ValueObject;
import java.math.BigDecimal;
import javax.swing.ImageIcon;
import javax.swing.Icon;

/**
 * <p>Title: OpenSwing Framework</p>
 * <p>Description: TableModel implementation, based on a list of ValueObjects.</p>
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
public class VOTableModel extends AbstractTableModel {

  /** list of ValueObjects */
  private List valueObjects;

  /** ValueObject setter methods */
  private Hashtable voSetterMethods = new Hashtable();

  /** ValueObject getter methods */
  private Hashtable voGetterMethods = new Hashtable();

  /** ValueObject type */
  private Class valueObjectType = null;

  /** collection of couples: related attribute (String), column model index (Integer) */
  private Hashtable reverseIndexes = new Hashtable();

  /** attribute names */
  private String[] attributeNames = null;


  /**
   * Constructs a TableModel, starting from the specified list of ValueObjects, having the specified list of attributes
   * @param valueObjects list of ValueObjects
   * @param attributeNames nome of attributes defined within the ValueObjects, used to define the TableModel
   */
  public VOTableModel(List valueObjects,String[] attributeNames) {
    this.valueObjects = valueObjects;
    this.attributeNames = attributeNames;
    this.valueObjectType = valueObjects.get(0).getClass();
    analyzeClassFields(new Hashtable(),"",new Method[0],valueObjectType);
  }


  /**
   * @param row row number
   * @return ValueObject
   */
  public final ValueObject getValueObject(int row) {
    return (ValueObject)valueObjects.get(row);
  }


  /**
   * Analyze class fields and fill in "voSetterMethods","voGetterMethods","indexes",reverseIndexes" attributes.
   * @param prefix e.g. "attrx.attry."
   * @param parentMethods getter methods of parent v.o.
   * @param classType class to analyze
   */
  private void analyzeClassFields(Hashtable vosAlreadyProcessed,String prefix,Method[] parentMethods,Class classType) {
    try {
      Integer num = (Integer)vosAlreadyProcessed.get(classType);
      if (num==null)
        num = new Integer(0);
      num = new Integer(num.intValue()+1);
      if (num.intValue()>10)
        return;
      vosAlreadyProcessed.put(classType,num);

      // retrieve all getter and setter methods defined in the specified value object...
      String attributeName = null;
      Method[] methods = classType.getMethods();
      String aName = null;
      for(int i=0;i<methods.length;i++) {
        attributeName = methods[i].getName();

        if (attributeName.startsWith("get") && methods[i].getParameterTypes().length==0 &&
          ValueObject.class.isAssignableFrom(methods[i].getReturnType())) {
          aName = getAttributeName(attributeName,classType);
          Method[] newparentMethods = new Method[parentMethods.length+1];
          System.arraycopy(parentMethods,0,newparentMethods,0,parentMethods.length);
          newparentMethods[parentMethods.length] = methods[i];
          analyzeClassFields(vosAlreadyProcessed,prefix+aName+".",newparentMethods,methods[i].getReturnType());
        }

        if (attributeName.startsWith("get") && methods[i].getParameterTypes().length==0 &&
            (methods[i].getReturnType().equals(String.class) ||
             methods[i].getReturnType().equals(Long.class) ||
             methods[i].getReturnType().equals(Long.TYPE) ||
             methods[i].getReturnType().equals(Float.class) ||
             methods[i].getReturnType().equals(Float.TYPE) ||
             methods[i].getReturnType().equals(Short.class) ||
             methods[i].getReturnType().equals(Short.TYPE) ||
             methods[i].getReturnType().equals(Double.class) ||
             methods[i].getReturnType().equals(Double.TYPE) ||
             methods[i].getReturnType().equals(BigDecimal.class) ||
             methods[i].getReturnType().equals(java.util.Date.class) ||
             methods[i].getReturnType().equals(java.sql.Date.class) ||
             methods[i].getReturnType().equals(java.sql.Timestamp.class) ||
             methods[i].getReturnType().equals(Integer.class) ||
             methods[i].getReturnType().equals(Integer.TYPE) ||
             methods[i].getReturnType().equals(Character.class) ||
             methods[i].getReturnType().equals(Boolean.class) ||
             methods[i].getReturnType().equals(boolean.class) ||
             methods[i].getReturnType().equals(ImageIcon.class) ||
             methods[i].getReturnType().equals(Icon.class) ||
             methods[i].getReturnType().equals(byte[].class) ||
             methods[i].getReturnType().equals(Object.class) ||
             ValueObject.class.isAssignableFrom( methods[i].getReturnType() )
            )) {
         attributeName = getAttributeName(attributeName,classType);
//          try {
//            if (classType.getMethod("set"+attributeName.substring(0,1).toUpperCase()+attributeName.substring(1),new Class[]{methods[i].getReturnType()})!=null)
          Method[] newparentMethods = new Method[parentMethods.length+1];
          System.arraycopy(parentMethods,0,newparentMethods,0,parentMethods.length);
          newparentMethods[parentMethods.length] = methods[i];
          voGetterMethods.put(prefix+attributeName,newparentMethods);
//          } catch (NoSuchMethodException ex) {
//          }
        }
        else if (attributeName.startsWith("is") &&
                 methods[i].getParameterTypes().length==0 &&
                 (methods[i].getReturnType().equals(Boolean.class) ||
                  methods[i].getReturnType().equals(boolean.class) )) {
          attributeName = getAttributeName(attributeName,classType);
          Method[] newparentMethods = new Method[parentMethods.length+1];
          System.arraycopy(parentMethods,0,newparentMethods,0,parentMethods.length);
          newparentMethods[parentMethods.length] = methods[i];
          voGetterMethods.put(prefix+attributeName,newparentMethods);
        }
        else if (attributeName.startsWith("set") && methods[i].getParameterTypes().length==1) {
          attributeName = getAttributeName(attributeName,classType);
          try {
            if (classType.getMethod("get"+attributeName.substring(0,1).toUpperCase()+attributeName.substring(1),new Class[0])!=null) {
              Method[] newparentMethods = new Method[parentMethods.length+1];
              System.arraycopy(parentMethods,0,newparentMethods,0,parentMethods.length);
              newparentMethods[parentMethods.length] = methods[i];
              voSetterMethods.put(prefix+attributeName,newparentMethods);
            }
          } catch (NoSuchMethodException ex) {
            try {
              if (classType.getMethod("is"+attributeName.substring(0,1).toUpperCase()+attributeName.substring(1),new Class[0])!=null) {
                Method[] newparentMethods = new Method[parentMethods.length+1];
                System.arraycopy(parentMethods,0,newparentMethods,0,parentMethods.length);
                newparentMethods[parentMethods.length] = methods[i];
                voSetterMethods.put(prefix+attributeName,newparentMethods);
              }
            } catch (NoSuchMethodException exx) {
            }
          }
        }
      }

      // fill in indexes with the colProperties indexes first; after them, it will be added the other indexes (of attributes not mapped with grid column...)
      HashSet alreadyAdded = new HashSet();
      int i=0;
      for(i=0;i<attributeNames.length;i++) {
        reverseIndexes.put(attributeNames[i],new Integer(i));
        alreadyAdded.add(attributeNames[i]);
      }
      Enumeration en = voGetterMethods.keys();
      while(en.hasMoreElements()) {
        attributeName = en.nextElement().toString();
        if (!alreadyAdded.contains(attributeName)) {
          reverseIndexes.put(attributeName,new Integer(i));
          i++;
        }
      }
    }
    catch (Exception ex) {
      ex.printStackTrace();
    }
  }


  /**
   * @param methodName getter method
   * @param clazz value object class
   * @return attribute name related to the specified getter method
   */
  private String getAttributeName(String methodName,Class classType) {
    String attributeName = null;
    if (methodName.startsWith("is"))
      attributeName = methodName.substring(2,3).toLowerCase()+(methodName.length()>3?methodName.substring(3):"");
    else
      attributeName = methodName.substring(3,4).toLowerCase()+(methodName.length()>4?methodName.substring(4):"");

    // an attribute name "Xxxx" becomes "xxxx" and this is not correct!
    try {
      Class c = classType;
      boolean attributeFound = false;
      while(!c.equals(Object.class)) {
        try {
          c.getDeclaredField(attributeName);
          attributeFound = true;
          break;
        }
        catch (Throwable ex2) {
          c = c.getSuperclass();
        }
      }
      if (!attributeFound) {
        // now trying to find an attribute having the first character in upper case (e.g. "Xxxx")
        String name = attributeName.substring(0,1).toUpperCase()+attributeName.substring(1);
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
          attributeName = name;
      }
    }
    catch (Throwable ex1) {
    }


    return attributeName;
  }


  public final int getColumnCount() {
    return attributeNames.length;
  }


  public final int getRowCount() {
    return valueObjects.size();
  }


  public final boolean isCellEditable(int rowIndex, int columnIndex) {
    return false;
  }


  public final Class getColumnClass(int columnIndex) {
    try {
      Method[] m = (Method[])voGetterMethods.get(attributeNames[columnIndex]);
      return m[m.length-1].getReturnType();
    }
    catch (Exception ex) {
      ex.printStackTrace();
      return String.class;
    }
  }


  public final Object getValueAt(int rowIndex, int colIndex) {
    try {
      Method[] m = (Method[])voGetterMethods.get(attributeNames[colIndex]);
      Object obj = valueObjects.get(rowIndex);
      for(int i=0;i<m.length-1;i++){
        obj = (ValueObject)m[i].invoke(obj,new Object[0]);
        if(obj == null) {
          return null;
        }
      }
      return m[m.length-1].invoke(obj,new Object[0]);
    }
    catch (Exception ex) {
      ex.printStackTrace();
      return null;
    }
  }


  /**
   * setValueAt
   *
   * @param aValue Object
   * @param rowIndex int
   * @param columnIndex int
   */
  public final void setValueAt(Object aValue, int rowIndex, int columnIndex) {
    throw new UnsupportedOperationException("TableModel is read only.");
  }


  /**
   * getColumnName
   *
   * @param columnIndex int
   * @return String
   */
  public final String getColumnName(int columnIndex) {
    return attributeNames[columnIndex];
  }



}
