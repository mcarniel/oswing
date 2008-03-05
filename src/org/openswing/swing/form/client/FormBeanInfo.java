package org.openswing.swing.form.client;

import java.beans.*;

/**
 * <p>Title: OpenSwing Framework</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (C) 2006 Mauro Carniel</p>
 * <p> </p>
 * @author not attributable
 * @version 1.0
 */

public class FormBeanInfo extends SimpleBeanInfo {
  Class beanClass = Form.class;
  String iconColor16x16Filename = "Form16.png";
  String iconColor32x32Filename = "Form.png";
  String iconMono16x16Filename = "Form16.png";
  String iconMono32x32Filename = "Form.png";

  public FormBeanInfo() {
  }
  public PropertyDescriptor[] getPropertyDescriptors() {
    try {
      PropertyDescriptor _copyButton = new PropertyDescriptor("copyButton", beanClass, "getCopyButton", "setCopyButton");
      PropertyDescriptor _deleteButton = new PropertyDescriptor("deleteButton", beanClass, "getDeleteButton", "setDeleteButton");
      PropertyDescriptor _editButton = new PropertyDescriptor("editButton", beanClass, "getEditButton", "setEditButton");
      PropertyDescriptor _formController = new PropertyDescriptor("formController", beanClass, "getFormController", "setFormController");
      PropertyDescriptor _functionId = new PropertyDescriptor("functionId", beanClass, "getFunctionId", "setFunctionId");
      PropertyDescriptor _insertButton = new PropertyDescriptor("insertButton", beanClass, "getInsertButton", "setInsertButton");
      PropertyDescriptor _reloadButton = new PropertyDescriptor("reloadButton", beanClass, "getReloadButton", "setReloadButton");
      PropertyDescriptor _saveButton = new PropertyDescriptor("saveButton", beanClass, "getSaveButton", "setSaveButton");
      PropertyDescriptor _VOClassName = new PropertyDescriptor("VOClassName", beanClass, "getVOClassName", "setVOClassName");
      PropertyDescriptor[] pds = new PropertyDescriptor[] {
        _copyButton,
        _deleteButton,
        _editButton,
        _formController,
        _functionId,
        _insertButton,
        _reloadButton,
        _saveButton,
        _VOClassName};
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
