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

public class LinkButtonBeanInfo extends SimpleBeanInfo {
  Class beanClass = LinkButton.class;
  String iconColor16x16Filename = "GenericButton16.png";
  String iconColor32x32Filename = "GenericButton.png";
  String iconMono16x16Filename = "GenericButton16.png";
  String iconMono32x32Filename = "GenericButton.png";


  public LinkButtonBeanInfo() {
  }
  public PropertyDescriptor[] getPropertyDescriptors() {
    try {
      PropertyDescriptor _labelAttributeName = new PropertyDescriptor("labelAttributeName", beanClass, "getLabelAttributeName", "setLabelAttributeName");
      _labelAttributeName.setPropertyEditorClass(StringAttributeNameEditor.class);

      PropertyDescriptor _tooltipAttributeName = new PropertyDescriptor("tooltipAttributeName", beanClass, "getTooltipAttributeName", "setTooltipAttributeName");
      _tooltipAttributeName.setPropertyEditorClass(StringAttributeNameEditor.class);

      PropertyDescriptor _uriAttributeName = new PropertyDescriptor("uriAttributeName", beanClass, "getUriAttributeName", "setUriAttributeName");
      _uriAttributeName.setPropertyEditorClass(StringAttributeNameEditor.class);

      PropertyDescriptor _uri = new PropertyDescriptor("uri", beanClass, "getUri", "setUri");
      PropertyDescriptor _font = new PropertyDescriptor("font", beanClass, "getFont", "setFont");
      PropertyDescriptor _foregroundColorWhenEntered = new PropertyDescriptor("foregroundColorWhenEntered", beanClass, "getForegroundColorWhenEntered", "setForegroundColorWhenEntered");
      PropertyDescriptor _label = new PropertyDescriptor("label", beanClass, "getLabel", "setLabel");
      PropertyDescriptor _showUnderline = new PropertyDescriptor("showUnderline", beanClass, "isShowUnderline", "setShowUnderline");
      PropertyDescriptor _textAlignment = new PropertyDescriptor("textAlignment", beanClass, "getTextAlignment", "setTextAlignment");
      PropertyDescriptor _toolTipText = new PropertyDescriptor("toolTipText", beanClass, "getToolTipText", "setToolTipText");
      PropertyDescriptor[] pds = new PropertyDescriptor[] {
        _font,
        _foregroundColorWhenEntered,
        _label,
        _labelAttributeName,
        _showUnderline,
        _textAlignment,
        _toolTipText,
        _tooltipAttributeName,
        _uri,
        _uriAttributeName
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
