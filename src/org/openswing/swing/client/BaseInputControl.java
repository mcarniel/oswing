package org.openswing.swing.client;

import java.beans.*;
import java.util.*;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

import org.openswing.swing.form.model.client.*;
import org.openswing.swing.logger.client.*;
import org.openswing.swing.util.client.*;
import com.toedter.calendar.*;
import javax.swing.text.JTextComponent;


/**
 * <p>Title: OpenSwing Framework</p>
 * <p>Description: Class that could be used as bease class for an input control.</p>
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
public class BaseInputControl extends JPanel implements InputControl {

  /** attribute name that identifies the input control */
  private String attributeName = null;

  /** mandatory property of the input control */
  private boolean required = false;

  /** define if the input control is enabled on INSERT mode */
  private boolean enabledOnInsert = true;

  /** define if the input control is enabled on EDIT mode */
  private boolean enabledOnEdit = true;

  /** define if the input control value is duplicated when user has clicked on COPY button */
  private boolean canCopy = true;

  /** define if the input control value is changed */
  private boolean changed = false;

  /** label used when showing error messages related to the input control */
  private LabelControl linkedLabel = null;

  /** mandatory symbol */
  private JLabel requiredIcon = new JLabel("*");

  /** old input control value */
  protected Object oldValue;

  /** value changed listener list */
  private ArrayList valueChangedListeners = new ArrayList();

  /** default background color of the binding component */
  protected Color defaultBackgroundColor = null;

  /** tooltip text */
  private String toolTipText = null;

  /** column text horizontal alignment */
  private int textAlignment = SwingConstants.LEFT;

  /** popup menu */
  private InputControlPopupMenu inputControlPopupMenu;


  /**
   * Constructor.
   */
  public BaseInputControl() {
    this.setOpaque(false);
    addFocusListener(new FocusListener() {
      public void focusGained(FocusEvent e) {
        if (!getBindingComponent().hasFocus())
          // set focus to the component inside this...
          getBindingComponent().requestFocus();
      }
      public void focusLost(FocusEvent e) {
      }
    });

    if (Beans.isDesignTime()) {
      requiredIcon.setForeground(Color.red);
      requiredIcon.setFont(new Font(requiredIcon.getFont().getFontName(),Font.PLAIN,20));
      requiredIcon.setPreferredSize(new Dimension(15, 15));
      requiredIcon.setFocusable(false);
    }
    else {
      requiredIcon.setText("");
      requiredIcon.setIcon(new ImageIcon(ClientUtils.getImage("obl.gif",getClass())));
    }

  }


  /**
   * Method called by the sub-class to initialize focus listener used to push input control value in the VOModel.
   * @param component component inside this
   */
  protected void initListeners() {
    defaultBackgroundColor = getBindingComponent().getBackground();

    getBindingComponent().addFocusListener(new FocusListener() {

      public void focusGained(FocusEvent e) {
        controlFocusGained(e);
      }

      public void focusLost(FocusEvent e) {
        controlFocusLost(e);
      }

    });

    if (getBindingComponent() instanceof ItemSelectable) {
      ((ItemSelectable)getBindingComponent()).addItemListener(new ItemListener() {
        public void itemStateChanged(ItemEvent e) {
          if (e.getStateChange()==e.SELECTED && attributeName!=null) {
            maybeFireValueChangedEvent();
          }
        }
      });
    }

    inputControlPopupMenu = new InputControlPopupMenu(this);

    ClientUtils.addTabListener(getBindingComponent());
  }


  /**
   * @return component inside this whose contains the value
   */
  public JComponent getBindingComponent() {
    Logger.error(this.getClass().getName(), "getBindingComponent", "This method must be overridden in '"+this.getClass().getName()+"'",null);
    return null;
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

      // mark the input control as changed
      setChanged(true);

      // fire value changed events...
      ValueChangeEvent e = new ValueChangeEvent(this, attributeName, this.oldValue, newValue);
      for (int i = 0; i < this.valueChangedListeners.size(); i++)
        ((ValueChangeListener)this.valueChangedListeners.get(i)).valueChanged(e);

      // update old value...
      this.oldValue = newValue;
    }

  }


  /**
   * This method is called when the input control receives focus.
   */
  private void controlFocusGained(FocusEvent e) {
    if (this instanceof DateControl)
      maybeFireValueChangedEvent();
    oldValue = getValue();
    if (ClientSettings.VIEW_BACKGROUND_SEL_COLOR && isEnabled()) {
      getBindingComponent().setBackground(ClientSettings.BACKGROUND_SEL_COLOR);
    }

    if (ClientSettings.SELECT_DATA_IN_EDITABLE_FORM &&
        isEnabled() &&
        getBindingComponent() instanceof JTextComponent)
      ((JTextComponent)getBindingComponent()).select(0,((JTextComponent)getBindingComponent()).getText().length());

  }


  /**
   * This method is called when the input control losts focus.
   */
  private void controlFocusLost(FocusEvent e) {
    if (ClientSettings.VIEW_BACKGROUND_SEL_COLOR && isEnabled()) {
      getBindingComponent().setBackground(defaultBackgroundColor);
    }
    maybeFireValueChangedEvent();
  }


  /**
   * Link the input control label to the specified label.
   * @param linkedLabel label used when showing error messages related to the input control
   */
  public final void setLinkLabel(LabelControl linkedLabel) {
    this.linkedLabel = linkedLabel;
  }


  /**
   * @return label used when showing error messages related to the input control
   */
  public final LabelControl getLinkLabel() {
    return linkedLabel;
  }


  /**
   * @return mandatory property of the input control
   */
  public final boolean isRequired() {
    return required;
  }


  /**
   * @return current Font setting
   */
  public final Font getFont() {
    return getBindingComponent()==null?null:getBindingComponent().getFont();
  }


  /**
   * Set the specified font.
   * @param font Font to set
   */
  public final void setFont(Font font) {
    if (getBindingComponent()!=null)
      getBindingComponent().setFont(font);
  }


  /**
   * Set a tooltip text. This text will be translated according to the internationalization settings.
   * @param toolTipText tool tip text entry in the dictionary
   */
  public final void setToolTipText(String toolTipText) {
    this.toolTipText = toolTipText;
    if (!Beans.isDesignTime())
      getBindingComponent().setToolTipText(ClientSettings.getInstance().getResources().getResource(toolTipText));
  }


  /**
   * @return tool tip text entry in the dictionary
   */
  public final String getToolTipText() {
    return toolTipText;
  }


  /**
   * Define mandatory property of the input control.
   * @param require mandatory property of the input control
   */
  public final void setRequired(boolean required) {
    if (!this.required && required && ClientSettings.VIEW_MANDATORY_SYMBOL) {
      if (this.getLayout() instanceof FlowLayout)
        this.add(requiredIcon);
      else if (this.getLayout() instanceof BorderLayout)
        this.add(requiredIcon,BorderLayout.EAST);
      else if (this.getLayout() instanceof GridBagLayout)
        this.add(requiredIcon, new GridBagConstraints(4, 0, 1, 1, 0.0, 1.0, GridBagConstraints.NORTHWEST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 5, 0));

      this.revalidate();
      this.repaint();
    } else if (this.required && !required && ClientSettings.VIEW_MANDATORY_SYMBOL) {
      this.remove(requiredIcon);
    }
    this.required = required;
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
   * Link the input control to the form which contains it and with the specified the attribute.
   * @param attributeName attribute name to which link the input control
   */
  public final void setAttributeName(String attributeName) {
    this.attributeName = attributeName;
//    AttributeNameEditor.setColumnType(this.getClass());
  }


  /**
   * Link the input control to the form which contains it and with the specified the attribute.
   * @param attributeName attribute name to which link the input control
   */
  public final String getAttributeName() {
    return attributeName;
  }


  /**
   * Set current input control abilitation.
   */
  public void setEnabled(boolean enabled) {
    Logger.error(this.getClass().getName(), "setEnabled", "This method must be overridden in '"+this.getClass().getName()+"'",null);
  }


  /**
   * @return current input control abilitation
   */
  public boolean isEnabled() {
    try {
      Logger.error(this.getClass().getName(), "isEnabled",
                   "This method must be overridden in '" +
                   this.getClass().getName() + "'", null);
      return false;
    }
    catch (Exception ex) {
      return false;
    }
  }


  /**
   * @return value related to the input control
   */
  public Object getValue() {
    Logger.error(this.getClass().getName(), "getValue", "This method must be overridden in '"+this.getClass().getName()+"'",null);
    return null;
  }


  /**
   * Set value to the input control.
   * @param value value to set into the input control
   */
  public void setValue(Object value) {
    Logger.error(this.getClass().getName(), "setValue", "This method must be overridden in '"+this.getClass().getName()+"'",null);
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
   * @return value changed listeners list
   */
  public final ValueChangeListener[] getValueChangeListeners() {
    return (ValueChangeListener[])valueChangedListeners.toArray(new ValueChangeListener[valueChangedListeners.size()]);
  }


  public void requestFocus() {
    getBindingComponent().requestFocus();
  }


  /**
   * @return column text horizontal alignment
   */
  public final int getTextAlignment() {
    return this.textAlignment;
  }


  /**
   * Set column text horizontal alignement.
   * @param alignment column text horizontal alignement; possible values: "SwingConstants.LEFT", "SwingConstants.RIGHT", "SwingConstants.CENTER".
   */
  public final void setTextAlignment(int alignment) {
    this.textAlignment = alignment;
    if (!Beans.isDesignTime() && getBindingComponent()!=null && getBindingComponent() instanceof JTextField)
      ((JTextField)getBindingComponent()).setHorizontalAlignment(alignment);
  }



}
