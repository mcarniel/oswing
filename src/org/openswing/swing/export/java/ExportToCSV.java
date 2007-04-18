package org.openswing.swing.export.java;

import org.openswing.swing.table.java.GridDataLocator;
import org.openswing.swing.message.receive.java.*;
import org.openswing.swing.message.send.java.GridParams;
import java.util.Hashtable;
import java.lang.reflect.Method;
import java.io.*;
import java.math.BigDecimal;
import java.sql.Date;
import java.text.SimpleDateFormat;


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
   * @return Excel document, as byte array
   */
  public byte[] getDocument(ExportOptions opt) throws Throwable {
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

    String sep = opt.getExportType().endsWith(opt.CSV_FORMAT1) ? "," : ";";
    StringBuffer sb = new StringBuffer("");

    SimpleDateFormat sdf = new SimpleDateFormat(opt.getDateFormat());
    SimpleDateFormat sdatf = new SimpleDateFormat(opt.getDateTimeFormat());
    SimpleDateFormat stf = new SimpleDateFormat(opt.getTimeFormat());

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

        for(int i=0;i<opt.getExportColumns().size();i++) {
          value = ((Method)gettersMethods.get(opt.getExportAttrColumns().get(i))).invoke(vo,new Object[0]);
          if (value!=null) {
            if (value instanceof Boolean) {
              sb.append ((Boolean)value);
            }
            else if (value.getClass().equals(boolean.class)) {
              sb.append ((Boolean)value);
            }
            else if (value instanceof Date ||
                     value instanceof java.util.Date ||
                     value instanceof java.sql.Timestamp) {
              type = ((Integer)opt.getColumnsType().get(opt.getExportAttrColumns().get(i))).intValue();
              if (type==opt.TYPE_DATE)
                sb.append( sdf.format((java.util.Date)value) );
              else if (type==opt.TYPE_DATE_TIME)
                sb.append( sdatf.format((java.util.Date)value) );
              else if (type==opt.TYPE_TIME)
                sb.append( stf.format((java.util.Date)value) );
            }
            else {
              sb.append( endodeText(value.toString(),sep) );
            }
          }

          if (i<opt.getExportColumns().size()-1)
            sb.append(sep);
        }
        sb.append("\n");

        rownum++;
      }

      start = start+((VOListResponse)response).getRows().size();

      if ( !((VOListResponse)response).isMoreRows() )
        break;
    }
    while (rownum<opt.getMaxRows());

    byte[] doc = sb.toString().getBytes();
    return doc;
  }


  /**
   * Fields that contain a special character ( comma, newline, or double quote ), must be enclosed in double quotes.
   * However, if a line contains a single entry which is the empty string, it may be enclosed in double quotes.
   * If a field's value contains a double quote character it is escaped by placing another double quote character next to it.
   * The CSV file format does not require a specific character encoding, byte order, or line terminator format.
   * @param text text to encode with rules defined above.
   * @return encoded text
   */
  private String endodeText(String text,String sep) {
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
