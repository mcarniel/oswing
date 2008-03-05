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
      PropertyDescriptor _expandAllNodes = new PropertyDescriptor("expandAllNodes", beanClass, "isExpandAllNodes", "setExpandAllNodes");
      PropertyDescriptor _folderIconName = new PropertyDescriptor("folderIconName", beanClass, "getFolderIconName", "setFolderIconName");
      PropertyDescriptor _leavesImageName = new PropertyDescriptor("leavesImageName", beanClass, "getLeavesImageName", "setLeavesImageName");
      PropertyDescriptor _loadWhenVisibile = new PropertyDescriptor("loadWhenVisibile", beanClass, "isLoadWhenVisibile", "setLoadWhenVisibile");
      PropertyDescriptor _treeController = new PropertyDescriptor("treeController", beanClass, "getTreeController", "setTreeController");
      PropertyDescriptor _treeDataLocator = new PropertyDescriptor("treeDataLocator", beanClass, "getTreeDataLocator", "setTreeDataLocator");
      PropertyDescriptor[] pds = new PropertyDescriptor[] {
        _expandAllNodes,
        _folderIconName,
        _leavesImageName,
        _loadWhenVisibile,
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
