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

public class ProgressDialogBeanInfo extends SimpleBeanInfo {
  Class beanClass = ProgressDialog.class;
  String iconColor16x16Filename;
  String iconColor32x32Filename;
  String iconMono16x16Filename;
  String iconMono32x32Filename;

  public ProgressDialogBeanInfo() {
  }
  public PropertyDescriptor[] getPropertyDescriptors() {
    try {
      PropertyDescriptor _imageName = new PropertyDescriptor("imageName", beanClass, "getImageName", "setImageName");
      PropertyDescriptor _maximumValue = new PropertyDescriptor("maximumValue", beanClass, "getMaximumValue", "setMaximumValue");
      PropertyDescriptor _minimumValue = new PropertyDescriptor("minimumValue", beanClass, "getMinimumValue", "setMinimumValue");
      PropertyDescriptor _progressBarColor = new PropertyDescriptor("progressBarColor", beanClass, "getProgressBarColor", "setProgressBarColor");
      PropertyDescriptor _showAllBands = new PropertyDescriptor("showAllBands", beanClass, "isShowAllBands", "setShowAllBands");
      PropertyDescriptor[] pds = new PropertyDescriptor[] {
        _imageName,
        _maximumValue,
        _minimumValue,
        _progressBarColor,
        _showAllBands};
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