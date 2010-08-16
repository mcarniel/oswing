package org.openswing.swing.mdi.client;

import java.util.*;

import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.InternalFrameEvent;

import org.openswing.swing.logger.client.*;
import org.openswing.swing.util.client.*;
import java.awt.*;
import java.beans.*;
import javax.swing.event.InternalFrameAdapter;


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
 * @author Mauro Carniel and Vinicius Marandola
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

  /** menu item related to close window */
  private JMenuItem menuWindowClose = new JMenuItem();

  /** count Menu Component start with 1 because the separator*/
  private int countMenuComponent = 1;
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

    //action for menu close window
    menuWindowClose.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        menuWindowClose_actionPerformed(e);
      }
    });
    this.menuWindowClose.setText(ClientSettings.getInstance().getResources().getResource("close window"));
    this.menuWindowClose.setMnemonic(ClientSettings.getInstance().getResources().getResource("closemnemonic").charAt(0));
    if(ClientSettings.ICON_MENU_WINDOW_CLOSE!=null)
      this.menuWindowClose.setIcon(new ImageIcon(ClientUtils.getImage(ClientSettings.ICON_MENU_WINDOW_CLOSE)));
    menuWindowCloseAll.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        menuWindowCloseAll_actionPerformed(e);
      }
    });
    this.menuWindowCloseAll.setText(ClientSettings.getInstance().getResources().getResource("Close All"));
    this.menuWindowCloseAll.setMnemonic(ClientSettings.getInstance().getResources().getResource("closeallmnemonic").charAt(0));
    if(ClientSettings.ICON_MENU_WINDOW_CLOSE_ALL!=null)
      this.menuWindowCloseAll.setIcon(new ImageIcon(ClientUtils.getImage(ClientSettings.ICON_MENU_WINDOW_CLOSE_ALL)));


    menuWindowSwitch.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        menuWindowSwitch_actionPerformed(e);
      }
    });
    this.menuWindowSwitch.setText(ClientSettings.getInstance().getResources().getResource("switch")+"...");
    this.menuWindowSwitch.setMnemonic(ClientSettings.getInstance().getResources().getResource("switchmnemonic").charAt(0));
    if(ClientSettings.ICON_MENU_WINDOW_SWITCH!=null)
      this.menuWindowSwitch.setIcon(new ImageIcon(ClientUtils.getImage(ClientSettings.ICON_MENU_WINDOW_SWITCH)));


    menuWindowTileH.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        menuWindowTileH_actionPerformed(e);
      }
    });
    this.menuWindowTileH.setText(ClientSettings.getInstance().getResources().getResource("tile horizontally"));
    this.menuWindowTileH.setMnemonic(ClientSettings.getInstance().getResources().getResource("tilehorizontallymnemonic").charAt(0));
    if(ClientSettings.ICON_MENU_WINDOW_TILE_H!=null)
      this.menuWindowTileH.setIcon(new ImageIcon(ClientUtils.getImage(ClientSettings.ICON_MENU_WINDOW_TILE_H)));


    menuWindowTileV.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        menuWindowTileV_actionPerformed(e);
      }
    });
    this.menuWindowTileV.setText(ClientSettings.getInstance().getResources().getResource("tile vertically"));
    this.menuWindowTileV.setMnemonic(ClientSettings.getInstance().getResources().getResource("tileverticallymnemonic").charAt(0));
    if(ClientSettings.ICON_MENU_WINDOW_TILE_V!=null)
      this.menuWindowTileV.setIcon(new ImageIcon(ClientUtils.getImage(ClientSettings.ICON_MENU_WINDOW_TILE_V)));


    menuWindowCascade.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        menuWindowCascade_actionPerformed(e);
      }
    });
    this.menuWindowCascade.setText(ClientSettings.getInstance().getResources().getResource("cascade"));
    this.menuWindowCascade.setMnemonic(ClientSettings.getInstance().getResources().getResource("cascademnemonic").charAt(0));
    if(ClientSettings.ICON_MENU_WINDOW_CASCADE!=null)
      this.menuWindowCascade.setIcon(new ImageIcon(ClientUtils.getImage(ClientSettings.ICON_MENU_WINDOW_CASCADE)));


    menuWindowMinimize.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        menuWindowMinimize_actionPerformed(e);
      }
    });
    this.menuWindowMinimize.setText(ClientSettings.getInstance().getResources().getResource("minimize"));
    this.menuWindowMinimize.setMnemonic(ClientSettings.getInstance().getResources().getResource("minimizemnemonic").charAt(0));
    if(ClientSettings.ICON_MENU_WINDOW_MINIMIZE!=null)
      this.menuWindowMinimize.setIcon(new ImageIcon(ClientUtils.getImage(ClientSettings.ICON_MENU_WINDOW_MINIMIZE)));


    menuWindowMinimizeAll.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        menuWindowMinimizeAll_actionPerformed(e);
      }
    });
    this.menuWindowMinimizeAll.setText(ClientSettings.getInstance().getResources().getResource("minimize all"));
    this.menuWindowMinimizeAll.setMnemonic(ClientSettings.getInstance().getResources().getResource("minimizeallmnemonic").charAt(0));
    if(ClientSettings.ICON_MENU_WINDOW_MINIMIZE_ALL!=null)
      this.menuWindowMinimizeAll.setIcon(new ImageIcon(ClientUtils.getImage(ClientSettings.ICON_MENU_WINDOW_MINIMIZE_ALL)));


    this.menuWindowCloseAll.setEnabled(false);
    this.menuWindowClose.setEnabled(false);
    this.menuWindowSwitch.setEnabled(false);
    this.menuWindowTileH.setEnabled(false);
    this.menuWindowTileV.setEnabled(false);
    this.menuWindowCascade.setEnabled(false);
    this.menuWindowMinimize.setEnabled(false);
    this.menuWindowMinimizeAll.setEnabled(false);

    if(ClientSettings.SHOW_MENU_WINDOW_SWITCH){
      this.add(menuWindowSwitch);
      countMenuComponent++;
    }
    this.add(new JSeparator());
    if(ClientSettings.SHOW_MENU_WINDOW_TILE_H) {
      this.add(menuWindowTileH);
      countMenuComponent++;
    }
    if(ClientSettings.SHOW_MENU_WINDOW_TILE_V) {
      this.add(menuWindowTileV);
      countMenuComponent++;
    }
      if(ClientSettings.SHOW_MENU_WINDOW_CASCADE) {
      countMenuComponent++;
      this.add(menuWindowCascade);
    }
    if(ClientSettings.SHOW_MENU_WINDOW_MINIMIZE) {
      this.add(menuWindowMinimize);
      countMenuComponent++;
    }
    if(ClientSettings.SHOW_MENU_WINDOW_MINIMIZE_ALL) {
      this.add(menuWindowMinimizeAll);
      countMenuComponent++;
    }
    if(ClientSettings.SHOW_MENU_WINDOW_CLOSE) {
      this.add(menuWindowClose);
      countMenuComponent++;
    }
    if(ClientSettings.SHOW_MENU_WINDOW_CLOSE_ALL) {
      this.add(menuWindowCloseAll);
      countMenuComponent++;
    }
 }


  /**
   * Update mneonics for items.
   * @return current number to use as mnemonic
   */
  private int updateMnemonics() {
    int i=0;
    JMenuItem item = null;
    String text = null;
    while(i<this.getMenuComponentCount()-countMenuComponent &&
        !(this.getMenuComponent(i) instanceof JSeparator)) {
      item = (JMenuItem)this.getMenuComponent(i);
      text = item.getText();
      text = i+text.substring(text.indexOf(' '));
      item.setText(text);
      item.setMnemonic(text.charAt(0));
      if(ClientSettings.ICON_DISABLE_FRAME!=null)
        item.setIcon(new ImageIcon(ClientUtils.getImage(ClientSettings.ICON_DISABLE_FRAME)));
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
    final JMenuItem window = new JMenuItem();
    if(ClientSettings.ICON_ENABLE_FRAME!=null)
     window.setIcon(new ImageIcon(ClientUtils.getImage(ClientSettings.ICON_ENABLE_FRAME)));
    window.setText(i+" "+frame.getTitle());
    if (i<10) {
      window.setMnemonic(window.getText().charAt(0));
      this.add(window,i);
    }

    this.menuWindowCloseAll.setEnabled(true);
    this.menuWindowSwitch.setEnabled(true);
    this.menuWindowMinimize.setEnabled(true);
    this.menuWindowMinimizeAll.setEnabled(true);
    this.menuWindowClose.setEnabled(true);

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
          updateMnemonics();
          if(ClientSettings.ICON_ENABLE_FRAME!=null)
            window.setIcon(new ImageIcon(ClientUtils.getImage(ClientSettings.ICON_ENABLE_FRAME)));
        }
        catch (Exception ex) {
          Logger.error(this.getClass().getName(),"addWindow","Error while setting the internal frame to front",ex);
        }
      }
    });
    // Viinii
    frame.addInternalFrameListener(new InternalFrameAdapter() {
      public void internalFrameActivated(InternalFrameEvent frame) {
        try {
          updateMnemonics();
          if(ClientSettings.ICON_ENABLE_FRAME!=null)
            window.setIcon(new ImageIcon(ClientUtils.getImage(ClientSettings.ICON_ENABLE_FRAME)));
        } catch(Exception ex) {
          Logger.error(this.getClass().getName(),"addWindow","Error while setting the internal frame to front",ex);
        }}
    });
  }


  /**
   * Remove an internal frame from the window menu.
   * @param frame internal frame to remove
   */
  public final void removeWindow(final JInternalFrame frame) {
    int nextFrame = internalFrames.getList().indexOf(frame);
    JMenuItem window = (JMenuItem)internalFrames.remove(frame);
    if (window!=null) {
      this.remove(window);
      updateMnemonics();
      if(!internalFrames.getList().isEmpty() &&
            this.getMenuComponentCount()>countMenuComponent &&
            ClientSettings.ICON_ENABLE_FRAME!=null ){
        if(nextFrame ==  internalFrames.getList().size())
          nextFrame = 0;
        ((JMenuItem)this.getMenuComponent(nextFrame)).setIcon(new ImageIcon(ClientUtils.getImage(ClientSettings.ICON_ENABLE_FRAME)));
      }
      revalidate();
    }
    this.menuWindowCloseAll.setEnabled(this.getMenuComponentCount()>countMenuComponent);
    this.menuWindowSwitch.setEnabled(this.getMenuComponentCount()>countMenuComponent);
    this.menuWindowTileH.setEnabled(internalFrames.size()>1);
    this.menuWindowTileV.setEnabled(internalFrames.size()>1);
    this.menuWindowCascade.setEnabled(internalFrames.size()>1);
    this.menuWindowMinimize.setEnabled(internalFrames.size()>0);
    this.menuWindowMinimizeAll.setEnabled(internalFrames.size()>0);
    this.menuWindowClose.setEnabled(internalFrames.size()>0);
    if (MDIFrame.getInstance()!=null)
      MDIFrame.getInstance().windowClosed(frame);

    System.gc();
  }


  public void menuWindowCloseAll_actionPerformed(ActionEvent e) {
    int i=0;
    //here
    while(this.getMenuComponentCount()>countMenuComponent) {
      this.remove(0);
    }
    InternalFrame frame = null;
    Enumeration en = internalFrames.keys();
    while(en.hasMoreElements()) {
      frame = (InternalFrame)en.nextElement();
      try {
        frame.closeFrame();
      }
      catch (PropertyVetoException ex) {
      }
    }
    this.menuWindowCloseAll.setEnabled(false);
    // here
    this.menuWindowClose.setEnabled(false);
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

  public void menuWindowClose_actionPerformed(ActionEvent e) {
    try {
      if(MDIFrame.getSelectedFrame()!=null) {
        MDIFrame.getSelectedFrame().closeFrame();
      }
    } catch(Exception ex) {
    }
  }


}
