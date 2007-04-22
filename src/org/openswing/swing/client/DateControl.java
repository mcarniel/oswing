package org.openswing.swing.client;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;
import java.util.*;
import java.text.*;
import javax.swing.text.Document;
import java.beans.Beans;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeEvent;

import com.toedter.calendar.*;

import java.awt.Container;
import org.openswing.swing.form.client.Form;
import org.openswing.swing.internationalization.java.Resources;
import org.openswing.swing.util.client.*;
import org.openswing.swing.util.java.*;
import org.openswing.swing.message.receive.java.ValueObject;
import org.openswing.swing.logger.client.Logger;
import java.lang.reflect.*;


/**
 * <p>Title: OpenSwing Framework</p>
 * <p>Description: Date input control: contains a text input control when digit the date and a button for opening a calendar.</p>
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
public class DateControl extends BaseInputControl implements KeyListener,FocusListener,InputControl {

  /** separator */
  private char separator = '/';

  /** flag used to show century */
  private boolean showCentury = true;

  /** date format; possible values:  Resources.YMD, Resources.DMY, Resources.MDY, Resources.YDM */
  private int dateFormat;

  /** current date value */
  private Calendar currentDate = null;

  /** date box */
  private DateBox date = new DateBox(8);

  /** maximum allowed date*/
  private Date upperLimit = null;

  /** minimum allowed date*/
  private Date lowerLimit = null;

  /** date format */
  private SimpleDateFormat sdf = null;

  /** calendar button */
  private JButton buttonChooser;

  /** dialog window which contains the calendar */
  private JDialog menu = new JDialog(ClientUtils.getParentFrame(this));

  /** flag used to set popup menu visibility */
  private boolean menuIsVisible = false;

  /** possibile values: Consts.TYPE_DATE, Consts.TYPE_TIME, Consts.TYPE_DATE_TIME */
  private int dateType;

  /** possibile values: Resources.HH_MM or Resources.H_MM_AAA */
  private String timeFormat = null;

  /** calendar */
  private JCalendar calendar = new JCalendar();

  /** flag used to indicate that the user has selected a month from the calendar */
  private boolean monthSelected = false;

  /** date changed listeners */
  private ArrayList dateListeners = new ArrayList();

  /** flag used in property listener to avoid date setting when pre-set the initial value */
  private boolean skipDateSetting = false;


  public DateControl() {
    this(
      Consts.TYPE_DATE,
      ClientSettings.getInstance().getResources().getDateFormat(),
      ClientSettings.getInstance().getResources().getDateFormatSeparator(),
      ClientSettings.getInstance().getResources().isShowCenturyInDateFormat(),
      Resources.HH_MM
    );
  }


  /**
   * Constructor.
   * @param dateType possibile values: Consts.TYPE_DATE, Consts.TYPE_TIME, Consts.TYPE_DATE_TIME
   * @param dateFormat; possible values:  Resources.YMD, Resources.DMY, Resources.MDY, Resources.YDM
   * @param dateFormatSeparator yy MM dd separator; for example: '/' or '-'
   * @param showCenturyInDateFormat define if the year is in the format 'yy' or 'yyyy'
   * @param timeFormat possibile values: Resources.HH_MM or Resources.H_MM_AAA
   */
  public DateControl(int dateType,int dateFormat,char dateFormatSeparator,boolean showCenturyInDateFormat,String timeFormat) {
    date.setDisabledTextColor(date.getForeground());
    if (dateType==Consts.TYPE_DATE_TIME)
      this.date.setColumns(12);
    this.dateType = dateType;
    this.separator = dateFormatSeparator;
    this.showCentury = showCenturyInDateFormat;
    this.timeFormat = timeFormat;
    setFormat(dateFormat);
    try {
      setOpaque(false);

      Icon icon = new ImageIcon(ClientUtils.getImage(ClientSettings.CALENDAR));
      buttonChooser = new JButton(icon);
//      add("East",buttonChooser);

      this.setLayout(new GridBagLayout());

      if (Beans.isDesignTime()) {
        this.add(new JTextField(" __ /  __ / __ "), new GridBagConstraints(0, 0, 1, 1, 1.0, 1.0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
      }
      else
        this.add(date, new GridBagConstraints(0, 0, 1, 1, 1.0, 1.0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
      this.add(buttonChooser, new GridBagConstraints(1, 0, 1, 1, 0.0, 1.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));

//      setLayout(new FlowLayout(FlowLayout.LEFT, 0, 0));
//      this.add(date);
//      this.add(buttonChooser);

//      setLayout(new BorderLayout(0, 0));
      date.setEnabled(true);
//      add("Center", date);
      date.addKeyListener(this);
      date.addFocusListener(this);


      buttonChooser.setPreferredSize(new Dimension(icon.getIconWidth()+6,date.getPreferredSize().height));
      buttonChooser.setMaximumSize(new Dimension(icon.getIconWidth()+6,date.getPreferredSize().height));
      buttonChooser.setMinimumSize(new Dimension(icon.getIconWidth()+6,date.getPreferredSize().height));

      ApplicationEventQueue.getInstance().addKeyListener(new KeyAdapter() {
        public void keyPressed(KeyEvent e) {
          if (e.getKeyCode()==e.VK_ESCAPE) {
            Container c = ((JComponent)e.getSource()).getParent();
            while(c.getParent()!=null && !c.equals(menu))
              c = c.getParent();
            if (c.equals(menu)) {
              menuIsVisible = false;
              menu.setVisible(false);
            }
          }
        }
      });

      menu.setUndecorated(true);
      calendar.setBorder(BorderFactory.createLineBorder(Color.black));
      menu.setSize(calendar.getPreferredSize().width+30,calendar.getPreferredSize().height+20);
      menu.getContentPane().setLayout(new BorderLayout());
      menu.getContentPane().add(calendar,BorderLayout.CENTER);
      calendar.setLocale(new Locale(ClientSettings.getInstance().getResources().getLanguageId()));

      calendar.getDayChooser().addPropertyChangeListener(new PropertyChangeListener() {
        public void propertyChange(PropertyChangeEvent evt) {
          if (skipDateSetting) {
            return;
          }
          if (Beans.isDesignTime())
            return;

          if (menuIsVisible) {
            menuIsVisible = false;
            setDate(calendar.getCalendar().getTime());
            calendar.transferFocus();
            menu.setVisible(false);
          }

        }
      });


      buttonChooser.addActionListener(new ActionListener() {
        public void actionPerformed(ActionEvent evt) {

          if (menuIsVisible) {
            menuIsVisible = false;
            menu.setVisible(false);
          }
          else {
            Container c = DateControl.this;
            int x = (int)c.getLocation().getX();
            int y = (int)c.getLocation().getY();
            while(c.getParent()!=null) {
              c = c.getParent();
              x += (int)c.getLocation().getX();
              y += (int)c.getLocation().getY();
            }

            if (currentDate != null) {
              calendar.setCalendar(currentDate);
            }

            menu.setLocation(
              x,
              y+buttonChooser.getY() + buttonChooser.getHeight()
            );

            menu.setVisible(true);
            menuIsVisible = true;
          }

          monthSelected = false;

        }

      });

      calendar.getMonthChooser().addPropertyChangeListener(new PropertyChangeListener() {
        public void propertyChange(PropertyChangeEvent evt) {
          if (menuIsVisible) {
            monthSelected = true;
          }
        }
      });

      buttonChooser.addFocusListener(new FocusAdapter() {
        public void focusGained(FocusEvent e) {
          if (menuIsVisible && !monthSelected) {
            menu.setVisible(false);
            menuIsVisible = false;
          }
          if (monthSelected)
            monthSelected = false;
        }
      });

      initListeners();
    }
    catch (Exception ex) {
      ex.printStackTrace();
    }
  }


  /**
   * Set separator.
   * @param separator separator character
   */
  public final void setSeparator(char separator) {
    if(Character.isLetterOrDigit(separator) || separator == ' ')
      return;
    this.separator = separator;
    setFormat(dateFormat);
    date.setText(buildText());
  }


  /**
   * @return separator
   */
  public final char getSeparator() {
    return separator;
  }


  /**
   * Set date format.
   * @param dateFormat; possible values:  Resources.YMD, Resources.DMY, Resources.MDY, Resources.YDM
   */
  public final void setFormat(int dateFormat) {
    if(dateFormat < 0 || dateFormat > 3)
      dateFormat = Resources.YMD;
    this.dateFormat = dateFormat;
    String mask = ClientSettings.getInstance().getResources().getDateMask(dateType,dateFormat,separator,showCentury,timeFormat);
    sdf = new SimpleDateFormat(mask);
    date.setText(buildText());
  }


  /**
   * @return date format; possible values:  YMD, DMY, MDY, YDM
   */
  public final int getFormat() {
      return dateFormat;
  }


  /**
   * Create a date string from the currentDate value.
   * @return date as string
   */
  private String buildText() {
    String voidYear = "  ";
    if (showCentury)
      voidYear += "  ";

    if (currentDate == null) {
      String value = null;
      switch(dateFormat) {
        case Resources.YMD:
          value = voidYear+separator+"  "+separator+"  ";
          break;
        case Resources.DMY:
          value = "  "+separator+"  "+separator+voidYear;
          break;
        case Resources.MDY:
          value = "  "+separator+"  "+separator+voidYear;
          break;
        case Resources.YDM:
          value = voidYear+separator+"  "+separator+"  ";
          break;
      }
      if (dateType==Consts.TYPE_DATE_TIME) {
        if (timeFormat==Resources.HH_MM)
          value += "   :  ";
        else
          value += "   :     ";
      }
      else if (dateType==Consts.TYPE_TIME) {
        if (timeFormat==Resources.HH_MM)
          value = "  :  ";
        else
          value = "  :     ";
      }

      return value;
    }

    return sdf.format(currentDate.getTime());
  }



  /**
   * Set date into the date field.
   * @param date date
   */
  public final void setDate(Date date) {
    Date oldDate = null;
    if (currentDate!=null)
      oldDate = (Date)currentDate.getTime().clone();

    if (date==null)
      this.currentDate = null;
    else {
      this.currentDate = Calendar.getInstance();
      this.currentDate.setTime(date);

      if (dateType==Consts.TYPE_DATE) {
        // remove time info from the date...
        this.currentDate.set(Calendar.HOUR,0);
        this.currentDate.set(Calendar.MINUTE,0);
        this.currentDate.set(Calendar.SECOND,0);
        this.currentDate.set(Calendar.MILLISECOND,0);
      }

      skipDateSetting = true;
      this.calendar.setCalendar(currentDate);
      skipDateSetting = false;
    }
    this.date.setText(buildText());

    for(int i=0;i<dateListeners.size();i++)
      ((DateChangedListener)dateListeners.get(i)).dateChanged(oldDate,getDate());

  }


  /**
   * @return date
   */
  public final Date getDate() {
    if (currentDate==null)
      return null;

    if (dateType==Consts.TYPE_DATE) {
      // remove time info from the date...
      this.currentDate.set(Calendar.HOUR,0);
      this.currentDate.set(Calendar.MINUTE,0);
      this.currentDate.set(Calendar.SECOND,0);
      this.currentDate.set(Calendar.MILLISECOND,0);
    }

    return currentDate.getTime();
  }


  /**
   * Set maximum allowed date.
   * @param upperLimit maximum allowed date
   */
  public final void setUpperLimit(Date upperLimit) {
    this.upperLimit = upperLimit;
    if(currentDate != null)
      refresh();
  }


  /**
   * @return minimum allowed date
   */
  public final Date getUpperLimit() {
    return upperLimit;
  }


  /**
   * Set minimum allowed date.
   * @param lowerLimit minimum allowed date
   */
  public final void setLowerLimit(Date lowerLimit) {
    this.lowerLimit = lowerLimit;
    if(currentDate != null)
      refresh();
  }


  /**
   * @return minimum allowed date
   */
  public final Date getLowerLimit() {
    return lowerLimit;
  }


  /**
   * Refresh calendar content.
   */
  public final void refresh() {
    limits();
    date.setText(buildText());
    repaint();
  }


  /**
   * Set date content, according to lower/upper limits (if setted).
   */
  private void limits() {
    if (upperLimit != null &&
        currentDate != null &&
        upperLimit.getTime() < currentDate.getTime().getTime()) {
      currentDate.setTime(upperLimit);

      if (dateType==Consts.TYPE_DATE) {
        // remove time info from the date...
        this.currentDate.set(Calendar.HOUR,0);
        this.currentDate.set(Calendar.MINUTE,0);
        this.currentDate.set(Calendar.SECOND,0);
        this.currentDate.set(Calendar.MILLISECOND,0);
      }

      return;
    }
    if (lowerLimit != null &&
        currentDate != null &&
       lowerLimit.getTime() > currentDate.getTime().getTime()) {
      currentDate.setTime(lowerLimit);

      if (dateType==Consts.TYPE_DATE) {
        // remove time info from the date...
        this.currentDate.set(Calendar.HOUR,0);
        this.currentDate.set(Calendar.MINUTE,0);
        this.currentDate.set(Calendar.SECOND,0);
        this.currentDate.set(Calendar.MILLISECOND,0);
      }

      return;
    }
  }


  public void setEnabled(boolean enabled) {
    date.setEnabled(true); // otherwise the disabled text is not selectable...
    date.setEditable(enabled);
    buttonChooser.setEnabled(enabled);
    date.setFocusable(enabled);
    buttonChooser.setFocusable(enabled);
  }


  /**
   * @return current input control abilitation
   */
  public final boolean isEnabled() {
    return date.isEditable();
  }



  /**
   * Riceve il fuoco e lo smista al componente wrappato.
   */
  public final void requestFocus() {
    date.requestFocus();
  }


  /**
   * Riceve la richiesta di trasferimento fuoco e lo smista al componente wrappato.
   */
  public final void transferFocus() {
    date.transferFocus();
  }


  /**
   * @return show century
   */
  public final boolean isShowCentury() {
    return showCentury;
  }


  /**
   * Used to show century.
   * @param showCentury show century
   */
  public final void setShowCentury(boolean showCentury) {
    this.showCentury = showCentury;
    setFormat(dateFormat);
  }


  /**
   * Invoked when a component gains the keyboard focus.
   */
  public void focusGained(FocusEvent e) {
    if (menuIsVisible) {
      menu.setVisible(false);
      menuIsVisible = false;
    }

    if (currentDate==null)
      refresh();
//    date.select(0,date.getText().length());
    date.setCaretPosition(0);
  }


  /**
   * Invoked when a component loses the keyboard focus.
   */
  public void focusLost(FocusEvent e) {
    Calendar oldCurrentDate = currentDate;
    try {
      String text = date.getText();
      if (dateType==Consts.TYPE_DATE_TIME &&
          text.endsWith("  :  ") &&
          timeFormat.equals(Resources.HH_MM)) {
        text = text.substring(0,9+(showCentury?2:0)) + "00:00";
      }
      else if (dateType==Consts.TYPE_DATE_TIME &&
          text.endsWith("  :  ") &&
          timeFormat.equals(Resources.H_MM_AAA)) {
        text = text.substring(0,9+(showCentury?2:0)) + "00:00 AM";
      }

      // check if the date is null...
      boolean isNull = true;
      for(int i=0;i<text.length();i++)
        if (text.charAt(i)!=' ' &&
            text.charAt(i)!=':' &&
            text.charAt(i)!=separator) {
          isNull = false;
          break;
        }
      if (!isNull)
        setDate(sdf.parse(text));
      else
        setDate(null);
    }
    catch (Exception ex) {
      currentDate = oldCurrentDate;
    }
    if (currentDate==null)
      date.setText("");

  }


  /**
   * Invoked when a key has been typed.
   * See the class description for {@link KeyEvent} for a definition of a key typed event.
   */
  public final void keyTyped(KeyEvent e) {
    if (!date.isEditable()) {
      e.consume();
      return;
    }
    e.consume();
    int i = date.getCaretPosition();
    if (e.getKeyChar()=='\b') {
      // backspace typed...
      if (date.getText().length()>0 && i>0) {
        if (date.getText().charAt(i-1)!=separator &&
            date.getText().charAt(i-1)!=':')
          setChar(i-1,' ');
        date.setCaretPosition(i-1);
      }
    }
    else if ((dateType==Consts.TYPE_DATE_TIME || dateType==Consts.TYPE_TIME) &&
            timeFormat.equals(Resources.H_MM_AAA) &&
            date.getText().length()>i &&
            (dateType==Consts.TYPE_DATE_TIME && i>5+9+(showCentury?2:0) || dateType==Consts.TYPE_TIME && i>5)) {
      if (e.getKeyChar()=='A' || e.getKeyChar()=='M' || e.getKeyChar()=='P')
        // AM/PM...
        setChar(i,e.getKeyChar());
      return;
    }
    else if (e.getKeyChar() >= '0' && e.getKeyChar() <= '9') {
      // digit typed...

      if (dateType==Consts.TYPE_DATE || dateType==Consts.TYPE_DATE_TIME) {
        // date or date+time...
        int len = 0;
        if (showCentury)
          len += 2;
        switch(dateFormat) {
          case Resources.YMD:
            if (i<2+len) {
              setChar(i,e.getKeyChar());
            }
            else if (i>=3+len && i<=4+len) {
              setChar(i,e.getKeyChar());
            }
            else if (i>=6+len && i<=7+len) {
              setChar(i,e.getKeyChar());
            }
            else if (date.getText().length()>i) {
              if (dateType==Consts.TYPE_DATE_TIME && i>8+len)
                setTimeDigits(i,e.getKeyChar(),9+len);
              else
               date.setCaretPosition(i+1);
            }
            break;
          case Resources.DMY:
            if (i<2) {
              setChar(i,e.getKeyChar());
            }
            else if (i>=3 && i<=4) {
              setChar(i,e.getKeyChar());
            }
            else if (i>=6 && i<=7+len) {
              setChar(i,e.getKeyChar());
            }
            else if (date.getText().length()>i) {
              if (dateType==Consts.TYPE_DATE_TIME && i>8+len)
                setTimeDigits(i,e.getKeyChar(),9+len);
              else
               date.setCaretPosition(i+1);
            }
            break;
          case Resources.MDY:
            if (i<2) {
              setChar(i,e.getKeyChar());
            }
            else if (i>=3 && i<=4) {
              setChar(i,e.getKeyChar());
            }
            else if (i>=6 && i<=7+len) {
              setChar(i,e.getKeyChar());
            }
            else if (date.getText().length()>i) {
              if (dateType==Consts.TYPE_DATE_TIME && i>8+len)
                setTimeDigits(i,e.getKeyChar(),9+len);
              else
               date.setCaretPosition(i+1);
            }
            break;
          case Resources.YDM:
            if (i<2+len) {
              setChar(i,e.getKeyChar());
            }
            else if (i>=3+len && i<=4+len) {
              setChar(i,e.getKeyChar());
            }
            else if (i>=6+len && i<=7+len) {
              setChar(i,e.getKeyChar());
            }
            else if (date.getText().length()>i) {
              if (dateType==Consts.TYPE_DATE_TIME && i>8+len)
                setTimeDigits(i,e.getKeyChar(),9+len);
              else
               date.setCaretPosition(i+1);
            }
            break;
        }
      }
      else {
        // time only...
        setTimeDigits(i,e.getKeyChar(),0);
      }
    }
  }


  /**
   * Format time digits.
   * @param i current cursor position
   * @param c current digit
   */
  private void setTimeDigits(int i,char c,int len) {
    if (i<2+len) {
      setChar(i,c);
    }
    else if (i>=3+len && i<=4+len) {
      setChar(i,c);
    }
    else if (i>=6+len && i<=7+len && timeFormat.equals(Resources.H_MM_AAA)) {
      setChar(i,c);
    }
    else if (date.getText().length()>i) {
       date.setCaretPosition(i+1);
    }
  }


  /**
   * Replace a character with another one.
   * @param pos caracter position
   * @param c character that replaces the current char at pos position
   */

  private void setChar(int pos,char c) {
    StringBuffer text = new StringBuffer(date.getText());
    text.replace(pos,pos+1,String.valueOf(c));
    date.setText(text.toString());
    date.setCaretPosition(pos+1);
    if (date.getText().length()>date.getCaretPosition() &&
        date.getText().charAt(date.getCaretPosition())==separator)
      date.setCaretPosition(pos+2);
  }


  /**
   * Invoked when a key has been pressed.
   * See the class description for {@link KeyEvent} for a definition of
   * a key pressed event.
   */
  public final void keyPressed(KeyEvent e) {
//    if (e.getKeyCode()!=e.VK_TAB && e.getKeyCode()!=e.VK_ENTER)
      e.consume();

    if (e.getKeyCode()==e.VK_ESCAPE) {
      setDate(null);
      return;
    }
    if (e.getKeyCode()==e.VK_F1) {
      setDate(new Date());
    }

    int i = date.getCaretPosition();
    if (e.getKeyCode()==e.VK_LEFT && i>0) {
      // left button typed...
      date.setCaretPosition(i-1);
    }
    else if (e.getKeyCode()==e.VK_RIGHT && i<date.getText().length()) {
      // right button typed...
      date.setCaretPosition(i+1);
    }
    else if (e.getKeyCode()==e.VK_HOME) {
      // home button typed...
      date.setCaretPosition(0);
    }
    else if (e.getKeyCode()==e.VK_END) {
      // end button typed...
      date.setCaretPosition(date.getText().length());
    }
  }


  /**
   * Invoked when a key has been released.
   * See the class description for {@link KeyEvent} for a definition of
   * a key released event.
   */
  public final void keyReleased(KeyEvent e) {
    e.consume();
  }



  /**
   * @return date input field
   */
  public final JTextField getDateField() {
    return date;
  }


  /**
   * @return possibile values: Consts.TYPE_DATE, Consts.TYPE_TIME, Consts.TYPE_DATE_TIME
   */
  public final int getDateType() {
    return dateType;
  }


  /**
   * Set date type: date only, date + time, time only.
   * @param dateType possibile values: Consts.TYPE_DATE, Consts.TYPE_TIME, Consts.TYPE_DATE_TIME
   */
  public final void setDateType(int dateType) {
    this.dateType = dateType;
    if (dateType==Consts.TYPE_DATE_TIME)
      this.date.setColumns(12);
    else
      this.date.setColumns(8);
    setFormat(dateFormat);
  }


  /**
   * @return possibile values: Resources.HH_MM or Resources.H_MM_AAA
   */
  public final String getTimeFormat() {
    return timeFormat;
  }


  /**
   * Set the time format.
   * @param timeFormat possibile values: Resources.HH_MM or Resources.H_MM_AAA
   */
  public final void setTimeFormat(String timeFormat) {
    this.timeFormat = timeFormat;
    setFormat(dateFormat);
  }


  /**
   * Add a date changed listener.
   */
  public final void addDateChangedListener(DateChangedListener listener) {
    dateListeners.add(listener);
  }


  /**
   * Remove a date changed listener.
   */
  public final void removeDateChangedListener(DateChangedListener listener) {
    dateListeners.remove(listener);
  }



  /**
   * @return component inside this whose contains the value
   */
  protected JComponent getBindingComponent() {
    return date;
  }


  /**
   * @return value related to the input control
   */
  public Object getValue() {
    return getDate();
  }


  /**
   * Set value to the input control.
   * @param value value to set into the input control
   */
  public void setValue(Object value) {
    setDate((java.util.Date)value);
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
      date.addFocusListener(listener);
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
      date.removeFocusListener(listener);
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
      date.addActionListener(listener);
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
      date.removeActionListener(listener);
    }
    catch (Exception ex) {
    }
  }


  public void processKeyEvent(KeyEvent e) {
    date.processKeyEvent(e);
  }


}


/**
 * <p>Title: OpenSwing Framework</p>
 * <p>Description: Inner class used to redirect key event to the inner JTextField.</p>
 * <p>Copyright: Copyright (C) 2006 Mauro Carniel</p>
 * @author Mauro Carniel
 * @version 1.0
 */
class DateBox extends JTextField {

  public DateBox(int cols) {
    super(cols);
  }

    public void processKeyEvent(KeyEvent e) {
      super.processKeyEvent(e);
    }

}
