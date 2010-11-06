package org.openswing.swing.client;

import java.math.*;
import java.text.*;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

import org.openswing.swing.logger.client.*;
import org.openswing.swing.util.client.*;


/**
 * <p>Title: OpenSwing Framework</p>
 * <p>Description: Numeric input control.</p>
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
public class NumericControl extends BaseInputControl implements InputControl {

  /** maximum number of digits */
  private int maxCharacters = 255;

  /** maximum value */
  private double maxValue = Integer.MAX_VALUE;

  /** minimum value; default value: 0 */
  private double minValue = 0;

  /** maximum number of decimals */
  private int decimals = 0;

  /** flag used to set thousands symbol visibility; default value = false */
  private boolean grouping = false;

  /** format for the text */
  private DecimalFormat format = null;

  /** value for an input control value not defined */
  private String nullValue = null;

  /** numeric field */
  private NumBox numBox = new NumBox();

  /** flag used to define whether zero digits (after decimal point) must be hided/showed; default value: <code>ClientSettings.ZERO_SHOWS_AS_ABSENT</code> i.e. show zero digits */
  private boolean hideZeroDigits = ClientSettings.HIDE_ZERO_DIGITS;


  /**
   * Constructor.
   */
  public NumericControl() {
    this(10);
  }


  /**
   * Constructor.
   * @param columns number of visibile characters
   */
  public NumericControl(int columns) {
    numBox.setColumns(columns);
    numBox.setDisabledTextColor(UIManager.getColor("TextField.foreground"));

//    setLayout(new FlowLayout(FlowLayout.LEFT, 0, 0));
//    this.add(numBox);
    this.setLayout(new GridBagLayout());
    this.add(numBox, new GridBagConstraints(0, 0, 1, 1, 1.0, 1.0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
//    this.setLayout(new java.awt.BorderLayout(0,0));
//    this.add(numBox, java.awt.BorderLayout.CENTER);


    StringBuffer s = new StringBuffer(numBox.getColumns()); for(int i=0;i<numBox.getColumns();i++) s.append("0");
    setMinimumSize(new Dimension(
      numBox.getFontMetrics(numBox.getFont()).stringWidth(s.toString()),
      numBox.getPreferredSize().height
    ));


    setFormat();
    addKeyListener();
    addFocusListener();
    initListeners();
  }


  /**
   * @return component inside this whose contains the value
   */
  public JComponent getBindingComponent() {
    return numBox;
  }


  /**
   * Method called to set the numeric format.
   */
  private void setFormat() {

    DecimalFormatSymbols dfs = new DecimalFormatSymbols();
    dfs.setGroupingSeparator(ClientSettings.getInstance().getResources().getGroupingSymbol());
    dfs.setDecimalSeparator(ClientSettings.getInstance().getResources().getDecimalSymbol());

    String zero = "0";
    if (hideZeroDigits)
      zero = "#";

    if (!grouping && decimals==0)
      format = new DecimalFormat("#");
    else if (grouping && decimals==0)
      format = new DecimalFormat("#,###",dfs);
    else if (grouping && decimals>0) {
      String dec = "";
      for(int i=0;i<decimals;i++)
        dec += zero;
      format = new DecimalFormat("#,##0."+dec,dfs);
    } else if (!grouping && decimals>0) {
      String dec = "";
      for(int i=0;i<decimals;i++)
        dec += zero;
      format = new DecimalFormat("0."+dec,dfs);
    }
    format.setGroupingUsed(grouping);
    nullValue = "";
  }


  /**
   * @return BigDecimal value
   */
  public final BigDecimal getBigDecimal() {
    if (numBox.getText().length()==0 || numBox.getText().equals(nullValue))
      return null;
    try {
      BigDecimal num = new BigDecimal(format.parse(numBox.getText()).doubleValue());
//      if (String.valueOf(num.doubleValue()-(double)num.intValue()).length()>10)
        num = num.setScale(decimals,BigDecimal.ROUND_HALF_UP);
      return num;
    }
    catch (ParseException ex) {
      Logger.error(this.getClass().getName(),"getBigDecimal","Error while creating BigDecimal object",ex);
      return null;
    }
  }


  /**
   * @return Double value
   */
  public final Double getDouble() {
    if (numBox.getText().length()==0 || numBox.getText().equals(nullValue))
      return null;
    try {
      return new Double(format.parse(numBox.getText()).doubleValue());
    }
    catch (ParseException ex) {
      Logger.error(this.getClass().getName(),"getDouble","Error while creating BigDecimal object",ex);
      return null;
    }
  }


  /**
   * @return BigDecimal value
   */
  public final Object getValue() {
    return getBigDecimal();
  }


  /**
   * @return numeric value (without any currency symbol)
   */
  public final String getText() {
    BigDecimal value = null;
    try {
      if (numBox.getText().length()!=0 && !numBox.getText().equals(nullValue) && !"-".equals(numBox.getText()))
        value = new BigDecimal(format.parse(numBox.getText()).doubleValue());
      if ("-".equals(numBox.getText()))
        value = new BigDecimal(0).negate();
    }
    catch (ParseException ex) {
      Logger.error(this.getClass().getName(),"getText","Error while creating BigDecimal object",ex);
      return null;
    }

    if (value==null)
      return null;
    else
      return String.valueOf(value.doubleValue());
  }


  /**
   * Set maximum number of characters.
   * @param maxCharacters maximum number of characters
   */
  public void setMaxCharacters(int maxCharacters) {
    this.maxCharacters = maxCharacters;
  }


  /**
   * @return maximum number of decimals
   */
  public int getDecimals() {
    return decimals;
  }


  /**
   * Set maximum number of decimals.
   * @param decimals maximum number of decimals
   */
  public void setDecimals(int decimals) {
    this.decimals = decimals;
    setFormat();
  }


  /**
   * @return maximum value
   */
  public double getMaxValue() {
    return maxValue;
  }


  /**
   * Set maximum value.
   * @param maxValue maximum value
   */
  public void setMaxValue(double maxValue) {
    this.maxValue = maxValue;
  }


  /**
   * @return minimum value
   */
  public double getMinValue() {
    return minValue;
  }


  /**
   * Set minimum value.
   * @param minValue minimum value
   */
  public void setMinValue(double minValue) {
    this.minValue = minValue;
  }


  /**
   * @return maximum number of digits
   */
  public int getMaxCharacters() {
    return maxCharacters;
  }


  /**
   * Set thousands symbol visibility.
   * @param grouping thousands symbol visibility
   */
  public final void setGrouping(boolean grouping) {
    this.grouping = grouping;
    setFormat();
  }


  /**
   * @return boolean thousands symbol visibility
   */
  public final boolean isGrouping() {
    return grouping;
  }


  /**
   * Create a key listener to correcly set the content of the control.
   */
  private void addKeyListener() {
    numBox.addKeyListener(new KeyAdapter() {

      public final void keyReleased(KeyEvent e) {
        e.consume();
      }

      public final void keyTyped(KeyEvent e) {
        if (NumericControl.this.getText()==null ||
            NumericControl.this.getText().length()==0)
          NumericControl.this.setText(nullValue);

        if (e.getKeyChar()==ClientSettings.getInstance().getResources().getDecimalSymbol() &&
            decimals>0 &&
            numBox.getText()!=null &&
            numBox.getText().indexOf(ClientSettings.getInstance().getResources().getDecimalSymbol())!=-1) {
          e.consume();
          return;
        }

        if (e.getKeyChar()==ClientSettings.getInstance().getResources().getDecimalSymbol() && decimals>0)
          return;
        else if (e.getKeyChar()==ClientSettings.getInstance().getResources().getGroupingSymbol() && grouping)
          return;
        else if (e.getKeyChar()=='\b')
          return;
        else if (e.getKeyChar()=='-' && minValue<0 && numBox.getCaretPosition()==0 && numBox.getText()!=null && (numBox.getText().length()==0 || numBox.getText().charAt(0)!='-'))
          return;
        else if (e.getKeyChar()<'0' || e.getKeyChar()>'9')
          e.consume();

        if (numBox.getText()!=null &&
            numBox.getSelectedText()==null &&
            numBox.getText().length()>=maxCharacters) {
          e.consume();
        }
        else if (numBox.getText()!=null &&
                 numBox.getSelectedText()!=null &&
                 numBox.getText().length()-numBox.getSelectedText().length()>=maxCharacters) {
          e.consume();
        }


      }

      public void keyPressed(KeyEvent e) {
        if (e.getKeyCode()==e.VK_ENTER ||
            e.getKeyCode()==e.VK_TAB ||
            e.getKeyCode()==e.VK_DELETE ||
            e.getKeyCode()==e.VK_BACK_SPACE ||
            e.getKeyCode()==e.VK_ESCAPE ||
            e.getKeyCode()==e.VK_LEFT ||
            e.getKeyCode()==e.VK_RIGHT ||
            e.isAltDown())
          return;
        e.consume();
      }

    });
  }


  /**
   * Method that overrides the super class setText method.
   * @param text number to set
   */
  public final void setText(String text) {
    try {
      BigDecimal number = null;
      if (text != null && text.length() > 0) {
        number = new BigDecimal(text);
      }
      if (number!=null)
        numBox.setText(format.format(number));
      else
        numBox.setText(nullValue);
    }
    catch (Exception ex) {
      numBox.setText(nullValue);
    }
  }


  /**
   * @param value number to set
   */
  public final void setValue(Object value) {
    try {
      if (value==null)
        numBox.setText(nullValue);
      else
        numBox.setText(format.format(value));
    }
    catch (Exception ex) {
      numBox.setText(nullValue);
    }
  }


  /**
   * Create a focus listener to correcly set the content of the control.
   */
  private void addFocusListener() {
    numBox.addFocusListener(new FocusAdapter() {

      public void focusLost(FocusEvent e) {

        if (numBox.isEnabled() && numBox.isEditable()) {
          try {
            Number number = format.parse(numBox.getText());
            if (number!=null) {
              if (number.doubleValue()>maxValue) {
                setText(String.valueOf(maxValue));
                number = new BigDecimal(maxValue);
              }
              else if (number.doubleValue()<minValue) {
                setText(String.valueOf(minValue));
                number = new BigDecimal(minValue);
              }
              else
                setText(String.valueOf(number));

              // test number of decimals...
              java.math.BigDecimal d = new java.math.BigDecimal(number.doubleValue());
              d = d.setScale(decimals,java.math.BigDecimal.ROUND_HALF_UP);
              setText(d.toString());

            }

          }
          catch (ParseException ex) {
            setText(nullValue);
          }
          if (numBox.getText().length()>maxCharacters)
            NumericControl.this.setText(numBox.getText().substring(0,maxCharacters));
        }

      }

    });
  }


  /**
   * @return flag used to define whether zero digits (after decimal point) must be hided/showed
   */
  public final boolean isHideZeroDigits() {
    return hideZeroDigits;
  }


  /**
   * Define whether zero digits (after decimal point) must be hided/showed; default value: <code>false</code> i.e. show zero digits.
   * @param hideZeroDigits flag used to define whether zero digits (after decimal point) must be hided/showed
   */
  public final void setHideZeroDigits(boolean hideZeroDigits) {
    this.hideZeroDigits = hideZeroDigits;
  }


  /**
   * Replace enabled setting with editable setting (this allow tab swithing).
   * @param enabled flag used to set abilitation of control
   */
  public void setEnabled(boolean enabled) {
    numBox.setEditable(enabled);
    numBox.setFocusable(enabled || ClientSettings.DISABLED_INPUT_CONTROLS_FOCUSABLE);
    if (!enabled)
      numBox.setBackground((Color)UIManager.get("TextField.inactiveBackground"));
  }


  /**
   * @return current input control abilitation
   */
  public final boolean isEnabled() {
    try {
      return numBox.isEditable();
    }
    catch (Exception ex) {
      return false;
    }
  }


  /**
   * Set input control visible characters.
   * @param columns visible characters
   */
  public final void setColumns(int columns) {
    numBox.setColumns(columns);

    StringBuffer s = new StringBuffer(numBox.getColumns()); for(int i=0;i<numBox.getColumns();i++) s.append("0");
    setMinimumSize(new Dimension(
      numBox.getFontMetrics(numBox.getFont()).stringWidth(s.toString()),
      numBox.getPreferredSize().height
    ));
  }


  /**
   * @return input control visibile characters
   */
  public final int getColumns() {
    return numBox.getColumns();
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
      numBox.addFocusListener(listener);
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
      numBox.removeFocusListener(listener);
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
      numBox.addActionListener(listener);
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
      numBox.addKeyListener(listener);
    }
    catch (Exception ex) {
    }
  }



  public void processKeyEvent(KeyEvent e) {
    numBox.processKeyEvent(e);
  }


  /**
   * Removes the specified action listener so that it no longer
   * receives action events from this field.
   *
   * @param l the action listener to be removed
   */
  public final void removeActionListener(ActionListener listener) {
    try {
      numBox.removeActionListener(listener);
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
      numBox.removeKeyListener(listener);
    }
    catch (Exception ex) {
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
    numBox.select(selectionStart,selectionEnd);
  }


}


/**
 * <p>Title: OpenSwing Framework</p>
 * <p>Description: Inner class used to redirect key event to the inner JTextField.</p>
 * <p>Copyright: Copyright (C) 2006 Mauro Carniel</p>
 * @author Mauro Carniel
 * @version 1.0
 */
class NumBox extends JTextField {

    public void processKeyEvent(KeyEvent e) {
      super.processKeyEvent(e);
    }

}
