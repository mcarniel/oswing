package org.openswing.swing.export.java;

import org.openswing.swing.message.receive.java.*;
import org.openswing.swing.message.send.java.GridParams;
import java.util.Hashtable;
import java.lang.reflect.Method;
import java.sql.Date;
import java.text.SimpleDateFormat;

import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Element;
import com.lowagie.text.Font;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Phrase;
import com.lowagie.text.pdf.BaseFont;
import com.lowagie.text.pdf.PdfContentByte;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfPageEventHelper;
import com.lowagie.text.pdf.PdfTemplate;
import com.lowagie.text.pdf.PdfWriter;
import java.awt.Color;
import java.io.ByteArrayOutputStream;
import com.lowagie.text.Rectangle;
import com.lowagie.text.PageSize;


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
    table.getDefaultCell().setGrayFill(0.75f);
    table.getDefaultCell().setPadding(3);
    table.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
    table.getDefaultCell().setVerticalAlignment(Element.ALIGN_MIDDLE);

    for(int i=0;i<opt.getExportColumns().size();i++)
      table.addCell(new Phrase(opt.getExportColumns().get(i).toString(),new Font(Font.HELVETICA,Font.DEFAULTSIZE,Font.BOLD)));

    table.setHeaderRows(1);
    table.getDefaultCell().setBorderWidth(1);
    table.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
    table.getDefaultCell().setVerticalAlignment(Element.ALIGN_TOP);

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
          table.getDefaultCell().setGrayFill(0.95f);
          even = false;
        } else {
          table.getDefaultCell().setGrayFill(1.00f);
          even = true;
        }

        vo = ((VOListResponse)response).getRows().get(j);
        for(int i=0;i<opt.getExportColumns().size();i++) {
          value = ((Method)gettersMethods.get(opt.getExportAttrColumns().get(i))).invoke(vo,new Object[0]);
          if (value!=null) {
            if (value instanceof Date ||
                     value instanceof java.util.Date ||
                     value instanceof java.sql.Timestamp) {
              type = ((Integer)opt.getColumnsType().get(opt.getExportAttrColumns().get(i))).intValue();
              if (type==opt.TYPE_DATE) {
                table.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
                table.addCell(new Phrase(sdf.format((java.util.Date)value),new Font(Font.HELVETICA)));
              }
              else if (type==opt.TYPE_DATE_TIME) {
                table.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
                table.addCell(new Phrase(sdatf.format((java.util.Date)value),new Font(Font.HELVETICA)));
              }
              else if (type==opt.TYPE_TIME) {
                table.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
                table.addCell(new Phrase(stf.format((java.util.Date)value),new Font(Font.HELVETICA)));
              }
            }
            else {
              table.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
              table.addCell(new Phrase(value.toString(),new Font(Font.HELVETICA)));
            }
          }
          else {
            table.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
            table.addCell(new Phrase("",new Font(Font.HELVETICA)));
          }
        }

        rownum++;
      }

      start = start+((VOListResponse)response).getRows().size();

      if ( !((VOListResponse)response).isMoreRows() )
        break;
    }
    while (rownum<opt.getMaxRows());

    document.add(table);

    document.close();
    return baos.toByteArray();
  }


}
