package org.openswing.swing.table.columns.client;

import java.beans.*;

/**
 * <p>Title: OpenSwing Framework</p>
 * <p>Description: B</p>
 * <p>Copyright: Copyright (C) 2008 Mauro Carniel</p>
 * <p> </p>
 * @author not attributable
 * @version 1.0
 */

public class LinkColumnBeanInfo extends SimpleBeanInfo {
  Class beanClass = LinkColumn.class;
  String iconColor16x16Filename = "ButtonColumn16.png";
  String iconColor32x32Filename = "ButtonColumn.png";
  String iconMono16x16Filename = "ButtonColumn16.png";
  String iconMono32x32Filename = "ButtonColumn.png";

  public LinkColumnBeanInfo() {
  }
  public PropertyDescriptor[] getPropertyDescriptors() {
    try {
      PropertyDescriptor _additionalHeaderColumnName = new PropertyDescriptor("additionalHeaderColumnName", beanClass, "getAdditionalHeaderColumnName", "setAdditionalHeaderColumnName");
      PropertyDescriptor _additionalHeaderColumnSpan = new PropertyDescriptor("additionalHeaderColumnSpan", beanClass, "getAdditionalHeaderColumnSpan", "setAdditionalHeaderColumnSpan");
      PropertyDescriptor _columnName = new PropertyDescriptor("columnName", beanClass, "getColumnName", "setColumnName");
      _columnName.setPropertyEditorClass(org.openswing.swing.client.AttributeNameEditor.class);
      PropertyDescriptor _uriAttributeName = new PropertyDescriptor("uriAttributeName", beanClass, "getUriAttributeName", "setUriAttributeName");
      _uriAttributeName.setPropertyEditorClass(org.openswing.swing.client.AttributeNameEditor.class);
      PropertyDescriptor _maxWidth = new PropertyDescriptor("maxWidth", beanClass, "getMaxWidth", "setMaxWidth");
      PropertyDescriptor _minWidth = new PropertyDescriptor("minWidth", beanClass, "getMinWidth", "setMinWidth");
      PropertyDescriptor _preferredWidth = new PropertyDescriptor("preferredWidth", beanClass, "getPreferredWidth", "setPreferredWidth");
      PropertyDescriptor _editableOnEdit = new PropertyDescriptor("editableOnEdit", beanClass, "isEditableOnEdit", "setEditableOnEdit");
      PropertyDescriptor _editableOnInsert = new PropertyDescriptor("editableOnInsert", beanClass, "isEditableOnInsert", "setEditableOnInsert");
      PropertyDescriptor _headerColumnName = new PropertyDescriptor("headerColumnName", beanClass, "getHeaderColumnName", "setHeaderColumnName");
      PropertyDescriptor[] pds = new PropertyDescriptor[] {
          _additionalHeaderColumnName,
          _additionalHeaderColumnSpan,
          _columnName,
          _editableOnEdit,
          _editableOnInsert,
          _headerColumnName,
          _maxWidth,
          _minWidth,
          _preferredWidth,
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
}
