package org.openswing.swing.table.columns.client;

import java.beans.*;

import org.openswing.swing.util.java.*;


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
public class SortVersusEditor extends PropertyEditorSupport {

  static private String[] colVersus = new String[]{
    Consts.NO_SORTED,
    Consts.ASC_SORTED,
    Consts.DESC_SORTED
  };

  static private String[] descColVersus = new String[]{
    "No Ord.",
    "Ascending",
    "Descending"
  };


  public SortVersusEditor() {
  }


  public String getJavaInitializationString() {
     if (getValue().equals(Consts.NO_SORTED))
         return "org.openswing.swing.util.java.Consts.NO_SORTED";
     if (getValue().equals(Consts.ASC_SORTED))
         return "org.openswing.swing.util.java.Consts.ASC_SORTED";
     if (getValue().equals(Consts.DESC_SORTED))
         return "org.openswing.swing.util.java.Consts.DESC_SORTED";
     else
         return "org.openswing.swing.util.java.Consts.NO_SORTED";
   }



  public String[] getTags() {
    return descColVersus;
  }


  public void setAsText(String text) throws IllegalArgumentException {
    for(int i=0;i<descColVersus.length;i++)
      if (descColVersus[i].equals(text))
        setValue(colVersus[i]);
  }


  public String getAsText() {
    for(int i=0;i<colVersus.length;i++)
      if (colVersus[i].equals(getValue()))
        return descColVersus[i];
    return null;
  }

}
