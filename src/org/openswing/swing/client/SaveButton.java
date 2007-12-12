package org.openswing.swing.client;

import javax.swing.*;
import java.awt.*;

import org.openswing.swing.client.*;

import org.openswing.swing.util.client.*;
import org.openswing.swing.internationalization.java.*;


/**
 * <p>Title: OpenSwing Framework</p>
 * <p>Description: Save button, linked to GridControl or Form components.</p>
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
public class SaveButton extends GenericButton {

  public SaveButton() {
    super(new ImageIcon(ClientUtils.getImage("save.gif")));
    setPreferredSize(new Dimension(32,32));
  }

  public void setText(String t) { }


  /**
   * Execute the save operation.
   * @param controller: data controller that contains the save logic.
   */
  protected final void executeOperation(DataController controller) throws Exception {
    try {
      controller.save();
    }
    catch (Exception ex) {
      ex.printStackTrace();
    }
  }


}
