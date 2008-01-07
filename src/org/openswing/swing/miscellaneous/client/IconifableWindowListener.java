package org.openswing.swing.miscellaneous.client;


/**
 * <p>Title: OpenSwing Framework</p>
 * <p>Description: Listener of the IconifableWindow component, used to listen several events fired by that window:
 * - window just created but not yet showed
 * - window created and just showed
 * - window closed
 * - window reduced to icon
 * - window restored to its original dimension (from the reduce to icon dimensions)
 *  </p>
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
public interface IconifableWindowListener {

  /** "window just created but not yet showed" event */
  public static final int WINDOW_CREATED = 0;

  /** "window created and just showed" event */
  public static final int WINDOW_SHOWED = 1;

  /** "window just closed" event */
  public static final int WINDOW_CLOSED = 2;

  /** "window just reduced to icon" event */
  public static final int WINDOW_REDUCED_TO_ICON = 3;

  /** "window restored to its original dimension" event */
  public static final int WINDOW_RESTORED = 4;


  /**
   * Event fired by the IconifableWindow
   * @param source IconifableWindow that fires the event
   * @param event event type; supported values: WINDOW_CREATED, WINDOW_SHOWED, WINDOW_CLOSED, WINDOW_REDUCED_TO_ICON, WINDOW_RESTORED
   */
  public void windowEvent(IconifableWindow source,int event);


}
