package org.openswing.swing.permissions.java;

import java.io.*;
import java.util.*;


/**
 * <p>Title: OpenSwing Framework</p>
 * <p>Description: According to the authorizations defined for the button container,
 * this class defines a button abilitation.
 * .</p>
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
public class ButtonsAuthorizations implements Serializable {

  /** collection of pairs: functionId, ButtonAuthorization object */
  private Hashtable authorizations = new Hashtable();

  /** collection of pairs: functionId, GenericButtonAuthorization object */
  private Hashtable genericButtonsAuthorizations = new Hashtable();


  public ButtonsAuthorizations() { }


  /**
   * Add buttons authorizations for insert, edit and delete buttons.
   */
  public final void addButtonAuthorization(String functionId,boolean isInsertEnabled,boolean isEditEnabled,boolean isDeleteEnabled) {
    ButtonAuthorization ba = (ButtonAuthorization)authorizations.get(functionId);
    if (ba==null)
      authorizations.put(functionId,new ButtonAuthorization(functionId,isInsertEnabled,isEditEnabled,isDeleteEnabled));
    else
      authorizations.put(functionId,new ButtonAuthorization(
          functionId,
          ba.isInsertEnabled() || isInsertEnabled,
          ba.isEditEnabled() || isEditEnabled,
          ba.isDeleteEnabled() || isDeleteEnabled
      ));
  }


  /**
   * Add button authorizations for a generic button, identified by button id.
   * This method must not be used for insert, edit or delete buttons.
   */
  public final void addGenericButtonAuthorization(String functionId,String buttonId,boolean isEnabled) {
    GenericButtonAuthorization ba = (GenericButtonAuthorization)genericButtonsAuthorizations.get(functionId);
    if (ba==null)
      genericButtonsAuthorizations.put(functionId,new GenericButtonAuthorization(buttonId,isEnabled));
    else
      ba.addGenericButtonAuthorization(buttonId,isEnabled);
  }


  /**
   * @param functionId identifier of the function
   * @return <code>true</code> to enable the button, <code>false</code> to disable the button
   */
  public final boolean isInsertEnabled(String functionId) {
    if (functionId==null)
      // if no functionId is defined, then button is enabled
      return true;
    ButtonAuthorization auth = (ButtonAuthorization)authorizations.get(functionId);
    if (auth==null)
      // button is enabled if no authorization was found...
      return true;
    else
      return auth.isInsertEnabled();
  }


  /**
   * @param functionId identifier of the function
   * @return <code>true</code> to enable the button, <code>false</code> to disable the button
   */
  public final boolean isEditEnabled(String functionId) {
    if (functionId==null)
      // if no functionId is defined, then button is enabled
      return true;
    ButtonAuthorization auth = (ButtonAuthorization)authorizations.get(functionId);
    if (auth==null)
      // button is enabled if no authorization was found...
      return true;
    else
      return auth.isEditEnabled();
  }


  /**
   * @param functionId identifier of the function
   * @return <code>true</code> to enable the button, <code>false</code> to disable the button
   */
  public final boolean isDeleteEnabled(String functionId) {
    if (functionId==null)
      // if no functionId is defined, then button is enabled
      return true;
    ButtonAuthorization auth = (ButtonAuthorization)authorizations.get(functionId);
    if (auth==null)
      // button is enabled if no authorization was found...
      return true;
    else
      return auth.isDeleteEnabled();
  }


  /**
   * @param functionId identifier of the function
   * @param buttonId button identifier, optionally setted using button id property in GenericButton
   * @return <code>true</code> to enable the button, <code>false</code> to disable the button
   */
  public final boolean isEnabled(String functionId,String buttonId) {
    if (functionId==null)
      // if no functionId is defined, then button is enabled
      return true;
    GenericButtonAuthorization auth = (GenericButtonAuthorization)genericButtonsAuthorizations.get(functionId);
    if (auth==null)
      // button is enabled if no authorization was found...
      return true;
    else {
      Boolean b = auth.isEnabled(buttonId);
      if (b==null)
        return true;
      else
        return b.booleanValue();
    }
  }


  public Hashtable getAuthorizations() {
    return authorizations;
  }
  public Hashtable getGenericButtonsAuthorizations() {
    return genericButtonsAuthorizations;
  }
  public void setAuthorizations(Hashtable authorizations) {
    this.authorizations = authorizations;
  }
  public void setGenericButtonsAuthorizations(Hashtable genericButtonsAuthorizations) {
    this.genericButtonsAuthorizations = genericButtonsAuthorizations;
  }




}
