package org.openswing.swing.pivottable.aggregators.java;

import java.util.Calendar;
import java.io.Serializable;

/**
 * <p>Title: OpenSwing Framework</p>
 * <p>Description: Generic aggregator for row/column fields of a pivot table:
 * it decodes a java.util.Date or java.sql.Date or java.sql.Timestamp to a Double value, related to the specified half year (1 or 2) in input value.</p>
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
public class HalfYearAggregator extends GenericAggregator implements Serializable {

  private Calendar cal = Calendar.getInstance();


  public HalfYearAggregator() {
  }


  public Object decodeValue(Object value) {
    if (value!=null && value instanceof java.util.Date) {
      cal.setTimeInMillis( ((java.util.Date)value).getTime() );
      int month = cal.get(cal.MONTH);
      if (month<=6)
        return new Double(1);
      else
        return new Double(2);
    }
    return value;
  }


  public final boolean equals(Object obj) {
    return obj.getClass()==HalfYearAggregator.class;
  }


  public final int hashCode() {
    return HalfYearAggregator.class.hashCode();
  }

}
