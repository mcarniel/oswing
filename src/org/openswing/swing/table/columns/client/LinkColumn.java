package org.openswing.swing.table.columns.client;

import java.util.*;

import java.awt.event.*;
import javax.swing.*;
import javax.swing.table.*;

import org.openswing.swing.table.client.*;
import org.openswing.swing.table.editors.client.*;
import org.openswing.swing.table.renderers.client.*;
import org.openswing.swing.util.client.*;
import java.awt.ComponentOrientation;


/**
 * <p>Title: OpenSwing Framework</p>
 * <p>Description: Column of type link: it contains a cliccable link.
 * Attribute name binded with this column is related to link text.
 * Moreover, another attribute must be binded to this column: "uriAttributeName" property is used to fetch URI to open
 * when clicking om the link.
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
public class LinkColumn extends Column {

  /** button action listeners */
  private ArrayList actionListeners = new ArrayList();

  /** attribute name linked to the uri of this link, used to bind this link to a row of GridControl's value object; this is the URI to automatically open when clicking on link */
  public String uriAttributeName = null;

  /** component orientation */
  private ComponentOrientation orientation = ClientSettings.TEXT_ORIENTATION;


  public LinkColumn() {
    setColumnRequired(false);
    setTextAlignment(SwingConstants.CENTER);
  }


  /**
   * @return column type
   */
  public int getColumnType() {
    return TYPE_LINK;
  }


  /**
   * Add an ItemListener to the link.
   * @param listener ActionListener to add
   */
  public final void addActionListener(ActionListener listener) {
    actionListeners.add(listener);
  }


  /**
   * Remove an ActionListener from the link.
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
   * @return TableCellRenderer for this column
   */
  public final TableCellRenderer getCellRenderer(GridController tableContainer,Grids grids) {
    return new LinkTableCellRenderer(
      tableContainer,
      getTextAlignment(),
      getUriAttributeName(),
      getTextOrientation(),
      getColumnName()
    );
  }


  /**
   * @return TableCellEditor for this column
   */
  public final TableCellEditor getCellEditor(GridController tableContainer,Grids grids) {
    return new LinkCellEditor(
      tableContainer,
      getUriAttributeName(),
      getActionListeners(),
      getTextOrientation(),
      getColumnName()
    );
  }


  /**
   * Set the component orientation: from left to right or from right to left.
   * @param orientation component orientation
   */
  public final void setTextOrientation(ComponentOrientation orientation) {
    this.orientation = orientation;
  }


  /**
   * @return component orientation
   */
  public final ComponentOrientation getTextOrientation() {
      return orientation;
  }


  /**
   * @return attribute name linked to the uri of this link, used to bind this link to a row of GridControl's value object; this is the URI to automatically open when clicking on link
   */
  public final String getUriAttributeName() {
    return uriAttributeName;
  }


  /**
   * Set the attribute name linked to the uri of this link, used to bind this link to a row of GridControl's value object; this is the URI to automatically open when clicking on link.
   * @param uriAttributeName attribute name linked to the uri of this link, used to bind this link to a row of GridControl's value object
   */
  public final void setUriAttributeName(String uriAttributeName) {
    this.uriAttributeName = uriAttributeName;
  }


}
