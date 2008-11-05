package org.openswing.swing.table.client;

import java.awt.*;
import javax.swing.*;
import javax.swing.plaf.basic.*;
import javax.swing.table.*;
import java.awt.event.MouseEvent;


/**
 * <p>Title: OpenSwing Framework</p>
 * <p>Description: Grid UI, redefined to allow cells span.</p>
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
public class GridUI extends BasicTableUI {


  /** Paint a representation of the <code>table</code> instance
   * that was set in installUI().
   */
  public void paint(Graphics g, JComponent c) {
      if (table.getRowCount() <= 0 || table.getColumnCount() <= 0) {
          return;
      }
      Rectangle clip = g.getClipBounds();
      Point upperLeft = clip.getLocation();
      Point lowerRight = new Point(clip.x + clip.width - 1, clip.y + clip.height - 1);

      if (((Grid)table).hasCellSpan()) {
        // no cells span defined: it is needed to split visible area in more regions, according to cells span:
        int rMin = table.rowAtPoint(upperLeft);
        // This should never happen.
        if (rMin == -1) {
            rMin = 0;
        }
        int rMax = rMin;
        int h = 0;
        for(int i=rMin;i<table.getRowCount();i++)
          if (h<lowerRight.y) {
            h += table.getRowHeight(i);
            rMax = i;
          }
          else
            break;

        // If the table does not have enough rows to fill the view we'll get -1.
        // Replace this with the index of the last row.
        if (rMax == -1) {
            rMax = table.getRowCount()-1;
        }

        boolean ltr = table.getComponentOrientation().isLeftToRight();
        int cMin = table.columnAtPoint(ltr ? upperLeft : lowerRight);
        // This should never happen.
        if (cMin == -1) {
            cMin = 0;
        }

        int cMax = cMin;
        int w = 0;
        for(int i=cMin;i<table.getColumnCount();i++)
          if (w<lowerRight.x) {
            w += table.getColumnModel().getColumn(i).getWidth();
            cMax = i;
          }
          else
            break;

        // If the table does not have enough columns to fill the view we'll get -1.
        // Replace this with the index of the last column.
        if (cMax == -1) {
            cMax = table.getColumnCount()-1;
        }

        // Paint the grid.
        paintGrid(g, rMin, rMax, cMin, cMax);

        // Paint the cells.
        paintCells(g, rMin, rMax, cMin, cMax);

      }
      else {
        // no cells span defined: it is possible to identify a unique visible area for the whole grid:
        int rMin = table.rowAtPoint(upperLeft);
        int rMax = table.rowAtPoint(lowerRight);
        // This should never happen.
        if (rMin == -1) {
            rMin = 0;
        }
        // If the table does not have enough rows to fill the view we'll get -1.
        // Replace this with the index of the last row.
        if (rMax == -1) {
            rMax = table.getRowCount()-1;
        }

        boolean ltr = table.getComponentOrientation().isLeftToRight();
        int cMin = table.columnAtPoint(ltr ? upperLeft : lowerRight);
        int cMax = table.columnAtPoint(ltr ? lowerRight : upperLeft);
        // This should never happen.
        if (cMin == -1) {
            cMin = 0;
        }
        // If the table does not have enough columns to fill the view we'll get -1.
        // Replace this with the index of the last column.
        if (cMax == -1) {
            cMax = table.getColumnCount()-1;
        }

        // Paint the grid.
        paintGrid(g, rMin, rMax, cMin, cMax);

        // Paint the cells.
        paintCells(g, rMin, rMax, cMin, cMax);
      }

  }

  /*
   * Paints the grid lines within <I>aRect</I>, using the grid
   * color set with <I>setGridColor</I>. Paints vertical lines
   * if <code>getShowVerticalLines()</code> returns true and paints
   * horizontal lines if <code>getShowHorizontalLines()</code>
   * returns true.
   */
  private void paintGrid(Graphics g, int rMin, int rMax, int cMin, int cMax) {
      g.setColor(table.getGridColor());
      TableColumnModel cm = table.getColumnModel();
      int tableWidth;
      int tableHeight;
      Rectangle cell = null;

      for (int col = cMin; col <= cMax; col++) {

        if (table.getShowHorizontalLines()) {
          for (int row = rMin; row <= rMax; row++) {
            tableWidth = cm.getColumn(col).getWidth();
            cell = table.getCellRect(row, col, true);
            for (int i=col+1;i<col+((Grid)table).getSpan(row,col).getN2();i++) {
              tableWidth += cm.getColumn(i).getWidth();
            }

            g.drawLine(cell.x, cell.y - 1, cell.x+tableWidth - 1, cell.y - 1);
          }
        }
        if (table.getShowVerticalLines()) {
          for (int row = rMin; row <= rMax; row++) {
            cell = table.getCellRect(row, col, true);
            tableHeight = cell.y+cell.height;
            for (int i=col+1;i<col+((Grid)table).getSpan(row,col).getN2();i++) {
              cell.x += cm.getColumn(i).getWidth();
            }
            g.drawLine(cell.x - 1, cell.y, cell.x - 1, tableHeight - 1);
          }
        }

      }
  }



  private void paintCells(Graphics g, int rMin, int rMax, int cMin, int cMax) {
      JTableHeader header = table.getTableHeader();
      TableColumn draggedColumn = (header == null) ? null : header.getDraggedColumn();

      TableColumnModel cm = table.getColumnModel();
      Rectangle cellRect;
      TableColumn aColumn;
      for(int row = rMin; row <= rMax; row++) {
          for(int column = cMin; column <= cMax; column++) {
              cellRect = table.getCellRect(row, column, false);
//
              aColumn = cm.getColumn(column);
//              columnWidth = aColumn.getWidth();
//              cellRect.width = columnWidth - columnMargin;
              if (aColumn != draggedColumn) {
                  paintCell(g, cellRect, row, column);
              }
//              cellRect.x += columnWidth;
          }
      }


      // Paint the dragged column if we are dragging.
      if (draggedColumn != null) {
          paintDraggedArea(g, rMin, rMax, draggedColumn, header.getDraggedDistance());
      }

      // Remove any renderers that may be left in the rendererPane.
      rendererPane.removeAll();
  }



  private void paintDraggedArea(Graphics g, int rMin, int rMax, TableColumn draggedColumn, int distance) {
      int draggedColumnIndex = viewIndexForColumn(draggedColumn);

      Rectangle minCell = table.getCellRect(rMin, draggedColumnIndex, true);
      Rectangle maxCell = table.getCellRect(rMax, draggedColumnIndex, true);

      Rectangle vacatedColumnRect = minCell.union(maxCell);

      // Paint a gray well in place of the moving column.
      g.setColor(table.getParent().getBackground());
      g.fillRect(vacatedColumnRect.x, vacatedColumnRect.y,
                 vacatedColumnRect.width, vacatedColumnRect.height);

      // Move to the where the cell has been dragged.
      vacatedColumnRect.x += distance;

      // Fill the background.
      g.setColor(table.getBackground());
      g.fillRect(vacatedColumnRect.x, vacatedColumnRect.y,
                 vacatedColumnRect.width, vacatedColumnRect.height);

      // Paint the vertical grid lines if necessary.
      if (table.getShowVerticalLines()) {
          g.setColor(table.getGridColor());
          int x1 = vacatedColumnRect.x;
          int y1 = vacatedColumnRect.y;
          int x2 = x1 + vacatedColumnRect.width - 1;
          int y2 = y1 + vacatedColumnRect.height - 1;
          // Left
          g.drawLine(x1-1, y1, x1-1, y2);
          // Right
          g.drawLine(x2, y1, x2, y2);
      }

      for(int row = rMin; row <= rMax; row++) {
          // Render the cell value
          Rectangle r = table.getCellRect(row, draggedColumnIndex, false);
          r.x += distance;
          paintCell(g, r, row, draggedColumnIndex);

          // Paint the (lower) horizontal grid line if necessary.
          if (table.getShowHorizontalLines()) {
              g.setColor(table.getGridColor());
              Rectangle rcr = table.getCellRect(row, draggedColumnIndex, true);
              rcr.x += distance;
              int x1 = rcr.x;
              int y1 = rcr.y;
              int x2 = x1 + rcr.width - 1;
              int y2 = y1 + rcr.height - 1;

              if (((Grid)table).isVisible(row,draggedColumnIndex))
                g.drawLine(x1, y2, x2, y2);
          }
      }
  }


  private void paintCell(Graphics g, Rectangle cellRect, int row, int column) {

      if (!((Grid)table).isVisible(row,column)) {
        return;
      }


      if (table.isEditing() && table.getEditingRow()==row &&
                               table.getEditingColumn()==column) {
          Component component = table.getEditorComponent();
          component.setBounds(cellRect);
          component.validate();
      }
      else {
          TableCellRenderer renderer = table.getCellRenderer(row, column);
          Component component = table.prepareRenderer(renderer, row, column);
          rendererPane.paintComponent(g, component, table, cellRect.x, cellRect.y,
                                      cellRect.width, cellRect.height, true);
      }
  }


  private int viewIndexForColumn(TableColumn aColumn) {
      TableColumnModel cm = table.getColumnModel();
      for (int column = 0; column < cm.getColumnCount(); column++) {
          if (cm.getColumn(column) == aColumn) {
              return column;
          }
      }
      return -1;
  }


}
