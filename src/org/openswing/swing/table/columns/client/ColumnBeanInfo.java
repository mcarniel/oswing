package org.openswing.swing.table.columns.client;

import java.beans.*;


/**
 * <p>Title: OpenSwing Framework</p>
 * <p>Description: Bean Info</p>
 * <p>Copyright: Copyright (C) 2006 Mauro Carniel</p>
 *
 * <p> This file is part of OpenSwing Framework.
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the (LGPL) Lesser General Public
 * License as published by the Free Software Foundation;
 *
 *                GNU LESSER GENERAL PUBLIC LICENSE
 *                 Version 2.1, February 1999
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Library General Public License for more details.
 *
 * You should have received a copy of the GNU Library General Public
 * License along with this library; if not, write to the Free
 * Software Foundation, Inc., 675 Mass Ave, Cambridge, MA 02139, USA.
 *
 *       The author may be contacted at:
 *           maurocarniel@tin.it</p>
 *
 * @author Mauro Carniel
 * @version 1.0
 */
public class ColumnBeanInfo extends SimpleBeanInfo {
  private Class beanClass = Column.class;
  private String iconColor16x16Filename;
  private String iconColor32x32Filename;
  private String iconMono16x16Filename;
  private String iconMono32x32Filename;

  public ColumnBeanInfo() {
  }
  public PropertyDescriptor[] getPropertyDescriptors() {
    try {
      PropertyDescriptor _additionalHeaderColumnName = new PropertyDescriptor("additionalHeaderColumnName", beanClass, "getAdditionalHeaderColumnName", "setAdditionalHeaderColumnName");
      PropertyDescriptor _additionalHeaderColumnSpan = new PropertyDescriptor("additionalHeaderColumnSpan", beanClass, "getAdditionalHeaderColumnSpan", "setAdditionalHeaderColumnSpan");
      PropertyDescriptor _autoFitColumn = new PropertyDescriptor("autoFitColumn", beanClass, "isAutoFitColumn", "setAutoFitColumn");
      PropertyDescriptor _columnDuplicable = new PropertyDescriptor("columnDuplicable", beanClass, "isColumnDuplicable", "setColumnDuplicable");
      PropertyDescriptor _columnFilterable = new PropertyDescriptor("columnFilterable", beanClass, "isColumnFilterable", "setColumnFilterable");
      PropertyDescriptor _columnRequired = new PropertyDescriptor("columnRequired", beanClass, "isColumnRequired", "setColumnRequired");
      PropertyDescriptor _columnSelectable = new PropertyDescriptor("columnSelectable", beanClass, "isColumnSelectable", "setColumnSelectable");
      PropertyDescriptor _columnSortable = new PropertyDescriptor("columnSortable", beanClass, "isColumnSortable", "setColumnSortable");
      PropertyDescriptor _columnVisibile = new PropertyDescriptor("columnVisible", beanClass, "isColumnVisible", "setColumnVisible");
      PropertyDescriptor _editableOnEdit = new PropertyDescriptor("editableOnEdit", beanClass, "isEditableOnEdit", "setEditableOnEdit");
      PropertyDescriptor _editableOnInsert = new PropertyDescriptor("editableOnInsert", beanClass, "isEditableOnInsert", "setEditableOnInsert");
      PropertyDescriptor _headerColumnName = new PropertyDescriptor("headerColumnName", beanClass, "getHeaderColumnName", "setHeaderColumnName");
      PropertyDescriptor _headerFont = new PropertyDescriptor("headerFont", beanClass, "getHeaderFont", "setHeaderFont");
      PropertyDescriptor _headerForegroundColor = new PropertyDescriptor("headerForegroundColor", beanClass, "getHeaderForegroundColor", "setHeaderForegroundColor");
      PropertyDescriptor _headerTextHorizontalAlignment = new PropertyDescriptor("headerTextHorizontalAlignment", beanClass, "getHeaderTextHorizontalAlignment", "setHeaderTextHorizontalAlignment");
      _headerTextHorizontalAlignment.setPropertyEditorClass(org.openswing.swing.table.columns.client.HeaderTextHorizontalAlignmentEditor.class);
      PropertyDescriptor _headerTextVerticalAlignment = new PropertyDescriptor("headerTextVerticalAlignment", beanClass, "getHeaderTextVerticalAlignment", "setHeaderTextVerticalAlignment");
      _headerTextVerticalAlignment.setPropertyEditorClass(org.openswing.swing.table.columns.client.HeaderTextVerticalAlignmentEditor.class);
      PropertyDescriptor _maxWidth = new PropertyDescriptor("maxWidth", beanClass, "getMaxWidth", "setMaxWidth");
      PropertyDescriptor _minWidth = new PropertyDescriptor("minWidth", beanClass, "getMinWidth", "setMinWidth");
      PropertyDescriptor _preferredWidth = new PropertyDescriptor("preferredWidth", beanClass, "getPreferredWidth", "setPreferredWidth");
      PropertyDescriptor _sortingOrder = new PropertyDescriptor("sortingOrder", beanClass, "getSortingOrder", "setSortingOrder");
      PropertyDescriptor _sortVersus = new PropertyDescriptor("sortVersus", beanClass, "getSortVersus", "setSortVersus");
      _sortVersus.setPropertyEditorClass(org.openswing.swing.table.columns.client.SortVersusEditor.class);
      PropertyDescriptor _textAlignment = new PropertyDescriptor("textAlignment", beanClass, "getTextAlignment", "setTextAlignment");
      _textAlignment.setPropertyEditorClass(org.openswing.swing.table.columns.client.HeaderTextHorizontalAlignmentEditor.class);

      PropertyDescriptor[] pds = new PropertyDescriptor[] {
              _additionalHeaderColumnName,
              _additionalHeaderColumnSpan,
              _autoFitColumn,
              _columnDuplicable,
	      _columnFilterable,
	      _columnRequired,
	      _columnSelectable,
	      _columnSortable,
	      _columnVisibile,
	      _editableOnEdit,
	      _editableOnInsert,
              _headerColumnName,
              _headerFont,
              _headerForegroundColor,
              _headerTextHorizontalAlignment,
              _headerTextVerticalAlignment,
	      _maxWidth,
	      _minWidth,
	      _preferredWidth,
              _sortingOrder,
	      _sortVersus,
              _textAlignment};
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
        new BeanDescriptor(Column.class);
    bd.setName("column");
    bd.setValue("isContainer",Boolean.FALSE);
    return bd;
  }

}
