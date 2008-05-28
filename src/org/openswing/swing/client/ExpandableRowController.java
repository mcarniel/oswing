package org.openswing.swing.client;

import org.openswing.swing.message.receive.java.ValueObject;
import org.openswing.swing.table.model.client.VOListTableModel;
import javax.swing.JComponent;


/**
 * <p>Title: OpenSwing Framework</p>
 * <p>Description: controller used to manage row expansion in grid control.</p>
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
public class ExpandableRowController {


  public ExpandableRowController() {
  }


  /**
   * @param model grid model
   * @param rowNum the current row number
   * @return <code>true</code> if the current row must be expanded, <code>false</code> otherwise
   */
  public boolean isRowExpandable(VOListTableModel model,int rowNum) {
    return false;
  }


  /**
   * @param model grid model
   * @param rowNum the current row number
   * @return JComponent to show when expanding row; null to do not show anything
   */
  public JComponent getComponentToShow(VOListTableModel model,int rowNum) {
    return null;
  }


}
