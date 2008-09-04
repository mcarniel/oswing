package org.openswing.swing.mdi.client;

import java.util.*;

import java.awt.event.*;
import javax.swing.*;

import org.openswing.swing.logger.client.*;
import org.openswing.swing.util.client.*;
import java.awt.*;


/**
 * <p>Title: OpenSwing Framework</p>
 * <p>Description:Windows Menu of the MDI Frame.</p>
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
public class WindowMenu extends JMenu {

  /** menu item related to closing all opened windows */
  private JMenuItem menuWindowCloseAll = new JMenuItem();

  /** collection of pairs: JInternalFrame, menu item */
  private WindowsList internalFrames = new WindowsList();

  /** menu item related to switch between opened windows */
  private JMenuItem menuWindowSwitch = new JMenuItem();

  /** menu item related to tile horizontally */
  private JMenuItem menuWindowTileH = new JMenuItem();

  /** menu item related to tile vertically */
  private JMenuItem menuWindowTileV = new JMenuItem();

  /** menu item related to cascade windows */
  private JMenuItem menuWindowCascade = new JMenuItem();

  /** menu item related to window minimization */
  private JMenuItem menuWindowMinimize = new JMenuItem();

  /** menu item related to all windows minimization */
  private JMenuItem menuWindowMinimizeAll = new JMenuItem();


  static {
    try {
      UIManager.setLookAndFeel(ClientSettings.LOOK_AND_FEEL_CLASS_NAME);
    }
    catch (Exception ex) {
      ex.printStackTrace();
    }
  }


  public WindowMenu() {
  }


  public void init() {
    this.removeAll();
    this.setText(ClientSettings.getInstance().getResources().getResource("Window"));
    this.setMnemonic(ClientSettings.getInstance().getResources().getResource("windowmnemonic").charAt(0));

    menuWindowCloseAll.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        menuWindowCloseAll_actionPerformed(e);
      }
    });
    this.menuWindowCloseAll.setText(ClientSettings.getInstance().getResources().getResource("Close All"));
    this.menuWindowCloseAll.setMnemonic(ClientSettings.getInstance().getResources().getResource("closeallmnemonic").charAt(0));


    menuWindowSwitch.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        menuWindowSwitch_actionPerformed(e);
      }
    });
    this.menuWindowSwitch.setText(ClientSettings.getInstance().getResources().getResource("switch")+"...");
    this.menuWindowSwitch.setMnemonic(ClientSettings.getInstance().getResources().getResource("switchmnemonic").charAt(0));


    menuWindowTileH.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        menuWindowTileH_actionPerformed(e);
      }
    });
    this.menuWindowTileH.setText(ClientSettings.getInstance().getResources().getResource("tile horizontally"));
    this.menuWindowTileH.setMnemonic(ClientSettings.getInstance().getResources().getResource("tilehorizontallymnemonic").charAt(0));


    menuWindowTileV.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        menuWindowTileV_actionPerformed(e);
      }
    });
    this.menuWindowTileV.setText(ClientSettings.getInstance().getResources().getResource("tile vertically"));
    this.menuWindowTileV.setMnemonic(ClientSettings.getInstance().getResources().getResource("tileverticallymnemonic").charAt(0));


    menuWindowCascade.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        menuWindowCascade_actionPerformed(e);
      }
    });
    this.menuWindowCascade.setText(ClientSettings.getInstance().getResources().getResource("cascade"));
    this.menuWindowCascade.setMnemonic(ClientSettings.getInstance().getResources().getResource("cascademnemonic").charAt(0));


    menuWindowMinimize.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        menuWindowMinimize_actionPerformed(e);
      }
    });
    this.menuWindowMinimize.setText(ClientSettings.getInstance().getResources().getResource("minimize"));
    this.menuWindowMinimize.setMnemonic(ClientSettings.getInstance().getResources().getResource("minimizemnemonic").charAt(0));


    menuWindowMinimizeAll.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        menuWindowMinimizeAll_actionPerformed(e);
      }
    });
    this.menuWindowMinimizeAll.setText(ClientSettings.getInstance().getResources().getResource("minimize all"));
    this.menuWindowMinimizeAll.setMnemonic(ClientSettings.getInstance().getResources().getResource("minimizeallmnemonic").charAt(0));


    this.add(menuWindowSwitch);
    this.add(new JSeparator());
    this.add(menuWindowTileH);
    this.add(menuWindowTileV);
    this.add(menuWindowCascade);
    this.add(menuWindowMinimize);
    this.add(menuWindowMinimizeAll);
    this.add(menuWindowCloseAll);
    this.menuWindowCloseAll.setEnabled(false);
    this.menuWindowSwitch.setEnabled(false);
    this.menuWindowTileH.setEnabled(false);
    this.menuWindowTileV.setEnabled(false);
    this.menuWindowCascade.setEnabled(false);
    this.menuWindowMinimize.setEnabled(false);
    this.menuWindowMinimizeAll.setEnabled(false);
  }


  /**
   * Update mneonics for items.
   * @return current number to use as mnemonic
   */
  private int updateMnemonics() {
    int i=0;
    JMenuItem item = null;
    String text = null;
    while(i<this.getMenuComponentCount()-8 &&
          !(this.getMenuComponent(i) instanceof JSeparator)) {
      item = (JMenuItem)this.getMenuComponent(i);
      text = item.getText();
      text = i+text.substring(text.indexOf(' '));
      item.setText(text);
      item.setMnemonic(text.charAt(0));
      i++;
    }
    return i;
  }


  /**
   * Add an internal frame to the window menu.
   * @param frame internal frame to add
   */
  public final void addWindow(final JInternalFrame frame) {
    int i = updateMnemonics();
    JMenuItem window = new JMenuItem();
    window.setText(i+" "+frame.getTitle());
    if (i<10) {
      window.setMnemonic(window.getText().charAt(0));
      this.add(window,i);
    }

    this.menuWindowCloseAll.setEnabled(true);
    this.menuWindowSwitch.setEnabled(true);
    this.menuWindowMinimize.setEnabled(true);
    this.menuWindowMinimizeAll.setEnabled(true);

    internalFrames.put(frame,window);

    this.menuWindowTileH.setEnabled(internalFrames.size()>1);
    this.menuWindowTileV.setEnabled(internalFrames.size()>1);
    this.menuWindowCascade.setEnabled(internalFrames.size()>1);

    window.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        try {
          if (frame.isIcon())
            frame.setIcon(false);
          frame.toFront();
          frame.setSelected(true);
        }
        catch (Exception ex) {
          Logger.error(this.getClass().getName(),"addWindow","Error while setting the internal frame to front",ex);
        }
      }
    });
  }


  /**
   * Remove an internal frame from the window menu.
   * @param frame internal frame to remove
   */
  public final void removeWindow(final JInternalFrame frame) {
    JMenuItem window = (JMenuItem)internalFrames.remove(frame);
    if (window!=null) {
      this.remove(window);
      updateMnemonics();
      revalidate();
    }
    this.menuWindowCloseAll.setEnabled(this.getMenuComponentCount()>8);
    this.menuWindowSwitch.setEnabled(this.getMenuComponentCount()>8);
    this.menuWindowTileH.setEnabled(internalFrames.size()>1);
    this.menuWindowTileV.setEnabled(internalFrames.size()>1);
    this.menuWindowCascade.setEnabled(internalFrames.size()>1);
    this.menuWindowMinimize.setEnabled(internalFrames.size()>0);
    this.menuWindowMinimizeAll.setEnabled(internalFrames.size()>0);
    MDIFrame.getInstance().windowClosed();

    System.gc();
  }


  public void menuWindowCloseAll_actionPerformed(ActionEvent e) {
    int i=0;
    while(this.getMenuComponentCount()>8) {
      this.remove(0);
    }
    InternalFrame frame = null;
    Enumeration en = internalFrames.keys();
    while(en.hasMoreElements()) {
      frame = (InternalFrame)en.nextElement();
      frame.closeFrame();
    }
    this.menuWindowCloseAll.setEnabled(false);
    this.menuWindowSwitch.setEnabled(false);
    this.menuWindowTileH.setEnabled(false);
    this.menuWindowTileV.setEnabled(false);
    this.menuWindowCascade.setEnabled(false);
    this.menuWindowMinimize.setEnabled(false);
    this.menuWindowMinimizeAll.setEnabled(false);
    revalidate();
  }


  public void menuWindowSwitch_actionPerformed(ActionEvent e) {
    new SwitchDialog(internalFrames);
  }


  public void menuWindowTileH_actionPerformed(ActionEvent e) {
    InternalFrame frame = null;
    Enumeration en = internalFrames.keys();
    int nWindows = MDIFrame.getInstance().getDesktopDimension().height/200;
    int max = Math.min(nWindows,internalFrames.size());
    int w = MDIFrame.getInstance().getDesktopDimension().width;
    int h = 0;

    for(int i=0;i<max;i++) {
      frame = (InternalFrame)en.nextElement();
      frame.setSize(new Dimension(w,MDIFrame.getInstance().getDesktopDimension().height/max));
      frame.setLocation(0,h);
      h += MDIFrame.getInstance().getDesktopDimension().height/max;
      frame.toFront();
    }
  }


  public void menuWindowTileV_actionPerformed(ActionEvent e) {
    InternalFrame frame = null;
    Enumeration en = internalFrames.keys();
    int nWindows = MDIFrame.getInstance().getDesktopDimension().width/200;
    int max = Math.min(nWindows,internalFrames.size());
    int w = 0;
    int h = MDIFrame.getInstance().getDesktopDimension().height;

    for(int i=0;i<max;i++) {
      frame = (InternalFrame)en.nextElement();
      frame.setSize(new Dimension(MDIFrame.getInstance().getDesktopDimension().width/max,h));
      frame.setLocation(w,0);
      w += MDIFrame.getInstance().getDesktopDimension().width/max;
      frame.toFront();
    }
  }


  public void menuWindowCascade_actionPerformed(ActionEvent e) {
    InternalFrame frame = null;
    Enumeration en = internalFrames.keys();
    int nWindows = MDIFrame.getInstance().getDesktopDimension().width/20;
    int max = Math.min(nWindows,internalFrames.size());
    int x = 0;
    int w = MDIFrame.getInstance().getDesktopDimension().width-20*max;
    int h = MDIFrame.getInstance().getDesktopDimension().height-20*max;

    for(int i=0;i<max;i++) {
      frame = (InternalFrame)en.nextElement();
      frame.setSize(new Dimension(w,h));
      frame.setLocation(x,x);
      x += 20;
      frame.toFront();
    }
  }


  public void menuWindowMinimize_actionPerformed(ActionEvent e) {
    try {
      if (MDIFrame.getSelectedFrame() != null &&
          !MDIFrame.getSelectedFrame().isIcon()) {
        MDIFrame.getSelectedFrame().setIcon(true);
      }
    }
    catch (Exception ex) {
    }
  }


  public void menuWindowMinimizeAll_actionPerformed(ActionEvent e) {
    try {
      InternalFrame frame = null;
      Enumeration en = internalFrames.keys();
      while(en.hasMoreElements()) {
        frame = (InternalFrame)en.nextElement();
        frame.setIcon(true);
      }
    }
    catch (Exception ex) {
    }
  }



}
