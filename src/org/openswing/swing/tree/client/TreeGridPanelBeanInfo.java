package org.openswing.swing.tree.client;

import java.beans.*;

/**
 * <p>Title: OpenSwing Framework</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (C) 2006 Mauro Carniel</p>
 * <p> </p>
 * @author not attributable
 * @version 1.0
 */

public class TreeGridPanelBeanInfo extends SimpleBeanInfo {
  Class beanClass = TreeGridPanel.class;
  String iconColor16x16Filename = "TreeGridPanel16.png";
  String iconColor32x32Filename = "TreeGridPanel.png";
  String iconMono16x16Filename = "TreeGridPanel16.png";
  String iconMono32x32Filename = "TreeGridPanel.png";

  public TreeGridPanelBeanInfo() {
  }
  public PropertyDescriptor[] getPropertyDescriptors() {
    try {
      PropertyDescriptor _background = new PropertyDescriptor("background", beanClass, "getBackground", "setBackground");
      PropertyDescriptor _expandAllNodes = new PropertyDescriptor("expandAllNodes", beanClass, "isExpandAllNodes", "setExpandAllNodes");
      PropertyDescriptor _expandRoot = new PropertyDescriptor("expandRoot", beanClass, "isExpandRoot", "setExpandRoot");
      PropertyDescriptor _folderIconName = new PropertyDescriptor("folderIconName", beanClass, "getFolderIconName", "setFolderIconName");
      PropertyDescriptor _iconAttributeName = new PropertyDescriptor("iconAttributeName", beanClass, "getIconAttributeName", "setIconAttributeName");
      PropertyDescriptor _leavesImageName = new PropertyDescriptor("leavesImageName", beanClass, "getLeavesImageName", "setLeavesImageName");
      PropertyDescriptor _loadWhenVisibile = new PropertyDescriptor("loadWhenVisibile", beanClass, "isLoadWhenVisibile", "setLoadWhenVisibile");
      PropertyDescriptor _rootVisible = new PropertyDescriptor("rootVisible", beanClass, "isRootVisible", "setRootVisible");
      PropertyDescriptor _treeController = new PropertyDescriptor("treeController", beanClass, "getTreeController", "setTreeController");
      PropertyDescriptor _treeDataLocator = new PropertyDescriptor("treeDataLocator", beanClass, "getTreeDataLocator", "setTreeDataLocator");
      PropertyDescriptor[] pds = new PropertyDescriptor[] {
        _background,
        _expandAllNodes,
        _expandRoot,
        _folderIconName,
        _iconAttributeName,
        _leavesImageName,
        _loadWhenVisibile,
        _rootVisible,
        _treeController,
        _treeDataLocator};
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
