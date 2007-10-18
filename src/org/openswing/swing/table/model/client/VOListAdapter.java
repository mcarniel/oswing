package org.openswing.swing.table.model.client;

import org.openswing.swing.domains.java.*;
import javax.swing.*;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;
import javax.swing.tree.*;
import java.lang.reflect.*;
import java.math.BigDecimal;
import java.util.Date;
import java.sql.Timestamp;
import java.util.Hashtable;
import org.openswing.swing.table.renderers.client.*;
import org.openswing.swing.table.editors.client.*;
import org.openswing.swing.table.columns.client.*;
import org.openswing.swing.client.*;
import org.openswing.swing.table.client.*;
import org.openswing.swing.client.*;
import org.openswing.swing.message.receive.java.*;
import org.openswing.swing.logger.client.Logger;
import org.openswing.swing.client.*;
import org.openswing.swing.domains.java.*;

import org.openswing.swing.util.client.*;
import org.openswing.swing.util.java.*;
import java.util.Enumeration;
import java.util.HashSet;
import org.openswing.swing.table.renderers.client.MultiLineTextTableCellRenderer;
import org.openswing.swing.table.editors.client.MultipleTypeCellEditor;
import org.openswing.swing.internationalization.java.Resources;
import org.openswing.swing.table.renderers.client.ComboVOTableCellRenderer;
import org.openswing.swing.table.columns.client.ComboVOColumn;


