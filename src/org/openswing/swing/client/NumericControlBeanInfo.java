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

public class NumericControlBeanInfo extends SimpleBeanInfo {
  Class beanClass = NumericControl.class;
  String iconColor16x16Filename = "NumericControl16.png";
  String iconColor32x32Filename = "NumericControl.png";
  String iconMono16x16Filename = "NumericControl16.png";
  String iconMono32x32Filename = "NumericControl.png";

  public NumericControlBeanInfo() {
  }
  public PropertyDescriptor[] getPropertyDescriptors() {
    try {
      PropertyDescriptor _attributeName = new PropertyDescriptor("attributeName", beanClass, "getAttributeName", "setAttributeName");
      _attributeName.setPropertyEditorClass(NumericAttributeNameEditor.class);
      PropertyDescriptor _canCopy = new PropertyDescriptor("canCopy", beanClass, "isCanCopy", "setCanCopy");
      PropertyDescriptor _columns = new PropertyDescriptor("columns", beanClass, "getColumns", "setColumns");
      PropertyDescriptor _decimals = new PropertyDescriptor("decimals", beanClass, "getDecimals", "setDecimals");
      PropertyDescriptor _font = new PropertyDescriptor("font", beanClass, "getFont", "setFont");
      PropertyDescriptor _grouping = new PropertyDescriptor("grouping", beanClass, "isGrouping", "setGrouping");
      PropertyDescriptor _hideZeroDigits = new PropertyDescriptor("hideZeroDigits", beanClass, "isHideZeroDigits", "setHideZeroDigits");
      PropertyDescriptor _linkLabel = new PropertyDescriptor("linkLabel", beanClass, "getLinkLabel", "setLinkLabel");
      PropertyDescriptor _maxCharacters = new PropertyDescriptor("maxCharacters", beanClass, "getMaxCharacters", "setMaxCharacters");
      PropertyDescriptor _maxValue = new PropertyDescriptor("maxValue", beanClass, "getMaxValue", "setMaxValue");
      PropertyDescriptor _minValue = new PropertyDescriptor("minValue", beanClass, "getMinValue", "setMinValue");
      PropertyDescriptor _required = new PropertyDescriptor("required", beanClass, "isRequired", "setRequired");
      PropertyDescriptor _enabled = new PropertyDescriptor("enabled", beanClass, "isEnabled", "setEnabled");
      PropertyDescriptor _enabledOnInsert = new PropertyDescriptor("enabledOnInsert", beanClass, "isEnabledOnInsert", "setEnabledOnInsert");
      PropertyDescriptor _enabledOnEdit = new PropertyDescriptor("enabledOnEdit", beanClass, "isEnabledOnEdit", "setEnabledOnEdit");
      PropertyDescriptor _textAlignment = new PropertyDescriptor("textAlignment", beanClass, "getTextAlignment", "setTextAlignment");
      _textAlignment.setPropertyEditorClass(org.openswing.swing.table.columns.client.HeaderTextHorizontalAlignmentEditor.class);
      PropertyDescriptor _toolTipText = new PropertyDescriptor("toolTipText", beanClass, "getToolTipText", "setToolTipText");
      PropertyDescriptor[] pds = new PropertyDescriptor[] {
        _attributeName,
        _canCopy,
        _columns,
        _decimals,
        _font,
        _grouping,
        _hideZeroDigits,
        _linkLabel,
        _maxCharacters,
        _maxValue,
        _minValue,
        _required,
        _enabled,
        _enabledOnInsert,
        _enabledOnEdit,
        _textAlignment,
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
