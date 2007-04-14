package org.openswing.swing.gantt.java;

import java.math.BigDecimal;

/**
 * <p>Title: OpenSwing Framework</p>
 * <p>Description: Interface used to define an appointment, based on a starting date+hour and an end date+hour.</p>
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
public interface Appointment extends Cloneable {


  public java.sql.Timestamp getStartDate();
  public void setStartDate(java.sql.Timestamp startDate);
  public java.sql.Timestamp getEndDate();
  public void setEndDate(java.sql.Timestamp endDate);
  public String getDescription();
  public void setDescription(String description);
  public java.awt.Color getForegroundColor();
  public void setForegroundColor(java.awt.Color foregroundColor);
  public java.awt.Color getBackgroundColor();
  public void setBackgroundColor(java.awt.Color backgroundColor);
  public void setDuration(BigDecimal duration);
  public BigDecimal getDuration();
  public void setEnableEdit(boolean enableEdit);
  public boolean isEnableEdit();
  public void setEnableDelete(boolean enableDelete);
  public boolean isEnableDelete();
  public Object clone();


}
