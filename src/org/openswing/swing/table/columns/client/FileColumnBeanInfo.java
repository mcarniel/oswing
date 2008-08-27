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

public class FileColumnBeanInfo extends SimpleBeanInfo {
  Class beanClass = FileColumn.class;
  String iconColor16x16Filename = "ImageControl16.png";
  String iconColor32x32Filename = "ImageControl16.png";
  String iconMono16x16Filename = "ImageControl16.png";
  String iconMono32x32Filename = "ImageControl16.png";

  public FileColumnBeanInfo() {
  }
  public PropertyDescriptor[] getPropertyDescriptors() {
    try {
      PropertyDescriptor _columnName = new PropertyDescriptor("columnName", beanClass, "getColumnName", "setColumnName");
      _columnName.setPropertyEditorClass(org.openswing.swing.client.BytesAttributeNameEditor.class);
      PropertyDescriptor _fileFilter = new PropertyDescriptor("fileFilter", beanClass, "getFileFilter", "setFileFilter");
      PropertyDescriptor _fileNameAttributeName = new PropertyDescriptor("fileNameAttributeName", beanClass, "getFileNameAttributeName", "setFileNameAttributeName");
      PropertyDescriptor _showUploadButton = new PropertyDescriptor("showUploadButton", beanClass, "isShowUploadButton", "setShowUploadButton");
      PropertyDescriptor _showDownloadButton = new PropertyDescriptor("showDownloadButton", beanClass, "isShowDownloadButton", "setShowDownloadButton");
      PropertyDescriptor[] pds = new PropertyDescriptor[] {
        _columnName,
        _fileFilter,
        _fileNameAttributeName,
        _showDownloadButton,
        _showUploadButton
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
