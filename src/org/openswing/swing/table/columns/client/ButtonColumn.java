package org.openswing.swing.table.columns.client;

import java.util.*;

import java.awt.event.*;
import javax.swing.*;
import javax.swing.table.*;

import org.openswing.swing.table.client.*;
import org.openswing.swing.table.editors.client.*;
import org.openswing.swing.table.renderers.client.*;
import org.openswing.swing.util.client.*;
import javax.swing.border.Border;


/**
 * <p>Title: OpenSwing Framework</p>
 * <p>Description: Column of type button: it contains a button with text "..."
 * The button text can be redefined.</p>
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
public class ButtonColumn extends Column {

  /** button text; default value: "..." */
  private String text = "...";

  /** button action listeners */
  private ArrayList actionListeners = new ArrayList();

  /** flag used to indicate that attribute value will be showed as button text; default value: <code>false</code> i.e. the button text is defined by the "text" property */
  private boolean showAttributeValue = false;

  /** flag used to indicate if the button is enabled also when the grid is in readonly mode; default value: <code>false</code> i.e. the button is enabled only in INSERT/EDIT modes, according to "editableOnEdit" and "editableOnInsert" properties */
  private boolean enableInReadOnlyMode = false;

  /** icon to render within the button (optional) */
  private Icon icon = null;

  /** file name (inside "image" folder) related to the icon to render within the button (optional) */
  private String iconName = null;

  /** define the border to show around the button */
  private Border buttonBorder = null;


  public ButtonColumn() {
    setColumnRequired(false);
    setTextAlignment(SwingConstants.CENTER);
  }


  /**
   * @return column type
   */
  public int getColumnType() {
    return TYPE_BUTTON;
  }


  /**
   * @return button text
   */
  public final String getText() {
    return text;
  }


  /**
   * Set button text.
   * @param text button text
   */
  public final void setText(String text) {
    this.text = text;
  }


  /**
   * Add an ItemListener to the button.
   * @param listener ActionListener to add
   */
  public final void addActionListener(ActionListener listener) {
    actionListeners.add(listener);
  }


  /**
   * Remove an ActionListener from the button.
   * @param listener ActionListener to remove
   */
  public final void removeActionListener(ActionListener listener) {
    actionListeners.remove(listener);
  }


  /**
   * @return ActionListener objects
   */
  public final ArrayList getActionListeners() {
    return actionListeners;
  }


  /**
   * @return indicate that attribute value will be showed as button text; default value: <code>false</code> i.e. the button text is defined by the "text" property
   */
  public final boolean isShowAttributeValue() {
    return showAttributeValue;
  }


  /**
   * Indicate that attribute value will be showed as button text; default value: <code>false</code> i.e. the button text is defined by the "text" property
   * @param showAttributeValue indicate that attribute value will be showed as button text
   */
  public final void setShowAttributeValue(boolean showAttributeValue) {
    this.showAttributeValue = showAttributeValue;
  }


  /**
   * @return indicate if the button is enabled also when the grid is in readonly mode; default value: <code>false</code> i.e. the button is enabled only in INSERT/EDIT modes, according to "editableOnEdit" and "editableOnInsert" properties
   */
  public final boolean isEnableInReadOnlyMode() {
    return enableInReadOnlyMode;
  }


  /**
   * Define if the button is enabled also when the grid is in readonly mode.
   * @param enableInReadOnlyMode flag used to indicate if the button is enabled also when the grid is in readonly mode; <code>false</code> means that the button is enabled only in INSERT/EDIT modes, according to "editableOnEdit" and "editableOnInsert" properties
   */
  public final void setEnableInReadOnlyMode(boolean enableInReadOnlyMode) {
    this.enableInReadOnlyMode = enableInReadOnlyMode;
  }


  /**
   * @return TableCellRenderer for this column
   */
  public final TableCellRenderer getCellRenderer(GridController tableContainer,Grids grids) {
    return new ButtonTableCellRenderer(
      getText(),
      isShowAttributeValue(),
      tableContainer,
      getTextAlignment(),
      isEnableInReadOnlyMode(),
      icon,
      buttonBorder,
      getColumnName()
    );
  }


  /**
   * @return TableCellEditor for this column
   */
  public final TableCellEditor getCellEditor(GridController tableContainer,Grids grids) {
    return new ButtonCellEditor(
      getText(),
      isShowAttributeValue(),
      getActionListeners(),
      icon,
      buttonBorder
    );
  }


  /**
   * @return icon to render within the button (optional)
   */
  public final Icon getIcon() {
    return icon;
  }


  /**
   * Set the icon to render within the button (optional).
   * @param icon icon to render within the button (optional)
   */
  public final void setIcon(Icon icon) {
    this.icon = icon;
  }


  /**
   * @return file name (inside "image" folder) related to the icon to render within the button (optional)
   */
  public final String getIconName() {
    return iconName;
  }


  /**
   * Set the file name (inside "image" folder) related to the icon to render within the button (optional)
   * @param iconName file name (inside "image" folder) related to the icon to render within the button (optional)
   */
  public final void setIconName(String iconName) {
    this.iconName = iconName;
    if (iconName!=null)
      icon = new ImageIcon(ClientUtils.getImage(iconName));

  }


  /**
   * @return border to show around the button
   */
  public final Border getBorder() {
    return buttonBorder;
  }

  /**
   * Define the border to show around the button.
   * @param buttonBorder border to show around the button
   */
  public final void setBorder(Border buttonBorder) {
    this.buttonBorder = buttonBorder;
  }


}
