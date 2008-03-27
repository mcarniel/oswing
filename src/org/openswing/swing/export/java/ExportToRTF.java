package org.openswing.swing.export.java;

import java.io.*;
import java.lang.reflect.*;
import java.sql.Date;
import java.text.*;
import java.util.*;

import java.awt.*;

import org.openswing.swing.message.receive.java.*;
import org.openswing.swing.message.send.java.*;
import com.lowagie.text.*;
import com.lowagie.text.Font;
import com.lowagie.text.Rectangle;
import com.lowagie.text.rtf.*;


/**
 * <p>Title: OpenSwing Framework</p>
 * <p>Description: This class is used to export grid data in RTF format.</p>
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
public class ExportToRTF {


  public ExportToRTF() {
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

    SimpleDateFormat sdf = new SimpleDateFormat(opt.getDateFormat());
    SimpleDateFormat sdatf = new SimpleDateFormat(opt.getDateTimeFormat());
    SimpleDateFormat stf = new SimpleDateFormat(opt.getTimeFormat());

    int headerwidths[] = new int[opt.getExportColumns().size()];
    int total = 0;
    for(int i=0;i<opt.getExportColumns().size();i++) {
      headerwidths[i] = Math.max(
          opt.getExportColumns().get(i).toString().length()*10,
          ((Integer)opt.getColumnsWidth().get(opt.getExportAttrColumns().get(i))).intValue()
      );
      total += headerwidths[i];
    }

    Document document = new Document(new Rectangle(total+30,PageSize.A4.height()));
//    Document document = new Document();
    ByteArrayOutputStream baos = new ByteArrayOutputStream();
    RtfWriter2 w = RtfWriter2.getInstance(document,baos);
    document.open();
    Table table = new Table(opt.getExportColumns().size());
    table.setWidths(headerwidths);
    table.setBorderWidth(2);
    table.setBorderColor(Color.black);
    table.setGrayFill(0.75f);
    table.setPadding(3);

    Phrase cell = null;
    for(int i=0;i<opt.getExportColumns().size();i++) {
      cell = new Phrase(opt.getExportColumns().get(i).toString(),new Font(Font.HELVETICA,Font.DEFAULTSIZE,Font.BOLD));
      table.addCell(cell);
    }
//    table.setHeaderRows(1);
//    table.getDefaultCell().setBorderWidth(1);
//    table.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
//    table.getDefaultCell().setVerticalAlignment(Element.ALIGN_TOP);


    for(int j=0;j<opt.getTopRows().size();j++) {
      // create a row for each top rows...
      vo = opt.getTopRows().get(j);
      appendRow(
        table,
        vo,
        opt,
        gettersMethods,
        sdf,
        sdatf,
        stf
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

      boolean even = false;

      for(int j=0;j<((VOListResponse)response).getRows().size();j++) {
        if (even) {
          even = false;
        } else {
          even = true;
        }

        vo = ((VOListResponse)response).getRows().get(j);

        appendRow(
          table,
          vo,
          opt,
          gettersMethods,
          sdf,
          sdatf,
          stf
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
        table,
        vo,
        opt,
        gettersMethods,
        sdf,
        sdatf,
        stf
      );
    }


//    rtfTable.importTable(table,total);
    document.add(table);

//    document.add(table);

    document.close();
    return baos.toByteArray();
  }


  /**
   * Append current row to result.
   * @return current row to append
   */
  private void appendRow(
    Table table,
    Object vo,
    ExportOptions opt,
    Hashtable gettersMethods,
    SimpleDateFormat sdf,
    SimpleDateFormat sdatf,
    SimpleDateFormat stf
  ) throws Throwable {
    int type;
    Object value = null;

    for(int i=0;i<opt.getExportColumns().size();i++) {
      value = ((Method)gettersMethods.get(opt.getExportAttrColumns().get(i))).invoke(vo,new Object[0]);
      if (value!=null) {
        if (value instanceof Date ||
                 value instanceof java.util.Date ||
                 value instanceof java.sql.Timestamp) {
          type = ((Integer)opt.getColumnsType().get(opt.getExportAttrColumns().get(i))).intValue();
          if (type==opt.TYPE_DATE) {
//                table.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(new Phrase(sdf.format((java.util.Date)value),new Font(Font.HELVETICA)));
          }
          else if (type==opt.TYPE_DATE_TIME) {
//                table.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(new Phrase(sdatf.format((java.util.Date)value),new Font(Font.HELVETICA)));
          }
          else if (type==opt.TYPE_TIME) {
//                table.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(new Phrase(stf.format((java.util.Date)value),new Font(Font.HELVETICA)));
          }
        }
        else {
//              table.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
          table.addCell(new Phrase(value.toString(),new Font(Font.HELVETICA)));
        }
      }
      else {
        table.addCell(new Phrase("",new Font(Font.HELVETICA)));
      }
    }
  }

}
