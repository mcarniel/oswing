package org.openswing.swing.client;

import java.beans.*;

/**
 * <p>Title: OpenSwing Framework</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (C) 2006 Mauro Carniel</p>
 * <p> </p>
 * @author not attributable
 * @version 1.0
 */

public class NavigatorBarBeanInfo extends SimpleBeanInfo {
  Class beanClass = NavigatorBar.class;
  String iconColor16x16Filename = "NavigatorBar16.png";
  String iconColor32x32Filename = "NavigatorBar.png";
  String iconMono16x16Filename = "NavigatorBar16.png";
  String iconMono32x32Filename = "NavigatorBar.png";

  public NavigatorBarBeanInfo() {
  }
  public PropertyDescriptor[] getPropertyDescriptors() {
    try {
      PropertyDescriptor _showPaginationButtons = new PropertyDescriptor("showPaginationButtons", beanClass, "isShowPaginationButtons","setShowPaginationButtons");
      PropertyDescriptor _showPageNumber = new PropertyDescriptor("showPageNumber", beanClass, "isShowPageNumber","setShowPageNumber");
      PropertyDescriptor[] pds = new PropertyDescriptor[] {
          _showPageNumber,
          _showPaginationButtons
      };
      return pds;
    }
    catch (IntrospectionException ex) {
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
