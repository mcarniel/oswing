package org.openswing.swing.miscellaneous.client;


import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

import org.openswing.swing.mdi.client.*;
import org.openswing.swing.util.client.*;


/**
 * <p>Title: OpenSwing Framework</p>
 * <p>Description: Dialog used to show a progress bar and messages related to progress state.</p>
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
public class ProgressDialog extends JDialog {

  BorderLayout borderLayout1 = new BorderLayout();
  ProgressPanel progressPanel = null;


  /**
   * Constructor that shows this window inside the MDI Frame.
   * @param title window title; this text will be automatically translate, according to ClientSettings content
   * @param mainTitle main title (showed in BOLD style); this text will be automatically translate, according to ClientSettings content
   * @param mainMessage main message; this text will be automatically translate, according to ClientSettings content
   * @param messageLabels list of label texts to set above the progress bar; this text will be automatically translate, according to ClientSettings content
   * @param minValue minimum value allowed in the progress bar
   * @param maxValue maximum value allowed in the progress bar
   * @param modal flag used to set the window as modal
   * @param boolean showCancelButton flag that indicates if the cancel button must be showed
   */
  public ProgressDialog(String title,String mainTitle,String mainMessage,String[] messageLabels,double minValue,double maxValue,boolean modal,boolean showCancelButton) {
    this(MDIFrame.getInstance(),title,mainTitle,mainMessage,messageLabels,minValue,maxValue,modal,showCancelButton);
  }


  /**
   * Constructor that can be used with any JFrame.
   * @param title window title; this text will be automatically translate, according to ClientSettings content
   * @param mainTitle main title (showed in BOLD style); this text will be automatically translate, according to ClientSettings content
   * @param mainMessage main message; this text will be automatically translate, according to ClientSettings content
   * @param messageLabels list of label texts to set above the progress bar; this text will be automatically translate, according to ClientSettings content
   * @param minValue minimum value allowed in the progress bar
   * @param maxValue maximum value allowed in the progress bar
   * @param modal flag used to set the window as modal
   * @param boolean showCancelButton flag that indicates if the cancel button must be showed
   */
  public ProgressDialog(JFrame parentFrame,String title,String mainTitle,String mainMessage,String[] messageLabels,double minValue,double maxValue,boolean modal,boolean showCancelButton) {
    super(parentFrame,ClientSettings.getInstance().getResources().getResource(title), modal);
    try {
      progressPanel = new ProgressPanel(mainTitle,mainMessage,messageLabels,minValue,maxValue,showCancelButton);
      progressPanel.addCancelButtonListener(new ProgressDialog_buttonCancel_actionAdapter(this));
      jbInit();

      // set window dimension...
      setSize(500,80+(int)progressPanel.getPreferredSize().getHeight());
      ClientUtils.centerDialog(parentFrame,this);
    }
    catch(Exception e) {
      e.printStackTrace();
    }
  }


  private void jbInit() throws Exception {
    this.getContentPane().setLayout(borderLayout1);
    this.getContentPane().add(progressPanel, BorderLayout.CENTER);
  }


  void buttonCancel_actionPerformed(ActionEvent e) {
    setVisible(false);
    this.dispose();
  }


  /**
   * Notify an event that will update the progress bar and the other window content.
   * @param event progress event
   */
  public final void processProgressEvent(ProgressEvent event) {
    progressPanel.processProgressEvent(event);
    if (event.getProgressValue()>=progressPanel.getMaximumValue())
      buttonCancel_actionPerformed(null);
  }


  /**
   * Add an action listener to the cancel button.
   * @param listener listener to add
   */
  public final void addCancelButtonListener(ActionListener listener) {
    progressPanel.addCancelButtonListener(listener);
  }


  /**
   * Remove the specified action listener to the cancel button.
   * @param listener listener to remove
   */
  public final void removeCancelButtonListener(ActionListener listener) {
    progressPanel.removeCancelButtonListener(listener);
  }


  /**
   * Set the minimum value allowed in the progress bar.
   * @param minValue minimum value allowed in the progress bar
   */
  public final void setMinimumValue(double minValue) {
    progressPanel.setMinimumValue(minValue);
  }


  /**
   * Set the maximum value allowed in the progress bar.
   * @param maxValue maximum value allowed in the progress bar
   */
  public final void setMaximumValue(double maxValue) {
    progressPanel.setMaximumValue(maxValue);
  }


  /**
   * @return inimum value allowed in the progress bar
   */
  public final double getMinimumValue() {
    return progressPanel.getMinimumValue();
  }


  /**
   * @return maximum value allowed in the progress bar
   */
  public final double getMaximumValue() {
    return progressPanel.getMaximumValue();
  }


  /**
   * Set the progress bar color.
   * @param color progress bar color
   */
  public final void setProgressBarColor(Color color) {
    progressPanel.setProgressBarColor(color);
  }


  /**
   * @return progress bar color
   */
  public final Color getProgressBarColor() {
    return progressPanel.getProgressBarColor();
  }


  /**
   * Set an image panel a the left of the main title.
   * @param imageName image name that identifies the image to show; the image must be located in "images" sub-folder
   */
  public final void setImageName(String imageName) {
    progressPanel.setImageName(imageName);
  }


  /**
   * @return imageName image name that identifies the image to show; the image must be located in "images" sub-folder
   */
  public final String getImageName() {
    return progressPanel.getImageName();
  }


  /**
   * Add an interval [minValue,maxValue] and a color to use to paint that interval.
   * @param minValue minimum value of this interval
   * @param maxValue maximum value of this interval
   * @param color color to use to paint this band
   * @return <code>true</code> if the specified interval does not match any other interval, <code>false</code> otherwise
   */
  public final boolean addColoredBand(double minValue,double maxValue,Color color) {
    return progressPanel.addColoredBand(minValue,maxValue,color);
  }


  /**
   * @return flag used to determine how color the progress bar: <code>true</code> to paint the bar with many colors, each band with its color, <code>false</code> to paint the bar with ony one color: the color related to the last matched band
   */
  public final boolean isShowAllBands() {
    return progressPanel.isShowAllBands();
  }


  /**
   * Determine how color the progress bar: <code>true</code> to paint the bar with many colors, each band with its color, <code>false</code> to paint the bar with ony one color: the color related to the last matched band.
   * @param showAllBands flag used to determine how color the progress bar: <code>true</code> to paint the bar with many colors, each band with its color, <code>false</code> to paint the bar with ony one color: the color related to the last matched band
   */
  public final void setShowAllBands(boolean showAllBands) {
    progressPanel.setShowAllBands(showAllBands);
  }


}

class ProgressDialog_buttonCancel_actionAdapter implements java.awt.event.ActionListener {
  ProgressDialog adaptee;

  ProgressDialog_buttonCancel_actionAdapter(ProgressDialog adaptee) {
    this.adaptee = adaptee;
  }
  public void actionPerformed(ActionEvent e) {
    adaptee.buttonCancel_actionPerformed(e);
  }
}
