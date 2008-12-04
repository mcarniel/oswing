package org.openswing.swing.pivottable.cellspantable.client;

import java.util.*;
import java.awt.*;
import javax.swing.*;
import javax.swing.table.*;
import javax.swing.plaf.basic.*;
import javax.swing.event.*;


/**
 * <p>Title: OpenSwing Framework</p>
 * <p>Description: JTable with support for cells span.</p>
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
public class CellSpanTable extends JTable {


  public CellSpanTable() {
    setShowGrid(false);
    this.getColumnModel().setColumnMargin(0);
    this.setRowMargin(0);
//    setUI(new CellSpanTableUI());
//    getTableHeader().setReorderingAllowed(false);
//    setCellSelectionEnabled(true);
//    setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
  }


    public void setModel(TableModel model) {
      super.setModel(model);
      setUI(new CellSpanTableUI());
//      setShowGrid(false);
//      setGridSize(new Dimension(model.getColumnCount(),model.getRowCount()));
    }


    public Rectangle getCellRect(int row, int column, boolean includeSpacing) {
      Rectangle sRect = super.getCellRect(row,column,includeSpacing);
      if ((row <0) || (column<0) ||
          (getRowCount() <= row) || (getColumnCount() <= column)) {
          return sRect;
      }
      if (!isGridVisible(row,column)) {
        int temp_row    = row;
        int temp_column = column;
        row    += getSpan(temp_row,temp_column)[ROW];
        column += getSpan(temp_row,temp_column)[COLUMN];
      }
      int[] n = getSpan(row,column);

      int index = 0;
      int columnMargin = getColumnModel().getColumnMargin();

      Rectangle cellFrame = new Rectangle();
      int aCellHeight = rowHeight + rowMargin;
      cellFrame.y = row * aCellHeight;
      cellFrame.height = n[ROW] * aCellHeight;

      Enumeration enumeration = getColumnModel().getColumns();
      while (enumeration.hasMoreElements()) {
        TableColumn aColumn = (TableColumn)enumeration.nextElement();
        cellFrame.width = aColumn.getWidth() + columnMargin;
        if (index == column) break;
        cellFrame.x += cellFrame.width;
        index++;
      }
      try {
        for (int i = 0; i < n[COLUMN] - 1; i++) {
          TableColumn aColumn = (TableColumn) enumeration.nextElement();
          cellFrame.width += aColumn.getWidth() + columnMargin;
        }
      }
      catch (Exception ex) {
      }



      if (!includeSpacing) {
        Dimension spacing = getIntercellSpacing();
        cellFrame.setBounds(cellFrame.x +      spacing.width/2,
                            cellFrame.y +      spacing.height/2,
                            cellFrame.width -  spacing.width,
                            cellFrame.height - spacing.height);
      }
      return cellFrame;
    }


    private int[] rowColumnAtPoint(Point point) {
      int[] retValue = {-1,-1};
      int row = point.y / (rowHeight + rowMargin);
      if ((row <0)||(getRowCount() <= row)) return retValue;
      int column = getColumnModel().getColumnIndexAtX(point.x);

      if (isGridVisible(row,column)) {
        retValue[COLUMN] = column;
        retValue[ROW   ] = row;
        return retValue;
      }
      retValue[COLUMN] = column + getSpan(row,column)[COLUMN];
      retValue[ROW   ] = row + getSpan(row,column)[ROW];
      return retValue;
    }


    public int rowAtPoint(Point point) {
      return rowColumnAtPoint(point)[ROW];
    }
    public int columnAtPoint(Point point) {
      return rowColumnAtPoint(point)[COLUMN];
    }



    public void columnSelectionChanged(ListSelectionEvent e) {
      repaint();
    }

    public void valueChanged(ListSelectionEvent e) {
      int firstIndex = e.getFirstIndex();
      int  lastIndex = e.getLastIndex();
      if (firstIndex == -1 && lastIndex == -1) { // Selection cleared.
        repaint();
      }
      Rectangle dirtyRegion = getCellRect(firstIndex, 0, false);
      int numCoumns = getColumnCount();
      int index = firstIndex;
      for (int i=0;i<numCoumns;i++) {
        dirtyRegion.add(getCellRect(index, i, false));
      }
      index = lastIndex;
      for (int i=0;i<numCoumns;i++) {
        dirtyRegion.add(getCellRect(index, i, false));
      }
      repaint(dirtyRegion.x, dirtyRegion.y, dirtyRegion.width, dirtyRegion.height);
    }



















    public static final int ROW    = 0;
    public static final int COLUMN = 1;

    protected int rowSize;
    protected int columnSize;
    protected int[][][] span;




    protected void initValue() {
      for(int i=0; i<span.length;i++) {
        for(int j=0; j<span[i].length; j++) {
          span[i][j][COLUMN] = 1;
          span[i][j][ROW]    = 1;
        }
      }
    }


    //
    // CellSpan
    //
    public int[] getSpan(int row, int column) {
      if (isOutOfBounds(row, column)) {
        int[] ret_code = {1,1};
        return ret_code;
      }
      return span[row][column];
    }

    public void setSpan(int[] span, int row, int column) {
      if (isOutOfBounds(row, column)) return;
      this.span[row][column] = span;
    }

    public boolean isGridVisible(int row, int column) {
      if (isOutOfBounds(row, column)) return false;
      if ((span[row][column][COLUMN] < 1)
        ||(span[row][column][ROW]    < 1)) return false;
      return true;
    }

    public void combine(int[] rows, int[] columns) {
      if (isOutOfBounds(rows, columns)) return;
      int    rowSpan  = rows.length;
      int columnSpan  = columns.length;
      int startRow    = rows.length==0?0:rows[0];
      int startColumn = columns.length==0?0:columns[0];
      for (int i=0;i<rowSpan;i++) {
        for (int j=0;j<columnSpan;j++) {
          if ((span[startRow +i][startColumn +j][COLUMN] != 1)
            ||(span[startRow +i][startColumn +j][ROW]    != 1)) {
            return ;
          }
        }
      }
      for (int i=0,ii=0;i<rowSpan;i++,ii--) {
        for (int j=0,jj=0;j<columnSpan;j++,jj--) {
          span[startRow +i][startColumn +j][COLUMN] = jj;
          span[startRow +i][startColumn +j][ROW]    = ii;
          //System.out.println("r " +ii +"  c " +jj);
        }
      }
      span[startRow][startColumn][COLUMN] = columnSpan;
      span[startRow][startColumn][ROW]    =    rowSpan;

    }

    public void split(int row, int column) {
      if (isOutOfBounds(row, column)) return;
      int columnSpan = span[row][column][COLUMN];
      int    rowSpan = span[row][column][ROW];
      for (int i=0;i<rowSpan;i++) {
        for (int j=0;j<columnSpan;j++) {
          span[row +i][column +j][COLUMN] = 1;
          span[row +i][column +j][ROW]    = 1;
        }
      }
    }


    public void addColumn() {
      int[][][] oldSpan = span;
      int numRows    = oldSpan.length;
      int numColumns = oldSpan[0].length==0?0:oldSpan[0].length;
      span = new int[numRows][numColumns + 1][2];
      for(int i=0;i<numRows;i++) {
        span[i] = new int[numColumns + 1][2];
        System.arraycopy(oldSpan[i],0,span[i],0,oldSpan[i].length);
        span[i][numColumns][COLUMN] = 1;
        span[i][numColumns][ROW] = 1;
      }
      columnSize++;
    }

    public void addRow() {
      int[][][] oldSpan = span;
      int numRows    = oldSpan.length;
      int numColumns = oldSpan.length==0?getModel().getColumnCount():oldSpan[0].length;
      span = new int[numRows + 1][numColumns][2];
      System.arraycopy(oldSpan,0,span,0,numRows);
      for (int i=0;i<numColumns;i++) {
        span[numRows][i][COLUMN] = 1;
        span[numRows][i][ROW]    = 1;
      }
      rowSize++;
    }

    public void insertRow(int row) {
      int[][][] oldSpan = span;
      int numRows    = oldSpan.length;
      int numColumns = oldSpan.length==0?getModel().getColumnCount():oldSpan[0].length;
      span = new int[numRows + 1][numColumns][2];
      if (0 < row) {
        System.arraycopy(oldSpan,0,span,0,row-1);
      }
      System.arraycopy(oldSpan,0,span,row,numRows - row);
      for (int i=0;i<numColumns;i++) {
        span[row][i][COLUMN] = 1;
        span[row][i][ROW]    = 1;
      }
      rowSize++;
    }


    public void setGridSize(Dimension size) {
      columnSize = size.width;
      rowSize    = size.height;
      span = new int[rowSize][columnSize][2];   // 2: COLUMN,ROW
      initValue();
    }

    public final boolean isOutOfBounds(int row, int column) {
      if ((row    < 0)||(rowSize    <= row)
        ||(column < 0)||(columnSize <= column)) {
        return true;
      }
      return false;
    }

    public final boolean isOutOfBounds(int[] rows, int[] columns) {
      for (int i=0;i<rows.length;i++) {
        if ((rows[i] < 0)||(rowSize <= rows[i])) return true;
      }
      for (int i=0;i<columns.length;i++) {
        if ((columns[i] < 0)||(columnSize <= columns[i])) return true;
      }
      return false;
    }

    public final void setValues(Object[][] target, Object value,
                             int[] rows, int[] columns) {
      for (int i=0;i<rows.length;i++) {
        int row = rows[i];
        for (int j=0;j<columns.length;j++) {
          int column = columns[j];
          target[row][column] = value;
        }
      }
    }



}
