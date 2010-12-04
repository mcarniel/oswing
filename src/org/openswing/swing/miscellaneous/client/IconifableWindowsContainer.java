package org.openswing.swing.miscellaneous.client;

import java.util.*;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

import org.openswing.swing.util.client.*;


/**
 * <p>Title: OpenSwing Framework</p>
 * <p>Description: JPanel used to contain IconifableWindow objects.
 * It is possible to force the automatic expansion of any IconifableWindow.</p>
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
public class IconifableWindowsContainer extends JPanel {

  /** flag used to auto expand an IconifableWindow */
  private boolean autoExpandWindow;

  /** list of IconifableWindow objects added to this container */
  private ArrayList windows = new ArrayList();

  private boolean firstTime = true;


  public IconifableWindowsContainer() {
  }


  public final void addNotify() {
    super.addNotify();
    if (firstTime) {
      firstTime = false;
      SwingUtilities.invokeLater(new Runnable() {
        // thread used to wait until this container has been showed...
        public void run() {
          try {
            while (!ClientUtils.getParentWindow(IconifableWindowsContainer.this).isVisible()) {
              Thread.sleep(100);
            }
          }
          catch (Exception ex) {
          }

          IconifableWindowsContainer.this.addComponentListener(new ComponentAdapter() {

            /**
             * Invoked when the component's size changes.
             */
            public void componentResized(ComponentEvent e) {
              IconifableWindow window = null;
              for (int i = 0; i < windows.size(); i++) {
                window = (IconifableWindow) windows.get(i);
                if (window.getWindow().getSize().height == window.getIconHeight()) {
                  window.getWindow().setSize(new Dimension(
                      IconifableWindowsContainer.this.getWidth(),
                      window.getIconHeight()
                  ));
                  window.setWindowMaximumSize(new Dimension(IconifableWindowsContainer.this.getWidth(),getHeight()-windows.size()*window.getIconHeight()+window.getIconHeight()));
                }
                else {
                  // update size for all window's content pane containers...
                  Container c = window.getWindow().getContentPane();
                  while(c!=null && !(c.equals(window.getWindow()))) {
                    c.setSize(IconifableWindowsContainer.this.getWidth(),getHeight()-windows.size()*window.getIconHeight()+window.getIconHeight());
                    c = c.getParent();
                  }

                  // update JWindow size...
                  window.getWindow().setSize(new Dimension(IconifableWindowsContainer.this.getWidth(),getHeight()-windows.size()*window.getIconHeight()+window.getIconHeight()));

                  // update size of content pane children...
                  for(int j=0;j<window.getWindow().getContentPane().getComponentCount();j++) {
                    window.getWindow().getContentPane().getComponents()[j].setSize(
                      new Dimension(
                        IconifableWindowsContainer.this.getWidth(),
                        window.getWindow().getContentPane().getComponents()[j].getHeight()
                      )
                    );
                    ((JComponent)window.getWindow().getContentPane().getComponents()[j]).revalidate();
                  }
                  window.setWindowMaximumSize(new Dimension(IconifableWindowsContainer.this.getWidth(),getHeight()-windows.size()*window.getIconHeight()+window.getIconHeight()));
                }
              }
            }
          });


          JComponent anchorComponent = IconifableWindowsContainer.this;
          IconifableWindow window = null;
          for(int i=windows.size()-1;i>=0;i--) {
            window = (IconifableWindow)windows.get(i);
            if (i==windows.size()-1)
              window.anchorWindow(anchorComponent,IconifableWindow.INSIDE_BOTTOM);
            else
              window.anchorWindow(anchorComponent,IconifableWindow.TOP);

            window.setWindowMaximumSize(new Dimension(getWidth(),getHeight()-windows.size()*window.getIconHeight()+window.getIconHeight()));
            window.showWindow();
            window.reduceToIcon();
            if (i==0 && autoExpandWindow) {
              window.getWindow().setSize(new Dimension(getWidth(),getHeight()-windows.size()*window.getIconHeight()+window.getIconHeight()));
              window.getWindow().setLocation(getLocationOnScreen().x,getLocationOnScreen().y);
            }
            anchorComponent = window;

            window.addIconifableWindowListener(new IconifableWindowListener(){

              public void windowEvent(IconifableWindow source,int event) {
                if (event==IconifableWindowListener.WINDOW_RESTORED) {
                  IconifableWindow w = null;
                  for(int k=0;k<windows.size();k++) {
                    w = (IconifableWindow)windows.get(k);
                    if (!w.equals(source))
                      w.reduceToIcon();
                  }
                  if (source.getConstraint()==source.TOP)
                    source.setLocation(source.getLocation().x,source.getLocation().y);

                }
                else if (event==IconifableWindowListener.WINDOW_REDUCED_TO_ICON && autoExpandWindow) {
                  IconifableWindow w = null;
                  for(int k=0;k<windows.size();k++) {
                    w = (IconifableWindow)windows.get(k);
                    if (w.getWindow().getHeight()!=w.getIconHeight())
                      return;
                  }
                  source.restoreWindow();
                }
              }

            });

          }
        }
      });
    }
  }



  /**
   * @return define if auto expand an IconifableWindow
   */
  public final boolean isAutoExpandWindow() {
    return autoExpandWindow;
  }


  /**
   * Define if auto expand an IconifableWindow.
   * @param autoExpandWindow auto expand an IconifableWindow
   */
  public final void setAutoExpandWindow(boolean autoExpandWindow) {
    this.autoExpandWindow = autoExpandWindow;
  }


  /**
   * Add an IconifableWindow to this container.
   * @param window IconifableWindow object to add
   */
  public final void addIconifableWindow(IconifableWindow window) {
    windows.add(window);
  }


  /**
   * Remove an IconifableWindow to this container.
   * @param window IconifableWindow object to remove
   */
  public final void removeIconifableWindow(IconifableWindow window) {
    windows.remove(window);
  }


  public final Component add(Component comp, int index) {
    throw new RuntimeException();
  }



  public final void add(Component comp, Object constraints, int index) {
    throw new RuntimeException();
  }


  public final void add(Component comp, Object constraints) {
    throw new RuntimeException();
  }


  public final Component add(Component comp) {
    throw new RuntimeException();
  }


  public final Component add(String name, Component comp) {
    throw new RuntimeException();
  }

}
