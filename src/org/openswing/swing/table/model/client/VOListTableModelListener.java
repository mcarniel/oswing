package org.openswing.swing.table.model.client;





/**
 * <p>Title: OpenSwing Framework</p>
 * <p>Description: Interface used to listen the TableModel mode changed event.
 * This interface is implemented by the grid to listen mode changes.</p>
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
public interface VOListTableModelListener {

  /**
   * This method is invoked by VOListTableModel when the grid mode is changed.
   * @param mode current edit grid mode; possible values: READONLY, EDIT,INSERT
   */
  public void modeChanged(int mode);

}
