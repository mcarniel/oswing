package org.openswing.swing.client;

import java.awt.*;
import java.awt.event.*;
import javax.swing.event.*;


/**
 * <p>Title: OpenSwing Framework</p>
 * <p>Description: Interface used by AutoCompletitionKeyListener and implemented by input controls that support auto completition.
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
public interface AutoCompletitionInputControl {



  /**
   * @return value related to the input control
   */
  public Object getValue();


  /**
   * Set value to the input control.
   * @param value value to set into the input control
   */
  public void setValue(Object value);


  /**
   * Registers <code>listener</code> so that it will receive
   * <code>AncestorEvents</code> when it or any of its ancestors
   * move or are made visible or invisible.
   * Events are also sent when the component or its ancestors are added
   * or removed from the containment hierarchy.
   *
   * @param listener  the <code>AncestorListener</code> to register
   * @see AncestorEvent
   */
  public void addAncestorListener(AncestorListener listener);


  /**
   * Adds the specified focus listener to receive focus events from
   * this component when this component gains input focus.
   * If listener <code>l</code> is <code>null</code>,
   * no exception is thrown and no action is performed.
   *
   * @param    l   the focus listener
   * @see      java.awt.event.FocusEvent
   * @see      java.awt.event.FocusListener
   * @see      #removeFocusListener
   * @see      #getFocusListeners
   * @since    JDK1.1
   */
  public void addFocusListener(FocusListener l);


  /**
   * Gets the location of this component in the form of a point
   * specifying the component's top-left corner in the screen's
   * coordinate space.
   * @return an instance of <code>Point</code> representing
   * 		the top-left corner of the component's bounds in the
   * 		coordinate space of the screen
   * @throws <code>IllegalComponentStateException</code> if the
   * 		component is not showing on the screen
   * @see #setLocation
   * @see #getLocation
   */
  public Point getLocationOnScreen();


  /**
   * Returns the current height of this component.
   * This method is preferable to writing
   * <code>component.getBounds().height</code>, or
   * <code>component.getSize().height</code> because it doesn't cause any
   * heap allocations.
   *
   * @return the current height of this component
   */
  public int getHeight();


  /**
   * Returns the current witdh of this component.
   * This method is preferable to writing
   * <code>component.getBounds().witdh</code>, or
   * <code>component.getSize().witdh</code> because it doesn't cause any
   * heap allocations.
   *
   * @return the current witdh of this component
   */
  public int getWidth();


}
