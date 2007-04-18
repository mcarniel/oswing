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
 * <p>Description: This class is used to export grid data in HTML format.</p>
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
public class ExportToHTML {


  public ExportToHTML() {
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

    StringBuffer sb = new StringBuffer("");

    SimpleDateFormat sdf = new SimpleDateFormat(opt.getDateFormat());
    SimpleDateFormat sdatf = new SimpleDateFormat(opt.getDateTimeFormat());
    SimpleDateFormat stf = new SimpleDateFormat(opt.getTimeFormat());

    String newline = System.getProperty("line.separator");

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
      sb.append("<HTML><HEAD><TITLE></TITLE></HEAD>").append(newline);
      sb.append("<BODY>").append(newline);
      sb.append("<TABLE BORDER=1>").append(newline);
      sb.append("<TR>").append(newline);
      for(int i=0;i<opt.getExportColumns().size();i++) {
        sb.append("\t<TD bgcolor=92C3EE align=CENTER><FONT face=\"Verdana, Arial, Helvetica, sans-serif\">").
           append(opt.getExportColumns().get(i)).
           append("</FONT></TD>").
           append(newline);
      }
      sb.append("</TR>").append(newline);

      for(int j=0;j<((VOListResponse)response).getRows().size();j++) {
        // create a row

        sb.append("<TR>").append(newline);

        vo = ((VOListResponse)response).getRows().get(j);

        for(int i=0;i<opt.getExportColumns().size();i++) {
          value = ((Method)gettersMethods.get(opt.getExportAttrColumns().get(i))).invoke(vo,new Object[0]);
          if (value!=null) {
            if (value instanceof Date ||
                     value instanceof java.util.Date ||
                     value instanceof java.sql.Timestamp) {
              type = ((Integer)opt.getColumnsType().get(opt.getExportAttrColumns().get(i))).intValue();
              if (type==opt.TYPE_DATE)
                sb.append("\t<TD bgcolor=E8E8E8><FONT face=\"Verdana, Arial, Helvetica, sans-serif\">").append(sdf.format((java.util.Date)value)).append("</FONT></TD>").append(newline);
              else if (type==opt.TYPE_DATE_TIME)
                sb.append("\t<TD bgcolor=E8E8E8><FONT face=\"Verdana, Arial, Helvetica, sans-serif\">").append(sdatf.format((java.util.Date)value)).append("</FONT></TD>").append(newline);
              else if (type==opt.TYPE_TIME)
                sb.append("\t<TD bgcolor=E8E8E8><FONT face=\"Verdana, Arial, Helvetica, sans-serif\">").append(stf.format((java.util.Date)value)).append("</FONT></TD>").append(newline);
            }
            else {
                sb.append("\t<TD bgcolor=E8E8E8><FONT face=\"Verdana, Arial, Helvetica, sans-serif\">").append(encodeText(value.toString())).append("</FONT></TD>").append(newline);
            }
          }
          else
            sb.append("\t<TD bgcolor=E8E8E8>&nbsp;</TD>").append(newline);
        }
        sb.append("</TR>").append(newline);

        rownum++;
      }
      start = start+((VOListResponse)response).getRows().size();

      if ( !((VOListResponse)response).isMoreRows() )
        break;
    }
    while (rownum<opt.getMaxRows());

    sb.append("</TABLE>").append("</BODY>").append(newline).append("</HTML>").append(newline);


    byte[] doc = sb.toString().getBytes();
    return doc;
  }


  /**
   * Encode text with XML escape chars.
   */
  private String encodeText(String text) {
    StringBuffer sb = new StringBuffer("");
    for(int i=0;i<text.length();i++)
      if (text.charAt(i)>=46 && text.charAt(i)<=90 ||
          text.charAt(i)>=97 && text.charAt(i)<=122)
        sb.append(text.charAt(i));
      else
        sb.append("&#").append((int)text.charAt(i)).append(";");
    return sb.toString();
  }


}
