package org.openswing.swing.wizard.client;

import java.beans.*;

/**
 * <p>Title: OpenSwing Framework</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (C) 2006 Mauro Carniel</p>
 * <p> </p>
 * @author not attributable
 * @version 1.0
 */

public class WizardPanelBeanInfo extends SimpleBeanInfo {
  Class beanClass = WizardPanel.class;
  String iconColor16x16Filename;
  String iconColor32x32Filename;
  String iconMono16x16Filename;
  String iconMono32x32Filename;

  public WizardPanelBeanInfo() {
  }
  public PropertyDescriptor[] getPropertyDescriptors() {
    try {
      PropertyDescriptor _cancelImageName = new PropertyDescriptor("cancelImageName", beanClass, "getCancelImageName", "setCancelImageName");
      PropertyDescriptor _finishImageName = new PropertyDescriptor("finishImageName", beanClass, "getFinishImageName", "setFinishImageName");
      PropertyDescriptor _imageName = new PropertyDescriptor("imageName", beanClass, "getImageName", "setImageName");
      PropertyDescriptor _navigationLogic = new PropertyDescriptor("navigationLogic", beanClass, "getNavigationLogic", "setNavigationLogic");
      PropertyDescriptor _nextImageName = new PropertyDescriptor("nextImageName", beanClass, "getNextImageName", "setNextImageName");
      PropertyDescriptor[] pds = new PropertyDescriptor[] {
        _cancelImageName,
        _finishImageName,
        _imageName,
        _navigationLogic,
        _nextImageName};
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
