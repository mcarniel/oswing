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

public class DateControlBeanInfo extends SimpleBeanInfo {
  Class beanClass = DateControl.class;
  String iconColor16x16Filename = "DateControl16.png";
  String iconColor32x32Filename = "DateControl.png";
  String iconMono16x16Filename = "DateControl16.png";
  String iconMono32x32Filename = "DateControl.png";

  public DateControlBeanInfo() {
  }
  public PropertyDescriptor[] getPropertyDescriptors() {
    try {
      PropertyDescriptor _attributeName = new PropertyDescriptor("attributeName", beanClass, "getAttributeName", "setAttributeName");
      _attributeName.setPropertyEditorClass(DateAttributeNameEditor.class);
      PropertyDescriptor _canCopy = new PropertyDescriptor("canCopy", beanClass, "isCanCopy", "setCanCopy");
      PropertyDescriptor _font = new PropertyDescriptor("font", beanClass, "getFont", "setFont");
      PropertyDescriptor _linkLabel = new PropertyDescriptor("linkLabel", beanClass, "getLinkLabel", "setLinkLabel");
      PropertyDescriptor _required = new PropertyDescriptor("required", beanClass, "isRequired", "setRequired");
      PropertyDescriptor _showCalendarButton = new PropertyDescriptor("showCalendarButton", beanClass, "isShowCalendarButton", "setShowCalendarButton");
      PropertyDescriptor _showCentury = new PropertyDescriptor("showCentury", beanClass, "isShowCentury", "setShowCentury");
      PropertyDescriptor _enabled = new PropertyDescriptor("enabled", beanClass, "isEnabled", "setEnabled");
      PropertyDescriptor _enabledOnInsert = new PropertyDescriptor("enabledOnInsert", beanClass, "isEnabledOnInsert", "setEnabledOnInsert");
      PropertyDescriptor _enabledOnEdit = new PropertyDescriptor("enabledOnEdit", beanClass, "isEnabledOnEdit", "setEnabledOnEdit");
      PropertyDescriptor _strictUsage = new PropertyDescriptor("strictUsage", beanClass, "isStrictUsage", "setStrictUsage");
      PropertyDescriptor _textAlignment = new PropertyDescriptor("textAlignment", beanClass, "getTextAlignment", "setTextAlignment");
      _textAlignment.setPropertyEditorClass(org.openswing.swing.table.columns.client.HeaderTextHorizontalAlignmentEditor.class);
      PropertyDescriptor _toolTipText = new PropertyDescriptor("toolTipText", beanClass, "getToolTipText", "setToolTipText");
      PropertyDescriptor[] pds = new PropertyDescriptor[] {
        _attributeName,
        _canCopy,
        _linkLabel,
        _required,
        _showCalendarButton,
        _showCentury,
        _enabled,
        _enabledOnInsert,
        _enabledOnEdit,
        _font,
        _strictUsage,
        _textAlignment,
        _toolTipText,
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
}
