package org.openswing.swing.permissions.java;

import java.util.HashMap;
import java.io.Serializable;

/**
 * <p>Description: Inner class used to store authorizations for a single functionId and generic buttons.</p>
 * <p>Copyright: Copyright (C) 2006 Mauro Carniel</p>
 * <p> </p>
 * @author Mauro Carniel
 * @version 1.0
 */
public class GenericButtonAuthorization implements Serializable
{

  private HashMap otherButtons = new HashMap(); // collection of pairs <button id,abilitation>

  public GenericButtonAuthorization(String buttonId,boolean isEnabled) {
    this.otherButtons.put(buttonId,new Boolean(isEnabled));
  }


  public void addGenericButtonAuthorization(String buttonId,boolean isEnabled) {
    Boolean b = isEnabled(buttonId);
    if (b==null)
      this.otherButtons.put(buttonId,new Boolean(isEnabled));
    else
      this.otherButtons.put(buttonId,new Boolean(isEnabled || b.booleanValue()));
  }

  public final Boolean isEnabled(String buttonId) {
    return (Boolean) otherButtons.get(buttonId);
  }

}
