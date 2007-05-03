package org.openswing.swing.client;

import javax.swing.JTextField;
import javax.swing.UIManager;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.Container;
import org.openswing.swing.form.client.Form;
import java.awt.event.*;
import org.openswing.swing.message.receive.java.ValueObject;
import javax.swing.JComponent;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.awt.FlowLayout;


/**
 * <p>Title: OpenSwing Framework</p>
 * <p>Description: Text input control.</p>
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
public class TextControl extends BaseInputControl implements InputControl {

  /** maximum number of characters */
  private int maxCharacters = 255;

  /** flag used to translate the text on uppercase */
  private boolean upperCase = false;

  /** flag used to trim the text */
  private boolean trimText = false;

  /** flag used to right padding the text (related to maxCharacters property) */
  private boolean rpadding = false;

  /** text field */
  private TextBox textBox = new TextBox();


  /**
   * Constructor.
   */
  public TextControl() {
    this(10);
  }


  /**
   * Constructor.
   * @param columns number of visibile characters
   */
  public TextControl(int columns) {
    textBox.setColumns(columns);
    textBox.setDisabledTextColor(UIManager.getColor("TextField.foreground"));
    this.setLayout(new GridBagLayout());
    this.add(textBox, new GridBagConstraints(0, 0, 1, 1, 1.0, 1.0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));

//    setLayout(new FlowLayout(FlowLayout.LEFT, 0, 0));
//    this.add(textBox);

    addFocusListener();
    addKeyListener();
    initListeners();
  }


  /**
   * Create a key listener to correcly set the content of the control.
   */
  private void addKeyListener() {
    textBox.addKeyListener(new KeyAdapter() {

      public final void keyReleased(KeyEvent e) {
        e.consume();
      }

      public final void keyTyped(KeyEvent e) {
        if (textBox.getText()!=null &&
            textBox.getText().length()>=maxCharacters &&
            e.getKeyChar()!='\b') {
          TextControl.this.setText(textBox.getText().substring(0,maxCharacters));
          e.consume();
        }
        else if (textBox.getText()!=null &&
                 upperCase &&
                 e.getKeyChar()>=' ') {
          TextControl.this.setText((textBox.getText()+String.valueOf(e.getKeyChar())).toUpperCase());
          e.consume();
        }
      }

      public void keyPressed(KeyEvent e) { }

    });
  }


  /**
   * Set maximum number of characters.
   * @param maxCharacters maximum number of characters
   */
  public void setMaxCharacters(int maxCharacters) {
    this.maxCharacters = maxCharacters;
  }


  /**
   * @return maximum number of characters
   */
  public int getMaxCharacters() {
    return maxCharacters;
  }


  /**
   * @return <code>true></code> to right padding the text (related to maxCharacters property)
   */
  public boolean isRpadding() {
    return rpadding;
  }


  /**
   * @return <code>true></code> to trim the text
   */
  public boolean isTrimText() {
    return trimText;
  }


  /**
   * @return <code>true></code> to translate the text on uppercase
   */
  public boolean isUpperCase() {
    return upperCase;
  }


  /**
   * Set <code>true></code> to right padding the text (related to maxCharacters property).
   * @param rpadding <code>true></code> to right padding the text (related to maxCharacters property)
   */
  public void setRpadding(boolean rpadding) {
    this.rpadding = rpadding;
  }


  /**
   * Set <code>true></code> to trim the text.
   * @param trimText <code>true></code> to trim the text
   */
  public void setTrimText(boolean trimText) {
    this.trimText = trimText;
  }


  /**
   * Set <code>true></code> to translate the text on uppercase.
   * @param upperCase <code>true></code> to translate the text on uppercase
   */
  public void setUpperCase(boolean upperCase) {
    this.upperCase = upperCase;
  }


  /**
   * Create a focus listener to correcly set the content of the control.
   */
  private void addFocusListener() {
    textBox.addFocusListener(new FocusAdapter() {

      public void focusLost(FocusEvent e) {
        if (textBox.isEnabled() && textBox.isEditable()) {
          setText(textBox.getText());
        }
      }

    });
  }


  public final void setText(String text) {
    if (text==null)
      text = "";
    if (upperCase)
      text = text.toUpperCase();
    if (trimText)
      text = text.trim();
    if (rpadding) {
      StringBuffer auxtext = new StringBuffer(text);
      for(int i=text.length();i<maxCharacters;i++)
        auxtext.append(" ");
      text = auxtext.toString();
    }
    if (text.length()>maxCharacters)
      text = text.substring(0,maxCharacters);
    textBox.setText(text);
  }


  /**
   * Replace enabled setting with editable setting (this allow tab swithing).
   * @param enabled flag used to set abilitation of control
   */
  public final void setEnabled(boolean enabled) {
    textBox.setEditable(enabled);
    textBox.setFocusable(enabled);
  }


  /**
   * @return current input control abilitation
   */
  public final boolean isEnabled() {
    return textBox.isEditable();
  }


  /**
   * @return value related to the input control
   */
  public final Object getValue() {
    return textBox.getText();
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
    setText((String)value);
  }


  /**
   * @return component inside this whose contains the value
   */
  protected final JComponent getBindingComponent() {
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
      textBox.removeActionListener(listener);
    }
    catch (Exception ex) {
    }
  }


  public void processKeyEvent(KeyEvent e) {
    textBox.processKeyEvent(e);
  }


}


/**
 * <p>Title: OpenSwing Framework</p>
 * <p>Description: Inner class used to redirect key event to the inner JTextField.</p>
 * <p>Copyright: Copyright (C) 2006 Mauro Carniel</p>
 * @author Mauro Carniel
 * @version 1.0
 */
class TextBox extends JTextField {

    public void processKeyEvent(KeyEvent e) {
      super.processKeyEvent(e);
    }

}
