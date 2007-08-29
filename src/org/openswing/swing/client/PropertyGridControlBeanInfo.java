package org.openswing.swing.client;

import java.beans.*;

/**
 * <p>Title: OpenSwing Framework</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (C) 2006 Mauro Carniel</p>
 * <p> </p>
 * @author Mauro Carniel
 * @version 1.0
 */

public class PropertyGridControlBeanInfo extends SimpleBeanInfo {
  Class beanClass = PropertyGridControl.class;
  String iconColor16x16Filename;
  String iconColor32x32Filename;
  String iconMono16x16Filename;
  String iconMono32x32Filename;

  public PropertyGridControlBeanInfo() {
  }
  public PropertyDescriptor[] getPropertyDescriptors() {
    try {
      PropertyDescriptor _controller = new PropertyDescriptor("controller", beanClass, "getController", "setController");
      PropertyDescriptor _editButton = new PropertyDescriptor("editButton", beanClass, "getEditButton", "setEditButton");
      PropertyDescriptor _insertButton = new PropertyDescriptor("insertButton", beanClass, "getInsertButton", "setInsertButton");
      PropertyDescriptor _propertyNameWidth = new PropertyDescriptor("propertyNameWidth", beanClass, null, "setPropertyNameWidth");
      PropertyDescriptor _propertyValueWidth = new PropertyDescriptor("propertyValueWidth", beanClass, "getPropertyValueWidth", "setPropertyValueWidth");
      PropertyDescriptor _reloadButton = new PropertyDescriptor("reloadButton", beanClass, "getReloadButton", "setReloadButton");
      PropertyDescriptor _saveButton = new PropertyDescriptor("saveButton", beanClass, "getSaveButton", "setSaveButton");
      PropertyDescriptor[] pds = new PropertyDescriptor[] {
        _controller,
        _editButton,
        _insertButton,
        _propertyNameWidth,
        _propertyValueWidth,
        _reloadButton,
        _saveButton};
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
}