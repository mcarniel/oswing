package org.openswing.swing.client;

import java.awt.*;
import javax.swing.*;

import org.openswing.swing.mdi.client.*;
import org.openswing.swing.util.client.*;


/**
 * <p>Title: OpenSwing Framework</p>
 * <p>Description: SplashScreen class.</p>
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
public class SplashScreen extends JWindow {

  /** image to show */
  private Image image;

  /** splash screen text (may be null) */
  private String text;

  /** delay time, expressed in seconds */
  private int delayTime;

  /** parent frame */
  private JFrame parentFrame;


  /**
   * Constructor.
   * @param imageName image file name (the image file must be located in "images" subfolder)
   * @param text (optional) text to show under the image; may be null
   * @param delayTime delay time, expressed in seconds
   */
  public SplashScreen(JFrame parentFrame,String imageName, String text,int delayTime) {
    super(parentFrame==null?MDIFrame.getInstance():parentFrame);
    this.parentFrame = parentFrame==null?MDIFrame.getInstance():parentFrame;
    this.image = ClientUtils.getImage(imageName);
    this.text = text;
    this.delayTime = delayTime;
    pack();
    ClientUtils.centerWindow(parentFrame,this);
    setVisible(true);
  }


  /**
   * Show the splash screen and text under it.
   */
  public final void paint(Graphics g) {
    super.paint(g);
    Dimension size = getSize();
    g.setColor(Color.black);
    g.drawRect(0, 0, size.width-1, size.height-1);
    g.drawImage(image, 1, 1, image.getWidth(this), image.getHeight(this), this);

    if(text != null) {
      FontMetrics fm = g.getFontMetrics();
      int y = image.getHeight(this) + 2 + fm.getAscent();
      int x = (size.width-fm.stringWidth(text)) / 2;
      g.setColor(getForeground());
      g.drawString(text, x, y);
    }
  }


  /**
   * Method automatically called by the JVM to fetch window dimensions.
   * @return Dimension
   */
  public Dimension getPreferredSize() {
    if (image!=null) {
      Dimension dim = new Dimension(image.getWidth(this)+2,image.getHeight(this)+2);
      if(text!=null) {
        FontMetrics fm = this.getGraphics().getFontMetrics();
        dim.height += fm.getHeight()+2;
      }
      return dim;
    }
    else
      return new Dimension(0,0);
  }


  /**
   * Method automatically called by the constructor to show the splash screen.
   * The method return immediately but the window will remain visibile for "delayTime" seconds.
   */
  public final void setVisible(boolean visible) {
    super.setVisible(visible);
    if(visible && delayTime > 0) {
      new Thread() {
        public void run() {
          try {
            Thread.sleep(delayTime*1000L);
          }
          catch(InterruptedException ex) {}
          dispose();
        }
      }.start();
    }
  }


}
