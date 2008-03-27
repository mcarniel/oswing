package org.openswing.swing.mdi.client;

import java.beans.*;
import java.util.*;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;

import org.openswing.swing.util.client.*;


/**
 * <p>Title: OpenSwing Framework</p>
 * <p>Description: Panel used to show the last opened windows and to switch between them.
 * It can contains a toggle button for each added internal frame.
 * User can click on the button to set to front the related internal frame or
 * can reduce to icon or close internal frame by means of the popup menu opened by clicking with the right mouse button on the toggle button or
 * can set to front the internal frame by entering the toggle button with the left mouse button clicked.
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
public class WinIconsPanel extends JPanel {

  FlowLayout flowLayout1 = new FlowLayout();

  /** collection of button, linked frame */
  private Hashtable buttons = new Hashtable();

  /** collection of frame title, number of frames with that title */
  private Hashtable buttonsNr = new Hashtable();

  private int x = 0;

  /** used to show a popup menu containing a "close frame" menu item */
  private JPopupMenu menu = new JPopupMenu();

  /** menu item inserted into the popup menu */
  private JMenuItem closeMenu = new JMenuItem(ClientSettings.getInstance().getResources().getResource("close window"));

  /** menu item inserted into the popup menu */
  private JMenuItem iconMenu = new JMenuItem(ClientSettings.getInstance().getResources().getResource("reduce to icon"));


  /** internal frame to close */
  private InternalFrame frameToClose = null;


  public WinIconsPanel() {
    try {
      jbInit();
    }
    catch(Exception e) {
      e.printStackTrace();
    }
  }


  public final void init() {
    this.removeAll();
    buttons.clear();
    buttonsNr.clear();
    this.setMinimumSize(new Dimension(2000,26));
    this.setPreferredSize(new Dimension(2000,26));
  }


  private void jbInit() throws Exception {
    this.setBorder(BorderFactory.createLoweredBevelBorder());
    flowLayout1.setAlignment(FlowLayout.LEFT);
    flowLayout1.setHgap(0);
    flowLayout1.setVgap(0);
    this.setLayout(flowLayout1);
    menu.add(closeMenu);
    closeMenu.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        frameToClose.closeFrame();
        frameToClose = null;
      }
    });
    menu.add(iconMenu);
    iconMenu.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        try {
          frameToClose.setIcon(true);
        }
        catch (PropertyVetoException ex) {
          ex.printStackTrace();
        }
        frameToClose = null;
      }
    });

  }


  /**
   * Add an internal frame icon to the panel.
   * Add an internal frame listener.
   * @param frame internal frame to add
   */
  public final void add(final InternalFrame frame) {
    try {
      Integer n = (Integer)buttonsNr.get(frame.getTitle());
      if (n==null) {
        n = new Integer(1);
        buttonsNr.put(frame.getTitle(),n);
      }
      else {
        n = new Integer(n.intValue()+1);
        buttonsNr.put(frame.getTitle(),n);
      }

      final JToggleButton btn = new JToggleButton((n.intValue()>1?" ["+n.intValue()+"] ":"")+frame.getTitle());
//      int len = btn.getFontMetrics(btn.getFont()).stringWidth(btn.getText());
//      btn.setMinimumSize(new Dimension(len+20,24));
      int len = 120;
      btn.setMinimumSize(new Dimension(len,24));
      btn.setMaximumSize(new Dimension(len,24));
      btn.setPreferredSize(new Dimension(len,24));
      btn.setSize(new Dimension(len,24));
//      while (x+len+20>this.getWidth()-200) {
//        x = x-this.getComponent(0).getWidth();
//        this.remove(0);
//
//        this.revalidate();
//        this.repaint();
//      }
      while (x+len+20>this.getWidth()-200) {
        x = x-this.getComponent(0).getWidth();
        this.remove(0);

        this.revalidate();
        this.repaint();
      }


      this.add(btn,null);
      x = x+len+20;
      buttons.put(btn,frame);
      btn.setSelected(true);
      this.revalidate();
      this.repaint();

      btn.addMouseListener(new MouseAdapter() {

        public void mouseClicked(MouseEvent e) {
          if (SwingUtilities.isRightMouseButton(e)) {
            frameToClose = (InternalFrame)buttons.get(btn);
            iconMenu.setVisible( frameToClose.isIconifiable() );

            menu.show(btn,e.getX(),e.getY());
          }
        }

        public void mouseEntered(MouseEvent e) {
          if (SwingUtilities.isLeftMouseButton(e)) {
            btn.setSelected(true);
            InternalFrame f = (InternalFrame)buttons.get(btn);
            f.toFront();
            try {
              f.setSelected(true);
            }
            catch (Exception ex) {
            }
          }
        }

      });

      btn.addActionListener(new ActionListener() {
        public void actionPerformed(ActionEvent e) {
          if (btn.isSelected()) {
            InternalFrame f = (InternalFrame)buttons.get(btn);
            f.toFront();
            try {
              f.setSelected(true);
            }
            catch (Exception ex) {
            }
          }
          else {
            InternalFrame f = (InternalFrame)buttons.get(btn);
            try {
              f.setSelected(false);
            }
            catch (Exception ex) {
            }
            f.toBack();
          }

        }
      });

      frame.addInternalFrameListener(new InternalFrameAdapter() {

        public void internalFrameClosed(InternalFrameEvent e) {
          try {
            buttons.remove(btn);
            try {
              if ( ( (Integer) buttonsNr.get(frame.getTitle())).intValue() == 1) {
                buttonsNr.remove(frame.getTitle());
              }
              else if ( ( (Integer) buttonsNr.get(frame.getTitle())).intValue() > 1) {
                int num = ((Integer) buttonsNr.get(frame.getTitle())).intValue() - 1;
                buttonsNr.put(frame.getTitle(),new Integer(num));
              }
            }
            catch (Exception ex) {
            }
            WinIconsPanel.this.remove(btn);
            WinIconsPanel.this.revalidate();
            WinIconsPanel.this.repaint();
            x = x - btn.getWidth();
          }
          catch (Exception ex1) {
          }
        }

        public void internalFrameActivated(InternalFrameEvent e) {
          btn.setSelected(true);
        }

        public void internalFrameDeactivated(InternalFrameEvent e) {
          btn.setSelected(false);
        }

      });
    }
    catch (Exception ex) {
      ex.printStackTrace();
    }
  }

}
