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

public class ProgressBarControlBeanInfo extends SimpleBeanInfo {
  Class beanClass = ProgressBarControl.class;
  String iconColor16x16Filename = "ProgressBarControl16.png";
  String iconColor32x32Filename = "ProgressBarControl.png";
  String iconMono16x16Filename = "ProgressBarControl16.png";
  String iconMono32x32Filename = "ProgressBarControl.png";


  public ProgressBarControlBeanInfo() {
  }
  public PropertyDescriptor[] getPropertyDescriptors() {
    try {
      PropertyDescriptor _color = new PropertyDescriptor("color", beanClass, "getColor", "setColor");
      PropertyDescriptor _maxValue = new PropertyDescriptor("maxValue", beanClass, "getMaxValue", "setMaxValue");
      PropertyDescriptor _minValue = new PropertyDescriptor("minValue", beanClass, "getMinValue", "setMinValue");
      PropertyDescriptor _showAllBands = new PropertyDescriptor("showAllBands", beanClass, "isShowAllBands", "setShowAllBands");
      PropertyDescriptor _attributeName = new PropertyDescriptor("attributeName", beanClass, "getAttributeName", "setAttributeName");
      _attributeName.setPropertyEditorClass(NumericAttributeNameEditor.class);
      PropertyDescriptor _canCopy = new PropertyDescriptor("canCopy", beanClass, "isCanCopy", "setCanCopy");
      PropertyDescriptor _toolTipText = new PropertyDescriptor("toolTipText", beanClass, "getToolTipText", "setToolTipText");
      PropertyDescriptor[] pds = new PropertyDescriptor[] {
        _attributeName,
        _color,
        _maxValue,
        _minValue,
        _showAllBands,
        _canCopy,
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
