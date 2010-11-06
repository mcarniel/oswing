package org.openswing.swing.client;

import java.math.*;
import java.text.*;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

import org.openswing.swing.logger.client.*;
import org.openswing.swing.util.client.*;
import org.openswing.swing.domains.java.Domain;
import java.beans.Beans;
import java.util.ArrayList;
import org.openswing.swing.domains.java.DomainPair;
import javax.swing.event.ChangeListener;
import javax.swing.event.ChangeEvent;


/**
 * <p>Title: OpenSwing Framework</p>
 * <p>Description: Spinner number input control.</p>
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
public class SpinnerNumberControl extends BaseInputControl implements InputControl {

  /** number of visible characters; default value: 5 */
  private int columns = 5;

  /** horizontal alignment */
  private int horizontalAlignment = JTextField.RIGHT;

  /** background color when the input control is enabled */
  private Color enabledBackColor = new JTextField().getBackground();

  /** maximum value */
  private Double maxValue = new Double(Integer.MAX_VALUE);

  /** minimum value; default value: 0 */
  private Double minValue = new Double(0);

  /** initial value; default value: 0 */
  private Double initialValue = new Double(0);

  /** increment value; default value: 1 */
  private Double step = new Double(1);

  private SpinnerNumberModel model = new SpinnerNumberModel(initialValue,minValue,maxValue,step);
  private JSpinner spinner = new JSpinner(model);
  private JSpinner.NumberEditor ftf = getTextField(spinner);

  /** list of ChangeListener objects */
  private ArrayList changeListeners = new ArrayList();


  /**
   * Contructor.
   */
  public SpinnerNumberControl() {
    this.setLayout(new GridBagLayout());
    this.add(spinner, new GridBagConstraints(0, 0, 1, 1, 1.0, 1.0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));

    setOpaque(false);
    spinner.addKeyListener(new KeyAdapter() {
      public void keyPressed(KeyEvent e) {
        if (e.getKeyCode()==e.VK_CANCEL || e.getKeyCode()==e.VK_BACK_SPACE || e.getKeyCode()==e.VK_DELETE)
          spinner.setValue(null);
      }
    });
    setColumns(columns);
    setHorizontalAlignment(horizontalAlignment);
//    model.setMinimum(minValue);
//    model.setMaximum(maxValue);
//    model.setStepSize(step);
//    model.setValue(initialValue);
//    model = new SpinnerNumberModel(initialValue,minValue,maxValue,step);
//    spinner.setModel(model);

    initListeners();
  }



  /**
   * Return the formatted text field used by the editor, or
   * null if the editor doesn't descend from JSpinner.DefaultEditor.
   */
  public JSpinner.NumberEditor getTextField(JSpinner spinner) {
      JSpinner.NumberEditor editor = new JSpinner.NumberEditor(spinner, "#");
      spinner.setEditor(editor);
      return editor;
  }


  /**
   * Select the spinner item related to the specified code.
   * @param code used to retrieve the corresponding item and to select that item in the spinner
   */
  public final void setValue(Object code) {
    try {
      spinner.setValue(code);
    }
    catch (Exception ex) {
    }
  }


  /**
   * @return code related to the selected spinner item; it return null if no item is selected
   */
  public final Object getValue() {
    return spinner.getValue();
  }


  public void setEnabled(boolean enabled) {
    spinner.setEnabled(enabled);
    spinner.setFocusable(enabled || ClientSettings.DISABLED_INPUT_CONTROLS_FOCUSABLE);
    if (!enabled) {
      spinner.setBackground((Color)UIManager.get("TextField.inactiveBackground"));
      ftf.setBackground((Color)UIManager.get("TextField.inactiveBackground"));
      ftf.getTextField().setBackground((Color)UIManager.get("TextField.inactiveBackground"));
    }
    else {
      ftf.setBackground(enabledBackColor);
      ftf.getTextField().setBackground(enabledBackColor);
    }
  }


  /**
   * @return current input control abilitation
   */
  public final boolean isEnabled() {
    try {
      return spinner.isEnabled();
    }
    catch (Exception ex) {
      return false;
    }
  }


  /**
   * @return component inside this whose contains the value
   */
  public JComponent getBindingComponent() {
    return spinner;
  }


  /**
   * Adds the specified focus listener to receive focus events from
   * this component when this component gains input focus.
   * If listener <code>l</code> is <code>null</code>,
   * no exception is thrown and no action is performed.
   *
   * @param    l   the focus listener
   * @see      java.awt.event.FocusEvent
   * @see      java.awt.event.FocusListener
   * @see      #removeFocusListener
   * @see      #getFocusListeners
   * @since    JDK1.1
   */
  public final void addFocusListener(FocusListener l) {
     try {
        if(spinner != null) spinner.addFocusListener(l);
     }
     catch (Exception ex) {
     }
  }


  /**
   * Removes the specified focus listener so that it no longer
   * receives focus events from this component. This method performs
   * no function, nor does it throw an exception, if the listener
   * specified by the argument was not previously added to this component.
   * If listener <code>l</code> is <code>null</code>,
   * no exception is thrown and no action is performed.
   *
   * @param    l   the focus listener
   * @see      java.awt.event.FocusEvent
   * @see      java.awt.event.FocusListener
   * @see      #addFocusListener
   * @see      #getFocusListeners
   * @since    JDK1.1
   */
  public final void removeFocusListener(FocusListener l) {
    try {
      spinner.removeFocusListener(l);
    }
    catch (Exception ex) {
    }
  }


  /**
   * Adds a listener to the list that is notified each time a change
   * to the model occurs.  The source of <code>ChangeEvents</code>
   * delivered to <code>ChangeListeners</code> will be this
   * <code>JSpinner</code>.  Note also that replacing the model
   * will not affect listeners added directly to JSpinner.
   * Applications can add listeners to  the model directly.  In that
   * case is that the source of the event would be the
   * <code>SpinnerModel</code>.
   *
   * @param listener the <code>ChangeListener</code> to add
   * @see #removeChangeListener
   * @see #getModel
   */
  public void addChangeListener(ChangeListener listener) {
    try {
      changeListeners.add(listener);
      spinner.addChangeListener(listener);
    }
    catch (Exception ex) {
    }
  }



  /**
   * Removes a <code>ChangeListener</code> from this spinner.
   *
   * @param listener the <code>ChangeListener</code> to remove
   * @see #fireStateChanged
   * @see #addChangeListener
   */
  public void removeChangeListener(ChangeListener listener) {
    try {
      changeListeners.remove(listener);
      spinner.removeChangeListener(listener);
    }
    catch (Exception ex) {
    }
  }


  /**
   * @return spinner control
   */
  public final JComponent getComponent() {
    return spinner;
  }


  /**
   * @return <code>true</code> if the input control is in read only mode (so search is enabled), <code>false</code> otherwise
   */
  public final boolean isReadOnly() {
    return isEnabled();
  }


  /**
   * @return <code>true</code> to disable key listening on input control (for instance, in case of nested grids), <code>false</code> to listen for key events
   */
  public final boolean disableListener() {
    return false;
  }


  /**
   * Set the component orientation: from left to right or from right to left.
   * @param o component orientation
   */
  public final void setTextOrientation(ComponentOrientation o) {
    spinner.setComponentOrientation(o);
  }


  /**
   * @return component orientation
   */
  public final ComponentOrientation getTextOrientation() {
    try {
      return spinner.getComponentOrientation();
    }
    catch (Exception ex) {
      return null;
    }
  }


  /**
   * @return number of visible characters
   */
  public final int getColumns() {
    return columns;
  }


  /**
   * Set the number of visible characters.
   * @param columns number of visible characters
   */
  public final void setColumns(int columns) {
    this.columns = columns;
    ftf.getTextField().setColumns(columns);
  }


  /**
   * Set horizontal alignment.
   * @param horizontalAlignment horizontal alignment
   */
  public final void setHorizontalAlignment(int horizontalAlignment) {
    this.horizontalAlignment = horizontalAlignment;
    ftf.getTextField().setHorizontalAlignment(horizontalAlignment);
  }


  /**
   * @return horizontal alignment
   */
  public final int getHorizontalAlignment() {
    return horizontalAlignment;
  }


  /**
   * @return the object in the sequence that comes after the object returned by getValue()
   */
  public final Object getNextValue() {
    return spinner.getNextValue();
  }


  /**
   * @return the object in the sequence that comes before the object returned by getValue()
   */
  public final Object getPreviousValue() {
    return spinner.getPreviousValue();
  }


  /**
   * @return maximum value
   */
  public final Double getMaxValue() {
    return maxValue;
  }


  /**
   * Set maximum value.
   * @param maxValue maximum value
   */
  public final void setMaxValue(Double maxValue) {
    this.maxValue = maxValue;
    try {
      //model = new SpinnerNumberModel(initialValue,minValue,maxValue,step);
      model.setMaximum(maxValue);
      spinner.setModel(model);
    }
    catch (Exception ex) {
    }
  }


  /**
   * @return minimum value
   */
  public final Double getMinValue() {
    return minValue;
  }


  /**
   * Set minimum value.
   * @param minValue minimum value
   */
  public final void setMinValue(Double minValue) {
    this.minValue = minValue;
    try {
//      model = new SpinnerNumberModel(initialValue,minValue,maxValue,step);
      model.setMinimum(minValue);
      spinner.setModel(model);
    }
    catch (Exception ex) {
    }
  }


  /**
   * @return increment value; default value: 1
   */
  public final Double getStep() {
    return step;
  }


  /**
   * Set the increment value. Default value: 1
   * @param step increment value;
   */
  public final void setStep(Double step) {
    this.step = step;
    try {
      //model = new SpinnerNumberModel(initialValue,minValue,maxValue,step);
      model.setStepSize(step);
      spinner.setModel(model);
    }
    catch (Exception ex) {
    }
  }


  /**
   * @return initial value
   */
  public final Double getInitialValue() {
    return initialValue;
  }


  /**
   * Set the initial value; default value: 0
   * @param initialValue initial value
   */
  public final void setInitialValue(Double initialValue) {
    this.initialValue = initialValue;
    try {
      //model = new SpinnerNumberModel(initialValue,minValue,maxValue,step);
      model.setValue(initialValue);
      spinner.setModel(model);
    }
    catch (Exception ex) {
    }
  }


}
