package org.openswing.swing.client;

import java.beans.*;
import java.util.*;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;
import javax.swing.plaf.*;
import javax.swing.plaf.basic.*;

import org.openswing.swing.form.model.client.*;
import org.openswing.swing.logger.client.*;
import org.openswing.swing.util.client.*;


/**
 * <p>Title: OpenSwing Framework</p>
 * <p>Description: check-box whose text is translated according to internalization settings.
 * As default behavior, check-box supports two states: Boolean.TRUE (selected) and Boolean.FALSE (deselected).
 * Optionally (when "allowNullValue" property has been set to <code>true</code>), check-box supports three states: Boolean.TRUE (selected) and Boolean.FALSE (deselected) and null (not selected and gray).</p>
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

  /** tooltip text */
  private String toolTipText = null;

  /** define if null value is alloed (i.e. distinct from Boolean.FALSE value); default value: <code>false</code> */
  private boolean allowNullValue = false;

  /** ButtonModel associated to the check-box when "allowNullValue" property is set to <code>true</code> */
  private CheckBoxButtonModel model = null;


  public CheckBoxControl() {
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
          if (e.getStateChange()==e.SELECTED && attributeName!=null) {
            maybeFireValueChangedEvent();
          }
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
    if (allowNullValue) {
      return model.getCurrentValue();
    }
    else
      return new Boolean(this.isSelected());
  }


  /**
   * Set value to the input control.
   * @param value value to set into the input control
   */
  public final void setValue(Object value) {
    if (allowNullValue) {
      try {
        model.setCurrentValue( (Boolean) value);
      }
      catch (ClassCastException ex) {
        Logger.error(this.getClass().getName(), "setValue", "Value is not of type Boolean: "+value.getClass().getName(),null);
      }
    }
    else {
      if (value==null)
        this.setSelected(false);
      else if (value instanceof Boolean)
        this.setSelected(((Boolean)value).booleanValue());
      else {
        Logger.error(this.getClass().getName(), "setValue", "Value is not of type Boolean: "+value.getClass().getName(),null);
      }
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
    setFocusable(enabled || ClientSettings.DISABLED_INPUT_CONTROLS_FOCUSABLE);
    try {
      UIManager.put("CheckBox.background", (Color)UIManager.getColor("TextField.inactiveBackground"));
      UIManager.put("CheckBoxMenuItem.selectionBackground", (Color)UIManager.getColor("TextField.inactiveBackground"));
    }
    catch (Exception ex) {
    }
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


  /**
   * @return define if null value is alloed (i.e. distinct from Boolean.FALSE value)
   */
  public final boolean isAllowNullValue() {
    return allowNullValue;
  }


  /**
   * Define if null value is alloed (i.e. distinct from Boolean.FALSE value)
   * @param allowNullValue define if null value is alloed (i.e. distinct from Boolean.FALSE value)
   */
  public final void setAllowNullValue(boolean allowNullValue) {
    this.allowNullValue = allowNullValue;

    if (allowNullValue) {
      MouseListener[] ll = this.getMouseListeners();
      for(int i=0;i<ll.length;i++)
        if (ll[i] instanceof BasicButtonListener)
          this.removeMouseListener(ll[i]);

      super.addMouseListener(new MouseAdapter() {
          public void mousePressed(MouseEvent e) {
            if (!CheckBoxControl.this.isEnabled())
                return;
            grabFocus();
            if (model.getCurrentValue()==null)
              model.setCurrentValue(Boolean.FALSE);
            else if (Boolean.TRUE.equals(model.getCurrentValue()))
              model.setCurrentValue(null);
            else if (Boolean.FALSE.equals(model.getCurrentValue()))
              model.setCurrentValue(Boolean.TRUE);
          }
      });
      ActionMap map = new ActionMapUIResource();
      map.put("pressed", new AbstractAction() {
          public void actionPerformed(ActionEvent e) {
              grabFocus();
              if (model.getCurrentValue()==null)
                model.setCurrentValue(Boolean.FALSE);
              else if (Boolean.TRUE.equals(model.getCurrentValue()))
                model.setCurrentValue(null);
              else if (Boolean.FALSE.equals(model.getCurrentValue()))
                model.setCurrentValue(Boolean.TRUE);
          }
      });
      map.put("released", null);
      SwingUtilities.replaceUIActionMap(this, map);
      model = new CheckBoxButtonModel(getModel());
      setModel(model);
    }
  }


  public final void setSelected(boolean selected) {
    if (allowNullValue) {
      model.setCurrentValue(new Boolean(selected));
    }
    else
      super.setSelected(selected);
  }


  /**
   * @return value changed listeners list
   */
  public final ValueChangeListener[] getValueChangeListeners() {
    return (ValueChangeListener[])valueChangedListeners.toArray(new ValueChangeListener[valueChangedListeners.size()]);
  }


  /**
   * <p>Title: OpenSwing Framework</p>
   * <p>Description: Inner class used to support three states: Boolean.TRUE, Boolean.FALSE and null</p>
   * <p>Copyright: Copyright (C) 2006 Mauro Carniel</p>
   * @author Mauro Carniel
   * @version 1.0
   */
  class CheckBoxButtonModel implements ButtonModel {

      private final ButtonModel model;


      public CheckBoxButtonModel(ButtonModel model) {
          this.model = model;
      }


      public void setCurrentValue(Boolean value) {
        if (value==null) {
          model.setArmed(true);
          setPressed(true);
          setSelected(false);
        } else if (Boolean.FALSE.equals(value)) {
          model.setArmed(false);
          setPressed(false);
          setSelected(false);
        }
        else if (Boolean.TRUE.equals(value)) {
          model.setArmed(false);
          setPressed(false);
          setSelected(true);
        }
      }


      public Boolean getCurrentValue() {
        if (isSelected()) {
          return Boolean.TRUE;
        }
        else if (!isSelected() && isArmed()) {
          return null;
        }
        else {
          return Boolean.FALSE;
        }
      }


      public final void setEnabled(boolean b) {
        setFocusable(b);
        model.setEnabled(b);
      }

      public boolean isSelected() {
        return model.isSelected();
      }

      public boolean isEnabled() {
        try {
          return model.isEnabled();
        }
        catch (Exception ex) {
          return false;
        }
      }

      public boolean isPressed() {
        return model.isPressed();
      }

      public boolean isRollover() {
        return model.isRollover();
      }

      public void setSelected(boolean selected) {
        model.setSelected(selected);
      }

      public void setPressed(boolean pressed) {
        model.setPressed(pressed);
      }

      public void setRollover(boolean rollover) {
        model.setRollover(rollover);
      }

      public void setMnemonic(int mnemonic) {
        model.setMnemonic(mnemonic);
      }

      public int getMnemonic() {
        return model.getMnemonic();
      }

      public void setActionCommand(String command) {
        model.setActionCommand(command);
      }

      public String getActionCommand() {
        return model.getActionCommand();
      }

      public void setGroup(ButtonGroup group) {
        model.setGroup(group);
      }

      public void addActionListener(ActionListener listener) {
        model.addActionListener(listener);
      }

      public void removeActionListener(ActionListener listener) {
        model.removeActionListener(listener);
      }

      public void addItemListener(ItemListener listener) {
        model.addItemListener(listener);
      }

      public void removeItemListener(ItemListener listener) {
        model.removeItemListener(listener);
      }

      public void addChangeListener(ChangeListener listener) {
        model.addChangeListener(listener);
      }

      public void removeChangeListener(ChangeListener listener) {
        model.removeChangeListener(listener);
      }

      public Object[] getSelectedObjects() {
        return model.getSelectedObjects();
      }

      public void setArmed(boolean armed) { }

      public boolean isArmed() {
        return model.isArmed();
      }

  }


}
