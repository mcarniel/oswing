package org.openswing.swing.form.client;

import java.awt.event.*;
import org.openswing.swing.util.client.ClientSettings;


/**
 * <p>Title: OpenSwing Framework</p>
 * <p>Description: Class used by Form panel to listen for focus events.</p>
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

public class FormShortcutsListener extends KeyAdapter {

  /** related form */
  private Form form = null;


  public FormShortcutsListener(Form form) {
    this.form = form;
  }


  public final Form getForm() {
    return form;
  }

  public void keyPressed(KeyEvent e) {
    if (form.isCurrentFormHasFocus()) {
      if (e.getKeyCode()==ClientSettings.RELOAD_BUTTON_KEY.getKeyCode() &&
          e.getModifiers()+e.getModifiersEx()==ClientSettings.RELOAD_BUTTON_KEY.getModifiers() &&
          form.getReloadButton() != null &&
          form.getReloadButton().isEnabled()) {
        form.getReloadButton().requestFocus();
        form.reload();
      }
      else if (e.getKeyCode()==ClientSettings.SAVE_BUTTON_KEY.getKeyCode() &&
               e.getModifiers()+e.getModifiersEx()==ClientSettings.SAVE_BUTTON_KEY.getModifiers() &&
               form.getSaveButton() != null &&
               form.getSaveButton().isEnabled()) {
        form.getSaveButton().requestFocus();
        form.save();
      }
      else if (e.getKeyCode()==ClientSettings.INSERT_BUTTON_KEY.getKeyCode() &&
               e.getModifiers()+e.getModifiersEx()==ClientSettings.INSERT_BUTTON_KEY.getModifiers() &&
               form.getInsertButton() != null &&
               form.getInsertButton().isEnabled()) {
        form.getInsertButton().requestFocus();
        form.insert();
      }
      else if (e.getKeyCode()==ClientSettings.COPY_BUTTON_KEY.getKeyCode() &&
               e.getModifiers()+e.getModifiersEx()==ClientSettings.COPY_BUTTON_KEY.getModifiers() &&
               form.getCopyButton() != null &&
               form.getCopyButton().isEnabled()) {
        form.getCopyButton().requestFocus();
        form.copy();
      }
      else if (e.getKeyCode()==ClientSettings.EDIT_BUTTON_KEY.getKeyCode() &&
               e.getModifiers()+e.getModifiersEx()==ClientSettings.EDIT_BUTTON_KEY.getModifiers() &&
               form.getEditButton() != null &&
               form.getEditButton().isEnabled()) {
        form.getEditButton().requestFocus();
        form.edit();
      }
      else if (e.getKeyCode()==ClientSettings.DELETE_BUTTON_KEY.getKeyCode() &&
               e.getModifiers()+e.getModifiersEx()==ClientSettings.DELETE_BUTTON_KEY.getModifiers() &&
               form.getDeleteButton() != null &&
               form.getDeleteButton().isEnabled())
        form.delete();
    }
  }


}
