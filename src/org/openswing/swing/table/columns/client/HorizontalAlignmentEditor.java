package org.openswing.swing.table.columns.client;


import java.beans.*;

import javax.swing.*;


/**
 * <p>Title: OpenSwing Framework</p>
 * <p>Description: Bean Editor</p>
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
public class HorizontalAlignmentEditor extends PropertyEditorSupport {

  static private int[] alignments = new int[]{
    SwingConstants.LEFT,
    SwingConstants.CENTER,
    SwingConstants.RIGHT,
    SwingConstants.LEADING,
    SwingConstants.TRAILING
  };

  static private String[] alignmentsDescr = new String[]{
    "LEFT",
    "CENTER",
    "RIGHT",
    "LEADING",
    "TRAILING"
  };

  public HorizontalAlignmentEditor() {
  }

  public String getJavaInitializationString() {
     switch( ((Number) getValue()).intValue()) {
       case SwingConstants.LEFT :
         return "javax.swing.SwingConstants.LEFT";
       case SwingConstants.CENTER :
         return "javax.swing.SwingConstants.CENTER";
       case SwingConstants.RIGHT :
         return "javax.swing.SwingConstants.RIGHT";
       case SwingConstants.LEADING :
         return "javax.swing.SwingConstants.LEADING";
       case SwingConstants.TRAILING :
         return "javax.swing.SwingConstants.TRAILING";
       default:
         return "javax.swing.SwingConstants.LEADING";
     }
   }

  public String[] getTags() {
    return alignmentsDescr;
  }

  public void setAsText(String text) throws IllegalArgumentException {
    for(int i=0;i<alignmentsDescr.length;i++)
      if (alignmentsDescr[i].equals(text))
        setValue(new Integer(alignments[i]));
  }

  public String getAsText() {
    for(int i=0;i<alignments.length;i++)
      if (alignments[i]==((Integer)getValue()).intValue())
        return alignmentsDescr[i];
    return null;
  }

}
