package org.openswing.swing.client;

import java.beans.*;
import org.openswing.swing.table.client.*;
import java.awt.FlowLayout;


public class GridControlBeanInfo extends SimpleBeanInfo {
  Class beanClass = GridControl.class;
  String iconColor16x16Filename = "GridControl16.gif";
  String iconColor32x32Filename = "GridControl.gif";
  String iconMono16x16Filename = "GridControl16.gif";
  String iconMono32x32Filename = "GridControl.gif";

  public GridControlBeanInfo() {
  }
  public PropertyDescriptor[] getPropertyDescriptors() {
    try {
      PropertyDescriptor _autoLoadData = new PropertyDescriptor("autoLoadData", beanClass, "isAutoLoadData", "setAutoLoadData");
      PropertyDescriptor _copyButton = new PropertyDescriptor("copyButton", beanClass, "getCopyButton", "setCopyButton");
      PropertyDescriptor _deleteButton = new PropertyDescriptor("deleteButton", beanClass, "getDeleteButton", "setDeleteButton");
      PropertyDescriptor _editButton = new PropertyDescriptor("editButton", beanClass, "getEditButton", "setEditButton");
      PropertyDescriptor _gridDataLocator = new PropertyDescriptor("gridDataLocator", beanClass, "getGridDataLocator", "setGridDataLocator");
      PropertyDescriptor _insertButton = new PropertyDescriptor("insertButton", beanClass, "getInsertButton", "setInsertButton");
      PropertyDescriptor _exportButton = new PropertyDescriptor("exportButton", beanClass, "getExportButton", "setExportButton");
      PropertyDescriptor _lockedColumns = new PropertyDescriptor("lockedColumns", beanClass, "getLockedColumns", "setLockedColumns");
      PropertyDescriptor _mode = new PropertyDescriptor("mode", beanClass, "getMode", "setMode");
      PropertyDescriptor _navBar = new PropertyDescriptor("navBar", beanClass, "getNavBar", "setNavBar");
      PropertyDescriptor _functionId = new PropertyDescriptor("functionId", beanClass, "getFunctionId", "setFunctionId");
      PropertyDescriptor _maxNumberOfRowsOnInsert = new PropertyDescriptor("maxNumberOfRowsOnInsert", beanClass, "getMaxNumberOfRowsOnInsert", "setMaxNumberOfRowsOnInsert");
      PropertyDescriptor _maxSortedColumns = new PropertyDescriptor("maxSortedColumns", beanClass, "getMaxSortedColumns", "setMaxSortedColumns");
      PropertyDescriptor _orderWithLoadData = new PropertyDescriptor("orderWithLoadData", beanClass, "isOrderWithLoadData", "setOrderWithLoadData");
      PropertyDescriptor _preferredSize = new PropertyDescriptor("preferredSize", beanClass, "getPreferredSize", "setPreferredSize");
      PropertyDescriptor _reloadButton = new PropertyDescriptor("reloadButton", beanClass, "getReloadButton", "setReloadButton");
      PropertyDescriptor _reorderingAllowed = new PropertyDescriptor("reorderingAllowed", beanClass, "isReorderingAllowed", "setReorderingAllowed");
      PropertyDescriptor _resizingAllowed = new PropertyDescriptor("resizingAllowed", beanClass, "isResizingAllowed", "setResizingAllowed");
      PropertyDescriptor _rowHeight = new PropertyDescriptor("rowHeight", beanClass, "getRowHeight", "setRowHeight");
      PropertyDescriptor _rowHeightFixed = new PropertyDescriptor("rowHeightFixed", beanClass, "getRowHeightFixed", "setRowHeightFixed");
      PropertyDescriptor _saveButton = new PropertyDescriptor("saveButton", beanClass, "getSaveButton", "setSaveButton");
      PropertyDescriptor _selectionMode = new PropertyDescriptor("selectionMode", beanClass, "getSelectionMode", "setSelectionMode");
      PropertyDescriptor _valueObjectClassName = new PropertyDescriptor("valueObjectClassName", beanClass, "getValueObjectClassName", "setValueObjectClassName");
      PropertyDescriptor _visibleStatusPanel = new PropertyDescriptor("visibleStatusPanel", beanClass, "isVisibleStatusPanel", "setVisibleStatusPanel");
      PropertyDescriptor[] pds = new PropertyDescriptor[] {
        _autoLoadData,
        _copyButton,
        _deleteButton,
        _editButton,
        _exportButton,
        _functionId,
        _gridDataLocator,
        _lockedColumns,
        _maxNumberOfRowsOnInsert,
        _maxSortedColumns,
        _insertButton,
        _mode,
        _navBar,
        _orderWithLoadData,
        _preferredSize,
        _reloadButton,
        _reorderingAllowed,
        _resizingAllowed,
        _rowHeight,
        _rowHeightFixed,
        _saveButton,
        _selectionMode,
        _valueObjectClassName,
        _visibleStatusPanel};
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



  public BeanDescriptor getBeanDescriptor() {
    BeanDescriptor bd =
        new BeanDescriptor(GridControl.class);
    bd.setName("GridControl");
    bd.setValue("preferred",Boolean.TRUE);
    bd.setValue("containerDelegate","getColumnContainer");
    bd.setValue("isContainer",Boolean.TRUE);
    bd.setValue("layoutManager", FlowLayout.class);
    return bd;
  }




}
