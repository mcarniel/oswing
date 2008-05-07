package org.openswing.swing.importdata.java;

import java.io.*;
import java.util.*;
import java.text.*;


/**
 * <p>Title: OpenSwing Framework</p>
 * <p>Description: This class is used to import grid data from a text file having CSV format (with ";" or "," separator).</p>
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
public class ImportFromCSV {

  private String format = null;

  SimpleDateFormat sdfdate = null;
  SimpleDateFormat sdftime = null;
  SimpleDateFormat sdfdatetime = null;


  public ImportFromCSV(String format,SimpleDateFormat sdfdate,SimpleDateFormat sdftime,SimpleDateFormat sdfdatetime) {
    this.format = format;
    this.sdfdate = sdfdate;
    this.sdftime = sdftime;
    this.sdfdatetime = sdfdatetime;
  }


  public ArrayList importData(int[] colTypes,ArrayList importAttrColumns,InputStream in) throws Throwable {
    BufferedInputStream bis = new BufferedInputStream(in);
    ArrayList rows = new ArrayList();
    char c;
    char lastchar = (char)-1;
    char dq = '\"';
    char nl = '\n';
    char cr = '\r';
    char delim = ',';
    if (format.equals(ImportOptions.CSV_FORMAT2))
      delim = ';';
    StringBuffer sb = new StringBuffer();
    boolean isnewline = false;
    boolean escape = false;
    ArrayList row = new ArrayList();
    int ichar = -1;
    while ( (ichar=bis.read()) != -1) {
      c = (char)ichar;
      if (c==dq) {
        if (!escape)
          escape = true;
        else if (sb.length()>0 && sb.charAt(sb.length()-1)==dq)
          ;
        else
          sb.append(dq);
      }
      else if (c==delim) {
        if (!escape) {
          row.add(sb);
          sb = new StringBuffer();
        }
        else {
          if (sb.length()>0 && sb.charAt(sb.length()-1)==dq) {
            sb = sb.delete(sb.length()-1,sb.length()-1);
            escape = false;
          }
          else
            sb.append(delim);
        }
      }
      else if (c==nl || c==cr) {
        if (escape)
          sb.append(c);
        else if (lastchar!=nl && lastchar!=cr) {
          row.add(sb);
          sb = new StringBuffer();
          escape = false;
          isnewline = true;
        }
      }
      else
        sb.append(c);

      // 123,abc,12-10-2008,,
      // "12,3","""abc""",12-10-2008,"abc\ncde",

      if (isnewline) {
        isnewline = false;
        rows.add( decodeRow(row,colTypes,importAttrColumns) );
        row.clear();
      }

      lastchar = c;
    }

    if (row.size()>0) {
      rows.add( decodeRow(row,colTypes,importAttrColumns) );
    }


    return rows;
  }


  private Object[] decodeRow(ArrayList row,int[] colTypes,ArrayList importAttrColumns) throws Throwable {
    Object[] rowobj = new Object[importAttrColumns.size()];
    for(int j=0;j<importAttrColumns.size();j++) {
      rowobj[j] = row.size()>j?row.get(j).toString():null;
      if (rowobj[j]!=null) {
        if (rowobj[j].equals(""))
          rowobj[j] = null;
        else {
          if (colTypes[j]==ImportOptions.TYPE_TEXT)
            rowobj[j] = rowobj[j].toString();
          if (colTypes[j]==ImportOptions.TYPE_DATE)
            rowobj[j] = sdfdate.parse(rowobj[j].toString());
          else if (colTypes[j]==ImportOptions.TYPE_DATE_TIME)
            rowobj[j] = sdfdatetime.parse(rowobj[j].toString());
          else if (colTypes[j]==ImportOptions.TYPE_TIME)
            rowobj[j] = sdftime.parse(rowobj[j].toString());
          else if (colTypes[j]==ImportOptions.TYPE_INT)
            rowobj[j] = new Long(rowobj[j].toString());
          else if (colTypes[j]==ImportOptions.TYPE_DEC)
            rowobj[j] = new Double(rowobj[j].toString());
          else if (colTypes[j]==ImportOptions.TYPE_CHECK)
            rowobj[j] = rowobj[j];
          else if (colTypes[j]==ImportOptions.TYPE_COMBO)
            rowobj[j] = rowobj[j];
          else if (colTypes[j]==ImportOptions.TYPE_LOOKUP)
            rowobj[j] = rowobj[j];
          else if (colTypes[j]==ImportOptions.TYPE_PERC)
            rowobj[j] = new Double(rowobj[j].toString());
          else if (colTypes[j]==ImportOptions.TYPE_CURRENCY)
            rowobj[j] = new Double(rowobj[j].toString());
          else if (colTypes[j]==ImportOptions.TYPE_FORMATTED_TEXT)
            rowobj[j] = rowobj[j].toString();
          else if (colTypes[j]==ImportOptions.TYPE_MULTI_LINE_TEXT)
            rowobj[j] = rowobj[j].toString();
          else if (colTypes[j]==ImportOptions.TYPE_PROGRESS_BAR)
            rowobj[j] = new Double(rowobj[j].toString());
          else if (colTypes[j]==ImportOptions.TYPE_COMBO_VO)
            rowobj[j] = rowobj[j];
        }
      }
    }
    return rowobj;
  }

}
