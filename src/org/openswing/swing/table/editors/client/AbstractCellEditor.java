package org.openswing.swing.table.editors.client;

import java.util.*;

import javax.swing.*;
import javax.swing.event.*;


/**
 * <p>Title: OpenSwing Framework</p>
 * <p>Description: Abstract cell editor, used by other column editors.</p>
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
public class AbstractCellEditor implements CellEditor {

  /** listeners registered into this cell editor */
  protected EventListenerList listenerList = new EventListenerList();


  /**
   * This method should be overrided.
   * @return null
   */
  public Object getCellEditorValue() {
    return null;
  }


  /**
   * This method should be overrided.
   */
  public boolean isCellEditable(EventObject evt) {
    return true;
  }


  /**
   * This method should be overrided.
   */
  public boolean shouldSelectCell(EventObject evt) {
    return false;
  }


  /**
   * This method should be overrided.
   */
  public boolean stopCellEditing() {
    return true;
  }


  /**
   * This method should be overrided.
   */
  public void cancelCellEditing() {
  }


  /**
   * Add a listener to this cell editor.
   * @param listener listener to add
   */
  public final void addCellEditorListener(CellEditorListener listener) {
    listenerList.add(CellEditorListener.class, listener);
  }


  /**
   * Remove the specified listener from this cell editor.
   * @param listener listener to remove
   */
  public final void removeCellEditorListener(CellEditorListener listener) {
    listenerList.remove(CellEditorListener.class, listener);
  }


   /**
    * Notify all listeners that have registered interest for notification on this event type.
    * @see EventListenerList
    */
  protected void fireEditingStopped() {
    Object[] listeners = listenerList.getListenerList();
    for(int i=listeners.length-2;i>=0;i-=2) {
      if(listeners[i] == CellEditorListener.class) {
        ((CellEditorListener)listeners[i+1]).editingStopped(new ChangeEvent(this));
      }
    }
  }


  /**
    * Notify all listeners that have registered interest for notification on this event type.
    * @see EventListenerList
    */
  protected void fireEditingCanceled() {
    Object[] listeners = listenerList.getListenerList();
    for(int i=listeners.length-2;i>=0;i-=2)
      if(listeners[i] == CellEditorListener.class)
        ((CellEditorListener)listeners[i + 1]).editingCanceled(new ChangeEvent(this));
  }




}

