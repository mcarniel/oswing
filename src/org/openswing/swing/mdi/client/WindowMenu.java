package org.openswing.swing.mdi.client;

import java.util.Hashtable;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JInternalFrame;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JSeparator;
import org.openswing.swing.util.client.ClientSettings;
import org.openswing.swing.logger.client.*;
import org.openswing.swing.internationalization.java.*;
import javax.swing.UIManager;


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
  private Hashtable internalFrames = new Hashtable();


  static {
    try {
      UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
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
    this.add(new JSeparator());
    this.add(menuWindowCloseAll);
    this.menuWindowCloseAll.setEnabled(false);
  }


  /**
   * Add an internal frame to the window menu.
   * @param frame internal frame to add
   */
  public final void addWindow(final JInternalFrame frame) {
    JMenuItem window = new JMenuItem();
    window.setText(frame.getTitle());
    this.add(window,this.getMenuComponentCount()-2);
    this.menuWindowCloseAll.setEnabled(true);
    internalFrames.put(frame,window);
    window.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        try {
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
    this.menuWindowCloseAll.setEnabled(this.getMenuComponentCount()>2);
    JMenuItem window = (JMenuItem)internalFrames.remove(frame);
    if (window!=null) {
      this.remove(window);
      revalidate();
    }
  }


  public void menuWindowCloseAll_actionPerformed(ActionEvent e) {
    int i=0;
    while(this.getMenuComponentCount()>2) {
      this.remove(0);
    }
    InternalFrame frame = null;
    while(internalFrames.keys().hasMoreElements()) {
      frame = (InternalFrame)internalFrames.keys().nextElement();
      frame.closeFrame();
    }
    this.menuWindowCloseAll.setEnabled(false);
    revalidate();
  }


}
