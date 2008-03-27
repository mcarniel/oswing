package org.openswing.swing.client;

import java.beans.*;

import org.openswing.swing.util.java.*;


/**
 * <p>Title: OpenSwing Framework</p>
 * <p>Description: Bean Editor used to select the filter panel on grid hiding policy.</p>
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
public class FilterPanelOnGridPolicyEditor extends PropertyEditorSupport {

  static private int[] policy = new int[]{
    Consts.FILTER_PANEL_ON_GRID_CLOSE_ON_EXIT,
    Consts.FILTER_PANEL_ON_GRID_USE_CLOSE_BUTTON,
    Consts.FILTER_PANEL_ON_GRID_USE_PADLOCK_PRESSED,
    Consts.FILTER_PANEL_ON_GRID_USE_PADLOCK_UNPRESSED
  };

  static private String[] descpolicy = new String[]{
    "Close on mouse exit",
    "Close using close button",
    "Close using padlock (pressed)",
    "Close using padlock (unpressed)"
  };


  public FilterPanelOnGridPolicyEditor() {
  }


  public String getJavaInitializationString() {
     if (getValue().equals(new Integer(Consts.FILTER_PANEL_ON_GRID_CLOSE_ON_EXIT)))
         return "org.openswing.swing.util.java.Consts.FILTER_PANEL_ON_GRID_CLOSE_ON_EXIT";
     if (getValue().equals(new Integer(Consts.FILTER_PANEL_ON_GRID_USE_CLOSE_BUTTON)))
         return "org.openswing.swing.util.java.Consts.FILTER_PANEL_ON_GRID_USE_CLOSE_BUTTON";
     if (getValue().equals(new Integer(Consts.FILTER_PANEL_ON_GRID_USE_PADLOCK_PRESSED)))
         return "org.openswing.swing.util.java.Consts.FILTER_PANEL_ON_GRID_USE_PADLOCK_PRESSED";
     else
         return "org.openswing.swing.util.java.Consts.FILTER_PANEL_ON_GRID_USE_PADLOCK_UNPRESSED";
  }


  public String[] getTags() {
    return descpolicy;
  }


  public void setAsText(String text) throws IllegalArgumentException {
    for(int i=0;i<descpolicy.length;i++)
      if (descpolicy[i].equals(text))
        setValue(new Integer(policy[i]));
  }


  public String getAsText() {
    for(int i=0;i<policy.length;i++)
      if (policy[i]==((Integer)getValue()).intValue())
        return descpolicy[i];
    return null;
  }

}
