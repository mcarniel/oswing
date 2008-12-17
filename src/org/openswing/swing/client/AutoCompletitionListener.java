package org.openswing.swing.client;

import java.util.*;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;
import org.openswing.swing.table.editors.client.*;


/**
 * <p>Title: OpenSwing Framework</p>
 * <p>Description: KeyListener that can be added to a text/number type input control to listen for a key pressed event:
 * after listening a key press event, the AutoCompletitionDataLocator class is invoked to retrieve a list of data
 * that starts with the text/number just typed; this list of data is showed in a window and user can select one of
 * these data to auto complete the content of the input field.
 * </p>
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
public final class AutoCompletitionListener extends KeyAdapter {

  /** data locator used to retrieve a list of data that starts with the data just typed */
  private AutoCompletitionDataLocator dataLocator = null;

  /** input control to listen for key press events */
  private AutoCompletitionInputControl inputControl = null;

  /** window that contains the list of data */
  private JWindow window = new JWindow();

  /** list of data */
  private JList list = new JList();

  /** list container */
  private JScrollPane scrollPane = new JScrollPane(list);

  /** time when occours last key pressed event */
  private long time = 0;

  /** wait time before showing data list (expressed in ms) */
  private long waitTime;

  /** wait timer */
  private WaitTimer timer = null;


  /**
   * Constructor invoked from some input control of type text/number.
   * @param inputControl input control to listen for key press events
   * @param dataLocator data locator used to retrieve a list of data that starts with the data just typed
   * @param waitTime wait time before showing data list (expressed in ms)
   */
  public AutoCompletitionListener(AutoCompletitionInputControl inputControl,AutoCompletitionDataLocator dataLocator,long waitTime) {
    this.inputControl = inputControl;
    this.dataLocator = dataLocator;
    this.waitTime = waitTime;
    scrollPane.getViewport().setOpaque(false);
    scrollPane.setOpaque(false);
    list.setOpaque(false);
    list.setBackground(new Color(250,250,200));
    window.getContentPane().setBackground(new Color(250,250,200));
    scrollPane.getVerticalScrollBar().setFocusable(true);
    for(int i=0;i<scrollPane.getVerticalScrollBar().getComponents().length;i++)
        if (scrollPane.getVerticalScrollBar().getComponents()[i] instanceof JButton) {
          scrollPane.getVerticalScrollBar().getComponents()[i].addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent e) {
      //        e.consume();
              window.setVisible(true);
            }
            public void mouseClicked(MouseEvent e) {
              e.consume();
            }
            public void mouseReleased(MouseEvent e) {
              e.consume();
            }
          });

        }
    scrollPane.getVerticalScrollBar().addMouseListener(new MouseAdapter() {
      public void mousePressed(MouseEvent e) {
//        e.consume();
        window.setVisible(true);
      }
      public void mouseClicked(MouseEvent e) {
        e.consume();
      }
      public void mouseReleased(MouseEvent e) {
        e.consume();
      }
    });
    window.setBackground(new Color(250,250,200));
    window.getContentPane().setLayout(new BorderLayout());
    window.getContentPane().add(scrollPane,BorderLayout.CENTER);

    list.addMouseListener(new MouseAdapter() {
      public void mouseClicked(MouseEvent e) {
          list.setSelectedIndex(list.locationToIndex(e.getPoint()));
          e.consume();
          AutoCompletitionListener.this.inputControl.setValue(list.getSelectedValue());
          window.setVisible(false);
          if (AutoCompletitionListener.this.inputControl instanceof CodLookupControl)
            ((CodLookupControl)AutoCompletitionListener.this.inputControl).getCodBox().forceValidate();
          if (AutoCompletitionListener.this.inputControl instanceof CodLookupCellEditor)
            ((CodLookupCellEditor)AutoCompletitionListener.this.inputControl).forceValidate();
          try {
            if (timer != null)
              timer.interrupt();
          }
          catch (Exception ex) {
          }
          time = 0;
        }
    });

    inputControl.addAncestorListener(new AncestorListener() {

      public void ancestorAdded(AncestorEvent event) {
      }

      public void ancestorMoved(AncestorEvent event) {
      }

      public void ancestorRemoved(AncestorEvent event) {
        try {
          if (timer != null)
            timer.interrupt();
        }
        catch (Exception ex) {
        }
        window.setVisible(false);
        window.dispose();
      }

    });

    inputControl.addFocusListener(new FocusAdapter() {

      /**
       * Invoked when a component loses the keyboard focus.
       */
      public void focusLost(final FocusEvent e) {
//        if (e.getOppositeComponent()!=null &&
//            e.getOppositeComponent().equals(window)) {
//          // user has clicked onto the scrollpane...
//          SwingUtilities.invokeLater(new Runnable() {
//            public void run() {
//              window.toFront();
//            }
//          });
//          return;
//        }
        window.setVisible(false);
        try {
          if (timer != null)
            timer.interrupt();
        }
        catch (Exception ex) {
        }
      }

    });

  }


  /**
   * @return <code>true</code> if window for autocompletition codes is currently visible, <code>false</code> otherwise
   */
  public final boolean isWindowVisible() {
    return window.isVisible();
  }


  public final void keyPressed(KeyEvent e) {
    if (e.getKeyCode()==e.VK_ENTER && window.isVisible() && list.getSelectedIndex()!=-1) {
      e.consume();
      inputControl.setValue(list.getSelectedValue());
      window.setVisible(false);
      if (inputControl instanceof CodLookupControl)
        ((CodLookupControl)inputControl).getCodBox().forceValidate();
      if (inputControl instanceof CodLookupCellEditor)
        ((CodLookupCellEditor)inputControl).forceValidate();
      try {
        if (timer != null)
          timer.interrupt();
      }
      catch (Exception ex) {
      }
      time = 0;
    }
    else if (e.getKeyCode()==e.VK_UP && window.isVisible()) {
      if (list.getSelectedIndex()==-1)
        list.setSelectedIndex(0);
      else if (list.getSelectedIndex()==0)
        list.setSelectedIndex(list.getModel().getSize()-1);
      else
        list.setSelectedIndex(list.getSelectedIndex()-1);
      list.ensureIndexIsVisible(list.getSelectedIndex());
    }
    else if (e.getKeyCode()==e.VK_DOWN && window.isVisible()) {
      if (list.getSelectedIndex()==-1)
        list.setSelectedIndex(0);
      else if (list.getSelectedIndex()<list.getModel().getSize()-1)
        list.setSelectedIndex(list.getSelectedIndex()+1);
      else
        list.setSelectedIndex(0);
      list.ensureIndexIsVisible(list.getSelectedIndex());
    }
    else if (e.getKeyCode()==e.VK_ESCAPE || e.getKeyCode()==e.VK_TAB) {
      window.setVisible(false);
      try {
        if (timer != null)
          timer.interrupt();
      }
      catch (Exception ex) {
      }
      time = 0;
    }
  }


  public final void keyReleased(KeyEvent e) {
    if (e.getKeyCode()==e.VK_ENTER || e.getKeyCode()==e.VK_UP || e.getKeyCode()==e.VK_DOWN || e.getKeyCode()==e.VK_ESCAPE)
      return;
    if (time==0) {
      time = System.currentTimeMillis();
      timer = new WaitTimer();
      timer.start();
    }
    else if (System.currentTimeMillis()-time<waitTime) {
      try {
        if (timer != null)
          timer.interrupt();
      }
      catch (Exception ex) {
      }
      timer = new WaitTimer();
      timer.start();
      time = System.currentTimeMillis();
      if (inputControl.getValue()==null || "".equals(inputControl.getValue().toString())) {
        window.setVisible(false);
        try {
          if (timer != null)
            timer.interrupt();
        }
        catch (Exception ex) {
        }
        time = 0;
      }
      else if (window.isVisible())
        checkInputControlValue();
    }
    else {
//      checkInputControlValue();
      time = System.currentTimeMillis();
    }
  }


  private void checkInputControlValue() {
//    else if (e.getKeyCode()==e.VK_ENTER) {
//      e.consume();
//      window.setVisible(false);
//      time = 0;
//    }
    if (inputControl.getValue()==null || "".equals(inputControl.getValue().toString())) {
      window.setVisible(false);
      try {
        if (timer != null)
          timer.interrupt();
      }
      catch (Exception ex) {
      }
      time = 0;
    }
    else if (!window.isVisible()) {
      window.setLocation(
        inputControl.getLocationOnScreen().x,
        inputControl.getLocationOnScreen().y+inputControl.getHeight()+1
      );
      if (reload()) {
        window.setSize(inputControl.getWidth(),200);
        window.setVisible(true);
      }
    }
    else {
      if (!reload()) {
        window.setVisible(false);
        try {
          if (timer != null)
            timer.interrupt();
        }
        catch (Exception ex) {
        }
        time = 0;
      }
    }
  }


  private boolean reload() {
    ArrayList data = dataLocator.getListOfData(inputControl.getValue());
    DefaultListModel model = new DefaultListModel();
    for(int i=0;i<data.size();i++)
      model.addElement(data.get(i));
    list.setModel(model);
    list.revalidate();
    list.repaint();
    if (data.size()>0)
      list.setSelectedIndex(0);
    return data.size()>0;
  }


  /**
   * <p>Title: OpenSwing Framework</p>
   * <p>Description: Timer</p>
   * <p>Copyright: Copyright (C) 2008 Mauro Carniel</p>
   * @version 1.0
   */
  class WaitTimer extends Thread {

    public void run() {
      try {
        sleep(waitTime);
        SwingUtilities.invokeLater(new Runnable() {
          public void run() {
            checkInputControlValue();
          }
        });
      }
      catch (InterruptedException ex) {
      }
    }

  }


}
