package org.openswing.swing.client;

import java.text.*;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.text.*;
import org.openswing.swing.util.client.ClientSettings;
import org.openswing.swing.logger.client.Logger;


/**
 * <p>Title: OpenSwing Framework</p>
 * <p>Description: Formatted text input control: it is a wrapper of JFormattedTextField.</p>
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
public class FormattedTextControl extends BaseInputControl implements InputControl {

  /** formatted text field */
  private FormattedTextBox textBox = new FormattedTextBox();

  /** formatter controller */
  private FormatterController controller;


  /**
   * Constructor.
   */
  public FormattedTextControl() {
    this(10);
  }


  /**
   * Constructor.
   * @param columns number of visibile characters
   */
  public FormattedTextControl(int columns) {
    try {
      textBox.setColumns(columns);
      textBox.setDisabledTextColor(UIManager.getColor("TextField.foreground"));
      this.setLayout(new GridBagLayout());
      this.add(textBox,
               new GridBagConstraints(0, 0, 1, 1, 1.0, 1.0,
                                      GridBagConstraints.WEST,
                                      GridBagConstraints.HORIZONTAL,
                                      new Insets(0, 0, 0, 0), 0, 0));

//    setLayout(new FlowLayout(FlowLayout.LEFT, 0, 0));
//    this.add(textBox);

      addFocusListener();
      initListeners();
    }
    catch (Exception ex) {
    }
  }


  /**
   * Create a focus listener to correcly set the content of the control.
   */
  private void addFocusListener() {
    if (textBox!=null)
      textBox.addFocusListener(new FocusAdapter() {

        public void focusLost(FocusEvent e) {
          if (textBox.isEnabled() && textBox.isEditable()) {
            if (textBox.getText()!=null && textBox.getText().length()>0) {
              setText(textBox.getText());
              setText(textBox.getText());
            }
            else
              setText(textBox.getText());
          }
        }

      });
  }


  public final void setText(String text) {
    try {
      if (text == null) {
        text = "";
      }
      textBox.setText(text);

      try {
        text = (String)textBox.getFormatter().stringToValue(text);

      }
      catch (ParseException ex1) {
        textBox.setValue(null);
      }
    }
    catch (Exception ex) {
    }
  }


  /**
   * Replace enabled setting with editable setting (this allow tab swithing).
   * @param enabled flag used to set abilitation of control
   */
  public final void setEnabled(boolean enabled) {
    if (textBox!=null) {
      textBox.setEditable(enabled);
      textBox.setFocusable(enabled || ClientSettings.DISABLED_INPUT_CONTROLS_FOCUSABLE);
      if (!enabled)
        getBindingComponent().setBackground((Color)UIManager.get("TextField.inactiveBackground"));
    }
  }


  /**
   * @return current input control abilitation
   */
  public final boolean isEnabled() {
    try {
      return textBox.isEditable();
    }
    catch (Exception ex) {
      return false;
    }
  }


  /**
   * @return value related to the input control
   */
  public final Object getValue() {
    return textBox.getValue();
  }


  /**
   * @return input control text
   */
  public final String getText() {
    return textBox.getText();
  }


  /**
   * Set value to the input control.
   * @param value value to set into the input control
   */
  public final void setValue(Object value) {
    try {
      if (value == null || value != null && value instanceof String) {
        setText(textBox.getFormatter().valueToString(value));
      }
      else {
        textBox.setValue(value);
      }
    }
    catch (Exception ex) {
      Logger.error(this.getClass().getName(),"setValue",ex.getMessage(),ex);
    }
  }


  /**
   * @return component inside this whose contains the value
   */
  public final JComponent getBindingComponent() {
    return textBox;
  }


  /**
   * Set input control visible characters.
   * @param columns visible characters
   */
  public final void setColumns(int columns) {
    textBox.setColumns(columns);
  }


  /**
   * @return input control visibile characters
   */
  public final int getColumns() {
    return textBox.getColumns();
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
  public final void addFocusListener(FocusListener listener) {
    try {
      if (textBox!=null)
        textBox.addFocusListener(listener);
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
  public final void removeFocusListener(FocusListener listener) {
    try {
      if (textBox!=null)
        textBox.removeFocusListener(listener);
    }
    catch (Exception ex) {
    }
  }


  /**
   * Adds the specified action listener to receive
   * action events from this textfield.
   *
   * @param l the action listener to be added
   */
  public final void addActionListener(ActionListener listener) {
    try {
      if (textBox!=null)
        textBox.addActionListener(listener);
    }
    catch (Exception ex) {
    }
  }


  /**
   * Removes the specified action listener so that it no longer
   * receives action events from this textfield.
   *
   * @param l the action listener to be removed
   */
  public final void removeActionListener(ActionListener listener) {
    try {
      if (textBox!=null)
        textBox.removeActionListener(listener);
    }
    catch (Exception ex) {
    }
  }


  public void processKeyEvent(KeyEvent e) {
    try {
      if (textBox != null) {
        textBox.processKeyEvent(e);
      }
    }
    catch (Exception ex) {
    }
  }










// Methods provided by the inner JFormattedTextField


  /**
   * Forces the current value to be taken from the AbstractFormatter and set as the current value.
   */
  public final void commitEdit() throws ParseException {
    try {
      if (textBox != null) {
        textBox.commitEdit();
      }
    }
    catch (Exception ex) {
    }
  }


  /**
   * @return fetches the command list for the editor
   */
  public final Action[] getActions() {
    try {
      if (textBox != null) {
        return textBox.getActions();
      }
      return new Action[0];
    }
    catch (Exception ex) {
      return null;
    }
  }


  /**
   * @return returns the behavior when focus is lost
   */
  public final int getFocusLostBehavior() {
    try {
      if (textBox != null) {
        return textBox.getFocusLostBehavior();
      }
      else {
        return JFormattedTextField.COMMIT_OR_REVERT;
      }
    }
    catch (Exception ex) {
      return JFormattedTextField.COMMIT_OR_REVERT;
    }
  }


  /**
   * @return returns the AbstractFormatter that is used to format and parse the current value
   */
  public final JFormattedTextField.AbstractFormatter getFormatter() {
    try {
      if (textBox != null) {
        return textBox.getFormatter();
      }
      else {
        return null;
      }
    }
    catch (Exception ex) {
      return null;
    }
  }


  /**
   * @return returns the current AbstractFormatterFactory
   */
  public final JFormattedTextField.AbstractFormatterFactory getFormatterFactory() {
    try {
      if (textBox != null) {
        return textBox.getFormatterFactory();
      }
      return null;
    }
    catch (Exception ex) {
      return null;
    }
  }


//  /**
//   * @return gets the class ID for a UI
//   */
//  public final String getUIClassID() {
//    try {
//      return textBox.getUIClassID();
//    }
//    catch (Exception ex) {
//      return null;
//    }
//  }


  /**
   * Invoked when the user inputs an invalid value.
   */
  public final void invalidEdit() {
    try {
      if (textBox != null) {
        textBox.invalidEdit();
      }
    }
    catch (Exception ex) {
    }
  }


  /**
   * @return returns <code>true</code> if the current value being edited is valid
   */
  public final boolean isEditValid() {
    try {
      if (textBox != null) {
        return textBox.isEditValid() || "".equals(textBox.getValue());
      }
      return false;
    }
    catch (Exception ex) {
      return false;
    }
  }


  /**
   * Associates the editor with a text document.
   * @param doc document linked to this text field
   */
  public final void setDocument(Document doc) {
    try {
      if (textBox != null) {
        textBox.setDocument(doc);
      }
    }
    catch (Exception ex) {
    }
  }


  /**
   * Sets the behavior when focus is lost.
   * @param behavior behavior to set
   */
  public void setFocusLostBehavior(int behavior) {
    try {
      if (textBox != null) {
        textBox.setFocusLostBehavior(behavior);
      }
    }
    catch (Exception ex) {
    }
  }


  /**
   * Sets the current AbstractFormatter.
   * @param format formatter to set
   */
  public final void setFormatter(JFormattedTextField.AbstractFormatter format) {
    try {
      if (textBox != null) {
        textBox.setFormatter(format);
      }
    }
    catch (Exception ex) {
    }
  }


  /**
   * Sets the AbstractFormatterFactory.
   * @param tf formatter factory
   */
  public final void setFormatterFactory(JFormattedTextField.AbstractFormatterFactory tf) {
    try {
      if (textBox != null) {
        textBox.setFormatterFactory(tf);
      }
    }
    catch (Exception ex) {
    }
  }


  /**
   * Set an input verifier.
   * @param verifier input verifier
   */
  public final void setInputVerifier(InputVerifier verifier) {
    try {
      if (textBox != null) {
        textBox.setInputVerifier(verifier);
      }
    }
    catch (Exception ex) {
    }
  }


  /**
   * @return formatter controller, used to override some JFormatterTextField protected methods
   */
  public final FormatterController getController() {
    return controller;
  }


  /**
   * Set the formatter controller, used to override some JFormatterTextField protected methods.
   * @param controller formatter controller
   */
  public final void setController(FormatterController controller) {
    this.controller = controller;
  }






  /**
   * <p>Title: OpenSwing Framework</p>
   * <p>Description: Inner class used to redirect key event to the inner JFormattedTextField.</p>
   * <p>Copyright: Copyright (C) 2006 Mauro Carniel</p>
   * @author Mauro Carniel
   * @version 1.0
   */
  class FormattedTextBox extends JFormattedTextField implements FormatterController{


//      public FormattedTextBox() {
//        super();
//        try {
//          setFormatter(new javax.swing.text.MaskFormatter("###"));
//        }
//        catch (ParseException ex) {
//        }
//      }

      public void processKeyEvent(KeyEvent e) {
        try {
          super.processKeyEvent(e);
        }
        catch (Exception ex) {
        }
      }


      /**
       * Invoked when the user inputs an invalid value.
       */
      public void invalidEdit() {
        try {
          if (controller == null) {
            super.invalidEdit();
          }
          else {
            controller.invalidEdit();
          }
        }
        catch (Exception ex) {
        }
      }


      /**
       * Sets the current AbstractFormatter.
       * @param format formatter to set
       */
      public void setFormatter(JFormattedTextField.AbstractFormatter format) {
        try {
          if (controller == null) {
            if (getFormatterFactory()==null)
              super.setFormatterFactory(new DefaultFormatterFactory(format));
            else
              super.setFormatter(format);

          }
          else {
            controller.setFormatter(format);
          }
        }
        catch (Exception ex) {
        }
      }


  }

}



