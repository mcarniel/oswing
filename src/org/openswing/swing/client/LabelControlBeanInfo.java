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

public class LabelControlBeanInfo extends SimpleBeanInfo {
  Class beanClass = LabelControl.class;
  String iconColor16x16Filename = "LabelControl16.gif";
  String iconColor32x32Filename = "LabelControl.gif";
  String iconMono16x16Filename = "LabelControl16.gif";
  String iconMono32x32Filename = "LabelControl.gif";

  public LabelControlBeanInfo() {
  }
  public PropertyDescriptor[] getPropertyDescriptors() {
    try {
      PropertyDescriptor _label = new PropertyDescriptor("label", beanClass, "getLabel", "setLabel");
      PropertyDescriptor _font = new PropertyDescriptor("font", beanClass, "getFont", "setFont");
      PropertyDescriptor _toolTipText = new PropertyDescriptor("toolTipText", beanClass, "getToolTipText", "setToolTipText");
      PropertyDescriptor[] pds = new PropertyDescriptor[] {
        _font,
        _label,
        _toolTipText};
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
