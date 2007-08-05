package org.openswing.swing.table.columns.client;

import java.beans.*;


/**
 * <p>Title: OpenSwing Framework</p>
 * <p>Description: BeanInfo</p>
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
public class CodLookupColumnBeanInfo extends SimpleBeanInfo {
  private Class beanClass = CodLookupColumn.class;
  private String iconColor16x16Filename = "CodLookupColumn16.gif";
  private String iconColor32x32Filename = "CodLookupColumn.gif";
  private String iconMono16x16Filename = "CodLookupColumn16.gif";
  private String iconMono32x32Filename = "CodLookupColumn.gif";

  public CodLookupColumnBeanInfo() {
  }
  public PropertyDescriptor[] getPropertyDescriptors() {
    try {
      PropertyDescriptor _allowOnlyNumbers = new PropertyDescriptor("allowOnlyNumbers", beanClass, "isAllowOnlyNumbers", "setAllowOnlyNumbers");
      PropertyDescriptor _codePadding = new PropertyDescriptor("codePadding", beanClass, "isCodePadding", "setCodePadding");
      PropertyDescriptor _columnName = new PropertyDescriptor("columnName", beanClass, "getColumnName", "setColumnName");
      _columnName.setPropertyEditorClass(org.openswing.swing.client.AttributeNameEditor.class);
      PropertyDescriptor _controllerClassName = new PropertyDescriptor("controllerClassName", beanClass, "getControllerClassName", "setControllerClassName");
      PropertyDescriptor _controllerMethodName = new PropertyDescriptor("controllerMethodName", beanClass, "getControllerMethodName", "setControllerMethodName");
      PropertyDescriptor _enableCodBox = new PropertyDescriptor("enableCodBox", beanClass, "isEnableCodBox", "setEnableCodBox");
      PropertyDescriptor _hideButton = new PropertyDescriptor("hideButton", beanClass, "isHideButton", "setHideButton");
      PropertyDescriptor _hideCodeBox = new PropertyDescriptor("hideCodBox", beanClass, "isHideCodeBox", "setHideCodeBox");
      PropertyDescriptor _lookupController = new PropertyDescriptor("lookupController", beanClass, "getLookupController", "setLookupController");
      PropertyDescriptor _maxCharacters = new PropertyDescriptor("maxCharacters", beanClass, "getMaxCharacters", "setMaxCharacters");
      PropertyDescriptor _zeroLengthAsNull = new PropertyDescriptor("zeroLengthAsNull", beanClass, "isZeroLengthAsNull", "setZeroLengthAsNull");
      PropertyDescriptor[] pds = new PropertyDescriptor[] {
	      _allowOnlyNumbers,
	      _codePadding,
              _columnName,
              _controllerClassName,
              _controllerMethodName,
	      _enableCodBox,
	      _hideButton,
              _hideCodeBox,
	      _lookupController,
	      _maxCharacters,
	      _zeroLengthAsNull};
      return pds;
    }
    catch(IntrospectionException ex) {
      ex.printStackTrace();
      return null;
    }
  }
  public java.awt.Image getIcon(int iconKind) {
    switch (iconKind) {
      case BeanInfo.ICON_COLOR_16x16:
        return iconColor16x16Filename != null ? loadImage(iconColor16x16Filename) : null;
      case BeanInfo.ICON_COLOR_32x32:
        return iconColor32x32Filename != null ? loadImage(iconColor32x32Filename) : null;
      case BeanInfo.ICON_MONO_16x16:
        return iconMono16x16Filename != null ? loadImage(iconMono16x16Filename) : null;
      case BeanInfo.ICON_MONO_32x32:
        return iconMono32x32Filename != null ? loadImage(iconMono32x32Filename) : null;
    }
    return null;
  }
  public BeanInfo[] getAdditionalBeanInfo() {
    Class superclass = beanClass.getSuperclass();
    try {
      BeanInfo superBeanInfo = Introspector.getBeanInfo(superclass);
      return new BeanInfo[] { superBeanInfo };
    }
    catch(IntrospectionException ex) {
      ex.printStackTrace();
      return null;
    }
  }
}
