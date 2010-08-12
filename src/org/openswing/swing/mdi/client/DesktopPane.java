package org.openswing.swing.mdi.client;

import java.beans.*;
import java.io.*;
import java.util.*;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;
import javax.swing.filechooser.FileFilter;

import org.openswing.swing.util.client.*;
import org.openswing.swing.util.java.*;


/**
 * <p>Title: OpenSwing Framework</p>
 * <p>Description: Desktop panel used to contain internal frames.
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
public class DesktopPane extends JDesktopPane implements InternalFrameListener {

  /** background image */
  private Image background = null;

  /** file related to custom background image */
  private String backgroundFileName = null;

  /** flag used to manage modal internal frames */
  private boolean modal;

  /** modal panel */
  protected JPanel modalPane = new JPanel();

  /** layering component */
  protected Component layeringComp;


  public DesktopPane() {
    this.setDragMode(JDesktopPane.OUTLINE_DRAG_MODE);
    setOpaque(true);
    this.setBackground(Color.lightGray);
    addMouseListener(new MouseAdapter() {
      public void mouseClicked(MouseEvent e) {
        if (e.getClickCount()==1 && SwingUtilities.isRightMouseButton(e)) {
          JPopupMenu popup = new JPopupMenu();
          JMenuItem selBack = new JMenuItem(ClientSettings.getInstance().getResources().getResource("change background"));
          popup.add(selBack);
          selBack.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
              selectBackground();
            }
          });
          JMenuItem clearBack = new JMenuItem(ClientSettings.getInstance().getResources().getResource("reset background"));
          popup.add(clearBack);
          clearBack.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
              resetBackground();
            }
          });
          popup.show(DesktopPane.this,e.getY(),e.getY());
        }
      }
    });

    initModalPane();
  }


  /**
   * Initialize modal pane, in case of modal internal frames.
   */
  private void initModalPane() {
    modalPane.setSize(java.awt.Toolkit.getDefaultToolkit().getScreenSize());
    modalPane.setOpaque(false);
    modalPane.setVisible(true);

    // add mouse listener to modal internal frame,
    // in order to generate a beep when clicking on it...
    modalPane.addMouseListener(new MouseListener () {

       public void mouseClicked(MouseEvent e) {
       }
       public void mouseEntered(MouseEvent e) {
       }
       public void mouseExited(MouseEvent e) {
       }
       public void mousePressed(MouseEvent e) {
         Toolkit.getDefaultToolkit().beep();
       }
       public void mouseReleased(MouseEvent e) {
       }

    });
  }


  /**
   *
   * @param modal boolean
   */
  public final void setModal(boolean modal) {
    if (this.modal!=modal)
      setModal(getSelectedFrame(), modal);
  }


  /**
   * Set modal state to the specified internal frame.
   * @param modalDialog internal frame whose modal state must be changed
   * @param modal modal state
   */
  public final void setModal(javax.swing.JInternalFrame modalDialog, boolean modal) {
     this.modal=modal;

     if (this.modal) {
       this.add(modalPane);
       this.setLayer(modalPane, JLayeredPane.PALETTE_LAYER.intValue());
       javax.swing.JInternalFrame[] frames=this.getAllFrames();
       int defaultLayer=JLayeredPane.DEFAULT_LAYER.intValue();
       for (int i=0;i<frames.length;i++) {
           if (frames[i].getLayer()!=defaultLayer) {
               this.setLayer(frames[i], defaultLayer);
           }
       }
       this.setLayer(modalDialog, JLayeredPane.MODAL_LAYER.intValue());
       modalDialog.moveToFront();
     } else {
       this.remove(modalPane);
       javax.swing.JInternalFrame[] frames=this.getAllFrames();
       int defaultLayer=JLayeredPane.DEFAULT_LAYER.intValue();
       for (int i=0;i<frames.length;i++) {
           if (frames[i].getLayer()!=defaultLayer) {
               this.setLayer(frames[i], defaultLayer);
           }
       }
     }
   }


   /**
    * @return <code>true</code> if there exists a modal internal frame or <code>false</code> otherwise
    */
   public final boolean isModal() {
       return this.modal;
   }


  /**
   * Select an image to use as MDI background.
   */
  private void selectBackground() {
    JFileChooser f = new JFileChooser();
    f.setDialogTitle(ClientSettings.getInstance().getResources().getResource("image selection"));
    f.setFileSelectionMode(f.FILES_ONLY);
    f.setFileFilter(new FileFilter() {

     public boolean accept(File f) {
       return f.isDirectory() ||
              f.getName().toLowerCase().endsWith(".gif") ||
              f.getName().toLowerCase().endsWith(".jpg");
     }

     /**
      * The description of this filter. For example: "JPG and GIF Images"
      * @see FileView#getName
      */
     public String getDescription() {
       return "Image Files (*.gif;*.jpg)";
     }

    });
    int res = f.showOpenDialog(MDIFrame.getInstance());
    if (res==f.APPROVE_OPTION) {
      // image file selected: it will be set into image panel...
      backgroundFileName = f.getSelectedFile().getAbsolutePath();
      setBackgroundImageFromFile();

      // save config file...
      saveUserProfile();
    }
  }


  /**
   * Reset the image to use as MDI background and restore the default one.
   */
  private void resetBackground() {
    backgroundFileName = null;
    background = ClientUtils.getImage(ClientSettings.BACKGROUND);
    repaint();

    // save config file...
    saveUserProfile();
  }


  /**
   * Set custom background image from file.
   */
  private boolean setBackgroundImageFromFile() {
    BufferedInputStream in = null;
    try {
      File file = new File(backgroundFileName);
      in = new BufferedInputStream(new FileInputStream(file));
      byte[] bytes = new byte[(int)file.length()];
      in.read(bytes);
      background = new ImageIcon(bytes).getImage();
      repaint();
      return true;
    }
    catch (Exception ex) {
      ex.printStackTrace();
      return false;
    }
    finally {
      try {
        if (in!=null)
          in.close();
      }
      catch (Exception ex1) {
      }
    }
  }


  /**
   * Save user profile.
   */
  private void saveUserProfile() {
    try {
      Properties p = new Properties();
      if (backgroundFileName==null)
        backgroundFileName = "";
      p.setProperty("backgroundFileName",backgroundFileName);
      String file = System.getProperty("user.home").replace('\\','/');
      if (!file.endsWith("/"))
        file += "/";
      file += "profiles/"+MDIFrame.getInstance().getTitle()+".cfg";
      p.store(new FileOutputStream(new File(file)),"User Properties");
    }
    catch (Exception ex) {
      ex.printStackTrace();
    }
  }


  /**
   * @return <code>true</code> if user profile was found and a background image has been setted, <code>false</code> otherwise
   */
  private boolean loadUserProfile(MDIController client) {
    String file = System.getProperty("user.home").replace('\\','/');
    if (!file.endsWith("/"))
      file += "/";
    file += "profiles/"+client.getMDIFrameTitle()+".cfg";
    File confFile = new File(file);
    if (!confFile.exists())
      return false;
    Properties p = new Properties();
    try {
      p.load(new FileInputStream(confFile));
      backgroundFileName = p.getProperty("backgroundFileName");
      if (backgroundFileName!=null && !backgroundFileName.equals(""))
        return setBackgroundImageFromFile();
      return false;
    }
    catch (Exception ex) {
      ex.printStackTrace();
      return false;
    }
  }


  public void init(MDIController client) {
    if (!loadUserProfile(client)) {
      if (ClientSettings.BACKGROUND!=null)
        background = ClientUtils.getImage(ClientSettings.BACKGROUND);
    }
  }



  /**
   * Paint background image.
   */
  public final void paintComponent(Graphics g) {
    super.paintComponent(g);

    if (background!=null) {
      if (ClientSettings.BACK_IMAGE_DISPOSITION==Consts.BACK_IMAGE_REPEATED) {
        // image must be repeated...
        int imgWidth = background.getWidth(this);
        int imgHeight = background.getHeight(this);

        for(int i=this.getWidth();i>0;i=i-imgWidth)
          for(int j=0;j<this.getHeight();j=j+imgHeight)
            g.drawImage(background,i-imgWidth,j,this);
      } else if (ClientSettings.BACK_IMAGE_DISPOSITION==Consts.BACK_IMAGE_CENTERED) {
        // image must be centered...
        int imgWidth = background.getWidth(this);
        int imgHeight = background.getHeight(this);
        int x = (this.getWidth()-imgWidth)/2;
        int y = (this.getHeight()-imgHeight)/2;
        g.drawImage(background,x,y,this);
      } else if (ClientSettings.BACK_IMAGE_DISPOSITION==Consts.BACK_IMAGE_STRETCHED) {
        // image must be streched...
        g.drawImage(background,0,0,getWidth(),getHeight(),this);
      }
    }

    if (ClientSettings.BACKGROUND_PAINTER!=null) {
      ClientSettings.BACKGROUND_PAINTER.paint(this, g);
    }

  }


  /**
   * Add an internal frame to the desktop.
   * Add an internal frame listener.
   * @param frame internal frame to add
   * @param maximum flag used to set that the internal frame will be maximized
   */
  public final void add(final InternalFrame frame,boolean maximum) {
    try {
      super.add(frame);

      int w = this.getVisibleRect().width;
      int h = this.getVisibleRect().height;

//      System.out.println(this.getVisibleRect()+" "+this.getSize()+" "+this.getPreferredSize()+" "+this.getMinimumSize());

      if (w==0)
        w = this.getSize().width;
      if (h==0)
        h = this.getSize().width;

      if (maximum) {
        frame.setSize(new Dimension(w,h));
        frame.setLocation(0,0);
      }


      if (!frame.isMaximum()) {
        frame.setLocation(
            (Math.max(w-frame.getWidth(),0))/2,
            (Math.max(h-frame.getHeight(),0))/2
            );
      }

      if (ClientSettings.STORE_INTERNAL_FRAME_PROFILE)
        loadState(frame);

      if (!frame.isVisible())
        frame.setVisible(true);
      if (!frame.isSelected())
        frame.setSelected(true);

      frame.addInternalFrameListener(new InternalFrameAdapter() {
        public void internalFrameClosed(InternalFrameEvent e) {

          if (ClientSettings.STORE_INTERNAL_FRAME_PROFILE)
            saveState(frame);

          if (DesktopPane.this.getSelectedFrame()==null &&
              DesktopPane.this.getAllFrames().length>0) {
            JInternalFrame frame = DesktopPane.this.getAllFrames()[0];
            try {
              frame.toFront();
              frame.setSelected(true);
            }
            catch (PropertyVetoException ex) {
            }
          }
        }
      });


      if (ClientSettings.SHOW_SCROLLBARS_IN_MDI) {
        Container c = this;
        while(c!=null && !(c instanceof JScrollPane))
          c = c.getParent();
        if (c!=null) {
          final JScrollPane scrollPane = (JScrollPane) c;
          frame.addComponentListener(new ComponentAdapter() {

            /**
             * Invoked when the component's position changes.
             */
            public void componentMoved(final ComponentEvent e) {
              if (e.getComponent().getLocation().x+e.getComponent().getSize().width>DesktopPane.this.getWidth() ||
                  e.getComponent().getLocation().y+e.getComponent().getSize().height>DesktopPane.this.getHeight()) {
                final int locx = e.getComponent().getLocation().x;
                final int locy = e.getComponent().getLocation().y;
                int x = Math.max(e.getComponent().getLocation().x+e.getComponent().getSize().width,DesktopPane.this.getWidth());
                int y = Math.max(e.getComponent().getLocation().y+e.getComponent().getSize().height,DesktopPane.this.getHeight());

                DesktopPane.this.setPreferredSize(new Dimension(x,y));
                DesktopPane.this.setSize(new Dimension(x,y));
                scrollPane.getViewport().setViewSize(new Dimension(x,y));
                scrollPane.getViewport().setSize(new Dimension(x,y));
                SwingUtilities.invokeLater(new Runnable() {
                  public void run() {
  //                    scrollPane.getHorizontalScrollBar().setValue(e.getComponent().getLocation().x);
  //                    scrollPane.getVerticalScrollBar().setValue(e.getComponent().getLocation().y);
                    scrollPane.getHorizontalScrollBar().setValue(locx);
                    scrollPane.getVerticalScrollBar().setValue(locy);

                  }
                });
              }
            }

         });
        }
      }
    }
    catch (Exception ex) {
      ex.printStackTrace();
    }
  }




  /**
   * Sets the layer attribute for the specified component and also sets its position within that layer.
   * @param c         the Component to set the layer for
   * @param layer     an int specifying the layer to set, where
   *                  lower numbers are closer to the bottom
   * @param position  an int specifying the position within the
   *                  layer, where 0 is the topmost position and -1
   *                  is the bottommost position
   */
  public final void setLayer(Component c, int layer, int position) {
      if (layeringComp!=c) {
          layeringComp=c;
          super.setLayer(c, layer, position);
          layeringComp=null;
      }
  }


  /**
   * Method overrided in order to inject modal state to internal frames.
   * @param comp componente to add
   * @param constraints constraints to apply to the specified component
   * @param index component position
   */
  protected final void addImpl(Component comp, Object constraints, int index) {
      DesktopManager manager=null;
      if (comp instanceof javax.swing.JInternalFrame) {
          javax.swing.JInternalFrame frame=(javax.swing.JInternalFrame)comp;
          EventListener[] listeners=frame.getListeners(InternalFrameListener.class);
          int i=0;
          for (i=0;i<listeners.length;i++) {
              if (listeners[i]==this) {
                  break;
              }
          }
          if (i>=listeners.length) {
              frame.addInternalFrameListener(this);
          }
          if (comp.getParent()==this) {
              return;
          }
          if (layeringComp!=comp) {
              manager=getDesktopManager();
          }
      }
      super.addImpl(comp, constraints, index);
      if (manager!=null) {
          manager.openFrame((javax.swing.JInternalFrame)comp);
      }
  }


  public final void internalFrameActivated(InternalFrameEvent e) {
      EventListener[] listeners=listenerList.getListeners(InternalFrameListener.class);
      for (int i=0;i<listeners.length;i++) {
          InternalFrameListener listener=(InternalFrameListener)listeners[i];
          listener.internalFrameActivated(e);
      }
  }


  public final void internalFrameClosed(InternalFrameEvent e) {
      if (this.modal) {
          // removed internal frame modal state...
          setModal(false);
      }
      EventListener[] listeners=listenerList.getListeners(InternalFrameListener.class);
      for (int i=0;i<listeners.length;i++) {
          InternalFrameListener listener=(InternalFrameListener)listeners[i];
          listener.internalFrameClosed(e);
      }
  }


  public final void internalFrameClosing(InternalFrameEvent e) {
      EventListener[] listeners=listenerList.getListeners(InternalFrameListener.class);
      for (int i=0;i<listeners.length;i++) {
          InternalFrameListener listener=(InternalFrameListener)listeners[i];
          listener.internalFrameClosing(e);
      }
  }


  public final void internalFrameDeactivated(InternalFrameEvent e) {
      EventListener[] listeners=listenerList.getListeners(InternalFrameListener.class);
      for (int i=0;i<listeners.length;i++) {
          InternalFrameListener listener=(InternalFrameListener)listeners[i];
          listener.internalFrameDeactivated(e);
      }
  }


  public final void internalFrameDeiconified(InternalFrameEvent e) {
      EventListener[] listeners=listenerList.getListeners(InternalFrameListener.class);
      for (int i=0;i<listeners.length;i++) {
          InternalFrameListener listener=(InternalFrameListener)listeners[i];
          listener.internalFrameDeiconified(e);
      }
  }


  public final void internalFrameIconified(InternalFrameEvent e) {
      EventListener[] listeners=listenerList.getListeners(InternalFrameListener.class);
      for (int i=0;i<listeners.length;i++) {
          InternalFrameListener listener=(InternalFrameListener)listeners[i];
          listener.internalFrameIconified(e);
      }
  }


  public final void internalFrameOpened(InternalFrameEvent e) {
      EventListener[] listeners=listenerList.getListeners(InternalFrameListener.class);
      for (int i=0;i<listeners.length;i++) {
          InternalFrameListener listener=(InternalFrameListener)listeners[i];
          listener.internalFrameOpened(e);
      }
  }


  /**
   * Store frame location and size to a local profile file.
   */
  private void saveState(InternalFrame frame) {

      if (frame.isMaximum()) {
        try {
          frame.setMaximum(false);
        }
        catch (PropertyVetoException ex) {
        }
      }

      Dimension size = frame.getSize();
      Point position = frame.getLocation();
      String userHome = System.getProperty("user.home").replace('\\', '/');
      if (!userHome.endsWith("/")) {
          userHome += "/";
      }
      userHome += "profiles";
      File dir = new File(userHome);
      dir.mkdirs();
      String[] str = System.getProperty("user.dir").split("\\\\");
      String project = str[str.length - 1].trim();
      String frameName = frame.getClass().getName().replace(' ', '_').replace('.', '_');
      if (getName()!=null)
        frameName += getName()+"_";
      File f = new File(userHome + "/" + project.replace(' ', '_') + "_" + frameName + "_" + System.getProperty("user.name").replace(' ', '_') + ".ini");
      if (f.exists())
          f.delete();

      PrintWriter pw = null;
      try {
          pw = new PrintWriter(new FileOutputStream(f));
          pw.println(size.width);
          pw.println(size.height);
          pw.println(position.x);
          pw.println(position.y);

      } catch (Throwable ex) {
      } finally {
          try {
              pw.close();
          } catch (Exception ex1) {
          }
      }
  }

  /**
   * Load frame location and size from a profile file and set them to this frame.
   */
  private void loadState(InternalFrame frame) {
      String userHome = System.getProperty("user.home").replace('\\', '/');
      if (!userHome.endsWith("/")) {
          userHome += "/";
      }
      userHome += "profiles";
      File dir = new File(userHome);
      dir.mkdirs();
      String[] str = System.getProperty("user.dir").split("\\\\");
      String project = str[str.length - 1].trim();
      String frameName = frame.getClass().getName().replace(' ', '_').replace('.', '_');
      if (getName()!=null)
        frameName += getName()+"_";
      File f = new File(userHome + "/" + project.replace(' ', '_') + "_" + frameName + "_" + System.getProperty("user.name").replace(' ', '_') + ".ini");
      if (!f.exists())
          return;

      BufferedReader br = null;
      try {
          br = new BufferedReader(new InputStreamReader(new FileInputStream(f)));
          int width = Integer.parseInt(br.readLine());
          int height = Integer.parseInt(br.readLine());
          int x = Integer.parseInt(br.readLine());
          int y = Integer.parseInt(br.readLine());
          frame.setSize(width,height);
          frame.setLocation(x,y);

      } catch (Throwable ex) {
      } finally {
          try {
              br.close();
          } catch (Exception ex1) {
          }
      }
  }



}
