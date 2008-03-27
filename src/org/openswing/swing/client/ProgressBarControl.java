package org.openswing.swing.client;

import java.math.*;

import java.awt.*;
import javax.swing.*;

import org.openswing.swing.miscellaneous.client.*;


/**
 * <p>Title: OpenSwing Framework</p>
 * <p>Description: Progress bar control: this is a ready only control that shows in a progress bar the current attribute value.
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
public class ProgressBarControl extends BaseInputControl implements InputControl {

  /** progress bar */
  private ProgressBar progressBar = new ProgressBar();


  /**
   * Constructor.
   */
  public ProgressBarControl() {
    this(10);
  }


  /**
   * Constructor.
   * @param columns number of visibile characters
   */
  public ProgressBarControl(int columns) {
    this.setLayout(new GridBagLayout());
    this.add(progressBar, new GridBagConstraints(0, 0, 1, 1, 1.0, 1.0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
//    initListeners();
  }


  /**
   * @return component inside this whose contains the value
   */
  public JComponent getBindingComponent() {
    return progressBar;
  }


  /**
   * Add an interval [minValue,maxValue] and a color to use to paint that interval.
   * @param minValue minimum value of this interval
   * @param maxValue maximum value of this interval
   * @param color color to use to paint this band
   * @return <code>true</code> if the specified interval does not match any other interval, <code>false</code> otherwise
   */
  public final boolean addColoredBand(double minValue,double maxValue,Color color) {
    return progressBar.addColoredBand(minValue,maxValue,color);
  }


  /**
   * @return current value
   */
  public final BigDecimal getBigDecimal() {
    return new BigDecimal(progressBar.getValue());
  }


  /**
   * @return Double value
   */
  public final Double getDouble() {
    return new Double(progressBar.getValue());
  }


  /**
   * @return BigDecimal value
   */
  public final Object getValue() {
    return getBigDecimal();
  }


  /**
   * @return maximum value
   */
  public double getMaxValue() {
    return progressBar.getMaxValue();
  }


  /**
   * Set maximum value.
   * @param maxValue maximum value
   */
  public void setMaxValue(double maxValue) {
    progressBar.setMaxValue(maxValue);
  }


  /**
   * @return minimum value
   */
  public double getMinValue() {
    return progressBar.getMinValue();
  }


  /**
   * Set minimum value.
   * @param minValue minimum value
   */
  public void setMinValue(double minValue) {
    progressBar.setMinValue(minValue);
  }


  /**
   * @param value number to set in the progress bar
   */
  public final void setValue(Object value) {
    if (value!=null && value instanceof Number)
    progressBar.setValue(((Number)value).doubleValue());
  }


  /**
   * @return flag used to determine how to color the progress bar: <code>true</code> to paint the bar with many colors, each band with its color, <code>false</code> to paint the bar with ony one color: the color related to the last matched band
   */
  public final boolean isShowAllBands() {
    return progressBar.isShowAllBands();
  }


  /**
   * Determine how to color the progress bar: <code>true</code> to paint the bar with many colors, each band with its color, <code>false</code> to paint the bar with ony one color: the color related to the last matched band.
   * @param showAllBands flag used to determine how to color the progress bar: <code>true</code> to paint the bar with many colors, each band with its color, <code>false</code> to paint the bar with ony one color: the color related to the last matched band
   */
  public final void setShowAllBands(boolean showAllBands) {
    progressBar.setShowAllBands(showAllBands);
  }


  /**
   * Set current color to used to color the bar.
   * @param currentColor current color to used to color the bar
   */
  public final void setColor(Color currentColor) {
    progressBar.setColor(currentColor);
  }


  /**
   * @return current color to used to color the bar
   */
  public final Color getColor() {
    return progressBar.getColor();
  }


  /**
   * Replace enabled setting with editable setting (this allow tab swithing).
   * @param enabled flag used to set abilitation of control
   */
  public void setEnabled(boolean enabled) {
  }


  /**
   * @return current input control abilitation
   */
  public final boolean isEnabled() {
    return false;
  }



}

