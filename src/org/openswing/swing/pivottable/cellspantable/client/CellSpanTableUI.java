package org.openswing.swing.pivottable.cellspantable.client;

import java.util.*;
import java.awt.*;
import javax.swing.*;
import javax.swing.table.*;
import javax.swing.plaf.basic.*;


/**
 * <p>Title: OpenSwing Framework</p>
 * <p>Description: JTable UI with support for cells span.</p>
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
public class CellSpanTableUI extends BasicTableUI {

  public void paint(Graphics g, JComponent c) {
    Rectangle oldClipBounds = g.getClipBounds();
    Rectangle clipBounds    = new Rectangle(oldClipBounds);
    int tableWidth   = table.getColumnModel().getTotalColumnWidth();
    clipBounds.width = Math.min(clipBounds.width, tableWidth);
    g.setClip(clipBounds);

    int firstIndex = table.rowAtPoint(new Point(0, clipBounds.y));
    int  lastIndex = table.getRowCount()-1;

    Rectangle rowRect = new Rectangle(0,0,
      tableWidth, table.getRowHeight() + table.getRowMargin());
    rowRect.y = firstIndex*rowRect.height;

    for (int index = firstIndex; index <= lastIndex; index++) {
      if (rowRect.intersects(clipBounds)) {
        //System.out.println();                  // debug
        //System.out.print("" + index +": ");    // row
        paintRow(g, index);
      }
      rowRect.y += rowRect.height;
    }
    g.setClip(oldClipBounds);
  }

  private void paintRow(Graphics g, int row) {
    Rectangle rect = g.getClipBounds();
    boolean drawn  = false;

    int numColumns = table.getColumnCount();

    for (int column = 0; column < numColumns; column++) {
      Rectangle cellRect = table.getCellRect(row,column,true);
      int cellRow,cellColumn;
      if (((CellSpanTable)table).isGridVisible(row,column)) {
        cellRow    = row;
        cellColumn = column;
      } else {
        cellRow    = row + ((CellSpanTable)table).getSpan(row,column)[CellSpanTable.ROW];
        cellColumn = column + ((CellSpanTable)table).getSpan(row,column)[CellSpanTable.COLUMN];
      }
      if (cellRect.intersects(rect)) {
        drawn = true;
        paintCell(g, cellRect, cellRow, cellColumn);
      } else {
        if (drawn) break;
      }
    }

  }

  private void paintCell(Graphics g, Rectangle cellRect, int row, int column) {
    int spacingHeight = table.getRowMargin();
    int spacingWidth  = table.getColumnModel().getColumnMargin();



    Color c = g.getColor();
    g.setColor(table.getGridColor());
    if (table.getShowHorizontalLines() && table.getShowVerticalLines())
//      g.drawRect(Math.max(0,cellRect.x-1),Math.max(0,cellRect.y-1),cellRect.width,cellRect.height);
      g.drawRect(cellRect.x-1,cellRect.y-1,cellRect.width,cellRect.height);
    g.setColor(c);

    cellRect.setBounds(cellRect.x + spacingWidth/2 -1, cellRect.y + spacingHeight/2 ,
                       cellRect.width - spacingWidth -1, cellRect.height - spacingHeight );

//    cellRect.setBounds(Math.max(0,cellRect.x ), Math.max(0,cellRect.y) ,
//                       cellRect.width, cellRect.height);

    if (table.isEditing() && table.getEditingRow()==row &&
        table.getEditingColumn()==column) {
      Component component = table.getEditorComponent();
      component.setBounds(cellRect);
      component.validate();
    }
    else {
      TableCellRenderer renderer = table.getCellRenderer(row, column);
      Component component = table.prepareRenderer(renderer, row, column);

      if (component.getParent() == null) {
        rendererPane.add(component);
      }
      rendererPane.paintComponent(g, component, table, cellRect.x, cellRect.y,
                                  cellRect.width, cellRect.height, true);
    }
  }
}
