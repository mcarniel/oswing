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
import org.openswing.swing.table.columns.client.Column;
import org.openswing.swing.table.columns.client.ComboColumn;
import org.openswing.swing.table.columns.client.ButtonColumn;
import org.openswing.swing.table.columns.client.CheckBoxColumn;
import org.openswing.swing.table.columns.client.ComboVOColumn;
import org.openswing.swing.table.columns.client.CurrencyColumn;
import org.openswing.swing.table.columns.client.DateColumn;
import org.openswing.swing.table.columns.client.DecimalColumn;
import org.openswing.swing.table.columns.client.FormattedTextColumn;
import org.openswing.swing.table.columns.client.IntegerColumn;
import org.openswing.swing.table.columns.client.CodLookupColumn;
import org.openswing.swing.table.columns.client.PercentageColumn;
import org.openswing.swing.table.columns.client.TextColumn;


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

  /** tree panel used in renderer */
  private PlusPanel rendTreePanel = new PlusPanel();

  /** tree panel used in nested component container */
  private MinusPanel expTreePanel = new MinusPanel();

  /** current expanded row; -1 if no row is currently expanded */
  private int currentExpandedRow = -1;

  /** variable used to store the old value of "resizingAllowed" grid property */
  private Boolean oldResizingAllowed = null;


  public ExpandableRenderer(Grid grid,Grids grids,int expandableColumn,VOListAdapter modelAdapter) {
    this.grid = grid;
    this.grids = grids;
    this.expandableColumn = expandableColumn;
    this.modelAdapter = modelAdapter;
//    mainPanel.setBackground(defaultColor);
    rendTreePanel.setOpaque(false);
    rendTreePanel.setMinimumSize(new Dimension(13,13));

    treeLinesPanel.setSize(12,12);
    treeLinesPanel.setMinimumSize(new Dimension(12,12));
    treeLinesPanel.setMaximumSize(new Dimension(12,12));

    modelAdapter.getGrids().getVOListTableModel().addTableModelListener(new TableModelListener() {

      public void tableChanged(TableModelEvent e) {
        if (e.getType()==e.DELETE || e.getType()==e.INSERT) {
          if (getCurrentExpandedRow()!=-1)
              collapseRow(getCurrentExpandedRow());
        }
      }

    });

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
              }
              ExpandableRenderer.this.grid.repaint();
            }
          }
        }
        else {
          // show inner component in next row
          if (SwingUtilities.isLeftMouseButton(e) &&
              e.getClickCount()==1 &&
              p1.x>=width && p1.x<=width+rendTreePanel.getWidth()) {
            if (row!=-1 &&
                ExpandableRenderer.this.grid.getExpandableRowController().isRowExpandable((VOListTableModel)ExpandableRenderer.this.grid.getModel(),row)) {
              // current cell is expandable...
              if (ExpandableRenderer.this.grids.isRowExpanded(row))  {
                collapseRow(row);
              }
              else {
                expandRow(row);
              }
              ExpandableRenderer.this.grid.repaint();
            }
          }

        }
      }
    });

  }


  /**
   * @return current expanded row; -1 if no row is currently expanded
   */
  public int getCurrentExpandedRow() {
    return currentExpandedRow;
  }


  /**
   * If expandableRenderer attribute is not null and
   * there is an expanded row, then refresh it.
   * This method can be invoked by Form when updating a nested Form panel,
   * in order to refresh expanded row content.
   */
  public final void refreshExpandableRenderer() {
    if (currentExpandedRow!=-1) {
      int row = currentExpandedRow;
      collapseRow(row);
      expandRow(row);
    }
  }


  private void collapseAllRowsExcept(int row) {
    for(int i=0;i<grids.getVOListTableModel().getRowCount();i++)
      if (i!=row && grids.isRowExpanded(i))
        collapseRow(i);
  }


  public final void collapseRow(int row) {
    currentExpandedRow = -1;
    ExpandablePanel c = (ExpandablePanel)grids.getComponentInCache(row);
    if (c!=null)
      grid.remove(c);

    grids.collapseRow(row);
    if (grid.getRowHeight(row)!=grid.getRowHeight())
      grid.setRowHeight(row,grid.getRowHeight());

    int[] cols = new int[grid.getColumnModel().getColumnCount()-expandableColumn];
    for(int i=0;i<cols.length;i++)
      cols[i] = i+expandableColumn;
    grid.removeMergedCells(new int[]{row},cols);

    if (grid.getExpandableRowController().removeShowedComponent(grids.getVOListTableModel(),row,(JComponent)c.getNestedComponent()))
      grids.removeComponentInCache(row);

    if (!grids.isAnyRowExpanded() && oldResizingAllowed!=null) {
      grid.setResizingAllowed(oldResizingAllowed.booleanValue());
    }
  }


  public final Component expandRow(final int row) {
    ClientUtils.fireBusyEvent(true);
    try {
      if (ExpandableRenderer.this.grid.isSingleExpandableRow()) {
        collapseAllRowsExcept(row);
      }
      grids.expandRow(row);
      currentExpandedRow = row;

      if (oldResizingAllowed==null)
        oldResizingAllowed = new Boolean(grid.getTableHeader().getResizingAllowed());
      grid.setResizingAllowed(false);

      ExpandablePanel expPanel = (ExpandablePanel)grids.getComponentInCache(row);
      Component c = null;
      if (expPanel==null)
        c = grid.getExpandableRowController().getComponentToShow(grids.getVOListTableModel(),row);
      else
        c = expPanel.getNestedComponent();

      if (c!=null) {
        // c is the nested component
        // now the size of the container of nested component is determined (as <width,height>)
        int width = 0;
        for(int i=expandableColumn;i<grid.getColumnModel().getColumnCount();i++) {
          width += grid.getColumnModel().getColumn(i).getWidth();
        }
        int height = grid.getRowHeight(row);
        Dimension dim = c.getSize();
        if (dim.width==0 && dim.height==0)
          dim = c.getPreferredSize();
        int delta = 0;
        if (!grid.isOverwriteRowWhenExpanding())
          delta = grid.getRowHeight(row);
        dim.height += delta;
        if (dim.getHeight()>height) {
          height = dim.height;
          if (grid.getRowHeight(row)!=height)
            grid.setRowHeight(row,height);
        }

        // now nested component size is defined
        ((JComponent)c).setMaximumSize(new Dimension(width-13,height-delta));
        ((JComponent)c).setSize(new Dimension(width-13,height-delta));
        ((JComponent)c).setPreferredSize(new Dimension(width-13,height-delta));

        // grid cell spans are changed, according to nested component dimension
        int[] cols = new int[grid.getColumnModel().getColumnCount()-expandableColumn];
        for(int i=0;i<cols.length;i++)
          cols[i] = i+expandableColumn;
        grid.mergeCells(new int[]{row},cols);

        // according to "isOverwriteRowWhenExpanding" value,
        // nested component is added to grid or
        // expanded row + nested component are added to grid
        if (!grid.isOverwriteRowWhenExpanding()) {
          JPanel nestedCompContainer = new JPanel();
          nestedCompContainer.setLayout(null);
          JPanel aux = new JPanel();
          aux.setLayout(null);
//          aux.setBackground(grid.getGridColor());
          Component comp = null;
  //        JPanel rendPanel = null;
          int w = 0;
          JLabel lb = null;
          JLabel cc = null;
          int leftMargin = 0;
          int rightMargin = 0;
          int topMargin = 0;
          int bottomMargin = 0;
          Column col = null;
          for(int i=expandableColumn;i<grid.getColumnModel().getColumnCount();i++) {
            comp = modelAdapter.getCellRenderer(grid.convertColumnIndexToModel(i)).getTableCellRendererComponent(grid, grid.getValueAt(row,i), false, false, row, i);
            col = modelAdapter.getFieldColumn(grid.convertColumnIndexToModel(i));
            leftMargin = 0;
            rightMargin = 0;
            topMargin = 0;
            bottomMargin = 0;
            if (col.getColumnType()==col.TYPE_COMBO) {
              leftMargin = ((ComboColumn)col).getLeftMargin();
              rightMargin = ((ComboColumn)col).getRightMargin();
              topMargin = ((ComboColumn)col).getTopMargin();
              bottomMargin = ((ComboColumn)col).getBottomMargin();
            }
            else if (col.getColumnType()==col.TYPE_COMBO_VO) {
              leftMargin = ((ComboVOColumn)col).getLeftMargin();
              rightMargin = ((ComboVOColumn)col).getRightMargin();
              topMargin = ((ComboVOColumn)col).getTopMargin();
              bottomMargin = ((ComboVOColumn)col).getBottomMargin();
            }
            else if (col.getColumnType()==col.TYPE_CURRENCY) {
              leftMargin = ((CurrencyColumn)col).getLeftMargin();
              rightMargin = ((CurrencyColumn)col).getRightMargin();
              topMargin = ((CurrencyColumn)col).getTopMargin();
              bottomMargin = ((CurrencyColumn)col).getBottomMargin();
            }
            else if (col.getColumnType()==col.TYPE_DEC) {
              leftMargin = ((DecimalColumn)col).getLeftMargin();
              rightMargin = ((DecimalColumn)col).getRightMargin();
              topMargin = ((DecimalColumn)col).getTopMargin();
              bottomMargin = ((DecimalColumn)col).getBottomMargin();
            }
            else if (col.getColumnType()==col.TYPE_FORMATTED_TEXT) {
              leftMargin = ((FormattedTextColumn)col).getLeftMargin();
              rightMargin = ((FormattedTextColumn)col).getRightMargin();
              topMargin = ((FormattedTextColumn)col).getTopMargin();
              bottomMargin = ((FormattedTextColumn)col).getBottomMargin();
            }
            else if (col.getColumnType()==col.TYPE_INT) {
              leftMargin = ((IntegerColumn)col).getLeftMargin();
              rightMargin = ((IntegerColumn)col).getRightMargin();
              topMargin = ((IntegerColumn)col).getTopMargin();
              bottomMargin = ((IntegerColumn)col).getBottomMargin();
            }
            else if (col.getColumnType()==col.TYPE_LOOKUP) {
              leftMargin = ((CodLookupColumn)col).getLeftMargin();
              rightMargin = ((CodLookupColumn)col).getRightMargin();
              topMargin = ((CodLookupColumn)col).getTopMargin();
              bottomMargin = ((CodLookupColumn)col).getBottomMargin();
            }
            else if (col.getColumnType()==col.TYPE_PERC) {
              leftMargin = ((PercentageColumn)col).getLeftMargin();
              rightMargin = ((PercentageColumn)col).getRightMargin();
              topMargin = ((PercentageColumn)col).getTopMargin();
              bottomMargin = ((PercentageColumn)col).getBottomMargin();
            }
            else if (col.getColumnType()==col.TYPE_TEXT) {
              leftMargin = ((TextColumn)col).getLeftMargin();
              rightMargin = ((TextColumn)col).getRightMargin();
              topMargin = ((TextColumn)col).getTopMargin();
              bottomMargin = ((TextColumn)col).getBottomMargin();
            }

            if (i==expandableColumn && col.getColumnType()==col.TYPE_COMBO ||
                comp.getWidth()==0 && comp.getHeight()==0) {
              if (comp instanceof JLabel) {
                lb = ((JLabel)comp);
                cc = new JLabel(lb.getText());
                cc.setBackground(lb.getBackground());
                cc.setForeground(lb.getForeground());
                cc.setFont(lb.getFont());
                cc.setHorizontalAlignment(lb.getHorizontalAlignment());
                cc.setHorizontalTextPosition(lb.getHorizontalTextPosition());
                comp = cc;
              }
              comp.setSize(
                  grid.getColumnModel().getColumn(i + expandableColumn).getWidth() - 1 - (i == expandableColumn ? 12 : 0)-leftMargin-rightMargin,
                  delta
              );
            }
            if (i==expandableColumn) {
              aux.add(expTreePanel);
              expTreePanel.setBackground(comp.getBackground());
              expTreePanel.setBounds(0,0,13,delta);
            }
            aux.add(comp);
            comp.setBounds(
              w+(i==expandableColumn?+12:0)+leftMargin,
              0+topMargin,
              grid.getColumnModel().getColumn(i+expandableColumn).getWidth()-1-(i==expandableColumn?12:0)-leftMargin-rightMargin,
              delta-topMargin-bottomMargin
            );
            w += grid.getColumnModel().getColumn(i+expandableColumn).getWidth();
            JSeparator sep = new JSeparator(JSeparator.VERTICAL);
            aux.add(sep);
            sep.setBounds(w-1,0,1,delta);
            aux.setBackground(comp.getBackground());
          }
          aux.setSize(w,delta);
          aux.setMinimumSize(new Dimension(w,delta));
          nestedCompContainer.setMinimumSize(new Dimension(width,height));
          nestedCompContainer.setSize(new Dimension(width,height));
          nestedCompContainer.setPreferredSize(new Dimension(width,height));

          treeLinesPanel.setBackground(expTreePanel.getBackground());
          nestedCompContainer.add(aux);
          aux.setBounds(0,0,w,delta);
          nestedCompContainer.add(treeLinesPanel);
          treeLinesPanel.setBounds(1,delta,treeLinesPanel.getWidth(),treeLinesPanel.getHeight());
          nestedCompContainer.add(c);
          c.setBounds(treeLinesPanel.getWidth()+1,delta,c.getWidth(),c.getHeight());
          nestedCompContainer.setFocusable(true);

          expPanel = new ExpandablePanel(grid,c,nestedCompContainer);
          nestedCompContainer.setBackground(comp.getBackground());
          expPanel.setBackground(comp.getBackground());
          grids.putComponentInCache(row,expPanel);
          grid.add(expPanel);

          int y = 0;
          for(int i=0;i<row;i++)
            y += grid.getRowHeight(i);
          int x = 0;
          for(int i=0;i<expandableColumn;i++)
            x += grid.getColumnModel().getColumn(i).getWidth();
          expPanel.setBounds(x,y,width,height);
        }
        else {
          expPanel = new ExpandablePanel(grid,c,c);
          grids.putComponentInCache(row,expPanel);
          grid.add(expPanel);
          int y = grid.getRowHeight();
          for(int i=0;i<row;i++)
            y += grid.getRowHeight(i);
          int x = 13;
          for(int i=0;i<expandableColumn;i++)
            x += grid.getColumnModel().getColumn(i).getWidth();
          expPanel.setBounds(x,y,width,height);

        }
        grids.setCurrentNestedComponent(row,expPanel);

        final ExpandablePanel aux = expPanel;
        SwingUtilities.invokeLater(new Runnable() {
          public void run() {
            grid.repaint();
            final Component comp = grid.getExpandableRowController().getFocusableComponent((JComponent)aux.getNestedComponent());
            if (comp!=null) {
              comp.requestFocus();

              if (comp instanceof JComponent) {
                Action exitAction = new AbstractAction() {
                    public void actionPerformed(ActionEvent e) {
                      ((JComponent)comp).getInputMap().remove(ClientSettings.COLLAPSE_CELL_KEY);
                      ((JComponent)comp).getActionMap().remove("exitAction");
                      collapseRow(row);
                      grid.requestFocus();
                    }
                };
                ((JComponent)comp).getInputMap().put(ClientSettings.COLLAPSE_CELL_KEY,"exitAction");
                ((JComponent)comp).getActionMap().put("exitAction",exitAction);
              }
            }
            else
              aux.requestFocus();
          }
        });
        return expPanel;
      }
      else
        return null;
    }
    catch(Throwable t) {
      t.printStackTrace();
      return null;
    }
    finally {
      ClientUtils.fireBusyEvent(false);
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
        rendTreePanel.setBackground(comp.getBackground());
        mainPanel.setBackground(comp.getBackground());
        mainPanel.removeAll();
        mainPanel.setLayout(new BorderLayout(2,0));
        mainPanel.add(comp,BorderLayout.CENTER);
        if (!grid.isOverwriteRowWhenExpanding())
          mainPanel.add(rendTreePanel,BorderLayout.WEST);
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
          rendTreePanel.setBackground(comp.getBackground());
          if (!grid.isOverwriteRowWhenExpanding())
            mainPanel.add(rendTreePanel,BorderLayout.WEST);
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


  /**
   * <p>Title: OpenSwing Framework</p>
   * <p>Description: Inner class that draw the expansion box (used on renderering cells).</p>
   * @version 1.0
   */
  class PlusPanel extends JPanel {

    public final void paintComponent(Graphics g) {
      super.paintComponent(g);
      if (!ExpandableRenderer.this.grid.isOverwriteRowWhenExpanding()) {
        if (row!=-1 &&
            grid.getExpandableRowController().isRowExpandable((VOListTableModel)grid.getModel(),row)) {
          if (plusImage==null)
            plusImage = ClientUtils.getImage("plus.gif");

          g.drawImage(plusImage,0,getHeight()/2-plusImage.getHeight(this)/2,plusImage.getWidth(this),plusImage.getHeight(this),this);
        }
      }
    }

  }



  /**
   * <p>Title: OpenSwing Framework</p>
   * <p>Description: Inner class that draw the tree collapsed box (used by the container of nested component).</p>
   * @version 1.0
   */
  class MinusPanel extends JPanel {

    public final void paintComponent(Graphics g) {
      super.paintComponent(g);
      if (!ExpandableRenderer.this.grid.isOverwriteRowWhenExpanding()) {
        if (minusImage==null)
          minusImage = ClientUtils.getImage("minus.gif");
        g.setColor(new Color(128,128,128));
        g.drawLine(minusImage.getWidth(this)/2,getHeight()/2+minusImage.getHeight(this)/2-1,minusImage.getWidth(this)/2,getHeight());
        g.drawImage(minusImage,0,getHeight()/2-minusImage.getHeight(this)/2,minusImage.getWidth(this),minusImage.getHeight(this),this);
      }
    }

  }


}
