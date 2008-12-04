package org.openswing.swing.pivottable.tablemodelreaders.server;

import javax.swing.table.TableModel;
import java.io.*;
import org.openswing.swing.util.dataconverters.java.DataConverter;
import org.openswing.swing.util.dataconverters.java.*;
import org.openswing.swing.pivottable.java.InputFilter;
import java.util.Iterator;
import java.util.HashMap;


/**
 * <p>Title: OpenSwing Framework</p>
 * <p>Description: CSV File reader, used in PivotTableEngine to read data to analyze.</p>
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
public class CSVFileReader implements Reader {

  /** CSV file to read */
  private File csvFile = null;

  /** input stream related to CSV file */
  private BufferedReader in = null;

  /** current line just read */
  private String currentLine = null;

  /** current line just read */
  private Object[] currentLineTokens = null;

  /** attribute names */
  private String[] attributeNames = null;

  /** data converters */
  private DataConverter[] dataConverters = null;

  /** collection of pairs <column name,column index> */
  private HashMap indexes = new HashMap();

  /** fields delimiter */
  private static final String DELIM = ";";


  /**
   * Create a reader of a CSV file, used in PivotTableEngine.
   * @param csvFile CSV file to read
   * @param attribute names that identifies CSF file columns
   * @param dataConverters converters to use to parse data read from file
   */
  public CSVFileReader(File csvFile,String[] attributeNames,DataConverter[] dataConverters) {
    this.csvFile = csvFile;
    this.attributeNames = attributeNames;
    this.dataConverters = dataConverters;
    for(int i=0;i<attributeNames.length;i++)
      indexes.put(attributeNames[i],new Integer(i));
  }


  /**
   * Initialize reading.
   * @param inputFilter optional input filter, used to skip rows
   * @return <code>true</code> if specified file has been correctly opened, <code>false</code> otherwise
   */
  public boolean initializeScrolling(InputFilter inputFilter) {
    try {
      in = new BufferedReader(new InputStreamReader(new FileInputStream(csvFile)));
      return true;
    }
    catch (Exception ex) {
      return false;
    }
  }


  /**
   * Get next row to read.
   * Note: this method can be called ONLY if "initializeScrolling" method has already been invoked.
   * @param inputFilter optional input filter, used to skip rows
   * @return <code>true</code> if there exists a row, <code>false</code> if no other rows are available
   */
  public final boolean nextRow(InputFilter inputFilter) {
    try {
      Iterator it = null;

      currentLine = in.readLine();
      splitLine();
      boolean skipRow;
      while(currentLine!=null && inputFilter!=null) {
        it = inputFilter.getFilteredColumnNames();
        String colName = null;
        skipRow = false;
        while(it.hasNext()) {
          colName = it.next().toString();
          skipRow = inputFilter.skipRow(colName,getValueAt(((Integer)indexes.get(colName)).intValue()));
          if (skipRow)
            break;
        }
        if (!skipRow)
          break;
        currentLine = in.readLine();
        splitLine();
      }
      if (currentLine==null)
        in.close();
    }
    catch (Exception ex) {
      currentLineTokens = null;
      return false;
    }
    return currentLine!=null;
  }


  private void splitLine() {
    if (currentLine==null)
      currentLineTokens = null;
    else {
      try{
        if (currentLine.trim().length()==0 ){
          currentLineTokens = null;
          return;
        }
        if (currentLineTokens==null)
          currentLineTokens = new Object[attributeNames.length];
        else
          for(int i=0;i<currentLineTokens.length;i++)
            currentLineTokens[i] = null;
        boolean bContinue = true;
        int i = 0;
        int j = 0;
        int ind;
        while(bContinue){
          ind = currentLine.indexOf(DELIM,j);
          if(ind == -1){
            ind = currentLine.length();
            bContinue = false;
            if (j>ind)
              continue;
          } else {
            // found ;
            // check if pattern has an odd number of " chars, until ; char
            if (isOdd(currentLine,j,ind)) {
              // case: "xyz;xyz";
              ind = getRightEdge(currentLine,ind); // ind = index of char next to righr delimiter of "
            }

          }
          String value = ind==currentLine.length()?
                         currentLine.substring(j) :
                         currentLine.substring(j,ind);
          value = decodePattern(value); //replace "" patterns with "
          j =  ind+1;
          if (value!=null && value.length()==0)
            value = null;
          try {
            currentLineTokens[i] = dataConverters[i].decodeValue(value);
          } catch (DataConverterException ex) {
            currentLineTokens[i] = null;
          }
          i++;
          if (i>=currentLineTokens.length)
            break;
        }
      } catch(Exception ec){
        currentLineTokens = null;
      }
//      currentcurrentLineTokens = currentcurrentLine.split(",");
    }
  }


  /**
   * @return check if "pattern" argument has an odd or event number of " chars, until the ; char
   * Examples:
   * """x -> true
   * "x   -> true
   * ""x  -> false
   * """"x -> false
   * x" -> false
   */
  private boolean isOdd(String pattern,int index,int end) {
    int odd = 0;
    for(int i=index;i<end;i++)
      if (pattern.charAt(i)=='\"')
        odd++;
    return odd%2==1;
  }


  /**
   * @param index ; char index
   * @return index of char next to ", that is the right DELIMiter of the field
   * Examples:
   * "xyz;xyz";
   * "xyz"";xyz;xyz";
   * "xyz"";xyz;xyz"
   */
  private int getRightEdge(String pattern,int index) {
    int odd = 0;
    for(int i=index;i<pattern.length();i++)
      if (pattern.charAt(i)=='\"')
        odd++;
      else {
        if (odd%2==0)
          odd = 0;
        else
          return i;
      }
    return pattern.length();
  }


 /**
  * Search for "" patterns and replace them with "
  * Remove left/right " DELIMiters, if there exist.
  */
  private String decodePattern(String pattern) {
    if (pattern.length()==0)
      return pattern;
    StringBuffer aux = new StringBuffer("");
    for(int i=0;i<pattern.length();i++)
      if (pattern.charAt(i)=='\"' && i+1<pattern.length() && pattern.charAt(i+1)=='\"') {
        aux.append( "\"" );
        i++;
      }
      else
        aux.append( String.valueOf(pattern.charAt(i)) );
    if (aux.charAt(0)=='\"' && aux.charAt(aux.length()-1)=='\"')
      return aux.substring(1,aux.length()-1);
    return aux.toString();
  }
















  /**
   * @param col column index
   * @return value stored at column index, related to current row
   */
  public final Object getValueAt(int col) {
    if (currentLineTokens==null)
      return null;
    return currentLineTokens[col];
//    try {
//      return dataConverters[col].decodeValue(currentcurrentLineTokens[col]);
//    }
//    catch (DataConverterException ex) {
//      return null;
//    }
  }


  /**
   * @return number of columns defined in TableModel
   */
  public final int getColumnCount() {
    return attributeNames.length;
  }


  /**
   * @param column index
   * @return column name defined in TableModel, related to the specified column index
   */
  public final String getColumnName(int index) {
    return attributeNames[index];
  }


}
