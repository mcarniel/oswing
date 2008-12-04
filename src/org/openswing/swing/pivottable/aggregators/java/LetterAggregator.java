package org.openswing.swing.pivottable.aggregators.java;

import java.util.Calendar;
import java.io.Serializable;
import java.util.Arrays;

/**
 * <p>Title: OpenSwing Framework</p>
 * <p>Description: Generic aggregator for row/column fields of a pivot table:
 * it decodes a String value to a String value, by grouping input values based on their prefix.
 *
 * Example: "Alan" -> "A", "Bob" -> "B", "Craig" -> "C".
 *
 * Example: if LetterAggregator is initialized as follows:
 *
 * addGroup('a','g',"A-G");
 * addGroup('h','l',"H-L");
 * addGroup('m','q',"M-Q");
 * addGroup('r','z',"R-Z");
 *
 * then returned values are: "Alan" -> "A-G", "Bob" -> "A-G", "Hillary" -> "H-L", "Sam" -> "R-Z"
 * </p>
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
public class LetterAggregator extends GenericAggregator implements Serializable {

  char[] fromChars = new char[0];
  char[] toChars = new char[0];
  String[] groupValues = new String[0];


  public LetterAggregator() {
  }


  public final void addGroup(char fromChar,char toChar,String groupValue) {
    char[] aux = new char[fromChars.length+1];
    System.arraycopy(fromChars,0,aux,0,fromChars.length);
    aux[aux.length-1] = Character.toUpperCase(fromChar);
    fromChars = aux;

    aux = new char[toChars.length+1];
    System.arraycopy(toChars,0,aux,0,toChars.length);
    aux[aux.length-1] = Character.toUpperCase(toChar);
    toChars = aux;

    String[] aux2 = new String[groupValues.length+1];
    System.arraycopy(groupValues,0,aux2,0,groupValues.length);
    aux2[aux2.length-1] = groupValue;
    groupValues = aux2;
  }


  public final Object decodeValue(Object value) {
    if (value!=null && value.toString().length()>0) {
      if (fromChars.length==0)
        return value.toString().toUpperCase().substring(0,1);

      char c = value.toString().toUpperCase().charAt(0);
      for(int i=0;i<fromChars.length;i++)
        if (c>=fromChars[i] && c<=toChars[i])
          return groupValues[i];

      return value.toString().toUpperCase().substring(0,1);
    }
    return value;
  }


  public final boolean equals(Object obj) {
    if (obj.getClass()!=LetterAggregator.class)
      return false;
    String s1 = "";
    for(int i=0;i<groupValues.length;i++)
      s1 += groupValues[i]+"_";
    String s2 = "";
    for(int i=0;i<((LetterAggregator)obj).groupValues.length;i++)
      s2 += ((LetterAggregator)obj).groupValues[i]+"_";
    return s1.equals(s2);
  }


  public final int hashCode() {
    String s1 = "";
    for(int i=0;i<groupValues.length;i++)
      s1 += groupValues[i]+"_";
    return s1.hashCode();
  }


}
