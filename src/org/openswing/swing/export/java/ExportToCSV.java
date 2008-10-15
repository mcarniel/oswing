package org.openswing.swing.export.java;

import java.lang.reflect.*;
import java.sql.Date;
import java.text.*;
import java.util.*;

import org.openswing.swing.message.receive.java.*;
import org.openswing.swing.message.send.java.*;


/**
 * <p>Title: OpenSwing Framework</p>
 * <p>Description: This class is used to export grid data in CSV format (with ";" or "," separator).</p>
 * CSV is a delimited data format that has fields/columns separated by the comma character and records/rows separated by newlines.
 * Fields that contain a special character ( comma, newline, or double quote ), must be enclosed in double quotes.
 * However, if a line contains a single entry which is the empty string, it may be enclosed in double quotes.
 * If a field's value contains a double quote character it is escaped by placing another double quote character next to it.
 * The CSV file format does not require a specific character encoding, byte order, or line terminator format.
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
public class ExportToCSV {


  public ExportToCSV() {
  }


  /**
   * @param opt export options
   * @return CSV document, as byte array
   */
  public byte[] getDocument(ExportOptions opt) throws Throwable {
    StringBuffer sb = new StringBuffer("");
    Object obj = null;
    for(int i=0;i<opt.getComponentsExportOptions().size();i++) {
      obj = opt.getComponentsExportOptions().get(i);
      processComponent(sb,opt,obj);
    }
    byte[] doc = sb.toString().getBytes();
    return doc;
  }


  private void processComponent(StringBuffer sb,ExportOptions exportOptions,Object obj) throws Throwable {
    if (obj!=null) {
      GridExportCallbacks callbacks = null;
      if (obj instanceof GridExportOptions) {
        callbacks = (GridExportCallbacks)((GridExportOptions)obj).getCallbacks();
        if (callbacks!=null)
          processComponent(sb,exportOptions,callbacks.getHeaderComponent());
        prepareGrid(sb,exportOptions,(GridExportOptions)obj);
        if (callbacks!=null)
          processComponent(sb,exportOptions,callbacks.getFooterComponent());
      }
      else if (obj instanceof ComponentExportOptions)
        prepareGenericComponent(sb,exportOptions,(ComponentExportOptions)obj);
      else
        return;

      sb.append("\n");
    }
  }


  private void prepareGenericComponent(StringBuffer sb,ExportOptions exportOptions,ComponentExportOptions opt) throws Throwable {
    Object[] row = null;
    Object obj = null;
    SimpleDateFormat sdatf = new SimpleDateFormat(exportOptions.getDateTimeFormat());
    String sep = exportOptions.getExportType().endsWith(ExportOptions.CSV_FORMAT1) ? "," : ";";

    if (opt.getCellsContent()!=null)
      for(int i=0;i<opt.getCellsContent().length;i++) {
        row = opt.getCellsContent()[i];
        for(int j=0;j<row.length;j++) {
          obj = row[j];
          if (obj!=null) {
            if (obj instanceof Boolean) {
              sb.append ((Boolean)obj);
            }
            else if (obj.getClass().equals(boolean.class)) {
              sb.append ((Boolean)obj);
            }
            else if (obj instanceof Date ||
                     obj instanceof java.util.Date ||
                     obj instanceof java.sql.Timestamp) {
             sb.append( sdatf.format((java.util.Date)obj) );
            }
            else {
              sb.append( encodeText(obj.toString(),sep) );
            }
          }

          if (j<row.length-1)
            sb.append(sep);
        }
        sb.append("\n");
      }

  }


  private void prepareGrid(StringBuffer sb,ExportOptions exportOptions,GridExportOptions opt) throws Throwable {
    // prepare vo getters methods...
    String methodName = null;
    String attributeName = null;
    Hashtable gettersMethods = new Hashtable();
    Method[] voMethods = opt.getValueObjectType().getMethods();
    for(int i=0;i<voMethods.length;i++) {
      methodName = voMethods[i].getName();
      if (methodName.startsWith("get")) {
        attributeName = methodName.substring(3,4).toLowerCase()+methodName.substring(4);
        if (opt.getExportAttrColumns().contains(attributeName))
          gettersMethods.put(attributeName,voMethods[i]);
      }
    }

    Response response = null;
    int start = 0;
    int rownum = 0;
    Object value = null;
    Object vo = null;
    int type;

    String sep = exportOptions.getExportType().endsWith(ExportOptions.CSV_FORMAT1) ? "," : ";";

    if (opt.getTitle()!=null && !opt.getTitle().equals(""))
      sb.append(opt.getTitle()).append("\n\n");

    String[] filters = opt.getFilteringConditions();
    if (filters!=null) {
      for(int i=0;i<filters.length;i++)
      sb.append(filters[i]).append("\n");
    sb.append("\n");
    }

    SimpleDateFormat sdf = new SimpleDateFormat(exportOptions.getDateFormat());
    SimpleDateFormat sdatf = new SimpleDateFormat(exportOptions.getDateTimeFormat());
    SimpleDateFormat stf = new SimpleDateFormat(exportOptions.getTimeFormat());

    for(int j=0;j<opt.getTopRows().size();j++) {
      // create a row for each top rows...
      vo = opt.getTopRows().get(j);
      appendRow(
        exportOptions,
        sb,
        vo,
        opt,
        gettersMethods,
        sdf,
        sdatf,
        stf,
        sep,
        j,
        0
      );
    }

    do {
      response=opt.getGridDataLocator().loadData(
        GridParams.NEXT_BLOCK_ACTION,
        start,
        opt.getFilteredColumns(),
        opt.getCurrentSortedColumns(),
        opt.getCurrentSortedVersusColumns(),
        opt.getValueObjectType(),
        opt.getOtherGridParams()
      );
      if (response.isError())
        throw new Exception(response.getErrorMessage());

      for(int j=0;j<((VOListResponse)response).getRows().size();j++) {
        // create a row
        vo = ((VOListResponse)response).getRows().get(j);

        appendRow(
          exportOptions,
          sb,
          vo,
          opt,
          gettersMethods,
          sdf,
          sdatf,
          stf,
          sep,
          rownum,
          1
        );

        rownum++;
      }

      start = start+((VOListResponse)response).getRows().size();

      if ( !((VOListResponse)response).isMoreRows() )
        break;
    }
    while (rownum<opt.getMaxRows());


    for(int j=0;j<opt.getBottomRows().size();j++) {
      // create a row for each bottom rows...
      vo = opt.getBottomRows().get(j);
      appendRow(
        exportOptions,
        sb,
        vo,
        opt,
        gettersMethods,
        sdf,
        sdatf,
        stf,
        sep,
        j,
        2
      );
    }
  }


  /**
   * Append current row to result StringBuffer.
   * @return current row to append
   */
  private void appendRow(
    ExportOptions exportOptions,
    StringBuffer sb,
    Object vo,
    GridExportOptions opt,
    Hashtable gettersMethods,
    SimpleDateFormat sdf,
    SimpleDateFormat sdatf,
    SimpleDateFormat stf,
    String sep,
    int rownum,
    int tableType
  ) throws Throwable {
    int type;
    String aName = null;
    Method getter = null;
    Class clazz = null;
    Object obj = null;
    for(int i=0;i<opt.getExportColumns().size();i++) {
//      value = ((Method)gettersMethods.get(opt.getExportAttrColumns().get(i))).invoke(vo,new Object[0]);

      clazz = vo.getClass();
      obj = vo;
      aName = opt.getExportAttrColumns().get(i).toString();

      // check if the specified attribute is a composed attribute and there exist inner v.o. to instantiate...
      while(aName.indexOf(".")!=-1) {
        try {
          getter = clazz.getMethod(
            "get" +
            aName.substring(0, 1).
            toUpperCase() +
            aName.substring(1,aName.indexOf(".")),
            new Class[0]
          );
        }
        catch (NoSuchMethodException ex2) {
          getter = clazz.getMethod("is"+aName.substring(0,1).toUpperCase()+aName.substring(1,aName.indexOf(".")),new Class[0]);
        }
        aName = aName.substring(aName.indexOf(".")+1);
        clazz = getter.getReturnType();
        obj = getter.invoke(obj,new Object[0]);
        if (obj==null)
          break;
      }

      try {
        getter = clazz.getMethod(
          "get" +
          aName.substring(0, 1).
          toUpperCase() +
          aName.substring(1),
          new Class[0]
        );
      }
      catch (NoSuchMethodException ex2) {
        getter = clazz.getMethod("is"+aName.substring(0,1).toUpperCase()+aName.substring(1),new Class[0]);
      }

      if (obj!=null)
        obj = getter.invoke(obj,new Object[0]);

      if (obj!=null) {
        if (obj instanceof Boolean) {
          sb.append ((Boolean)obj);
        }
        else if (obj.getClass().equals(boolean.class)) {
          sb.append ((Boolean)obj);
        }
        else if (obj instanceof Date ||
                 obj instanceof java.util.Date ||
                 obj instanceof java.sql.Timestamp) {
          type = ((Integer)opt.getColumnsType().get(opt.getExportAttrColumns().get(i))).intValue();
          if (type==opt.TYPE_DATE)
            sb.append( sdf.format((java.util.Date)obj) );
          else if (type==opt.TYPE_DATE_TIME)
            sb.append( sdatf.format((java.util.Date)obj) );
          else if (type==opt.TYPE_TIME)
            sb.append( stf.format((java.util.Date)obj) );
        }
        else {
          sb.append( encodeText(obj.toString(),sep) );
        }
      }

      if (i<opt.getExportColumns().size()-1)
        sb.append(sep);
    }
    sb.append("\n");

    if (opt.getCallbacks()!=null) {
      if (tableType==0) {
        processComponent(
          sb,
          exportOptions,
          opt.getCallbacks().getComponentPerRowInHeader(
            (ValueObject)vo,
            rownum
          )
        );
      }
      else if (tableType==1) {
        ComponentExportOptions compOpts = opt.getCallbacks().getComponentPerRow(
          (ValueObject)vo,
          rownum
        );
        if (compOpts!=null) {
          processComponent(
            sb,
            exportOptions,
            compOpts
          );
        }
      }
      else if (tableType==2) {
        processComponent(
          sb,
          exportOptions,
          opt.getCallbacks().getComponentPerRowInFooter(
            (ValueObject)vo,
            rownum
          )
        );
      }
    }
  }


  /**
   * Fields that contain a special character ( comma, newline, or double quote ), must be enclosed in double quotes.
   * However, if a line contains a single entry which is the empty string, it may be enclosed in double quotes.
   * If a field's value contains a double quote character it is escaped by placing another double quote character next to it.
   * The CSV file format does not require a specific character encoding, byte order, or line terminator format.
   * @param text text to encode with rules defined above.
   * @return encoded text
   */
  private String encodeText(String text,String sep) {
    if (text.indexOf("\"")!=-1) {
      StringBuffer sb = new StringBuffer("");
      for(int i=0;i<text.length();i++) {
        sb.append(text.charAt(i));
        if (text.charAt(i)=='\"')
          sb.append("\"");
      }
      text = sb.toString();
    }
    if (text.indexOf(sep)!=-1 || text.indexOf("\n")!=-1 || text.indexOf("\"")!=-1)
      text = "\""+text+"\"";
    return text;
  }


//  public static void main(String[] argv) {
//    ExportToCSV a = new ExportToCSV();
//    System.out.println( a.endodeText("ac, abs, moon",",") );
//    System.out.println( a.endodeText("aVenture \"Extended Edition\"",",") );
//    System.out.println( a.endodeText("MUST SELL!\nair, moon roof, loaded",",") );
//  }

}
