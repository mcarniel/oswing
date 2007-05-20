package org.openswing.swing.client;

import java.beans.PropertyEditorSupport;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.ArrayList;
import javax.swing.JOptionPane;
import org.openswing.swing.message.receive.java.ValueObject;
import org.openswing.swing.form.client.Form;
import org.openswing.swing.table.columns.client.*;
import org.openswing.swing.message.receive.java.ValueObjectImpl;
import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;


/**
 * <p>Title: OpenSwing Framework</p>
 * <p>Description: Editor used in design time to select an attribute name from the value object.</p>
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
public class AttributeNameEditor extends PropertyEditorSupport {

  /** default value object */
  private static final Class DEFAULT_VO_CLASS_NAME =  ValueObjectImpl.class;

  /** current editing column type */
  private static Class columnType = null;

  /** v.o. class linked to this Form panel; used only in design time */
  private static Class designVOClass;

  /** list of tags */
  private static String[] attributeNames;


  public AttributeNameEditor() {
  }


  /**
   * Method called by input controls.
   */
  public static final synchronized void setColumnType(Class colType) {
    if (colType!=null && columnType!=null && !columnType.equals(colType))
      attributeNames = null;
    columnType = colType;
  }


  public String[] getTags() {
//    if (attributeNames!=null && attributeNames.length>0)
//      return attributeNames;
    attributeNames = new String[0];

    try {
      if (designVOClass==null) {
        if (designVOClass==null) {
//          String className = JOptionPane.showInputDialog(null,"Please enter value object class name: ","Value Object",JOptionPane.INFORMATION_MESSAGE);
//          if (className==null || className.equals(""))
            designVOClass = DEFAULT_VO_CLASS_NAME;
//          else
//            designVOClass = Class.forName(className);
        }
      }
      Object o = designVOClass.newInstance();
      if (o instanceof ValueObject) {
        try {
          ArrayList attrList = new ArrayList();
          analyzeClassFields("",attrList,designVOClass);
//          Method[] methods = designVOClass.getMethods();
//          String attrName = null;
//          for(int i=0;i<methods.length;i++) {
//            if (methods[i].getName().startsWith("get")) {
//              attrName = methods[i].getName().substring(3);
//              if (attrName.length()==1)
//                attrName = attrName.toLowerCase();
//              else
//                attrName = attrName.substring(0,1).toLowerCase()+attrName.substring(1);
//              if (columnType==null ||
//                  isCompatible(methods[i].getReturnType()))
//                attrList.add(attrName);
//            }
//            else if (methods[i].getName().startsWith("is")) {
//              attrName = methods[i].getName().substring(2);
//              if (attrName.length()==1)
//                attrName = attrName.toLowerCase();
//              else
//                attrName = attrName.substring(0,1).toLowerCase()+attrName.substring(1);
//              if (columnType==null ||
//                  isCompatible(methods[i].getReturnType()))
//                attrList.add(attrName);
//            }
//          }
          attributeNames = (String[])attrList.toArray(new String[attrList.size()]);
        }
        catch (Exception ex) {
          JOptionPane.showMessageDialog(null,"Error while fetching attribute names:\n"+ex.getMessage(),"Error",JOptionPane.ERROR_MESSAGE);
        }
      } else {
        JOptionPane.showMessageDialog(null,designVOClass.getName()+" does not extend ValueObject class.","Error",JOptionPane.ERROR_MESSAGE);
      }
    }
//    catch (ClassNotFoundException ex) {
//      JOptionPane.showMessageDialog(null,"Value Object not Found","Error",JOptionPane.ERROR_MESSAGE);
//    }
    catch (Exception ex) {
      JOptionPane.showMessageDialog(null,"Error while analyzing Value Object:\n"+ex.getMessage(),"Error",JOptionPane.ERROR_MESSAGE);
    }

    return attributeNames;
  }


  private void analyzeClassFields(String prefix,ArrayList attributes,Class classType) {
    try {
      BeanInfo  info = Introspector.getBeanInfo(classType);
      // retrieve attribute properties...
      PropertyDescriptor[] props = info.getPropertyDescriptors();
      for (int i = 0; i < props.length; i++) {

        if (props[i].getReadMethod()!=null &&
            props[i].getReadMethod().getParameterTypes().length==0 &&
            ValueObject.class.isAssignableFrom( props[i].getReadMethod().getReturnType() )
        ) {
          analyzeClassFields(prefix+props[i].getName()+".",attributes,props[i].getReadMethod().getReturnType());
        }
        else {
          if (props[i].getReadMethod().getReturnType()!=null &&
              isCompatible(props[i].getReadMethod().getReturnType()))
            attributes.add(prefix+props[i].getName());
        }
      }

    }
    catch (Throwable ex) {
    }
  }


  /**
   * @param attrType tipo dell'attributo
   * @return "true" se i due tipi sono compatibili, "false" altrimenti
   */
  private boolean isCompatible(Class attrType) {
    try {
      // input controls...
      if ((attrType.equals(Integer.class) ||
           attrType.equals(Long.class) ||
           attrType.equals(Double.class) ||
           attrType.equals(BigDecimal.class) ||
           attrType.equals(Float.class)) &&
          (columnType.equals(NumericControl.class) ||
           columnType.equals(CurrencyControl.class) )) {
        return true;
      }
      else if ((attrType.equals(java.util.Date.class) ||
                attrType.equals(java.sql.Date.class) ||
                attrType.equals(Timestamp.class))  &&
               (columnType.equals(DateControl.class))) {
        return true;
      }
      else if (attrType.equals(byte[].class)  &&
               (columnType.equals(ImageControl.class))) {
        return true;
      }
      else if (attrType.equals(String.class) && columnType.equals(TextControl.class)) {
        return true;
      }
      else if (attrType.equals(String.class) && columnType.equals(TextAreaControl.class)) {
        return true;
      }
      else if (columnType.equals(CheckBoxControl.class) && (attrType.equals(Boolean.class) || attrType.equals(boolean.class))) {
        return true;
      }
      else if (columnType.equals(RadioButtonControl.class) && (attrType.equals(Boolean.class) || attrType.equals(boolean.class))) {
        return true;
      }
      else if (columnType.equals(ComboBoxControl.class) || ComboBoxControl.class.isAssignableFrom(columnType)) {
        return true;
      }
      else if (columnType.equals(CodLookupControl.class)) {
        return true;
      }

      // grid columns...
      if ((attrType.equals(Integer.class) ||
           attrType.equals(Long.class)) &&
          (columnType.equals(PercentageColumn.class) ||
           columnType.equals(IntegerColumn.class))) {
        return true;
      }
      else if (attrType.equals(byte[].class) &&
               columnType.equals(ImageColumn.class)) {
        return true;
      }
      else if ((attrType.equals(Double.class) ||
                attrType.equals(BigDecimal.class) ||
                attrType.equals(Float.class)) &&
               (columnType.equals(DecimalColumn.class) ||
                columnType.equals(IntegerColumn.class) ||
                columnType.equals(CurrencyColumn.class) ||
                columnType.equals(PercentageColumn.class))) {
        return true;
      }
      else if ((attrType.equals(java.util.Date.class) ||
                attrType.equals(java.sql.Date.class) ||
                attrType.equals(Timestamp.class)) &&
               (columnType.equals(DateColumn.class) ||
                columnType.equals(DateTimeColumn.class) ||
                columnType.equals(TimeColumn.class))) {
        return true;
      }
      else if (attrType.equals(String.class) && columnType.equals(TextColumn.class)) {
        return true;
      }
      else if (columnType.equals(CheckBoxColumn.class)) {
        return true;
      }
      else if (columnType.equals(ComboColumn.class)) {
        return true;
      }
      else if (columnType.equals(CodLookupColumn.class)) {
        return true;
      }

      return false;
    }
    catch (Throwable ex) {
      return false;
    }
  }


  public String getJavaInitializationString() {
    if (attributeNames==null)
      getTags();
    if (attributeNames==null)
      return "";
    for(int i=0;i<attributeNames.length;i++)
     if (getValue().equals(attributeNames[i]))
         return "\""+attributeNames[i]+"\"";
    return "";
   }



  public void setAsText(String text) throws IllegalArgumentException {
    setValue(text);
  }


  public String getAsText() {
    return (String)getValue();
  }


  public static void setDesignVOClass(Class voClass) {
    designVOClass = voClass;
  }

}
