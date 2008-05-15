package org.openswing.swing.table.columns.client;

import java.beans.*;

import org.openswing.swing.client.*;

/**
 * <p>Title: Benetton - Gestione Imballi</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2005 Benetton spa</p>
 * <p>Company: Tecnoinformatica spa</p>
 * @author Mauro Carniel
 * @version 1.0
 */

public class CheckBoxColumnBeanInfo extends SimpleBeanInfo {
  private Class beanClass = CheckBoxColumn.class;
  private String iconColor16x16Filename = "CheckBoxColumn16.png";
  private String iconColor32x32Filename = "CheckBoxColumn.png";
  private String iconMono16x16Filename = "CheckBoxColumn16.png";
  private String iconMono32x32Filename = "CheckBoxColumn.png";

  public CheckBoxColumnBeanInfo() {
  }
  public PropertyDescriptor[] getPropertyDescriptors() {
    try {
      PropertyDescriptor _allowNullValue = new PropertyDescriptor("allowNullValue", beanClass, "isAllowNullValue", "setAllowNullValue");
      PropertyDescriptor _columnName = new PropertyDescriptor("columnName", beanClass, "getColumnName", "setColumnName");
      _columnName.setPropertyEditorClass(BooleanAttributeNameEditor.class);
      PropertyDescriptor _showDeSelectAllInPopupMenu = new PropertyDescriptor("showDeSelectAllInPopupMenu", beanClass, "isShowDeSelectAllInPopupMenu", "setShowDeSelectAllInPopupMenu");
      PropertyDescriptor _deSelectAllCells = new PropertyDescriptor("deSelectAllCells", beanClass, "isDeSelectAllCells", "setDeSelectAllCells");
      PropertyDescriptor _enableInReadOnlyMode = new PropertyDescriptor("enableInReadOnlyMode", beanClass, "isEnableInReadOnlyMode", "setEnableInReadOnlyMode");
      PropertyDescriptor _negativeValue = new PropertyDescriptor("negativeValue", beanClass, "getNegativeValue", "setNegativeValue");
      PropertyDescriptor _positiveValue = new PropertyDescriptor("positiveValue", beanClass, "getPositiveValue", "setPositiveValue");
      PropertyDescriptor[] pds = new PropertyDescriptor[] {
              _allowNullValue,
              _columnName,
              _deSelectAllCells,
              _enableInReadOnlyMode,
              _negativeValue,
              _positiveValue,
              _showDeSelectAllInPopupMenu
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
