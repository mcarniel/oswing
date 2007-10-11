package org.openswing.swing.table.columns.client;

import javax.swing.SwingConstants;
import java.util.ArrayList;
import java.util.Date;
import org.openswing.swing.client.DateChangedListener;
import org.openswing.swing.internationalization.java.Resources;


/**
 * <p>Title: OpenSwing Framework</p>
 * <p>Description: Column of type date+time:
 * it contains a date input field + time input field and a button to open a calendar.</p>
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
public class DateTimeColumn extends Column {

  /** separator */
  private char separator = '/';

  /** date format; possible values:  Resources.YMD, Resources.DMY, Resources.MDY, Resources.YDM */
  private int dateFormat;

  /** date changed listeners */
  private ArrayList dateListeners = new ArrayList();

  /** maximum allowed date*/
  private Date upperLimit = null;

  /** minimum allowed date*/
  private Date lowerLimit = null;

  /** flag used to show century */
  private boolean showCentury = true;

  /** possibile values: Resources.HH_MM or Resources.H_MM_AAA */
  private String timeFormat = null;


  public DateTimeColumn() {
    setTextAlignment(SwingConstants.CENTER);
  }


  /**
   * @return column type
   */
  public int getColumnType() {
    return TYPE_DATE_TIME;
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
   * Set date format.
   * @param dateFormat; possible values:  Resources.YMD, Resources.DMY, Resources.MDY, Resources.YDM
   */
  public final void setFormat(int dateFormat) {
    if(dateFormat < 0 || dateFormat > 3)
      dateFormat = Resources.YMD;
    this.dateFormat = dateFormat;
  }


  /**
   * @return date format; possible values:  YMD, DMY, MDY, YDM
   */
  public final int getFormat() {
      return dateFormat;
  }


  /**
   * Set maximum allowed date.
   * @param upperLimit maximum allowed date
   */
  public final void setUpperLimit(Date upperLimit) {
    this.upperLimit = upperLimit;
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
  }


  /**
   * @return minimum allowed date
   */
  public final Date getLowerLimit() {
    return lowerLimit;
  }


  /**
   * Set separator.
   * @param separator separator character
   */
  public final void setSeparator(char separator) {
    if(Character.isLetterOrDigit(separator) || separator == ' ')
      return;
    this.separator = separator;
  }


  /**
   * @return separator
   */
  public final char getSeparator() {
    return separator;
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


  public final ArrayList getDateListeners() {
    return dateListeners;
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
  }


}
