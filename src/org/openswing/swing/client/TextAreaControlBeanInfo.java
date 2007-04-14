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

public class TextAreaControlBeanInfo extends SimpleBeanInfo {
  Class beanClass = TextAreaControl.class;
  String iconColor16x16Filename = "TextAreaControl16.gif";
  String iconColor32x32Filename = "TextAreaControl.gif";
  String iconMono16x16Filename = "TextAreaControl16.gif";
  String iconMono32x32Filename = "TextAreaControl.gif";

  public TextAreaControlBeanInfo() {
  }
  public PropertyDescriptor[] getPropertyDescriptors() {
    try {
      PropertyDescriptor _attributeName = new PropertyDescriptor("attributeName", beanClass, "getAttributeName", "setAttributeName");
      _attributeName.setPropertyEditorClass(AttributeNameEditor.class);
      PropertyDescriptor _canCopy = new PropertyDescriptor("canCopy", beanClass, "isCanCopy", "setCanCopy");
      PropertyDescriptor _enabled = new PropertyDescriptor("enabled", beanClass, "isEnabled", "setEnabled");
      PropertyDescriptor _enabledOnEdit = new PropertyDescriptor("enabledOnEdit", beanClass, "isEnabledOnEdit", "setEnabledOnEdit");
      PropertyDescriptor _enabledOnInsert = new PropertyDescriptor("enabledOnInsert", beanClass, "isEnabledOnInsert", "setEnabledOnInsert");
      PropertyDescriptor _linkLabel = new PropertyDescriptor("linkLabel", beanClass, "getLinkLabel", "setLinkLabel");
      PropertyDescriptor _maxCharacters = new PropertyDescriptor("maxCharacters", beanClass, "getMaxCharacters", "setMaxCharacters");
      PropertyDescriptor _required = new PropertyDescriptor("required", beanClass, "isRequired", "setRequired");
      PropertyDescriptor[] pds = new PropertyDescriptor[] {
        _attributeName,
        _canCopy,
        _enabled,
        _enabledOnEdit,
        _enabledOnInsert,
        _linkLabel,
        _maxCharacters,
        _required};
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
