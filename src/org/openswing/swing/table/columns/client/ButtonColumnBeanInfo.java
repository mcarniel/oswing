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

public class ButtonColumnBeanInfo extends SimpleBeanInfo {
  Class beanClass = ButtonColumn.class;
  String iconColor16x16Filename = "ButtonColumn16.png";
  String iconColor32x32Filename = "ButtonColumn.png";
  String iconMono16x16Filename = "ButtonColumn16.png";
  String iconMono32x32Filename = "ButtonColumn.png";

  public ButtonColumnBeanInfo() {
  }
  public PropertyDescriptor[] getPropertyDescriptors() {
    try {
      PropertyDescriptor _additionalHeaderColumnName = new PropertyDescriptor("additionalHeaderColumnName", beanClass, "getAdditionalHeaderColumnName", "setAdditionalHeaderColumnName");
      PropertyDescriptor _additionalHeaderColumnSpan = new PropertyDescriptor("additionalHeaderColumnSpan", beanClass, "getAdditionalHeaderColumnSpan", "setAdditionalHeaderColumnSpan");
      PropertyDescriptor _border = new PropertyDescriptor("border", beanClass, "getBorder", "setBorder");
      PropertyDescriptor _icon = new PropertyDescriptor("icon", beanClass, "getIcon", "setIcon");
      PropertyDescriptor _iconName = new PropertyDescriptor("iconName", beanClass, "getIconName", "setIconName");
      PropertyDescriptor _text = new PropertyDescriptor("text", beanClass, "getText", "setText");
      PropertyDescriptor _columnName = new PropertyDescriptor("columnName", beanClass, "getColumnName", "setColumnName");
      _columnName.setPropertyEditorClass(org.openswing.swing.client.AttributeNameEditor.class);
      PropertyDescriptor _enableInReadOnlyMode = new PropertyDescriptor("enableInReadOnlyMode", beanClass, "isEnableInReadOnlyMode", "setEnableInReadOnlyMode");
      PropertyDescriptor _maxWidth = new PropertyDescriptor("maxWidth", beanClass, "getMaxWidth", "setMaxWidth");
      PropertyDescriptor _minWidth = new PropertyDescriptor("minWidth", beanClass, "getMinWidth", "setMinWidth");
      PropertyDescriptor _preferredWidth = new PropertyDescriptor("preferredWidth", beanClass, "getPreferredWidth", "setPreferredWidth");
      PropertyDescriptor _editableOnEdit = new PropertyDescriptor("editableOnEdit", beanClass, "isEditableOnEdit", "setEditableOnEdit");
      PropertyDescriptor _editableOnInsert = new PropertyDescriptor("editableOnInsert", beanClass, "isEditableOnInsert", "setEditableOnInsert");
      PropertyDescriptor _headerColumnName = new PropertyDescriptor("headerColumnName", beanClass, "getHeaderColumnName", "setHeaderColumnName");
      PropertyDescriptor[] pds = new PropertyDescriptor[] {
          _additionalHeaderColumnName,
          _additionalHeaderColumnSpan,
          _border,
          _columnName,
          _editableOnEdit,
          _editableOnInsert,
          _enableInReadOnlyMode,
          _headerColumnName,
          _icon,
          _iconName,
          _maxWidth,
          _minWidth,
          _preferredWidth,
        _text
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
