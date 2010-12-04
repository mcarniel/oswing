package org.openswing.swing.client;

import java.beans.*;
import java.util.*;

import javax.swing.*;

import org.openswing.swing.message.receive.java.*;


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

  /** v.o. class linked to this Form panel; used only in design time */
  private static Class designVOClass;

  /** list of tags */
  private String[] attributeNames;


  public AttributeNameEditor() {
  }


  public String[] getTags() {
    attributeNames = new String[0];
    try {
      if (designVOClass==null) {
        if (designVOClass==null) {
            designVOClass = DEFAULT_VO_CLASS_NAME;
        }
      }
      Object o = designVOClass.newInstance();
      if (o instanceof ValueObject) {
        try {
          ArrayList attrList = new ArrayList();
          analyzeClassFields("",attrList,designVOClass);
          attributeNames = (String[])attrList.toArray(new String[attrList.size()]);
        }
        catch (Exception ex) {
          JOptionPane.showMessageDialog(null,"Error while fetching attribute names:\n"+ex.getMessage(),"Error",JOptionPane.ERROR_MESSAGE);
        }
      } else {
        JOptionPane.showMessageDialog(null,designVOClass.getName()+" does not extend ValueObject class.","Error",JOptionPane.ERROR_MESSAGE);
      }
    }
    catch (Throwable ex) {
      JOptionPane.showMessageDialog(null,"Error while analyzing Value Object:\n"+ex.getMessage(),"Error",JOptionPane.ERROR_MESSAGE);
    }

    return attributeNames;
  }


  private void analyzeClassFields(String prefix,ArrayList attributes,Class classType) {
    try {
      if (classType!=null) {
        if (prefix.split("\\.").length>3) {
          return;
        }
      }

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
        if (props[i].getReadMethod().getReturnType()!=null &&
            isCompatible(props[i].getReadMethod().getReturnType()) &&
            !props[i].getName().equals("class"))
          attributes.add(prefix+props[i].getName());
      }

    }
    catch (Throwable ex) {
    }
  }


  /**
   * @param attrType tipo dell'attributo
   * @return "true" se i due tipi sono compatibili, "false" altrimenti
   */
  protected boolean isCompatible(Class attrType) {
    return true;
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
