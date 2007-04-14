package org.openswing.swing.gantt.java;

import org.openswing.swing.message.receive.java.ValueObjectImpl;
import java.math.BigDecimal;

/**
 * <p>Title: OpenSwing Framework</p>
 * <p>Description: Value object used to store an appointment, based on a starting date+hour and an end date+hour.</p>
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
public class AppointmentVO extends ValueObjectImpl implements Appointment {


  private java.sql.Timestamp startDate;
  private java.sql.Timestamp endDate;
  private String description;
  private java.awt.Color foregroundColor;
  private java.awt.Color backgroundColor;
  private BigDecimal duration;
  private boolean enableDelete;
  private boolean enableEdit;


  public AppointmentVO() {
  }


  public java.sql.Timestamp getStartDate() {
    return startDate;
  }
  public void setStartDate(java.sql.Timestamp startDate) {
    this.startDate = startDate;
  }
  public java.sql.Timestamp getEndDate() {
    return endDate;
  }
  public void setEndDate(java.sql.Timestamp endDate) {
    this.endDate = endDate;
  }
  public String getDescription() {
    return description;
  }
  public void setDescription(String description) {
    this.description = description;
  }
  public java.awt.Color getForegroundColor() {
    return foregroundColor;
  }
  public void setForegroundColor(java.awt.Color foregroundColor) {
    this.foregroundColor = foregroundColor;
  }
  public java.awt.Color getBackgroundColor() {
    return backgroundColor;
  }
  public void setBackgroundColor(java.awt.Color backgroundColor) {
    this.backgroundColor = backgroundColor;
  }
  public BigDecimal getDuration() {
    return duration;
  }
  public void setDuration(BigDecimal duration) {
    this.duration = duration;
  }
  public void setEnableEdit(boolean enableEdit) {
    this.enableEdit = enableEdit;
  }
  public boolean isEnableEdit() {
    return enableEdit;
  }
  public void setEnableDelete(boolean enableDelete) {
    this.enableDelete = enableDelete;
  }
  public boolean isEnableDelete() {
    return enableDelete;
  }
  public Object clone() {
    try {
      return super.clone();
    }
    catch (CloneNotSupportedException ex) {
      return null;
    }
  }

}
