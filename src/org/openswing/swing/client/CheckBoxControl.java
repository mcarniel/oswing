package org.openswing.swing.client;

import org.openswing.swing.util.client.ClientSettings;
import javax.swing.JCheckBox;
import java.awt.Container;
import java.awt.Container;
import java.awt.event.ItemListener;
import java.awt.event.ItemEvent;
import org.openswing.swing.message.receive.java.ValueObject;
import java.util.ArrayList;
import java.awt.Color;
import java.awt.event.FocusListener;
import java.awt.event.FocusEvent;
import org.openswing.swing.form.model.client.ValueChangeListener;
import org.openswing.swing.form.model.client.ValueChangeEvent;
import org.openswing.swing.logger.client.Logger;
import javax.swing.UIManager;
import java.beans.Beans;


/**
 * <p>Title: OpenSwing Framework</p>
 * <p>Description: check-box whose text is translated according to internalization settings.</p>
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
public class CheckBoxControl extends JCheckBox implements InputControl {


  /** attribute name to which link the input control */
  private String attributeName = null;

  /** label used when showing error messages related to the input control (optional) */
  private LabelControl label = null;

  /** define if the input control is enabled on INSERT mode */
  private boolean enabledOnInsert = true;

  /** define if the input control is enabled on EDIT model */
  private boolean enabledOnEdit = true;

  /** define if the input control value is duplicated when user has clicked on COPY button */
  private boolean canCopy = false;

  /** old input control value */
  protected Object oldValue;

  /** value changed listener list */
  private ArrayList valueChangedListeners = new ArrayList();

  /** default background color of the binding component */
  private Color defaultBackgroundColor = null;

  /** define if the input control value is changed */
  private boolean changed = false;


  public CheckBoxControl() {
    setOpaque(false);
    initListeners();
  }


  /**
   * Method called by the sub-class to initialize focus listener used to push input control value in the VOModel.
   * @param component component inside this
   */
  protected void initListeners() {
    defaultBackgroundColor = getBackground();

    addFocusListener(new FocusListener() {

      public void focusGained(FocusEvent e) {
        controlFocusGained(e);
      }

      public void focusLost(FocusEvent e) {
        controlFocusLost(e);
      }

    });

    addItemListener(new ItemListener() {
        public void itemStateChanged(ItemEvent e) {
          if (e.getStateChange()==e.SELECTED && attributeName!=null) {
            maybeFireValueChangedEvent();
          }
        }
    });

  }


  /**
   * Add a value changed listener to this input control.
   * @param listener value changed listener to add
   */
  public final void addValueChangedListener(ValueChangeListener listener) {
    this.valueChangedListeners.add(listener);
  }


  /**
   * Remove a value changed listener from this input control.
   * @param listener value changed listener to remove
   */
  public final void removeValueChangedListener(ValueChangeListener listener) {
    this.valueChangedListeners.remove(listener);
  }


  /**
   * Method called by the sub-class to fire a value changed event.
   */
  protected void maybeFireValueChangedEvent() {
    // retrieve current value...
    Object newValue = getValue();

    if (isEnabled() &&
        ((this.oldValue==null && newValue!=null) ||
         (this.oldValue!=null && newValue==null) ||
         (this.oldValue!=null && newValue!=null && ! this.oldValue.equals(newValue)))) {

      // update old value...
      this.oldValue = newValue;

      // mark the input control as changed
      setChanged(true);

      // fire value changed events...
      ValueChangeEvent e = new ValueChangeEvent(this, attributeName, this.oldValue, newValue);
      for (int i = 0; i < this.valueChangedListeners.size(); i++)
        ((ValueChangeListener)this.valueChangedListeners.get(i)).valueChanged(e);
    }

  }


  /**
   * This method is called when the input control receives focus.
   */
  private void controlFocusGained(FocusEvent e) {
    oldValue = getValue();
    if (ClientSettings.VIEW_BACKGROUND_SEL_COLOR && isEnabled()) {
      UIManager.put("CheckBox.background", ClientSettings.BACKGROUND_SEL_COLOR);
      UIManager.put("CheckBoxMenuItem.selectionBackground", ClientSettings.BACKGROUND_SEL_COLOR);
      repaint();
    }
  }


  /**
   * This method is called when the input control losts focus.
   */
  private void controlFocusLost(FocusEvent e) {
    if (ClientSettings.VIEW_BACKGROUND_SEL_COLOR && isEnabled()) {
      UIManager.put("CheckBox.background", defaultBackgroundColor);
      UIManager.put("CheckBoxMenuItem.selectionBackground", defaultBackgroundColor);
      repaint();
    }
    maybeFireValueChangedEvent();
  }


  /**
   * Link the input control to the form which contains it and with the specified the attribute.
   * @param attributeName attribute name to which link the input control
   */
  public final void setAttributeName(String attributeName) {
    this.attributeName = attributeName;
    AttributeNameEditor.setColumnType(this.getClass());
  }


  /**
   * Link the input control to the form which contains it and with the specified the attribute.
   * @param attributeName attribute name to which link the input control
   */
  public final String getAttributeName() {
    AttributeNameEditor.setColumnType(this.getClass());
    return attributeName;
  }


  /**
   * @return mandatory property of the input control
   */
  public final boolean isRequired() {
    return false;
  }


  /**
   * Link the input control label to the specified label.
   * @param label label used when showing error messages related to the input control
   */
  public final void setLinkLabel(LabelControl label) {
    this.label = label;
  }


  /**
   * Link the input control to the form which contains it and with the specified the attribute.
   * @param attributeName attribute name to which link the input control
   */
  public final LabelControl getLinkLabel() {
    return label;
  }


  /**
   * @return <code>true</code> if the input control is enabled on INSERT mode, <code>false</code> otherwise
   */
  public final boolean isEnabledOnInsert() {
    return enabledOnInsert;
  }


  /**
   * Define if the input control is enabled on INSERT mode.
   * @param enabled <code>true</code> if the input control is enabled on INSERT mode, <code>false</code> otherwise
   */
  public final void setEnabledOnInsert(boolean enabled) {
    this.enabledOnInsert = enabled;
  }


  /**
   * @return <code>true</code> if the input control is enabled on EDIT mode, <code>false</code> otherwise
   */
  public final boolean isEnabledOnEdit() {
    return enabledOnEdit;
  }


  /**
   * Define if the input control is enabled on EDIT mode, <code>false</code> otherwise
   * @param enabled mandatory property of the input control
   */
  public final void setEnabledOnEdit(boolean enabled) {
    this.enabledOnEdit = enabled;
  }


  /**
   * @return define if the input control value is duplicated when user has clicked on COPY button
   */
  public final boolean isCanCopy() {
    return canCopy;
  }


  /**
   * Define if the input control value is duplicated when user has clicked on COPY button.
   * @param canCopy define if the input control value is duplicated when user has clicked on COPY button
   */
  public final void setCanCopy(boolean canCopy) {
    this.canCopy = canCopy;
  }



  /**
   * @return value related to the input control
   */
  public final Object getValue() {
    return new Boolean(this.isSelected());
  }


  /**
   * Set value to the input control.
   * @param value value to set into the input control
   */
  public final void setValue(Object value) {
    if (value==null)
      this.setSelected(false);
    else if (value instanceof Boolean)
      this.setSelected(((Boolean)value).booleanValue());
    else {
      Logger.error(this.getClass().getName(), "setValue", "Value is not of type Boolean: "+value.getClass().getName(),null);
    }
  }


  /**
   * @return <code>true</code> if the input control value is changed, <code>false</code> otherwise
   */
  public final boolean isChanged() {
    return changed;
  }


  /**
   * Define if the input control value is changed.
   * @param changed <code>true</code> if the input control value is changed, <code>false</code> otherwise
   */
  public final void setChanged(boolean changed) {
    this.changed = changed;
  }


  /**
   * Set abilitation setting.
   * @param enabled flag used to set abilitation of control
   */
  public final void setEnabled(boolean enabled) {
    super.setEnabled(enabled);
    setFocusable(enabled);
  }


  /**
   * Set check box label.
   * @param text check box label
   */
  public final void setText(String text) {
    if (Beans.isDesignTime())
      super.setText(text);
    else
      super.setText(ClientSettings.getInstance().getResources().getResource(text));
  }


}
