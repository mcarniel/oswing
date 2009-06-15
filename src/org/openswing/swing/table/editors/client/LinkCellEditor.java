package org.openswing.swing.table.editors.client;

import java.util.*;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.table.*;

import org.openswing.swing.util.client.*;
import org.openswing.swing.client.LinkButton;
import org.openswing.swing.table.client.GridController;
import org.openswing.swing.table.client.Grid;


/**
 * <p>Title: OpenSwing Framework</p>
 * <p>Description: Column editor used to click a link (link type column).</p>
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
public class LinkCellEditor extends AbstractCellEditor implements TableCellEditor,ActionListener {

  /** link inside the editable cell */
  private LinkButton field = new LinkButton();

  /** attribute name linked to the text of this link, used to bind this link to a row of GridControl's value object */
  public String labelAttributeName = null;

  /** attribute name linked to the uri of this link, used to bind this link to a row of GridControl's value object; this is the URI to automatically open when clicking on link */
  public String uriAttributeName = null;

  /** current value */
  private Object value = null;

  /** list of ActionListeners linked to the button */
  private ArrayList actionListeners = null;

  /** table that contains this button */
  private JTable table = null;

  /** current selected row*/
  private int row = -1;

  /** grid controller */
  private GridController gridController = null;


  /**
   * Constructor.
   * @param text button text
   * @param actionListeners list of ActionListeners linked to the button
   */
  public LinkCellEditor(GridController gridController,String uriAttributeName,ArrayList actionListeners,
                        ComponentOrientation orientation,String labelAttributeName) {
    this.gridController = gridController;
    this.uriAttributeName = uriAttributeName;
    this.labelAttributeName = labelAttributeName;
    this.actionListeners = actionListeners;
    field.addActionListener(this);
    field.setOpaque(true);

    if (orientation!=null)
      field.setComponentOrientation(orientation);
  }


  public final void actionPerformed(ActionEvent e) {
    if (row!=-1)
        table.setRowSelectionInterval(row,row);
    if (actionListeners!=null)
      for(int i=0;i<actionListeners.size();i++)
        ((ActionListener)actionListeners.get(i)).actionPerformed(e);
  }


  /**
   * Stop cell editing. This method stops cell editing (effectively committing the edit) only if the data entered is validated successfully.
   * @return <code>true</code> if cell editing may stop, and <code>false</code> otherwise.
   */
  public final boolean stopCellEditing() {
    return validate();
  }


  /**
   * Perform the validation.
   */
  private final boolean validate() {
    fireEditingStopped();
    return true;
  }


  public final Object getCellEditorValue() {
    return value;
  }


  /**
   * Prepare the editor for a value.
   */
  private final Component _prepareEditor(Object value) {
    this.value = value;
    if (value!=null)
      field.setText(value.toString());
    else {
      field.setText("");
    }
    return field;
  }


  public final Component getTableCellEditorComponent(JTable table, Object value,
                                               boolean isSelected, int row,
                                               int column) {
    this.table = table;
    this.row = row;
//    field.setBackground(gridController.getBackgroundColor(row,table.getModel().getColumnName(table.convertColumnIndexToModel(column)),value));
    Color backColor = gridController.getBackgroundColor(row,table.getModel().getColumnName(table.convertColumnIndexToModel(column)),value);
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
    field.setBackground(new Color(
        (backColor.getRed()+selColor.getRed())/2,
        (backColor.getGreen()+selColor.getGreen())/2,
        (backColor.getBlue()+selColor.getBlue())/2
    ));
    table.setRowSelectionInterval(row,row);
    table.setColumnSelectionInterval(column,column);
    field.setBorder(BorderFactory.createLineBorder(table.getSelectionForeground()));
    field.setToolTipText(gridController.getCellTooltip(row,((Grid)table).getVOListTableModel().getColumnName(table.convertColumnIndexToModel(column))));
    field.setLabel( (String)((Grid)table).getVOListTableModel().getField(row,labelAttributeName) );
    field.setUri( (String)((Grid)table).getVOListTableModel().getField(row,uriAttributeName) );
    field.linkClicked();
    return _prepareEditor(value);
  }


  public final void finalize() {
    if (field!=null)
      field.removeActionListener(this);
    table = null;
    field = null;
    actionListeners.clear();
  }



}


