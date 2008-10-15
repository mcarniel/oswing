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

public class ListControlBeanInfo extends SimpleBeanInfo {
  Class beanClass = ListControl.class;
  String iconColor16x16Filename = "ListControl16.png";
  String iconColor32x32Filename = "ListControl16.png";
  String iconMono16x16Filename = "ListControl16.png";
  String iconMono32x32Filename = "ListControl16.png";


  public ListControlBeanInfo() {
  }


  public PropertyDescriptor[] getPropertyDescriptors() {
    try {
      PropertyDescriptor _attributeName = new PropertyDescriptor("attributeName", beanClass, "getAttributeName", "setAttributeName");
      _attributeName.setPropertyEditorClass(AttributeNameEditor.class);
      PropertyDescriptor _canCopy = new PropertyDescriptor("canCopy", beanClass, "isCanCopy", "setCanCopy");
      PropertyDescriptor _domain = new PropertyDescriptor("domain", beanClass, "getDomain", "setDomain");
      PropertyDescriptor _domainId = new PropertyDescriptor("domainId", beanClass, "getDomainId", "setDomainId");
      PropertyDescriptor _enabled = new PropertyDescriptor("enabled", beanClass, "isEnabled", "setEnabled");
      PropertyDescriptor _fixedCellHeight = new PropertyDescriptor("fixedCellHeight", beanClass, "getFixedCellHeight", "setFixedCellHeight");
      PropertyDescriptor _fixedCellWidth = new PropertyDescriptor("fixedCellWidth", beanClass, "getFixedCellWidth", "setFixedCellWidth");
      PropertyDescriptor _font = new PropertyDescriptor("font", beanClass, "getFont", "setFont");
      PropertyDescriptor _linkLabel = new PropertyDescriptor("linkLabel", beanClass, "getLinkLabel", "setLinkLabel");
      PropertyDescriptor _layoutOrientation = new PropertyDescriptor("layoutOrientation", beanClass, "getLayoutOrientation", "setLayoutOrientation");
      PropertyDescriptor _nullAsDefaultValue = new PropertyDescriptor("nullAsDefaultValue", beanClass, "isNullAsDefaultValue", "setNullAsDefaultValue");
      PropertyDescriptor _required = new PropertyDescriptor("required", beanClass, "isRequired", "setRequired");
      PropertyDescriptor _enabledOnInsert = new PropertyDescriptor("enabledOnInsert", beanClass, "isEnabledOnInsert", "setEnabledOnInsert");
      PropertyDescriptor _enabledOnEdit = new PropertyDescriptor("enabledOnEdit", beanClass, "isEnabledOnEdit", "setEnabledOnEdit");
      PropertyDescriptor _toolTipText = new PropertyDescriptor("toolTipText", beanClass, "getToolTipText", "setToolTipText");
      PropertyDescriptor _selectionBackground = new PropertyDescriptor("selectionBackground", beanClass, "getSelectionBackground", "setSelectionBackground");
      PropertyDescriptor _selectionForeground = new PropertyDescriptor("selectionForeground", beanClass, "getSelectionForeground", "setSelectionForeground");
      PropertyDescriptor _selectionMode = new PropertyDescriptor("selectionMode", beanClass, "getSelectionMode", "setSelectionMode");
      PropertyDescriptor _translateItemDescriptions = new PropertyDescriptor("translateItemDescriptions", beanClass, "isTranslateItemDescriptions", "setTranslateItemDescriptions");
      PropertyDescriptor _valueIsAdjusting = new PropertyDescriptor("valueIsAdjusting", beanClass, "getValueIsAdjusting", "setValueIsAdjusting");
      PropertyDescriptor _visibleRowCount = new PropertyDescriptor("visibleRowCount", beanClass, "getVisibleRowCount", "setVisibleRowCount");
      PropertyDescriptor[] pds = new PropertyDescriptor[] {
        _attributeName,
        _canCopy,
        _domain,
        _domainId,
        _enabled,
        _enabledOnInsert,
        _enabledOnEdit,
        _fixedCellHeight,
        _fixedCellWidth,
        _font,
        _linkLabel,
        _layoutOrientation,
        _nullAsDefaultValue,
        _required,
        _selectionBackground,
        _selectionForeground,
        _selectionMode,
        _translateItemDescriptions,
        _valueIsAdjusting,
        _visibleRowCount,
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
