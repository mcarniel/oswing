package org.openswing.swing.table.columns.client;

import javax.swing.table.*;

import org.openswing.swing.table.client.*;
import org.openswing.swing.table.editors.client.*;
import org.openswing.swing.table.renderers.client.*;
import org.openswing.swing.util.client.ClientSettings;
import java.awt.ComponentOrientation;


/**
 * <p>Title: OpenSwing Framework</p>
 * <p>Description: Column of type text:
 * it contains a multi-line text, inside a text area field.</p>
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
public class MultiLineTextColumn extends Column {

  /** maximum number of characters */
  private int maxCharacters = 255;

  /** component left margin, with respect to component container; defaut value: 2 */
  private int leftMargin = 2;

  /** component right margin, with respect to component container; defaut value: 0 */
  private int rightMargin = 0;

  /** component top margin, with respect to component container; defaut value: 0 */
  private int topMargin = 0;

  /** component bottom margin, with respect to component container; defaut value: 0 */
  private int bottomMargin = 0;

  /** flag used in grid to automatically select data in cell when editing cell; default value: ClientSettings.SELECT_DATA_IN_EDIT; <code>false</code>to do not select data stored cell; <code>true</code> to automatically select data already stored in cell */
  private boolean selectDataOnEdit = ClientSettings.SELECT_DATA_IN_EDITABLE_GRID;

  /** component orientation */
  private ComponentOrientation orientation = ClientSettings.TEXT_ORIENTATION;


  public MultiLineTextColumn() { }


  /**
   * @return column type
   */
  public int getColumnType() {
    return TYPE_MULTI_LINE_TEXT;
  }


  /**
   * Set maximum number of characters.
   * @param maxCharacters maximum number of characters
   */
  public void setMaxCharacters(int maxCharacters) {
    this.maxCharacters = maxCharacters;
  }


  /**
   * @return maximum number of characters
   */
  public int getMaxCharacters() {
    return maxCharacters;
  }


  /**
   * @return component bottom margin, with respect to component container
   */
  public final int getBottomMargin() {
    return bottomMargin;
  }


  /**
   * @return component left margin, with respect to component container
   */
  public final int getLeftMargin() {
    return leftMargin;
  }


  /**
   * @return component right margin, with respect to component container
   */
  public final int getRightMargin() {
    return rightMargin;
  }


  /**
   * @return component top margin, with respect to component container
   */
  public final int getTopMargin() {
    return topMargin;
  }


  /**
   * Set component top margin, with respect to component container.
   * @param topMargin component top margin
   */
  public final void setTopMargin(int topMargin) {
    this.topMargin = topMargin;
  }


  /**
   * Set component right margin, with respect to component container.
   * @param rightMargin component right margin
   */
  public final void setRightMargin(int rightMargin) {
    this.rightMargin = rightMargin;
  }


  /**
   * Set component left margin, with respect to component container.
   * @param leftMargin component left margin
   */
  public final void setLeftMargin(int leftMargin) {
    this.leftMargin = leftMargin;
  }


  /**
   * Set component bottom margin, with respect to component container.
   * @param bottomMargin component bottom margin
   */
  public final void setBottomMargin(int bottomMargin) {
    this.bottomMargin = bottomMargin;
  }


  /**
   * @return <code>false</code>to do not select data stored cell; <code>true</code> to automatically select data already stored in cell
   */
  public final boolean isSelectDataOnEdit() {
    return selectDataOnEdit;
  }


  /**
   * Define if data stored in cell must be selected when cell is set in edit
   * @param selectDataOnEdit <code>false</code>to do not select data stored cell; <code>true</code> to automatically select data already stored in cell
   */
  public final void setSelectDataOnEdit(boolean selectDataOnEdit) {
    this.selectDataOnEdit = selectDataOnEdit;
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
   * @return TableCellRenderer for this column
   */
  public final TableCellRenderer getCellRenderer(GridController tableContainer,Grids grids) {
    return new MultiLineTextTableCellRenderer(
      tableContainer,
      leftMargin,
      rightMargin,
      topMargin,
      bottomMargin,
      getTextOrientation(),
      getColumnName()
    );
  }


  /**
   * @return TableCellEditor for this column
   */
  public final TableCellEditor getCellEditor(GridController tableContainer,Grids grids) {
    return new MultiLineTextCellEditor(
      getMaxCharacters(),
      isColumnRequired(),
      selectDataOnEdit,
      getTextOrientation()
    );
  }




}
