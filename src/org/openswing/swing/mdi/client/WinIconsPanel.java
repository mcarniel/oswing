package org.openswing.swing.mdi.client;

import java.beans.*;
import java.util.*;

import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.*;
import javax.swing.event.*;

import org.openswing.swing.util.client.*;
import java.util.List;
import java.util.Collections;


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

  /** collection of pairs <frame title, SortedSet of associated Integer number> */
  private Hashtable buttonsNr = new Hashtable();

  /* toggle button width */
  private static final int len = 120;

  /** current horizontal position when locating a new toggle button */
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
        try {
          x = x-len;
          frameToClose.closeFrame();
          frameToClose = null;
        }
        catch (PropertyVetoException ex) {
        }
      }
    });
    closeMenu.setVisible(ClientSettings.SHOW_POPUP_MENU_CLOSE);
    if(ClientSettings.ICON_MENU_WINDOW_CLOSE!=null)
      closeMenu.setIcon(new ImageIcon(ClientUtils.getImage(ClientSettings.ICON_MENU_WINDOW_CLOSE)));

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
    iconMenu.setVisible(ClientSettings.SHOW_ICON_POPUP_MENU_REDUCE_ICON);
    if(ClientSettings.ICON_POPUP_MENU_REDUCE_ICON!=null)
      iconMenu.setIcon(new ImageIcon(ClientUtils.getImage(ClientSettings.ICON_POPUP_MENU_REDUCE_ICON)));
  }


  /**
   * Add an internal frame icon to the panel.
   * Add an internal frame listener.
   * @param frame internal frame to add
   */
  public final void add(final InternalFrame frame) {
    try {
      Integer n = null;
      SortedSet list = (SortedSet)buttonsNr.get(frame.getTitle());
      if (list==null) {
        list = new TreeSet();
        n = new Integer(1);
        list.add(n);
        buttonsNr.put(frame.getTitle(),list);
      }
      else {
        n = new Integer( ((Integer)list.last()).intValue()+1 );
        for(int i=1;i<n.intValue();i++)
          if (!list.contains(new Integer(i))) {
            n = new Integer(i);
            break;
          }
        list.add(n);
      }

      final JToggleButton btn = new JToggleButton((n.intValue()>1?" ["+n.intValue()+"] ":"")+frame.getTitle());

      if (ClientSettings.ICON_ENABLE_FRAME!=null)
          btn.setIcon(new ImageIcon(ClientUtils.getImage(ClientSettings.ICON_ENABLE_FRAME)));
      btn.setHorizontalAlignment(SwingConstants.LEFT);

      btn.setToolTipText(frame.getTitle());
//      int len = btn.getFontMetrics(btn.getFont()).stringWidth(btn.getText());
//      btn.setMinimumSize(new Dimension(len+20,24));
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
        if (this.getComponentCount()>0)
          x = x-this.getComponent(0).getWidth();
        if (this.getComponentCount()>0)
          this.remove(0);

        this.revalidate();
        this.repaint();
      }


      this.add(btn,null);
      //x = x+len+20;
      x = x+len;
      buttons.put(btn,frame);
      btn.setSelected(true);
      this.revalidate();
      this.repaint();

      btn.addMouseMotionListener(new MouseMotionAdapter() {
      public void mouseMoved(MouseEvent e) {
          if (e.getX()<25) {
            if (ClientSettings.ICON_CLOSE_FRAME_SELECTED!=null)
              btn.setIcon(new ImageIcon(ClientUtils.getImage(ClientSettings.ICON_CLOSE_FRAME_SELECTED)));
          } else {
            if (ClientSettings.ICON_CLOSE_FRAME!=null)
              btn.setIcon(new ImageIcon(ClientUtils.getImage(ClientSettings.ICON_CLOSE_FRAME)));
          }
        }
      });

      btn.addMouseListener(new MouseAdapter() {
        public void mouseExited(MouseEvent e) {
          if (frame.isSelected()) {
            if (ClientSettings.ICON_ENABLE_FRAME!=null)
              btn.setIcon(new ImageIcon(ClientUtils.getImage(ClientSettings.ICON_ENABLE_FRAME)));
          } else {
            if(!btn.isSelected())
              if(ClientSettings.ICON_DISABLE_FRAME!=null)
                btn.setIcon(new ImageIcon(ClientUtils.getImage(ClientSettings.ICON_DISABLE_FRAME)));
          }
        }

        public void mouseClicked(MouseEvent e) {
          if (SwingUtilities.isRightMouseButton(e)) {
            frameToClose = (InternalFrame)buttons.get(btn);
            if (frameToClose!=null &&
                frameToClose.getDesktopPane()!=null &&
                ((DesktopPane)frameToClose.getDesktopPane()).isModal() &&
                !frameToClose.isModal()) {
              e.consume();
              return;
            }
            iconMenu.setVisible( frameToClose.isIconifiable() );

            menu.show(btn,e.getX(),e.getY());
          }else{
            if(e.getX() < 25){
              frameToClose = (InternalFrame)buttons.get(btn);
              try {
                frameToClose.closeFrame();
              } catch (PropertyVetoException ex) {
            } }
          }
        }

        public void mouseEntered(MouseEvent e) {
           if (SwingUtilities.isLeftMouseButton(e)) {
            btn.setSelected(true);
            InternalFrame f = (InternalFrame)buttons.get(btn);

            if (f!=null &&
                f.getDesktopPane()!=null &&
                ((DesktopPane)f.getDesktopPane()).isModal()) {
              e.consume();
              return;
            }

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
            if (f!=null &&
                f.getDesktopPane()!=null &&
                ((DesktopPane)f.getDesktopPane()).isModal() &&
                !f.isModal()) {
              btn.setSelected(!btn.isSelected());
              return;
            }
            f.toFront();
            try {
              f.setSelected(true);
            }
            catch (Exception ex) {
            }
          }
          else {
            InternalFrame f = (InternalFrame)buttons.get(btn);
            if (f!=null &&
                f.getDesktopPane()!=null &&
                ((DesktopPane)f.getDesktopPane()).isModal() &&
                !f.isModal()) {
              btn.setSelected(!btn.isSelected());
              return;
            }
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
              SortedSet list = (SortedSet)buttonsNr.get(frame.getTitle());
              if (list!=null) {
                if (list.size() == 1) {
                buttonsNr.remove(frame.getTitle());
                }
                else {
                  String aux = btn.getText();
                  if (aux.indexOf("[")!=-1 && aux.indexOf("]")!=-1)
                    aux = aux.substring(aux.indexOf("[")+1,aux.indexOf("]"));
                  else
                    aux = "1";
                  list.remove(new Integer(aux));
                }
              }
            }
            catch (Exception ex) {
            }
            WinIconsPanel.this.remove(btn);
            WinIconsPanel.this.revalidate();
            WinIconsPanel.this.repaint();
            //x = x - btn.getWidth() - 20;
            x = x - btn.getWidth();
          }
          catch (Exception ex1) {
          }
        }

        public void internalFrameActivated(InternalFrameEvent e) {
          btn.setSelected(true);
          if (ClientSettings.ICON_ENABLE_FRAME!=null)
            btn.setIcon(new ImageIcon(ClientUtils.getImage(ClientSettings.ICON_ENABLE_FRAME)));
        }

        public void internalFrameDeactivated(InternalFrameEvent e) {
          btn.setSelected(false);
          if(!btn.isFocusOwner())
           if(ClientSettings.ICON_DISABLE_FRAME!=null)
             btn.setIcon(new ImageIcon(ClientUtils.getImage(ClientSettings.ICON_DISABLE_FRAME)));
        }

      });
    }
    catch (Exception ex) {
      ex.printStackTrace();
    }
  }

}
