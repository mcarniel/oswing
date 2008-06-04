package org.openswing.swing.table.renderers.client;

import java.util.*;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;
import javax.swing.table.*;

import org.openswing.swing.table.client.*;
import org.openswing.swing.table.model.client.*;
import org.openswing.swing.util.client.*;
import org.openswing.swing.client.ImagePanel;
import org.openswing.swing.util.java.Consts;


/**
 * <p>Title: OpenSwing Framework</p>
 * <p>Description: cell renderer used to show within a cell renderer in case of expandable rows.</p>
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
public class ExpandableRenderer extends DefaultTableCellRenderer {

  /** cell content */
  private JPanel mainPanel = new JPanel();

  /** default cell content */
  private TableCellRenderer defaultCellRenderer = null;

  /** JTable component */
  private Grid grid = null;

  /** JTable container */
  private Grids grids = null;

  /** TableModel adapter, used to link ValueObjects to TableModel */
  private VOListAdapter modelAdapter = null;

  /** expandable column index */
  private int expandableColumn;

  /** plus image to used when expanding row */
  private Image plusImage = null;

  /** minus image to used when expanding row */
  private Image minusImage = null;

  /** tree lines image panel to used when expanding row */
  private TreeLinesPanel treeLinesPanel = new TreeLinesPanel();

  /** current editing row*/
  private int row = -1;

  /** tree panel */
  private JPanel treePanel = new JPanel() {

    public final void paintComponent(Graphics g) {
      super.paintComponent(g);
      if (!ExpandableRenderer.this.grid.isOverwriteRowWhenExpanding()) {
        if (row!=-1 &&
            grid.getExpandableRowController().isRowExpandable((VOListTableModel)grid.getModel(),row)) {
          if (plusImage==null)
            plusImage = ClientUtils.getImage("plus.gif");
          if (minusImage==null)
            minusImage = ClientUtils.getImage("minus.gif");

          Image img = null;
          if (grids.isRowExpanded(row)) {
            img = minusImage;
            g.setColor(new Color(128,128,128));
            g.drawLine(img.getWidth(this)/2,getHeight()/2+img.getHeight(this)/2-1,img.getWidth(this)/2,getHeight());
          }
          else
            img = plusImage;
          g.drawImage(img,0,getHeight()/2-img.getHeight(this)/2,img.getWidth(this),img.getHeight(this),this);
        }
      }
    }

  };


  public ExpandableRenderer(Grid grid,Grids grids,int expandableColumn,VOListAdapter modelAdapter) {
    this.grid = grid;
    this.grids = grids;
    this.expandableColumn = expandableColumn;
    this.modelAdapter = modelAdapter;
//    mainPanel.setBackground(defaultColor);
    treePanel.setOpaque(false);
    treePanel.setMinimumSize(new Dimension(13,13));

    treeLinesPanel.setSize(12,12);
    treeLinesPanel.setMinimumSize(new Dimension(12,12));
    treeLinesPanel.setMaximumSize(new Dimension(12,12));

    grid.addMouseListener(new MouseAdapter() {
      public void mouseClicked(MouseEvent e) {
        VOListTableModel model = (VOListTableModel)ExpandableRenderer.this.grid.getModel();
        Point p1 = e.getPoint();
        int width = 0;
        for(int i=0;i<ExpandableRenderer.this.expandableColumn;i++)
          width += ExpandableRenderer.this.grid.getColumnModel().getColumn(i).getWidth();
        row = ExpandableRenderer.this.grid.getSelectedRow();

        if (ExpandableRenderer.this.grid.isOverwriteRowWhenExpanding()) {
          // show inner component in this row
          if (SwingUtilities.isLeftMouseButton(e) &&
              e.getClickCount()==1 &&
              p1.x>=width && p1.x<=width+ExpandableRenderer.this.grid.getColumnModel().getColumn(ExpandableRenderer.this.expandableColumn).getWidth()) {
            if (row!=-1 &&
                ExpandableRenderer.this.grid.getExpandableRowController().isRowExpandable(model,row)) {
              // current cell is expandable...
              if (ExpandableRenderer.this.grids.isRowExpanded(row))  {
                collapseRow(row);
              }
              else {
                expandRow(row);
                if (ExpandableRenderer.this.grid.isSingleExpandableRow()) {
                  collapseAllRowsExcept(row);
                }
              }
              ExpandableRenderer.this.grid.repaint();
            }
          }
        }
        else {
          // show inner component in next row
          if (SwingUtilities.isLeftMouseButton(e) &&
              e.getClickCount()==1 &&
              p1.x>=width && p1.x<=width+treePanel.getWidth()) {
            if (row!=-1 &&
                ExpandableRenderer.this.grid.getExpandableRowController().isRowExpandable((VOListTableModel)ExpandableRenderer.this.grid.getModel(),row)) {
              // current cell is expandable...
              if (ExpandableRenderer.this.grids.isRowExpanded(row))  {
                collapseRow(row);
              }
              else {
                expandRow(row);
                if (ExpandableRenderer.this.grid.isSingleExpandableRow()) {
                  collapseAllRowsExcept(row);
                }
              }
              ExpandableRenderer.this.grid.repaint();
            }
          }

        }
      }
    });

  }


  private void collapseAllRowsExcept(int row) {
    for(int i=0;i<grids.getVOListTableModel().getRowCount();i++)
      if (i!=row && grids.isRowExpanded(i))
        collapseRow(i);
  }


  private void collapseRow(int row) {
    Component c = (Component)grids.getComponentInCache(row);
    if (c!=null)
      grid.remove(c);

    grids.collapseRow(row);
    if (grid.getRowHeight(row)!=grid.getRowHeight())
      grid.setRowHeight(row,grid.getRowHeight());

    int[] cols = new int[grid.getColumnModel().getColumnCount()-expandableColumn];
    for(int i=0;i<cols.length;i++)
      cols[i] = i+expandableColumn;
    grid.removeMergedCells(new int[]{row},cols);

    if (grid.getExpandableRowController().removeShowedComponent(grids.getVOListTableModel(),row))
      grids.removeComponentInCache(row);
  }


  private void expandRow(int row) {
    grids.expandRow(row);

    Component c = (Component)grids.getComponentInCache(row);
    if (c==null) {
      c = grid.getExpandableRowController().getComponentToShow(grids.getVOListTableModel(),row);

      if (c!=null) {

        c = new ExpandablePanel(grid,c);
        grids.putComponentInCache(row,c);
      }
    }

    if (c!=null) {
      int width = 0; // component width
      for(int i=expandableColumn;i<grid.getColumnModel().getColumnCount();i++) {
        width += grid.getColumnModel().getColumn(i).getWidth();
      }
      int height = grid.getRowHeight(row);

      Dimension dim = c.getSize();
      if (dim.width==0 && dim.height==0)
        dim = c.getPreferredSize();

      if (!grid.isOverwriteRowWhenExpanding())
        dim.height += grid.getRowHeight();

      if (dim.getHeight()>height) {
        height = dim.height;
        if (grid.getRowHeight(row)!=height)
          grid.setRowHeight(row,height);
      }

      ((JComponent)c).setMaximumSize(new Dimension(width,height));


      int[] cols = new int[grid.getColumnModel().getColumnCount()-expandableColumn];
      for(int i=0;i<cols.length;i++)
        cols[i] = i+expandableColumn;
      grid.mergeCells(new int[]{row},cols);

      grid.add(c);

      int y = grid.getRowHeight();
      for(int i=0;i<row;i++)
        y += grid.getRowHeight(i);
      int x = 13;
      for(int i=0;i<expandableColumn;i++)
        x += grid.getColumnModel().getColumn(i).getWidth();
      c.setBounds(x,y,width,height);

      new Thread() {
        public void run() {
          try {
            sleep(1000);
          }
          catch (InterruptedException ex) {
          }
          grid.repaint();
        }
      }.start();
    }
  }


  public void setDefaultCellRenderer(TableCellRenderer rend) {
    this.defaultCellRenderer = rend;
  }


  /**
   *
   * Returns the default table cell renderer.
   *
   * @param table  the <code>JTable</code>
   * @param value  the value to assign to the cell at
   *			<code>[row, column]</code>
   * @param isSelected true if cell is selected
   * @param hasFocus true if cell has focus
   * @param row  the row of the cell to render
   * @param column the column of the cell to render
   * @return the default table cell renderer
   */
  public Component getTableCellRendererComponent(JTable table, Object value,
                        boolean isSelected, boolean hasFocus, int row, int column) {
    this.row = row;

    if (!grid.getExpandableRowController().isRowExpandable((VOListTableModel)grid.getModel(),row)) {
      if (column==expandableColumn) {
        Component comp = defaultCellRenderer.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
        treePanel.setBackground(comp.getBackground());
        mainPanel.setBackground(comp.getBackground());
        mainPanel.removeAll();
        mainPanel.setLayout(new BorderLayout(2,0));
        mainPanel.add(comp,BorderLayout.CENTER);
        if (!grid.isOverwriteRowWhenExpanding())
          mainPanel.add(treePanel,BorderLayout.WEST);
        mainPanel.revalidate();
        return mainPanel;
      }
      else
        return defaultCellRenderer.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
    }
    // current row is expandable...
    if (column<expandableColumn)
      return defaultCellRenderer.getTableCellRendererComponent(table, value, isSelected && !grids.isRowExpanded(row), !grids.isRowExpanded(row) && hasFocus, row, column);
    else {
      // column>=expandableColumn
      if (!grids.isRowExpanded(row)) {
        // row is expandable and not yet expanded...
        if (column==expandableColumn) {
          mainPanel.removeAll();
          mainPanel.setLayout(new BorderLayout(2,0));
          Component comp = defaultCellRenderer.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
          mainPanel.add(comp,BorderLayout.CENTER);
          mainPanel.setBackground(comp.getBackground());
          treePanel.setBackground(comp.getBackground());
          if (!grid.isOverwriteRowWhenExpanding())
            mainPanel.add(treePanel,BorderLayout.WEST);
          mainPanel.revalidate();
          return mainPanel;
        }
        else
          // column>expandableColumn
          return defaultCellRenderer.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
      }
      else {
        // column>=expandableColumn and
        // row is expandable and
        // already expanded...
        Component c = (Component)grids.getComponentInCache(row);
        if (c!=null) {
//          c.setBounds(
//            0,
//            0,
//            defaultCellRenderer.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column).getWidth(),
//            grid.getRowHeight(row)
//          );
          c.setFocusable(true);

          if (grid.isOverwriteRowWhenExpanding())
            return c;
          else {
            mainPanel.removeAll();
            mainPanel.setLayout(new GridBagLayout());
            JPanel aux = new JPanel();
            aux.setLayout(null);
            aux.setBackground(grid.getGridColor());
            Component comp = null;
            JPanel rendPanel = null;
            for(int i=expandableColumn;i<grid.getColumnModel().getColumnCount();i++) {
              comp = modelAdapter.getCellRenderer(i).getTableCellRendererComponent(table, grid.getValueAt(row,i), false, false, row, i);
              rendPanel = new JPanel();
              rendPanel.setLayout(new BorderLayout(2,0));
              if (i==expandableColumn) {
                rendPanel.add(treePanel,BorderLayout.WEST);
                treePanel.setBackground(comp.getBackground());
              }
              rendPanel.add(comp,BorderLayout.CENTER);
              rendPanel.setBackground(comp.getBackground());
              aux.add(rendPanel);
            }
            int w = 0;
            for(int i=0;i<aux.getComponentCount();i++) {
              rendPanel = (JPanel)aux.getComponent(i);
              rendPanel.setBounds(w,0,grid.getColumnModel().getColumn(i+expandableColumn).getWidth()-1,grid.getRowHeight());
//              System.out.println(w+","+0);
              w += grid.getColumnModel().getColumn(i+expandableColumn).getWidth();
            }
            aux.setSize(w,grid.getRowHeight());
            aux.setMinimumSize(new Dimension(w,grid.getRowHeight()));

            treeLinesPanel.setBackground(treePanel.getBackground());
            mainPanel.add(aux,             new GridBagConstraints(0, 0, 2, 1, 1.0, 0.0
                    ,GridBagConstraints.NORTHWEST, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
            mainPanel.add(treeLinesPanel,             new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0
                    ,GridBagConstraints.NORTHWEST, GridBagConstraints.NONE, new Insets(0, 1, 0, 0), 0, 0));
            mainPanel.add(c,             new GridBagConstraints(1, 1, 1, 1, 1.0, 1.0
                    ,GridBagConstraints.NORTHWEST, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
            mainPanel.revalidate();
            mainPanel.setFocusable(true);
            return mainPanel;
          }
        }
        else
          // this is an error...
          return defaultCellRenderer.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
      }
    }
  }


  /**
   * <p>Title: OpenSwing Framework</p>
   * <p>Description: Inner class that draw a vertical line</p>
   * @version 1.0
   */
  class LinePanel extends JPanel {

    public LinePanel() {
      setMaximumSize(new Dimension(1,grid.getRowHeight()));
    }

    public void paint(Graphics g) {
      super.paint(g);
      g.setColor(grid.getGridColor());
      g.drawLine(0,0,0,getHeight());
    }

  }


  /**
   * <p>Title: OpenSwing Framework</p>
   * <p>Description: Inner class that draw the tree lines.</p>
   * @version 1.0
   */
  class TreeLinesPanel extends JPanel {

    public void paint(Graphics g) {
      super.paint(g);
      g.setColor(new Color(128,128,128));
      g.drawLine(getWidth()/2-2,0,getWidth()/2-2,10);
      g.drawLine(getWidth()/2-2,10,getWidth(),10);
    }

  }



}
