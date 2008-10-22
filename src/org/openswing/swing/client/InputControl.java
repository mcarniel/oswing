package org.openswing.swing.client;

import org.openswing.swing.form.model.client.*;
import java.awt.event.FocusListener;


/**
 * <p>Title: OpenSwing Framework</p>
 * <p>Description: Interface defined for each input control, used to specify attribute name and required properties.</p>
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
public interface InputControl {



  /**
   * Link the input control to the form which contains it and with the specified the attribute.
   * @param attributeName attribute name to which link the input control
   */
  public void setAttributeName(String attributeName);


  /**
   * Link the input control to the form which contains it and with the specified the attribute.
   * @param attributeName attribute name to which link the input control
   */
  public String getAttributeName();


  /**
   * Link the input control label to the specified label.
   * @param label label used when showing error messages related to the input control
   */
  public void setLinkLabel(LabelControl label);


  /**
   * @return label used when showing error messages related to the input control
   */
  public LabelControl getLinkLabel();


  /**
   * @return mandatory property of the input control
   */
  public boolean isRequired();


  /**
   * @return <code>true</code> if the input control is enabled on INSERT mode, <code>false</code> otherwise
   */
  public boolean isEnabledOnInsert();


  /**
   * Define if the input control is enabled on INSERT mode.
   * @param enabled <code>true</code> if the input control is enabled on INSERT mode, <code>false</code> otherwise
   */
  public void setEnabledOnInsert(boolean enabled);


  /**
   * @return <code>true</code> if the input control is enabled on EDIT mode, <code>false</code> otherwise
   */
  public boolean isEnabledOnEdit();


  /**
   * Define if the input control is enabled on EDIT mode, <code>false</code> otherwise
   * @param enabled mandatory property of the input control
   */
  public void setEnabledOnEdit(boolean enabled);



  /**
   * @return define if the input control value is duplicated when user has clicked on COPY button
   */
  public boolean isCanCopy();


  /**
   * Define if the input control value is duplicated when user has clicked on COPY button.
   * @param canCopy define if the input control value is duplicated when user has clicked on COPY button
   */
  public void setCanCopy(boolean canCopy);


  /**
   * @return value related to the input control
   */
  public Object getValue();


  /**
   * Set value to the input control.
   * @param value value to set into the input control
   */
  public void setValue(Object value);


  /**
   * @return <code>true</code> if the input control value is changed, <code>false</code> otherwise
   */
  public boolean isChanged();


  /**
   * Define if the input control value is changed.
   * @param changed <code>true</code> if the input control value is changed, <code>false</code> otherwise
   */
  public void setChanged(boolean changed);


  /**
   * Add a value changed listener to this input control.
   * @param listener value changed listener to add
   */
  public void addValueChangedListener(ValueChangeListener listener);


  /**
   * Remove a value changed listener from this input control.
   * @param listener value changed listener to remove
   */
  public void removeValueChangedListener(ValueChangeListener listener) ;


  /**
   * Set current input control abilitation.
   */
  public void setEnabled(boolean enabled);


  /**
   * @return current input control abilitation
   */
  public boolean isEnabled();


  /**
   * @return ValueChangeListener listeners added to this input control
   */
  public ValueChangeListener[] getValueChangeListeners();


  /**
   * Add a FocusListener to this input control.
   * @param listener FocusListener to add
   */
  public void addFocusListener(FocusListener listener);


  /**
   * Remove a FocusListener listener from this input control.
   * @param listener FocusListener to remove
   */
  public void removeFocusListener(FocusListener listener) ;


}
