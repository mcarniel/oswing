package org.openswing.swing.miscellaneous.client;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

import org.openswing.swing.client.*;

/**
 * <p>Title: OpenSwing Framework</p>
 * <p>Description: this class inherits from IconifableWidìndow, so it allows to show an iconifable window,
 * i.e. a window that can contains any kind of graphics component within it,
 * since it inherits from JPanel. As default behavior, it is composed of a top panel and a main panel:
 * - top panel may contain: an optional title icon, a title (showed in bold style) and two buttons: a "reduce to icon" button and a "close" button, both can be hidden
 * - main panel is this, i.e. any content manually added to this is showed inside this main panel.
 * Window can be reduced to icon by pressing the "reduce to icon" button or by double clicking inside the top panel (if no "reduce to icon" button is visible).
 * Window can be closed by pressing the "close" button or by clicking inside the main panel (if no "close" button is visible).
 * As default settings, this panel has a dimension of 300 x 150 pixels.
 * Window location can be defined in several ways:
 * - using absolute location, by using this.setLocation method
 * - by anchoring the window to the TOP/BOTTOM/INSIDE_BOTTOM/INSIDE_TOP of another component, through setAnchorWindow() method.</p>
 * Several events fired by this window can be listened, through the method: addIconifableWindowListener.
 * </p>
 * <p>As default behavior, the main panel contains two optional graphics components:
 * - an icon, anchored to the left margin of the window
 * - a multi-line text
 * Alert window will be automatically showed when invoking "showWindow" method.
 * It can be showed also after a while, or hide after a while; it supports fade in/out too.
 * </p>
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
public class AlertWindow extends IconifableWindow {

  /** wait time before automatically closing window; 0 means do not close window; default value: 0 */
  private long timeout = 0;

  /** fade out time in milliseconds; 0 means no fade out effect; default value: 1000 */
  private long fadeOutTime = 1000;

  /** fade in time in milliseconds; minimum value: 50; 0 means no fade in effect; minimum value: 50; default value: 1000 */
  private long fadeInTime = 1000;

  /** multi-line text to show inside this panel */
  private MultiLineLabelControl body = new MultiLineLabelControl();

  /** image name to show at the left of the multi-line text (optional) */
  private String imageName;

  /** define if the window must be closed (false) or only reduce to icon (true), when the timeout has been rised; default value: <code>false</code> */
  private boolean reduceToIconOnTimeout = false;

  /** panel that contains the default graphics components (image and multi-line text) */
  private JPanel defaultComponentsPanel = new JPanel();

  /** icon to show at the left of the multi-line text (optional) */
  private ImagePanel imagePanel = new ImagePanel();


  public AlertWindow() {
    setAllowsCloseWindow(true);
    body.addMouseListener(new MouseAdapter() {

      public void mouseClicked(MouseEvent e) {
        if (SwingUtilities.isLeftMouseButton(e) && !isShowCloseButton() && isAllowsCloseWindow()) {
          hideWindow();
        }
      }

    });

    // prepare default panel content...
    defaultComponentsPanel.setLayout(new GridBagLayout());
    imagePanel.setOpaque(false);
    imagePanel.setBorder(BorderFactory.createEmptyBorder());
    imagePanel.setScrollBarsPolicy(imagePanel.SCROLLBAR_NEVER);
    defaultComponentsPanel.setOpaque(false);
    defaultComponentsPanel.add(body,      new GridBagConstraints(1, 1, 1, 1, 1.0, 1.0
       ,GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0,0,0,0), 0, 0));
  }


  /**
   * @return fade in time in milliseconds; 0 means no fade in effect
   */
  public final long getFadeInTime() {
    return fadeInTime;
  }


  /**
   * Set the fade in time in milliseconds; minimum value: 50; 0 means no fade in effect.
   * @param fadeInTime fade in time in milliseconds; 0 means no fade in effect
   */
  public final void setFadeInTime(long fadeInTime) {
    this.fadeInTime = fadeInTime;
  }


  /**
   * @return wait time before automatically closing window; 0 means do not close window1
   */
  public final long getTimeout() {
    return timeout;
  }


  /**
   * Set the wait time before automatically closing window; 0 means do not close window.
   * @param timeout wait time before automatically closing window; 0 means do not close window
   */
  public final void setTimeout(long timeout) {
    this.timeout = timeout;
  }


  /**
   * @return fade out time in milliseconds; 0 means no fade out effect
   */
  public final long getFadeOutTime() {
    return fadeOutTime;
  }


  /**
   * Set the fade out time in milliseconds; minimum value: 50; 0 means no fade out effect.
   * @param fadeOutTime fade out time in milliseconds; 0 means no fade out effect
   */
  public final void setFadeOutTime(long fadeOutTime) {
    this.fadeOutTime = fadeOutTime;
  }


  /**
   * Set the multi-line text to show inside this panel.
   * @param body multi-line text to show inside this panel; it will be translated, according to language settings
   */
  public final void setMainText(String body) {
    this.body.setLabel(body);
  }


  /**
   * @return body multi-line text to show inside this panel
   */
  public final String getMainText() {
    return this.body.getLabel();
  }


  /**
   * @return image name (optional)
   */
  public final String getImageName() {
    return imageName;
  }


  /**
   * Set the image name (optional).
   * @param imageName image name
   */
  public final void setImageName(String imageName) {
    this.imageName = imageName;
  }


  /**
   * @return define if the window must be closed (false) or only reduce to icon (true), when the timeout has been rised
   */
  public final boolean isReduceToIconOnTimeout() {
    return reduceToIconOnTimeout;
  }


  /**
   * Define if the window must be closed (false) or only reduce to icon (true), when the timeout has been rised.
   * @param reduceToIconOnTimeout define if the window must be closed (false) or only reduce to icon (true), when the timeout has been rised
   */
  public final void setReduceToIconOnTimeout(boolean reduceToIconOnTimeout) {
    this.reduceToIconOnTimeout = reduceToIconOnTimeout;
  }


  /**
   * This method shows the window.
   */
  protected final void showWindowInternally() {
    if (this.getComponents().length==0) {
      // show default graphics objects...
      imagePanel.setBackground(this.getBackground());
      if (imageName!=null && !imageName.equals("")) {
        defaultComponentsPanel.remove(imagePanel);
        imagePanel.setImageName(imageName);
        imagePanel.setMinimumSize(imagePanel.getPreferredSize());
        defaultComponentsPanel.add(imagePanel,     new GridBagConstraints(0, 0, 1, 2, 0.0, 0.0
             ,GridBagConstraints.NORTHWEST, GridBagConstraints.NONE, new Insets(2,2,2,2), 0, 0));
      }

      body.setPreferredSize(new Dimension(
        getWindow().getWidth()-(imageName!=null && !imageName.equals("")?imagePanel.getPreferredSize().width+8:0),
        getWindow().getHeight()
      ));
      body.setSize(body.getPreferredSize());
      body.setMaximumSize(new Dimension(
        getWindow().getWidth()-(imageName!=null && !imageName.equals("")?imagePanel.getPreferredSize().width+8:0),
        getWindow().getHeight()
      ));
      getWindow().getContentPane().remove(this);
      getWindow().getContentPane().add(defaultComponentsPanel,BorderLayout.CENTER);
    }

    if (fadeInTime>50) {
      final double delta = getWindow().getHeight()/(fadeInTime/50);
      final double h = getWindow().getHeight();
      final int finalYLocation = getWindow().getLocation().y;
      getWindow().setSize(getWindow().getWidth(),0);
      getWindow().setLocation(getWindow().getLocation().x,getWindow().getLocation().y);
      final int startingYLocation = getWindow().getLocation().y+(int)h;
//      getWindow().setVisible(true);
      new Thread() {
        public void run() {
          for(double i=0;i<h;i=i+delta) {
            getWindow().setVisible(false);
            if (constraint==TOP)
              getWindow().setLocation(getWindow().getLocation().x,startingYLocation-(int)i);
            getWindow().setSize(getWindow().getWidth(),(int)i);
            getWindow().setVisible(true);
            try {
              sleep(fadeInTime/50);
            }
            catch (InterruptedException ex1) {
            }
          }

          getWindow().setSize(getWindow().getWidth(),(int)h);
          if (constraint==TOP)
            getWindow().setLocation(getWindow().getLocation().x,finalYLocation);
          getWindow().setVisible(true);
          fireWindowEvent(IconifableWindowListener.WINDOW_SHOWED);
        }
      }.start();
    }
    else
      super.showWindowInternally();

    if (timeout>0)
      new Thread() {
        public void run() {
          try {
            sleep(timeout);
          }
          catch (InterruptedException ex) {
          }
          int h = getWindow().getHeight();
          for(int i=0;i<h-(reduceToIconOnTimeout?getIconHeight():0);i++) {
            getWindow().setSize(getWindow().getWidth(),h-i);
            if (constraint==TOP)
              getWindow().setLocation(getWindow().getLocation().x,getWindow().getLocation().y+1);
            try {
              sleep(Math.max(fadeOutTime/h-5,5));
            }
            catch (InterruptedException ex1) {
            }
          }
          if (reduceToIconOnTimeout)
            setupTopPanel(true);
          else
            hideWindow();
        }
      }.start();
  }




}
