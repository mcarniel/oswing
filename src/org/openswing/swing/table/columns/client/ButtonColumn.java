package org.openswing.swing.table.columns.client;

import java.util.ArrayList;
import java.awt.event.ActionListener;
import org.openswing.swing.util.client.ClientSettings;
import javax.swing.SwingConstants;


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
   * Add an ItemListener to the combo.
   * @param listener ActionListener to add
   */
  public final void addActionListener(ActionListener listener) {
    actionListeners.add(listener);
  }


  /**
   * Remove an ActionListener from the combo.
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



}
