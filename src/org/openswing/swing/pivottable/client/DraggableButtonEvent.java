package org.openswing.swing.pivottable.client;

/**
 * <p>Title: OpenSwing Framework</p>
 * <p>Description: Droppable panel used to host draggable buttons of pivot table.</p>
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
public class DraggableButtonEvent {

  /** source button index */
  private int source;

  /** target button index */
  private int target;

  /** button container id */
  private String panelId = null;


  public DraggableButtonEvent(String panelId,int source,int target) {
    this.panelId = panelId;
    this.source = source;
    this.target = target;
  }


  /**
   * @return DraggableButton source button
   */
  public final int getSource() {
    return source;
  }


  /**
   * @return DraggableButton target button
   */
  public final int getTarget() {
    return target;
  }


  /**
   * @return button container id
   */
  public final String getPanelId() {
    return panelId;
  }




}
