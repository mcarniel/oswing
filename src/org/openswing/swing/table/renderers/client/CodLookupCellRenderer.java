package org.openswing.swing.table.renderers.client;

import javax.swing.*;

import org.openswing.swing.table.client.*;
import org.openswing.swing.util.client.ClientSettings;
import java.awt.ComponentOrientation;


/**
 * <p>Title: OpenSwing Framework</p>
 * <p>Description: Column renderer used to format a lookup type column:
 * its behaviour is as the text column, except in the case when "hideCodBox" is set to <code>true</code>.</p>
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
public class CodLookupCellRenderer extends TextTableCellRenderer {

  /** flag used to view "" instead of the real text */
  private boolean hideCodBox;


  /**
   * Constructor.
   * @param gridController grid controller
   * @param attributeName attribute name associated to this column
   */
  public CodLookupCellRenderer(GridController gridController,boolean hideCodBox,int alignement,
                               int leftMargin,int rightMargin,int topMargin,int bottomMargin,
                               ComponentOrientation orientation,String attributeName) {
    super(gridController,false,alignement,leftMargin,rightMargin,topMargin,bottomMargin,orientation,attributeName);
    this.hideCodBox = hideCodBox;
  }


  public java.awt.Component getTableCellRendererComponent(JTable table, Object value,
                          boolean isSelected, boolean hasFocus, int row, int column) {
    JComponent c = (JComponent)super.getTableCellRendererComponent(table, value,isSelected, hasFocus, row, column);
    if (hideCodBox)
      ((JLabel)c).setText("");

    return c;
  }



}
