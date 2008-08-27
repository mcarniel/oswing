package org.openswing.swing.miscellaneous.client;


import java.util.*;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

import org.openswing.swing.client.*;
import org.openswing.swing.util.client.*;


/**
 * <p>Title: OpenSwing Framework</p>
 * <p>Description: Panel that contains a progress bar and messages related to progress state.</p>
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
public class ProgressPanel extends JPanel {

  BorderLayout borderLayout1 = new BorderLayout();
  JPanel titlePanel = new JPanel();
  JPanel progressPanel = new JPanel();
  GridBagLayout gridBagLayout1 = new GridBagLayout();
  ImagePanel imagePanel = null;
  JLabel labelMainTitle = new JLabel();
  JScrollPane scrollPane = new JScrollPane();
  JTextArea controlMainMessage = new JTextArea();
  GridBagLayout gridBagLayout2 = new GridBagLayout();
  JLabel labelProgress = new JLabel();
  ProgressBar progressBar = new ProgressBar();
  JButton buttonCancel = new JButton();

  /** list of JLabels */
  private ArrayList messages = new ArrayList();

  /** number of labeld added above the progress bar */
  private int currentRow = 0;


  /**
   * Constructor that can be used with any JFrame.
   * @param mainTitle main title (showed in BOLD style); this text will be automatically translate, according to ClientSettings content
   * @param mainMessage main message; this text will be automatically translate, according to ClientSettings content
   * @param messageLabels list of label texts to set above the progress bar; this text will be automatically translate, according to ClientSettings content
   * @param minValue minimum value allowed in the progress bar
   * @param maxValue maximum value allowed in the progress bar
   * @param boolean showCancelButton flag that indicates if the cancel button must be showed
   */
  public ProgressPanel(String mainTitle,String mainMessage,String[] messageLabels,double minValue,double maxValue,boolean showCancelButton) {
    progressBar.setMinValue(minValue);
    progressBar.setMaxValue(maxValue);
    try {
      jbInit();

      // set text content...
      labelMainTitle.setText(ClientSettings.getInstance().getResources().getResource(mainTitle));
      controlMainMessage.setFont(new JLabel().getFont());
      controlMainMessage.setBackground(progressPanel.getBackground());

      for(int i=0;i<messageLabels.length;i++) {
        JLabel label1a = new JLabel(ClientSettings.getInstance().getResources().getResource(messageLabels[i]));
        JLabel label1b = new JLabel();
        messages.add(label1b);

        progressPanel.add(label1a,   new GridBagConstraints(0, currentRow, 1, 1, 0.0, 0.0
                ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
        progressPanel.add(label1b,    new GridBagConstraints(1, currentRow, 1, 1, 0.0, 0.0
                ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
        currentRow++;
      }

      // set main message height...
      StringTokenizer st = new StringTokenizer(mainMessage,"\n");
      controlMainMessage.setRows(st.countTokens()+1);
      controlMainMessage.setText(ClientSettings.getInstance().getResources().getResource(mainMessage));

      // set progress bar bounds...
      setMinimumValue(minValue);
      setMaximumValue(maxValue);

      // show/hide the cancel button...
      if (showCancelButton)
        progressPanel.add(buttonCancel,    new GridBagConstraints(1, currentRow+3, 1, 1, 0.0, 1.0
                ,GridBagConstraints.NORTHEAST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
      else
        progressPanel.add(new JLabel(""),    new GridBagConstraints(1, currentRow+3, 1, 1, 0.0, 1.0
                ,GridBagConstraints.NORTHEAST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));

      progressBar.setForeground(ClientSettings.GRID_SELECTION_BACKGROUND);

    }
    catch(Exception e) {
      e.printStackTrace();
    }
  }


  private void jbInit() throws Exception {
    this.setLayout(borderLayout1);
    progressPanel.setBorder(BorderFactory.createEtchedBorder());
    progressPanel.setLayout(gridBagLayout2);
    titlePanel.setLayout(gridBagLayout1);
    scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
    scrollPane.setBorder(BorderFactory.createEmptyBorder());
//    controlMainMessage.setBackground(SystemColor.scrollbar);
    controlMainMessage.setDoubleBuffered(false);
    controlMainMessage.setEditable(false);
    controlMainMessage.setText("");
    labelMainTitle.setFont(new java.awt.Font(labelMainTitle.getFont().getName(), Font.BOLD, labelMainTitle.getFont().getSize()+1));
    labelProgress.setText(ClientSettings.getInstance().getResources().getResource("progress")+":");
    buttonCancel.setText(ClientSettings.getInstance().getResources().getResource("cancel"));
    this.add(titlePanel, BorderLayout.NORTH);
    this.add(progressPanel,  BorderLayout.CENTER);
    titlePanel.add(labelMainTitle,  new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
    titlePanel.add(scrollPane,    new GridBagConstraints(1, 1, 1, 1, 1.0, 1.0
            ,GridBagConstraints.WEST, GridBagConstraints.BOTH, new Insets(5, 25, 5, 5), 0, 0));
    scrollPane.getViewport().add(controlMainMessage, null);
    progressPanel.add(labelProgress,    new GridBagConstraints(0, 2, 1, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
    progressPanel.add(progressBar,    new GridBagConstraints(1, currentRow+2, 1, 1, 1.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 0, 0));
  }


  /**
   * Notify an event that will update the progress bar and the other window content.
   * @param event progress event
   */
  public final void processProgressEvent(ProgressEvent event) {
    labelMainTitle.setText(ClientSettings.getInstance().getResources().getResource(event.getMainTitle()));
    controlMainMessage.setText(ClientSettings.getInstance().getResources().getResource(event.getMainMessage()));

    for(int i=0;i<event.getText().length;i++)
      ((JLabel)messages.get(i)).setText(ClientSettings.getInstance().getResources().getResource(event.getText()[i]));

    progressBar.setValue((int)event.getProgressValue());
  }


  /**
   * Add an action listener to the cancel button.
   * @param listener listener to add
   */
  public final void addCancelButtonListener(ActionListener listener) {
    buttonCancel.addActionListener(listener);
  }


  /**
   * Remove the specified action listener to the cancel button.
   * @param listener listener to remove
   */
  public final void removeCancelButtonListener(ActionListener listener) {
    buttonCancel.removeActionListener(listener);
  }


  /**
   * Set the minimum value allowed in the progress bar.
   * @param minValue minimum value allowed in the progress bar
   */
  public final void setMinimumValue(double minValue) {
    progressBar.setMinValue(minValue);
  }


  /**
   * Set the maximum value allowed in the progress bar.
   * @param maxValue maximum value allowed in the progress bar
   */
  public final void setMaximumValue(double maxValue) {
    progressBar.setMaxValue(maxValue);
  }


  /**
   * @return inimum value allowed in the progress bar
   */
  public final double getMinimumValue() {
    return progressBar.getMinValue();
  }


  /**
   * @return maximum value allowed in the progress bar
   */
  public final double getMaximumValue() {
    return progressBar.getMaxValue();
  }


  /**
   * Set the progress bar color.
   * @param color progress bar color
   */
  public final void setProgressBarColor(Color color) {
    progressBar.setBackground(color);
  }


  /**
   * @return progress bar color
   */
  public final Color getProgressBarColor() {
    return progressBar.getBackground();
  }


  /**
   * Set an image panel a the left of the main title.
   * @param imageName image name that identifies the image to show; the image must be located in "images" sub-folder
   */
  public final void setImageName(String imageName) {
    if (imagePanel==null) {
      imagePanel = new ImagePanel();
      imagePanel.setScrollBarsPolicy(ImagePanel.SCROLLBAR_NEVER);
      titlePanel.add(imagePanel,  new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0
              ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
    }
    imagePanel.setImageName(imageName);
  }


  /**
   * @return imageName image name that identifies the image to show; the image must be located in "images" sub-folder
   */
  public final String getImageName() {
    return imagePanel==null?null:imagePanel.getImageName();
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
   * @return flag used to determine how color the progress bar: <code>true</code> to paint the bar with many colors, each band with its color, <code>false</code> to paint the bar with ony one color: the color related to the last matched band
   */
  public final boolean isShowAllBands() {
    return progressBar.isShowAllBands();
  }


  /**
   * Determine how color the progress bar: <code>true</code> to paint the bar with many colors, each band with its color, <code>false</code> to paint the bar with ony one color: the color related to the last matched band.
   * @param showAllBands flag used to determine how color the progress bar: <code>true</code> to paint the bar with many colors, each band with its color, <code>false</code> to paint the bar with ony one color: the color related to the last matched band
   */
  public final void setShowAllBands(boolean showAllBands) {
    progressBar.setShowAllBands(showAllBands);
  }


}

