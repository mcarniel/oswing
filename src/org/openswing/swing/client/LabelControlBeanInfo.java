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
  String iconColor16x16Filename = "LabelControl16.png";
  String iconColor32x32Filename = "LabelControl.png";
  String iconMono16x16Filename = "LabelControl16.png";
  String iconMono32x32Filename = "LabelControl.png";

  public LabelControlBeanInfo() {
  }
  public PropertyDescriptor[] getPropertyDescriptors() {
    try {
      PropertyDescriptor _label = new PropertyDescriptor("label", beanClass, "getLabel", "setLabel");
      PropertyDescriptor _font = new PropertyDescriptor("font", beanClass, "getFont", "setFont");
      PropertyDescriptor _toolTipText = new PropertyDescriptor("toolTipText", beanClass, "getToolTipText", "setToolTipText");
      PropertyDescriptor _textAlignment = new PropertyDescriptor("textAlignment", beanClass, "getTextAlignment", "setTextAlignment");
      PropertyDescriptor _text = new PropertyDescriptor("text", beanClass, "getText", "setText");
      _textAlignment.setPropertyEditorClass(org.openswing.swing.table.columns.client.HeaderTextHorizontalAlignmentEditor.class);
      PropertyDescriptor[] pds = new PropertyDescriptor[] {
        _font,
        _label,
        _text,
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
