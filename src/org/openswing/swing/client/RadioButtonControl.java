package org.openswing.swing.client;

import java.beans.*;
import java.util.*;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

import org.openswing.swing.form.model.client.*;
import org.openswing.swing.util.client.*;


/**
 * <p>Title: OpenSwing Framework</p>
 * <p>Description: radio button whose text is translated according to internalization settings.</p>
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
public class RadioButtonControl extends JRadioButton implements InputControl {


  /** attribute name to which link the input control */
  private String attributeName = null;

  /** flag use to define mandatory property of the input control */
  private boolean required = false;

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

  /** value that will be setted by the radio button when it's selected */
  private Object selectedValue = null;

  /** button group linked to this radio button */
  private ButtonGroup buttonGroup = null;

  /** tooltip text */
  private String toolTipText = null;


  public RadioButtonControl() {
    setOpaque(false);
    initListeners();
  }


  /**
   * @return current Font setting
   */
  public final Font getFont() {
    return super.getFont();
  }


  /**
   * Set the specified font.
   * @param font Font to set
   */
  public final void setFont(Font font) {
    super.setFont(font);
  }


  /**
   * Set a tooltip text. This text will be translated according to the internationalization settings.
   * @param toolTipText tool tip text entry in the dictionary
   */
  public final void setToolTipText(String toolTipText) {
    this.toolTipText = toolTipText;
    if (!Beans.isDesignTime())
      super.setToolTipText(ClientSettings.getInstance().getResources().getResource(toolTipText));
  }


  /**
   * @return tool tip text entry in the dictionary
   */
  public final String getToolTipText() {
    return toolTipText;
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
          maybeFireValueChangedEvent();
        }
    });

    ClientUtils.addTabListener(this);
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
   * @return value changed listeners list
   */
  public final ValueChangeListener[] getValueChangeListeners() {
    return (ValueChangeListener[])valueChangedListeners.toArray(new ValueChangeListener[valueChangedListeners.size()]);
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
  }


  /**
   * Link the input control to the form which contains it and with the specified the attribute.
   * @param attributeName attribute name to which link the input control
   */
  public final String getAttributeName() {
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
  public void setLinkLabel(LabelControl label) {
    this.label = label;
  }


  /**
   * Link the input control to the form which contains it and with the specified the attribute.
   * @param attributeName attribute name to which link the input control
   */
  public LabelControl getLinkLabel() {
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
    if (this.isSelected())
      return selectedValue;
    else {
      Enumeration en = buttonGroup.getElements();
      RadioButtonControl comp = null;
      while(en.hasMoreElements()) {
        comp = (RadioButtonControl)en.nextElement();
        if (comp.isSelected())
          return comp.getSelectedValue();
      }
      return null;
    }
  }


  /**
   * Set value to the input control.
   * @param value value to set into the input control
   */
  public final void setValue(Object value) {
    if (value==null)
      this.setSelected(false);
    else if (value.equals(selectedValue))
      this.setSelected(true);
    else
      this.setSelected(false);
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
    setFocusable(enabled || ClientSettings.DISABLED_INPUT_CONTROLS_FOCUSABLE);
  }


  /**
   * @return value that will be setted by the radio button when it's selected
   */
  public final Object getSelectedValue() {
    return selectedValue;
  }


  /**
   * Set the value that will be setted by the radio button when it's selected.
   * @param selectedValue value that will be setted by the radio button when it's selected
   */
  public final void setSelectedValue(Object selectedValue) {
    this.selectedValue = selectedValue;
  }


  /**
   * @return button group linked to this radio button
   */
  public final ButtonGroup getButtonGroup() {
    return buttonGroup;
  }


  /**
   * Set button group linked to this radio button.
   * @param buttonGroup button group linked to this radio button
   */
  public final void setButtonGroup(ButtonGroup buttonGroup) {
    this.buttonGroup = buttonGroup;
    buttonGroup.add(this);
  }


  /**
   * Set radio button label.
   * @param text radio button label
   */
  public final void setText(String text) {
    if (Beans.isDesignTime())
      super.setText(text);
    else
      super.setText(ClientSettings.getInstance().getResources().getResource(text));
  }


}
