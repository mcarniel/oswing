package org.openswing.swing.miscellaneous.client;

import java.beans.*;

/**
 * <p>Title: OpenSwing Framework</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (C) 2006 Mauro Carniel</p>
 * <p> </p>
 * @author Mauro Carniel
 * @version 1.0
 */

public class IconifableWindowBeanInfo extends SimpleBeanInfo {
  Class beanClass = IconifableWindow.class;
  String iconColor16x16Filename;
  String iconColor32x32Filename;
  String iconMono16x16Filename;
  String iconMono32x32Filename;

  public IconifableWindowBeanInfo() {
  }
  public PropertyDescriptor[] getPropertyDescriptors() {
    try {
      PropertyDescriptor _allowsCloseWindow = new PropertyDescriptor("allowsCloseWindow", beanClass, "isAllowsCloseWindow", "setAllowsCloseWindow");
      PropertyDescriptor _iconHeight = new PropertyDescriptor("iconHeight", beanClass, "getIconHeight", "setIconHeight");
      PropertyDescriptor _windowMaximumSize = new PropertyDescriptor("windowMaximumSize", beanClass, "getWindowMaximumSize", "setWindowMaximumSize");
      PropertyDescriptor _showCloseButton = new PropertyDescriptor("showCloseButton", beanClass, "isShowCloseButton", "setShowCloseButton");
      PropertyDescriptor _showReduceToIconButton = new PropertyDescriptor("showReduceToIconButton", beanClass, "isShowReduceToIconButton", "setShowReduceToIconButton");
      PropertyDescriptor _title = new PropertyDescriptor("title", beanClass, "getTitle", "setTitle");
      PropertyDescriptor _titleImageName = new PropertyDescriptor("titleImageName", beanClass, "getTitleImageName", "setTitleImageName");
      PropertyDescriptor[] pds = new PropertyDescriptor[] {
        _allowsCloseWindow,
        _iconHeight,
        _showCloseButton,
        _showReduceToIconButton,
        _title,
        _titleImageName,
        _windowMaximumSize};
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
