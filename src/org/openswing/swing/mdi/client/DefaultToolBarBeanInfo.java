package org.openswing.swing.mdi.client;

import java.beans.*;

/**
 * <p>Title: OpenSwing Framework</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (C) 2008 Mauro Carniel</p>
 * <p> </p>
 * @author not attributable
 * @version 1.0
 */

public class DefaultToolBarBeanInfo extends SimpleBeanInfo {
  Class beanClass = DefaultToolBar.class;
  String iconColor16x16Filename;
  String iconColor32x32Filename;
  String iconMono16x16Filename;
  String iconMono32x32Filename;

  public DefaultToolBarBeanInfo() {
  }
  public PropertyDescriptor[] getPropertyDescriptors() {
    try {
      PropertyDescriptor _copyButton = new PropertyDescriptor("copyButton", beanClass, "isCopyButton", "setCopyButton");
      PropertyDescriptor _deleteButton = new PropertyDescriptor("deleteButton", beanClass, "isDeleteButton", "setDeleteButton");
      PropertyDescriptor _editButton = new PropertyDescriptor("editButton", beanClass, "isEditButton", "setEditButton");
      PropertyDescriptor _exportButton = new PropertyDescriptor("exportButton", beanClass, "isExportButton", "setExportButton");
      PropertyDescriptor _filterButton = new PropertyDescriptor("filterButton", beanClass, "isFilterButton", "setFilterButton");
      PropertyDescriptor _importButton = new PropertyDescriptor("importButton", beanClass, "isImportButton", "setImportButton");
      PropertyDescriptor _insertButton = new PropertyDescriptor("insertButton", beanClass, "isInsertButton", null);
      PropertyDescriptor _navigatorBar = new PropertyDescriptor("navigatorBar", beanClass, "isNavigatorBar", "setNavigatorBar");
      PropertyDescriptor _reloadButton = new PropertyDescriptor("reloadButton", beanClass, "isReloadButton", null);
      PropertyDescriptor _saveButton = new PropertyDescriptor("saveButton", beanClass, "isSaveButton", null);
      PropertyDescriptor[] pds = new PropertyDescriptor[] {
        _copyButton,
        _deleteButton,
        _editButton,
        _exportButton,
        _filterButton,
        _importButton,
        _insertButton,
        _navigatorBar,
        _reloadButton,
        _saveButton};
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