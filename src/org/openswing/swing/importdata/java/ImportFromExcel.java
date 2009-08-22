package org.openswing.swing.importdata.java;

import java.io.*;
import java.util.*;

import org.apache.poi.hssf.usermodel.*;


/**
 * <p>Title: OpenSwing Framework</p>
 * <p>Description: This class is used to import grid data from an excel document.</p>
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
public class ImportFromExcel {


  public ImportFromExcel() {
  }


  public ArrayList importData(int cols,InputStream in) throws Throwable {
    // read existing workbook
    HSSFWorkbook wb = new HSSFWorkbook(new BufferedInputStream(in));
    // retrieve existing sheet
    HSSFSheet s = wb.getSheetAt(0);

    int i=0;
    ArrayList rows = new ArrayList();
    Object[] rowobj = null;
    HSSFRow row = null;
    boolean rowEmpty = true;
    HSSFCell cell = null;
    while(true) {
      rowEmpty = true;
      rowobj = new Object[cols];
      row = s.getRow(i);
      if (row==null)
        break;
      for(short j=0;j<cols;j++) {
        cell = row.getCell(j);
        if (cell!=null) {
          if (cell.getCellType()==cell.CELL_TYPE_NUMERIC) {
              rowobj[j] = new Double(cell.getNumericCellValue());
          }
          else
            try {
              rowobj[j] = cell.getDateCellValue();
            }
            catch (Exception ex) {
              rowobj[j] = cell.getStringCellValue();
            }
        }

        if (rowobj[j]!=null)
          rowEmpty = false;
      }
      if (rowEmpty)
        break;
      rows.add(rowobj);
      i++;
    }

    return rows;
  }


}
