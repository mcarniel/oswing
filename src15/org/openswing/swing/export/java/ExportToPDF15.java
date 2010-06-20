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
public class ExportToPDF15 {


  public ExportToPDF15() {
  }


  /**
   * @param opt export options
   * @return Excel document, as byte array
   */
  public byte[] getDocument(ExportOptions opt) throws Throwable {

//    Document document = new Document(new Rectangle(total+30,PageSize.A4.height()));
    Document document = new Document(new Rectangle(PageSize.A4.getWidth(),PageSize.A4.getHeight()));
    ByteArrayOutputStream baos = new ByteArrayOutputStream();
    PdfWriter w = PdfWriter.getInstance(document,baos);
    document.open();

    Object obj = null;
    for(int i=0;i<opt.getComponentsExportOptions().size();i++) {
      obj = opt.getComponentsExportOptions().get(i);
      processComponent(null,0,document,opt,obj);
    }

    document.close();
    return baos.toByteArray();
  }


  private void processComponent(PdfPTable table,int parentTableCols,Document document,ExportOptions opt,Object obj) throws Throwable {
    if (obj!=null) {
      GridExportCallbacks callbacks = null;
      if (obj instanceof GridExportOptions) {
        callbacks = (GridExportCallbacks)((GridExportOptions)obj).getCallbacks();
        if (callbacks!=null)
          processComponent(table,parentTableCols,document,opt,callbacks.getHeaderComponent());
        prepareGrid(table,parentTableCols,document,opt,(GridExportOptions)obj);
        if (callbacks!=null)
          processComponent(table,parentTableCols,document,opt,callbacks.getFooterComponent());
      }
      else if (obj instanceof ComponentExportOptions)
        prepareGenericComponent(table,parentTableCols,document,opt,(ComponentExportOptions)obj);
      else
        return;

      if (table!=null) {
        PdfPCell cell = new PdfPCell(new Paragraph("\n"));
        cell.setColspan(parentTableCols);
        table.addCell(cell);
      }
      else
        document.add(new Paragraph("\n"));
    }
  }


  private void prepareGenericComponent(PdfPTable parentTable,int parentTableCols,Document document,ExportOptions exportOptions,ComponentExportOptions opt) throws Throwable {
    if (opt.getCellsContent()==null ||
        opt.getCellsContent().length==0)
      return;

    int cols = opt.getCellsContent()[0].length;
    Object[] row = null;
    Object obj = null;
    SimpleDateFormat sdatf = new SimpleDateFormat(exportOptions.getDateTimeFormat());
    int[] headerwidths = new int[cols];
    for(int i=0;i<headerwidths.length;i++)
      headerwidths[i] = (int)PageSize.A4.getWidth()/cols;

    PdfPTable table = new PdfPTable(cols);
    table.setWidths(headerwidths);
    table.setWidthPercentage(90);
    table.getDefaultCell().setBorderWidth(2);
    table.getDefaultCell().setBorderColor(Color.black);
    table.getDefaultCell().setGrayFill(exportOptions.getExportToPDFAdapter().getHeaderGrayFill());
    table.getDefaultCell().setPadding(3);
    table.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
    table.getDefaultCell().setVerticalAlignment(Element.ALIGN_MIDDLE);
    table.setHeaderRows(0);
    table.getDefaultCell().setBorderWidth(0);
    table.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
    table.getDefaultCell().setVerticalAlignment(Element.ALIGN_TOP);

    for(int i=0;i<opt.getCellsContent().length;i++) {
      row = opt.getCellsContent()[i];
      for(int j=0;j<row.length;j++) {
        obj = row[j];

        if (obj!=null) {
          if (obj instanceof Date ||
                   obj instanceof java.util.Date ||
                   obj instanceof java.sql.Timestamp) {
            table.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(new Phrase(sdatf.format((java.util.Date)obj),(Font)exportOptions.getExportToPDFAdapter().getGenericComponentFont(i,j,obj)));
          }
          else {
            table.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
            table.addCell(new Phrase(obj.toString(),(Font)exportOptions.getExportToPDFAdapter().getGenericComponentFont(i,j,obj)));
          }
        }
        else {
          table.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
          table.addCell(new Phrase("",(Font)exportOptions.getExportToPDFAdapter().getGenericComponentFont(i,j,null)));
        }

      }
    }

    if (parentTable!=null) {
      PdfPCell cell = new PdfPCell(table);
      cell.setColspan(parentTableCols);
      parentTable.addCell(cell);
    }
    else
      document.add(table);
  }


