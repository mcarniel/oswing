package org.openswing.swing.client;

import java.beans.*;
import org.openswing.swing.util.java.Consts;


/**
 * <p>Title: OpenSwing Framework</p>
 * <p>Description: Bean Editor used to select the quick filter criteria.</p>
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
public class QuickFilterCriteriaEditor extends PropertyEditorSupport {

  static private int[] criteria = new int[]{
    Consts.EQUALS,
    Consts.CONTAINS,
    Consts.STARTS_WITH,
    Consts.ENDS_WITH
  };

  static private String[] desccriteria = new String[]{
    "Equals",
    "Contains",
    "Starts With",
    "Ends With"
  };


  public QuickFilterCriteriaEditor() {
  }


  public String getJavaInitializationString() {
     if (getValue().equals(new Integer(Consts.EQUALS)))
         return "org.openswing.swing.util.java.Consts.EQUALS";
     if (getValue().equals(new Integer(Consts.CONTAINS)))
         return "org.openswing.swing.util.java.Consts.CONTAINS";
     if (getValue().equals(new Integer(Consts.STARTS_WITH)))
         return "org.openswing.swing.util.java.Consts.STARTS_WITH";
     else
         return "org.openswing.swing.util.java.Consts.ENDS_WITH";
  }


  public String[] getTags() {
    return desccriteria;
  }


  public void setAsText(String text) throws IllegalArgumentException {
    for(int i=0;i<desccriteria.length;i++)
      if (desccriteria[i].equals(text))
        setValue(new Integer(criteria[i]));
  }


  public String getAsText() {
    for(int i=0;i<criteria.length;i++)
      if (criteria[i]==((Integer)getValue()).intValue())
        return desccriteria[i];
    return null;
  }

}
