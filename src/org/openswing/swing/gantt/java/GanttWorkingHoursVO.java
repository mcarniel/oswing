package org.openswing.swing.gantt.java;

import org.openswing.swing.message.receive.java.ValueObjectImpl;


/**
 * <p>Title: OpenSwing Framework</p>
 * <p>Description: Value object used to store the working hours used in a gantt row.</p>
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
public class GanttWorkingHoursVO extends ValueObjectImpl implements GanttWorkingHours {

  private java.sql.Timestamp morningStartHour;
  private java.sql.Timestamp morningEndHour;
  private java.sql.Timestamp afternoonStartHour;
  private java.sql.Timestamp afternoonEndHour;


  public GanttWorkingHoursVO() {
  }


  public java.sql.Timestamp getMorningStartHour() {
    return morningStartHour;
  }
  public void setMorningStartHour(java.sql.Timestamp morningStartHour) {
    this.morningStartHour = morningStartHour;
  }
  public java.sql.Timestamp getMorningEndHour() {
    return morningEndHour;
  }
  public void setMorningEndHour(java.sql.Timestamp morningEndHour) {
    this.morningEndHour = morningEndHour;
  }
  public java.sql.Timestamp getAfternoonStartHour() {
    return afternoonStartHour;
  }
  public void setAfternoonStartHour(java.sql.Timestamp afternoonStartHour) {
    this.afternoonStartHour = afternoonStartHour;
  }
  public java.sql.Timestamp getAfternoonEndHour() {
    return afternoonEndHour;
  }
  public void setAfternoonEndHour(java.sql.Timestamp afternoonEndHour) {
    this.afternoonEndHour = afternoonEndHour;
  }


}
