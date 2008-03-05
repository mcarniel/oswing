package org.openswing.swing.gantt.client;

import java.beans.*;

/**
 * <p>Title: OpenSwing Framework</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (C) 2006 Mauro Carniel</p>
 * <p> </p>
 * @author not attributable
 * @version 1.0
 */

public class GanttControlBeanInfo extends SimpleBeanInfo {
  Class beanClass = GanttControl.class;
  String iconColor16x16Filename = "GanttControl16.png";
  String iconColor32x32Filename = "GanttControl.png";
  String iconMono16x16Filename = "GanttControl16.png";
  String iconMono32x32Filename = "GanttControl.png";

  public GanttControlBeanInfo() {
  }
  public PropertyDescriptor[] getPropertyDescriptors() {
    try {
      PropertyDescriptor _autoLoadData = new PropertyDescriptor("autoLoadData", beanClass, "isAutoLoadData", "setAutoLoadData");
      PropertyDescriptor _columnWidth = new PropertyDescriptor("columnWidth", beanClass, "getColumnWidth", "setColumnWidth");
      PropertyDescriptor _dividerLocation = new PropertyDescriptor("dividerLocation", beanClass, "getDividerLocation", "setDividerLocation");
      PropertyDescriptor _enableDelete = new PropertyDescriptor("enableDelete", beanClass, "isEnableDelete", "setEnableDelete");
      PropertyDescriptor _enableEdit = new PropertyDescriptor("enableEdit", beanClass, "isEnableEdit", "setEnableEdit");
      PropertyDescriptor _enableInsert = new PropertyDescriptor("enableInsert", beanClass, "isEnableInsert", "setEnableInsert");
      PropertyDescriptor _ganttDataLocator = new PropertyDescriptor("ganttDataLocator", beanClass, "getGanttDataLocator", "setGanttDataLocator");
      PropertyDescriptor _ganttEnabled = new PropertyDescriptor("ganttEnabled", beanClass, "isGanttEnabled", "setGanttEnabled");
      PropertyDescriptor _gridEnabled = new PropertyDescriptor("gridEnabled", beanClass, "isGridEnabled", "setGridEnabled");
      PropertyDescriptor _rowHeight = new PropertyDescriptor("rowHeight", beanClass, "getRowHeight", "setRowHeight");
      PropertyDescriptor _showDescription = new PropertyDescriptor("showDescription", beanClass, "isShowDescription", "setShowDescription");
      PropertyDescriptor _showTime = new PropertyDescriptor("showTime", beanClass, "isShowTime", "setShowTime");
      PropertyDescriptor[] pds = new PropertyDescriptor[] {
        _autoLoadData,
        _columnWidth,
        _dividerLocation,
        _enableDelete,
        _enableEdit,
        _enableInsert,
        _ganttDataLocator,
        _ganttEnabled,
        _gridEnabled,
        _rowHeight,
        _showDescription,
        _showTime};
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
