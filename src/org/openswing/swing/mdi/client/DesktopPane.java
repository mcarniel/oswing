package org.openswing.swing.mdi.client;

import javax.swing.*;
import javax.swing.event.*;
import java.awt.*;
import java.awt.event.*;
import java.beans.*;
import java.util.ArrayList;
import org.openswing.swing.client.*;
import org.openswing.swing.client.*;
import org.openswing.swing.client.*;

import org.openswing.swing.util.client.*;


/**
 * <p>Title: OpenSwing Framework</p>
 * <p>Description: Desktop panel used to contain internal frames.
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
public class DesktopPane extends JDesktopPane {

  /** background image */
  private Image background = null;

  /** this flag set if the background image is centered on the panel; false = the image will be repeated */
  private boolean centered = false;


  public DesktopPane() {
    this.setDragMode(JDesktopPane.OUTLINE_DRAG_MODE);
    setOpaque(true);
    this.setBackground(Color.lightGray);
  }


  public void init() {
    background = ClientUtils.getImage(ClientSettings.BACKGROUND);
  }



  /**
   * Paint background image.
   */
  public final void paintComponent(Graphics g) {
    super.paintComponent(g);

    if (!centered && background!=null) {
      // immagine da ripetere...
      int imgWidth = background.getWidth(this);
      int imgHeight = background.getHeight(this);

      for(int i=this.getWidth();i>0;i=i-imgWidth)
        for(int j=0;j<this.getHeight();j=j+imgHeight)
          g.drawImage(background,i-imgWidth,j,this);

/*
      for(int i=0;i<this.getWidth();i=i+imgWidth)
        for(int j=0;j<this.getHeight();j=j+imgHeight)
          g.drawImage(background,i,j,this);
*/
    } else if (centered && background!=null) {
      // immagine da centrare...
      int imgWidth = background.getWidth(this);
      int imgHeight = background.getHeight(this);
      int x = (this.getWidth()-imgWidth)/2;
      int y = (this.getHeight()-imgHeight)/2;
      g.drawImage(background,x,y,this);
    }
  }


  /**
   * Add an internal frame to the desktop.
   * Add an internal frame listener.
   * @param frame internal frame to add
   * @param maximum flag used to set that the internal frame will be maximized
   */
  public final void add(InternalFrame frame,boolean maximum) {
    try {
      super.add(frame);
      if (maximum) {
        frame.setSize(new Dimension(this.getVisibleRect().width,this.getVisibleRect().height));
        frame.setLocation(0,0);
      }

      if (!frame.isMaximum()) {
        frame.setLocation(
            (Math.max(this.getVisibleRect().width-frame.getWidth(),0))/2,
            (Math.max(this.getVisibleRect().height-frame.getHeight(),0))/2
            );
      }

      if (!frame.isVisible())
        frame.setVisible(true);
      if (!frame.isSelected())
        frame.setSelected(true);

      frame.addInternalFrameListener(new InternalFrameAdapter() {
        public void internalFrameClosed(InternalFrameEvent e) {
          if (DesktopPane.this.getSelectedFrame()==null &&
              DesktopPane.this.getAllFrames().length>0) {
            JInternalFrame frame = DesktopPane.this.getAllFrames()[0];
            try {
              frame.toFront();
              frame.setSelected(true);
            }
            catch (PropertyVetoException ex) {
            }
          }
        }
      });
    }
    catch (Exception ex) {
      ex.printStackTrace();
    }
  }


}