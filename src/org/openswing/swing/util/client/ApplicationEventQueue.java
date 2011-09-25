package org.openswing.swing.util.client;

import java.util.*;

import java.awt.*;
import java.awt.event.*;
import org.openswing.swing.client.InputControl;

/**
 * <p>Title: OpenSwing Framework</p>
 * <p>Description: System event queue, used to intercept key events or mouse events or focus events for the MDIFrame or Form object
 * or any other JComponent.</p>
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

  /** list of mouse listeners */
  private ArrayList mouseListeners = new ArrayList();

  /** event queue */
  private InternalEventQueue eventQueue = new InternalEventQueue();


  /** single istance */
  private static ApplicationEventQueue applicationEventQueue = null;



  /**
   * Add a key listener.
   * @param listener KeyListener to add
   */
  public final void addKeyListener(KeyListener listener) {
    keyListeners.add(listener);
  }


  /**
   * Remove a key listener.
   * @param listener KeyListener to remove
   */
  public final void removeKeyListener(KeyListener listener) {
    keyListeners.remove(listener);
  }


  /**
   * @return KeyListener objects currently registered
   */
  public final KeyListener[] getKeyListeners() {
    return (KeyListener[])keyListeners.toArray(new KeyListener[keyListeners.size()]);
  }


  /**
   * Add a mouse listener.
   * @param listener MouseListener to add
   */
  public final void addMouseListener(MouseListener listener) {
    mouseListeners.add(listener);
  }


  /**
   * Remove a mouse listener.
   * @param listener MouseListener to remove
   */
  public final void removeMouseListener(MouseListener listener) {
    mouseListeners.remove(listener);
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
    Toolkit.getDefaultToolkit().getSystemEventQueue().push(eventQueue);


//    getRootPane().putClientProperty("defeatSystemEventQueueCheck",Boolean.TRUE);
  }


  /**
   * Send an AWT event.
   * @param AWTEvent to send
   */
  public final void dispatchEvent(AWTEvent e) {
    eventQueue.dispatchEvent(e);
  }


  class InternalEventQueue extends EventQueue {

      protected void dispatchEvent(AWTEvent e) {
        try {
          super.dispatchEvent(e);
        }
        catch (Exception ex) {
          if (ClientSettings.SHOW_EVENT_QUEUE_EXCEPTIONS)
            ex.printStackTrace();
        }

        if (e instanceof MouseEvent) {
          if (e.getID()==MouseEvent.MOUSE_PRESSED)
            for(int i=0;i<mouseListeners.size();i++)
              ((MouseListener)mouseListeners.get(i)).mousePressed((MouseEvent)e);
          else if (e.getID()==MouseEvent.MOUSE_RELEASED)
            for(int i=0;i<mouseListeners.size();i++)
              ((MouseListener)mouseListeners.get(i)).mouseReleased((MouseEvent)e);
          else if (e.getID()==MouseEvent.MOUSE_CLICKED)
            for(int i=0;i<mouseListeners.size();i++)
              ((MouseListener)mouseListeners.get(i)).mouseClicked((MouseEvent)e);


        }

        if (e instanceof KeyEvent) {
          if (e.getID()==KeyEvent.KEY_PRESSED)
            for(int i=0;i<keyListeners.size();i++)
              ((KeyListener)keyListeners.get(i)).keyPressed((KeyEvent)e);
          else if (e.getID()==KeyEvent.KEY_RELEASED)
            for(int i=0;i<keyListeners.size();i++)
              ((KeyListener)keyListeners.get(i)).keyReleased((KeyEvent)e);
          else if (e.getID()==KeyEvent.KEY_TYPED)
            for(int i=0;i<keyListeners.size();i++)
              ((KeyListener)keyListeners.get(i)).keyTyped((KeyEvent)e);

        }
      }

  } // end inner class

}
