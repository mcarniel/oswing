package org.openswing.swing.mdi.client;

import java.awt.Component;
import java.awt.Container;
import java.util.ArrayList;
import javax.swing.JInternalFrame;
import javax.swing.WindowConstants;
import javax.swing.event.InternalFrameAdapter;
import javax.swing.event.InternalFrameEvent;
import javax.swing.ImageIcon;
import org.openswing.swing.client.*;

import org.openswing.swing.util.client.*;


/**
 * <p>Title: OpenSwing Framework</p>
 * <p>Description: Base Internal Frame: to use together with MDI Frame.</p>
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
public class InternalFrame extends JInternalFrame {

  /** children windows (optional); they will be automatically closed when this window is closed */
  private ArrayList frameList = new ArrayList();

  /** parent frame (optional) */
  private InternalFrame  parentFrame;

  /** used when this window will be closed: if this flag is set to <code>true</code> then a warning dialog will be  showed before close the window to ask if it must be close */
  private boolean askBeforeClose = true;


  /**
   * Costructor.
   */
  public InternalFrame() {
    super("",true,true,true,true);
    setFrameIcon(new ImageIcon(ClientUtils.getImage(ClientSettings.ICON_FILENAME)));
    this.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
    this.addInternalFrameListener(new InternalFrameAdapter() {
      public void internalFrameClosing(InternalFrameEvent e) {
        closeFrame();
      }
    });
  }


  /**
   * Create a link between this window and the parent window.
   * @param parent parent window
   */
  public final void setParentFrame(InternalFrame parentFrame) {
    this.parentFrame = parentFrame;
  }


  /**
   * This method is called when this window will be closed.
   * The first method calle is beforeCloseFrame; if this will return <code>false</code> then the closing window operation will be interrupted.
   */
  public final void closeFrame() {
    if (!beforeCloseFrame())
      return;
    if (askBeforeClose) {
      /** @todo */
    }
    try{
      // close all children windows...
      while(frameList.size()>0){
        InternalFrame frame = (InternalFrame)frameList.get(0);
        frame.closeFrame();
      }
      // remove link with the parent window...
      if (parentFrame!=null)
        parentFrame.popFrame(this);
    } catch (Exception e){
      e.printStackTrace();
    }
    try {
      setVisible(false);
      dispose();
      MDIFrame.getWindowMenu().removeWindow(this);
    } catch (Exception ex) {
      ex.printStackTrace();
    }
  }


  /**
   * Remove a child window from this.
   * It's called by closeFrame.
   * @param frame child window to remove
   */
  public final InternalFrame popFrame(InternalFrame frame) {
    if (frame!=null)
      frameList.remove(frame);
    return frame;
  }


  /**
   * Add a child window to this.
   * @param frame child window to add
   */
  public final void pushFrame(InternalFrame frame) {
    frameList.add(frame);
  }


  /**
   * Callback method invoked by closeFrame.
   * @return <code>true</code> allows the closing operation to continue, <code>false</code> the closing operation will be interrupted
   */
  protected boolean beforeCloseFrame() {
    return true;
  }


  /**
   * @return used when this window will be closed: if this flag is set to <code>true</code> then a warning dialog will be  showed before close the window to ask if it must be close
   */
  public final boolean isAskBeforeClose() {
    return askBeforeClose;
  }


  /**
   * Used when this window will be closed: if this flag is set to <code>true</code> then a warning dialog will be  showed before close the window to ask if it must be close
   * @param askBeforeClose <code>true</code> to show a warning dialog before close the window to ask if it must be close
   */
  public final void setAskBeforeClose(boolean askBeforeClose) {
    this.askBeforeClose = askBeforeClose;
  }


}