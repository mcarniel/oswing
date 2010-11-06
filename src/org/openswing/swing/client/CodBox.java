package org.openswing.swing.client;

import java.beans.*;
import java.math.*;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

import org.openswing.swing.lookup.client.*;
import org.openswing.swing.util.client.ClientSettings;

/**
 * <p>Title: OpenSwing Framework</p>
 * <p>Description: Input Control used to digit a code. A code can be an alphanumeric or a numeric value.
 * When focus is lost from this control, a code validation is executed (only if the specified code is changed).
 * </p>
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
public class CodBox extends JTextField {

  /** maximum code length */
  private int maxCharacters = 1;

  /** previous code value; initial value = "" */
  private String oldValue = "";

  /** code container (code controller) */
  private CodBoxContainer container = null;

  /** define if the cod box allows numeric values only */
  private boolean allowOnlyNumbers = false;


  public CodBox() {

    if (Beans.isDesignTime())
      return;

    this.addKeyListener(new KeyAdapter() {

      public void keyPressed(KeyEvent e) {
        if (e.getKeyCode()==e.VK_TAB && ClientSettings.FORCE_FOCUS_ON_LOOKUP_CONTROL) {
          CodBox.this.transferFocus();
          if (CodBox.this.getText().trim().length()>0 &&
              container instanceof CodLookupControl)
            // cod box is empty: focus is automatically setted to the component next to lookup button
            SwingUtilities.invokeLater(new Runnable() {
              public void run() {
                FocusManager.getCurrentManager().getFocusOwner().transferFocus();
              }
            });
        }
      }


      public void keyTyped(KeyEvent e) {
        if (e.getKeyChar()=='\n' &&
            !(ClientSettings.AS_TAB!=null && ClientSettings.AS_TAB.getKeyChar()=='\n')) {
          if (isEnabled() && isEditable())
            // start validation when press ENTER button...
            startValidation();
        }
        else if (e.getKeyChar()=='\b')
          return;
        if (allowOnlyNumbers && (e.getKeyChar()<'0' || e.getKeyChar()>'9'))
          e.consume();
        else if ((getSelectedText()==null || getSelectedText().length()==0) &&
                 (getText().length()>=maxCharacters))
          e.consume();
        else if (getText()!=null &&
                 getSelectedText()!=null &&
                 getText().length()-getSelectedText().length()>=maxCharacters)
          e.consume();

      }
    });
    this.addFocusListener(new FocusAdapter() {
      public void focusLost(FocusEvent e) {
        if (isEnabled() && isEditable())
          // start validation on lost focus...
          startValidation();
      }
      public void focusGained(FocusEvent e) {
//        oldValue = getText();
      }
    });


    StringBuffer s = new StringBuffer(getColumns()); for(int i=0;i<getColumns();i++) s.append("W");
    int w = getFontMetrics(getFont()).stringWidth(s.toString())+10;
    int h = getPreferredSize().height;
    setMinimumSize(new Dimension(w,h));
    setPreferredSize(new Dimension(w,h));


    if (ClientSettings.FORCE_FOCUS_ON_LOOKUP_CONTROL)
      // disable transfer focus management: in this way TAB event will be listened...
      setFocusTraversalKeysEnabled(false);
  }


  /**
   * This method executes the code validation.
   */
  private void startValidation() {
    // the code is trimmed...
    super.setText(getText().trim().toUpperCase());

    // code validation is executed ONLY if:
    // - the specified code value is not equals to the previous code value and
    // - there exists a code container (code controller)
    if (container!=null && (
        oldValue!=null && !oldValue.equals(getText()) ||
        oldValue==null && getText()!=null
        )) {
      oldValue = getText();
      try {
        container.validateCode(getText());
        oldValue = getText();
      }
      catch (RestoreFocusOnInvalidCodeException ex) {
        oldValue = "";
        SwingUtilities.invokeLater(new Runnable() {
          public void run() {
            if (!CodBox.this.hasFocus())
              CodBox.this.requestFocus();
          }
        });
      }
      catch (Exception ex) {
        ex.printStackTrace();
      }
    }

  }


  public void setEnabled(boolean enabled) {
    setEditable(enabled);
    if (this.container!=null)
      this.container.setEnabled(enabled);
  }


  public final void setEditable(boolean enabled) {
    super.setEditable(enabled);
    try {
      if (enabled) {
        setForeground(UIManager.getColor("TextField.foreground"));
        setBackground(UIManager.getColor("TextField.background"));
      }
      else {
        setForeground(UIManager.getColor("TextField.foreground"));
        setBackground(UIManager.getColor("TextField.inactiveBackground"));
      }
    }
    catch (Exception ex) {
    }
  }




  /**
   * @return maximum code length
   */
  public final int getMaxCharacters() {
    return maxCharacters;
  }


  /**
   * Set maximum code length.
   * @param maxCharacters maximum code length
   */
  public final void setMaxCharacters(int maxCharacters) {
    this.maxCharacters = maxCharacters;
  }


  /**
   * Set code container.
   * @param container code container
   */
  public final void setContainer(CodBoxContainer container) {
    this.container = container;
  }

  /**
   * @return code value; null when no code is specified
   */
  public final Object getValue() {
    if (getText().length()==0)
      return null;
    else if (allowOnlyNumbers)
      return new BigDecimal(getText());
    else
      return getText();
  }


  /**
   * Set code value.
   * @param value code value
   */
  public final void setValue(Object value) {
    super.setText(value==null?null:value.toString());
    oldValue = value==null?null:value.toString();
  }


  /**
   * Set code value (like setValue method).
   * @param text code value
   */
  public final void setText(String text) {
    setValue(text);
  }


  /**
   * Method called by code container when the code validation is completed.
   * @param validated validation result
   */
  public final void codeValidated(boolean validated) {
    if (!validated) {
      super.setText("");
      requestFocus();
    }
    else if (getText().length()>0) {
      oldValue = getText();
    }
  }


  /**
   * Force validation.
   * This method can be called by the developer to force a code validation, without losting focus.
   */
  public void forceValidate() {
    try {
      oldValue = null;
      startValidation();
    }
    catch (Exception ex) {
      ex.printStackTrace();
    }
  }


  /**
   * @return define if the cod box allows numeric values only
   */
  public final boolean isAllowOnlyNumbers() {
    return allowOnlyNumbers;
  }


  /**
   * Define if the cod box allows numeric values only.
   * @param allowOnlyNumbers define if the cod box allows numeric values only
   */
  public final void setAllowOnlyNumbers(boolean allowOnlyNumbers) {
    this.allowOnlyNumbers = allowOnlyNumbers;
  }

}
