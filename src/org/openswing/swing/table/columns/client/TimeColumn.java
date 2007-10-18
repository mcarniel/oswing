package org.openswing.swing.table.columns.client;

import javax.swing.SwingConstants;
import org.openswing.swing.client.DateChangedListener;
import java.util.ArrayList;
import org.openswing.swing.util.client.ClientSettings;


/**
 * <p>Title: OpenSwing Framework</p>
 * <p>Description: Column of type time:
 * it contains a time input field.</p>
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
public class TimeColumn extends Column {

  /** possibile values: Resources.HH_MM or Resources.H_MM_AAA */
  private String timeFormat = null;

  /** date changed listeners */
  private ArrayList dateListeners = new ArrayList();


  public TimeColumn() {
    setTextAlignment(SwingConstants.CENTER);
    try {
      timeFormat = ClientSettings.getInstance().getResources().getTimeFormat();
    }
    catch (Exception ex) {
    }
  }


  /**
   * @return column type
   */
  public int getColumnType() {
    return TYPE_TIME;
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
