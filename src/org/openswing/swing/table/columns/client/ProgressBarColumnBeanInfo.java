package org.openswing.swing.table.columns.client;

import java.beans.*;

/**
 * <p>Title: OpenSwing Framework</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (C) 2006 Mauro Carniel</p>
 * <p> </p>
 * @author Mauro Carniel
 * @version 1.0
 */

public class ProgressBarColumnBeanInfo extends SimpleBeanInfo {
  Class beanClass = ProgressBarColumn.class;
  private String iconColor16x16Filename = "ProgressBarColumn16.png";
  private String iconColor32x32Filename = "ProgressBarColumn.png";
  private String iconMono16x16Filename = "ProgressBarColumn16.png";
  private String iconMono32x32Filename = "ProgressBarColumn.png";

  public ProgressBarColumnBeanInfo() {
  }
  public PropertyDescriptor[] getPropertyDescriptors() {
    try {
      PropertyDescriptor _columnName = new PropertyDescriptor("columnName", beanClass, "getColumnName", "setColumnName");
      _columnName.setPropertyEditorClass(org.openswing.swing.client.NumericAttributeNameEditor.class);
      PropertyDescriptor _color = new PropertyDescriptor("color", beanClass, "getColor", "setColor");
      PropertyDescriptor _maxValue = new PropertyDescriptor("maxValue", beanClass, "getMaxValue", "setMaxValue");
      PropertyDescriptor _minValue = new PropertyDescriptor("minValue", beanClass, "getMinValue", "setMinValue");
      PropertyDescriptor _showAllBands = new PropertyDescriptor("showAllBands", beanClass, "isShowAllBands", "setShowAllBands");
      PropertyDescriptor[] pds = new PropertyDescriptor[] {
        _color,
        _maxValue,
        _minValue,
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
