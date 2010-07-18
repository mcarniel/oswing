package org.openswing.swing.mdi.client;

import java.beans.*;
import java.util.*;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.*;
import javax.swing.event.*;

import java.io.*;

import org.openswing.swing.client.*;
import org.openswing.swing.form.client.*;
import org.openswing.swing.logger.client.*;
import org.openswing.swing.tree.client.*;
import org.openswing.swing.util.client.*;
import org.openswing.swing.util.java.*;


/**
 * <p>Title: OpenSwing Framework</p>
 * <p>Description: Base Internal Frame: to use together with MDI Frame.
 * You can set this internal frame as modal, by means of setModal() method BUT ONLY AFTER invoking MDIFrame.add() method,
 * i.e. after internal frame is already visible.</p>
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
public class InternalFrame extends JInternalFrame {

  /** children windows (optional); they will be automatically closed when this window is closed */
  private ArrayList frameList = new ArrayList();

  /** parent frame (optional) */
  private InternalFrame  parentFrame;

  /** used when this window will be closed: if this flag is set to <code>true</code> then a warning dialog will be  showed before close the window to ask if it must be close; default value: ClientSettings.ASK_BEFORE_CLOSE */
  private boolean askBeforeClose = ClientSettings.ASK_BEFORE_CLOSE;

  /** last border of internal frame, before hiding title bar */
  private Border lastBorder = null;

  /** flag used to hide/show title bar; default value: <code>false</code> i.e. title bar is visible */
  private boolean hideTitleBar = false;

  /** last component added to the north of the internal frame, before hiding title bar */
  private JComponent lastNorthPane = null;

  /** internal frame modal state; default value: <code>false</code> i.e. internal frame is not modal */
  private boolean modal;

  /** internal frame owner */
  protected javax.swing.JInternalFrame owner = this;

  /** used to define whether this window can be opened more times or only one instance can be created per time; default value: <code>false</code>, i.e. any number of instances of this window can be created */
  private boolean uniqueInstance = false;


  /**
   * Costructor.
   */
  public InternalFrame() {
    super("",true,true,true,true);
    setFrameIcon(new ImageIcon(ClientUtils.getImage(ClientSettings.ICON_FILENAME)));
    this.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
    this.addInternalFrameListener(new InternalFrameAdapter() {

      public void internalFrameClosing(InternalFrameEvent e) {
        try {
          closeFrame();
        }
        catch (PropertyVetoException ex) {
        }
      }

      /**
       * Invoked when an internal frame is activated.
       */
      public void internalFrameActivated(InternalFrameEvent e) {
      }

      /**
       * Invoked when an internal frame is de-activated.
       */
      public void internalFrameDeactivated(InternalFrameEvent e) {
        if (ClientSettings.MDI_TOOLBAR!=null)
          ClientSettings.MDI_TOOLBAR.disableAllButtons();
      }


    });
  }


  /**
   * Create a link between this window and the parent window.
   * @param parent parent window
   */
  public final void setParentFrame(InternalFrame parentFrame) {
    this.parentFrame = parentFrame;
  }


  /**
   * @return list of TreePanel/GridControl/Form objects within this that is in insert/edit mode and that contain changes, an empty list otherwise
   */
  public final ArrayList checkComponents() {
    return checkComponents(this.getComponents());
  }


  /**
   * @param c components to analize
   * @return list of TreePanel/GridControl/Form objects within c components, that are in insert/edit mode and that contain changes, otherwise an empty list
   */
  private ArrayList checkComponents(Component[] c) {
    ArrayList aux = new ArrayList();
    for(int i=0;i<c.length;i++) {
      if (c[i] instanceof GridControl) {
        c[i].transferFocus();
        if (((GridControl)c[i]).getMode()==Consts.INSERT)
          aux.add(c[i]);
        if (((GridControl)c[i]).getMode()==Consts.EDIT &&
          ((GridControl)c[i]).getVOListTableModel().getChangedRowNumbers().length>0) {
          aux.add(c[i]);
        }
      }
      else if (c[i] instanceof Form) {
        try {
          Component obj = DefaultKeyboardFocusManager.getCurrentKeyboardFocusManager().getFocusOwner();
          if (obj!=null) {
            FocusListener[] f = obj.getFocusListeners();
            if (f!=null)
              for(int j=0;j<f.length;j++)
                f[j].focusLost(new FocusEvent(this,FocusEvent.FOCUS_LOST));
          }
            obj.transferFocus();
        }
        catch (Exception ex) {
          ((Form) c[i]).transferFocus();
        }
        boolean changed = ((Form)c[i]).isChanged();
        if (((Form)c[i]).getMode()==Consts.INSERT && changed)
          aux.add(c[i]);
        if (((Form)c[i]).getMode()==Consts.EDIT && changed)
          aux.add(c[i]);
      }
      else if (c[i] instanceof TreePanel) {
        if (((TreePanel)c[i]).isChanged())
          aux.add(c[i]);
      }
      else if (c[i] instanceof JScrollPane) {
        aux.addAll(checkComponents( ((JScrollPane)c[i]).getViewport().getComponents() ));
      }
      else if (c[i] instanceof Container) {
        aux.addAll(checkComponents( ((Container)c[i]).getComponents() ));
      }
    }
    return aux;
  }


  /**
   * Callback method invoked by "closeFrame" when internal frame contains changes and the user selects "yes" option on dialog about saving changes:
   * If this method is not ovverrided, then "closeFrame" will attempt to automatically save each changed objects (Form, Grid, TreePanel)
   * and this automation could be incorrect, in case of multiple objects to save in a specific order,
   * if this is the case, then "saveChanges" method should be overrided, in order to specify a custom saving strategy.
   * @return <code>true</code> if changes has been correctly saved, <code>false</code> otherwise, i.e. some error occours on saving data, so frame must NOT be closed; as default dehavior it throws an exception: UnsupportedOperationException, i.e. closeFrame method automatically attempts to save changes
   */
  public boolean saveChanges() {
    throw new UnsupportedOperationException("No check performed");
  }


  /**
   * Supports reporting constrained property changes.
   * This method can be called when a constrained property has changed
   * and it will send the appropriate <code>PropertyChangeEvent</code>
   * to any registered <code>VetoableChangeListeners</code>.
   *
   * @param propertyName  the name of the property that was listened on
   * @param oldValue  the old value of the property
   * @param newValue  the new value of the property
   * @exception PropertyVetoException when the attempt to set the
   *		property is vetoed by the component
   */
  protected final void fireVetoableChange(String propertyName, Object oldValue, Object newValue)
      throws java.beans.PropertyVetoException
  {
    if (IS_CLOSED_PROPERTY.equals(propertyName) && Boolean.FALSE.equals(oldValue) && Boolean.TRUE.equals(newValue)) {
      throw new PropertyVetoException("",new PropertyChangeEvent(this,IS_CLOSED_PROPERTY,Boolean.TRUE,Boolean.FALSE));
    }
    else
      super.fireVetoableChange(propertyName, oldValue, newValue);
  }



  /**
   * This method is called when this window will be closed.
   * The first method calle is beforeCloseFrame; if this will return <code>false</code> then the closing window operation will be interrupted.
   */
  public final void closeFrame() throws PropertyVetoException {
    if (!beforeCloseFrame()) {
      return;
    }
    if (askBeforeClose) {
      // check if this contains a GridControl or a Form panel in insert/edit mode and they contain changes...
      ArrayList changedObjects = checkComponents();
      if (changedObjects.size()>0) {
        int res = OptionPane.showConfirmDialog(
          this,
          "save changes?",
          "confirm window closing",
          JOptionPane.YES_NO_CANCEL_OPTION
        );

        if (res==JOptionPane.YES_OPTION) {
          try {
            boolean ok = saveChanges();
            if (!ok) {
              // save task contains errors: frame closing will be aborted...
              return;
            }
          }
          catch (UnsupportedOperationException ex1) {
            // "saveChanged" method was not overrided:
            // each changed object will be saved automatically
            // NOTE: this automation could be incorrect, in case of multiple objects to save in a specific order,
            //       if this is the case, then "saveChanges" method should be overrided, in order to specify a custom saving strategy
            for(int i=0;i<changedObjects.size();i++) {
              if (changedObjects.get(i) instanceof GridControl) {
                if (!((GridControl)changedObjects.get(i)).save())
                  return;
              }
              else if (changedObjects.get(i) instanceof Form) {
                if (!((Form)changedObjects.get(i)).save()) {
                  return;
                }
              }
              else if (changedObjects.get(i) instanceof TreePanel) {
                Logger.error(this.getClass().getName(), "closeFrame", "Error while saving TreePanel: you have to override 'saveChanges' method.",null);
                return;
              }
            }
          }
        }
        if (res==JOptionPane.CANCEL_OPTION) {
          return;
        }
      }
    }
    try{
      // close all children windows...
      while(frameList.size()>0){
        InternalFrame frame = (InternalFrame)frameList.get(0);
        frameList.remove(0);
        frame.closeFrame();
      }
      // remove link with the parent window...
      if (parentFrame!=null)
        parentFrame.popFrame(this);
    } catch (Exception e){
      e.printStackTrace();
    }
    try {
      ClientUtils.disposeComponents(getContentPane().getComponents());

      setVisible(false);
      dispose();
      MDIFrame.getWindowMenu().removeWindow(this);
    } catch (Exception ex) {
      ex.printStackTrace();
    }
  }


  /**
   * Remove a child window from this.
   * It's called by closeFrame.
   * @param frame child window to remove
   */
  public final InternalFrame popFrame(InternalFrame frame) {
    if (frame!=null)
      frameList.remove(frame);
    return frame;
  }


  /**
   * Add a child window to this.
   * @param frame child window to add
   */
  public final void pushFrame(InternalFrame frame) {
    frameList.add(frame);
  }


  /**
   * Callback method invoked by closeFrame.
   * @return <code>true</code> allows the closing operation to continue, <code>false</code> the closing operation will be interrupted
   */
  protected boolean beforeCloseFrame() {
    return true;
  }


  /**
   * @return used when this window will be closed: if this flag is set to <code>true</code> then a warning dialog will be  showed before close the window to ask if it must be close
   */
  public final boolean isAskBeforeClose() {
    return askBeforeClose;
  }


  /**
   * Used when this window will be closed: if this flag is set to <code>true</code> then a warning dialog will be  showed before close the window to ask if it must be close
   * @param askBeforeClose <code>true</code> to show a warning dialog before close the window to ask if it must be close
   */
  public final void setAskBeforeClose(boolean askBeforeClose) {
    this.askBeforeClose = askBeforeClose;
  }


  /**
   * Define if title bar must be hidden or showed
   * @param hideTitleBar flag used to hide/show title bar; <code>true</code> to hide title bar; <code>false</code> to show it
   */
  public final boolean isHideTitleBar() {
    return hideTitleBar;
  }



  /**
   * Define if title bar must be hidden or showed
   * @param hideTitleBar flag used to hide/show title bar; <code>true</code> to hide title bar; <code>false</code> to show it
   */
  public final void setHideTitleBar(boolean hideTitleBar) {
    this.hideTitleBar = hideTitleBar;
    if (hideTitleBar && !Beans.isDesignTime()) {
      super.setIconifiable(false);
      super.setMaximizable(false);
      lastNorthPane = ((javax.swing.plaf.basic.BasicInternalFrameUI)this.getUI()).getNorthPane();
      ((javax.swing.plaf.basic.BasicInternalFrameUI)this.getUI()).setNorthPane(null);
      lastBorder = super.getBorder();
      super.setBorder(BorderFactory.createEmptyBorder());
    }
    else if (lastNorthPane!=null && lastBorder!=null) {
      ((javax.swing.plaf.basic.BasicInternalFrameUI)this.getUI()).setNorthPane(lastNorthPane);
      super.setBorder(lastBorder);
    }
  }


  /**
   * @return internal frame owner; used in case of modal internal frame
   */
  public final javax.swing.JInternalFrame getOwner() {
    return owner;
  }


  /**
   * Set the internal frame owner; used in case of modal internal frame.
   * @param owner internal frame owner
   */
  public final void setOwner(javax.swing.JInternalFrame owner) {
     this.owner=owner;
  }


  /**
   * Define this internal frame as modal.
   * IMPORTANT NOTE: do not call this method before showing this frame, but only AFTER MDIFrame.add() invokation.
   * @param modal <code>true</code> to set this internal frame as modal, <code>false</code> otherwise
   */
  public final void setModal(boolean modal) {
      this.modal=modal;
      javax.swing.JDesktopPane desktop=getDesktopPane();
      if (desktop instanceof JDesktopPane) {
        ((DesktopPane)desktop).setModal(this,modal);
      }
  }


  /**
   * @return <code>true</code> whether this internal frame as modal, <code>false</code> otherwise
   */
  public final boolean isModal() {
     return modal;
  }


  /**
   * Iconify this internal frame.
   * Please do not call this method for modal internal frames!
   * @param iconifiable <code>true</code> to iconify internal frame
   */
  public final void setIconifiable(boolean iconifiable) {
    if (modal && iconifiable)
      throw new IllegalArgumentException("InternalFrame class cannot be iconifiable while modal");
    super.setIconifiable(iconifiable);
  }


  /*
   * Creates a new EventDispatchThread to dispatch events from this.
   * This method returns when stopModal is invoked.
   */
  public synchronized void startModal() {
    if (isVisible() && !isShowing()) {
      Container parent = this.getParent();
      while (parent!=null) {
        if (!parent.isVisible())
          parent.setVisible(true);
        parent = parent.getParent();
      }
    }

    try {
      if (javax.swing.SwingUtilities.isEventDispatchThread()) {
        EventQueue theQueue = getToolkit().getSystemEventQueue();
        while (isVisible()) {
          // This is essentially the body of EventDispatchThread
          AWTEvent event = theQueue.getNextEvent();
          Object src = event.getSource();
          // can't call theQueue.dispatchEvent, so I pasted its body here
          if (event instanceof ActiveEvent) {
              ((ActiveEvent) event).dispatch();
          } else if (src instanceof Component) {
              ((Component) src).dispatchEvent(event);
          } else if (src instanceof MenuComponent) {
              ((MenuComponent) src).dispatchEvent(event);
          } else {
            Logger.error(this.getClass().getName(), "startModal", "Unable to dispatch event: "+event,null);
          }
        }
      } else {
        while (isVisible())
          wait();
      }
    } catch(Exception ex){
      ex.printStackTrace();
    }
  }


  /*
   * Stops the event dispatching loop created by a previous call to <code>startModal</code>.
   */
  public synchronized void stopModal() {
    notifyAll();
  }


  /**
   * @return used to define whether this window can be opened more times or only one instance can be created per time
   */
  public final boolean isUniqueInstance() {
    return uniqueInstance;
  }


  /**
   * Define whether this window can be opened more times or only one instance can be created per time.
   * Default value: <code>false</code>, i.e. any number of instances of this window can be created.
   * @param uniqueInstance <code>true</code> only one instance of this window will be opened per time, <code>false</code> otherwise
   */
  public final void setUniqueInstance(boolean uniqueInstance) {
    this.uniqueInstance = uniqueInstance;
  }


}
