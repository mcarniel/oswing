package org.openswing.swing.table.columns.client;

import java.util.*;

import java.awt.*;
import javax.swing.table.*;

import org.openswing.swing.table.client.*;
import org.openswing.swing.table.editors.client.*;
import org.openswing.swing.table.renderers.client.*;
import org.openswing.swing.util.client.*;


/**
 * <p>Title: OpenSwing Framework</p>
 * <p>Description: Progress bar column: this is a ready only column that shows in a progress bar the current attribute value.
 * The type of the binded attribute must be an instance of Number.</p>
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
public class ProgressBarColumn extends Column {

  /** list of colored bands */
  private ArrayList coloredBands = new ArrayList();

  /** max value */
  private double maxValue;

  /** minimum value */
  private double minValue;

  /** flag used to determine how to color the progress bar: <code>true</code> to paint the bar with many colors, each band with its color, <code>false</code> to paint the bar with ony one color: the color related to the last matched band; default value: true */
  private boolean showAllBands = true;

  /** current used color; default value: ClientSettings.GRID_SELECTION_BACKGROUND */
  private Color currentColor = ClientSettings.GRID_SELECTION_BACKGROUND;


  public ProgressBarColumn() {
  }


  /**
   * @return coumn type
   */
  public final int getColumnType() {
    return TYPE_PROGRESS_BAR;
  }


  /**
   * Add an interval [minValue,maxValue] and a color to use to paint that interval.
   * @param minValue minimum value of this interval
   * @param maxValue maximum value of this interval
   * @param color color to use to paint this band
   */
  public final void addColoredBand(double minValue,double maxValue,Color color) {
    coloredBands.add(new Object[]{new Double(minValue),new Double(maxValue),color});
  }


  /**
   * @return maximum value
   */
  public double getMaxValue() {
    return maxValue;
  }


  /**
   * Set maximum value.
   * @param maxValue maximum value
   */
  public void setMaxValue(double maxValue) {
    this.maxValue = maxValue;
  }


  /**
   * @return minimum value
   */
  public double getMinValue() {
    return minValue;
  }


  /**
   * Set minimum value.
   * @param minValue minimum value
   */
  public void setMinValue(double minValue) {
    this.minValue = minValue;
  }


  /**
   * @return flag used to determine how to color the progress bar: <code>true</code> to paint the bar with many colors, each band with its color, <code>false</code> to paint the bar with ony one color: the color related to the last matched band
   */
  public final boolean isShowAllBands() {
    return showAllBands;
  }


  /**
   * Determine how to color the progress bar: <code>true</code> to paint the bar with many colors, each band with its color, <code>false</code> to paint the bar with ony one color: the color related to the last matched band.
   * @param showAllBands flag used to determine how to color the progress bar: <code>true</code> to paint the bar with many colors, each band with its color, <code>false</code> to paint the bar with ony one color: the color related to the last matched band
   */
  public final void setShowAllBands(boolean showAllBands) {
    this.showAllBands = showAllBands;
  }


  /**
   * Set current color to used to color the bar.
   * @param currentColor current color to used to color the bar
   */
  public final void setColor(Color currentColor) {
    this.currentColor = currentColor;
  }


  /**
   * @return current color to used to color the bar
   */
  public final Color getColor() {
    return currentColor;
  }


  /**
   * @return column editing on edit grid mode
   */
  public final boolean isEditableOnEdit() {
    return false;
  }


  /**
   * @return column editing on insert grid mode
   */
  public final boolean isEditableOnInsert() {
    return false;
  }


  /**
   * @return list of colored bands
   */
  public final ArrayList getColoredBands() {
    return coloredBands;
  }


  /**
   * @return TableCellRenderer for this column
   */
  public final TableCellRenderer getCellRenderer(GridController tableContainer,Grids grids) {
    return new ProgressBarTableCellRenderer(
      tableContainer,
      getColoredBands(),
      getMinValue(),
      getMaxValue(),
      isShowAllBands(),
      getColor(),
      getColumnName()
    );
  }


  /**
   * @return TableCellEditor for this column
   */
  public final TableCellEditor getCellEditor(GridController tableContainer,Grids grids) {
    return new TextCellEditor(
        0,
        false
    );
  }


}
