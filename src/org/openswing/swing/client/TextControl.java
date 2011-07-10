package org.openswing.swing.client;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

import org.openswing.swing.util.client.*;


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
  protected JTextField textBox = getTextBox();


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

    StringBuffer s = new StringBuffer(textBox.getColumns()); for(int i=0;i<textBox.getColumns();i++) s.append("0");
    setMinimumSize(new Dimension(
      textBox.getFontMetrics(textBox.getFont()).stringWidth(s.toString()),
      textBox.getPreferredSize().height
    ));

//    setLayout(new FlowLayout(FlowLayout.LEFT, 0, 0));
//    this.add(textBox);

    if (ClientSettings.TEXT_ORIENTATION!=null)
        setComponentOrientation(ClientSettings.TEXT_ORIENTATION);

    addFocusListener();
    addKeyListener();
    initListeners();
  }


  /**
   * @return text box; this method is overrided by PasswordControl
   */
  protected JTextField getTextBox() {
    return new PlainTextField();
  }


  /**
   * Create a key listener to correcly set the content of the control.
   */
  private void addKeyListener() {
    textBox.addKeyListener(new KeyAdapter() {

      private boolean skip = false;
      private boolean consumeEvent = false;

      public final void keyReleased(KeyEvent e) {
        if (consumeEvent) {
          e.consume();
          return;
        }
        if (textBox.getText()!=null &&
            textBox.getText().length()>maxCharacters) {
          TextControl.this.setText(textBox.getText().substring(0,maxCharacters));
        }
        else if (upperCase && textBox.getText()!=null &&
                 !textBox.getText().equals(textBox.getText().toUpperCase())) {
          TextControl.this.setText(textBox.getText().toUpperCase());
        }
      }

      public final void keyTyped(KeyEvent e) {
        if (consumeEvent) {
          e.consume();
          return;
        }
        if (skip)
          return;
        if (textBox.getText()!=null &&
            textBox.getSelectedText()==null &&
            textBox.getText().length()>=maxCharacters) {
          e.consume();
        }
        else if (textBox.getText()!=null &&
                 textBox.getSelectedText()!=null &&
                 textBox.getText().length()-textBox.getSelectedText().length()>=maxCharacters) {
          e.consume();
        }
      }

      public void keyPressed(KeyEvent e) {
        skip = false;
        consumeEvent = false;

        if (!e.isAltDown() &&
            upperCase &&
            Character.isLetter(e.getKeyChar()) &&
            !String.valueOf(e.getKeyChar()).toUpperCase().equals(String.valueOf(e.getKeyChar()))) {
          processKeyEvent(new KeyEvent(
            (Component)e.getSource(),
            e.KEY_TYPED,
            e.getWhen(),
            e.getModifiers(),
            e.VK_UNDEFINED,
            String.valueOf(e.getKeyChar()).toUpperCase().charAt(0)
          ));
          consumeEvent = true;
          e.consume();
          return;
        }

        if (e.getKeyCode()==e.VK_BACK_SPACE)
          skip = true;
      }

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

    if (!textBox.isEditable()) {
      if ( (textBox.getHorizontalAlignment() == JTextField.LEADING) ||
           (textBox.getHorizontalAlignment() == JTextField.LEFT)) {
        textBox.setCaretPosition(0);
      }
    }

    textBox.setText(text);
  }


  /**
   * Replace enabled setting with editable setting (this allow tab swithing).
   * @param enabled flag used to set abilitation of control
   */
  public final void setEnabled(boolean enabled) {
    try {
      if (!enabled) {
        textBox.setForeground(UIManager.getColor("TextField.foreground"));
        textBox.setBackground(UIManager.getColor("TextField.inactiveBackground"));
      }
    }
    catch (Exception ex) {
    }
    textBox.setEditable(enabled);
    textBox.setFocusable(enabled || ClientSettings.DISABLED_INPUT_CONTROLS_FOCUSABLE);
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
    if (value!=null)
      value = value.toString();
    setText((String)value);
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

    StringBuffer s = new StringBuffer(textBox.getColumns()); for(int i=0;i<textBox.getColumns();i++) s.append("0");
    setMinimumSize(new Dimension(
      textBox.getFontMetrics(textBox.getFont()).stringWidth(s.toString()),
      textBox.getPreferredSize().height
    ));

  }


  /**
   * @return input control visibile characters
   */
  public final int getColumns() {
    return textBox.getColumns();
  }


  /**
   * Set the component orientation: from left to right or from right to left.
   * @param o component orientation
   */
  public final void setTextOrientation(ComponentOrientation o) {
    textBox.setComponentOrientation(o);
  }


  /**
   * @return component orientation
   */
  public final ComponentOrientation getTextOrientation() {
    try {
      return textBox.getComponentOrientation();
    }
    catch (Exception ex) {
      return null;
    }
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
   * Adds the specified key listener to receive
   * action events from this field.
   *
   * @param l the key listener to be added
   */
  public final void addKeyListener(KeyListener listener) {
    try {
      textBox.addKeyListener(listener);
    }
    catch (Exception ex) {
    }
  }



  /**
   * Removes the specified key listener so that it no longer
   * receives action events from this field.
   *
   * @param l the key listener to be removed
   */
  public final void removeKeyListener(KeyListener listener) {
    try {
      textBox.removeKeyListener(listener);
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
    try {
      ((OpenSwingTextField)textBox).processKeyEvent(e);
    }
    catch (Throwable ex) {
    }
  }


  /**
   * Selects the text between the specified start and end positions.
   * <p>
   * This method sets the start and end positions of the
   * selected text, enforcing the restriction that the start position
   * must be greater than or equal to zero.  The end position must be
   * greater than or equal to the start position, and less than or
   * equal to the length of the text component's text.
   * <p>
   * If the caller supplies values that are inconsistent or out of
   * bounds, the method enforces these constraints silently, and
   * without failure. Specifically, if the start position or end
   * position is greater than the length of the text, it is reset to
   * equal the text length. If the start position is less than zero,
   * it is reset to zero, and if the end position is less than the
   * start position, it is reset to the start position.
   * <p>
   * This call is provided for backward compatibility.
   * It is routed to a call to <code>setCaretPosition</code>
   * followed by a call to <code>moveCaretPosition</code>.
   * The preferred way to manage selection is by calling
   * those methods directly.
   *
   * @param selectionStart the start position of the text
   * @param selectionEnd the end position of the text
   * @see #setCaretPosition
   * @see #moveCaretPosition
   */
  public final void select(int selectionStart,int selectionEnd) {
    textBox.select(selectionStart,selectionEnd);
  }



  /**
   * @return text box; this method is overrided by PasswordControl
   */
  class PlainTextField extends JTextField implements OpenSwingTextField {

    public void processKeyEvent(KeyEvent e) {
      super.processKeyEvent(e);
    }

  }


}
