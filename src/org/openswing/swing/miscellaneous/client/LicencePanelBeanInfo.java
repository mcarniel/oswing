package org.openswing.swing.miscellaneous.client;

import java.beans.*;

/**
 * <p>Title: OpenSwing Framework</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (C) 2006 Mauro Carniel</p>
 * <p> </p>
 * @author not attributable
 * @version 1.0
 */

public class LicencePanelBeanInfo extends SimpleBeanInfo {
  Class beanClass = LicencePanel.class;
  String iconColor16x16Filename;
  String iconColor32x32Filename;
  String iconMono16x16Filename;
  String iconMono32x32Filename;

  public LicencePanelBeanInfo() {
  }
  public PropertyDescriptor[] getPropertyDescriptors() {
    try {
      PropertyDescriptor _imageName = new PropertyDescriptor("imageName", beanClass, "getImageName", "setImageName");
      PropertyDescriptor _licence = new PropertyDescriptor("licence", beanClass, "getLicence", "setLicence");
      PropertyDescriptor _showBackButton = new PropertyDescriptor("showBackButton", beanClass, "isShowBackButton", "setShowBackButton");
      PropertyDescriptor _showCancelButton = new PropertyDescriptor("showCancelButton", beanClass, "isShowCancelButton", "setShowCancelButton");
      PropertyDescriptor _showOkButton = new PropertyDescriptor("showOkButton", beanClass, "isShowOkButton", "setShowOkButton");
      PropertyDescriptor _subTitle = new PropertyDescriptor("subTitle", beanClass, "getSubTitle", "setSubTitle");
      PropertyDescriptor _title = new PropertyDescriptor("title", beanClass, "getTitle", "setTitle");
      PropertyDescriptor[] pds = new PropertyDescriptor[] {
        _imageName,
        _licence,
        _showBackButton,
        _showCancelButton,
        _showOkButton,
        _subTitle,
        _title};
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
