package org.openswing.swing.table.renderers.client;

import java.awt.*;
import javax.swing.*;
import javax.swing.plaf.basic.*;
import javax.swing.table.*;

import org.openswing.swing.table.client.*;
import org.openswing.swing.util.client.*;
import org.openswing.swing.util.java.*;


/**
 * <p>Title: OpenSwing Framework</p>
 * <p>Description: Renderer used to format a check-box column.</p>
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
public class CheckBoxTableCellRenderer extends DefaultTableCellRenderer {

  /** flag used to store the current check-box state */
  private Boolean selected = Boolean.FALSE;

  /** cell content (the check-box is drawed inside) */
  private CheckLabel rend = new CheckLabel();

  /** flag used to draw the check-box border, if cell has currently the focus */
  private boolean paintBorder = false;

  /** border color, if cell has currently the focus */
  private Color selectionForeground = null;

  /** grid container */
  private GridController gridContainer = null;

  /** default font */
  private Font defaultFont = null;

  /** flag used to indicate if the button is enabled also when the grid is in readonly mode; default value: <code>false</code> i.e. the button is enabled only in INSERT/EDIT modes, according to "editableOnEdit" and "editableOnInsert" properties */
  private boolean enableInReadOnlyMode = false;

  /** define if null value is alloed (i.e. distinct from Boolean.FALSE value); default value: <code>false</code> */
  private boolean allowNullValue;

  /** attribute name associated to this column */
  private String attributeName = null;

  /** value used to select the check-box */
  private Object positiveValue = null;

  /** value used to deselect the check-box */
  private Object negativeValue = null;


  /**
   * Constructor.
   * @param gridContainer grid container
   * @param attributeName attribute name associated to this column
   */
  public CheckBoxTableCellRenderer(GridController gridContainer,int alignement,boolean enableInReadOnlyMode,boolean allowNullValue,String attributeName,Object positiveValue,Object negativeValue) {
    this.gridContainer = gridContainer;
    rend.setOpaque(true);
    rend.setHorizontalAlignment(alignement);
    this.enableInReadOnlyMode = enableInReadOnlyMode;
    this.allowNullValue = allowNullValue;
    this.attributeName = attributeName;
    this.positiveValue = positiveValue;
    this.negativeValue = negativeValue;
  }


  public Component getTableCellRendererComponent(JTable table, Object value,
                        boolean isSelected, boolean hasFocus, int row, int column) {
    if (defaultFont==null)
      defaultFont = rend.getFont();
    if (hasFocus && table instanceof Grid) {
      if (ClientSettings.IGNORE_GRID_SELECTION_FOREGROUND)
        rend.setForeground(gridContainer.getForegroundColor(row,table.getModel().getColumnName(table.convertColumnIndexToModel(column)),value));
      else
        rend.setForeground(table.getSelectionForeground());

//      rend.setBackground(((Grid)table).getActiveCellBackgroundColor());
//      Color selColor = ((Grid)table).getActiveCellBackgroundColor();
      Color selColor = null;
      try {
        selColor = new Color(
            Math.min(255,
                     2 * ( (Grid) table).getActiveCellBackgroundColor().getRed() -
                     ClientSettings.GRID_CELL_BACKGROUND.getRed()),
            Math.min(255,
                     2 * ( (Grid) table).getActiveCellBackgroundColor().getGreen() -
                     ClientSettings.GRID_CELL_BACKGROUND.getGreen()),
            Math.min(255,
                     2 * ( (Grid) table).getActiveCellBackgroundColor().getBlue() -
                     ClientSettings.GRID_CELL_BACKGROUND.getBlue())
            );
      }
      catch (Exception ex1) {
        selColor = ( (Grid) table).getActiveCellBackgroundColor();
      }
      Color backColor = gridContainer.getBackgroundColor(row,table.getModel().getColumnName(table.convertColumnIndexToModel(column)),value);
      rend.setBackground(new Color(
          (backColor.getRed()+selColor.getRed())/2,
          (backColor.getGreen()+selColor.getGreen())/2,
          (backColor.getBlue()+selColor.getBlue())/2,
          200
      ));
      selectionForeground = table.getSelectionForeground();
      paintBorder = true;
    } else if (isSelected && !hasFocus) {
      if (ClientSettings.IGNORE_GRID_SELECTION_FOREGROUND)
        rend.setForeground(gridContainer.getForegroundColor(row,table.getModel().getColumnName(table.convertColumnIndexToModel(column)),value));
      else
        rend.setForeground(table.getSelectionForeground());

//      rend.setBackground(table.getSelectionBackground());
      Color backColor = gridContainer.getBackgroundColor(row,table.getModel().getColumnName(table.convertColumnIndexToModel(column)),value);
      Color selColor = null;
      try {
        selColor = new Color(
            Math.min(255,
                     2 * table.getSelectionBackground().getRed() -
                     ClientSettings.GRID_CELL_BACKGROUND.getRed()),
            Math.min(255,
                     2 * table.getSelectionBackground().getGreen() -
                     ClientSettings.GRID_CELL_BACKGROUND.getGreen()),
            Math.min(255,
                     2 * table.getSelectionBackground().getBlue() -
                     ClientSettings.GRID_CELL_BACKGROUND.getBlue())
            );
      }
      catch (Exception ex1) {
        selColor = ( (Grid) table).getSelectionBackground();
      }
      rend.setBackground(new Color(
          (backColor.getRed()+selColor.getRed())/2,
          (backColor.getGreen()+selColor.getGreen())/2,
          (backColor.getBlue()+selColor.getBlue())/2
      ));
      paintBorder = false;
    } else {
      if (!enableInReadOnlyMode && (((Grid)table).getMode()==Consts.READONLY || !((Grid)table).isColorsInReadOnlyMode()))
        rend.setBackground(gridContainer.getBackgroundColor(row,table.getModel().getColumnName(table.convertColumnIndexToModel(column)),value));
      else {
        if (enableInReadOnlyMode || table.isCellEditable(row,column))
          rend.setBackground(ClientSettings.GRID_EDITABLE_CELL_BACKGROUND);
        else
          rend.setBackground(ClientSettings.GRID_NOT_EDITABLE_CELL_BACKGROUND);
      }
      paintBorder = false;
    }

    if (value==null && allowNullValue)
      selected = null;
    else if (value==null && !allowNullValue)
      selected = Boolean.FALSE;
    else if (value.equals(positiveValue))
      selected = Boolean.TRUE;
    else
      selected = Boolean.FALSE;
    rend.setPreferredSize(new Dimension(table.getColumnModel().getColumn(column).getWidth(),table.getHeight()));


    Font f = gridContainer.getFont(row,table.getModel().getColumnName(table.convertColumnIndexToModel(column)),value,defaultFont);
    if (f!=null)
      rend.setFont(f);
    else
      rend.setFont(defaultFont);

    if (table instanceof Grid)
      rend.setToolTipText(gridContainer.getCellTooltip(row,((Grid)table).getVOListTableModel().getColumnName(table.convertColumnIndexToModel(column))));

    return rend;
  }


  public final void finalize() {
    gridContainer = null;
  }







  /**
   * <p>Title: OpenSwing Framework</p>
   * <p>Description: Inner class used to draw the check-box (and its selection) inside a JLabel object (cell content).</p>
   * <p>Copyright: Copyright (C) 2006 Mauro Carniel</p>
   * <p> </p>
   * @author Mauro Carniel
   * @version 1.0
   */
  class CheckLabel extends JLabel {

    public void paintComponent(Graphics g) {
      super.paintComponent(g);
      if (selectionForeground!=null)
        g.setColor(selectionForeground);
      if (paintBorder && selectionForeground!=null)
        g.drawRect(0,0,this.getWidth()-1,this.getHeight()-1);

      g.translate((int)this.getWidth()/2-6,this.getHeight()/2-5);
      Color col1 = Color.black;
      Color col2 = Color.darkGray;
      g.setColor(Color.white);
      g.fillRect(0,0,12,12);
      BasicGraphicsUtils.drawLoweredBezel(g,0,0,12,12,Color.darkGray,Color.black,Color.white,Color.gray);
      if (allowNullValue && selected==null) {
        g.setColor(Color.lightGray);
        g.fillRect(1,1,10,10);
      }
      if (Boolean.TRUE.equals(selected)) {
        g.setColor(Color.black);
        g.drawLine(3,5,5,7);
        g.drawLine(3,6,5,8);
        g.drawLine(3,7,5,9);
        g.drawLine(6,6,9,3);
        g.drawLine(6,7,9,4);
        g.drawLine(6,8,9,5);
      }
    }

  }



}
