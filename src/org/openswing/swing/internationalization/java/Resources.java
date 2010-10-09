package org.openswing.swing.internationalization.java;

import java.io.*;
import java.util.*;

import org.openswing.swing.util.java.*;


/**
 * <p>Title: OpenSwing Framework</p>
 * <p>Description: Class for collecting all internationalization properties:
 * translations, data/numeric/currency formats.</p>
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
public class Resources implements Serializable {

  /** dictionary containing the translations */
  private Properties dictionary;

  /** currency symbol */
  private String currencySymbol;

  /** decimal symbol */
  private char decimalSymbol;

  /** grouping symbol */
  private char groupingSymbol;

  /** time format;  possible values: HH_MM, H_MM_AAA, HH_MM_SS, H_MM_SS_AAA, HH_MM_SS, H_MM_SS_AAA */
  private String timeFormat;

  /** example 18:30 */
  public static final String HH_MM = "HH:mm";

  /** example 6:30 PM */
  public static final String H_MM_AAA = "h:mm aaa";

  /** example 18:30:59 */
  public static final String HH_MM_SS = "HH:mm:ss";

  /** example 6:30:59 PM */
  public static final String H_MM_SS_AAA = "h:mm:ss aaa";

  /** example 18:30:59,123 */
  public static final String HH_MM_SS_SSS = "HH:mm:ss,SSS";

  /** example 6:30:59,123 PM */
  public static final String H_MM_SS_SSS_AAA = "h:mm:ss,SSS aaa";

  /** language identifier */
  private String languageId;

  /** date format separator; for example: '-' or '/' */
  private char dateFormatSeparator;

  /* date format: yy[yy]MMdd */
  public static final int YMD = 0;

  /* date format: ddMMyy[yy] */
  public static final int DMY = 1;

  /* date format: MMddyy[yy] */
  public static final int MDY = 2;

  /* date format: yy[yy]ddMM */
  public static final int YDM = 3;

  /** flag used to show century in the date format */
  private boolean showCenturyInDateFormat;

  /** date format; possible values:  YMD, DMY, MDY, YDM */
  private int dateFormat;

  /** warn when no resource key not found */
  private boolean showResourceNotFoundWarning = true;


  /**
   * Costructor used to inizialize the dictionary and other settings.
   * @param dictionary dictionary containing the translations
   * @param currencySymbol currency symbol
   * @param decimalSymbol decimal symbol
   * @param groupingSymbol grouping symbol
   * @param dateFormat date format
   * @param timeFormat time format; possibile values: HH_MM, H_MM_AAA, HH_MM_SS, H_MM_SS_AAA
   * @param languageId language identifier
   * @param showResourceNotFoundWarning define if log when a resource is not found
   */
  public Resources(
      Properties dictionary,
      String currencySymbol,
      char decimalSymbol,
      char groupingSymbol,
      int dateFormat,
      boolean showCenturyInDateFormat,
      char dateFormatSeparator,
      String timeFormat,
      String languageId,
      boolean showResourceNotFoundWarning
    ) {
    if(dateFormat < 0 || dateFormat > 3)
      dateFormat = Resources.YMD;

    this.dictionary = dictionary;
    this.currencySymbol = currencySymbol;
    this.decimalSymbol = decimalSymbol;
    this.groupingSymbol = groupingSymbol;
    this.dateFormat = dateFormat;
    this.showCenturyInDateFormat = showCenturyInDateFormat;
    this.dateFormatSeparator = dateFormatSeparator;
    this.languageId = languageId;
    this.showResourceNotFoundWarning = showResourceNotFoundWarning;

    if (!timeFormat.equals(HH_MM) &&
        !timeFormat.equals(H_MM_AAA) &&
        !timeFormat.equals(HH_MM_SS) &&
        !timeFormat.equals(H_MM_SS_AAA) &&
        !timeFormat.equals(HH_MM_SS_SSS) &&
        !timeFormat.equals(H_MM_SS_SSS_AAA)) {
      System.err.println("The time format specified '"+timeFormat+"' is not allowed.\nAllowable values are: '"+HH_MM+"' or '"+H_MM_AAA+"'"+"' or '"+HH_MM_SS+"'"+"' or '"+H_MM_SS_AAA+"' or '"+HH_MM_SS_SSS+"'"+"' or '"+H_MM_SS_SSS_AAA+"'");
      timeFormat = HH_MM;
    }
    this.timeFormat = timeFormat;
  }


  /**
   * @return Enumeration of all entries in dictionary
   */
  public final Enumeration getEntries() {
    return dictionary.keys();
  }


  /**
   * @param text text to translate
   * @return translated text
   */
  public final String getResource(String text) {
    if (text==null || text.equals(""))
      return "";
    if (dictionary.containsKey(text))
      return dictionary.getProperty(text);
    if (dictionary.size()>0 && showResourceNotFoundWarning)
      // if dictionary was loaded (i.e. application must support multi-language)
      // then warn the translation missing...
      System.err.println("The resource '"+text+"' is not defined.");
    return text;
  }


  /**
   * @return grouping symbol
   */
  public final char getGroupingSymbol() {
    return groupingSymbol;
  }


  /**
   * @return currency symbol
   */
  public final String getCurrencySymbol() {
    return currencySymbol;
  }


  /**
   * @return decimal symbol
   */
  public final char getDecimalSymbol() {
    return decimalSymbol;
  }


  /**
   * @return date format; possible values:  YMD, DMY, MDY, YDM
   */
  public final int getDateFormat() {
    return dateFormat;
  }


  /**
   * @return time format
   */
  public final String getTimeFormat() {
    return timeFormat;
  }


  /**
   * @return language identifier
   */
  public final String getLanguageId() {
    return languageId;
  }


  /**
   * @return date format separator; for example: '-' or '/'
   */
  public final char getDateFormatSeparator() {
    return dateFormatSeparator;
  }


  /**
   * @return used to show century in the date format
   */
  public final boolean isShowCenturyInDateFormat() {
    return showCenturyInDateFormat;
  }


  /**
   * @param dateType possibile values: Consts.TYPE_DATE, Consts.TYPE_TIME, Consts.TYPE_DATE_TIME
   * @return date format, according to dateFormat, dateFormatSeparator and showCenturyInDateFormat properties
   */
  public final String getDateMask(int dateType) {
    return getDateMask(dateType,dateFormat,dateFormatSeparator,showCenturyInDateFormat,timeFormat);
  }


  /**
   * @return date format, according to dateType, dateFormat, separator, showCentury and timeFormat arguments
   */
  public final String getDateMask(int dateType,int dateFormat,char separator,boolean showCentury,String timeFormat) {
    String mask = null;

    switch(dateType) {
      case Consts.TYPE_DATE:
      case Consts.TYPE_DATE_TIME:

        // set date format...
        String year = "yy";
        if (showCentury)
          year += "yy";
        switch(dateFormat) {
          case Resources.YMD:
            mask = year+separator+"MM"+separator+"dd";
            break;
          case Resources.DMY:
            mask = "dd"+separator+"MM"+separator+year;
            break;
          case Resources.MDY:
            mask = "MM"+separator+"dd"+separator+year;
            break;
          case Resources.YDM:
            mask = year+separator+"dd"+separator+"MM";
            break;
        }

        if (dateType==Consts.TYPE_DATE_TIME) {
          // set time format...
          mask += " "+timeFormat;
        }

        break;

      case Consts.TYPE_TIME:
        // set time format...
        mask = timeFormat;
        break;
    }

    return mask;
  }


  /**
   * @return dictionary containing the translations
   */
  public final Properties getDictionary() {
    return dictionary;
  }




}
