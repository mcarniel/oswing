package org.openswing.swing.client;

import javax.swing.*;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.Container;
import org.openswing.swing.form.client.Form;
import java.awt.event.*;
import java.awt.Color;
import java.awt.BorderLayout;
import org.openswing.swing.message.receive.java.ValueObject;
import java.awt.FlowLayout;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.Insets;


/**
 * <p>Title: OpenSwing Framework</p>
 * <p>Description: Text Area control.</p>
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
public class TextAreaControl extends BaseInputControl implements InputControl {

  /** maximum number of characters */
  private int maxCharacters = 255;

  /** used by addNotify method */
  private JScrollPane scrollPane = new JScrollPane();

  /** text editor */
  private JTextArea textArea = new JTextArea() {

    public void setEnabled(boolean enabled) {
      TextAreaControl.this.setEnabled(enabled);
    }

  };


  /**
   * Constructor.
   * @param columns number of visibile characters
   */
  public TextAreaControl() {
    scrollPane.getViewport().add(textArea,null);
    this.setLayout(new BorderLayout(0,0));
    this.add(scrollPane,BorderLayout.CENTER);
    textArea.setFont(UIManager.getFont("TextField.font"));

    addFocusListener();
    addKeyListener();
    initListeners();
  }


  /**
   * Create a key listener to correcly set the content of the control.
   */
  private void addKeyListener() {
    textArea.addKeyListener(new KeyAdapter() {

      public final void keyReleased(KeyEvent e) {
        e.consume();
      }

      public final void keyTyped(KeyEvent e) {
        if (textArea.getText()!=null &&
            textArea.getText().length()>=maxCharacters &&
            e.getKeyChar()!='\b') {
          textArea.setText(textArea.getText().substring(0,maxCharacters));
          e.consume();
        }
      }

      public void keyPressed(KeyEvent e) {
        if (e.getKeyCode()==e.VK_TAB) {
          e.consume();
          textArea.transferFocus();
        }
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
   * Create a focus listener to correcly set the content of the control.
   */
  private void addFocusListener() {
    textArea.addFocusListener(new FocusAdapter() {

      public void focusLost(FocusEvent e) {
        if (textArea.isEnabled() && textArea.isEditable()) {
          textArea.setText(textArea.getText());
        }
      }

    });
  }


  public final void setText(String text) {
    if (text==null)
      text = "";
    if (text.length()>maxCharacters)
      text = text.substring(0,maxCharacters);
    textArea.setText(text);
  }


  /**
   * Replace enabled setting with editable setting (this allow tab swithing).
   * @param enabled flag used to set abilitation of control
   */
  public void setEnabled(boolean enabled) {
    textArea.setEditable(enabled);
    textArea.setFocusable(enabled);
    if (enabled)
      textArea.setBackground((Color)UIManager.get("TextField.background"));
    else
      textArea.setBackground((Color)UIManager.get("TextField.inactiveBackground"));

  }


  /**
   * @return current input control abilitation
   */
  public final boolean isEnabled() {
    return textArea.isEditable();
  }


  /**
   * @return text editor
   */
  public final JTextArea getTextArea() {
    return textArea;
  }


  /**
   * @return component inside this whose contains the value
   */
  public JComponent getBindingComponent() {
    return textArea;
  }


  /**
   * @return value related to the input control
   */
  public Object getValue() {
    if (textArea.getText()!=null && textArea.getText().length()==0)
      return null;
    return textArea.getText();
  }


  /**
   * Set value to the input control.
   * @param value value to set into the input control
   */
  public void setValue(Object value) {
    textArea.setText((String)value);
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
      textArea.addFocusListener(listener);
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
      textArea.removeFocusListener(listener);
    }
    catch (Exception ex) {
    }
  }



}
