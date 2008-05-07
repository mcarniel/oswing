package org.openswing.swing.client;

import java.beans.*;
import org.openswing.swing.util.java.Consts;


/**
 * <p>Title: OpenSwing Framework</p>
 * <p>Description: Bean Editor for button behavior.</p>
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
public class ButtonBehaviorEditor extends PropertyEditorSupport {

  static private int[] buttonBehaviors = new int[]{
      Consts.BUTTON_IMAGE_ONLY,
      Consts.BUTTON_TEXT_ONLY,
      Consts.BUTTON_IMAGE_AND_TEXT
  };

  static private String[] descbuttonBehaviors = new String[]{
      "Image only",
      "Text only",
      "Image and text",
  };


  public ButtonBehaviorEditor() {
  }


  public String[] getTags() {
    return descbuttonBehaviors;
  }


  public void setAsText(String text) throws IllegalArgumentException {
    for(int i=0;i<descbuttonBehaviors.length;i++)
      if (descbuttonBehaviors[i].equals(text))
        setValue(new Integer(buttonBehaviors[i]));
  }


  public String getAsText() {
    for(int i=0;i<buttonBehaviors.length;i++)
      if (buttonBehaviors[i]==((Integer)getValue()).intValue())
        return descbuttonBehaviors[i];
    return null;
  }

}
