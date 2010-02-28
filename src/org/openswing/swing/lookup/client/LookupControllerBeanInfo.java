package org.openswing.swing.lookup.client;

import java.beans.*;


/**
 * <p>Title: OpenSwing Framework</p>
 * @author Mauro Carniel
 * @version 1.0
 */
public class LookupControllerBeanInfo extends SimpleBeanInfo {
  Class beanClass = LookupController.class;
  String iconColor16x16Filename;
  String iconColor32x32Filename;
  String iconMono16x16Filename;
  String iconMono32x32Filename;

  public LookupControllerBeanInfo() {
  }
  public PropertyDescriptor[] getPropertyDescriptors() {
    try {
      PropertyDescriptor _allColumnPreferredWidth = new PropertyDescriptor("allColumnPreferredWidth", beanClass, "getAllColumnPreferredWidth", "setAllColumnPreferredWidth");
      PropertyDescriptor _allColumnsSelectable = new PropertyDescriptor("allColumnsSelectable", beanClass, "isAllColumnsSelectable", "setAllColumnsSelectable");
      PropertyDescriptor _allColumnVisible = new PropertyDescriptor("allColumnVisible", beanClass, "isAllColumnVisible", "setAllColumnVisible");
      PropertyDescriptor _allowTreeLeafSelectionOnly = new PropertyDescriptor("allowTreeLeafSelectionOnly", beanClass, "isAllowTreeLeafSelectionOnly", "setAllowTreeLeafSelectionOnly");
      PropertyDescriptor _anchorLastColumn = new PropertyDescriptor("anchorLastColumn", beanClass, "isAnchorLastColumn", "setAnchorLastColumn");
      PropertyDescriptor _codeSelectionWindow = new PropertyDescriptor("codeSelectionWindow", beanClass, "getCodeSelectionWindow", "setCodeSelectionWindow");
      _codeSelectionWindow.setPropertyEditorClass(SelectionWindowEditor.class);
      PropertyDescriptor _customPanel = new PropertyDescriptor("customPanel", beanClass, "getCustomPanel", "setCustomPanel");
      PropertyDescriptor _disableFrameClosing = new PropertyDescriptor("disableFrameClosing", beanClass, "isDisableFrameClosing", "setDisableFrameClosing");
      PropertyDescriptor _form = new PropertyDescriptor("form", beanClass, "getForm", "setForm");
      PropertyDescriptor _framePreferedSize = new PropertyDescriptor("framePreferedSize", beanClass, "getFramePreferedSize", "setFramePreferedSize");
      PropertyDescriptor _frameTitle = new PropertyDescriptor("frameTitle", beanClass, "getFrameTitle", "setFrameTitle");
      PropertyDescriptor _lookupDataLocator = new PropertyDescriptor("lookupDataLocator", beanClass, "getLookupDataLocator", "setLookupDataLocator");
      PropertyDescriptor _lookupGridController = new PropertyDescriptor("lookupGridController", beanClass, "getLookupGridController", "setLookupGridController");
      PropertyDescriptor _lookupValueObjectClassName = new PropertyDescriptor("lookupValueObjectClassName", beanClass, "getLookupValueObjectClassName", "setLookupValueObjectClassName");
      PropertyDescriptor _maxSortedColumns = new PropertyDescriptor("maxSortedColumns", beanClass, "getMaxSortedColumns", "setMaxSortedColumns");
      PropertyDescriptor _onInvalidCode = new PropertyDescriptor("onInvalidCode", beanClass, "getOnInvalidCode", "setOnInvalidCode");
      _onInvalidCode.setPropertyEditorClass(InvalidCodeEditor.class);
      PropertyDescriptor _showCustomErrorMessage = new PropertyDescriptor("showCustomErrorMessage", beanClass, "isShowCustomErrorMessage", "setShowCustomErrorMessage");
      PropertyDescriptor _showNavigatorBar = new PropertyDescriptor("showNavigatorBar", beanClass, "isShowNavigatorBar", "setShowNavigatorBar");

      PropertyDescriptor[] pds = new PropertyDescriptor[] {
        _allColumnPreferredWidth,
        _allColumnsSelectable,
        _allColumnVisible,
        _allowTreeLeafSelectionOnly,
        _anchorLastColumn,
        _codeSelectionWindow,
        _customPanel,
        _disableFrameClosing,
        _form,
        _framePreferedSize,
        _frameTitle,
        _lookupDataLocator,
        _lookupGridController,
        _lookupValueObjectClassName,
        _maxSortedColumns,
        _onInvalidCode,
        _showCustomErrorMessage,
        _showNavigatorBar
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
