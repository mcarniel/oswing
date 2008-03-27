package org.openswing.swing.util.java;

import java.util.*;

import org.openswing.swing.message.send.java.*;

/**
 * <p>Title: OpenSwing Framework</p>
 * <p>Description: Singleton class used to compare/translate data types.</p>
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
public class Utils {


  /**
   * Compare two arrays of integers.
   * @return <code>true</code> if two arrays contain the same values, <code>false</code> otherwise
   */
  public static final boolean equals(int[] a1,int[] a2) {
    if (a1==null || a2==null)
      return false;
    if (a1.length!=a2.length)
      return false;
    for(int i=0;i<a1.length;i++)
      if (a1[i]!=a2[i])
        return false;
    return true;
  }


  /**
   * Compare two arrays of strings.
   * @return <code>true</code> if two arrays contain the same values, <code>false</code> otherwise
   */
  public static final boolean equals(String[] a1,String[] a2) {
    if (a1==null || a2==null)
      return false;
    if (a1.length!=a2.length)
      return false;
    for(int i=0;i<a1.length;i++)
      if (a1[i]==null && a2[i]!=null)
        return false;
      else if (a1[i]!=null && a2[i]==null)
        return false;
      else if (a1[i]!=null && !a1[i].equals(a2[i]))
        return false;
    return true;
  }


  /**
   * Compare two arrays of objects.
   * @return <code>true</code> if two arrays contain the same values, <code>false</code> otherwise
   */
  public static final boolean equals(Object[] a1,Object[] a2) {
    if (a1==null || a2==null)
      return false;
    if (a1.length!=a2.length)
      return false;
    for(int i=0;i<a1.length;i++)
      if (a1[i]==null && a2[i]!=null)
        return false;
      else if (a1[i]!=null && a2[i]==null)
        return false;
      else if (a1[i]!=null && !a1[i].equals(a2[i]))
        return false;
    return true;
  }


  /**
   * Compare two arrays of booleans.
   * @return <code>true</code> if two arrays contain the same values, <code>false</code> otherwise
   */
  public static final boolean equals(boolean[] a1,boolean[] a2) {
    if (a1==null || a2==null)
      return false;
    if (a1.length!=a2.length)
      return false;
    for(int i=0;i<a1.length;i++)
      if (a1[i]!=a2[i])
        return false;
    return true;
  }


  /**
   * Compare two lists of objects.
   * @return <code>true</code> if two lists contain the same values, <code>false</code> otherwise
   */
  public static final boolean equals(List a1,List a2) {
    if (a1==null || a2==null)
      return false;
    if (a1.size()!=a2.size())
      return false;
    for(int i=0;i<a1.size();i++)
      if (a1.get(i)==null && a2.get(i)!=null)
        return false;
      else if (a1.get(i)!=null && a2.get(i)==null)
        return false;
      else if (a1.get(i)!=null && !a1.get(i).equals(a2.get(i)))
        return false;
    return true;
  }


  /**
   * Compare two arrays of FilterWhereClause.
   * @return <code>true</code> if two arrays contain the same values, <code>false</code> otherwise
   */
  public static final boolean equals(FilterWhereClause[] a1,FilterWhereClause[] a2) {
    if (a1==null || a2==null)
      return false;
    if (a1.length!=a2.length)
      return false;
    for(int i=0;i<a1.length;i++)
      if (a1[i]==null && a2[i]!=null)
        return false;
      else if (a1[i]!=null && a2[i]==null)
        return false;
      else if (a1[i]!=null && !a1[i].equals(a2[i]))
        return false;
    return true;
  }


  /**
   * Compare two maps of objects: both keys and values are compared.
   * Supported values are base java types, List, Map, String[], Object[], int[], boolean[] and FilterWhereClause[].
   * @return <code>true</code> if two lists contain the same values, <code>false</code> otherwise
   */
  public static final boolean equals(Map a1,Map a2) {
    if (a1==null || a2==null)
      return false;
    if (a1.size()!=a2.size())
      return false;

    Iterator it = a1.keySet().iterator();
    Object key = null;
    Object value1 = null;
    Object value2 = null;
    while(it.hasNext()) {
      key = it.next();
      value1 = a1.get(key);
      value2 = a2.get(key);
      if (value2==null)
        return false;
      if (!value1.equals(value2)) {
        if (value1 instanceof String[] &&
            value2 instanceof String[])
          return equals((String[])value1,(String[])value2);
        else if (value1 instanceof Object[] &&
            value2 instanceof Object[])
          return equals((Object[])value1,(Object[])value2);
        else if (value1 instanceof int[] &&
            value2 instanceof int[])
          return equals((int[])value1,(int[])value2);
        else if (value1 instanceof boolean[] &&
            value2 instanceof boolean[])
          return equals((boolean[])value1,(boolean[])value2);
        else if (value1 instanceof List &&
            value2 instanceof List)
          return equals((List)value1,(List)value2);
        else if (value1 instanceof Map &&
            value2 instanceof Map)
          return equals((Map)value1,(Map)value2);
        else if (value1 instanceof FilterWhereClause[] &&
            value2 instanceof FilterWhereClause[])
          return equals((FilterWhereClause[])value1,(FilterWhereClause[])value2);
        return false;
      }
    }

    return true;
  }



}
