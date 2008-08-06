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
import com.lowagie.text.pdf.*;
import org.openswing.swing.util.client.ClientSettings;


/**
 * <p>Title: OpenSwing Framework</p>
 * <p>Description: This class is used to export grid data in PDF format.</p>
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
public class ExportToPDF {


  public ExportToPDF() {
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
    ByteArrayOutputStream baos = new ByteArrayOutputStream();
    PdfWriter w = PdfWriter.getInstance(document,baos);
    document.open();

    PdfPTable table = new PdfPTable(opt.getExportColumns().size());
    table.setWidths(headerwidths);
    table.setWidthPercentage(90);
    table.getDefaultCell().setBorderWidth(2);
    table.getDefaultCell().setBorderColor(Color.black);
    table.getDefaultCell().setGrayFill(ClientSettings.EXPORT_TO_PDF_ADAPTER.getHeaderGrayFill());
    table.getDefaultCell().setPadding(3);
    table.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
    table.getDefaultCell().setVerticalAlignment(Element.ALIGN_MIDDLE);

    for(int i=0;i<opt.getExportColumns().size();i++)
      table.addCell(
        new Phrase(
          opt.getExportColumns().get(i).toString(),
          ClientSettings.EXPORT_TO_PDF_ADAPTER.getHeaderFont(
            opt.getExportAttrColumns().get(i).toString()
          )
        )
      );

    table.setHeaderRows(1);
    table.getDefaultCell().setBorderWidth(1);
    table.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
    table.getDefaultCell().setVerticalAlignment(Element.ALIGN_TOP);

    for(int j=0;j<opt.getTopRows().size();j++) {
      // create a row for each top rows...
      table.getDefaultCell().setGrayFill(ClientSettings.EXPORT_TO_PDF_ADAPTER.getTopRowsGrayFill(j));
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
          table.getDefaultCell().setGrayFill(ClientSettings.EXPORT_TO_PDF_ADAPTER.getEvenRowsGrayFill());
          even = false;
        } else {
          table.getDefaultCell().setGrayFill(ClientSettings.EXPORT_TO_PDF_ADAPTER.getOddRowsGrayFill());
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
      table.getDefaultCell().setGrayFill(ClientSettings.EXPORT_TO_PDF_ADAPTER.getBottomRowsGrayFill(j));
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


    document.add(table);

    document.close();
    return baos.toByteArray();
  }


  /**
   * Append current row to result.
   * @return current row to append
   */
  private void appendRow(
    PdfPTable table,
    Object vo,
    ExportOptions opt,
    Hashtable gettersMethods,
    SimpleDateFormat sdf,
    SimpleDateFormat sdatf,
    SimpleDateFormat stf
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
        if (obj instanceof Date ||
                 obj instanceof java.util.Date ||
                 obj instanceof java.sql.Timestamp) {
          type = ((Integer)opt.getColumnsType().get(opt.getExportAttrColumns().get(i))).intValue();
          if (type==opt.TYPE_DATE) {
            table.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(new Phrase(sdf.format((java.util.Date)obj),ClientSettings.EXPORT_TO_PDF_ADAPTER.getRowFont(aName)));
          }
          else if (type==opt.TYPE_DATE_TIME) {
            table.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(new Phrase(sdatf.format((java.util.Date)obj),ClientSettings.EXPORT_TO_PDF_ADAPTER.getRowFont(aName)));
          }
          else if (type==opt.TYPE_TIME) {
            table.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(new Phrase(stf.format((java.util.Date)obj),ClientSettings.EXPORT_TO_PDF_ADAPTER.getRowFont(aName)));
          }
        }
        else {
          table.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
          table.addCell(new Phrase(obj.toString(),ClientSettings.EXPORT_TO_PDF_ADAPTER.getRowFont(aName)));
        }
      }
      else {
        table.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
        table.addCell(new Phrase("",ClientSettings.EXPORT_TO_PDF_ADAPTER.getRowFont(aName)));
      }
    }

  }




}
