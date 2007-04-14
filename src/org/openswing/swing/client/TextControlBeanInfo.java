package org.openswing.swing.client;

import java.beans.*;
import javax.swing.beaninfo.SwingBeanInfo;

/**
 * <p>Title: OpenSwing Framework</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (C) 2006 Mauro Carniel</p>
 * <p> </p>
 * @author Mauro Carniel
 * @version 1.0
 */

public class TextControlBeanInfo extends SimpleBeanInfo {
  Class beanClass = TextControl.class;
  String iconColor16x16Filename = "TextControl16.gif";
  String iconColor32x32Filename = "TextControl.gif";
  String iconMono16x16Filename = "TextControl16.gif";
  String iconMono32x32Filename = "TextControl.gif";

  public TextControlBeanInfo() {
  }
  public PropertyDescriptor[] getPropertyDescriptors() {
    try {
      PropertyDescriptor _attributeName = new PropertyDescriptor("attributeName", beanClass, "getAttributeName", "setAttributeName");
      _attributeName.setPropertyEditorClass(AttributeNameEditor.class);
      PropertyDescriptor _canCopy = new PropertyDescriptor("canCopy", beanClass, "isCanCopy", "setCanCopy");
      PropertyDescriptor _columns = new PropertyDescriptor("columns", beanClass, "getColumns", "setColumns");
      PropertyDescriptor _linkLabel = new PropertyDescriptor("linkLabel", beanClass, "getLinkLabel", "setLinkLabel");
      PropertyDescriptor _maxCharacters = new PropertyDescriptor("maxCharacters", beanClass, "getMaxCharacters", "setMaxCharacters");
      PropertyDescriptor _required = new PropertyDescriptor("required", beanClass, "isRequired", "setRequired");
      PropertyDescriptor _rpadding = new PropertyDescriptor("rpadding", beanClass, "isRpadding", "setRpadding");
      PropertyDescriptor _trimText = new PropertyDescriptor("trimText", beanClass, "isTrimText", "setTrimText");
      PropertyDescriptor _upperCase = new PropertyDescriptor("upperCase", beanClass, "isUpperCase", "setUpperCase");
      PropertyDescriptor _enabled = new PropertyDescriptor("enabled", beanClass, "isEnabled", "setEnabled");
      PropertyDescriptor _enabledOnInsert = new PropertyDescriptor("enabledOnInsert", beanClass, "isEnabledOnInsert", "setEnabledOnInsert");
      PropertyDescriptor _enabledOnEdit = new PropertyDescriptor("enabledOnEdit", beanClass, "isEnabledOnEdit", "setEnabledOnEdit");
      PropertyDescriptor[] pds = new PropertyDescriptor[] {
        _attributeName,
        _canCopy,
        _columns,
        _linkLabel,
        _maxCharacters,
        _required,
        _rpadding,
        _trimText,
        _upperCase,
        _enabled,
        _enabledOnInsert,
        _enabledOnEdit};

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
