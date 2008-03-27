package org.openswing.swing.miscellaneous.client;

import java.util.*;

import java.awt.*;
import javax.swing.*;

import org.openswing.swing.util.client.*;


/**
 * <p>Title: OpenSwing Framework</p>
 * <p>Description: Progress bar that allows to:
 * - define minimum and maxixum values
 * - background color
 * - internal intervals and colors to use inside each interval.</p>
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
public class ProgressBar extends JPanel {

  /** minimum value allowed in the progress bar; default value = 0 */
  private double minValue = 0;

  /** maximum value allowed in the progress bar; default value: 100 */
  private double maxValue = 100;

  /** current value to set */
  private double currentValue = 0;

  /** current used color; default value: ClientSettings.GRID_SELECTION_BACKGROUND */
  private Color currentColor = ClientSettings.GRID_SELECTION_BACKGROUND;

  /** list of bands included in [minValue,maxValue] interval, having a specific color */
  private ArrayList coloredBands = new ArrayList();

  /** flag used to determine how to color the progress bar: <code>true</code> to paint the bar with many colors, each band with its color, <code>false</code> to paint the bar with ony one color: the color related to the last matched band */
  private boolean showAllBands = true;


  public ProgressBar() {
    setPreferredSize(new Dimension(300,20));
    setBorder(BorderFactory.createLoweredBevelBorder());
    setOpaque(false);
  }


  public final void paint(Graphics g) {
    if (coloredBands.size()>0) {
      if (showAllBands) {
        // color several bands, each one with its color, until "currentValue" limit...
        ColoredBand band = null;
        double val;
        for(int i=0;i<coloredBands.size();i++) {
          band = (ColoredBand)coloredBands.get(i);
          if (band.getMaxValue()<=currentValue) {
            val = Math.min(band.getMaxValue(),currentValue);
            g.setColor(band.getColor());
            g.fillRect(
                1+(int)((double)getWidth()*band.getMinValue()/(maxValue-minValue)),
                1,
                (int)((double)getWidth()*val/(maxValue-minValue)),
                getHeight()-2
            );
          }
          else
            break;

        }
      }
      else {
        // color the whole bar with only one color: the color related to the band that matches "currentValue" limit...
        ColoredBand band = null;
        double val;
        for(int i=coloredBands.size()-1;i>=0;i--) {
          band = (ColoredBand)coloredBands.get(i);
          if (band.getMaxValue()>=currentValue && band.getMinValue()<=currentValue) {
            g.setColor(band.getColor());
            g.fillRect(
                1,
                1,
                (int)((double)getWidth()*currentValue/(maxValue-minValue)),
                getHeight()-2
            );
            break;
          }
        }
      }
    }
    else  {
      g.setColor(currentColor);
      g.fillRect(1,1,(int)((double)getWidth()*currentValue/(maxValue-minValue)),getHeight()-2);
    }
    super.paint(g);
  }


  /**
   * Set current color to used to color the bar.
   * @param currentColor current color to used to color the bar
   */
  public final void setColor(Color currentColor) {
    this.currentColor = currentColor;
    repaint();
  }


  /**
   * @return current color to used to color the bar
   */
  public final Color getColor() {
    return this.currentColor;
  }


  /**
   * Set current value.
   * @param currentValue current value (between minValue and maxValue), used to show the bar inside this panel.
   */
  public synchronized final void setValue(double currentValue) {
    if (currentValue>maxValue)
      currentValue = maxValue;
    if (currentValue<minValue)
      currentValue = minValue;
    this.currentValue = currentValue;
    repaint();
  }


  /**
   * @return currentValue current value (between minValue and maxValue) showed in the bar inside this panel.
   */
  public final double getValue() {
    return currentValue;
  }


  /**
   * @return maximum value allowed in the progress bar
   */
  public final double getMaxValue() {
    return maxValue;
  }


  /**
   * @return minimum value allowed in the progress bar; default value = 0
   */
  public final double getMinValue() {
    return minValue;
  }


  /**
   * Set the maximum value allowed in the progress bar.
   * @param maxValue maximum value allowed in the progress bar
   */
  public final void setMaxValue(double maxValue) {
    this.maxValue = maxValue;
    repaint();
  }


  /**
   * Set the minimum value allowed in the progress bar.
   * @param minValue minimum value allowed in the progress bar
   */
  public final void setMinValue(double minValue) {
    this.minValue = minValue;
    repaint();
  }


  /**
   * Add an interval [minValue,maxValue] and a color to use to paint that interval.
   * @param minValue minimum value of this interval
   * @param maxValue maximum value of this interval
   * @param color color to use to paint this band
   * @return <code>true</code> if the specified interval does not match any other interval, <code>false</code> otherwise
   */
  public final boolean addColoredBand(double minValue,double maxValue,Color color) {
    if (minValue<this.minValue)
      minValue = this.minValue;
    if (maxValue>this.maxValue)
      maxValue = this.maxValue;
    if (minValue>=maxValue)
      return false;
    ColoredBand interv = null;
    int pos = 0;
    for(int i=0;i<coloredBands.size();i++) {
      interv = (ColoredBand)coloredBands.get(i);
      if (minValue>interv.getMinValue() && minValue<interv.getMaxValue())
        return false;
      if (maxValue>interv.getMinValue() && maxValue<interv.getMaxValue())
        return false;
      if (minValue>=interv.getMaxValue())
        pos = i+1;
    }
    coloredBands.add(pos,new ColoredBand(minValue,maxValue,color));
    return true;
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
   * <p>Title: OpenSwing Framework</p>
   * <p>Description: Inner class used to store a colored band.</p>
   * <p>Copyright: Copyright (C) 2006 Mauro Carniel</p>
   * @author Mauro Carniel
   * @version 1.0
   */
  class ColoredBand {

    /** minimum value of this band */
   private double minValue;

   /** maximum value of this band */
   private double maxValue;

   /** color used to paint this band */
   private Color color;

   public ColoredBand(double minValue,double maxValue,Color color) {
     this.minValue = minValue;
     this.maxValue = maxValue;
     this.color = color;
   }


   public double getMinValue() {
     return minValue;
   }


   public double getMaxValue() {
     return maxValue;
   }


   public Color getColor() {
     return color;
   }

  }

}
