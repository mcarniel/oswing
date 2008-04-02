package org.openswing.swing.mdi.client;

import java.util.*;

import javax.swing.*;
import javax.swing.border.*;
import javax.swing.event.*;

import org.openswing.swing.util.client.*;
import org.openswing.swing.client.OptionPane;
import java.awt.Component;
import org.openswing.swing.client.GridControl;
import org.openswing.swing.form.client.Form;
import org.openswing.swing.util.java.Consts;
import java.awt.Container;
import org.openswing.swing.tree.client.TreePanel;
import java.awt.event.FocusEvent;
import java.awt.DefaultKeyboardFocusManager;


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

  /** used when this window will be closed: if this flag is set to <code>true</code> then a warning dialog will be  showed before close the window to ask if it must be close; default value: ClientSettings.ASK_BEFORE_CLOSE */
  private boolean askBeforeClose = ClientSettings.ASK_BEFORE_CLOSE;

  /** last border of internal frame, before hiding title bar */
  private Border lastBorder = null;

  /** flag used to hide/show title bar; default value: <code>false</code> i.e. title bar is visible */
  private boolean hideTitleBar = false;

  /** last component added to the north of the internal frame, before hiding title bar */
  private JComponent lastNorthPane = null;


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
   * @return <code>true</code> if there exists a GridControl or a Form panel within this that is in insert/edit mode, <code>false</code> otherwise
   */
  private boolean checkComponents(Component[] c) {
    for(int i=0;i<c.length;i++) {
      if (c[i] instanceof GridControl) {
        c[i].transferFocus();
        if (((GridControl)c[i]).getMode()==Consts.INSERT)
          return true;
        if (((GridControl)c[i]).getMode()==Consts.EDIT &&
          ((GridControl)c[i]).getVOListTableModel().getChangedRowNumbers().length>0) {
          return true;
        }

      }
      else if (c[i] instanceof Form) {
        try {
          Component obj = DefaultKeyboardFocusManager.getCurrentKeyboardFocusManager().getFocusOwner();
          if (obj!=null)
            obj.transferFocus();
        }
        catch (Exception ex) {
          ((Form) c[i]).transferFocus();
        }
        boolean changed = ((Form)c[i]).isChanged();
        if (((Form)c[i]).getMode()==Consts.INSERT && changed)
          return true;
        if (((Form)c[i]).getMode()==Consts.EDIT && changed)
          return true;
      }
      else if (c[i] instanceof TreePanel) {
        if (((TreePanel)c[i]).isEnabled())
          return true;
      }
      else if (c[i] instanceof JScrollPane) {
        if (checkComponents( ((JScrollPane)c[i]).getViewport().getComponents() ))
          return true;
      }
      else if (c[i] instanceof Container) {
        if (checkComponents( ((Container)c[i]).getComponents() ))
          return true;
      }
    }
    return false;
  }


  /**
   * This method is called when this window will be closed.
   * The first method calle is beforeCloseFrame; if this will return <code>false</code> then the closing window operation will be interrupted.
   */
  public final void closeFrame() {
    if (!beforeCloseFrame())
      return;
    if (askBeforeClose) {
      // check if this contains a GridControl or a Form panel in insert/edit mode...
      if (checkComponents(this.getComponents())) {
        int res = OptionPane.showInternalConfirmDialog(
          this,
          "are you sure to close this window?",
          "confirm window closing",
          JOptionPane.YES_NO_OPTION
        );
        if (res==JOptionPane.NO_OPTION)
          return;
      }
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


  /**
   * Define if title bar must be hidden or showed
   * @param hideTitleBar flag used to hide/show title bar; <code>true</code> to hide title bar; <code>false</code> to show it
   */
  public final void setHideTitleBar(boolean hideTitleBar) {
    this.hideTitleBar = hideTitleBar;
    if (hideTitleBar) {
      super.setIconifiable(false);
      super.setMaximizable(false);
      lastNorthPane = ((javax.swing.plaf.basic.BasicInternalFrameUI)this.getUI()).getNorthPane();
      ((javax.swing.plaf.basic.BasicInternalFrameUI)this.getUI()).setNorthPane(null);
      lastBorder = super.getBorder();
      super.setBorder(BorderFactory.createEmptyBorder());
    }
    else if (lastNorthPane!=null && lastBorder!=null) {
      ((javax.swing.plaf.basic.BasicInternalFrameUI)this.getUI()).setNorthPane(lastNorthPane);
      super.setBorder(lastBorder);
    }
  }




}
