package org.openswing.swing.table.renderers.client;

import java.lang.reflect.*;
import java.util.*;

import java.awt.*;
import javax.swing.*;
import javax.swing.table.*;

import org.openswing.swing.items.client.*;
import org.openswing.swing.message.receive.java.*;
import org.openswing.swing.table.client.*;
import org.openswing.swing.table.columns.client.*;
import org.openswing.swing.util.client.*;
import org.openswing.swing.util.java.*;


/**
 * <p>Title: OpenSwing Framework</p>
 * <p>Description: Column renderer used to format a ComboVOColumn.</p>
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
public class ComboVOTableCellRenderer extends DefaultTableCellRenderer {


  /** grid controller */
  private GridController gridController = null;

  /** default font */
  private Font defaultFont = null;

  /** items data source */
  private ItemsDataLocator itemsDataLocator = null;

  /** items value object */
  private ValueObject itemsVO = null;

  /** columns associated to lookup grid */
  private Column[] colProperties = new Column[0];

  /** flag used to set visibility on all columns of lookup grid; default "false"  */
  private boolean allColumnVisible = false;

  /** default preferredWidth for all columns of lookup grid; default 100 pixels */
  private int allColumnPreferredWidth = 100;

  /** collection of pairs <v.o. attribute name,Method object, related to the attribute getter method> */
  private Hashtable getters = new Hashtable();

  /** value objects list  */
  private java.util.List items = null;

  /** cell content */
  private ItemRenderer rend = new ItemRenderer();

  /** attribute name associated to this column */
  private String attributeName = null;

  /** component left margin, with respect to component container */
  private int leftMargin = 0;

  /** component right margin, with respect to component container */
  private int rightMargin = 0;

  /** component top margin, with respect to component container */
  private int topMargin = 0;

  /** component bottom margin, with respect to component container */
  private int bottomMargin = 0;

  /** attribute name in the combo-box v.o. that identify the attribute name in the v.o. of the combo-box container; as default value this attribute is null; null means that "attributeName" property will be used to identify the v.o. in the combo-box, i.e. the attribute names in the combo-box v.o. and in the container v.o. must have the same name */
  private String foreignKeyAttributeName;


  /**
   * Constructor.
   * @param domain domain linked to the column
   * @param gridController grid controller
   * @param attributeName attribute name associated to this column
   */
  public ComboVOTableCellRenderer(
      ItemsDataLocator itemsDataLocator,
      String attributeName,
      ValueObject itemsVO,
      Column[] colProperties,
      boolean allColumnVisible,
      int allColumnPreferredWidth,
      Hashtable getters,
      GridController gridController,
      int leftMargin,int rightMargin,int topMargin,int bottomMargin,
      ComponentOrientation orientation,
      String foreignKeyAttributeName
  ) {
    this.itemsDataLocator = itemsDataLocator;
    this.attributeName = attributeName;
    this.itemsVO = itemsVO;
    this.colProperties = colProperties;
    this.allColumnVisible = allColumnVisible;
    this.allColumnPreferredWidth = allColumnPreferredWidth;
    this.getters = getters;
    this.gridController = gridController;
    this.leftMargin = leftMargin;
    this.rightMargin = rightMargin;
    this.topMargin = topMargin;
    this.bottomMargin = bottomMargin;
    this.foreignKeyAttributeName = foreignKeyAttributeName;
    rend.setOpaque(true);
    setComponentOrientation(orientation);

    if (itemsDataLocator!=null && itemsVO!=null) {
      Response res = itemsDataLocator.loadData(itemsVO.getClass());
      if (!res.isError()) {
        items = ((VOListResponse)res).getRows();
      }
    }

    rend.init(getters,colProperties,leftMargin,rightMargin,topMargin,bottomMargin);
  }


  /**
   * Method used to reload items in combo-box.
   */
  public final void reloadItems() {
    if (itemsDataLocator!=null && itemsVO!=null) {
      Response res = itemsDataLocator.loadData(itemsVO.getClass());
      if (!res.isError()) {
        items = ((VOListResponse)res).getRows();
        repaint();
      }
    }
  }


  public Component getTableCellRendererComponent(JTable table, Object value,
                          boolean isSelected, boolean hasFocus, int row, int column) {
    if (defaultFont==null)
      defaultFont = rend.getFont();

    Object obj = null;
    Object vo = null;
    int i = 0;
    try {
      if (value!=null)
        for (i = 0; i < items.size(); i++) {
          obj = ( (Method) getters.get(getFKAttributeName())).invoke(
              items.get(i),
              new Object[0]
          );
          if (value!=null && value.equals(obj))  {
            vo = items.get(i);
            break;
          }
          else
            obj = null;
        }
    }
    catch (Throwable ex) {
      obj = null;
    }

    JPanel c = (JPanel)rend.getListCellRendererComponent(new JList(),vo,i,false,false);
    if (hasFocus && table instanceof Grid) {
      if (ClientSettings.IGNORE_GRID_SELECTION_FOREGROUND)
        c.setForeground(gridController.getForegroundColor(row,table.getModel().getColumnName(table.convertColumnIndexToModel(column)),value));
      else
        c.setForeground(table.getSelectionForeground());

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
      c.setBackground(new Color(
          (backColor.getRed()+selColor.getRed())/2,
          (backColor.getGreen()+selColor.getGreen())/2,
          (backColor.getBlue()+selColor.getBlue())/2
      ));
      c.setBorder(BorderFactory.createLineBorder(table.getSelectionForeground()));
    } else if (isSelected && !hasFocus) {
      if (ClientSettings.IGNORE_GRID_SELECTION_FOREGROUND)
        c.setForeground(gridController.getForegroundColor(row,table.getModel().getColumnName(table.convertColumnIndexToModel(column)),value));
      else
        c.setForeground(table.getSelectionForeground());

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
      c.setBackground(new Color(
          (backColor.getRed()+selColor.getRed())/2,
          (backColor.getGreen()+selColor.getGreen())/2,
          (backColor.getBlue()+selColor.getBlue())/2
      ));
//      c.setBackground(table.getSelectionBackground());
      c.setBorder(BorderFactory.createEmptyBorder());
    } else {
      c.setForeground(gridController.getForegroundColor(row,table.getModel().getColumnName(table.convertColumnIndexToModel(column)),value));
      c.setBorder(BorderFactory.createEmptyBorder());
      if (((Grid)table).getMode()==Consts.READONLY || !((Grid)table).isColorsInReadOnlyMode())
        c.setBackground(gridController.getBackgroundColor(row,table.getModel().getColumnName(table.convertColumnIndexToModel(column)),value));
      else {
        if (table.isCellEditable(row,column))
          c.setBackground(ClientSettings.GRID_EDITABLE_CELL_BACKGROUND);
        else
          c.setBackground(ClientSettings.GRID_NOT_EDITABLE_CELL_BACKGROUND);
      }
    }


    Font f = gridController.getFont(row,table.getModel().getColumnName(table.convertColumnIndexToModel(column)),value,defaultFont);
    if (f!=null)
      c.setFont(f);
    else
      c.setFont(defaultFont);

    if (table instanceof Grid)
      c.setToolTipText(gridController.getCellTooltip(row,((Grid)table).getVOListTableModel().getColumnName(table.convertColumnIndexToModel(column))));

    c.setBorder(
      BorderFactory.createCompoundBorder(c.getBorder(),BorderFactory.createEmptyBorder(topMargin,leftMargin,bottomMargin,rightMargin))
    );
    return c;
  }


  public final void finalize() {
    gridController = null;
    items = null;
    itemsDataLocator = null;
    colProperties = null;
    rend = null;
  }


  /**
   * @return attribute name in the combo-box v.o. that identify the combo-box item
   */
  private String getFKAttributeName() {
    return
        foreignKeyAttributeName==null || foreignKeyAttributeName.equals("") ?
        attributeName :
        foreignKeyAttributeName;
  }


}