  private void prepareGrid(PdfPTable parentTable,int parentTableCols,Document document,ExportOptions exportOptions,GridExportOptions opt) throws Throwable {
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


    int headerwidths[] = new int[opt.getExportColumns().size()];
    int total = 0;
    for(int i=0;i<opt.getExportColumns().size();i++) {
      headerwidths[i] = Math.max(
          opt.getExportColumns().get(i).toString().length()*10,
          ((Integer)opt.getColumnsWidth().get(opt.getExportAttrColumns().get(i))).intValue()
      );
      total += headerwidths[i];
    }

    Paragraph line = null;
    if (opt.getTitle()!=null && !opt.getTitle().equals("")) {
      line = new Paragraph(opt.getTitle(),(Font)exportOptions.getExportToPDFAdapter().getFontTitle());line.setAlignment(Element.ALIGN_CENTER);
      document.add(line);
      document.add(new Paragraph("\n"));
    }
    String[] filters = opt.getFilteringConditions();
    if (filters!=null) {
      for(int i=0;i<filters.length;i++) {
        line = new Paragraph(filters[i]);
        document.add(line);
      }
      document.add(new Paragraph("\n"));
    }



    PdfPTable table = new PdfPTable(opt.getExportColumns().size());
    table.setWidths(headerwidths);
    table.setWidthPercentage(90);
    table.getDefaultCell().setBorderWidth(2);
    table.getDefaultCell().setBorderColor(Color.black);
    table.getDefaultCell().setGrayFill(exportOptions.getExportToPDFAdapter().getHeaderGrayFill());
    table.getDefaultCell().setPadding(3);
    table.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
    table.getDefaultCell().setVerticalAlignment(Element.ALIGN_MIDDLE);

    for(int i=0;i<opt.getExportColumns().size();i++)
      table.addCell(
        new Phrase(
          opt.getExportColumns().get(i).toString(),
          (Font)exportOptions.getExportToPDFAdapter().getHeaderFont(
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
      table.getDefaultCell().setGrayFill(exportOptions.getExportToPDFAdapter().getTopRowsGrayFill(j));
      vo = opt.getTopRows().get(j);
      appendRow(
        document,
        exportOptions,
        table,
        vo,
        opt,
        gettersMethods,
        sdf,
        sdatf,
        stf,
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

      boolean even = false;

      for(int j=0;j<((VOListResponse)response).getRows().size();j++) {
        if (even) {
          table.getDefaultCell().setGrayFill(exportOptions.getExportToPDFAdapter().getEvenRowsGrayFill());
          even = false;
        } else {
          table.getDefaultCell().setGrayFill(exportOptions.getExportToPDFAdapter().getOddRowsGrayFill());
          even = true;
        }

        vo = ((VOListResponse)response).getRows().get(j);

        appendRow(
          document,
          exportOptions,
          table,
          vo,
          opt,
          gettersMethods,
          sdf,
          sdatf,
          stf,
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
      table.getDefaultCell().setGrayFill(exportOptions.getExportToPDFAdapter().getBottomRowsGrayFill(j));
      vo = opt.getBottomRows().get(j);
      appendRow(
        document,
        exportOptions,
        table,
        vo,
        opt,
        gettersMethods,
        sdf,
        sdatf,
        stf,
        j,
        2
      );
    }

    if (parentTable!=null) {
      PdfPCell cell = new PdfPCell(table);
      cell.setColspan(parentTableCols);
      parentTable.addCell(cell);
    }
    else
      document.add(table);

  }


  /**
   * Append current row to result.
   * @return current row to append
   */
  private void appendRow(
    Document document,
    ExportOptions exportOptions,
    PdfPTable table,
    Object vo,
    GridExportOptions opt,
    Hashtable gettersMethods,
    SimpleDateFormat sdf,
    SimpleDateFormat sdatf,
    SimpleDateFormat stf,
    int row,
    int tableType // 0 = header, 1 = body, 2 = footer
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
            table.addCell(new Phrase(sdf.format((java.util.Date)obj),(Font)exportOptions.getExportToPDFAdapter().getRowFont(aName)));
          }
          else if (type==opt.TYPE_DATE_TIME) {
            table.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(new Phrase(sdatf.format((java.util.Date)obj),(Font)exportOptions.getExportToPDFAdapter().getRowFont(aName)));
          }
          else if (type==opt.TYPE_TIME) {
            table.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(new Phrase(stf.format((java.util.Date)obj),(Font)exportOptions.getExportToPDFAdapter().getRowFont(aName)));
          }
        }
        else {
          table.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
          table.addCell(new Phrase(obj.toString(),(Font)exportOptions.getExportToPDFAdapter().getRowFont(aName)));
        }
      }
      else {
        table.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
        table.addCell(new Phrase("",(Font)exportOptions.getExportToPDFAdapter().getRowFont(aName)));
      }

    }

    if (opt.getCallbacks()!=null) {
      if (tableType==0)
        processComponent(
          table,
          opt.getExportColumns().size(),
          document,
          exportOptions,
          opt.getCallbacks().getComponentPerRowInHeader(
            (ValueObject)vo,
            row
          )
        );
      else if (tableType==1)
        processComponent(
          table,
          opt.getExportColumns().size(),
          document,
          exportOptions,
          opt.getCallbacks().getComponentPerRow(
            (ValueObject)vo,
            row
          )
        );
      else if (tableType==2)
        processComponent(
          table,
          opt.getExportColumns().size(),
          document,
          exportOptions,
          opt.getCallbacks().getComponentPerRowInFooter(
            (ValueObject)vo,
            row
          )
        );
    }

  }




}
