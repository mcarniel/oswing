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

public class CodLookupControlBeanInfo extends SimpleBeanInfo {
  Class beanClass = CodLookupControl.class;
  String iconColor16x16Filename = "CodLookupControl16.png";
  String iconColor32x32Filename = "CodLookupControl.png";
  String iconMono16x16Filename = "CodLookupControl16.png";
  String iconMono32x32Filename = "CodLookupControl.png";

  public CodLookupControlBeanInfo() {
  }
  public PropertyDescriptor[] getPropertyDescriptors() {
    try {
      PropertyDescriptor _allowOnlyNumbers = new PropertyDescriptor("allowOnlyNumbers", beanClass, "isAllowOnlyNumbers", "setAllowOnlyNumbers");
      PropertyDescriptor _attributeName = new PropertyDescriptor("attributeName", beanClass, "getAttributeName", "setAttributeName");
      _attributeName.setPropertyEditorClass(AttributeNameEditor.class);
      PropertyDescriptor _canCopy = new PropertyDescriptor("canCopy", beanClass, "isCanCopy", "setCanCopy");
      PropertyDescriptor _codBoxVisible = new PropertyDescriptor("codBoxVisible", beanClass, "isCodBoxVisible", "setCodBoxVisible");
      PropertyDescriptor _columns = new PropertyDescriptor("columns", beanClass, "getColumns", "setColumns");
      PropertyDescriptor _controllerClassName = new PropertyDescriptor("controllerClassName", beanClass, "getControllerClassName", "setControllerClassName");
      PropertyDescriptor _controllerMethodName = new PropertyDescriptor("controllerMethodName", beanClass, "getControllerMethodName", "setControllerMethodName");
      PropertyDescriptor _font = new PropertyDescriptor("font", beanClass, "getFont", "setFont");
      PropertyDescriptor _autoCompletitionWaitTime = new PropertyDescriptor("autoCompletitionWaitTime", beanClass, "getAutoCompletitionWaitTime", "setAutoCompletitionWaitTime");
      PropertyDescriptor _enabled = new PropertyDescriptor("enabled", beanClass, "isEnabled", "setEnabled");
      PropertyDescriptor _enableCodBox = new PropertyDescriptor("enableCodBox", beanClass, "isEnableCodBox", "setEnableCodBox");
      PropertyDescriptor _enabledOnEdit = new PropertyDescriptor("enabledOnEdit", beanClass, "isEnabledOnEdit", "setEnabledOnEdit");
      PropertyDescriptor _enabledOnInsert = new PropertyDescriptor("enabledOnInsert", beanClass, "isEnabledOnInsert", "setEnabledOnInsert");
      PropertyDescriptor _linkLabel = new PropertyDescriptor("linkLabel", beanClass, "getLinkLabel", "setLinkLabel");
      PropertyDescriptor _lookupButtonVisible = new PropertyDescriptor("lookupButtonVisible", beanClass, "isLookupButtonVisible", "setLookupButtonVisible");
      PropertyDescriptor _lookupController = new PropertyDescriptor("lookupController", beanClass, "getLookupController", "setLookupController");
      PropertyDescriptor _maxCharacters = new PropertyDescriptor("maxCharacters", beanClass, "getMaxCharacters", "setMaxCharacters");
      PropertyDescriptor _required = new PropertyDescriptor("required", beanClass, "isRequired", "setRequired");
      PropertyDescriptor _textAlignment = new PropertyDescriptor("textAlignment", beanClass, "getTextAlignment", "setTextAlignment");
      _textAlignment.setPropertyEditorClass(org.openswing.swing.table.columns.client.HeaderTextHorizontalAlignmentEditor.class);
      PropertyDescriptor _toolTipText = new PropertyDescriptor("toolTipText", beanClass, "getToolTipText", "setToolTipText");
      PropertyDescriptor[] pds = new PropertyDescriptor[] {
        _allowOnlyNumbers,
        _attributeName,
        _autoCompletitionWaitTime,
        _canCopy,
        _codBoxVisible,
        _columns,
        _controllerClassName,
        _controllerMethodName,
        _enabled,
        _enableCodBox,
        _enabledOnEdit,
        _enabledOnInsert,
        _font,
        _linkLabel,
        _lookupButtonVisible,
        _lookupController,
        _maxCharacters,
        _required,
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
