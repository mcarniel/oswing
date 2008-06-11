package org.openswing.swing.table.columns.client;

import java.beans.*;


/**
 * <p>Title: OpenSwing Framework</p>
 * <p>Description: Bean Info</p>
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

public class FormattedTextColumnBeanInfo extends SimpleBeanInfo {
  Class beanClass = FormattedTextColumn.class;
  String iconColor16x16Filename = "TextColumn16.png";
  String iconColor32x32Filename = "TextColumn.png";
  String iconMono16x16Filename = "TextColumn16.png";
  String iconMono32x32Filename = "TextColumn.png";

  public FormattedTextColumnBeanInfo() {
  }
  public PropertyDescriptor[] getPropertyDescriptors() {
    PropertyDescriptor[] pds = new PropertyDescriptor[0];
    try {
      PropertyDescriptor _columnName = null;
      PropertyDescriptor _leftMargin = new PropertyDescriptor("leftMargin", beanClass, "getLeftMargin", "setLeftMargin");
      PropertyDescriptor _rightMargin = new PropertyDescriptor("rightMargin", beanClass, "getRightMargin", "setRightMargin");
      PropertyDescriptor _topMargin = new PropertyDescriptor("topMargin", beanClass, "getTopMargin", "setTopMargin");
      PropertyDescriptor _bottomMargin = new PropertyDescriptor("bottomMargin", beanClass, "getBottomMargin", "setBottomMargin");
      _columnName = new PropertyDescriptor("columnName", beanClass,
                                           "getColumnName", "setColumnName");
      _columnName.setPropertyEditorClass(org.openswing.swing.client.AttributeNameEditor.class);
      pds = new PropertyDescriptor[] {
        _bottomMargin,
        _columnName,
        _leftMargin,
        _rightMargin,
        _topMargin
    };
  }
  catch (IntrospectionException ex) {
  }
    return pds;
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
