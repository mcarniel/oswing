package org.openswing.swing.client;

import org.openswing.swing.util.client.ClientSettings;
import org.openswing.swing.logger.client.Logger;
import javax.swing.JTextField;
import javax.swing.UIManager;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.text.DecimalFormat;
import java.text.*;
import java.math.BigDecimal;
import java.awt.Container;
import org.openswing.swing.form.client.Form;
import javax.swing.event.DocumentListener;
import javax.swing.event.DocumentEvent;
import org.openswing.swing.internationalization.java.*;
import org.openswing.swing.message.receive.java.ValueObject;
import javax.swing.JComponent;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.awt.FlowLayout;
import java.awt.event.FocusListener;
import java.awt.event.ActionListener;
import javax.swing.KeyStroke;


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

  /** minimum value */
  private double minValue = Integer.MIN_VALUE;

  /** maximum number of decimals */
  private int decimals = 0;

  /** flag used to set thousands symbol visibility; default value = false */
  private boolean grouping = false;

  /** format for the text */
  protected DecimalFormat format = null;

  /** value for an input control value not defined */
  protected String nullValue = null;

  /** numeric field */
  private NumBox numBox = new NumBox();


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

    setFormat();
    addKeyListener();
    addFocusListener();
    initListeners();
  }


  /**
   * @return component inside this whose contains the value
   */
  protected JComponent getBindingComponent() {
    return numBox;
  }


  /**
   * Method called to set the numeric format.
   */
  protected void setFormat() {

    DecimalFormatSymbols dfs = new DecimalFormatSymbols();
    dfs.setGroupingSeparator(ClientSettings.getInstance().getResources().getGroupingSymbol());
    dfs.setDecimalSeparator(ClientSettings.getInstance().getResources().getDecimalSymbol());

    if (!grouping && decimals==0)
      format = new DecimalFormat("#");
    else if (grouping && decimals==0)
      format = new DecimalFormat("#,###",dfs);
    else if (grouping && decimals>0) {
      String dec = "";
      for(int i=0;i<decimals;i++)
        dec += "#";
      format = new DecimalFormat("#,###."+dec,dfs);
    } else if (!grouping && decimals>0) {
      String dec = "";
      for(int i=0;i<decimals;i++)
        dec += "#";
      format = new DecimalFormat("#."+dec,dfs);
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
      if (numBox.getText().length()!=0 && !numBox.getText().equals(nullValue))
        value = new BigDecimal(format.parse(numBox.getText()).doubleValue());
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

        if (e.getKeyChar()==ClientSettings.getInstance().getResources().getDecimalSymbol() && decimals>0)
          return;
        else if (e.getKeyChar()==ClientSettings.getInstance().getResources().getGroupingSymbol() && grouping)
          return;
        else if (e.getKeyChar()=='\b')
          return;
        else if (e.getKeyChar()<'0' || e.getKeyChar()>'9')
          e.consume();
      }

      public void keyPressed(KeyEvent e) {
        if (e.getKeyCode()==e.VK_ENTER ||
            e.getKeyCode()==e.VK_TAB ||
            e.getKeyCode()==e.VK_DELETE ||
            e.getKeyCode()==e.VK_BACK_SPACE ||
            e.getKeyCode()==e.VK_LEFT ||
            e.getKeyCode()==e.VK_RIGHT)
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
              if (number.doubleValue()>maxValue)
                setText(String.valueOf(maxValue));
              else if (number.doubleValue()<minValue)
                setText(String.valueOf(minValue));
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
   * Replace enabled setting with editable setting (this allow tab swithing).
   * @param enabled flag used to set abilitation of control
   */
  public void setEnabled(boolean enabled) {
    numBox.setEditable(enabled);
    numBox.setFocusable(enabled);
  }


  /**
   * @return current input control abilitation
   */
  public final boolean isEnabled() {
    return numBox.isEditable();
  }


  /**
   * Set input control visible characters.
   * @param columns visible characters
   */
  public final void setColumns(int columns) {
    numBox.setColumns(columns);
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


  public void processKeyEvent(KeyEvent e) {
    numBox.processKeyEvent(e);
  }


  /**
   * Removes the specified action listener so that it no longer
   * receives action events from this textfield.
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
