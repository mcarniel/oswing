package org.openswing.swing.gantt.client;

import org.openswing.swing.gantt.java.Appointment;

/**
 * <p>Title: OpenSwing Framework</p>
 * <p>Description: Appointment change event: new appointment, appointment changed/deleted.</p>
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
public class AppointmentChangeEvent {

  /** new appointment event type */
  public static final int NEW_APPOINTMENT = 0;

  /** appointment changed event type */
  public static final int APPOINTMENT_CHANGED = 1;

  /** appointment deleted event type */
  public static final int APPOINTMENT_DELETED = 2;

  /** type of event; possible values: NEW_APPOINTMENT,APPOINTMENT_CHANGED,APPOINTMENT_DELETED */
  private int eventType;

  /** row number in the GanttControl */
  private int rowNumber;

  /** Appointment that fires the event (old values) */
  private Appointment oldappointment;

  /** Appointment that fires the event (new values) */
  private Appointment newappointment;

  /** GanttControl that contains the Appointment that fires the event */
  private GanttControl source;


  /**
   * Constructor.
   * @param eventType type of event; possible values: NEW_APPOINTMENT,APPOINTMENT_CHANGED,APPOINTMENT_DELETED
   * @param rowNumber row number in the GanttControl
   * @param appointment Appointment that fires the event
   * @param source GanttControl that contains the Appointment that fires the event
   */
  public AppointmentChangeEvent(int eventType,int rowNumber,Appointment appointment,GanttControl source) {
    this(eventType,rowNumber,appointment,null,source);
  }


  /**
   * Constructor.
   * @param eventType type of event; possible values: NEW_APPOINTMENT,APPOINTMENT_CHANGED,APPOINTMENT_DELETED
   * @param rowNumber row number in the GanttControl
   * @param oldappointment Appointment that fires the event (old values)
   * @param newappointment Appointment that fires the event (new values)
   * @param source GanttControl that contains the Appointment that fires the event
   */
  public AppointmentChangeEvent(int eventType,int rowNumber,Appointment oldappointment,Appointment newappointment,GanttControl source) {
    this.eventType = eventType;
    this.rowNumber = rowNumber;
    this.oldappointment = oldappointment;
    this.newappointment = newappointment;
    this.source = source;
  }


  /**
   * @return old Appointment that fires the event
   */
  public final Appointment getOldAppointment() {
    return oldappointment;
  }


  /**
   * @return new Appointment that fires the event
   */
  public final Appointment getNewAppointment() {
    return newappointment;
  }


  /**
   * @return type of event; possible values: NEW_APPOINTMENT,APPOINTMENT_CHANGED,APPOINTMENT_DELETED
   */
  public final int getEventType() {
    return eventType;
  }


  /**
   * @return row number in the GanttControl
   */
  public final int getRowNumber() {
    return rowNumber;
  }


  /**
   * @return GanttControl that contains the Appointment that fires the event
   */
  public final GanttControl getSource() {
    return source;
  }


}
