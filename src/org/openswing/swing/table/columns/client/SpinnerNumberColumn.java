package org.openswing.swing.table.columns.client;

import java.util.*;

import java.awt.event.*;
import javax.swing.table.*;

import org.openswing.swing.domains.java.*;
import org.openswing.swing.logger.client.*;
import org.openswing.swing.table.client.*;
import org.openswing.swing.table.editors.client.*;
import org.openswing.swing.table.renderers.client.*;
import org.openswing.swing.util.client.*;
import java.awt.ComponentOrientation;
import javax.swing.event.ChangeListener;
import javax.swing.JTextField;
import org.openswing.swing.table.renderers.client.TextTableCellRenderer;
import org.openswing.swing.table.editors.client.SpinnerNumberCellEditor;


/**
 * <p>Title: OpenSwing Framework</p>
 * <p>Description: Column of type spinner number.</p>
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
public class SpinnerNumberColumn extends Column {

  /** change listeners */
  private ArrayList changeListeners = new ArrayList();

  /** component left margin, with respect to component container; defaut value: 2 */
  private int leftMargin = 2;

  /** component right margin, with respect to component container; defaut value: 0 */
  private int rightMargin = 0;

  /** component top margin, with respect to component container; defaut value: 0 */
  private int topMargin = 0;

  /** component bottom margin, with respect to component container; defaut value: 0 */
  private int bottomMargin = 0;

  /** component orientation */
  private ComponentOrientation orientation = ClientSettings.TEXT_ORIENTATION;

  /** horizontal alignment */
  private int horizontalAlignment = JTextField.RIGHT;

  /** maximum value */
  private Double maxValue = new Double(Integer.MAX_VALUE);

  /** minimum value; default value: 0 */
  private Double minValue = new Double(0);

  /** initial value; default value: 0 */
  private Double initialValue = new Double(0);

  /** increment value; default value: 1 */
  private Double step = new Double(1);

  /** TextTableCellRenderer (cell renderer), one for each grid controller (top locked rows, bottom locked rows, etc.) */
  private HashMap renderers = new HashMap();

  /** SpinnerNumberCellEditor (cell editor), one for each grid controller (top locked rows, bottom locked rows, etc.) */
  private HashMap editors = new HashMap();


  public SpinnerNumberColumn() { }


  /**
   * @return column type
   */
  public int getColumnType() {
    return TYPE_SPINNER_NUMBER;
  }


  /**
   * Add an ChangeListener to the combo.
   * @param listener ChangeListener to add
   */
  public final void addChangeListener(ChangeListener listener) {
    changeListeners.add(listener);
  }


  /**
   * Remove an ChangeListener from the combo.
   * @param listener ChangeListener to remove
   */
  public final void removeChangeListener(ChangeListener listener) {
    changeListeners.remove(listener);
  }


  /**
   * @return ChangeListener objects
   */
  public final ArrayList getChangeListeners() {
    return changeListeners;
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
   * Set horizontal alignment.
   * @param horizontalAlignment horizontal alignment
   */
  public final void setHorizontalAlignment(int horizontalAlignment) {
    this.horizontalAlignment = horizontalAlignment;
  }


  /**
   * @return horizontal alignment
   */
  public final int getHorizontalAlignment() {
    return horizontalAlignment;
  }


  /**
   * @return maximum value
   */
  public final Double getMaxValue() {
    return maxValue;
  }


  /**
   * Set maximum value.
   * @param maxValue maximum value
   */
  public final void setMaxValue(Double maxValue) {
    this.maxValue = maxValue;
  }


  /**
   * @return minimum value
   */
  public final Double getMinValue() {
    return minValue;
  }


  /**
   * Set minimum value.
   * @param minValue minimum value
   */
  public final void setMinValue(Double minValue) {
    this.minValue = minValue;
  }


  /**
   * @return increment value; default value: 1
   */
  public final Double getStep() {
    return step;
  }


  /**
   * Set the increment value. Default value: 1
   * @param step increment value;
   */
  public final void setStep(Double step) {
    this.step = step;
  }


  /**
   * @return initial value
   */
  public final Double getInitialValue() {
    return initialValue;
  }


  /**
   * Set the initial value; default value: 0
   * @param initialValue initial value
   */
  public final void setInitialValue(Double initialValue) {
    this.initialValue = initialValue;
  }




  /**
   * @return TableCellRenderer for this column
   */
  public final TableCellRenderer getCellRenderer(GridController tableContainer,Grids grids) {
    TextTableCellRenderer renderer = (TextTableCellRenderer)renderers.get(tableContainer.toString());
    if (renderer==null) {
      renderer = new TextTableCellRenderer(
          tableContainer,
          false,
          getTextAlignment(),
          getLeftMargin(),
          getRightMargin(),
          getTopMargin(),
          getBottomMargin(),
          getTextOrientation(),
          getColumnName()
      );
      renderers.put(tableContainer.toString(),renderer);
    }
    return renderer;
  }


  /**
   * @return TableCellEditor for this column
   */
  public final TableCellEditor getCellEditor(GridController tableContainer,Grids grids) {
    SpinnerNumberCellEditor editor = (SpinnerNumberCellEditor)editors.get(tableContainer.toString());
    if (editor==null) {
      editor = new SpinnerNumberCellEditor(
          isColumnRequired(),
          getTextAlignment(),
          getChangeListeners(),
          getMaxValue(),
          getMinValue(),
          getInitialValue(),
          getStep(),
          getTextOrientation()
      );
      editors.put(tableContainer.toString(),editor);
    }
    return editor;
  }


}