/**
 * <p>Title: OpenSwing Framework</p>
 * <p>Description: Adapter used to link TableModel columns and a ValueObject.</p>
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
public class VOListAdapter {

  /** TableModel column properties (name, type, etc) */
  private Column[] colProperties = null;

  /** ValueObject setter methods */
  private Hashtable voSetterMethods = new Hashtable();

  /** ValueObject getter methods */
  private Hashtable voGetterMethods = new Hashtable();

  /** grid container */
  private GridController tableContainer = null;

  /** ValueObject type */
  private Class valueObjectType = null;

  /** grids */
  private Grids grids = null;

  /** collection of couples: column model index (Integer), related attribute (String) */
  private Hashtable indexes = new Hashtable();

  /** collection of couples: related attribute (String), column model index (Integer) */
  private Hashtable reverseIndexes = new Hashtable();


  /**
   * @param valueObjectType ValueObject type
   * @param tableContainer grid container
   * @param colProperties list of TableModel column properties (name, type, etc)
   * @param grid grid
   */
  public VOListAdapter(Class valueObjectType,GridController tableContainer,Column[] colProperties,Grids grids) {
    this.tableContainer = tableContainer;
    this.colProperties = colProperties;
    this.grids = grids;
    this.valueObjectType = valueObjectType;
    analyzeClassFields("",new Method[0],valueObjectType);
  }


  /**
   * Analyze class fields and fill in "voSetterMethods","voGetterMethods","indexes",reverseIndexes" attributes.
   * @param prefix e.g. "attrx.attry."
   * @param parentMethods getter methods of parent v.o.
   * @param classType class to analyze
   */
    private void analyzeClassFields(String prefix,Method[] parentMethods,Class classType) {
    try {
      // retrieve all getter and setter methods defined in the specified value object...
      String attributeName = null;
      Method[] methods = classType.getMethods();
      String aName = null;
      for(int i=0;i<methods.length;i++) {
        attributeName = methods[i].getName();

        if (attributeName.startsWith("get") && methods[i].getParameterTypes().length==0 &&
          ValueObject.class.isAssignableFrom(methods[i].getReturnType())) {
          aName = attributeName.substring(3,4).toLowerCase()+(attributeName.length()>4?attributeName.substring(4):"");
          Method[] newparentMethods = new Method[parentMethods.length+1];
          System.arraycopy(parentMethods,0,newparentMethods,0,parentMethods.length);
          newparentMethods[parentMethods.length] = methods[i];
          analyzeClassFields(prefix+aName+".",newparentMethods,methods[i].getReturnType());
        }

        if (attributeName.startsWith("get") && methods[i].getParameterTypes().length==0 &&
            (methods[i].getReturnType().equals(String.class) ||
             methods[i].getReturnType().equals(Long.class) ||
             methods[i].getReturnType().equals(Float.class) ||
             methods[i].getReturnType().equals(Double.class) ||
             methods[i].getReturnType().equals(BigDecimal.class) ||
             methods[i].getReturnType().equals(java.util.Date.class) ||
             methods[i].getReturnType().equals(java.sql.Date.class) ||
             methods[i].getReturnType().equals(java.sql.Timestamp.class) ||
             methods[i].getReturnType().equals(Integer.class) ||
             methods[i].getReturnType().equals(Character.class) ||
             methods[i].getReturnType().equals(Boolean.class) ||
             methods[i].getReturnType().equals(boolean.class) ||
             methods[i].getReturnType().equals(byte[].class) ||
             methods[i].getReturnType().equals(Object.class) ||
             ValueObject.class.isAssignableFrom( methods[i].getReturnType() )
            )) {
          attributeName = attributeName.substring(3,4).toLowerCase()+(attributeName.length()>4?attributeName.substring(4):"");
//          try {
//            if (classType.getMethod("set"+attributeName.substring(0,1).toUpperCase()+attributeName.substring(1),new Class[]{methods[i].getReturnType()})!=null)
          Method[] newparentMethods = new Method[parentMethods.length+1];
          System.arraycopy(parentMethods,0,newparentMethods,0,parentMethods.length);
          newparentMethods[parentMethods.length] = methods[i];
          voGetterMethods.put(prefix+attributeName,newparentMethods);
//          } catch (NoSuchMethodException ex) {
//          }
        }
        else if (attributeName.startsWith("is") &&
                 methods[i].getParameterTypes().length==0 &&
                 (methods[i].getReturnType().equals(Boolean.class) ||
                  methods[i].getReturnType().equals(boolean.class) )) {
          attributeName = attributeName.substring(2,3).toLowerCase()+(attributeName.length()>3?attributeName.substring(3):"");
          Method[] newparentMethods = new Method[parentMethods.length+1];
          System.arraycopy(parentMethods,0,newparentMethods,0,parentMethods.length);
          newparentMethods[parentMethods.length] = methods[i];
          voGetterMethods.put(prefix+attributeName,newparentMethods);
        }
        else if (attributeName.startsWith("set") && methods[i].getParameterTypes().length==1) {
          attributeName = attributeName.substring(3,4).toLowerCase()+(attributeName.length()>4?attributeName.substring(4):"");
          try {
            if (classType.getMethod("get"+attributeName.substring(0,1).toUpperCase()+attributeName.substring(1),new Class[0])!=null) {
              Method[] newparentMethods = new Method[parentMethods.length+1];
              System.arraycopy(parentMethods,0,newparentMethods,0,parentMethods.length);
              newparentMethods[parentMethods.length] = methods[i];
              voSetterMethods.put(prefix+attributeName,newparentMethods);
            }
          } catch (NoSuchMethodException ex) {
            try {
              if (classType.getMethod("is"+attributeName.substring(0,1).toUpperCase()+attributeName.substring(1),new Class[0])!=null) {
                Method[] newparentMethods = new Method[parentMethods.length+1];
                System.arraycopy(parentMethods,0,newparentMethods,0,parentMethods.length);
                newparentMethods[parentMethods.length] = methods[i];
                voSetterMethods.put(prefix+attributeName,newparentMethods);
              }
            } catch (NoSuchMethodException exx) {
            }
          }
        }
      }

      // fill in indexes with the colProperties indexes first; after them, it will be added the other indexes (of attributes not mapped with grid column...)
      HashSet alreadyAdded = new HashSet();
      int i=0;
      for(i=0;i<colProperties.length;i++) {
        indexes.put(new Integer(i), colProperties[i].getColumnName());
        reverseIndexes.put(colProperties[i].getColumnName(),new Integer(i));
        alreadyAdded.add(colProperties[i].getColumnName());
      }
      Enumeration en = voGetterMethods.keys();
      while(en.hasMoreElements()) {
        attributeName = en.nextElement().toString();
        if (!alreadyAdded.contains(attributeName)) {
          indexes.put(new Integer(i),attributeName);
          reverseIndexes.put(attributeName,new Integer(i));
          i++;
        }
      }
    }
    catch (Exception ex) {
      ex.printStackTrace();
    }
  }


  /**
   * @return TableModel columns count
   */
  public final int getFieldCount() {
    return colProperties.length;
  }


  /**
   * @param colIndex TableModel column index
   * @return TableModel column name
   */
  public final String getFieldName(int colIndex) {
    String attributeName = (String)indexes.get(new Integer(colIndex));
    if (attributeName==null)
      Logger.error(this.getClass().getName(),"getField","No attribute found for index "+colIndex+".",null);
    return attributeName;
  }


  /**
   * @param obj ValueObject related to the current row
   * @param colIndex TableModel column index
   * @return Object contained into the TableModel at the specified column and ValueObject
   */
  public final Object getField(ValueObject obj, int colIndex) {
    try {
      Method[] m = (Method[])voGetterMethods.get(getFieldName(colIndex));
      if (m==null)
        Logger.error(this.getClass().getName(),"getField","No getter method for index "+colIndex+" and attribute name '"+getFieldName(colIndex)+"'.",null);

      for(int i=0;i<m.length-1;i++){
        obj = (ValueObject)m[i].invoke(obj,new Object[0]);
        if(obj == null) {
          if (grids.getGridControl()==null || !grids.getGridControl().isCreateInnerVO())
            return null;
          else
            obj = (ValueObject)m[i].getReturnType().newInstance();
        }
      }

      return m[m.length-1].invoke(obj,new Object[0]);
    }
    catch (Exception ex) {
      ex.printStackTrace();
      return null;
    }
  }


  /**
   * @param obj ValueObject where updating the value for the specified attribute (identified by colunm index)
   * @param colIndex TableModel column index
   * @param value new Object to set onto ValueObject
   */
  public final void setField(ValueObject obj, int colIndex, Object value) {
    try {
      Method[] getter = ((Method[])voGetterMethods.get(getFieldName(colIndex)));
      Method[] setter = ((Method[])voSetterMethods.get(getFieldName(colIndex)));
      if (getter==null)
        Logger.error(this.getClass().getName(),"setField","No getter method for index "+colIndex+" and attribute name '"+getFieldName(colIndex)+"'.",null);
      if (setter==null)
        Logger.error(this.getClass().getName(),"setField","No setter method for index "+colIndex+" and attribute name '"+getFieldName(colIndex)+"'.",null);


      if (value!=null && (value instanceof Number || !value.equals("") && value instanceof String)) {
        if (!getter[getter.length-1].getReturnType().equals(value.getClass())) {
            Class attrType = getter[getter.length-1].getReturnType();
            if (attrType.equals(Integer.class))
              value = new Integer(Double.valueOf(value.toString()).intValue());
            else if (attrType.equals(Double.class))
              value = new Double(value.toString());
            else if (attrType.equals(BigDecimal.class))
              value = new BigDecimal(value.toString());
            else if (attrType.equals(Long.class))
              value = new Long(Double.valueOf(value.toString()).longValue());
            else if (attrType.equals(Float.class))
              value = new Float(Double.valueOf(value.toString()).floatValue());
        }
      }
      else if (value!=null && value.equals("")) {
        if (!getter[getter.length-1].getReturnType().equals(value.getClass()))
          value = null;
      }
      // test date compatibility...
      if (value!=null && value.getClass().equals(java.util.Date.class)) {
        if (setter[setter.length-1].getParameterTypes()[0].equals(java.sql.Date.class))
          value = new java.sql.Date(((java.util.Date)value).getTime());
        else if (setter[setter.length-1].getParameterTypes()[0].equals(java.sql.Timestamp.class))
          value = new java.sql.Timestamp(((java.util.Date)value).getTime());
      }




      // retrieve inner v.o.: if not present then maybe create it, according to "createInnerVO" property...
      Method[] m = (Method[])voGetterMethods.get(getFieldName(colIndex));
      if (m==null)
        Logger.error(this.getClass().getName(),"setField","No getter method for index "+colIndex+" and attribute name '"+getFieldName(colIndex)+"'.",null);

      for(int i=0;i<m.length-1;i++){
        obj = (ValueObject)m[i].invoke(obj,new Object[0]);
        if(obj == null) {
          if (grids.getGridControl()==null || !grids.getGridControl().isCreateInnerVO())
            return;
          else
            obj = (ValueObject)m[i].getReturnType().newInstance();
        }
      }

      setter[setter.length-1].invoke(obj,new Object[]{value});
    }
    catch (Exception ex) {
      ex.printStackTrace();
    }
  }


  /**
   * @param colIndex TableModel column index
   * @return TableCellRenderer for the specified column
   */
  public final TableCellRenderer getCellRenderer(int colIndex) {
    try {
      Class colType = getFieldClass(colIndex);
      if(colProperties[colIndex].getColumnType()==Column.TYPE_INT) {
        return new NumericTableCellRenderer(
            0,
            ((IntegerColumn)colProperties[colIndex]).isGrouping(),
            tableContainer,
            ((IntegerColumn)colProperties[colIndex]).getDynamicSettings(),
            colProperties[colIndex].getTextAlignment()
        );

      }
      else if (colProperties[colIndex].getColumnType()==Column.TYPE_DEC) {
        return new NumericTableCellRenderer(
            ((DecimalColumn)colProperties[colIndex]).getDecimals(),
            ((DecimalColumn)colProperties[colIndex]).isGrouping(),
            tableContainer,
            ((DecimalColumn)colProperties[colIndex]).getDynamicSettings(),
            colProperties[colIndex].getTextAlignment()
        );
      }
      else if (colProperties[colIndex].getColumnType()==Column.TYPE_PERC) {
        // real number (percentage)
        return new NumericTableCellRenderer(
            ((PercentageColumn)colProperties[colIndex]).getDecimals(),
            ((PercentageColumn)colProperties[colIndex]).isGrouping(),
            tableContainer,
            ((PercentageColumn)colProperties[colIndex]).getDynamicSettings(),
            colProperties[colIndex].getTextAlignment()
        );
      }
      else if (colProperties[colIndex].getColumnType()==Column.TYPE_CURRENCY) {
        // real number (currency)
        return new CurrencyTableCellRenderer(
            ((CurrencyColumn)colProperties[colIndex]).getDecimals(),
            ((CurrencyColumn)colProperties[colIndex]).isGrouping(),
            ((CurrencyColumn)colProperties[colIndex]).getCurrencySymbol(),
            tableContainer,
            ((CurrencyColumn)colProperties[colIndex]).getDynamicSettings(),
            colProperties[colIndex].getTextAlignment()
        );
      }
      else if ( colProperties[colIndex].getColumnType()==Column.TYPE_DATE) {
        return new DateTableCellRenderer(
          Column.TYPE_DATE,
          tableContainer,
          colProperties[colIndex].getTextAlignment(),
          ((DateColumn)colProperties[colIndex]).getSeparator(),
          ((DateColumn)colProperties[colIndex]).getFormat(),
          ((DateColumn)colProperties[colIndex]).getDateListeners(),
          ((DateColumn)colProperties[colIndex]).isShowCentury(),
          Resources.HH_MM
        );
      }
      else if ( colProperties[colIndex].getColumnType()==Column.TYPE_DATE_TIME) {
        return new DateTableCellRenderer(
          Column.TYPE_DATE_TIME,
          tableContainer,
          colProperties[colIndex].getTextAlignment(),
          ((DateTimeColumn)colProperties[colIndex]).getSeparator(),
          ((DateTimeColumn)colProperties[colIndex]).getFormat(),
          ((DateTimeColumn)colProperties[colIndex]).getDateListeners(),
          ((DateTimeColumn)colProperties[colIndex]).isShowCentury(),
          ((DateTimeColumn)colProperties[colIndex]).getTimeFormat()
        );
      }
      else if ( colProperties[colIndex].getColumnType()==Column.TYPE_TIME) {
        return new DateTableCellRenderer(
          Column.TYPE_TIME,
          tableContainer,
          colProperties[colIndex].getTextAlignment(),
          '/',
          Resources.DMY,
          ((TimeColumn)colProperties[colIndex]).getDateListeners(),
          true,
          ((TimeColumn)colProperties[colIndex]).getTimeFormat()
        );
      }
      else if (colProperties[colIndex].getColumnType()==Column.TYPE_TEXT) {
        return new TextTableCellRenderer(
            tableContainer,
            ((TextColumn)colProperties[colIndex]).isEncryptText(),
            colProperties[colIndex].getTextAlignment()
        );
      }
      else if (colProperties[colIndex].getColumnType()==Column.TYPE_FORMATTED_TEXT) {
        return new TextTableCellRenderer(
            tableContainer,
            false,
            colProperties[colIndex].getTextAlignment()
        );
      }
      else if (colProperties[colIndex].getColumnType()==Column.TYPE_MULTI_LINE_TEXT) {
        return new MultiLineTextTableCellRenderer(
            tableContainer
        );
      }
      else if (colProperties[colIndex].getColumnType()==Column.TYPE_MULTIPLE_TYPE) {
        return new MultipleTypeTableCellRenderer(
          tableContainer,
          ((MultipleTypeColumn)colProperties[colIndex]).getTypeController(),
          colProperties[colIndex].getTextAlignment(),
          colProperties[colIndex].getColumnName(),
          grids.getGridControl()
        );
      }

//      else if (colProperties[colIndex].getColumnType()==Column.TYPE_FORMATTED_TEXT) {
//        return new FormattedTextTableCellRenderer(
//            tableContainer,
//            ((FormattedTextColumn)colProperties[colIndex]).getTextBox()
//        );
//      }
      else if (colProperties[colIndex].getColumnType()==Column.TYPE_CHECK) {
        return new CheckBoxTableCellRenderer(tableContainer,colProperties[colIndex].getTextAlignment());
      }
      else if (colProperties[colIndex].getColumnType()==Column.TYPE_COMBO) {
        if (((ComboColumn)colProperties[colIndex]).getDomainId()==null &&
            ((ComboColumn)colProperties[colIndex]).getDomain()==null) {
          Logger.error(this.getClass().getName(),"getCellRenderer","You must set the 'domainId' property for the column index "+colIndex+".",null);
          return null;
        }
        Domain domain = ((ComboColumn)colProperties[colIndex]).getDomain();
        if (domain==null)
          domain = ClientSettings.getInstance().getDomain( ((ComboColumn)colProperties[colIndex]).getDomainId() );
        if (domain!=null)
          return new DomainTableCellRenderer(domain,tableContainer,colProperties[colIndex].getTextAlignment());
        else {
          Logger.error(this.getClass().getName(),"getCellRenderer","The domainId '"+((ComboColumn)colProperties[colIndex]).getDomainId()+"' for the column index "+colIndex+" doesn't exist.",null);
          return null;
        }
      }
      else if (colProperties[colIndex].getColumnType()==Column.TYPE_LOOKUP) {
        return new CodLookupCellRenderer(
            tableContainer,
            ((CodLookupColumn)colProperties[colIndex]).isHideCodeBox(),
            colProperties[colIndex].getTextAlignment()
        );
      }
      else if ( colProperties[colIndex].getColumnType()==Column.TYPE_BUTTON) {
        return new ButtonTableCellRenderer(
          ((ButtonColumn)colProperties[colIndex]).getText(),
          ((ButtonColumn)colProperties[colIndex]).isShowAttributeValue(),
          tableContainer,
            colProperties[colIndex].getTextAlignment()
        );
      }
      else if ( colProperties[colIndex].getColumnType()==Column.TYPE_IMAGE) {
        return new ImageTableCellRenderer(
          tableContainer,
          colProperties[colIndex].getTextAlignment()
        );
      }
      else if ( colProperties[colIndex].getColumnType()==Column.TYPE_PROGRESS_BAR) {
        return new ProgressBarTableCellRenderer(
          tableContainer,
          ((ProgressBarColumn)colProperties[colIndex]).getColoredBands(),
          ((ProgressBarColumn)colProperties[colIndex]).getMinValue(),
          ((ProgressBarColumn)colProperties[colIndex]).getMaxValue(),
          ((ProgressBarColumn)colProperties[colIndex]).isShowAllBands(),
          ((ProgressBarColumn)colProperties[colIndex]).getColor()
        );
      }
      else if (colProperties[colIndex].getColumnType()==Column.TYPE_COMBO_VO) {
        return new ComboVOTableCellRenderer(
            ((ComboVOColumn)colProperties[colIndex]).getComboDataLocator(),
            ((ComboVOColumn)colProperties[colIndex]).getColumnName(),
            ((ComboVOColumn)colProperties[colIndex]).getItemsVO(),
            ((ComboVOColumn)colProperties[colIndex]).getColProperties(),
            ((ComboVOColumn)colProperties[colIndex]).isAllColumnVisible(),
            ((ComboVOColumn)colProperties[colIndex]).getAllColumnPreferredWidth(),
            ((ComboVOColumn)colProperties[colIndex]).getGetters(),
            tableContainer
        );
      }
      else {
          Logger.error(this.getClass().getName(),"getCellRenderer","The column type for column index "+colIndex+" is not supported.",null);
        return null;
      }
    }
    catch (Exception ex) {
      ex.printStackTrace();
      return null;
    }
  }


  /**
   * @param colIndex TableModel column index
   * @return TableCellEditor for the specified column
   */
  public final TableCellEditor getCellEditor(int colIndex) {
    try {
      Class colType = getFieldClass(colIndex);
      if ( colProperties[colIndex].getColumnType()==Column.TYPE_INT) {
        // integer number
        return new NumericCellEditor(
            Column.TYPE_INT,
            0,
            colProperties[colIndex].isColumnRequired(),
            ((IntegerColumn)colProperties[colIndex]).getMinValue(),
            ((IntegerColumn)colProperties[colIndex]).getMaxValue(),
            ((IntegerColumn)colProperties[colIndex]).getDynamicSettings()
        );
      }
      else if (colProperties[colIndex].getColumnType()==Column.TYPE_DEC) {
        // real number
        return new NumericCellEditor(
            Column.TYPE_DEC,
            ((DecimalColumn)colProperties[colIndex]).getDecimals(),
            ((DecimalColumn)colProperties[colIndex]).isColumnRequired(),
            ((DecimalColumn)colProperties[colIndex]).getMinValue(),
            ((DecimalColumn)colProperties[colIndex]).getMaxValue(),
            ((DecimalColumn)colProperties[colIndex]).getDynamicSettings()
        );
      }
      else if (colProperties[colIndex].getColumnType()==Column.TYPE_PERC) {
        // real number (percentage)
        return new NumericCellEditor(
            Column.TYPE_PERC,
            ((PercentageColumn)colProperties[colIndex]).getDecimals(),
            ((PercentageColumn)colProperties[colIndex]).isColumnRequired(),
            ((PercentageColumn)colProperties[colIndex]).getMinValue(),
            ((PercentageColumn)colProperties[colIndex]).getMaxValue(),
            ((PercentageColumn)colProperties[colIndex]).getDynamicSettings()
        );
      }
      else if (colProperties[colIndex].getColumnType()==Column.TYPE_CURRENCY) {
        // real number (currency)
        return new CurrencyCellEditor(
            Column.TYPE_CURRENCY,
            ((CurrencyColumn)colProperties[colIndex]).getDecimals(),
            ((CurrencyColumn)colProperties[colIndex]).isColumnRequired(),
            ((CurrencyColumn)colProperties[colIndex]).getMinValue(),
            ((CurrencyColumn)colProperties[colIndex]).getMaxValue(),
            ((CurrencyColumn)colProperties[colIndex]).getCurrencySymbol(),
            ((CurrencyColumn)colProperties[colIndex]).getDynamicSettings()
        );
      }
      else if (colProperties[colIndex].getColumnType()==Column.TYPE_DATE) {
        // date
        return new DateCellEditor(colProperties[colIndex].isColumnRequired(),Column.TYPE_DATE);
      }
      else if (colProperties[colIndex].getColumnType()==Column.TYPE_DATE_TIME) {
        // date + time
        return new DateCellEditor(colProperties[colIndex].isColumnRequired(),Column.TYPE_DATE_TIME);
      }
      else if (colProperties[colIndex].getColumnType()==Column.TYPE_TIME) {
        // time
        return new DateCellEditor(colProperties[colIndex].isColumnRequired(),Column.TYPE_TIME);
      }
      else if (colProperties[colIndex].getColumnType()==Column.TYPE_TEXT) {
        // text
        if (((TextColumn)colProperties[colIndex]).isEncryptText())
          return new TextCellEditor(
              ((TextColumn)colProperties[colIndex]).getMaxCharacters(),
              colProperties[colIndex].isColumnRequired()
          );
        else
          return new TextCellEditor(
            ((TextColumn)colProperties[colIndex]).getMaxCharacters(),
            colProperties[colIndex].isColumnRequired(),
            ((TextColumn)colProperties[colIndex]).isRpadding(),
            ((TextColumn)colProperties[colIndex]).isTrimText(),
            ((TextColumn)colProperties[colIndex]).isUpperCase()
          );
      }
      else if (colProperties[colIndex].getColumnType()==Column.TYPE_MULTI_LINE_TEXT) {
        // multi line text
          return new MultiLineTextCellEditor(
            ((MultiLineTextColumn)colProperties[colIndex]).getMaxCharacters(),
            colProperties[colIndex].isColumnRequired()
          );
      }
      else if (colProperties[colIndex].getColumnType()==Column.TYPE_MULTIPLE_TYPE) {
        return new MultipleTypeCellEditor(
          tableContainer,
          ((MultipleTypeColumn)colProperties[colIndex]).getTypeController(),
          colProperties[colIndex].getColumnName(),
          grids.getGridControl(),
          colProperties[colIndex].isColumnRequired()
        );
      }
      else if (colProperties[colIndex].getColumnType()==Column.TYPE_FORMATTED_TEXT) {
        return new FormattedTextCellEditor(
          ((FormattedTextColumn)colProperties[colIndex]).getTextBox(),
          colProperties[colIndex].isColumnRequired()
        );
      }
      else if (colProperties[colIndex].getColumnType()==Column.TYPE_CHECK) {
        return new CheckBoxCellEditor(colProperties[colIndex].isColumnRequired(),((CheckBoxColumn)colProperties[colIndex]).getItemListeners());
      }
      else if (colProperties[colIndex].getColumnType()==Column.TYPE_COMBO) {
        if (((ComboColumn)colProperties[colIndex]).getDomainId()==null &&
            ((ComboColumn)colProperties[colIndex]).getDomain()==null) {
          Logger.error(this.getClass().getName(),"getCellEditor","You must set the 'domainId' property for the column index "+colIndex+".",null);
          return null;
        }
        Domain domain = ((ComboColumn)colProperties[colIndex]).getDomain();
        if (domain==null)
          domain = ClientSettings.getInstance().getDomain( ((ComboColumn)colProperties[colIndex]).getDomainId() );
        if (domain!=null)
          return new DomainCellEditor(
              domain,
              colProperties[colIndex].isColumnRequired(),
              ((ComboColumn)colProperties[colIndex]).getItemListeners()
          );
        else {
          Logger.error(this.getClass().getName(),"getCellEditor","The domainId '"+((ComboColumn)colProperties[colIndex]).getDomainId()+"' for the column index "+colIndex+" doesn't exist.",null);
          return null;
        }
      }
      else if (colProperties[colIndex].getColumnType()==Column.TYPE_LOOKUP) {
        if (((CodLookupColumn)colProperties[colIndex]).getLookupController()==null) {
          Logger.error(this.getClass().getName(),"getCellEditor","The column with index "+colIndex+"  ("+colProperties[colIndex].getColumnName()+") has not set the 'lookupController' property.",null);
          return null;
        }
        return new CodLookupCellEditor(
            ((CodLookupColumn)colProperties[colIndex]).getMaxCharacters(),
            ((CodLookupColumn)colProperties[colIndex]).getLookupController(),
            ((CodLookupColumn)colProperties[colIndex]).isColumnRequired(),
            ((CodLookupColumn)colProperties[colIndex]).isAllowOnlyNumbers(),
            !((CodLookupColumn)colProperties[colIndex]).isHideCodeBox(),
            ((CodLookupColumn)colProperties[colIndex]).isEnableCodBox(),
            ((CodLookupColumn)colProperties[colIndex]).getControllerClassName(),
            ((CodLookupColumn)colProperties[colIndex]).getControllerMethodName());
      }
      else if (colProperties[colIndex].getColumnType()==Column.TYPE_BUTTON) {
        return new ButtonCellEditor(
          ((ButtonColumn)colProperties[colIndex]).getText(),
          ((ButtonColumn)colProperties[colIndex]).isShowAttributeValue(),
          ((ButtonColumn)colProperties[colIndex]).getActionListeners()
        );
      }
      else if (colProperties[colIndex].getColumnType()==Column.TYPE_IMAGE) {
        return new ImageCellEditor(
          ((ImageColumn)colProperties[colIndex]).isShowButton(),
          ((ImageColumn)colProperties[colIndex]).getFileFilter(),
          ((ImageColumn)colProperties[colIndex]).getListeners()
        );
      }
      else if (colProperties[colIndex].getColumnType()==Column.TYPE_PROGRESS_BAR) {
        // text
        return new TextCellEditor(
            0,
            false
        );
      }
      else if (colProperties[colIndex].getColumnType()==Column.TYPE_COMBO_VO) {
        return new ComboBoxVOCellEditor(
            ((ComboVOColumn)colProperties[colIndex]).getItemsMapper(),
            ((ComboVOColumn)colProperties[colIndex]).getComboDataLocator(),
            ((ComboVOColumn)colProperties[colIndex]).getColumnName(),
            ((ComboVOColumn)colProperties[colIndex]).getItemsVO(),
            ((ComboVOColumn)colProperties[colIndex]).getColProperties(),
            ((ComboVOColumn)colProperties[colIndex]).isAllColumnVisible(),
            ((ComboVOColumn)colProperties[colIndex]).getAllColumnPreferredWidth(),
            ((ComboVOColumn)colProperties[colIndex]).getGetters(),
            ((ComboVOColumn)colProperties[colIndex]).isColumnRequired(),
            ((ComboVOColumn)colProperties[colIndex]).getItemListeners()
        );
      }
      else {
        Logger.error(this.getClass().getName(),"getCellEditor","The column type for column index "+colIndex+" is not supported.",null);
        return null;
      }
    }
    catch (Exception ex) {
      ex.printStackTrace();
      return null;
    }
  }


  /**
   * Method not supported.
   */
  public final boolean isFieldEditable(int colIndex) {
    return false;
  }


  /**
   * @param mode current edit grid mode
   * @param rowNumber TableModel row index
   * @param colIndex TableModel column index
   * @return <code>true</code> means that the cell having the specified row and column index is editable, <code>false</code> otherwise
   */
  public final boolean isFieldEditable(int mode,int rowNumber,int colIndex) {
    if (colIndex<colProperties.length) {
      if (mode==Consts.INSERT)
        return colProperties[colIndex].isEditableOnInsert();
      else if (mode==Consts.EDIT)
        return colProperties[colIndex].isEditableOnEdit();
      else
        return false;
    }
    else if (mode==Consts.INSERT || mode==Consts.EDIT)
      // if the column index is related to an attribute not mapped with a grid column,
      // then it assumed that it's editable (if the the grid is on INSERT/EDIT mode...)
      return true;
    else
      return false;
  }


  /**
   * @param colIndex TableModel column index
   * @return <code>true</code> means that the cell having the specified column index is duplicable, <code>false</code> otherwise
   */
  public final boolean isFieldDuplicable(int colIndex) {
    return colProperties[colIndex].isColumnDuplicable();
  }


  /**
   * @param colIndex TableModel column index
   * @return column type
   */
  public final Class getFieldClass(int colIndex) {
    try {
      Method[] m = (Method[])voGetterMethods.get(getFieldName(colIndex));
      if (m==null)
        Logger.error(this.getClass().getName(),"getField","No getter method for index "+colIndex+" and attribute name '"+getFieldName(colIndex)+"'.",null);
      return m[m.length-1].getReturnType();
    }
    catch (Exception ex) {
      ex.printStackTrace();
      return String.class;
    }
  }


  /**
   * @param colIndex indice della colonna del modello dati
   * @return column preferred width
   */
  public final int getFieldPreferredWidth(int colIndex) {
    return colProperties[colIndex].getPreferredWidth();
  }


  /**
   * @param colIndex indice della colonna del modello dati
   * @return column minimum width
   */
  public final int getFieldMinWidth(int colIndex) {
    return colProperties[colIndex].getMinWidth();
  }


  /**
   * @param colIndex indice della colonna del modello dati
   * @return column maximum width
   */
  public final int getFieldMaxWidth(int colIndex) {
    return colProperties[colIndex].getMaxWidth();
  }


  /**
   * @return grid container
   */
  public final GridController getTableContainer() {
    return tableContainer;
  }


  /**
   * @return ValueObject type
   */
  public final Class getValueObjectType() {
    return valueObjectType;
  }


  /**
   * @param fieldName attribute name
   * @return int column index
   */
  public final int getFieldIndex(String fieldName) {
    Integer index = (Integer)reverseIndexes.get(fieldName);
    if (index!=null)
      return index.intValue();
    Logger.error(this.getClass().getName(),"getFieldIndex","The column '"+fieldName+"' does not exist",null);
    return(-1);
  }


  /**
   * @return grid
   */
  public final Grids getGrids() {
    return grids;
  }

}
