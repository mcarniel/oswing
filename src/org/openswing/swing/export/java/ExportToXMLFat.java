package org.openswing.swing.export.java;

import java.lang.reflect.*;
import java.sql.Date;
import java.text.*;
import java.util.*;

import org.openswing.swing.message.receive.java.*;
import org.openswing.swing.message.send.java.*;


/**
 * <p>Title: OpenSwing Framework</p>
 * <p>Description: This class is used to export grid data in XML format (fat format, i.e. each tag is named as the attribute name).</p>
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
public class ExportToXMLFat {

  String newline = System.getProperty("line.separator");


  public ExportToXMLFat() {
  }


  /**
   * @param opt export options
   * @return Excel document, as byte array
   */
  public byte[] getDocument(ExportOptions opt) throws Throwable {
    StringBuffer sb = new StringBuffer("");
    sb.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>").append(newline);
    sb.append("<content>").append(newline);

    Object obj = null;
    for(int i=0;i<opt.getComponentsExportOptions().size();i++) {
      obj = opt.getComponentsExportOptions().get(i);
      processComponent(sb,opt,obj);
    }
    sb.append("</content>").append(newline);

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

      sb.append(newline);
    }
  }


  private void prepareGenericComponent(StringBuffer sb,ExportOptions exportOptions,ComponentExportOptions opt) throws Throwable {
    Object[] row = null;
    Object obj = null;
    SimpleDateFormat sdatf = new SimpleDateFormat(exportOptions.getDateTimeFormat());
    String newline = System.getProperty("line.separator");

    if (opt.getCellsContent()!=null)
      for(int i=0;i<opt.getCellsContent().length;i++) {
        row = opt.getCellsContent()[i];
        sb.append("<row>").append(newline);

        for(int j=0;j<row.length;j++) {
          obj = row[j];
          sb.append("\t<cell>").append(newline);

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
              sb.append( encodeText(obj.toString()) );
            }
          }

          sb.append("\t<cell>").append(newline);
        }
        sb.append("</row>").append(newline);
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

    SimpleDateFormat sdf = new SimpleDateFormat(exportOptions.getDateFormat());
    SimpleDateFormat sdatf = new SimpleDateFormat(exportOptions.getDateTimeFormat());
    SimpleDateFormat stf = new SimpleDateFormat(exportOptions.getTimeFormat());

    String newline = System.getProperty("line.separator");
    String tagName = null;

    if (opt.getTitle()!=null && !opt.getTitle().equals("")) {
      sb.append("<title>").append(opt.getTitle()).append("</title>").append(newline);;
    }
    String[] filters = opt.getFilteringConditions();
    if (filters!=null) {
      sb.append("<filters>").append(newline);;
      for(int i=0;i<filters.length;i++) {
        sb.append("<filter>").append(filters[i]).append("</filter>").append(newline);;
      }
      sb.append("</filters>").append(newline);;
    }


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
        newline,
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

      // create tag related to column headers...

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
          newline,
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
        newline,
        j,
        2
      );
    }
  }


  /**
   * Append current row to result.
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
    String newline,
    int rownum,
    int tableType
  ) throws Throwable {
    int type;
    String aName = null;
    Method getter = null;
    Class clazz = null;
    Object obj = null;
    String tagName = null;

    sb.append("\t<row>").append(newline);
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

      tagName = opt.getExportAttrColumns().get(i).toString();
      sb.append("\t\t<"+tagName).
      append(" type=\"").
      append( getter.getReturnType().getName() ).
      append("\" >");

      if (obj!=null) {
        if (obj instanceof Date ||
                 obj instanceof java.util.Date ||
                 obj instanceof java.sql.Timestamp) {
          type = ((Integer)opt.getColumnsType().get(opt.getExportAttrColumns().get(i))).intValue();
          if (type==opt.TYPE_DATE)
            sb.append(sdf.format((java.util.Date)obj)).append("</"+tagName+">").append(newline);
          else if (type==opt.TYPE_DATE_TIME)
            sb.append(sdatf.format((java.util.Date)obj)).append("</"+tagName+">").append(newline);
          else if (type==opt.TYPE_TIME)
            sb.append(stf.format((java.util.Date)obj)).append("</"+tagName+">").append(newline);
        }
        else {
            sb.append(encodeText(obj.toString())).append("</"+tagName+">").append(newline);
        }
      }
      else {
        sb.append("</"+tagName+">").append(newline);
      }
    }
    sb.append("\t</row>").append(newline);

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
   * Encode text with XML escape chars.
   */
  private String encodeText(String text) {
    StringBuffer sb = new StringBuffer("");
    for(int i=0;i<text.length();i++)
      if (text.charAt(i)>=46 && text.charAt(i)<=90 ||
          text.charAt(i)>=97 && text.charAt(i)<=122 ||
          text.charAt(i)==' ')
        sb.append(text.charAt(i));
      else
        sb.append("&#").append((int)text.charAt(i)).append(";");
    return sb.toString();
  }


}
