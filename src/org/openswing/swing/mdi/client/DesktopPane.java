package org.openswing.swing.mdi.client;

import java.beans.*;

import java.awt.*;
import javax.swing.*;
import javax.swing.event.*;

import org.openswing.swing.util.client.*;
import org.openswing.swing.util.java.Consts;


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


  public DesktopPane() {
    this.setDragMode(JDesktopPane.OUTLINE_DRAG_MODE);
    setOpaque(true);
    this.setBackground(Color.lightGray);
  }


  public void init() {
    if (ClientSettings.BACKGROUND!=null)
      background = ClientUtils.getImage(ClientSettings.BACKGROUND);
  }



  /**
   * Paint background image.
   */
  public final void paintComponent(Graphics g) {
    super.paintComponent(g);

    if (background!=null) {
      if (ClientSettings.BACK_IMAGE_DISPOSITION==Consts.BACK_IMAGE_REPEATED) {
        // image must be repeated...
        int imgWidth = background.getWidth(this);
        int imgHeight = background.getHeight(this);

        for(int i=this.getWidth();i>0;i=i-imgWidth)
          for(int j=0;j<this.getHeight();j=j+imgHeight)
            g.drawImage(background,i-imgWidth,j,this);
      } else if (ClientSettings.BACK_IMAGE_DISPOSITION==Consts.BACK_IMAGE_CENTERED) {
        // image must be centered...
        int imgWidth = background.getWidth(this);
        int imgHeight = background.getHeight(this);
        int x = (this.getWidth()-imgWidth)/2;
        int y = (this.getHeight()-imgHeight)/2;
        g.drawImage(background,x,y,this);
      } else if (ClientSettings.BACK_IMAGE_DISPOSITION==Consts.BACK_IMAGE_STRETCHED) {
        // image must be streched...
        g.drawImage(background,0,0,getWidth(),getHeight(),this);
      }
    }

    if (ClientSettings.BACKGROUND_PAINTER!=null) {
      ClientSettings.BACKGROUND_PAINTER.paint(this, g);
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
