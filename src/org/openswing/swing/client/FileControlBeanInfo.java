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

public class FileControlBeanInfo extends SimpleBeanInfo {
  Class beanClass = FileControl.class;
  String iconColor16x16Filename = "FileControl16.png";
  String iconColor32x32Filename = "FileControl.png";
  String iconMono16x16Filename = "FileControl16.png";
  String iconMono32x32Filename = "FileControl.png";

  public FileControlBeanInfo() {
  }
  public PropertyDescriptor[] getPropertyDescriptors() {
    try {
      PropertyDescriptor _attributeName = new PropertyDescriptor("attributeName", beanClass, "getAttributeName", "setAttributeName");
      _attributeName.setPropertyEditorClass(BytesAttributeNameEditor.class);
      PropertyDescriptor _canCopy = new PropertyDescriptor("canCopy", beanClass, "isCanCopy", "setCanCopy");
      PropertyDescriptor _showUploadButton = new PropertyDescriptor("showUploadButton", beanClass, "isShowUploadButton", "setShowUploadButton");
      PropertyDescriptor _showDownloadButton = new PropertyDescriptor("showDownloadButton", beanClass, "isShowDownloadButton", "setShowDownloadButton");
      PropertyDescriptor _linkLabel = new PropertyDescriptor("linkLabel", beanClass, "getLinkLabel", "setLinkLabel");
      PropertyDescriptor _fileNameAttributeName = new PropertyDescriptor("fileNameAttributeName", beanClass, "getFileNameAttributeName", "setFileNameAttributeName");
      _fileNameAttributeName.setPropertyEditorClass(StringAttributeNameEditor.class);
      PropertyDescriptor _required = new PropertyDescriptor("required", beanClass, "isRequired", "setRequired");
      PropertyDescriptor _enabledOnInsert = new PropertyDescriptor("enabledOnInsert", beanClass, "isEnabledOnInsert", "setEnabledOnInsert");
      PropertyDescriptor _enabledOnEdit = new PropertyDescriptor("enabledOnEdit", beanClass, "isEnabledOnEdit", "setEnabledOnEdit");
      PropertyDescriptor[] pds = new PropertyDescriptor[] {
        _attributeName,
        _canCopy,
        _showUploadButton,
        _showDownloadButton,
        _fileNameAttributeName,
        _linkLabel,
        _required,
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


