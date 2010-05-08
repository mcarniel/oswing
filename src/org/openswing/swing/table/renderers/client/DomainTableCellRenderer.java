package org.openswing.swing.table.renderers.client;

import java.awt.*;
import javax.swing.*;
import javax.swing.table.*;

import org.openswing.swing.domains.java.*;
import org.openswing.swing.table.client.*;
import org.openswing.swing.util.client.*;
import org.openswing.swing.util.java.*;


/**
 * <p>Title: OpenSwing Framework</p>
 * <p>Description: Column renderer used to format a domain type column.</p>
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
public class DomainTableCellRenderer extends DefaultTableCellRenderer {

  /** cell content */
  private JLabel rend = new JLabel();

  /** domain linked to the column */
  private Domain domain = null;

  /** grid controller */
  private GridController gridController = null;

  /** default font */
  private Font defaultFont = null;

  /** attribute name associated to this column */
  private String attributeName = null;

  /** component container */
  private JPanel container = new JPanel();

  /** component left margin, with respect to component container */
  private int leftMargin = 0;

  /** component right margin, with respect to component container */
  private int rightMargin = 0;

  /** component top margin, with respect to component container */
  private int topMargin = 0;

  /** component bottom margin, with respect to component container */
  private int bottomMargin = 0;

  /** define if description in combo items must be translated */
  private boolean translateItemDescriptions;


  /**
   * Constructor.
   * @param domain domain linked to the column
   * @param gridController grid controller
   * @param attributeName attribute name associated to this column
   */
  public DomainTableCellRenderer(Domain domain,boolean translateItemDescriptions,GridController gridController,int alignement,
                                 int leftMargin,int rightMargin,int topMargin,int bottomMargin,
                                 ComponentOrientation orientation,String attributeName) {
    this.domain = domain;
    this.translateItemDescriptions = translateItemDescriptions;
    this.gridController = gridController;
    this.leftMargin = leftMargin;
    this.rightMargin = rightMargin;
    this.topMargin = topMargin;
    this.bottomMargin = bottomMargin;
    this.attributeName = attributeName;
    rend.setOpaque(true);
    rend.setHorizontalAlignment(alignement);

    if (orientation!=null)
        setComponentOrientation(orientation);
  }


  /**
   * Method invoked by ComboColumn, when Domain is changed after grid is already showed.
   * @param domain new Domain to set
   */
  public final void setDomain(Domain domain) {
    this.domain = domain;
  }


  public Component getTableCellRendererComponent(JTable table, Object value,
                          boolean isSelected, boolean hasFocus, int row, int column) {
    if (defaultFont==null)
      defaultFont = rend.getFont();
    DomainPair pair = (DomainPair)domain.getDomainPair(value);
    if (pair==null)
      rend.setText(null);
    else if (translateItemDescriptions)
      rend.setText(ClientSettings.getInstance().getResources().getResource(pair.getDescription()));
    else
      rend.setText(pair.getDescription());

    if (hasFocus && table instanceof Grid) {
      if (ClientSettings.IGNORE_GRID_SELECTION_FOREGROUND)
        rend.setForeground(gridController.getForegroundColor(row,table.getModel().getColumnName(table.convertColumnIndexToModel(column)),value));
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
      Color backColor = gridController.getBackgroundColor(row,table.getModel().getColumnName(table.convertColumnIndexToModel(column)),value);
      rend.setBackground(new Color(
          (backColor.getRed()+selColor.getRed())/2,
          (backColor.getGreen()+selColor.getGreen())/2,
          (backColor.getBlue()+selColor.getBlue())/2
      ));
      rend.setBorder(BorderFactory.createLineBorder(table.getSelectionForeground()));
    } else if (isSelected && !hasFocus) {
      if (ClientSettings.IGNORE_GRID_SELECTION_FOREGROUND)
        rend.setForeground(gridController.getForegroundColor(row,table.getModel().getColumnName(table.convertColumnIndexToModel(column)),value));
      else
        rend.setForeground(table.getSelectionForeground());

      Color backColor = gridController.getBackgroundColor(row,table.getModel().getColumnName(table.convertColumnIndexToModel(column)),value);
//      Color selColor = table.getSelectionBackground();
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
//      rend.setBackground(table.getSelectionBackground());
      rend.setBorder(BorderFactory.createEmptyBorder());
    } else {
      rend.setForeground(gridController.getForegroundColor(row,table.getModel().getColumnName(table.convertColumnIndexToModel(column)),value));
      rend.setBorder(BorderFactory.createEmptyBorder());
      if (((Grid)table).getMode()==Consts.READONLY || !((Grid)table).isColorsInReadOnlyMode())
        rend.setBackground(gridController.getBackgroundColor(row,table.getModel().getColumnName(table.convertColumnIndexToModel(column)),value));
      else {
        if (table.isCellEditable(row,column))
          rend.setBackground(ClientSettings.GRID_EDITABLE_CELL_BACKGROUND);
        else
          rend.setBackground(ClientSettings.GRID_NOT_EDITABLE_CELL_BACKGROUND);
      }
    }


    Font f = gridController.getFont(row,table.getModel().getColumnName(table.convertColumnIndexToModel(column)),value,defaultFont);
    if (f!=null)
      rend.setFont(f);
    else
      rend.setFont(defaultFont);

    if (table instanceof Grid)
      rend.setToolTipText(gridController.getCellTooltip(row,((Grid)table).getVOListTableModel().getColumnName(table.convertColumnIndexToModel(column))));

    rend.setBorder(
      BorderFactory.createCompoundBorder(rend.getBorder(),BorderFactory.createEmptyBorder(topMargin,leftMargin,bottomMargin,rightMargin))
    );
    return rend;
  }


  public final void finalize() {
    gridController = null;
    rend = null;
  }


}


