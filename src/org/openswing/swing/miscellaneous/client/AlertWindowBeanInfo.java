package org.openswing.swing.miscellaneous.client;

import java.beans.*;

/**
 * <p>Title: OpenSwing Framework</p>
 * @author MC
 * @version 1.0
 */
public class AlertWindowBeanInfo extends SimpleBeanInfo {
  Class beanClass = AlertWindow.class;
  String iconColor16x16Filename;
  String iconColor32x32Filename;
  String iconMono16x16Filename;
  String iconMono32x32Filename;

  public AlertWindowBeanInfo() {
  }
  public PropertyDescriptor[] getPropertyDescriptors() {
    try {
      PropertyDescriptor _allowsCloseWindow = new PropertyDescriptor("allowsCloseWindow", beanClass, "isAllowsCloseWindow", "setAllowsCloseWindow");
      PropertyDescriptor _fadeInTime = new PropertyDescriptor("fadeInTime", beanClass, "getFadeInTime", "setFadeInTime");
      PropertyDescriptor _fadeOutTime = new PropertyDescriptor("fadeOutTime", beanClass, "getFadeOutTime", "setFadeOutTime");
      PropertyDescriptor _mainText = new PropertyDescriptor("mainText", beanClass, "getMainText", "setMainText");
      PropertyDescriptor _title = new PropertyDescriptor("title", beanClass, "getTitle", "setTitle");
      PropertyDescriptor _iconHeight = new PropertyDescriptor("iconHeight", beanClass, "getIconHeight", "setIconHeight");
      PropertyDescriptor _imageName = new PropertyDescriptor("imageName", beanClass, "getImageName", "setImageName");
      PropertyDescriptor _reduceToIconOnTimeout = new PropertyDescriptor("reduceToIconOnTimeout", beanClass, "isReduceToIconOnTimeout", "setReduceToIconOnTimeout");
      PropertyDescriptor _showCloseButton = new PropertyDescriptor("showCloseButton", beanClass, "isShowCloseButton", "setShowCloseButton");
      PropertyDescriptor _showReduceToIconButton = new PropertyDescriptor("showReduceToIconButton", beanClass, "isShowReduceToIconButton", "setShowReduceToIconButton");
      PropertyDescriptor _timeout = new PropertyDescriptor("timeout", beanClass, "getTimeout", "setTimeout");
      PropertyDescriptor _windowMaximumSize = new PropertyDescriptor("windowMaximumSize", beanClass, "getWindowMaximumSize", "setWindowMaximumSize");
      PropertyDescriptor[] pds = new PropertyDescriptor[] {
        _allowsCloseWindow,
        _fadeInTime,
        _fadeOutTime,
        _iconHeight,
        _imageName,
        _mainText,
        _reduceToIconOnTimeout,
        _showCloseButton,
        _showReduceToIconButton,
        _timeout,
        _title,
        _windowMaximumSize
      };
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
