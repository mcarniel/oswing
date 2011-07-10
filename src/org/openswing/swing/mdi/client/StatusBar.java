package org.openswing.swing.mdi.client;

import java.awt.*;
import javax.swing.*;

import org.openswing.swing.util.client.*;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

/**
 * <p>Title: OpenSwing Framework</p>
 * <p>Description: Status Bar: it contains a message panel.
 * It can contains any other JComponent, added form left to right.
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
public class StatusBar extends JPanel {

  JTextField statusText = new JTextField();
  GridBagLayout gridBagLayout1 = new GridBagLayout();
  JProgressBar progressBar = new JProgressBar(0,15);

  /** next position for a new component to add */
  private int pos = 3;

  /** delay in progress bar (in milliseconds) */
  private int delay = ClientSettings.PROGRESS_BAR_DELAY;

  /** flag used to start/stop the progress bar */
  private boolean busy = false;

  /** progress bar color */
  private Color progressBarColor = ClientSettings.PROGRESS_BAR_COLOR;

  /** stop progress bar button */
  private JButton stopProgress = new JButton(new ImageIcon(ClientUtils.getImage("stop.gif")));


  public StatusBar() {
    try {
      jbInit();
      progressBar.setForeground(progressBarColor);
    }
    catch(Exception e) {
      e.printStackTrace();
    }
  }


  public void removeAll() {
    super.removeAll();
    try {
      jbInit();
    }
    catch (Exception ex) {
      ex.printStackTrace();
    }
  }


  /**
   * Add a new component to the status bar, from left to right.
   * @param c component to add
   */
  public final void addStatusComponent(JComponent c) {
    this.add(c,      new GridBagConstraints(pos++, 0, 1, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 0, 0, 2), 0, 0));
    this.revalidate();
    this.repaint();
  }


  /**
   * Set the status panel message.
   * @param text message to view
   */
  public final void setText(String text) {
    statusText.setText(text);
  }


  /**
   * Start/stop the progress bar.
   * @param busy <code>true</code> start the progress bar, <code>false>/code> stop the progress bar
   */
  public final void setBusy(boolean busy) {
    this.busy = busy;
    if (busy) {
      statusText.setText(ClientSettings.getInstance().getResources().getResource("Operation in progress..."));
      new ProgressBarThread().start();
    }
    else
      statusText.setText("");
  }


  private void jbInit() throws Exception {
    stopProgress.setMaximumSize(new Dimension(20,20));
    stopProgress.setPreferredSize(new Dimension(20,20));
    stopProgress.setSize(20,20);
    stopProgress.addActionListener(new ActionListener() {

      public void actionPerformed(ActionEvent e) {
        MDIFrame.getInstance().stopProgressBar();
      }

    });

    statusText.setOpaque(false);
    statusText.setEditable(false);
    statusText.setText(" ");
    this.setLayout(gridBagLayout1);
    this.setBorder(BorderFactory.createLoweredBevelBorder());
    progressBar.setMinimumSize(new Dimension(175, (int)statusText.getPreferredSize().getHeight()));
    progressBar.setPreferredSize(new Dimension(175, (int)statusText.getPreferredSize().getHeight()));
    this.add(progressBar,       new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
    this.add(stopProgress,       new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 0, 0, 2), 0, 0));

    int pos = 2;
    this.add(statusText,       new GridBagConstraints(pos, 0, 1, 1, 1.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 2), 0, 0));
  }


  public void setProgressBarColor(Color progressBarColor) {
    this.progressBarColor = progressBarColor;
    progressBar.setForeground(progressBarColor);
  }


  /**
   * Set progress bar value. Minimum value is 0 and maximum value is 15.
   * @param progressBarValue value to set for the progress bar; if specified value is less than 0 then 0 is setted; if specified value is greater than 15 then it is set to 15.
   */
  public final void setProgressBarValue(int progressBarValue) {
    if (progressBarValue<0)
      progressBarValue = 0;
    if (progressBarValue>progressBar.getMaximum())
      progressBarValue = progressBar.getMaximum();
    progressBar.setValue(progressBarValue);
  }


  /**
   * @return current value in progress bar; minimum value is 0 and maximum value is 15
   */
  public final int getProgressBarValue() {
    return progressBar.getValue();
  }


  /**
   * <p>Description: Inner Thread used to animate the progress wait bar.</p>
   * <p>Copyright: Copyright (C) 2006 Mauro Carniel</p>
   * @author Mauro Carniel
   * @version 1.0
   */
  class ProgressBarThread extends Thread {

    private boolean leftToRight = true;

    public void run() {
      while(busy) {
        if (progressBar.getValue()==progressBar.getMaximum())
          leftToRight = false;
        else if (progressBar.getValue()==progressBar.getMinimum())
          leftToRight = true;

        SwingUtilities.invokeLater(new Runnable() {
          public void run() {
            progressBar.setValue(progressBar.getValue()+(leftToRight?1:-1));
          }
        });
        try {
          sleep(delay);
        }
        catch (InterruptedException ex) {
        }
      }
      SwingUtilities.invokeLater(new Runnable() {
        public void run() {
          progressBar.setValue(progressBar.getMinimum());
        }
      });
    }

  }


}
