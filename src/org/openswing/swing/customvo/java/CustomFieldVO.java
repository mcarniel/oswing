package org.openswing.swing.customvo.java;

import org.openswing.swing.message.receive.java.*;
import org.openswing.swing.util.java.Consts;



/**
 * <p>Title: OpenSwing Framework</p>
 * <p>Description: Value object used to define properties for attributes of BaseValueObject class.</p>
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
public class CustomFieldVO extends ValueObjectImpl {

  /** text type column */
  public static final int TYPE_TEXT = 0;

  /** numeric type column */
  public static final int TYPE_NUM = 1;

  /** date type column */
  public static final int TYPE_DATE = 2;

  /** date+time column */
  public static final int TYPE_DATE_TIME = 3;

  /** time column */
  public static final int TYPE_TIME = 4;

  /** char type column */
  public static final int TYPE_CHAR = 5;

  private String constraintValues;
  private String attributeName;
  private boolean visible = true;
  private String defaultValueText;
  private java.math.BigDecimal defaultValueNum;
  private String fieldName;
  private boolean sorted;

  /** default: "ASC" (ascending) */
  private String sortVersus = Consts.NO_SORTED;

  private int sortOrder;
  private boolean editableOnEdit;
  private boolean editableOnInsert;
  private boolean required;
  private boolean filterable;
  private boolean sortable;
  private String tableName;

  /** default: 100 characters */
  private int columnWidth = 100;

  /** default: 255 */
  private int maxChars = 255;
  private int decimals;
  private int integers;

  /** type of attribute; possible values: CustomFieldVO.TYPE_xxx */
  private int attributeType;
  private String description;
  private java.sql.Timestamp defaultValueDate;
  private boolean pk;


  public CustomFieldVO() {
  }


  public String getAttributeName() {
    return attributeName;
  }
  public void setAttributeName(String attributeName) {
    this.attributeName = attributeName;
  }
  public int getAttributeType() {
    return attributeType;
  }
  public void setAttributeType(int attributeType) {
    this.attributeType = attributeType;
  }
  public java.sql.Timestamp getDefaultValueDate() {
    return defaultValueDate;
  }
  public void setDefaultValueDate(java.sql.Timestamp defaultValueDate) {
    this.defaultValueDate = defaultValueDate;
  }
  public String getDefaultValueText() {
    return defaultValueText;
  }
  public void setDefaultValueText(String defaultValueText) {
    this.defaultValueText = defaultValueText;
  }
  public java.math.BigDecimal getDefaultValueNum() {
    return defaultValueNum;
  }
  public void setDefaultValueNum(java.math.BigDecimal defaultValueNum) {
    this.defaultValueNum = defaultValueNum;
  }
  public String getConstraintValues() {
    return constraintValues;
  }
  public void setConstraintValues(String constraintValues) {
    this.constraintValues = constraintValues;
  }
  public boolean isVisible() {
    return visible;
  }
  public void setVisible(boolean visible) {
    this.visible = visible;
  }
  public String getFieldName() {
    return fieldName;
  }
  public void setFieldName(String fieldName) {
    this.fieldName = fieldName;
  }
  public boolean isEditableOnEdit() {
    return editableOnEdit;
  }
  public void setEditableOnEdit(boolean editableOnEdit) {
    this.editableOnEdit = editableOnEdit;
  }
  public boolean isEditableOnInsert() {
    return editableOnInsert;
  }
  public void setEditableOnInsert(boolean editableOnInsert) {
    this.editableOnInsert = editableOnInsert;
  }
  public boolean isRequired() {
    return required;
  }
  public void setRequired(boolean required) {
    this.required = required;
  }
  public boolean isFilterable() {
    return filterable;
  }
  public void setFilterable(boolean filterable) {
    this.filterable = filterable;
  }
  public boolean isSortable() {
    return sortable;
  }
  public void setSortable(boolean sortable) {
    this.sortable = sortable;
  }
  public boolean isSorted() {
    return sorted;
  }
  public void setSorted(boolean sorted) {
    this.sorted = sorted;
  }
  public String getSortVersus() {
    return sortVersus;
  }
  public void setSortVersus(String sortVersus) {
    this.sortVersus = sortVersus;
  }
  public int getSortOrder() {
    return sortOrder;
  }
  public void setSortOrder(int sortOrder) {
    this.sortOrder = sortOrder;
  }
  public String getTableName() {
    return tableName;
  }
  public void setTableName(String tableName) {
    this.tableName = tableName;
  }
  public int getColumnWidth() {
    return columnWidth;
  }
  public void setColumnWidth(int columnWidth) {
    this.columnWidth = columnWidth;
  }
  public int getMaxChars() {
    return maxChars;
  }
  public void setMaxChars(int maxChars) {
    this.maxChars = maxChars;
  }
  public int getDecimals() {
    return decimals;
  }
  public void setDecimals(int decimals) {
    this.decimals = decimals;
  }
  public int getIntegers() {
    return integers;
  }
  public void setIntegers(int integers) {
    this.integers = integers;
  }
  public String getDescription() {
    return description;
  }
  public void setDescription(String description) {
    this.description = description;
  }
  public boolean isPk() {
    return pk;
  }
  public void setPk(boolean pk) {
    this.pk = pk;
  }

}
