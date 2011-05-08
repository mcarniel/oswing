package org.openswing.swing.client;

import java.beans.*;
import java.text.*;
import java.util.*;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

import org.openswing.swing.internationalization.java.*;
import org.openswing.swing.util.client.*;
import org.openswing.swing.util.java.*;
import com.toedter.calendar.*;
import javax.swing.event.AncestorListener;
import javax.swing.event.AncestorEvent;


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

  /** possibile values: Consts.TYPE_DATE, Consts.TYPE_TIME, Consts.TYPE_DATE_TIME */
  private int dateType;

  /** possibile values: Resources.HH_MM or Resources.H_MM_AAA or Resources.HH_MM_SS or Resources.H_MM_SS_AAA */
  private String timeFormat = null;

  /** date changed listeners */
  private ArrayList dateListeners = new ArrayList();

  /** calendar */
  private JDateChooser calendar = new JDateChooser(date);

  /** flag used in setDate to identify whre the date setting event has been fired */
  private boolean eventFiredByCalendar = false;

  /** default date to set into the calendar, when opening it for the first time; null means today */
  private Calendar defaultDate = null;

  /** flag used to show/hide calendar button; default value: <code>true</code> */
  private boolean showCalendarButton = true;

  /** define how the date control must behave when an invalid date has been specified within it: clean up the content (stricy usage) or trying to correct it; default value: <code>ClientSettings.DATE_COMPONENT_STRICT_USAGE</code> */
  private boolean strictUsage = ClientSettings.DATE_COMPONENT_STRICT_USAGE;


  public DateControl() {
    try {
      init(
          Consts.TYPE_DATE,
          ClientSettings.getInstance().getResources().getDateFormat(),
          ClientSettings.getInstance().getResources().getDateFormatSeparator(),
          ClientSettings.getInstance().getResources().isShowCenturyInDateFormat(),
          Resources.HH_MM
      );


//      addAncestorListener(new AncestorListener() {
//
//        public void ancestorAdded(AncestorEvent event) {
//        }
//
//        public void ancestorMoved(AncestorEvent event) {
//        }
//
//        public void ancestorRemoved(AncestorEvent event) {
//          ClientUtils.disposeComponents(DateControl.this.getComponents());
//
//          date = null;
//          dateListeners = null;
//          calendar = null;
//        }
//
//      });

    }
    catch (Throwable ex) {
      ex.printStackTrace();
    }
  }


  public final void finalize() {
    try {
      ClientUtils.disposeComponents(DateControl.this.getComponents());

      if (calendar!=null) {
        calendar.getDateEditor().removePropertyChangeListener(calendar);
        calendar.getJCalendar().getDayChooser().removePropertyChangeListener(calendar);
        calendar.cleanup();
      }
      date = null;
      dateListeners = null;
      calendar = null;
    }
    catch (Exception ex1) {
      ex1.printStackTrace();
    }

  }


  /**
   * Constructor.
   * @param dateType possibile values: Consts.TYPE_DATE, Consts.TYPE_TIME, Consts.TYPE_DATE_TIME
   * @param dateFormat; possible values:  Resources.YMD, Resources.DMY, Resources.MDY, Resources.YDM
   * @param dateFormatSeparator yy MM dd separator; for example: '/' or '-'
   * @param showCenturyInDateFormat define if the year is in the format 'yy' or 'yyyy'
   * @param timeFormat possibile values: Resources.HH_MM or Resources.H_MM_AAA or Resources.HH_MM_SS or Resources.H_MM_SS_AAA
   */
  public DateControl(int dateType,int dateFormat,char dateFormatSeparator,boolean showCenturyInDateFormat,String timeFormat) {
    init(dateType,dateFormat,dateFormatSeparator,showCenturyInDateFormat,timeFormat);
  }


  private void init(int dateType,int dateFormat,char dateFormatSeparator,boolean showCenturyInDateFormat,String timeFormat) {
    try {
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

        calendar.setDateFormatString(sdf.toPattern());
        calendar.setIcon(new ImageIcon(ClientUtils.getImage(ClientSettings.CALENDAR)));
        calendar.updateUI();
        calendar.getJCalendar().setDecorationBordersVisible(true);
        calendar.getJCalendar().getDayChooser().setDecorationBordersVisible(true);
        calendar.getJCalendar().getDayChooser().setDayBordersVisible(true);

        this.setLayout(new GridBagLayout());
//        if (Beans.isDesignTime())
//          this.add(new JTextField(" __ /  __ / __ "), new GridBagConstraints(0, 0, 1, 1, 1.0, 1.0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
//        else
          this.add(calendar, new GridBagConstraints(0, 0, 1, 1, 1.0, 1.0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));

        if (Beans.isDesignTime())
          return;


//      setLayout(new FlowLayout(FlowLayout.LEFT, 0, 0));
//      this.add(date);
//      this.add(buttonChooser);

//      setLayout(new BorderLayout(0, 0));
        date.setEnabled(true);
//      add("Center", date);
        date.addKeyListener(this);
        date.addFocusListener(this);
        date.setFocusable(true);


        setMinimumSize(new Dimension(
          date.getFontMetrics(calendar.getFont()).stringWidth("0000000000"),
          date.getPreferredSize().height
        ));


        String id = ClientSettings.getInstance().getResources().getLanguageId();
        String language = id;
        String country = "";
        if (id.length()==5) {
          language = id.substring(0,2);
          country = id.substring(3);
        }
        calendar.setLocale(new Locale(language,country));

        initListeners();
      }
      catch (Exception ex) {
        ex.printStackTrace();
      }
    }
    catch (Throwable ex) {
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
        else if (timeFormat==Resources.H_MM_AAA)
          value += "   :     ";
        else if (timeFormat==Resources.HH_MM_SS)
          value += "   :  :  ";
        else if (timeFormat==Resources.HH_MM_SS_SSS)
          value += "   :  :  ,   ";
        else if (timeFormat==Resources.H_MM_SS_SSS_AAA)
          value += "   :  :  ,      ";
        else
          value += "   :  :     ";
      }
      else if (dateType==Consts.TYPE_TIME) {
        if (timeFormat==Resources.HH_MM)
          value = "  :  ";
        else if (timeFormat==Resources.H_MM_AAA)
          value = "  :     ";
        else if (timeFormat==Resources.HH_MM_SS)
          value += "  :  :  ";
        else if (timeFormat==Resources.HH_MM_SS_SSS)
          value += "  :  :  ,   ";
        else if (timeFormat==Resources.H_MM_SS_SSS_AAA)
          value += "  :  :  ,      ";
        else
          value += "  :  :     ";
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

      if (!eventFiredByCalendar)
        this.calendar.setCalendar(currentDate);
    }
    this.date.setText(buildText());

    for(int i=0;i<dateListeners.size();i++)
      ((DateChangedListener)dateListeners.get(i)).dateChanged(oldDate,getDate());

    maybeFireValueChangedEvent();
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
    if (!eventFiredByCalendar)
      calendar.setMaxSelectableDate(upperLimit);
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
    if (!eventFiredByCalendar)
      calendar.setMinSelectableDate(lowerLimit);
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
    if (date==null)
      return;
    date.setEnabled(true); // otherwise the disabled text is not selectable...
    date.setEditable(enabled);
    calendar.setEnabled(enabled);
    date.setFocusable(enabled || ClientSettings.DISABLED_INPUT_CONTROLS_FOCUSABLE);
    calendar.setFocusable(enabled);
    if (!enabled)
      getBindingComponent().setBackground((Color)UIManager.get("TextField.inactiveBackground"));
  }


  /**
   * @return current input control abilitation
   */
  public final boolean isEnabled() {
    try {
      if (date != null) {
        return date.isEditable();
      }
      else {
        return false;
      }
    }
    catch (Exception ex) {
      return false;
    }
  }



  /**
   * Riceve il fuoco e lo smista al componente wrappato.
   */
  public final void requestFocus() {
    if (!date.hasFocus())
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
    if (currentDate==null)
      refresh();
//    date.select(0,date.getText().length());
    if (!date.hasFocus())
      date.requestFocus();
    date.setCaretPosition(0);
  }


  /**
   * Invoked when a component loses the keyboard focus.
   */
  public void focusLost(FocusEvent e) {
//    if (e.getSource().equals(date))
//      return;
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
      else if (dateType==Consts.TYPE_DATE_TIME &&
          text.endsWith("  :  :  ") &&
          timeFormat.equals(Resources.HH_MM_SS)) {
        text = text.substring(0,9+(showCentury?2:0)) + "00:00:00";
      }
      else if (dateType==Consts.TYPE_DATE_TIME &&
          text.endsWith("  :  :  ") &&
          timeFormat.equals(Resources.H_MM_SS_AAA)) {
        text = text.substring(0,9+(showCentury?2:0)) + "00:00:00 AM";
      }
      else if (dateType==Consts.TYPE_DATE_TIME &&
          text.endsWith("  :  :  ") &&
          timeFormat.equals(Resources.HH_MM_SS_SSS)) {
        text = text.substring(0,9+(showCentury?2:0)) + "00:00:00,000";
      }
      else if (dateType==Consts.TYPE_DATE_TIME &&
          text.endsWith("  :  :  ") &&
          timeFormat.equals(Resources.H_MM_SS_SSS_AAA)) {
        text = text.substring(0,9+(showCentury?2:0)) + "00:00:00,000 AM";
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
      setDate(currentDate==null?null:currentDate.getTime());
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
            (dateType==Consts.TYPE_DATE_TIME && i>4+9+(showCentury?2:0) || dateType==Consts.TYPE_TIME && i>4)) {
      if (e.getKeyChar()=='A' || e.getKeyChar()=='M' || e.getKeyChar()=='P')
        // AM/PM...
        setChar(i,e.getKeyChar());
      return;
    }
    else if ((dateType==Consts.TYPE_DATE_TIME || dateType==Consts.TYPE_TIME) &&
            timeFormat.equals(Resources.H_MM_SS_AAA) &&
            date.getText().length()>i &&
            (dateType==Consts.TYPE_DATE_TIME && i>7+9+(showCentury?2:0) || dateType==Consts.TYPE_TIME && i>7)) {
      if (e.getKeyChar()=='A' || e.getKeyChar()=='M' || e.getKeyChar()=='P')
        // AM/PM...
        setChar(i,e.getKeyChar());
      return;
    }
    else if ((dateType==Consts.TYPE_DATE_TIME || dateType==Consts.TYPE_TIME) &&
            timeFormat.equals(Resources.H_MM_SS_SSS_AAA) &&
            date.getText().length()>i &&
            (dateType==Consts.TYPE_DATE_TIME && i>11+9+(showCentury?2:0) || dateType==Consts.TYPE_TIME && i>11)) {
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
    if (timeFormat.equals(Resources.H_MM_AAA)) {
      int delta = 0;
      if (date.getText().charAt(len+2)==':' && Character.isDigit(c))
        delta = 1;
      else if (date.getText().charAt(len+1)==':' && Character.isDigit(c))
        delta = 0;
      else if (
               (Character.isDigit(date.getText().charAt(len+2)) ||
                date.getText().charAt(len+2)==' '))
        delta = 1;
      if (i<1+delta+len)
        setChar(i,c);
      else if (i>=2+delta+len && i<=3+delta+len)
        setChar(i,c);
      else if (i>=5+delta+len && i<=6+delta+len)
        setChar(i,c);
    }
    else if (timeFormat.equals(Resources.H_MM_SS_AAA)) {
      int delta = 0;
      if (date.getText().charAt(len+2)==':' && Character.isDigit(c))
        delta = 1;
      else if (date.getText().charAt(len+1)==':' && Character.isDigit(c))
        delta = 0;
      else if (
               (Character.isDigit(date.getText().charAt(len+2)) ||
                date.getText().charAt(len+2)==' '))
        delta = 1;
      if (i<1+delta+len)
        setChar(i,c);
      else if (i>=2+delta+len && i<=3+delta+len)
        setChar(i,c);
      else if (i>=5+delta+len && i<=6+delta+len)
        setChar(i,c);
      else if (i>=8+delta+len && i<=9+delta+len)
        setChar(i,c);
    }
    else if (timeFormat.equals(Resources.H_MM_SS_SSS_AAA)) {
      int delta = 0;
      if (date.getText().charAt(len+2)==':' && Character.isDigit(c))
        delta = 1;
      else if (date.getText().charAt(len+1)==':' && Character.isDigit(c))
        delta = 0;
      else if (
               (Character.isDigit(date.getText().charAt(len+2)) ||
                date.getText().charAt(len+2)==' '))
        delta = 1;
      if (i<1+delta+len)
        setChar(i,c);
      else if (i>=2+len && i<=3+len)
        setChar(i,c);
      else if (i>=5+len && i<=6+len)
        setChar(i,c);
      else if (i>=8+len && i<=10+len)
        setChar(i,c);
      else if (i>=12+len && i<=13+len)
        setChar(i,c);
    }
    else if (timeFormat.equals(Resources.HH_MM)) {
      if (i<2+len)
        setChar(i,c);
      else if (i>=3+len && i<=4+len)
        setChar(i,c);
    }
    else if (timeFormat.equals(Resources.HH_MM_SS)) {
      if (i<2+len)
        setChar(i,c);
      else if (i>=3+len && i<=4+len)
        setChar(i,c);
      else if (i>=6+len && i<=7+len)
        setChar(i,c);
    }
    else if (timeFormat.equals(Resources.HH_MM_SS_SSS)) {
      if (i<2+len)
        setChar(i,c);
      else if (i>=3+len && i<=4+len)
        setChar(i,c);
      else if (i>=6+len && i<=7+len)
        setChar(i,c);
      else if (i>=9+len && i<=11+len)
        setChar(i,c);
    }

    try {
      if (date.getText().length() > i) {
        date.setCaretPosition(i + 1);
      }
    }
    catch (Exception ex) {
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
    if (e.getKeyCode()==ClientSettings.CALENDAR_CURRENT_DATE_KEY.getKeyCode() &&
        e.getModifiers()+e.getModifiersEx()==ClientSettings.CALENDAR_CURRENT_DATE_KEY.getModifiers()) {
      setDate(new Date());
    }
    if (e.getKeyCode()==ClientSettings.CALENDAR_OPEN_KEY.getKeyCode() &&
        e.getModifiers()+e.getModifiersEx()==ClientSettings.CALENDAR_OPEN_KEY.getModifiers()) {
      focusLost(null);

      if (currentDate==null && defaultDate!=null) {
        calendar.getJCalendar().setCalendar(defaultDate);
      }
      else
        calendar.setCalendar(currentDate);
      calendar.actionPerformed(null);
      calendar.getJCalendar().requestFocus();
      calendar.getJCalendar().getDayChooser().requestFocus();
      calendar.getJCalendar().getDayChooser().setFocus();
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

    if (dateType==Consts.TYPE_TIME) {
      // hide date button...
      setShowCalendarButton(false);
    }

    setFormat(dateFormat);
  }


  /**
   * @return possibile values: Resources.HH_MM or Resources.H_MM_AAA or Resources.HH_MM_SS or Resources.H_MM_SS_AAA
   */
  public final String getTimeFormat() {
    return timeFormat;
  }


  /**
   * Set the time format.
   * @param timeFormat possibile values: Resources.HH_MM or Resources.H_MM_AAA or Resources.HH_MM_SS or Resources.H_MM_SS_AAA
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
  public JComponent getBindingComponent() {
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
   * Adds the specified key listener to receive
   * action events from this field.
   *
   * @param l the key listener to be added
   */
  public final void addKeyListener(KeyListener listener) {
    try {
      date.addKeyListener(listener);
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
      date.removeKeyListener(listener);
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


  /**
   * @return default date to set into the calendar, when opening it for the first time; null means today
   */
  public final Calendar getDefaultDate() {
    return defaultDate;
  }


  /**
   * Set the default date to set into the calendar, when opening it for the first time.
   * A null value means today.
   * @param defaultDate default date to set into the calendar, when opening it for the first time; null means today
   */
  public final void setDefaultDate(Calendar defaultDate) {
    this.defaultDate = defaultDate;
  }


  /**
   * @return show/hide calendar button; default value: <code>true</code>
   */
  public final boolean isShowCalendarButton() {
    return showCalendarButton;
  }


 /**
  * Show/hide calendar button; default value: <code>true</code>
  * @param showCalendarButton show/hide calendar button;
  */
 public final void setShowCalendarButton(boolean showCalendarButton) {
    this.showCalendarButton = showCalendarButton;
    try {
      if (showCalendarButton) {
        calendar.getCalendarButton().setVisible(showCalendarButton);
      }
      else {
        calendar.getCalendarButton().setVisible(showCalendarButton);
      }
    }
    catch (Exception ex) {
    }
  }


  /**
   * @return define how the date control must behave when an invalid date has been specified within it: clean up the content (stricy usage) or trying to correct it
   */
  public final boolean isStrictUsage() {
    return strictUsage;
  }


  /**
   * define how the date control must behave when an invalid date has been specified within it: clean up the content (stricy usage) or trying to correct it.
   * Default value: <code>ClientSettings.DATE_COMPONENT_STRICT_USAGE</code>
   * @param strictUsage define how the date control must behave when an invalid date has been specified within it: clean up the content (stricy usage) or trying to correct it
   */
  public final void setStrictUsage(boolean strictUsage) {
    this.strictUsage = strictUsage;
    if (sdf!=null)
      sdf.setLenient(!strictUsage);
  }



  public boolean isFocusable() {
    return false;
  }



  /**
   * <p>Title: OpenSwing Framework</p>
   * <p>Description: Inner class used to redirect key event to the inner JTextField.</p>
   * <p>Copyright: Copyright (C) 2006 Mauro Carniel</p>
   * @author Mauro Carniel
   * @version 1.0
   */
  class DateBox extends JTextField implements IDateEditor {

    public DateBox(int cols) {
      super(cols);
    }


    public void processKeyEvent(KeyEvent e) {
      super.processKeyEvent(e);
    }


    public Date getDate() {
      Date dateToReturn = DateControl.this.getDate();
      if (dateToReturn==null && defaultDate!=null)
        dateToReturn = defaultDate.getTime();
      return dateToReturn;
    }


    public void setDate(Date date) {
      eventFiredByCalendar = true;
      DateControl.this.setDate(date);
      eventFiredByCalendar = false;
    }


    public void setDateFormatString(String dateFormatString) {
      if (dateFormatString!=null)
        DateControl.this.sdf = new SimpleDateFormat(dateFormatString);
    }


    public String getDateFormatString() {
      return DateControl.this.sdf.toPattern();
    }


    public void setSelectableDateRange(Date min, Date max) {
      eventFiredByCalendar = true;
      setLowerLimit(min);
      setUpperLimit(max);
      eventFiredByCalendar = false;
    }


    public Date getMaxSelectableDate() {
      return getUpperLimit();
    }


    public Date getMinSelectableDate() {
      return getLowerLimit();
    }


    public void setMaxSelectableDate(Date max) {
      eventFiredByCalendar = true;
      setUpperLimit(max);
      eventFiredByCalendar = false;
    }


    public void setMinSelectableDate(Date min) {
      eventFiredByCalendar = true;
      setLowerLimit(min);
      eventFiredByCalendar = false;
    }


    public boolean isFocusable() {
      return true;
    }

    public JComponent getUiComponent() {
      return this;
    }

  } // end inner class


}
