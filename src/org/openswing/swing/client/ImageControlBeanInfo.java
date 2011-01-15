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

public class ImageControlBeanInfo extends SimpleBeanInfo {
  Class beanClass = ImageControl.class;
  String iconColor16x16Filename = "ImageControl16.png";
  String iconColor32x32Filename = "ImageControl.png";
  String iconMono16x16Filename = "ImageControl16.png";
  String iconMono32x32Filename = "ImageControl.png";

  public ImageControlBeanInfo() {
  }
  public PropertyDescriptor[] getPropertyDescriptors() {
    try {
      PropertyDescriptor _attributeName = new PropertyDescriptor("attributeName", beanClass, "getAttributeName", "setAttributeName");
      _attributeName.setPropertyEditorClass(BytesAttributeNameEditor.class);
      PropertyDescriptor _autoResize = new PropertyDescriptor("autoResize", beanClass, "isAutoResize", "setAutoResize");
      PropertyDescriptor _fileFilter = new PropertyDescriptor("fileFilter", beanClass, "getFileFilter", "setFileFilter");
      PropertyDescriptor _height = new PropertyDescriptor("imageHeight", beanClass, "getImageHeight", "setImageHeight");
      PropertyDescriptor _showButton = new PropertyDescriptor("showButton", beanClass, "isShowButton", "setShowButton");
      PropertyDescriptor _width = new PropertyDescriptor("imageWidth", beanClass, "getImageWidth", "setImageWidth");
      PropertyDescriptor _canCopy = new PropertyDescriptor("canCopy", beanClass, "isCanCopy", "setCanCopy");
      PropertyDescriptor _linkLabel = new PropertyDescriptor("linkLabel", beanClass, "getLinkLabel", "setLinkLabel");
      PropertyDescriptor _required = new PropertyDescriptor("required", beanClass, "isRequired", "setRequired");
      PropertyDescriptor _enabledOnInsert = new PropertyDescriptor("enabledOnInsert", beanClass, "isEnabledOnInsert", "setEnabledOnInsert");
      PropertyDescriptor _enabledOnEdit = new PropertyDescriptor("enabledOnEdit", beanClass, "isEnabledOnEdit", "setEnabledOnEdit");
      PropertyDescriptor _showPreview = new PropertyDescriptor("showPreview", beanClass, "isShowPreview", "setShowPreview");
      PropertyDescriptor _toolTipText = new PropertyDescriptor("toolTipText", beanClass, "getToolTipText", "setToolTipText");
      PropertyDescriptor[] pds = new PropertyDescriptor[] {
        _autoResize,
        _fileFilter,
        _height,
        _showButton,
        _width,
        _attributeName,
        _canCopy,
        _autoResize,
        _linkLabel,
        _required,
        _enabledOnInsert,
        _enabledOnEdit,
        _showPreview,
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
