package org.openswing.swing.util.client;

import java.util.ArrayList;
import java.awt.event.KeyListener;
import java.awt.EventQueue;
import java.awt.Toolkit;
import java.awt.AWTEvent;
import java.awt.event.KeyEvent;

/**
 * <p>Title: OpenSwing Framework</p>
 * <p>Description: System event queue, used to intercept key events for the MDIFrame or Form object.</p>
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
public class ApplicationEventQueue {

  /** list of key listeners */
  private ArrayList keyListeners = new ArrayList();

  /** single istance */
  private static ApplicationEventQueue applicationEventQueue = null;


  /**
   * Add a key listener.
   * @param listener KeyListener
   */
  public final void addKeyListener(KeyListener listener) {
    keyListeners.add(listener);
  }


  /**
   * Add a key listener.
   * @param listener KeyListener
   */
  public final void removeKeyListener(KeyListener listener) {
    keyListeners.remove(listener);
  }


  /**
   * @return single instance of ApplicationEventQueue
   */
  public static final ApplicationEventQueue getInstance() {
    if (applicationEventQueue==null)
      applicationEventQueue = new ApplicationEventQueue();
    return applicationEventQueue;
  }


  private ApplicationEventQueue() {
    Toolkit.getDefaultToolkit().getSystemEventQueue().push(new EventQueue() {

      protected void dispatchEvent(AWTEvent e) {
        super.dispatchEvent(e);
        if (e instanceof KeyEvent &&
            e.getID()==KeyEvent.KEY_PRESSED)
          for(int i=0;i<keyListeners.size();i++)
            ((KeyListener)keyListeners.get(i)).keyPressed((KeyEvent)e);
        else if (e instanceof KeyEvent &&
            e.getID()==KeyEvent.KEY_RELEASED)
          for(int i=0;i<keyListeners.size();i++)
            ((KeyListener)keyListeners.get(i)).keyReleased((KeyEvent)e);
        else if (e instanceof KeyEvent &&
            e.getID()==KeyEvent.KEY_TYPED)
          for(int i=0;i<keyListeners.size();i++)
            ((KeyListener)keyListeners.get(i)).keyTyped((KeyEvent)e);
      }

    });
//    getRootPane().putClientProperty("defeatSystemEventQueueCheck",Boolean.TRUE);

  }

}
