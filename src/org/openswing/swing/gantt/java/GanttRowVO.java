package org.openswing.swing.gantt.java;

import org.openswing.swing.message.receive.java.ValueObjectImpl;
import java.util.HashSet;


/**
 * <p>Title: OpenSwing Framework</p>
 * <p>Description: Value object used to store a gantt row info.</p>
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
public class GanttRowVO extends ValueObjectImpl {

  private GanttWorkingHours sundayWorkingHours;
  private GanttWorkingHours mondayWorkingHours;
  private GanttWorkingHours tuesdayWorkingHours;
  private GanttWorkingHours wednesdayWorkingHours;
  private GanttWorkingHours thursdayWorkingHours;
  private GanttWorkingHours fridayWorkingHours;
  private GanttWorkingHours saturdayWorkingHours;

  /** list of Appointment objects */
  private HashSet appointments = new HashSet();

  /** values to show on the grid on the left */
  private Object[] legend;

  /** Appointment object class, used to create it by reflection */
  private Class appointmentClass;


  public GanttRowVO() {
  }


  public GanttWorkingHours getSundayWorkingHours() {
    return sundayWorkingHours;
  }
  public void setSundayWorkingHours(GanttWorkingHours sundayWorkingHours) {
    this.sundayWorkingHours = sundayWorkingHours;
  }
  public GanttWorkingHours getMondayWorkingHours() {
    return mondayWorkingHours;
  }
  public void setMondayWorkingHours(GanttWorkingHours mondayWorkingHours) {
    this.mondayWorkingHours = mondayWorkingHours;
  }
  public GanttWorkingHours getTuesdayWorkingHours() {
    return tuesdayWorkingHours;
  }
  public void setTuesdayWorkingHours(GanttWorkingHours tuesdayWorkingHours) {
    this.tuesdayWorkingHours = tuesdayWorkingHours;
  }
  public GanttWorkingHours getWednesdayWorkingHours() {
    return wednesdayWorkingHours;
  }
  public void setWednesdayWorkingHours(GanttWorkingHours wednesdayWorkingHours) {
    this.wednesdayWorkingHours = wednesdayWorkingHours;
  }
  public GanttWorkingHours getThursdayWorkingHours() {
    return thursdayWorkingHours;
  }
  public void setThursdayWorkingHours(GanttWorkingHours thursdayWorkingHours) {
    this.thursdayWorkingHours = thursdayWorkingHours;
  }
  public GanttWorkingHours getFridayWorkingHours() {
    return fridayWorkingHours;
  }
  public void setFridayWorkingHours(GanttWorkingHours fridayWorkingHours) {
    this.fridayWorkingHours = fridayWorkingHours;
  }
  public GanttWorkingHours getSaturdayWorkingHours() {
    return saturdayWorkingHours;
  }
  public void setSaturdayWorkingHours(GanttWorkingHours saturdayWorkingHours) {
    this.saturdayWorkingHours = saturdayWorkingHours;
  }
  public HashSet getAppointments() {
    return appointments;
  }
  public void setAppointments(HashSet appointments) {
    this.appointments = appointments;
  }
  public Object[] getLegend() {
    return legend;
  }
  public void setLegend(Object[] legend) {
    this.legend = legend;
  }
  public Class getAppointmentClass() {
    return appointmentClass;
  }
  public void setAppointmentClass(Class appointmentClass) {
    this.appointmentClass = appointmentClass;
  }




}
