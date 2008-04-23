package org.openswing.swing.export.java;

import java.io.*;
import java.lang.reflect.*;
import java.math.*;
import java.sql.Date;
import java.util.*;

import org.apache.poi.hssf.usermodel.*;
import org.openswing.swing.message.receive.java.*;
import org.openswing.swing.message.send.java.*;


/**
 * <p>Title: OpenSwing Framework</p>
 * <p>Description: This class is used to export grid data in excel format.</p>
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
public class ExportToExcel {


  public ExportToExcel() {
  }


  /**
   * @param opt export options
   * @return Excel document, as byte array
   */
  public byte[] getDocument(ExportOptions opt) throws Throwable {
    // create a new workbook
    HSSFWorkbook wb = new HSSFWorkbook();
    // create a new sheet
    HSSFSheet s = wb.createSheet();
    // declare a row object reference
    HSSFRow r = null;
    // declare a cell object reference
    HSSFCell c = null;
    // create 3 cell styles
    HSSFCellStyle csText = wb.createCellStyle();
    csText.setDataFormat(HSSFDataFormat.getBuiltinFormat("text"));
    csText.setBorderBottom(HSSFCellStyle.BORDER_THIN);
    csText.setBorderLeft(HSSFCellStyle.BORDER_THIN);
    csText.setBorderRight(HSSFCellStyle.BORDER_THIN);
    csText.setBorderTop(HSSFCellStyle.BORDER_THIN);

    HSSFCellStyle csTitle = wb.createCellStyle();
    csTitle.setDataFormat(HSSFDataFormat.getBuiltinFormat("text"));
    csTitle.setBorderBottom(HSSFCellStyle.BORDER_THIN);
    csTitle.setBorderLeft(HSSFCellStyle.BORDER_THIN);
    csTitle.setBorderRight(HSSFCellStyle.BORDER_THIN);
    csTitle.setBorderTop(HSSFCellStyle.BORDER_THIN);
    HSSFFont f = wb.createFont();
    f.setBoldweight(f.BOLDWEIGHT_NORMAL);
    csTitle.setFont(f);

    HSSFCellStyle csBool = wb.createCellStyle();
    csBool.setBorderBottom(HSSFCellStyle.BORDER_THIN);
    csBool.setBorderLeft(HSSFCellStyle.BORDER_THIN);
    csBool.setBorderRight(HSSFCellStyle.BORDER_THIN);
    csBool.setBorderTop(HSSFCellStyle.BORDER_THIN);

    HSSFCellStyle csDecNum = wb.createCellStyle();
    csDecNum.setDataFormat(wb.createDataFormat().getFormat("#,##0.#####"));
    csDecNum.setBorderBottom(HSSFCellStyle.BORDER_THIN);
    csDecNum.setBorderLeft(HSSFCellStyle.BORDER_THIN);
    csDecNum.setBorderRight(HSSFCellStyle.BORDER_THIN);
    csDecNum.setBorderTop(HSSFCellStyle.BORDER_THIN);

    HSSFCellStyle csIntNum = wb.createCellStyle();
    csIntNum.setDataFormat(wb.createDataFormat().getFormat("#,##0"));
    csIntNum.setBorderBottom(HSSFCellStyle.BORDER_THIN);
    csIntNum.setBorderLeft(HSSFCellStyle.BORDER_THIN);
    csIntNum.setBorderRight(HSSFCellStyle.BORDER_THIN);
    csIntNum.setBorderTop(HSSFCellStyle.BORDER_THIN);

    HSSFCellStyle csDate = wb.createCellStyle();
    csDate.setDataFormat(HSSFDataFormat.getBuiltinFormat("m/d/yy"/*opt.getDateFormat()*/));
    csDate.setBorderBottom(HSSFCellStyle.BORDER_THIN);
    csDate.setBorderLeft(HSSFCellStyle.BORDER_THIN);
    csDate.setBorderRight(HSSFCellStyle.BORDER_THIN);
    csDate.setBorderTop(HSSFCellStyle.BORDER_THIN);

    HSSFCellStyle csTime = wb.createCellStyle();
    csTime.setDataFormat(HSSFDataFormat.getBuiltinFormat(opt.getTimeFormat().equals("HH:mm")?"h:mm":"h:mm AM/PM"));
    csTime.setBorderBottom(HSSFCellStyle.BORDER_THIN);
    csTime.setBorderLeft(HSSFCellStyle.BORDER_THIN);
    csTime.setBorderRight(HSSFCellStyle.BORDER_THIN);
    csTime.setBorderTop(HSSFCellStyle.BORDER_THIN);

    HSSFCellStyle csDateTime = wb.createCellStyle();
    csDateTime.setDataFormat(HSSFDataFormat.getBuiltinFormat("m/d/yy h:mm"/*opt.getDateTimeFormat()*/));
    csDateTime.setBorderBottom(HSSFCellStyle.BORDER_THIN);
    csDateTime.setBorderLeft(HSSFCellStyle.BORDER_THIN);
    csDateTime.setBorderRight(HSSFCellStyle.BORDER_THIN);
    csDateTime.setBorderTop(HSSFCellStyle.BORDER_THIN);

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
    boolean firstRow = true;


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
        if (firstRow) {
          firstRow = false;
          // create the first row...
          r = s.createRow(rownum++);
          for(short i=0;i<opt.getExportColumns().size();i++) {
            c = r.createCell(i);
            c.setEncoding(HSSFWorkbook.ENCODING_UTF_16);
            c.setCellValue(opt.getExportColumns().get(i).toString());
            c.setCellStyle(csTitle);
          }


          for(int k=0;k<opt.getTopRows().size();k++) {
            // create a row for each top rows...
            vo = opt.getTopRows().get(k);
            appendRow(
              s,
              vo,
              opt,
              gettersMethods,
              csText,
              csBool,
              csDecNum,
              csIntNum,
              csDate,
              csTime,
              csDateTime,
              rownum
            );
            rownum++;
          }

        }

        // create a row
        vo = ((VOListResponse)response).getRows().get(j);

        appendRow(
          s,
          vo,
          opt,
          gettersMethods,
          csText,
          csBool,
          csDecNum,
          csIntNum,
          csDate,
          csTime,
          csDateTime,
          rownum
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
        s,
        vo,
        opt,
        gettersMethods,
        csText,
        csBool,
        csDecNum,
        csIntNum,
        csDate,
        csTime,
        csDateTime,
        rownum
      );
      rownum++;
    }


    // write the workbook to the output stream
    // close our file (don't blow out our file handles
    ByteArrayOutputStream out = new ByteArrayOutputStream();
    wb.write(out);
    byte[] doc = out.toByteArray();
    out.close();

    return doc;
  }


  /**
   * Append current row to result StringBuffer.
   * @return current row to append
   */
  private void appendRow(
    HSSFSheet s,
    Object vo,
    ExportOptions opt,
    Hashtable gettersMethods,
    HSSFCellStyle csText,
    HSSFCellStyle csBool,
    HSSFCellStyle csDecNum,
    HSSFCellStyle csIntNum,
    HSSFCellStyle csDate,
    HSSFCellStyle csTime,
    HSSFCellStyle csDateTime,
    int rownum
  ) throws Throwable {
    int type;
    Object value = null;
    HSSFRow r = null;
    HSSFCell c = null;
    r = s.createRow(rownum);
    for(short i=0;i<opt.getExportColumns().size();i++) {
      c = r.createCell(i);
      value = ((Method)gettersMethods.get(opt.getExportAttrColumns().get(i))).invoke(vo,new Object[0]);
      if (value!=null) {
        if (value instanceof String) {
          c.setEncoding(HSSFWorkbook.ENCODING_UTF_16);
          c.setCellValue(value.toString());
          c.setCellStyle(csText);
        }
        else if (value instanceof BigDecimal ||
                 value instanceof Double ||
                 value instanceof Float ||
                 value.getClass()==Double.TYPE ||
                 value.getClass()==Float.TYPE) {
          c.setCellValue(Double.parseDouble(value.toString()));
          c.setCellStyle(csDecNum);
        }
        else if (value instanceof Integer ||
                 value instanceof Short ||
                 value instanceof Long ||
                 value.getClass()==Integer.TYPE ||
                 value.getClass()==Short.TYPE ||
                 value.getClass()==Long.TYPE) {
          c.setCellValue(Double.parseDouble(value.toString()));
          c.setCellStyle(csIntNum);
        }
        else if (value instanceof Boolean) {
          c.setCellValue(((Boolean)value).booleanValue());
          c.setCellStyle(csBool);
        }
        else if (value.getClass().equals(boolean.class)) {
          c.setCellValue(((Boolean)value).booleanValue());
          c.setCellStyle(csBool);
        }
        else if (value instanceof Date ||
                 value instanceof java.util.Date ||
                 value instanceof java.sql.Timestamp) {
          c.setCellValue((Date)value);
          type = ((Integer)opt.getColumnsType().get(opt.getExportAttrColumns().get(i))).intValue();
          if (type==opt.TYPE_DATE)
            c.setCellStyle(csDate);
          else if (type==opt.TYPE_DATE_TIME)
            c.setCellStyle(csDateTime);
          else if (type==opt.TYPE_TIME)
            c.setCellStyle(csTime);
        }
      }
      else {
        c.setCellValue("");
        c.setCellStyle(csText);
      }

      // make this column a bit wider
      s.setColumnWidth( i, (short)(256/8*((Integer)opt.getColumnsWidth().get(opt.getExportAttrColumns().get(i))).shortValue()) );
    }

  }


}
